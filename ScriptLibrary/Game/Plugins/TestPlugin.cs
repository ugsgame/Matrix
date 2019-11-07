using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;

namespace Game.Plugins
{
    public class TestPlugin
    {
        public TestPlugin()
        {

        }

        public void HellowPlugin()
        {
            Console.WriteLine("C# HellowPlugin");

            Test();
        }

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Test();
    }
}
