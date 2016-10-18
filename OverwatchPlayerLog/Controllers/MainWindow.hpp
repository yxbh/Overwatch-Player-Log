#ifndef MAINWINDOW_HPP
#define MAINWINDOW_HPP
#include <QMainWindow>
#include <QStandardItemModel>
#include <QSortFilterProxyModel>
#include "Logics/Entities/OwPlayer.hpp"

namespace Ui
{
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

private:
    Ui::MainWindow *ui;

    QSortFilterProxyModel allPlayerFilterModel;
    QStandardItemModel allPlayersModel;

public:
    explicit MainWindow(QWidget *parent = nullptr) noexcept;
    ~MainWindow();

protected:
    virtual void closeEvent(QCloseEvent *event) override;

private:
    void setupSignalsAndSlots(void);
    void readSettings(void);
    void writeSettings(void);
    void setupModels(void);
    void refreshModels(void);
    void openPlayerInfoPane(const OwPlayer & player, const QString & label);

private slots:
    void on_lastWindowClosed(void);
    void on_playerInfoChanged(void);

    void on_action_ExitApp_triggered(void);
    void on_action_AboutQt_triggered(void);
    void on_action_AddPlayer_triggered(void);
    void on_tabWidget_playerInfos_tabCloseRequested(int index);
    void on_action_LoadCustomStylesheet_triggered(void);
    void on_action_ResetStylesheet_triggered(void);
    void on_listView_searchAll_doubleClicked(const QModelIndex & index);
    void on_lineEdit_searchBar_textChanged(const QString & searchText);
};

#endif // MAINWINDOW_HPP
