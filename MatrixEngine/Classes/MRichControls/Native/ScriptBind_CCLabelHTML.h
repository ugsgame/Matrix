
#ifndef __SCRIPTBIND_CCLABELHTML__
#define __SCRIPTBIND_CCLABELHTML__

#include "ScriptBind_RichControls.h"

class cocos2d::extension::CCHTMLLabel;
class cocos2d::CCSize;

class ScriptBind_CCLabelHTML:public ScriptBind_RichControls
{
public:
	ScriptBind_CCLabelHTML();
	~ScriptBind_CCLabelHTML();

protected:
	virtual const char* GetClassName(){ return "NativeLabelHTML"; };

	static cocos2d::extension::CCHTMLLabel* Create();
	static cocos2d::extension::CCHTMLLabel* CreateWithString(mono::string utf8_str, cocos2d::CCSize& preferred_size, mono::string font_alias);

	static void SetString(cocos2d::extension::CCHTMLLabel* label,mono::string str);
	static mono::string GetString(cocos2d::extension::CCHTMLLabel* label);

	static void AppendString(cocos2d::extension::CCHTMLLabel* label,mono::string str);

	static void RegisterListener(cocos2d::extension::CCHTMLLabel* label,mono::object scriptHandler);
	//static void RemoveListener(cocos2d::extension::CCHTMLLabel* label,void* target);
private:
};

#endif

