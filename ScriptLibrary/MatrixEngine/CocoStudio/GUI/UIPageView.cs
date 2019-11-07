using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public class UIPageView : UILayout
    {
        internal UIPageView(IntPtr t) : base(t) { }
        private IPageEventListener pageListener;
        public UIPageView()
            : base(NativeUIPageView.Create())
        {
            //this.CppObjPtr = NativeUIPageView.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public void AddWidgetToPage(UIWidget widget, int pageIdx, bool forceCreate)
        {
            var page = GetPage(pageIdx);
            page.scriptManager.PutScriptObject(widget);
            widget._parent = page;
            
            NativeUIPageView.AddWidgetToPage(this.CppObjPtr, widget.CppObjPtr, pageIdx, forceCreate);
        }

        public void AddPage(UILayout page)
        {
            scriptManager.PutScriptObject(page);
            NativeUIPageView.AddPage(this.CppObjPtr, page.CppObjPtr);
        }

        public void InsertPage(UILayout page, int idx)
        {
            scriptManager.PutScriptObject(page);
            NativeUIPageView.InsertPage(this.CppObjPtr, page.CppObjPtr, idx);
        }

        public void RemovePage(UILayout page)
        {
            scriptManager.RemoveScriptObject(page);
            NativeUIPageView.RemovePage(this.CppObjPtr, page.CppObjPtr);
        }

        public void RemovePageAtIndex(int index)
        {
            var page = GetPage(index);
            scriptManager.RemoveScriptObject(page);
            NativeUIPageView.RemovePageAtIndex(this.CppObjPtr, index);
        }

        public void RemoveAllPages()
        {
            scriptManager.clear();
            NativeUIPageView.RemoveAllPages(this.CppObjPtr);
        }

        public void ScrollToPage(int idx)
        {
            NativeUIPageView.ScrollToPage(this.CppObjPtr, idx);
        }

        public int GetCurPageIndex()
        {
            return NativeUIPageView.GetCurPageIndex(this.CppObjPtr);
        }

        public UILayout GetPage(int index)
        {
            //查找子的指针
            IntPtr scriptObject = NativeUIPageView.GetPage(this.CppObjPtr, index);
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

        public void AddEventListenerPageView(IPageEventListener listener)
        {
            pageListener = listener;
            NativeUIPageView.AddEventListenerPageView(this.CppObjPtr, pageListener);
        }

        public bool NeedOtimization
        {
            get { return NativeUIPageView.IsNeedOptimization(this.CppObjPtr); }
            set { NativeUIPageView.SetNeedOptimization(this.CppObjPtr, value); }
        }
    }
}
