

#include "cocos2d.h"
#include "cocos-ext.h"
#include "renren-ext.h"
#include "ExtensionMacros.h"

#include "ScriptBind_CCLabelHTML.h"

USING_NS_CC;
USING_NS_CC_EXT;

class REvScriptHandler:public CCObject,public IRichEventHandler
{
	__ScriptBind
public:
	REvScriptHandler(IMonoObject* obj)
	{
		this->SetMonoObject(obj);
	}
	~REvScriptHandler(){};

	virtual void onClick(IRichNode* root, IRichElement* ele, int _id)
	{
		//TODO:把IRichNode* root, IRichElement* ele传到c#
		GetMonoObject()->CallMethod("Native_OnClick", _id);
	}
	virtual void onMoved(IRichNode* root, IRichElement* ele, int _id, const CCPoint& location, const CCPoint& delta)
	{
		//TODO:把IRichNode* root, IRichElement* ele传到c#
		GetMonoObject()->CallMethod("Native_OnMove",_id,location.x,location.y,delta.x,delta.y);
	}

protected:
private:
};


ScriptBind_CCLabelHTML::ScriptBind_CCLabelHTML()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(CreateWithString);
	REGISTER_METHOD(SetString);
	REGISTER_METHOD(GetString);
	REGISTER_METHOD(AppendString);
	REGISTER_METHOD(RegisterListener);
}

ScriptBind_CCLabelHTML::~ScriptBind_CCLabelHTML()
{

}

CCHTMLLabel* ScriptBind_CCLabelHTML::Create()
{
	return CCHTMLLabel::create();
}

CCHTMLLabel* ScriptBind_CCLabelHTML::CreateWithString(mono::string utf8_str, CCSize& preferred_size, mono::string font_alias)
{
	return CCHTMLLabel::createWithString(ToMatrixString(utf8_str).c_str(),preferred_size,ToMatrixString(font_alias).c_str());
}

void ScriptBind_CCLabelHTML::SetString(CCHTMLLabel* label,mono::string str)
{
	CCAssert(label,"");
	label->setString(ToMatrixString(str).c_str());
}

mono::string ScriptBind_CCLabelHTML::GetString(CCHTMLLabel* label)
{
	CCAssert(label,"");
	return ToMonoString(label->getString());
}

void ScriptBind_CCLabelHTML::AppendString(CCHTMLLabel* label,mono::string str)
{
	CCAssert(label,"");
	label->appendString(ToMatrixString(str).c_str());
}

void ScriptBind_CCLabelHTML::RegisterListener(CCHTMLLabel* label,mono::object scriptHandler)
{
	REvScriptHandler* listener = new REvScriptHandler(*scriptHandler);

	label->registerListener(listener,listener);
}