
#include "MCActionInterval.h"

MCActionInterval* MCActionInterval::create(float fDuration)
{
	MCActionInterval *pRet = new MCActionInterval(); 
	pRet->initWithDuration(fDuration);
	pRet->autorelease();

	return pRet;
}

bool MCActionInterval::initWithDuration(float fDuration)
{
	if (CCActionInterval::initWithDuration(fDuration))
	{
		return true;
	}

	return false;
}

CCObject* MCActionInterval::copyWithZone(CCZone* pZone)
{
	MCActionInterval* pCopy = NULL;
	IMonoObject* obj;

	if(pZone && pZone->m_pCopyObject)
	{
		//in case of being called at sub class
		pCopy = (MCActionInterval*)(pZone->m_pCopyObject);
		obj = ((MCActionInterval*)(pZone->m_pCopyObject))->getMonoObject();
	}
	else
	{
		//没有c#对象就只反回空的
		return NULL;
	}

	CCActionInterval::copyWithZone(pZone);

	pCopy->initWithDuration(m_fDuration);

	//Action *copy = [[[self class] allocWithZone: zone] initWithDuration:[self duration] angle: angle];
	return pCopy;	
}

void MCActionInterval::startWithTarget(CCNode* pTarget)
{ 
	CCActionInterval::startWithTarget(pTarget);
	if(this->isBindMonoObject())
	{
		this->getMonoObject()->CallMethod("native_StartWithTarget",pTarget->getMonoObject()->GetManagedObject());
	}
}

CCActionInterval* MCActionInterval::reverse(void)
{
	CCActionInterval* _action = NULL;
	if(this->isBindMonoObject())
	{
		IMonoObject* action = *(this->getMonoObject()->CallMethod("native_Reverse"));
		_action = (CCActionInterval*)(action->GetAnyValue().i);
	}

	return _action;
}

void MCActionInterval::update(float time)
{
	if(m_pTarget&& this->isBindMonoObject())
	{
		this->getMonoObject()->CallMethod("native_OnUpdate",(float)time);
	}
}

void MCActionInterval::stop()
{
	if(this->isBindMonoObject())
	{
		this->getMonoObject()->CallMethod("native_OnStop");
	}
}