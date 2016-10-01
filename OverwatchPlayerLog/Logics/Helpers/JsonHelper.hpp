#ifndef JSONHELPER_H
#define JSONHELPER_H
#include <QJsonDocument>
#include <QString>

class JsonHelper
{
public:
    static bool loadFile(QString jsonFilePath, QJsonDocument & jsonDoc);
    static bool saveFile(QString jsonFilePath, QJsonDocument & jsonDoc);

    static QJsonValue readValue(QJsonDocument & jsonDoc, QString keyChain);
};

#endif // JSONHELPER_H
