
#ifndef __SCRIPTBIND_GUIREADER__
#define __SCRIPTBIND_GUIREADER__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::Widget;

class ScriptBind_GUIReader:public ScriptBind_CocoStudio
{
public:
	ScriptBind_GUIReader();
	~ScriptBind_GUIReader();

	virtual const char* GetClassName(){ return "NativeGUIReader"; };

	static cocos2d::ui::Widget* WidgetFromJsonFile(mono::string fileName);
};

#endif