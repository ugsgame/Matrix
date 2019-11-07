//#include "stdneb.h"
#include "cocos2d.h"
#include "MCocos2d/MCAction.h"
#include "MCocos2d/MCActionInstant.h"
#include "MCocos2d/MCActionInterval.h"
#include "ScriptBind_CCAction.h"

USING_NS_CC;

class CallFucEvent:public CCObject
{
public:
	CallFucEvent(){}
	~CallFucEvent(){}
	static CallFucEvent* Create(IMonoObject* obj)
	{
		CallFucEvent* event = new CallFucEvent();
		event->setMonoObject(obj);

		if (event)
		{
			event->autorelease();
			return event;
		}
		CC_SAFE_DELETE(event);
		return NULL;
	}

	void CallFuc()
	{
		CCAssert(p_MonoObject,"");
		p_MonoObject->CallMethod("CallFuc");	
	}
protected:
private:
};


ScriptBind_CCAction::ScriptBind_CCAction()
{
	REGISTER_METHOD(IsDone);
	REGISTER_METHOD(GetTag);
	REGISTER_METHOD(SetTag);

	REGISTER_METHOD(SetDuration);
	REGISTER_METHOD(GetDuration);
	REGISTER_METHOD(Reverse);

	REGISTER_METHOD(ActionShow);
	REGISTER_METHOD(ActionHide);
	REGISTER_METHOD(ActionToggleVisibility);
	REGISTER_METHOD(ActionRemoveSelf);
	REGISTER_METHOD(ActionFlipX);
	REGISTER_METHOD(ActionFlipY);
	REGISTER_METHOD(ActionPlace);
	REGISTER_METHOD(ActionCallFunc);

	REGISTER_METHOD(ActionSequence);
	REGISTER_METHOD(ActionSpawn);
	REGISTER_METHOD(ActionRepeat);
	REGISTER_METHOD(ActionRepeatForever);

	REGISTER_METHOD(ActionRotateTo1);
	REGISTER_METHOD(ActionRotateTo2);
	REGISTER_METHOD(ActionRotateBy1);
	REGISTER_METHOD(ActionRotateBy2);
	REGISTER_METHOD(ActionMoveBy);
	REGISTER_METHOD(ActionMoveTo);
	REGISTER_METHOD(ActionSkewTo);
	REGISTER_METHOD(ActionSkewBy);
	REGISTER_METHOD(ActionJumpBy);
	REGISTER_METHOD(ActionJumpTo);
	REGISTER_METHOD(ActionBezierBy);
	REGISTER_METHOD(ActionBezierTo);
	REGISTER_METHOD(ActionScaleTo1);
	REGISTER_METHOD(ActionScaleTo2);
	REGISTER_METHOD(ActionScaleBy1);
	REGISTER_METHOD(ActionScaleBy2);
	REGISTER_METHOD(ActionBlink);
	REGISTER_METHOD(ActionFadeIn);
	REGISTER_METHOD(ActionFadeOut);
	REGISTER_METHOD(ActionFadeTo);
	REGISTER_METHOD(ActionTintTo);
	REGISTER_METHOD(ActionTintBy);
	REGISTER_METHOD(ActionDelayTime);
	REGISTER_METHOD(ActionReverseTime);
	REGISTER_METHOD(ActionProgressTo);
	REGISTER_METHOD(ActionProgressFromTo);
	REGISTER_METHOD(ActionAnimate);

	REGISTER_METHOD(ActionEaseIn);
	REGISTER_METHOD(ActionEaseOut);
	REGISTER_METHOD(ActionEaseInOut);
	REGISTER_METHOD(ActionEaseExponentialIn);
	REGISTER_METHOD(ActionEaseExponentialOut);
	REGISTER_METHOD(ActionEaseExponentialInOut);
	REGISTER_METHOD(ActionEaseSineIn);
	REGISTER_METHOD(ActionEaseSineOut);
	REGISTER_METHOD(ActionEaseSineInOut);
	REGISTER_METHOD(ActionEaseElastic1);
	REGISTER_METHOD(ActionEaseElastic2);
	REGISTER_METHOD(ActionEaseElasticIn1);
	REGISTER_METHOD(ActionEaseElasticIn2);
	REGISTER_METHOD(ActionEaseElasticOut1);
	REGISTER_METHOD(ActionEaseElasticOut2);
	REGISTER_METHOD(ActionEaseElasticInOut1);
	REGISTER_METHOD(ActionEaseElasticInOut2);
	REGISTER_METHOD(ActionEaseBounce);
	REGISTER_METHOD(ActionEaseBounceIn);
	REGISTER_METHOD(ActionEaseBounceOut);
	REGISTER_METHOD(ActionEaseBounceInOut);
	REGISTER_METHOD(ActionEaseBackIn);
	REGISTER_METHOD(ActionEaseBackOut);
	REGISTER_METHOD(ActionEaseBackInOut);

	REGISTER_METHOD(MCAction);
	REGISTER_METHOD(MCActionInterval);
	REGISTER_METHOD(MCActionInstant);

}
ScriptBind_CCAction::~ScriptBind_CCAction()
{

}

