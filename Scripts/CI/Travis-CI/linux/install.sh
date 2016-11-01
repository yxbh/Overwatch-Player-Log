#!/bin/bash
set -ev

sudo apt-get install -qq qt57base

echo '#### begin script content ####'
cat /opt/qt57/bin/qt57-env.sh
echo '#### end script content ####'

echo $0
# source /opt/qt57/bin/qt57-env.sh
echo 'source /opt/qt57/bin/qt57-env.sh'