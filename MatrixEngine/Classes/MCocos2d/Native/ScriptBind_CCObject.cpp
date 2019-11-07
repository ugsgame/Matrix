
//#include "stdneb.h"
#include "cocos2d.h"
#include "ScriptBind_CCObject.h"

USING_NS_CC;

#ifndef WIN32
/*
#include <stdio.h>  
#include <stdlib.h>  
#include <signal.h>  
#include <setjmp.h>  
#include <unistd.h>  

 
//x86/Linux、x86/Solaris、SPARC/Solaris will sigal SIGSEGV 
//x86/FreeBSD、x86/NetBSD、x86/OpenBSD MacOS will sigal SIGBUS 
 

#if defined(__MACH__) && defined(__FreeBSD__) && defined(__NetBSD__) && defined(__OpenBSD__)\&& defined(__DragonFly__)  
#define ERROR_SIGNAL SIGBUS  
#else  
#define ERROR_SIGNAL SIGSEGV  
#endif  

#ifndef	__MSC_VER__
typedef	int	BOOL;
typedef	unsigned int	UINT;
typedef	unsigned char	BYTE;
#define	TRUE	1
#define	FALSE	0
#endif	//	__MSC_VER__

static BOOL g_bPtrTestInstalled;
static jmp_buf g_PtrTestJmpBuf;

void __cdecl PtrTestHandler(int nSig)
{
	if (g_bPtrTestInstalled)
		longjmp(g_PtrTestJmpBuf, 1);
}

BOOL __IsBadReadPtr(void* lp, UINT cb)
{
	UINT i;
	BYTE b1;
	BOOL bRet = TRUE;
	void (__cdecl* pfnPrevHandler)(int);
	g_bPtrTestInstalled	= TRUE;
	if (setjmp(g_PtrTestJmpBuf))
	{
		bRet = FALSE;
		goto Ret;
	}
	pfnPrevHandler = signal(SIGSEGV, PtrTestHandler);

	for (i = 0; i < cb; i ++)
		b1 = ((BYTE*)lp)[i];
	Ret:
	g_bPtrTestInstalled	= FALSE;
	signal(SIGSEGV, pfnPrevHandler);

	return bRet;
}
*/
#endif

ScriptBind_CCObject::ScriptBind_CCObject()
{
	REGISTER_METHOD(SetMonoObject);
	REGISTER_METHOD(GetMonoObject);
	REGISTER_METHOD(IsBindMonoObject);
	REGISTER_METHOD(UnBindMonoObject);

	REGISTER_METHOD(Release);
	REGISTER_METHOD(DelayRelease);
	REGISTER_METHOD(Retain);
	REGISTER_METHOD(RetainCount);
	REGISTER_METHOD(IsEqual);
	REGISTER_METHOD(Copy);
	REGISTER_METHOD(GetID);
}
ScriptBind_CCObject::~ScriptBind_CCObject()
{
	
}

void ScriptBind_CCObject::SetMonoObject(cocos2d::CCObject* _this,mono::object object)
{
	_this->setMonoObject(*object);
}

mono::object ScriptBind_CCObject::GetMonoObject(cocos2d::CCObject* _this)
{
	IMonoObject* object = _this->getMonoObject();
	return object->GetManagedObject();
}

bool ScriptBind_CCObject::IsBindMonoObject(cocos2d::CCObject* _this)
{
	return _this->isBindMonoObject();
}

void ScriptBind_CCObject::UnBindMonoObject(cocos2d::CCObject* _this)
{
	_this->unBindMonoObject();
}

void ScriptBind_CCObject::Release(CCObject* _this)
{
	//TODO
#ifdef WIN32
	//if(!IsBadCodePtr((FARPROC)_this))
#else
	//if(!__IsBadReadPtr(_this,sizeof(CCObject)))
#endif
		CC_SAFE_RELEASE(_this);
}
void ScriptBind_CCObject::DelayRelease(CCObject* _this)
{
	CCAssert(_this,"");
	CCDelayRelease::sharedDelayRelease()->push(_this);
}

void ScriptBind_CCObject::Retain(CCObject* _this)
{	
	CCAssert(_this,"");
	_this->retain();
}
int	ScriptBind_CCObject::RetainCount(CCObject* _this)
{
	CCAssert(_this,"");
	return _this->retainCount();
}
bool ScriptBind_CCObject::IsEqual(CCObject* _this,CCObject* object)
{
	CCAssert(_this,"");
	return _this->isEqual(object);
}
CCObject* ScriptBind_CCObject::Copy(CCObject* _this)
{
	CCAssert(_this,"");
	return _this->copy();
}

int ScriptBind_CCObject::GetID(CCObject* _this)
{
	CCAssert(_this,"");
	return _this->getID();
}