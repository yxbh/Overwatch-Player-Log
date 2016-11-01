#!/bin/bash
set -ev

sudo apt-get install -qq qt57base

cat /opt/qt57/bin/qt57-env.sh

echo 'bash /opt/qt57/bin/qt57-env.sh'