#include "MainWindow.hpp"
#include "ui_MainWindow.h"
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
    this->ui->listView_searchAll->setModel(&allPlayersModel);
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
    tabWidget->addTab(playerInfoPane, "TODO: add player nane here");
    this->connect(playerInfoPane, PlayerInfoPaneWidget::playerInfoChanged, this, MainWindow::on_playerInfoChanged);
    tabWidget->setCurrentWidget(playerInfoPane);
    playerInfoPane->setFocus();
}

void MainWindow::on_tabWidget_playerInfos_tabCloseRequested(int index)
{
    this->ui->tabWidget_playerInfos->removeTab(index);
}
