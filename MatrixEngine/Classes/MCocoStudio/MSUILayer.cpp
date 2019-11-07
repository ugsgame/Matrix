
//#include "stdneb.h"

#include "MatrixMono.h"
#include "MatrixScript.h"

#include "MSUILayer.h"


bool MSUILayer::init()
{
	//////////////////////////////
	return UILayer::init(); 
	//scheduleUpdate();
}

// MSUILayer* MSUILayer::Create()
// {
// 	MSUILayer* layer = MSUILayer::create();
// 	return layer;
// }

void MSUILayer::update(float dt)
{
	CCAssert(p_MonoObject,"");

	UILayer::update(dt);
	tick = dt;
	//p_MonoObject->CallMethod("OnUpdate",dt);
	p_MonoObject->CallMethod("native_OnUpdate");
}

void MSUILayer::onEnter()
{
	CCAssert(p_MonoObject,"");
	scheduleUpdate();

	UILayer::onEnter();
	p_MonoObject->CallMethod("OnEnter");
}

void MSUILayer::onExit()
{
	CCAssert(p_MonoObject,"");
	unscheduleUpdate();

	UILayer::onExit();
	p_MonoObject->CallMethod("OnExit");
}

bool MSUILayer::ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent)
{
	CCAssert(p_MonoObject,"");
	bool rel = UILayer::ccTouchBegan(pTouch,pEvent);
	IMonoObject* mBool = *p_MonoObject->CallMethod("OnTouchBegan",pTouch->getLocation().x,pTouch->getLocation().y);

	return mBool->GetAnyValue().b||rel;
}

void MSUILayer::ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent)
{
	CCAssert(p_MonoObject,"");
	UILayer::ccTouchMoved(pTouch,pEvent);
	p_MonoObject->CallMethod("OnTouchMoved",pTouch->getLocation().x,pTouch->getLocation().y);
}

void MSUILayer::ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent)
{
	CCAssert(p_MonoObject,"");
	UILayer::ccTouchEnded(pTouch,pEvent);
	p_MonoObject->CallMethod("OnTouchEnded",pTouch->getLocation().x,pTouch->getLocation().y);
}

void MSUILayer::ccTouchCancelled(CCTouch *pTouch, CCEvent *pEvent)
{
	CCAssert(p_MonoObject,"");
	UILayer::ccTouchCancelled(pTouch,pEvent);
	p_MonoObject->CallMethod("OnTouchCancelled",pTouch->getLocation().x,pTouch->getLocation().y);

}

