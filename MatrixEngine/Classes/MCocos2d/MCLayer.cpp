//#include "stdneb.h"
// #include "IMonoObject.h"
// #include "IMonoClass.h"
// #include "IMonoAssembly.h"
#include "MatrixMono.h"
#include "MatrixScript.h"

#include "MCLayer.h"
#include "MCScriptHelper.h"

MCLayer::MCLayer()
{
	tick = 0;
}

MCLayer::~MCLayer()
{

}

bool MCLayer::init()
{

	//////////////////////////////
	return CCLayer::init();
	//CCScheduler* pScheduler = CCDirector::sharedDirector()->getScheduler();
	//pScheduler->scheduleSelector(schedule_selector(MCLayer::onUpdate), this, 0.0f, false);
	//scheduleUpdate();
}

MCLayer *MCLayer::create()
{
	MCLayer *pRet = new MCLayer();
	if (pRet && pRet->init())
	{
		pRet->autorelease();
		return pRet;
	}
	else
	{
		CC_SAFE_DELETE(pRet);
		return NULL;
	}
}

void MCLayer::scheduleStep(float dt)
{
	CCAssert(p_MonoObject,"");

	this->schedule(schedule_selector(MCLayer::step),dt);
}

void MCLayer::unscheduleStep()
{
	CCAssert(p_MonoObject,"");
	this->unschedule(schedule_selector(MCLayer::step));
}

void MCLayer::step(float dt)
{
	CCAssert(p_MonoObject,"");

	p_MonoObject->CallMethod("OnStep",dt);
}

void MCLayer::update(float dt)
{
	CCAssert(p_MonoObject,"");

	CCLayer::update(dt);
	tick = dt;
	//p_MonoObject->CallMethod("OnUpdate",dt);
	p_MonoObject->CallMethod("native_OnUpdate");
}

void MCLayer::onEnter()
{
	CCAssert(p_MonoObject,"");
	scheduleUpdate();

	CCLayer::onEnter();
	p_MonoObject->CallMethod("OnEnter");
}

void MCLayer::onExit()
{
	CCAssert(p_MonoObject,"");
	unscheduleUpdate();

	CCLayer::onExit();
	p_MonoObject->CallMethod("OnExit");
}

void MCLayer::onEnterTransitionDidFinish()
{
	CCAssert(p_MonoObject,"");
	p_MonoObject->CallMethod("OnEnterTransitionFinish");
}

void MCLayer::onExitTransitionDidStart()
{
	CCAssert(p_MonoObject,"");
	p_MonoObject->CallMethod("OnExitTransitionStart");
}

bool MCLayer::ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent)
{
	CCAssert(p_MonoObject,"");
	//bool rel = CCLayer::ccTouchBegan(pTouch,pEvent);
	IMonoObject* mBool = *p_MonoObject->CallMethod("OnTouchBegan",pTouch->getLocation().x,pTouch->getLocation().y);
	return mBool->GetAnyValue().b;
}

void MCLayer::ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent)
{
	CCAssert(p_MonoObject,"");
	CCLayer::ccTouchMoved(pTouch,pEvent);
	p_MonoObject->CallMethod("OnTouchMoved",pTouch->getLocation().x,pTouch->getLocation().y);
}

void MCLayer::ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent)
{
	CCAssert(p_MonoObject,"");
	CCLayer::ccTouchEnded(pTouch,pEvent);
	p_MonoObject->CallMethod("OnTouchEnded",pTouch->getLocation().x,pTouch->getLocation().y);
}

void MCLayer::ccTouchCancelled(CCTouch *pTouch, CCEvent *pEvent)
{
	CCAssert(p_MonoObject,"");
	CCLayer::ccTouchCancelled(pTouch,pEvent);
	p_MonoObject->CallMethod("OnTouchCancelled",pTouch->getLocation().x,pTouch->getLocation().y);

}

void MCLayer::ccTouchesBegan(CCSet *pTouches, CCEvent *pEvent)
{
	CCAssert(p_MonoObject,"");
	CCLayer::ccTouchesBegan(pTouches,pEvent);
	mono::object ccSet = (mono::object)ConvertScriptSet(pTouches);
	p_MonoObject->CallMethod("OnTouchesBegan",ccSet);
}
void MCLayer::ccTouchesMoved(CCSet *pTouches, CCEvent *pEvent)
{
	CCAssert(p_MonoObject,"");
	CCLayer::ccTouchesMoved(pTouches,pEvent);
	mono::object ccSet = (mono::object)ConvertScriptSet(pTouches);
	p_MonoObject->CallMethod("OnTouchesMoved",ccSet);
}
void MCLayer::ccTouchesEnded(CCSet *pTouches, CCEvent *pEvent)
{
	CCAssert(p_MonoObject,"");
	CCLayer::ccTouchesEnded(pTouches,pEvent);
	mono::object ccSet = (mono::object)ConvertScriptSet(pTouches);
	p_MonoObject->CallMethod("OnTouchesEnded",ccSet);
}
void MCLayer::ccTouchesCancelled(CCSet *pTouches, CCEvent *pEvent)
{
	CCAssert(p_MonoObject,"");
	CCLayer::ccTouchesCancelled(pTouches,pEvent);
	mono::object ccSet = (mono::object)ConvertScriptSet(pTouches);
	p_MonoObject->CallMethod("OnTouchesCancelled",ccSet);
}

void MCLayer::keyBackClicked()
{
	CCAssert(p_MonoObject,"");
	CCLayer::keyBackClicked();
	p_MonoObject->CallMethod("OnKeyBackClicked");

}

void MCLayer::keyMenuClicked()
{
	CCAssert(p_MonoObject,"");
	CCLayer::keyMenuClicked();
	p_MonoObject->CallMethod("OnKeyMenuClicked");
}

void MCLayer::didAccelerate(CCAcceleration* pAccelerationValue)
{
	CCAssert(p_MonoObject,"");
	CCLayer::didAccelerate(pAccelerationValue);
	p_MonoObject->CallMethod("OnAccelerate",(float)pAccelerationValue->x,(float)pAccelerationValue->y,(float)pAccelerationValue->z);
}
//////////////////////////////////////////////////////////////////////////
//有性能警告，待优化
//////////////////////////////////////////////////////////////////////////
void* MCLayer::ConvertScriptSet(CCSet *pTouches)
{
	mono::object ccSet = MCScriptHelper::Share()->New_CCSet();

	CCSetIterator iter = pTouches->begin();
	for (;iter != pTouches->end();iter++)
	{
		CCTouch* pTouch = (CCTouch*)(*iter);
		CCPoint point = pTouch->getLocation();
		mono::object ccTouch = MCScriptHelper::Share()->New_CCTouch(pTouch->getID(),point.x,point.y);
		IMonoObject* obj = *ccSet;
		obj->CallMethod("AddTouch",ccTouch);
	}

	return ccSet;
}

void MCLayer::SetSwallowsTouches(bool bSwallowsTouches)
{
	if (isTouchEnabled())
	{
		CCDirector::sharedDirector()->getTouchDispatcher()->removeDelegate(this);

		CCTouchDispatcher* td = CCDirector::sharedDirector()->getTouchDispatcher();
		 td->addTargetedDelegate(this, 0, bSwallowsTouches); //kCCMenuHandlerPriority - 10
	}
}