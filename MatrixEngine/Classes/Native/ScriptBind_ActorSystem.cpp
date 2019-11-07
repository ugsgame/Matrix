
//#include "stdneb.h"
#include "ActorSystem/GameActor.h"
#include "Component/Component.h"

#include "ScriptBind_ActorSystem.h"

ScriptBind_ActorSystem::ScriptBind_ActorSystem()
{
	REGISTER_METHOD(CreateActor);
	REGISTER_METHOD(ActorSetActive);
	REGISTER_METHOD(ActorIsActive);
	REGISTER_METHOD(ActorAddComponent);
	REGISTER_METHOD(ActorRemoveComponent);
	REGISTER_METHOD(TickTime);
}
ScriptBind_ActorSystem::~ScriptBind_ActorSystem()
{

}
GameActor* ScriptBind_ActorSystem::CreateActor()
{
	//TOD
	return GameActor::create("actor");
}

void ScriptBind_ActorSystem::ActorSetActive(GameActor* actor,bool active)
{
	actor->SetActive(active);
}

bool ScriptBind_ActorSystem::ActorIsActive(GameActor* actor)
{
	return actor->IsActive();
}

Component* ScriptBind_ActorSystem::ActorAddComponent(GameActor* actor, mono::string comName,mono::object component)
{
	return actor->AddComponent(ToMatrixString(comName).c_str(),*component);
}

void ScriptBind_ActorSystem::ActorRemoveComponent(GameActor* actor, mono::string comName)
{
	//
	actor->RemoveComponent(ToMatrixString(comName).c_str());
}

float ScriptBind_ActorSystem::TickTime(GameActor* actor)
{
	return actor->TickTime();
}