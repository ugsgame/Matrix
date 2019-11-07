using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.Cocos2d;
using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{

    public enum CCProgressTimerType
    {
        /// Radial Counter-Clockwise
        kCCProgressTimerTypeRadial,
        /// Bar
        kCCProgressTimerTypeBar,
    } ; 

    public class CCProgressTimer: CCNodeRGBA
    {
        internal CCProgressTimer(IntPtr t) : base(t) { }
        public CCProgressTimer(CCSprite sprite)
            : base(NativeProgressTimer.Create(sprite.CppObjPtr))
        {

        }

        public CCProgressTimerType Type
        {
            get { return NativeProgressTimer.GetType(this.CppObjPtr); }
            set { NativeProgressTimer.SetType(this.CppObjPtr, value); }
        }

        public float Percentage
        {
            get { return NativeProgressTimer.GetPercentage(this.CppObjPtr); }
            set { NativeProgressTimer.SetPercentage(this.CppObjPtr, value); }
        }

        public CCSprite Sprite
        {
            get 
            {
                //查找子的指针
                IntPtr scriptObject = NativeProgressTimer.GetSprite(this.CppObjPtr);
                //如果不是空指针表示存在子
                if (scriptObject != IntPtr.Zero)
                {
                    ///查找相关的对象缓存是否存在
                    CCSprite putNode = scriptManager.GetScriptObject(scriptObject) as CCSprite;
                    if (putNode == null)
                    {
                        //对象类型缓存
                        putNode = new CCSprite(scriptObject);
                        scriptManager.PutScriptObject(putNode);
                    }
                    return putNode;
                }
                return null;
            }
            set { NativeProgressTimer.SetSprite(this.CppObjPtr, value.CppObjPtr); }
        }

        public void SetReverseProgress(bool reverse)
        {
            NativeProgressTimer.SetReverseProgress(this.CppObjPtr,reverse);
        }

        public bool ReverseDirection
        {
            get { return NativeProgressTimer.IsReverseDirection(this.CppObjPtr); }
            set { NativeProgressTimer.SetReverseDirection(this.CppObjPtr,value); }
        }

        public Vector2 MidPoint
        {
            get 
            {
                Vector2 pos = new Vector2();
                NativeProgressTimer.GetMidPoint(this.CppObjPtr, out pos);
                return pos;
            }
            set
            {
                Vector2 pos = new Vector2();
                NativeProgressTimer.SetMidPoint(this.CppObjPtr, ref pos);
            }
        }

        public Vector2 BarChangeRate
        {
            get
            {
                Vector2 pos = new Vector2();
                NativeProgressTimer.GetBarChangeRate(this.CppObjPtr, out pos);
                return pos;
            }
            set
            {
                Vector2 pos = new Vector2();
                NativeProgressTimer.SetBarChangeRate(this.CppObjPtr, ref pos);
            }
        }
    }
}
