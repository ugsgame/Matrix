
#ifndef __COLLIDER__
#define __COLLIDER__

#include "MatrixMono.h"

#include "Box2D/Box2D.h"
#include "ActorSystem/GameActor.h"
#include "Component/Component.h"

class Rigidbody;
class Component;

enum ColliderShape
{
	EdgeShape = 0,		//��
	BoxShape,			//����
	CircleShape,		//Բ
	PolygonShape		//�����
};

class Collider:public Component
{
public:
	Collider();
	~Collider();

public:
	virtual void SetActor(GameActor* actor);
	virtual GameActor* GetActor();
	
	virtual void SetTrigger(bool trigger);
	virtual bool IsTrigger();

	virtual ColliderShape GetColliderShape(void){ return mShapeType; }
public:
	//�ص�����
	//TODO
	virtual void OnUpdate(float fDelta){}; 
protected:
	virtual void CreateCollider() = 0; 
protected:
	//
	Rigidbody* m_pRigidbody;
	//
	b2Shape* m_pShape;

	ColliderShape mShapeType;
private:
	bool m_bTrigger;
	float mRatio;
};

#endif