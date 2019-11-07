
#include "MCAction.h"

MCAction* MCAction::create(float fDuration)
{
	MCAction *pRet = new MCAction(); 
	pRet->initWithDuration(fDuration);
	pRet->autorelease();

	return pRet;
}

bool MCAction::initWithDuration(float fDuration)
{
	if (CCActionInterval::initWithDuration(fDuration))
	{
		return true;
	}

	return false;
}

CCObject* MCAction::copyWithZone(CCZone* pZone)
{
	MCAction* pCopy = NULL;
	IMonoObject* obj;

	if(pZone && pZone->m_pCopyObject)
	{
		//in case of being called at sub class
		pCopy = (MCAction*)(pZone->m_pCopyObject);
		obj = ((MCAction*)(pZone->m_pCopyObject))->getMonoObject();
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

void MCAction::startWithTarget(CCNode* pTarget)
{ 
	CCActionInterval::startWithTarget(pTarget);
	if(this->isBindMonoObject())
	{
		this->getMonoObject()->CallMethod("native_StartWithTarget",pTarget->getMonoObject()->GetManagedObject());
	}
}

CCActionInterval* MCAction::reverse(void)
{
	CCActionInterval* _action = NULL;
	if(this->isBindMonoObject())
	{
		IMonoObject* action = *(this->getMonoObject()->CallMethod("native_Reverse"));
		_action = (CCActionInterval*)(action->GetAnyValue().i);
	}

	return _action;
}

void MCAction::update(float time)
{
	if(m_pTarget&& this->isBindMonoObject())
	{
		this->getMonoObject()->CallMethod("native_OnUpdate",(float)time);
	}
}

void MCAction::stop()
{
	if(this->isBindMonoObject())
	{
		this->getMonoObject()->CallMethod("native_OnStop");
	}
}