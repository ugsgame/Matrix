
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCSpriteFrameCache
    {
        static internal ScriptManager scriptManager = new ScriptManager();

	    public static void AddSpriteFramesWithFile(string pszPlist)
        {
            NativeSpriteFrameCache.AddSpriteFramesWithFile_0(pszPlist);
        }

        public static void AddSpriteFramesWithFile(string plist, string textureFileName)
        {
            NativeSpriteFrameCache.AddSpriteFramesWithFile_1(plist, textureFileName);
        }

        public static void AddSpriteFramesWithFile(string pszPlist, CCTexture2D pobTexture)
        {
            NativeSpriteFrameCache.AddSpriteFramesWithFile_2(pszPlist, pobTexture.CppObjPtr);
        }

        public static void AddSpriteFrame(CCSpriteFrame pobFrame, string pszFrameName)
        {
            NativeSpriteFrameCache.AddSpriteFrame(pobFrame.CppObjPtr, pszFrameName);
        }

        public static void RemoveSpriteFrames()
        {
            NativeSpriteFrameCache.RemoveSpriteFrames();
        }

        public static void RemoveUnusedSpriteFrames()
        {
            NativeSpriteFrameCache.RemoveUnusedSpriteFrames();
        }

        public static void RemoveSpriteFrameByName(string pszName)
        {
            NativeSpriteFrameCache.RemoveSpriteFrameByName(pszName);
        }

        public static void RemoveSpriteFramesFromFile(string plist)
        {
            NativeSpriteFrameCache.RemoveSpriteFramesFromFile(plist);
        }

        public static void RemoveSpriteFramesFromTexture(CCTexture2D texture)
        {
            NativeSpriteFrameCache.RemoveSpriteFramesFromTexture(texture.CppObjPtr);
        }

        public static CCSpriteFrame SpriteFrameByName(string pszName)
        {
            IntPtr pSpriteFrame = NativeSpriteFrameCache.SpriteFrameByName(pszName);
            CCSpriteFrame spriteFrame = null;
            if (NativeObject.IsBindMonoObject(pSpriteFrame) && NativeObject.GetMonoObject(pSpriteFrame)!=null)
            {
                object obj = NativeObject.GetMonoObject(pSpriteFrame);
                if (obj is CCSpriteFrame)
                {
                    spriteFrame = (CCSpriteFrame)obj;
                }
                else
                {
                    Console.WriteLine("Waring:Native Object("+obj+")is not CCSpriteFrame!!");
                    spriteFrame = new CCSpriteFrame(pSpriteFrame);
                }
                
            }
            else
            {
                spriteFrame = new CCSpriteFrame(pSpriteFrame);
            }
            scriptManager.PutScriptObject(spriteFrame);
            return spriteFrame;
        }

        public bool CheckSpriteFrameByName(string pszName)
        {
            IntPtr pSpriteFrame = NativeSpriteFrameCache.SpriteFrameByName(pszName);
            if(pSpriteFrame == IntPtr.Zero)   
                return false;
            else
                return true;
        }

        public static void PurgeSharedSpriteFrameCache()
        {
            NativeSpriteFrameCache.PurgeSharedSpriteFrameCache();
        }
    }
}
