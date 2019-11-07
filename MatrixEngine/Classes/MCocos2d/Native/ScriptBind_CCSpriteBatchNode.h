#ifndef __SCRIPTBIND_CCSPRITEBATCHNODE__
#define __SCRIPTBIND_CCSPRITEBATCHNODE__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCSprite;
class cocos2d::CCTexture2D;
class cocos2d::CCSpriteBatchNode;
class cocos2d::CCTextureAtlas;
struct cocos2d::_ccBlendFunc;

class ScriptBind_CCSpriteBatchNode:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCSpriteBatchNode();
	~ScriptBind_CCSpriteBatchNode();

	virtual const char* GetClassName(){ return "NativeSpriteBatchNode"; }
protected:
	static cocos2d::CCSpriteBatchNode* CreateWithFile(mono::string fileName);
	static cocos2d::CCSpriteBatchNode* CreateWithTexture(cocos2d::CCTexture2D *texture);

	static void RemoveChildAtIndex(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,int index,bool doCleanup);

	static void InsertChild(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,cocos2d::CCSprite *child,  int index);
	static void AppendChild(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,cocos2d::CCSprite* sprite);
	static void RemoveSpriteFromAtlas(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,cocos2d::CCSprite *sprite);

	static int RebuildIndexInOrder(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,cocos2d::CCSprite *parent,  int index);
	static int HighestAtlasIndexInChild(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,cocos2d::CCSprite *sprite);
	static int LowestAtlasIndexInChild(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,cocos2d::CCSprite *sprite);
	static int AtlasIndexForChild(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,cocos2d::CCSprite *sprite, int z);

	static void ReorderBatch(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,bool reorder);

	static cocos2d::CCTextureAtlas* GetTextureAtlas(cocos2d::CCSpriteBatchNode* pSpriteBatchNode);
	static void SetTextureAtlas(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,cocos2d::CCTextureAtlas* textureAtlas);

	static cocos2d::CCTexture2D* GetTexture(cocos2d::CCSpriteBatchNode* pSpriteBatchNode);
	static void SetTexture(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,cocos2d::CCTexture2D *texture);

	static void SetBlendFunc(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,cocos2d::_ccBlendFunc& blendFunc);
	static void GetBlendFunc(cocos2d::CCSpriteBatchNode* pSpriteBatchNode,cocos2d::_ccBlendFunc& blendFunc);
private:
};

#endif