#include <QApplication>
#include "Controllers/MainWindow.hpp"
#include "Logics/Config.hpp"

int main(int argc, char *argv[])
{
    QCoreApplication::setOrganizationDomain("IBS");
    QCoreApplication::setApplicationName("OPL");

    QApplication app(argc, argv);

    MainWindow window;
    window.show();

    return app.exec();
}
