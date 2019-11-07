
//#include "stdneb.h"
#include "cocos2d.h"

#include "ScriptBind_CCNode.h"

USING_NS_CC;


ScriptBind_CCNode::ScriptBind_CCNode()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetUserData);
	REGISTER_METHOD(GetUserData);
	REGISTER_METHOD(SetZOrder);
	REGISTER_METHOD(GetZOrder);
	REGISTER_METHOD(SetVertexZ);
	REGISTER_METHOD(GetVertexZ);
	REGISTER_METHOD(SetScaleX);
	REGISTER_METHOD(GetScaleX);
	REGISTER_METHOD(SetScaleY);
	REGISTER_METHOD(GetScaleY);
	REGISTER_METHOD(SetScale);
	REGISTER_METHOD(GetScale);
	REGISTER_METHOD(SetPostion);
	REGISTER_METHOD(SetPostionX);
	REGISTER_METHOD(SetPostionY);
	REGISTER_METHOD(GetPostionX);
	REGISTER_METHOD(GetPostionY);
	REGISTER_METHOD(SetSkewX);
	REGISTER_METHOD(GetSkewX);
	REGISTER_METHOD(SetSkewY);
	REGISTER_METHOD(GetSkewY);
	REGISTER_METHOD(SetAnchorPoint);
	REGISTER_METHOD(GetAnchorPointX);
	REGISTER_METHOD(GetAnchorPointY);
	REGISTER_METHOD(SetContentSize);
	REGISTER_METHOD(GetContentSizeW);
	REGISTER_METHOD(GetContentSizeH);
	REGISTER_METHOD(SetVisible);
	REGISTER_METHOD(IsVisible);
	REGISTER_METHOD(SetRotation);
	REGISTER_METHOD(GetRotation);
	REGISTER_METHOD(SetOrderOfArrival);
	REGISTER_METHOD(GetOrderOfArrival);
	REGISTER_METHOD(SetTag);
	REGISTER_METHOD(GetTag);
	REGISTER_METHOD(BoundingBox);
	REGISTER_METHOD(SetParent);
	REGISTER_METHOD(GetParent);
	REGISTER_METHOD(SetIgnoreAnchorPointForPosition);
	REGISTER_METHOD(IsIgnoreAnchorPointForPosition);
	REGISTER_METHOD(AddChild);
	REGISTER_METHOD(AddChildByOrder);
	REGISTER_METHOD(AddChildByOrderTag);
	REGISTER_METHOD(GetChildByTag);
	REGISTER_METHOD(GetChildByIndex);
	REGISTER_METHOD(GetChildrenCount);
	REGISTER_METHOD(RemoveFromParent);
	REGISTER_METHOD(RemoveFromParentAndCleanup);
	REGISTER_METHOD(RemoveChild);
	REGISTER_METHOD(RemoveChildAndCleanup);
	REGISTER_METHOD(RemoveChildByTag);
	REGISTER_METHOD(RemoveChildByTagAndCleanup);
	REGISTER_METHOD(RemoveAllChildren);
	REGISTER_METHOD(RemoveAllChildrenAndCleanup);
	REGISTER_METHOD(RunAction);
	REGISTER_METHOD(StopAction);
	REGISTER_METHOD(StopAllAction);
	REGISTER_METHOD(GetScheduler);
	REGISTER_METHOD(SetScheduler);
	REGISTER_METHOD(ScheduleUpdate);
	REGISTER_METHOD(UnscheduleUpdate);
	REGISTER_METHOD(ResumeSchedulerAndActions);
	REGISTER_METHOD(PauseSchedulerAndActions);
	REGISTER_METHOD(CoverToNodeSpace);
	REGISTER_METHOD(ConvertToWorldSpace);
	REGISTER_METHOD(ConvertToNodeSpaceAR);
	REGISTER_METHOD(ConvertToWorldSpaceAR);
	REGISTER_METHOD(OnEnter);

}

ScriptBind_CCNode::~ScriptBind_CCNode()
{

}

