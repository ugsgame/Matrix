
//#include "stdneb.h"
#include "GameActor.h"
#include "ActorManager.h"


ActorManager::ActorManager()
{
	ActorArray = NULL;
}

ActorManager::~ActorManager()
{
	//TODO
}
//TODO
bool ActorManager::init()
{
	ActorArray = CCArray::create();
	return true;
}

void ActorManager::AddActor(GameActor* actor)
{
	ActorArray->addObject(actor);
}

void ActorManager::RemoveActor(GameActor* actor)
{
	ActorArray->removeObject(actor,true);
}

ActorManager* ActorManager::Create(IMonoObject* pObj)
{
	ActorManager* pManager = new ActorManager();

	if (pManager && pManager->init())
	{
		pManager->autorelease();
	}
	else
	{
		CC_SAFE_DELETE(pManager);
	}

	return pManager;
}