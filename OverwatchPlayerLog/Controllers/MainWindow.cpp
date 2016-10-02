#include "MainWindow.hpp"
#include "ui_MainWindow.h"
#include <QGuiApplication>
#include "Logics/Config.hpp"

MainWindow::MainWindow(QWidget * parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->setupSignalsAndSlots();
    this->readSettings();
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

void MainWindow::on_action_ExitApp_triggered(void)
{
    qApp->closeAllWindows();
}

void MainWindow::on_action_AboutQt_triggered(void)
{
    qApp->aboutQt();
}

void MainWindow::on_lastWindowClosed(void)
{
    // save config.

    this->writeSettings();
}
