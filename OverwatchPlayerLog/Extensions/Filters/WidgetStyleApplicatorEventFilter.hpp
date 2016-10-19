#ifndef WIDGETSTYLEAPPLICATOREVENTFILTER_H
#define WIDGETSTYLEAPPLICATOREVENTFILTER_H

#include <QObject>

class WidgetStyleApplicatorEventFilter : public QObject
{
    Q_OBJECT
public:
    explicit WidgetStyleApplicatorEventFilter(QObject *parent = 0);

    virtual bool eventFilter(QObject *watched, QEvent *event) override;

signals:

public slots:
};

#endif // WIDGETSTYLEAPPLICATOREVENTFILTER_H
