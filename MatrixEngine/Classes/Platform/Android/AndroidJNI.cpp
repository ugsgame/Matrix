
#include "cocos2d.h"
#include "AndroidJNI.h"

USING_NS_CC;

#ifdef ANDROID
#include "platform/android/jni/JniHelper.h"

#include <string.h>
#include <android/log.h>

#if 1
#define  LOG_TAG    "AndroidJNI"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define  LOGD(...) 
#endif

#define JAVAVM    cocos2d::JniHelper::getJavaVM()

extern "C"
{

	//////////////////////////////////////////////////////////////////////////
	// java vm helper function
	//////////////////////////////////////////////////////////////////////////

	static pthread_key_t s_threadKey;

	static void detach_current_thread (void *env) {
		JAVAVM->DetachCurrentThread();
	}

	static bool getEnv(JNIEnv **env)
	{
		bool bRet = false;

		switch(JAVAVM->GetEnv((void**)env, JNI_VERSION_1_4))
		{
		case JNI_OK:
			bRet = true;
			break;
		case JNI_EDETACHED:
			pthread_key_create (&s_threadKey, detach_current_thread);
			if (JAVAVM->AttachCurrentThread(env, 0) < 0)
			{
				LOGD("Failed to get the environment using AttachCurrentThread()");
				break;
			}
			if (pthread_getspecific(s_threadKey) == NULL)
				pthread_setspecific(s_threadKey, env); 
			bRet = true;
			break;
		default:
			LOGD("Failed to get the environment using GetEnv()");
			break;
		}      

		return bRet;
	}

	static jclass getClassID_(const char *className, JNIEnv *env)
	{
		JNIEnv *pEnv = env;
		jclass ret = 0;

		do 
		{
			if (! pEnv)
			{
				if (! getEnv(&pEnv))
				{
					break;
				}
			}

			ret = pEnv->FindClass(className);
			if (! ret)
			{
				LOGD("Failed to find class of %s", className);
				break;
			}
		} while (0);

		return ret;
	}

	static bool getStaticMethodInfo_(cocos2d::JniMethodInfo &methodinfo, jclass classID, const char *methodName, const char *paramCode)
	{
		jmethodID methodID = 0;
		JNIEnv *pEnv = 0;
		bool bRet = false;

		do 
		{
			if (! getEnv(&pEnv))
			{
				break;
			}

			//jclass classID = getClassID_(className, pEnv);

			methodID = pEnv->GetStaticMethodID(classID, methodName, paramCode);
			if (! methodID)
			{
				LOGD("Failed to find static method id of %s", methodName);
				break;
			}

			methodinfo.classID = classID;
			methodinfo.env = pEnv;
			methodinfo.methodID = methodID;

			bRet = true;
		} while (0);

		return bRet;
	}

	static bool getMethodInfo_(cocos2d::JniMethodInfo &methodinfo, jclass classID, const char *methodName, const char *paramCode)
	{
		jmethodID methodID = 0;
		JNIEnv *pEnv = 0;
		bool bRet = false;

		do 
		{
			if (! getEnv(&pEnv))
			{
				break;
			}

			//jclass classID = getClassID_(className, pEnv);

			methodID = pEnv->GetMethodID(classID, methodName, paramCode);
			if (! methodID)
			{
				LOGD("Failed to find method id of %s", methodName);
				break;
			}

			methodinfo.classID = classID;
			methodinfo.env = pEnv;
			methodinfo.methodID = methodID;

			bRet = true;
		} while (0);

		return bRet;
	}

	static std::string jstring2string_(jstring jstr)
	{
		if (jstr == NULL)
		{
			return "";
		}

		JNIEnv *env = 0;

		if (! getEnv(&env))
		{
			return 0;
		}

		const char* chars = env->GetStringUTFChars(jstr, NULL);
		std::string ret(chars);
		env->ReleaseStringUTFChars(jstr, chars);

		return ret;
	}
	static mono::string jstring2monostring_(jstring jstr)
	{
		if (jstr == NULL)
		{
			return ToMonoString("");
		}

		JNIEnv *env = 0;

		if (! getEnv(&env))
		{
			return 0;
		}

		const char* chars = env->GetStringUTFChars(jstr, NULL);
		mono::string ret = ToMonoString(chars);
		
		env->ReleaseStringUTFChars(jstr, chars);

		return ret;
	}

}

