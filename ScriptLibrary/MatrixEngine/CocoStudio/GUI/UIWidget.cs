using System;
using System.Collections.Generic;
using MatrixEngine.Cocos2d;
using MatrixEngine.Cocos2d.Native;
using MatrixEngine.CocoStudio.Native;
using MatrixEngine.Math;

namespace MatrixEngine.CocoStudio.GUI
{
    public enum TouchEventType
    {
        TOUCH_EVENT_BEGAN,
        TOUCH_EVENT_MOVED,
        TOUCH_EVENT_ENDED,
        TOUCH_EVENT_CANCELED
    };

    public enum WidgetType
    {
        WidgetTypeWidget, //control
        WidgetTypeContainer //container
    };

    public enum TextureResType
    {
        UI_TEX_TYPE_LOCAL,
        UI_TEX_TYPE_PLIST
    };

    public enum BrightStyle
    {
        BRIGHT_NONE = -1,
        BRIGHT_NORMAL,
        BRIGHT_HIGHLIGHT
    };

    public enum SizeType
    {
        SIZE_ABSOLUTE,
        SIZE_PERCENT
    };

    public enum PositionType
    {
        POSITION_ABSOLUTE,
        POSITION_PERCENT
    };

    public class UIWidget : CCNode, ICloneable
    {
        public object UserData;

        internal UIWidget(IntPtr t)
            : base(t)
        {
        }

        public UIWidget()
            : base(NativeUIWidget.Create())
        {
            //this.CppObjPtr = NativeUIWidget.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public bool Enabled
        {
            set { NativeUIWidget.SetEnabled(this.CppObjPtr, value); }
            get { return NativeUIWidget.IsEnabled(this.CppObjPtr); }
        }

        public bool Bright
        {
            set { NativeUIWidget.SetBright(this.CppObjPtr, value); }
            get { return NativeUIWidget.IsBright(this.CppObjPtr); }
        }

        public bool TouchEnabled
        {
            set { NativeUIWidget.SetTouchEnabled(this.CppObjPtr, value); }
            get { return NativeUIWidget.IsTouchEnabled(this.CppObjPtr); }
        }

        public void SetBrightStyle(BrightStyle style)
        {
            NativeUIWidget.SetBrightStyle(this.CppObjPtr, (int)style);
        }

        public bool Focused
        {
            set { NativeUIWidget.SetFocused(this.CppObjPtr, value); }
            get { return NativeUIWidget.IsFocused(this.CppObjPtr); }
        }

        public float GetLeftInParent()
        {
            return NativeUIWidget.GetLeftInParent(this.CppObjPtr);
        }

        public float GetBottomInParent()
        {
            return NativeUIWidget.GetBottomInParent(this.CppObjPtr);
        }

        public float GetRightInParent()
        {
            return NativeUIWidget.GetRightInParent(this.CppObjPtr);
        }

        public float GetTopInParent()
        {
            return NativeUIWidget.GetTopInParent(this.CppObjPtr);
        }

        public new UIWidget this[int index]
        {
            get
            {
                int count = GetChildrenCount();
                if (index < 0 || index >= count)
                {
                    throw new IndexOutOfRangeException("" + count);
                }
                //查找子的指针
                IntPtr scriptObject = NativeNode.GetChildByIndex(this.CppObjPtr, index);
                //如果不是空指针表示存在子
                if (scriptObject != IntPtr.Zero)
                {
                    ///查找相关的对象缓存是否存在
                    UIWidget putNode = scriptManager.GetScriptObject(scriptObject) as UIWidget;
                    if (putNode == null)
                    {
                        //对象类型缓存
                        putNode = GetWidgetNameObject(scriptObject);
                        putNode.Parent = this;
                        scriptManager.PutScriptObject(putNode);
                    }
                    return putNode;
                }
                return null;
            }
        }

        public override CCNode Parent
        {
            set
            {
                if (_parent != null)
                {
                    _parent.scriptManager.RemoveScriptObject(this);
                }
                _parent = value;
                if (_parent != null)
                {
                    _parent.scriptManager.PutScriptObject(this);
                }
                NativeNode.SetParent(this.CppObjPtr, value.CppObjPtr);
            }
            get
            {
                if (_parent != null)
                {
                    return _parent;
                }
                //查找子的指针
                IntPtr scriptObject = NativeNode.GetParent(this.CppObjPtr);
                //如果不是空指针表示存在子
                if (scriptObject != IntPtr.Zero)
                {
                    ///查找相关的对象缓存是否存在
                    CCNode putNode = scriptManager.GetScriptObject(scriptObject) as CCNode;
                    if (putNode == null)
                    {
                        //对象类型缓存
                        putNode = GetWidgetNameObject(scriptObject);
                        putNode._parent = this;
                        scriptManager.PutScriptObject(putNode);
                    }
                    return putNode;
                }
                return null;
            }
        }

        public UIWidget GetWidget(string name)
        {
            //TODO 
            //return new UIWidget(NativeWidget.GetChildByName(this.CppObjPtr, name));
            //查找子的指针
            IntPtr scriptObject = NativeUIWidget.GetChildByName(this.CppObjPtr, name);
            
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                ///查找相关的对象缓存是否存在
                UIWidget putNode = scriptManager.GetScriptObject(scriptObject) as UIWidget;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = GetWidgetNameObject(scriptObject);
                    putNode._parent = this;
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }

            return null;
            /*
            UIWidget widget = null;
            if (scriptObject != IntPtr.Zero)
            {
                if (NativeObject.IsBindMonoObject(scriptObject) && NativeObject.GetMonoObject(scriptObject) != null)
                {
                    object obj = NativeObject.GetMonoObject(scriptObject);
                    if (obj is UIWidget)
                    {
                        widget = (UIWidget)obj;
                    }
                    else
                    {
                        widget = GetWidgetNameObject(scriptObject);  
                    }
                }
                else
                {
                    widget = GetWidgetNameObject(scriptObject);                 
                }
                widget._parent = this;
            }

            return widget;
             */
        }

