#include "PreferencesDialog.hpp"
#include "ui_PreferencesDialog.h"
#include <QDebug>
#include <QFileDialog>
#include <QMessageBox>
#include "Extensions/Filters/WidgetFocusHandlerEventFilter.hpp"
#include "Logics/Config.hpp"
#include "Logics/Exceptions/Exception.hpp"

PreferencesDialog::PreferencesDialog(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::PreferencesDialog)
{
    this->setAttribute(Qt::WA_DeleteOnClose);
    this->ui->setupUi(this);
    this->installEventFilter(new WidgetFocusHandlerEventFilter(this));
    this->ui->tabWidget->setFocus();

    this->ui->checkBox_useCustomStylesheet->setChecked(Config::isUsingCustomStyleSheet());
    this->ui->lineEdit_customStylesheetPath->setText(Config::getGlobalStylesheetPath());
    this->ui->lineEdit_playerInforDatabasePath->setText(Config::getDatabasePath());
}

PreferencesDialog::~PreferencesDialog(void)
{
    delete ui;
}

void PreferencesDialog::on_pushButton_browseCustomStylesheet_clicked(void)
{
    auto stylesheetPath = QFileDialog::getOpenFileName(this, "Open Stylesheet");
    if (stylesheetPath.isEmpty())
        return;

    // TODO: validate choosen file is stylesheet
    QFile stylesheetFile(stylesheetPath);
    if (!stylesheetFile.exists())
    {
        qDebug() << "Stylesheet file does not exist [" << stylesheetPath << "]";
        return;
    }

    Config::saveGlobalStylesheetPath(stylesheetPath);
    Config::setIsUsingCustomStyleSheet(true);
    qApp->setStyleSheet(Config::getGlobalStylesheet());
    this->ui->checkBox_useCustomStylesheet->setChecked(Config::isUsingCustomStyleSheet());
}

void PreferencesDialog::on_pushButton_reloadCustomStylesheet_clicked(void)
{
    qApp->setStyleSheet(Config::getGlobalStylesheet());
    Config::setIsUsingCustomStyleSheet(true);
    this->ui->checkBox_useCustomStylesheet->setChecked(Config::isUsingCustomStyleSheet());
}

void PreferencesDialog::on_pushButton_reloadSystemStylesheet_clicked(void)
{
    qApp->setStyleSheet("");
    Config::setIsUsingCustomStyleSheet(false);
    this->ui->checkBox_useCustomStylesheet->setChecked(Config::isUsingCustomStyleSheet());
}

void PreferencesDialog::on_checkBox_useCustomStylesheet_toggled(bool checked)
{
    Config::setIsUsingCustomStyleSheet(checked);
    qApp->setStyleSheet(Config::getGlobalStylesheet());
}

void PreferencesDialog::on_pushButton_browsePlayerInfoDatabase_clicked(void)
{
    auto databasePath = QFileDialog::getOpenFileName(this, "Open Database");
    if (databasePath.isEmpty())
        return;

    try
    {
        Config::saveDatabasePath(databasePath);
        this->ui->lineEdit_playerInforDatabasePath->setText(databasePath);
    }
    catch (Exception & e)
    {
        QMessageBox::critical(this, "Error Saving database path", e.what());
    }
}
