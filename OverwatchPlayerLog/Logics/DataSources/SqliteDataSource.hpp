#ifndef SQLITEDATASOURCE_H
#define SQLITEDATASOURCE_H
#include "IDataSource.hpp"

class SqliteDataSource :
        public IDataSource
{
public:
    SqliteDataSource(void);

    virtual bool connect(void) override;
    virtual void savePlayer(OwPlayer & player) override;
};

#endif // SQLITEDATASOURCE_H
