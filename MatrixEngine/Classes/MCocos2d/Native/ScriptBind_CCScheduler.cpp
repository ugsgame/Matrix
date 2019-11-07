
#include "cocos2d.h"
#include "ScriptBind_CCScheduler.h"

USING_NS_CC;

ScriptBind_Scheduler::ScriptBind_Scheduler()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(GetTimeScale);
	REGISTER_METHOD(SetTimeScale);
}

ScriptBind_Scheduler::~ScriptBind_Scheduler() 
{

}

CCScheduler* ScriptBind_Scheduler::Create()
{
	return new CCScheduler();
}

float ScriptBind_Scheduler::GetTimeScale(CCScheduler* scheduler)
{
	return scheduler->getTimeScale();
}

void ScriptBind_Scheduler::SetTimeScale(CCScheduler* scheduler, float timeScale)
{
	scheduler->setTimeScale(timeScale);
}