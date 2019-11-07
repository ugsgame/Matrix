
#ifndef __MC_SCHEDULENODE__
#define __MC_SCHEDULENODE__

#include "cocos2d.h"
#include "MatrixMono.h"

USING_NS_CC;

class MCScheduleNode:public CCNode
{
	//__ScriptBind
public:
	MCScheduleNode();
	~MCScheduleNode();

	static MCScheduleNode* create(IMonoObject* object,std::string method, float dt);

	void scheduleSelf();
	void unscheduleSelf();
	bool isScheduling()
	{
		//return this->isScheduled(schedule_selector(MCScheduleNode::scheduleMethod));
		return false;
	}

	std::string getMethodName()
	{
		return scheduleMethodName;
	}

	void setMethodName(std::string name)
	{
		scheduleMethodName = name;
	}

	void setDelayTime(float dTime)
	{
		delayTime = dTime;
	}

	float getDelayTime()
	{
		return delayTime;
	}

protected:
	void SetScript(IMonoObject* script);
	IMonoObject* GetScript(void){ return pScript;}

	IMonoObject* pScript; 

protected:

	void scheduleMethod(float dt);

private:

	std::string scheduleMethodName;
	float delayTime;
};

#endif