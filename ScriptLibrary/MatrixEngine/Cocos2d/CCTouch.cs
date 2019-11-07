using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;

namespace MatrixEngine.Cocos2d
{
    public class CCTouch
    {
        private Vector2 vec;
        public int _Id = 0;

        public CCTouch() { }
        public CCTouch(int id, float x, float y) 
        {
            _Id = id;
            vec.X = x;
            vec.Y = y;
        }

        public int Id
        {
            set { _Id = value; }
            get { return _Id; }
        }

    }
}
