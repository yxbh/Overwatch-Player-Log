#ifndef QEVENTNAMEHELPER_H
#define QEVENTNAMEHELPER_H
#include <QEvent>
#include <QMetaEnum>

class QEventNameHelper
{
public:
    static QString toEventName(const QEvent * event);
private:
    QEventNameHelper(void) = delete;
};

#endif // QEVENTNAMEHELPER_H
