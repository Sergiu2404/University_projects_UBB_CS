#include "Test.h"
#include <QtWidgets/QApplication>
#include "Service.h"
int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    Service s;
    Test ui{ s };
    ui.show();
    return a.exec();
}
