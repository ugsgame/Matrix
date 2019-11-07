
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "ScriptBind_UILoadingBar.h"

USING_NS_CC;
using namespace cocos2d::ui;

ScriptBind_UILoadingBar::ScriptBind_UILoadingBar()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(SetDirection);
	REGISTER_METHOD(GetDirection);
	REGISTER_METHOD(LoadTexture);
	REGISTER_METHOD(SetPercent);
	REGISTER_METHOD(GetPercent);
	REGISTER_METHOD(SetCapInsets);
}
ScriptBind_UILoadingBar::~ScriptBind_UILoadingBar()
{
	
}

LoadingBar* ScriptBind_UILoadingBar::Create()
{
	return LoadingBar::create();
}

void ScriptBind_UILoadingBar::SetDirection(LoadingBar* bar, int dir)
{
	bar->setDirection((LoadingBarType)dir);
}

int ScriptBind_UILoadingBar::GetDirection(LoadingBar* bar)
{
	return bar->getDirection();
}

void ScriptBind_UILoadingBar::LoadTexture(LoadingBar* bar,mono::string texture,TextureResType textType)
{
	bar->loadTexture(ToMatrixString(texture).c_str(), textType);
}

void ScriptBind_UILoadingBar::SetPercent(LoadingBar* bar,int percent)
{
	bar->setPercent(percent);
}

int ScriptBind_UILoadingBar::GetPercent(LoadingBar* bar)
{
	return bar->getPercent();
}

//static void SetScale9Enabled(bool enabled);

void ScriptBind_UILoadingBar::SetCapInsets(LoadingBar* bar,float x, float y, float width, float height)
{
	bar->setCapInsets(CCRect(x,y,width,height));
}