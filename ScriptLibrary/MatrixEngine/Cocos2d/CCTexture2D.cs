using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCTexture2D : CCObject
    {
        internal CCTexture2D(IntPtr t) : base(t) { }
        public CCTexture2D()
            : base(NativeTexture2D.Create())
        {

        }
        //TODO:createwith ccimage
//      public bool InitWithImage(CCImage image)
//      {
// 
//      }
        //PVRFile or ETCFile,根据不同的后缀来判断
        public bool InitWithString(string text, string fontName, float fontSize, Size dimensions, CCTextAlignment hAlignment, CCVerticalTextAlignment vAlignment)
        {
            return NativeTexture2D.InitWithString(text, fontName, fontSize, ref dimensions, hAlignment, vAlignment);
        }

        public bool InitWithPVRFile(string file)
        {
            return NativeTexture2D.InitWithPVRFile(file);
        }

        public bool InitWithETCFile(string file)
        {
            return NativeTexture2D.InitWithETCFile(file);
        }

    }
}
