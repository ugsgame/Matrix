using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCAnimationFrame:CCObject
    {
        internal ScriptManager scriptManager = new ScriptManager();
        internal CCAnimationFrame(IntPtr t) 
            : base(t)
        {
        }

        public CCAnimationFrame()
            : base(NativeAnimationFrame.Create())
        {

        }

        ~CCAnimationFrame()
        {
            scriptManager.clear();
        }

        public virtual  CCSpriteFrame SpriteFrame
        {
            set 
            {
                scriptManager.PutScriptObject(value);
                NativeAnimationFrame.SetSpriteFrame(this.CppObjPtr, value.CppObjPtr);
            }
        }

        public virtual float DelayUnits
        {
            set 
            {
                NativeAnimationFrame.SetDelayUnits(this.CppObjPtr, value);
            }
            get
            {
                return NativeAnimationFrame.GetDelayUnits(this.CppObjPtr);
            }
        }
    }
}
