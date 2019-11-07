using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public class UILayer:CCLayer
    {
        internal UILayer(IntPtr t) : base(t) { }
        public UILayer():base(NativeUILayer.Create())
        {
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);

            this.SetState(LayerState.Touch, true);
        }

        protected override void native_OnUpdate()
        {
            OnUpdate(NativeUILayer.TickTime(this.CppObjPtr));
        }

        public void AddWidget(UIWidget widget)
        {
            if (widget.Parent != null)
            {
                widget.RemoveFromParent(false);
            }

            widget._parent = this;
            scriptManager.PutScriptObject(widget);
            NativeUILayer.AddWidget(this.CppObjPtr, widget.CppObjPtr);
        }

        public void RemoveWidget(UIWidget widget)
        {
            widget._parent = null;
            scriptManager.RemoveScriptObject(widget);
            NativeUILayer.RemoveWidget(this.CppObjPtr, widget.CppObjPtr);
        }

        public UIWidget GetWidget(string name)
        {
           //return new UIWidget(NativeUILayer.GetWidgetByName(this.CppObjPtr,name));
            //查找子的指针
            IntPtr scriptObject = NativeUILayer.GetWidgetByName(this.CppObjPtr, name);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                //查找相关的对象缓存是否存在
                UIWidget putNode = scriptManager.GetScriptObject(scriptObject) as UIWidget;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new UIWidget(scriptObject);
                    putNode._parent = this;
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;
        }

        public UIWidget GetWidget(int tag)
        {
            //return new UIWidget(NativeUILayer.GetWidgetByTag(this.CppObjPtr, tag));
            //查找子的指针
            IntPtr scriptObject = NativeUILayer.GetWidgetByTag(this.CppObjPtr, tag);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                //查找相关的对象缓存是否存在
                UIWidget putNode = scriptManager.GetScriptObject(scriptObject) as UIWidget;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new UIWidget(scriptObject);
                    putNode._parent = this;
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;
        }

        public UIWidget GetRootWidget()
        {
            //return new UIWidget(NativeUILayer.GetRootWidget(this.CppObjPtr));
            //查找子的指针
            IntPtr scriptObject = NativeUILayer.GetRootWidget(this.CppObjPtr);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                //查找相关的对象缓存是否存在
                UIWidget putNode = scriptManager.GetScriptObject(scriptObject) as UIWidget;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new UIWidget(scriptObject);
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;
        }
        
    }
}
