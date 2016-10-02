#ifndef APP_H
#define APP_H
#include <memory>
#include "Logics/DataSources/IDataSource.hpp"

class App
{
private:
    static std::unique_ptr<App> s_instance;

    std::unique_ptr<IDataSource> dataSoure;

public:
    App();

    bool intitaliseDataSource(void);

    static App * getInstance(void)
    {
        if (nullptr == s_instance)
            s_instance.reset(new App);
        return s_instance.get();
    }
};

#endif // APP_H
