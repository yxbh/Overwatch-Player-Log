#ifndef OWPLAYERENTITY_H
#define OWPLAYERENTITY_H
#include <QDateTime>
#include <QString>
#include <QUuid>
#include "IEntity.hpp"

class OwPlayer :
        public IEntity
{
public:
    enum Rating : int8_t
    {
        Dislike = 0,
        Undecided = 1,
        Like = 2
    };

private:
    bool isNewpPlayer = true;
    QUuid id;

    bool isFavoritePlayer = false;
    QString battleTag;
    QString platform;
    QString region;
    Rating rating;
    QString note;

    QDateTime creationDateTime;
    QDateTime lastUpdateDateTime;

public:
    OwPlayer();

    inline bool isNew(void) const { return this->isNewpPlayer; }
    inline const QUuid & getId(void) const { return this->id; }
    inline const QString & getBattleTag(void) const { return this->battleTag; }
    inline const QString & getPlatform(void) const { return this->platform; }
    inline const QString & getRegion(void) const { return this->region; }
    inline Rating getRating(void) const { return this->rating; }
    inline const QString & getNote(void) const { return this->note; }
    inline const QDateTime & getCreationDateTime(void) const { return this->creationDateTime; }
    inline const QDateTime & getLastUpdateDateTime(void) const { return this->lastUpdateDateTime; }
    inline bool isFavorite(void) const { return this->isFavoritePlayer; }

    inline OwPlayer & setBattleTag(const QString & btag) { this->battleTag = btag; return *this; }
    inline OwPlayer & setPlatform(const QString & platform) { this->platform = platform; return *this; }
    inline OwPlayer & setRegion(const QString & region) { this->region = region; return *this; }
    inline OwPlayer & setRating(Rating rating) { this->rating = rating; return *this; }
    inline OwPlayer & setNote(const QString & note) { this->note = note; return *this; }
    inline OwPlayer & setCreationDateTime(const QDateTime & dateTime) { this->creationDateTime = dateTime; return *this; }
    inline OwPlayer & setLastUpdateDateTime(const QDateTime & dateTime) { this->lastUpdateDateTime = dateTime; return *this; }
    inline OwPlayer & setFavorite(bool isFavorite) { this->isFavoritePlayer = isFavorite; return *this; }

    virtual bool validate(void);
    virtual bool save(void);
    virtual bool remove(void);

    static OwPlayer fromBattleTag(const QString & battleTag);

private:
    static OwPlayer instantiateExsitingPlayer(QUuid id);


    friend class SqliteDataSource;
};

#endif // OWPLAYERENTITY_H
