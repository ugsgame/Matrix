
#ifndef __TRANSFORM_H__
#define __TRANSFORM_H__

#include "cocos2d.h"

USING_NS_CC;

struct Transform
{
	/// The default constructor does nothing.
	Transform() {}

	/// Initialize using a position vector and a rotation.
	Transform(const CCPoint& position, float rotation) : p(position), q(rotation) {}

	/// Set this based on the position and angle.
	void Set(const CCPoint& position, float32 angle)
	{
		p = position;
		q = angle;
	}

	CCPoint p;
	float q;
};

#endif