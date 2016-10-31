#!/bin/bash
set -ev

# Install from homebrew
brew outdated cmake || brew upgrade cmake
brew install qt5 p7zip ninja lcov

# Install other dependencies
pushd dependencies

export PATH=/usr/local/opt/qt5/bin:$PATH

popd