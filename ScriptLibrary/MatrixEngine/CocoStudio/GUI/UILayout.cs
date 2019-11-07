using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public enum LayoutBackGroundColorType
    {
        LAYOUT_COLOR_NONE,
        LAYOUT_COLOR_SOLID,
        LAYOUT_COLOR_GRADIENT
    };

    public enum LayoutType
    {
        LAYOUT_ABSOLUTE,
        LAYOUT_LINEAR_VERTICAL,
        LAYOUT_LINEAR_HORIZONTAL,
        LAYOUT_RELATIVE
    };

    public class UILayout : UIWidget
    {
        internal UILayout(IntPtr t) : base(t) { }
        public UILayout()
            : base(NativeUILayout.Create())
        {
            //this.CppObjPtr = NativeUILayout.Create();
            //Console.WriteLine("UILayout" + this.RetainCount());
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        public void SetBackGroundImage(string fileName)
        {
            NativeUILayout.SetBackGroundImage(this.CppObjPtr,fileName,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void SetBackGroundImage(string fileName,TextureResType textTpye)
        {
            NativeUILayout.SetBackGroundImage(this.CppObjPtr, fileName, textTpye);
        }

        public void SetBackGroundImageCapInsets(Rect rect)
        {
            NativeUILayout.SetBackGroundImageCapInsets(this.CppObjPtr,rect.origin.X,rect.origin.Y,rect.size.width,rect.size.height);
        }

        public void SetBackGroundColorType(LayoutBackGroundColorType _type)
        {
            NativeUILayout.SetBackGroundColorType(this.CppObjPtr,(int)_type);
        }
        public void SetBackGroundColorS(Color32 startColor)
        {
            NativeUILayout.SetBackGroundColorS(this.CppObjPtr,ref startColor);
        }
        public void SetBackGroundColorSE(Color32 startColor,Color32 endColor)
        {
            NativeUILayout.SetBackGroundColorSE(this.CppObjPtr,ref startColor,ref endColor);
        }

        public void SetBackGroundColorOpacity(int opacity)
        {
            NativeUILayout.SetBackGroundColorOpacity(this.CppObjPtr,opacity);
        }
        public void SetBackGroundColorVector(Vector2 vector)
        {
            NativeUILayout.SetBackGroundColorVector(this.CppObjPtr,vector);
        }
        public void RemoveBackGroundImage()
        {
            NativeUILayout.RemoveBackGroundImage(this.CppObjPtr);
        }
        public void GetBackGroundImageTextureSize()
        {
            Size size = default(Size);
            NativeUILayout.GetBackGroundImageTextureSize(this.CppObjPtr,out size);
        }
        public bool ClippingEnable
        {
            set{ NativeUILayout.SetClippingEnabled(this.CppObjPtr,value); }
            get{ return NativeUILayout.IsClippingEnabled(this.CppObjPtr); }
        }

        public void SetLayoutType(LayoutType _type)
        {
            NativeUILayout.SetLayoutType(this.CppObjPtr,(int)_type);
        }
        public LayoutType GetLayoutType()
        {
            return (LayoutType)NativeUILayout.GetLayoutType(this.CppObjPtr);
        }
    }
}
