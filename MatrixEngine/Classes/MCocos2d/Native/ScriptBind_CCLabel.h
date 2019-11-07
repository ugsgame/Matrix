
#ifndef  __SCRIPTBIND_CCLABEL__
#define  __SCRIPTBIND_CCLABEL__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCLabelAtlas;
class cocos2d::CCLabelBMFont;
class cocos2d::CCLabelTTF;
class cocos2d::CCSize;
class cocos2d::CCPoint;

class ScriptBind_CCLabel:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCLabel();
	~ScriptBind_CCLabel();

	virtual const char* GetClassName(){ return "NativeLabel"; }

	static  cocos2d::CCLabelAtlas*  CreateLabelAtlasWithFont(mono::string text, mono::string fntFile);
	static  cocos2d::CCLabelAtlas*  CreateLabelAtlasWithMap(mono::string text,mono::string charMapFile,
															int with,int height ,int starCharMap);

	static  cocos2d::CCLabelBMFont* CreateLabelBMFont(mono::string text,mono::string fintFile,
													   float width, cocos2d::CCTextAlignment alignment,
													   cocos2d::CCPoint& pos);
	static  cocos2d::CCLabelTTF* CreateLabelTTF(mono::string text, mono::string fintFile, float fontSize,
												 cocos2d::CCSize& dimensions, cocos2d::CCTextAlignment hAlignment, 
											     cocos2d::CCVerticalTextAlignment vAlignment);

	static void SetString_Atlas(cocos2d::CCLabelAtlas* labelProtocol,mono::string text);
	static void SetString_BMFont(cocos2d::CCLabelBMFont* labelProtocol,mono::string text);
	static void SetString_TTF(cocos2d::CCLabelTTF* labelProtocol,mono::string text);

	static mono::string GetString_Atlas(cocos2d::CCLabelAtlas* labelProtocol);
	static mono::string GetString_BMFont(cocos2d::CCLabelBMFont* labelProtocol);
	static mono::string GetString_TTF(cocos2d::CCLabelTTF* labelProtocol);

	//TTF
	static void SetTextDefinition_TTF(cocos2d::CCLabelTTF* ttf,cocos2d::ccFontDefinition *theDefinition);
	static cocos2d::ccFontDefinition* GetTextDefinition_TTF(cocos2d::CCLabelTTF* ttf);

	static void EnableShadow_TTF(cocos2d::CCLabelTTF* ttf,cocos2d::CCSize &shadowOffset, float shadowOpacity, float shadowBlur, bool mustUpdateTexture);
	static void DisableShadow_TTF(cocos2d::CCLabelTTF* ttf,bool mustUpdateTexture);

	static void EnableStroke_TTF(cocos2d::CCLabelTTF* ttf,cocos2d::ccColor3B &strokeColor, float strokeSize, bool mustUpdateTexture);
	static void DisableStroke_TTF(cocos2d::CCLabelTTF* ttf,bool mustUpdateTexture);

	static void SetFontFillColor_TTF(cocos2d::CCLabelTTF* ttf,cocos2d::ccColor3B &tintColor, bool mustUpdateTexture);

	static cocos2d::CCTextAlignment GetHorizontalAlignment_TTF(cocos2d::CCLabelTTF* ttf);
	static void SetHorizontalAlignment_TTF(cocos2d::CCLabelTTF* ttf,cocos2d::CCTextAlignment alignment);

	static cocos2d::CCVerticalTextAlignment GetVerticalAlignment_TTF(cocos2d::CCLabelTTF* ttf);
	static void SetVerticalAlignment_TTF(cocos2d::CCLabelTTF* ttf,cocos2d::CCVerticalTextAlignment verticalAlignment);

	static void GetDimensions_TTF(cocos2d::CCLabelTTF* ttf,cocos2d::CCSize &dim);
	static void SetDimensions_TTF(cocos2d::CCLabelTTF* ttf,cocos2d::CCSize &dim);

	static float GetFontSize_TTF(cocos2d::CCLabelTTF* ttf);
	static void SetFontSize_TTF(cocos2d::CCLabelTTF* ttf,float fontSize);

	static mono::string GetFontName_TTF(cocos2d::CCLabelTTF* ttf);
	static void SetFontName_TTF(cocos2d::CCLabelTTF* ttf,mono::string fontName);
protected:
private:
};

#endif