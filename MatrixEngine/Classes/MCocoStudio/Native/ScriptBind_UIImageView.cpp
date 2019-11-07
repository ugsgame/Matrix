
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "ScriptBind_UIImageView.h"

USING_NS_CC;
using namespace cocos2d::ui;


ScriptBind_UIImageView::ScriptBind_UIImageView()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(LoadTexture);
	REGISTER_METHOD(SetTextureRect);

	REGISTER_METHOD(SetScale9Enabled);
}
ScriptBind_UIImageView::~ScriptBind_UIImageView()
{
	
}


ImageView* ScriptBind_UIImageView::Create()
{
	return ImageView::create();
}

void ScriptBind_UIImageView::LoadTexture(ImageView* imagewiew,mono::string fileName,TextureResType textType)
{
	imagewiew->loadTexture(ToMatrixString(fileName).c_str(),textType);
}

void ScriptBind_UIImageView::SetTextureRect(ImageView* imagewiew,float x,float y,float w,float h)
{
	imagewiew->setTextureRect(CCRect(x,y,w,h));
}

void ScriptBind_UIImageView::SetScale9Enabled(ImageView* imagewiew,bool able)
{
	imagewiew->setScale9Enabled(able);
}