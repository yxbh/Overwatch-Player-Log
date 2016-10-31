#!/bin/bash
set -ev

pwd
ls -l
cd OverwatchPlayerLog
ls -l
qmake OverwatchPlayerLog.pro -r "CONFIG+=debug"
ls -l
make
qmake OverwatchPlayerLog_UnitTest.pro -r
make