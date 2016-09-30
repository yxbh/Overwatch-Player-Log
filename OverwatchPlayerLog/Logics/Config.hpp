#ifndef CONFIG_H
#define CONFIG_H
#include <memory>
#include <QJsonDocument>
#include <QString>

class Config
{
private:
    static std::unique_ptr<Config> s_instance;
    static const QString s_defaultConfigFilePath;

    QJsonDocument m_jsonDoc;

public:

    static inline Config * getInstance()
    {
        initialise();
        return s_instance.get();
    }

    static void initialise();
};

#endif // CONFIG_H
