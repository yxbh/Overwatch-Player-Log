#ifndef PLAYERINFOPANEWIDGET_HPP
#define PLAYERINFOPANEWIDGET_HPP

#include <QScrollArea>
#include "Logics/Entities/OwPlayer.hpp"

namespace Ui
{
class PlayerInfoPaneWidget;
}

class PlayerInfoPaneWidget : public QScrollArea
{
    Q_OBJECT

private:
    Ui::PlayerInfoPaneWidget *ui;
    OwPlayer player;
    bool isPlayerInfoDirty = false;

public:
    explicit PlayerInfoPaneWidget(QWidget *parent, OwPlayer player);
    ~PlayerInfoPaneWidget(void);

private:
    void updateLabelUrls(void);
    void updateToolButtons(void);

signals:
    void playerInfoChanged(const OwPlayer & player);

private slots:
    void on_toolButton_savePlayerInfo_clicked(void);
    void on_toolButton_updatePlayerInfo_clicked(void);
    void on_toolButton_deletePlayerInfo_clicked(void);
    void on_lineEdit_playerBattleTag_textEdited(const QString & newBattleTag);
    void on_comboBox_owRegion_currentIndexChanged(const QString & region);
    void on_plainTextEdit_playerNote_textChanged(void);
};

#endif // PLAYERINFOPANEWIDGET_HPP
