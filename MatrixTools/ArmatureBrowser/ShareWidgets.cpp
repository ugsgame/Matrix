
#include "ShareWidgets.h"

#include "AnimsView.h"
#include "ArmatureView.h"

ShareWidgets * ShareWidgets::_shareWidgets = 0;

ShareWidgets::ShareWidgets()
{

}

ShareWidgets::~ShareWidgets()
{

}

ShareWidgets* ShareWidgets::shareWidgets()
{
	if(!_shareWidgets)
	{
		_shareWidgets = new ShareWidgets();
	}
	return _shareWidgets;
}

void ShareWidgets::setAnimsView(AnimsView* view)
{
	animsView = view;
}

void ShareWidgets::setArmatureView(ArmatureView* view)
{
	armatureView = view;
}


AnimsView* ShareWidgets::getAnimsView()
{
	return animsView;
}

ArmatureView* ShareWidgets::getArmatureView()
{
	return armatureView;
}