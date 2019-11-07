using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    /// <summary>
    /// 图层功能
    /// </summary>
    public enum LayerState
    {
        Touch,      //
        Sensor,     //
        Keypad      //    
    };
    /// <summary>
    /// 触控模式
    /// </summary>
    public enum TouchMode
    {
        Multi,      //
        Single      //    
    };

    public class CCLayer : CCNode
    {
        internal CCLayer(IntPtr t):base(t){ }
        public CCLayer()
            : base(NativeLayer.Create())
        {
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);

            //默认为单点触摸模式
            //this.SetTouchMode(TouchMode.Single);
            this.SetState(LayerState.Touch, true);
        }

        public void SetState(LayerState state, bool enable)
        {
            int _state = (int)state;
            NativeLayer.SetState(this.CppObjPtr, _state, enable);
        }

        public void ScheduleStep(float dt)
        {
            NativeLayer.ScheduleStep(this.CppObjPtr, dt);
        }

        public void UnscheduleStep()
        {
            NativeLayer.UnscheduleStep(this.CppObjPtr);
        }

        public void SetTouchMode(TouchMode mode)
        {
            int _mode = (int)mode;
            NativeLayer.SetTouchMode(this.CppObjPtr, _mode); 
        }

        public TouchMode TouchMode
        {
            set { NativeLayer.SetTouchMode(this.CppObjPtr,(int)value); }
            get { return NativeLayer.GetTouchMode(this.CppObjPtr); }
        }

        public bool IsTouchEnabled
        {
            set { NativeLayer.SetTouchEnabeld(this.CppObjPtr, value); }
            get { return NativeLayer.IsTouchEnabeld(this.CppObjPtr); }
        }

        public bool IsAccelerometerEnabled
        {
            set { NativeLayer.SetAccelerometerEnabled(this.CppObjPtr, value); }
            get { return NativeLayer.IsAccelerometerEnabled(this.CppObjPtr); }
        }

        public bool IsKeypadEnabled
        {
            set { NativeLayer.SetKeypadEnabled(this.CppObjPtr, value); }
            get { return NativeLayer.IsKeypadEnabled(this.CppObjPtr); }
        }

        public int TouchPriority
        {
            set { NativeLayer.SetTouchPriority(this.CppObjPtr, value); }
            get { return NativeLayer.GetTouchPriority(this.CppObjPtr); }
        }

        public void SetSwallowsTouches(bool bSwallowsTouches)
        {
            NativeLayer.SetSwallowsTouches(this.CppObjPtr, bSwallowsTouches);
        }

        public virtual void OnStep(float dTime) { }


        protected virtual void native_OnUpdate()
        {
            OnUpdate(NativeLayer.TickTime(this.CppObjPtr));
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="dTime"></param>
        public virtual void OnUpdate(float dTime) { }

        public virtual void OnEnter() { }

        public virtual void OnExit() { }

        public virtual void OnEnterTransitionFinish()
        {
            //Console.WriteLine("OnEnterTransitionFinish");
        }

        public virtual void OnExitTransitionStart()
        {
            //Console.WriteLine("OnExitTransitionStart");
        }

        /// <summary>
        /// 单点触控回调
        /// </summary>
        /// <param name="X"></param>
        /// <param name="Y"></param>
        /// <returns>flase 不吞事件，true 吞事件 </returns>
        public virtual bool OnTouchBegan(float x, float y)
        {
            return false;
        }

        public virtual void OnTouchMoved(float x, float y) { }

        public virtual void OnTouchEnded(float x, float y) { }

        public virtual void OnTouchCancelled(float x, float y) { }

        /// <summary>
        /// 多点触控回调
        /// </summary>
        public virtual void OnTouchesBegan(CCSet touches) { }

        public virtual void OnTouchesMoved(CCSet touches) { }

        public virtual void OnTouchesEnded(CCSet touches) { }

        public virtual void OnTouchesCancelled(CCSet touches) { }

        /// <summary>
        /// 以后要改成统一的按键回调
        /// </summary>
        public virtual void OnKeyBackClicked() { }
        public virtual void OnKeyMenuClicked() { }
        /// <summary>
        /// 传感器
        /// </summary>
        /// <param name="X"></param>
        /// <param name="Y"></param>
        /// <param name="z"></param>
        public virtual void OnAccelerate(float x, float y, float z) { }
    }
}
