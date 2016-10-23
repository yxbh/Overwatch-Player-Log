#-------------------------------------------------
#
# Project created by QtCreator 2016-09-29T20:38:39
#
#-------------------------------------------------

QT       += core gui sql

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = OverwatchPlayerLogger
TEMPLATE = app

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

target.path += $${DESTDIR}
target.files = Resources/OPL.config
INSTALLS += target

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
    Controllers/PreferencesDialog.cpp

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
    Controllers/PreferencesDialog.hpp

FORMS    += \
    UI/MainWindow.ui \
    UI/PlayerInfoPaneWidget.ui \
    UI/PreferencesDialog.ui

DISTFILES += \
    Resources/OPL.config

RESOURCES += \
    resources.qrc
