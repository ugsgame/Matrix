using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;
using MatrixEngine.Mooger.Native;

namespace MatrixEngine.Mooger
{
    public class MoogerMap : CCSprite
    {
        internal MoogerMap(IntPtr t) : base(t) { }
        public MoogerMap()
            : base(NativeMoogerMap.CreateMoogrMap())
        {

        }

        public bool LoadWithFile(string filePath, string resPath)
        {
            return NativeMoogerMap.LoadMapWithFile(this.CppObjPtr, filePath, resPath);
        }
    }
}