AndroidJNI::AndroidJNI()
{
	REGISTER_METHOD(AllocObject);
	REGISTER_METHOD(AttachCurrentThread);

	REGISTER_METHOD(CallBooleanMethod);
	REGISTER_METHOD(CallByteMethod);
	REGISTER_METHOD(CallCharMethod);
	REGISTER_METHOD(CallDoubleMethod);
	REGISTER_METHOD(CallFloatMethod);
	REGISTER_METHOD(CallIntMethod);
	REGISTER_METHOD(CallLongMethod);
	REGISTER_METHOD(CallObjectMethod);
	REGISTER_METHOD(CallShortMethod);
	REGISTER_METHOD(CallStaticBooleanMethod);
	REGISTER_METHOD(CallStaticByteMethod);
	REGISTER_METHOD(CallStaticCharMethod);
	REGISTER_METHOD(CallStaticDoubleMethod);
	REGISTER_METHOD(CallStaticFloatMethod);
	REGISTER_METHOD(CallStaticIntMethod);
	REGISTER_METHOD(CallStaticLongMethod);
	REGISTER_METHOD(CallStaticObjectMethod);
	REGISTER_METHOD(CallStaticShortMethod);
	REGISTER_METHOD(CallStaticStringMethod);
	REGISTER_METHOD(CallStaticVoidMethod);
	REGISTER_METHOD(CallStringMethod);
	REGISTER_METHOD(CallVoidMethod);

	REGISTER_METHOD(DeleteGlobalRef);
	REGISTER_METHOD(DeleteLocalRef);
	REGISTER_METHOD(DetachCurrentThread);

	REGISTER_METHOD(EnsureLocalCapacity);

	REGISTER_METHOD(ExceptionClear);
	REGISTER_METHOD(ExceptionDescribe);
	REGISTER_METHOD(ExceptionOccurred);

	REGISTER_METHOD(FatalError);
	REGISTER_METHOD(FindClass);

	REGISTER_METHOD(GetArrayLength);
	REGISTER_METHOD(GetBooleanArrayElement);
	REGISTER_METHOD(GetBooleanField);
	REGISTER_METHOD(GetByteArrayElement);
	REGISTER_METHOD(GetByteField);
	REGISTER_METHOD(GetCharArrayElement);
	REGISTER_METHOD(GetCharField);
	REGISTER_METHOD(GetDoubleArrayElement);
	REGISTER_METHOD(GetDoubleField);
	REGISTER_METHOD(GetFieldID);
	REGISTER_METHOD(GetFloatArrayElement);
	REGISTER_METHOD(GetFloatField);
	REGISTER_METHOD(GetIntArrayElement);
	REGISTER_METHOD(GetIntField);
	REGISTER_METHOD(GetLongArrayElement);
	REGISTER_METHOD(GetLongField);
	REGISTER_METHOD(GetMethodID);
	REGISTER_METHOD(GetObjectArrayElement);
	REGISTER_METHOD(GetObjectClass);
	REGISTER_METHOD(GetObjectField);
	REGISTER_METHOD(GetShortArrayElement);
	REGISTER_METHOD(GetShortField);
	REGISTER_METHOD(GetStaticBooleanField);
	REGISTER_METHOD(GetStaticByteField);
	REGISTER_METHOD(GetStaticCharField);
	REGISTER_METHOD(GetStaticDoubleField);
	REGISTER_METHOD(GetStaticFieldID);
	REGISTER_METHOD(GetStaticFloatField);
	REGISTER_METHOD(GetStaticIntField);
	REGISTER_METHOD(GetStaticLongField);
	REGISTER_METHOD(GetStaticMethodID);
	REGISTER_METHOD(GetStaticObjectField);
	REGISTER_METHOD(GetStaticShortField);
	//REGISTER_METHOD(GetStaticStringField);
	//REGISTER_METHOD(GetStringField);
	REGISTER_METHOD(GetStringUTFChars);
	REGISTER_METHOD(GetStringUTFLength);
	REGISTER_METHOD(GetSuperclass);
	REGISTER_METHOD(GetVersion);

	REGISTER_METHOD(IsAssignableFrom);
	REGISTER_METHOD(IsInstanceOf);
	REGISTER_METHOD(IsSameObject);

	REGISTER_METHOD(NewBooleanArray);
	REGISTER_METHOD(NewByteArray);
	REGISTER_METHOD(NewCharArray);
	REGISTER_METHOD(NewDoubleArray);
	REGISTER_METHOD(NewFloatArray);
	REGISTER_METHOD(NewGlobalRef);
	REGISTER_METHOD(NewIntArray);
	REGISTER_METHOD(NewLocalRef);
	REGISTER_METHOD(NewLongArray);
	REGISTER_METHOD(NewObject);
	REGISTER_METHOD(NewObjectArray);
	REGISTER_METHOD(NewShortArray);
	REGISTER_METHOD(NewStringUTF);

	REGISTER_METHOD(PopLocalFrame);
	REGISTER_METHOD(PushLocalFrame);

	REGISTER_METHOD(SetBooleanArrayElement);
	REGISTER_METHOD(SetBooleanField);
	REGISTER_METHOD(SetByteArrayElement);
	REGISTER_METHOD(SetByteField);
	REGISTER_METHOD(SetCharArrayElement);
	REGISTER_METHOD(SetCharField);
	REGISTER_METHOD(SetDoubleArrayElement);
	REGISTER_METHOD(SetDoubleField);
	REGISTER_METHOD(SetFloatArrayElement);
	REGISTER_METHOD(SetFloatField);
	REGISTER_METHOD(SetIntArrayElement);
	REGISTER_METHOD(SetIntField);
	REGISTER_METHOD(SetLongArrayElement);
	REGISTER_METHOD(SetLongField);
	REGISTER_METHOD(SetObjectArrayElement);
	REGISTER_METHOD(SetObjectField);
	REGISTER_METHOD(SetShortArrayElement);
	REGISTER_METHOD(SetShortField);
	REGISTER_METHOD(SetStaticBooleanField);
	REGISTER_METHOD(SetStaticByteField);
	REGISTER_METHOD(SetStaticCharField);
	REGISTER_METHOD(SetStaticDoubleField);
	REGISTER_METHOD(SetStaticFloatField);
	REGISTER_METHOD(SetStaticIntField);
	REGISTER_METHOD(SetStaticLongField);
	REGISTER_METHOD(SetStaticObjectField);
	REGISTER_METHOD(SetStaticShortField);
// 	REGISTER_METHOD(SetStaticStringField);
// 	REGISTER_METHOD(SetStringField);

	REGISTER_METHOD(Throw);
	REGISTER_METHOD(ThrowNew);

	REGISTER_METHOD(ToBooleanArray);
	REGISTER_METHOD(ToByteArray);
	REGISTER_METHOD(ToCharArray);
	REGISTER_METHOD(ToDoubleArray);
	REGISTER_METHOD(ToFloatArray);
	REGISTER_METHOD(ToIntArray);
	REGISTER_METHOD(ToLongArray);
	REGISTER_METHOD(ToObjectArray);
	REGISTER_METHOD(ToReflectedField);
	REGISTER_METHOD(ToReflectedMethod);
	REGISTER_METHOD(ToShortArray);
}

