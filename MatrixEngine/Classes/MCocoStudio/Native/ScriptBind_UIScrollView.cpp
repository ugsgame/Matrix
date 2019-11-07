
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "MatrixScriptHelper.h"
#include "ScriptBind_UIScrollView.h"

USING_NS_CC;
using namespace cocos2d::ui;

class ScrollEventListener:public CCObject
{
public:
	ScrollEventListener(){}
	~ScrollEventListener(){}
	static ScrollEventListener* Create(IMonoObject* obj)
	{
		ScrollEventListener* event = new ScrollEventListener();
		event->setMonoObject(obj);
		return event;
	}

	//TODO 回调c#函数中第一个参数 应该传入c#的对像，而不是指针

	void ScrollListener(CCObject *pSender, SliderEventType type)
	{
		CCAssert(p_MonoObject,"");
		//TODO
		//pScript->CallMethod("TouchEvent",IMonoObject object,(int)type);	
		p_MonoObject->CallMethod("ScrollListener",(int)type);	
	}
};

ScriptBind_UIScrollView::ScriptBind_UIScrollView()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetDirection);
	REGISTER_METHOD(GetDirection);
	REGISTER_METHOD(GetInnerContainer);
	REGISTER_METHOD(ScrollToBottom);
	REGISTER_METHOD(ScrollToTop);
	REGISTER_METHOD(ScrollToLeft);
	REGISTER_METHOD(ScrollToRight);
	REGISTER_METHOD(ScrollToTopLeft);
	REGISTER_METHOD(ScrollToTopRight);
	REGISTER_METHOD(ScrollToBottomLeft);
	REGISTER_METHOD(ScrollToBottomRight);
	REGISTER_METHOD(ScrollToPercentVertical);
	REGISTER_METHOD(ScrollToPercentHorizontal);
	REGISTER_METHOD(ScrollToPercentBothDirection);
	REGISTER_METHOD(JumpToBottom);
	REGISTER_METHOD(JumpToTop);
	REGISTER_METHOD(JumpToLeft);
	REGISTER_METHOD(JumpToRight);
	REGISTER_METHOD(JumpToTopLeft);
	REGISTER_METHOD(JumpToTopRight);
	REGISTER_METHOD(JumpToBottomLeft);
	REGISTER_METHOD(JumpToBottomRight);
	REGISTER_METHOD(JumpToPercentVertical);
	REGISTER_METHOD(JumpToPercentHorizontal);
	REGISTER_METHOD(JumpToPercentBothDirection);
	REGISTER_METHOD(SetInnerContainerSize);
	REGISTER_METHOD(GetInnerContainerSize);
	REGISTER_METHOD(AddEventListenerScrollView);
	REGISTER_METHOD(SetBounceEnabled);
	REGISTER_METHOD(IsBounceEnabled);
	REGISTER_METHOD(SetInertiaScrollEnabled);
	REGISTER_METHOD(IsInertiaScrollEnabled);
	REGISTER_METHOD(SetNeedOptimization);
	REGISTER_METHOD(IsNeedOptimization);
}
ScriptBind_UIScrollView::~ScriptBind_UIScrollView()
{

}

ScrollView* ScriptBind_UIScrollView::Create()
{
	return ScrollView::create();
}

void ScriptBind_UIScrollView::SetDirection(ScrollView* srollView,int dir)
{
	srollView->setDirection((SCROLLVIEW_DIR)dir);
}

int ScriptBind_UIScrollView::GetDirection(ScrollView* srollView)
{
	return (int)srollView->getDirection();
}

Layout* ScriptBind_UIScrollView::GetInnerContainer(ScrollView* srollView)
{
	return srollView->getInnerContainer();
}

void ScriptBind_UIScrollView::ScrollToBottom(ScrollView* srollView,float time, bool attenuated)
{
	srollView->scrollToBottom(time,attenuated);
}

void ScriptBind_UIScrollView::ScrollToTop(ScrollView* srollView,float time, bool attenuated)
{
	srollView->scrollToTop(time,attenuated);
}

void ScriptBind_UIScrollView::ScrollToLeft(ScrollView* srollView,float time, bool attenuated)
{
	srollView->scrollToBottomLeft(time,attenuated);
}

void ScriptBind_UIScrollView::ScrollToRight(ScrollView* srollView,float time, bool attenuated)
{
	srollView->scrollToRight(time,attenuated);
}

void ScriptBind_UIScrollView::ScrollToTopLeft(ScrollView* srollView,float time, bool attenuated)
{
	srollView->scrollToTopLeft(time,attenuated);
}

