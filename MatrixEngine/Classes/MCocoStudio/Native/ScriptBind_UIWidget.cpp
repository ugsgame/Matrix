
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"
#include "MatrixScriptHelper.h"
#include "ScriptBind_UIWidget.h"

USING_NS_CC;
using namespace cocos2d::ui;


class TouchEvent:public CCObject
{
public:
	TouchEvent(){}
	~TouchEvent(){}
	 static TouchEvent* Create(IMonoObject* obj)
	{
		TouchEvent* event = new TouchEvent();
		event->setMonoObject(obj);
		//c#层没有计数，native层也不计了
		event->ObjectCount--;
		return event;
	}
	//TODO 回调c#函数中第一个参数 应该传入c#的对像，而不是指针

	void TouchListener(CCObject *pSender, TouchEventType type)
	{
		CCAssert(p_MonoObject,"");
		//TODO
		//pScript->CallMethod("TouchEvent",IMonoObject object,(int)type);
		p_MonoObject->CallMethod("TouchListener", (int)pSender, (int)type);
	}
};

ScriptBind_UIWidget::ScriptBind_UIWidget()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetEnabled);
	REGISTER_METHOD(IsEnabled);
	REGISTER_METHOD(SetBright);
	REGISTER_METHOD(IsBright);
	REGISTER_METHOD(SetTouchEnabled);
	REGISTER_METHOD(IsTouchEnabled);
	REGISTER_METHOD(SetBrightStyle);
	REGISTER_METHOD(SetFocused);
	REGISTER_METHOD(IsFocused);
	REGISTER_METHOD(GetLeftInParent);
	REGISTER_METHOD(GetBottomInParent);
	REGISTER_METHOD(GetRightInParent);
	REGISTER_METHOD(GetTopInParent);
	REGISTER_METHOD(GetChildByName);
	REGISTER_METHOD(AddTouchEventListener);
	REGISTER_METHOD(SetPositionType);
	REGISTER_METHOD(GetPositionType);
	REGISTER_METHOD(SetFlipX);
	REGISTER_METHOD(IsFlipX);
	REGISTER_METHOD(SetFlipY);
	REGISTER_METHOD(IsFlipY);
	REGISTER_METHOD(SetColor);
	REGISTER_METHOD(GetColor);
	REGISTER_METHOD(SetOpacity);
	REGISTER_METHOD(GetOpacity);
	REGISTER_METHOD(GetTouchStartPos);
	REGISTER_METHOD(GetTouchMovePos);
	REGISTER_METHOD(GetTouchEndPos);
	REGISTER_METHOD(SetName);
	REGISTER_METHOD(GetName);
	REGISTER_METHOD(GetWidgetType);
	REGISTER_METHOD(SetSize);
	REGISTER_METHOD(GetSize);
	REGISTER_METHOD(SetSizeType);
	REGISTER_METHOD(SetLayoutParameter);
	REGISTER_METHOD(GetLayoutParameter);
	REGISTER_METHOD(IgnoreContentAdaptWithSize);
	REGISTER_METHOD(IsIgnoreContentAdaptWithSize);
	REGISTER_METHOD(GetDescription);
	REGISTER_METHOD(AddNode);
	REGISTER_METHOD(AddNodeWithzOrder);
	REGISTER_METHOD(AddNodeWithzOrderAndTag);
	REGISTER_METHOD(GetNodeByTag);
	REGISTER_METHOD(GetNodeByIndex);
	REGISTER_METHOD(RemoveNode);
	REGISTER_METHOD(RemoveNodeByTag);
	REGISTER_METHOD(RemoveAllNodes);
	REGISTER_METHOD(Clone);

	REGISTER_METHOD(SetEffectNode);
}

ScriptBind_UIWidget::~ScriptBind_UIWidget()
{
	
}

Widget* ScriptBind_UIWidget::Create()
{
	return Widget::create();
}

void ScriptBind_UIWidget::SetEnabled(Widget* widget ,bool enabled)
{
	CCAssert(widget != 0,"");
	widget->setEnabled(enabled);
}
bool ScriptBind_UIWidget::IsEnabled(Widget* widget)
{
	CCAssert(widget != 0,"");
	return widget->isEnabled();
}

void ScriptBind_UIWidget::SetBright(Widget* widget ,bool bright)
{
	CCAssert(widget != 0,"");
	widget->setBright(bright);
}
bool ScriptBind_UIWidget::IsBright(Widget* widget)
{
	CCAssert(widget != 0,"");
	return widget->isBright();
}

