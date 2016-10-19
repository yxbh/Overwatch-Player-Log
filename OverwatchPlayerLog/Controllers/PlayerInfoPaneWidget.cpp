#include "PlayerInfoPaneWidget.hpp"
#include <QDebug>
#include <QDesktopServices>
#include <QMessageBox>
#include "ui_PlayerInfoPaneWidget.h"
#include "App.hpp"
#include "Logics/Config.hpp"

PlayerInfoPaneWidget::PlayerInfoPaneWidget(QWidget *parent, OwPlayer player) :
    QWidget(parent),
    ui(new Ui::PlayerInfoPaneWidget),
    player(player)
{
    ui->setupUi(this);
    ui->plainTextEdit_playerNote->installEventFilter(this);
    this->ui->lineEdit_playerBattleTag->setText(player.getBattleTag());
    this->ui->comboBox_owRegion->setCurrentText(player.getRegion());
    this->ui->plainTextEdit_playerNote->setPlainText(player.getNote());
    this->ui->checkBox_isFavorite->setChecked(player.isFavorite());
    this->ui->radioButton_likesPlayer->setChecked(player.getRating() == OwPlayer::Rating::Like);
    this->ui->radioButto_undecidedPlayer->setChecked(player.getRating() == OwPlayer::Rating::Undecided);
    this->ui->radioButton_dislikesPlayer->setChecked(player.getRating() == OwPlayer::Rating::Dislike);
    this->isPlayerInfoDirty = false;

    this->updateStatsSiteButtons();
    this->updateToolButtons();
}

PlayerInfoPaneWidget::~PlayerInfoPaneWidget(void)
{
    delete ui;
}

bool PlayerInfoPaneWidget::eventFilter(QObject * object, QEvent * event)
{
    if (object != this->ui->plainTextEdit_playerNote)
        return QWidget::eventFilter(object, event);

    switch (event->type())
    {
        case QEvent::FocusOut:
            this->ui->plainTextEdit_playerNote->setStyleSheet(Config::getGlobalStylesheet());
        break;

        case QEvent::FocusIn:
        {
            QString stylesheet = Config::getGlobalStylesheet().section("QPlainTextEdit:focus {", 1, 1).section("}", 0, 0);
            this->ui->plainTextEdit_playerNote->setStyleSheet(stylesheet);
        }
        break;

    default: /* do nothing. */ break;
    }

    return QWidget::eventFilter(object, event);
}

void PlayerInfoPaneWidget::updateStatsSiteButtons(void)
{
    if (this->player.getBattleTag().isEmpty())
    {
        this->ui->toolButton_openUrlPlayOverwatch->hide();
        this->ui->toolButton_openUrlMasterOverwatch->hide();
        this->ui->toolButton_openUrlOverbuff->hide();
    }
    else
    {
        this->ui->toolButton_openUrlPlayOverwatch->show();
        this->ui->toolButton_openUrlMasterOverwatch->show();
        this->ui->toolButton_openUrlOverbuff->show();
    }
}

void PlayerInfoPaneWidget::updateToolButtons(void)
{
    auto dataSource = App::getInstance()->getDataSource();
    if (this->player.isNew())
    {
        if (this->isPlayerInfoDirty && dataSource->validatePlayer(this->player))
            this->ui->toolButton_savePlayerInfo->show();
        else
            this->ui->toolButton_savePlayerInfo->hide();
        this->ui->toolButton_updatePlayerInfo->hide();
        this->ui->toolButton_deletePlayerInfo->hide();
    }
    else
    {
        this->ui->toolButton_savePlayerInfo->hide();
        if (this->isPlayerInfoDirty && dataSource->validatePlayer(this->player))
            this->ui->toolButton_updatePlayerInfo->show();
        else
            this->ui->toolButton_updatePlayerInfo->hide();
        this->ui->toolButton_deletePlayerInfo->show();
    }
}

