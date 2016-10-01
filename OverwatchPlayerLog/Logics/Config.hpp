#ifndef CONFIG_H
#define CONFIG_H
#include <memory>
#include <QSettings>
#include <QPoint>
#include <QSize>

class Config
{
private:
    static std::unique_ptr<QSettings> settings;

public:
    static initialise(void);

    static QPoint getMainWindowPosition(void);
    static void saveMainWindowPosition(QPoint pos);

    static QSize getMainWindowSize(void);
    static void saveMainWindowSize(QSize size);
};

#endif // CONFIG_H
