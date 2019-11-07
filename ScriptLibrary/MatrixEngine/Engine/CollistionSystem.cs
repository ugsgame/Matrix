using System;
using MatrixEngine.Cocos2d;
using MatrixEngine.Native;

namespace MatrixEngine
{
    public class CollistionSystem
    {
        private static CCNode putNode;

        public static CCNode GetSystemNode()
        {
            //查找子的指针
            IntPtr scriptObject = NativeCollistionSystem.GetCollistionSystem();

            if (scriptObject != IntPtr.Zero)
            {
                if (putNode == null || putNode.CppObjPtr != scriptObject)
                {
                    //对象类型缓存
                    putNode = new CCNode(scriptObject);

                    return putNode;
                }
            }
            return putNode;
        }

        public static void SetDrawLayer(CCNode node)
        {
            CCNode sysnode = GetSystemNode();
            if (sysnode != null)
            {
                sysnode.RemoveFromParent();
            }
            NativeCollistionSystem.CollistionSystemSetDrawLayer(node.CppObjPtr);
        }

        public static void Update(float dTime)
        {
            NativeCollistionSystem.Update(dTime);
        }
    }
}