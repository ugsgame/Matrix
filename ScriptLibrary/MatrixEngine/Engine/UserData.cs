using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Native;

namespace MatrixEngine
{
    public class UserData : Base
    {
        public UserData()
            : base(IntPtr.Zero)
        {

        }

        public UserData(IntPtr data)
            : base(IntPtr.Zero)
        {

        }
    }
}
