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

    virtual bool connect(void) final;
    virtual bool initialiseTables(void) final;

    virtual QStringList getAllPlayerNames(void) final;

    virtual QUuid getIdByBattleTag(const QString & btag) final;
    virtual QString getRegionByPlayerId(const QUuid & id) final;

    virtual bool validatePlayer(const OwPlayer & player) final;
    virtual bool savePlayer(const OwPlayer & player) final;

private:
    bool hasPlayerId(QUuid id);
    bool hasPlayerBattleTag(QString battleTag);
};

#endif // SQLITEDATASOURCE_H
