
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"
#include "MatrixScriptHelper.h"

#include "ScriptBind_UILayer.h"
#include "MCocoStudio/MSUILayer.h"

USING_NS_CC;
using namespace cocos2d::ui;

ScriptBind_UILayer::ScriptBind_UILayer()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(AddWidget);
	REGISTER_METHOD(RemoveWidget);
	REGISTER_METHOD(GetWidgetByTag);
	REGISTER_METHOD(GetWidgetByName);
	REGISTER_METHOD(GetRootWidget);
	REGISTER_METHOD(TickTime);
}

ScriptBind_UILayer::~ScriptBind_UILayer()
{

}

MSUILayer* ScriptBind_UILayer::Create()
{
	//return MSUILayer::Create();
	return MSUILayer::create();
}

void ScriptBind_UILayer::AddWidget(MSUILayer* layer, cocos2d::ui::Widget* widget)
{
	CCAssert(layer,"");
	layer->addWidget(widget);
}
void ScriptBind_UILayer::RemoveWidget(MSUILayer* layer, cocos2d::ui::Widget* widget)
{
	CCAssert(layer,"");
	layer->removeWidget(widget);
}
Widget* ScriptBind_UILayer::GetWidgetByTag(MSUILayer* layer, int tag)
{
	CCAssert(layer,"");
	return layer->getWidgetByTag(tag);
}
Widget* ScriptBind_UILayer::GetWidgetByName(MSUILayer* layer, mono::string name)
{
	CCAssert(layer,"");
	return layer->getWidgetByName(ToMatrixString(name).c_str());
}
Widget* ScriptBind_UILayer::GetRootWidget(MSUILayer* layer)
{
	CCAssert(layer,"");
	Widget* widget = layer->getRootWidget();
	CCAssert(widget,"");
	return widget;
}
float ScriptBind_UILayer::TickTime(MSUILayer* layer)
{
	CCAssert(layer,"");
	return layer->tickTime();
}