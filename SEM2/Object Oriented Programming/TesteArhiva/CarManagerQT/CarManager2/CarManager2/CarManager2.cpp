#include "CarManager2.h"
#include <QMessageBox>

CarManager2::CarManager2(Service service,QWidget *parent)
    : QMainWindow(parent),service{service}
{
    ui.setupUi(this);
    this->populateList();
    this->useSignals();
}

CarManager2::~CarManager2()
{}

void CarManager2::populateList()
{
    this->ui.carsList->clear();
    
    for (int i = 0; i < this->service.getAll().size(); i++)
    {
        this->ui.carsList->addItem(QString::fromStdString(this->service.getAll()[i].toString()));
        this->ui.carsList->item(i)->setBackground(QColor(QString::fromStdString(this->service.getAll()[i].getColor())));
        this->ui.carsList->item(i)->setFont(QFont(".", 10, QFont::Bold));
    }

}

void CarManager2::useSignals()
{
    QObject::connect(this->ui.manufacturerButton,&QPushButton::clicked,this,&CarManager2::populateManufacturer);
    QObject::connect(this->ui.yearButton, &QPushButton::clicked, this, &CarManager2::populateYear);
    QObject::connect(this->ui.grey, &QCheckBox::stateChanged, this, &CarManager2::populateCheckBox);
    QObject::connect(this->ui.blue, &QCheckBox::stateChanged, this, &CarManager2::populateCheckBox);
    QObject::connect(this->ui.red, &QCheckBox::stateChanged, this, &CarManager2::populateCheckBox);
    QObject::connect(this->ui.addButton, &QPushButton::clicked, this, &CarManager2::add);
}

void CarManager2::populateManufacturer()
{
    this->ui.carsList->clear();
    string manufacturer = this->ui.manufacturerLineEdit->text().toStdString();

    for (auto car : this->service.getManufacturer(manufacturer))
    {
        this->ui.carsList->addItem(QString::fromStdString(car.toString()));
    }
}

void CarManager2::populateCheckBox()
{
    this->ui.carsList->clear();
    vector<string> colors;
    if (this->ui.blue->isChecked())
        colors.push_back("blue");
    if (this->ui.grey->isChecked())
        colors.push_back("grey");
    if (this->ui.red->isChecked())
        colors.push_back("red");

    vector<Car> cars=this->service.getFiltered(colors);

    for (auto car : cars)
        this->ui.carsList->addItem(QString::fromStdString(car.toString()));


}

void CarManager2::populateYear()
{
    this->ui.carsList->clear();

    for (auto car : this->service.getAll())
    {
        if (car.getYear() > this->ui.yearSlider->value())
            this->ui.carsList->addItem(QString::fromStdString(car.toString()));
    }
}

void CarManager2::add()
{
    string brand = this->ui.brandLineEdit->text().toStdString();
    string model = this->ui.modelLineEdit->text().toStdString();
    int year = stoi(this->ui.yearLineEdit->text().toStdString());
    string color = this->ui.colorLineEdit->text().toStdString();

    if (this->service.add(Car(brand, model, year, color)) == false)
        QMessageBox::critical(this, "Error", "already exists");
    else {
        this->service.add(Car(brand, model, year, color));

        int indexRow = this->service.getAll().size() - 1;
        this->ui.carsList->setCurrentRow(indexRow);

        this->populateList();
    }
}
