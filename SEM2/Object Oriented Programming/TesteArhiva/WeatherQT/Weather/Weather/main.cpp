#include "Weather.h"
#include <QtWidgets/QApplication>
#include <QListWidget>
#include "Service.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    Service service;
    Weather ui{ service };
    ui.show();
    /*QListWidget list;
    for (Precipitation p : service.getAll())
        list.addItem(QString::fromStdString(p.representation()));

    list.show();*/
    return a.exec();
}
