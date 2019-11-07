#ifndef __SCRIPTBIND_UILAYOUT__
#define __SCRIPTBIND_UILAYOUT__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::Layout;
class cocos2d::CCSize;

class ScriptBind_UILayout:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UILayout();
	~ScriptBind_UILayout();

	virtual const char* GetClassName(){ return "NativeUILayout";}

	static cocos2d::ui::Layout* Create();

	static  void SetBackGroundImage(cocos2d::ui::Layout* layout,mono::string fileName,cocos2d::ui::TextureResType textType);
	static  void SetBackGroundImageCapInsets(cocos2d::ui::Layout* layout,float x,float y ,float w,float h);

	static  void SetBackGroundColorType(cocos2d::ui::Layout* layout,int type);
	static  void SetBackGroundColorS(cocos2d::ui::Layout* layout,cocos2d::_ccColor3B& color);
	static  void SetBackGroundColorSE(cocos2d::ui::Layout* layout,cocos2d::_ccColor3B& startColor, cocos2d::_ccColor3B& endColor);
	static  void SetBackGroundColorOpacity(cocos2d::ui::Layout* layout,int opacity);
	static  void SetBackGroundColorVector(cocos2d::ui::Layout* layout,mono::object vector);

	static  void RemoveBackGroundImage(cocos2d::ui::Layout* layout);
	static  void GetBackGroundImageTextureSize(cocos2d::ui::Layout* layout,cocos2d::CCSize& size);

	static  void SetClippingEnabled(cocos2d::ui::Layout* layout,bool enabled);
	static  bool IsClippingEnabled(cocos2d::ui::Layout* layout);
	static  void SetClippingType(cocos2d::ui::Layout* layout,int type);

	static  void SetLayoutType(cocos2d::ui::Layout* layout,int type);
	static  int GetLayoutType(cocos2d::ui::Layout* layout);
protected:
private:
};


#endif