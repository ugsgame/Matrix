#include "cocos2d.h"
#include "NativeBridge.h"
#include "MatrixScript.h"
#include "pthread.h"

USING_NS_CC;

static NativeBridge *s_SharedNativeBridge = NULL;

//TODO:当程序回到回台来回调时，CreateMonoArray，InvokeArray,都会产生错误，
//原因没确定，回到后台先用标记，回到前台再执行，但限制太多,到时再看看怎么
//完美解决吧

NativeBridge::NativeBridge():isPause(false)
{
	//pthread_mutex_init(&m_pushMutex, NULL);
	callFuc.call =false;

	pArgs1 = CreateMonoArray(1);
	pArgs2 = CreateMonoArray(2);
	pArgs3 = CreateMonoArray(3);
	pArgs4 = CreateMonoArray(4);
}

NativeBridge::~NativeBridge()
{
	//pthread_mutex_destroy(&m_pushMutex);

	SAFE_RELEASE(pArgs1);
	SAFE_RELEASE(pArgs2);
	SAFE_RELEASE(pArgs3);
	SAFE_RELEASE(pArgs4);
}

void NativeBridge::CallGameStaticFuction(IMonoArray *pames,std::string functonName,std::string className,std::string nameSpace)
{
	//pthread_mutex_lock(&m_pushMutex);
	IMonoClass* klass = gameAssembly->GetClass(className.c_str(),nameSpace.c_str());
	klass->InvokeArray(NULL,functonName.c_str(),pames,NULL);
	//pthread_mutex_unlock(&m_pushMutex);
}

void NativeBridge::CallEngineStaticFunction(IMonoArray *pames,std::string functonName,std::string className,std::string nameSpace)
{
	//pthread_mutex_lock(&m_pushMutex);
	IMonoClass* klass = engineAssembly->GetClass(className.c_str(),nameSpace.c_str());
	klass->InvokeArray(NULL,functonName.c_str(),pames,NULL);
	//pthread_mutex_unlock(&m_pushMutex);
}

NativeBridge* NativeBridge::ShareNativeBridge()
{
	if(!s_SharedNativeBridge)
	{
		s_SharedNativeBridge = new NativeBridge();
		s_SharedNativeBridge->engineAssembly = MatrixScript::ShareMatrixScript()->GetEngineAssembly();
		s_SharedNativeBridge->gameAssembly = MatrixScript::ShareMatrixScript()->GetGameAssembly();
	}
	return s_SharedNativeBridge;
}

void NativeBridge::OnPause()
{
	this->isPause = true;
}

void NativeBridge::OnResume()
{
	if(this->isPause&&callFuc.call)
	{
		this->CallGameStaticFuction(callFuc.pArgs,callFuc.function,callFuc.klass,callFuc.nameSpace);
		callFuc.call = false;
	}
	this->isPause = false;
}

void  NativeBridge::CallStaticVoidFuction(std::string functonName,std::string className,std::string nameSpace)
{
	if(this->isPause)
	{
		callFuc.pArgs = NULL;
		callFuc.function = functonName;
		callFuc.klass = className;
		callFuc.nameSpace = nameSpace;
	}
	else
		this->CallGameStaticFuction(NULL,functonName,className,nameSpace);
}
void  NativeBridge::CallStaticVoidFuction(std::string functonName,std::string className,std::string nameSpace,int parm)
{
	pArgs1->Clear();
	pArgs1->Insert(parm);
	if(this->isPause)
	{
		callFuc.pArgs = pArgs1;
		callFuc.function = functonName;
		callFuc.klass = className;
		callFuc.nameSpace = nameSpace;
		callFuc.call = true;
	}
	else
		this->CallGameStaticFuction(pArgs1,functonName,className,nameSpace);
}
void  NativeBridge::CallStaticVoidFuction(std::string functonName,std::string className,std::string nameSpace,std::string parm)
{
	pArgs1->Clear();
	pArgs1->Insert(parm);
	if(this->isPause)
	{
		callFuc.pArgs = pArgs1;
		callFuc.function = functonName;
		callFuc.klass = className;
		callFuc.nameSpace = nameSpace;
		callFuc.call = true;
	}
	else
		this->CallGameStaticFuction(pArgs1,functonName,className,nameSpace);
}
void  NativeBridge::CallStaticVoidFuction(std::string functonName,std::string className,std::string nameSpace,bool parm)
{
	pArgs1->Clear();
	pArgs1->Insert(parm);
	if(this->isPause)
	{
		callFuc.pArgs = pArgs1;
		callFuc.function = functonName;
		callFuc.klass = className;
		callFuc.nameSpace = nameSpace;
		callFuc.call = true;
	}
	else
		this->CallGameStaticFuction(pArgs1,functonName,className,nameSpace);
}
void  NativeBridge::CallStaticVoidFuction(std::string functonName,std::string className,std::string nameSpace,bool parm1,std::string parm2)
{
	pArgs2->Clear();
	pArgs2->Insert(parm1);
	pArgs2->Insert(parm2);
	if(this->isPause)
	{
		callFuc.pArgs = pArgs2;
		callFuc.function = functonName;
		callFuc.klass = className;
		callFuc.nameSpace = nameSpace;
		callFuc.call = true;
	}
	else
		this->CallGameStaticFuction(pArgs2,functonName,className,nameSpace);
}

int NativeBridge::CallStaticIntFuction(std::string functonName,std::string className,std::string nameSpace)
{
	//!!!有反回值不能用延时回调--这里先不要在后台调吧~~坑
	if(this->isPause)
	{
		callFuc.pArgs = NULL;
		callFuc.function = functonName;
		callFuc.klass = className;
		callFuc.nameSpace = nameSpace;
		callFuc.call = true;
		return -404;  //这是暗号(不能形成返回值)
	}
	else
	{
		IMonoClass* klass = gameAssembly->GetClass(className.c_str(),nameSpace.c_str());
		IMonoObject* result = *klass->InvokeArray(NULL,functonName.c_str(),NULL,NULL);
		return result->GetAnyValue().i;
	}
}