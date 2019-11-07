
#include "cocos2d.h"
#include "ScriptBind_CCAnimation.h"

USING_NS_CC;

ScriptBind_CCAnimation::ScriptBind_CCAnimation()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(CreateWithSpriteFrames);
	REGISTER_METHOD(CreateWithAnimationFrames);
	REGISTER_METHOD(AddSpriteFrame);
	REGISTER_METHOD(AddSpriteFrameWithFileName);
	REGISTER_METHOD(AddSpriteFrameWithTexture);
	REGISTER_METHOD(GetTotalDelayUnits);
	REGISTER_METHOD(SetDelayPerUnit);
	REGISTER_METHOD(GetDelayPerUnit);
	REGISTER_METHOD(GetDuration);
	REGISTER_METHOD(SetFrames);
	//REGISTER_METHOD(GetFrames);
	REGISTER_METHOD(SetRestoreOriginalFrame);
	REGISTER_METHOD(GetRestoreOriginalFrame);
	REGISTER_METHOD(SetLoops);
	REGISTER_METHOD(GetLoops);
}
ScriptBind_CCAnimation::~ScriptBind_CCAnimation()
{

}

CCAnimation* ScriptBind_CCAnimation::Create()
{
	return CCAnimation::create();
}
CCAnimation* ScriptBind_CCAnimation::CreateWithSpriteFrames(mono::object spriteFrames)
{
	IMonoArray* mArray = *spriteFrames;
    CCAnimation* pAnimation = CCAnimation::create();

	for (int i=0;i<mArray->GetSize();i++)
	{
		IMonoObject* mObj   = *mArray->GetItem(i);
		IMonoObject* val	= *(mObj->CallMethod("GetPtr"));
		int* ptr = (int*)(val->GetAnyValue().GetValue());
		CCSpriteFrame* spriteFrame =(CCSpriteFrame*)(*ptr);
		pAnimation->addSpriteFrame(spriteFrame);
		mObj->Release();
	}
	mArray->Release();
	return pAnimation;
}
CCAnimation* ScriptBind_CCAnimation::CreateWithAnimationFrames(mono::object animationFrameNames)
{
	IMonoArray* mArray = *animationFrameNames;
	CCArray* cArray = new CCArray();
	for (int i=0;i<mArray->GetSize();i++)
	{
		IMonoObject* mObj   = *mArray->GetItem(i);
		IMonoObject* val	= *(mObj->CallMethod("GetPtr"));
		int* ptr = (int*)(val->GetAnyValue().GetValue());
		CCAnimationFrame* animationFrame =(CCAnimationFrame*)(*ptr);
		cArray->addObject(animationFrame);
		mObj->Release();
	}
	mArray->Release();
	return CCAnimation::create(cArray,10);
}

void ScriptBind_CCAnimation::AddSpriteFrame(CCAnimation* pAnimation,CCSpriteFrame *pFrame)
{
	pAnimation->addSpriteFrame(pFrame);
}
void ScriptBind_CCAnimation::AddSpriteFrameWithFileName(CCAnimation* pAnimation, mono::string pzFileName)
{
	pAnimation->addSpriteFrameWithFileName(ToMatrixString(pzFileName).c_str());
}
void ScriptBind_CCAnimation::AddSpriteFrameWithTexture(CCAnimation* pAnimation,  CCTexture2D* pTexture,CCRect& rect)
{
	pAnimation->addSpriteFrameWithTexture(pTexture,rect);
}

float ScriptBind_CCAnimation::GetTotalDelayUnits(CCAnimation* pAnimation)
{
	return pAnimation->getTotalDelayUnits();
}

void  ScriptBind_CCAnimation::SetDelayPerUnit(CCAnimation* pAnimation,float delayPerUnit)
{
	pAnimation->setDelayPerUnit(delayPerUnit);
}
float ScriptBind_CCAnimation::GetDelayPerUnit(CCAnimation* pAnimation)
{
	return pAnimation->getDelayPerUnit();
}

float ScriptBind_CCAnimation::GetDuration(CCAnimation* pAnimation)
{
	return pAnimation->getDuration();
}

void ScriptBind_CCAnimation::SetFrames(CCAnimation* pAnimation,mono::object frames)
{
	IMonoArray* mArray = *frames;
	CCArray* cArray = new CCArray();
	for (int i=0;i<mArray->GetSize();i++)
	{
		IMonoObject* mObj   = *mArray->GetItem(i);
		IMonoObject* val	= *(mObj->CallMethod("GetPtr"));
		int* ptr = (int*)(val->GetAnyValue().GetValue());
		CCSpriteFrame* animationFrame =(CCSpriteFrame*)(*ptr);
		cArray->addObject(animationFrame);
		mObj->Release();
	}
	mArray->Release();
	pAnimation->setFrames(cArray);
}
// mono::object ScriptBind_CCAnimation::GetFrames(CCAnimation* pAnimation)
// {
// 
// }

void ScriptBind_CCAnimation::SetRestoreOriginalFrame(CCAnimation* pAnimation,bool var)
{
	pAnimation->setRestoreOriginalFrame(var);
}
bool ScriptBind_CCAnimation::GetRestoreOriginalFrame(CCAnimation* pAnimation)
{
	return pAnimation->getRestoreOriginalFrame();
}

void ScriptBind_CCAnimation::SetLoops(CCAnimation* pAnimation,int var)
{
	pAnimation->setLoops(var);
}
int  ScriptBind_CCAnimation::GetLoops(CCAnimation* pAnimation)
{
	return pAnimation->getLoops();
}