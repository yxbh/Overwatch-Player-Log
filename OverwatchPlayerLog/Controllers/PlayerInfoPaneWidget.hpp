#ifndef PLAYERINFOPANEWIDGET_HPP
#define PLAYERINFOPANEWIDGET_HPP

#include <QScrollArea>

namespace Ui
{
class PlayerInfoPaneWidget;
}

class PlayerInfoPaneWidget : public QScrollArea
{
    Q_OBJECT

private:
    Ui::PlayerInfoPaneWidget *ui;

    bool isNewPlayer = false;

public:
    explicit PlayerInfoPaneWidget(QWidget *parent = nullptr, QString playerBattleTag = "");
    ~PlayerInfoPaneWidget(void);

signals:
    void playerInfoChanged(void);

private slots:
    void on_toolButton_savePlayerInfo_clicked(void);
};

#endif // PLAYERINFOPANEWIDGET_HPP
