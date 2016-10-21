#include "WidgetFocusHandlerEventFilter.hpp"
#include <QDebug>
#include <QApplication>
#include <QMouseEvent>
#include <QWidget>
#include "Helpers/QEventNameHelper.hpp"

WidgetFocusHandlerEventFilter::WidgetFocusHandlerEventFilter(QObject *parent) : QObject(parent)
{

}

bool WidgetFocusHandlerEventFilter::eventFilter(QObject *watched, QEvent *event)
{
    if (nullptr == watched || nullptr == event)
        return false;

    auto focusWidget = qApp->focusWidget();
    if (nullptr == focusWidget || watched == focusWidget)
        return false;

    if (event->type() == QEvent::MouseButtonPress ||
        event->type() == QEvent::MouseButtonRelease ||
        event->type() == QEvent::MouseButtonDblClick)
    {
        auto focusWidgetClassName = focusWidget->metaObject()->className();
        if (focusWidgetClassName == QString("QLineEdit") ||
            focusWidgetClassName == QString("QPlainTextEdit") ||
            focusWidgetClassName == QString("QListView"))
        {
            auto mouseEvent = static_cast<const QMouseEvent*>(event);
            if (!focusWidget->rect().contains(focusWidget->mapFromGlobal(mouseEvent->globalPos())))
                focusWidget->clearFocus();
        }
    }

    return false;
}
