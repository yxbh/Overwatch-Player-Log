#include <QApplication>
#include "Controllers/MainWindow.hpp"
#include "Logics/Config.hpp"

int main(int argc, char *argv[])
{
    QCoreApplication::setOrganizationName("IBS");
    QCoreApplication::setApplicationName("OPL");
    Config::initialise();

    QApplication app(argc, argv);

    MainWindow window;
    window.show();

    return app.exec();
}
