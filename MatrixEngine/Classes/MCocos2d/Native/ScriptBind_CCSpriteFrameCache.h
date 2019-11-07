#ifndef __SCRIPTBIND_CCSpriteFrameCache__
#define __SCRIPTBIND_CCSpriteFrameCache__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCTexture2D;
class cocos2d::CCSpriteFrame;

class ScriptBind_CCSpriteFrameCache:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCSpriteFrameCache();
	~ScriptBind_CCSpriteFrameCache();

	virtual const char* GetClassName(){ return "NativeSpriteFrameCache";}
protected:
	
	static void AddSpriteFramesWithFile_0(mono::string pszPlist);

	static void AddSpriteFramesWithFile_1(mono::string plist, mono::string textureFileName);

	static void AddSpriteFramesWithFile_2(mono::string pszPlist, cocos2d::CCTexture2D *pobTexture);

	static void AddSpriteFrame(cocos2d::CCSpriteFrame *pobFrame, mono::string pszFrameName);

	static void RemoveSpriteFrames(void);

	static void RemoveUnusedSpriteFrames(void);

	static void RemoveSpriteFrameByName(mono::string pszName);

	static void RemoveSpriteFramesFromFile(mono::string plist);

	static void RemoveSpriteFramesFromTexture(cocos2d::CCTexture2D* texture);

	static cocos2d::CCSpriteFrame* SpriteFrameByName(mono::string pszName);

	static void PurgeSharedSpriteFrameCache(void);
};

#endif