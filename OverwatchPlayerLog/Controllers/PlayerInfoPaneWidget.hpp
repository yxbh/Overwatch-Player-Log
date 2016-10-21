#ifndef PLAYERINFOPANEWIDGET_HPP
#define PLAYERINFOPANEWIDGET_HPP

#include <QWidget>
#include "Logics/Entities/OwPlayer.hpp"

namespace Ui
{
class PlayerInfoPaneWidget;
}

class PlayerInfoPaneWidget : public QWidget
{
    Q_OBJECT

private:
    Ui::PlayerInfoPaneWidget *ui;
    OwPlayer player;
    bool isPlayerInfoDirty = false;

public:
    explicit PlayerInfoPaneWidget(QWidget *parent, OwPlayer player);
    ~PlayerInfoPaneWidget(void);

protected:
    virtual bool eventFilter(QObject * object, QEvent * event) override;

private:
    void updateStatsSiteButtons(void);
    void updatePlayerInfoDateTimes(void);
    void updateToolButtons(void);

    void saveCurrentPlayerInfo(void);

signals:
    void playerInfoChanged(const OwPlayer & player);

private slots:
    void on_toolButton_savePlayerInfo_clicked(void);
    void on_toolButton_updatePlayerInfo_clicked(void);
    void on_toolButton_deletePlayerInfo_clicked(void);
    void on_lineEdit_playerBattleTag_textEdited(const QString & newBattleTag);
    void on_comboBox_owRegion_currentIndexChanged(const QString & region);
    void on_plainTextEdit_playerNote_textChanged(void);
    void on_checkBox_isFavorite_toggled(bool checked);
    void on_radioButton_likesPlayer_toggled(bool checked);
    void on_radioButto_undecidedPlayer_toggled(bool checked);
    void on_radioButton_dislikesPlayer_toggled(bool checked);
    void on_toolButton_openUrlPlayOverwatch_clicked(void);
    void on_toolButton_openUrlMasterOverwatch_clicked(void);
    void on_toolButton_openUrlOverbuff_clicked(void);
};

#endif // PLAYERINFOPANEWIDGET_HPP
