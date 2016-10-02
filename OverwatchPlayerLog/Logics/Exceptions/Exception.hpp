#ifndef EXCEPTION_H
#define EXCEPTION_H
#include <QException>
#include <QString>

class Exception : public QException
{
private:
    QString whatText;

public:
    Exception(const QString & whatText) : whatText(whatText) {}

    void raise(void) const { throw *this; }
    Exception* clone(void) const { return new Exception(this->whatText); }

    virtual const char* what(void) const noexcept { return this->whatText.toStdString().c_str(); }
};

#endif // EXCEPTION_H
