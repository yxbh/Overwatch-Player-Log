#include "App.hpp"
#include "Logics/DataSources/SqliteDataSource.hpp"

std::unique_ptr<App> App::s_instance { nullptr };

App::App(void)
{

}

bool App::intitaliseDataSource(void)
{
    this->dataSource.reset(new SqliteDataSource);
    return this->dataSource->connect() && this->dataSource->initialiseTables();
}
