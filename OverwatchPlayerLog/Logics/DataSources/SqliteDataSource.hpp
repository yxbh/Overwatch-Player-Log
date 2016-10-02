#ifndef SQLITEDATASOURCE_H
#define SQLITEDATASOURCE_H
#include <QSqlDatabase>
#include "IDataSource.hpp"

class SqliteDataSource :
        public IDataSource
{
private:
    QSqlDatabase database;

public:
    SqliteDataSource(void);

    virtual bool connect(void) override;
    virtual bool initialiseTables(void) override;

    virtual QStringList getAllPlayerNames(void) override;

    virtual QUuid getIdByBattleTag(const QString & btag) override;

    virtual bool validatePlayer(const OwPlayer & player) override;
    virtual bool savePlayer(const OwPlayer & player) override;

private:
    bool hasPlayerId(QUuid id);
    bool hasPlayerBattleTag(QString battleTag);
};

#endif // SQLITEDATASOURCE_H