        public override string ToString()
        {
            return GetType().Name + " " + Name + "@" + CppObjPtr.ToInt32();
        }

        public virtual object Clone()
        {
            //查找子的指针
            IntPtr scriptObject = NativeUIWidget.Clone(this.CppObjPtr);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                UIWidget putNode = GetWidgetNameObject(scriptObject);
                return putNode;
            }
            return null;
        }

        private static UIWidget GetWidgetNameObject(IntPtr scriptObject)
        {
            UIWidget putNode;
            string WidgetName = NativeUIWidget.GetDescription(scriptObject);
            if (WidgetName == DesWidget)
                putNode = new UIWidget(scriptObject);
            else if (WidgetName == DesButton)
                putNode = new UIButton(scriptObject);
            else if (WidgetName == DesCheckBox)
                putNode = new UICheckBox(scriptObject);
            else if (WidgetName == DesImageView)
                putNode = new UIImageView(scriptObject);
            else if (WidgetName == DesLabel)
                putNode = new UILabel(scriptObject);
            else if (WidgetName == DesLabelAtlas)
                putNode = new UILabelAtlas(scriptObject);
            else if (WidgetName == DesLabelBMFont)
                putNode = new UILabelBMFont(scriptObject);
            else if (WidgetName == DesLayout)
                putNode = new UILayout(scriptObject);
            else if (WidgetName == DesLoadingBar)
                putNode = new UILoadingBar(scriptObject);
            else if (WidgetName == DesSlider)
                putNode = new UISlider(scriptObject);
            else if (WidgetName == DesTextField)
                putNode = new UITextField(scriptObject);
            else if (WidgetName == DesListView)
                putNode = new UIListView(scriptObject);
            else if (WidgetName == DesPageView)
                putNode = new UIPageView(scriptObject);
            else if (WidgetName == DesScrollView)
                putNode = new UIScrollView(scriptObject);
            else
                putNode = new UIWidget(scriptObject);
            return putNode;
        }

        public new UIWidget Copy()
        {
            return (UIWidget)Clone();
        }

        public delegate void TouchEventHandle(UIWidget widget, TouchEventType eventType);
        private event TouchEventHandle _TouchEvent;
        public event TouchEventHandle TouchEvent
        {
            add
            {
                _TouchEvent += value;
                initTouchEventListener();
            }
            remove
            {
                _TouchEvent -= value;
            }
        }


        public delegate void TouchedEventHandle(UIWidget widget);

