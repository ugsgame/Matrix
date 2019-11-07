using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.CocoStudio.Native;
using MatrixEngine.Cocos2d;

namespace MatrixEngine.CocoStudio.Scene
{
    public class SceneReader
    {
        static ScriptManager scriptManager = new ScriptManager();

        public static string SceneReaderVersion()
        {
            return NativeSceneReader.SceneReaderVersion();
        }

        public static CCNode CreateNodeWithSceneFile(string pszFileName)
        {
            IntPtr pNode = NativeSceneReader.CreateNodeWithSceneFile(pszFileName);
            if (pNode != IntPtr.Zero)
            {
                return new CCNode(pNode);
            }
            else 
            {
                return null;
            }
        }

        public static CCNode GetNodeByTag(int nTag)
        {
            IntPtr pNode = NativeSceneReader.GetNodeByTag(nTag);
            if (pNode != IntPtr.Zero)
            {
                CCNode node = scriptManager.GetScriptObject(pNode) as CCNode;
                if (node == null)
                {
                    node = new CCNode(pNode);
                    scriptManager.PutScriptObject(node);
                }
                return node;
            }
            else
            {
                return null;
            }
        }

        public static CCParticleSystem GetParticleByTag(int nTag)
        {
            IntPtr pNode = NativeSceneReader.GetNodeByTag(nTag);
            if (pNode != IntPtr.Zero)
            {
                CCParticleSystem particle = scriptManager.GetScriptObject(pNode) as CCParticleSystem;
                if (particle == null)
                {
                    particle = new CCParticleSystem(pNode);
                    scriptManager.PutScriptObject(particle);
                }
                return particle;
            }
            else
            {
                return null;
            }
        }

        public static void Purge()
        {
            NativeSceneReader.Purge();
            scriptManager.clear();
        }

        public static void DoOptimization()
        {
            NativeSceneReader.DoOptimization();
        }
    }
}
