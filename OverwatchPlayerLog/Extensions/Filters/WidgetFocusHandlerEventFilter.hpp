#ifndef WIDGETFOCUSHANDLEREVENTFILTER_H
#define WIDGETFOCUSHANDLEREVENTFILTER_H
#include <QObject>

class WidgetFocusHandlerEventFilter : public QObject
{
    Q_OBJECT
public:
    explicit WidgetFocusHandlerEventFilter(QObject *parent = 0);

    virtual bool eventFilter(QObject *watched, QEvent *event) override;
};

#endif // WIDGETFOCUSHANDLEREVENTFILTER_H
