using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public class UITextField : UIWidget
    {
        internal UITextField(IntPtr t) : base(t) { }
        public UITextField()
            : base(NativeUITextField.Create())
        {
            //this.CppObjPtr = NativeUITextField.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public void SetTouchSize(Size size)
        {
            NativeUITextField.SetTouchSize(this.CppObjPtr, size.width, size.height);
        }

        public void SetText(string text)
        {
            NativeUITextField.SetText(this.CppObjPtr, text);
        }

        public void SetPlaceHolder(string value)
        {
            NativeUITextField.SetPlaceHolder(this.CppObjPtr, value);
        }

        public void SetFontSize(int size)
        {
            NativeUITextField.SetFontSize(this.CppObjPtr, size);
        }

        public void SetFontName(string name)
        {
            NativeUITextField.SetFontName(this.CppObjPtr, name);
        }

        public void DidNotSelectSelf()
        {
            NativeUITextField.DidNotSelectSelf(this.CppObjPtr);
        }
        public string GetStringValue()
        {
            return NativeUITextField.GetStringValue(this.CppObjPtr);
        }

        public bool MaxLengthEnabled
        {
            set { NativeUITextField.SetMaxLengthEnabled(this.CppObjPtr, value); }
            get { return NativeUITextField.IsMaxLengthEnabled(this.CppObjPtr); }
        }

        public int MaxLength
        {
            set { NativeUITextField.SetMaxLength(this.CppObjPtr, value); }
            get { return NativeUITextField.GetMaxLength(this.CppObjPtr); }
        }

        public bool PasswordEnabled
        {
            set { NativeUITextField.SetPasswordEnabled(this.CppObjPtr, value); }
            get { return NativeUITextField.IsPasswordEnabled(this.CppObjPtr); }
        }

        public void SetPasswordStyleText(string styleText)
        {
            NativeUITextField.SetPasswordStyleText(this.CppObjPtr, styleText);
        }

        public bool AttachWithIME
        {
            set { NativeUITextField.SetAttachWithIME(this.CppObjPtr, value); }
            get { return NativeUITextField.GetAttachWithIME(this.CppObjPtr); }
        }

        public bool DetachWithIME
        {
            set { NativeUITextField.SetDetachWithIME(this.CppObjPtr, value); }
            get { return NativeUITextField.GetDetachWithIME(this.CppObjPtr); }
        }

        public bool InsertText
        {
            set { NativeUITextField.SetInsertText(this.CppObjPtr, value); }
            get { return NativeUITextField.GetInsertText(this.CppObjPtr); }
        }

        public bool DeleteBackward
        {
            set { NativeUITextField.SetDeleteBackward(this.CppObjPtr, value); }
            get { return NativeUITextField.GetDeleteBackward(this.CppObjPtr); }
        }
        private ListenerClass listenerObj;
        public void AddEventListenerTextField(ITextFieldEventListener listener)
        {
            if (listenerObj == null)
            {
                
                touchEventListener = new List<ITextFieldEventListener>();
                listenerObj = new ListenerClass(touchEventListener);
                NativeUITextField.AddEventListenerTextField(this.CppObjPtr, listenerObj);
            }
            if (listener != null && !touchEventListener.Contains(listener))
            {
                touchEventListener.Add(listener);
            }
        }

        public void RemoveEventListenerTextField(ITextFieldEventListener listener)
        {
            if (listener != null && touchEventListener != null)
            {
                touchEventListener.Remove(listener);
            }
        }


        private List<ITextFieldEventListener> touchEventListener;

        private class ListenerClass : ITextFieldEventListener
        {
            private List<ITextFieldEventListener> touchEventListener;
            public ListenerClass(List<ITextFieldEventListener> touchEventListener)
            {
                this.touchEventListener = touchEventListener;
            }

            public void TextFieldListener(TextFiledEventType eventType)
            {
                foreach (ITextFieldEventListener item in touchEventListener)
                {
                    item.TextFieldListener(eventType);
                }
            }
        }
    }

    public enum TextFiledEventType
    {
        TEXTFIELD_EVENT_ATTACH_WITH_IME,
        TEXTFIELD_EVENT_DETACH_WITH_IME,
        TEXTFIELD_EVENT_INSERT_TEXT,
        TEXTFIELD_EVENT_DELETE_BACKWARD,
    }
}
