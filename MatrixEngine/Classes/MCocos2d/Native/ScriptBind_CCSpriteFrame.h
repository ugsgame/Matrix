
#ifndef __SCRIPTBIND_CCSPRITEFRAME__
#define __SCRIPTBIND_CCSPRITEFRAME__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCSpriteFrame;
class cocos2d::CCTexture2D;
class cocos2d::CCRect;
class cocos2d::CCPoint;
class cocos2d::CCSize;

class ScriptBind_CCSpriteFrame:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCSpriteFrame();
	~ScriptBind_CCSpriteFrame();

	virtual const char* GetClassName(){ return "NativeSpriteFrame"; }
protected:
	static cocos2d::CCSpriteFrame* CreateWithFile(mono::string fileName,cocos2d::CCRect& rect);
	static cocos2d::CCSpriteFrame* CreateWithTexture(cocos2d::CCTexture2D* pTexture,cocos2d::CCRect& rect);

	static void GetRectInPixels(cocos2d::CCSpriteFrame* frame,cocos2d::CCRect& rect);
	static void SetRectInPixels(cocos2d::CCSpriteFrame* frame,cocos2d::CCRect& rect);

	static bool IsRotated(cocos2d::CCSpriteFrame* frame);
	static void SetRotated(cocos2d::CCSpriteFrame* frame,bool bRotated);

	static void GetRect(cocos2d::CCSpriteFrame* frame,cocos2d::CCRect& rect);
	static void SetRect(cocos2d::CCSpriteFrame* frame,cocos2d::CCRect& rect);

	static void GetOffsetInPixels(cocos2d::CCSpriteFrame* frame,cocos2d::CCPoint& offsetInPixels);
	static void SetOffsetInPixels(cocos2d::CCSpriteFrame* frame,cocos2d::CCPoint& offsetInPixels);

	static void GetOriginalSizeInPixels(cocos2d::CCSpriteFrame* frame,cocos2d::CCSize& sizeInPixels);
	static void SetOriginalSizeInPixels(cocos2d::CCSpriteFrame* frame,cocos2d::CCSize& sizeInPixels);

	static void GetOriginalSize(cocos2d::CCSpriteFrame* frame,cocos2d::CCSize& sizeInPixels);
	static void SetOriginalSize(cocos2d::CCSpriteFrame* frame,cocos2d::CCSize& sizeInPixels);

	static cocos2d::CCTexture2D* GetTexture(cocos2d::CCSpriteFrame* frame);
	static void SetTexture(cocos2d::CCSpriteFrame* frame,cocos2d::CCTexture2D* pTexture);

	static void GetOffset(cocos2d::CCSpriteFrame* frame,cocos2d::CCPoint& offsets);
	static void SetOffset(cocos2d::CCSpriteFrame* frame,cocos2d::CCPoint& offsets);
private:
};

#endif