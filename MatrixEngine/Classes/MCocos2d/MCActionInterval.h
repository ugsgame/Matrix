
#ifndef __MATRIX_ACTIONINTERVAL__
#define __MATRIX_ACTIONINTERVAL__

#include "cocos2d.h"
#include "MatrixMono.h"

USING_NS_CC;

class MCActionInterval:public CCActionInterval
{
public:
	bool initWithDuration(float fDuration);


	virtual CCObject* copyWithZone(CCZone* pZone);
	virtual void startWithTarget(CCNode *pTarget);
	virtual CCActionInterval* reverse(void);
	virtual void update(float time);
	virtual void stop(void);

	static MCActionInterval* create(float fDuration);
};

#endif