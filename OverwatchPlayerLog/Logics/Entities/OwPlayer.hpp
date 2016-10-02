#ifndef OWPLAYERENTITY_H
#define OWPLAYERENTITY_H
#include <QString>
#include <QUuid>
#include "IEntity.hpp"

class OwPlayer :
        public IEntity
{
private:
    bool isNewpPlayer = true;
    QUuid id;

    QString battleTag;
    QString region;

public:
    OwPlayer();

    inline bool isNew(void) const { return this->isNewpPlayer; }
    inline const QUuid & getId(void) const { return this->id; }
    inline const QString & getBattleTag(void) const { return this->battleTag; }
    inline const QString & getRegion(void) const { return this->region; }

    inline OwPlayer & setBattleTag(const QString & btag) { this->battleTag = btag; return *this; }
    inline OwPlayer & setRegion(const QString & region) { this->region = region; return *this; }

    virtual bool validate(void) final;
    virtual bool save(void) final;

    static OwPlayer fromBattleTag(const QString & battleTag);
};

#endif // OWPLAYERENTITY_H
