
#ifndef __MATRIX_SCENE__
#define __MATRIX_SCENE__

#include "cocos2d.h"
#include "MCLayer.h"

USING_NS_CC;

struct IMonoObject;

class /*CC_DLL*/ MCScene : public MCLayer
{
public:
	// Here's a difference. Method 'init' in cocos2d-x returns bool, instead of returning 'id' in cocos2d-iphone
	virtual bool init();  

	// there's no 'id' in cpp, so we recommend returning the class instance pointer
	static MCScene* Create();

	//TODO
	void SetScene(CCScene* scene)
	{ 
		mScene = scene;
		scene->addChild(this);
		mScene->setUserData(this);
	}
	CCScene* GetScene(){ return mScene;}

	//»Øµ÷º¯Êý
// 	virtual void onEnterTransitionDidFinish();
// 	virtual void onExitTransitionDidStart();


	// implement the "static node()" method manually
	CREATE_FUNC(MCScene);

private:
	CCScene*		mScene;
};

#endif