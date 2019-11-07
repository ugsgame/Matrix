
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"
#include "ExtensionMacros.h"

#include "ScriptBind_CCBone.h"

USING_NS_CC;
USING_NS_CC_EXT;

ScriptBind_CCBone::ScriptBind_CCBone()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(CreateWithName);
	REGISTER_METHOD(AddDisplay);
	REGISTER_METHOD(AddNodeDisplay);
	REGISTER_METHOD(RemoveDisplay);
	REGISTER_METHOD(ChangeDisplayWithIndex);
	REGISTER_METHOD(ChangeDisplayWithName);
	REGISTER_METHOD(AddChildBone);
	REGISTER_METHOD(SetParentBone);
	REGISTER_METHOD(GetParentBone);
	REGISTER_METHOD(RemoveFromParent);
	REGISTER_METHOD(RemoveChildBone);
	REGISTER_METHOD(GetDisplayRenderNode);
	REGISTER_METHOD(GetDisplayRenderType);
	REGISTER_METHOD(GetName);
}
ScriptBind_CCBone::~ScriptBind_CCBone()
{
	
}

CCBone* ScriptBind_CCBone::Create()
{
	return CCBone::create();
}

CCBone* ScriptBind_CCBone::CreateWithName(mono::string name)
{
	return CCBone::create(ToMatrixString(name).c_str());
}

void ScriptBind_CCBone::AddDisplay(CCBone* pBone, CCDisplayData* displayData, int index)
{
	pBone->addDisplay(displayData,index);
}
void ScriptBind_CCBone::AddNodeDisplay(CCBone* pBone, CCNode* display, int index)
{
	pBone->addDisplay(display,index);
}

void ScriptBind_CCBone::RemoveDisplay(CCBone* pBone, int index)
{
	pBone->removeDisplay(index);
}

void ScriptBind_CCBone::ChangeDisplayWithIndex(CCBone* pBone, int index, bool force)
{
	pBone->changeDisplayWithIndex(index,force);
}
void ScriptBind_CCBone::ChangeDisplayWithName(CCBone* pBone, mono::string name, bool force)
{
	pBone->changeDisplayWithName(ToMatrixString(name).c_str(),force);
}

void ScriptBind_CCBone::AddChildBone(CCBone* pBone,CCBone *child)
{
	pBone->addChildBone(child);
}
void ScriptBind_CCBone::SetParentBone(CCBone* pBone,CCBone *parent)
{
	pBone->setParentBone(parent);
}
CCBone*	ScriptBind_CCBone::GetParentBone(CCBone* pBone)
{
	return pBone->getParentBone();
}

void ScriptBind_CCBone::RemoveFromParent(CCBone* pBone,bool recursion)
{
	pBone->removeFromParent(recursion);
}
void ScriptBind_CCBone::RemoveChildBone(CCBone* pBone,CCBone *bone, bool recursion)
{
	pBone->removeChildBone(bone,recursion);
}

CCNode* ScriptBind_CCBone::GetDisplayRenderNode(CCBone* pBone)
{
	return pBone->getDisplayRenderNode();
}

DisplayType ScriptBind_CCBone::GetDisplayRenderType(CCBone* pBone)
{
	return pBone->getDisplayRenderNodeType();
}

mono::string ScriptBind_CCBone::GetName(CCBone* pBone)
{
	return ToMonoString(pBone->getName().c_str());
}