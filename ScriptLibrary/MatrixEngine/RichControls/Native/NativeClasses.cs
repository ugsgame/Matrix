using MatrixEngine.Math;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;

namespace MatrixEngine.RichControls.Native
{
    internal static class NativeLabelHTML
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create(); 
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateWithString(string utf8_str,ref Size preferred_size, string font_alias);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetString(IntPtr label,string str);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetString(IntPtr label);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AppendString(IntPtr label,string str);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RegisterListener(IntPtr label,CCLabelHTML listener);
    }

    internal static class NativeFontFactory
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create(string alias, string font_name, uint color, int size_pt, FontFactory.EFontStyle style, float strength, uint secondary_color, int faceidx, int ppi);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Find(string alias, bool no_fail);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Another_Alias(string another_alias, string origin_alias);
    }

    internal static class NativeFontCatalog
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void  Flush(IntPtr fontcatalog);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int Char_Width(IntPtr fontcatalog);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int Char_Height(IntPtr fontcatalog);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool Add_HackFont(IntPtr fontcatalog,string fontname,int shift_y);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool Add_HackFontWithFaceIndex(IntPtr fontcatalog, string fontname, long face_idx, int shift_y);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool Dump_Textures(IntPtr fontcatalog,string prefix);
    }
}