        private event TouchedEventHandle _TouchBeganEvent;
        private event TouchedEventHandle _TouchCanceledEvent;
        private event TouchedEventHandle _TouchEndedEvent;
        private event TouchedEventHandle _TouchMovedEvent;

        public void ClearAllTouchEvent()
        {
            _TouchBeganEvent = null;
            _TouchCanceledEvent = null;
            _TouchEndedEvent = null;
            _TouchMovedEvent = null;
        }

        public event TouchedEventHandle TouchBeganEvent
        {
            add
            {
                _TouchBeganEvent += value;
                initTouchEventListener();
            }
            remove
            {
                _TouchBeganEvent -= value;
            }

        }
        public event TouchedEventHandle TouchCanceledEvent
        {
            add
            {
                _TouchCanceledEvent += value;
                initTouchEventListener();
            }
            remove
            {
                _TouchCanceledEvent -= value;
            }
        }

        public event TouchedEventHandle TouchEndedEvent
        {
            add
            {
                _TouchEndedEvent += value;
                initTouchEventListener();
            }
            remove
            {
                _TouchEndedEvent -= value;
            }
        }
        public event TouchedEventHandle TouchMovedEvent
        {
            add
            {
                _TouchMovedEvent += value;
                initTouchEventListener();
            }
            remove
            {
                _TouchMovedEvent -= value;
            }
        }

        public void AddTouchEventListener(ITouchEventListener listener)
        {
            initTouchEventListener();
            if (listener != null && !touchEventListener.Contains(listener))
            {
                touchEventListener.Add(listener);
            }
            //touchEventListener = listener;
        }

        public void RemoveTouchEventListener(ITouchEventListener listener)
        {
            if (touchEventListener != null && listener != null)
            {
                touchEventListener.Remove(listener);
            }
            //touchEventListener = listener;
        }

        private List<ITouchEventListener> touchEventListener;
        private TouchListenerClass touchListenerObj;
        private void initTouchEventListener()
        {
            if (touchListenerObj == null)
            {
                touchEventListener = new List<ITouchEventListener>(4);
                touchListenerObj = new TouchListenerClass(this);
                //默认带事件
                NativeUIWidget.AddTouchEventListener(this.CppObjPtr, touchListenerObj);
            }
        }

        private class TouchListenerClass
        {
            private UIWidget widget;

            public TouchListenerClass(UIWidget widget)
            {
                this.widget = widget;
            }

            //TODO
            private void TouchListener(int pWidget, TouchEventType eventType)
            {
                if (widget._TouchEvent != null)
                {
                    widget._TouchEvent(widget, eventType);
                }
                switch (eventType)
                {
                    case TouchEventType.TOUCH_EVENT_BEGAN:
                        if (widget._TouchBeganEvent != null)
                        {
                            widget._TouchBeganEvent(widget);
                        }
                        break;
                    case TouchEventType.TOUCH_EVENT_CANCELED:
                        if (widget._TouchCanceledEvent != null)
                        {
                            widget._TouchCanceledEvent(widget);
                        }
                        break;
                    case TouchEventType.TOUCH_EVENT_ENDED:
                        if (widget._TouchEndedEvent != null)
                        {

                            //#region 打印坐标信息

                            //var screenSize = new Vector2(960, 640);
                            //var pos = Vector2.Zero;
                            //if (!widget.IsIgnoreAnchorPointForPosition)
                            //{
                            //    pos = new Vector2(-widget.AnchorPoint.X*widget.Size.width,
                            //                      -widget.AnchorPoint.Y*widget.Size.height);
                            //}
                            //pos = widget.ConvertToWorldSpace(pos);
                            //Console.WriteLine("-----------------------------begin---------------------------------");
                            //Console.WriteLine("宽：" + widget.Size.width + ",高度：" + widget.Size.height);
                            //Console.WriteLine("左下角坐标：" + pos);
                            //Console.WriteLine("左中心坐标：" + (pos - new Vector2(0, screenSize.Y/2)));
                            //Console.WriteLine("左上角坐标：" + (pos - new Vector2(0, screenSize.Y)));

                            //Console.WriteLine("右下角坐标：" + (pos - new Vector2(screenSize.X, 0)));
                            //Console.WriteLine("右中心坐标：" + (pos - new Vector2(screenSize.X, screenSize.Y/2)));
                            //Console.WriteLine("右上角坐标：" + (pos - new Vector2(screenSize.X, screenSize.Y)));

                            //Console.WriteLine("顶中心坐标：" + (pos - new Vector2(screenSize.X/2, screenSize.Y)));
                            //Console.WriteLine("底中心坐标：" + (pos - new Vector2(screenSize.X/2, 0)));
                            //Console.WriteLine("------------------------------end----------------------------------");

                            //#endregion

                            widget._TouchEndedEvent(widget);
                        }
                        break;
                    case TouchEventType.TOUCH_EVENT_MOVED:
                        if (widget._TouchMovedEvent != null)
                        {
                            widget._TouchMovedEvent(widget);
                        }
                        break;
                }
                if (widget.touchEventListener != null)
                {
                    foreach (ITouchEventListener item in widget.touchEventListener)
                    {
                        item.TouchListener(widget, eventType);
                    }
                }
            }
        }

