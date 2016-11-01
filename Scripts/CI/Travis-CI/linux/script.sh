# #!/bin/bash
# set -ev

pwd
cd OverwatchPlayerLog

qmake OverwatchPlayerLog.pro "CONFIG+=debug"
make

make clean
qmake OverwatchPlayerLog.pro
make

qmake OverwatchPlayerLog_UnitTest.pro
make