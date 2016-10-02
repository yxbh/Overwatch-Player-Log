#include <QApplication>
#include <QMessageBox>
#include <QString>
#include "Controllers/MainWindow.hpp"
#include "Logics/Config.hpp"
#include "App.hpp"

int main(int argc, char *argv[])
{
    QCoreApplication::setOrganizationName("IBS");
    QCoreApplication::setApplicationName("OPL");
    Config::initialise();

    QApplication app(argc, argv);

    if (!App::getInstance()->intitaliseDataSource())
    {
        QMessageBox::critical(nullptr, QObject::tr("Critical DataSource Error"), "Error initialising the applicaiton data soruce.");
        return -1;
    }

    MainWindow window;
    window.show();

    return app.exec();
}
