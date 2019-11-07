
#ifndef __SCRIPTBIND_SCENEREADER__
#define __SCRIPTBIND_SCENEREADER__

#include "ScriptBind_CocoStudio.h"

class cocos2d::CCObject;
class cocos2d::CCNode;

class ScriptBind_SceneReader:public ScriptBind_CocoStudio
{
public:
	ScriptBind_SceneReader();
	~ScriptBind_SceneReader();

	virtual const char* GetClassName(){ return "NativeSceneReader"; };

protected:

	static void Purge();
	static mono::string SceneReaderVersion();
	static cocos2d::CCNode* CreateNodeWithSceneFile(mono::string pszFileName);
	//static void SetTarget(cocos2d::CCObject *rec, SEL_CallFuncOD selector);
	static cocos2d::CCNode* GetNodeByTag(int nTag); 
	static void DoOptimization();
private:
};

#endif