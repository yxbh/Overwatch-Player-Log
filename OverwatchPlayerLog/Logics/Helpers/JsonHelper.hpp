#ifndef JSONHELPER_H
#define JSONHELPER_H
#include <QJsonDocument>
#include <QString>

class JsonHelper
{
public:
    static void readValue(QJsonDocument & jsonDoc, QString value);
};

#endif // JSONHELPER_H
