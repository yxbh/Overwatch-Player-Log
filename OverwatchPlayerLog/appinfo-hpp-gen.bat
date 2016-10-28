@REM reference: http://stackoverflow.com/questions/1417061/automatic-increment-of-build-number-in-qt-creator

@echo off 
(
echo #pragma once
echo #define APP_INFO_COMPANY "%~1"
echo #define APP_INFO_PRODUCT "%~2"
echo #define APP_INFO_DESCRIPTION "%~3"
echo #define APP_INFO_COPYRIGHT "%~4"
echo.
echo namespace app
echo {
echo namespace info
echo {
echo static constexpr char* COMPANY = "%~1";
echo static constexpr char* PRODUCT = "%~2";
echo static constexpr char* DESCRIPTION = "%~3";
echo static constexpr char* COPYRIGHT = "%~4";
echo }
echo } 
) >appinfo.hpp
echo Application information header generated.