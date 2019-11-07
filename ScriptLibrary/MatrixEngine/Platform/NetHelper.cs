using MatrixEngine.Native;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine;

namespace MatrixEngine.Platform
{
    public class NetHelper
    {
        public static void OpenUrl(string url)
        {

            switch (MatrixEngine.Engine.System.TARGET_PLATFORM)
            {
                case MatrixEngine.Engine.System.PLATFORM.WIN32:
                case MatrixEngine.Engine.System.PLATFORM.UNKNOWN:
                    global::System.Diagnostics.Process.Start(url);
                    break;
                default:
                    NativeNetHelper.OpenUrl(url);
                    break;
            }

        }
    }
}
