
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Native;
using MatrixEngine.RichControls.Native;

namespace MatrixEngine.RichControls
{
    public class FontCatalog:Base
    {
        internal FontCatalog(IntPtr ptr)
            : base(ptr)
        {

        }
        //TODO:
        public FontCatalog()
            : base(IntPtr.Zero)
        {

        }

        public void Flush()
        {
            NativeFontCatalog.Flush(this.CppObjPtr);
        }

        public int Char_Width()
        {
            return NativeFontCatalog.Char_Width(this.CppObjPtr);
        }

        public int Char_Height()
        {
            return NativeFontCatalog.Char_Height(this.CppObjPtr);
        }

        public bool Add_HackFont(string fontname)
        {
            return NativeFontCatalog.Add_HackFont(this.CppObjPtr,fontname, 0);
        }
        public bool Add_HackFont(string fontname, int shift_y)
        {
            return NativeFontCatalog.Add_HackFont(this.CppObjPtr, fontname, shift_y);
        }

        public bool Add_HackFont(string fontname, int shift_y, long face_idx)
        {
            return NativeFontCatalog.Add_HackFontWithFaceIndex(this.CppObjPtr, fontname, face_idx , shift_y);
        }

        public void Dump_Textures(string prefix)
        {
            NativeFontCatalog.Dump_Textures(this.CppObjPtr, prefix);
        }
    }
}
