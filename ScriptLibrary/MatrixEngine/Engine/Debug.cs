using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Native;

namespace MatrixEngine
{
    public class Debug
    {
        public static void Log(string log)
        {
            NativeSystem.Log(log, true);
        }

        public static void Log(string log, bool line)
        {
            NativeSystem.Log(log, line);
        }

        public static void LogWaring(string log)
        {
            NativeSystem.LogWaring(log);
        }

        public static void LogError(string log)
        {
            NativeSystem.LogError(log);
        }

        public static void LogFile(string log)
        {
            NativeSystem.LogFile(log);
        }

        public static void CollistionDisplay(bool display)
        {
            NativeCollistionSystem.CollistionSystemSetDebug(display);
        }
    }
}
