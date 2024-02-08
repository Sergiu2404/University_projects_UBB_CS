#include "Weather.h"
#include <algorithm>

Weather::Weather(Service service,QWidget *parent)
    : QMainWindow(parent),service{service}
{
    ui.setupUi(this);
    this->populateList();
    this->useSignals();
}

Weather::~Weather()
{}

bool compare(Precipitation p1, Precipitation p2) {
    return p1.getEnd() < p2.getEnd();
}

void Weather::populateList()
{
    this->ui.weatherList->clear();
    int index = 0;

    vector<Precipitation> sorted=this->service.getAll();
    sort(sorted.begin(), sorted.end(), compare);

    for (Precipitation it : sorted)
    {
        this->ui.weatherList->addItem(QString::fromStdString(it.representation()));
        if (it.getDescription() == "overcast")
        {
            this->ui.weatherList->item(index)->setBackground(QColor(QString::fromStdString("grey")));
            this->ui.weatherList->item(index)->setFont(QFont(".", 10, QFont::Bold));
        }
        index++;
    }
    
}

void Weather::useSignals()
{
    QObject::connect(this->ui.searchButton,&QPushButton::clicked,this,&Weather::populateFilteredList);
    QObject::connect(this->ui.checkBox, &QCheckBox::stateChanged, this, &Weather::populateFilteredBox);
    QObject::connect(this->ui.checkBox_2, &QCheckBox::stateChanged, this, &Weather::populateFilteredBox);
    QObject::connect(this->ui.checkBox_3, &QCheckBox::stateChanged, this, &Weather::populateFilteredBox);
    QObject::connect(this->ui.checkBox_4, &QCheckBox::stateChanged, this, &Weather::populateFilteredBox);
    QObject::connect(this->ui.checkBox_5, &QCheckBox::stateChanged, this, &Weather::populateFilteredBox);
}

void Weather::populateFilteredList()
{
    this->ui.filteredList->clear();
    int value=this->ui.horizontalSlider->value();

    vector<Precipitation> sorted = this->service.getAll();
    sort(sorted.begin(), sorted.end(), compare);

    for (Precipitation it : sorted)
    {
        if (it.getProbability() < value) 
            this->ui.filteredList->addItem(QString::fromStdString(it.representation()));
    }
}

void Weather::populateFilteredBox()
{
    this->ui.filteredList->clear();

    vector<string> result;

    if (this->ui.checkBox->isChecked())
        result.push_back("sunny");
    if (this->ui.checkBox_2->isChecked())
        result.push_back("rain");
    if (this->ui.checkBox_3->isChecked())
        result.push_back("foggy");
    if (this->ui.checkBox_4->isChecked())
        result.push_back("overcast");
    if (this->ui.checkBox_5->isChecked())
        result.push_back("thunderstorm");

    vector<Precipitation> sorted = this->service.getAllDescription(result);
    sort(sorted.begin(), sorted.end(), compare);

    for (auto it : sorted)
        this->ui.filteredList->addItem(QString::fromStdString(it.representation()));
}
