
#ifndef __SCRIPTBIND_UISCROLLVIEW__
#define __SCRIPTBIND_UISCROLLVIEW__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::ScrollView;
class cocos2d::ui::Layout;

class cocos2d::CCSize;

class ScriptBind_UIScrollView:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UIScrollView();
	~ScriptBind_UIScrollView();

	virtual const char* GetClassName(){ return "NativeUIScrollView";}

	static cocos2d::ui::ScrollView* Create();

	static void SetDirection(cocos2d::ui::ScrollView* srollView,int dir);

	static int GetDirection(cocos2d::ui::ScrollView* srollView);

	static cocos2d::ui::Layout* GetInnerContainer(cocos2d::ui::ScrollView* srollView);

	static void ScrollToBottom(cocos2d::ui::ScrollView* srollView,float time, bool attenuated);

	static void ScrollToTop(cocos2d::ui::ScrollView* srollView,float time, bool attenuated);

	static void ScrollToLeft(cocos2d::ui::ScrollView* srollView,float time, bool attenuated);

	static void ScrollToRight(cocos2d::ui::ScrollView* srollView,float time, bool attenuated);

	static void ScrollToTopLeft(cocos2d::ui::ScrollView* srollView,float time, bool attenuated);

	static void ScrollToTopRight(cocos2d::ui::ScrollView* srollView,float time, bool attenuated);

	static void ScrollToBottomLeft(cocos2d::ui::ScrollView* srollView,float time, bool attenuated);

	static void ScrollToBottomRight(cocos2d::ui::ScrollView* srollView,float time, bool attenuated);

	static void ScrollToPercentVertical(cocos2d::ui::ScrollView* srollView,float percent, float time, bool attenuated);

	static void ScrollToPercentHorizontal(cocos2d::ui::ScrollView* srollView,float percent, float time, bool attenuated);

	static void ScrollToPercentBothDirection(cocos2d::ui::ScrollView* srollView,float percentX,float percentY, float time, bool attenuated);

	static void JumpToBottom(cocos2d::ui::ScrollView* srollView);

	static void JumpToTop(cocos2d::ui::ScrollView* srollView);

	static void JumpToLeft(cocos2d::ui::ScrollView* srollView);

	static void JumpToRight(cocos2d::ui::ScrollView* srollView);

	static void JumpToTopLeft(cocos2d::ui::ScrollView* srollView);

	static void JumpToTopRight(cocos2d::ui::ScrollView* srollView);

	static void JumpToBottomLeft(cocos2d::ui::ScrollView* srollView);

	static void JumpToBottomRight(cocos2d::ui::ScrollView* srollView);

	static void JumpToPercentVertical(cocos2d::ui::ScrollView* srollView,float percent);

	static void JumpToPercentHorizontal(cocos2d::ui::ScrollView* srollView,float percent);

	static void JumpToPercentBothDirection(cocos2d::ui::ScrollView* srollView,float percentX,float percentY);

	static void SetInnerContainerSize(cocos2d::ui::ScrollView* srollView, float w,float h);

	static void GetInnerContainerSize(cocos2d::ui::ScrollView* srollView,cocos2d::CCSize& size);

	static void AddEventListenerScrollView(cocos2d::ui::ScrollView* srollView,mono::object target);

	static void SetBounceEnabled(cocos2d::ui::ScrollView* srollView,bool enabled);

	static bool IsBounceEnabled(cocos2d::ui::ScrollView* srollView);

	static void SetInertiaScrollEnabled(cocos2d::ui::ScrollView* srollView,bool enabled);

	static bool IsInertiaScrollEnabled(cocos2d::ui::ScrollView* srollView);

	static void SetNeedOptimization(cocos2d::ui::ScrollView* srollView,bool optimization);

	static bool IsNeedOptimization(cocos2d::ui::ScrollView* srollView);
protected:
private:
};


#endif