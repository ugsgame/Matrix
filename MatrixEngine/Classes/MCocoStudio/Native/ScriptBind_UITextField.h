
#ifndef __SCRIPTBIND_UITEXTFIELD__
#define __SCRIPTBIND_UITEXTFIELD__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::TextField;

class ScriptBind_UITextField:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UITextField();
	~ScriptBind_UITextField();

	virtual const char* GetClassName(){ return "NativeUITextField";}

	static cocos2d::ui::TextField* Create();

	static void SetTouchSize(cocos2d::ui::TextField* textfile, float w,float h);

	static void SetText(cocos2d::ui::TextField* textfile,mono::string text);

	static void SetPlaceHolder(cocos2d::ui::TextField* textfile,mono::string value);

	static void SetFontSize(cocos2d::ui::TextField* textfile,int size);

	static void SetFontName(cocos2d::ui::TextField* textfile,mono::string name);

	static void DidNotSelectSelf(cocos2d::ui::TextField* textfile);

	static mono::string GetStringValue(cocos2d::ui::TextField* textfile);

	static void SetMaxLengthEnabled(cocos2d::ui::TextField* textfile,bool enable);
	static bool IsMaxLengthEnabled(cocos2d::ui::TextField* textfile);

	static void SetMaxLength(cocos2d::ui::TextField* textfile,int length);
	static int  GetMaxLength(cocos2d::ui::TextField* textfile);

	static void SetPasswordEnabled(cocos2d::ui::TextField* textfile,bool enable);
	static bool IsPasswordEnabled(cocos2d::ui::TextField* textfile);

	static void SetPasswordStyleText(cocos2d::ui::TextField* textfile,mono::string styleText);
	static bool GetAttachWithIME(cocos2d::ui::TextField* textfile);

	static void SetAttachWithIME(cocos2d::ui::TextField* textfile,bool attach);

	static bool GetDetachWithIME(cocos2d::ui::TextField* textfile);

	static void SetDetachWithIME(cocos2d::ui::TextField* textfile,bool detach);
	static bool GetInsertText(cocos2d::ui::TextField* textfile);

	static void SetInsertText(cocos2d::ui::TextField* textfile,bool insertText);
	static bool GetDeleteBackward(cocos2d::ui::TextField* textfile);

	static void SetDeleteBackward(cocos2d::ui::TextField* textfile,bool deleteBackward);

	static void AddEventListenerTextField(cocos2d::ui::TextField* textfile,mono::object target);

protected:
private:
};


#endif