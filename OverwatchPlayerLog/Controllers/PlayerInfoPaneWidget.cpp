#include "PlayerInfoPaneWidget.hpp"
#include <QDebug>
#include <QMessageBox>
#include "ui_PlayerInfoPaneWidget.h"
#include "App.hpp"

PlayerInfoPaneWidget::PlayerInfoPaneWidget(QWidget *parent, OwPlayer player) :
    QScrollArea(parent),
    ui(new Ui::PlayerInfoPaneWidget),
    player(player)
{
    ui->setupUi(this);
}

PlayerInfoPaneWidget::~PlayerInfoPaneWidget(void)
{
    delete ui;
}

namespace
{
    QString generateHrefAnchor(const QString & link, const QString & anchorText)
    {
        return "<a href=\"" + link + "\">" + anchorText + "</a>";
    }
}

void PlayerInfoPaneWidget::updateLabelUrls(void)
{
    QString playerHyperBtag = player.getBattleTag();
    playerHyperBtag.replace("#", "-");

    QString platform = "pc";
    QString region = this->ui->comboBox_owRegion->currentText().toLower();

    this->ui->label_openUrlPlayOverwatch->setText(generateHrefAnchor("https://playoverwatch.com/en-us/career/" + platform + "/" + region +"/" + playerHyperBtag, "PlayOverwatch"));
    this->ui->label_openUrlMasterOverwatch->setText(generateHrefAnchor("http://masteroverwatch.com/profile/" + platform + "/" + region +"/" + playerHyperBtag, "MasterOverwatch"));
    this->ui->label_openUrlOverbuff->setText(generateHrefAnchor("https://www.overbuff.com/players/" + platform + "/" + playerHyperBtag, "Overbuff"));
}

void PlayerInfoPaneWidget::on_toolButton_savePlayerInfo_clicked(void)
{
    if (!player.validate())
    {
        QMessageBox::critical(this, "Validation Error", "Validation failed.");
        return;
    }

    if (!player.save())
    {
        QMessageBox::critical(this, "Save Error", "Player info failed to save.");
        return;
    }

    emit playerInfoChanged();
}

void PlayerInfoPaneWidget::on_lineEdit_playerBattleTag_textEdited(const QString & newBattleTag)
{
    player.setBattleTag(newBattleTag);
    this->updateLabelUrls();
}

void PlayerInfoPaneWidget::on_comboBox_owRegion_currentIndexChanged(const QString & region)
{
    player.setRegion(region);
    this->updateLabelUrls();
}
