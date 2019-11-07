
#ifndef __SCRIPTBIND_UILABELATLAS__
#define __SCRIPTBIND_UILABELATLAS__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::LabelAtlas;

class ScriptBind_UILabelAtlas:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UILabelAtlas();
	~ScriptBind_UILabelAtlas();

	virtual const char* GetClassName(){ return "NativeUILabelAtlas";}

	static cocos2d::ui::LabelAtlas* Create();

	static void SetProperty(cocos2d::ui::LabelAtlas* label, mono::string stringValue,mono::string charMapFile, int itemWidth, int itemHeight, mono::string  startCharMap); 

	static void SetText(cocos2d::ui::LabelAtlas* label,mono::string stringValue);
	static mono::string GetText(cocos2d::ui::LabelAtlas* label);
protected:
private:
};


#endif