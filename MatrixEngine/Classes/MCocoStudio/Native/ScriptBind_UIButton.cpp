
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "ScriptBind_UIButton.h"
#include "MatrixScriptHelper.h"

USING_NS_CC;
using namespace cocos2d::ui;

ScriptBind_UIButton::ScriptBind_UIButton()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(LoadTextures);
	REGISTER_METHOD(LoadTextureNormal);
	REGISTER_METHOD(LoadTexturePressed);
	REGISTER_METHOD(LoadTextureDisabled);
	REGISTER_METHOD(SetCapInsets);
	REGISTER_METHOD(SetCapInsetsNormalRenderer);
	REGISTER_METHOD(SetCapInsetsPressedRenderer);
	REGISTER_METHOD(SetCapInsetsDisabledRenderer);
	REGISTER_METHOD(SetPressedActionEnabled);
	REGISTER_METHOD(SetTitleText);
	REGISTER_METHOD(GetTitleText);
	REGISTER_METHOD(SetTitleColor);
	REGISTER_METHOD(GetTitleColor);
	REGISTER_METHOD(SetTitleFontSize);
	REGISTER_METHOD(GetTitleFontSize);
	REGISTER_METHOD(SetTitleFontName);
	REGISTER_METHOD(GetTitleFontName);

	REGISTER_METHOD(SetScale9Enabled);
}
ScriptBind_UIButton::~ScriptBind_UIButton()
{

}

UIButton* ScriptBind_UIButton::Create()
{
	return Button::create();
}

void ScriptBind_UIButton::LoadTextures(Button* button,mono::string normal,mono::string selected,mono::string disabled,TextureResType textType)
{
	button->loadTextures(ToMatrixString(normal).c_str(),ToMatrixString(selected).c_str(),ToMatrixString(disabled).c_str(), textType);
}
void ScriptBind_UIButton::LoadTextureNormal(Button* button,mono::string normal,TextureResType textType)
{
	button->loadTextureNormal(ToMatrixString(normal).c_str(), textType);
}
void ScriptBind_UIButton::LoadTexturePressed(Button* button,mono::string selected,TextureResType textType)
{
	button->loadTexturePressed(ToMatrixString(selected).c_str(), textType);
}
void ScriptBind_UIButton::LoadTextureDisabled(Button* button,mono::string disabled,TextureResType textType)
{
	button->loadTextureDisabled(ToMatrixString(disabled).c_str(), textType);
}

void ScriptBind_UIButton::SetCapInsets(Button* button,float x,float y,float w,float h)
{
	button->setCapInsets(CCRect(x,y,w,h));
}
void ScriptBind_UIButton::SetCapInsetsNormalRenderer(Button* button,float x,float y,float w,float h)
{
	button->setCapInsetsNormalRenderer(CCRect(x,y,w,h));
}
void ScriptBind_UIButton::SetCapInsetsPressedRenderer(Button* button,float x,float y,float w,float h)
{
	button->setCapInsetsPressedRenderer(CCRect(x,y,w,h));
}
void ScriptBind_UIButton::SetCapInsetsDisabledRenderer(Button* button,float x,float y,float w,float h)
{
	button->setCapInsetsDisabledRenderer(CCRect(x,y,w,h));
}
void ScriptBind_UIButton::SetPressedActionEnabled(Button* button,bool enabled)
{
	button->setPressedActionEnabled(enabled);
}

void ScriptBind_UIButton::SetTitleText(Button* button,mono::string text)
{
	button->setTitleText(std::string(*text));
}
mono::string ScriptBind_UIButton::GetTitleText(Button* button)
{
	return ToMonoString(button->getTitleText());
}

void ScriptBind_UIButton::SetTitleColor(Button* button,int r,int g,int b)
{
	button->setTitleColor(ccc3(r,g,b));
}
void ScriptBind_UIButton::GetTitleColor(Button* button,_ccColor3B& color)
{
	color = button->getTitleColor();
}

void ScriptBind_UIButton::SetTitleFontSize(Button* button,float size)
{
	button->setTitleFontSize(size);
}
float ScriptBind_UIButton::GetTitleFontSize(Button* button)
{
	return button->getTitleFontSize();
}

void ScriptBind_UIButton::SetTitleFontName(Button* button,mono::string fontName)
{
	button->setTitleFontName(ToMatrixString(fontName).c_str());
}
mono::string ScriptBind_UIButton::GetTitleFontName(Button* button)
{
	return ToMonoString(button->getTitleFontName());
}

void ScriptBind_UIButton::SetScale9Enabled(Button* button,bool able)
{
	button->setScale9Enabled(able);
}