#ifndef OWPLAYERNAME_H
#define OWPLAYERNAME_H
#include <QString>
#include "IEntity.hpp"

class OwPlayerName :
        public IEntity
{
private:
    QString battleTag;
    QString battleTagId;

public:
    OwPlayerName(QString battleTag, QString battleTagId) : battleTag(battleTag), battleTagId(battleTagId) {}

    inline const QString & getBattleTag(void) { return this->battleTag; }
    inline const QString & getBattleTagId(void) { return this->battleTagId; }
};

#endif // OWPLAYERNAME_H
