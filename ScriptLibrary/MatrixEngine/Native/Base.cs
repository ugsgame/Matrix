using System;

namespace MatrixEngine.Native
{
    /// <summary>
    /// C#脚本的基类,此类会与引擎底层的C++类关联  
    /// </summary>
    //[StructLayout(LayoutKind.Sequential)] // - define the memory layout
    public class Base
    {
        // - pointer of the cpp object which this object associate with
        private IntPtr _CppObjPtr = IntPtr.Zero;

        internal Base(IntPtr ptr)
        {
            CppObjPtr = ptr;
        }

        internal virtual IntPtr CppObjPtr
        {
            get { return _CppObjPtr; }
            set { _CppObjPtr = value; }
        }

        internal virtual void ClearPtr()
        {
            this._CppObjPtr = IntPtr.Zero;
        }

        private int GetPtr()
        {
            return _CppObjPtr.ToInt32(); 
        }

        public override int GetHashCode()
        {
            return CppObjPtr.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            return CppObjPtr != IntPtr.Zero && obj is Base && ((Base) obj).CppObjPtr == CppObjPtr;
        }

        public override string ToString()
        {
            return GetType().FullName + "@" + CppObjPtr.ToInt32().ToString("x");
        }

        ~Base()
        {
        }
    }
}