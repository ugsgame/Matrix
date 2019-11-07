using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d.Native;
using MatrixEngine.Math;

namespace MatrixEngine.Cocos2d
{
    public class CCAnimation : CCObject
    {
        internal ScriptManager scriptManager = new ScriptManager();
        internal CCAnimation(IntPtr t)
            : base(t)
        {
        }

        public CCAnimation()
            : base(NativeAnimation.Create())
        {
        }

        public CCAnimation(CCSpriteFrame[] spriteFrames)
            : base(NativeAnimation.CreateWithSpriteFrames(spriteFrames))
        {
            for (int i = 0; i < spriteFrames.Length; i++)
            {
                scriptManager.PutScriptObject(spriteFrames[i]);
            }
        }

        public CCAnimation(CCAnimationFrame[] animationFrameNames)
            : base(NativeAnimation.CreateWithAnimationFrames(animationFrameNames))
        {
            for (int i = 0; i < animationFrameNames.Length; i++)
            {
                scriptManager.PutScriptObject(animationFrameNames[i]);
            }
        }

        ~CCAnimation()
        {
            scriptManager.clear();
        }

        public virtual void AddSpriteFrame(CCSpriteFrame spriteFrame)
        {
            //scriptManager.PutScriptObject(spriteFrame);
            NativeAnimation.AddSpriteFrame(this.CppObjPtr, spriteFrame.CppObjPtr);
        }
        public virtual void AddSpriteFrame(string pztFileName)
        {
            NativeAnimation.AddSpriteFrameWithFileName(this.CppObjPtr, pztFileName);
        }
        public virtual void AddSpriteFrame(CCTexture2D texture, Rect rect)
        {
            //scriptManager.PutScriptObject(texture);
            NativeAnimation.AddSpriteFrameWithTexture(this.CppObjPtr, texture.CppObjPtr, ref rect);
        }

        public virtual float TotalDelayUnits
        {
            get
            {
                return NativeAnimation.GetTotalDelayUnits(this.CppObjPtr);
            }
        }

        public virtual float DelayPerUnit
        {
            set 
            {
                NativeAnimation.SetDelayPerUnit(this.CppObjPtr, value);
            }
            get
            {
                return NativeAnimation.GetDelayPerUnit(this.CppObjPtr);
            }
        }

        public virtual float Duration
        {
            get
            {
                return NativeAnimation.GetDuration(this.CppObjPtr);
            }
        }

        public virtual CCSpriteFrame[] Frames
        {
            set
            {
//                 for (int i = 0; i < value.Length; i++)
//                 {
//                     scriptManager.PutScriptObject(value[i]);
//                 }

                NativeAnimation.SetFrames(this.CppObjPtr, value);
            }
        }

        public virtual bool RestoreOriginalFrame
        {
            set
            {
                NativeAnimation.SetRestoreOriginalFrame(this.CppObjPtr, value);
            }
            get
            {
                return NativeAnimation.GetRestoreOriginalFrame(this.CppObjPtr);
            }
        }

        public virtual int Loops
        {
            set
            {
                NativeAnimation.SetLoops(this.CppObjPtr, value);
            }
            get
            {
                return NativeAnimation.GetLoops(this.CppObjPtr);
            }
        }
    }
}
