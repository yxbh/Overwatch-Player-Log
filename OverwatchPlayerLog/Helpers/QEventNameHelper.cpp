#include "QEventNameHelper.hpp"

namespace
{
    QString pointerToString(const void * pointer)
    {
        return QString("0x%1").arg((quintptr)pointer,QT_POINTER_SIZE * 2, 16, QChar('0'));
    }
}

// ref: http://stackoverflow.com/questions/22535469/how-to-get-human-readable-event-type-from-qevent
QString QEventNameHelper::toEventName(const QEvent * event)
{
    static int eventEnumIndex = QEvent::staticMetaObject.indexOfEnumerator("Type");
    if (event)
    {
        QString name = QEvent::staticMetaObject.enumerator(eventEnumIndex).valueToKey(event->type());
        return (!name.isEmpty()) ? name : QString::number(event->type());
    }
    else
    {
        return ::pointerToString(reinterpret_cast<const void*>(event));
    }
}
