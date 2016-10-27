@REM reference: http://stackoverflow.com/questions/1417061/automatic-increment-of-build-number-in-qt-creator
@REM build.no.txt file may not exist when first run. Create the file with '0' as content.

@echo off 
set /p var= <build-no.txt 
set /a var= %var%+1 
>build-no.txt echo %var%
(
echo #pragma once
echo #define APP_VER_MAJOR %1
echo #define APP_VER_MINOR %2
echo #define APP_VER_PATCH %3
echo #define APP_VER_BUILD_NUMBER %var%
echo.
echo namespace app
echo {
echo namespace version
echo {
echo static constexpr unsigned MAJOR = %1;
echo static constexpr unsigned MINOR = %2;
echo static constexpr unsigned PATCH = %3;
echo static constexpr unsigned BUILD_NUMBER = %var%;
echo }
echo } 
) >version.hpp
echo Version header generated for build %var%.