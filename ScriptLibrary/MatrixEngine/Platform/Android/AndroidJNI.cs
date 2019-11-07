using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using System.Runtime.CompilerServices;

namespace MatrixEngine.Platform.Android
{
    public sealed class AndroidJNI
    {
        //
        // Static Methods
        //
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr AllocObject(IntPtr clazz);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int AttachCurrentThread();

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern bool CallBooleanMethod(IntPtr obj, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern byte CallByteMethod(IntPtr obj, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern char CallCharMethod(IntPtr obj, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern double CallDoubleMethod(IntPtr obj, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern float CallFloatMethod(IntPtr obj, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int CallIntMethod(IntPtr obj, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern long CallLongMethod(IntPtr obj, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr CallObjectMethod(IntPtr obj, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern short CallShortMethod(IntPtr obj, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern bool CallStaticBooleanMethod(IntPtr clazz, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern byte CallStaticByteMethod(IntPtr clazz, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern char CallStaticCharMethod(IntPtr clazz, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern double CallStaticDoubleMethod(IntPtr clazz, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern float CallStaticFloatMethod(IntPtr clazz, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int CallStaticIntMethod(IntPtr clazz, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern long CallStaticLongMethod(IntPtr clazz, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr CallStaticObjectMethod(IntPtr clazz, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern short CallStaticShortMethod(IntPtr clazz, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern string CallStaticStringMethod(IntPtr clazz, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void CallStaticVoidMethod(IntPtr clazz, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern string CallStringMethod(IntPtr obj, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void CallVoidMethod(IntPtr obj, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void DeleteGlobalRef(IntPtr obj);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void DeleteLocalRef(IntPtr obj);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int DetachCurrentThread();

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int EnsureLocalCapacity(int capacity);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void ExceptionClear();

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void ExceptionDescribe();

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr ExceptionOccurred();

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void FatalError(string message);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr FindClass(string name);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern bool[] FromBooleanArray(IntPtr array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern byte[] FromByteArray(IntPtr array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern char[] FromCharArray(IntPtr array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern double[] FromDoubleArray(IntPtr array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern float[] FromFloatArray(IntPtr array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int[] FromIntArray(IntPtr array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern long[] FromLongArray(IntPtr array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr[] FromObjectArray(IntPtr array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr FromReflectedField(IntPtr refField);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr FromReflectedMethod(IntPtr refMethod);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern short[] FromShortArray(IntPtr array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int GetArrayLength(IntPtr array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern bool GetBooleanArrayElement(IntPtr array, int index);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern bool GetBooleanField(IntPtr obj, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern byte GetByteArrayElement(IntPtr array, int index);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern byte GetByteField(IntPtr obj, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern char GetCharArrayElement(IntPtr array, int index);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern char GetCharField(IntPtr obj, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern double GetDoubleArrayElement(IntPtr array, int index);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern double GetDoubleField(IntPtr obj, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr GetFieldID(IntPtr clazz, string name, string sig);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern float GetFloatArrayElement(IntPtr array, int index);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern float GetFloatField(IntPtr obj, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int GetIntArrayElement(IntPtr array, int index);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int GetIntField(IntPtr obj, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern long GetLongArrayElement(IntPtr array, int index);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern long GetLongField(IntPtr obj, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr GetMethodID(IntPtr clazz, string name, string sig);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr GetObjectArrayElement(IntPtr array, int index);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr GetObjectClass(IntPtr obj);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr GetObjectField(IntPtr obj, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern short GetShortArrayElement(IntPtr array, int index);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern short GetShortField(IntPtr obj, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern bool GetStaticBooleanField(IntPtr clazz, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern byte GetStaticByteField(IntPtr clazz, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern char GetStaticCharField(IntPtr clazz, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern double GetStaticDoubleField(IntPtr clazz, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr GetStaticFieldID(IntPtr clazz, string name, string sig);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern float GetStaticFloatField(IntPtr clazz, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int GetStaticIntField(IntPtr clazz, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern long GetStaticLongField(IntPtr clazz, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr GetStaticMethodID(IntPtr clazz, string name, string sig);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr GetStaticObjectField(IntPtr clazz, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern short GetStaticShortField(IntPtr clazz, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern string GetStaticStringField(IntPtr clazz, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern string GetStringField(IntPtr obj, IntPtr fieldID);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern string GetStringUTFChars(IntPtr str);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int GetStringUTFLength(IntPtr str);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr GetSuperclass(IntPtr clazz);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int GetVersion();

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern bool IsAssignableFrom(IntPtr clazz1, IntPtr clazz2);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern bool IsInstanceOf(IntPtr obj, IntPtr clazz);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern bool IsSameObject(IntPtr obj1, IntPtr obj2);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewBooleanArray(int size);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewByteArray(int size);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewCharArray(int size);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewDoubleArray(int size);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewFloatArray(int size);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewGlobalRef(IntPtr obj);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewIntArray(int size);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewLocalRef(IntPtr obj);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewLongArray(int size);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewObject(IntPtr clazz, IntPtr methodID, jvalue[] args);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewObjectArray(int size, IntPtr clazz, IntPtr obj);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewShortArray(int size);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr NewStringUTF(string bytes);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr PopLocalFrame(IntPtr result);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int PushLocalFrame(int capacity);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetBooleanArrayElement(IntPtr array, int index, byte val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetBooleanField(IntPtr obj, IntPtr fieldID, bool val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetByteArrayElement(IntPtr array, int index, sbyte val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetByteField(IntPtr obj, IntPtr fieldID, byte val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetCharArrayElement(IntPtr array, int index, char val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetCharField(IntPtr obj, IntPtr fieldID, char val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetDoubleArrayElement(IntPtr array, int index, double val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetDoubleField(IntPtr obj, IntPtr fieldID, double val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetFloatArrayElement(IntPtr array, int index, float val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetFloatField(IntPtr obj, IntPtr fieldID, float val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetIntArrayElement(IntPtr array, int index, int val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetIntField(IntPtr obj, IntPtr fieldID, int val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetLongArrayElement(IntPtr array, int index, long val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetLongField(IntPtr obj, IntPtr fieldID, long val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetObjectArrayElement(IntPtr array, int index, IntPtr obj);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetObjectField(IntPtr obj, IntPtr fieldID, IntPtr val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetShortArrayElement(IntPtr array, int index, short val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetShortField(IntPtr obj, IntPtr fieldID, short val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetStaticBooleanField(IntPtr clazz, IntPtr fieldID, bool val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetStaticByteField(IntPtr clazz, IntPtr fieldID, byte val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetStaticCharField(IntPtr clazz, IntPtr fieldID, char val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetStaticDoubleField(IntPtr clazz, IntPtr fieldID, double val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetStaticFloatField(IntPtr clazz, IntPtr fieldID, float val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetStaticIntField(IntPtr clazz, IntPtr fieldID, int val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetStaticLongField(IntPtr clazz, IntPtr fieldID, long val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetStaticObjectField(IntPtr clazz, IntPtr fieldID, IntPtr val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetStaticShortField(IntPtr clazz, IntPtr fieldID, short val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetStaticStringField(IntPtr clazz, IntPtr fieldID, string val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern void SetStringField(IntPtr obj, IntPtr fieldID, string val);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int Throw(IntPtr obj);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern int ThrowNew(IntPtr clazz, string message);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr ToBooleanArray(bool[] array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr ToByteArray(byte[] array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr ToCharArray(char[] array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr ToDoubleArray(double[] array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr ToFloatArray(float[] array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr ToIntArray(int[] array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr ToLongArray(long[] array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr ToObjectArray(IntPtr[] array);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr ToReflectedField(IntPtr clazz, IntPtr fieldID, bool isStatic);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr ToReflectedMethod(IntPtr clazz, IntPtr methodID, bool isStatic);

  
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr ToShortArray(short[] array);
    }
}
