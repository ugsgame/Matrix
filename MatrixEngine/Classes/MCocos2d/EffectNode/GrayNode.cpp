#include "GrayNode.h"



GrayNode::GrayNode(void)
{

}
GrayNode::~GrayNode(void)
{
}

bool GrayNode::init()
{
	ShaderNode::init();
	this->initProgram();
	return true;
}

void GrayNode::initProgram()
{
	//    GLchar * fragSource = (GLchar*) CCString::createWithContentsOfFile(
	//                                                                       CCFileUtils::sharedFileUtils()->fullPathForFilename("Shaders/example_Blur.fsh").c_str())->getCString();

	const  GLchar * pfrag =    "#ifdef GL_ES \n \
							   precision mediump float; \n \
							   #endif \n\
							   uniform sampler2D u_texture; \n \
							   varying vec2 v_texCoord; \n \
							   varying vec4 v_fragmentColor; \n \
							   void main(void) \n \
							   { \n \
							   float alpha = texture2D(u_texture, v_texCoord).a; \n \
							   float grey = dot(texture2D(u_texture, v_texCoord).rgb, vec3(0.299,0.587,0.114)); \n \
							   gl_FragColor = vec4(grey, grey, grey,alpha); \n \
							   } ";


	CCGLProgram* pProgram = new CCGLProgram();
	pProgram->initWithVertexShaderByteArray(ccPositionTextureColor_vert, pfrag);
	setShaderProgram(pProgram);
	//pProgram->release();
	effectProgram = pProgram;

	CHECK_GL_ERROR_DEBUG();

	getShaderProgram()->addAttribute(kCCAttributeNamePosition, kCCVertexAttrib_Position);
	getShaderProgram()->addAttribute(kCCAttributeNameColor, kCCVertexAttrib_Color);
	getShaderProgram()->addAttribute(kCCAttributeNameTexCoord, kCCVertexAttrib_TexCoords);

	CHECK_GL_ERROR_DEBUG();

	getShaderProgram()->link();

	CHECK_GL_ERROR_DEBUG();

	getShaderProgram()->updateUniforms();
}

GrayNode* GrayNode::create()
{
	GrayNode* pRet = new GrayNode();
	if (pRet && pRet->init())
	{
		pRet->autorelease();
	}
	else
	{
		CC_SAFE_DELETE(pRet);
	}

	return pRet;
}

void GrayNode::listenBackToForeground(CCObject *obj)
{
	setShaderProgram(NULL);
	initProgram();
}

void GrayNode::resetShader()
{
	ergodicAllNode(this); 
}

void GrayNode::ergodicAllNode(CCNode *rootNode)  
{  
	CCObject *temp;  
	//获得rootnode根下的节点  
	CCArray *nodeArray = rootNode->getChildren();  
	CCARRAY_FOREACH(nodeArray, temp)  
	{  
		//判断rootnode的节点下还是否存在节点 遍历调用  
		if(((CCNode*)temp)->getChildrenCount())  
			ergodicAllNode((CCNode*)temp);  
		//这里 do something  
		((CCNode*)temp)->setShaderProgram(this->getShaderProgram());
	}  
	return ;  
}  