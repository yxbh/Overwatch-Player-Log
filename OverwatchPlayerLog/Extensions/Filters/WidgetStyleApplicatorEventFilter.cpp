#include "WidgetStyleApplicatorEventFilter.hpp"
#include <QDebug>
#include <QEvent>
#include <QWidget>
#include "Logics/Config.hpp"
#include "Helpers/QEventNameHelper.hpp"

WidgetStyleApplicatorEventFilter::WidgetStyleApplicatorEventFilter(QObject *parent) : QObject(parent)
{

}

bool WidgetStyleApplicatorEventFilter::eventFilter(QObject *watched, QEvent *event)
{
//    static unsigned callCount = 0;
//    ++callCount;
//    qDebug() << "eventFilter on " << watched->metaObject()->className() << ": " << callCount << "Event:" << QEventNameHelper::getEventClassName(event);
    if (nullptr == watched || nullptr == event)
        return false;

    if (watched->metaObject()->className() == QString("QPlainTextEdit"))
    {
        switch (event->type())
        {
            case QEvent::FocusOut:
                static_cast<QWidget*>(watched)->setStyleSheet(Config::getGlobalStylesheet());
                break;

            case QEvent::FocusIn:
                static_cast<QWidget*>(watched)->setStyleSheet(Config::getGlobalStylesheet().section("QPlainTextEdit:focus", 1, 1).section("{", 1, 1).section("}", 0, 0));
                break;

            default: /* do nothing. */ break;
        }

    }

    return false;
}
