using System;

namespace MatrixEngine.Math
{

    public struct Vector2 : IEquatable<Vector2>
    {
        public float X;
        public float Y;
        private static Vector2 _zero = new Vector2();
        private static Vector2 _one = new Vector2(1f, 1f);
        private static Vector2 _unitX = new Vector2(1f, 0f);
        private static Vector2 _unitY = new Vector2(0f, 1f);
        public static Vector2 Zero
        {
            get
            {
                return _zero;
            }
        }
        public static Vector2 One
        {
            get
            {
                return _one;
            }
        }
        public static Vector2 UnitX
        {
            get
            {
                return _unitX;
            }
        }
        public static Vector2 UnitY
        {
            get
            {
                return _unitY;
            }
        }

        public Vector2(float x, float y)
        {
            this.X = x;
            this.Y = y;
        }

        public Vector2(float value)
        {
            this.X = this.Y = value;
        }

        public override string ToString()
        {
            return "Vector2(" + "X = " + X + ", Y = " + Y + ")";
        }

        public bool Equals(Vector2 other)
        {
            return ((this.X == other.X) && (this.Y == other.Y));
        }

        public override bool Equals(object obj)
        {
            bool flag = false;
            if (obj is Vector2)
            {
                flag = this.Equals((Vector2)obj);
            }
            return flag;
        }

        public override int GetHashCode()
        {
            return (this.X.GetHashCode() + this.Y.GetHashCode());
        }

        public float Length
        {
            get{ return MathHelper.Sqrt(X * X + Y * Y);}
        }


        public Vector2 Normalize
        {
            get
            {
                float len = this.Length;
                if (len == 0.0f) return _unitX;
                return this / len;      
            }
        }

        public float ToDegrees()
        {
            return MathHelper.RadiansToDegrees(MathHelper.ATan2(X,Y));
        }

        public float ToRadians()
        {
            return MathHelper.ATan2(X, Y);
        }

        public static float Distance(Vector2 pos1, Vector2 pos2)
        {
            float a1 = (pos1.X - pos2.X) * (pos1.X - pos2.X);
            float a2 = (pos1.Y - pos2.Y) * (pos1.Y - pos2.Y);
            return MathHelper.Sqrt(a1 + a2);
        }


        public static Vector2 operator -(Vector2 value)
        {
            Vector2 vector = new Vector2();
            vector.X = -value.X;
            vector.Y = -value.Y;
            return vector;
        }


        public static bool operator ==(Vector2 value1, Vector2 value2)
        {
            return ((value1.X == value2.X) && (value1.Y == value2.Y));
        }

        public static bool operator !=(Vector2 value1, Vector2 value2)
        {
            if (value1.X == value2.X)
            {
                return (value1.Y != value2.Y);
            }
            return true;
        }

        public static Vector2 operator +(Vector2 value1, Vector2 value2)
        {
            Vector2 vector = new Vector2();
            vector.X = value1.X + value2.X;
            vector.Y = value1.Y + value2.Y;
            return vector;
        }

        public static Vector2 operator -(Vector2 value1, Vector2 value2)
        {
            Vector2 vector = new Vector2();
            vector.X = value1.X - value2.X;
            vector.Y = value1.Y - value2.Y;
            return vector;
        }

        public static Vector2 operator *(Vector2 value1, Vector2 value2)
        {
            Vector2 vector = new Vector2();
            vector.X = value1.X * value2.X;
            vector.Y = value1.Y * value2.Y;
            return vector;
        }

        public static Vector2 operator *(Vector2 value, float scaleFactor)
        {
            Vector2 vector = new Vector2();
            vector.X = value.X * scaleFactor;
            vector.Y = value.Y * scaleFactor;
            return vector;
        }

        public static Vector2 operator *(float scaleFactor, Vector2 value)
        {
            Vector2 vector = new Vector2();
            vector.X = value.X * scaleFactor;
            vector.Y = value.Y * scaleFactor;
            return vector;
        }

        public static Vector2 operator /(Vector2 value1, Vector2 value2)
        {
            Vector2 vector = new Vector2();
            vector.X = value1.X / value2.X;
            vector.Y = value1.Y / value2.Y;
            return vector;
        }

        public static Vector2 operator /(Vector2 value1, float divider)
        {
            Vector2 vector = new Vector2();
            float num = 1f / divider;
            vector.X = value1.X * num;
            vector.Y = value1.Y * num;
            return vector;
        }

        public static implicit operator Vector2(float xy)
        {
            return new Vector2(xy, xy);
        }

        public static implicit operator Vector2(Size v)
        {
            return new Vector2(v.width, v.height);
        }
    }
}
