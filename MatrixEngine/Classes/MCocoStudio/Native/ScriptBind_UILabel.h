
#ifndef __SCRIPTBIND_UILABEL__
#define __SCRIPTBIND_UILABEL__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::Label;

class ScriptBind_UILabel:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UILabel();
	~ScriptBind_UILabel();

	virtual const char* GetClassName(){ return "NativeUILabel";}
	
	static cocos2d::ui::Label* Create();
	
	static void SetText(cocos2d::ui::Label* label,mono::string text);
	static mono::string GetText(cocos2d::ui::Label* label);

	static int GetStringLength(cocos2d::ui::Label* label);

	static void SetFontSize(cocos2d::ui::Label* label,int size);
	static int  GetFontSize(cocos2d::ui::Label* label);

	static void SetFontName(cocos2d::ui::Label* label,mono::string name);
	static mono::string GetFontName(cocos2d::ui::Label* label);

	static void SetTouchScaleChangeEnabled(cocos2d::ui::Label* label,bool enabled);
	static bool IsTouchScaleChangeEnabled(cocos2d::ui::Label* label);

	static void SetTextAreaSize(cocos2d::ui::Label* label,float w,float h);
	static void SetTextHorizontalAlignment(cocos2d::ui::Label* label,cocos2d::CCTextAlignment alignment);
	static void SetTextVerticalAlignment(cocos2d::ui::Label* label,cocos2d::CCVerticalTextAlignment alignment);
protected:
private:
};

#endif