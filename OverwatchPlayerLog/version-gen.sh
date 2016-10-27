# reference: http://stackoverflow.com/questions/1417061/automatic-increment-of-build-number-in-qt-creator

#!/bin/bash
number=`cat build-no.txt`
let number++
echo "$number" | tee build-no.txt #<-- output and save the number back to file

cat <<EOT > version.hpp
#pragma once
#define APP_VER_MAJOR $1
#define APP_VER_MINOR $2
#define APP_VER_PATCH $3
#define APP_VER_BUILD_NUMBER $number

namespace app
{
namespace version
{
static constexpr unsigned MAJOR = $1;
static constexpr unsigned MINOR = $2;
static constexpr unsigned PATCH = $3;
static constexpr unsigned BUILD_NUMBER = $number;
}
}
EOT

echo Version header generated for build $number.