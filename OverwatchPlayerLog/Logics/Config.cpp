#include "Config.hpp"
#include <QDebug>
#include <QFile>

std::unique_ptr<Config> Config::s_instance { nullptr };

const QString Config::s_defaultConfigFilePath { ("OPL.config") };

void Config::initialise(void)
{
    if (!s_instance)
    {
        s_instance.reset(new Config);
    }

    QFile configFile(s_defaultConfigFilePath);
    if (!configFile.exists())
    {
        qDebug() << "config file does not exist";
        // TODO: generate default
        return;
    }

    // TODO: load config from config file.

}
