#ifndef __SCRIPTBIND_SCHEDULE__
#define __SCRIPTBIND_SCHEDULE__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCScheduler;

class ScriptBind_Scheduler:public ScriptBind_Cocos2d
{
public:
	ScriptBind_Scheduler();
	~ScriptBind_Scheduler();

	virtual const char* GetClassName(){ return "NativeScheduler";}

protected:
	static cocos2d::CCScheduler* Create();

	static float GetTimeScale(cocos2d::CCScheduler* scheduler);
	static void SetTimeScale(cocos2d::CCScheduler* scheduler, float timeScale);

};

#endif