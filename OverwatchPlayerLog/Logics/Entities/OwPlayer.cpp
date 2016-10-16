#include "OwPlayer.hpp"
#include "App.hpp"

OwPlayer::OwPlayer() :
    id(QUuid::createUuid()),
    platform("pc"),
    region("us")
{

}

bool OwPlayer::validate(void)
{
    return App::getInstance()->getDataSource()->validatePlayer(*this);
}

bool OwPlayer::save(void)
{
    if (App::getInstance()->getDataSource()->savePlayer(*this))
    {
        this->isNewpPlayer = false;
        return true;
    }
    return false;
}

bool OwPlayer::remove(void)
{
    return App::getInstance()->getDataSource()->removePlayer(*this);
}

OwPlayer OwPlayer::fromBattleTag(const QString & battleTag)
{
    auto dataSource = App::getInstance()->getDataSource();
    OwPlayer player;
    player.isNewpPlayer = false;
    player.id = dataSource->getIdByBattleTag(battleTag);
    player.battleTag = battleTag;
    player.region = dataSource->getRegionByPlayerId(player.id);
    return player;
}

OwPlayer OwPlayer::instantiateExsitingPlayer(QUuid id)
{
    OwPlayer player;
    player.id = id;
    player.isNewpPlayer = false;

    return player;
}
