
#ifndef __SCRIPTBIND_UILISTVIEW__
#define __SCRIPTBIND_UILISTVIEW__

#include "ScriptBind_CocoStudio.h"

class cocos2d::ui::ListView;
class cocos2d::ui::Widget; 

class ScriptBind_UIListView:public ScriptBind_CocoStudio
{
public:
	ScriptBind_UIListView();
	~ScriptBind_UIListView();

	virtual const char* GetClassName(){ return "NativeUIListView";}

	static cocos2d::ui::ListView* Create();

	static void SetItemModel(cocos2d::ui::ListView* listView,cocos2d::ui::Widget* model);

	static void PushBackDefaultItem(cocos2d::ui::ListView* listView);

	static void InsertDefaultItem(cocos2d::ui::ListView* listView,int index);

	static void PushBackCustomItem(cocos2d::ui::ListView* listView,cocos2d::ui::Widget* item);

	static void InsertCustomItem(cocos2d::ui::ListView* listView,cocos2d::ui::Widget* item, int index);

	static void RemoveItem(cocos2d::ui::ListView* listView,int index);

	static void RemoveAllItems(cocos2d::ui::ListView* listView);

	static cocos2d::ui::Widget* GetItem(cocos2d::ui::ListView* listView,int index);

	//CCArray* GetItems();

	static int GetIndex(cocos2d::ui::ListView* listView,cocos2d::ui::Widget* item);

	static void SetGravity(cocos2d::ui::ListView* listView,int gravity);

	static void SetItemsMargin(cocos2d::ui::ListView* listView,float margin);

	static int GetCurSelectedIndex(cocos2d::ui::ListView* listView);

	static void AddEventListenerListView(cocos2d::ui::ListView* listView,mono::object target);

	static void RequestRefreshView(cocos2d::ui::ListView* listView);
protected:
private:
};


#endif