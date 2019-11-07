using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;

namespace MatrixEngine.Engine
{
    public static class System
    {
        public enum PLATFORM
        {
            UNKNOWN,
            IOS,
            ANDROID,
            WIN32,
            MARMALADE,
            LINUX,
            BADA,
            BLACKBERRY,
            MAC,
            NACL,
            EMSCRIPTEN,
            TIZEN,
            WINRT,
            WP8
        }

        public static PLATFORM TARGET_PLATFORM = (PLATFORM)CCDirector.TargetPlatform;
    }
}
