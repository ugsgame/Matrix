#include"OBB.h"
#include<math.h>

OBB::OBB(CCRect rect, float rot) :
	rotation(rot),
	width(rect.size.width),
	height(rect.size.height),
	centerPoint(ccp(rect.origin.x, rect.origin.y)),
	_extents(CCSizeMake(rect.size.width / 2, rect.size.height /2))
{
	OBBVector2 v1;
	v1.x = cos(rot); v1.y = sin(rot);
	_axes1 = v1;

	OBBVector2 v2;
	v2.x = -1 * sin(rot); v2.y = cos(rot);
	_axes2 = v2;
}

bool OBB::isCollision(OBB obb1, OBB obb2)
{
	CCPoint sub = obb1.centerPoint - obb2.centerPoint;
	OBBVector2 nv;
	nv.x = sub.x; nv.y = sub.y;
	OBBVector2 axisA1 = obb1._axes1;
	if (obb1.getProjectionRadius(axisA1) + obb2.getProjectionRadius(axisA1) <= fabs(obbVectorDot(nv, axisA1))) {
		return false;
	}
	OBBVector2 axisA2 = obb1._axes2;
	if (obb1.getProjectionRadius(axisA2) + obb2.getProjectionRadius(axisA2) <= fabs(obbVectorDot(nv, axisA2))) {
		return false;
	}
	OBBVector2 axisB1 = obb2._axes1;
	if (obb1.getProjectionRadius(axisB1) + obb2.getProjectionRadius(axisB1) <= fabs(obbVectorDot(nv, axisB1))) {
		return false;
	}
	OBBVector2 axisB2 = obb2._axes2;
	if (obb1.getProjectionRadius(axisB2) + obb2.getProjectionRadius(axisB2) <= fabs(obbVectorDot(nv, axisB2))) {
		return false;
	}
	return true;
}

float OBB::getProjectionRadius(OBBVector2 vec)
{
	return _extents.width * fabs(obbVectorDot(vec, _axes1)) + _extents.height * fabs(obbVectorDot(vec, _axes2));
}

float OBB::obbVectorDot(OBBVector2 vec1, OBBVector2 vec2)
{
	return vec1.x * vec2.x + vec1.y * vec2.y;
}

bool OBB::isValidity()
{
	if (width == 0 && height == 0)
	{
		return false;
	}
	return true;
}
