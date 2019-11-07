
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"
#include "ExtensionMacros.h"

#include "ScriptBind_GUIReader.h"

USING_NS_CC;
USING_NS_CC_EXT;
using namespace cocos2d::ui;

ScriptBind_GUIReader::ScriptBind_GUIReader()
{
	REGISTER_METHOD(WidgetFromJsonFile);
}

ScriptBind_GUIReader::~ScriptBind_GUIReader()
{

}

Widget* ScriptBind_GUIReader::WidgetFromJsonFile(mono::string fileName)
{
	return GUIReader::shareReader()->widgetFromJsonFile(ToMatrixString(fileName).c_str());
}