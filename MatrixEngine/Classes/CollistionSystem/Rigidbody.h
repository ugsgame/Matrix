
#ifndef __RIGIDBODY_H__
#define __RIGIDBODY_H__


//#include "core/refcounted.h"
#include "Box2D/Box2D.h"

#include "Entity/Entity.h"
#include "Entity/Transform.h"
#include "Component/Component.h"

class  Collider;
class  GameActor;
struct Transform;

//��������
enum RigidbodyType
{
	StaticBody = 0,
	KinematicBody,
	DynamicBody
};
//
class Rigidbody:public Component
{
	//__DeclareClass(Rigidbody);
public:
	Rigidbody();
	~Rigidbody();

public: 
	static Rigidbody* Create();
	static Rigidbody* Create(GameActor* actor,IMonoObject* obj);
	
	virtual void SetTransform(Transform& tnf);
	virtual const Transform& GetTransform();

	virtual void SetActor(GameActor* actor);
	
	void SetBodyType(RigidbodyType type);
	RigidbodyType GetBodyType(void);

	b2FixtureDef* GetFixtureDef(){ return mFixtureDef; }
	b2Body* GetBody(){ return mBody; }
	//Ħ����
	void  SetFriction(float friction);
	float GetFriction(void);
	//����
	void  SetRestitution(float restitution);
	float GetRestitution(void);
	//�ܶ�
	void  SetDensity(float density);
	float GetDensity(void);
public:
	//TODO
	virtual void OnUpdate(float fDelta){}; 
protected:
	void CreateRigidbody();
private:
	float mRatio; 

	b2BodyDef*	mBodyDef;
	b2FixtureDef* mFixtureDef;
	b2Body* mBody ;

	b2World* mWorld;

	Transform mTransform;
};

#endif