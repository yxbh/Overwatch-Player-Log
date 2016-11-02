#include "AboutDialog.hpp"
#include "ui_AboutDialog.h"
#include "version.hpp"
#include "appinfo.hpp"

AboutDialog::AboutDialog(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::AboutDialog)
{
    this->setAttribute(Qt::WA_DeleteOnClose);
    this->ui->setupUi(this);
    this->setFixedSize(this->size());
    this->setWindowFlags(this->windowFlags() & ~Qt::WindowContextHelpButtonHint);

    auto aboutContent = this->ui->textBrowser->document()->toHtml();
    aboutContent
        .replace("%APP_INFO_PRODUCT%", app::info::PRODUCT)
        .replace("%APP_INFO_DESCRIPTION%", app::info::DESCRIPTION)
        .replace("%APP_INFO_COPYRIGHT%", app::info::COPYRIGHT)
        .replace("%APP_VER_SEMANTIC%", app::version::SEMANTIC)
        .replace("%APP_VER_BUILD_NUMBER%", QString::number(app::version::BUILD_NUMBER));
    this->ui->textBrowser->document()->setHtml(aboutContent);
}

AboutDialog::~AboutDialog(void)
{
    delete ui;
}
