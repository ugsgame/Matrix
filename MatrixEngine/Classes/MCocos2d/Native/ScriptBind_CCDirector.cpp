
//#include "stdneb.h"
#include "cocos2d.h"
#include "MCocos2d/MCScene.h"

#include "ScriptBind_CCDirector.h"

USING_NS_CC;

class FadeWhiteTransition : public CCTransitionFade 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionFade::create(t, s, ccWHITE); 
	}
};

class FlipXLeftOver : public CCTransitionFlipX 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionFlipX::create(t, s, kCCTransitionOrientationLeftOver);
	}
};

class FlipXRightOver : public CCTransitionFlipX 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionFlipX::create(t, s, kCCTransitionOrientationRightOver);
	}
};

class FlipYUpOver : public CCTransitionFlipY 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionFlipY::create(t, s, kCCTransitionOrientationUpOver); 
	}
};

class FlipYDownOver : public CCTransitionFlipY 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionFlipY::create(t, s, kCCTransitionOrientationDownOver); 
	}
};

class FlipAngularLeftOver : public CCTransitionFlipAngular 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionFlipAngular::create(t, s, kCCTransitionOrientationLeftOver); 
	}
};

class FlipAngularRightOver : public CCTransitionFlipAngular 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionFlipAngular::create(t, s, kCCTransitionOrientationRightOver);
	}
};

class ZoomFlipXLeftOver : public CCTransitionZoomFlipX 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionZoomFlipX::create(t, s, kCCTransitionOrientationLeftOver); 
	}
};

class ZoomFlipXRightOver : public CCTransitionZoomFlipX 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionZoomFlipX::create(t, s, kCCTransitionOrientationRightOver);
	}
};

class ZoomFlipYUpOver : public CCTransitionZoomFlipY 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionZoomFlipY::create(t, s, kCCTransitionOrientationUpOver); 

	}
};

class ZoomFlipYDownOver : public CCTransitionZoomFlipY 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionZoomFlipY::create(t, s, kCCTransitionOrientationDownOver); 
	}
};

class ZoomFlipAngularLeftOver : public CCTransitionZoomFlipAngular 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionZoomFlipAngular::create(t, s, kCCTransitionOrientationLeftOver); 
	}
};

class ZoomFlipAngularRightOver : public CCTransitionZoomFlipAngular 
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		return CCTransitionZoomFlipAngular::create(t, s, kCCTransitionOrientationRightOver);
	}
};

class PageTransitionForward : public CCTransitionPageTurn
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		CCDirector::sharedDirector()->setDepthTest(true);
		return CCTransitionPageTurn::create(t, s, false);
	}
};

class PageTransitionBackward : public CCTransitionPageTurn
{
public:
	static CCTransitionScene* create(float t, CCScene* s)
	{
		CCDirector::sharedDirector()->setDepthTest(true);
		return CCTransitionPageTurn::create(t, s, true);
	}
};

#define MAX_LAYER    41

static std::string transitions[MAX_LAYER] = {
	"CCTransitionJumpZoom",

	"CCTransitionProgressRadialCCW",
	"CCTransitionProgressRadialCW",
	"CCTransitionProgressHorizontal",
	"CCTransitionProgressVertical",
	"CCTransitionProgressInOut",
	"CCTransitionProgressOutIn",

	"CCTransitionCrossFade",
	"TransitionPageForward",
	"TransitionPageBackward",
	"CCTransitionFadeTR",
	"CCTransitionFadeBL",
	"CCTransitionFadeUp",
	"CCTransitionFadeDown",
	"CCTransitionTurnOffTiles",
	"CCTransitionSplitRows",
	"CCTransitionSplitCols",

	"CCTransitionFade",
	"FadeWhiteTransition",

	"FlipXLeftOver",
	"FlipXRightOver",
	"FlipYUpOver",
	"FlipYDownOver",
	"FlipAngularLeftOver",
	"FlipAngularRightOver",

	"ZoomFlipXLeftOver",
	"ZoomFlipXRightOver",
	"ZoomFlipYUpOver",
	"ZoomFlipYDownOver",
	"ZoomFlipAngularLeftOver",
	"ZoomFlipAngularRightOver",

	"CCTransitionShrinkGrow",
	"CCTransitionRotoZoom",

	"CCTransitionMoveInL",
	"CCTransitionMoveInR",
	"CCTransitionMoveInT",
	"CCTransitionMoveInB",
	"CCTransitionSlideInL",
	"CCTransitionSlideInR",
	"CCTransitionSlideInT",
	"CCTransitionSlideInB",


};

