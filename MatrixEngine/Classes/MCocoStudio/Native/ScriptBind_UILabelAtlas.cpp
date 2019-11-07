
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "ScriptBind_UILabelAtlas.h"

USING_NS_CC;
using namespace cocos2d::ui;

ScriptBind_UILabelAtlas::ScriptBind_UILabelAtlas()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetProperty);
	REGISTER_METHOD(SetText);
	REGISTER_METHOD(GetText);
}
ScriptBind_UILabelAtlas::~ScriptBind_UILabelAtlas()
{

}


LabelAtlas* ScriptBind_UILabelAtlas::Create()
{
	return LabelAtlas::create();
}

void ScriptBind_UILabelAtlas::SetProperty(LabelAtlas* label, mono::string stringValue,\
										  mono::string charMapFile, int itemWidth, int itemHeight, mono::string  startCharMap)
{
	label->setProperty(std::string(*stringValue),std::string(*charMapFile),itemWidth,itemHeight,std::string(*startCharMap));
}

void ScriptBind_UILabelAtlas::SetText(LabelAtlas* label,mono::string stringValue)
{
	label->setStringValue(std::string(*stringValue));
}
mono::string ScriptBind_UILabelAtlas::GetText(LabelAtlas* label)
{
	return ToMonoString(label->getStringValue());
}