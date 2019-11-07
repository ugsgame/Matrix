
#ifndef __SCRIPTBIND_CCTEXTURE__
#define __SCRIPTBIND_CCTEXTURE__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCTexture2D;
class cocos2d::CCImage;
class cocos2d::CCSize;
//TODO:用到更多的接口再加
class ScriptBind_CCTexture2D:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCTexture2D();
	~ScriptBind_CCTexture2D();

	virtual const char* GetClassName(){ return "NativeTexture2D";}

protected:

	static cocos2d::CCTexture2D* Create();
	//static bool InitWithData(void* data );
	static bool InitWithImage(cocos2d::CCImage* uiImage);
	static bool InitWithString(mono::string text,mono::string fontName,float fontSize,cocos2d::CCSize& dimensions,cocos2d::CCTextAlignment hAlignment, cocos2d::CCVerticalTextAlignment vAlignment);
	static bool InitWithPVRFile(mono::string file);
	static bool InitWithETCFile(mono::string file);
private:
};
#endif