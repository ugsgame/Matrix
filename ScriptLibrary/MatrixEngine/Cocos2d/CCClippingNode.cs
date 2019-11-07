using MatrixEngine.Cocos2d.Native;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MatrixEngine.Cocos2d
{
    public class CCClippingNode:CCNode
    {
        internal CCClippingNode(IntPtr t)
            : base(t)
        {
        }

        public CCClippingNode()
            : base(NativeClippingNode.Create())
        {
        }

        public CCClippingNode(CCNode stencil)
            : base(stencil.CppObjPtr)
        {
        }

        public CCNode Stencil
        {
            set 
            {
                NativeClippingNode.SetStencil(this.CppObjPtr,value.CppObjPtr);
            }
            get 
            {
                IntPtr pStencil = NativeClippingNode.GetStencil(this.CppObjPtr);
                CCNode node = null;
                if (NativeObject.IsBindMonoObject(pStencil))
                {
                    node = (CCNode)NativeObject.GetMonoObject(pStencil);
                }
                else
                {
                    node = new CCNode(pStencil);
                }
                return node;
            }
        }

        public CCNode AddDefaultStendcil()
        {
            IntPtr pStencil = NativeClippingNode.AddDefaultStendcil(this.CppObjPtr);
            CCNode node = null;
            if (NativeObject.IsBindMonoObject(pStencil))
            {
                node = (CCNode)NativeObject.GetMonoObject(pStencil);
            }
            else
            {
                node = new CCNode(pStencil);
            }
            return node;
        }

        public float AlphaThreshold
        {
            set
            {
                NativeClippingNode.SetAlphaThreshold(this.CppObjPtr,value);
            }
            get
            {
                return NativeClippingNode.GetAlphaThreshold(this.CppObjPtr);
            }
        }

        public bool IsInverted
        {
            set
            {
                NativeClippingNode.SetInverted(this.CppObjPtr, value);
            }
            get
            {
                return NativeClippingNode.IsInverted(this.CppObjPtr);
            }
        }
    }
}
