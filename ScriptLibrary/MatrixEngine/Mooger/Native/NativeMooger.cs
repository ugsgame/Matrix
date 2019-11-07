using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.CompilerServices;

namespace MatrixEngine.Mooger.Native
{
    /// <summary>
    /// MoogerMap
    /// </summary>
    internal static class NativeMoogerMap
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateMoogrMap();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool LoadMapWithFile(IntPtr map, string fileName,string resPath);
    }
}
