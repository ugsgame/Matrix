
#ifndef __ACTOR_HEADER_H__
#define __ACTOR_HEADER_H__

//TODO
//#include "util/string.h"
#include "MatrixMono.h"
#include "ActorManager.h"
#include "Entity/Entity.h"
#include "Entity/Transform.h"

//using namespace Util;

class Collider;
class Rigidbody;
class Component;
class ComponentContainer;

class GameActor:public Entity
{
//	__ScriptBind
public:
	GameActor(const char* name);
	~GameActor();

public:
	//CCNode Spouse
	virtual bool init();
	virtual void onEnter();
	virtual void onExit();
	virtual void update(float delta);

	virtual void setPosition(const CCPoint &position);
	virtual void setRotation(float fRotation);
	//

	virtual void OnTriggerEnter(Collider* aCollider,Collider* bCollider);
	virtual void OnTriggerExit(Collider* aCollider,Collider* bCollider);
	virtual void OnTriggerStay(Collider* aCollider,Collider* bCollider);

	Component*  AddComponent(const char* cName,IMonoObject* obj);
	bool RemoveComponent(const char* cName);

	virtual float TickTime(){ return tick; }
	//TODO
	//激活游戏对象
	virtual bool IsActive();
	virtual void SetActive(bool active);
	//TODO
	//void AddEvent();
	//void RemoveEvent();
	//
	const char* GetName(){ return mName; }

	static GameActor* create(const char* name);

//protected:
	Rigidbody* GetRigidbody();
protected:
	//Components 
	//TODO
	ComponentContainer* mComponents;
private:
	//GameObject Name
	float tick;
	const char* mName;
	bool bActive;
	Rigidbody* mRigidbody;	
	
	//friend Collider;
};

#endif