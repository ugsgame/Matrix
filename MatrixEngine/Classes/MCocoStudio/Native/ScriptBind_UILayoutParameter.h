
#ifndef __SCRIPTBIND_UILAYOUTPARAMETER__
#define __SCRIPTBIND_UILAYOUTPARAMETER__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::LayoutParameter;

class ScriptBind_UILayoutParameter: public ScriptBind_CocoStudio
{
public:
	ScriptBind_UILayoutParameter();
	~ScriptBind_UILayoutParameter();

	virtual const char* GetClassName(){ return "NativeUILayoutParameter"; };

	static cocos2d::ui::LayoutParameter* Create();

	static void SetMargin(cocos2d::ui::LayoutParameter* layout ,mono::object obj);
	//static mono::object GetMargin(cocos2d::ui::LayoutParameter* layout);

	static int GetLayoutType(cocos2d::ui::LayoutParameter* layout);
};

#endif