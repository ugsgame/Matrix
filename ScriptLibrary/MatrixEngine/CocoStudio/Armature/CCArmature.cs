using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine;
using MatrixEngine.Cocos2d;
using MatrixEngine.CocoStudio.Native;
using MatrixEngine.Math;

namespace MatrixEngine.CocoStudio.Armature
{
    /// <summary>
    /// TODO:CCArmature 不能被完全释放
    /// </summary>
    public class CCArmature : CCNodeRGBA
    {

        private CCAnimation tempAnimation = null;

        internal CCArmature(IntPtr t) { }
        public CCArmature()
            : base(NativeArmature.Create())
        {

        }

        public CCArmature(string name):base(NativeArmature.CreateWithName(name))
        {
            //this.CppObjPtr = NativeArmature.CreateWithName(name);
        }

        public CCArmature(string name, CCBone bone):base(NativeArmature.CreateWithNameAndBone(name, bone.CppObjPtr))
        {
            //this.CppObjPtr = NativeArmature.CreateWithNameAndBone(name, bone.CppObjPtr);
            scriptManager.PutScriptObject(bone);
        }
        /// <summary>
        /// 注册动画碰撞监听
        /// </summary>
        public static void RegisterContactListener()
        {
            NativeArmature.RegisterContactListener();
        }

        public void BindGameActor(ActorBehavior gameActor)
        {
            NativeArmature.BindGameActor(this.CppObjPtr, gameActor.CppObjPtr);
        }

        public void UnBindGameActor()
        {
            NativeArmature.UnBindGameActor(this.CppObjPtr);
        }

        private Rect boneRect = Rect.Zero;
        public Rect GetBoneRect(string boneName)
        {
            //Rect rect = Rect.Zero;
            NativeArmature.GetBoneRect(this.CppObjPtr, boneName, ref boneRect);
            return boneRect;
        }
        public Rect GetBoneRectInWorld(string boneName)
        {
            Rect rect = this.GetBoneRect(boneName);
            rect.origin = this.ConvertToWorldSpace(rect.origin);
            return rect;
        }

        private Vector2 bonePosition = Vector2.Zero;
        public Vector2 GetBonePosition(string boneName)
        {
            NativeArmature.GetBonePosition(this.CppObjPtr, boneName, ref bonePosition);
            return bonePosition;
        }

        public Vector2 GetBonePositionInWorld(string boneName)
        {
            Vector2 pos = this.GetBonePosition(boneName);
            pos = this.ConvertToWorldSpace(pos);
            return pos;
        }

        public bool BoneIsDisplay(string boneName)
        {
            return NativeArmature.BoneIsDisplay(this.CppObjPtr, boneName);
        }

        public void AddBone(CCBone bone)
        {
            scriptManager.PutScriptObject(bone);
            NativeArmature.AddBone(this.CppObjPtr, bone.CppObjPtr);
        }

        public CCBone GetBone(string name)
        {
            //TODO
            //IntPtr ptr = NativeArmature.GetBone(this.CppObjPtr, name);
            //if (ptr == IntPtr.Zero)
            //    return null;
            //return new CCBone(ptr);

            //查找子的指针
            IntPtr scriptObject = NativeArmature.GetBone(this.CppObjPtr, name);
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

        public void RemoveBone(CCBone bone, bool recursion)
        {
            scriptManager.RemoveScriptObject(bone);
            NativeArmature.RemoveBone(this.CppObjPtr, bone.CppObjPtr, recursion);
        }

        public void ChangeBoneParent(CCBone bone, string name)
        {
            NativeArmature.ChangeBoneParent(this.CppObjPtr, bone.CppObjPtr, name);
        }

        public void SetAnimation(CCAnimation anim)
        {
            NativeArmature.SetAnimation(this.CppObjPtr, anim.CppObjPtr);
        }

        private BlendFunc blendFuc = new BlendFunc();
        public BlendFunc BlendFunc
        {
            get
            {
                NativeArmature.GetBlendFunc(this.CppObjPtr, out blendFuc);
                return blendFuc;
            }
            set
            {
                NativeArmature.SetBlendFunc(this.CppObjPtr, ref value);
            }
        }

        public tCCPositionType ParticlePositionType
        {
            get
            {
                return NativeArmature.GetParticlePositionType(this.CppObjPtr);
            }
            set
            {
                NativeArmature.SetParticlePositionType(this.CppObjPtr, value);
            }
        }

        public CCAnimation GetAnimation()
        {
            //TODO
            IntPtr ptr = NativeArmature.GetAnimation(this.CppObjPtr);
            if (ptr == IntPtr.Zero)
                return null;
            else if (tempAnimation != null)
            {
                tempAnimation.CppObjPtr = ptr;
                return tempAnimation;
            }
            else
            {
                tempAnimation = new CCAnimation(ptr);
                return tempAnimation;
            }
        }

        public void SetColliderFilter(CCColliderFilter colliderFilter)
        {
            NativeArmature.SetColliderFilter(this.CppObjPtr, colliderFilter.CppObjPtr);
        }
    }
}
