using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MatrixEngine.Cocos2d
{
    // XXX: If any of these enums are edited and/or reordered, update CCTexture2D.m
    //! Vertical text alignment type
    public enum CCVerticalTextAlignment
    {
        kCCVerticalTextAlignmentTop,
        kCCVerticalTextAlignmentCenter,
        kCCVerticalTextAlignmentBottom,
    } ;

    // XXX: If any of these enums are edited and/or reordered, update CCTexture2D.m
    //! Horizontal text alignment type
    public enum CCTextAlignment
    {
        kCCTextAlignmentLeft,
        kCCTextAlignmentCenter,
        kCCTextAlignmentRight,
    } ;

    public enum BlendValue:int
    {
        GL_ZERO = 0,
        GL_ONE = 1,
        GL_SRC_COLOR = 0x0300,
        GL_ONE_MINUS_SRC_COLOR = 0x0301,
        GL_DST_COLOR = 0x0306,
        GL_ONE_MINUS_DST_COLOR = 0x0307,
        GL_SRC_ALPHA = 0x0302,
        GL_ONE_MINUS_SRC_ALPHA = 0x0303,
        GL_DST_ALPHA = 0x0304,
        GL_ONE_MINUS_DST_ALPHA = 0x0305,
        GL_SRC_ALPHA_SATURATE = 0x0308,
        GL_CONSTANT_COLOR = 0x8001,
        GL_ONE_MINUS_CONSTANT_COLOR = 0x8002,
        GL_CONSTANT_ALPHA = 0x8003,
        GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004,
    }

    public struct BlendFunc
    {
        public BlendValue src;
        public BlendValue dst;

        public static BlendFunc Normal
        {
            get 
            {
                BlendFunc blend = new BlendFunc();
                blend.src = BlendValue.GL_SRC_ALPHA;
                blend.dst = BlendValue.GL_ONE_MINUS_SRC_ALPHA;
                return blend;
            }
        }

        public static BlendFunc Additive
        {
            get 
            {
                BlendFunc blend = new BlendFunc();
                blend.src = BlendValue.GL_SRC_ALPHA;
                blend.dst = BlendValue.GL_ONE;
                return blend;
            }
        }

        public static bool operator==(BlendFunc value1,BlendFunc value2)
        {
            return ((value1.src == value2.src)&&(value1.dst == value2.dst));
        }

        public static bool operator !=(BlendFunc value1, BlendFunc value2)
        {
            if (value1.src == value2.src)
            {
                return (value1.dst != value2.dst);
            }
            return true;
        }
    }

    //特殊的混合值

    //正常

    //发亮

}
