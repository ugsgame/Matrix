/************************************************************************/
/*¹Ç÷À¶¯»­Åö×²¼àÌýÆ÷                                                                      
/************************************************************************/
#ifndef __ARMATURECONTACTLISTENER__
#define __ARMATURECONTACTLISTENER__

#include "cocos2d.h"
#include "cocos-ext.h"
#include "ExtensionMacros.h"
#include "MatrixMono.h"

#include "Box2D/Box2D.h"

USING_NS_CC;
USING_NS_CC_EXT;

class GameActor;
// class cocos2d::extension::CCBone;
// class cocos2d::extension::CCArmature;

class ArmatureContactListener:public b2ContactListener
{
public:
	ArmatureContactListener();
	~ArmatureContactListener();
public:
	//b2WorldCallbacks
	virtual void BeginContact(b2Contact* contact);
	virtual void EndContact(b2Contact* contact);
	virtual void PreSolve(b2Contact* contact,const b2Manifold *oldManifold);
	virtual void PostSolve(const b2Contact *contact, const b2ContactImpulse *impulse);
	//ArmatureCollistionSystem Callback
	virtual void OnArmatureEnter(CCArmature* ArmatureA,CCArmature* ArmatureB,const char* BoneAName,const char* BoneBName);
	virtual void OnArmatureExit(CCArmature* ArmatureA,CCArmature* ArmatureB,const char* BoneAName,const char* BoneBName);
	virtual void OnArmatureStay(CCArmature* ArmatureA,CCArmature* ArmatureB,const char* BoneAName,const char* BoneBName);
	//TODO
	virtual void OnArmatureEnter(GameActor* ActorA,GameActor* ActorB,CCBone* BoneA,CCBone* BoneB);
	virtual void OnArmatureExit(GameActor* ActorA,GameActor* ActorB,CCBone* BoneA,CCBone* BoneB);
	virtual void OnArmatureStay(GameActor* ActorA,GameActor* ActorB,CCBone* BoneA,CCBone* BoneB);

	static ArmatureContactListener* ShareListener();
protected:
private:
};

#endif

