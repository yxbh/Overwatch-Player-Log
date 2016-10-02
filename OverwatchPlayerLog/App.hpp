#ifndef APP_H
#define APP_H
#include <memory>
#include "Logics/DataSources/IDataSource.hpp"

class App
{
private:
    static std::unique_ptr<App> s_instance;
    static constexpr unsigned s_latestDataSourceVersion = 1;

    std::unique_ptr<IDataSource> dataSource;

public:
    App();

    bool intitaliseDataSource(void);

    IDataSource * getDataSource(void) { return this->dataSource.get(); }

    static App * getInstance(void)
    {
        if (nullptr == s_instance)
            s_instance.reset(new App);
        return s_instance.get();
    }

    inline static unsigned getLatestDataSourceVersion(void) { return s_latestDataSourceVersion; }
};

#endif // APP_H
