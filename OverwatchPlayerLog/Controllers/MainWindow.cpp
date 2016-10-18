#include "MainWindow.hpp"
#include "ui_MainWindow.h"
#include <QDebug>
#include <QGuiApplication>
#include <QMessageBox>
#include "Logics/Config.hpp"
#include "App.hpp"
#include "Controllers/PlayerInfoPaneWidget.hpp"
#include "Logics/Entities/OwPlayer.hpp"
#include "Logics/Exceptions/Exception.hpp"
#include "Models/OwPlayerItem.hpp"

MainWindow::MainWindow(QWidget * parent) noexcept:
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    ui->mainToolBar->addAction(ui->action_LoadCustomStylesheet);
    ui->mainToolBar->addAction(ui->action_ResetStylesheet);
    this->setupSignalsAndSlots();
    this->readSettings();

    this->setupModels();
    this->refreshModels();
}

MainWindow::~MainWindow(void)
{
    delete ui;
}

void MainWindow::closeEvent(QCloseEvent * event)
{
    this->writeSettings();

    this->QMainWindow::closeEvent(event);
}

void MainWindow::setupSignalsAndSlots(void)
{
    this->connect(qApp, QGuiApplication::lastWindowClosed, this, MainWindow::on_lastWindowClosed);
}

void MainWindow::readSettings(void)
{
    this->move(Config::getMainWindowPosition());
    this->resize(Config::getMainWindowSize());
}

void MainWindow::writeSettings(void)
{
    Config::saveMainWindowPosition(this->pos());
    Config::saveMainWindowSize(this->size());
}

void MainWindow::setupModels(void)
{
    this->allPlayerFilterModel.setFilterCaseSensitivity(Qt::CaseInsensitive);
    this->allPlayerFilterModel.setSourceModel(&this->allPlayersModel);
    this->ui->listView_searchAll->setModel(&this->allPlayerFilterModel);
}

void MainWindow::refreshModels(void)
{
    this->allPlayersModel.clear();
    try
    {
        for (auto player : App::getInstance()->getDataSource()->getAllPlayers())
        {
            this->allPlayersModel.appendRow(new OwPlayerItem(player));
        }
    }
    catch (Exception & e)
    {
        QMessageBox::critical(this, tr("Critical Error"), tr("Error refreshing the data models.\n") + e.what());
    }
}

void MainWindow::on_lastWindowClosed(void)
{
    this->writeSettings();
}

void MainWindow::on_playerInfoChanged(void)
{
    this->refreshModels();
}

void MainWindow::on_action_ExitApp_triggered(void)
{
    qApp->closeAllWindows();
}

void MainWindow::on_action_AboutQt_triggered(void)
{
    qApp->aboutQt();
}

void MainWindow::on_action_AddPlayer_triggered(void)
{
    OwPlayer newPlayer;
    auto tabWidget = ui->tabWidget_playerInfos;
    auto playerInfoPane = new PlayerInfoPaneWidget(tabWidget, newPlayer);
    tabWidget->addTab(playerInfoPane, "New Player");
    this->connect(playerInfoPane, PlayerInfoPaneWidget::playerInfoChanged, this, MainWindow::on_playerInfoChanged);
    tabWidget->setCurrentWidget(playerInfoPane);
    playerInfoPane->setFocus();
}

void MainWindow::on_tabWidget_playerInfos_tabCloseRequested(int index)
{
    this->ui->tabWidget_playerInfos->removeTab(index);
}

void MainWindow::on_action_LoadCustomStylesheet_triggered(void)
{
    qDebug() << "Loading custom stylesheet";
    qApp->setStyleSheet(Config::getGlobalStylesheet());
}

void MainWindow::on_action_ResetStylesheet_triggered(void)
{
    qDebug() << "Resetting stylesheet";
    qApp->setStyleSheet("");
}

void MainWindow::on_listView_searchAll_doubleClicked(const QModelIndex & index)
{
    auto row = this->allPlayerFilterModel.mapToSource(index).row();
    auto item = static_cast<OwPlayerItem*>(this->allPlayersModel.item(row));
    auto player = item->getPlayer();
    auto tabWidget = ui->tabWidget_playerInfos;
    auto playerInfoPane = new PlayerInfoPaneWidget(tabWidget, player);
    tabWidget->addTab(playerInfoPane, player.getBattleTag());
    this->connect(playerInfoPane, PlayerInfoPaneWidget::playerInfoChanged, this, MainWindow::on_playerInfoChanged);
    tabWidget->setCurrentWidget(playerInfoPane);
    playerInfoPane->setFocus();
}

void MainWindow::on_lineEdit_searchBar_textChanged(const QString & searchText)
{
    this->allPlayerFilterModel.setFilterFixedString(searchText);
}
