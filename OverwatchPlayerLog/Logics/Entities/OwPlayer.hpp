#ifndef OWPLAYERENTITY_H
#define OWPLAYERENTITY_H
#include <QString>
#include "IEntity.hpp"

class OwPlayer :
        public IEntity
{
private:
    QString battleTag;

public:

private:
    OwPlayer();
};

#endif // OWPLAYERENTITY_H
