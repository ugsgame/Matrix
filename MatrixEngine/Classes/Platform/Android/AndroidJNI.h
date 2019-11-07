
#ifndef __SCRIPTBIND_ANDROIDJNI__
#define __SCRIPTBIND_ANDROIDJNI__

#include "Native/ScriptBind_Matrix.h"


#ifdef ANDROID
#include <jni.h>

class AndroidJNI:public ScriptBind_Matrix
{
public:
	AndroidJNI();
	~AndroidJNI();

	virtual const char* GetNamespace(){ return "MatrixEngine.Platform.Android";}
	virtual const char*	GetClassName(){ return "AndroidJNI";}

protected:

	static  jobject AllocObject(jclass clazz);
 	static  int AttachCurrentThread();
 
 	static  bool CallBooleanMethod(jobject obj, jmethodID methodID, mono::object args);
 	static  unsigned char  CallByteMethod(jobject obj, jmethodID methodID, mono::object args);	
 	static  char CallCharMethod(jobject obj, jmethodID methodID, mono::object args);
 	static  double CallDoubleMethod(jobject obj, jmethodID methodID, mono::object args);	
 	static  float CallFloatMethod(jobject obj, jmethodID methodID, mono::object args);
 	static  int CallIntMethod(jobject obj, jmethodID methodID, mono::object args);
 	static  long CallLongMethod(jobject obj, jmethodID methodID, mono::object args);
 	static  jobject CallObjectMethod(jobject obj, jmethodID methodID, mono::object args);
 	static  short CallShortMethod(jobject obj, jmethodID methodID, mono::object args);
 	static  bool CallStaticBooleanMethod(jclass clazz, jmethodID methodID, mono::object args);
 	static  unsigned char CallStaticByteMethod(jclass clazz, jmethodID methodID, mono::object args);
 	static  char CallStaticCharMethod(jclass clazz, jmethodID methodID, mono::object args);
 	static  double CallStaticDoubleMethod(jclass clazz, jmethodID methodID, mono::object args);
 	static  float CallStaticFloatMethod(jclass clazz, jmethodID methodID, mono::object args);
 	static  int CallStaticIntMethod(jclass clazz, jmethodID methodID, mono::object args);
 	static  long CallStaticLongMethod(jclass clazz, jmethodID methodID, mono::object args);	
 	static  jobject CallStaticObjectMethod(jclass clazz, jmethodID methodID, mono::object args);
 	static  short CallStaticShortMethod(jclass clazz, jmethodID methodID, mono::object args);
 	static  mono::string CallStaticStringMethod(jclass clazz, jmethodID methodID, mono::object args);	
 	static  void CallStaticVoidMethod(jclass clazz, jmethodID methodID, mono::object args);
 	static  mono::string CallStringMethod(jobject obj, jmethodID methodID, mono::object args);
 	static  void CallVoidMethod(jobject obj, jmethodID methodID, mono::object args);

 	static  void DeleteGlobalRef(jobject obj);
 	static  void DeleteLocalRef(jobject obj);
 	static  int DetachCurrentThread();
 
 	static  int EnsureLocalCapacity(int capacity);
 
 	static  void ExceptionClear();
 	static  void ExceptionDescribe();
	static  jthrowable ExceptionOccurred();
 
 	static  void FatalError(mono::string message);
 	static  jclass FindClass(mono::string name);
// 
// 	static  bool[] FromBooleanArray(jbooleanArray array);
// 	static  byte[] FromByteArray(IntPtr array);
// 	static  char[] FromCharArray(IntPtr array);
// 	static  double[] FromDoubleArray(IntPtr array);
// 	static  float[] FromFloatArray(IntPtr array);
// 	static  int[] FromIntArray(IntPtr array);
// 	static  long[] FromLongArray(IntPtr array);
// 	static  IntPtr[] FromObjectArray(IntPtr array);
// 	static  IntPtr FromReflectedField(IntPtr refField);
// 	static  IntPtr FromReflectedMethod(IntPtr refMethod);
// 	static  short[] FromShortArray(IntPtr array);
 
 	static  int GetArrayLength(jarray array);
 	static  bool GetBooleanArrayElement(jbooleanArray array, int index);	
 	static  bool GetBooleanField(jobject obj, jfieldID fieldID);
 	static  unsigned char GetByteArrayElement(jbyteArray array, int index);
 	static  unsigned char GetByteField(jobject obj, jfieldID fieldID);
 	static  char GetCharArrayElement(jcharArray array, int index);
 	static  char GetCharField(jobject obj, jfieldID fieldID);
 	static  double GetDoubleArrayElement(jdoubleArray array, int index);
 	static  double GetDoubleField(jobject obj, jfieldID fieldID);
 	static  jfieldID GetFieldID(jclass clazz, mono::string name, mono::string sig);
 	static  float GetFloatArrayElement(jfloatArray array, int index);
 	static  float GetFloatField(jobject obj, jfieldID fieldID);
 	static  int GetIntArrayElement(jintArray array, int index);
 	static  int GetIntField(jobject obj, jfieldID fieldID);
 	static  long GetLongArrayElement(jlongArray array, int index);
 	static  long GetLongField(jobject obj, jfieldID fieldID);
 	static  jmethodID GetMethodID(jclass clazz, mono::string name, mono::string sig);
 	static  jobject GetObjectArrayElement(jobjectArray array, int index);
 	static  jclass GetObjectClass(jobject obj);
 	static  jobject GetObjectField(jobject obj, jfieldID fieldID);
 	static  short GetShortArrayElement(jshortArray array, int index);
 	static  short GetShortField(jobject obj, jfieldID fieldID);	
 	static  bool GetStaticBooleanField(jclass clazz, jfieldID fieldID);	
 	static  unsigned char GetStaticByteField(jclass clazz, jfieldID fieldID);
 	static  char GetStaticCharField(jclass clazz, jfieldID fieldID);
 	static  double GetStaticDoubleField(jclass clazz, jfieldID fieldID);
 	static  jfieldID GetStaticFieldID(jclass clazz, mono::string name, mono::string sig);
 	static  float GetStaticFloatField(jclass clazz, jfieldID fieldID);
 	static  int GetStaticIntField(jclass clazz, jfieldID fieldID);
 	static  long GetStaticLongField(jclass clazz, jfieldID fieldID);
 	static  jmethodID GetStaticMethodID(jclass clazz, mono::string name, mono::string sig);
 	static  jobject GetStaticObjectField(jclass clazz, jfieldID fieldID);
 	static  short GetStaticShortField(jclass clazz, jfieldID fieldID);
//TODO:用 GetStaticObjectField 来实现
// 	static  mono::string GetStaticStringField(jclass clazz, jfieldID fieldID);
// 	static  mono::string GetStringField(jobject obj, jfieldID fieldID);
 	static  mono::string GetStringUTFChars(jstring str);
 	static  int GetStringUTFLength(jstring str);	
 	static  jclass GetSuperclass(jclass clazz);
 	static  int GetVersion();
 
