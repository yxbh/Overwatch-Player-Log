#include "OwPlayer.hpp"
#include "App.hpp"

OwPlayer::OwPlayer(void) :
    id(QUuid::createUuid())
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
    OwPlayer player;
    player.isNewpPlayer = false;
    player.id = App::getInstance()->getDataSource()->getIdByBattleTag(battleTag);
    player.battleTag = battleTag;
    return player;
}
