#ifndef APP_H
#define APP_H
#include <memory>

class App
{
private:
    static std::unique_ptr<App> s_instance;

public:
    App();

    static App * getInstance()
    {
        if (nullptr == s_instance)
            s_instance.reset(new App);
        return s_instance.get();
    }
};

#endif // APP_H
