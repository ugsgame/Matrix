using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCSprite : CCNodeRGBA
    {
        internal CCSprite(IntPtr t) : base(t) { }
        public CCSprite()
            : base(NativeSprite.Create())
        {
            //this.CppObjPtr = NativeSprite.Create();
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
        public CCSprite(string filePath)
            : base(NativeSprite.CreateByFile(filePath))
        {
        }

        public CCSprite(string pszSpriteFrameName,bool plist)
            : base(IntPtr.Zero)
        {
            if (plist)
                this.CppObjPtr = NativeSprite.CreateWithSpriteFrameName(pszSpriteFrameName);
            else
                this.CppObjPtr = NativeSprite.CreateByFile(pszSpriteFrameName);
        }

        public BlendFunc BlendFunc
        {
            get
            {
                BlendFunc blend = new BlendFunc();
                NativeSprite.GetBlendFunc(this.CppObjPtr, out blend);
                return blend;
            }
            set
            {
                NativeSprite.SetBlendFunc(this.CppObjPtr, ref value);
            }
        }


        public void SetTextureRect(Rect rect)
        {
            NativeSprite.SetTextureRect1(this.CppObjPtr, rect.origin.X, rect.origin.Y, rect.size.width, rect.size.height);
        }

        public void SetTextureRect(Rect rect, Size size, bool rotated)
        {
            NativeSprite.SetTextureRect2(this.CppObjPtr, rect.origin.X, rect.origin.Y, rect.size.width, rect.size.height, rotated, size.width, size.height);
        }

        public Rect GetTextureRect()
        {
            Rect rect = default(Rect);
            NativeSprite.GetTextureRect(this.CppObjPtr, rect);
            return rect;
        }

        //TODO 
        //改成CCBatchNode
        public CCNode BatchNode
        {
            set { NativeSprite.SetBatchNode(this.CppObjPtr, value.CppObjPtr); }
            get
            {
                IntPtr scriptObject = NativeSprite.GetBatchNode(this.CppObjPtr);
                //如果不是空指针表示存在子
                if (scriptObject != IntPtr.Zero)
                {
                    ///查找相关的对象缓存是否存在
                    CCNode putNode = scriptManager.GetScriptObject(scriptObject) as CCNode;
                    if (putNode == null)
                    {
                        //对象类型缓存
                        putNode = new CCNode(scriptObject);
                        scriptManager.PutScriptObject(putNode);
                    }
                    return putNode;
                }
                return null;
            }
        }

        public bool Dirty
        {
            set { NativeSprite.SetDirty(this.CppObjPtr, value); }
            get { return NativeSprite.IsDirty(this.CppObjPtr); }
        }

        public Vector2 GetOffsetPosition()
        {
            Vector2 pos = default(Vector2);
            NativeSprite.GetOffsetPosition(this.CppObjPtr, pos);
            return pos;
        }
    }
}
