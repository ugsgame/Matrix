
#ifndef __SCRIPTBIND_CCPROGRESSTIMER__
#define __SCRIPTBIND_CCPROGRESSTIMER__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCProgressTimer;
class cocos2d::CCSprite;
class cocos2d::CCPoint;

class ScriptBind_CCProgressTimer:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCProgressTimer();
	~ScriptBind_CCProgressTimer();

	virtual const char* GetClassName(){ return "NativeProgressTimer";}

	static  cocos2d::CCProgressTimer* Create(cocos2d::CCSprite* sprite);

	static  cocos2d::CCProgressTimerType GetType(cocos2d::CCProgressTimer* timer);
	static  void SetType(cocos2d::CCProgressTimer* timer,cocos2d::CCProgressTimerType type_);

	static  float GetPercentage(cocos2d::CCProgressTimer* timer);
	static  void SetPercentage(cocos2d::CCProgressTimer* timer,float fPercentage);

	static  void SetSprite(cocos2d::CCProgressTimer* timer,cocos2d::CCSprite* sprite);
	static  cocos2d::CCSprite* GetSprite(cocos2d::CCProgressTimer* timer);

	static  void SetReverseProgress(cocos2d::CCProgressTimer* timer, bool reverse);
	
	static  bool IsReverseDirection(cocos2d::CCProgressTimer* timer);
	static  void SetReverseDirection(cocos2d::CCProgressTimer* timer,bool value);

	static  void SetMidPoint(cocos2d::CCProgressTimer* timer,cocos2d::CCPoint& midPoint);
	static  void GetMidPoint(cocos2d::CCProgressTimer* timer,cocos2d::CCPoint& midPoint);

	static  void SetBarChangeRate(cocos2d::CCProgressTimer* timer,cocos2d::CCPoint& midPoint);
	static  void GetBarChangeRate(cocos2d::CCProgressTimer* timer,cocos2d::CCPoint& midPoint);
protected:
private:
};

#endif