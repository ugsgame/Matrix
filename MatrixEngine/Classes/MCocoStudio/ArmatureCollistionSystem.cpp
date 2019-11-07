
#include "ArmatureContactListener.h"
#include "ArmatureCollistionSystem.h"
#include "OBB.h"

#include <stdlib.h>
#include <stdio.h>


static ArmatureCollistionSystem* s_SharedArmCollistionSystem = NULL;

ArmatureCollistionSystem::ArmatureCollistionSystem()
{
	//Test:
	addBoneCollider("RectHurt");
	addBoneCollider("RectTriggerH");
	addBoneCollider("RectTriggerA");
	addBoneCollider("RectAttackS");
	addBoneCollider("RectAttackN");
};

ArmatureCollistionSystem::~ArmatureCollistionSystem()
{

}

void ArmatureCollistionSystem::addBoneCollider(const char* boneName)
{
	CCAssert(boneName!=NULL,"");

	std::string _boneName = boneName;

	// 	int rel = true;
	// 	for (int i = 0; i < sColliders.size(); i++)
	// 	{
	// 		if (sColliders[i] == _boneName)
	// 		{
	// 			rel = false;
	// 			break;
	// 		}
	// 	}
	// 	if (rel)sColliders.push_back(_boneName);

	std::vector<std::string>::iterator result = find(sColliders.begin(),sColliders.end(),_boneName);
	if(result==sColliders.end())sColliders.push_back(_boneName);
}

void ArmatureCollistionSystem::removeBoneCollider(const char* boneName)
{
	std::string _boneName = boneName;
	// 	std::vector<std::string>::iterator itr = sColliders.begin();
	// 	while (itr != sColliders.end()){
	// 		if (*itr == _boneName)
	// 			sColliders.erase(itr);
	// 		itr++;
	// 	}

	std::vector<std::string>::iterator result = find(sColliders.begin(),sColliders.end(),_boneName);
	if(result!=sColliders.end())sColliders.erase(result);
}

void ArmatureCollistionSystem::bindArmature(CCArmature* pArmature)
{
	CCAssert(pArmature!=NULL,"");

	// 	int rel = true;
	// 	for (int i = 0; i < pArmatureList.size(); i++)
	// 	{
	// 		if (pArmatureList[i] == pArmature)
	// 		{
	// 			rel = false;
	// 			break;
	// 		}
	// 	}
	// 	if (rel)pArmatureList.push_back(pArmature);

	std::vector<CCArmature*>::iterator result = find(pArmatureList.begin(),pArmatureList.end(),pArmature);
	if(result==pArmatureList.end())pArmatureList.push_back(pArmature);
}

void ArmatureCollistionSystem::unBindArmature(CCArmature* pArmature)
{
	// 	std::vector<CCArmature*>::iterator itr = pArmatureList.begin();
	// 	while (itr != pArmatureList.end()){
	// 		if (*itr == pArmature)
	// 			pArmatureList.erase(itr);
	// 		itr++;
	// 	}

	std::vector<CCArmature*>::iterator result = find(pArmatureList.begin(),pArmatureList.end(),pArmature);
	if(result!=pArmatureList.end())pArmatureList.erase(result);
}

void ArmatureCollistionSystem::_update(float dt)
{
	for (int i=0;i<pArmatureList.size();i++)
	{
		CCArmature* ArmatureA = pArmatureList[i];
		//if(!ArmatureA)continue;;
		for (int j=i+1;j<pArmatureList.size();j++)
		{
			CCArmature* ArmatureB = pArmatureList[j];
			//碰撞过滤
			if(!shouldCollide(ArmatureA,ArmatureB))continue;
			armatureCollision(ArmatureA,ArmatureB);
		}
	}
// 	std::map<std::string,ColliderData>::iterator it;
// 	for(it=oldColliderMap.begin();it!=oldColliderMap.end();++it)
// 	{
// 		m_pContactListener->OnArmatureExit(it->second.armatureA,it->second.armatureB,it->second.boneAName,it->second.boneBName);
// 		curColliderMap.erase(it->first);
// 	}
// 	oldColliderMap.clear();
// 	oldColliderMap = curColliderMap;
}

ArmatureCollistionSystem* ArmatureCollistionSystem::shareCollistionSystem()
{
	if (!s_SharedArmCollistionSystem)
	{
		s_SharedArmCollistionSystem = ArmatureCollistionSystem::create();

		s_SharedArmCollistionSystem->retain();
	}
	return s_SharedArmCollistionSystem;
}

