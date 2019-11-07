
#include "cocos2d.h"

#include "ScriptBind_CCProgressTimer.h"

USING_NS_CC;

ScriptBind_CCProgressTimer::ScriptBind_CCProgressTimer()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(GetType);
	REGISTER_METHOD(SetType);
	REGISTER_METHOD(GetPercentage);
	REGISTER_METHOD(SetPercentage);
	REGISTER_METHOD(SetSprite);
	REGISTER_METHOD(GetSprite);
	REGISTER_METHOD(SetReverseProgress);
	REGISTER_METHOD(IsReverseDirection);
	REGISTER_METHOD(SetReverseDirection);
	REGISTER_METHOD(SetMidPoint);
	REGISTER_METHOD(GetMidPoint);
	REGISTER_METHOD(SetBarChangeRate);
	REGISTER_METHOD(GetBarChangeRate);
}

ScriptBind_CCProgressTimer::~ScriptBind_CCProgressTimer()
{

}

CCProgressTimer* ScriptBind_CCProgressTimer::Create(CCSprite* sprite)
{
	CCAssert(sprite,"");
	return CCProgressTimer::create(sprite);
}

CCProgressTimerType ScriptBind_CCProgressTimer::GetType(CCProgressTimer* timer)
{
	CCAssert(timer,"");
	return timer->getType();
}

void ScriptBind_CCProgressTimer::SetType(CCProgressTimer* timer,CCProgressTimerType type_)
{
	CCAssert(timer,"");
	timer->setType(type_);
}

float ScriptBind_CCProgressTimer::GetPercentage(CCProgressTimer* timer)
{
	CCAssert(timer,"");
	return timer->getPercentage();
}
void ScriptBind_CCProgressTimer::SetPercentage(CCProgressTimer* timer,float fPercentage)
{
	CCAssert(timer,"");
	timer->setPercentage(fPercentage);
}

void ScriptBind_CCProgressTimer::SetSprite(CCProgressTimer* timer,CCSprite* sprite)
{
	CCAssert(timer,"");
	timer->setSprite(sprite);
}
CCSprite* ScriptBind_CCProgressTimer::GetSprite(CCProgressTimer* timer)
{
	CCAssert(timer,"");
	return timer->getSprite();
}

void ScriptBind_CCProgressTimer::SetReverseProgress(CCProgressTimer* timer, bool reverse)
{
	CCAssert(timer,"");
	timer->setReverseProgress(true);
}

bool ScriptBind_CCProgressTimer::IsReverseDirection(CCProgressTimer* timer)
{
	CCAssert(timer,"");
	return timer->isReverseDirection();
}

void ScriptBind_CCProgressTimer::SetReverseDirection(CCProgressTimer* timer,bool value)
{
	CCAssert(timer,"");
	timer->setReverseDirection(value);
}

void ScriptBind_CCProgressTimer::SetMidPoint(CCProgressTimer* timer,CCPoint& midPoint)
{

}

void ScriptBind_CCProgressTimer::GetMidPoint(CCProgressTimer* timer,CCPoint& midPoint)
{

}

void ScriptBind_CCProgressTimer::SetBarChangeRate(CCProgressTimer* timer,CCPoint& midPoint)
{

}

void ScriptBind_CCProgressTimer::GetBarChangeRate(CCProgressTimer* timer,CCPoint& midPoint)
{

}