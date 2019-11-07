using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public class UILabelBMFont : UIWidget
    {
        internal UILabelBMFont(IntPtr t) : base(t) { }
        public UILabelBMFont()
            : base(NativeUILabelBMFont.Create())
        {
            //this.CppObjPtr = NativeUILabelBMFont.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public void SetFontFile(string fileName)
        {
            NativeUILabelBMFont.SetFontFile(this.CppObjPtr, fileName);
        }

        public string Text
        {
            get { return GetText(); }
            set { SetText(value); }
        }

        public void SetText(string text)
        {
            NativeUILabelBMFont.SetText(this.CppObjPtr, text);
        }

        public string GetText()
        {
            return NativeUILabelBMFont.GetText(this.CppObjPtr);
        }
    }
}
