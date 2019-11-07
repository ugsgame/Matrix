namespace MatrixEngine.Math
{
    using System;
    using System.Globalization;
    using System.Runtime.InteropServices;

    [Serializable, StructLayout(LayoutKind.Sequential)]
    public struct Color32 : IEquatable<Color32>
    {
        public byte R;
        public byte G;
        public byte B;
        public byte A;

        //public byte R
        //{
        //    get { return r; }
        //    set { r = value; }
        //}

        //public byte G
        //{
        //    get { return g; }
        //    set { g = value; }
        //}

        //public byte B
        //{
        //    get { return b; }
        //    set { b = value; }
        //}

        //public byte A
        //{
        //    get { return a; }
        //    set { a = value; }
        //}

        public uint PackedValue
        {
            get
            {
                return (uint)((A << 24) | (R << 16) | (G << 8) | (B));
            }
            set
            {
                this.A = (byte)(value >> 24);
                this.R = (byte)(value >> 16);
                this.G = (byte)(value >> 8);
                this.B = (byte)(value);
            }
        }

        public uint PackedValueLittleEndian
        {
            get
            {
                return (uint)((A << 24) | (B << 16) | (G << 8) | (R));
            }
            set
            {
                this.A = (byte)(value >> 24);
                this.B = (byte)(value >> 16);
                this.G = (byte)(value >> 8);
                this.R = (byte)(value);
            }
        }

        public Color32(uint packedValue)
        {
            this.A = (byte)(packedValue >> 24);
            this.R = (byte)(packedValue >> 16);
            this.G = (byte)(packedValue >> 8);
            this.B = (byte)(packedValue);
        }

        public Color32(int packedValue)
            : this((uint)(packedValue & 0xffffff) | 0xff000000u)
        {
            //this.packedValue = (uint)(packedValue & 0xffffff) | 0xff000000u;
        }

        public Color32(byte r, byte g, byte b, byte a)
        {
            this.R = r;
            this.G = g;
            this.B = b;
            this.A = a;
        }

        public Color32(byte r, byte g, byte b)
        {
            this.R = r;
            this.G = g;
            this.B = b;
            this.A = 0xff;
        }

        public override string ToString()
        {
            return string.Format(CultureInfo.CurrentCulture, "{R:{0} G:{1} B:{2} A:{3}}", new object[] { this.R, this.G, this.B, this.A });
        }

        public override int GetHashCode()
        {
            return this.PackedValue.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            return ((obj is Color32) && this.Equals((Color32)obj));
        }

        public bool Equals(Color32 other)
        {
            return this.R == other.R && this.G == other.G && this.B == other.B && this.A == other.A;
        }

        public static bool operator ==(Color32 a, Color32 b)
        {
            return a.Equals(b);
        }

        public static bool operator !=(Color32 a, Color32 b)
        {
            return !a.Equals(b);
        }

        public static implicit operator Color32(uint color)
        {
            return new Color32(color);
        }

        public static implicit operator Color32(int color)
        {
            return new Color32(color);
        }

        public static implicit operator uint(Color32 color)
        {
            return color.PackedValue;
        }

        public static implicit operator int(Color32 color)
        {
            return (int)color.PackedValue;
        }

        public static Color32 Black
        {
            get
            {
                return new Color32(0, 0, 0);
            }
        }
        public static Color32 Blue
        {
            get
            {
                return new Color32(0, 0xff, 0);
            }
        }
        public static Color32 Green
        {
            get
            {
                return new Color32(0, 0, 0xff);
            }
        }
        public static Color32 Red
        {
            get
            {
                return new Color32(0xff, 0, 0);
            }
        }
        public static Color32 Yellow
        {
            get
            {
                return new Color32(0xff, 0xff, 0);
            }
        }
        public static Color32 White
        {
            get
            {
                return new Color32(0xff, 0xff, 0xff);
            }
        }

        public ColorF ToColorF()
        {
            ColorF rf;
            rf.R = PackUtils.UnpackUNorm(0xff, this.R);
            rf.G = PackUtils.UnpackUNorm(0xff, this.G);
            rf.B = PackUtils.UnpackUNorm(0xff, this.B);
            rf.A = PackUtils.UnpackUNorm(0xff, this.A);
            return rf;
        }

        private static int ClampToByte32(int value)
        {
            if (value < 0)
            {
                return 0;
            }
            if (value > 0xff)
            {
                return 0xff;
            }
            return value;
        }
    }
}
