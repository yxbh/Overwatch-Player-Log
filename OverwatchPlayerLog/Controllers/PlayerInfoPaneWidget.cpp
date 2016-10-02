#include "PlayerInfoPaneWidget.hpp"
#include "ui_PlayerInfoPaneWidget.h"
#include "App.hpp"

PlayerInfoPaneWidget::PlayerInfoPaneWidget(QWidget *parent, QString playerBattleTag) :
    QScrollArea(parent),
    ui(new Ui::PlayerInfoPaneWidget),
    isNewPlayer(playerBattleTag.isEmpty())
{
    ui->setupUi(this);
}

PlayerInfoPaneWidget::~PlayerInfoPaneWidget(void)
{
    delete ui;
}

void PlayerInfoPaneWidget::on_toolButton_savePlayerInfo_clicked(void)
{

    emit playerInfoChanged();
}
