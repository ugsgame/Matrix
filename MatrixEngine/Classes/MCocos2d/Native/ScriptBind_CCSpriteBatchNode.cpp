
#include "cocos2d.h"
#include "ScriptBind_CCSpriteBatchNode.h"

USING_NS_CC;

ScriptBind_CCSpriteBatchNode::ScriptBind_CCSpriteBatchNode()
{
	REGISTER_METHOD(CreateWithFile);
	REGISTER_METHOD(CreateWithTexture);
	REGISTER_METHOD(RemoveChildAtIndex);
	REGISTER_METHOD(InsertChild);
	REGISTER_METHOD(AppendChild);
	REGISTER_METHOD(RemoveSpriteFromAtlas);
	REGISTER_METHOD(RebuildIndexInOrder);
	REGISTER_METHOD(HighestAtlasIndexInChild);
	REGISTER_METHOD(LowestAtlasIndexInChild);
	REGISTER_METHOD(AtlasIndexForChild);
	REGISTER_METHOD(ReorderBatch);
	REGISTER_METHOD(GetTextureAtlas);
	REGISTER_METHOD(SetTextureAtlas);
	REGISTER_METHOD(GetTexture);
	REGISTER_METHOD(SetTexture);
	REGISTER_METHOD(SetBlendFunc);
	REGISTER_METHOD(GetBlendFunc);
}
ScriptBind_CCSpriteBatchNode::~ScriptBind_CCSpriteBatchNode()
{

}

CCSpriteBatchNode* ScriptBind_CCSpriteBatchNode::CreateWithFile(mono::string fileName)
{
	return CCSpriteBatchNode::create(ToMatrixString(fileName).c_str());
}
CCSpriteBatchNode* ScriptBind_CCSpriteBatchNode::CreateWithTexture(CCTexture2D *texture)
{
	return CCSpriteBatchNode::createWithTexture(texture);
}

void ScriptBind_CCSpriteBatchNode::RemoveChildAtIndex(CCSpriteBatchNode* pSpriteBatchNode,int index,bool doCleanup)
{
	pSpriteBatchNode->removeChildAtIndex(index,doCleanup);
}

void ScriptBind_CCSpriteBatchNode::InsertChild(CCSpriteBatchNode* pSpriteBatchNode,CCSprite *child, int index)
{
	pSpriteBatchNode->insertChild(child,index);
}
void ScriptBind_CCSpriteBatchNode::AppendChild(CCSpriteBatchNode* pSpriteBatchNode,CCSprite* sprite)
{
	pSpriteBatchNode->appendChild(sprite);
}
void ScriptBind_CCSpriteBatchNode::RemoveSpriteFromAtlas(CCSpriteBatchNode* pSpriteBatchNode,CCSprite *sprite)
{
	pSpriteBatchNode->removeSpriteFromAtlas(sprite);
}

int ScriptBind_CCSpriteBatchNode::RebuildIndexInOrder(CCSpriteBatchNode* pSpriteBatchNode,CCSprite *parent, int index)
{
	return pSpriteBatchNode->rebuildIndexInOrder(parent,index);
}
int ScriptBind_CCSpriteBatchNode::HighestAtlasIndexInChild(CCSpriteBatchNode* pSpriteBatchNode,CCSprite *sprite)
{
	return pSpriteBatchNode->highestAtlasIndexInChild(sprite);
}
int ScriptBind_CCSpriteBatchNode::LowestAtlasIndexInChild(CCSpriteBatchNode* pSpriteBatchNode,CCSprite *sprite)
{
	return pSpriteBatchNode->lowestAtlasIndexInChild(sprite);
}
int ScriptBind_CCSpriteBatchNode::AtlasIndexForChild(CCSpriteBatchNode* pSpriteBatchNode,CCSprite *sprite, int z)
{
	return pSpriteBatchNode->atlasIndexForChild(sprite,z);
}

void ScriptBind_CCSpriteBatchNode::ReorderBatch(CCSpriteBatchNode* pSpriteBatchNode,bool reorder)
{
	pSpriteBatchNode->reorderBatch(reorder);
}

CCTextureAtlas* ScriptBind_CCSpriteBatchNode::GetTextureAtlas(CCSpriteBatchNode* pSpriteBatchNode)
{
	return pSpriteBatchNode->getTextureAtlas();
}
void ScriptBind_CCSpriteBatchNode::SetTextureAtlas(CCSpriteBatchNode* pSpriteBatchNode,CCTextureAtlas* textureAtlas)
{
	pSpriteBatchNode->setTextureAtlas(textureAtlas);
}

 CCTexture2D* ScriptBind_CCSpriteBatchNode::GetTexture(CCSpriteBatchNode* pSpriteBatchNode)
{
	return pSpriteBatchNode->getTexture();
}
void ScriptBind_CCSpriteBatchNode::SetTexture(CCSpriteBatchNode* pSpriteBatchNode,CCTexture2D *texture)
{
	pSpriteBatchNode->setTexture(texture);
}

void ScriptBind_CCSpriteBatchNode::SetBlendFunc(CCSpriteBatchNode* pSpriteBatchNode,_ccBlendFunc& blendFunc)
{
	pSpriteBatchNode->setBlendFunc(blendFunc);
}
void ScriptBind_CCSpriteBatchNode::GetBlendFunc(CCSpriteBatchNode* pSpriteBatchNode,_ccBlendFunc& blendFunc)
{
	blendFunc = pSpriteBatchNode->getBlendFunc();
}