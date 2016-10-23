#include "MainWindow.hpp"
#include "ui_MainWindow.h"
#include <QDebug>
#include <QGuiApplication>
#include <QMessageBox>
#include "App.hpp"
#include "Logics/Config.hpp"
#include "Controllers/PlayerInfoPaneWidget.hpp"
#include "Controllers/PreferencesDialog.hpp"
#include "Extensions/Filters/WidgetFocusHandlerEventFilter.hpp"
#include "Logics/Exceptions/Exception.hpp"
#include "Models/OwPlayerItem.hpp"

MainWindow::MainWindow(QWidget * parent) noexcept:
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    this->ui->setupUi(this);
    this->ui->mainToolBar->addAction(ui->action_LoadCustomStylesheet);
    this->ui->mainToolBar->addAction(ui->action_ResetStylesheet);
    this->installEventFilter(new WidgetFocusHandlerEventFilter(this));
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
    this->connect(qApp, &QGuiApplication::lastWindowClosed, this, &MainWindow::on_lastWindowClosed);
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
    this->favoritePlayerFilterModel.setFilterCaseSensitivity(Qt::CaseInsensitive);
    this->favoritePlayerFilterModel.setSourceModel(&this->allPlayersModel);
    this->ui->listView_favoritePlayers->setModel(&this->favoritePlayerFilterModel);

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

void MainWindow::openPlayerInfoPane(const OwPlayer & player, const QString & label)
{
    auto tabWidget = ui->tabWidget_playerInfos;
    auto playerInfoPane = new PlayerInfoPaneWidget(tabWidget, player);
    tabWidget->addTab(playerInfoPane, label);
    this->connect(playerInfoPane, &PlayerInfoPaneWidget::playerInfoChanged, this, &MainWindow::on_playerInfoChanged);
    tabWidget->setCurrentWidget(playerInfoPane);
    playerInfoPane->setFocus();
}

void MainWindow::on_lastWindowClosed(void)
{
    this->writeSettings();
}

void MainWindow::on_playerInfoChanged(const OwPlayer & player)
{
    this->refreshModels();
    this->ui->tabWidget_playerInfos->setTabText(this->ui->tabWidget_playerInfos->currentIndex(), player.getBattleTag());
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
    this->openPlayerInfoPane(OwPlayer(), "New Player");
}

void MainWindow::on_tabWidget_playerInfos_tabCloseRequested(int index)
{
    this->ui->tabWidget_playerInfos->removeTab(index);
}

void MainWindow::on_action_LoadCustomStylesheet_triggered(void)
{
    qDebug() << "Loading custom stylesheet";
    Config::setIsUsingCustomStyleSheet(true);
    qApp->setStyleSheet(Config::getGlobalStylesheet());
}

void MainWindow::on_action_ResetStylesheet_triggered(void)
{
    qDebug() << "Resetting stylesheet";
    Config::setIsUsingCustomStyleSheet(false);
    qApp->setStyleSheet(Config::getGlobalStylesheet());
}

namespace
{
    QWidget * findTab(QTabWidget * tabWidget, const QString & tabText)
    {
        for (int tabIdx = 0; tabIdx < tabWidget->count(); ++tabIdx)
        {
            if (tabWidget->tabText(tabIdx) == tabText)
                return tabWidget->widget(tabIdx);
        }
        return nullptr;
    }
}

void MainWindow::on_listView_searchAll_doubleClicked(const QModelIndex & index)
{
    auto row = this->allPlayerFilterModel.mapToSource(index).row();
    auto item = static_cast<OwPlayerItem*>(this->allPlayersModel.item(row));
    auto player = item->getPlayer();

    auto existingTab = ::findTab(this->ui->tabWidget_playerInfos, player.getBattleTag());
    if (nullptr != existingTab)
    {
        this->ui->tabWidget_playerInfos->setCurrentWidget(existingTab);
    }
    else
    {
        this->openPlayerInfoPane(player, player.getBattleTag());
    }
}

void MainWindow::on_listView_favoritePlayers_doubleClicked(const QModelIndex &index)
{
    auto row = this->favoritePlayerFilterModel.mapToSource(index).row();
    auto item = static_cast<OwPlayerItem*>(this->allPlayersModel.item(row));
    auto player = item->getPlayer();

    auto existingTab = ::findTab(this->ui->tabWidget_playerInfos, player.getBattleTag());
    if (nullptr != existingTab)
    {
        this->ui->tabWidget_playerInfos->setCurrentWidget(existingTab);
    }
    else
    {
        this->openPlayerInfoPane(player, player.getBattleTag());
    }
}

void MainWindow::on_lineEdit_searchBar_textChanged(const QString & searchText)
{
    this->allPlayerFilterModel.setFilterFixedString(searchText);
    this->favoritePlayerFilterModel.setFilterFixedString(searchText);
    this->ui->tabWidget_playerLists->setCurrentIndex(1);
}

void MainWindow::on_actionPreferences_triggered(void)
{
    (new PreferencesDialog(this))->show();
}
