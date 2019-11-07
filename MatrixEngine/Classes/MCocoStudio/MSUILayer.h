
#ifndef __COCOSTUDIO_UILAYER__
#define __COCOSTUDIO_UILAYER__

#include "cocos2d.h"
#include "cocos-ext.h"

USING_NS_CC;
using namespace cocos2d::ui;

class MSUILayer:public UILayer
{
public:
	virtual bool init();

	//static MSUILayer*Create();
	//
	//更新
	virtual void update(float dt);
	//进入场景
	virtual void onEnter();
	//退出场景
	virtual void onExit();

	//触摸事件回调
	virtual bool ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchCancelled(CCTouch *pTouch, CCEvent *pEvent);
	virtual float tickTime(){ return tick; }

	CREATE_FUNC(MSUILayer);
private:

	float tick;
};

#endif