#ifndef __QC_SHAREWIDGETS__
#define __QC_SHAREWIDGETS__

class AnimsView;
class ArmatureView;

class ShareWidgets
{
public:
	ShareWidgets();
	~ShareWidgets();

	static ShareWidgets* shareWidgets();

	//set widgets
	void setAnimsView(AnimsView* view);
	void setArmatureView(ArmatureView* view);
	//get widgets
	AnimsView* getAnimsView();
	ArmatureView* getArmatureView();

protected:

	AnimsView* animsView;
	ArmatureView* armatureView;

private:

	static ShareWidgets* _shareWidgets;
};

#endif