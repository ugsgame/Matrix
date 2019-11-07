using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    /** @typedef tCCPositionType
    possible types of particle positions
    */
    public enum tCCPositionType
    {
        /** Living particles are attached to the world and are unaffected by emitter repositioning. */
        kCCPositionTypeFree,

        /** Living particles are attached to the world but will follow the emitter repositioning.
        Use case: Attach an emitter to an sprite, and you want that the emitter follows the sprite.
        */
        kCCPositionTypeRelative,

        /** Living particles are attached to the emitter and are translated along with it. */
        kCCPositionTypeGrouped,
    };

    //* @enum
    public enum kCCParticleMode
    {
        /** Gravity mode (A mode) */
        kCCParticleModeGravity,

        /** Radius mode (B mode) */
        kCCParticleModeRadius,
    };

    public class CCParticleSystem : CCNode
    {
        internal CCParticleSystem(IntPtr t) : base(t) { }
        public CCParticleSystem(string plistFile)
            : base(NativeParticleSystem.Create(plistFile))
        {
        }

        protected virtual void OnRemove()
        {
            //this.Parent.RemoveChild(this);
            this.RemoveFromParent();
        }

        /// <summary>
        /// IsAutoRemoveOnFinish = true
        /// call it
        /// </summary>
        private void native_OnRemove()
        {
            try
            {
                OnRemove();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        public bool IsActive()
        {
            return NativeParticleSystem.IsActive(this.CppObjPtr);
        }

        public bool IsBlendAdditive
        {
            set { NativeParticleSystem.SetBlendAdditive(this.CppObjPtr, value); }
            get { return NativeParticleSystem.IsBlendAdditive(this.CppObjPtr); }
        }
        //

        public void InitIndices()
        {
            NativeParticleSystem.InitIndices(this.CppObjPtr);
        }

        public void InitTexCoordsWithRect(Rect rect)
        {
            NativeParticleSystem.InitTexCoordsWithRect(this.CppObjPtr, rect.origin.X, rect.origin.Y, rect.size.width, rect.size.height);
        }

        //void SetDisplayFrame(IntPtr particle, IntPtr spriteFrame);

        //void SetTextureWithRect(IntPtr particle, IntPtr texture, float rx, float ry, float rw, float rh);


        public tCCPositionType PositionType
        {
            set { NativeParticleSystem.SetPositionType(this.CppObjPtr, value); }
            get { return NativeParticleSystem.GetPositionType(this.CppObjPtr); }
        }

        public kCCParticleMode EmitterMode
        {
            set { NativeParticleSystem.SetEmitterMode(this.CppObjPtr, value); }
            get { return NativeParticleSystem.GetEmitterMode(this.CppObjPtr); }
        }

        public bool RotationIsDir
        {
            set { NativeParticleSystem.SetRotationIsDir(this.CppObjPtr, value); }
            get { return NativeParticleSystem.GetRotationIsDir(this.CppObjPtr); }
        }

        public bool IsAutoRemoveOnFinish
        {
            set { NativeParticleSystem.SetAutoRemoveOnFinish(this.CppObjPtr, value); }
            get { return NativeParticleSystem.IsAutoRemoveOnFinish(this.CppObjPtr); }
        }


        // void SetTexture(IntPtr particle, IntPtr texture);


        public void PostStep()
        {
            NativeParticleSystem.PostStep(this.CppObjPtr);
        }
        //void SetBatchNode(IntPtr particle, IntPtr batchNode);

        public void Start()
        {
            NativeParticleSystem.Start(this.CppObjPtr);
        }
        public void Play()
        {
            NativeParticleSystem.Play(this.CppObjPtr);
        }
        public void Stop()
        {
            NativeParticleSystem.Stop(this.CppObjPtr);
        }
    }

}