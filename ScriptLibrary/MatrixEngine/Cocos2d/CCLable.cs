using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCLabelAtlas : CCNodeRGBA
    {
        internal CCLabelAtlas(IntPtr t) : base(t) { }
        public CCLabelAtlas(string _string, string fntFile):base(NativeLabel.CreateLabelAtlasWithFont(_string, fntFile))
        {
        }
        public CCLabelAtlas(string _string, string charMapFile, int itemWidth, int itemHeight, int starCharMap):
            base(NativeLabel.CreateLabelAtlasWithMap(_string, charMapFile,itemWidth,itemHeight,starCharMap))
        {
        }

        public string String
        {
            get { return NativeLabel.GetString_Atlas(this.CppObjPtr); }
            set { NativeLabel.SetString_Atlas(this.CppObjPtr, value); }
        }
    }

    public class CCLabelBMFont : CCNode
    {
        internal CCLabelBMFont(IntPtr t) : base(t) { }
        public CCLabelBMFont(string _string, string fintFile, float width)
        {
            Vector2 offset = new Vector2();
            this.CppObjPtr = NativeLabel.CreateLabelBMFont(_string, fintFile,width,CCTextAlignment.kCCTextAlignmentLeft,ref offset);
        }
        public CCLabelBMFont(string _string, string fintFile, float width, CCTextAlignment alignment, Vector2 imgOffset)
        {
            this.CppObjPtr = NativeLabel.CreateLabelBMFont(_string, fintFile, width, alignment, ref imgOffset);
        }

        public string String
        {
            get { return NativeLabel.GetString_BMFont(this.CppObjPtr); }
            set { NativeLabel.SetString_BMFont(this.CppObjPtr, value); }
        }
    }

    public class CCLabelTTF:CCSprite
    {
        internal CCLabelTTF(IntPtr t) : base(t) { }
        public CCLabelTTF(string _string, float fontSize)
        {
            Size dimensions = new Size();
            this.CppObjPtr = NativeLabel.CreateLabelTTF(_string, "", fontSize, ref dimensions, CCTextAlignment.kCCTextAlignmentCenter, CCVerticalTextAlignment.kCCVerticalTextAlignmentTop);
        }
        public CCLabelTTF(string _string, string fontName, float fontSize)
        {
            Size dimensions = new Size();
            this.CppObjPtr = NativeLabel.CreateLabelTTF(_string, fontName, fontSize, ref dimensions, CCTextAlignment.kCCTextAlignmentCenter, CCVerticalTextAlignment.kCCVerticalTextAlignmentTop);
        }

        public CCLabelTTF(string _string, string  fontName, float fontSize,
                               Size dimensions, CCTextAlignment hAlignment)
        {
            this.CppObjPtr = NativeLabel.CreateLabelTTF(_string, fontName, fontSize,ref dimensions, hAlignment, CCVerticalTextAlignment.kCCVerticalTextAlignmentTop);
        }

        public CCLabelTTF(string _string, string  fontName, float fontSize,
                               Size dimensions, CCTextAlignment hAlignment, 
                               CCVerticalTextAlignment vAlignment)
        {
            this.CppObjPtr = NativeLabel.CreateLabelTTF(_string, fontName, fontSize, ref dimensions, hAlignment, vAlignment);
        }

        public string String
        {
            get { return NativeLabel.GetString_TTF(this.CppObjPtr); }
            set { NativeLabel.SetString_TTF(this.CppObjPtr, value); }
        }

        public void EnableShadow(Size shadowOffset, float shadowOpacity, float shadowBlur)
        {
            NativeLabel.EnableShadow_TTF(this.CppObjPtr,ref shadowOffset, shadowOpacity,shadowBlur,true);
        }

        public void EnableShadow(Size shadowOffset, float shadowOpacity, float shadowBlur, bool mustUpdateTexture)
        {
            NativeLabel.EnableShadow_TTF(this.CppObjPtr, ref shadowOffset, shadowOpacity, shadowBlur, mustUpdateTexture);
        }
        public void DisableShadow()
        {
            NativeLabel.DisableShadow_TTF(this.CppObjPtr,true);
        }
        public void DisableShadow(bool mustUpdateTexture)
        {
            NativeLabel.DisableShadow_TTF(this.CppObjPtr,mustUpdateTexture);
        }

        public void EnableStroke(Color32 strokeColor,float strokeSize)
        {
            NativeLabel.EnableStroke_TTF(this.CppObjPtr,ref strokeColor, strokeSize,true);
        }
        public void EnableStroke(Color32 strokeColor,float strokeSize, bool mustUpdateTexture)
        {
            NativeLabel.EnableStroke_TTF(this.CppObjPtr, ref strokeColor, strokeSize, mustUpdateTexture);
        }
        public void DisableStroke()
        {
            NativeLabel.DisableStroke_TTF(this.CppObjPtr, true);
        }
        public void DisableStroke(bool mustUpdateTexture)
        {
            NativeLabel.DisableStroke_TTF(this.CppObjPtr,mustUpdateTexture);
        }

        public CCTextAlignment HorizontalAlignment
        {
            get
            {
                return NativeLabel.GetHorizontalAlignment_TTF(this.CppObjPtr);
            }
            set
            {
                NativeLabel.SetHorizontalAlignment_TTF(this.CppObjPtr,value);
            }
        }

        public CCVerticalTextAlignment VerticalAlignment
        {
            get
            {
                return NativeLabel.GetVerticalAlignment_TTF(this.CppObjPtr);
            }
            set
            {
                NativeLabel.SetVerticalAlignment_TTF(this.CppObjPtr, value);
            }
        }

        public Size Dimensions
        {
            get
            {
                Size size = new Size();
                NativeLabel.GetDimensions_TTF(this.CppObjPtr,ref size);
                return size;
            }
            set
            {
                NativeLabel.SetDimensions_TTF(this.CppObjPtr, ref value);
            }
        }

        public float FontSize
        {
            get
            {
                return NativeLabel.GetFontSize_TTF(this.CppObjPtr);
            }
            set
            {
                NativeLabel.SetFontSize_TTF(this.CppObjPtr, value);
            }
        }

        public string FontName
        {
            get
            {
                return NativeLabel.GetFontName_TTF(this.CppObjPtr);
            }
            set
            {
                NativeLabel.SetFontName_TTF(this.CppObjPtr, value);
            }
        }
    }
}
