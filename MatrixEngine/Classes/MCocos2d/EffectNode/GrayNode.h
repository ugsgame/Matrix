#ifndef  GrayNode_H
#define  GrayNode_H

#include "cocos2d.h"
#include "ShaderNode.h"

USING_NS_CC;

class GrayNode :
	public ShaderNode
{
public:
	GrayNode(void);
	~GrayNode(void);

	virtual bool init();
	virtual void initProgram();

	void listenBackToForeground(CCObject *obj);

	virtual void resetShader();
	static GrayNode* create();

protected:

	void ergodicAllNode(CCNode *rootNode);
};
#endif

