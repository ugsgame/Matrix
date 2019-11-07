using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MatrixEngine.Math
{
    public struct Size : IEquatable<Size>
    {
        public float width;
        public float height;

        private static Size _zero;
        public static Size Zero
        {
            get { return _zero; }
        }

        public Size(float w, float h)
        {
            this.width = w; 
            this.height = h;
        }

        public override string ToString()
        {
            return "Size(" + "Width = " + this.width + ", Height = " + this.height + ")";
        }

        public bool Equals(Size other)
        {
            return ((this.width == other.width) && (this.height == other.height));
        }

        public override bool Equals(object obj)
        {
            if (obj is Size)
            {
                return this.Equals((Size)obj);
            }
            return false;
        }

        public override int GetHashCode()
        {
            return (this.width.GetHashCode() + this.height.GetHashCode());
        }

        public static bool operator ==(Size value1, Size value2)
        {
            return ((value1.width == value2.width) && (value1.height == value2.height));
        }

        public static Size operator -(Size value)
        {
            Size vector = new Size();
            vector.width = -value.width;
            vector.height = -value.height;
            return vector;
        }

        public static bool operator !=(Size value1, Size value2)
        {
            if (value1.width == value2.width)
            {
                return (value1.height != value2.height);
            }
            return true;
        }

        public static Size operator +(Size value1, Size value2)
        {
            Size vector = new Size();
            vector.width = value1.width + value2.width;
            vector.height = value1.height + value2.height;
            return vector;
        }

        public static Size operator -(Size value1, Size value2)
        {
            Size vector = new Size();
            vector.width = value1.width - value2.width;
            vector.height = value1.height - value2.height;
            return vector;
        }

        public static Size operator *(Size value1, Size value2)
        {
            Size vector = new Size();
            vector.width = value1.width * value2.width;
            vector.height = value1.height * value2.height;
            return vector;
        }

        public static Size operator *(Size value, float scaleFactor)
        {
            Size vector = new Size();
            vector.width = value.width * scaleFactor;
            vector.height = value.height * scaleFactor;
            return vector;
        }

        public static Size operator *(float scaleFactor, Size value)
        {
            Size vector = new Size();
            vector.width = value.width * scaleFactor;
            vector.height = value.height * scaleFactor;
            return vector;
        }

        public static Size operator /(Size value1, Size value2)
        {
            Size vector = new Size();
            vector.width = value1.width / value2.width;
            vector.height = value1.height / value2.height;
            return vector;
        }

        public static Size operator /(Size value1, float divider)
        {
            Size vector = new Size();
            float num = 1f / divider;
            vector.width = value1.width * num;
            vector.height = value1.height * num;
            return vector;
        }

        public static implicit operator Size(float xy)
        {
            return new Size(xy, xy);
        }

        public static implicit operator Size(Vector2 v)
        {
            return new Size(v.X, v.Y);
        }
    }
}