CCTransitionScene* createTransition(int nIndex, float t, CCScene* s)
{
	// fix bug #486, without setDepthTest(false), FlipX,Y will flickers
	CCDirector::sharedDirector()->setDepthTest(false);

	switch(nIndex)
	{
	case 0: return CCTransitionJumpZoom::create(t, s);

	case 1: return CCTransitionProgressRadialCCW::create(t, s);
	case 2: return CCTransitionProgressRadialCW::create(t, s);
	case 3: return CCTransitionProgressHorizontal::create(t, s);
	case 4: return CCTransitionProgressVertical::create(t, s);
	case 5: return CCTransitionProgressInOut::create(t, s);
	case 6: return CCTransitionProgressOutIn::create(t, s);

	case 7: return CCTransitionCrossFade::create(t,s);

	case 8: return PageTransitionForward::create(t, s);
	case 9: return PageTransitionBackward::create(t, s);
	case 10: return CCTransitionFadeTR::create(t, s);
	case 11: return CCTransitionFadeBL::create(t, s);
	case 12: return CCTransitionFadeUp::create(t, s);
	case 13: return CCTransitionFadeDown::create(t, s);

	case 14: return CCTransitionTurnOffTiles::create(t, s);

	case 15: return CCTransitionSplitRows::create(t, s);
	case 16: return CCTransitionSplitCols::create(t, s);

	case 17: return CCTransitionFade::create(t, s);
	case 18: return FadeWhiteTransition::create(t, s);

	case 19: return FlipXLeftOver::create(t, s);
	case 20: return FlipXRightOver::create(t, s);
	case 21: return FlipYUpOver::create(t, s);
	case 22: return FlipYDownOver::create(t, s);
	case 23: return FlipAngularLeftOver::create(t, s);
	case 24: return FlipAngularRightOver::create(t, s);

	case 25: return ZoomFlipXLeftOver::create(t, s);
	case 26: return ZoomFlipXRightOver::create(t, s);
	case 27: return ZoomFlipYUpOver::create(t, s);
	case 28: return ZoomFlipYDownOver::create(t, s);
	case 29: return ZoomFlipAngularLeftOver::create(t, s);
	case 30: return ZoomFlipAngularRightOver::create(t, s);

	case 31: return CCTransitionShrinkGrow::create(t, s);
	case 32: return CCTransitionRotoZoom::create(t, s);

	case 33: return CCTransitionMoveInL::create(t, s);
	case 34: return CCTransitionMoveInR::create(t, s);
	case 35: return CCTransitionMoveInT::create(t, s);
	case 36: return CCTransitionMoveInB::create(t, s);

	case 37: return CCTransitionSlideInL::create(t, s);
	case 38: return CCTransitionSlideInR::create(t, s);
	case 39: return CCTransitionSlideInT::create(t, s);
	case 40: return CCTransitionSlideInB::create(t, s);

	default: break;
	}

	return NULL;
}      

ScriptBind_CCDirector::ScriptBind_CCDirector()
{

	REGISTER_METHOD(SetNotificationNode);
	REGISTER_METHOD(GetNotificationNode);
	REGISTER_METHOD(GetTargetPlatform);
	REGISTER_METHOD(SetDisplayFPS);
	REGISTER_METHOD(SetFPS);
	REGISTER_METHOD(GetFPS);
	REGISTER_METHOD(SetFrameSize);
	REGISTER_METHOD(SetResolutionSize);
	REGISTER_METHOD(GetResolutionSize);
	REGISTER_METHOD(GetFrameSize);
	REGISTER_METHOD(GetSecondsPerFrame);
	REGISTER_METHOD(GetScheduler);
	REGISTER_METHOD(RunWithScene);
	REGISTER_METHOD(ReplaceScene);
	REGISTER_METHOD(ReplaceSceneWithTransitions);
	REGISTER_METHOD(GetRuningScene);
	REGISTER_METHOD(GetObjectCount);
	REGISTER_METHOD(GetReleasePoolSize);
	REGISTER_METHOD(PushScene);
	REGISTER_METHOD(PushSceneWithTransitions);
	REGISTER_METHOD(PopScene);
	REGISTER_METHOD(PopToRootScene);
	REGISTER_METHOD(PopToSceneStackLevel);
	REGISTER_METHOD(End);
	REGISTER_METHOD(Pause);
	REGISTER_METHOD(Resume);
	REGISTER_METHOD(IsPaused);
	REGISTER_METHOD(ConverToUI);
	REGISTER_METHOD(ConverToGL);
}

ScriptBind_CCDirector::~ScriptBind_CCDirector()
{

}

