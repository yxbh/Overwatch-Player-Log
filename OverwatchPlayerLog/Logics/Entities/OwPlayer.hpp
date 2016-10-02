#ifndef OWPLAYERENTITY_H
#define OWPLAYERENTITY_H
#include "IEntity.hpp"
#include "OwPlayerName.hpp"

class OwPlayer :
        public IEntity
{
private:
    OwPlayerName owPlayerName;

public:

private:
    OwPlayer();
};

#endif // OWPLAYERENTITY_H
