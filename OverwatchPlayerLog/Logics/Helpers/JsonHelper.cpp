#include "JsonHelper.hpp"
#include <memory>
#include <vector>
#include <QDebug>
#include <QFile>
#include <QList>
#include <QJsonObject>

bool JsonHelper::loadFile(QString jsonFilePath, QJsonDocument & jsonDoc)
{
    QFile jsonFile(jsonFilePath);
    if (!jsonFile.exists())
    {
        qDebug() << "JSON file does not exist.";
        return false;
    }

    if (!jsonFile.open(QFile::ReadOnly))
    {
        qDebug() << "JSON file could not open.";
        return false;
    }

    QByteArray jsonBinData = jsonFile.readAll();
    if (jsonBinData.isEmpty())
    {
        qWarning() << "JSON file empty or error reading.";
        return false;
    }

    jsonDoc = QJsonDocument::fromBinaryData(jsonBinData);
    if (jsonDoc.isNull())
    {
        qWarning() << "JSON file JSON data invalid.";
        return false;
    }

    return true;
}

bool JsonHelper::saveFile(QString jsonFilePath, QJsonDocument & jsonDoc)
{
    QFile jsonFile(jsonFilePath);
    if (!jsonFile.open(QFile::WriteOnly))
    {
        return false;
    }

    auto bytesWritten = jsonFile.write(jsonDoc.toBinaryData());
}

namespace
{
    class IJsonKeyChainEntity
    {
    public:
        enum Type
        {
            Object,
            Array
        };

        Type type;
        QString name;

        IJsonKeyChainEntity(Type type, QString name) : type(type), name(name) {}
    };

    class KeyChainObject :
            public IJsonKeyChainEntity
    {
    public:
        KeyChainObject(QString name) : IJsonKeyChainEntity(Type::Object, name) {}
    };

    class KeyChainArray :
            public IJsonKeyChainEntity
    {
    public:
        int index;

        KeyChainArray(QString name, int index) : IJsonKeyChainEntity(Type::Object, name), index(index) {}
    };

    using JsonKeyChainEntityList = std::vector<std::unique_ptr<IJsonKeyChainEntity>>;

    static JsonKeyChainEntityList toEntityList(QString keyChain)
    {
        JsonKeyChainEntityList entityList;
        for (auto object : keyChain.split('.'))
        {
            entityList.emplace_back(new KeyChainObject(object));
        }
        return entityList;
    }
}

QJsonValue JsonHelper::readValue(QJsonDocument & jsonDoc, QString keyChain)
{
    JsonKeyChainEntityList entityList = ::toEntityList(keyChain);
    auto jsonObject = jsonDoc.object();
    QJsonValue jsonValue;
    for (auto & entity : entityList)
    {
        if (!jsonObject.contains(entity->name))
        {
            return QJsonValue(); // return null value.
        }

        jsonValue = jsonObject[entity->name];
        if (jsonValue.isObject())
        {
            jsonObject = jsonValue.toObject();
        }
    }
    return jsonValue;
}
