#ifndef FAVORITEOWPLAYERSORTFILTERPROXYMODEL_H
#define FAVORITEOWPLAYERSORTFILTERPROXYMODEL_H
#include <QSortFilterProxyModel>

class FavoriteOwPlayerSortFilterProxyModel : public QSortFilterProxyModel
{
public:
    using QSortFilterProxyModel::QSortFilterProxyModel;

    virtual bool filterAcceptsRow(int sourceRow, const QModelIndex &sourceParent) const final;
};

#endif // FAVORITEOWPLAYERSORTFILTERPROXYMODEL_H
