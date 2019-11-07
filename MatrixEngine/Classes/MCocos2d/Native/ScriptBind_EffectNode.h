#ifndef __SCRIPTBIND_EFFECTNODE__
#define __SCRIPTBIND_EFFECTNODE__

#include "ScriptBind_Cocos2d.h"

class ShaderNode;
class GrayNode;

class ScriptBind_EffectNode:public ScriptBind_Cocos2d
{
public:

	ScriptBind_EffectNode();
	~ScriptBind_EffectNode();

	virtual const char* GetClassName(){ return "NativeEffectNode";}

	static void SetRealTimeReset(ShaderNode* pNode, bool realTime);
	static void ResetShader(ShaderNode* pNode);
	static void SetIsEnable(ShaderNode* pNode, bool enable);
	static bool IsEnable(ShaderNode* pNode);
	//GrayNode
	static GrayNode* GrayNodeCreate();
	//
protected:
private:
};

#endif