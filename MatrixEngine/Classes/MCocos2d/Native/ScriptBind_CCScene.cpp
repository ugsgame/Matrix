
//#include "stdneb.h"
#include "cocos2d.h"
#include "MCocos2d/MCScene.h"

#include "ScriptBind_CCScene.h"

USING_NS_CC;

ScriptBind_CCScene::ScriptBind_CCScene()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(GetScene);
}

ScriptBind_CCScene::~ScriptBind_CCScene()
{
	
}

MCScene* ScriptBind_CCScene::Create()
{
	return  MCScene::Create();
}

CCScene* ScriptBind_CCScene::GetScene(MCScene* pScene)
{
	return pScene->GetScene();
}