CCNode* ScriptBind_CCNode::Create()
{
	return CCNode::create();
}

void  ScriptBind_CCNode::SetUserData(cocos2d::CCNode* pNode,void* data)
{
	pNode->setUserData(data);
}
void* ScriptBind_CCNode::GetUserData(cocos2d::CCNode* pNode)
{
	return pNode->getUserData();
}

void ScriptBind_CCNode::SetZOrder(CCNode* pNode,int zOrder)
{
	//TODO
	CCAssert(pNode,"");

	pNode->setZOrder(zOrder);
}

int ScriptBind_CCNode::GetZOrder(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");

	return pNode->getZOrder();
}

void ScriptBind_CCNode::SetVertexZ(CCNode* pNode, float vertexZ)
{
	//TODO
	CCAssert(pNode,"");

	return pNode->setVertexZ(vertexZ);
}

float ScriptBind_CCNode::GetVertexZ(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");

	return pNode->getVertexZ();
}

void ScriptBind_CCNode::SetScaleX(CCNode* pNode, float fScaleX)
{
	//TODO
	CCAssert(pNode,"");

	pNode->setScaleX(fScaleX);
}

float ScriptBind_CCNode::GetScaleX(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");

	return pNode->getScaleX();
}

void ScriptBind_CCNode::SetScaleY(CCNode* pNode, float fScaleY)
{
	//TODO
	CCAssert(pNode,"");

	pNode->setScaleY(fScaleY);
}

float ScriptBind_CCNode::GetScaleY(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");

	return pNode->getScaleY();
}

void ScriptBind_CCNode::SetScale(CCNode* pNode, float fScale)
{
	//TODO
	CCAssert(pNode,"");

	pNode->setScale(fScale);
}

float ScriptBind_CCNode::GetScale(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");

	return pNode->getScale();
}

void ScriptBind_CCNode::SetPostion(CCNode* pNode, float posX, float posY)
{
	//TODO
	CCAssert(pNode,"");
	pNode->setPosition(ccp(posX,posY));
}
void  ScriptBind_CCNode::SetPostionX(CCNode* pNode, float x)
{
	//TODO
	CCAssert(pNode,"");
	pNode->setPositionX(x);
}
void  ScriptBind_CCNode::SetPostionY(CCNode* pNode, float y)
{
	//TODO
	CCAssert(pNode,"");
	pNode->setPositionY(y);
}
float ScriptBind_CCNode::GetPostionX(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	return pNode->getPositionX();
}
float ScriptBind_CCNode::GetPostionY(CCNode* pNode)
{	
	//TODO
	CCAssert(pNode,"");
	return pNode->getPositionY();
}

void	ScriptBind_CCNode::SetSkewX(CCNode* pNode, float fSkewX)
{
	//TODO
	CCAssert(pNode,"");
	pNode->setSkewX(fSkewX);
}
float	ScriptBind_CCNode::GetSkewX(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	return pNode->getSkewY();
}
void	ScriptBind_CCNode::SetSkewY(CCNode* pNode, float fSkewY)
{
	//TODO
	CCAssert(pNode,"");
	pNode->setSkewY(fSkewY);
}
float	ScriptBind_CCNode::GetSkewY(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	return pNode->getSkewY();
}

void	ScriptBind_CCNode::SetAnchorPoint(CCNode* pNode,float x,float y)
{
	//TODO
	CCAssert(pNode,"");
	pNode->setAnchorPoint(ccp(x,y));
}
float    ScriptBind_CCNode::GetAnchorPointX(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	CCPoint point = pNode->getAnchorPoint();
	return point.x;
}
float    ScriptBind_CCNode::GetAnchorPointY(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	CCPoint point = pNode->getAnchorPoint();
	return point.y;
}

void    ScriptBind_CCNode::SetContentSize(CCNode* pNode, float wide, float height)
{
	//TODO
	CCAssert(pNode,"");
	CCSize size(ccp(wide,height));
	pNode->setContentSize(size);
}
float	ScriptBind_CCNode::GetContentSizeW(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	CCSize size = pNode->getContentSize();
	return size.width;
}
float	ScriptBind_CCNode::GetContentSizeH(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	CCSize size = pNode->getContentSize();
	return size.height;
}

