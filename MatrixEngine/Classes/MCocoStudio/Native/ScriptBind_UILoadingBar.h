
#ifndef __SCRIPTBIND_UILOADINGBAR__
#define __SCRIPTBIND_UILOADINGBAR__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::LoadingBar;

class ScriptBind_UILoadingBar:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UILoadingBar();
	~ScriptBind_UILoadingBar();

	virtual const char* GetClassName(){ return "NativeUILoadingBar";}

	static cocos2d::ui::LoadingBar* Create();

	static void SetDirection(cocos2d::ui::LoadingBar* bar, int dir);

	static int GetDirection(cocos2d::ui::LoadingBar* bar);

	static void LoadTexture(cocos2d::ui::LoadingBar* bar,mono::string texture,cocos2d::ui::TextureResType textType);

	static void SetPercent(cocos2d::ui::LoadingBar* bar,int percent);

	static int GetPercent(cocos2d::ui::LoadingBar* bar);

	//static void SetScale9Enabled(bool enabled);

	static void SetCapInsets(cocos2d::ui::LoadingBar* bar,float x, float y, float width, float height);
protected:
private:
};


#endif