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
echo static const char* const COMPANY = "%~1";
echo static const char* const PRODUCT = "%~2";
echo static const char* const DESCRIPTION = "%~3";
echo static const char* const COPYRIGHT = "%~4";
echo }
echo } 
) >appinfo.hpp
echo Application information header generated.