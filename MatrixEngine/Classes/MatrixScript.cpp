
//#include "stdneb.h"
#include "cocos2d.h"

#include "MatrixMono.h"
USING_NS_CC;
#include "Native/ScriptBind_System.h"
#include "Native/ScriptBind_ActorSystem.h"
#include "Native/ScriptBind_CollistionSystem.h"
#include "Native/ScriptBind_NetHelper.h"
//TODO
#include "MCocos2d/Native/ScriptBind_CCFileUtils.h"
#include "MCocos2d/Native/ScriptBind_CCDirector.h"
#include "MCocos2d/Native/ScriptBind_CCAction.h"
#include "MCocos2d/Native/ScriptBind_CCLayer.h"
#include "MCocos2d/Native/ScriptBind_CCScene.h"
#include "MCocos2d/Native/ScriptBind_CCObject.h"
#include "MCocos2d/Native/ScriptBind_CCNode.h"
#include "MCocos2d/Native/ScriptBind_CCNodeRBGA.h"
#include "MCocos2d/Native/ScriptBind_CCSprite.h"
#include "MCocos2d/Native/ScriptBind_CCSpriteFrameCache.h"
#include "MCocos2d/Native/ScriptBind_CCLabel.h"
#include "MCocos2d/Native/ScriptBind_EffectNode.h"
#include "MCocos2d/Native/ScriptBind_ScheduleNode.h"
#include "MCocos2d/Native/ScriptBind_CCScheduler.h"
#include "MCocos2d/Native/ScriptBind_CCParticleSystem.h"
#include "MCocos2d/Native/ScriptBind_CCProgressTimer.h"
#include "MCocos2d/Native/ScriptBind_CCTextureCache.h"
#include "MCocos2d/Native/ScriptBind_CCTexture2D.h"
#include "MCocos2d/Native/ScriptBind_CCAnimation.h"
#include "MCocos2d/Native/ScriptBind_CCSpriteFrame.h"
#include "MCocos2d/Native/ScriptBind_CCAnimationFrame.h"
#include "MCocos2d/Native/ScriptBind_CCSpriteBatchNode.h"
#include "MCocos2d/Native/ScriptBind_CCClippingNode.h"
//
#include "MCocoStudio/MatrixCocoStudio.h"
//
#include "MCocosDenshion/MatrixCocosDenshion.h"
//
#include "Mooger/Native/ScriptBind_Mooger.h"
//
#include "MRichControls/MatrixRichControls.h"
//
#include "MatrixScript.h"

//
#include "Platform/Android/AndroidJNI.h"
#include "Platform/Android/AndroidJNIHelper.h"

//TODO

static MatrixScript *s_SharedMatrixScript = NULL;


MatrixScript* MatrixScript::ShareMatrixScript(void)
{
    if (!s_SharedMatrixScript)
    {
        s_SharedMatrixScript = new MatrixScript();
    }

    return s_SharedMatrixScript;
}


bool MatrixScript::SetupMatrixScript(void)
{
	//邦定引擎脚本
	RegisterBinding(ScriptBind_System);
	RegisterBinding(ScriptBind_ActorSystem);
	RegisterBinding(ScriptBind_CollistionSystem);
	RegisterBinding(ScriptBind_NetHelper);
	//TODO 把cocos2d脚本邦定代码称到MatrixCococ2d中去
	RegisterBinding(ScriptBind_CCFileUtils);
 	RegisterBinding(ScriptBind_CCDirector);
	RegisterBinding(ScriptBind_CCAction);
	RegisterBinding(ScriptBind_CCScene);
	RegisterBinding(ScriptBind_CCObject);
	RegisterBinding(ScriptBind_CCLayer);
	RegisterBinding(ScriptBind_CCNode);
	RegisterBinding(ScriptBind_CCNodeRGBA);
	RegisterBinding(ScriptBind_CCSprite);
	RegisterBinding(ScriptBind_CCSpriteFrameCache);
	RegisterBinding(ScriptBind_CCLabel);
	RegisterBinding(ScriptBind_EffectNode);
	RegisterBinding(ScriptBind_ScheduleNode);
	RegisterBinding(ScriptBind_Scheduler);
	RegisterBinding(ScriptBind_CCParticleSystem);
	RegisterBinding(ScriptBind_CCProgressTimer);
	RegisterBinding(ScriptBind_CCTextureCache); 
	RegisterBinding(ScriptBind_CCTexture2D);
	RegisterBinding(ScriptBind_CCAnimation);
	RegisterBinding(ScriptBind_CCSpriteFrame);
	RegisterBinding(ScriptBind_CCAnimationFrame);
	RegisterBinding(ScriptBind_CCSpriteBatchNode);
	RegisterBinding(ScriptBind_CCClippingNode)
	//
	RegisterBinding(ScriptBind_Mooger);

#if(CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
	RegisterBinding(AndroidJNI);
	RegisterBinding(AndroidJNIHelper);
#endif

	MatrixCocoStudio::ShareMatrixCocoStudio()->RegisterScript();

	MatrixCocosDenshion::ShareMatrixCocosDenshion()->RegisterScript();

	MatrixRichControls::ShareMatrixRichControls()->RegisterScript();

	return true;
}

