#include "ArmatureView.h"
#include "AnimsView.h"
#include "ActionConnect.h"
#include "ShareWidgets.h"

#include <qevent.h>

ArmatureView::ArmatureView(QWidget *parent /* = 0 */ )
	:QWidget(parent)
{
	ui.setupUi(this);

	armatureList = ui.listWidget;

	connect(armatureList,SIGNAL(itemClicked ( QListWidgetItem * )),this,SLOT( changeArmature(QListWidgetItem *) ));
	connect(armatureList,SIGNAL(itemDoubleClicked ( QListWidgetItem * )),this,SLOT( reloadArmature(QListWidgetItem *) ));
	//connect(armatureList,SIGNAL(DefaultContextMenu( QContextMenuEvent  * )),this,SLOT( contextListMenu(QContextMenuEvent  *) ));

	ShareWidgets::shareWidgets()->setArmatureView(this);
}

ArmatureView::~ArmatureView()
{

}

void ArmatureView::resetList(std::vector<std::string> armatures)
{
	armatureList->clear();
	for (int i = 0;i<armatures.size();i++)
	{
		armatureList->addItem(QString(armatures[i].c_str()));
	}
	
}

void ArmatureView::resizeEvent(QResizeEvent * event)
{
	QSize size = event->size();
	//armatureList->resize(size.width(),size.height());
	ui.verticalLayoutWidget->resize(size);
}

void ArmatureView::changeArmature(QListWidgetItem * item)
{
	QString name = item->text();
	ActionConnect* actionConnect = ActionConnect::shareActionConnect();

	if (actionConnect->getCurArmature().compare(name.toStdString()))
	{
		actionConnect->changeArmature(name.toStdString());
		(ShareWidgets::shareWidgets()->getAnimsView())->resetList(actionConnect->getMovementList(name.toStdString()));
	}
}

void ArmatureView::reloadArmature(QListWidgetItem * item)
{
	QString name = item->text();
	ActionConnect* actionConnect = ActionConnect::shareActionConnect();

	actionConnect->openArmature(actionConnect->getArmatureFile(name.toStdString()));
	(ShareWidgets::shareWidgets()->getAnimsView())->resetList(actionConnect->getMovementList(name.toStdString()));

}

void ArmatureView::contextListMenu(QContextMenuEvent *event)
{
}
