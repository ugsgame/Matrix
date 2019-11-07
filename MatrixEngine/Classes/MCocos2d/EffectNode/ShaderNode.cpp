
#include "ShaderNode.h"

ShaderNode::ShaderNode():
	bRealTimeReset(false),
	bEnable(true)
{
	effectProgram  = NULL;
	nullProgram    = NULL;
}

ShaderNode::~ShaderNode()
{
	if(nullProgram)
	{
		nullProgram->release();
		nullProgram = NULL;
	}
	if(effectProgram)
	{
		effectProgram->release();
		effectProgram = NULL;
	}
}

void ShaderNode::initProgram()
{

}

void ShaderNode::setIsEnable(bool enable)
{
	if(enable)
	{
		nullProgram->release();
		this->setShaderProgram(NULL);
		initProgram();
	}
	else
	{
		effectProgram->release();
		this->setShaderProgram(NULL);
		initNullProgram();
	}
	bEnable = enable;
	resetShader();
	this->setRealTimeReset(false);
}

bool ShaderNode::IsEnable()
{
	return bEnable;
}

void ShaderNode::initProgram(const char* vShaderFilename, const char* fShaderFilename)
{

}

void ShaderNode::initNullProgram()
{
	const  GLchar * pfrag =    "#ifdef GL_ES \n \
							   precision mediump float; \n \
							   #endif \n\
							   uniform sampler2D u_texture; \n \
							   varying vec2 v_texCoord; \n \
							   void main(void) \n \
							   { \n \
							   gl_FragColor = texture2D(u_texture, v_texCoord); \n \
							   } ";

	CCGLProgram* pProgram = new CCGLProgram();
	pProgram->initWithVertexShaderByteArray(ccPositionTextureColor_vert, pfrag);
	setShaderProgram(pProgram);
	//pProgram->release();
	nullProgram = pProgram;
	CHECK_GL_ERROR_DEBUG();
	getShaderProgram()->addAttribute(kCCAttributeNamePosition, kCCVertexAttrib_Position);
	getShaderProgram()->addAttribute(kCCAttributeNameColor, kCCVertexAttrib_Color);
	getShaderProgram()->addAttribute(kCCAttributeNameTexCoord, kCCVertexAttrib_TexCoords);
	CHECK_GL_ERROR_DEBUG();
	getShaderProgram()->link();
	CHECK_GL_ERROR_DEBUG();
	getShaderProgram()->updateUniforms();
}

void ShaderNode::visit()
{
	if(bRealTimeReset)resetShader();
	CCNode::visit();
}

