
#include "Collistion.h"
#include "Collider.h"
#include "CollistionDebugDraw.h"

#include "CollistionSystem.h"


Collistion::Collistion(const char* name)
{
	mName = name;

	mGravity = b2Vec2(0,0);
	p_mWorld = new b2World(mGravity);
	p_mWorld->SetAllowSleeping(true);

	mDebugDraw = new CollistionDebugDraw(CollistionSystem::ShareCollistionSystem()->GetRatio());  
	p_mWorld->SetDebugDraw(mDebugDraw);

	uint32 flags = 0;
	flags += b2Draw::e_shapeBit;
	flags += b2Draw::e_jointBit;
	flags += b2Draw::e_aabbBit;
	flags += b2Draw::e_pairBit;
	flags += b2Draw::e_centerOfMassBit;

	mDebugDraw->SetFlags(flags);
}

Collistion::~Collistion()
{
	SAFE_DELETE(mDebugDraw);
	//TODE Test
	SAFE_DELETE(p_mWorld);
}

bool Collistion::init()
{
	if ( !CCNode::init() )return false;
	//TODO:
	//
	return true;
}

void Collistion::update(float delta)
{
	p_mWorld->Step(delta,0,0);
}

void Collistion::draw()
{
	ccGLEnableVertexAttribs( kCCVertexAttribFlag_Position );
	kmGLPushMatrix();
	p_mWorld->DrawDebugData();
	kmGLPopMatrix();
}

void Collistion::registerDestructionListener(CollistionDestructionListener* listener)
{
	p_mWorld->SetDestructionListener((b2DestructionListener*)listener);
}

void Collistion::registerContactListener(CollisionContactListener* listener)
{
	p_mWorld->SetContactListener((b2ContactListener*) listener);
}

void Collistion::registerContactFilter(CollistionContactFilter* listener)
{
	p_mWorld->SetContactFilter((b2ContactFilter*)listener);
}

void Collistion::registerQueryCallback(CollisionQueryCallback* listener)
{
	//TODO
	//p_mWorld->QueryAABB((b2QueryCallback*)listener,)
}

void Collistion::registerCastCallBack(CollisionCastCallBack* listener,CCPoint& point1,CCPoint& point2)
{
	p_mWorld->RayCast((b2RayCastCallback*)listener,b2Vec2(point1.x,point1.y),b2Vec2(point2.x,point2.y));
}

void Collistion::setGravity(CCPoint& gravity)
{
	mGravity.x = gravity.x;
	mGravity.y = gravity.y;
	this->p_mWorld->SetGravity(mGravity);
}

void Collistion::setDebug(bool debug)
{
	this->setVisible(debug);
}

bool Collistion::isDebug()
{
	return  this->isVisible();
}

Collistion* Collistion::create(const char* mName)
{
	Collistion* collistion = new Collistion(mName);

	if (collistion && collistion->init())
	{
		collistion->autorelease();
	}
	else
	{
		CC_SAFE_DELETE(collistion);
	}

	//collistion->retain();

	return collistion;
}