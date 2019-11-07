
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"
#include "ExtensionMacros.h"

#include "MCocoStudio/ArmatureContactListener.h"
#include "ActorSystem/GameActor.h"
#include "CollistionSystem/CollistionSystem.h"
#include "CollistionSystem/Rigidbody.h"

#include "MCocoStudio/ArmatureCollistionSystem.h"

#include "ScriptBind_CCArmature.h"

USING_NS_CC;
USING_NS_CC_EXT;


ScriptBind_CCArmature::ScriptBind_CCArmature()
{
	REGISTER_METHOD(RegisterContactListener);
	REGISTER_METHOD(BindGameActor);
	REGISTER_METHOD(UnBindGameActor);
	REGISTER_METHOD(CreateColliderFilter);
	REGISTER_METHOD(Create);
	REGISTER_METHOD(CreateWithName);
	REGISTER_METHOD(CreateWithNameAndBone);
	REGISTER_METHOD(GetBoneRect);
	REGISTER_METHOD(GetBonePosition);
	REGISTER_METHOD(BoneIsDisplay);
	REGISTER_METHOD(AddBone);
	REGISTER_METHOD(GetBone);
	REGISTER_METHOD(RemoveBone);
	REGISTER_METHOD(ChangeBoneParent);
	REGISTER_METHOD(SetBlendFunc);
	REGISTER_METHOD(GetBlendFunc);
	REGISTER_METHOD(SetParticlePositionType);
	REGISTER_METHOD(GetParticlePositionType);
	REGISTER_METHOD(SetAnimation);
	REGISTER_METHOD(GetAnimation);
	REGISTER_METHOD(SetColliderFilter);
}

ScriptBind_CCArmature::~ScriptBind_CCArmature()
{

}

//注册动画碰撞监听器
void ScriptBind_CCArmature::RegisterContactListener()
{
	CollistionSystem::ShareCollistionSystem()->RegisterNewListener(ArmatureContactListener::ShareListener());

	//ArmatureCollistionSystem::shareCollistionSystem()->registerContactListener(ArmatureContactListener::ShareListener());
}
//邦定GameActor到动画（其实是为了拿它的刚体）
void ScriptBind_CCArmature::BindGameActor(cocos2d::extension::CCArmature* pArm,GameActor* actor)
{
	GameActor* mActor = actor;
	pArm->setBody(actor->GetRigidbody()->GetBody());

// 	pArm->setUserObject(actor);
// 	ArmatureCollistionSystem::shareCollistionSystem()->bindArmature(pArm);
}

void ScriptBind_CCArmature::UnBindGameActor(cocos2d::extension::CCArmature* pArm)
{
	//TODO
	//pArm->setBody(NULL);
	pArm->removeBody();
	//ArmatureCollistionSystem::shareCollistionSystem()->unBindArmature(pArm);
}

//
CCColliderFilter* ScriptBind_CCArmature::CreateColliderFilter(unsigned short categoryBits /* = 0x0001 */, unsigned short maskBits /* = 0xFFFF */, signed short groupIndex /* = 0 */)
{
	return new CCColliderFilter(categoryBits,maskBits,groupIndex);	
}


CCArmature* ScriptBind_CCArmature::Create()
{
	return CCArmature::create();
}
CCArmature* ScriptBind_CCArmature::CreateWithName(mono::string name)
{
	return CCArmature::create(ToMatrixString(name).c_str());
}

CCArmature* ScriptBind_CCArmature::CreateWithNameAndBone(mono::string name, CCBone *parentBone)
{
	return CCArmature::create(ToMatrixString(name).c_str(),parentBone);
}

