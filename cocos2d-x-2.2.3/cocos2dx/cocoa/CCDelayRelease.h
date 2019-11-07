

#ifndef __DELAYRELEASE_H__
#define __DELAYRELEASE_H__

#include "CCObject.h"
#include <queue>
#include <stack>

#ifndef	ANDROID
struct pthread_mutex_t_;
typedef struct pthread_mutex_t_ * pthread_mutex_t;
#endif

NS_CC_BEGIN

class CC_DLL CCDelayRelease
{
	unsigned int verify;
	pthread_mutex_t		m_pushMutex;
	std::stack<CCObject*>* releasePool;
public:
	CCDelayRelease();
	~CCDelayRelease();

	void push(CCObject* pObject);
	void pop();
	int size();

	static CCDelayRelease* sharedDelayRelease();
    static void purgeDelayRelease();
private:

};

NS_CC_END

#endif //