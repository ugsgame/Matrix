
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace UniLua.Tools
{
	// using Logger = DebugAssist;
	// using Logger = UnityEngine.Debug;

	// thanks to dharco
	// refer to https://github.com/dharco/UniLua/commit/2854ddf2500ab2f943f01a6d3c9af767c092ce75
	public class ULDebug
	{
		public static System.Action<object> Log = NoAction;
		public static System.Action<object> LogError = NoAction;

		private static void NoAction(object msg) { }

		static ULDebug()
		{
#if UNITY
            Log = Logger.Log;
            LogError = Logger.LogError;
#endif
            Log = Llog;
            LogError = LlogError;
//             Log = Console.WriteLine;
//             LogError = Console.WriteLine;
		}
        //TODO:
        static void Llog(object msg)
        {
            Console.WriteLine("lLog:" + msg);
        }
        //TODO:
        static void LlogError(object msg)
        {
            Console.WriteLine("Lua error:" + msg);
        }

	}
}

