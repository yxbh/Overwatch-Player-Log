#ifndef IDATASOURCE_H
#define IDATASOURCE_H
#include "Logics/Entities/OwPlayer.hpp"

class IDataSource
{
public:
    virtual ~IDataSource(void) = 0;

    virtual bool connect(void) = 0;

    ///
    /// \brief Update the player record if it already exists. If not, create it.
    /// \param player
    ///
    virtual void savePlayer(OwPlayer & player) = 0;
};

inline IDataSource::~IDataSource(void) {}

#endif // IDATASOURCE_H
