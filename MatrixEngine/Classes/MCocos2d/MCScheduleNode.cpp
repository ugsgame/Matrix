
#include "MCScheduleNode.h"

MCScheduleNode::MCScheduleNode()
{
	scheduleMethodName = "";
	delayTime = 0.01f;
};

MCScheduleNode::~MCScheduleNode()
{
	if(pScript)
	{
		pScript->Release(false);
		pScript = NULL;
	}
}

MCScheduleNode* MCScheduleNode::create(IMonoObject* object,std::string method, float dt)
{
	MCScheduleNode* pRet = new MCScheduleNode();
	pRet->SetScript(object);
	pRet->setMethodName(method);
	pRet->setDelayTime(dt);
	pRet->autorelease();

	return pRet;
}

void MCScheduleNode::SetScript(IMonoObject* script)
{
	pScript = script;
	pScript->FreeGCHandle();
}

void MCScheduleNode::scheduleSelf()
{
	this->schedule(schedule_selector(MCScheduleNode::scheduleMethod),delayTime);
}

void MCScheduleNode::unscheduleSelf()
{
	this->unschedule(schedule_selector(MCScheduleNode::scheduleMethod));
}

void MCScheduleNode::scheduleMethod(float dt)
{
	if(this->GetScript())
	{
		IMonoObject* obj = this->GetScript();

		obj->CallMethod(scheduleMethodName.c_str(),delayTime);
	}
}