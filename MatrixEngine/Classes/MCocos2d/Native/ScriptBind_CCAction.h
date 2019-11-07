
#ifndef __SCRIPTBIND_CCACTION__
#define __SCRIPTBIND_CCACTION__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCAction;
class cocos2d::CCFiniteTimeAction;
class cocos2d::CCAnimation;

class ScriptBind_CCAction:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCAction();
	~ScriptBind_CCAction();

	virtual const char* GetClassName(){ return "NativeAction";}

	//Action
	static bool IsDone(cocos2d::CCAction* action);
	static int  GetTag(cocos2d::CCAction* action);
	static void SetTag(cocos2d::CCAction* action,int tag);

	//FiniteTimeAction
	static void SetDuration(cocos2d::CCFiniteTimeAction* action,float time);
	static float GetDuration(cocos2d::CCFiniteTimeAction* action);
	static  cocos2d::CCFiniteTimeAction* Reverse(cocos2d::CCFiniteTimeAction* action);
	//InstantActions
	static cocos2d::CCAction* ActionShow();
	static cocos2d::CCAction* ActionHide();
	static cocos2d::CCAction* ActionToggleVisibility();
	static cocos2d::CCAction* ActionRemoveSelf(bool isNeedCleanUp);
	static cocos2d::CCAction* ActionFlipX(bool x);
	static cocos2d::CCAction* ActionFlipY(bool y);
	static cocos2d::CCAction* ActionPlace(float x,float y);
	static cocos2d::CCAction* ActionCallFunc(mono::object obj);
	//IntervalActions
	static cocos2d::CCAction* ActionSequence(mono::object mArray);
	static cocos2d::CCAction* ActionSpawn(mono::object mArray);
	static cocos2d::CCAction* ActionRepeat(cocos2d::CCAction* action,int times);
	static cocos2d::CCAction* ActionRepeatForever(cocos2d::CCAction* action);

	static cocos2d::CCAction* ActionRotateTo1(float fDuration, float fDeltaAngle);
	static cocos2d::CCAction* ActionRotateTo2(float fDuration, float fDeltaAngleX, float fDeltaAngleY);
	static cocos2d::CCAction* ActionRotateBy1(float fDuration, float fDeltaAngle);
	static cocos2d::CCAction* ActionRotateBy2(float fDuration, float fDeltaAngleX, float fDeltaAngleY);
	static cocos2d::CCAction* ActionMoveBy(float duration, float posX,float posY);
	static cocos2d::CCAction* ActionMoveTo(float duration, float posX,float posY);
	static cocos2d::CCAction* ActionSkewTo(float t, float sx, float sy);
	static cocos2d::CCAction* ActionSkewBy(float t, float sx, float sy);
	static cocos2d::CCAction* ActionJumpBy(float duration, float posX,float posY, float height, int jumps);
	static cocos2d::CCAction* ActionJumpTo(float duration, float posX,float posY, float height, int jumps);
	static cocos2d::CCAction* ActionBezierBy(float duration, float pos1X,float pos1Y,float pos2X,float pos2Y,float pos3X,float pos3Y);
	static cocos2d::CCAction* ActionBezierTo(float duration, float pos1X,float pos1Y,float pos2X,float pos2Y,float pos3X,float pos3Y);
	static cocos2d::CCAction* ActionScaleTo1(float duration, float s);
	static cocos2d::CCAction* ActionScaleTo2(float duration, float sx, float sy);
	static cocos2d::CCAction* ActionScaleBy1(float duration, float s);
	static cocos2d::CCAction* ActionScaleBy2(float duration, float sx, float sy);
	static cocos2d::CCAction* ActionBlink(float duration,int uBlinks);
	static cocos2d::CCAction* ActionFadeIn(float time);
	static cocos2d::CCAction* ActionFadeOut(float time);
	static cocos2d::CCAction* ActionFadeTo(float time,int opacity);
	static cocos2d::CCAction* ActionTintTo(float duration, int red, int green, int blue);
	static cocos2d::CCAction* ActionTintBy(float duration, int red, int green, int blue);
	static cocos2d::CCAction* ActionDelayTime(float duration);
	static cocos2d::CCAction* ActionReverseTime(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionProgressTo(float duration, float fPercent);
	static cocos2d::CCAction* ActionProgressFromTo(float duration, float fFromPercentage, float fToPercentage);
	static cocos2d::CCAction* ActionAnimate(cocos2d::CCAnimation* animation);
	//EaseActions
	static cocos2d::CCAction* ActionEaseIn(cocos2d::CCAction* action, float fRate);
	static cocos2d::CCAction* ActionEaseOut(cocos2d::CCAction* action, float fRate);
	static cocos2d::CCAction* ActionEaseInOut(cocos2d::CCAction* action, float fRate);
	static cocos2d::CCAction* ActionEaseExponentialIn(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseExponentialOut(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseExponentialInOut(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseSineIn(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseSineOut(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseSineInOut(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseElastic1(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseElastic2(cocos2d::CCAction* action,float fPeriod);
	static cocos2d::CCAction* ActionEaseElasticIn1(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseElasticIn2(cocos2d::CCAction* action,float fPeriod);
	static cocos2d::CCAction* ActionEaseElasticOut1(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseElasticOut2(cocos2d::CCAction* action,float fPeriod);
	static cocos2d::CCAction* ActionEaseElasticInOut1(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseElasticInOut2(cocos2d::CCAction* action,float fPeriod);
	static cocos2d::CCAction* ActionEaseBounce(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseBounceIn(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseBounceOut(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseBounceInOut(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseBackIn(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseBackOut(cocos2d::CCAction* action);
	static cocos2d::CCAction* ActionEaseBackInOut(cocos2d::CCAction* action);
	//À©Õ¹action
	static cocos2d::CCAction* MCAction(float duration);
	static cocos2d::CCAction* MCActionInterval(float duration);
	static cocos2d::CCAction* MCActionInstant();
};


#endif