        public PositionType PositionType
        {
            set { NativeUIWidget.SetPositionType(this.CppObjPtr, (int)value); }
            get { return (PositionType)NativeUIWidget.GetPositionType(this.CppObjPtr); }
        }

        public bool FlipX
        {
            set { NativeUIWidget.SetFlipX(this.CppObjPtr, value); }
            get { return NativeUIWidget.IsFlipX(this.CppObjPtr); }
        }

        public bool FlipY
        {
            set { NativeUIWidget.SetFlipY(this.CppObjPtr, value); }
            get { return NativeUIWidget.IsFlipY(this.CppObjPtr); }
        }

        public int Alpha
        {
            set { NativeUIWidget.SetOpacity(this.CppObjPtr, value); }
            get { return NativeUIWidget.GetOpacity(this.CppObjPtr); }
        }

        public Color32 Color
        {
            set 
            { 
                NativeUIWidget.SetColor(this.CppObjPtr, value.R, value.G, value.B);
            }
            get
            {
                Color32 color = default(Color32);
                NativeUIWidget.GetColor(this.CppObjPtr, out color);
                return color;
            }
        }

        public Vector2 GetTouchStartPos()
        {
            Vector2 pos = default(Vector2);
            NativeUIWidget.GetTouchStartPos(this.CppObjPtr, out pos);
            return pos;
        }
        public Vector2 GetTouchMovePos()
        {
            Vector2 pos = default(Vector2);
            NativeUIWidget.GetTouchMovePos(this.CppObjPtr, out pos);
            return pos;
        }
        public Vector2 GetTouchEndPos()
        {
            Vector2 pos = default(Vector2);
            NativeUIWidget.GetTouchEndPos(this.CppObjPtr, out pos);
            return pos;
        }

        public string Name
        {
            set { NativeUIWidget.SetName(this.CppObjPtr, value); }
            get { return NativeUIWidget.GetName(this.CppObjPtr); }
        }

        public WidgetType GetWidgetType()
        {
            return (WidgetType)NativeUIWidget.GetWidgetType(this.CppObjPtr);
        }

        public void SetSize(Size size)
        {
            NativeUIWidget.SetSize(this.CppObjPtr, size.width, size.height);
        }

        public Size GetSize()
        {
            Size size = default(Size);
            NativeUIWidget.GetSize(this.CppObjPtr, out size);
            return size;
        }

        public Size Size
        {
            get { return GetSize(); }
            set { SetSize(value); }
        }

        public SizeType SizeType
        {
            set { NativeUIWidget.SetSizeType(this.CppObjPtr, (int)value); }
            get { return (SizeType)NativeUIWidget.GetSizeType(this.CppObjPtr); }
        }

        public void SetLayoutParameter(UILayoutParameter parameter)
        {
            NativeUIWidget.SetLayoutParameter(this.CppObjPtr, parameter.CppObjPtr);
        }

        public UILayoutParameter GetLayoutParameter(LayoutParameterType _type)
        {
            //查找子的指针
            IntPtr scriptObject = NativeUIWidget.GetLayoutParameter(this.CppObjPtr, (int)_type);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                ///查找相关的对象缓存是否存在
                UILayoutParameter putNode = scriptManager.GetScriptObject(scriptObject) as UILayoutParameter;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new UILayoutParameter(scriptObject);
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;
            //TODO
            //return new UILayoutParameter(NativeUIWidget.GetLayoutParameter(this.CppObjPtr, (int)_type));
        }

