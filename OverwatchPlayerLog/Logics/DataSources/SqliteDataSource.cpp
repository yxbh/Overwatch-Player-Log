#include "SqliteDataSource.hpp"
#include <QDebug>
#include <QSqlQuery>
#include <QSqlError>
#include "App.hpp"
#include "Logics/Config.hpp"

namespace
{
    unsigned getCurrentDatabaseVersion(QSqlDatabase & database)
    {
        QSqlQuery query(database);
        if (!query.exec("select count(*) from sqlite_master where type = 'table' and name = 'MetaInfo'"))
        {
            qDebug() << "Failure inspecting sqlite_master table for MetaInfo table info. Error: "
                     << query.lastError().text();
            return 0u;
        }

        query.next();
        if (0u == query.value(0).toUInt())
            return 0u;

        if (!query.exec("select Value from MetaInfo where Key = 'Version'"))
        {
            qDebug() << "Failure getting version value from MetaInfo table. Error: "
                     << query.lastError().text();
            return 0u;
        }
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
            if (!query.exec())
            {
                qDebug() << "Failure inserting version value into MetaInfo table. Error: "
                         << query.lastError().text();
                return false;
            }
            return true;
        }
        else
        {
            query.prepare("update MetaInfo set Value = (:version) where Key = 'Version'");
            query.bindValue(":version", version);
            if (!query.exec())
            {
                qDebug() << "Failure updating version value in the MetaInfo table. Error: "
                         << query.lastError().text();
                return false;
            }
            return true;
        }
    }

    bool updateTables(QSqlDatabase & database, unsigned version);
    bool updateTablesV0ToV1(QSqlDatabase & database);
}

SqliteDataSource::SqliteDataSource(void)
{

}


bool SqliteDataSource::connect(void)
{
    qDebug() << "connecting data source.";
    this->database = QSqlDatabase::addDatabase("QSQLITE");
    this->database.setDatabaseName(Config::getDatabasePath());
    if (!this->database.open())
    {
        qDebug() << "Failure opening database. error: " + this->database.lastError().text();
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

QStringList SqliteDataSource::getAllPlayerNames(void)
{
    QSqlQuery query(this->database);
    if (!query.exec("select * from Players"))
    {
        qDebug() << "Failure getting all player names. error: " << query.lastError().text();
        return {};
    }
    QStringList list;
    while (query.next())
        list.append(query.value(0).toString());
    return list;
}

void SqliteDataSource::savePlayer(const OwPlayer & player)
{

}

namespace
{
    bool updateTables(QSqlDatabase & database, unsigned version)
    {
        switch (version)
        {
        case 0:
            if (!updateTablesV0ToV1(database)) return false;
        break;

        default:
            return true;
        }
        return true;
    }

    bool updateTablesV0ToV1(QSqlDatabase & database)
    {
        qDebug() << "Upgrading tables from V0 to V1";
        QSqlQuery query(database);
        if (!query.exec("create table MetaInfo(Key text unique, Value text)"))
        {
            qDebug() << "Error creating MetaInfo table. error: " << query.lastError().text();
            return false;
        }

        if (!query.exec("create table Players (id text unique, BattleTag text unique)"))
        {
            qDebug() << "Error creating Players table. error: " << query.lastError().text();
            return false;
        }

        if (!::setCurrentDatabaseVersion(database, 1u))
            return false;
        return true;
    }
}
