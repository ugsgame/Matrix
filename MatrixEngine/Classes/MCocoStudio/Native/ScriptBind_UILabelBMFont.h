
#ifndef __SCRIPTBIND_UILABELBMFONT__
#define __SCRIPTBIND_UILABELBMFONT__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::LabelBMFont;

class ScriptBind_UILabelBMFont:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UILabelBMFont();
	~ScriptBind_UILabelBMFont();

	virtual const char* GetClassName(){ return "NativeUILabelBMFont";}

	static cocos2d::ui::LabelBMFont* Create();

	static void SetFontFile(cocos2d::ui::LabelBMFont* label,mono::string fileName);
	static void SetText(cocos2d::ui::LabelBMFont* label,mono::string text);
	static mono::string GetText(cocos2d::ui::LabelBMFont* label);
protected:
private:
};


#endif