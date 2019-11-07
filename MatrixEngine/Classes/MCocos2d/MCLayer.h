
#ifndef __MATRIX_LAYER__
#define __MATRIX_LAYER__

#include "cocos2d.h"

USING_NS_CC;

struct IMonoObject;

class /*CC_DLL*/ MCLayer:public CCLayer
{
public:
	MCLayer(void);
	virtual ~MCLayer(void);
public:
	virtual bool init();

	void scheduleStep(float dt);
	void unscheduleStep();
	//自己定义计时器
	void step(float dt);
	//回调函数
	//更新
	virtual void update(float dt);
	//进入场景
	virtual void onEnter();
	//退出场景
	virtual void onExit();

	//回调函数
	virtual void onEnterTransitionDidFinish();
	virtual void onExitTransitionDidStart();

	//触摸事件回调
	virtual bool ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchCancelled(CCTouch *pTouch, CCEvent *pEvent);

	virtual void ccTouchesBegan(CCSet *pTouches, CCEvent *pEvent);
	virtual void ccTouchesMoved(CCSet *pTouches, CCEvent *pEvent);
	virtual void ccTouchesEnded(CCSet *pTouches, CCEvent *pEvent);
	virtual void ccTouchesCancelled(CCSet *pTouches, CCEvent *pEvent);
	//按键回调
	virtual void keyBackClicked(void);
	virtual void keyMenuClicked(void);
	//传感器回调
	virtual void didAccelerate(CCAcceleration* pAccelerationValue);
	//
	virtual float tickTime(){ return tick; }
	//
	void SetSwallowsTouches(bool bSwallowsTouches);
	//
	//CREATE_FUNC(MCLayer);
	static MCLayer *create(void);
protected:
	//转换成C#的CCSet对象
	void*	ConvertScriptSet(CCSet *pTouches);
	float tick;
};

#endif