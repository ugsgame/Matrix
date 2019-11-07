using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public enum SCROLLVIEW_DIR
    {
        SCROLLVIEW_DIR_NONE,
        SCROLLVIEW_DIR_VERTICAL,
        SCROLLVIEW_DIR_HORIZONTAL,
        SCROLLVIEW_DIR_BOTH
    };
    public class UIScrollView : UILayout
    {
        internal UIScrollView(IntPtr t) : base(t) { }
        public UIScrollView()
            : base(NativeUIScrollView.Create())
        {
            //this.CppObjPtr = NativeUIScrollView.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public SCROLLVIEW_DIR Direction
        {
            set { NativeUIScrollView.SetDirection(this.CppObjPtr, (int)value); }
            get { return (SCROLLVIEW_DIR)NativeUIScrollView.GetDirection(this.CppObjPtr); }
        }

        public UILayout GetInnerContainer()
        {
            //查找子的指针
            IntPtr scriptObject = NativeUIScrollView.GetInnerContainer(this.CppObjPtr);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                ///查找相关的对象缓存是否存在
                UILayout putNode = scriptManager.GetScriptObject(scriptObject) as UILayout;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new UILayout(scriptObject);
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;
        }

        public Vector2 InnerContainerPosition
        {
            get
            {
               return GetInnerContainer().Postion;
            }
        }

        public void ScrollToBottom(float time, bool attenuated)
        {
            NativeUIScrollView.ScrollToBottom(this.CppObjPtr, time, attenuated);
        }
        public void ScrollToTop(float time, bool attenuated)
        {
            NativeUIScrollView.ScrollToTop(this.CppObjPtr, time, attenuated);
        }
        public void ScrollToLeft(float time, bool attenuated)
        {
            NativeUIScrollView.ScrollToLeft(this.CppObjPtr, time, attenuated);
        }
        public void ScrollToRight(float time, bool attenuated)
        {
            NativeUIScrollView.ScrollToRight(this.CppObjPtr, time, attenuated);
        }
        public void ScrollToTopLeft(float time, bool attenuated)
        {
            NativeUIScrollView.ScrollToTopLeft(this.CppObjPtr, time, attenuated);
        }
        public void ScrollToTopRight(float time, bool attenuated)
        {
            NativeUIScrollView.ScrollToTopRight(this.CppObjPtr, time, attenuated);
        }
        public void ScrollToBottomLeft(float time, bool attenuated)
        {
            NativeUIScrollView.ScrollToBottomLeft(this.CppObjPtr, time, attenuated);
        }
        public void ScrollToBottomRight(float time, bool attenuated)
        {
            NativeUIScrollView.ScrollToBottomRight(this.CppObjPtr, time, attenuated);
        }
        public void ScrollToPercentVertical(float percent, float time, bool attenuated)
        {
            NativeUIScrollView.ScrollToPercentVertical(this.CppObjPtr, percent, time, attenuated);
        }
        public void ScrollToPercentHorizontal(float percent, float time, bool attenuated)
        {
            NativeUIScrollView.ScrollToPercentHorizontal(this.CppObjPtr, percent, time, attenuated);
        }
        public void ScrollToPercentBothDirection(Vector2 point, float time, bool attenuated)
        {
            NativeUIScrollView.ScrollToPercentBothDirection(this.CppObjPtr, point.X, point.Y, time, attenuated);
        }
        public void JumpToBottom()
        {
            NativeUIScrollView.JumpToBottom(this.CppObjPtr);
        }

        public void JumpToTop()
        {
            NativeUIScrollView.JumpToTop(this.CppObjPtr);
        }
        public void JumpToLeft()
        {
            NativeUIScrollView.JumpToLeft(this.CppObjPtr);
        }
        public void JumpToRight()
        {
            NativeUIScrollView.JumpToRight(this.CppObjPtr);
        }
        public void JumpToTopLeft()
        {
            NativeUIScrollView.JumpToTopLeft(this.CppObjPtr);
        }

        public void JumpToTopRight()
        {
            NativeUIScrollView.JumpToTopRight(this.CppObjPtr);
        }
        public void JumpToBottomLeft()
        {
            NativeUIScrollView.JumpToBottomLeft(this.CppObjPtr);
        }
        public void JumpToBottomRight()
        {
            NativeUIScrollView.JumpToBottomRight(this.CppObjPtr);
        }
        public void JumpToPercentVertical(float percent)
        {
            NativeUIScrollView.JumpToPercentVertical(this.CppObjPtr, percent);
        }
        public void JumpToPercentHorizontal(float percent)
        {
            NativeUIScrollView.JumpToPercentHorizontal(this.CppObjPtr, percent);
        }
        public void JumpToPercentBothDirection(Vector2 point)
        {
            NativeUIScrollView.JumpToPercentBothDirection(this.CppObjPtr, point.X, point.Y);
        }

        public Size InnerContainerSize
        {
            set { NativeUIScrollView.SetInnerContainerSize(this.CppObjPtr, value.width, value.height); }
            get
            {
                Size size = default(Size);
                NativeUIScrollView.GetInnerContainerSize(this.CppObjPtr, out size);
                return size;
            }
        }

        public bool BounceEnabled
        {
            set { NativeUIScrollView.SetBounceEnabled(this.CppObjPtr, value); }
            get { return NativeUIScrollView.IsBounceEnabled(this.CppObjPtr); }
        }

        public bool InertiaScrollEnabled
        {
            set { NativeUIScrollView.SetInertiaScrollEnabled(this.CppObjPtr, value); }
            get { return NativeUIScrollView.IsInertiaScrollEnabled(this.CppObjPtr); }
        }

        public bool NeedOptimization
        {
            get { return NativeUIScrollView.IsNeedOptimization(this.CppObjPtr); }
            set { NativeUIScrollView.SetNeedOptimization(this.CppObjPtr, value); }
        }

        private ListenerClass listenerObj;
        public void AddEventListenerScrollView(IScrollEventListener listener)
        {
            if (listenerObj == null)
            {
                
                touchEventListener = new List<IScrollEventListener>(3);
                listenerObj = new ListenerClass(touchEventListener);
                NativeUIScrollView.AddEventListenerScrollView(this.CppObjPtr, listenerObj);
            }
            if (listener != null && !touchEventListener.Contains(listener))
            {
                touchEventListener.Add(listener);
            }
        }

        public void RemoveEventListenerScrollView(IScrollEventListener listener)
        {
            if (listener != null && touchEventListener != null)
            {
                touchEventListener.Remove(listener);
            }
        }

        private List<IScrollEventListener> touchEventListener;

        private class ListenerClass : IScrollEventListener
        {
            private List<IScrollEventListener> touchEventListener;
            public ListenerClass(List<IScrollEventListener> touchEventListener)
            {
                this.touchEventListener = touchEventListener;
            }

            public void ScrollListener(SCROLLVIEW_DIR eventType)
            {
                foreach (IScrollEventListener item in touchEventListener)
                {
                    item.ScrollListener(eventType);
                }
            }
        }
    }
}