 	static  bool IsAssignableFrom(jclass clazz1, jclass clazz2);
 	static  bool IsInstanceOf(jobject obj, jclass clazz);
 	static  bool IsSameObject(jobject obj1, jobject obj2);
 
	static  jbooleanArray NewBooleanArray(int size);
	static  jbyteArray NewByteArray(int size);
	static  jcharArray NewCharArray(int size);
	static  jdoubleArray NewDoubleArray(int size);	
	static  jfloatArray NewFloatArray(int size);
	static  jobject NewGlobalRef(jobject obj);
	static  jintArray NewIntArray(int size);
	static  jobject NewLocalRef(jobject obj);
	static  jlongArray NewLongArray(int size);
	static  jobject NewObject(jclass clazz, jmethodID methodID, mono::object args);
	static  jobjectArray NewObjectArray(int size, jclass clazz, jobject obj);
	static  jshortArray NewShortArray(int size);	
 	static  jstring NewStringUTF(mono::string str);
 
	static  jobject PopLocalFrame(jobject result);
	static  int PushLocalFrame(int capacity);
 
	static  void SetBooleanArrayElement(jbooleanArray array, int index, unsigned char val);
	static  void SetBooleanField(jobject obj, jfieldID fieldID, bool val);
	static  void SetByteArrayElement(jbyteArray array, int index, unsigned char val);
	static  void SetByteField(jobject obj, jfieldID fieldID, unsigned char val);
	static  void SetCharArrayElement(jcharArray array, int index, char val);
	static  void SetCharField(jobject obj, jfieldID fieldID, char val);
	static  void SetDoubleArrayElement(jdoubleArray array, int index, double val);
	static  void SetDoubleField(jobject obj, jfieldID fieldID, double val);
	static  void SetFloatArrayElement(jfloatArray array, int index, float val);
	static  void SetFloatField(jobject obj, jfieldID fieldID, float val);
	static  void SetIntArrayElement(jintArray array, int index, int val);
	static  void SetIntField(jobject obj, jfieldID fieldID, int val);
	static  void SetLongArrayElement(jlongArray array, int index, long val);
	static  void SetLongField(jobject obj, jfieldID fieldID, long val);
	static  void SetObjectArrayElement(jobjectArray array, int index, jobject obj);
	static  void SetObjectField(jobject obj, jfieldID fieldID, jobject val);
	static  void SetShortArrayElement(jshortArray array, int index, short val);
	static  void SetShortField(jobject obj, jfieldID fieldID, short val);
	static  void SetStaticBooleanField(jclass clazz, jfieldID fieldID, bool val);
	static  void SetStaticByteField(jclass clazz, jfieldID fieldID, unsigned char val);
	static  void SetStaticCharField(jclass clazz, jfieldID fieldID, char val);
	static  void SetStaticDoubleField(jclass clazz, jfieldID fieldID, double val);
	static  void SetStaticFloatField(jclass clazz, jfieldID fieldID, float val);	
	static  void SetStaticIntField(jclass clazz, jfieldID fieldID, int val);
	static  void SetStaticLongField(jclass clazz, jfieldID fieldID, long val);
	static  void SetStaticObjectField(jclass clazz, jfieldID fieldID, jobject val);
	static  void SetStaticShortField(jclass clazz, jfieldID fieldID, short val);
//  TODO:用 SetStaticObjectField 来实现
//	static  void SetStaticStringField(jclass clazz, jfieldID fieldID, mono::string val);
//	static  void SetStringField(jobject obj, jfieldID fieldID, mono::string val);

 
	static  int Throw(jthrowable obj);
	static  int ThrowNew(jclass clazz, mono::string message);
 
	static  jbooleanArray ToBooleanArray(mono::object array);
	static  jbyteArray ToByteArray(mono::object array);
	static  jcharArray ToCharArray(mono::object array);
	static  jdoubleArray ToDoubleArray(mono::object array);
	static  jfloatArray ToFloatArray(mono::object array);
	static  jintArray ToIntArray(mono::object array);
	static  jlongArray ToLongArray(mono::object array);
	static  jobjectArray ToObjectArray(mono::object array);
	static  jobject ToReflectedField(jclass clazz, jfieldID fieldID, bool isStatic);
	static  jobject ToReflectedMethod(jclass clazz, jmethodID methodID, bool isStatic);
	static  jshortArray ToShortArray(mono::object array);

private:
};

#endif //

#endif