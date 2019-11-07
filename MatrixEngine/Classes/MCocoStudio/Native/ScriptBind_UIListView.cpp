
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "ScriptBind_UIListView.h"

USING_NS_CC;
using namespace cocos2d::ui;

class ListEventListener:public CCObject
{
public:
	ListEventListener(){}
	~ListEventListener(){}

	static ListEventListener* Create(IMonoObject* obj)
	{
		ListEventListener* event = new ListEventListener();
		event->setMonoObject(obj);
		return event;
	}
	//TODO 回调c#函数中第一个参数 应该传入c#的对像，而不是指针

	void ListListener(CCObject *pSender, ListViewEventType type)
	{
		CCAssert(p_MonoObject,"");
		//TODO
		//pScript->CallMethod("TouchEvent",IMonoObject object,(int)type);	
		p_MonoObject->CallMethod("ListListener",(int)type);	
	}
};

ScriptBind_UIListView::ScriptBind_UIListView()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetItemModel);
	REGISTER_METHOD(PushBackDefaultItem);
	REGISTER_METHOD(InsertDefaultItem);
	REGISTER_METHOD(PushBackCustomItem);
	REGISTER_METHOD(InsertCustomItem);
	REGISTER_METHOD(RemoveItem);
	REGISTER_METHOD(RemoveAllItems);
	REGISTER_METHOD(GetItem);
	REGISTER_METHOD(GetIndex);
	REGISTER_METHOD(SetGravity);
	REGISTER_METHOD(SetItemsMargin);
	REGISTER_METHOD(GetCurSelectedIndex);
	REGISTER_METHOD(AddEventListenerListView);
	REGISTER_METHOD(RequestRefreshView);
}
ScriptBind_UIListView::~ScriptBind_UIListView()
{

}


ListView* ScriptBind_UIListView::Create()
{
	return ListView::create();
}

void ScriptBind_UIListView::SetItemModel(ListView* listView,Widget* model)
{
	CCAssert(listView!=0,"");
	CCAssert(model,"");
	listView->setItemModel(model);
}

void ScriptBind_UIListView::PushBackDefaultItem(ListView* listView)
{
	CCAssert(listView!=0,"");
	listView->pushBackDefaultItem();
}

void ScriptBind_UIListView::InsertDefaultItem(ListView* listView,int index)
{
	CCAssert(listView!=0,"");
	listView->insertDefaultItem(index);
}

void ScriptBind_UIListView::PushBackCustomItem(ListView* listView,Widget* item)
{
	CCAssert(listView!=0,"");
	listView->pushBackCustomItem(item);
}

void ScriptBind_UIListView::InsertCustomItem(ListView* listView,Widget* item, int index)
{
	CCAssert(listView!=0,"");
	CCAssert(item,"");
	listView->insertCustomItem(item,index);
}

void ScriptBind_UIListView::RemoveItem(ListView* listView,int index)
{
	CCAssert(listView!=0,"");
	listView->removeItem(index);
}

void ScriptBind_UIListView::RemoveAllItems(ListView* listView)
{
	CCAssert(listView!=0,"");
	listView->removeAllItems();
}

Widget* ScriptBind_UIListView::GetItem(ListView* listView,int index)
{
	CCAssert(listView!=0,"");
	return listView->getItem(index);
}


int ScriptBind_UIListView::GetIndex(ListView* listView,Widget* item)
{
	CCAssert(listView!=0,"");
	return listView->getIndex(item);
}

void ScriptBind_UIListView::SetGravity(ListView* listView,int gravity)
{
	CCAssert(listView!=0,"");
	listView->setGravity((ListViewGravity)gravity);
}

void ScriptBind_UIListView::SetItemsMargin(ListView* listView,float margin)
{
	CCAssert(listView!=0,"");
	listView->setItemsMargin(margin);
}

int ScriptBind_UIListView::GetCurSelectedIndex(ListView* listView)
{
	CCAssert(listView!=0,"");
	return listView->getCurSelectedIndex();
}

void ScriptBind_UIListView::AddEventListenerListView(ListView* listView,mono::object target)
{
	CCAssert(listView!=0,"");
	listView->addEventListenerListView(ListEventListener::Create(*target),listvieweventselector(ListEventListener::ListListener));
}

void ScriptBind_UIListView::RequestRefreshView(ListView* listView)
{
	CCAssert(listView!=0,"");
	listView->requestRefreshView();
}