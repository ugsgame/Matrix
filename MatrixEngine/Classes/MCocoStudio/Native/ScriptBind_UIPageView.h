
#ifndef __SCRIPTBIND_UIPAGEVIEW__
#define __SCRIPTBIND_UIPAGEVIEW__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::Widget;
class cocos2d::ui::PageView;
class cocos2d::ui::Layout;


class ScriptBind_UIPageView:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UIPageView();
	~ScriptBind_UIPageView();

	virtual const char* GetClassName(){ return "NativeUIPageView";}

	static cocos2d::ui::PageView* Create();

	static void AddWidgetToPage(cocos2d::ui::PageView* pageView,cocos2d::ui::Widget* widget, int pageIdx, bool forceCreate);

	static void AddPage(cocos2d::ui::PageView* pageView,cocos2d::ui::Layout* page);

	static void InsertPage(cocos2d::ui::PageView* pageView,cocos2d::ui::Layout* page, int idx);

	static void RemovePage(cocos2d::ui::PageView* pageView,cocos2d::ui::Layout* page);

	static void RemovePageAtIndex(cocos2d::ui::PageView* pageView,int index);

	static void RemoveAllPages(cocos2d::ui::PageView* pageView);

	static void ScrollToPage(cocos2d::ui::PageView* pageView,int idx);

	static int GetCurPageIndex(cocos2d::ui::PageView* pageView);

	//CCArray* getPages();

	static cocos2d::ui::Layout* GetPage(cocos2d::ui::PageView* pageView,int index);

	static void AddEventListenerPageView(cocos2d::ui::PageView* pageView,mono::object target);

	static void SetNeedOptimization(cocos2d::ui::PageView* pageView,bool optimization);

	static bool IsNeedOptimization(cocos2d::ui::PageView* pageView);
protected:
private:
};


#endif