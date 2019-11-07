using MatrixEngine.Cocos2d.Native;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MatrixEngine.Cocos2d
{
    public class CCScheduler:CCObject
    {
        internal CCScheduler(IntPtr t) 
            : base(t)
        {
        }

        public CCScheduler()
            : base(NativeScheduler.Create())
        {

        }

        public float TimeScale
        {
            get { return NativeScheduler.GetTimeScale(this.CppObjPtr); }
            set { NativeScheduler.SetTimeScale(this.CppObjPtr,value); }
        }
    }
}
