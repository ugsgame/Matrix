
#include "cocos2d.h"
#include "CCDelayRelease.h"
#include "pthread.h"

NS_CC_BEGIN

static CCDelayRelease* s_pDelayRelease = NULL;

CCDelayRelease::CCDelayRelease()
{	
	verify = 0;
	releasePool = new std::stack<CCObject*>();
	pthread_mutex_init(&m_pushMutex, NULL);
}

CCDelayRelease::~CCDelayRelease()
{
	CC_SAFE_DELETE(releasePool);
	pthread_mutex_destroy(&m_pushMutex);
}

void CCDelayRelease::push(CCObject* pObject)
{
	pthread_mutex_lock(&m_pushMutex);
	releasePool->push(pObject);
	verify++;
	pthread_mutex_unlock(&m_pushMutex);
}

void CCDelayRelease::pop()
{
	pthread_mutex_lock(&m_pushMutex);
	if(!releasePool->empty())
	{
		if(releasePool->size()>100)
		{
			for (int i=0;i<100;i++)
			{
				CCObject* pObject = releasePool->top();
				CC_SAFE_RELEASE(pObject);
				releasePool->pop();
				verify--;
			}
		}
		else
		{
			CCObject* pObject = releasePool->top();
			CC_SAFE_RELEASE(pObject);
			releasePool->pop();
			verify--;
		}
	}
	pthread_mutex_unlock(&m_pushMutex);
}

int CCDelayRelease::size()
{
	return releasePool->size();
}

CCDelayRelease* CCDelayRelease::sharedDelayRelease()
{
	if(s_pDelayRelease == NULL)
	{
		s_pDelayRelease = new CCDelayRelease();
	}
	return s_pDelayRelease;
}

void CCDelayRelease::purgeDelayRelease()
{
	CC_SAFE_DELETE(s_pDelayRelease);
}


NS_CC_END