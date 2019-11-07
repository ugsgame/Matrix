
#ifndef __SHADERNODE_H__
#define __SHADERNODE_H__

#include "cocos2d.h"
#include "ShaderNode.h"

USING_NS_CC;

class ShaderNode:public CCNode
{
public:
	ShaderNode();
	~ShaderNode();

	virtual void initProgram();
	virtual void initProgram(const char* vShaderFilename, const char* fShaderFilename);
	virtual void resetShader() = 0;
	virtual void setIsEnable(bool enable);
	virtual bool IsEnable();

	void setRealTimeReset(bool realTime){ bRealTimeReset = realTime; };

	virtual void visit(void);
protected:
	virtual void initNullProgram();
protected:

	bool bRealTimeReset;
	bool bEnable;
	CCGLProgram* effectProgram;
	CCGLProgram* nullProgram;
private:
};

#endif