
#ifndef __SCRIPTBIND_UILAYER__
#define __SCRIPTBIND_UILAYER__

#include "ScriptBind_CocoStudio.h"

class MSUILayer;
class cocos2d::ui::Widget; 

class ScriptBind_UILayer:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UILayer();
	~ScriptBind_UILayer();

	virtual const char* GetClassName(){ return "NativeUILayer";}
	
	static MSUILayer* Create();

	static void AddWidget(MSUILayer* layer, cocos2d::ui::Widget* widget);
	static void RemoveWidget(MSUILayer* layer, cocos2d::ui::Widget* widget);
	static cocos2d::ui::Widget* GetWidgetByTag(MSUILayer* layer, int tag);
	static cocos2d::ui::Widget* GetWidgetByName(MSUILayer* layer, mono::string name);
	static cocos2d::ui::Widget* GetRootWidget(MSUILayer* layer);
	static float TickTime(MSUILayer* layer);
};

#endif