void ScriptBind_CCDirector::SetNotificationNode(CCNode* pNode)
{
	CCAssert(pNode!=0,"");
	CCDirector::sharedDirector()->setNotificationNode(pNode);
}
CCNode* ScriptBind_CCDirector::GetNotificationNode()
{
	return CCDirector::sharedDirector()->getNotificationNode();
}

int ScriptBind_CCDirector::GetTargetPlatform()
{
	return CC_TARGET_PLATFORM;
}

void ScriptBind_CCDirector::SetDisplayFPS(bool display)
{
	CCDirector::sharedDirector()->setDisplayStats(display);
}

void ScriptBind_CCDirector::SetFPS(float FPS)
{
	CCDirector::sharedDirector()->setAnimationInterval(FPS);
}

float ScriptBind_CCDirector::GetFPS()
{
	return CCDirector::sharedDirector()->getAnimationInterval();
}

void ScriptBind_CCDirector::SetFrameSize(float width,float height)
{
	CCEGLView* pEGLView = CCEGLView::sharedOpenGLView();
	pEGLView->setFrameSize(width,height);
}

void ScriptBind_CCDirector::SetResolutionSize(float width,float height,ResolutionPolicy resolution)
{
	CCEGLView* pEGLView = CCEGLView::sharedOpenGLView();
	pEGLView->setDesignResolutionSize(width,height,resolution);
}

void ScriptBind_CCDirector::GetResolutionSize(CCSize& size)
{
	CCEGLView* pEGLView = CCEGLView::sharedOpenGLView();
	size = pEGLView->getDesignResolutionSize();
}

void ScriptBind_CCDirector::GetFrameSize(CCSize& size)
{
	CCEGLView* pEGLView = CCEGLView::sharedOpenGLView();
	size = pEGLView->getFrameSize();	
}

float ScriptBind_CCDirector::GetSecondsPerFrame()
{
	return 	CCDirector::sharedDirector()->getSecondsPerFrame();
}

CCScheduler* ScriptBind_CCDirector::GetScheduler()
{
	return CCDirector::sharedDirector()->getScheduler();
}

CCScene* ScriptBind_CCDirector::GetRuningScene()
{
	return CCDirector::sharedDirector()->getRunningScene();
}

int ScriptBind_CCDirector::GetObjectCount()
{
	return CCObject::ObjectCount;
}
int ScriptBind_CCDirector::GetReleasePoolSize()
{
	return CCDelayRelease::sharedDelayRelease()->size();
}

void ScriptBind_CCDirector::RunWithScene(CCScene* scene)
{
	CCDirector::sharedDirector()->runWithScene(scene);
}

void ScriptBind_CCDirector::ReplaceScene(CCScene* scene)
{
	CCDirector::sharedDirector()->replaceScene(scene);
}

void ScriptBind_CCDirector::ReplaceSceneWithTransitions(CCScene* scene, int nIndex, float dTime)
{
	CCScene* pScene = createTransition(nIndex, dTime, scene);
	//外面记得删除
	//s->release();
	if (pScene)
	{
		CCDirector::sharedDirector()->replaceScene(pScene);
	} 
}

void ScriptBind_CCDirector::PushScene(CCScene* scene)
{
	CCDirector::sharedDirector()->pushScene(scene);
}

void ScriptBind_CCDirector::PushSceneWithTransitions(CCScene* scene, int nIndex, float dTime)
{
	CCScene* pScene = createTransition(nIndex, dTime, scene);
	if (pScene)
	{
		CCDirector::sharedDirector()->pushScene(pScene);
	} 
}

void ScriptBind_CCDirector::PopScene()
{
	CCDirector::sharedDirector()->popScene();
}

void ScriptBind_CCDirector::PopToRootScene()
{
	CCDirector::sharedDirector()->popToRootScene();
}

void ScriptBind_CCDirector::PopToSceneStackLevel(int level)
{
	CCDirector::sharedDirector()->popToSceneStackLevel(level);
}

void ScriptBind_CCDirector::End()
{
	CCDirector::sharedDirector()->end();
}

void ScriptBind_CCDirector::Pause()
{
	CCDirector::sharedDirector()->pause();
}

void ScriptBind_CCDirector::Resume()
{
	CCDirector::sharedDirector()->resume();
}

bool ScriptBind_CCDirector::IsPaused()
{
	return CCDirector::sharedDirector()->isPaused();
}

void ScriptBind_CCDirector::ConverToUI(CCPoint& in,CCPoint& out)
{
	out = CCDirector::sharedDirector()->convertToUI(in);
}

void ScriptBind_CCDirector::ConverToGL(CCPoint& in,CCPoint& out)
{
	out = CCDirector::sharedDirector()->convertToGL(in);
}