#ifndef IENTITY_H
#define IENTITY_H


class IEntity
{
public:
    virtual ~IEntity(void) = 0;

    virtual bool validate(void) = 0;
    virtual bool save(void) = 0;
};

inline IEntity::~IEntity(void) {}

#endif // IENTITY_H
