#ifndef QEVENTNAMEHELPER_H
#define QEVENTNAMEHELPER_H
#include <QEvent>
#include <QMetaEnum>

class QEventNameHelper
{
public:
    static QString getEventClassName(const QEvent * event);
private:
    QEventNameHelper(void) = delete;
};

#endif // QEVENTNAMEHELPER_H
