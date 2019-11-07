
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "ScriptBind_UILabelBMFont.h"

USING_NS_CC;
using namespace cocos2d::ui;

ScriptBind_UILabelBMFont::ScriptBind_UILabelBMFont()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetFontFile);
	REGISTER_METHOD(SetText);
	REGISTER_METHOD(GetText);
}
ScriptBind_UILabelBMFont::~ScriptBind_UILabelBMFont()
{

}


LabelBMFont* ScriptBind_UILabelBMFont::Create()
{
	return LabelBMFont::create();
}

void ScriptBind_UILabelBMFont::SetFontFile(LabelBMFont* label,mono::string fileName)
{
	label->setFntFile(ToMatrixString(fileName).c_str());
}
void ScriptBind_UILabelBMFont::SetText(LabelBMFont* label,mono::string text)
{
	label->setText(ToMatrixString(text).c_str());
}
mono::string ScriptBind_UILabelBMFont::GetText(LabelBMFont* label)
{
	return ToMonoString(label->getStringValue());
}