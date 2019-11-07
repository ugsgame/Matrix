using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCNodeRGBA : CCNode
    {
        internal CCNodeRGBA(IntPtr t) : base(t) { }
        public CCNodeRGBA()
            : base(NativeNodeRGBA.Create())
        {
            //this.CppObjPtr = NativeNodeRGBA.Create();
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public virtual int Alpha
        {
            set { NativeNodeRGBA.SetAlpha(this.CppObjPtr, value); }
            get { return NativeNodeRGBA.GetAlpha(this.CppObjPtr); }
        }

        public virtual Color32 Color
        {
            set { NativeNodeRGBA.SetColor(this.CppObjPtr, value.R, value.G, value.B); }
            get
            {
                Color32 color = default(Color32);
                NativeNodeRGBA.GetColor(this.CppObjPtr, out color);
                return color;
            }
        }
    }
}
