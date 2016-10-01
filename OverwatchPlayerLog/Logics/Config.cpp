#include "Config.hpp"
#include <QCoreApplication>

std::unique_ptr<QSettings> Config::settings; // { QSettings::IniFormat, QSettings::UserScope, QCoreApplication::organizationName(), QCoreApplication::applicationName() };

void Config::initialise(void)
{
    settings.reset(new  QSettings(QSettings::IniFormat, QSettings::UserScope, QCoreApplication::organizationName(), QCoreApplication::applicationName()));
}

QPoint Config::getMainWindowPosition(void)
{
    return settings->value("mainWindow/pos", QPoint(0, 0)).toPoint();
}

void Config::saveMainWindowPosition(QPoint pos)
{
    settings->setValue("mainWindow/pos", pos);
}

QSize Config::getMainWindowSize(void)
{
    return settings->value("mainWindow/size", QSize(700, 600)).toSize();
}

void Config::saveMainWindowSize(QSize size)
{
    settings->setValue("mainWindow/size", size);
}
