
#ifndef __SCRIPTBIND_ANDROIDJNIHELPER__
#define __SCRIPTBIND_ANDROIDJNIHELPER__

#include "Native/ScriptBind_Matrix.h"

#ifdef ANDROID
#include <jni.h>

class AndroidJNIHelper:public ScriptBind_Matrix
{
public:
	AndroidJNIHelper();
	~AndroidJNIHelper();

	virtual const char* GetNamespace(){ return "MatrixEngine.Platform.Android"; };
	virtual const char* GetClassName(){ return "AndroidJNIHelper"; };
protected:

	static jmethodID GetMethodID(jclass clazz,mono::string name,mono::string sig, bool isStatic);
	static jmethodID GetConstructorID(jclass clazz,mono::string sig);
private:
};
#endif

#endif