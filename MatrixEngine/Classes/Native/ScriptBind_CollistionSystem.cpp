

//#include "stdneb.h"
#include "ActorSystem/GameActor.h"
#include "CollistionSystem/Collider.h"
#include "CollistionSystem/CollistionSystem.h"

//Test:
#include "MCocoStudio/ArmatureCollistionSystem.h"

#include "ScriptBind_CollistionSystem.h"


ScriptBind_CollistionSystem::ScriptBind_CollistionSystem()
{
	REGISTER_METHOD(ColliderGetActor);

	REGISTER_METHOD(Update);

	REGISTER_METHOD(GetCollistionSystem);
	REGISTER_METHOD(CollistionSystemSetDrawLayer);
	REGISTER_METHOD(CollistionSystemSetDebug);
}
ScriptBind_CollistionSystem::~ScriptBind_CollistionSystem()
{

}

mono::object ScriptBind_CollistionSystem::ColliderGetActor(Collider* collider)
{
	return collider->GetActor()->getMonoObject()->GetManagedObject();
}

CollistionSystem* ScriptBind_CollistionSystem::GetCollistionSystem()
{
	return CollistionSystem::ShareCollistionSystem();
}

void  ScriptBind_CollistionSystem::Update(float dTime)
{
	if(CollistionSystem::ShareCollistionSystem())
		CollistionSystem::ShareCollistionSystem()->update(dTime);

// 	if(ArmatureCollistionSystem::shareCollistionSystem())
// 		ArmatureCollistionSystem::shareCollistionSystem()->_update(dTime);
}

void ScriptBind_CollistionSystem::CollistionSystemSetDrawLayer(cocos2d::CCNode* pNode)
{
	CCAssert(pNode!=NULL,"");
	//pNode->removeFromParent();

 //    if(CollistionSystem::ShareCollistionSystem()->getParent())
 //		CollistionSystem::ShareCollistionSystem()->removeFromParent();

	pNode->addChild(CollistionSystem::ShareCollistionSystem());
}

void ScriptBind_CollistionSystem::CollistionSystemSetDebug(bool debug)
{
	CollistionSystem::ShareCollistionSystem()->SetDebug(debug);
}