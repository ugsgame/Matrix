
#ifndef __SCRIPTBIND_CCARMANIM__
#define __SCRIPTBIND_CCARMANIM__

#include "ScriptBind_CocoStudio.h"

class cocos2d::extension::CCArmature;
class cocos2d::extension::CCArmatureAnimation;
class cocos2d::extension::CCColliderFilter;

class ScriptBind_CCArmAnim:public ScriptBind_CocoStudio
{
public:
	ScriptBind_CCArmAnim();
	~ScriptBind_CCArmAnim();

	virtual const char* GetClassName(){ return "NativeAnimation";}

	static cocos2d::extension::CCArmatureAnimation* Create(cocos2d::extension::CCArmature *armature);

	static void SetAnimScale(cocos2d::extension::CCArmatureAnimation* pArmAnim,float scale);
	static float GetAnimScale(cocos2d::extension::CCArmatureAnimation* pArmAnim);

	static void SetSpeedScale(cocos2d::extension::CCArmatureAnimation* pArmAnim,float scale);
	static float GetSpeedScale(cocos2d::extension::CCArmatureAnimation* pArmAnim);
	
	static void PlayWithName(cocos2d::extension::CCArmatureAnimation* pArmAnim,mono::string name,int durationTo , int durationTween , int loop);
	static void PlayWithIndex(cocos2d::extension::CCArmatureAnimation* pArmAnim,int animationIndex,  int durationTo, int durationTween, int loop);
	static void PlayByIndex(cocos2d::extension::CCArmatureAnimation* pArmAnim,int animationIndex,  int durationTo, int durationTween, int loop);

	static void GotoAndPlay(cocos2d::extension::CCArmatureAnimation* pArmAnim,int frameIndex);
	static void GotoAndPause(cocos2d::extension::CCArmatureAnimation* pArmAnim,int frameIndex);

	static void Pause(cocos2d::extension::CCArmatureAnimation* pArmAnim);
	static void Resume(cocos2d::extension::CCArmatureAnimation* pArmAnim);
	static void Stop(cocos2d::extension::CCArmatureAnimation* pArmAnim);

	static int GetMovementCount(cocos2d::extension::CCArmatureAnimation* pArmAnim);
	static mono::string GetCureentMovementID(cocos2d::extension::CCArmatureAnimation* pArmAnim);

	static void SetColliderFilter(cocos2d::extension::CCColliderFilter* filter); 

	static void SetMovementEvent(cocos2d::extension::CCArmatureAnimation* pArmAnim,mono::object event);
	static void SetFrameEvent(cocos2d::extension::CCArmatureAnimation* pArmAnim,mono::object event);
};

#endif