
//#include "core/refcounted.h"

#include "BoxCollider.h"
#include "Rigidbody.h"


//__ImplementClass(BoxCollider,'BOXC',Core::RefCounted);

BoxCollider::BoxCollider():
	mSize(sizef(0.5,0.5))
{
	SetName("BoxCollider");
	mShapeType = BoxShape;
	m_pShape = new b2PolygonShape();

    ((b2PolygonShape*)m_pShape)->SetAsBox(mSize.width,mSize.height,b2Vec2(0,0),0);
}

BoxCollider::~BoxCollider()
{
	SAFE_DELETE(m_pShape);
}

BoxCollider* BoxCollider::Create()
{
	return new BoxCollider();
}

BoxCollider* BoxCollider::Create(GameActor* actor,IMonoObject* obj)
{
	BoxCollider* collider = BoxCollider::Create();
	collider->SetActor(actor);
	collider->CreateCollider();
	collider->SetMonoObject(obj);
	return collider;
}

void BoxCollider::SetSize(float w,float h)
{
	mSize.width = w;
	mSize.height = h;
}

void BoxCollider::SetSize(sizef& size)
{
	mSize = size;
}

const sizef& BoxCollider::GetSize()
{
	return mSize;
}

float BoxCollider::GetWidth()
{
	return mSize.width;
}

float BoxCollider::GetHeight()
{
	return mSize.height;
}

void BoxCollider::CreateCollider()
{
	b2Body* body =  m_pRigidbody->GetBody();
	b2FixtureDef* pFixDef = m_pRigidbody->GetFixtureDef();
	b2FixtureDef  mFixDef = *pFixDef;
	
	mFixDef.isSensor = IsTrigger();
	mFixDef.shape = m_pShape;
	mFixDef.userData = this;

	body->CreateFixture(&mFixDef);
}
