
#include "cocos2d.h"

#include "Rigidbody.h"
#include "Collider.h"
#include "CollistionSystem.h"

USING_NS_CC;

//__ImplementClass(Rigidbody,'RIGI',Core::RefCounted)

Rigidbody::Rigidbody():
	mBodyDef(new b2BodyDef()),
	mFixtureDef(new b2FixtureDef()),
	mBody(NULL)
{
	SetName("Rigidbody");
	mBodyDef->type = b2_dynamicBody;
	mBodyDef->bullet = true;

	mWorld = CollistionSystem::ShareCollistionSystem()->GetWorld();
	mRatio = CollistionSystem::ShareCollistionSystem()->GetRatio();
}

Rigidbody::~Rigidbody()
{
	mWorld->DestroyBody(mBody);
	SAFE_DELETE(mBodyDef);
	SAFE_DELETE(mFixtureDef);
}

Rigidbody* Rigidbody::Create()
{
	return new Rigidbody();
}

Rigidbody* Rigidbody::Create(GameActor* actor,IMonoObject* obj)
{
	Rigidbody* body = (Rigidbody*)Rigidbody::Create();
	body->SetActor(actor);
	body->CreateRigidbody();
	body->SetMonoObject(obj);
	return body;
}

void Rigidbody::SetTransform(Transform& tnf)
{
	mBody->SetTransform(b2Vec2(tnf.p.x/mRatio,tnf.p.y/mRatio),- tnf.q);
}

const Transform& Rigidbody::GetTransform()
{
	mTransform.p.x = mBody->GetPosition().x*mRatio;
	mTransform.p.y = mBody->GetPosition().y*mRatio;
	mTransform.q = - mBody->GetAngle();
	return mTransform;
}

void Rigidbody::SetActor(GameActor* actor)
{
	Component::SetActor(actor);
	mBodyDef->userData = actor;
}

void Rigidbody::SetBodyType(RigidbodyType type)
{
	switch(type)
	{
	case StaticBody:
		mBodyDef->type = b2_staticBody;
		break;
	case KinematicBody:
		mBodyDef->type = b2_kinematicBody;
		break;
	case DynamicBody:
		mBodyDef->type = b2_dynamicBody;
		break;
	default:
		mBodyDef->type = b2_staticBody;
	}
}
RigidbodyType Rigidbody::GetBodyType(void)
{
	return (RigidbodyType)mBody->GetType();
}

//Ä¦²ÁÁ¦
void  Rigidbody::SetFriction(float friction)
{
	mFixtureDef->friction = friction;
}
float Rigidbody::GetFriction(void)
{
	return mFixtureDef->friction;
}
//µ¯Á¦
void  Rigidbody::SetRestitution(float restitution)
{
	mFixtureDef->restitution = restitution;
}
float Rigidbody::GetRestitution(void)
{
	return mFixtureDef->friction;
}
//ÃÜ¶È
void  Rigidbody::SetDensity(float density)
{
	mFixtureDef->density = density;
}
float Rigidbody::GetDensity(void)
{
	return mFixtureDef->density;
}

void Rigidbody::CreateRigidbody()
{
	mBody = mWorld->CreateBody(mBodyDef);
}