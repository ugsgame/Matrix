
#ifndef __SCRIPTBIND_CCSCENE__
#define __SCRIPTBIND_CCSCENE__

#include "ScriptBind_Cocos2d.h"

class MCScene;
class cocos2d::CCScene;

class ScriptBind_CCScene:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCScene();
	~ScriptBind_CCScene();

	virtual const char*			GetClassName(){ return "NativeScene";}

	static MCScene*				Create();

	static cocos2d::CCScene*	GetScene(MCScene* pScene);
};

#endif