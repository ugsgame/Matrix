using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public class UIListView : UIScrollView
    {
        internal UIListView(IntPtr t) : base(t) { }
        public UIListView()
            : base(NativeUIListView.Create())
        {
            //this.CppObjPtr = NativeUIListView.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public void SetItemModel(UIWidget model)
        {
            NativeUIListView.SetItemModel(this.CppObjPtr, model.CppObjPtr);
        }
        public void PushBackDefaultItem()
        {
            NativeUIListView.PushBackDefaultItem(this.CppObjPtr);
        }
        public void InsertDefaultItem(int index)
        {
            NativeUIListView.InsertDefaultItem(this.CppObjPtr, index);
        }
        public void PushBackCustomItem(UIWidget item)
        {
            scriptManager.PutScriptObject(item);
            NativeUIListView.PushBackCustomItem(this.CppObjPtr, item.CppObjPtr);
        }
        public void InsertCustomItem(UIWidget item,int index)
        {
            scriptManager.PutScriptObject(item);
            NativeUIListView.InsertCustomItem(this.CppObjPtr, item.CppObjPtr,index);
        }
        public void RemoveItem(int index)
        {
            var item = GetItem(index);
            scriptManager.RemoveScriptObject(item);
            NativeUIListView.RemoveItem(this.CppObjPtr, index);
        }
        public void  RemoveAllItems()
        {
            scriptManager.clear();
            NativeUIListView.RemoveAllItems(this.CppObjPtr);
        }

        public UIWidget GetItem(int index)
        {
            //查找子的指针
            IntPtr scriptObject = NativeUIListView.GetItem(this.CppObjPtr, index);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                ///查找相关的对象缓存是否存在
                UIWidget putNode = scriptManager.GetScriptObject(scriptObject) as UIWidget;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new UIWidget(scriptObject);
//                    putNode._parent = this;
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;
        }

        public int GetIndex(UIWidget item)
        {
            return NativeUIListView.GetIndex(this.CppObjPtr, item.CppObjPtr);
        }

        public void  SetGravity(int gravity)
        {
            NativeUIListView.SetGravity(this.CppObjPtr, gravity);
        }

        public void SetItemsMargin(float margin)
        {
            NativeUIListView.SetItemsMargin(this.CppObjPtr, margin);
        }

        public int GetCurSelectedIndex()
        {
            return NativeUIListView.GetCurSelectedIndex(this.CppObjPtr);
        }

        private IListEventListener listEventObj;
        public void AddEventListenerListView(IListEventListener listener)
        {
            listEventObj = listener;
            NativeUIListView.AddEventListenerListView(this.CppObjPtr, listEventObj);
        }
        public void RequestRefreshView()
        {
            NativeUIListView.RequestRefreshView(this.CppObjPtr);
        }
    }
}
