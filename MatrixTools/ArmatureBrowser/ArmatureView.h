
#ifndef __QC_ARMATUREVIEW__
#define __QC_ARMATUREVIEW__

#include <QWidget>
#include <QMenu>
#include "ui_ArmatureView.h"

QT_BEGIN_NAMESPACE
class QListWidget;
QT_END_NAMESPACE

class ArmatureView:public QWidget 
{
	Q_OBJECT

public:
	ArmatureView(QWidget *parent = 0 );
	~ArmatureView();

	void resetList(std::vector<std::string> armatures);

protected:
	virtual void resizeEvent(QResizeEvent * event);

private  slots:
	void changeArmature(QListWidgetItem * item);
	void reloadArmature(QListWidgetItem * item);
	void contextListMenu(QContextMenuEvent *event);
private:

	Ui::ArmatureViewClass ui;

	QListWidget *armatureList;
};

#endif