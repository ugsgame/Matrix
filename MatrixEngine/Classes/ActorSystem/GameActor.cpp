
#include "GameActor.h"
#include "Component/Component.h"
#include "Component/ComponentContainer.h"
#include "CollistionSystem/Rigidbody.h"
#include "CollistionSystem/Collider.h"
#include "CollistionSystem/BoxCollider.h"


GameActor::GameActor(const char* name):
mRigidbody(NULL),
bActive(false),
tick(0),
mName(name)
{
	mComponents = ComponentContainer::Create();
//	this->CreateComponent("Rigidbody");
}

GameActor::~GameActor()
{
	mComponents->Remove("Rigidbody");
	//TODO:改用宏安全释放
	if(mRigidbody)
	{
		delete mRigidbody;
		mRigidbody = NULL;
	}

	if(mComponents)
	{
		delete mComponents;
		mComponents = NULL;
	}	

	this->getMonoObject()->CallMethod("native_OnDestroy");
}

bool GameActor::init()
{
	return CCNode::init();
	//scheduleUpdate();

	//this->getMonoObject()->CallMethod("native_OnStart");
}

void GameActor::onEnter()
{
	Entity::onEnter();
	scheduleUpdate();

	this->getMonoObject()->CallMethod("native_OnEnter");
}

void GameActor::onExit()
{
	Entity::onExit();
	unscheduleUpdate();

	this->getMonoObject()->CallMethod("native_OnExit");
}

void GameActor::update(float delta)
{
	if(mRigidbody)
	{
		Transform trf = mRigidbody->GetTransform();
		this->setRotation(CC_RADIANS_TO_DEGREES(trf.q));
		this->setPosition(trf.p);
	}
	Entity::update(delta);
	tick = delta;
	//this->getMonoObject()->CallMethod("native_OnUpdate",delta);
	this->getMonoObject()->CallMethod("native_OnUpdate");
}

void GameActor::setPosition(const CCPoint &position)
{
	Entity::setPosition(position);
	if(mRigidbody)
	{
		Transform trf(position,mRigidbody->GetTransform().q);
		mRigidbody->SetTransform(trf);
	}
}

void GameActor::setRotation(float fRotation)
{
	Entity::setRotation(fRotation);
	if(mRigidbody)
	{
		Transform trf = mRigidbody->GetTransform();
		trf.q = CC_DEGREES_TO_RADIANS(fRotation);
		mRigidbody->SetTransform(trf);
	}
}

void GameActor::OnTriggerEnter(Collider* aCollider,Collider* bCollider)
{
	IMonoObject* script = getMonoObject();
	IMonoObject* aSCollider = aCollider->GetMonoObject();
	IMonoObject* bSCollider = bCollider->GetMonoObject();

	script->CallMethod("native_OnTriggerEnter",aSCollider->GetManagedObject(),bSCollider->GetManagedObject());
}
void GameActor::OnTriggerExit(Collider* aCollider,Collider* bCollider)
{
	IMonoObject* script = getMonoObject();
	IMonoObject* aSCollider = aCollider->GetMonoObject();
	IMonoObject* bSCollider = bCollider->GetMonoObject();

	script->CallMethod("native_OnTriggerExit",aSCollider->GetManagedObject(),bSCollider->GetManagedObject());
}
void GameActor::OnTriggerStay(Collider* aCollider,Collider* bCollider)
{
	IMonoObject* script = getMonoObject();
	IMonoObject* aSCollider = aCollider->GetMonoObject();
	IMonoObject* bSCollider = bCollider->GetMonoObject();

	script->CallMethod("native_OnTriggerStay",aSCollider->GetManagedObject(),bSCollider->GetManagedObject());
}

Component* GameActor::AddComponent(const char* cName,IMonoObject* obj)
{
	std::string comName(cName);
	Component* component = NULL;
	if (comName == "Rigidbody")
	{
		mRigidbody = Rigidbody::Create(this,obj);
		mComponents->Add(mRigidbody);
		component = mRigidbody;
	} 
	else if(comName == "BoxCollider")
	{
		component = BoxCollider::Create(this,obj);
		mComponents->Add(component);
	}
	return component;
}

bool GameActor::RemoveComponent(const char* cName)
{
	//TODO:
	std::string comName(cName);
	mComponents->Remove(cName);

	std::string rigidbody = "Rigidbody";

	if (comName == rigidbody)
	{
		if (mRigidbody)
		{
			delete mRigidbody;
			mRigidbody = NULL;
			return true;
		}
	}

	return false;
}

bool GameActor::IsActive()
{
	return bActive;
}

void  GameActor::SetActive(bool active)
{
	IMonoObject* script = getMonoObject();
	if(active)
		script->CallMethod("native_OnEnable");
	else
		script->CallMethod("native_OnDisable");
	bActive = active;
}

Rigidbody* GameActor::GetRigidbody()
{
	return mRigidbody;
}

GameActor* GameActor::create(const char* name)
{
	GameActor *pRet = new GameActor(name); 
	if (pRet && pRet->init()) 
	{ 
		pRet->autorelease(); 
		return pRet; 
	} 
	else 
	{ 
		delete pRet; 
		pRet = NULL; 
		return NULL; 
	} 
	
	return pRet;
}