void ScriptBind_UIScrollView::ScrollToTopRight(ScrollView* srollView,float time, bool attenuated)
{
	srollView->scrollToTopRight(time,attenuated);
}

void ScriptBind_UIScrollView::ScrollToBottomLeft(ScrollView* srollView,float time, bool attenuated)
{
	srollView->scrollToBottomLeft(time,attenuated);
}

void ScriptBind_UIScrollView::ScrollToBottomRight(ScrollView* srollView,float time, bool attenuated)
{
	srollView->scrollToBottomRight(time,attenuated);
}

void ScriptBind_UIScrollView::ScrollToPercentVertical(ScrollView* srollView,float percent, float time, bool attenuated)
{
	srollView->scrollToPercentVertical(percent,time,attenuated);
}

void ScriptBind_UIScrollView::ScrollToPercentHorizontal(ScrollView* srollView,float percent, float time, bool attenuated)
{
	srollView->scrollToPercentHorizontal(percent,time,attenuated);
}

void ScriptBind_UIScrollView::ScrollToPercentBothDirection(ScrollView* srollView,float percentX,float percentY, float time, bool attenuated)
{
	srollView->scrollToPercentBothDirection(ccp(percentX,percentY),time,attenuated);
}

void ScriptBind_UIScrollView::JumpToBottom(ScrollView* srollView)
{
	srollView->jumpToBottom();
}

void ScriptBind_UIScrollView::JumpToTop(ScrollView* srollView)
{
	srollView->jumpToTop();
}

void ScriptBind_UIScrollView::JumpToLeft(ScrollView* srollView)
{
	srollView->jumpToLeft();
}

void ScriptBind_UIScrollView::JumpToRight(ScrollView* srollView)
{
	srollView->jumpToRight();
}

void ScriptBind_UIScrollView::JumpToTopLeft(ScrollView* srollView)
{
	srollView->jumpToTopLeft();
}

void ScriptBind_UIScrollView::JumpToTopRight(ScrollView* srollView)
{
	srollView->jumpToRight();
}

void ScriptBind_UIScrollView::JumpToBottomLeft(ScrollView* srollView)
{
	srollView->jumpToBottomLeft();
}

void ScriptBind_UIScrollView::JumpToBottomRight(ScrollView* srollView)
{
	srollView->jumpToBottomRight();
}

void ScriptBind_UIScrollView::JumpToPercentVertical(ScrollView* srollView,float percent)
{
	srollView->jumpToPercentVertical(percent);
}

void ScriptBind_UIScrollView::JumpToPercentHorizontal(ScrollView* srollView,float percent)
{
	srollView->jumpToPercentHorizontal(percent);
}

void ScriptBind_UIScrollView::JumpToPercentBothDirection(ScrollView* srollView,float percentX,float percentY)
{
	srollView->jumpToPercentBothDirection(ccp(percentX,percentY));
}

void ScriptBind_UIScrollView::SetInnerContainerSize(ScrollView* srollView, float w,float h)
{
	srollView->setInnerContainerSize(CCSize(ccp(w,h)));
}

void ScriptBind_UIScrollView::GetInnerContainerSize(ScrollView* srollView,cocos2d::CCSize& size)
{
	size = srollView->getInnerContainerSize();
}

void ScriptBind_UIScrollView::AddEventListenerScrollView(ScrollView* srollView,mono::object target)
{
	srollView->addEventListenerScrollView(ScrollEventListener::Create(*target),scrollvieweventselector(ScrollEventListener::ScrollListener));
}

void ScriptBind_UIScrollView::SetBounceEnabled(ScrollView* srollView,bool enabled)
{
	srollView->setBounceEnabled(enabled);
}

bool ScriptBind_UIScrollView::IsBounceEnabled(ScrollView* srollView)
{
	return srollView->isInertiaScrollEnabled();
}

void ScriptBind_UIScrollView::SetInertiaScrollEnabled(ScrollView* srollView,bool enabled)
{
	return srollView->setInertiaScrollEnabled(enabled);
}

bool ScriptBind_UIScrollView::IsInertiaScrollEnabled(ScrollView* srollView)
{
	return srollView->isInertiaScrollEnabled();
}

void ScriptBind_UIScrollView::SetNeedOptimization(ScrollView* srollView,bool optimization)
{
	srollView->setNeedOptimization(optimization);
}

bool ScriptBind_UIScrollView::IsNeedOptimization(ScrollView* srollView)
{
	return srollView->isNeedOptimization();
}