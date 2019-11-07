
#ifndef __SCRIPTBIND_UISLIDER__
#define __SCRIPTBIND_UISLIDER__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::Slider;

class ScriptBind_UISlider:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UISlider();
	~ScriptBind_UISlider();

	virtual const char* GetClassName(){ return "NativeUISlider";}

	static cocos2d::ui::Slider* Create();

	static void LoadBarTexture(cocos2d::ui::Slider* slider , mono::string fileName,cocos2d::ui::TextureResType textType);

	static void SetCapInsets(cocos2d::ui::Slider* slider,float x,float y ,float w,float h);

	static void SetCapInsetsBarRenderer(cocos2d::ui::Slider* slider,float x,float y ,float w,float h);

	static void SetCapInsetProgressBarRebderer(cocos2d::ui::Slider* slider,float x,float y ,float w,float h);

	static void LoadSlidBallTextures(cocos2d::ui::Slider* slider,mono::string normal,mono::string pressed,mono::string disabled,cocos2d::ui::TextureResType textType);

	static void LoadSlidBallTextureNormal(cocos2d::ui::Slider* slider,mono::string normal,cocos2d::ui::TextureResType textType);

	static void LoadSlidBallTexturePressed(cocos2d::ui::Slider* slider,mono::string pressed,cocos2d::ui::TextureResType textType);

	static void LoadSlidBallTextureDisabled(cocos2d::ui::Slider* slider,mono::string disabled,cocos2d::ui::TextureResType textType);

	static void LoadProgressBarTexture(cocos2d::ui::Slider* slider,mono::string fileName,cocos2d::ui::TextureResType textType);

	static void SetPercent(cocos2d::ui::Slider* slider,int percent);

	static int GetPercent(cocos2d::ui::Slider* slider);

	static void AddEventListenerSlider(cocos2d::ui::Slider* slider,mono::object target);

	static void SetScale9Enabled(cocos2d::ui::Slider* slider,bool able);
protected:
private:
};

#endif