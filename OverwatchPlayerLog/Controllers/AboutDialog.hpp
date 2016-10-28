#ifndef ABOUTDIALOG_HPP
#define ABOUTDIALOG_HPP

#include <QDialog>

namespace Ui {
class AboutDialog;
}

class AboutDialog : public QDialog
{
    Q_OBJECT

private:
    Ui::AboutDialog *ui;

public:
    explicit AboutDialog(QWidget *parent = nullptr);
    ~AboutDialog(void);

};

#endif // ABOUTDIALOG_HPP
