

#include "cocos2d.h"

#include "ScriptBind_CCLabel.h"

USING_NS_CC;

ScriptBind_CCLabel::ScriptBind_CCLabel()
{
	REGISTER_METHOD(CreateLabelAtlasWithFont);
	REGISTER_METHOD(CreateLabelAtlasWithMap);
	REGISTER_METHOD(CreateLabelBMFont);
	REGISTER_METHOD(CreateLabelTTF);

	REGISTER_METHOD(SetString_Atlas);
	REGISTER_METHOD(SetString_BMFont);
	REGISTER_METHOD(SetString_TTF);
	REGISTER_METHOD(GetString_Atlas);
	REGISTER_METHOD(GetString_BMFont);
	REGISTER_METHOD(GetString_TTF);

	REGISTER_METHOD(SetTextDefinition_TTF);
	REGISTER_METHOD(GetTextDefinition_TTF);
	REGISTER_METHOD(EnableShadow_TTF);
	REGISTER_METHOD(DisableShadow_TTF);
	REGISTER_METHOD(EnableStroke_TTF);
	REGISTER_METHOD(DisableStroke_TTF);
	REGISTER_METHOD(SetFontFillColor_TTF);
	REGISTER_METHOD(GetHorizontalAlignment_TTF);
	REGISTER_METHOD(SetHorizontalAlignment_TTF);
	REGISTER_METHOD(GetVerticalAlignment_TTF);
	REGISTER_METHOD(SetVerticalAlignment_TTF);
	REGISTER_METHOD(GetDimensions_TTF);
	REGISTER_METHOD(SetDimensions_TTF);
	REGISTER_METHOD(GetFontSize_TTF);
	REGISTER_METHOD(SetFontSize_TTF);
	REGISTER_METHOD(GetFontName_TTF);
	REGISTER_METHOD(SetFontName_TTF);
}

ScriptBind_CCLabel::~ScriptBind_CCLabel()
{

}

CCLabelAtlas* ScriptBind_CCLabel::CreateLabelAtlasWithFont(mono::string text, mono::string fntFile)
{

	return CCLabelAtlas::create(ToMatrixString(text).c_str(),ToMatrixString(fntFile).c_str());
}

CCLabelAtlas* ScriptBind_CCLabel::CreateLabelAtlasWithMap(mono::string text,mono::string charMapFile, int itemWidth,int itemHeight ,int starCharMap)
{
	return CCLabelAtlas::create(ToMatrixString(text).c_str(),ToMatrixString(charMapFile).c_str(),itemWidth,itemHeight,starCharMap);
}

CCLabelBMFont* ScriptBind_CCLabel::CreateLabelBMFont(mono::string text,mono::string fintFile, float width, CCTextAlignment alignment, CCPoint& pos)
{
	return CCLabelBMFont::create(ToMatrixString(text).c_str(),ToMatrixString(fintFile).c_str(),width,alignment,pos);
}

CCLabelTTF* ScriptBind_CCLabel::CreateLabelTTF(mono::string text, mono::string fintFile, float fontSize, CCSize& dimensions, CCTextAlignment hAlignment, CCVerticalTextAlignment vAlignment)
{
	return CCLabelTTF::create(ToMatrixString(text).c_str(),ToMatrixString(fintFile).c_str(),fontSize,dimensions,hAlignment,vAlignment);
}

void ScriptBind_CCLabel::SetString_Atlas(CCLabelAtlas* labelProtocol,mono::string text)
{
	CCAssert(labelProtocol,"");
	labelProtocol->setString(ToMatrixString(text).c_str());
}

void ScriptBind_CCLabel::SetString_BMFont(CCLabelBMFont* labelProtocol,mono::string text)
{
	CCAssert(labelProtocol,"");
	labelProtocol->setString(ToMatrixString(text).c_str());
}

void ScriptBind_CCLabel::SetString_TTF(CCLabelTTF* labelProtocol,mono::string text)
{
	CCAssert(labelProtocol,"");
	labelProtocol->setString((ToMatrixString(text)).c_str());
}

mono::string ScriptBind_CCLabel::GetString_Atlas(CCLabelAtlas* labelProtocol)
{
	CCAssert(labelProtocol,"");
	return ToMonoString(labelProtocol->getString());
}

