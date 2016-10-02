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

    virtual void savePlayer(const OwPlayer & player) override;
};

#endif // SQLITEDATASOURCE_H
