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

QString SqliteDataSource::getRegionByPlayerId(const QUuid & id)
{
    QSqlQuery query(this->database);
    if (!query.prepare("select Region from Players where Id = (:id)"))
        throw Exception("Failure getting player region. SQL error: " + query.lastError().text());
    query.bindValue(":id", id);
    if (!query.exec() || !query.next())
        throw Exception("Failure getting player region. SQL error: " + query.lastError().text());

    return query.value(0).toString();
}

bool SqliteDataSource::validatePlayer(const OwPlayer & player)
{
    QSqlQuery query(this->database);

    if (player.getBattleTag().isEmpty())
    {
        qDebug() << "Player battle tag is empty.";
        return false;
    }

    if (player.getPlatform().isEmpty())
    {
        qDebug() << "Player platform is empty.";
        return false;
    }

    if (player.getRegion().isEmpty())
    {
        qDebug() << "Player region is empty.";
        return false;
    }

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
                query.prepare("insert into Players values((:id), (:battletag), (:platform), (:region), (:isfavorite), (:rating), (:note))") :
                query.prepare("update Players set BattleTag = (:battletag), Platform = (:platform), Region = (:region), IsFavorite = (:isfavorite), Rating = (:rating), Note = (:note)  where id = (:id)");
    query.bindValue(":id", player.getId());
    query.bindValue(":battletag", player.getBattleTag());
    query.bindValue(":platform", player.getPlatform());
    query.bindValue(":region", player.getRegion());
    query.bindValue(":isfavorite", player.isFavorite());
    query.bindValue(":rating", player.getRating());
    query.bindValue(":note", player.getNote());

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

bool SqliteDataSource::removePlayer(const OwPlayer & player)
{
    QSqlQuery query(this->database);
    bool success = query.prepare("delete from Players where id = (:id)");
    query.bindValue(":id", player.getId());
    if (!success || !query.exec())
    {
        qDebug() << "Failure removing player with id[" + player.getId().toString() + "]. "
                 << "SQL error: " << query.lastError().text();
        return false;
    }
    return true;
}

OwPlayer SqliteDataSource::getPlayer(const QUuid & id)
{
    QSqlQuery query(this->database);
    bool success = query.prepare("select id, BattleTag, Platform, Region, IsFavorite, Rating, Note from Players");
    query.bindValue(":id",  id);
    if (!success || !query.exec())
    {
        throw Exception("Failure getting all players. SQL error: " + query.lastError().text());
    }

    if (query.next())
    {
        OwPlayer player = OwPlayer::instantiateExsitingPlayer(id);
        player.setBattleTag(query.value(1).toString());
        player.setPlatform(query.value(2).toString());
        player.setRegion(query.value(3).toString());
        player.setFavorite(query.value(4).toBool());
        player.setRating(static_cast<OwPlayer::Rating>(query.value(5).toInt()));
        player.setNote(query.value(6).toString());

        return player;
    }

    throw Exception("Failure player with id[" + id.toString() + "].");
}

QVector<OwPlayer> SqliteDataSource::getAllPlayers(void)
{
    QSqlQuery query(this->database);
    bool success = query.prepare("select id, BattleTag, Platform, Region, IsFavorite, Rating, Note from Players");
    if (!success || !query.exec())
    {
        throw Exception("Failure getting all players. SQL error: " + query.lastError().text());
    }

    QVector<OwPlayer> players;
    while (query.next())
    {
        OwPlayer player = OwPlayer::instantiateExsitingPlayer(query.value(0).toUuid());
        player.setBattleTag(query.value(1).toString());
        player.setPlatform(query.value(2).toString());
        player.setRegion(query.value(3).toString());
        player.setFavorite(query.value(4).toBool());
        player.setRating(static_cast<OwPlayer::Rating>(query.value(5).toInt()));
        player.setNote(query.value(6).toString());
        players.append(player);
    }
    return players;
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

        if (!query.exec("create table Players (id text unique, BattleTag text unique, Platform text, Region text, IsFavorite boolean, Rating smallint,  Note text)"))
        {
            qDebug() << "Error creating Players table. error: " << query.lastError().text();
            return false;
        }

        if (!::setCurrentDatabaseVersion(database, 1u))
            return false;
        return true;
    }
}
