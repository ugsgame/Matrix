
#ifndef __SCRIPTBIND_CCOBJECT__
#define __SCRIPTBIND_CCOBJECT__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCObject;

class ScriptBind_CCObject:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCObject();
	~ScriptBind_CCObject();

	virtual const char* GetClassName(){ return "NativeObject";}

protected:
	static void SetMonoObject(cocos2d::CCObject* _this,mono::object object);
	static mono::object GetMonoObject(cocos2d::CCObject* _this);
	static bool IsBindMonoObject(cocos2d::CCObject* _this);
	static void UnBindMonoObject(cocos2d::CCObject* _this);

	static void Release(cocos2d::CCObject* _this);
	static void DelayRelease(cocos2d::CCObject* _this);
	static void Retain(cocos2d::CCObject* _this);
	static int	RetainCount(cocos2d::CCObject* _this);
	static bool IsEqual(cocos2d::CCObject* _this,cocos2d::CCObject* object);
	static cocos2d::CCObject* Copy(cocos2d::CCObject* _this);
	static int GetID(cocos2d::CCObject* _this);
};

#endif