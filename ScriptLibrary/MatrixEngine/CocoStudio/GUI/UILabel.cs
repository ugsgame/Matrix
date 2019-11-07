using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;
using MatrixEngine.Cocos2d;

namespace MatrixEngine.CocoStudio.GUI
{
    public class UILabel : UIWidget
    {
        internal UILabel(IntPtr t) : base(t) { }
        public UILabel()
            : base(NativeUILabel.Create())
        {
            //this.CppObjPtr = NativeUILabel.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public string Text
        {
            get { return GetText(); }
            set { SetText(value); }
        }

        public int FontSize
        {
            get { return GetFontSize(); }
            set { SetFontSize(value); }
        }

        public string FontName
        {
            get { return GetFontName(); }
            set { SetFontName(value); }
        }

        public void SetText(string text)
        {
            NativeUILabel.SetText(this.CppObjPtr, text);
        }

        public string GetText()
        {
            return NativeUILabel.GetText(this.CppObjPtr);
        }

        public int GetStringLength()
        {
            return NativeUILabel.GetStringLength(this.CppObjPtr);
        }

        public void SetFontSize(int size)
        {
            NativeUILabel.SetFontSize(this.CppObjPtr, size);
        }

        public int GetFontSize()
        {
            return  NativeUILabel.GetFontSize(this.CppObjPtr);
        }

        public void SetFontName(string name)
        {
            NativeUILabel.SetFontName(this.CppObjPtr, name);
        }

        public string GetFontName()
        {
            return NativeUILabel.GetFontName(this.CppObjPtr);
        }

        public bool TouchScaleChangeEnabled
        {
            set { NativeUILabel.SetTouchScaleChangeEnabled(this.CppObjPtr, value); }
            get { return NativeUILabel.IsTouchScaleChangeEnabled(this.CppObjPtr); }
        }

        public void SetTextAreaSize(Size areaSize)
        {
            NativeUILabel.SetTextAreaSize(this.CppObjPtr, areaSize.width, areaSize.height);
        }

        public void SetTextHorizontalAlignment(CCTextAlignment alignment)
        {
            NativeUILabel.SetTextHorizontalAlignment(this.CppObjPtr, alignment);
        }

        public void SetTextVerticalAlignment(CCVerticalTextAlignment alignment)
        {
            NativeUILabel.SetTextVerticalAlignment(this.CppObjPtr, alignment);
        }
    }
}
