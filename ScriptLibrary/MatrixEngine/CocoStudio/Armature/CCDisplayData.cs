using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;


namespace MatrixEngine.CocoStudio.Armature
{
    public enum DisplayType
    {
        CS_DISPLAY_SPRITE,                //! display is a single CCSprite
        CS_DISPLAY_ARMATURE,         //! display is a CCArmature
        CS_DISPLAY_PARTICLE,            //! display is a CCParticle.

        CS_DISPLAY_MAX
    };
    //TODO
    public class CCDisplayData : CCObject
    {
        internal CCDisplayData(IntPtr t) { }
        public CCDisplayData():base(IntPtr.Zero)
        {

        }
    }
}
