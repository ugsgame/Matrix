using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MatrixEngine.Util
{
    public class CppPtrUtil
    {
        public static void CheckPtr(IntPtr ptr)
        {
            if (ptr == IntPtr.Zero)
            {
                throw new NullReferenceException();
            }
        }
    }
}