mono::string ScriptBind_CCLabel::GetString_BMFont(CCLabelBMFont* labelProtocol)
{
	CCAssert(labelProtocol,"");
	return ToMonoString(labelProtocol->getString());
}

mono::string ScriptBind_CCLabel::GetString_TTF(CCLabelTTF* labelProtocol)
{
	CCAssert(labelProtocol,"");
	return ToMonoString(labelProtocol->getString());
}

//TTF
void ScriptBind_CCLabel::SetTextDefinition_TTF(CCLabelTTF* ttf,ccFontDefinition *theDefinition)
{
	CCAssert(ttf,"");
	ttf->setTextDefinition(theDefinition);
}
ccFontDefinition* ScriptBind_CCLabel::GetTextDefinition_TTF(CCLabelTTF* ttf)
{
	CCAssert(ttf,"");
	return ttf->getTextDefinition();
}

void ScriptBind_CCLabel::EnableShadow_TTF(CCLabelTTF* ttf,CCSize &shadowOffset, float shadowOpacity, float shadowBlur, bool mustUpdateTexture)
{
	CCAssert(ttf,"");
	ttf->enableShadow(shadowOffset,shadowOpacity,shadowBlur,mustUpdateTexture);
}
void ScriptBind_CCLabel::DisableShadow_TTF(CCLabelTTF* ttf,bool mustUpdateTexture)
{
	CCAssert(ttf,"");
	ttf->disableShadow(mustUpdateTexture);
}

void ScriptBind_CCLabel::EnableStroke_TTF(CCLabelTTF* ttf,ccColor3B &strokeColor, float strokeSize, bool mustUpdateTexture)
{
	CCAssert(ttf,"");
	ttf->enableStroke(strokeColor,strokeSize,mustUpdateTexture);
}
void ScriptBind_CCLabel::DisableStroke_TTF(CCLabelTTF* ttf,bool mustUpdateTexture)
{
	CCAssert(ttf,"");
	ttf->disableStroke(mustUpdateTexture);
}

void ScriptBind_CCLabel::SetFontFillColor_TTF(CCLabelTTF* ttf,ccColor3B &tintColor, bool mustUpdateTexture)
{
	CCAssert(ttf,"");
	ttf->setFontFillColor(tintColor,mustUpdateTexture);
}

CCTextAlignment ScriptBind_CCLabel::GetHorizontalAlignment_TTF(CCLabelTTF* ttf)
{
	CCAssert(ttf,"");
	return ttf->getHorizontalAlignment();
}
void ScriptBind_CCLabel::SetHorizontalAlignment_TTF(CCLabelTTF* ttf,CCTextAlignment alignment)
{
	CCAssert(ttf,"");
	ttf->setHorizontalAlignment(alignment);
}

CCVerticalTextAlignment ScriptBind_CCLabel::GetVerticalAlignment_TTF(CCLabelTTF* ttf)
{
	CCAssert(ttf,"");
	return ttf->getVerticalAlignment();
}
void ScriptBind_CCLabel::SetVerticalAlignment_TTF(CCLabelTTF* ttf,CCVerticalTextAlignment verticalAlignment)
{
	CCAssert(ttf,"");
	ttf->setVerticalAlignment(verticalAlignment);
}

void ScriptBind_CCLabel::GetDimensions_TTF(CCLabelTTF* ttf,CCSize &dim)
{
	CCAssert(ttf,"");
	dim = ttf->getDimensions();
}
void ScriptBind_CCLabel::SetDimensions_TTF(CCLabelTTF* ttf,CCSize &dim)
{
	CCAssert(ttf,"");
	return ttf->setDimensions(dim);
}

float ScriptBind_CCLabel::GetFontSize_TTF(CCLabelTTF* ttf)
{
	CCAssert(ttf,"");
	return ttf->getFontSize();
}
void ScriptBind_CCLabel::SetFontSize_TTF(CCLabelTTF* ttf,float fontSize)
{
	CCAssert(ttf,"");
	ttf->setFontSize(fontSize);
}

mono::string ScriptBind_CCLabel::GetFontName_TTF(CCLabelTTF* ttf)
{
	CCAssert(ttf,"");
	return ToMonoString(ttf->getFontName());
}
void ScriptBind_CCLabel::SetFontName_TTF(CCLabelTTF* ttf,mono::string fontName)
{
	CCAssert(ttf,"");
	ttf->setFontName(ToMatrixString(fontName).c_str());
}