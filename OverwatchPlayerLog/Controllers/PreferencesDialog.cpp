#include "PreferencesDialog.hpp"
#include "ui_PreferencesDialog.h"
#include <QDebug>
#include "QFileDialog"
#include "Extensions/Filters/WidgetFocusHandlerEventFilter.hpp"
#include "Logics/Config.hpp"

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
