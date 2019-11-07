using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    //TODO:加入CCTexture2D
    public class CCTextureCache
    {
        static ScriptManager scriptManager = new ScriptManager();
        public static void PurgeSharedTextureCache()
        {
            NativeTextureCache.PurgeSharedTextureCache();
        }

        private static CCTexture2D FideTexture(IntPtr texPtr)
        {
            IntPtr scriptObject = texPtr;
            if (scriptObject != IntPtr.Zero)
            {
                CCTexture2D texture = scriptManager.GetScriptObject(scriptObject) as CCTexture2D;
                if (texture == null)
                {
                    texture = new CCTexture2D(scriptObject);
                    scriptManager.PutScriptObject(texture);
                }
                return texture;
            }
            return null;
        }

        public static CCTexture2D AddImage(string fileimage)
        {
            IntPtr scriptObject = NativeTextureCache.AddImage(fileimage);
            return FideTexture(scriptObject);
        }
        //TODO:加入c#的回调
        //public static void AddImageAsync(const char *path, CCObject *target, SEL_CallFuncO selector);

        //public static CCTexture2D AddUIImageWithKey(IntPtr image, string key);
        public static CCTexture2D TextureForKey(string key)
        {
            IntPtr scriptObject = NativeTextureCache.TextureForKey(key);
            return FideTexture(scriptObject);
        }

        public static bool ReloadTexture(string fileName)
        {
            return NativeTextureCache.ReloadTexture(fileName);
        }
        public static void RemoveAllTextures()
        {
            scriptManager.clear();
            NativeTextureCache.RemoveAllTextures();
        }
        public static void RemoveUnusedTextures()
        {
            NativeTextureCache.RemoveUnusedTextures();
        }
        public static void RemoveTexture(CCTexture2D texture)
        {
            //TODO:C# texure是否跟着删除？
            scriptManager.RemoveScriptObject(texture);
            NativeTextureCache.RemoveTexture(texture.CppObjPtr);
            //texture = null;
        }
        public static void RemoveTextureForKey(string textureKeyName)
        {
            NativeTextureCache.RemoveTextureForKey(textureKeyName);
        }
        public static void DumpCachedTextureInfo()
        {
            NativeTextureCache.DumpCachedTextureInfo();
        }
        public static CCTexture2D AddPVRImage(string filename)
        {
            IntPtr scriptObject = NativeTextureCache.AddPVRImage(filename);
            return FideTexture(scriptObject);
        }
        public static CCTexture2D AddETCImage(string filename)
        {
            IntPtr scriptObject = NativeTextureCache.AddETCImage(filename);
            return FideTexture(scriptObject);
        }

        public static void ReloadAllTextures()
        {
            NativeTextureCache.ReloadAllTextures();
        }
    }
}
