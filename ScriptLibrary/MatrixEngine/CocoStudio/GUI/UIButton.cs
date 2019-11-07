using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public class UIButton : UIWidget
    {
        internal UIButton(IntPtr t) : base(t) { }
        public UIButton()
            : base(NativeUIButton.Create())
        {
            //this.CppObjPtr = NativeUIButton.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
            TouchEnabled = true;
        }

        public void LoadTextures(string normal, string selected, string disabled)
        {
            NativeUIButton.LoadTextures(this.CppObjPtr, normal, selected, disabled,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadTextures(string normal, string selected, string disabled,TextureResType textType)
        {
//             Console.WriteLine("UIButton Texture key:" + normal);
//             Console.WriteLine("UIButton Texture key:" + selected);
//             Console.WriteLine("UIButton Texture key:" + disabled);

            NativeUIButton.LoadTextures(this.CppObjPtr, normal, selected, disabled, textType);
        }

        public void LoadTextureNormal(string normal)
        {
            NativeUIButton.LoadTextureNormal(this.CppObjPtr, normal,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadTextureNormal(string normal,TextureResType textType)
        {
//            Console.WriteLine("UIButton Texture key:" + normal);
            NativeUIButton.LoadTextureNormal(this.CppObjPtr, normal, textType);
        }

        public void LoadTexturePressed(string selected)
        {
            NativeUIButton.LoadTexturePressed(this.CppObjPtr, selected, TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadTexturePressed(string selected,TextureResType textType)
        {
 //           Console.WriteLine("UIButton Texture key:" + selected);
            NativeUIButton.LoadTexturePressed(this.CppObjPtr, selected, textType);
        }

        public void LoadTextureDisabled(string disabled)
        {
            NativeUIButton.LoadTextureDisabled(this.CppObjPtr, disabled,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadTextureDisabled(string disabled,TextureResType textType)
        {
//            Console.WriteLine("UIButton Texture key:" + disabled);
            NativeUIButton.LoadTextureDisabled(this.CppObjPtr, disabled, textType);
        }

        public void SetCapInsets(Rect rect)
        {
            NativeUIButton.SetCapInsets(this.CppObjPtr,rect.origin.X,rect.origin.Y,rect.size.width,rect.size.height);
        }
        public void SetCapInsetsNormalRenderer(Rect rect)
        {
            NativeUIButton.SetCapInsetsNormalRenderer(this.CppObjPtr,rect.origin.X,rect.origin.Y,rect.size.width,rect.size.height);
        }
        public void SetCapInsetsDisabledRenderer(Rect rect)
        {
            NativeUIButton.SetCapInsetsDisabledRenderer(this.CppObjPtr,rect.origin.X,rect.origin.Y,rect.size.width,rect.size.height);
        }
        public void SetCapInsetsPressedRenderer(Rect rect)
        {
            NativeUIButton.SetCapInsetsPressedRenderer(this.CppObjPtr,rect.origin.X,rect.origin.Y,rect.size.width,rect.size.height);
        }
        public void SetPressedActionEnabled(bool enabled)
        {
            NativeUIButton.SetPressedActionEnabled(this.CppObjPtr,enabled);
        }

        public string TitleText
        {
            set{ NativeUIButton.SetTitleText(this.CppObjPtr,value); }
            get{ return NativeUIButton.GetTitleText(this.CppObjPtr); }
        }

        public Color32 TitleColor
        {
            set { NativeUIButton.SetTitleColor(this.CppObjPtr, value.R, value.G, value.B); }
            get 
            {
                Color32 result = default(Color32);
                NativeUIButton.GetTitleColor(this.CppObjPtr,out result);
                return result;
            }
        }

        public float TitleFontSize
        {
            set{ NativeUIButton.SetTitleFontSize(this.CppObjPtr,value); }
            get{ return NativeUIButton.GetTitleFontSize(this.CppObjPtr); }
        }

        public string TitleFontName
        {
            set{ NativeUIButton.SetTitleFontName(this.CppObjPtr,value); }
            get{ return NativeUIButton.GetTitleFontName(this.CppObjPtr); }
        }

        public void SetScale9Enabled(bool able)
        {
            NativeUIButton.SetScale9Enabled(this.CppObjPtr, able);
        }
    }
}
