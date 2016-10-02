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

public:
    explicit PlayerInfoPaneWidget(QWidget *parent, OwPlayer player);
    ~PlayerInfoPaneWidget(void);

private:
    void updateLabelUrls(void);

signals:
    void playerInfoChanged(void);

private slots:
    void on_toolButton_savePlayerInfo_clicked(void);
    void on_lineEdit_playerBattleTag_textEdited(const QString &arg1);
};

#endif // PLAYERINFOPANEWIDGET_HPP
