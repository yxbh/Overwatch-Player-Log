#-------------------------------------------------
#
# Project created by QtCreator 2016-09-29T20:38:39
#
#-------------------------------------------------

QT       += core gui sql

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = OverwatchPlayerLog
TEMPLATE = app

#
# Version configuration
#
APP_VERSION_MAJOR = 1
APP_VERSION_MINOR = 0
APP_VERSION_PATCH = 0
VERSION = $${APP_VERSION_MAJOR}.$${APP_VERSION_MINOR}.$${APP_VERSION_PATCH}.000
#

#
# Organisation, product & copyright details.
#
QMAKE_TARGET_COMPANY = Infinity Box Studio
QMAKE_TARGET_PRODUCT = Overwatch Player Log
QMAKE_TARGET_DESCRIPTION = Overwatch Player Log is a simple utility application that allows you to record your favorite (or not!) players that you have met in Overwatch.
QMAKE_TARGET_COPYRIGHT = Copyright (c) 2016 Benjamin Huang.
#

#
# Configure release & debug build directories.
#   - DESTDIR:      binary output
#   - OBJECTS_DIR:  object files output
#   - MOC_DIR:      MOC generated source output
#   - RCC_DIR:      resource file(s) output
#   - UI_DIR:       UI source output
#
BUILDDIRWIN32DBG = ../Build/OPL-Win32-Debug
BUILDDIRWIN32REL = ../Build/OPL-Win32-Release
BUILDDIRUNIXDBG  = ../Build/OPL-Unix-Debug
BUILDDIRUNIXREL  = ../Build/OPL-Unix-Release
BUILDDIROSXDBG   = ../Build/OPL-OSX-Debug
BUILDDIROSXREL   = ../Build/OPL-OSX-Release
CONFIG(debug, debug|release) { # Debug build dirs
    win32:      DESTDIR     = $${BUILDDIRWIN32DBG}/
    win32:      OBJECTS_DIR = $${BUILDDIRWIN32DBG}/obj
    win32:      MOC_DIR     = $${BUILDDIRWIN32DBG}/moc
    win32:      RCC_DIR     = $${BUILDDIRWIN32DBG}/rsc
    win32:      UI_DIR      = $${BUILDDIRWIN32DBG}/ui
    macx:       DESTDIR     = $${BUILDDIROSXDBG}/
    macx:       OBJECTS_DIR = $${BUILDDIROSXDBG}/obj
    macx:       MOC_DIR     = $${BUILDDIROSXDBG}/moc
    macx:       RCC_DIR     = $${BUILDDIROSXDBG}/rsc
    macx:       UI_DIR      = $${BUILDDIROSXDBG}/ui
    unix!macx:  DESTDIR     = $${BUILDDIRUNIXDBG}/
    unix!macx:  OBJECTS_DIR = $${BUILDDIRUNIXDBG}/obj
    unix!macx:  MOC_DIR     = $${BUILDDIRUNIXDBG}/moc
    unix!macx:  RCC_DIR     = $${BUILDDIRUNIXDBG}/rsc
    unix!macx:  UI_DIR      = $${BUILDDIRUNIXDBG}/ui
}
CONFIG(release, debug|release) { # Release build dirs
    win32:      DESTDIR     = $${BUILDDIRWIN32REL}/
    win32:      OBJECTS_DIR = $${BUILDDIRWIN32REL}/obj
    win32:      MOC_DIR     = $${BUILDDIRWIN32REL}/moc
    win32:      RCC_DIR     = $${BUILDDIRWIN32REL}/rsc
    win32:      UI_DIR      = $${BUILDDIRWIN32REL}/ui
    macx:       DESTDIR     = $${BUILDDIROSXREL}/
    macx:       OBJECTS_DIR = $${BUILDDIROSXREL}/obj
    macx:       MOC_DIR     = $${BUILDDIROSXREL}/moc
    macx:       RCC_DIR     = $${BUILDDIROSXREL}/rsc
    macx:       UI_DIR      = $${BUILDDIROSXREL}/ui
    unix!macx:  DESTDIR     = $${BUILDDIRUNIXREL}/
    unix!macx:  OBJECTS_DIR = $${BUILDDIRUNIXREL}/obj
    unix!macx:  MOC_DIR     = $${BUILDDIRUNIXREL}/moc
    unix!macx:  RCC_DIR     = $${BUILDDIRUNIXREL}/rsc
    unix!macx:  UI_DIR      = $${BUILDDIRUNIXREL}/ui
}
#

