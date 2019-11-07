
#ifndef __BOXCOLLIDER_H__
#define __BOXCOLLIDER_H__

#include "Math/Size.h"

#include "Box2D/Box2D.h"
#include "Collider.h"

using namespace Math;

class GameActor;

class BoxCollider:public Collider
{
	//__DeclareClass(BoxCollider);
public:
	BoxCollider();
	~BoxCollider();

public:
	static BoxCollider* Create();
	static BoxCollider* Create(GameActor* actor,IMonoObject* obj);

	void SetSize(float w,float h);
	void SetSize(sizef& size);
	const sizef& GetSize(void);

	float GetWidth();
	float GetHeight();
protected:
	virtual void CreateCollider();
private:
	sizef mSize;
};

#endif