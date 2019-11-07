using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public class UISlider : UIWidget
    {
        internal UISlider(IntPtr t) : base(t) { }
        public UISlider()
            : base(NativeUISlider.Create())
        {
            //this.CppObjPtr = NativeUISlider.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public void LoadBarTexture(string fileName)
        {
            NativeUISlider.LoadBarTexture(this.CppObjPtr, fileName,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadBarTexture(string fileName,TextureResType textType)
        {
            NativeUISlider.LoadBarTexture(this.CppObjPtr, fileName, textType);
        }

        public void SetCapInsets(Rect rect)
        {
            NativeUISlider.SetCapInsets(this.CppObjPtr, rect.origin.X, rect.origin.Y, rect.size.width, rect.size.height);
        }

        public void SetCapInsetsBarRenderer(Rect rect)
        {
            NativeUISlider.SetCapInsetsBarRenderer(this.CppObjPtr, rect.origin.X, rect.origin.Y, rect.size.width, rect.size.height);
        }

        public void SetCapInsetProgressBarRebderer(Rect rect)
        {
            NativeUISlider.SetCapInsetProgressBarRebderer(this.CppObjPtr, rect.origin.X, rect.origin.Y, rect.size.width, rect.size.height);
        }

        public void LoadSlidBallTextures(string normal, string pressed, string disabled)
        {
            NativeUISlider.LoadSlidBallTextures(this.CppObjPtr, normal, pressed, disabled,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadSlidBallTextures(string normal, string pressed, string disabled,TextureResType textType)
        {
            NativeUISlider.LoadSlidBallTextures(this.CppObjPtr, normal, pressed, disabled, textType);
        }

        public void LoadSlidBallTextureNormal(string normal)
        {
            NativeUISlider.LoadSlidBallTextureNormal(this.CppObjPtr, normal,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadSlidBallTextureNormal(string normal,TextureResType textType)
        {
            NativeUISlider.LoadSlidBallTextureNormal(this.CppObjPtr, normal, textType);
        }

        public void LoadSlidBallTexturePressed(string pressed)
        {
            NativeUISlider.LoadSlidBallTexturePressed(this.CppObjPtr, pressed,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadSlidBallTexturePressed(string pressed, TextureResType textType)
        {
            NativeUISlider.LoadSlidBallTexturePressed(this.CppObjPtr, pressed, textType);
        }

        public void LoadSlidBallTextureDisabled(string disabled)
        {
            NativeUISlider.LoadSlidBallTextureDisabled(this.CppObjPtr, disabled,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadSlidBallTextureDisabled(string disabled,TextureResType textType)
        {
            NativeUISlider.LoadSlidBallTextureDisabled(this.CppObjPtr, disabled, textType);
        }

        public void LoadProgressBarTexture(string fileName)
        {
            NativeUISlider.LoadProgressBarTexture(this.CppObjPtr, fileName,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadProgressBarTexture(string fileName,TextureResType textType)
        {
            NativeUISlider.LoadProgressBarTexture(this.CppObjPtr, fileName, textType);
        }

        public int Percent
        {
            set { NativeUISlider.SetPercent(this.CppObjPtr, value); }
            get { return NativeUISlider.GetPercent(this.CppObjPtr); }
        }

        public void AddEventListenerSlider(ISliderEventListener listener)
        {
            NativeUISlider.AddEventListenerSlider(this.CppObjPtr, listener);
        }

        public void SetScale9Enabled(bool able)
        {
            NativeUISlider.SetScale9Enabled(this.CppObjPtr, able);
        }
    }
}