void ScriptBind_UIWidget::SetTouchEnabled(Widget* widget,bool enabled)
{
	CCAssert(widget != 0,"");
	widget->setTouchEnabled(enabled);
}
bool ScriptBind_UIWidget::IsTouchEnabled(Widget* widget)
{
	CCAssert(widget != 0,"");
	return widget->isTouchEnabled();
}


void ScriptBind_UIWidget::SetBrightStyle(Widget* widget ,int style)
{
	CCAssert(widget != 0,"");
	widget->setBrightStyle((BrightStyle)style);
}

void ScriptBind_UIWidget::SetFocused(Widget* widget ,bool fucosed)
{
	CCAssert(widget != 0,"");
	widget->setFocused(fucosed);
}
bool ScriptBind_UIWidget::IsFocused(Widget* widget)
{
	CCAssert(widget != 0,"");
	return widget->isFocused();
}

float ScriptBind_UIWidget::GetLeftInParent(Widget* widget)
{
	CCAssert(widget != 0,"");
	return widget->getLeftInParent();
}
float ScriptBind_UIWidget::GetBottomInParent(Widget* widget)
{
	CCAssert(widget != 0,"");
	return widget->getBottomInParent();
}
float ScriptBind_UIWidget::GetRightInParent(Widget* widget)
{
	CCAssert(widget != 0,"");
	return widget->getRightInParent();
}
float ScriptBind_UIWidget::GetTopInParent(Widget* widget)
{
	CCAssert(widget != 0,"");
	return widget->getTopInParent();
}

Widget* ScriptBind_UIWidget::GetChildByName(Widget* widget ,mono::string name)
{
	CCAssert(widget != 0,"");
	return widget->getChildByName(ToMatrixString(name).c_str());
}

void ScriptBind_UIWidget::AddTouchEventListener(Widget* widget ,mono::object target)
{
	CCAssert(widget != 0,"");
	widget->addTouchEventListener(TouchEvent::Create(*target),toucheventselector(TouchEvent::TouchListener)); 
}

void ScriptBind_UIWidget::SetPositionType(Widget* widget ,int type)
{
	CCAssert(widget != 0,"");
	widget->setPositionType((PositionType)type);
}
int  ScriptBind_UIWidget::GetPositionType(Widget* widget)
{
	CCAssert(widget != 0,"");
	return (int)widget->getPositionType();
}

void ScriptBind_UIWidget::SetFlipX(Widget* widget ,bool flipX)
{
	CCAssert(widget != 0,"");
	widget->setFlipX(flipX);
}
bool ScriptBind_UIWidget::IsFlipX(Widget* widget)
{
	CCAssert(widget != 0,"");
	return widget->isFlipX();
}
void ScriptBind_UIWidget::SetFlipY(Widget* widget ,bool flipY)
{
	CCAssert(widget != 0,"");
	widget->setFlipY(flipY);
}
bool ScriptBind_UIWidget::IsFlipY(Widget* widget)
{
	CCAssert(widget != 0,"");
	return widget->isFlipY();
}

void ScriptBind_UIWidget::SetColor(Widget* widget, int r,int g ,int b)
{
	CCAssert(widget != 0,"");
	widget->setColor(ccc3(r,g,b));
}
void ScriptBind_UIWidget::GetColor(Widget* widget,struct _ccColor3B& color)
{
	CCAssert(widget != 0,"");
	color = widget->getColor();
}

void ergodicAllNode(Widget *rootNode,int opacity)  
{  
	CCObject *temp;  
	//获得rootnode根下的节点  
	CCArray *nodeArray = rootNode->getChildren();  
	CCARRAY_FOREACH(nodeArray, temp)  
	{  
		//判断rootnode的节点下还是否存在节点 遍历调用  
		if(((Widget*)temp)->getChildrenCount())  
			ergodicAllNode((Widget*)temp, opacity);  
		//这里 do something  
		((Widget*)temp)->setOpacity(opacity);
	}  
	return ;  
}  

void ScriptBind_UIWidget::SetOpacity(Widget* widget, int a)
{
	CCAssert(widget != 0,"");
	widget->setOpacity(a);
	ergodicAllNode(widget,a);  
}
int ScriptBind_UIWidget::GetOpacity(Widget* widget)
{
	CCAssert(widget != 0,"");
	return widget->getOpacity();
}

