
#ifndef __SCRIPTBIND_ACTORSYSTEM__
#define __SCRIPTBIND_ACTORSYSTEM__

#include "ScriptBind_Matrix.h"

class GameActor;
class Component;

class ScriptBind_ActorSystem:public ScriptBind_Matrix
{
public:
	ScriptBind_ActorSystem();
	~ScriptBind_ActorSystem();

	virtual const char* GetClassName(){ return "NativeActorSystem"; }
	
	//Actor
	static GameActor* CreateActor();

	static void   ActorSetActive(GameActor* actor,bool active);
	static bool   ActorIsActive(GameActor* actor);
	static Component* ActorAddComponent(GameActor* actor, mono::string comName,mono::object component);
	static void ActorRemoveComponent(GameActor* actor, mono::string comName);
	static float TickTime(GameActor* actor);
};

#endif