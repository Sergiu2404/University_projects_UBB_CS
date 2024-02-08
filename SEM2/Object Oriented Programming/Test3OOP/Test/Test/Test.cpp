#include "Test.h"

Test::Test(Service service,QWidget *parent)
    : QMainWindow(parent),service{service}
{
    ui.setupUi(this);

    this->populateList();
    this->useSignals();

}

Test::~Test()
{}

void Test::populateList()
{
    this->ui.shoppingList->clear();

    for (auto s : this->service.getAll())
        this->ui.shoppingList->addItem(QString::fromStdString(s.toString()));

}

void Test::useSignals()
{
    QObject::connect(this->ui.categoryButton, &QPushButton::clicked, this, &Test::populateCategory);
    //QObject::connect(this->ui.filterButton, &QPushButton::clicked, this, &Test::populateFiltered);
    QObject::connect(this->ui.filteredLineEdit, &QLineEdit::textChanged, this, &Test::populateFiltered);
}

void Test::populateCategory()
{
    this->ui.categoryList->clear();

    string category = this->ui.categoryLineEdit->text().toStdString();

    for (auto s : this->service.getCategory(category))
        this->ui.categoryList->addItem(QString::fromStdString(s.toString()));
}

void Test::populateFiltered()
{
    this->ui.shoppingList->clear();
    string substring = this->ui.filteredLineEdit->text().toStdString();

    for (auto s : this->service.getFiltered(substring))
        this->ui.shoppingList->addItem(QString::fromStdString(s.toString()));
}
