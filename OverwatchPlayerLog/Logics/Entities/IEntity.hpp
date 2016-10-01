#ifndef IENTITY_H
#define IENTITY_H


class IEntity
{
public:
    IEntity();
    virtual ~IEntity(void) = 0;
};

inline IEntity::~IEntity(void) {}

#endif // IENTITY_H
