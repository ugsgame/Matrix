using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MatrixEngine.Platform.Android
{
    public class AndroidJavaObject : AndroidJNIHelper
    {
        protected IntPtr m_jobject;
        protected IntPtr m_jclass;
        protected Dictionary<int, IntPtr> m_methodIDs;
        protected Dictionary<int, IntPtr> m_fieldIDs;

        public AndroidJavaObject(IntPtr jobject)
            : this()
        {
            if (jobject == IntPtr.Zero)
            {
                Debug.LogWaring("JNI: Init'd AndroidJavaObject with null ptr!");
                return;
            }
            this.m_jobject = AndroidJNI.NewGlobalRef(jobject);
            this.m_jclass = AndroidJNI.NewGlobalRef(AndroidJNI.GetObjectClass(this.m_jobject));
        }
        protected AndroidJavaObject()
        {
            this.m_methodIDs = new Dictionary<int, IntPtr>();
            this.m_fieldIDs = new Dictionary<int, IntPtr>();
        }
        public AndroidJavaObject(string className, params object[] args)
            : this()
        {
            this._AndroidJavaObject(className, args);
        }
        private void _AndroidJavaObject(string className, params object[] args)
        {
            if (args == null)
            {
                args = new object[1];
            }
            className = className.Replace('.', '/');
            IntPtr intPtr = AndroidJNI.FindClass(className);
            this.m_jclass = AndroidJNI.NewGlobalRef(intPtr);
            IntPtr constructorID = AndroidJNIHelper.GetConstructorID(intPtr, args);
            jvalue[] args2 = AndroidJNIHelper.CreateJNIArgArray(args);
            IntPtr obj = AndroidJNI.NewObject(intPtr, constructorID, args2);
            this.m_jobject = AndroidJNI.NewGlobalRef(obj);
        }

        public IntPtr GetRawObject()
        {
            return this.m_jobject;
        }

        public IntPtr GetRawClass()
        {
            return this.m_jclass;
        }
        public void Call(string methodName, params object[] args)
        {
            this._Call(methodName, false, args);
        }

        public ReturnType Call<ReturnType>(string methodName, params object[] args)
        {
            return this._Call<ReturnType>(methodName, false, args);
        }


        public void CallStatic(string methodName, params object[] args)
        {
            this._Call(methodName, true, args);
        }

        public ReturnType CallStatic<ReturnType>(string methodName, params object[] args)
        {
            return this._Call<ReturnType>(methodName, true, args);
        }

        protected void _Call(string methodName, bool isStatic, params object[] args)
        {
            if (args == null)
            {
                args = new object[1];
            }
            string signature = AndroidJNIHelper.GetSignature(args);
            Debug.LogWaring("Call<void>" + methodName + signature + args);
            IntPtr cachedMethodID = AndroidJNIHelper.GetMethodID(this.m_jclass, methodName, args, isStatic);
            jvalue[] args2 = AndroidJNIHelper.CreateJNIArgArray(args);
            if (isStatic)
            {
                AndroidJNI.CallStaticVoidMethod(this.m_jobject, cachedMethodID, args2);
            }
            else
            {
                AndroidJNI.CallVoidMethod(this.m_jobject, cachedMethodID, args2);
            }
        }

        protected ReturnType _Call<ReturnType>(string methodName, bool isStatic, params object[] args)
        {
            if (args == null)
            {
                args = new object[1];
            }
            string signature = AndroidJNIHelper.GetSignature<ReturnType>(args);
            Debug.LogWaring("Call<" + typeof(ReturnType).ToString() + ">" + methodName + signature + args);
            IntPtr cachedMethodID = AndroidJNIHelper.GetMethodID<ReturnType>(this.m_jclass, methodName, args, isStatic);
            jvalue[] args2 = AndroidJNIHelper.CreateJNIArgArray(args);
            if (typeof(ReturnType).IsPrimitive)
            {
                if (isStatic)
                {
                    if (typeof(ReturnType) == typeof(int))
                    {
                        object rt = AndroidJNI.CallStaticIntMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(bool))
                    {
                        object rt = AndroidJNI.CallStaticBooleanMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(byte))
                    {
                        object rt = AndroidJNI.CallStaticByteMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(short))
                    {
                        object rt = AndroidJNI.CallStaticShortMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(long))
                    {
                        object rt = AndroidJNI.CallStaticLongMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(float))
                    {
                        object rt = AndroidJNI.CallStaticFloatMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(double))
                    {
                        object rt = AndroidJNI.CallStaticDoubleMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(char))
                    {
                        object rt = AndroidJNI.CallStaticCharMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                }
                else
                {
                    if (typeof(ReturnType) == typeof(int))
                    {
                        object rt = AndroidJNI.CallIntMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(bool))
                    {
                        object rt = AndroidJNI.CallBooleanMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(byte))
                    {
                        object rt = AndroidJNI.CallByteMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(short))
                    {
                        object rt = AndroidJNI.CallShortMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(long))
                    {
                        object rt = AndroidJNI.CallLongMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(float))
                    {
                        object rt = AndroidJNI.CallFloatMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(double))
                    {
                        object rt = AndroidJNI.CallDoubleMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(char))
                    {
                        object rt = AndroidJNI.CallCharMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                }
            }
            else
            {
                if (isStatic)
                {
                    if (typeof(ReturnType) == typeof(string))
                    {
                        object rt = AndroidJNI.CallStaticStringMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(AndroidJavaObject))
                    {
                        IntPtr jobject = AndroidJNI.CallStaticObjectMethod(this.m_jobject, cachedMethodID, args2);
                        object rt = new AndroidJavaObject(jobject);
                        return (ReturnType)rt;
                    }
                    //                 if (typeof(Array).IsAssignableFrom(typeof(ReturnType)))
                    //                 {
                    //                     IntPtr array = AndroidJNI.CallObjectMethod(this.m_jobject, cachedMethodID, args2);
                    //                     return (ReturnType)AndroidJNIHelper.ConvertFromJNIArray<ReturnType>(array);
                    //                 }
                    Debug.LogWaring("JNI: Unknown return type '" + typeof(ReturnType) + "'");
                }
                else
                {
                    if (typeof(ReturnType) == typeof(string))
                    {
                        object rt = AndroidJNI.CallStringMethod(this.m_jobject, cachedMethodID, args2);
                        return (ReturnType)rt;
                    }
                    if (typeof(ReturnType) == typeof(AndroidJavaObject))
                    {
                        IntPtr jobject = AndroidJNI.CallObjectMethod(this.m_jobject, cachedMethodID, args2);
                        object rt = new AndroidJavaObject(jobject);
                        return (ReturnType)rt;
                    }
                    //                 if (typeof(Array).IsAssignableFrom(typeof(ReturnType)))
                    //                 {
                    //                     IntPtr array = AndroidJNI.CallObjectMethod(this.m_jobject, cachedMethodID, args2);
                    //                     return (ReturnType)AndroidJNIHelper.ConvertFromJNIArray<ReturnType>(array);
                    //                 }
                    Debug.LogWaring("JNI: Unknown return type '" + typeof(ReturnType) + "'");
                }
            }
            return default(ReturnType);
        }

    }
}
