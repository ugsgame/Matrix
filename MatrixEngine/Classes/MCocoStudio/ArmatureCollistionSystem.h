
#ifndef __ARMATURECOLLISTIONSYSTEM__
#define __ARMATURECOLLISTIONSYSTEM__

#include "cocos2d.h"
#include "cocos-ext.h"
#include "ExtensionMacros.h"


USING_NS_CC_EXT;
USING_NS_CC;


class ArmatureContactListener;
class OBB;

class ArmatureCollistionSystem:CCNode
{
	struct ColliderData
	{
		CCArmature* armatureA;
		CCArmature* armatureB;

		const char* boneAName;
		const char* boneBName; 
	};
public:
	ArmatureCollistionSystem();
	~ArmatureCollistionSystem();

	void registerContactListener(ArmatureContactListener* listener)
	{
		this->m_pContactListener = listener;
	}

	void addBoneCollider(const char* boneName);
	void removeBoneCollider(const char* boneName);

	void bindArmature(CCArmature* pArmature);
	void unBindArmature(CCArmature* pArmature);

	//virtual void draw(void);

	void _update(float dt);

	static ArmatureCollistionSystem* shareCollistionSystem();

	CREATE_FUNC(ArmatureCollistionSystem);
protected:
	OBB getBoneOBB(CCArmature* pArmature,const char* boneName);
	void armatureCollision(CCArmature* pArmatureA,CCArmature* pArmatureB);
	bool shouldCollide(CCArmature* pArmatureA,CCArmature* pArmatureB);
private:

	std::vector<std::string> sColliders;
	std::vector<CCArmature*> pArmatureList;

	std::map<std::string,ColliderData> curColliderMap;
	std::map<std::string,ColliderData> oldColliderMap;

	ArmatureContactListener*  m_pContactListener;
};

#endif