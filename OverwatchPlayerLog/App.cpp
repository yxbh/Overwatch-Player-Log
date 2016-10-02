#include "App.hpp"
#include "Logics/DataSources/SqliteDataSource.hpp"

std::unique_ptr<App> App::s_instance { nullptr };

App::App(void)
{

}

bool App::intitaliseDataSource(void)
{
    this->dataSoure.reset(new SqliteDataSource);
    return this->dataSoure->connect() && this->dataSoure->initialiseTables();
}
