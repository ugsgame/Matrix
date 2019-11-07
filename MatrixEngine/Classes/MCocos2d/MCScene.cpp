//#include "stdneb.h"
#include "IMonoObject.h"

#include "MCScene.h"


MCScene* MCScene::Create()
{
	// 'scene' is an autorelease object
	CCScene *scene = CCScene::create();

	// 'layer' is an autorelease object
	MCScene *layer = MCScene::create();
	layer->SetScene(scene);

	// return the scene
	return layer;
}

// on "init" you need to initialize your instance
bool MCScene::init()
{
	//////////////////////////////
	// 1. super init first
	if ( !MCLayer::init() )
	{
		return false;
	}

	this->mScene = NULL;

	return true;
}

/*
void MCScene::onEnterTransitionDidFinish()
{
	CCAssert(pScript,"");
	MCLayer::onEnterTransitionDidFinish();
	pScript->CallMethod("OnEnterTransitionFinish");
}

void MCScene::onExitTransitionDidStart()
{
	CCAssert(pScript,"");
	MCLayer::onExitTransitionDidStart();
	pScript->CallMethod("OnExitTransitionStart");
}
*/