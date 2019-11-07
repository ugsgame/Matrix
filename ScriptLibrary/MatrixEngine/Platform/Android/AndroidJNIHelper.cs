using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Text;

namespace MatrixEngine.Platform.Android
{
    [StructLayout(LayoutKind.Explicit)]
    public struct jvalue
    {
        //
        // Fields
        //
        [FieldOffset(0)]
        public float f;

        [FieldOffset(0)]
        public long j;

        [FieldOffset(0)]
        public IntPtr l;

        [FieldOffset(0)]
        public double d;

        [FieldOffset(0)]
        public int i;

        [FieldOffset(0)]
        public byte b;

        [FieldOffset(0)]
        public bool z;

        [FieldOffset(0)]
        public short s;

        [FieldOffset(0)]
        public char c;
    }

    public class AndroidJNIHelper
    {
        public static jvalue[] CreateJNIArgArray(object[] args)
        {
            return _AndroidJNIHelper.CreateJNIArgArray(args);
        }
        public static IntPtr ConvertToJNIArray(Array array)
        {
            return _AndroidJNIHelper.ConvertToJNIArray(array);
        }

        public static IntPtr GetConstructorID(IntPtr jclass, object[] args)
        {
            return _AndroidJNIHelper.GetConstructorID(jclass, args);
        }

        public static string GetSignature(object obj)
        {
            return _AndroidJNIHelper.GetSignature(obj);
        }
        public static string GetSignature(object[] args)
        {
            return _AndroidJNIHelper.GetSignature(args);
        }

        public static string GetSignature<ReturnType>(object[] args)
        {
            return _AndroidJNIHelper.GetSignature<ReturnType>(args);
        }


        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr GetConstructorID(IntPtr javaClass, string signature);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        public static extern IntPtr GetMethodID(IntPtr javaClass, string methodName, string signature, bool isStatic);

        public static IntPtr GetMethodID(IntPtr javaClass, string methodName, string signature)
        {
            bool isStatic = false;
            return AndroidJNIHelper.GetMethodID(javaClass, methodName, signature, isStatic);
        }
        public static IntPtr GetMethodID(IntPtr javaClass, string methodName)
        {
            bool isStatic = false;
            string empty = string.Empty;
            return AndroidJNIHelper.GetMethodID(javaClass, methodName, empty, isStatic);
        }

        public static IntPtr GetMethodID(IntPtr jclass, string methodName, object[] args, bool isStatic)
        {
            return _AndroidJNIHelper.GetMethodID(jclass, methodName, args, isStatic);
        }


        public static IntPtr GetMethodID<ReturnType>(IntPtr jclass, string methodName, object[] args, bool isStatic)
        {
            return _AndroidJNIHelper.GetMethodID<ReturnType>(jclass, methodName, args, isStatic);
        }

    }

