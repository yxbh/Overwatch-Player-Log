#!/bin/bash
set -ev

sudo add-apt-repository ppa:beineri/opt-qt57-trusty -y;
sudo apt-get update -qq

# # install newer compiler
# sudo add-apt-repository ppa:ubuntu-toolchain-r/test -y
# sudo apt-get update
# sudo apt-get install gcc-6 g++-6
# # set as default
# sudo update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-6 60 --slave /usr/bin/g++ g++ /usr/bin/g++-6

# # add repo for Qt
# sudo add-apt-repository --yes ppa:ubuntu-sdk-team/ppa -y
# sudo apt-get update -qq