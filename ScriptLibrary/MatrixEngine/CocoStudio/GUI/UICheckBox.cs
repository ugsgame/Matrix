using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public enum CheckBoxEventType
    {
        CHECKBOX_STATE_EVENT_SELECTED,
        CHECKBOX_STATE_EVENT_UNSELECTED
    };

    public class UICheckBox : UIWidget
    {
        internal UICheckBox(IntPtr t) : base(t) { }
        public UICheckBox()
            : base(NativeUICheckBox.Create())
        {
            //this.CppObjPtr = NativeUICheckBox.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);

            TouchEnabled = true;
        }

        public void LoadTextures(string backGround, string backGroundSelected, string cross, string backGroundDisabled, string frontCrossDisabled)
        {
            NativeUICheckBox.LoadTextures(this.CppObjPtr, backGround, backGroundSelected, cross, backGroundDisabled, frontCrossDisabled,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadTextures(string backGround, string backGroundSelected, string cross, string backGroundDisabled, string frontCrossDisabled,TextureResType textType)
        {
            NativeUICheckBox.LoadTextures(this.CppObjPtr, backGround, backGroundSelected, cross, backGroundDisabled, frontCrossDisabled, textType);
        }

        public void LoadTextureBackGround(string backGround)
        {
            NativeUICheckBox.LoadTextureBackGround(this.CppObjPtr, backGround,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadTextureBackGround(string backGround,TextureResType textType)
        {
            NativeUICheckBox.LoadTextureBackGround(this.CppObjPtr, backGround, textType);
        }

        public void LoadTextureBackGroundSelected(string backGroundSelected)
        {
            NativeUICheckBox.LoadTextureBackGroundSelected(this.CppObjPtr,backGroundSelected,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadTextureBackGroundSelected(string backGroundSelected,TextureResType textType)
        {
            NativeUICheckBox.LoadTextureBackGroundSelected(this.CppObjPtr, backGroundSelected, textType);
        }

        public void LoadTextureFrontCross(string cross)
        {
            NativeUICheckBox.LoadTextureFrontCross(this.CppObjPtr, cross,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadTextureFrontCross(string cross,TextureResType textType)
        {
            NativeUICheckBox.LoadTextureFrontCross(this.CppObjPtr, cross,textType);
        }

        public void LoadTextureBackGroundDisabled(string backGroundDisabled)
        {
            NativeUICheckBox.LoadTextureBackGroundDisabled(this.CppObjPtr, backGroundDisabled,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadTextureBackGroundDisabled(string backGroundDisabled,TextureResType textType)
        {
            NativeUICheckBox.LoadTextureBackGroundDisabled(this.CppObjPtr, backGroundDisabled,textType);
        }

        public void LoadTextureFrontCrossDisabled(string frontCrossDisabled)
        {
            NativeUICheckBox.LoadTextureFrontCrossDisabled(this.CppObjPtr, frontCrossDisabled,TextureResType.UI_TEX_TYPE_LOCAL);
        }

        public void LoadTextureFrontCrossDisabled(string frontCrossDisabled,TextureResType textType)
        {
            NativeUICheckBox.LoadTextureFrontCrossDisabled(this.CppObjPtr, frontCrossDisabled, textType);
        }

        public bool SelectedState
        {
            set { NativeUICheckBox.SetSelectedState(this.CppObjPtr, value); }
            get { return NativeUICheckBox.GetSelectedState(this.CppObjPtr); }
        }

        public void AddEventListenerCheckBox(ICheckBoxEventListener _event)
        {
            NativeUICheckBox.AddEventListenerCheckBox(this.CppObjPtr, _event);
        }
    }
}
