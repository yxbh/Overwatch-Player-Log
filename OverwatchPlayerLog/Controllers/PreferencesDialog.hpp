#ifndef PREFERENCESDIALOG_HPP
#define PREFERENCESDIALOG_HPP

#include <QDialog>

namespace Ui {
class PreferencesDialog;
}

class PreferencesDialog : public QDialog
{
    Q_OBJECT

private:
    Ui::PreferencesDialog *ui;

public:
    explicit PreferencesDialog(QWidget *parent = nullptr);
    ~PreferencesDialog(void);

private slots:
    void on_pushButton_browseCustomStylesheet_clicked(void);
    void on_pushButton_reloadCustomStylesheet_clicked(void);
    void on_pushButton_reloadSystemStylesheet_clicked(void);
    void on_checkBox_useCustomStylesheet_toggled(bool checked);
    void on_pushButton_browsePlayerInfoDatabase_clicked(void);

};

#endif // PREFERENCESDIALOG_HPP
