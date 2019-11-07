using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MatrixEngine.Math
{
    public struct Rect
    {
        public Vector2 origin;
        public Size size;

        private static Rect _zero = new Rect();
        public static Rect Zero
        {
            get { return _zero; }
        }
        //TODO
        public Rect(float x, float y, float width, float height)
        {
            origin.X = x;
            origin.Y = y;
            size.width = width;
            size.height = height;
        }

        public float GetMaxX()
        {
            return (float)(origin.X + size.width);
        }

        public float GetMidX()
        {
            return (float)(origin.X + size.width / 2.0);
        }

        public float GetMinX()
        {
            return origin.X;
        }

        public float GetMaxY()
        {
            return origin.Y + size.height;
        }

        public float GetMidY()
        {
            return (float)(origin.Y + size.height / 2.0);
        }

        public float GetMinY()
        {
            return origin.Y;
        }
        //TODO
        public bool ContainsPoint(Vector2 point)
        {
            bool bRet = false;

            if (point.X >= GetMinX() && point.X <= GetMaxX()
                && point.Y >= GetMinY() && point.Y <= GetMaxY())
            {
                bRet = true;
            }

            return bRet;
        }

        public bool IntersectsRect(Rect rect)
        {
            return !(GetMaxX() < rect.GetMinX() ||
                     rect.GetMaxX() < GetMinX() ||
                          GetMaxY() < rect.GetMinY() ||
                     rect.GetMaxY() < GetMinY());
        }

        public bool IsValidity()
        {
            if (size.width == 0 && size.height == 0)
            {
                return false;
            }
            return true;
        }

        public override string ToString()
        {
            //return base.ToString();
            return "Rect: " + origin.ToString() + " " + size.ToString();
        }
    }
}
