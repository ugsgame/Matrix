
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "ScriptBind_UILayoutParameter.h"

USING_NS_CC;
using namespace cocos2d::ui;

ScriptBind_UILayoutParameter::ScriptBind_UILayoutParameter()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetMargin);
//	REGISTER_METHOD(GetMargin);
	REGISTER_METHOD(GetLayoutType);
}

ScriptBind_UILayoutParameter::~ScriptBind_UILayoutParameter()
{
	
}

LayoutParameter* ScriptBind_UILayoutParameter::Create()
{
	return  LayoutParameter::create();
}

void ScriptBind_UILayoutParameter::SetMargin(LayoutParameter* layout ,mono::object obj)
{
	//TODO
}

// mono::object ScriptBind_UILayoutParameter::GetMargin(LayoutParameter* layout)
// {
// 	//TODO
// }

int ScriptBind_UILayoutParameter::GetLayoutType(LayoutParameter* layout)
{
	return (int)layout->getLayoutType();
}