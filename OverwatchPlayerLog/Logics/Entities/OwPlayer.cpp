#include "OwPlayer.hpp"
#include "App.hpp"

OwPlayer::OwPlayer(void) :
    id(QUuid::createUuid()),
    region("us")
{

}

bool OwPlayer::validate(void)
{
    return App::getInstance()->getDataSource()->validatePlayer(*this);
}

bool OwPlayer::save(void)
{
    return App::getInstance()->getDataSource()->savePlayer(*this);
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