void PlayerInfoPaneWidget::saveCurrentPlayerInfo(void)
{
    if (!this->player.validate())
    {
        QMessageBox::critical(this, "Validation Error", "Validation failed. Save cancelled");
        return;
    }

    this->player.setBattleTag(this->player.getBattleTag().trimmed());
    this->ui->lineEdit_playerBattleTag->setText(this->player.getBattleTag());

    if (!this->player.save())
    {
        QMessageBox::critical(this, "Save Error", "Player info failed to save.");
        return;
    }

    this->isPlayerInfoDirty = false;
    this->updateToolButtons();
}

void PlayerInfoPaneWidget::on_toolButton_savePlayerInfo_clicked(void)
{
    this->saveCurrentPlayerInfo();
    emit playerInfoChanged(this->player);
}

void PlayerInfoPaneWidget::on_toolButton_updatePlayerInfo_clicked(void)
{
    this->saveCurrentPlayerInfo();
    emit playerInfoChanged(this->player);
}

void PlayerInfoPaneWidget::on_toolButton_deletePlayerInfo_clicked(void)
{
    auto buttonPressed = QMessageBox::question(this, "Confirmation", "Are you sure you would like to delete this player record?",
                                          QMessageBox::Yes, QMessageBox::Cancel, QMessageBox::NoButton);
    if (buttonPressed != QMessageBox::Yes)
        return;

    if (!this->player.remove())
    {
        QMessageBox::critical(this, "Save Error", "Failed to remove player info.");
        return;
    }

    emit playerInfoChanged(this->player);
    this->updateToolButtons();
    this->deleteLater();
}


void PlayerInfoPaneWidget::on_lineEdit_playerBattleTag_textEdited(const QString & newBattleTag)
{
    player.setBattleTag(newBattleTag);
    this->isPlayerInfoDirty = true;
    this->updateToolButtons();
}

void PlayerInfoPaneWidget::on_comboBox_owRegion_currentIndexChanged(const QString & region)
{
    player.setRegion(region);
    this->isPlayerInfoDirty = true;
    this->updateStatsSiteButtons();
    this->updateToolButtons();
}

void PlayerInfoPaneWidget::on_plainTextEdit_playerNote_textChanged(void)
{
    player.setNote(this->ui->plainTextEdit_playerNote->toPlainText());
    this->isPlayerInfoDirty = true;
    this->updateToolButtons();
}

void PlayerInfoPaneWidget::on_checkBox_isFavorite_toggled(bool checked)
{
    player.setFavorite(checked);
    this->isPlayerInfoDirty = true;
    this->updateToolButtons();
}

void PlayerInfoPaneWidget::on_radioButton_likesPlayer_toggled(bool checked)
{
    if (checked)
    {
        player.setRating(OwPlayer::Rating::Like);
        this->isPlayerInfoDirty = true;
        this->updateToolButtons();
    }
}

void PlayerInfoPaneWidget::on_radioButto_undecidedPlayer_toggled(bool checked)
{
    if (checked)
    {
        player.setRating(OwPlayer::Rating::Undecided);
        this->isPlayerInfoDirty = true;
        this->updateToolButtons();
    }
}

void PlayerInfoPaneWidget::on_toolButton_openUrlPlayOverwatch_clicked(void)
{
    QDesktopServices::openUrl(QUrl("https://playoverwatch.com/en-us/career/" + this->player.getPlatform() + "/" + this->player.getRegion() + "/" + QString(this->player.getBattleTag()).replace("#", "-")));
}

void PlayerInfoPaneWidget::on_toolButton_openUrlMasterOverwatch_clicked(void)
{
    QDesktopServices::openUrl(QUrl("http://masteroverwatch.com/profile/" + this->player.getPlatform() + "/" + this->player.getRegion() + "/" + QString(this->player.getBattleTag()).replace("#", "-")));
}

void PlayerInfoPaneWidget::on_toolButton_openUrlOverbuff_clicked(void)
{
    QDesktopServices::openUrl(QUrl("https://www.overbuff.com/players/" + this->player.getPlatform() + "/" + QString(this->player.getBattleTag()).replace("#", "-")));
}


void PlayerInfoPaneWidget::on_radioButton_dislikesPlayer_toggled(bool checked)
{
    if (checked)
    {
        player.setRating(OwPlayer::Rating::Dislike);
        this->isPlayerInfoDirty = true;
        this->updateToolButtons();
    }
}
