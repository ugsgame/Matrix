
#ifndef __MATRIX_ACTIONINSTANT__
#define __MATRIX_ACTIONINSTANT__

#include "cocos2d.h"
#include "MatrixMono.h"

USING_NS_CC;

class MCActionInstant:public CCActionInstant
{
public:
	virtual CCObject* copyWithZone(CCZone* pZone);
	virtual CCActionInstant* reverse(void);
	virtual void update(float time);

	static MCActionInstant* create();
};

#endif