
#ifndef __SCRIPTBIND_CCANIMATION__
#define __SCRIPTBIND_CCANIMATION__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCAnimation;
class cocos2d::CCSpriteFrame;
class cocos2d::CCTexture2D;
class cocos2d::CCRect;

class ScriptBind_CCAnimation:public ScriptBind_Cocos2d
{
public:

	ScriptBind_CCAnimation();
	~ScriptBind_CCAnimation();

	virtual const char* GetClassName(){ return "NativeAnimation"; }

protected:
	static cocos2d::CCAnimation* Create();
	static cocos2d::CCAnimation* CreateWithSpriteFrames(mono::object spriteFrames);
	static cocos2d::CCAnimation* CreateWithAnimationFrames(mono::object animationFrameNames);

	static void AddSpriteFrame(cocos2d::CCAnimation* pAnimation,cocos2d::CCSpriteFrame *pFrame);
	static void AddSpriteFrameWithFileName(cocos2d::CCAnimation* pAnimation,mono::string pzFileName);
	static void AddSpriteFrameWithTexture(cocos2d::CCAnimation* pAnimation,cocos2d::CCTexture2D* pTexture,cocos2d::CCRect& rect);

	static float GetTotalDelayUnits(cocos2d::CCAnimation* pAnimation);
	static void  SetDelayPerUnit(cocos2d::CCAnimation* pAnimation,float delayPerUnit);
	static float GetDelayPerUnit(cocos2d::CCAnimation* pAnimation);
	static float GetDuration(cocos2d::CCAnimation* pAnimation);

	static void SetFrames(cocos2d::CCAnimation* pAnimation,mono::object frames);
	//static mono::object GetFrames(cocos2d::CCAnimation* pAnimation);

	static void SetRestoreOriginalFrame(cocos2d::CCAnimation* pAnimation,bool var);
	static bool GetRestoreOriginalFrame(cocos2d::CCAnimation* pAnimation);

	static void SetLoops(cocos2d::CCAnimation* pAnimation,int var);
	static int  GetLoops(cocos2d::CCAnimation* pAnimation);
private:
};

#endif // !__SCRIPTBIND_CCANIMATION__
