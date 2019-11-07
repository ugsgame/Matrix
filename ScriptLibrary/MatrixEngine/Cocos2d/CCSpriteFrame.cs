using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCSpriteFrame:CCObject
    {
        internal ScriptManager scriptManager = new ScriptManager();
        internal CCSpriteFrame(IntPtr t)
            : base(t)
        {

        }

        public CCSpriteFrame(string fileName,Rect rect)
            : base(NativeSpriteFrame.CreateWithFile(fileName,ref rect))
        {

        }

        public CCSpriteFrame(CCTexture2D texture,Rect rect)
            : base(NativeSpriteFrame.CreateWithTexture(texture.CppObjPtr, ref rect))
        {
            scriptManager.PutScriptObject(texture);
        }

        ~CCSpriteFrame()
        {
            scriptManager.clear();
        }

        public virtual Rect RectInPixels
        {
            set 
            {
                NativeSpriteFrame.SetRectInPixels(this.CppObjPtr, ref value);
            }
            get
            {
                Rect rect;
                NativeSpriteFrame.GetRectInPixels(this.CppObjPtr,out rect);
                return rect;
            }
        }

        public virtual bool Rotated
        {
            set 
            {
                NativeSpriteFrame.SetRotated(this.CppObjPtr, value);
            }
            get 
            {
                return NativeSpriteFrame.IsRotated(this.CppObjPtr);
            }
        }

        public virtual Rect Rect
        {
            set 
            {
                NativeSpriteFrame.SetRect(this.CppObjPtr, ref value);
            }
            get 
            {
                Rect rect;
                NativeSpriteFrame.GetRect(this.CppObjPtr, out rect);
                return rect;
            }
        }

        public virtual Vector2 OffsetInPixels
        {
            set 
            {
                NativeSpriteFrame.SetOffsetInPixels(this.CppObjPtr, ref value);
            }
            get
            {
                Vector2 pos;
                NativeSpriteFrame.GetOffsetInPixels(this.CppObjPtr,out pos);
                return pos;
            }
        }

        public virtual Size OriginalSizeInPixels
        {
            set 
            {
                NativeSpriteFrame.SetOriginalSizeInPixels(this.CppObjPtr,ref value);
            }
            get
            {
                Size size;
                NativeSpriteFrame.GetOriginalSizeInPixels(this.CppObjPtr, out size);
                return size;
            }
        }

        public virtual Size OriginalSize
        {
            set
            {
                NativeSpriteFrame.SetOriginalSize(this.CppObjPtr, ref value);
            }
            get
            {
                Size size;
                NativeSpriteFrame.GetOriginalSize(this.CppObjPtr, out size);
                return size;
            }
        }

        public virtual CCTexture2D Texture
        {
            set
            {
                scriptManager.PutScriptObject(value);
                NativeSpriteFrame.SetTexture(this.CppObjPtr, value.CppObjPtr);
            }
            get
            {
                IntPtr pTexture2D = NativeSpriteFrame.GetTexture(this.CppObjPtr);
                CCTexture2D texture = null;
                if (NativeObject.IsBindMonoObject(pTexture2D))
                {
                    texture = (CCTexture2D)NativeObject.GetMonoObject(pTexture2D);
                }
                else
                {
                    texture = new CCTexture2D(pTexture2D);
                }
                return texture;
            }
        }

        public virtual Vector2 Offset
        {
            set
            {
                NativeSpriteFrame.SetOffset(this.CppObjPtr, ref value);
            }
            get
            {
                Vector2 pos;
                NativeSpriteFrame.GetOffset(this.CppObjPtr, out pos);
                return pos;
            }
        }
    }
}
