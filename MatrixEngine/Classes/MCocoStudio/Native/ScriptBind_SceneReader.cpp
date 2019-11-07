
#include "cocos2d.h"
#include "cocos-ext.h"
#include "ExtensionMacros.h"

#include "ScriptBind_SceneReader.h"

USING_NS_CC;
USING_NS_CC_EXT;

ScriptBind_SceneReader::ScriptBind_SceneReader()
{
	REGISTER_METHOD(Purge);
	REGISTER_METHOD(SceneReaderVersion);
	REGISTER_METHOD(CreateNodeWithSceneFile);
	REGISTER_METHOD(GetNodeByTag);
	REGISTER_METHOD(DoOptimization);
}

ScriptBind_SceneReader::~ScriptBind_SceneReader()
{

}

void ScriptBind_SceneReader::Purge()
{
	SceneReader::sharedSceneReader()->purge();
}

mono::string ScriptBind_SceneReader::SceneReaderVersion()
{
	return ToMonoString(SceneReader::sharedSceneReader()->sceneReaderVersion());
}

cocos2d::CCNode* ScriptBind_SceneReader::CreateNodeWithSceneFile(mono::string pszFileName)
{
	return SceneReader::sharedSceneReader()->createNodeWithSceneFile(ToMatrixString(pszFileName).c_str());
}

cocos2d::CCNode* ScriptBind_SceneReader::GetNodeByTag(int nTag)
{
	return SceneReader::sharedSceneReader()->getNodeByTag(nTag);
}

void ScriptBind_SceneReader::DoOptimization()
{
	SceneReader::sharedSceneReader()->doOptimization();
}