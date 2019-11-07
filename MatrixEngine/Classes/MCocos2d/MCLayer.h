
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
	//�Լ������ʱ��
	void step(float dt);
	//�ص�����
	//����
	virtual void update(float dt);
	//���볡��
	virtual void onEnter();
	//�˳�����
	virtual void onExit();

	//�ص�����
	virtual void onEnterTransitionDidFinish();
	virtual void onExitTransitionDidStart();

	//�����¼��ص�
	virtual bool ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchCancelled(CCTouch *pTouch, CCEvent *pEvent);

	virtual void ccTouchesBegan(CCSet *pTouches, CCEvent *pEvent);
	virtual void ccTouchesMoved(CCSet *pTouches, CCEvent *pEvent);
	virtual void ccTouchesEnded(CCSet *pTouches, CCEvent *pEvent);
	virtual void ccTouchesCancelled(CCSet *pTouches, CCEvent *pEvent);
	//�����ص�
	virtual void keyBackClicked(void);
	virtual void keyMenuClicked(void);
	//�������ص�
	virtual void didAccelerate(CCAcceleration* pAccelerationValue);
	//
	virtual float tickTime(){ return tick; }
	//
	void SetSwallowsTouches(bool bSwallowsTouches);
	//
	//CREATE_FUNC(MCLayer);
	static MCLayer *create(void);
protected:
	//ת����C#��CCSet����
	void*	ConvertScriptSet(CCSet *pTouches);
	float tick;
};

#endif