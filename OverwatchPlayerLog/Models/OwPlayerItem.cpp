#include "OwPlayerItem.hpp"
#include <QDebug>

QVariant OwPlayerItem::data(int role) const
{
//    qDebug() << "function called: " << "QVariant OwPlayerItem::data(int role) const";
    switch (role)
    {
    case Qt::DisplayRole:
    case Qt::EditRole:
//        qDebug() << "returning battle tag: " << this->player.getBattleTag();
        return this->player.getBattleTag();
    default:
        return QStandardItem::data(role);
    }
}