AndroidJNI::~AndroidJNI()
{
}

jobject AndroidJNI::AllocObject(jclass clazz)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jobject jobj = env->AllocObject(clazz);
	return jobj;
}

int AndroidJNI::AttachCurrentThread()
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return JAVAVM->AttachCurrentThread(&env,NULL);
}

bool AndroidJNI::CallBooleanMethod(jobject obj, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	bool ref = env->CallBooleanMethodA(obj, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

unsigned char AndroidJNI::CallByteMethod(jobject obj, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	unsigned char ref = env->CallByteMethodA(obj, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

char AndroidJNI::CallCharMethod(jobject obj, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	char ref = env->CallCharMethodA(obj, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

double AndroidJNI::CallDoubleMethod(jobject obj, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	double ref = env->CallDoubleMethodA(obj, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

float AndroidJNI::CallFloatMethod(jobject obj, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	float ref = env->CallFloatMethodA(obj, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

int AndroidJNI::CallIntMethod(jobject obj, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	int ref = env->CallIntMethodA(obj, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

long AndroidJNI::CallLongMethod(jobject obj, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	long ref = env->CallLongMethodA(obj, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

jobject AndroidJNI::CallObjectMethod(jobject obj, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	jobject ref = env->CallObjectMethodA(obj, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

short AndroidJNI::CallShortMethod(jobject obj, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	short ref = env->CallShortMethodA(obj, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

bool AndroidJNI::CallStaticBooleanMethod(jclass clazz, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	bool ref = env->CallStaticBooleanMethodA(clazz, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

unsigned char AndroidJNI::CallStaticByteMethod(jclass clazz, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	unsigned char ref = env->CallStaticByteMethodA(clazz, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

char AndroidJNI::CallStaticCharMethod(jclass clazz, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	char ref = env->CallStaticCharMethodA(clazz, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

double AndroidJNI::CallStaticDoubleMethod(jclass clazz, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	double ref = env->CallStaticDoubleMethodA(clazz, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

float AndroidJNI::CallStaticFloatMethod(jclass clazz, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	float ref = env->CallStaticFloatMethodA(clazz, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

int AndroidJNI::CallStaticIntMethod(jclass clazz, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	int ref = env->CallStaticIntMethodA(clazz, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

long AndroidJNI::CallStaticLongMethod(jclass clazz, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	long ref = env->CallStaticLongMethodA(clazz, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

jobject AndroidJNI::CallStaticObjectMethod(jclass clazz, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	jobject ref = env->CallStaticObjectMethodA(clazz, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

short AndroidJNI::CallStaticShortMethod(jclass clazz, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	short ref = env->CallStaticShortMethodA(clazz, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
	return ref;
}

mono::string AndroidJNI::CallStaticStringMethod(jclass clazz, jmethodID methodID, mono::object args)
{
// 	JNIEnv * env;
// 	CCAssert(getEnv(&env),"Can't get env!!");
// 	IMonoArray* moArgs = *args;
// 	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
// 	const char* ref = env->CallStaticStringMethodA(clazz, methodID,jvalues);
// 	moArgs->Release();
// 	CC_SAFE_DELETE_ARRAY(jvalues);
	return NULL;
}

void AndroidJNI::CallStaticVoidMethod(jclass clazz, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	env->CallStaticVoidMethodA(clazz, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
}

mono::string AndroidJNI::CallStringMethod(jobject obj, jmethodID methodID, mono::object args)
{
// 	JNIEnv * env;
// 	CCAssert(getEnv(&env),"Can't get env!!");
// 	IMonoArray* moArgs = *args;
// 	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
// 	jobject ref = env->CallObjectMethodA(obj, methodID,jvalues);
// 	moArgs->Release();
// 	CC_SAFE_DELETE_ARRAY(jvalues);
	return NULL;
}

void AndroidJNI::CallVoidMethod(jobject obj, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	env->CallVoidMethodA(obj, methodID,jvalues);
	moArgs->Release();
	CC_SAFE_DELETE_ARRAY(jvalues);
}

void AndroidJNI::DeleteGlobalRef(jobject obj)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->DeleteGlobalRef(env,obj);
}

void AndroidJNI::DeleteLocalRef(jobject obj)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->DeleteLocalRef(env,obj);
}

int AndroidJNI::DetachCurrentThread()
{
 	return JAVAVM->DetachCurrentThread();
}

int AndroidJNI::EnsureLocalCapacity(int capacity)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->EnsureLocalCapacity(env,capacity);
}

void AndroidJNI::ExceptionClear()
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->ExceptionClear(env);
}

void AndroidJNI::ExceptionDescribe()
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->ExceptionDescribe(env);
}

jthrowable AndroidJNI::ExceptionOccurred()
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->ExceptionOccurred(env);
}

void AndroidJNI::FatalError(mono::string message)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->FatalError(env,ToMatrixString(message).c_str());
}

jclass AndroidJNI::FindClass(mono::string name)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->FindClass(ToMatrixString(name).c_str());
}

int AndroidJNI::GetArrayLength(jarray array)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (int)env->GetArrayLength(array);
}

bool AndroidJNI::GetBooleanArrayElement(jbooleanArray array, int index)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jboolean isCopy = true;
	jboolean* jarrele = env->functions->GetBooleanArrayElements(env,array,&isCopy);
	jboolean ret = jarrele[index];
	env->functions->ReleaseBooleanArrayElements(env,array,jarrele,0);
	return ret;
}

bool AndroidJNI::GetBooleanField(jobject obj, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (bool)env->functions->GetBooleanField(env,obj,fieldID);
}

unsigned char AndroidJNI::GetByteArrayElement(jbyteArray array, int index)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jboolean isCopy = true;
	jbyte* jarrele = env->functions->GetByteArrayElements(env,array,&isCopy);
	jbyte ret = jarrele[index];
	env->functions->ReleaseByteArrayElements(env,array,jarrele,0);
	return ret;
}

unsigned char AndroidJNI::GetByteField(jobject obj, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (unsigned char)env->functions->GetByteField(env,obj,fieldID);
}

char AndroidJNI::GetCharArrayElement(jcharArray array, int index)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jboolean isCopy = true;
	jchar* jarrele = env->functions->GetCharArrayElements(env,array,&isCopy);
	jchar ret = jarrele[index];
	env->functions->ReleaseCharArrayElements(env,array,jarrele,0);
	return ret;
}

char AndroidJNI::GetCharField(jobject obj, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (char)env->functions->GetCharField(env,obj,fieldID);
}

double AndroidJNI::GetDoubleArrayElement(jdoubleArray array, int index)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jboolean isCopy = true;
	jdouble* jarrele = env->functions->GetDoubleArrayElements(env,array,&isCopy);
	jdouble ret = jarrele[index];
	env->functions->ReleaseDoubleArrayElements(env,array,jarrele,0);
	return ret;
}

double AndroidJNI::GetDoubleField(jobject obj, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (double)env->functions->GetDoubleField(env,obj,fieldID);	
}

jfieldID AndroidJNI::GetFieldID(jclass clazz, mono::string name, mono::string sig)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->GetFieldID(env,clazz,ToMatrixString(name).c_str(),ToMatrixString(sig).c_str());
}

float AndroidJNI::GetFloatArrayElement(jfloatArray array, int index)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jboolean isCopy = true;
	jfloat* jarrele = env->functions->GetFloatArrayElements(env,array,&isCopy);
	jfloat ret = jarrele[index];
	env->functions->ReleaseFloatArrayElements(env,array,jarrele,0);
	return ret;
}

float AndroidJNI::GetFloatField(jobject obj, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (float)env->functions->GetFloatField(env,obj,fieldID);	
}

int AndroidJNI::GetIntArrayElement(jintArray array, int index)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jboolean isCopy = true;
	jint* jarrele = env->functions->GetIntArrayElements(env,array,&isCopy);
	jint ret = jarrele[index];
	env->functions->ReleaseIntArrayElements(env,array,jarrele,0);
	return ret;
}

int AndroidJNI::GetIntField(jobject obj, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (int)env->functions->GetIntField(env,obj,fieldID);
}

long AndroidJNI::GetLongArrayElement(jlongArray array, int index)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jboolean isCopy = true;
	jlong* jarrele = env->functions->GetLongArrayElements(env,array,&isCopy);
	jlong ret = jarrele[index];
	env->functions->ReleaseLongArrayElements(env,array,jarrele,0);
	return ret;
}

long AndroidJNI::GetLongField(jobject obj, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (long)env->functions->GetLongField(env,obj,fieldID);
}

jmethodID AndroidJNI::GetMethodID(jclass clazz, mono::string name, mono::string sig)
{
	JniMethodInfo t;
	getMethodInfo_(t,clazz,ToMatrixString(name).c_str(),ToMatrixString(sig).c_str());
	return t.methodID;
}

jobject AndroidJNI::GetObjectArrayElement(jobjectArray array, int index)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jobject ref = env->functions->GetObjectArrayElement(env,array,index);
	return ref;
}

jclass AndroidJNI::GetObjectClass(jobject obj)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jclass ref = env->functions->GetObjectClass(env,obj);
	return ref;
}

jobject AndroidJNI::GetObjectField(jobject obj, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jclass ref = env->functions->GetObjectField(env,obj,fieldID);
	return ref;
}

short AndroidJNI::GetShortArrayElement(jshortArray array, int index)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jboolean isCopy = true;
	jshort* jarrele = env->functions->GetShortArrayElements(env,array,&isCopy);
	jshort ret = jarrele[index];
	env->functions->ReleaseShortArrayElements(env,array,jarrele,0);
	return ret;
}

short AndroidJNI::GetShortField(jobject obj, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (short)env->functions->GetShortField(env,obj,fieldID);
}

bool AndroidJNI::GetStaticBooleanField(jclass clazz, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (bool)env->functions->GetStaticBooleanField(env,clazz,fieldID);
}

unsigned char AndroidJNI::GetStaticByteField(jclass clazz, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (unsigned char)env->functions->GetStaticByteField(env,clazz,fieldID);
}

char AndroidJNI::GetStaticCharField(jclass clazz, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (char)env->functions->GetStaticCharField(env,clazz,fieldID);
}

double AndroidJNI::GetStaticDoubleField(jclass clazz, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (double)env->functions->GetStaticDoubleField(env,clazz,fieldID);
}

jfieldID AndroidJNI::GetStaticFieldID(jclass clazz, mono::string name, mono::string sig)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (jfieldID)env->functions->GetStaticFieldID(env,clazz,ToMatrixString(name).c_str(),ToMatrixString(sig).c_str());
}

float AndroidJNI::GetStaticFloatField(jclass clazz, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (float)env->functions->GetStaticFloatField(env,clazz,fieldID);
}

int AndroidJNI::GetStaticIntField(jclass clazz, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (int)env->functions->GetStaticIntField(env,clazz,fieldID);
}

long AndroidJNI::GetStaticLongField(jclass clazz, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (long)env->functions->GetStaticLongField(env,clazz,fieldID);
}

jmethodID AndroidJNI::GetStaticMethodID(jclass clazz, mono::string name, mono::string sig)
{
	JniMethodInfo t;
	getStaticMethodInfo_(t,clazz,ToMatrixString(name).c_str(),ToMatrixString(sig).c_str());
	return t.methodID;
}

jobject AndroidJNI::GetStaticObjectField(jclass clazz, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (jobject)env->functions->GetStaticObjectField(env,clazz,fieldID);
}

short AndroidJNI::GetStaticShortField(jclass clazz, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return (short)env->functions->GetStaticShortField(env,clazz,fieldID);
}
/*
mono::string AndroidJNI::GetStaticStringField(jclass clazz, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jboolean isCopy = true;
	jstring ref = (jstring)env->functions->GetStaticStringField(env,clazz,fieldID);
	const jchar* str = env->functions->GetStringChars(env,ref,&isCopy);
	return ToMonoString(str);
}

mono::string AndroidJNI::GetStringField(jobject obj, jfieldID fieldID)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jboolean isCopy = true;
	jstring ref = (jstring)env->functions->GetStringField(env,obj,fieldID);
	const jchar* str = env->functions->GetStringChars(env,ref,&isCopy);
	return ToMonoString((const char*)str);
}
*/
mono::string AndroidJNI::GetStringUTFChars(jstring str)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jboolean isCopy = true;
	const char* ref = env->functions->GetStringUTFChars(env,str,&isCopy);
	return ToMonoString(ref);
}

int AndroidJNI::GetStringUTFLength(jstring str)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->GetStringUTFLength(env,str);
}

jclass AndroidJNI::GetSuperclass(jclass clazz)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->GetSuperclass(env,clazz);
}

int AndroidJNI::GetVersion()
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->GetVersion(env);
}


bool AndroidJNI::IsAssignableFrom(jclass clazz1, jclass clazz2)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->IsAssignableFrom(env,clazz1,clazz2);
}
bool AndroidJNI::IsInstanceOf(jobject obj, jclass clazz)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->IsInstanceOf(env,obj,clazz);
}
bool AndroidJNI::IsSameObject(jobject obj1, jobject obj2)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->IsSameObject(env,obj1,obj2);
}

jbooleanArray AndroidJNI::NewBooleanArray(int size)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->NewBooleanArray(env,size);
}

jbyteArray AndroidJNI::NewByteArray(int size)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->NewByteArray(env,size);
}

jcharArray AndroidJNI::NewCharArray(int size)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->NewCharArray(env,size);
}

jdoubleArray AndroidJNI::NewDoubleArray(int size)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->NewDoubleArray(env,size);
}

jfloatArray AndroidJNI::NewFloatArray(int size)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->NewFloatArray(env,size);
}

jobject AndroidJNI::NewGlobalRef(jobject obj)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->NewGlobalRef(env,obj);
}

jintArray AndroidJNI::NewIntArray(int size)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->NewIntArray(env,size);
}

jobject AndroidJNI::NewLocalRef(jobject obj)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->NewLocalRef(env,obj);
}

