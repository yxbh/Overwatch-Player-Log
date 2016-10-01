#include "Config.hpp"
#include <QApplication>

QSettings Config::settings { QSettings::IniFormat, QSettings::UserScope, QApplication::organizationName(), QApplication::applicationName() };

void Config::saveToConfigFile(void)
{

}

QPoint Config::getMainWindowPosition(void)
{
    return settings.value("mainWindow/pos", QPoint(0, 0)).toPoint();
}

void Config::saveMainWindowPosition(QPoint pos)
{
    settings.setValue("mainWindow/pos", pos);
}

QSize Config::getMainWindowSize(void)
{
    return settings.value("mainWindow/size", QSize(400, 300)).toSize();
}

void Config::saveMainWindowSize(QSize size)
{
    settings.setValue("mainWindow/size", size);
}