void ScriptBind_CCArmature::GetBoneRect(CCArmature* pArm,mono::string name,CCRect& rect)
{
	char* str = mono_string_to_utf8((MonoString*)name);
	CCBone* bone = pArm->getBone(str);
	if (!bone) {
		rect = CCRectMake(0, 0, 0, 0);
		g_free(str);
		return;
	}
	int displayIndex = bone->getDisplayManager()->getCurrentDisplayIndex();
	if (displayIndex < 0) {
		rect = CCRectMake(0, 0, 0, 0);
		g_free(str);
		return;
	}

	CCFrameData* frameData = bone->getTweenData();
	float x = frameData->x + bone->getPositionX();
	float y = frameData->y + bone->getPositionY();
	float scaleX = frameData->scaleX * bone->getScaleX();
	float scaleY = frameData->scaleY * bone->getScaleY();

	CCNode* render = bone->getDisplayRenderNode();
	CCSize size = render->getContentSize();

	float width = size.width * scaleX;
	float height = size.height * scaleY;

	rect = CCRectMake(x -  width/2, y - height/2, width, height);
	g_free(str);
}

void ScriptBind_CCArmature::GetBonePosition(CCArmature* pArm,mono::string name,CCPoint& point)
{
	char* str = mono_string_to_utf8((MonoString*)name);
	CCBone* bone = pArm->getBone(str);
	if (!bone) {
		point = ccp(0,0);
		g_free(str);
		return;
	}
	int displayIndex = bone->getDisplayManager()->getCurrentDisplayIndex();
	if (displayIndex < 0) {
		point = ccp(0,0);
		g_free(str);
		return;
	}

	CCFrameData* frameData = bone->getTweenData();
	float x = frameData->x + bone->getPositionX();
	float y = frameData->y + bone->getPositionY();

	point = ccp(x/2,y/2); 
	g_free(str);
}

bool ScriptBind_CCArmature::BoneIsDisplay(CCArmature* pArm,mono::string name)
{
	char* str = mono_string_to_utf8((MonoString*)name);
	bool rel = true;
	CCBone* bone = pArm->getBone(str);
	if (!bone)
	{
		g_free(str);
		return false;
	}
	if (bone->getDisplayManager()->getCurrentDisplayIndex())rel = false;
	g_free(str);
	return rel;
}

void ScriptBind_CCArmature::AddBone(CCArmature* pArm, CCBone* bone,mono::string  parentName)
{
	pArm->addBone(bone,ToMatrixString(parentName).c_str());
}
CCBone* ScriptBind_CCArmature::GetBone(CCArmature* pArm,mono::string name)
{
	return pArm->getBone(ToMatrixString(name).c_str());
}
void ScriptBind_CCArmature::ChangeBoneParent(CCArmature* pArm,CCBone* bone,mono::string parentName)
{
	pArm->changeBoneParent(bone,ToMatrixString(parentName).c_str());
}
void ScriptBind_CCArmature::RemoveBone(CCArmature* pArm,CCBone* bone,bool recursion)
{
	pArm->removeBone(bone,recursion);
}

void ScriptBind_CCArmature::SetBlendFunc(CCArmature* pArm,ccBlendFunc &blendFunc)
{
	pArm->setBlendFunc(blendFunc);
}

ccBlendFunc ScriptBind_CCArmature::GetBlendFunc(CCArmature* pArm)
{
	return pArm->getBlendFunc();
}

void ScriptBind_CCArmature::SetParticlePositionType(CCArmature* pArm,tCCPositionType positionType)
{
	pArm->setParticlePositionType(positionType);
}

tCCPositionType ScriptBind_CCArmature::GetParticlePositionType(CCArmature* pArm)
{
	return pArm->getParticlePositionType();
}

void ScriptBind_CCArmature::SetAnimation(CCArmature* pArm,CCArmatureAnimation *animation)
{
	pArm->setAnimation(animation);
}
CCArmatureAnimation* ScriptBind_CCArmature::GetAnimation(CCArmature* pArm) 
{
	return pArm->getAnimation();
}

void ScriptBind_CCArmature::SetColliderFilter(cocos2d::extension::CCArmature* pArm,cocos2d::extension::CCColliderFilter* colliderfilter)
{
	pArm->setColliderFilter(colliderfilter);
}
