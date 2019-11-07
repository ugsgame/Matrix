//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "ScriptBind_UICheckBox.h"

USING_NS_CC;
using namespace cocos2d::ui;

class CheckBoxEvent:public CCObject
{
public:
	CheckBoxEvent(){}
	~CheckBoxEvent(){}

	static CheckBoxEvent* Create(IMonoObject* obj)
	{
		CheckBoxEvent* event = new CheckBoxEvent();
		event->setMonoObject(obj);
		//c#层没有计数，native层也不计了
		event->ObjectCount--;
		return event;
	}
	//TODO 回调c#函数中第一个参数 应该传入c#的对像，而不是指针

	void SelectedStateEvent(CCObject *pSender, CheckBoxEventType type)
	{
		CCAssert(p_MonoObject,"");
		//TODO
		//pScript->CallMethod("TouchEvent",IMonoObject object,(int)type);	
		p_MonoObject->CallMethod("SelectedStateEvent",(int)type);	
	}
};

ScriptBind_UICheckBox::ScriptBind_UICheckBox()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(LoadTextures);
	REGISTER_METHOD(LoadTextureBackGround);
	REGISTER_METHOD(LoadTextureBackGroundSelected);
	REGISTER_METHOD(LoadTextureFrontCross);
	REGISTER_METHOD(LoadTextureBackGroundDisabled);
	REGISTER_METHOD(LoadTextureFrontCrossDisabled);

	REGISTER_METHOD(SetSelectedState);
	REGISTER_METHOD(GetSelectedState);
	REGISTER_METHOD(AddEventListenerCheckBox);
}
ScriptBind_UICheckBox::~ScriptBind_UICheckBox()
{

}

CheckBox* ScriptBind_UICheckBox::Create()
{
	return CheckBox::create();
}

void ScriptBind_UICheckBox::LoadTextures(CheckBox * checkbox,mono::string backGround,mono::string backGroundSelected,\
						 mono::string cross,mono::string backGroundDisabled,mono::string frontCrossDisabled,TextureResType textType)
{
	checkbox->loadTextures(ToMatrixString(backGround).c_str(),ToMatrixString(backGroundSelected).c_str(),ToMatrixString(cross).c_str(),ToMatrixString(backGroundDisabled).c_str(),ToMatrixString(frontCrossDisabled).c_str(), textType);
}

void ScriptBind_UICheckBox::LoadTextureBackGround(CheckBox * checkbox,mono::string backGround,TextureResType textType)
{
	checkbox->loadTextureBackGround(ToMatrixString(backGround).c_str(), textType);
}
void ScriptBind_UICheckBox::LoadTextureBackGroundSelected(CheckBox * checkbox,mono::string backGroundSelected,TextureResType textType)
{
	checkbox->loadTextureBackGroundSelected(ToMatrixString(backGroundSelected).c_str(), textType);
}
void ScriptBind_UICheckBox::LoadTextureFrontCross(CheckBox * checkbox,mono::string cross,TextureResType textType)
{
	checkbox->loadTextureFrontCross(ToMatrixString(cross).c_str(), textType);
}
void ScriptBind_UICheckBox::LoadTextureBackGroundDisabled(CheckBox * checkbox,mono::string backGroundDisabled,TextureResType textType)
{
	checkbox->loadTextureBackGroundDisabled(ToMatrixString(backGroundDisabled).c_str(), textType);
}
void ScriptBind_UICheckBox::LoadTextureFrontCrossDisabled(CheckBox * checkbox,mono::string frontCrossDisabled,TextureResType textType)
{
	checkbox->loadTextureFrontCrossDisabled(ToMatrixString(frontCrossDisabled).c_str(), textType);
}

void ScriptBind_UICheckBox::SetSelectedState(CheckBox * checkbox,bool state)
{
	checkbox->setSelectedState(state);
}
bool ScriptBind_UICheckBox::GetSelectedState(CheckBox * checkbox)
{
	return checkbox->getSelectedState();
}

void ScriptBind_UICheckBox::AddEventListenerCheckBox(CheckBox * checkbox,mono::object obj)
{
	checkbox->addEventListenerCheckBox(CheckBoxEvent::Create(*obj),checkboxselectedeventselector(CheckBoxEvent::SelectedStateEvent));
}