
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"

#include "ScriptBind_UISlider.h"

USING_NS_CC;
using namespace cocos2d::ui;

class SliderEventListener:public CCObject
{
public:
	SliderEventListener(){}
	~SliderEventListener(){}

	static SliderEventListener* Create(IMonoObject* obj)
	{
		SliderEventListener* event = new SliderEventListener();
		event->setMonoObject(obj);
		//c#层没有计数，native层也不计了
		event->ObjectCount--;
		return event;
	}
	//TODO 回调c#函数中第一个参数 应该传入c#的对像，而不是指针
	void SliderListener(CCObject *pSender, ScrollviewEventType type)
	{
		CCAssert(p_MonoObject,"");
		//TODO
		//pScript->CallMethod("TouchEvent",IMonoObject object,(int)type);	
		p_MonoObject->CallMethod("SliderListener",(int)type);	
	}
protected:
	//TODO
	IMonoObject* pScript; 
};

ScriptBind_UISlider::ScriptBind_UISlider()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(LoadBarTexture);
	REGISTER_METHOD(SetCapInsets);
	REGISTER_METHOD(SetCapInsetsBarRenderer);
	REGISTER_METHOD(SetCapInsetProgressBarRebderer);
	REGISTER_METHOD(LoadSlidBallTextures);
	REGISTER_METHOD(LoadSlidBallTextureNormal);
	REGISTER_METHOD(LoadSlidBallTexturePressed);
	REGISTER_METHOD(LoadSlidBallTextureDisabled);
	REGISTER_METHOD(LoadProgressBarTexture);
	REGISTER_METHOD(SetPercent);
	REGISTER_METHOD(GetPercent);
	REGISTER_METHOD(AddEventListenerSlider);
	REGISTER_METHOD(SetScale9Enabled);
}
ScriptBind_UISlider::~ScriptBind_UISlider()
{

}

Slider* ScriptBind_UISlider::Create()
{
	return Slider::create();
}

void ScriptBind_UISlider::LoadBarTexture(Slider* slider , mono::string fileName,TextureResType textType)
{
	slider->loadBarTexture(ToMatrixString(fileName).c_str(),textType);	
}

void ScriptBind_UISlider::SetCapInsets(Slider* slider,float x,float y ,float w,float h)
{
	slider->setCapInsets(CCRect(x,y,w,h));
}

void ScriptBind_UISlider::SetCapInsetsBarRenderer(Slider* slider,float x,float y ,float w,float h)
{
	slider->setCapInsetsBarRenderer(CCRect(x,y,w,h));
}

void ScriptBind_UISlider::SetCapInsetProgressBarRebderer(Slider* slider,float x,float y ,float w,float h)
{
	slider->setCapInsetProgressBarRebderer(CCRect(x,y,w,h));
}

void ScriptBind_UISlider::LoadSlidBallTextures(Slider* slider,mono::string normal,mono::string pressed,mono::string disabled,TextureResType textType)
{
	slider->loadSlidBallTextures(ToMatrixString(normal).c_str(),ToMatrixString(pressed).c_str(),ToMatrixString(disabled).c_str(),textType);
}

void ScriptBind_UISlider::LoadSlidBallTextureNormal(Slider* slider,mono::string normal,TextureResType textType)
{
	slider->loadSlidBallTextureNormal(ToMatrixString(normal).c_str(),textType);
}

void ScriptBind_UISlider::LoadSlidBallTexturePressed(Slider* slider,mono::string pressed,TextureResType textType)
{
	slider->loadSlidBallTexturePressed(ToMatrixString(pressed).c_str(),textType);
}

void ScriptBind_UISlider::LoadSlidBallTextureDisabled(Slider* slider,mono::string disabled,TextureResType textType)
{
	slider->loadSlidBallTextureDisabled(ToMatrixString(disabled).c_str(),textType);
}

void ScriptBind_UISlider::LoadProgressBarTexture(Slider* slider,mono::string fileName,TextureResType textType)
{
	slider->loadProgressBarTexture(ToMatrixString(fileName).c_str(),textType);
}

void ScriptBind_UISlider::SetPercent(Slider* slider,int percent)
{
	slider->setPercent(percent);
}

int ScriptBind_UISlider::GetPercent(Slider* slider)
{
	return slider->getPercent();
}

void ScriptBind_UISlider::AddEventListenerSlider(Slider* slider,mono::object target)
{
	slider->addEventListenerSlider(SliderEventListener::Create(*target),sliderpercentchangedselector(SliderEventListener::SliderListener));
}

void ScriptBind_UISlider::SetScale9Enabled(Slider* slider,bool able)
{
	slider->setScale9Enabled(able);
}