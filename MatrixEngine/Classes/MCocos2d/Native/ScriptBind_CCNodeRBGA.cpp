
//#include "stdneb.h"
#include "cocos2d.h"

#include "ScriptBind_CCNodeRBGA.h"

USING_NS_CC;

ScriptBind_CCNodeRGBA::ScriptBind_CCNodeRGBA()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetAlpha);
	REGISTER_METHOD(GetAlpha);
	REGISTER_METHOD(SetColor);
	REGISTER_METHOD(GetColor);
}

ScriptBind_CCNodeRGBA::~ScriptBind_CCNodeRGBA()
{

}

CCNodeRGBA* ScriptBind_CCNodeRGBA::Create()
{
	return CCNodeRGBA::create();
}

void ScriptBind_CCNodeRGBA::SetAlpha(cocos2d::CCNodeRGBA* pNode,int alpha)
{
	CCAssert(pNode,"");
	pNode->setOpacity(alpha);
}

int ScriptBind_CCNodeRGBA::GetAlpha(cocos2d::CCNodeRGBA* pNode)
{
	CCAssert(pNode,"");
	return (int)pNode->getOpacity();
}

void ScriptBind_CCNodeRGBA::SetColor(cocos2d::CCNodeRGBA* pNode,int r,int g ,int b)
{
	CCAssert(pNode,"");
	pNode->setColor(ccc3(r,g,b));
}

void ScriptBind_CCNodeRGBA::GetColor(cocos2d::CCNodeRGBA* pNode,ccColor3B& color)
{
	CCAssert(pNode,"");
	color = pNode->getColor();
}