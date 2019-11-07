using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MatrixEngine.Platform.Android
{
    public class AndroidJavaClass : AndroidJavaObject
    {
        public AndroidJavaClass(string className)
        {
            this._AndroidJavaClass(className);
        }
        private void _AndroidJavaClass(string className)
        {
            IntPtr intPtr = AndroidJNI.FindClass("java/lang/Class");
            IntPtr staticMethodID = AndroidJNI.GetStaticMethodID(intPtr, "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
            jvalue[] args = AndroidJNIHelper.CreateJNIArgArray(new object[]
			{
				className
			});
            intPtr = AndroidJNI.CallStaticObjectMethod(intPtr, staticMethodID, args);
            this.m_jclass = AndroidJNI.NewGlobalRef(intPtr);
            this.m_jobject = IntPtr.Zero;
        }
    }
}
