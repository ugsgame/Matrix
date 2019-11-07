
#ifndef __SCRIPTBIND_CCBONE__
#define __SCRIPTBIND_CCBONE__

#include "ScriptBind_CocoStudio.h"

class cocos2d::CCNode;
class cocos2d::extension::CCBone;
class cocos2d::extension::CCDisplayData;

class ScriptBind_CCBone: public ScriptBind_CocoStudio
{
public:
	ScriptBind_CCBone();
	~ScriptBind_CCBone();

	virtual const char* GetClassName(){ return "NativeBone";}

	static cocos2d::extension::CCBone* Create();
	static cocos2d::extension::CCBone* CreateWithName(mono::string name);

	static void AddDisplay(cocos2d::extension::CCBone* pBone, cocos2d::extension::CCDisplayData* displayData, int index);
	static void AddNodeDisplay(cocos2d::extension::CCBone* pBone, cocos2d::CCNode* display, int index);
	static void RemoveDisplay(cocos2d::extension::CCBone* pBone, int index);

	static void ChangeDisplayWithIndex(cocos2d::extension::CCBone* pBone, int index, bool force);
	static void ChangeDisplayWithName(cocos2d::extension::CCBone* pBone, mono::string name, bool force);

	static void		AddChildBone(cocos2d::extension::CCBone* pBone,cocos2d::extension::CCBone *child);
	static void		SetParentBone(cocos2d::extension::CCBone* pBone,cocos2d::extension::CCBone *parent);
	static cocos2d::extension::CCBone*	GetParentBone(cocos2d::extension::CCBone* pBone);

	static void RemoveFromParent(cocos2d::extension::CCBone* pBone,bool recursion);
	static void RemoveChildBone(cocos2d::extension::CCBone* pBone,cocos2d::extension::CCBone *bone, bool recursion);

	static cocos2d::CCNode*  GetDisplayRenderNode(cocos2d::extension::CCBone* pBone);
	 static cocos2d::extension::DisplayType  GetDisplayRenderType(cocos2d::extension::CCBone* pBone);

	static mono::string GetName(cocos2d::extension::CCBone* pBone);
};

#endif