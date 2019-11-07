using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;
using MatrixEngine.RichControls.Native;
using MatrixEngine.Math;

namespace MatrixEngine.RichControls
{
    public class CCLabelFT : CCLabelHTML
    {
        private string doc = "";
        private string face = "font1";
        private string align = "left";
        private int padding = 0;
        private int margin = 0;
        private Color32 color = 0xffffffff;


        internal CCLabelFT(IntPtr t) : base(t) { }
        CCLabelFT(string _string)
            : base(NativeLabelHTML.Create())
        {
            doc = _string;
            this.String = doc;
        }

        public CCLabelFT(string _string, string fontName)
            : base(NativeLabelHTML.Create())
        {
            doc = _string;
            face = fontName;
            this.String = doc;
        }

        public CCLabelFT(string _string, string fontName,
                               Size dimensions, CCTextAlignment hAlignment)
        {
            doc = _string;
            face = fontName;
            Alignment = hAlignment;

            this.CppObjPtr = NativeLabelHTML.CreateWithString(MakeHTML(), ref dimensions, align);
        }

        public virtual Color32 Color
        {
            get { return color; }
            set
            {
                color = value;
                this.String = doc;
            }
        }

        public override string String
        {
            get { return doc; }
            set
            {
                doc = value;
                base.String = MakeHTML();
            }
        }

        public CCTextAlignment Alignment
        {
            get
            {
                switch (align)
                {
                    case "left":
                        return CCTextAlignment.kCCTextAlignmentLeft;
                    case "center":
                        return CCTextAlignment.kCCTextAlignmentCenter;
                    case "right":
                        return CCTextAlignment.kCCTextAlignmentRight;
                    default:
                        return CCTextAlignment.kCCTextAlignmentLeft;
                }
            }
            set
            {
                switch (value)
                {
                    case CCTextAlignment.kCCTextAlignmentLeft:
                        align = "left";
                        break;
                    case CCTextAlignment.kCCTextAlignmentCenter:
                        align = "center";
                        break;
                    case CCTextAlignment.kCCTextAlignmentRight:
                        align = "right";
                        break;
                    default:
                        align = "left";
                        break;
                }
                this.String = doc;
            }
        }

        private string MakeColor()
        {
            string R = color.R < 10 ? "0" + color.R.ToString("x") : color.R.ToString("x");
            string G = color.G < 10 ? "0" + color.G.ToString("x") : color.G.ToString("x");
            string B = color.B < 10 ? "0" + color.B.ToString("x") : color.B.ToString("x");
            string A = color.A < 10 ? "0" + color.A.ToString("x") : color.A.ToString("x");
            return R + G + B + A;
        }

        private string MakeHTML()
        {
            string line_1 = "<p style=\"text-align:" + align + ";" + "margin:" + margin + "px;" + "padding:" + padding + "px \" >\n";
            string line_2 = "<font face=\"" + face + "\" color= \"#" + MakeColor() + "\" >\n";
            string line_3 = doc;
            string line_4 = "</font>";
            string line_5 = "</p>";

            return line_1 + line_2 + line_3 + line_4 + line_5;
        }

        public override string ToString()
        {
            return MakeHTML();
        }
    }
}
