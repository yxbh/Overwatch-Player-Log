#!/bin/bash
set -ev

# Install from homebrew
brew install qt5

# Install other dependencies
pushd dependencies

export PATH=/usr/local/opt/qt5/bin:$PATH

popd