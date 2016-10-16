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
    QString platform;
    QString region;

public:
    OwPlayer();

    inline bool isNew(void) const { return this->isNewpPlayer; }
    inline const QUuid & getId(void) const { return this->id; }
    inline const QString & getBattleTag(void) const { return this->battleTag; }
    inline const QString & getPlatform(void) const { return this->platform; }
    inline const QString & getRegion(void) const { return this->region; }

    inline OwPlayer & setBattleTag(const QString & btag) { this->battleTag = btag; return *this; }
    inline OwPlayer & setPlatform(const QString & platform) { this->platform = platform; return *this; }
    inline OwPlayer & setRegion(const QString & region) { this->region = region; return *this; }

    virtual bool validate(void);
    virtual bool save(void);
    virtual bool remove(void);

    static OwPlayer fromBattleTag(const QString & battleTag);

private:
    static OwPlayer instantiateExsitingPlayer(QUuid id);


    friend class SqliteDataSource;
};

#endif // OWPLAYERENTITY_H
