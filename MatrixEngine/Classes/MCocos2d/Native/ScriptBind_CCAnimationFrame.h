
#ifndef __SCRIPTBIND_CCANIMATIONFRAME__
#define __SCRIPTBIND_CCANIMATIONFRAME__ 

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCAnimationFrame;
class cocos2d::CCSpriteFrame;

class ScriptBind_CCAnimationFrame:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCAnimationFrame();
	~ScriptBind_CCAnimationFrame();

	virtual const char* GetClassName(){ return "NativeAnimationFrame"; }
protected:
	static cocos2d::CCAnimationFrame* Create();

	static void SetSpriteFrame(cocos2d::CCAnimationFrame* pAnimationFrame,cocos2d::CCSpriteFrame* pSpriteFrame);

	static void SetDelayUnits(cocos2d::CCAnimationFrame* pAnimationFrame,float var);
	static float GetDelayUnits(cocos2d::CCAnimationFrame* pAnimationFrame);
private:
};

#endif