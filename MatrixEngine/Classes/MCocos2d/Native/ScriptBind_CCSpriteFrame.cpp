
#include "cocos2d.h"
#include "ScriptBind_CCSpriteFrame.h"

USING_NS_CC;

ScriptBind_CCSpriteFrame::ScriptBind_CCSpriteFrame()
{
	REGISTER_METHOD(CreateWithFile);
	REGISTER_METHOD(CreateWithTexture);
	REGISTER_METHOD(GetRectInPixels);
	REGISTER_METHOD(SetRectInPixels);
	REGISTER_METHOD(IsRotated);
	REGISTER_METHOD(SetRotated);
	REGISTER_METHOD(GetRect);
	REGISTER_METHOD(SetRect);
	REGISTER_METHOD(GetOffsetInPixels);
	REGISTER_METHOD(SetOffsetInPixels);
	REGISTER_METHOD(GetOriginalSizeInPixels);
	REGISTER_METHOD(SetOriginalSizeInPixels);
	REGISTER_METHOD(GetOriginalSize);
	REGISTER_METHOD(SetOriginalSize);
	REGISTER_METHOD(GetTexture);
	REGISTER_METHOD(SetTexture);
	REGISTER_METHOD(GetOffset);
	REGISTER_METHOD(SetOffset);
}

ScriptBind_CCSpriteFrame::~ScriptBind_CCSpriteFrame()
{

}

CCSpriteFrame* ScriptBind_CCSpriteFrame::CreateWithFile(mono::string fileName,CCRect& rect)
{
	return CCSpriteFrame::create(ToMatrixString(fileName).c_str(),rect);
}
CCSpriteFrame* ScriptBind_CCSpriteFrame::CreateWithTexture(CCTexture2D* pTexture,CCRect& rect)
{
	return CCSpriteFrame::createWithTexture(pTexture,rect);
}

void ScriptBind_CCSpriteFrame::GetRectInPixels(CCSpriteFrame* frame,CCRect& rect)
{
	rect = frame->getRectInPixels();
}
void ScriptBind_CCSpriteFrame::SetRectInPixels(CCSpriteFrame* frame,CCRect& rect)
{
	frame->setRectInPixels(rect);
}

bool ScriptBind_CCSpriteFrame::IsRotated(CCSpriteFrame* frame)
{
	return frame->isRotated();
}
void ScriptBind_CCSpriteFrame::SetRotated(CCSpriteFrame* frame,bool bRotated)
{
	frame->setRotated(bRotated);
}

void ScriptBind_CCSpriteFrame::GetRect(CCSpriteFrame* frame,CCRect& rect)
{
	rect = frame->getRect();
}
void ScriptBind_CCSpriteFrame::SetRect(CCSpriteFrame* frame,CCRect& rect)
{
	frame->setRect(rect);
}

void ScriptBind_CCSpriteFrame::GetOffsetInPixels(CCSpriteFrame* frame,CCPoint& offsetInPixels)
{
	offsetInPixels = frame->getOffsetInPixels();
}
void ScriptBind_CCSpriteFrame::SetOffsetInPixels(CCSpriteFrame* frame,CCPoint& offsetInPixels)
{
	frame->setOffsetInPixels(offsetInPixels);
}

void ScriptBind_CCSpriteFrame::GetOriginalSizeInPixels(CCSpriteFrame* frame,CCSize& sizeInPixels)
{
	sizeInPixels = frame->getOriginalSizeInPixels();
}
void ScriptBind_CCSpriteFrame::SetOriginalSizeInPixels(CCSpriteFrame* frame,CCSize& sizeInPixels)
{
	frame->setOriginalSizeInPixels(sizeInPixels);
}

void ScriptBind_CCSpriteFrame::GetOriginalSize(CCSpriteFrame* frame,CCSize& sizeInPixels)
{
	sizeInPixels = frame->getOriginalSize();
}
void ScriptBind_CCSpriteFrame::SetOriginalSize(CCSpriteFrame* frame,CCSize& sizeInPixels)
{
	frame->setOriginalSize(sizeInPixels);
}

CCTexture2D* ScriptBind_CCSpriteFrame::GetTexture(CCSpriteFrame* frame)
{
	return frame->getTexture();
}
void ScriptBind_CCSpriteFrame::SetTexture(CCSpriteFrame* frame,CCTexture2D* pTexture)
{
	frame->setTexture(pTexture);
}

void ScriptBind_CCSpriteFrame::GetOffset(CCSpriteFrame* frame,CCPoint& offsets)
{
	offsets = frame->getOffset();
}
void ScriptBind_CCSpriteFrame::SetOffset(CCSpriteFrame* frame,CCPoint& offsets)
{
	frame->setOffset(offsets);
}