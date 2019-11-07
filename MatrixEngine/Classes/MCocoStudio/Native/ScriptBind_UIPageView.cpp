
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "ScriptBind_UIPageView.h"

USING_NS_CC;
using namespace cocos2d::ui;


class PageEventListener:public CCObject
{
public:
	PageEventListener(){}
	~PageEventListener(){}
	static PageEventListener* Create(IMonoObject* obj)
	{
		PageEventListener* event = new PageEventListener();
		event->setMonoObject(obj);
		return event;
	}

	//TODO 回调c#函数中第一个参数 应该传入c#的对像，而不是指针

	void PageListener(CCObject *pSender, PageViewEventType type)
	{
		CCAssert(p_MonoObject,"");
		//TODO
		//pScript->CallMethod("TouchEvent",IMonoObject object,(int)type);	
		p_MonoObject->CallMethod("PageListener",(int)type);	
	}
};

ScriptBind_UIPageView::ScriptBind_UIPageView()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(AddWidgetToPage);
	REGISTER_METHOD(AddPage);
	REGISTER_METHOD(InsertPage);
	REGISTER_METHOD(RemovePage);
	REGISTER_METHOD(RemoveAllPages);
	REGISTER_METHOD(ScrollToPage);
	REGISTER_METHOD(GetCurPageIndex);
	REGISTER_METHOD(GetPage);
	REGISTER_METHOD(AddEventListenerPageView);
	REGISTER_METHOD(SetNeedOptimization);
	REGISTER_METHOD(IsNeedOptimization);
}
ScriptBind_UIPageView::~ScriptBind_UIPageView()
{

}


PageView* ScriptBind_UIPageView::Create()
{
	return PageView::create();
}

void ScriptBind_UIPageView::AddWidgetToPage(PageView* pageView,Widget* widget, int pageIdx, bool forceCreate)
{
	pageView->addWidgetToPage(widget,pageIdx,forceCreate);
}

void ScriptBind_UIPageView::AddPage(PageView* pageView,Layout* page)
{
	pageView->addPage(page);
}

void ScriptBind_UIPageView::InsertPage(PageView* pageView,Layout* page, int idx)
{
	pageView->insertPage(page,idx);
}

void ScriptBind_UIPageView::RemovePage(PageView* pageView,Layout* page)
{
	pageView->removePage(page);
}

void ScriptBind_UIPageView::RemovePageAtIndex(PageView* pageView,int index)
{
	pageView->removePageAtIndex(index);
}

void ScriptBind_UIPageView::RemoveAllPages(PageView* pageView)
{
	pageView->removeAllPages();
}

void ScriptBind_UIPageView::ScrollToPage(PageView* pageView,int idx)
{
	pageView->scrollToPage(idx);
}

int ScriptBind_UIPageView::GetCurPageIndex(PageView* pageView)
{
	return pageView->getCurPageIndex();
}

//CCArray* getPages();

Layout* ScriptBind_UIPageView::GetPage(PageView* pageView,int index)
{
	return pageView->getPage(index);
}

void ScriptBind_UIPageView::AddEventListenerPageView(PageView* pageView,mono::object target)
{
	pageView->addEventListenerPageView(PageEventListener::Create(*target),pagevieweventselector(PageEventListener::PageListener));
}

void ScriptBind_UIPageView::SetNeedOptimization(PageView* pageView,bool optimization)
{
	pageView->setNeedOptimization(optimization);
}

bool ScriptBind_UIPageView::IsNeedOptimization(PageView* pageView)
{
	return pageView->isNeedOptimization();
}