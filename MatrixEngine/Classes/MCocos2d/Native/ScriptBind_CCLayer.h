
#ifndef __SCRIPTBIND_CCLAYER__
#define __SCRIPTBIND_CCLAYER__

#include "ScriptBind_Cocos2d.h"

class MCLayer;
class cocos2d::CCLayer;

enum LayerState
{
   StateTouch,
   StateSensor,
   StateKeypad
};

class ScriptBind_CCLayer:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCLayer();
	~ScriptBind_CCLayer();

	virtual const char* GetClassName(){ return "NativeLayer";}

	static MCLayer* Create();

	static void		ScheduleStep(MCLayer* pLayer,float dt);
	static void     UnscheduleStep(MCLayer* pLayer);

	static void     SetState(cocos2d::CCLayer* pLayer,int state, bool enable);

	static void     SetTouchMode(cocos2d::CCLayer* pLayer,int mode);
	static int		GetTouchMode(cocos2d::CCLayer* pLayer);

	static void     SetTouchEnabeld(cocos2d::CCLayer* pLayer,bool enable);
	static bool     IsTouchEnabeld(cocos2d::CCLayer* pLayer);

	static void     SetAccelerometerEnabled(cocos2d::CCLayer* pLayer,bool enable);
	static bool     IsAccelerometerEnabled(cocos2d::CCLayer* pLayer);

	static void		SetKeypadEnabled(cocos2d::CCLayer* pLayer,bool enable);
	static bool		IsKeypadEnabled(cocos2d::CCLayer* pLayer);

	static void     SetTouchPriority(cocos2d::CCLayer* pLayer,int priority);
	static int		GetTouchPriority(cocos2d::CCLayer* pLayer);
	//手动开启更新计时器
	static void     ScheduleUpdate(cocos2d::CCLayer* pLayer);
	//
	static float	TickTime(MCLayer* pLayer);
	//
	static void		SetSwallowsTouches(MCLayer* pLayer,bool bSwallowsTouches);
};

#endif