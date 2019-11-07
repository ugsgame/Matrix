using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public class UIImageView : UIWidget
    {
        internal UIImageView(IntPtr t) : base(t) { }
        public UIImageView()
            : base(NativeUIImageView.Create())
        {
            //this.CppObjPtr = NativeUIImageView.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public void LoadTexture(string fileName)
        {
            NativeUIImageView.LoadTexture(this.CppObjPtr, fileName, TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadTexture(string fileName,TextureResType textType)
        {
            //Console.WriteLine("UIImageView Texture key:" + fileName);
            NativeUIImageView.LoadTexture(this.CppObjPtr, fileName, textType);
        }

        public void SetTextureRect(Rect rect)
        {
            NativeUIImageView.SetTextureRect(this.CppObjPtr, rect.origin.X, rect.origin.Y, rect.size.width, rect.size.height);
        }

        public void SetScale9Enabled(bool able)
        {
            NativeUIImageView.SetScale9Enabled(this.CppObjPtr, able);
        }
    }
}
