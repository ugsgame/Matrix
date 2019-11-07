
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"
#include "ExtensionMacros.h"

#include "ScriptBind_CCArmAnim.h"

USING_NS_CC;
USING_NS_CC_EXT;

class AnimationEvent:public CCObject
{
public:
	AnimationEvent(){}
	~AnimationEvent(){};
	static AnimationEvent* Create(IMonoObject* obj)
	{
		AnimationEvent* event = new AnimationEvent();
		int objectCount = event->ObjectCount;
		event->setMonoObject(obj);
		if(event->ObjectCount>objectCount)event->ObjectCount--;
		return event;
	}
	//TODO 回调c#函数中第一个产数 应该传入c#的对像，而不是指针

	void OfAnimationEvent(CCArmature *armature, MovementEventType movementType, const char *movementID)
	{
		//TODO
		//pScript->CallMethod("AnimationEvent",armature,(int)movementType,movementID);	
		p_MonoObject->CallMethod("AnimationEvent",(int)movementType,movementID);	
	}
	void OfFrameEvent(CCBone *bone, const char *evt, int originFrameIndex, int currentFrameIndex)
	{
		//TODO
		//pScript->CallMethod("FrameEvent",bone,evt,originFrameIndex,currentFrameIndex);
		p_MonoObject->CallMethod("FrameEvent",evt,originFrameIndex,currentFrameIndex);	
	}
};

ScriptBind_CCArmAnim::ScriptBind_CCArmAnim()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetAnimScale);
	REGISTER_METHOD(GetAnimScale);
	REGISTER_METHOD(SetSpeedScale);
	REGISTER_METHOD(GetSpeedScale);
	REGISTER_METHOD(PlayWithName);
	REGISTER_METHOD(PlayWithIndex);
	REGISTER_METHOD(PlayByIndex);
	REGISTER_METHOD(GotoAndPlay);
	REGISTER_METHOD(GotoAndPause);
	REGISTER_METHOD(Pause);
	REGISTER_METHOD(Resume);
	REGISTER_METHOD(Stop);
	REGISTER_METHOD(GetMovementCount);
	REGISTER_METHOD(GetCureentMovementID);
	REGISTER_METHOD(SetMovementEvent);
	REGISTER_METHOD(SetFrameEvent);
}
ScriptBind_CCArmAnim::~ScriptBind_CCArmAnim()
{

}

CCArmatureAnimation* ScriptBind_CCArmAnim::Create(CCArmature *armature)
{
	return CCArmatureAnimation::create(armature);
}

void ScriptBind_CCArmAnim::SetAnimScale(CCArmatureAnimation* pArmAnim,float scale)
{
	pArmAnim->setAnimationScale(scale);
}
float ScriptBind_CCArmAnim::GetAnimScale(CCArmatureAnimation* pArmAnim)
{
	return pArmAnim->getAnimationScale();
}

void ScriptBind_CCArmAnim::SetSpeedScale(CCArmatureAnimation* pArmAnim,float scale)
{
	pArmAnim->setSpeedScale(scale);
}
float ScriptBind_CCArmAnim::GetSpeedScale(CCArmatureAnimation* pArmAnim)
{
	return pArmAnim->getSpeedScale();
}

void ScriptBind_CCArmAnim::PlayWithName(CCArmatureAnimation* pArmAnim,mono::string name,int durationTo , int durationTween , int loop)
{
	pArmAnim->play(ToMatrixString(name).c_str(),durationTo,durationTween,loop);
}
void ScriptBind_CCArmAnim::PlayWithIndex(CCArmatureAnimation* pArmAnim,int animationIndex,  int durationTo, int durationTween, int loop)
{
	CCAssert(pArmAnim!=0,"");
	pArmAnim->playWithIndex(animationIndex,durationTween,durationTo,loop);
}
void ScriptBind_CCArmAnim::PlayByIndex(CCArmatureAnimation* pArmAnim,int animationIndex,  int durationTo, int durationTween, int loop)
{
	pArmAnim->playByIndex(animationIndex,durationTo,durationTween,loop);
}

void ScriptBind_CCArmAnim::GotoAndPlay(CCArmatureAnimation* pArmAnim,int frameIndex)
{
	pArmAnim->gotoAndPlay(frameIndex);
}
void ScriptBind_CCArmAnim::GotoAndPause(CCArmatureAnimation* pArmAnim,int frameIndex)
{
	pArmAnim->gotoAndPause(frameIndex);
}

void ScriptBind_CCArmAnim::Pause(CCArmatureAnimation* pArmAnim)
{
	pArmAnim->pause();
}
void ScriptBind_CCArmAnim::Resume(CCArmatureAnimation* pArmAnim)
{
	pArmAnim->resume();
}
void ScriptBind_CCArmAnim::Stop(CCArmatureAnimation* pArmAnim)
{
	pArmAnim->stop();
}

int ScriptBind_CCArmAnim::GetMovementCount(CCArmatureAnimation* pArmAnim)
{
	return pArmAnim->getMovementCount();
}
mono::string ScriptBind_CCArmAnim::GetCureentMovementID(CCArmatureAnimation* pArmAnim)
{
	return ToMonoString(pArmAnim->getCurrentMovementID().c_str());
}

void ScriptBind_CCArmAnim::SetMovementEvent(CCArmatureAnimation* pArmAnim,mono::object event)
{
	CCAssert(event,"");
	pArmAnim->setMovementEventCallFunc(AnimationEvent::Create(*event),movementEvent_selector(AnimationEvent::OfAnimationEvent));
}
void ScriptBind_CCArmAnim::SetFrameEvent(CCArmatureAnimation* pArmAnim,mono::object event)
{
	CCAssert(event,"");
	pArmAnim->setFrameEventCallFunc(AnimationEvent::Create(*event),frameEvent_selector(AnimationEvent::OfFrameEvent));
}