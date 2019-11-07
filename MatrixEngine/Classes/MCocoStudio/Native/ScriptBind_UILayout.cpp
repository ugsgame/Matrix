
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"
#include "MatrixScriptHelper.h"

#include "ScriptBind_UILayout.h"

USING_NS_CC;
using namespace ui;

ScriptBind_UILayout::ScriptBind_UILayout()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetBackGroundImage);
	REGISTER_METHOD(SetBackGroundImageCapInsets);
	REGISTER_METHOD(SetBackGroundColorType);
	REGISTER_METHOD(SetBackGroundColorS);
	REGISTER_METHOD(SetBackGroundColorSE);
	REGISTER_METHOD(SetBackGroundColorOpacity);
	REGISTER_METHOD(SetBackGroundColorVector);
	REGISTER_METHOD(RemoveBackGroundImage);
	REGISTER_METHOD(GetBackGroundImageTextureSize);
	REGISTER_METHOD(SetClippingEnabled);
	REGISTER_METHOD(IsClippingEnabled);
	REGISTER_METHOD(SetClippingType);
	REGISTER_METHOD(SetLayoutType);
	REGISTER_METHOD(GetLayoutType);
}
ScriptBind_UILayout::~ScriptBind_UILayout()
{
	
}


Layout* ScriptBind_UILayout::Create()
{
	return Layout::create();
}

void ScriptBind_UILayout::SetBackGroundImage(Layout* layout,mono::string fileName,TextureResType textType)
{
	layout->setBackGroundImage(ToMatrixString(fileName).c_str(), textType);
}
void ScriptBind_UILayout::SetBackGroundImageCapInsets(Layout* layout,float x,float y ,float w,float h)
{
	layout->setBackGroundImageCapInsets(CCRect(x,y,w,h));
}

void ScriptBind_UILayout::SetBackGroundColorType(Layout* layout,int type)
{
	layout->setBackGroundColorType((LayoutBackGroundColorType)type);
}
void ScriptBind_UILayout::SetBackGroundColorS(Layout* layout,_ccColor3B& color)
{
	layout->setBackGroundColor(color);
}
void ScriptBind_UILayout::SetBackGroundColorSE(Layout* layout,_ccColor3B& startColor, _ccColor3B& endColor)
{
	layout->setBackGroundColor(startColor,endColor);
}
void ScriptBind_UILayout::SetBackGroundColorOpacity(Layout* layout,int opacity)
{
	layout->setBackGroundColorOpacity(opacity);
}
void ScriptBind_UILayout::SetBackGroundColorVector(Layout* layout,mono::object vector)
{
;
	float* x = (float*)MonoGetField(*vector,"X");
	float* y = (float*)MonoGetField(*vector,"Y");

	layout->setBackGroundColorVector(ccp(*x,*y));
}

void ScriptBind_UILayout::RemoveBackGroundImage(Layout* layout)
{
	layout->removeBackGroundImage();
}
void ScriptBind_UILayout::GetBackGroundImageTextureSize(Layout* layout,CCSize& size)
{
	size = layout->getBackGroundImageTextureSize();
}

void ScriptBind_UILayout::SetClippingEnabled(Layout* layout,bool enabled)
{
	layout->setClippingEnabled(enabled);
}
bool ScriptBind_UILayout::IsClippingEnabled(Layout* layout)
{
	return layout->isClippingEnabled();
}
void ScriptBind_UILayout::SetClippingType(Layout* layout,int type)
{
	layout->setClippingType((LayoutClippingType)type);
}

void ScriptBind_UILayout::SetLayoutType(Layout* layout,int type)
{
	layout->setLayoutType((LayoutType)type);
}
int ScriptBind_UILayout::GetLayoutType(Layout* layout)
{
	return (int)layout->getLayoutType();
}