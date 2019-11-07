
#ifndef __SCRIPTBIND_CCNODERGBA__
#define __SCRIPTBIND_CCNODERGBA__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCNodeRGBA;
struct cocos2d::_ccColor3B;

class ScriptBind_CCNodeRGBA:public ScriptBind_Cocos2d
{
 public:
	ScriptBind_CCNodeRGBA();
	~ScriptBind_CCNodeRGBA();

	virtual const char* GetClassName(){ return "NativeNodeRGBA";}

	static cocos2d::CCNodeRGBA*		Create();

	static void						SetAlpha(cocos2d::CCNodeRGBA* pNode,int alpha);
	static int						GetAlpha(cocos2d::CCNodeRGBA* pNode);

	static void						SetColor(cocos2d::CCNodeRGBA* pNode, int r,int g ,int b);
	static void						GetColor(cocos2d::CCNodeRGBA* pNode,struct cocos2d::_ccColor3B& color);		
};

#endif