#
# Source files.
#
SOURCES += \
    main.cpp\
    Controllers/MainWindow.cpp \
    App.cpp \
    Logics/Config.cpp \
    Logics/Entities/IEntity.cpp \
    Logics/Entities/OwPlayer.cpp \
    Logics/DataSources/IDataSource.cpp \
    Logics/DataSources/SqliteDataSource.cpp \
    Controllers/PlayerInfoPaneWidget.cpp \
    Logics/Exceptions/Exception.cpp \
    Models/OwPlayerItem.cpp \
    Extensions/Filters/WidgetStyleApplicatorEventFilter.cpp \
    Helpers/QEventNameHelper.cpp \
    Extensions/Filters/WidgetFocusHandlerEventFilter.cpp \
    Models/FavoriteOwPlayerSortFilterProxyModel.cpp \
    Controllers/PreferencesDialog.cpp \
    Controllers/AboutDialog.cpp

HEADERS  += \
    Controllers/MainWindow.hpp \
    App.hpp \
    Logics/Config.hpp \
    Logics/Entities/IEntity.hpp \
    Logics/Entities/OwPlayer.hpp \
    Logics/DataSources/IDataSource.hpp \
    Logics/DataSources/SqliteDataSource.hpp \
    Controllers/PlayerInfoPaneWidget.hpp \
    Logics/Exceptions/Exception.hpp \
    Models/OwPlayerItem.hpp \
    Extensions/Filters/WidgetStyleApplicatorEventFilter.hpp \
    Helpers/QEventNameHelper.hpp \
    Extensions/Filters/WidgetFocusHandlerEventFilter.hpp \
    Models/FavoriteOwPlayerSortFilterProxyModel.hpp \
    Controllers/PreferencesDialog.hpp \
    Controllers/AboutDialog.hpp

FORMS    += \
    UI/MainWindow.ui \
    UI/PlayerInfoPaneWidget.ui \
    UI/PreferencesDialog.ui \
    UI/AboutDialog.ui

DISTFILES += \
    Resources/OPL.config

RESOURCES += \
    resources.qrc
#

#
# App icon configuration.
#
macx {
    ICON = Resources/Icons/app-icon.icns
}
win32 {
    RC_ICONS = Resources/Icons/app-icon.ico
}
#

#
# Logic for generating a C++ version header file.
#
#
VERSION_GEN_CMD = dummyValue
win32 {
    VERSION_GEN_CMD = version-hpp-gen.bat $${APP_VERSION_MAJOR} $${APP_VERSION_MINOR} $${APP_VERSION_PATCH}
}
unix {
    VERSION_GEN_CMD = bash version-hpp-gen.sh $${APP_VERSION_MAJOR} $${APP_VERSION_MINOR} $${APP_VERSION_PATCH}
}
gen_version.commands = $${VERSION_GEN_CMD}
gen_version.depends = FORCE
QMAKE_EXTRA_TARGETS += gen_version
PRE_TARGETDEPS += gen_version
HEADERS  += version.hpp
#

#
# Read in build number for Windows rc file build number purpose.
# This requires qmake to be executed again each time before the rc file is updated with a newer build number.
# The build number will always be behind to the true build number by atleast 1 due to the fact that the resource
# object is compiled before the scripts are executed to replace the build number.
# (there's no way AFAIK to circumvent this with out using a manually generated rc file instead of the qmake generated one)
#
win32 {
    RC_INJECT_VERHEADER_CMD = rc-verheader-inject.bat \"$${TARGET}_resource.rc\" \"000\" \"build-no.txt\"
    rc_verheader_inject.commands = $${RC_INJECT_VERHEADER_CMD}
    rc_verheader_inject.depends = FORCE
    QMAKE_EXTRA_TARGETS += rc_verheader_inject
    PRE_TARGETDEPS += rc_verheader_inject
}
#

#
# Logic for generating a C++ app info header file.
#
#
APPINFO_GEN_CMD = dummyValue
win32 {
    APPINFO_GEN_CMD = powershell . .\appinfo-hpp-gen.ps1 -targetCompany \"\"\"$${QMAKE_TARGET_COMPANY}\"\"\" -targetProduct \"\"\"$${QMAKE_TARGET_PRODUCT}\"\"\" -targetDescription \"\"\"$${QMAKE_TARGET_DESCRIPTION}\"\"\" -targetCopyright \"\"\"$${QMAKE_TARGET_COPYRIGHT}\"\"\"
}
unix {
    APPINFO_GEN_CMD = bash appinfo-hpp-gen.sh \"$${QMAKE_TARGET_COMPANY}\" \"$${QMAKE_TARGET_PRODUCT}\" \"$${QMAKE_TARGET_DESCRIPTION}\" \"$${QMAKE_TARGET_COPYRIGHT}\"
}
gen_appinfo.commands = $${APPINFO_GEN_CMD}
gen_appinfo.depends = FORCE
QMAKE_EXTRA_TARGETS += gen_appinfo
PRE_TARGETDEPS += gen_appinfo
HEADERS  += appinfo.hpp
#
