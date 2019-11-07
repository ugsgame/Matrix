
#include "cocos2d.h"
#include "ScriptBind_CCAnimationFrame.h"

USING_NS_CC;

ScriptBind_CCAnimationFrame::ScriptBind_CCAnimationFrame()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetSpriteFrame);
	REGISTER_METHOD(SetDelayUnits);
	REGISTER_METHOD(GetDelayUnits);
}
ScriptBind_CCAnimationFrame::~ScriptBind_CCAnimationFrame()
{

}

CCAnimationFrame* ScriptBind_CCAnimationFrame::Create()
{
	CCAnimationFrame* animationFrame = new CCAnimationFrame();
	animationFrame->autorelease();
	return animationFrame;
}

void ScriptBind_CCAnimationFrame::SetSpriteFrame(CCAnimationFrame* pAnimationFrame,CCSpriteFrame* pSpriteFrame)
{
	pAnimationFrame->setSpriteFrame(pSpriteFrame);
}

void ScriptBind_CCAnimationFrame::SetDelayUnits(CCAnimationFrame* pAnimationFrame,float var)
{
	pAnimationFrame->setDelayUnits(var);
}
float ScriptBind_CCAnimationFrame::GetDelayUnits(CCAnimationFrame* pAnimationFrame)
{
	return pAnimationFrame->getDelayUnits();
}