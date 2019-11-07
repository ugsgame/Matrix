using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.Armature
{
    public enum MovementEventType
    {
        START,
        COMPLETE,
        LOOP_COMPLETE
    };

    public class CCAnimation : CCObject
    {
        internal CCAnimation(IntPtr t):base(t){ }
        public CCAnimation(CCArmature arm)
            : base(NativeAnimation.Create(arm.CppObjPtr))
        {

        }

        public float AnimScale
        {
            set { NativeAnimation.SetAnimScale(this.CppObjPtr, value); }
            get { return NativeAnimation.GetAnimScale(this.CppObjPtr); }
        }

        public float SpeedScale
        {
            set { NativeAnimation.SetSpeedScale(this.CppObjPtr, value); }
            get { return NativeAnimation.GetSpeedScale(this.CppObjPtr); }
        }

        public void Play(string name,bool loop)
        {
            int Loop = 0;
            if (loop) Loop = 1;
            NativeAnimation.PlayWithName(this.CppObjPtr,name,-1,-1,Loop);

        }
        public void Play(string name, bool loop, int durationTo, int durationTween)
        {
            int Loop = 0;
            if (loop) Loop = 1;
            NativeAnimation.PlayWithName(this.CppObjPtr, name, durationTo, durationTween, Loop);
        }

        public void Play(int animationIndex, bool loop)
        {
            int Loop = 0;
            if (loop) Loop = 1;
            NativeAnimation.PlayWithIndex(this.CppObjPtr, animationIndex, -1, -1, Loop);
        }

        public void Play(int animationIndex, bool loop, int durationTo, int durationTween)
        {
            int Loop = 0;
            if (loop) Loop = 1;
            NativeAnimation.PlayWithIndex(this.CppObjPtr, animationIndex, durationTo, durationTween, Loop);
        }
        //还不知道有什么区别
        public void PlayByIndex(int animationIndex, bool loop)
        {
            int Loop = 0;
            if (loop) Loop = 1;
            NativeAnimation.PlayByIndex(this.CppObjPtr, animationIndex, -1, -1, Loop);
        }
        public void PlayByIndex(int animationIndex, bool loop, int durationTo, int durationTween)
        {
            int Loop = 0;
            if (loop) Loop = 1;
            NativeAnimation.PlayByIndex(this.CppObjPtr, animationIndex, durationTo, durationTween, Loop);
        }

        public void GotoAndPlay(int frameIndex)
        {
            NativeAnimation.GotoAndPlay(this.CppObjPtr,frameIndex);
        }

        public void GotoAndPause(int frameIndex)
        {
            NativeAnimation.GotoAndPause(this.CppObjPtr, frameIndex);
        }

        public void Pause()
        {
            NativeAnimation.Pause(this.CppObjPtr);
        }

        public void Resume()
        {
            NativeAnimation.Resume(this.CppObjPtr);
        }

        public void Stop()
        {
            NativeAnimation.Stop(this.CppObjPtr);
        }

        public int GetMovementCount()
        {
            return NativeAnimation.GetMovementCount(this.CppObjPtr);
        }

        public string GetCureentMovementID()
        {
            return NativeAnimation.GetCureentMovementID(this.CppObjPtr);
        }

        public void SetMovementEvent(IAnimationEvent _event)
        {
            NativeAnimation.SetMovementEvent(this.CppObjPtr, _event);
        }

        public void SetFrameEvent(IAnimationEvent _event)
        {
            NativeAnimation.SetFrameEvent(this.CppObjPtr, _event);
        }

    }
}
