# reference: http://stackoverflow.com/questions/1417061/automatic-increment-of-build-number-in-qt-creator

#!/bin/bash

cat <<EOT > appinfo.hpp
#pragma once
#define APP_INFO_COMPANY "%~1"
#define APP_INFO_PRODUCT "%~2"
#define APP_INFO_DESCRIPTION "%~3"
#define APP_INFO_COPYRIGHT "%~4"

namespace app
{
namespace info
{
static constexpr char* COMPANY = "%~1";
static constexpr char* PRODUCT = "%~2";
static constexpr char* DESCRIPTION = "%~3";
static constexpr char* COPYRIGHT = "%~4";
}
} 
EOT

echo Application information header generated.