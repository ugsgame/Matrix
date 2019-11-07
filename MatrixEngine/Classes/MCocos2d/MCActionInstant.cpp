
#include "MCActionInstant.h"

MCActionInstant* MCActionInstant::create()
{
	MCActionInstant *pRet = new MCActionInstant(); 
	pRet->autorelease();

	return pRet;
}

CCObject* MCActionInstant::copyWithZone(CCZone* pZone)
{
	MCActionInstant* pCopy = NULL;
	IMonoObject* obj;

	if(pZone && pZone->m_pCopyObject)
	{
		//in case of being called at sub class
		pCopy = (MCActionInstant*)(pZone->m_pCopyObject);
		obj = ((MCActionInstant*)(pZone->m_pCopyObject))->getMonoObject();
	}
	else
	{
		//没有c#对象就只反回空的
		return NULL;
	}

	CCActionInstant::copyWithZone(pZone);
	//Action *copy = [[[self class] allocWithZone: zone] initWithDuration:[self duration] angle: angle];
	return pCopy;	
}


CCActionInstant* MCActionInstant::reverse(void)
{
	CCActionInstant* _action = NULL;
	if(this->isBindMonoObject())
	{
		IMonoObject* action = *(this->getMonoObject()->CallMethod("native_Reverse"));
		_action = (CCActionInstant*)(action->GetAnyValue().i);
	}

	return _action;
}

void MCActionInstant::update(float time)
{
	CC_UNUSED_PARAM(time);
	if(m_pTarget&& this->isBindMonoObject())
	{
		this->getMonoObject()->CallMethod("native_OnUpdate",m_pTarget->getMonoObject()->GetManagedObject());
	}
}
