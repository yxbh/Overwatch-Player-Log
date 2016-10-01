#ifndef MAINWINDOW_HPP
#define MAINWINDOW_HPP

#include <QMainWindow>

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

protected:
    virtual void closeEvent(QCloseEvent *event) override;

private slots:
    void on_action_ExitApp_triggered();

    void on_action_AboutQt_triggered();

    void on_app_lastWIndowClosed();

private:
    void setupSignalsAndSlots();
    void readSettings();
    void writeSettings();

private:
    Ui::MainWindow *ui;
};

#endif // MAINWINDOW_HPP
