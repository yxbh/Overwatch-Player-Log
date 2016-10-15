#ifndef IDATASOURCE_H
#define IDATASOURCE_H
#include <QStringList>
#include <QVector>
#include "Logics/Entities/OwPlayer.hpp"

class IDataSource
{
public:
    virtual ~IDataSource(void) = 0;

    ///
    /// \brief Connect to the data source.
    /// \return true if successful.
    ///
    virtual bool connect(void) = 0;

    ///
    /// \brief Initialise the inner database's tables. Create and/or update them if necessary
    /// (e.g. new tables/columns in a new version of the application).
    /// \return true if successful.
    ///
    virtual bool initialiseTables(void) = 0;

    virtual QStringList getAllPlayerNames(void) = 0;

    virtual QUuid getIdByBattleTag(const QString & btag) = 0;
    virtual QString getRegionByPlayerId(const QUuid & id) = 0;

    virtual bool validatePlayer(const OwPlayer & player) = 0;
    ///
    /// \brief Update the player record if it already exists. If not, create it.
    /// \param player
    ///
    virtual bool savePlayer(const OwPlayer & player) = 0;

    virtual bool removePlayer(const OwPlayer & player) = 0;

    virtual QVector<OwPlayer> getAllPlayers(void) = 0;
};

inline IDataSource::~IDataSource(void) {}

#endif // IDATASOURCE_H
