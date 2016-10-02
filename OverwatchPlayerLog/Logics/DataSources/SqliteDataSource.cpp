#include "SqliteDataSource.hpp"
#include <QDebug>
#include <QSql>
#include <QSqlQuery>
#include <QSqlError>
#include "App.hpp"
#include "Logics/Config.hpp"

namespace
{
    unsigned getCurrentDatabaseVersion(QSqlDatabase & database)
    {
        QSqlQuery query("select count(*) from sqlite_master where type = 'table' and name = 'MetaInfo'", database);
        query.next();
        if (0u == query.value(0).toUInt())
            return 0u;
        query.exec("select Value from MetaInfo where Key = 'Version'");
        query.next();
        return query.value(0).toUInt();
    }
    bool setCurrentDatabaseVersion(QSqlDatabase & database, unsigned version)
    {
        QSqlQuery query(database);
        if (!query.exec("select count(*) from MetaInfo where Key = 'Version'"))
            return false;
        query.next();
        if (query.value(0).toUInt() == 0u)
        {
            query.prepare("insert into MetaInfo values('Version', (:version))");
            query.bindValue(":version", version);
            return query.exec();
        }
        else
        {
            query.prepare("update MetaInfo set Value = (:version) where Key = 'Version'");
            query.bindValue(":version", version);
            return query.exec();
        }
    }

    bool updateTables(QSqlDatabase & database, unsigned version);
    bool updateTablesV0ToV1(QSqlDatabase & database);
}

SqliteDataSource::SqliteDataSource()
{

}


bool SqliteDataSource::connect(void)
{
    qDebug() << "connecting data source.";
    this->database = QSqlDatabase::addDatabase("QSQLITE");
    this->database.setDatabaseName(Config::getDatabasePath());
    if (!this->database.open())
    {
        qDebug() << "failure opening database. error: " + this->database.lastError().text();
        return false;
    }
    return true;
}

bool SqliteDataSource::initialiseTables(void)
{
    qDebug() << "Initialising database tables.";
    while (App::getLatestDataSourceVersion() != ::getCurrentDatabaseVersion(this->database))
    {
        if (!::updateTables(this->database, ::getCurrentDatabaseVersion(this->database)))
        {
            return false;
        }
    }

    return true;
}

void SqliteDataSource::savePlayer(const OwPlayer & player)
{

}

namespace
{
    bool updateTables(QSqlDatabase & database, unsigned version)
    {
        for (;;)
        {
            switch (version)
            {
            case 0:
                if (!updateTablesV0ToV1(database)) return false;
            break;

            default:
                return true;
            }
        }
    }

    bool updateTablesV0ToV1(QSqlDatabase & database)
    {
        QSqlQuery query(database);
        if (!query.exec("create table MetaInfo(Key int, Value int)"))
        {
            qDebug() << query.lastError().text();
            return false;
        }

        if (!query.exec("create table Players (BattleTag text)"))
        {
            qDebug() << query.lastError().text();
            return false;
        }

        if (!::setCurrentDatabaseVersion(database, 1u))
            return false;
        return true;
    }
}
