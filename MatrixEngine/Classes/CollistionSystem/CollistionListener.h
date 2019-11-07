#ifndef __COLLISTION_LISTENER__
#define __COLLISTION_LISTENER__

#include "MatrixMono.h"

#include "Box2D/Box2D.h"

// b2DestructionListener
// TODO:
class CollistionDestructionListener:b2DestructionListener
{
	__ScriptBind
public:

	CollistionDestructionListener();
	~CollistionDestructionListener();

	virtual void SayGoodbye(b2Joint* joint);
	virtual void SayGoodbye(b2Fixture* fixture);
};

// b2ContactFilter
// TODO:
class CollistionContactFilter:b2DestructionListener
{
	__ScriptBind
public:
	CollistionContactFilter();
	~CollistionContactFilter();

	virtual bool ShouldCollide(b2Fixture* fixtureA, b2Fixture* fixtureB);
};

// b2ContactListener
class CollisionContactListener:b2ContactListener
{
	__ScriptBind
public:
	CollisionContactListener();
	~CollisionContactListener();

	virtual void BeginContact(b2Contact* contact);
	virtual void EndContact(b2Contact* contact);
	virtual void PreSolve(b2Contact* contact,const b2Manifold *oldManifold);
	virtual void PostSolve(const b2Contact *contact, const b2ContactImpulse *impulse);

protected:

private:

};

class CollisionQueryCallback:b2QueryCallback
{
	__ScriptBind
public:
	CollisionQueryCallback();
	~CollisionQueryCallback();

	virtual bool ReportFixture(b2Fixture* fixture);
};

class CollisionCastCallBack:b2RayCastCallback
{
	__ScriptBind
public:
	CollisionCastCallBack();
	~CollisionCastCallBack();

	virtual float32 ReportFixture(    b2Fixture* fixture, const b2Vec2& point,
		const b2Vec2& normal, float32 fraction);
};

#endif