//Action
bool ScriptBind_CCAction::IsDone(CCAction* action)
{
	return action->isDone();
}
int  ScriptBind_CCAction::GetTag(CCAction* action)
{
	return action->getTag();
}
void ScriptBind_CCAction::SetTag(CCAction* action,int tag)
{
	action->setTag(tag);
}

//FiniteTimeAction
void ScriptBind_CCAction::SetDuration(CCFiniteTimeAction* action,float time)
{
	action->setDuration(time);
}
float ScriptBind_CCAction::GetDuration(CCFiniteTimeAction* action)
{
	return action->getDuration();
}
CCFiniteTimeAction* ScriptBind_CCAction::Reverse(CCFiniteTimeAction* action)
{
	return action->reverse();
}
//InstantActions
CCAction* ScriptBind_CCAction::ActionShow()
{
	return CCShow::create();
}
CCAction* ScriptBind_CCAction::ActionHide()
{
	return CCHide::create();
}
CCAction* ScriptBind_CCAction::ActionToggleVisibility()
{
	return CCToggleVisibility::create();
}
CCAction* ScriptBind_CCAction::ActionRemoveSelf(bool isNeedCleanUp)
{
	return CCRemoveSelf::create(isNeedCleanUp);
}
CCAction* ScriptBind_CCAction::ActionFlipX(bool x)
{
	return CCFlipX::create(x);
}
CCAction* ScriptBind_CCAction::ActionFlipY(bool y)
{
	return CCFlipY::create(y);
}
CCAction* ScriptBind_CCAction::ActionPlace(float x,float y)
{
	return CCPlace::create(ccp(x,y));
}
CCAction* ScriptBind_CCAction::ActionCallFunc(mono::object obj)
{
	return CCCallFunc::create(CallFucEvent::Create(*obj),callfunc_selector(CallFucEvent::CallFuc));
}
//IntervalActions
CCAction* ScriptBind_CCAction::ActionSequence(mono::object mArray)
{
	
	CCArray* aArray = CCArray::create();
	IMonoArray* moArray = (IMonoArray*)(*mArray);

	for (int i=0;i< moArray->GetSize();i++)
	{
		IMonoObject* mObj   = *moArray->GetItem(i);
		IMonoObject* val	= *(mObj->CallMethod("GetPtr"));
		int* ptr = (int*)(val->GetAnyValue().GetValue());
		CCAction* action =(CCAction*)(*ptr);
		aArray->addObject(action);
		mObj->Release();
	}
	moArray->Release();
	return CCSequence::create(aArray);
}
CCAction* ScriptBind_CCAction::ActionSpawn(mono::object mArray)
{
	
	CCArray* aArray = CCArray::create();
	IMonoArray* moArray = (IMonoArray*)(*mArray);
	
	for (int i=0;i< moArray->GetSize();i++)
	{
		IMonoObject* mObj   = *moArray->GetItem(i);
		IMonoObject* val	= *(mObj->CallMethod("GetPtr"));
		int* ptr = (int*)(val->GetAnyValue().GetValue());
		CCAction* action =(CCAction*)(*ptr);
		aArray->addObject(action);
		mObj->Release();
	}
	moArray->Release();
	return CCSpawn::create(aArray);
}
CCAction* ScriptBind_CCAction::ActionRepeat(CCAction* action,int times)
{
	return CCRepeat::create((CCFiniteTimeAction*)action,times);
}
CCAction* ScriptBind_CCAction::ActionRepeatForever(CCAction* action)
{
	return CCRepeatForever::create((CCActionInterval *)action);
}

