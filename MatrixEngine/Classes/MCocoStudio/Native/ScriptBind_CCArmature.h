
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

	//注册动画碰撞监听器
	static void RegisterContactListener();
	//邦定GameActor到动画（其实是为了拿它的刚体）
	static void BindGameActor(cocos2d::extension::CCArmature* pArm,GameActor* actor);
	static void UnBindGameActor(cocos2d::extension::CCArmature* pArm);
	//创建碰撞筛选
	static cocos2d::extension::CCColliderFilter* CreateColliderFilter(unsigned short categoryBits = 0x0001, unsigned short maskBits = 0xFFFF, signed short groupIndex = 0);

	static cocos2d::extension::CCArmature* Create();
	static cocos2d::extension::CCArmature* CreateWithName(mono::string name);
	static cocos2d::extension::CCArmature* CreateWithNameAndBone(mono::string name, cocos2d::extension::CCBone *parentBone);
	//////////////////////////////////////////////////////////////////////////
	//获取骨头（相对CCArmature的）矩形
	static void GetBoneRect(cocos2d::extension::CCArmature* pArm,mono::string name,cocos2d::CCRect& rect);
	//获取骨头（相对CCArmature的）坐标
	static void GetBonePosition(cocos2d::extension::CCArmature* pArm,mono::string name,cocos2d::CCPoint& point);
	//指定骨头是否显示
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