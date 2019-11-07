
#ifndef __OBJECTSWIDGET_H__
#define __OBJECTSWIDGET_H__

#include <QWidget>
#include "ui_ObjectsView.h"

class ObjectsWidget:public QWidget
{
	Q_OBJECT
public:
	ObjectsWidget(QWidget *parent = 0);
	~ObjectsWidget();
protected:
private:
	Ui::ObjectsViewClass ui;
};

#endif