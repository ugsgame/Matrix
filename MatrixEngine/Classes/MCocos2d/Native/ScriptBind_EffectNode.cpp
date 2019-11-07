
//#include "stdneb.h"
#include "cocos2d.h"
#include "MCocos2d/EffectNode/ShaderNode.h"
#include "MCocos2d/EffectNode/GrayNode.h"

#include "ScriptBind_EffectNode.h"

ScriptBind_EffectNode::ScriptBind_EffectNode()
{
	REGISTER_METHOD(SetRealTimeReset);
	REGISTER_METHOD(ResetShader);
	REGISTER_METHOD(SetIsEnable);
	REGISTER_METHOD(IsEnable);
	REGISTER_METHOD(GrayNodeCreate);
}

ScriptBind_EffectNode::~ScriptBind_EffectNode()
{

}

void ScriptBind_EffectNode::SetRealTimeReset(ShaderNode* pNode, bool realTime)
{
	pNode->setRealTimeReset(realTime);
}

void ScriptBind_EffectNode::ResetShader(ShaderNode* pNode)
{
	pNode->resetShader();
}

void ScriptBind_EffectNode::SetIsEnable(ShaderNode* pNode, bool enable)
{
	pNode->setIsEnable(enable);
}

bool ScriptBind_EffectNode::IsEnable(ShaderNode* pNode)
{
	return pNode->IsEnable();
}

GrayNode* ScriptBind_EffectNode::GrayNodeCreate()
{
	return GrayNode::create();
}