
//#include "stdneb.h"
#include "cocos2d.h"

#include "ScriptBind_CCSprite.h"

#include "MatrixScript.h"


USING_NS_CC;

ScriptBind_CCSprite::ScriptBind_CCSprite()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(CreateByFile);
	REGISTER_METHOD(CreateWithSpriteFrameName);

	REGISTER_METHOD(SetBlendFunc);
	REGISTER_METHOD(GetBlendFunc);
	REGISTER_METHOD(SetBatchNode);
	REGISTER_METHOD(GetBatchNode);
	REGISTER_METHOD(SetTextureRect1);
	REGISTER_METHOD(SetTextureRect2);
	REGISTER_METHOD(GetTextureRect);
	REGISTER_METHOD(IsDirty);
	REGISTER_METHOD(SetDirty);
	REGISTER_METHOD(GetAtlasIndex);
	REGISTER_METHOD(SetAtlasIndex);
	REGISTER_METHOD(GetOffsetPosition);
}

ScriptBind_CCSprite::~ScriptBind_CCSprite()
{

}

CCSprite* ScriptBind_CCSprite::Create()
{
	return CCSprite::create();
}

CCSprite* ScriptBind_CCSprite::CreateByFile(mono::string fileName)
{
//TODO
//  	IMonoAssembly* ass = MatrixScript::ShareMatrixScript()->GetEngineAssembly();
//  	ass->GetException("MatrixEngine.Cocos2d","CCSprite","Failed to locate field %s in class %s","xxx","xxx")->Throw();
	return CCSprite::create(ToMatrixString(fileName).c_str());
}

CCSprite* ScriptBind_CCSprite::CreateWithSpriteFrameName(mono::string pszSpriteFrameName)
{
	return CCSprite::createWithSpriteFrameName(ToMatrixString(pszSpriteFrameName).c_str());
}

void ScriptBind_CCSprite::SetBlendFunc(CCSprite* sprite,ccBlendFunc& blendfunc)
{
	sprite->setBlendFunc(blendfunc);
}
void ScriptBind_CCSprite::GetBlendFunc(CCSprite* sprite,ccBlendFunc& blendfunc)
{
	blendfunc = sprite->getBlendFunc();
}

void ScriptBind_CCSprite::SetBatchNode(CCSprite* sprite,CCSpriteBatchNode* batchNode)
{
	sprite->setBatchNode(batchNode);
}

CCSpriteBatchNode* ScriptBind_CCSprite::GetBatchNode(CCSprite* sprite)
{
	return sprite->getBatchNode();
}

void ScriptBind_CCSprite::SetTextureRect1(CCSprite* sprite, float x,float y,float w,float h)
{
	sprite->setTextureRect(CCRect(x,y,w,h));
}

void ScriptBind_CCSprite::SetTextureRect2(CCSprite* sprite, float x,float y,float w,float h, bool rotated, float sw,float sh)
{
	sprite->setTextureRect(CCRect(x,y,w,h),rotated,CCSize(sw,sh));
}

void ScriptBind_CCSprite::GetTextureRect(CCSprite* sprite,CCRect& rect)
{
	rect = sprite->getTextureRect();	
}

bool ScriptBind_CCSprite::IsDirty(CCSprite* sprite)
{
	return sprite->isDirty();
}
void ScriptBind_CCSprite::SetDirty(CCSprite* sprite,bool bDirty)
{
	sprite->setDirty(bDirty);
}	

int  ScriptBind_CCSprite::GetAtlasIndex(CCSprite* sprite)
{
	return sprite->getAtlasIndex();
}
void ScriptBind_CCSprite::SetAtlasIndex(CCSprite* sprite,int uAtlasIndex)
{
	sprite->setAtlasIndex(uAtlasIndex);
}

void ScriptBind_CCSprite::GetOffsetPosition(CCSprite* sprite,CCPoint& point)
{
	point = sprite->getOffsetPosition();
}

