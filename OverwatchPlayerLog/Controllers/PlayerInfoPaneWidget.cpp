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
    this->updateLabelUrls();
    this->updateToolButtons();
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
    if (playerHyperBtag.isEmpty())
    {
        this->ui->label_openUrlPlayOverwatch->setText("");
        this->ui->label_openUrlMasterOverwatch->setText("");
        this->ui->label_openUrlOverbuff->setText("");
        return;
    }

    playerHyperBtag.replace("#", "-");

    QString platform = "pc";
    QString region = this->ui->comboBox_owRegion->currentText().toLower();

    this->ui->label_openUrlPlayOverwatch->setText(generateHrefAnchor("https://playoverwatch.com/en-us/career/" + platform + "/" + region +"/" + playerHyperBtag, "PlayOverwatch"));
    this->ui->label_openUrlMasterOverwatch->setText(generateHrefAnchor("http://masteroverwatch.com/profile/" + platform + "/" + region +"/" + playerHyperBtag, "MasterOverwatch"));
    this->ui->label_openUrlOverbuff->setText(generateHrefAnchor("https://www.overbuff.com/players/" + platform + "/" + playerHyperBtag, "Overbuff"));
}

void PlayerInfoPaneWidget::updateToolButtons(void)
{
    if (player.isNew())
    {
        this->ui->toolButton_savePlayerInfo->show();
        this->ui->toolButton_updatePlayerInfo->hide();
        this->ui->toolButton_deletePlayerInfo->hide();
    }
    else
    {
        this->ui->toolButton_savePlayerInfo->hide();
        this->ui->toolButton_updatePlayerInfo->show();
        this->ui->toolButton_deletePlayerInfo->show();
    }
}

void PlayerInfoPaneWidget::on_toolButton_savePlayerInfo_clicked(void)
{
    if (!player.validate())
    {
        QMessageBox::critical(this, "Validation Error", "Validation failed. Save cancelled");
        return;
    }

    if (!player.save())
    {
        QMessageBox::critical(this, "Save Error", "Player info failed to save.");
        return;
    }

    emit playerInfoChanged();
    this->updateToolButtons();
}

void PlayerInfoPaneWidget::on_toolButton_updatePlayerInfo_clicked(void)
{
    if (!player.validate())
    {
        QMessageBox::critical(this, "Validation Error", "Validation failed. Save cancelled");
        return;
    }

    if (!player.save())
    {
        QMessageBox::critical(this, "Save Error", "Player info failed to save.");
        return;
    }

    emit playerInfoChanged();
    this->updateToolButtons();
}

void PlayerInfoPaneWidget::on_toolButton_deletePlayerInfo_clicked(void)
{
    auto buttonPressed = QMessageBox::question(this, "Confirmation", "Are you sure you would like to delete this player record?",
                                          QMessageBox::Yes, QMessageBox::Cancel, QMessageBox::NoButton);
    if (buttonPressed != QMessageBox::Yes)
        return;

    if (!player.remove())
    {
        QMessageBox::critical(this, "Save Error", "Failed to remove player info.");
        return;
    }

    emit playerInfoChanged();
    this->updateToolButtons();
    this->close();
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