void ScriptBind_UIWidget::GetTouchStartPos(Widget* widget,CCPoint& point)
{
	CCAssert(widget != 0,"");
	point = widget->getTouchStartPos();
}
void ScriptBind_UIWidget::GetTouchMovePos(Widget* widget,CCPoint& point)
{
	CCAssert(widget != 0,"");
	CCPoint pos = widget->getTouchMovePos();	
}
void ScriptBind_UIWidget::GetTouchEndPos(Widget* widget,CCPoint& point)
{
	CCAssert(widget != 0,"");
	point = widget->getTouchEndPos();
}

void ScriptBind_UIWidget::SetName(Widget* widget ,mono::string name)
{
	CCAssert(widget != 0,"");
	widget->setName(ToMatrixString(name).c_str());
}
mono::string ScriptBind_UIWidget::GetName(Widget* widget)
{
	CCAssert(widget != 0,"");
	return ToMonoString(widget->getName());
}

int ScriptBind_UIWidget::GetWidgetType(Widget* widget)
{
	CCAssert(widget != 0,"");
	return (int)widget->getWidgetType();
}

void ScriptBind_UIWidget::SetSize(Widget* widget ,float w, float h)
{
	CCAssert(widget != 0,"");
	widget->setSize(CCSize(ccp(w,h)));
}
void ScriptBind_UIWidget::GetSize(Widget* widget,cocos2d::CCSize& size)
{
	CCAssert(widget != 0,"");
	size = widget->getSize();	
}

void ScriptBind_UIWidget::SetSizeType(Widget* widget ,int type)
{
	CCAssert(widget != 0,"");
	widget->setSizeType((SizeType)type);
}
int  ScriptBind_UIWidget::GetSizeType(Widget* widget)
{
	CCAssert(widget != 0,"");
	return (int)widget->getSizeType();
}

void ScriptBind_UIWidget::SetLayoutParameter(Widget* widget ,LayoutParameter* parameter)
{
	CCAssert(widget != 0,"");
	widget->setLayoutParameter(parameter);
}
LayoutParameter* ScriptBind_UIWidget::GetLayoutParameter(Widget* widget ,int type)
{
	CCAssert(widget != 0,"");
	return widget->getLayoutParameter((LayoutParameterType)type);
}

void ScriptBind_UIWidget::IgnoreContentAdaptWithSize(Widget* widget ,bool ignore)
{
	CCAssert(widget != 0,"");
	widget->ignoreContentAdaptWithSize(ignore);
}
bool ScriptBind_UIWidget::IsIgnoreContentAdaptWithSize(Widget* widget)
{
	CCAssert(widget != 0,"");
	return widget->isIgnoreContentAdaptWithSize();
}

mono::string ScriptBind_UIWidget::GetDescription(Widget* widget)
{
	return ToMonoString(widget->getDescription().c_str());
}

void ScriptBind_UIWidget::AddNode(Widget* widget,CCNode* node)
{
	widget->addNode(node);
}
void ScriptBind_UIWidget::AddNodeWithzOrder(Widget* widget,CCNode * node, int zOrder)
{
	widget->addNode(node,zOrder);
}
void ScriptBind_UIWidget::AddNodeWithzOrderAndTag(Widget* widget,CCNode* node, int zOrder, int tag)
{
	widget->addNode(node,zOrder,tag);
}
CCNode* ScriptBind_UIWidget::GetNodeByTag(Widget* widget,int tag)
{
	return widget->getNodeByTag(tag);
}
CCNode* ScriptBind_UIWidget::GetNodeByIndex(Widget* widget,int index)
{
	CCAssert(widget,"");
	CCArray* nArray = widget->getNodes();
	CCAssert( index < (int)nArray->count() ,"child index is illegal!!!");
	return (CCNode*)nArray->objectAtIndex(index);	
}
//CCArray* getNodes();
void ScriptBind_UIWidget::RemoveNode(Widget* widget,CCNode* node)
{
	widget->removeNode(node);
}
void ScriptBind_UIWidget::RemoveNodeByTag(Widget* widget,int tag)
{
	widget->removeNodeByTag(tag);
}
void ScriptBind_UIWidget::RemoveAllNodes(Widget* widget)
{
	widget->removeAllNodes();
}

Widget* ScriptBind_UIWidget::Clone(Widget* widget)
{
	return widget->clone();
}

void ScriptBind_UIWidget::SetEffectNode(Widget* widget,CCNode* node)
{
	widget->setShaderProgram(node->getShaderProgram());
}