void	ScriptBind_CCNode::SetVisible(CCNode* pNode, bool visible)
{
	//TODO
	CCAssert(pNode,"");
	pNode->setVisible(visible);
}
bool	ScriptBind_CCNode::IsVisible(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	return pNode->isVisible();
}

void    ScriptBind_CCNode::SetRotation(CCNode* pNode, float fRotation)
{
	//TODO
	CCAssert(pNode,"");
	pNode->setRotation(fRotation);
}
float	ScriptBind_CCNode::GetRotation(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	return pNode->getRotation();
}

void	ScriptBind_CCNode::SetOrderOfArrival(CCNode* pNode,int order)
{
	//TODO
	CCAssert(pNode,"");
	pNode->setOrderOfArrival(order);
}
int		ScriptBind_CCNode::GetOrderOfArrival(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	return pNode->getOrderOfArrival();
}

void	ScriptBind_CCNode::SetTag(CCNode* pNode,int tag)
{
	//TODO
	CCAssert(pNode,"");
	pNode->setTag(tag);
}
int		ScriptBind_CCNode::GetTag(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	return pNode->getTag();
}

void ScriptBind_CCNode::SetScheduler(CCNode* pNode,CCScheduler* pScheduler)
{
	CCAssert(pNode,"");
	pNode->setScheduler(pScheduler);
}

CCScheduler* ScriptBind_CCNode::GetScheduler(CCNode* pNode)
{
	return pNode->getScheduler();
}

void   ScriptBind_CCNode::BoundingBox(CCNode* pNode,CCRect& box)
{
	//
	CCAssert(pNode,"");
	box = pNode->boundingBox();
}

void    ScriptBind_CCNode::SetParent(CCNode* pNode,CCNode* pParent)
{
	//TODO
	CCAssert(pNode,"");
	pNode->setParent(pParent);
}
CCNode* ScriptBind_CCNode::GetParent(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	return pNode->getParent();
}

void ScriptBind_CCNode::SetIgnoreAnchorPointForPosition(CCNode* pNode,bool ignore)
{
	//TODO
	CCAssert(pNode,"");
	pNode->ignoreAnchorPointForPosition(ignore);
}
bool ScriptBind_CCNode::IsIgnoreAnchorPointForPosition(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	return pNode->isIgnoreAnchorPointForPosition();
}

void ScriptBind_CCNode::AddChild(CCNode* pParent, CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	pParent->addChild(pNode);
}
void ScriptBind_CCNode::AddChildByOrder(CCNode* pParent,CCNode* pNode, int zOrder)
{
	//TODO
	CCAssert(pNode,"");
	pParent->addChild(pNode,zOrder);
}
void     ScriptBind_CCNode::AddChildByOrderTag(CCNode* pParent,CCNode* pNode, int zOrder, int tag)
{
	//TODO
	CCAssert(pNode,"");
	pParent->addChild(pNode,zOrder,tag);
}
CCNode* ScriptBind_CCNode::GetChildByTag(CCNode* pParent,int tag)
{
	//TODO
	CCAssert(pParent,"");
	return pParent->getChildByTag(tag);
}

CCNode* ScriptBind_CCNode::GetChildByIndex(CCNode* pParent,int index)
{
	CCAssert(pParent,"");
	CCArray* nArray = pParent->getChildren();
	CCAssert( index < (int)nArray->count() ,"child index is illegal!!!");
	return (CCNode*)nArray->objectAtIndex(index);
}

int	ScriptBind_CCNode::GetChildrenCount(CCNode* pParent)
{
	//TODO
	CCAssert(pParent,"");
	return pParent->getChildrenCount();
}

