
//#include "stdneb.h"
#include "cocos2d.h"
#include "MCocos2d/MCLayer.h"

#include "ScriptBind_CCLayer.h"

USING_NS_CC;

ScriptBind_CCLayer::ScriptBind_CCLayer()
{
	REGISTER_METHOD(Create);

	REGISTER_METHOD(ScheduleStep);
	REGISTER_METHOD(UnscheduleStep);

	REGISTER_METHOD(SetState);

	REGISTER_METHOD(SetTouchMode);
	REGISTER_METHOD(GetTouchMode);

	REGISTER_METHOD(SetTouchEnabeld);
	REGISTER_METHOD(IsTouchEnabeld);

	REGISTER_METHOD(SetAccelerometerEnabled);
	REGISTER_METHOD(IsAccelerometerEnabled);

	REGISTER_METHOD(SetTouchPriority);
	REGISTER_METHOD(GetTouchPriority);

	REGISTER_METHOD(TickTime);

	REGISTER_METHOD(ScheduleUpdate);
	REGISTER_METHOD(SetSwallowsTouches);
}

ScriptBind_CCLayer::~ScriptBind_CCLayer()
{

}

MCLayer* ScriptBind_CCLayer::Create()
{
	return MCLayer::create();
}

void ScriptBind_CCLayer::ScheduleStep(MCLayer* pLayer,float dt)
{
	pLayer->scheduleStep(dt);
}

void ScriptBind_CCLayer::UnscheduleStep(MCLayer* pLayer)
{
	pLayer->unscheduleStep();
}

void ScriptBind_CCLayer::SetState(CCLayer* pLayer,int state, bool enable)
{
	LayerState _state = (LayerState)state;
	switch (_state)
	{
	case StateTouch:
		pLayer->setTouchEnabled(enable);
		break;
	case StateSensor:
		pLayer->setAccelerometerEnabled(enable);
		break;
	case StateKeypad:
		pLayer->setKeypadEnabled(enable);
		break;
	default:
		printf("Unknow state !!!\n");
	}
}

void ScriptBind_CCLayer::SetTouchMode(CCLayer* pLayer,int mode)
{
	if (mode == 0)
		pLayer->setTouchMode(kCCTouchesAllAtOnce);
	else if(mode == 1)
		pLayer->setTouchMode(kCCTouchesOneByOne);
	else
		pLayer->setTouchMode(kCCTouchesOneByOne);
}
int	ScriptBind_CCLayer::GetTouchMode(CCLayer* pLayer)
{
	return pLayer->getTouchMode();
}

void ScriptBind_CCLayer::SetTouchEnabeld(CCLayer* pLayer,bool enable)
{
	pLayer->setTouchEnabled(true);
}
bool ScriptBind_CCLayer::IsTouchEnabeld(CCLayer* pLayer)
{
	return pLayer->isTouchEnabled();
}

void ScriptBind_CCLayer::SetAccelerometerEnabled(CCLayer* pLayer,bool enable)
{
	pLayer->setAccelerometerEnabled(enable);
}
bool ScriptBind_CCLayer::IsAccelerometerEnabled(CCLayer* pLayer)
{
	return pLayer->isAccelerometerEnabled();
}

void ScriptBind_CCLayer::SetKeypadEnabled(CCLayer* pLayer,bool enable)
{
	pLayer->setKeypadEnabled(enable);
}
bool ScriptBind_CCLayer::IsKeypadEnabled(CCLayer* pLayer)
{
	return pLayer->isKeypadEnabled();
}

void  ScriptBind_CCLayer::SetTouchPriority(CCLayer* pLayer,int priority)
{
	pLayer->setTouchPriority(priority);
}
int	ScriptBind_CCLayer::GetTouchPriority(CCLayer* pLayer)
{
	return pLayer->getTouchPriority();
}

void ScriptBind_CCLayer::ScheduleUpdate(CCLayer* pLayer)
{
	CCAssert(pLayer,"");
	pLayer->scheduleUpdate();
}

float ScriptBind_CCLayer::TickTime(MCLayer* pLayer)
{
	return pLayer->tickTime();
}

void ScriptBind_CCLayer::SetSwallowsTouches(MCLayer* pLayer,bool bSwallowsTouches)
{
	pLayer->SetSwallowsTouches(bSwallowsTouches);
}