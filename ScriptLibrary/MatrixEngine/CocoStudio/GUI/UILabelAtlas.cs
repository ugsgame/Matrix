using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public class UILabelAtlas:UIWidget
    {
        internal UILabelAtlas(IntPtr t) : base(t) { }
        public UILabelAtlas()
            : base(NativeUILabelAtlas.Create())
        {
            //this.CppObjPtr = NativeUILabelAtlas.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public string Text
        {
            get { return GetText(); }
            set { SetText(value); }
        }

        public void SetProperty(string stringValue, string charMapFile, int itemWidth, int itemHeight, string startCharMap)
        {
            NativeUILabelAtlas.SetProperty(this.CppObjPtr, stringValue, charMapFile, itemWidth, itemHeight, startCharMap);
        }

        public void SetText(string stringValue)
        {
            NativeUILabelAtlas.SetText(this.CppObjPtr, stringValue);
        }

        public string GetText()
        {
            return NativeUILabelAtlas.GetText(this.CppObjPtr);
        }
    }
}
