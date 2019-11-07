
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "ScriptBind_UILabel.h"

USING_NS_CC;
using namespace cocos2d::ui;


ScriptBind_UILabel::ScriptBind_UILabel()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetText);
	REGISTER_METHOD(GetText);
	REGISTER_METHOD(GetStringLength);
	REGISTER_METHOD(SetFontSize);
	REGISTER_METHOD(GetFontSize);
	REGISTER_METHOD(SetFontName);
	REGISTER_METHOD(GetFontName);
	REGISTER_METHOD(SetTouchScaleChangeEnabled);
	REGISTER_METHOD(IsTouchScaleChangeEnabled);
	REGISTER_METHOD(SetTextAreaSize);
	REGISTER_METHOD(SetTextHorizontalAlignment);
	REGISTER_METHOD(SetTextVerticalAlignment);
}
ScriptBind_UILabel::~ScriptBind_UILabel()
{

}


Label* ScriptBind_UILabel::Create()
{
	return Label::create();
}

void  ScriptBind_UILabel::SetText(Label* label,mono::string text)
{
	label->setText(std::string(*text));
}
mono::string  ScriptBind_UILabel::GetText(Label* label)
{
	return ToMonoString(label->getStringValue());
}

int  ScriptBind_UILabel::GetStringLength(Label* label)
{
	return label->getStringLength();
}

void  ScriptBind_UILabel::SetFontSize(Label* label,int size)
{
	label->setFontSize(size);
}

int  ScriptBind_UILabel::GetFontSize(cocos2d::ui::Label* label)
{
	return label->getFontSize();
}

void ScriptBind_UILabel::SetFontName(Label* label,mono::string name)
{
	label->setFontName(std::string(*name));
}

mono::string ScriptBind_UILabel::GetFontName(cocos2d::ui::Label* label)
{
	return ToMonoString(label->getFontName());
}

void  ScriptBind_UILabel::SetTouchScaleChangeEnabled(Label* label,bool enabled)
{
	label->setTouchScaleChangeEnabled(enabled);
}
bool  ScriptBind_UILabel::IsTouchScaleChangeEnabled(Label* label)
{
	return label->isTouchScaleChangeEnabled();
}

void ScriptBind_UILabel::SetTextAreaSize(Label* label,float w,float h)
{
	label->setTextAreaSize(CCSize(w,h));
}
void ScriptBind_UILabel::SetTextHorizontalAlignment(Label* label,CCTextAlignment alignment)
{
	label->setTextHorizontalAlignment(alignment);
}
void ScriptBind_UILabel::SetTextVerticalAlignment(Label* label,CCVerticalTextAlignment alignment)
{
	label->setTextVerticalAlignment(alignment);
}