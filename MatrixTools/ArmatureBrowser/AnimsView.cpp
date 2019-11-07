#include "AnimsView.h"
#include "ActionConnect.h"
#include "ShareWidgets.h"

#include <QtGlobal>
#include <qevent.h>

AnimsView::AnimsView(QWidget *parent /* = 0 */ )
	:QWidget(parent)
{
	ui.setupUi(this);

	animList = ui.listWidget;

	connect(animList,SIGNAL(itemClicked ( QListWidgetItem * )),this,SLOT( playAnimation(QListWidgetItem *) ));
	connect(animList,SIGNAL(itemSelectionChanged ( )),this,SLOT( itemChanged() ));
	//
	ShareWidgets::shareWidgets()->setAnimsView(this);
}

AnimsView::~AnimsView()
{

}

void AnimsView::resetList(std::vector<std::string> movements)
{
	animList->clear();
	for (int i = 0;i<movements.size();i++)
	{
		animList->addItem(QString(movements[i].c_str()));
	}
}

void AnimsView::resizeEvent(QResizeEvent * event)
{
	QSize size = event->size();
	//armatureList->resize(size.width(),size.height());
	ui.verticalLayoutWidget->resize(size);
}

void AnimsView::playAnimation(QListWidgetItem * item)
{
	QString name = item->text();

	if(curAnim.compare(name.toStdString()))
	{
		curAnim = name.toStdString();
		ActionConnect::shareActionConnect()->playAnimation(curAnim,true);
	}
}

void AnimsView::itemChanged()
{
	ActionConnect* actionConnect = ActionConnect::shareActionConnect();
	QModelIndex index = animList->currentIndex();
	std::vector<std::string> animList = actionConnect->getMovementList(actionConnect->getCurArmature());

	if(animList.size() > 0 && index.row()<animList.size())
	{
		std::string anim = animList[index.row()];
		if(curAnim.compare(anim))
		{
			curAnim = anim;
			actionConnect->playAnimation(curAnim,true);
		}
	}

}