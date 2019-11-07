
#include "cocos2d.h"
#include "ScriptBind_ScheduleNode.h"
#include "MCocos2d/MCScheduleNode.h"

USING_NS_CC;

ScriptBind_ScheduleNode::ScriptBind_ScheduleNode()
{
	REGISTER_METHOD(Create);

	REGISTER_METHOD(Schedule);
	REGISTER_METHOD(Unschedule);
	REGISTER_METHOD(IsScheduling);

	REGISTER_METHOD(GetMethodName);
	REGISTER_METHOD(SetMethodName);

	REGISTER_METHOD(SetDelayTime);
	REGISTER_METHOD(GetDelayTime);
}

ScriptBind_ScheduleNode::~ScriptBind_ScheduleNode()
{

}

MCScheduleNode* ScriptBind_ScheduleNode::Create(mono::object klass,mono::string method, float dt)
{
	return MCScheduleNode::create(*klass,ToMatrixString(method).c_str(),dt);
}

void ScriptBind_ScheduleNode::Schedule(MCScheduleNode* pNode)
{
	CCAssert(pNode,"");
	pNode->scheduleSelf();
}

void ScriptBind_ScheduleNode::Unschedule(MCScheduleNode* pNode)
{
	CCAssert(pNode,"");
	pNode->unscheduleSelf();
}

void ScriptBind_ScheduleNode::SetMethodName(MCScheduleNode* pNode,mono::string method)
{
	CCAssert(pNode,"");
	//pNode->setMethodName(ToMatrixString(method).c_str());
	pNode->setMethodName(*method);
}

bool ScriptBind_ScheduleNode::IsScheduling(MCScheduleNode* pNode)
{
	CCAssert(pNode,"");
	return pNode->isScheduling();
}

mono::string ScriptBind_ScheduleNode::GetMethodName(MCScheduleNode* pNode)
{
	CCAssert(pNode,"");
	return ToMonoString(pNode->getMethodName().c_str());
}

void ScriptBind_ScheduleNode::SetDelayTime(MCScheduleNode* pNode,float dt)
{
	CCAssert(pNode,"");
	pNode->setDelayTime(dt);
}
float ScriptBind_ScheduleNode::GetDelayTime(MCScheduleNode* pNode)
{
	CCAssert(pNode,"");
	return pNode->getDelayTime();
}
