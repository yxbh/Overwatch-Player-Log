#ifndef MAINWINDOW_HPP
#define MAINWINDOW_HPP
#include <QMainWindow>
#include <QStandardItemModel>

namespace Ui
{
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

private:
    Ui::MainWindow *ui;
    QStandardItemModel allPlayersModel;

public:
    explicit MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

protected:
    virtual void closeEvent(QCloseEvent *event) override;

private:
    void setupSignalsAndSlots(void);
    void readSettings(void);
    void writeSettings(void);
    void setupModels(void);
    void refreshModels(void);

private slots:
    void on_lastWindowClosed(void);
    void on_playerInfoChanged(void);

    void on_action_ExitApp_triggered(void);

    void on_action_AboutQt_triggered(void);

    void on_action_AddPlayer_triggered(void);

    void on_tabWidget_playerInfos_tabCloseRequested(int index);
};

#endif // MAINWINDOW_HPP
