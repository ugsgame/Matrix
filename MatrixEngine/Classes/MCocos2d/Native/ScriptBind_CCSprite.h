
#ifndef __SCRIPTBIND_CCSPRITE__
#define __SCRIPTBIND_CCSPRITE__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCSprite;
class cocos2d::CCSpriteBatchNode;

class cocos2d::CCPoint;
class cocos2d::CCRect;
struct cocos2d::_ccBlendFunc;

//TODO

class ScriptBind_CCSprite:public ScriptBind_Cocos2d
{
 public:
	ScriptBind_CCSprite();
	~ScriptBind_CCSprite();

	virtual const char* GetClassName(){ return "NativeSprite";}

	static cocos2d::CCSprite* Create();
	static cocos2d::CCSprite* CreateByFile(mono::string fileName);
	static cocos2d::CCSprite* CreateWithSpriteFrameName(mono::string pszSpriteFrameName);


	static void SetBlendFunc(cocos2d::CCSprite* sprite,cocos2d::_ccBlendFunc& blendfunc);
	static void GetBlendFunc(cocos2d::CCSprite* sprite,cocos2d::_ccBlendFunc& blendfunc);

	static void SetBatchNode(cocos2d::CCSprite* sprite,cocos2d::CCSpriteBatchNode* batchNode);
	static cocos2d::CCSpriteBatchNode* GetBatchNode(cocos2d::CCSprite* sprite);

	static void SetTextureRect1(cocos2d::CCSprite* sprite, float x,float y,float w,float h);
	static void SetTextureRect2(cocos2d::CCSprite* sprite, float x,float y,float w,float h, bool rotated, float sw,float wh);
	static void GetTextureRect(cocos2d::CCSprite* sprite,cocos2d::CCRect& rect); 

	static bool IsDirty(cocos2d::CCSprite* sprite);
	static void SetDirty(cocos2d::CCSprite* sprite,bool bDirty);

	static int  GetAtlasIndex(cocos2d::CCSprite* sprite);
	static void SetAtlasIndex(cocos2d::CCSprite* sprite,int uAtlasIndex);

	static void GetOffsetPosition(cocos2d::CCSprite* sprite,cocos2d::CCPoint& point);
};

#endif