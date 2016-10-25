#include <Libs/CatchUnitTest/Catch>
#include <QDebug>
#include <QEvent>
#include <QMouseEvent>
#include "QEventNameHelper.hpp"

TEST_CASE("QEventNameHelper", "QEventNameHelper")
{
    QMouseEvent mouseEvent(QEvent::MouseButtonPress, QPointF(), Qt::MouseButton::LeftButton, QFlags<Qt::MouseButton>(), Qt::NoModifier);
    REQUIRE(QEventNameHelper::toEventName(&mouseEvent) == "MouseButtonPress");

    QMouseEvent mouseEvent2(QEvent::MouseButtonRelease, QPointF(), Qt::MouseButton::LeftButton, QFlags<Qt::MouseButton>(), Qt::NoModifier);
    REQUIRE(QEventNameHelper::toEventName(&mouseEvent2) == "MouseButtonRelease");
}
