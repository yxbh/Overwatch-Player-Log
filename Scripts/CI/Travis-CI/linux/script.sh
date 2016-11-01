#!/bin/bash
set -ev

pwd
cd OverwatchPlayerLog

qmake OverwatchPlayerLog.pro -r "CONFIG+=debug"
make

make clean
qmake OverwatchPlayerLog.pro -r
make

qmake OverwatchPlayerLog_UnitTest.pro -r
make