CCAction* ScriptBind_CCAction::ActionRotateTo1(float fDuration, float fDeltaAngle)
{
	return CCRotateTo::create(fDuration,fDeltaAngle);
}
CCAction* ScriptBind_CCAction::ActionRotateTo2(float fDuration, float fDeltaAngleX, float fDeltaAngleY)
{
	return CCRotateTo::create(fDuration,fDeltaAngleX,fDeltaAngleY);
}
CCAction* ScriptBind_CCAction::ActionRotateBy1(float fDuration, float fDeltaAngle)
{
	return CCRotateBy::create(fDuration,fDeltaAngle);
}
CCAction* ScriptBind_CCAction::ActionRotateBy2(float fDuration, float fDeltaAngleX, float fDeltaAngleY)
{
	return CCRotateBy::create(fDuration,fDeltaAngleX,fDeltaAngleY);
}
CCAction* ScriptBind_CCAction::ActionMoveBy(float duration, float posX,float posY)
{
	return CCMoveBy::create(duration,ccp(posX,posY));
}
CCAction* ScriptBind_CCAction::ActionMoveTo(float duration, float posX,float posY)
{
	return CCMoveTo::create(duration,ccp(posX,posY));
}
CCAction* ScriptBind_CCAction::ActionSkewTo(float t, float sx, float sy)
{
	return CCSkewTo::create(t,sx,sy);
}
CCAction* ScriptBind_CCAction::ActionSkewBy(float t, float sx, float sy)
{
	return CCSkewBy::create(t,sx,sy);
}
CCAction* ScriptBind_CCAction::ActionJumpBy(float duration, float posX,float posY, float height, int jumps)
{
	return CCJumpBy::create(duration,ccp(posX,posY),height,jumps);
}
CCAction* ScriptBind_CCAction::ActionJumpTo(float duration, float posX,float posY, float height, int jumps)
{
	return CCJumpTo::create(duration,ccp(posX,posY),height,jumps);
}
CCAction* ScriptBind_CCAction::ActionBezierBy(float duration, float pos1X,float pos1Y,float pos2X,float pos2Y,float pos3X,float pos3Y)
{
	ccBezierConfig c;
	c.controlPoint_1 = ccp(pos1Y,pos2X);
	c.controlPoint_2 = ccp(pos2X,pos2Y);
	c.endPosition = ccp(pos3X,pos3Y);
	return CCBezierBy::create(duration,c);
}
CCAction* ScriptBind_CCAction::ActionBezierTo(float duration, float pos1X,float pos1Y,float pos2X,float pos2Y,float pos3X,float pos3Y)
{
	ccBezierConfig c;
	c.controlPoint_1 = ccp(pos1Y,pos2X);
	c.controlPoint_2 = ccp(pos2X,pos2Y);
	c.endPosition = ccp(pos3X,pos3Y);
	return CCBezierTo::create(duration,c);	
}
CCAction* ScriptBind_CCAction::ActionScaleTo1(float duration, float s)
{
	return CCScaleTo::create(duration,s);
}
CCAction* ScriptBind_CCAction::ActionScaleTo2(float duration, float sx, float sy)
{
	return CCScaleTo::create(duration,sx,sy);
}
CCAction* ScriptBind_CCAction::ActionScaleBy1(float duration, float s)
{
	return CCScaleBy::create(duration,s);
}
CCAction* ScriptBind_CCAction::ActionScaleBy2(float duration, float sx, float sy)
{
	return CCScaleBy::create(duration,sx,sy);
}
CCAction* ScriptBind_CCAction::ActionBlink(float duration,int uBlinks)
{
	return CCBlink::create(duration,uBlinks);
}
CCAction* ScriptBind_CCAction::ActionFadeIn(float time)
{
	return CCFadeIn::create(time);
}
CCAction* ScriptBind_CCAction::ActionFadeOut(float time)
{
	return CCFadeOut::create(time);
}
CCAction* ScriptBind_CCAction::ActionFadeTo(float time,int opacity)
{
	return CCFadeTo::create(time,opacity);
}
CCAction* ScriptBind_CCAction::ActionTintTo(float duration, int red, int green, int blue)
{
	return CCTintTo::create(duration,red,green,blue);
}
CCAction* ScriptBind_CCAction::ActionTintBy(float duration, int red, int green, int blue)
{
	return CCTintBy::create(duration,red,green,blue);
}
CCAction* ScriptBind_CCAction::ActionDelayTime(float duration)
{
	return CCDelayTime::create(duration);
}
CCAction* ScriptBind_CCAction::ActionReverseTime(CCAction* action)
{
	return CCReverseTime::create((CCFiniteTimeAction *)action);
}
CCAction* ScriptBind_CCAction::ActionProgressTo(float duration, float fPercent)
{
	return CCProgressTo::create(duration,fPercent);
}
CCAction* ScriptBind_CCAction::ActionProgressFromTo(float duration, float fFromPercentage, float fToPercentage)
{
	return CCProgressFromTo::create(duration, fFromPercentage, fToPercentage);
}
CCAction* ScriptBind_CCAction::ActionAnimate(CCAnimation* animation)
{
	return CCAnimate::create(animation);
}
//EaseActions
CCAction* ScriptBind_CCAction::ActionEaseIn(CCAction* action, float fRate)
{
	return CCEaseIn::create((CCActionInterval*)action,fRate);
}
CCAction* ScriptBind_CCAction::ActionEaseOut(CCAction* action, float fRate)
{
	return CCEaseOut::create((CCActionInterval*)action,fRate);
}
CCAction* ScriptBind_CCAction::ActionEaseInOut(CCAction* action, float fRate)
{
	return CCEaseInOut::create((CCActionInterval*)action,fRate);
}
CCAction* ScriptBind_CCAction::ActionEaseExponentialIn(CCAction* action)
{
	return CCEaseExponentialIn::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseExponentialOut(CCAction* action)
{
	return CCEaseExponentialOut::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseExponentialInOut(CCAction* action)
{
	return CCEaseExponentialInOut::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseSineIn(CCAction* action)
{
	return CCEaseSineIn::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseSineOut(CCAction* action)
{
	return CCEaseSineInOut::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseSineInOut(CCAction* action)
{
	return CCEaseSineInOut::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseElastic1(CCAction* action)
{
	return CCEaseElastic::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseElastic2(CCAction* action,float fPeriod)
{
	return CCEaseElastic::create((CCActionInterval*)action,fPeriod);
}
CCAction* ScriptBind_CCAction::ActionEaseElasticIn1(CCAction* action)
{
	return CCEaseElasticIn::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseElasticIn2(CCAction* action,float fPeriod)
{
	return CCEaseElasticIn::create((CCActionInterval*)action,fPeriod);
}
CCAction* ScriptBind_CCAction::ActionEaseElasticOut1(CCAction* action)
{
	return CCEaseElasticOut::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseElasticOut2(CCAction* action,float fPeriod)
{
	return CCEaseElasticOut::create((CCActionInterval*)action,fPeriod);
}
CCAction* ScriptBind_CCAction::ActionEaseElasticInOut1(CCAction* action)
{
	return CCEaseElasticInOut::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseElasticInOut2(CCAction* action,float fPeriod)
{
	return CCEaseElasticInOut::create((CCActionInterval*)action,fPeriod);
}
CCAction* ScriptBind_CCAction::ActionEaseBounce(CCAction* action)
{
	return CCEaseBounce::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseBounceIn(CCAction* action)
{
	return CCEaseBounceIn::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseBounceOut(CCAction* action)
{
	return CCEaseBounceOut::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseBounceInOut(CCAction* action)
{
	return CCEaseBounceInOut::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseBackIn(CCAction* action)
{
	return CCEaseBackIn::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseBackOut(CCAction* action)
{
	return CCEaseBackOut::create((CCActionInterval*)action);
}
CCAction* ScriptBind_CCAction::ActionEaseBackInOut(CCAction* action)
{
	return CCEaseBackInOut::create((CCActionInterval*)action);
}

CCAction* ScriptBind_CCAction::MCAction(float duration)
{
	return MCAction::create(duration);
}
CCAction* ScriptBind_CCAction::MCActionInterval(float duration)
{
	return MCActionInterval::create(duration);
}
CCAction* ScriptBind_CCAction::MCActionInstant()
{
	return MCActionInstant::create();
}