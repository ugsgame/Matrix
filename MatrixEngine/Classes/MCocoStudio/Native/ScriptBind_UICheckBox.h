
#ifndef __SCRIPTBIND_UICHECKBOX__
#define __SCRIPTBIND_UICHECKBOX__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::CheckBox;

class ScriptBind_UICheckBox:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UICheckBox();
	~ScriptBind_UICheckBox();

	virtual const char* GetClassName(){ return "NativeUICheckBox"; }

	static cocos2d::ui::CheckBox* Create();

	static void LoadTextures(cocos2d::ui::CheckBox * checkbox,mono::string backGround,mono::string backGroundSelected,\
		mono::string cross,mono::string backGroundDisabled,mono::string frontCrossDisabled,cocos2d::ui::TextureResType textType);

	static void LoadTextureBackGround(cocos2d::ui::CheckBox * checkbox,mono::string backGround,cocos2d::ui::TextureResType textType);
	static void LoadTextureBackGroundSelected(cocos2d::ui::CheckBox * checkbox,mono::string backGroundSelected,cocos2d::ui::TextureResType textType);
	static void LoadTextureFrontCross(cocos2d::ui::CheckBox * checkbox,mono::string cross,cocos2d::ui::TextureResType textType);
	static void LoadTextureBackGroundDisabled(cocos2d::ui::CheckBox * checkbox,mono::string backGroundDisabled,cocos2d::ui::TextureResType textType);
	static void LoadTextureFrontCrossDisabled(cocos2d::ui::CheckBox * checkbox,mono::string frontCrossDisabled,cocos2d::ui::TextureResType textType);

	static void SetSelectedState(cocos2d::ui::CheckBox * checkbox,bool state);
	static bool GetSelectedState(cocos2d::ui::CheckBox * checkbox);

	static void AddEventListenerCheckBox(cocos2d::ui::CheckBox * checkbox,mono::object obj);

protected:
private:
};

#endif

