using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    //     public enum ActionID
    //     {
    //     }
    public class CCAction : CCObject
    {
        protected CCNode Node;
        internal CCAction(IntPtr t) : base(t) { }
        public CCAction()
            : base(IntPtr.Zero)
        {
        }
        ~CCAction()
        {
        }
        public bool IsDone()
        {
            return NativeAction.IsDone(this.CppObjPtr);
        }

        public int GetTag()
        {
            //TODO
            return NativeAction.GetTag(this.CppObjPtr);
        }
        public void SetTag(int nTag)
        {
            //TODO 
            NativeAction.SetTag(this.CppObjPtr, nTag);
        }
    }

    //瞬间动作
    public class CCAtionInstant : CCAction
    {
        public CCAtionInstant(IntPtr t) : base(t) { }

        public void Reverse()
        {
            NativeAction.Reverse(this.CppObjPtr);
        }
    }
    //
    public class CCActionShow : CCAtionInstant
    {
        public CCActionShow()
            : base(NativeAction.ActionShow())
        {
            //this.CppObjPtr = NativeAction.CCActionShow();
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionHide : CCAtionInstant
    {
        public CCActionHide()
            : base(NativeAction.ActionHide())
        {
            //this.CppObjPtr = NativeAction.CCActionHide();
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionToggleVisibility : CCAtionInstant
    {
        public CCActionToggleVisibility()
            : base(NativeAction.ActionToggleVisibility())
        {
            //this.CppObjPtr = NativeAction.CCActionToggleVisibility();
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionRemoveSelf : CCAtionInstant
    {
        public CCActionRemoveSelf(bool isNeedCleanUp)
            : base(NativeAction.ActionRemoveSelf(isNeedCleanUp))
        {
            //this.CppObjPtr = NativeAction.CCActionRemoveSelf(isNeedCleanUp);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionFlipX : CCAtionInstant
    {
        public CCActionFlipX(bool x)
            : base(NativeAction.ActionFlipX(x))
        {
            //this.CppObjPtr = NativeAction.CCActionFlipX(x);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionFlipY : CCAtionInstant
    {
        public CCActionFlipY(bool y)
            : base(NativeAction.ActionFlipY(y))
        {
            //this.CppObjPtr = NativeAction.CCActionFlipY(y);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionPlace : CCAtionInstant
    {
        public CCActionPlace(Vector2 pos)
            : base(NativeAction.ActionPlace(pos.X, pos.Y))
        {
            //this.CppObjPtr = NativeAction.CCActionPlace(pos.X, pos.Y);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }
    //TODO
    //用委托

    public delegate void CallFunc(params object[] args);
    public delegate void CallFuncNoArgs();
    //public delegate void funcN(CCNode node);
    //public delegate void funcND(CCNode node, UserData data);
    //public delegate void funcO(CCObject obj);

    /// <summary>
    /// 回调一个函数的事件
    /// </summary>
    public class CCActionCallFunc : CCAtionInstant
    {
        //private func callfuc = null;
        //private object[] args;
        private object callBack;
        public CCActionCallFunc(CallFunc fuc, params object[] args):base(IntPtr.Zero)
        {
            callBack = new CallBackClass(fuc, args);
            this.CppObjPtr = NativeAction.ActionCallFunc(callBack);

//            aaa.Add(this);
            //this.CppObjPtr = NativeAction.CCActionCallFunc(this);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);

            //callfuc = fuc;
            //this.args = args;
        }
        public CCActionCallFunc(CallFuncNoArgs fuc)
            : base(IntPtr.Zero)
        {
            callBack = new CallBackClassNoArgs(fuc);
            this.CppObjPtr = NativeAction.ActionCallFunc(callBack);

            //this.CppObjPtr = NativeAction.CCActionCallFunc(this);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);

            //callfuc = fuc;
            //this.args = args;
        }

        //private void CallFuc()
        //{
        //    callfuc(args);
        //}

        private class CallBackClassNoArgs 
        {
            private CallFuncNoArgs callfuc = null;

            public CallBackClassNoArgs(CallFuncNoArgs fuc)
            {
                callfuc = fuc;
            }
            ~CallBackClassNoArgs()
            {
            }

            private void CallFuc()
            {
                try
                {
                    callfuc();
                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                    Console.WriteLine(e.StackTrace);
                }
            }
        }

        private class CallBackClass 
        {
            private CallFunc callfuc = null;
            private object[] args;
            public CallBackClass(CallFunc fuc, object[] args)
            {
                callfuc = fuc;
                this.args = args;
            }
            ~CallBackClass()
            {
            }
            private void CallFuc()
            {
                try
                {
                    callfuc(args);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                    Console.WriteLine(e.StackTrace);
                }
            }
        }

    }

    //public class ActionCallFuncN : CCAtionInstant
    //{
    //    //private funcN callfuc = null;
    //    //private CCNode _node = null;
    //    public ActionCallFuncN(funcN fuc, CCNode node)
    //        : base(NativeAction.CCActionCallFunc(new CallBackClass(fuc, node)))
    //    {
    //        //this.CppObjPtr = NativeAction.CCActionCallFunc(this);
    //        //对象类型缓存
    //        //ScriptManager.PutScriptObject(this);
    //        //callfuc = fuc;
    //        //_node = node;
    //    }
    //    //private void CallFuc()
    //    //{
    //    //    callfuc(_node);
    //    //}

    //    private class CallBackClass : CCAction
    //    {
    //        private funcN callfuc = null;
    //        private CCNode _node = null;
    //        public CallBackClass(funcN fuc, CCNode node)
    //            : base(IntPtr.Zero)
    //        {
    //            callfuc = fuc;
    //            _node = node;
    //        }
    //        private void CallFuc()
    //        {
    //            callfuc(_node);
    //        }
    //    }
    //}

    //public class ActionCallFunND : CCAtionInstant
    //{
    //    private funcND callfuc = null;
    //    private CCNode _node = null;
    //    private UserData _data = null;
    //    public ActionCallFunND(funcND fuc, CCNode node, UserData data)
    //        : base(IntPtr.Zero)
    //    {
    //        callfuc = fuc;
    //        _node = node;
    //        _data = data;
    //        this.CppObjPtr = NativeAction.CCActionCallFunc(this);
    //        //对象类型缓存
    //        //ScriptManager.PutScriptObject(this);
    //    }
    //    private void CallFuc()
    //    {
    //        callfuc(_node, _data);
    //    }
    //}

    //public class ActionCallFunO : CCAtionInstant
    //{
    //    private funcO callfuc = null;
    //    private CCObject _obj = null;

    //    public ActionCallFunO(CCObject obj)
    //        : base(IntPtr.Zero)
    //    {
    //        _obj = obj;
    //        this.CppObjPtr = NativeAction.CCActionCallFunc(this);
    //        //对象类型缓存
    //        //ScriptManager.PutScriptObject(this);
    //    }
    //    private void CallFuc()
    //    {
    //        callfuc(_obj);
    //    }
    //}

    //间隔动作
    public class CCActionInterval : CCAction
    {
        internal object actionObject;
        internal CCActionInterval(IntPtr t) : base(t) { }

        public float GetTime()
        {
            //TODO
            return NativeAction.GetDuration(this.CppObjPtr);
        }
        public void SetTime(float time)
        {
            NativeAction.SetDuration(this.CppObjPtr, time);
        }

        public virtual CCAction Reverse()
        {
            return new CCAction(NativeAction.Reverse(this.CppObjPtr));
        }
    }

    /// <summary>
    /// 轮流执行多个Action
    /// </summary>
    public class CCActionSequence : CCActionInterval
    {
        private List<CCAction> runObjs = new List<CCAction>(); 
        public CCActionSequence(params CCAction[] actions)
            : base(NativeAction.ActionSequence(actions))
        {
            this.actionObject = actions;
//            runObjs.AddRange(actions);
            foreach (var ccAction in actions)
            {
                runObjs.Add(ccAction);
            }
            //this.CppObjPtr = NativeAction.CCActionSequence(actions);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
        ~CCActionSequence()
        {
            //Console.WriteLine("析构：CCActionSequence");
        }
    }

    /// <summary>
    /// 同时执行多个Action
    /// </summary>
    public class CCActionSpawn : CCActionInterval
    {
        private List<CCAction> runObjs = new List<CCAction>(); 
        public CCActionSpawn(params CCAction[] actions)
            : base(NativeAction.ActionSpawn(actions))
        {
            this.actionObject = actions;
           
            foreach (var ccAction in actions)
            {
                runObjs.Add(ccAction);
            }
            //this.CppObjPtr = NativeAction.CCActionSpawn(actions);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
        ~CCActionSpawn()
        {
            //Console.WriteLine("析构：CCActionSpawn");
        }
    }

    /// <summary>
    /// 重复执行某个Action
    /// </summary>
    public class CCActionRepeat : CCActionInterval
    {
        public CCActionRepeat(CCActionInterval action, int times)
            : base(NativeAction.ActionRepeat(action.CppObjPtr, times))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.CCActionRepeat(action.CppObjPtr, times);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    /// <summary>
    /// 循环执行某个Action
    /// </summary>
    public class CCActionRepeatForever : CCActionInterval
    {
        public CCActionRepeatForever(CCActionInterval action)
            : base(NativeAction.ActionRepeatForever(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.CCActionRepeatForever(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    /// <summary>
    /// 从当前角度旋转到指定角度
    /// </summary>
    public class CCActionRotateTo : CCActionInterval
    {
        public CCActionRotateTo(float time, float angle)
            : base(NativeAction.ActionRotateTo1(time, angle))
        {
            //this.CppObjPtr = NativeAction.ActionRotateTo1(time, angle);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionRotateTo(float time, float angleX, float angleY)
            : base(NativeAction.ActionRotateTo2(time, angleX, angleY))
        {
            //this.CppObjPtr = NativeAction.ActionRotateTo2(time, angleX, angleY);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionRotateTo(float time, Vector2 vec2)
            : this(time, vec2.X, vec2.Y)
        {
        }

    }

    /// <summary>
    /// 从当前角度旋转指定的角度
    /// </summary>
    public class CCActionRotateBy : CCActionInterval
    {
        public CCActionRotateBy(float time, float angle)
            : base(NativeAction.ActionRotateBy1(time, angle))
        {
            //this.CppObjPtr = NativeAction.ActionRotateBy1(time, angle);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);

        }

        public CCActionRotateBy(float time, float angleX, float angleY)
            : base(NativeAction.ActionRotateBy2(time, angleX, angleY))
        {
            //this.CppObjPtr = NativeAction.ActionRotateBy2(time, angleX, angleY);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionRotateBy(float time, Vector2 vec2)
            : this(time, vec2.X, vec2.Y)
        {
        }
    }

    /// <summary>
    /// 从当前位置移动指定的距离
    /// </summary>
    public class CCActionMoveBy : CCActionInterval
    {
        public CCActionMoveBy(float time, float posX, float posY)
            : base(NativeAction.ActionMoveBy(time, posX, posY))
        {
            //this.CppObjPtr = NativeAction.ActionMoveBy(time, posX, posY);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionMoveBy(float time, Vector2 pos)
            : this(time, pos.X, pos.Y)
        {
        }
    }

    /// <summary>
    /// 从当前位置移动到指定位置
    /// </summary>
    public class CCActionMoveTo : CCActionInterval
    {
        public CCActionMoveTo(float time, float posX, float posY)
            : base(NativeAction.ActionMoveTo(time, posX, posY))
        {
            //this.CppObjPtr = NativeAction.ActionMoveTo(time, pos.X, pos.Y);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionMoveTo(float time, Vector2 pos)
            : this(time, pos.X, pos.Y)
        {
        }
    }

    public class CCActionSkewTo : CCActionInterval
    {
        public CCActionSkewTo(float time, float sx, float sy)
            : base(NativeAction.ActionSkewTo(time, sx, sy))
        {
            //this.CppObjPtr = NativeAction.ActionSkewTo(time, sx, sy);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionSkewTo(float time, Vector2 pos)
            : this(time, pos.X, pos.Y)
        {
        }
    }

    public class CCActionSkewBy : CCActionInterval
    {
        public CCActionSkewBy(float time, float sx, float sy)
            : base(NativeAction.ActionSkewBy(time, sx, sy))
        {
            //this.CppObjPtr = NativeAction.ActionSkewBy(time, sx, sy);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionSkewBy(float time, Vector2 pos)
            : this(time, pos.X, pos.Y)
        {
        }
    }

    public class CCActionJumpBy : CCActionInterval
    {
        public CCActionJumpBy(float duration, Vector2 position, float height, int jumps)
            : base(NativeAction.ActionJumpBy(duration, position.X, position.Y, height, jumps))
        {
            //this.CppObjPtr = NativeAction.ActionJumpBy(duration, position.X, position.Y, height, jumps);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionJumpTo : CCActionInterval
    {
        public CCActionJumpTo(float duration, Vector2 position, float height, int jumps)
            : base(NativeAction.ActionJumpTo(duration, position.X, position.Y, height, jumps))
        {
            //this.CppObjPtr = NativeAction.ActionJumpTo(duration, position.X, position.Y, height, jumps);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionBezierBy : CCActionInterval
    {
        public CCActionBezierBy(float time, Vector2 controlPoint_1, Vector2 controlPoint_2, Vector2 endPosition)
            : base(NativeAction.ActionBezierBy(time, controlPoint_1.X, controlPoint_1.Y, controlPoint_2.X, controlPoint_2.Y, endPosition.X, endPosition.Y))
        {
            //this.CppObjPtr = NativeAction.ActionBezierBy(time, controlPoint_1.X, controlPoint_1.Y, controlPoint_2.X, controlPoint_2.Y, endPosition.X, endPosition.Y);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionBezierTo : CCActionInterval
    {
        public CCActionBezierTo(float time, Vector2 controlPoint_1, Vector2 controlPoint_2, Vector2 endPosition)
            : base(NativeAction.ActionBezierTo(time, controlPoint_1.X, controlPoint_1.Y, controlPoint_2.X, controlPoint_2.Y, endPosition.X, endPosition.Y))
        {
            //this.CppObjPtr = NativeAction.ActionBezierTo(time, controlPoint_1.X, controlPoint_1.Y, controlPoint_2.X, controlPoint_2.Y, endPosition.X, endPosition.Y);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    /// <summary>
    /// 从当前大小缩放到指定大小
    /// </summary>
    public class CCActionScaleTo : CCActionInterval
    {
        public CCActionScaleTo(float time, float s)
            : base(NativeAction.ActionScaleTo1(time, s))
        {
            //this.CppObjPtr = NativeAction.ActionScaleTo1(time, s);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionScaleTo(float time, float sx, float sy)
            : base(NativeAction.ActionScaleTo2(time, sx, sy))
        {
            //this.CppObjPtr = NativeAction.ActionScaleTo2(time, sx, sy);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionScaleTo(float time, Vector2 vec2)
            : this(time, vec2.X, vec2.Y)
        {
        }
    }

    /// <summary>
    /// 从当前大小缩放指定的大小
    /// </summary>
    public class CCActionScaleBy : CCActionInterval
    {
        public CCActionScaleBy(float time, float scale)
            : base(NativeAction.ActionScaleBy1(time, scale))
        {
            //this.CppObjPtr = NativeAction.ActionScaleBy1(time, scale);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionScaleBy(float time, float sx, float sy)
            : base(NativeAction.ActionScaleBy2(time, sx, sy))
        {
            //this.CppObjPtr = NativeAction.ActionScaleBy2(time, sx, sy);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionScaleBy(float time, Vector2 vec2)
            : this(time, vec2.X, vec2.Y)
        {
        }
    }

    public class CCActionBlink : CCActionInterval
    {
        public CCActionBlink(float duration, int uBlinks)
            : base(NativeAction.ActionBlink(duration, uBlinks))
        {
            //this.CppObjPtr = NativeAction.ActionBlink(duration, uBlinks);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionFadeIn : CCActionInterval
    {
        public CCActionFadeIn(float time)
            : base(NativeAction.ActionFadeIn(time))
        {
            //this.CppObjPtr = NativeAction.ActionFadeIn(time);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionFadeOut : CCActionInterval
    {
        public CCActionFadeOut(float time)
            : base(NativeAction.ActionFadeOut(time))
        {
            //this.CppObjPtr = NativeAction.ActionFadeOut(time);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionFadeTo : CCActionInterval
    {
        public CCActionFadeTo(float time, int opacity)
            : base(NativeAction.ActionFadeTo(time, opacity))
        {
            //this.CppObjPtr = NativeAction.ActionFadeTo(time, opacity);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionTintTo : CCActionInterval
    {
        public CCActionTintTo(float time, Color32 color)
            : base(NativeAction.ActionTintTo(time, color.R, color.G, color.B))
        {
            //this.CppObjPtr = NativeAction.ActionTintTo(time, color.R, color.G, color.B);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionTintBy : CCActionInterval
    {
        public CCActionTintBy(float time, Color32 color)
            : base(NativeAction.ActionTintBy(time, color.R, color.G, color.B))
        {
            //this.CppObjPtr = NativeAction.ActionTintBy(time, color.R, color.G, color.B);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    /// <summary>
    /// 延迟时间的事件
    /// </summary>
    public class CCActionDelayTime : CCActionInterval
    {
        public CCActionDelayTime(float time)
            : base(NativeAction.ActionDelayTime(time))
        {
            //this.CppObjPtr = NativeAction.ActionDelayTime(time);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }
    //
    public class CCActionProgressTo : CCActionInterval
    {
        public CCActionProgressTo(float duration, float fPercent)
            : base(NativeAction.ActionProgressTo(duration,fPercent))
        {
        }
    }
    //
    public class CCActionProgressFromTo : CCActionInterval
    {
        public CCActionProgressFromTo(float duration, float fFromPercentage, float fToPercentage)
            : base(NativeAction.ActionProgressFromTo(duration, fFromPercentage, fToPercentage))
        {
        }
    }

    public class CCActionAnimate : CCActionInterval
    {
        public CCActionAnimate(CCAnimation animation)
            : base(NativeAction.ActionAnimate(animation.CppObjPtr))
        {

        }
    }
    //非线性动作

    public class CCActionEase : CCActionInterval
    {
        internal object actionObject;
        internal CCActionEase(IntPtr t) : base(t) { }

        public void SetRate(float rate)
        {
            //TODO
        }

        public float GetRate()
        {
            //TODO
            return 0;
        }
    }

    public class CCActionEaseIn : CCActionEase
    {
        public CCActionEaseIn(CCActionInterval action, float fRate)
            : base(NativeAction.ActionEaseIn(action.CppObjPtr, fRate))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseIn(action.CppObjPtr, fRate);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseOut : CCActionEase
    {
        public CCActionEaseOut(CCActionInterval action, float fRate)
            : base(NativeAction.ActionEaseOut(action.CppObjPtr, fRate))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseOut(action.CppObjPtr, fRate);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseInOut : CCActionEase
    {
        public CCActionEaseInOut(CCActionInterval action, float fRate)
            : base(NativeAction.ActionEaseInOut(action.CppObjPtr, fRate))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseInOut(action.CppObjPtr, fRate);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseExponentialIn : CCActionEase
    {
        public CCActionEaseExponentialIn(CCActionInterval action)
            : base(NativeAction.ActionEaseExponentialIn(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseExponentialIn(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseExponentialOut : CCActionEase
    {
        public CCActionEaseExponentialOut(CCActionInterval action)
            : base(NativeAction.ActionEaseExponentialOut(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseExponentialOut(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseExponentialInOut : CCActionEase
    {
        public CCActionEaseExponentialInOut(CCActionInterval action)
            : base(NativeAction.ActionEaseExponentialInOut(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseExponentialInOut(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseSineIn : CCActionEase
    {
        public CCActionEaseSineIn(CCActionInterval action)
            : base(NativeAction.ActionEaseSineIn(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseSineIn(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseSineOut : CCActionEase
    {
        public CCActionEaseSineOut(CCActionInterval action)
            : base(NativeAction.ActionEaseSineOut(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseSineOut(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseSineInOut : CCActionEase
    {
        public CCActionEaseSineInOut(CCActionInterval action)
            : base(NativeAction.ActionEaseSineInOut(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseSineInOut(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseElastic : CCActionEase
    {
        public CCActionEaseElastic(CCActionInterval action)
            : base(NativeAction.ActionEaseElastic1(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseElastic1(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionEaseElastic(CCActionInterval action, float period)
            : base(NativeAction.ActionEaseElastic2(action.CppObjPtr, period))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseElastic2(action.CppObjPtr, period);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseElasticIn : CCActionEase
    {
        public CCActionEaseElasticIn(CCActionInterval action)
            : base(NativeAction.ActionEaseElasticIn1(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseElasticIn1(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionEaseElasticIn(CCActionInterval action, float period)
            : base(NativeAction.ActionEaseElasticIn2(action.CppObjPtr, period))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseElasticIn2(action.CppObjPtr, period);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseElasticOut : CCActionEase
    {
        public CCActionEaseElasticOut(CCActionInterval action)
            : base(NativeAction.ActionEaseElasticOut1(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseElasticOut1(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionEaseElasticOut(CCActionInterval action, float period)
            : base(NativeAction.ActionEaseElasticOut2(action.CppObjPtr, period))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseElasticOut2(action.CppObjPtr, period);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseElasticInOut : CCActionEase
    {
        public CCActionEaseElasticInOut(CCActionInterval action)
            : base(NativeAction.ActionEaseElasticInOut1(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseElasticInOut1(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public CCActionEaseElasticInOut(CCActionInterval action, float period)
            : base(NativeAction.ActionEaseElasticInOut2(action.CppObjPtr, period))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseElasticInOut2(action.CppObjPtr, period);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseBounce : CCActionEase
    {
        public CCActionEaseBounce(CCActionInterval action)
            : base(NativeAction.ActionEaseBounce(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseBounce(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseBounceIn : CCActionEase
    {
        public CCActionEaseBounceIn(CCActionInterval action)
            : base(NativeAction.ActionEaseBounceIn(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseBounceIn(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseBounceOut : CCActionEase
    {
        public CCActionEaseBounceOut(CCActionInterval action)
            : base(NativeAction.ActionEaseBounceOut(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseBounceOut(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseBounceInOut : CCActionEase
    {
        public CCActionEaseBounceInOut(CCActionInterval action)
            : base(NativeAction.ActionEaseBounceInOut(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseBounceInOut(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseBackIn : CCActionEase
    {
        public CCActionEaseBackIn(CCActionInterval action)
            : base(NativeAction.ActionEaseBackIn(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseBackIn(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseBackOut : CCActionEase
    {
        public CCActionEaseBackOut(CCActionInterval action)
            : base(NativeAction.ActionEaseBackOut(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseBackOut(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }

    public class CCActionEaseBackInOut : CCActionEase
    {
        public CCActionEaseBackInOut(CCActionInterval action)
            : base(NativeAction.ActionEaseBackInOut(action.CppObjPtr))
        {
            this.actionObject = action;
            //this.CppObjPtr = NativeAction.ActionEaseBackInOut(action.CppObjPtr);
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
    }
}
