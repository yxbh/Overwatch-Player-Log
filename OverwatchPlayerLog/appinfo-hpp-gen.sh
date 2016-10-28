# reference: http://stackoverflow.com/questions/1417061/automatic-increment-of-build-number-in-qt-creator

#!/bin/bash

cat <<EOT > appinfo.hpp
#pragma once
#define APP_INFO_COMPANY     "%~1"
#define APP_INFO_PRODUCT     "%~2"
#define APP_INFO_DESCRIPTION "%~3"
#define APP_INFO_COPYRIGHT   "%~4"

namespace app
{
namespace info
{
static const char* const COMPANY     = "%~1";
static const char* const PRODUCT     = "%~2";
static const char* const DESCRIPTION = "%~3";
static const char* const COPYRIGHT   = "%~4";
}
} 
EOT

echo Application information header generated.