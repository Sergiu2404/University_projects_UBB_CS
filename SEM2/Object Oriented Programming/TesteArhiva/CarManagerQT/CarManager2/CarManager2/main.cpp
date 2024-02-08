#include "CarManager2.h"
#include <QtWidgets/QApplication>
#include "Service.h"
int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    Service s;
    CarManager2 ui{ s };
    ui.show();
    return a.exec();
}
