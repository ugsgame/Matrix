
#ifndef __SCRIPTBIND_WIDGET__
#define __SCRIPTBIND_WIDGET__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::Widget; 
class cocos2d::CCNode;

class cocos2d::CCPoint;
class cocos2d::CCSize;

class ScriptBind_UIWidget:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UIWidget();
	~ScriptBind_UIWidget();

	virtual const char* GetClassName(){ return "NativeUIWidget"; }; 

	static cocos2d::ui::Widget* Create();

	static void SetEnabled(cocos2d::ui::Widget* widget ,bool enabled);
	static bool IsEnabled(cocos2d::ui::Widget* widget);

	static void SetBright(cocos2d::ui::Widget* widget ,bool bright);
	static bool IsBright(cocos2d::ui::Widget* widget);

	static void SetTouchEnabled(cocos2d::ui::Widget* widget,bool enabled);
	static bool IsTouchEnabled(cocos2d::ui::Widget* widget);

	static void SetBrightStyle(cocos2d::ui::Widget* widget ,int style);

	static void SetFocused(cocos2d::ui::Widget* widget ,bool fucosed);
	static bool IsFocused(cocos2d::ui::Widget* widget);

	static float GetLeftInParent(cocos2d::ui::Widget* widget);
	static float GetBottomInParent(cocos2d::ui::Widget* widget);
	static float GetRightInParent(cocos2d::ui::Widget* widget);
	static float GetTopInParent(cocos2d::ui::Widget* widget);

	static cocos2d::ui::Widget* GetChildByName(cocos2d::ui::Widget* widget ,mono::string name);

	static void AddTouchEventListener(cocos2d::ui::Widget* widget ,mono::object target);

	static void SetPositionType(cocos2d::ui::Widget* widget ,int type);
	static int  GetPositionType(cocos2d::ui::Widget* widget);

	static  void SetFlipX(cocos2d::ui::Widget* widget ,bool flipX);
	static  bool IsFlipX(cocos2d::ui::Widget* widget);
	static  void SetFlipY(cocos2d::ui::Widget* widget ,bool flipY);
	static  bool IsFlipY(cocos2d::ui::Widget* widget);

	static  void SetColor(cocos2d::ui::Widget* widget, int r,int g ,int b);
	static  void GetColor(cocos2d::ui::Widget* widget,struct cocos2d::_ccColor3B& color);
	static  void SetOpacity(cocos2d::ui::Widget* widget, int a);
	static  int GetOpacity(cocos2d::ui::Widget* widget); 

	static  void GetTouchStartPos(cocos2d::ui::Widget* widget,cocos2d::CCPoint& point);
	static  void GetTouchMovePos(cocos2d::ui::Widget* widget,cocos2d::CCPoint& point);
	static  void GetTouchEndPos(cocos2d::ui::Widget* widget,cocos2d::CCPoint& point);

	static void SetName(cocos2d::ui::Widget* widget ,mono::string name);
	static mono::string GetName(cocos2d::ui::Widget* widget);

	static int GetWidgetType(cocos2d::ui::Widget* widget);

	static void SetSize(cocos2d::ui::Widget* widget ,float w, float h);
	static void GetSize(cocos2d::ui::Widget* widget,cocos2d::CCSize& size);

	static void SetSizeType(cocos2d::ui::Widget* widget ,int type);
	static int  GetSizeType(cocos2d::ui::Widget* widget);

	static void SetLayoutParameter(cocos2d::ui::Widget* widget ,cocos2d::ui::LayoutParameter* parameter);
	static cocos2d::ui::LayoutParameter* GetLayoutParameter(cocos2d::ui::Widget* widget ,int type);

	static void IgnoreContentAdaptWithSize(cocos2d::ui::Widget* widget ,bool ignore);
	static bool IsIgnoreContentAdaptWithSize(cocos2d::ui::Widget* widget);

	static mono::string GetDescription(cocos2d::ui::Widget* widget);

	static void AddNode(cocos2d::ui::Widget* widget,cocos2d::CCNode* node);
	static void AddNodeWithzOrder(cocos2d::ui::Widget* widget,cocos2d::CCNode * node, int zOrder);
	static void AddNodeWithzOrderAndTag(cocos2d::ui::Widget* widget,cocos2d::CCNode* node, int zOrder, int tag);
	static cocos2d::CCNode * GetNodeByTag(cocos2d::ui::Widget* widget,int tag);
	static cocos2d::CCNode * GetNodeByIndex(cocos2d::ui::Widget* widget,int index);
	//static CCArray* getNodes();
	static void RemoveNode(cocos2d::ui::Widget* widget,cocos2d::CCNode* node);
	static void RemoveNodeByTag(cocos2d::ui::Widget* widget,int tag);
	static void RemoveAllNodes(cocos2d::ui::Widget* widget);

	static cocos2d::ui::Widget* Clone(cocos2d::ui::Widget* widget);

	//TEST
	static void SetEffectNode(cocos2d::ui::Widget* widget,cocos2d::CCNode* node);
protected:
private:
};

#endif