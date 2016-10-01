#ifndef CONFIG_H
#define CONFIG_H
#include <memory>
#include <QJsonDocument>
#include <QString>
#include <QSettings>
#include <QPoint>

class Config
{
private:
    static QSettings settings;

public:
    static void saveToConfigFile(void);

    static QPoint getMainWindowPosition(void);
    static void saveMainWindowPosition(QPoint pos);

    static QSize getMainWindowSize(void);
    static void saveMainWindowSize(QSize size);
};

#endif // CONFIG_H
