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
    static void initialise(void);

    static QPoint getMainWindowPosition(void);
    static void saveMainWindowPosition(const QPoint & pos);

    static QSize getMainWindowSize(void);
    static void saveMainWindowSize(const QSize & size);

    static QString getDatabasePath(void);

    static QString getGlobalStylesheetPath(void);
    static void saveGlobalStylesheetPath(const QString & stylesheetPath);
    static QString getGlobalStylesheet(void);

    static bool isUsingCustomStyleSheet(void);
    static void setIsUsingCustomStyleSheet(bool isUsingCustomStyleSheet = true);
};

#endif // CONFIG_H
