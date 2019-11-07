
#include "CollistionListener.h"

CollistionDestructionListener::CollistionDestructionListener()
{
}

CollistionDestructionListener::~CollistionDestructionListener()
{
}

void CollistionDestructionListener::SayGoodbye(b2Joint* joint)
{
}

void CollistionDestructionListener::SayGoodbye(b2Fixture* fixture)
{
}
//////////////////////////////////////////////////////////////////////////

CollistionContactFilter::CollistionContactFilter()
{
}

CollistionContactFilter::~CollistionContactFilter()
{
}

bool CollistionContactFilter::ShouldCollide(b2Fixture* fixtureA, b2Fixture* fixtureB)
{
	//TODO
	return true;
}

//////////////////////////////////////////////////////////////////////////

CollisionContactListener::CollisionContactListener()
{
}
CollisionContactListener::~CollisionContactListener()
{
}

void CollisionContactListener::BeginContact(b2Contact* contact)
{

}

void CollisionContactListener::EndContact(b2Contact* contact)
{

}

void CollisionContactListener::PreSolve(b2Contact* contact,const b2Manifold *oldManifold)
{

}

void CollisionContactListener::PostSolve(const b2Contact *contact, const b2ContactImpulse *impulse)
{

}

//////////////////////////////////////////////////////////////////////////

CollisionQueryCallback::CollisionQueryCallback()
{
}

CollisionQueryCallback::~CollisionQueryCallback()
{
}

bool CollisionQueryCallback::ReportFixture(b2Fixture* fixture)
{
	//TODO:
	return true;
}

//////////////////////////////////////////////////////////////////////////

CollisionCastCallBack::CollisionCastCallBack()
{
}

CollisionCastCallBack::~CollisionCastCallBack()
{
}

float32 CollisionCastCallBack::ReportFixture(b2Fixture* fixture, const b2Vec2& point, const b2Vec2& normal, float32 fraction)
{
	//TODO:
	return 0;
}

