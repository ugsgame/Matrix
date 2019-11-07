
#include "cocos2d.h"

#include "ScriptBind_CCTextureCache.h"

USING_NS_CC;

ScriptBind_CCTextureCache::ScriptBind_CCTextureCache()
{
	REGISTER_METHOD(PurgeSharedTextureCache);
	REGISTER_METHOD(AddImage);
	REGISTER_METHOD(AddUIImageWithKey);
	REGISTER_METHOD(TextureForKey);
	REGISTER_METHOD(ReloadTexture);
	REGISTER_METHOD(RemoveAllTextures);
	REGISTER_METHOD(RemoveUnusedTextures);
	REGISTER_METHOD(RemoveTexture);
	REGISTER_METHOD(RemoveTextureForKey);
	REGISTER_METHOD(DumpCachedTextureInfo);
	REGISTER_METHOD(AddPVRImage);
	REGISTER_METHOD(AddETCImage);
	REGISTER_METHOD(ReloadAllTextures);
}

ScriptBind_CCTextureCache::~ScriptBind_CCTextureCache()
{

}

void ScriptBind_CCTextureCache::PurgeSharedTextureCache()
{
	CCTextureCache::sharedTextureCache()->purgeSharedTextureCache();
}
CCTexture2D* ScriptBind_CCTextureCache::AddImage(mono::string fileimage)
{
	return CCTextureCache::sharedTextureCache()->addImage(ToMatrixString(fileimage).c_str());
}
//TODO:加入c#的回调
//void addImageAsync(const char *path, CCObject *target, SEL_CallFuncO selector);
CCTexture2D* ScriptBind_CCTextureCache::AddUIImageWithKey(CCImage *image, mono::string key)
{
	return CCTextureCache::sharedTextureCache()->addUIImage(image,ToMatrixString(key).c_str());
}

CCTexture2D* ScriptBind_CCTextureCache::TextureForKey(mono::string key)
{
	return CCTextureCache::sharedTextureCache()->textureForKey(ToMatrixString(key).c_str());
}

bool ScriptBind_CCTextureCache::ReloadTexture(mono::string fileName)
{
	return  CCTextureCache::sharedTextureCache()->reloadTexture(ToMatrixString(fileName).c_str());
}

void ScriptBind_CCTextureCache::RemoveAllTextures()
{
	CCTextureCache::sharedTextureCache()->removeAllTextures();
}
void ScriptBind_CCTextureCache::RemoveUnusedTextures()
{
	CCTextureCache::sharedTextureCache()->removeUnusedTextures();
}

void ScriptBind_CCTextureCache::RemoveTexture(CCTexture2D* texture)
{
	CCTextureCache::sharedTextureCache()->removeTexture(texture);
}

void ScriptBind_CCTextureCache::RemoveTextureForKey(mono::string textureKeyName)
{
	CCTextureCache::sharedTextureCache()->removeTextureForKey(ToMatrixString(textureKeyName).c_str());
}

void ScriptBind_CCTextureCache::DumpCachedTextureInfo()
{
	CCTextureCache::sharedTextureCache()->dumpCachedTextureInfo();
}

CCTexture2D* ScriptBind_CCTextureCache::AddPVRImage(mono::string filename)
{
	return CCTextureCache::sharedTextureCache()->addPVRImage(ToMatrixString(filename).c_str());
}

CCTexture2D* ScriptBind_CCTextureCache::AddETCImage(mono::string filename)
{
	return CCTextureCache::sharedTextureCache()->addETCImage(ToMatrixString(filename).c_str());
}

void ScriptBind_CCTextureCache::ReloadAllTextures()
{
	CCTextureCache::sharedTextureCache()->reloadAllTextures();
}