
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCSpriteBatchNode : CCNode
    {
        internal CCSpriteBatchNode(IntPtr t) : base(t) { }

        public CCSpriteBatchNode(string fileName)
            : base(NativeSpriteBatchNode.CreateWithFile(fileName))
        {

        }
        public CCSpriteBatchNode(CCTexture2D texture)
            : base(NativeSpriteBatchNode.CreateWithTexture(texture.CppObjPtr))
        {
            scriptManager.PutScriptObject(texture);
        }

        public virtual void RemoveChildAtIndex(int index)
        {
            this.RemoveChildAtIndex(index, false);
        }
        public virtual void RemoveChildAtIndex(int index, bool doCleanup)
        {
            NativeSpriteBatchNode.RemoveChildAtIndex(this.CppObjPtr, index, doCleanup);
        }

        public virtual void InsertChild(int index, CCSprite sprite)
        {
            scriptManager.PutScriptObject(sprite);
            NativeSpriteBatchNode.InsertChild(this.CppObjPtr, sprite.CppObjPtr, index);
        }
        public virtual void AppendChild(CCSprite sprite)
        {
            scriptManager.PutScriptObject(sprite);
            NativeSpriteBatchNode.AppendChild(this.CppObjPtr, sprite.CppObjPtr);
        }
        public virtual void RemoveSpriteFromAtlas(CCSprite sprite)
        {
            NativeSpriteBatchNode.RemoveSpriteFromAtlas(this.CppObjPtr, sprite.CppObjPtr);
        }

        public virtual int RebuildIndexInOrder(CCSprite parent, int index)
        {
            return NativeSpriteBatchNode.RebuildIndexInOrder(this.CppObjPtr, parent.CppObjPtr, index);
        }
        public virtual int HighestAtlasIndexInChild(CCSprite sprite)
        {
            return NativeSpriteBatchNode.HighestAtlasIndexInChild(this.CppObjPtr, sprite.CppObjPtr);
        }
        public virtual int LowestAtlasIndexInChild(CCSprite sprite)
        {
            return NativeSpriteBatchNode.LowestAtlasIndexInChild(this.CppObjPtr, sprite.CppObjPtr);
        }
        public virtual int AtlasIndexForChild(CCSprite sprite, int z)
        {
            return NativeSpriteBatchNode.AtlasIndexForChild(this.CppObjPtr, sprite.CppObjPtr, z);
        }

        public virtual void ReorderBatch(bool reorder)
        {
            NativeSpriteBatchNode.ReorderBatch(this.CppObjPtr, reorder);
        }

        public virtual CCTexture2D Texture
        {
            set
            {
                scriptManager.PutScriptObject(value);
                NativeSpriteBatchNode.SetTexture(this.CppObjPtr, value.CppObjPtr);
            }
            get
            {
                IntPtr pTexture2D = NativeSpriteBatchNode.GetTexture(this.CppObjPtr);
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

        public virtual BlendFunc BlendFunc
        {
            set
            {
                NativeSpriteBatchNode.SetBlendFunc(this.CppObjPtr, ref value);
            }
            get
            {
                BlendFunc blend;
                NativeSpriteBatchNode.GetBlendFunc(this.CppObjPtr, out blend);
                return blend;
            }
        }
    }
}
