# go to source directory.
pwd
cd OverwatchPlayerLog

# build DEBUG build.
qmake OverwatchPlayerLog.pro "CONFIG+=debug"
mingw32-make

# build RELEASE build.
qmake OverwatchPlayerLog.pro
mingw32-make