#include <QApplication>
#include "Controllers/MainWindow.hpp"
#include "Logics/Config.hpp"

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);

    Config::initialise();

    MainWindow window;
    window.show();

    return app.exec();
}
