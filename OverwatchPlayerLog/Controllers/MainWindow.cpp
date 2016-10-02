#include "MainWindow.hpp"
#include "ui_MainWindow.h"
#include <QGuiApplication>
#include "Logics/Config.hpp"
#include "App.hpp"
#include "Controllers/PlayerInfoPaneWidget.hpp"
#include "Logics/Entities/OwPlayer.hpp"

MainWindow::MainWindow(QWidget * parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->setupSignalsAndSlots();
    this->readSettings();

    this->setupModels();
    this->refreshPlayerNamesModel();
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
    this->ui->listView_searchAll->setModel(&allPlayerNamesModel);
}

void MainWindow::refreshPlayerNamesModel(void)
{
    this->allPlayerNamesModel.setStringList(App::getInstance()->getDataSource()->getAllPlayerNames());
}

void MainWindow::on_lastWindowClosed(void)
{
    this->writeSettings();
}

void MainWindow::on_playerInfoChanged(void)
{
    this->refreshPlayerNamesModel();
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
