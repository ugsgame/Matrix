
#ifndef __COLLISTIONSYSTEM_H__
#define __COLLISTIONSYSTEM_H__

#include "cocos2d.h"
#include "MatrixMono.h"

#include "Box2D/Box2D.h"

USING_NS_CC;

class Collider;
class CollistionDebugDraw;

class CollistionSystem:public CCNode,b2ContactListener
{
	__ScriptBind
public:
	CollistionSystem(b2World* world = NULL);
	~CollistionSystem();
public:
	virtual bool init();
	virtual void update(float delta);
	virtual void draw(); 

	//b2WorldCallbacks
	virtual void BeginContact(b2Contact* contact);
	virtual void EndContact(b2Contact* contact);
	virtual void PreSolve(b2Contact* contact,const b2Manifold *oldManifold);
	virtual void PostSolve(const b2Contact *contact, const b2ContactImpulse *impulse);
	//注册一个新的监听器
	void RegisterNewListener(b2ContactListener* listener);

	void SetGravity(CCPoint& gravity);
	CCPoint GetGravity(){ return ccp(mGravity.x,mGravity.y); }

	void SetDebug(bool debug);

	void SetRatio(float ratio);
	float GetRatio(){ return this->f_mRatio; }


	b2World* GetWorld(){ return p_mWorld; }

	static CollistionSystem* Create(b2World* world = NULL);

	static CollistionSystem* ShareCollistionSystem();
private:
	//重力
	b2Vec2 mGravity;
	//box2d与游戏世界的比例
	float f_mRatio;
	//是否需要画出box2d信息
	bool  b_mDebug;
	//
	b2World* p_mWorld;
	//
	CollistionDebugDraw* mDebugDraw;
};

#endif