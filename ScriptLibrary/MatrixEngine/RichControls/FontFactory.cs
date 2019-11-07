using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Native;
using MatrixEngine.RichControls.Native;
using MatrixEngine.Math;

namespace MatrixEngine.RichControls
{
    public class FontFactory
    {
        private static ScriptManager _scriptManager = null;
        static ScriptManager scriptManager
        {
            get
            {
                if(_scriptManager==null)
                    _scriptManager = new ScriptManager();
                return _scriptManager;
            }
        }
        public enum EFontStyle
        {
	        e_plain,
	        e_strengthen,
	        e_border,
	        e_shadow
        };

        public static FontCatalog CreateFont(string alias,string font_name, Color32 color, int size_pt)
        {
            return CreateFont(alias, font_name, color, size_pt, EFontStyle.e_plain, 1.0f, 0xff000000, 0, 96);
        }

        public static FontCatalog CreateFont(string alias, string font_name, Color32 color, int size_pt, FontFactory.EFontStyle style, float strength, Color32 secondary_color, int faceidx, int ppi)
        {
            IntPtr scriptObject = NativeFontFactory.Create(alias, font_name, color.PackedValueLittleEndian, size_pt, style, strength, secondary_color.PackedValueLittleEndian, faceidx, ppi);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                ///查找相关的对象缓存是否存在
                FontCatalog putNode = scriptManager.GetScriptObject(scriptObject) as FontCatalog;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new FontCatalog(scriptObject);
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;
        }

        public static FontCatalog FindFont(string alias)
        {
            return FindFont(alias);
        }

        public static FontCatalog FindFont(string alias, bool no_fail)
        {
            IntPtr scriptObject = NativeFontFactory.Find(alias,true);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                ///查找相关的对象缓存是否存在
                FontCatalog putNode = scriptManager.GetScriptObject(scriptObject) as FontCatalog;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new FontCatalog(scriptObject);
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;
        }

        public static FontCatalog Another_Alias(string another_alias, string origin_alias)
        {
            IntPtr scriptObject = NativeFontFactory.Another_Alias(another_alias,origin_alias);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                ///查找相关的对象缓存是否存在
                FontCatalog putNode = scriptManager.GetScriptObject(scriptObject) as FontCatalog;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new FontCatalog(scriptObject);
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;
        }
    }
}
