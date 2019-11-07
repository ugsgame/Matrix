
//#include "stdneb.h"
// #include "cocos2d.h"
// #include "cocos-ext.h"
// #include "ExtensionMacros.h"

#include "ArmatureContactListener.h"
#include "ActorSystem/GameActor.h"



static ArmatureContactListener* s_SharedContactListener = NULL;

ArmatureContactListener::ArmatureContactListener()
{

};

ArmatureContactListener::~ArmatureContactListener()
{

};

void ArmatureContactListener::BeginContact(b2Contact* contact)
{

	b2Body* aBody = contact->GetFixtureA()->GetBody();
	b2Body* bBody = contact->GetFixtureB()->GetBody();

	GameActor* aActor = (GameActor*)aBody->GetUserData();
	GameActor* bActor = (GameActor*)bBody->GetUserData();

 	CCAssert(dynamic_cast<GameActor*>(aActor) != NULL, "Armature b2Body only supports GameActor as usrdate");
 	CCAssert(dynamic_cast<GameActor*>(bActor) != NULL, "Armature b2Body only supports GameActor as usrdate");

	CCBone* aBone = (CCBone*)contact->GetFixtureA()->GetUserData();
	CCBone* bBone = (CCBone*)contact->GetFixtureB()->GetUserData();

	CCAssert(dynamic_cast<CCBone*>(aBone) != NULL, "Armature b2Fixture only supports CCBone as usrdate");
	CCAssert(dynamic_cast<CCBone*>(bBone) != NULL, "Armature b2Fixture only supports CCBone as usrdate");

	this->OnArmatureEnter(aActor,bActor,aBone,bBone);
}
void ArmatureContactListener::EndContact(b2Contact* contact)
{
	b2Body* aBody = contact->GetFixtureA()->GetBody();
	b2Body* bBody = contact->GetFixtureB()->GetBody();

	GameActor* aActor = (GameActor*)aBody->GetUserData();
	GameActor* bActor = (GameActor*)bBody->GetUserData();

	CCAssert(dynamic_cast<GameActor*>(aActor) != NULL, "Armature b2Body only supports GameActor as usrdate");
	CCAssert(dynamic_cast<GameActor*>(bActor) != NULL, "Armature b2Body only supports GameActor as usrdate");

	CCBone* aBone = (CCBone*)contact->GetFixtureA()->GetUserData();
	CCBone* bBone = (CCBone*)contact->GetFixtureB()->GetUserData();

	CCAssert(dynamic_cast<CCBone*>(aBone) != NULL, "Armature b2Fixture only supports CCBone as usrdate");
	CCAssert(dynamic_cast<CCBone*>(bBone) != NULL, "Armature b2Fixture only supports CCBone as usrdate");

	this->OnArmatureExit(aActor,bActor,aBone,bBone);
}
void ArmatureContactListener::PreSolve(b2Contact* contact,const b2Manifold *oldManifold)
{

}
void ArmatureContactListener::PostSolve(const b2Contact *contact, const b2ContactImpulse *impulse)
{
	const b2Body* aBody = contact->GetFixtureA()->GetBody();
	const b2Body* bBody = contact->GetFixtureB()->GetBody();

	GameActor* aActor = (GameActor*)aBody->GetUserData();
	GameActor* bActor = (GameActor*)bBody->GetUserData();

	CCAssert(dynamic_cast<GameActor*>(aActor) != NULL, "Armature b2Body only supports GameActor as usrdate");
	CCAssert(dynamic_cast<GameActor*>(bActor) != NULL, "Armature b2Body only supports GameActor as usrdate");

	CCBone* aBone = (CCBone*)contact->GetFixtureA()->GetUserData();
	CCBone* bBone = (CCBone*)contact->GetFixtureB()->GetUserData();

	CCAssert(dynamic_cast<CCBone*>(aBone) != NULL, "Armature b2Fixture only supports CCBone as usrdate");
	CCAssert(dynamic_cast<CCBone*>(bBone) != NULL, "Armature b2Fixture only supports CCBone as usrdate");

	this->OnArmatureStay(aActor,bActor,aBone,bBone);
}
//
void ArmatureContactListener::OnArmatureEnter(CCArmature* ArmatureA,CCArmature* ArmatureB,const char* boneAName,const char* boneBName)
{
	//CCLog("Enter");
	GameActor* ActorA = (GameActor*)ArmatureA->getUserObject();
	//CCAssert(ActorA!=NULL,"game actorA is not bind to armatureA!!");
	GameActor* ActorB = (GameActor*)ArmatureB->getUserObject();
	//CCAssert(ActorB!=NULL,"game actorB is not bind to armatureB!!");
	if(!ActorA&&ActorB)return;

	IMonoObject* scriptA = ActorA->getMonoObject();
	IMonoObject* scriptB = ActorB->getMonoObject();
// 	std::string nameA = BoneA->getName();
// 	std::string nameB = BoneB->getName();
// 	const char* boneAName = nameA.c_str();
// 	const char* boneBName = nameB.c_str();

	scriptA->CallMethod("native_OnArmatureEnter",scriptA->GetManagedObject(),ActorB->getMonoObject()->GetManagedObject(),boneAName,boneBName);
}
void ArmatureContactListener::OnArmatureExit(CCArmature* ArmatureA,CCArmature* ArmatureB,const char* boneAName,const char* boneBName)
{
	//CCLog("Exit");
	if(!(ArmatureA&&ArmatureB))return; 
	GameActor* ActorA = (GameActor*)ArmatureA->getUserObject();
	//CCAssert(ActorA!=NULL,"game actorA is not bind to armatureA!!");
	GameActor* ActorB = (GameActor*)ArmatureB->getUserObject();
	//CCAssert(ActorB!=NULL,"game actorB is not bind to armatureB!!");
	if(!(ActorA&&ActorB))return;

	IMonoObject* scriptA = ActorA->getMonoObject();
	IMonoObject* scriptB = ActorB->getMonoObject();
// 	std::string nameA = BoneA->getName();
// 	std::string nameB = BoneB->getName();
// 	const char* boneAName = nameA.c_str();
// 	const char* boneBName = nameB.c_str();

	scriptA->CallMethod("native_OnArmatureExit",scriptA->GetManagedObject(),ActorB->getMonoObject()->GetManagedObject(),boneAName,boneBName);
}
void ArmatureContactListener::OnArmatureStay(CCArmature* ArmatureA,CCArmature* ArmatureB,const char* boneAName,const char* boneBName)
{
	//CCLog("Stay");
	GameActor* ActorA = (GameActor*)ArmatureA->getUserObject();
	//CCAssert(ActorA!=NULL,"game actorA is not bind to armatureA!!");
	GameActor* ActorB = (GameActor*)ArmatureB->getUserObject();
	//CCAssert(ActorB!=NULL,"game actorB is not bind to armatureB!!");
	if(!ActorA&&ActorB)return;

	IMonoObject* scriptA = ActorA->getMonoObject();
	IMonoObject* scriptB = ActorB->getMonoObject();
// 	std::string nameA = BoneA->getName();
// 	std::string nameB = BoneB->getName();
// 	const char* boneAName = nameA.c_str();
// 	const char* boneBName = nameB.c_str();

	scriptA->CallMethod("native_OnArmatureStay",scriptA->GetManagedObject(),ActorB->getMonoObject()->GetManagedObject(),boneAName,boneBName);
}
//TODO
void ArmatureContactListener::OnArmatureEnter(GameActor* ActorA,GameActor* ActorB,CCBone* BoneA,CCBone* BoneB)
{
//	CCLog("OnArmatureEnter");
	IMonoObject* scriptA = ActorA->getMonoObject();
	IMonoObject* scriptB = ActorB->getMonoObject();
	std::string nameA = BoneA->getName();
	std::string nameB = BoneB->getName();
	const char* boneAName = nameA.c_str();
	const char* boneBName = nameB.c_str();
	scriptA->CallMethod("native_OnArmatureEnter",scriptA->GetManagedObject(),ActorB->getMonoObject()->GetManagedObject(),boneAName,boneBName);
	scriptB->CallMethod("native_OnArmatureEnter",scriptB->GetManagedObject(),ActorA->getMonoObject()->GetManagedObject(),boneBName,boneAName);
}
void ArmatureContactListener::OnArmatureExit(GameActor* ActorA,GameActor* ActorB,CCBone* BoneA,CCBone* BoneB)
{ 
//	CCLog("OnArmatureExit");
	IMonoObject* scriptA = ActorA->getMonoObject();
	IMonoObject* scriptB = ActorB->getMonoObject();
	std::string nameA = BoneA->getName();
	std::string nameB = BoneB->getName();
	const char* boneAName = nameA.c_str();
	const char* boneBName = nameB.c_str();
	scriptA->CallMethod("native_OnArmatureExit",scriptA->GetManagedObject(),ActorB->getMonoObject()->GetManagedObject(),boneAName,boneBName);
	scriptB->CallMethod("native_OnArmatureExit",scriptB->GetManagedObject(),ActorA->getMonoObject()->GetManagedObject(),boneBName,boneAName);
}
void ArmatureContactListener::OnArmatureStay(GameActor* ActorA,GameActor* ActorB,CCBone* BoneA,CCBone* BoneB)
{
//	CCLog("OnArmatureStay");
	IMonoObject* scriptA = ActorA->getMonoObject();
	IMonoObject* scriptB = ActorB->getMonoObject();
	std::string nameA = BoneA->getName();
	std::string nameB = BoneB->getName();
	const char* boneAName = nameA.c_str();
	const char* boneBName = nameB.c_str();
	scriptA->CallMethod("native_OnArmatureStay",scriptA->GetManagedObject(),ActorB->getMonoObject()->GetManagedObject(),boneAName,boneBName);
	scriptB->CallMethod("native_OnArmatureStay",scriptB->GetManagedObject(),ActorA->getMonoObject()->GetManagedObject(),boneBName,boneAName);
}

ArmatureContactListener* ArmatureContactListener::ShareListener()
{
	if (!s_SharedContactListener)
	{
		s_SharedContactListener = new ArmatureContactListener();
	}
	return s_SharedContactListener;
}