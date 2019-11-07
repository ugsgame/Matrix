
#ifndef __SCRIPTBIND_CCTEXTURECACHE__
#define __SCRIPTBIND_CCTEXTURECACHE__

#include "ScriptBind_Cocos2d.h"

class  cocos2d::CCTexture2D;
class  cocos2d::CCImage;

class ScriptBind_CCTextureCache:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCTextureCache();
	~ScriptBind_CCTextureCache();

	virtual const char* GetClassName(){ return "NativeTextureCache";}
protected:

	static void PurgeSharedTextureCache();
	static cocos2d::CCTexture2D* AddImage(mono::string fileimage);
	//TODO:加入c#的回调
	//void addImageAsync(const char *path, CCObject *target, SEL_CallFuncO selector);
	static cocos2d::CCTexture2D* AddUIImageWithKey(cocos2d::CCImage *image, mono::string key);
	static cocos2d::CCTexture2D* TextureForKey(mono::string key);
	static bool ReloadTexture(mono::string fileName);
	static void RemoveAllTextures();
	static void RemoveUnusedTextures();
	static void RemoveTexture(cocos2d::CCTexture2D* texture);
	static void RemoveTextureForKey(mono::string textureKeyName);
	static void DumpCachedTextureInfo();
	static cocos2d::CCTexture2D* AddPVRImage(mono::string filename);
	static cocos2d::CCTexture2D* AddETCImage(mono::string filename);
	static void ReloadAllTextures();
private:
};

#endif