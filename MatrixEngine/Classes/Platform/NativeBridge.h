
#ifndef __MATRIX_NATIVEBRIDGE__
#define __MATRIX_NATIVEBRIDGE__

#include "MatrixMono.h"

#include <string>

#ifndef	ANDROID
struct pthread_mutex_t_;
typedef struct pthread_mutex_t_ * pthread_mutex_t;
#endif

class NativeBridge
{

	struct DelyCallFuction
	{
		bool call;
		IMonoArray* pArgs;
		std::string function;
		std::string klass;
		std::string nameSpace;
	};

public:
	NativeBridge();
	~NativeBridge();

	virtual void  CallGameStaticFuction(IMonoArray *pames,std::string functonName,std::string className,std::string nameSpace);
	virtual void  CallEngineStaticFunction(IMonoArray *pames,std::string functonName,std::string className,std::string nameSpace);

	virtual void  CallStaticVoidFuction(std::string functonName,std::string className,std::string nameSpace);
	virtual void  CallStaticVoidFuction(std::string functonName,std::string className,std::string nameSpace,int parm);
	virtual void  CallStaticVoidFuction(std::string functonName,std::string className,std::string nameSpace,std::string parm);
	virtual void  CallStaticVoidFuction(std::string functonName,std::string className,std::string nameSpace,bool parm);
	virtual void  CallStaticVoidFuction(std::string functonName,std::string className,std::string nameSpace,bool parm1,std::string parm2);

	virtual int CallStaticIntFuction(std::string functonName,std::string className,std::string nameSpace);
public:
	virtual void OnPause();
	virtual void OnResume();

	static NativeBridge* ShareNativeBridge();

protected:
	pthread_mutex_t		m_pushMutex;

	IMonoArray* pArgs1;
	IMonoArray* pArgs2;
	IMonoArray* pArgs3;
	IMonoArray* pArgs4;

	IMonoAssembly* engineAssembly;
	IMonoAssembly* gameAssembly;

	bool isPause;
	DelyCallFuction callFuc;
private:
};

#endif