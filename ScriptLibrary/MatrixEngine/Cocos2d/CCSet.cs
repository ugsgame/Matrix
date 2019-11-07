using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MatrixEngine.Cocos2d
{
    public class CCSet
    {
        private List<CCTouch> touches = new List<CCTouch>();
        public CCSet(){}

        public int Length()
        {
            return touches.Count;
        }

        public void AddTouch(CCTouch touch)
        {
            touches.Add(touch);
        }

        public CCTouch GetTouch(int index)
        {
            return touches[index]; 
        }


    }
}
