using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;
using MatrixEngine.Mooger;
using MatrixEngine;

using UniLua;

namespace Game.Test
{

    public static class ScriptLib
    {
        public const string LIB_NAME = "ScriptLib";

        public static int RegisterLib(ILuaState lua)
        {
            var define = new NameFuncPair[]
            {
                new NameFuncPair("add", Add),
                new NameFuncPair("sub", Sub),
                new NameFuncPair("log", Log),
            };

            lua.L_NewLib(define);

            return 1;
        }

        public static int Add(ILuaState lua)
        {
            var a = lua.L_CheckNumber(1); // 第一个参数
            var b = lua.L_CheckNumber(2); // 第二个参数
            var c = a + b;                // 执行加法操作
            lua.PushNumber(c);            // 将返回值入栈
            return 1;                     // 有一个返回值
        }

        public static int Sub(ILuaState lua)
        {
            var a = lua.L_CheckNumber(1);   // 第一个参数
            var b = lua.L_CheckNumber(2);   // 第二个参数
            var c = a - b;                  // 执行减法操作
            lua.PushNumber(c);              // 将返回值入栈
            return 1;                       // 有一个返回值
        }

        public static int Log(ILuaState lua)
        {
            var a = lua.L_CheckNumber(1);   // 第一个参数
            Console.WriteLine(a);

            return 0;
        }

    }

    class LuaTest : CCLayer
    {
        private ILuaState Lua;
        private int fuc;

        public LuaTest()
        {
            // 创建 Lua 虚拟机
            Lua = LuaAPI.NewState();
            // 加载基本库
            Lua.L_OpenLibs();

            Lua.L_RequireF(ScriptLib.LIB_NAME   // 库的名字
              , ScriptLib.RegisterLib           // 库的初始化函数
              , true                           // 不默认放到全局命名空间 (在需要的地方用require获取)
              );

            try
            {
                string luaStr = CCFileUtils.GetFileDataToString("LuaScript.lua");
                Console.WriteLine("Lua state:" + Lua.L_DoString(luaStr));
                //Console.WriteLine("Lua state:" + Lua.L_DoFile("LuaScript.lua"));

//                 if (!Lua.IsTable(-1))
//                 {
//                     throw new Exception("framework main's return value is not a table");
//                 }
//                 fuc = StoreMethod("OnEnter");
//                 Lua.Pop(1);
// 
//                 CallMethod(fuc);

            }
            catch (Exception e)
            {

                Console.WriteLine(e.ToString());
            }

        }

        public object[] CallFuction(ILuaState l, string fName, params object[] args)
        {
            l.GetGlobal(fName);
            if (!l.IsFunction(-1))
            {
                //Console.WriteLine(fName + " is not a funciton!!");
                throw new Exception(string.Format("function not found：" + fName, fName));
            }
            Console.WriteLine("args:" + args.Length);

            for (int i = 0; i < args.Length; i++)
            {
                var param = args[i];
                if (param is int)
                {
                    l.PushInteger((int)param);
                }
                else if (param is float || param is double)
                {
                    l.PushNumber((double)param);
                }
                else if (param is string)
                {
                    l.PushString((string)param);
                }
                else if (param is bool)
                {
                    l.PushBoolean((bool)param);
                }
                else
                {
                    throw new Exception(string.Format("can't set this param!", fName));
                }
            }
            //调用函数
            l.Call(args.Length, 0);
            //TODO:返回函数返回值列表
            return null;
        }

        public override void OnEnter()
        {
            base.OnEnter();

            CallFuction(Lua, "OnEnter");
            /*CallMethod(fuc);*/
            //Lua.L_DoFile("LuaScript.lua");
        }

        public override void OnUpdate(float dTime)
        {
            base.OnUpdate(dTime);
        }

        private int StoreMethod(string name)
        {
            Lua.GetField(-1, name);
            if (!Lua.IsFunction(-1))
            {
                throw new Exception(string.Format("method {0} not found!", name));
            }
            return Lua.L_Ref(LuaDef.LUA_REGISTRYINDEX);
        }

        private void CallMethod(int funcRef)
        {
            Lua.RawGetI(LuaDef.LUA_REGISTRYINDEX, funcRef);

            // insert `traceback' function  
            var b = Lua.GetTop();
            Lua.PushCSharpFunction(Traceback);
            Lua.Insert(b);

            var status = Lua.PCall(0, 0, b);
            if (status != ThreadStatus.LUA_OK)
            {
                Debug.LogError(Lua.ToString(-1));
            }

            // remove `traceback' function  
            Lua.Remove(b);
        }

        private static int Traceback(ILuaState lua)
        {
            var msg = lua.ToString(1);
            if (msg != null)
            {
                lua.L_Traceback(lua, msg, 1);
            }
            // is there an error object?  
            else if (!lua.IsNoneOrNil(1))
            {
                // try its `tostring' metamethod  
                if (!lua.L_CallMeta(1, "__tostring"))
                {
                    lua.PushString("(no error message)");
                }
            }
            return 1;
        }


    }
}
