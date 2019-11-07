
#include "CollistionSystem.h"
#include "Collider.h"
#include "CollistionDebugDraw.h"


static CollistionSystem* s_SharedCollistionSystem = NULL;

CollistionSystem::CollistionSystem(b2World* world):
	p_mWorld(world),
	f_mRatio(32.0f)
{
	//创建物理世界
	if(!p_mWorld)
	{
		//TODO
		mGravity = b2Vec2(0,0);
		p_mWorld = new b2World(mGravity);
		p_mWorld->SetAllowSleeping(true);
	}
	mDebugDraw = new CollistionDebugDraw(f_mRatio);  
	p_mWorld->SetContactListener(this);
	p_mWorld->SetDebugDraw(mDebugDraw);

    uint32 flags = 0;
    flags += b2Draw::e_shapeBit;
    flags += b2Draw::e_jointBit;
    flags += b2Draw::e_aabbBit;
    flags += b2Draw::e_pairBit;
    flags += b2Draw::e_centerOfMassBit;

    mDebugDraw->SetFlags(flags);
}

CollistionSystem::~CollistionSystem()
{
	SAFE_DELETE(mDebugDraw);
}

bool CollistionSystem::init()
{
	//TODO
	if ( !CCNode::init() )return false;
	//scheduleUpdate();

	return true;
}

void CollistionSystem::update(float delta)
{
	p_mWorld->Step(delta,0,0);
}

void CollistionSystem::draw()
{
	ccGLEnableVertexAttribs( kCCVertexAttribFlag_Position );
	kmGLPushMatrix();
	p_mWorld->DrawDebugData();
	kmGLPopMatrix();
}

void CollistionSystem::BeginContact(b2Contact* contact)
{
	b2Body* aBody = contact->GetFixtureA()->GetBody();
	b2Body* bBody = contact->GetFixtureB()->GetBody();

	GameActor* aActor = (GameActor*)aBody->GetUserData();
	GameActor* bActor = (GameActor*)bBody->GetUserData();

	b2Fixture* aFixture = contact->GetFixtureA();
	b2Fixture* bFixture = contact->GetFixtureB();

	Collider* aCollider = (Collider*)aFixture->GetUserData();
	Collider* bCollider = (Collider*)bFixture->GetUserData();

// 	CCAssert2(dynamic_cast<Collider*>(aCollider) != NULL, "CollistionSystem only supports Collider as usrdate");
// 	CCAssert2(dynamic_cast<Collider*>(bCollider) != NULL, "CollistionSystem only supports Collider as usrdate");
	
	aActor->OnTriggerEnter(aCollider,bCollider);
	
}
void CollistionSystem::EndContact(b2Contact* contact)
{
	b2Body* aBody = contact->GetFixtureA()->GetBody();
	b2Body* bBody = contact->GetFixtureB()->GetBody();

	GameActor* aActor = (GameActor*)aBody->GetUserData();
	GameActor* bActor = (GameActor*)bBody->GetUserData();

	b2Fixture* aFixture = contact->GetFixtureA();
	b2Fixture* bFixture = contact->GetFixtureB();

	Collider* aCollider = (Collider*)aFixture->GetUserData();
	Collider* bCollider = (Collider*)bFixture->GetUserData();

	// 	CCAssert2(dynamic_cast<Collider*>(aCollider) != NULL, "CollistionSystem only supports Collider as usrdate");
	// 	CCAssert2(dynamic_cast<Collider*>(bCollider) != NULL, "CollistionSystem only supports Collider as usrdate");

	aActor->OnTriggerExit(aCollider,bCollider);
}

void CollistionSystem::PreSolve(b2Contact *contact, const b2Manifold *oldManifold)
{

}
void CollistionSystem::PostSolve(const b2Contact *contact, const b2ContactImpulse *impulse)
{
	
}

void CollistionSystem::RegisterNewListener(b2ContactListener* listener)
{
	this->p_mWorld->SetContactListener(listener);
}

void CollistionSystem::SetGravity(CCPoint& gravity)
{
	mGravity.x = gravity.x;
	mGravity.y = gravity.y;
	this->p_mWorld->SetGravity(mGravity);
}

void CollistionSystem::SetDebug(bool debug)
{
	this->b_mDebug = debug;
	this->setVisible(b_mDebug);
}

void CollistionSystem::SetRatio(float ratio)
{
	this->f_mRatio = ratio;
	mDebugDraw->SetRatio(ratio);
}

CollistionSystem* CollistionSystem::Create(b2World* world /* = NULL */)
{
	CollistionSystem* pManager = new CollistionSystem(world);

	if (pManager && pManager->init())
	{
		pManager->autorelease();
	}
	else
	{
		CC_SAFE_DELETE(pManager);
	}

	//pManager->retain();

	return pManager;
}

CollistionSystem* CollistionSystem::ShareCollistionSystem()
{
	if (!s_SharedCollistionSystem)
	{
		s_SharedCollistionSystem = CollistionSystem::Create();

		s_SharedCollistionSystem->retain();
	}
	return s_SharedCollistionSystem;
}
