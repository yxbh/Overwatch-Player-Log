#include "WidgetFocusHandlerEventFilter.hpp"
#include <QDebug>
#include <QApplication>
#include <QMouseEvent>
#include <QStringList>
#include <QWidget>
#include "Helpers/QEventNameHelper.hpp"

namespace
{
    static const QStringList WidgetList = []() {
        QStringList list;
        list.append("QLineEdit");
        list.append("QPlainTextEdit");
        list.append("QListView");
        list.append("QDateTimeEdit");
        return list;
    }();
}

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
        if (::WidgetList.contains(focusWidgetClassName))
        {
            auto mouseEvent = static_cast<const QMouseEvent*>(event);
            if (!focusWidget->rect().contains(focusWidget->mapFromGlobal(mouseEvent->globalPos())))
                focusWidget->clearFocus();
        }
    }

    return false;
}
