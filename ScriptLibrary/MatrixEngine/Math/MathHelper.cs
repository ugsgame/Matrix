namespace MatrixEngine.Math
{
    using System;

    public static class MathHelper
    {
        public const float Deg2Rad = 0.01745329f;
        public const float E = 2.718282f;
        public const float Epsilon = float.Epsilon;
        public const float Infinity = float.PositiveInfinity;
        public const float Log10E = 0.4342945f;
        public const float Log2E = 1.442695f;
        public const float NegativeInfinity = float.NegativeInfinity;
        public const float Pi = 3.141593f;
        public const float PiOver2 = 1.570796f;
        public const float PiOver4 = 0.7853982f;
        public const float Rad2Deg = 57.29578f;
        public const float TwoPi = 6.283185f;

        private static Random random = new Random((int)(DateTime.Now.Ticks & 0xffffffffL) | (int)(DateTime.Now.Ticks >> 32));

        public static int Abs(int value)
        {
            return Math.Abs(value);
        }

        public static float Abs(float f)
        {
            return Math.Abs(f);
        }

        public static float ACos(float f)
        {
            return (float)Math.Acos((double)f);
        }

        public static float ASin(float f)
        {
            return (float)Math.Asin((double)f);
        }

        public static float ATan(float f)
        {
            return (float)Math.Atan((double)f);
        }

        public static float ATan2(float a, float b)
        {
            return (float)Math.Atan2((double)a, (double)b);
        }

        public static float Ceil(float f)
        {
            return (float)Math.Ceiling((double)f);
        }

        public static int Clamp(int value, int min, int max)
        {
            value = (value > max) ? max : value;
            value = (value < min) ? min : value;
            return value;
        }

        public static float Clamp(float value, float min, float max)
        {
            value = (value > max) ? max : value;
            value = (value < min) ? min : value;
            return value;
        }

        public static float Cos(float f)
        {
            return (float)Math.Cos((double)f);
        }

        public static float DegreesToRadians(float degrees)
        {
            return (degrees * 0.01745329f);
        }

        public static float RadiansToDegrees(float radians)
        {
            return (radians * 57.29578f);
        }

        public static Vector2 RadiansToVector2(float radians)
        {
            Vector2 dir = Vector2.Zero;
            dir.X = MathHelper.Sin(radians);
            dir.Y = MathHelper.Cos(radians);
            return dir;
        }

        public static Vector2 DegreesToVector2(float degrees)
        {
            Vector2 dir = Vector2.Zero;
            dir.X = MathHelper.Sin(degrees * 0.01745329f);
            dir.Y = MathHelper.Cos(degrees * 0.01745329f);
            return dir;
        }

        public static float Exp(float power)
        {
            return (float)Math.Exp((double)power);
        }

        public static float Floor(float f)
        {
            return (float)Math.Floor((double)f);
        }

        public static float Hermite(float value1, float tangent1, float value2, float tangent2, float amount)
        {
            float num = amount;
            float num2 = num * num;
            float num3 = num * num2;
            float num4 = ((2f * num3) - (3f * num2)) + 1f;
            float num5 = (-2f * num3) + (3f * num2);
            float num6 = (num3 - (2f * num2)) + num;
            float num7 = num3 - num2;
            return ((((value1 * num4) + (value2 * num5)) + (tangent1 * num6)) + (tangent2 * num7));
        }

        public static int ICeil(float f)
        {
            return (int)Math.Ceiling((double)f);
        }

        public static int IFloor(float f)
        {
            return (int)Math.Floor((double)f);
        }

        public static int IRound(float f)
        {
            return (int)Math.Round((double)f);
        }

        public static bool IsPowerOfTwo(int value)
        {
            return ((value & (value - 1)) == 0);
        }

        public static float Lerp(float value1, float value2, float amount)
        {
            return (value1 + ((value2 - value1) * amount));
        }

        public static float Log(float f)
        {
            return (float)Math.Log((double)f);
        }

        public static float Log10(float f)
        {
            return (float)Math.Log10((double)f);
        }

        public static int Max(int a, int b)
        {
            if (a > b)
            {
                return a;
            }
            return b;
        }

        public static float Max(float a, float b)
        {
            if (a > b)
            {
                return a;
            }
            return b;
        }

        public static int Min(int a, int b)
        {
            if (a < b)
            {
                return a;
            }
            return b;
        }

        public static float Min(float a, float b)
        {
            if (a < b)
            {
                return a;
            }
            return b;
        }

        public static int NextPowerOfTwo(int v)
        {
            v--;
            v |= v >> 0x10;
            v |= v >> 8;
            v |= v >> 4;
            v |= v >> 2;
            v |= v >> 1;
            return (v + 1);
        }

        public static float Pow(float fBase, float fExponent)
        {
            return (float)Math.Pow((double)fBase, (double)fExponent);
        }

        public static float Round(float f)
        {
            return (float)Math.Round((double)f);
        }

        public static float Sin(float f)
        {
            return (float)Math.Sin((double)f);
        }

        public static float SmoothStep(float value1, float value2, float amount)
        {
            float num = Clamp(amount, 0f, 1f);
            return Lerp(value1, value2, (num * num) * (3f - (2f * num)));
        }

        public static float Sqrt(float f)
        {
            return (float)Math.Sqrt((double)f);
        }

        public static float Tan(float f)
        {
            return (float)Math.Tan((double)f);
        }

        public static float Random_minus1_1()
        {
            float r = 2.0f * (float)random.NextDouble() - 1.0f;
            return r;
        }

        public static float Random_minus0_1()
        {
            return (float)random.NextDouble();
        }

        public static int Random_minus0_n(int n)
        {
            return random.Next(n);
        }
    }
}