void ScriptBind_CCNode::RemoveFromParent(CCNode* pParent)
{
	//TODO
	CCAssert(pParent,"");
	pParent->removeFromParent();
}
void	ScriptBind_CCNode::RemoveFromParentAndCleanup(CCNode* pParent,bool cleanup)
{
	//TODO
	CCAssert(pParent,"");
	return pParent->removeFromParentAndCleanup(cleanup);
}
void	ScriptBind_CCNode::RemoveChild(CCNode* pNode,CCNode* pChild)
{
	//TODO
	CCAssert(pNode,"");
	 pNode->removeChild(pChild);
}
void	ScriptBind_CCNode::RemoveChildAndCleanup(CCNode* pNode,CCNode* pChild,bool cleanup)
{
	//TODO
	CCAssert(pNode,"");
	pNode->removeChild(pChild,cleanup);
}
void    ScriptBind_CCNode::RemoveChildByTag(CCNode* pNode, int tag)
{
	//TODO
	CCAssert(pNode,"");
	pNode->removeChildByTag(tag);
}
void    ScriptBind_CCNode::RemoveChildByTagAndCleanup(CCNode* pNode,int tag,bool cleanup)
{
	//TODO
	CCAssert(pNode,"");
	pNode->removeChildByTag(tag,cleanup);
}
void    ScriptBind_CCNode::RemoveAllChildren(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	pNode->removeAllChildren();
}
void    ScriptBind_CCNode::RemoveAllChildrenAndCleanup(CCNode* pNode,bool cleanup)
{
	//TODO
	CCAssert(pNode,"");
	pNode->removeAllChildrenWithCleanup(cleanup);
}

CCAction* ScriptBind_CCNode::RunAction(CCNode* pNode, CCAction* pAction)
{
	//TODO
	CCAssert(pNode,"");
	return pNode->runAction(pAction);
}
void	ScriptBind_CCNode::StopAction(CCNode* pNode,CCAction* pAction)
{
	//TODO
	CCAssert(pNode,"");
	pNode->stopAction(pAction);
}
void	ScriptBind_CCNode::StopAllAction(CCNode* pNode)
{
	//TODO
	CCAssert(pNode,"");
	pNode->stopAllActions();
}

void ScriptBind_CCNode::ScheduleUpdate(CCNode* pNode)
{
	CCAssert(pNode,"");
	pNode->scheduleUpdate();
}

void ScriptBind_CCNode::UnscheduleUpdate(CCNode* pNode)
{
	CCAssert(pNode,"");
	pNode->unscheduleUpdate();
}

void ScriptBind_CCNode::ResumeSchedulerAndActions(cocos2d::CCNode* pNode)
{
	CCAssert(pNode,"");
	pNode->resumeSchedulerAndActions();
}
void ScriptBind_CCNode::PauseSchedulerAndActions(cocos2d::CCNode* pNode)
{
	CCAssert(pNode,"");
	pNode->pauseSchedulerAndActions();
}

void	ScriptBind_CCNode::CoverToNodeSpace(CCNode* pNode, CCPoint& worldPoint, CCPoint& outPoint)
{
	//TODO
	CCAssert(pNode,"");	
	outPoint = pNode->convertToNodeSpace(worldPoint);
}
void	ScriptBind_CCNode::ConvertToWorldSpace(CCNode* pNode, CCPoint& nodePoint, CCPoint& outPoint)
{
	//TODO
	CCAssert(pNode,"");
	outPoint = pNode->convertToWorldSpace(nodePoint);
}
void	ScriptBind_CCNode::ConvertToNodeSpaceAR(CCNode* pNode, CCPoint& worldPoint, CCPoint& outPoint)
{
	//TODO
	CCAssert(pNode,"");
	outPoint = pNode->convertToNodeSpaceAR(worldPoint);
}
void	ScriptBind_CCNode::ConvertToWorldSpaceAR(CCNode* pNode, CCPoint& nodePoint, CCPoint& outPoint)
{
	//TODO
	CCAssert(pNode,"");
	outPoint = pNode->convertToWorldSpaceAR(nodePoint);
}

void ScriptBind_CCNode::OnEnter(CCNode* pNode)
{
	CCAssert(pNode,"");
	if(!pNode->isRunning())pNode->onEnter();
}

