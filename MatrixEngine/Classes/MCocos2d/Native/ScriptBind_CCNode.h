
#ifndef __SCRIPTBIND_CCNODE__
#define __SCRIPTBIND_CCNODE__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCNode;
class cocos2d::CCAction;
class cocos2d::CCGLProgram;
class cocos2d::CCScheduler;

class ScriptBind_CCNode:public ScriptBind_Cocos2d
{
 public:
	ScriptBind_CCNode();
	~ScriptBind_CCNode();

	virtual const char* GetClassName(){ return "NativeNode";}

	static cocos2d::CCNode*	Create();


	static void		SetUserData(cocos2d::CCNode* pNode,void* data); 
	static void*	GetUserData(cocos2d::CCNode* pNode);

	static void		SetZOrder(cocos2d::CCNode* pNode,int zOrder);
	static int		GetZOrder(cocos2d::CCNode* pNode);

	static void		SetVertexZ(cocos2d::CCNode* pNode, float vertexZ);
	static float	GetVertexZ(cocos2d::CCNode* pNode);

	static void		SetScaleX(cocos2d::CCNode* v, float fScaleX);
	static float	GetScaleX(cocos2d::CCNode* pNode);

	static void		SetScaleY(cocos2d::CCNode* pNode, float fScaleY);
	static float	GetScaleY(cocos2d::CCNode* pNode);

	static void		SetScale(cocos2d::CCNode* pNode, float fScale);
	static float	GetScale(cocos2d::CCNode* pNode);

	static void		SetPostion(cocos2d::CCNode* pNode, float posX, float posY);
	static void     SetPostionX(cocos2d::CCNode* pNode, float x);
	static void		SetPostionY(cocos2d::CCNode* pNode, float y);
	static float	GetPostionX(cocos2d::CCNode* pNode);
	static float	GetPostionY(cocos2d::CCNode* pNode);

	static void		SetSkewX(cocos2d::CCNode* pNode, float fSkewX);
	static float	GetSkewX(cocos2d::CCNode* pNode);
	static void		SetSkewY(cocos2d::CCNode* pNode, float fSkewY);
	static float	GetSkewY(cocos2d::CCNode* pNode);

	static void		SetAnchorPoint(cocos2d::CCNode* pNode,float x,float y);
	static float    GetAnchorPointX(cocos2d::CCNode* pNode);
	static float    GetAnchorPointY(cocos2d::CCNode* pNode);

	static void     SetContentSize(cocos2d::CCNode* pNode, float wide, float height);
	static float	GetContentSizeW(cocos2d::CCNode* pNode);
	static float	GetContentSizeH(cocos2d::CCNode* pNode);

	static void		SetVisible(cocos2d::CCNode* pNode, bool visible);
	static bool		IsVisible(cocos2d::CCNode* pNode);

	static void     SetRotation(cocos2d::CCNode* pNode, float fRotation);
	static float	GetRotation(cocos2d::CCNode* pNode);

	static void		SetOrderOfArrival(cocos2d::CCNode* pNode,int order);
	static int		GetOrderOfArrival(cocos2d::CCNode* pNode);

	static void		SetTag(cocos2d::CCNode* pNode,int tag);
	static int		GetTag(cocos2d::CCNode* pNode);

	static void     BoundingBox(cocos2d::CCNode* pNode,cocos2d::CCRect& box);

	static void     SetParent(cocos2d::CCNode* pNode,cocos2d::CCNode* pParent);
	static cocos2d::CCNode* GetParent(cocos2d::CCNode* pNode);

	static void     SetIgnoreAnchorPointForPosition(cocos2d::CCNode* pNode,bool ignore);
	static bool		IsIgnoreAnchorPointForPosition(cocos2d::CCNode* pNode);

	static void		AddChild(cocos2d::CCNode* pParent, cocos2d::CCNode* pNode);
	static void     AddChildByOrder(cocos2d::CCNode* pParent,cocos2d::CCNode* pNode, int zOrder);
	static void     AddChildByOrderTag(cocos2d::CCNode* pParent,cocos2d::CCNode* pNode, int zOrder, int tag);
	static cocos2d::CCNode* GetChildByTag(cocos2d::CCNode* pParent,int tag);
	static cocos2d::CCNode* GetChildByIndex(cocos2d::CCNode* nParent,int index);
	//static void 	GetChildren(cocos2d::CCNode* pParent);
	static int		GetChildrenCount(cocos2d::CCNode* pParent);

	static void		RemoveFromParent(cocos2d::CCNode* pParent);
	static void     RemoveFromParentAndCleanup(cocos2d::CCNode* pParent,bool cleanup);
	static void		RemoveChild(cocos2d::CCNode* pNode,cocos2d::CCNode* pChild);
	static void		RemoveChildAndCleanup(cocos2d::CCNode* pNode,cocos2d::CCNode* pChild,bool cleanup);
	static void     RemoveChildByTag(cocos2d::CCNode* pNode, int tag);
	static void     RemoveChildByTagAndCleanup(cocos2d::CCNode* pNode,int tag,bool cleanup);
	static void     RemoveAllChildren(cocos2d::CCNode* pNode);
	static void     RemoveAllChildrenAndCleanup(cocos2d::CCNode* pNode,bool cleanup);

	static cocos2d::CCAction* RunAction(cocos2d::CCNode* pNode, cocos2d::CCAction* pAction);
	static void		StopAction(cocos2d::CCNode* pNode,cocos2d::CCAction* pAction);
	static void		StopAllAction(cocos2d::CCNode* pNode);

	static void     SetScheduler(cocos2d::CCNode* pNode,cocos2d::CCScheduler* pScheduler);
	static cocos2d::CCScheduler* GetScheduler(cocos2d::CCNode* pNode);

	static void     ScheduleUpdate(cocos2d::CCNode* pNode);
	static void     UnscheduleUpdate(cocos2d::CCNode* pNode);

	static void		ResumeSchedulerAndActions(cocos2d::CCNode* pNode);
	static void		PauseSchedulerAndActions(cocos2d::CCNode* pNode);

	static void		CoverToNodeSpace(cocos2d::CCNode* pNode, cocos2d::CCPoint& worldPoint, cocos2d::CCPoint& outPoint);
	static void		ConvertToWorldSpace(cocos2d::CCNode* pNode, cocos2d::CCPoint& nodePoint, cocos2d::CCPoint& outPoint);
	static void     ConvertToNodeSpaceAR(cocos2d::CCNode* pNode, cocos2d::CCPoint& worldPoint, cocos2d::CCPoint& outPoint);
	static void		ConvertToWorldSpaceAR(cocos2d::CCNode* pNode, cocos2d::CCPoint& nodePoint, cocos2d::CCPoint& outPoint);

	static void     OnEnter(cocos2d::CCNode* pNode);
};

#endif