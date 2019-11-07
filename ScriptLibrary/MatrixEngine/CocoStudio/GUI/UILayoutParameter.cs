using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;
using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.GUI
{
    public enum LayoutParameterType
    {
        LAYOUT_PARAMETER_NONE,
        LAYOUT_PARAMETER_LINEAR,
        LAYOUT_PARAMETER_RELATIVE
    };

    public class UILayoutParameter : CCObject
    {
        internal UILayoutParameter(IntPtr t) : base(t) { }
        public UILayoutParameter()
            : base(NativeUILayoutParameter.Create())
        {
            //this.CppObjPtr = NativeUILayoutParameter.Create();

            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }

        //         public void SetMargin()
        //         {
        // 
        //         }
        // 
        //         public void GetMargin()
        //         {
        // 
        //         }

        public LayoutParameterType GetLayoutType()
        {
            return (LayoutParameterType)NativeUILayoutParameter.GetLayoutType(this.CppObjPtr);
        }

    }
}
