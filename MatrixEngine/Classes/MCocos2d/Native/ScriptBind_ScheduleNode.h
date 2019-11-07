
#ifndef __SCRIPTBIND_SCHEDULENODE__
#define __SCRIPTBIND_SCHEDULENODE__

#include "ScriptBind_Cocos2d.h"

class MCScheduleNode;

class ScriptBind_ScheduleNode:public ScriptBind_Cocos2d
{
public:
	ScriptBind_ScheduleNode();
	~ScriptBind_ScheduleNode();

	virtual const char* GetClassName(){ return "NativeScheduleNode";}

	static MCScheduleNode* Create(mono::object klass,mono::string method, float dt);

	static void Schedule(MCScheduleNode* pNode);
	static void Unschedule(MCScheduleNode* pNode);
	static bool IsScheduling(MCScheduleNode* pNode);

	static mono::string GetMethodName(MCScheduleNode* pNode);
	static void SetMethodName(MCScheduleNode* pNode,mono::string method);

	static void SetDelayTime(MCScheduleNode* pNode,float dt);
	static float GetDelayTime(MCScheduleNode* pNode);
};

#endif