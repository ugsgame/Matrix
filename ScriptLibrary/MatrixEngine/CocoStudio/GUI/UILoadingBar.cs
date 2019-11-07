using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public enum LoadingBarType
    {
        LoadingBarTypeLeft,
        LoadingBarTypeRight
    };

    public class UILoadingBar : UIWidget
    {
        internal UILoadingBar(IntPtr t) : base(t) { }
        public UILoadingBar()
            : base(NativeUILoadingBar.Create())
        {
            //this.CppObjPtr = NativeUILoadingBar.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public LoadingBarType Direction
        {
            set { NativeUILoadingBar.SetDirection(this.CppObjPtr, (int)value); }
            get { return (LoadingBarType)NativeUILoadingBar.GetDirection(this.CppObjPtr); }
        }

        public void LoadTexture(string texture)
        {
            NativeUILoadingBar.LoadTexture(this.CppObjPtr, texture,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadTexture(string texture,TextureResType textType)
        {
            NativeUILoadingBar.LoadTexture(this.CppObjPtr, texture, textType);
        }

        public int Percent
        {
            set { NativeUILoadingBar.SetPercent(this.CppObjPtr, value); }
            get { return NativeUILoadingBar.GetPercent(this.CppObjPtr); }
        }
        public void SetCapInsets(Rect rect)
        {
            NativeUILoadingBar.SetCapInsets(this.CppObjPtr, rect.origin.X, rect.origin.Y, rect.size.width, rect.size.height);
        }
    }
}
