using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine;
using MatrixEngine.Math;
using MatrixEngine.Cocos2d;
using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Armature;
using MatrixEngine.CocoStudio.GUI;

using Game.Test;
using MatrixEngine.Engine;


namespace Game
{
    public class MatrixApp
    {

        private static CCLayer NotificationLayer = new CCLayer();

        public enum PLATFORM
        {
            CC_PLATFORM_UNKNOWN,
            CC_PLATFORM_IOS,
            CC_PLATFORM_ANDROID,
            CC_PLATFORM_WIN32,
            CC_PLATFORM_MARMALADE,
            CC_PLATFORM_LINUX,
            CC_PLATFORM_BADA,
            CC_PLATFORM_BLACKBERRY,
            CC_PLATFORM_MAC,
            CC_PLATFORM_NACL,
            CC_PLATFORM_EMSCRIPTEN,
            CC_PLATFORM_TIZEN,
            CC_PLATFORM_WINRT,
            CC_PLATFORM_WP8
        }

        public static PLATFORM _TargetPlatform;

        public static void CallFunc(object[] a)
        {

        }

        public static void OnEnterBackground()
        {

        }

        public static void OnEnterForeground()
        {

        }

        public static int StartApp(string[] args)
        {
            try
            {
                Console.WriteLine("MatrixEngine Start!");

                CCDirector.SetNotificationLayer(NotificationLayer);
                NotificationLayer.AddChild(SystemInfoLayer.Instance);

                if (args != null)
                {
                    Console.WriteLine("args len:" + args.Length);
                    for (int i = 0; i < args.Length; i++)
                    {
                        Console.WriteLine("args:" + i + " " + args[i]);
                    }
                }

                // CCDirector.SetResolutionSize(new Size(960, 640), ResolutionPolicy.kResolutionExactFit);


                TestScene testScene = new TestScene();
                CCDirector.RunWithScene(testScene);

            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
            return 1;
        }
        public static void Start()
        {
            Console.WriteLine("hello world!!!!!!!!");
        }
    }
}
