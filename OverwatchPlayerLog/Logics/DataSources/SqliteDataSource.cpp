#include "SqliteDataSource.hpp"
#include <QDebug>
#include <QSqlQuery>
#include <QSqlError>
#include "App.hpp"
#include "Logics/Config.hpp"
#include "Logics/Exceptions/Exception.hpp"

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
        {
            qDebug() << "Failure inspecting MetaINfo table for version key. Error: " << query.lastError().text();
            return false;
        }

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
    if (!query.exec("select BattleTag from Players"))
        throw Exception("Failure getting all player names. SQL error: " + query.lastError().text());

    QStringList list;
    while (query.next())
        list.append(query.value(0).toString());
    return list;
}

QUuid SqliteDataSource::getIdByBattleTag(const QString & btag)
{

    QSqlQuery query(this->database);
    if (!query.prepare("select Id from Players where BattleTag = (:battletag)"))
        throw Exception("Failure getting player id. SQL error: " + query.lastError().text());
    query.bindValue(":battletag", btag);
    if (!query.exec() || !query.next())
        throw Exception("Failure getting player id. SQL error: " + query.lastError().text());

    return query.value(0).toUuid();
}

bool SqliteDataSource::validatePlayer(const OwPlayer & player)
{
    QSqlQuery query(this->database);

    if (player.getBattleTag().isEmpty())
        return false;

    if (player.isNew())
    {
        if (this->hasPlayerId(player.getId()))
        {
            qDebug() << "Player is new but its id exists in the data source.";
            return false;
        }

        // check if player name already exists.
        if (this->hasPlayerBattleTag(player.getBattleTag()))
        {
            qDebug() << "Player is new but its battle tag already exists in the data source.";
            return false;
        }
    }

    return true;
}

bool SqliteDataSource::savePlayer(const OwPlayer & player)
{
    QSqlQuery query(this->database);
    bool success = player.isNew() ?
                query.prepare("insert into Players values((:id), (:battletag))") :
                query.prepare("update Players set battletag = (:battletag) where id = (:id)");
    query.bindValue(":id", player.getId());
    query.bindValue(":battletag", player.getBattleTag());

    if (!success || !query.exec())
    {
        qDebug() << "Failure saving player with id[" + player.getId().toString() +
                    "], battletag[" + player.getBattleTag() + "]. " +
                    "SQL error: " + query.lastError().text();
//        throw Exception("Failure saving player with id[" + player.getId().toString() +
//                        "], battletag[" + player.getBattleTag() + "]. " +
//                        "SQL error: " + query.lastError().text());
        return false;
    }

    return true;
}

bool SqliteDataSource::hasPlayerId(QUuid id)
{
    QSqlQuery query(this->database);
    bool success = query.prepare("select count(*) from Players where Id = (:id)");
    query.bindValue(":id", id.toString());
    if (!success || !query.exec() || !query.next())
    {
        throw Exception("Failure searching for player with id[" + id.toString() + "]. "
                        + "SQL error: " + query.lastError().text());
    }

    return query.value(0).toUInt() != 0u;
}

bool SqliteDataSource::hasPlayerBattleTag(QString battleTag)
{
    QSqlQuery query(this->database);
    query.prepare("select count(*) from Players where BattleTag = (:battletag)");
    query.bindValue(":battletag", battleTag);
    if (!query.exec() || !query.next())
    {
        throw Exception("Failure searching for player with battletag[" + battleTag + "]. " +
                        "SQL error: " + query.lastError().text());
    }

    return query.value(0).toUInt() != 0u;
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
