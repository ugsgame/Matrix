namespace MatrixEngine.Math
{
    using System;
    using System.Globalization;
    using System.Runtime.InteropServices;

    [Serializable, StructLayout(LayoutKind.Sequential)]
    public struct ColorF : IEquatable<ColorF>
    {
        public float R;
        public float G;
        public float B;
        public float A;
        public ColorF(float r, float g, float b, float a)
        {
            this.R = r;
            this.G = g;
            this.B = b;
            this.A = a;
        }

        public ColorF(float r, float g, float b)
        {
            this.R = r;
            this.G = g;
            this.B = b;
            this.A = 1f;
        }

        public override string ToString()
        {
            return string.Format(CultureInfo.CurrentCulture, "{R:{0} G:{1} B:{2} A:{3}}", new object[] { this.R, this.G, this.B, this.A });
        }

        public override int GetHashCode()
        {
            return (((this.R.GetHashCode() + this.G.GetHashCode()) + this.B.GetHashCode()) + this.A.GetHashCode());
        }

        public override bool Equals(object obj)
        {
            return ((obj is ColorF) && this.Equals((ColorF)obj));
        }

        public bool Equals(ColorF other)
        {
            return ((((this.R == other.R) && (this.G == other.G)) && (this.B == other.B)) && (this.A == other.A));
        }

        public static bool operator ==(ColorF value1, ColorF value2)
        {
            return ((((value1.R == value2.R) && (value1.G == value2.G)) && (value1.B == value2.B)) && (value1.A == value2.A));
        }

        public static bool operator !=(ColorF value1, ColorF value2)
        {
            if (((value1.R == value2.R) && (value1.G == value2.G)) && (value1.B == value2.B))
            {
                return (value1.A != value2.A);
            }
            return true;
        }

        public static ColorF operator +(ColorF value1, ColorF value2)
        {
            ColorF rf;
            rf.R = value1.R + value2.R;
            rf.G = value1.G + value2.G;
            rf.B = value1.B + value2.B;
            rf.A = value1.A + value2.A;
            return rf;
        }

        public static ColorF operator -(ColorF value1, ColorF value2)
        {
            ColorF rf;
            rf.R = value1.R - value2.R;
            rf.G = value1.G - value2.G;
            rf.B = value1.B - value2.B;
            rf.A = value1.A - value2.A;
            return rf;
        }

        public static ColorF operator *(ColorF value1, ColorF value2)
        {
            ColorF rf;
            rf.R = value1.R * value2.R;
            rf.G = value1.G * value2.G;
            rf.B = value1.B * value2.B;
            rf.A = value1.A * value2.A;
            return rf;
        }

        public static ColorF operator *(ColorF value1, float scaleFactor)
        {
            ColorF rf;
            rf.R = value1.R * scaleFactor;
            rf.G = value1.G * scaleFactor;
            rf.B = value1.B * scaleFactor;
            rf.A = value1.A * scaleFactor;
            return rf;
        }

        public static ColorF operator *(float scaleFactor, ColorF value)
        {
            ColorF rf;
            rf.R = value.R * scaleFactor;
            rf.G = value.G * scaleFactor;
            rf.B = value.B * scaleFactor;
            rf.A = value.A * scaleFactor;
            return rf;
        }

        public static ColorF operator /(ColorF value1, ColorF value2)
        {
            ColorF rf;
            rf.R = value1.R / value2.R;
            rf.G = value1.G / value2.G;
            rf.B = value1.B / value2.B;
            rf.A = value1.A / value2.A;
            return rf;
        }

        public static ColorF operator /(ColorF value1, float divider)
        {
            ColorF rf;
            float num = 1f / divider;
            rf.R = value1.R * num;
            rf.G = value1.G * num;
            rf.B = value1.B * num;
            rf.A = value1.A * num;
            return rf;
        }

        public Color32 ToColor32()
        {
            return new Color32(PackHelper(this.R, this.G, this.B, this.A));
        }

        public Vector4 ToVector4()
        {
            return new Vector4(this.R, this.G, this.B, this.A);
        }

        private static uint PackHelper(float vectorX, float vectorY, float vectorZ, float vectorW)
        {
            uint num = PackUtils.PackUNorm(255f, vectorX);
            uint num2 = PackUtils.PackUNorm(255f, vectorY) << 8;
            uint num3 = PackUtils.PackUNorm(255f, vectorZ) << 0x10;
            uint num4 = PackUtils.PackUNorm(255f, vectorW) << 0x18;
            return (((num | num2) | num3) | num4);
        }
    }
}
