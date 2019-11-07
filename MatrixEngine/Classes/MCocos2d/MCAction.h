
#ifndef __MATRIX_ACTION__
#define __MATRIX_ACTION__

#include "cocos2d.h"
#include "MatrixMono.h"

USING_NS_CC;

class MCAction:public CCActionInterval
{
public:
	bool initWithDuration(float fDuration);


	virtual CCObject* copyWithZone(CCZone* pZone);
	virtual void startWithTarget(CCNode *pTarget);
	virtual CCActionInterval* reverse(void);
	virtual void update(float time);
	virtual void stop(void);

	static MCAction* create(float fDuration);
};

#endif