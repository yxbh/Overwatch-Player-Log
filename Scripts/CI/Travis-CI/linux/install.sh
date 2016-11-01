#!/bin/bash
# set -ev

sudo apt-get install -qq qt57base

echo '#### begin script content ####'
cat /opt/qt57/bin/qt57-env.sh
echo '#### end script content ####'

# source /opt/qt57/bin/qt57-env.sh

# sudo apt-get install qtbase5-dev libsqlite3-dev libqt5svg5-dev
# sudo apt-get install qt5-default qttools5-dev-tools