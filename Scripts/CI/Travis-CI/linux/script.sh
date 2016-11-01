#!/bin/bash
set -ev

pwd
cd OverwatchPlayerLog

qmake OverwatchPlayerLog.pro -r "CONFIG+=debug"
make

qmake OverwatchPlayerLog_UnitTest.pro -r
make