# go to source directory.
pwd
cd OverwatchPlayerLog

# build DEBUG build.
qmake OverwatchPlayerLog.pro "CONFIG+=debug"
IF ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

mingw32-make
IF ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

# build RELEASE build.
qmake OverwatchPlayerLog.pro
IF ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

mingw32-make
IF ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }