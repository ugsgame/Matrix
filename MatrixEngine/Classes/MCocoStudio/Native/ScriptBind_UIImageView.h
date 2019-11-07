
#ifndef __SCRIPTBIND_UIIMAGEVIEW__
#define __SCRIPTBIND_UIIMAGEVIEW__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::ImageView;

class ScriptBind_UIImageView:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UIImageView();
	~ScriptBind_UIImageView();

	virtual const char* GetClassName(){ return "NativeUIImageView"; }

	static cocos2d::ui::ImageView* Create();

	static void LoadTexture(cocos2d::ui::ImageView* imagewiew,mono::string fileName,cocos2d::ui::TextureResType textType);
	static void SetTextureRect(cocos2d::ui::ImageView* imagewiew,float x,float y,float w,float h);

	static void SetScale9Enabled(cocos2d::ui::ImageView* imagewiew,bool able);
protected:
private:
};

#endif