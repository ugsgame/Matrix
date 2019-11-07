
#include "CollistionSystem.h"
#include "Collider.h"
#include "Rigidbody.h"

Collider::Collider():
	m_pRigidbody(NULL),
	m_pShape(NULL)
{
	mRatio = CollistionSystem::ShareCollistionSystem()->GetRatio();
}

Collider::~Collider()
{
}

void Collider::SetActor(GameActor* actor)
{
	m_pActor = actor;
	m_pRigidbody = actor->GetRigidbody();
	CCAssert(m_pRigidbody!=NULL,"");
}

GameActor* Collider::GetActor()
{
	return m_pActor;
}

void Collider::SetTrigger(bool trigger)
{
	m_bTrigger =trigger;

}

bool Collider::IsTrigger()
{
	return m_bTrigger;
}
