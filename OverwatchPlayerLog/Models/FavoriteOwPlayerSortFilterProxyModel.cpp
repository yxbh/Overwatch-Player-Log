#include "FavoriteOwPlayerSortFilterProxyModel.hpp"
#include "Models/OwPlayerItem.hpp"

bool FavoriteOwPlayerSortFilterProxyModel::filterAcceptsRow(int sourceRow, const QModelIndex &sourceParent) const
{
    const QModelIndex index0 = this->sourceModel()->index(sourceRow, 0, sourceParent);
    auto data = this->sourceModel()->data(index0, OwPlayerItem::OwPlayerType);
    auto playerItem = data.value<const OwPlayerItem *>();

    if (playerItem->getPlayer().isFavorite())
    {
        return this->QSortFilterProxyModel::filterAcceptsRow(sourceRow, sourceParent);
    }
    return false;
}
