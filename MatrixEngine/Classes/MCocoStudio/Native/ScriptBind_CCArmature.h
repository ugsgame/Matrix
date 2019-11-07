
#ifndef __SCRIPTBIND_CCARMATURE__
#define __SCRIPTBIND_CCARMATURE__


#include "ScriptBind_CocoStudio.h"

class cocos2d::extension::CCArmature;
class cocos2d::extension::CCBone;
class cocos2d::extension::CCArmatureAnimation;

class GameActor;

class ScriptBind_CCArmature:public ScriptBind_CocoStudio 
{
public:
	ScriptBind_CCArmature();
	~ScriptBind_CCArmature();

	virtual const char* GetClassName(){ return "NativeArmature";}

	//ע�ᶯ����ײ������
	static void RegisterContactListener();
	//�GameActor����������ʵ��Ϊ�������ĸ��壩
	static void BindGameActor(cocos2d::extension::CCArmature* pArm,GameActor* actor);
	static void UnBindGameActor(cocos2d::extension::CCArmature* pArm);
	//������ײɸѡ
	static cocos2d::extension::CCColliderFilter* CreateColliderFilter(unsigned short categoryBits = 0x0001, unsigned short maskBits = 0xFFFF, signed short groupIndex = 0);

	static cocos2d::extension::CCArmature* Create();
	static cocos2d::extension::CCArmature* CreateWithName(mono::string name);
	static cocos2d::extension::CCArmature* CreateWithNameAndBone(mono::string name, cocos2d::extension::CCBone *parentBone);
	//////////////////////////////////////////////////////////////////////////
	//��ȡ��ͷ�����CCArmature�ģ�����
	static void GetBoneRect(cocos2d::extension::CCArmature* pArm,mono::string name,cocos2d::CCRect& rect);
	//��ȡ��ͷ�����CCArmature�ģ�����
	static void GetBonePosition(cocos2d::extension::CCArmature* pArm,mono::string name,cocos2d::CCPoint& point);
	//ָ����ͷ�Ƿ���ʾ
	static bool BoneIsDisplay(cocos2d::extension::CCArmature* pArm,mono::string name);
	//////////////////////////////////////////////////////////////////////////
	static void AddBone(cocos2d::extension::CCArmature* pArm, cocos2d::extension::CCBone* bone,mono::string parentName);
	static cocos2d::extension::CCBone* GetBone(cocos2d::extension::CCArmature* pArm,mono::string name);
	static void RemoveBone(cocos2d::extension::CCArmature* pArm,cocos2d::extension::CCBone* bone,bool recursion);
	static void ChangeBoneParent(cocos2d::extension::CCArmature* pArm,cocos2d::extension::CCBone* bone,mono::string parentName);

	static void SetBlendFunc(cocos2d::extension::CCArmature* pArm,cocos2d::ccBlendFunc &blendFunc);
	static cocos2d::ccBlendFunc GetBlendFunc(cocos2d::extension::CCArmature* pArm);

	static void SetParticlePositionType(cocos2d::extension::CCArmature* pArm,cocos2d::tCCPositionType positionType);
	static cocos2d::tCCPositionType GetParticlePositionType(cocos2d::extension::CCArmature* pArm);

	static void SetAnimation(cocos2d::extension::CCArmature* pArm,cocos2d::extension::CCArmatureAnimation *animation);
	static cocos2d::extension::CCArmatureAnimation* GetAnimation(cocos2d::extension::CCArmature* pArm); 


	static void SetColliderFilter(cocos2d::extension::CCArmature* pArm,cocos2d::extension::CCColliderFilter* colliderfilter);
};

#endif