        public bool IgnoreContentAdaptWithSize
        {
            set { NativeUIWidget.IgnoreContentAdaptWithSize(this.CppObjPtr, value); }
            get { return NativeUIWidget.IsIgnoreContentAdaptWithSize(this.CppObjPtr); }
        }
        /// <summary>
        /// 获取控件类别的名字
        /// </summary>
        /// <returns></returns>
        public string GetDescription()
        {
            if (_desciption == null) _desciption = NativeUIWidget.GetDescription(this.CppObjPtr);
            return _desciption;
        }

        //public delegate void AddChildDelegate();
        //public event void AddChildEvent;

        public void AddChild(UIWidget node)
        {
            base.AddChild(node);
        }

        public void AddChild(UIWidget node, int zOrder)
        {
            base.AddChild(node, zOrder);
        }

        public void AddChild(UIWidget node, int zOrder, int tag)
        {
            base.AddChild(node, zOrder, tag);
        }

        public void AddNode(CCNode node)
        {
            node._parent = this;
            scriptManager.PutScriptObject(node);
            NativeUIWidget.AddNode(this.CppObjPtr, node.CppObjPtr);
        }

        public void AddNode(CCNode node, int zOrder)
        {
            node._parent = this;
            scriptManager.PutScriptObject(node);
            NativeUIWidget.AddNodeWithzOrder(this.CppObjPtr, node.CppObjPtr, zOrder);
        }

        public void AddNode(CCNode node, int zOrder, int tag)
        {
            node._parent = this;
            scriptManager.PutScriptObject(node);
            NativeUIWidget.AddNodeWithzOrderAndTag(this.CppObjPtr, node.CppObjPtr, zOrder, tag);
        }

        public CCNode GetNode(int tag)
        {
            //查找子的指针
            IntPtr scriptObject = NativeUIWidget.GetNodeByTag(this.CppObjPtr, tag);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                ///查找相关的对象缓存是否存在
                CCNode putNode = scriptManager.GetScriptObject(scriptObject) as CCNode;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = GetWidgetNameObject(scriptObject);
                    putNode._parent = this;
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;
        }

        public CCNode GetNodeByIndex(int index)
        {

            //查找子的指针
            IntPtr scriptObject = NativeUIWidget.GetNodeByIndex(this.CppObjPtr, index);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                ///查找相关的对象缓存是否存在
                CCNode putNode = scriptManager.GetScriptObject(scriptObject) as CCNode;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new CCNode(scriptObject);
                    putNode._parent = this;
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;

        }

        public void RemoveNode(CCNode node)
        {
            node._parent = null;
            scriptManager.RemoveScriptObject(node);
            NativeUIWidget.RemoveNode(this.CppObjPtr, node.CppObjPtr);
        }

        public void RemoveNode(int tag)
        {
            var node = GetNode(tag);
            if(node != null)
            {
                node._parent = null;
                scriptManager.RemoveScriptObject(node);
            }
            NativeUIWidget.RemoveNodeByTag(this.CppObjPtr, tag);
        }

        public void RemoveAllNodes()
        {
            NativeUIWidget.RemoveAllNodes(this.CppObjPtr);
        }

        public void SetEffectNode(CCNode node)
        {
            NativeUIWidget.SetEffectNode(this.CppObjPtr, node.CppObjPtr);
        }

        private const string DesWidget = "Widget";
        private const string DesButton = "Button";
        private const string DesCheckBox = "CheckBox";
        private const string DesImageView = "ImageView";
        private const string DesLabel = "Label";
        private const string DesLabelAtlas = "LabelAtlas";
        private const string DesLabelBMFont = "LabelBMFont";
        private const string DesLayout = "Layout";
        private const string DesLoadingBar = "LoadingBar";
        private const string DesSlider = "Slider";
        private const string DesTextField = "TextField";
        private const string DesListView = "ListView";
        private const string DesPageView = "PageView";
        private const string DesScrollView = "ScrollView";

        private string _desciption = null;
    }

}
