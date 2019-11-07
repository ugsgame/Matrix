
#ifndef __COLLISTION__
#define __COLLISTION__

#include "cocos2d.h"
#include "MatrixMono.h"

#include "Box2D/Box2D.h"

USING_NS_CC;

class Collider;
class CollistionDebugDraw;

class Rigidbody;
class GameActor;

class CollistionDestructionListener;
class CollistionContactFilter;
class CollisionContactListener;
class CollisionQueryCallback;
class CollisionCastCallBack;

class Collistion:public CCNode
{
	__ScriptBind
public:
	Collistion(const char* mName);
	~Collistion();
public:
	virtual bool init();
	virtual void update(float delta);
	virtual void draw(); 

	//register listener
	void  registerDestructionListener(CollistionDestructionListener* listener);
	void  registerContactListener(CollisionContactListener* listener);
	void  registerContactFilter(CollistionContactFilter* listener);
	void  registerQueryCallback(CollisionQueryCallback* listener);
	void  registerCastCallBack(CollisionCastCallBack* listener,CCPoint& point1,CCPoint& point2);

	// 属性
	// TODO:添加更多的属性
	void setGravity(CCPoint& gravity);
	CCPoint getGravity(){ return ccp(mGravity.x,mGravity.y); }

	void setDebug(bool debug);
    bool isDebug();
	//
	b2World* getWorld(){ return p_mWorld; }
	const char* getName(){ return mName; }

	//factory
// 	Rigidbody* createRigidbody(GameActor* actor);
// 	bool destoryRigidbody(Rigidbody* body);
//	Joint* createJoint(GameActor* actor);
//	bool destoryJoint(Joint* joint)

	static Collistion* create(const char* mName);

protected:

private:
	//
	const char* mName;
	//重力
	b2Vec2 mGravity;
	//
	b2World* p_mWorld;
	//
	CollistionDebugDraw* mDebugDraw;
};

#endif