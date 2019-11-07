#ifndef  __SCRIPTBIND_CCCLIPINGNODE__
#define  __SCRIPTBIND_CCCLIPINGNODE__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCNode;
class cocos2d::CCClippingNode;

class ScriptBind_CCClippingNode:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCClippingNode();
	~ScriptBind_CCClippingNode();

	virtual const char* GetClassName(){ return "NativeClippingNode"; }
protected:

	static cocos2d::CCClippingNode* Create();
	static cocos2d::CCClippingNode* CreateWithStencil(cocos2d::CCNode* pStencil);

	static cocos2d::CCNode* GetStencil(cocos2d::CCClippingNode* pNode);
	static void SetStencil(cocos2d::CCClippingNode* pNode,cocos2d::CCNode *pStencil);

	static cocos2d::CCNode* AddDefaultStendcil(cocos2d::CCClippingNode* pNode);

	static float GetAlphaThreshold(cocos2d::CCClippingNode* pNode);
	static void SetAlphaThreshold(cocos2d::CCClippingNode* pNode,float fAlphaThreshold);

	static bool IsInverted(cocos2d::CCClippingNode* pNode);
	static void SetInverted(cocos2d::CCClippingNode* pNode,bool bInverted);
private:
};

#endif