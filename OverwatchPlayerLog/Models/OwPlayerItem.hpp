#ifndef OWPLAYERITEM_H
#define OWPLAYERITEM_H
#include <QStandardItem>
#include "Logics/Entities/OwPlayer.hpp"

class OwPlayerItem : public QStandardItem
{
public:
    static constexpr int OwPlayerType = QStandardItem::UserType + 1;
    static constexpr int OwPlayerIsFavoriteFlag = OwPlayerType + 1;

private:
    OwPlayer player;

public:
    using QStandardItem::QStandardItem;
    OwPlayerItem(const OwPlayer & player) : player(player) {}

    inline OwPlayer & getPlayer(void) { return this->player; }
    inline const OwPlayer & getPlayer(void) const { return this->player; }

    virtual int type(void) const final { return OwPlayerType; }
    virtual OwPlayerItem * clone(void) const final { return new OwPlayerItem(this->player); }
    virtual QVariant data(int role = OwPlayerIsFavoriteFlag) const final;
};

Q_DECLARE_METATYPE(OwPlayerItem*)
Q_DECLARE_METATYPE(const OwPlayerItem*)

#endif // OWPLAYERITEM_H