    public class _AndroidJNIHelper
    {
        public static jvalue[] CreateJNIArgArray(object[] args)
        {
            jvalue[] array = new jvalue[args.GetLength(0)];
            int num = 0;
            for (int i = 0; i < args.Length; i++)
            {
                object obj = args[i];
                if (obj == null)
                {
                    array[num].l = IntPtr.Zero;
                }
                else
                {
                    if (obj.GetType().IsPrimitive)
                    {
                        if (obj is int)
                        {
                            array[num].i = (int)obj;
                        }
                        else
                        {
                            if (obj is bool)
                            {
                                array[num].z = (bool)obj;
                            }
                            else
                            {
                                if (obj is byte)
                                {
                                    array[num].b = (byte)obj;
                                }
                                else
                                {
                                    if (obj is short)
                                    {
                                        array[num].s = (short)obj;
                                    }
                                    else
                                    {
                                        if (obj is long)
                                        {
                                            array[num].j = (long)obj;
                                        }
                                        else
                                        {
                                            if (obj is float)
                                            {
                                                array[num].f = (float)obj;
                                            }
                                            else
                                            {
                                                if (obj is double)
                                                {
                                                    array[num].d = (double)obj;
                                                }
                                                else
                                                {
                                                    if (obj is char)
                                                    {
                                                        array[num].c = (char)obj;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if (obj is string)
                        {
                            array[num].l = AndroidJNI.NewStringUTF((string)obj);
                        }
                        else
                        {
                            if (obj is AndroidJavaObject)
                            {
                                array[num].l = ((AndroidJavaObject)obj).GetRawObject();
                            }
                            else
                            {
                                if (obj is Array)
                                {
                                    array[num].l = _AndroidJNIHelper.ConvertToJNIArray((Array)obj);
                                }
                                else
                                {
                                    //                                     if (obj is AndroidJavaRunnable)
                                    //                                     {
                                    //                                         array[num].l = AndroidJNIHelper.CreateJavaRunnable((AndroidJavaRunnable)obj);
                                    //                                     }
                                    //                                     else
                                    {
                                        Debug.LogWaring("JNI: Unknown argument type '" + obj.GetType() + "'");
                                    }
                                }
                            }
                        }
                    }
                }
                num++;
            }
            return array;
        }


        public static IntPtr ConvertToJNIArray(Array array)
        {
            Type elementType = array.GetType().GetElementType();
            if (elementType.IsPrimitive)
            {
                if (elementType == typeof(int))
                {
                    return AndroidJNI.ToIntArray((int[])array);
                }
                if (elementType == typeof(bool))
                {
                    return AndroidJNI.ToBooleanArray((bool[])array);
                }
                if (elementType == typeof(byte))
                {
                    return AndroidJNI.ToByteArray((byte[])array);
                }
                if (elementType == typeof(short))
                {
                    return AndroidJNI.ToShortArray((short[])array);
                }
                if (elementType == typeof(long))
                {
                    return AndroidJNI.ToLongArray((long[])array);
                }
                if (elementType == typeof(float))
                {
                    return AndroidJNI.ToFloatArray((float[])array);
                }
                if (elementType == typeof(double))
                {
                    return AndroidJNI.ToDoubleArray((double[])array);
                }
                if (elementType == typeof(char))
                {
                    return AndroidJNI.ToCharArray((char[])array);
                }
            }
            else
            {
                if (elementType == typeof(string))
                {
                    string[] array2 = (string[])array;
                    int length = array.GetLength(0);
                    IntPtr[] array3 = new IntPtr[length];
                    for (int i = 0; i < length; i++)
                    {
                        array3[i] = AndroidJNI.NewStringUTF(array2[i]);
                    }
                    return AndroidJNI.ToObjectArray(array3);
                }
                if (elementType == typeof(AndroidJavaObject))
                {
                    AndroidJavaObject[] array4 = (AndroidJavaObject[])array;
                    int length2 = array.GetLength(0);
                    IntPtr[] array5 = new IntPtr[length2];
                    for (int j = 0; j < length2; j++)
                    {
                        array5[j] = ((array4[j] != null) ? array4[j].GetRawObject() : IntPtr.Zero);
                    }
                    return AndroidJNI.ToObjectArray(array5);
                }
                Debug.LogWaring("JNI: Unknown array type '" + elementType + "'");
            }
            return IntPtr.Zero;
        }

        public static IntPtr GetConstructorID(IntPtr jclass, object[] args)
        {
            string signature = _AndroidJNIHelper.GetSignature(args);
            IntPtr constructorID = AndroidJNIHelper.GetConstructorID(jclass, signature);
            if (constructorID == IntPtr.Zero)
            {
                Debug.LogWaring("JNI: Unable to find constructor method id with signature '" + signature + "'");
            }
            return constructorID;
        }

        public static string GetSignature(object[] args)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.Append('(');
            for (int i = 0; i < args.Length; i++)
            {
                object obj = args[i];
                stringBuilder.Append(_AndroidJNIHelper.GetSignature(obj));
            }
            stringBuilder.Append(")V");
            return stringBuilder.ToString();
        }

        public static string GetSignature(object obj)
        {
            if (obj == null)
            {
                return "Ljava/lang/Object;";
            }
            Type type = (!(obj is Type)) ? obj.GetType() : ((Type)obj);
            if (type.IsPrimitive)
            {
                if (type.Equals(typeof(int)))
                {
                    return "I";
                }
                if (type.Equals(typeof(bool)))
                {
                    return "Z";
                }
                if (type.Equals(typeof(byte)))
                {
                    return "B";
                }
                if (type.Equals(typeof(short)))
                {
                    return "S";
                }
                if (type.Equals(typeof(long)))
                {
                    return "J";
                }
                if (type.Equals(typeof(float)))
                {
                    return "F";
                }
                if (type.Equals(typeof(double)))
                {
                    return "D";
                }
                if (type.Equals(typeof(char)))
                {
                    return "C";
                }
            }
            else
            {
                if (type.Equals(typeof(string)))
                {
                    return "Ljava/lang/String;";
                }
                //                 if (type.Equals(typeof(AndroidJavaRunnable)))
                //                 {
                //                     return "Ljava/lang/Runnable;";
                //                 }
                if (type.Equals(typeof(AndroidJavaObject)))
                {
                    if (obj == type)
                    {
                        return "Ljava/lang/Object;";
                    }
                    AndroidJavaObject androidJavaObject = (AndroidJavaObject)obj;
                    AndroidJavaObject androidJavaObject2;

                    object[] args = new object[0];
                    IntPtr cachedMethodID = AndroidJNIHelper.GetMethodID<AndroidJavaObject>(androidJavaObject.GetRawClass(), "getClass", args, false);
                    jvalue[] args2 = AndroidJNIHelper.CreateJNIArgArray(args);
                    IntPtr pobj = AndroidJNI.CallObjectMethod(androidJavaObject.GetRawObject(), cachedMethodID, args2);
                    androidJavaObject2 = new AndroidJavaObject(pobj);


                    string retStr;
                    IntPtr cachedMethodID2 = AndroidJNIHelper.GetMethodID<AndroidJavaObject>(androidJavaObject.GetRawClass(), "getName", args, false);

                    retStr = AndroidJNI.CallStringMethod(androidJavaObject2.GetRawObject(), cachedMethodID2, args2);
                    return "L" + retStr + ";";
                }
                else
                {
                    if (typeof(Array).IsAssignableFrom(type))
                    {
                        if (type.GetArrayRank() != 1)
                        {
                            Debug.LogWaring("JNI: System.Array in n dimensions is not allowed");
                            return string.Empty;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.Append('[');
                        stringBuilder.Append(_AndroidJNIHelper.GetSignature(type.GetElementType()));
                        return stringBuilder.ToString();
                    }
                    else
                    {
                        Debug.LogWaring(string.Concat(new object[]
						{
							"JNI: Unknown signature for type '",
							type,
							"' (obj = ",
							obj,
							") ",
							(type != obj) ? "instance" : "equal"
						}));
                    }
                }
            }
            return string.Empty;
        }

        public static string GetSignature<ReturnType>(object[] args)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.Append('(');
            for (int i = 0; i < args.Length; i++)
            {
                object obj = args[i];
                stringBuilder.Append(_AndroidJNIHelper.GetSignature(obj));
            }
            stringBuilder.Append(')');
            stringBuilder.Append(_AndroidJNIHelper.GetSignature(typeof(ReturnType)));
            return stringBuilder.ToString();
        }
        public static IntPtr GetMethodID(IntPtr jclass, string methodName, object[] args, bool isStatic)
        {
            string signature = _AndroidJNIHelper.GetSignature(args);
            IntPtr methodID = AndroidJNIHelper.GetMethodID(jclass, methodName, signature, isStatic);
            if (methodID == IntPtr.Zero)
            {
                Debug.LogWaring("JNI: Unable to find method id for '" + methodName + "'" + ((!isStatic) ? string.Empty : " (static)"));
                // AndroidJNI.ExceptionClear();
            }
            return methodID;
        }

        public static IntPtr GetMethodID<ReturnType>(IntPtr jclass, string methodName, object[] args, bool isStatic)
        {
            string signature = _AndroidJNIHelper.GetSignature<ReturnType>(args);
            IntPtr methodID = AndroidJNIHelper.GetMethodID(jclass, methodName, signature, isStatic);
            if (methodID == IntPtr.Zero)
            {
                Debug.LogWaring("JNI: Unable to find method id for '" + methodName + "'" + ((!isStatic) ? string.Empty : " (static)"));
                //AndroidJNI.ExceptionClear();
            }
            return methodID;
        }


        //         public static ArrayType ConvertFromJNIArray<ArrayType>(IntPtr array)
        //         {
        //             Type elementType = typeof(ArrayType).GetElementType();
        //             if (elementType.IsPrimitive)
        //             {
        //                 if (elementType == typeof(int))
        //                 {
        //                     return (ArrayType)AndroidJNI.FromIntArray(array);
        //                 }
        //                 if (elementType == typeof(bool))
        //                 {
        //                     return (ArrayType)AndroidJNI.FromBooleanArray(array);
        //                 }
        //                 if (elementType == typeof(byte))
        //                 {
        //                     return (ArrayType)AndroidJNI.FromByteArray(array);
        //                 }
        //                 if (elementType == typeof(short))
        //                 {
        //                     return (ArrayType)AndroidJNI.FromShortArray(array);
        //                 }
        //                 if (elementType == typeof(long))
        //                 {
        //                     return (ArrayType)AndroidJNI.FromLongArray(array);
        //                 }
        //                 if (elementType == typeof(float))
        //                 {
        //                     return (ArrayType)AndroidJNI.FromFloatArray(array);
        //                 }
        //                 if (elementType == typeof(double))
        //                 {
        //                     return (ArrayType)AndroidJNI.FromDoubleArray(array);
        //                 }
        //                 if (elementType == typeof(char))
        //                 {
        //                     return (ArrayType)AndroidJNI.FromCharArray(array);
        //                 }
        //             }
        //             else
        //             {
        //                 if (elementType == typeof(string))
        //                 {
        //                     IntPtr[] array2 = AndroidJNI.FromObjectArray(array);
        //                     int length = array2.GetLength(0);
        //                     string[] array3 = new string[length];
        //                     for (int i = 0; i < length; i++)
        //                     {
        //                         array3[i] = AndroidJNI.GetStringUTFChars(array2[i]);
        //                     }
        //                     return (ArrayType)array3;
        //                 }
        //                 if (elementType == typeof(AndroidJavaObject))
        //                 {
        //                     IntPtr[] array4 = AndroidJNI.FromObjectArray(array);
        //                     int length2 = array4.GetLength(0);
        //                     AndroidJavaObject[] array5 = new AndroidJavaObject[length2];
        //                     for (int j = 0; j < length2; j++)
        //                     {
        //                         array5[j] = new AndroidJavaObject(array4[j]);
        //                     }
        //                     return (ArrayType)array5;
        //                 }
        //                 Debug.Log("JNI: Unknown generic array type '" + elementType + "'");
        //             }
        //             return default(ArrayType);
        //         }
    }
}
