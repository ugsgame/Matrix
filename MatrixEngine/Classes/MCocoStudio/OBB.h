
#ifndef __CC_OBB_H__
#define __CC_OBB_H__

#include"cocos2d.h"

USING_NS_CC;

struct OBBVector2 {
	float x, y;
};

class OBB : public CCObject
{

public:

	OBB(CCRect rect, float rotation);

	static bool isCollision(OBB obb1, OBB obb2);

	static float obbVectorDot(OBBVector2 vec1, OBBVector2 vec2);

	float getProjectionRadius(OBBVector2 vec);

	bool isValidity();

public:
	float width;
	float height;
	float rotation;
	CCPoint centerPoint;

private:
	CCSize _extents;
	OBBVector2 _axes1;
	OBBVector2 _axes2;
};

#endif// __CCOBB_H__
