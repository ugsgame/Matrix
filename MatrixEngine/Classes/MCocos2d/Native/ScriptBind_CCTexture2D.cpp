
#include "cocos2d.h"
#include "ScriptBind_CCTexture2D.h"

USING_NS_CC;

ScriptBind_CCTexture2D::ScriptBind_CCTexture2D()
{
	REGISTER_METHOD(Create);
/*	REGISTER_METHOD(InitWithData);*/
	REGISTER_METHOD(InitWithImage);
	REGISTER_METHOD(InitWithString);
	REGISTER_METHOD(InitWithPVRFile);
	REGISTER_METHOD(InitWithETCFile);
}

ScriptBind_CCTexture2D::~ScriptBind_CCTexture2D()
{

}

CCTexture2D* ScriptBind_CCTexture2D::Create()
{
	return new CCTexture2D();
}

// CCTexture2D* ScriptBind_CCTexture2D::nitWithData()
// {
// 	return NULL;
// }

bool ScriptBind_CCTexture2D::InitWithImage(CCImage* uiImage)
{
	CCTexture2D* texture = new CCTexture2D();
	return texture->initWithImage(uiImage);
}
bool ScriptBind_CCTexture2D::InitWithString(mono::string text,mono::string fontName,float fontSize,CCSize& dimensions,CCTextAlignment hAlignment, CCVerticalTextAlignment vAlignment)
{
	CCTexture2D* texture = new CCTexture2D();
	return texture->initWithString(ToMatrixString(text).c_str(),ToMatrixString(fontName).c_str(),fontSize,dimensions,hAlignment,vAlignment);
}
bool ScriptBind_CCTexture2D::InitWithPVRFile(mono::string file)
{
	CCTexture2D* texture = new CCTexture2D();
	return texture->initWithPVRFile(ToMatrixString(file).c_str());
}
bool ScriptBind_CCTexture2D::InitWithETCFile(mono::string file)
{
	CCTexture2D* texture = new CCTexture2D();
	return texture->initWithETCFile(ToMatrixString(file).c_str());
}