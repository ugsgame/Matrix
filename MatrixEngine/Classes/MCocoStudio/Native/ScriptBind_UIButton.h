
#ifndef __SCRIPTBIND_UIBUTTON__
#define __SCRIPTBIND_UIBUTTON__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::Button;
struct cocos2d::_ccColor3B;

class ScriptBind_UIButton:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UIButton();
	~ScriptBind_UIButton();

	virtual const char* GetClassName(){ return "NativeUIButton"; }

	static cocos2d::ui::Button* Create();

	static void LoadTextures(cocos2d::ui::Button* button,mono::string normal,mono::string selected,mono::string disabled,cocos2d::ui::TextureResType textType);
	static void LoadTextureNormal(cocos2d::ui::Button* button,mono::string norma,cocos2d::ui::TextureResType textType);
	static void LoadTexturePressed(cocos2d::ui::Button* button,mono::string selected,cocos2d::ui::TextureResType textType);
	static void LoadTextureDisabled(cocos2d::ui::Button* button,mono::string disabled,cocos2d::ui::TextureResType textType);

	static void SetCapInsets(cocos2d::ui::Button* button,float x,float y,float w,float h);
	static void SetCapInsetsNormalRenderer(cocos2d::ui::Button* button,float x,float y,float w,float h);
	static void SetCapInsetsPressedRenderer(cocos2d::ui::Button* button,float x,float y,float w,float h);
	static void SetCapInsetsDisabledRenderer(cocos2d::ui::Button* button,float x,float y,float w,float h);
	static void SetPressedActionEnabled(cocos2d::ui::Button* button,bool enabled);

	static void SetTitleText(cocos2d::ui::Button* button,mono::string text);
	static mono::string GetTitleText(cocos2d::ui::Button* button) ;

	static void SetTitleColor(cocos2d::ui::Button* button,int r,int g,int b);
	static void GetTitleColor(cocos2d::ui::Button* button,cocos2d::_ccColor3B& color) ;

	static void SetTitleFontSize(cocos2d::ui::Button* button,float size);
	static float GetTitleFontSize(cocos2d::ui::Button* button) ;

	static void SetTitleFontName(cocos2d::ui::Button* button,mono::string fontName);
	static mono::string GetTitleFontName(cocos2d::ui::Button* button) ;

	static void SetScale9Enabled(cocos2d::ui::Button* button,bool able);
protected:
private:
};

#endif