jlongArray AndroidJNI::NewLongArray(int size)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->NewLongArray(env,size);
}

jobject AndroidJNI::NewObject(jclass clazz, jmethodID methodID, mono::object args)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArgs = *args;
	jvalue* jvalues = moArgs->ToCppArray<jvalue>();
	return env->functions->NewObjectA(env,clazz,methodID,jvalues);
}

jobjectArray AndroidJNI::NewObjectArray(int size, jclass clazz, jobject obj)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->NewObjectArray(env,size,clazz,obj);
}

jshortArray AndroidJNI::NewShortArray(int size)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->NewShortArray(env,size);
}

jstring AndroidJNI::NewStringUTF(mono::string str)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jstring jkey = env->NewStringUTF(ToMatrixString(str).c_str());
	return jkey;
}

jobject AndroidJNI::PopLocalFrame(jobject result)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->PopLocalFrame(env,result);
}

int AndroidJNI::PushLocalFrame(int capacity)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->PushLocalFrame(env,capacity);
}

void AndroidJNI::SetBooleanArrayElement(jbooleanArray array, int index, unsigned char val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetBooleanArrayRegion(env,array,index,1,&val);
}
void AndroidJNI::SetBooleanField(jobject obj, jfieldID fieldID, bool val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetBooleanField(env,obj,fieldID,val);
}
void AndroidJNI::SetByteArrayElement(jbyteArray array, int index, unsigned char val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetByteArrayRegion(env,array,index,1,&val);
}
void AndroidJNI::SetByteField(jobject obj, jfieldID fieldID, unsigned char val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetByteField(env,obj,fieldID,val);
}
void AndroidJNI::SetCharArrayElement(jcharArray array, int index, char val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetCharArrayRegion(env,array,index,1,(const jchar*)&val);
}
void AndroidJNI::SetCharField(jobject obj, jfieldID fieldID, char val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetCharField(env,obj,fieldID,val);
}
void AndroidJNI::SetDoubleArrayElement(jdoubleArray array, int index, double val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetDoubleArrayRegion(env,array,index,1,&val);
}
void AndroidJNI::SetDoubleField(jobject obj, jfieldID fieldID, double val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetDoubleField(env,obj,fieldID,val);
}
void AndroidJNI::SetFloatArrayElement(jfloatArray array, int index, float val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetFloatArrayRegion(env,array,index,1,&val);
}
void AndroidJNI::SetFloatField(jobject obj, jfieldID fieldID, float val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetFloatField(env,obj,fieldID,val);
}
void AndroidJNI::SetIntArrayElement(jintArray array, int index, int val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetIntArrayRegion(env,array,index,1,&val);
}
void AndroidJNI::SetIntField(jobject obj, jfieldID fieldID, int val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetIntField(env,obj,fieldID,val);
}
void AndroidJNI::SetLongArrayElement(jlongArray array, int index, long val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetLongArrayRegion(env,array,index,1,(const jlong*)&val);
}
void AndroidJNI::SetLongField(jobject obj, jfieldID fieldID, long val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetLongField(env,obj,fieldID,val);
}
void AndroidJNI::SetObjectArrayElement(jobjectArray array, int index, jobject obj)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetObjectArrayElement(env,array,index,obj);
}
void AndroidJNI::SetObjectField(jobject obj, jfieldID fieldID, jobject val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetObjectField(env,obj,fieldID,val);
}
void AndroidJNI::SetShortArrayElement(jshortArray array, int index, short val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetShortArrayRegion(env,array,index,1,&val);
}
void AndroidJNI::SetShortField(jobject obj, jfieldID fieldID, short val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetShortField(env,obj,fieldID,val);
}
void AndroidJNI::SetStaticBooleanField(jclass clazz, jfieldID fieldID, bool val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetStaticBooleanField(env,clazz,fieldID,val);
}
void AndroidJNI::SetStaticByteField(jclass clazz, jfieldID fieldID, unsigned char val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetStaticByteField(env,clazz,fieldID,val);
}
void AndroidJNI::SetStaticCharField(jclass clazz, jfieldID fieldID, char val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetStaticCharField(env,clazz,fieldID,val);
}
void AndroidJNI::SetStaticDoubleField(jclass clazz, jfieldID fieldID, double val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetStaticDoubleField(env,clazz,fieldID,val);
}
void AndroidJNI::SetStaticFloatField(jclass clazz, jfieldID fieldID, float val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetStaticFloatField(env,clazz,fieldID,val);
}
void AndroidJNI::SetStaticIntField(jclass clazz, jfieldID fieldID, int val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetStaticIntField(env,clazz,fieldID,val);
}
void AndroidJNI::SetStaticLongField(jclass clazz, jfieldID fieldID, long val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetStaticLongField(env,clazz,fieldID,val);
}
void AndroidJNI::SetStaticObjectField(jclass clazz, jfieldID fieldID, jobject val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetStaticObjectField(env,clazz,fieldID,val);
}
void AndroidJNI::SetStaticShortField(jclass clazz, jfieldID fieldID, short val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetStaticShortField(env,clazz,fieldID,val);
}
/*
void AndroidJNI::SetStaticStringField(jclass clazz, jfieldID fieldID, mono::string val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetStaticStringField(env,clazz,fieldID,val);
}
void AndroidJNI::SetStringField(jobject obj, jfieldID fieldID, mono::string val)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	env->functions->SetStringField(env,obj,fieldID,val);
}
*/

