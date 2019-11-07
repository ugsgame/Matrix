using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.CocoStudio.Native;
using MatrixEngine.Cocos2d;

namespace MatrixEngine.CocoStudio.Armature
{
    public class CCBone : CCNodeRGBA
    {
        internal CCBone(IntPtr t):base(t) { }
        public CCBone()
            : base(NativeBone.Create())
        {
            //this.CppObjPtr = NativeBone.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
        public CCBone(string name):base(NativeBone.CreateWithName(name))
        {
        }

        public void AddDisplay(CCDisplayData display, int index)
        {
            scriptManager.PutScriptObject(display);
            NativeBone.AddDisplay(this.CppObjPtr, display.CppObjPtr, index);
        }
        public void AddDisplay(CCNode node, int index)
        {
            scriptManager.PutScriptObject(node);
            NativeBone.AddNodeDisplay(this.CppObjPtr, node.CppObjPtr, index);
        }

        public void RemoveDisplay(int index)
        {
            NativeBone.RemoveDisplay(this.CppObjPtr, index);
        }

        public void ChangeDisplay(int index, bool force)
        {
            NativeBone.ChangeDisplayWithIndex(this.CppObjPtr, index, force);
        }
        public void ChangeDisplay(string name, bool force)
        {
            NativeBone.ChangeDisplayWithName(this.CppObjPtr, name, force);
        }

        public void AddChildBone(CCBone bone)
        {
            scriptManager.PutScriptObject(bone);
            NativeBone.AddChildBone(this.CppObjPtr, bone.CppObjPtr);
        }

        public void SetParentBone(CCBone bone)
        {
            scriptManager.PutScriptObject(bone);
            NativeBone.SetParentBone(this.CppObjPtr, bone.CppObjPtr);
        }

        public CCBone GetParentBone()
        {
            //IntPtr ptr = NativeBone.GetParentBone(this.CppObjPtr);
            //if (ptr == IntPtr.Zero)
            //    return null;
            //return new CCBone(ptr); 
            //查找子的指针
            IntPtr scriptObject = NativeBone.GetParentBone(this.CppObjPtr);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                ///查找相关的对象缓存是否存在
                CCBone putNode = scriptManager.GetScriptObject(scriptObject) as CCBone;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new CCBone(scriptObject);
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;
        }

//         public void RemoveFromParent(bool recursion)
//          {
//              NativeBone.RemoveFromParent(this.CppObjPtr,recursion);
//          }

        public void RemoveChildBone(CCBone bone,bool recursion)
        {
            scriptManager.RemoveScriptObject(bone);
            NativeBone.RemoveChildBone(this.CppObjPtr,bone.CppObjPtr, recursion);
        }

        public CCNode GetDisplayRenderNode()
        {
            //查找子的指针
            IntPtr scriptObject = NativeBone.GetDisplayRenderNode(this.CppObjPtr);
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

        public DisplayType GetDisplayRenderType()
        {
            return NativeBone.GetDisplayRenderType(this.CppObjPtr);
        }

        public string GetName()
        {
            return NativeBone.GetName(this.CppObjPtr);
        }

    }
}
