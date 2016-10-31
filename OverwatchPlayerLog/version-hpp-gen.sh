# reference: http://stackoverflow.com/questions/1417061/automatic-increment-of-build-number-in-qt-creator

#!/bin/bash
file="build-no.txt"
touch "$file" 2>/dev/null || { echo "Cannot write to $file" >&2; exit 1; }

number=`cat build-no.txt`
let number++
echo "$number" | tee build-no.txt #<-- output and save the number back to file

cat <<EOT > version.hpp
#pragma once
#define APP_VER_MAJOR $1
#define APP_VER_MINOR $2
#define APP_VER_PATCH $3
#define APP_VER_BUILD_NUMBER $number
#define APP_VER_SEMANTIC "$1.$2.$3"
#define APP_VER_FULL "$1.$2.$3.$number"

namespace app
{
namespace version
{
static constexpr unsigned MAJOR = $1;
static constexpr unsigned MINOR = $2;
static constexpr unsigned PATCH = $3;
static constexpr unsigned BUILD_NUMBER = $number;
static const     char* const SEMANTIC = "$1.$2.$3";
static const     char* const FULL = "$1.$2.$3.$number";
}
}
EOT

echo Version header generated for build $number.