int AndroidJNI::Throw(jthrowable obj)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->Throw(env,obj);
}

int AndroidJNI::ThrowNew(jclass clazz, mono::string message)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->ThrowNew(env,clazz,ToMatrixString(message).c_str());
}

jbooleanArray AndroidJNI::ToBooleanArray(mono::object array)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArr = *array;
	jboolean* jarr = moArr->ToCppArray<jboolean>();

	size_t size	= moArr->GetSize();
	jbooleanArray jboolarr = env->functions->NewBooleanArray(env,size);
	env->functions->SetBooleanArrayRegion(env,jboolarr,0,size,jarr);
	moArr->Release();
	CC_SAFE_DELETE_ARRAY(jarr);
	return jboolarr;
}
jbyteArray AndroidJNI::ToByteArray(mono::object array)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArr = *array;
	jbyte* jarr = moArr->ToCppArray<jbyte>();

	size_t size	= moArr->GetSize();
	jbyteArray jbytearr = env->functions->NewByteArray(env,size);
	env->functions->SetByteArrayRegion(env,jbytearr,0,size,jarr);
	moArr->Release();
	CC_SAFE_DELETE_ARRAY(jarr);
	return jbytearr;
}
jcharArray AndroidJNI::ToCharArray(mono::object array)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArr = *array;
	jchar* jarr = moArr->ToCppArray<jchar>();

	size_t size	= moArr->GetSize();
	jcharArray jchararr = env->functions->NewCharArray(env,size);
	env->functions->SetCharArrayRegion(env,jchararr,0,size,jarr);
	moArr->Release();
	CC_SAFE_DELETE_ARRAY(jarr);
	return jchararr;
}
jdoubleArray AndroidJNI::ToDoubleArray(mono::object array)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArr = *array;
	jdouble* jarr = moArr->ToCppArray<jdouble>();

	size_t size	= moArr->GetSize();
	jdoubleArray jdoublearr = env->functions->NewDoubleArray(env,size);
	env->functions->SetDoubleArrayRegion(env,jdoublearr,0,size,jarr);
	moArr->Release();
	CC_SAFE_DELETE_ARRAY(jarr);
	return jdoublearr;
}
jfloatArray AndroidJNI::ToFloatArray(mono::object array)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArr = *array;
	jfloat* jarr = moArr->ToCppArray<jfloat>();

	size_t size	= moArr->GetSize();
	jfloatArray jfloatarr = env->functions->NewFloatArray(env,size);
	env->functions->SetFloatArrayRegion(env,jfloatarr,0,size,jarr);
	moArr->Release();
	CC_SAFE_DELETE_ARRAY(jarr);
	return jfloatarr;
}
jintArray AndroidJNI::ToIntArray(mono::object array)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArr = *array;
	jint* jarr = moArr->ToCppArray<jint>();

	size_t size	= moArr->GetSize();
	jintArray jintarr = env->functions->NewIntArray(env,size);
	env->functions->SetIntArrayRegion(env,jintarr,0,size,jarr);
	moArr->Release();
	CC_SAFE_DELETE_ARRAY(jarr);
	return jintarr;
}
jlongArray AndroidJNI::ToLongArray(mono::object array)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArr = *array;
	jlong* jarr = moArr->ToCppArray<jlong>();

	size_t size	= moArr->GetSize();
	jlongArray jlongarr = env->functions->NewLongArray(env,size);
	env->functions->SetLongArrayRegion(env,jlongarr,0,size,jarr);
	moArr->Release();
	CC_SAFE_DELETE_ARRAY(jarr);
	return jlongarr;
}
jobjectArray AndroidJNI::ToObjectArray(mono::object array)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArr = *array;
	jobject* jarr = moArr->ToCppArray<jobject>();

	jclass jcls=  env->functions->GetObjectClass(env,jarr[0]);
	size_t size	= moArr->GetSize();
	jobjectArray jobjctarr = env->functions->NewObjectArray(env,size,jcls,jarr[0]);
	for(int i = 0;i<size;i++)
	{
		env->functions->SetObjectArrayElement(env,jobjctarr,i,jarr[i]);
	}
	moArr->Release();
	CC_SAFE_DELETE_ARRAY(jarr);
	return jobjctarr;
}
jobject AndroidJNI::ToReflectedField(jclass clazz, jfieldID fieldID, bool isStatic)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->ToReflectedField(env,clazz,fieldID,isStatic);
}
jobject AndroidJNI::ToReflectedMethod(jclass clazz, jmethodID methodID, bool isStatic)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	return env->functions->ToReflectedMethod(env,clazz,methodID,isStatic);
}
jshortArray AndroidJNI::ToShortArray(mono::object array)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	IMonoArray* moArr = *array;
	jshort* jarr = moArr->ToCppArray<jshort>();

	size_t size	= moArr->GetSize();
	jshortArray jshortarr = env->functions->NewShortArray(env,size);
	env->functions->SetShortArrayRegion(env,jshortarr,0,size,jarr);
	moArr->Release();
	CC_SAFE_DELETE_ARRAY(jarr);
	return jshortarr;
}

#endif