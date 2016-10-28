#include "AboutDialog.hpp"
#include "ui_AboutDialog.h"
#include "version.hpp"

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
        .replace("%APP_VER_MAJOR%", QString::number(app::version::MAJOR))
        .replace("%APP_VER_MINOR%", QString::number(app::version::MINOR))
        .replace("%APP_VER_PATCH%", QString::number(app::version::PATCH))
        .replace("%APP_VER_BUILD_NUMBER%", QString::number(app::version::BUILD_NUMBER));
    this->ui->textBrowser->document()->setHtml(aboutContent);
}

AboutDialog::~AboutDialog(void)
{
    delete ui;
}
