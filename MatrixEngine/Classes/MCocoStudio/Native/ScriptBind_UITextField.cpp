
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "ScriptBind_UITextField.h"

USING_NS_CC;
using namespace cocos2d::ui;

class TextFieldEvent:public CCObject
{
public:
	TextFieldEvent(){}
	~TextFieldEvent(){}
	static TextFieldEvent* Create(IMonoObject* obj)
	{
		TextFieldEvent* event = new TextFieldEvent();
		event->setMonoObject(obj);
		//c#层没有计数，native层也不计了
		event->ObjectCount--;
		return event;
	}
	//TODO 回调c#函数中第一个参数 应该传入c#的对像，而不是指针

	void TextFieldListener(CCObject *pSender, TextFiledEventType type)
	{
		CCAssert(p_MonoObject,"");
		//TODO
		//pScript->CallMethod("TouchEvent",IMonoObject object,(int)type);	
		p_MonoObject->CallMethod("TextFieldListener",(int)type);	
	}
};


ScriptBind_UITextField::ScriptBind_UITextField()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetTouchSize);
	REGISTER_METHOD(SetText);
	REGISTER_METHOD(SetPlaceHolder);
	REGISTER_METHOD(SetFontSize);
	REGISTER_METHOD(SetFontName);
	REGISTER_METHOD(DidNotSelectSelf);
	REGISTER_METHOD(GetStringValue);
	REGISTER_METHOD(SetMaxLengthEnabled);
	REGISTER_METHOD(IsMaxLengthEnabled);
	REGISTER_METHOD(SetMaxLength);
	REGISTER_METHOD(GetMaxLength);
	REGISTER_METHOD(SetPasswordEnabled);
	REGISTER_METHOD(IsPasswordEnabled);
	REGISTER_METHOD(SetPasswordStyleText);
	REGISTER_METHOD(GetAttachWithIME);
	REGISTER_METHOD(SetAttachWithIME);
	REGISTER_METHOD(GetDetachWithIME);
	REGISTER_METHOD(SetDetachWithIME);
	REGISTER_METHOD(GetInsertText);
	REGISTER_METHOD(SetInsertText);
	REGISTER_METHOD(GetDeleteBackward);
	REGISTER_METHOD(SetDeleteBackward);
	REGISTER_METHOD(AddEventListenerTextField);
}
ScriptBind_UITextField::~ScriptBind_UITextField()
{

}

TextField* ScriptBind_UITextField::Create()
{
	return TextField::create();
}

void ScriptBind_UITextField::SetTouchSize(TextField* textfile, float w,float h)
{
	textfile->setTouchSize(CCSize(w,h));
}

void ScriptBind_UITextField::SetText(TextField* textfile,mono::string text)
{
	textfile->setText(std::string(*text));
}

void ScriptBind_UITextField::SetPlaceHolder(TextField* textfile,mono::string value)
{
	textfile->setPlaceHolder(std::string(*value));
}

void ScriptBind_UITextField::SetFontSize(TextField* textfile,int size)
{
	textfile->setFontSize(size);
}

void ScriptBind_UITextField::SetFontName(TextField* textfile,mono::string name)
{
	textfile->setFontName(std::string(*name));
}

void ScriptBind_UITextField::DidNotSelectSelf(TextField* textfile)
{
	textfile->didNotSelectSelf();
}

mono::string ScriptBind_UITextField::GetStringValue(TextField* textfile)
{
	return ToMonoString(textfile->getStringValue());
}

void ScriptBind_UITextField::SetMaxLengthEnabled(TextField* textfile,bool enable)
{
	textfile->setMaxLengthEnabled(enable);
}
bool ScriptBind_UITextField::IsMaxLengthEnabled(TextField* textfile)
{
	return textfile->isMaxLengthEnabled();
}

void ScriptBind_UITextField::SetMaxLength(TextField* textfile,int length)
{
	textfile->setMaxLength(length);
}
int  ScriptBind_UITextField::GetMaxLength(TextField* textfile)
{
	return textfile->getMaxLength();
}

void ScriptBind_UITextField::SetPasswordEnabled(TextField* textfile,bool enable)
{
	textfile->setPasswordEnabled(enable);
}
bool ScriptBind_UITextField::IsPasswordEnabled(TextField* textfile)
{
	return textfile->isPasswordEnabled();
}

void ScriptBind_UITextField::SetPasswordStyleText(TextField* textfile,mono::string styleText)
{
	textfile->setPasswordStyleText(ToMatrixString(styleText).c_str());
}
bool ScriptBind_UITextField::GetAttachWithIME(TextField* textfile)
{
	return textfile->getAttachWithIME();
}

void ScriptBind_UITextField::SetAttachWithIME(TextField* textfile,bool attach)
{
	textfile->setAttachWithIME(attach);
}

bool ScriptBind_UITextField::GetDetachWithIME(TextField* textfile)
{
	return textfile->getDetachWithIME();
}

void ScriptBind_UITextField::SetDetachWithIME(TextField* textfile,bool detach)
{
	textfile->setDetachWithIME(detach);
}
bool ScriptBind_UITextField::GetInsertText(TextField* textfile)
{
	return textfile->getInsertText();
}

void ScriptBind_UITextField::SetInsertText(TextField* textfile,bool insertText)
{
	textfile->setInsertText(insertText);
}
bool ScriptBind_UITextField::GetDeleteBackward(TextField* textfile)
{
	return textfile->getDeleteBackward();
}

void ScriptBind_UITextField::SetDeleteBackward(TextField* textfile,bool deleteBackward)
{
	textfile->setDeleteBackward(deleteBackward);
}

void ScriptBind_UITextField::AddEventListenerTextField(TextField* textfile,mono::object target)
{
	textfile->addEventListenerTextField(TextFieldEvent::Create(*target),textfieldeventselector(TextFieldEvent::TextFieldListener));
}