OBB ArmatureCollistionSystem::getBoneOBB(CCArmature* pArmature,const char* boneName)
{
	CCBone* bone = pArmature->getBone(boneName);
	if (!bone) {
		OBB obb(CCRectMake(0, 0, 0, 0), 0);
		return obb;
	}
	int displayIndex = bone->getDisplayManager()->getCurrentDisplayIndex();
	if (displayIndex < 0) {
		OBB obb(CCRectMake(0, 0, 0, 0), 0);
		return obb;
	}

	CCFrameData* frameData = bone->getTweenData();
	float x = frameData->x + bone->getPositionX();
	float y = frameData->y + bone->getPositionY();
	float scaleX = frameData->scaleX * bone->getScaleX();
	float scaleY = frameData->scaleY * bone->getScaleY();
	float skewX = frameData->skewX + bone->getSkewX() + bone->getRotationX();
	float skewY = frameData->skewY + bone->getSkewY() - bone->getRotationY();

	CCNode* render = bone->getDisplayRenderNode();
	CCSize size = render->getContentSize();

	float width = size.width * scaleX;
	float height = size.height * scaleY;

	CCPoint origin = pArmature->convertToWorldSpace(ccp(x, y));
	OBB obbBox(CCRectMake(origin.x, origin.y, width, height), -skewX);
	return obbBox;
}

void ArmatureCollistionSystem::armatureCollision(CCArmature* pArmatureA,CCArmature* pArmatureB)
{
	for (int i=0;i<sColliders.size();i++)
	{
		std::string ColliderA = sColliders[i];
		OBB obbA = this->getBoneOBB(pArmatureA,ColliderA.c_str());
		if(!obbA.isValidity())continue;
		for (int j=0;j<sColliders.size();j++)
		{
			std::string ColliderB = sColliders[j];
			OBB obbB = this->getBoneOBB(pArmatureB,ColliderB.c_str());
			if(!obbB.isValidity())continue;
			if(OBB::isCollision(obbA,obbB))
			{
				//do something
				//有性能问题
				ColliderA += pArmatureA->m_uID;
				ColliderB += pArmatureB->m_uID;
				std::string key = ColliderA + ColliderB;
				//
				std::map<std::string,ColliderData>::iterator iter;
				iter = curColliderMap.find(key);
				if(iter == curColliderMap.end())
				{
					ColliderData data;
					data.armatureA = pArmatureA;
					data.armatureB = pArmatureB;
					data.boneAName = ColliderA.c_str();
					data.boneBName = ColliderB.c_str();
					curColliderMap.insert(std::pair<std::string,ColliderData>(key,data));

					m_pContactListener->OnArmatureEnter(pArmatureA,pArmatureB,ColliderA.c_str(),ColliderB.c_str());
				}
				else
				{
					//m_pContactListener->OnArmatureStay(pArmatureA,pArmatureB,ColliderA.c_str(),ColliderB.c_str());
					if(oldColliderMap.find(key)!=oldColliderMap.end())oldColliderMap.erase(key);
				}
			}
		}
	}
}

bool ArmatureCollistionSystem::shouldCollide(CCArmature* pArmatureA,CCArmature* pArmatureB)
{
	//Check Boudingbox
	// 	CCRect rectA = pArmatureA->boundingBox();;
	// 	rectA.origin.x *= pArmatureA->getScaleX();
	// 	rectA.origin = pArmatureA->convertToWorldSpace(rectA.origin);
	// 
	// 	CCRect rectB = pArmatureB->boundingBox();
	// 	rectB.origin.x *= pArmatureB->getScaleX();
	// 	rectB.origin = pArmatureB->convertToWorldSpace(rectB.origin);
	// 
	// 	if(!rectA.intersectsRect(rectB))return false;

	//Filter
	CCColliderFilter* filterA = pArmatureA->getColliderFilter();
	CCColliderFilter* filterB = pArmatureB->getColliderFilter();
	//没有filter默可碰撞
	if(!(filterA&&filterB))return true;

	if (filterA->getGroupIndex() == filterB->getGroupIndex() && filterA->getGroupIndex() != 0) 
	{ 
		return filterA->getGroupIndex() > 0; 
	} 

	return (filterA->getMaskBits() & filterB->getCategoryBits()) != 0 && (filterA->getCategoryBits() & filterB->getMaskBits()) != 0; 
}
