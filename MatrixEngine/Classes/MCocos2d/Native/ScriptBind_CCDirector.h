
#ifndef __SCRIPTBIND_CCDIRECTOR__
#define __SCRIPTBIND_CCDIRECTOR__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCScene;
class cocos2d::CCNode;
class cocos2d::CCSize;
class cocos2d::CCPoint;
class cocos2d::CCScheduler;

class ScriptBind_CCDirector:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCDirector();
	~ScriptBind_CCDirector();

	virtual const char* GetClassName(){ return "NativeDirector";}

	static void   SetNotificationNode(cocos2d::CCNode* pNode);
	static cocos2d::CCNode* GetNotificationNode();

	static int GetTargetPlatform(); 

	static void SetDisplayFPS(bool display);

	static void SetFPS(float FPS);
	static float GetFPS();

	static void SetFrameSize(float width,float height);

	static void SetResolutionSize(float width,float height,ResolutionPolicy resolution);

	static void GetResolutionSize(cocos2d::CCSize& size);

	static void GetFrameSize(cocos2d::CCSize& size);

	static float GetSecondsPerFrame();
	
	static cocos2d::CCScheduler* GetScheduler();

	static void RunWithScene(cocos2d::CCScene* scene);

	static void ReplaceScene(cocos2d::CCScene* scene);

	static void ReplaceSceneWithTransitions(cocos2d::CCScene* scene, int nIndex, float dTime);

	static void PushScene(cocos2d::CCScene* scene);

	static void PushSceneWithTransitions(CCScene* scene, int nIndex, float dTime);

	static void PopScene(void);

	static void PopToRootScene(void);

	static void PopToSceneStackLevel(int level);

	static void End();

	static void Pause();

	static void Resume();

	static bool IsPaused();

	static cocos2d::CCScene* GetRuningScene(); 

	static int GetObjectCount();
	static int GetReleasePoolSize();

	static void ConverToUI(cocos2d::CCPoint& in,cocos2d::CCPoint& out);
	static void ConverToGL(cocos2d::CCPoint& in,cocos2d::CCPoint& out);
};


#endif