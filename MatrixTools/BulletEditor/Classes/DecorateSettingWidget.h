
#ifndef __DECORATESETTINGWIDGET_H__
#define __DECORATESETTINGWIDGET_H__

#include <QWidget>
#include "ui_DecorateSetting.h"

class DecorateSettingWidget:public QWidget
{
	Q_OBJECT
public:
	DecorateSettingWidget(QWidget *parent = 0);
	~DecorateSettingWidget();

protected:
private:
	Ui::DecorateSettingClass ui;
};

#endif
