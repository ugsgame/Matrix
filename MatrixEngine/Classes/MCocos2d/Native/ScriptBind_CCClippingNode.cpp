
#include "cocos2d.h"
#include "ScriptBind_CCClippingNode.h"

USING_NS_CC;

ScriptBind_CCClippingNode::ScriptBind_CCClippingNode()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(CreateWithStencil);

	REGISTER_METHOD(GetStencil);
	REGISTER_METHOD(SetStencil);

	REGISTER_METHOD(AddDefaultStendcil);

	REGISTER_METHOD(GetAlphaThreshold);
	REGISTER_METHOD(SetAlphaThreshold);

	REGISTER_METHOD(IsInverted);
	REGISTER_METHOD(SetInverted);


}

ScriptBind_CCClippingNode::~ScriptBind_CCClippingNode()
{

}

CCClippingNode* ScriptBind_CCClippingNode::Create()
{
	return CCClippingNode::create();
}
CCClippingNode* ScriptBind_CCClippingNode::CreateWithStencil(CCNode* pStencil)
{
	return CCClippingNode::create(pStencil);
}

CCNode* ScriptBind_CCClippingNode::GetStencil(CCClippingNode* pNode)
{
	return pNode->getStencil();
}
void ScriptBind_CCClippingNode::SetStencil(CCClippingNode* pNode,CCNode *pStencil)
{
	pNode->setStencil(pStencil);
}
CCNode* ScriptBind_CCClippingNode::AddDefaultStendcil(CCClippingNode* pNode)
{
	CCDrawNode *stencil = CCDrawNode::create();
	CCPoint rectangle[4];
	rectangle[0] = ccp(0, 0);
	rectangle[1] = ccp(pNode->getContentSize().width, 0);
	rectangle[2] = ccp(pNode->getContentSize().width, pNode->getContentSize().height);
	rectangle[3] = ccp(0, pNode->getContentSize().height);

	ccColor4F white = {1, 1, 1, 1};
	stencil->drawPolygon(rectangle, 4, white, 1, white);
	pNode->setStencil(stencil);
	return stencil;
}

float ScriptBind_CCClippingNode::GetAlphaThreshold(CCClippingNode* pNode)
{
	return pNode->getAlphaThreshold();
}
void ScriptBind_CCClippingNode::SetAlphaThreshold(CCClippingNode* pNode,float fAlphaThreshold)
{
	pNode->setAlphaThreshold(fAlphaThreshold);
}

bool ScriptBind_CCClippingNode::IsInverted(CCClippingNode* pNode)
{
	return pNode->isInverted();
}
void ScriptBind_CCClippingNode::SetInverted(CCClippingNode* pNode,bool bInverted)
{
	pNode->setInverted(true);
}

