#include "cocos2d.h"

#include "ScriptBind_CCSpriteFrameCache.h"

#include "MatrixScript.h"


USING_NS_CC;

ScriptBind_CCSpriteFrameCache::ScriptBind_CCSpriteFrameCache()
{
	REGISTER_METHOD(AddSpriteFramesWithFile_0);
	REGISTER_METHOD(AddSpriteFramesWithFile_1);
	REGISTER_METHOD(AddSpriteFramesWithFile_2);
	REGISTER_METHOD(AddSpriteFrame);
	REGISTER_METHOD(RemoveSpriteFrames);
	REGISTER_METHOD(RemoveUnusedSpriteFrames);
	REGISTER_METHOD(RemoveSpriteFrameByName);
	REGISTER_METHOD(RemoveSpriteFramesFromFile);
	REGISTER_METHOD(RemoveSpriteFramesFromTexture);
	REGISTER_METHOD(SpriteFrameByName);
	REGISTER_METHOD(PurgeSharedSpriteFrameCache);
}

ScriptBind_CCSpriteFrameCache::~ScriptBind_CCSpriteFrameCache()
{

}

void ScriptBind_CCSpriteFrameCache::AddSpriteFramesWithFile_0(mono::string pszPlist)
{
	CCSpriteFrameCache* spriteFrameCache = CCSpriteFrameCache::sharedSpriteFrameCache();
	spriteFrameCache->addSpriteFramesWithFile(ToMatrixString(pszPlist).c_str());
}

void ScriptBind_CCSpriteFrameCache::AddSpriteFramesWithFile_1(mono::string plist, mono::string textureFileName)
{
	CCSpriteFrameCache* spriteFrameCache = CCSpriteFrameCache::sharedSpriteFrameCache();
	spriteFrameCache->addSpriteFramesWithFile(ToMatrixString(plist).c_str(),ToMatrixString(textureFileName).c_str());
}

void ScriptBind_CCSpriteFrameCache::AddSpriteFramesWithFile_2(mono::string pszPlist, cocos2d::CCTexture2D *pobTexture)
{
	CCSpriteFrameCache* spriteFrameCache = CCSpriteFrameCache::sharedSpriteFrameCache();
	spriteFrameCache->addSpriteFramesWithFile(ToMatrixString(pszPlist).c_str(),pobTexture);
}

void ScriptBind_CCSpriteFrameCache::AddSpriteFrame(cocos2d::CCSpriteFrame *pobFrame, mono::string pszFrameName)
{
	CCSpriteFrameCache* spriteFrameCache = CCSpriteFrameCache::sharedSpriteFrameCache();
	spriteFrameCache->addSpriteFrame(pobFrame,ToMatrixString(pszFrameName).c_str());
}

void ScriptBind_CCSpriteFrameCache::RemoveSpriteFrames(void)
{
	CCSpriteFrameCache* spriteFrameCache = CCSpriteFrameCache::sharedSpriteFrameCache();
	spriteFrameCache->removeSpriteFrames();
}

void ScriptBind_CCSpriteFrameCache::RemoveUnusedSpriteFrames(void)
{
	CCSpriteFrameCache* spriteFrameCache = CCSpriteFrameCache::sharedSpriteFrameCache();
	spriteFrameCache->removeUnusedSpriteFrames();
}

void ScriptBind_CCSpriteFrameCache::RemoveSpriteFrameByName(mono::string pszName)
{
	CCSpriteFrameCache* spriteFrameCache = CCSpriteFrameCache::sharedSpriteFrameCache();
	spriteFrameCache->removeSpriteFrameByName(ToMatrixString(pszName).c_str());
}

void ScriptBind_CCSpriteFrameCache::RemoveSpriteFramesFromFile(mono::string plist)
{
	CCSpriteFrameCache* spriteFrameCache = CCSpriteFrameCache::sharedSpriteFrameCache();
	spriteFrameCache->removeSpriteFramesFromFile(ToMatrixString(plist).c_str());
}

void ScriptBind_CCSpriteFrameCache::RemoveSpriteFramesFromTexture(cocos2d::CCTexture2D* texture)
{
	CCSpriteFrameCache* spriteFrameCache = CCSpriteFrameCache::sharedSpriteFrameCache();
	spriteFrameCache->removeSpriteFramesFromTexture(texture);
}

CCSpriteFrame* ScriptBind_CCSpriteFrameCache::SpriteFrameByName(mono::string pszName)
{
	CCSpriteFrameCache* spriteFrameCache = CCSpriteFrameCache::sharedSpriteFrameCache();
	return spriteFrameCache->spriteFrameByName(ToMatrixString(pszName).c_str());
}

void ScriptBind_CCSpriteFrameCache::PurgeSharedSpriteFrameCache(void)
{
	CCSpriteFrameCache* spriteFrameCache = CCSpriteFrameCache::sharedSpriteFrameCache();
	spriteFrameCache->purgeSharedSpriteFrameCache();
}