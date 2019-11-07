
#ifndef __MAINSCENE_H__
#define __MAINSCENE_H__

#include "cocos2d.h"
class BulletEmitter;

class MainScene:public cocos2d::CCLayer 
{
public:
	virtual bool init();  
	virtual void resize(float w,float h);
	virtual void update(float delta);
	virtual void draw();

	// there's no 'id' in cpp, so we recommend returning the class instance pointer
	static cocos2d::CCScene* scene();

	// 
	static MainScene* sharedMainScene();
	// implement the "static node()" method manually
	CREATE_FUNC(MainScene);
public:

	BulletEmitter* getBulletEmitter();

protected:
	//触摸事件回调
	virtual bool ccTouchBegan(cocos2d::CCTouch *pTouch, cocos2d::CCEvent *pEvent);
	virtual void ccTouchMoved(cocos2d::CCTouch *pTouch, cocos2d::CCEvent *pEvent);
	virtual void ccTouchEnded(cocos2d::CCTouch *pTouch, cocos2d::CCEvent *pEvent);
	virtual void ccTouchCancelled(cocos2d::CCTouch *pTouch, cocos2d::CCEvent *pEvent);

	virtual void ccTouchesBegan(cocos2d::CCSet *pTouches, cocos2d::CCEvent *pEvent);
	virtual void ccTouchesMoved(cocos2d::CCSet *pTouches, cocos2d::CCEvent *pEvent);
	virtual void ccTouchesEnded(cocos2d::CCSet *pTouches, cocos2d::CCEvent *pEvent);
	virtual void ccTouchesCancelled(cocos2d::CCSet *pTouches, cocos2d::CCEvent *pEvent);
private:
	BulletEmitter* m_pBulletEmitter;
	CCNode* m_User;
	static MainScene * sm_pSharedMainScene;
};

#endif