using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCScene : CCLayer
    {
        /// <summary>
        /// pScene是指cocos2d 的 CCScene
        /// </summary>
        private IntPtr pScene = IntPtr.Zero;

        private CCNode sceneNode;

        internal CCScene(IntPtr t) : base(t) { }
        public CCScene()
            : base(NativeScene.Create())
        {
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);

            this.pScene = NativeScene.GetScene(this.CppObjPtr);

            sceneNode = new CCNode(this.pScene);
            //记录下指针数值
            NativeObject.Retain(this.pScene);
            //打开相应的事件
            this.SetState(LayerState.Touch, true);
            this.SetState(LayerState.Keypad, true);
            //默认为单点触摸模式
            //this.SetTouchMode(TouchMode.Single); 
        }

        ~CCScene()
        {
            //释放下指针数值
        //    NativeObject.Release(this.pScene);
        }

        internal IntPtr GetScene()
        {
            return pScene;
        }
        public CCNode GetSceneNode()
        {
            return sceneNode;
        }
        /// <summary>
        /// 场景进入效果完成后回调
        /// </summary>
        public virtual void OnEnterTransitionFinish()
        {
            //Console.WriteLine("OnEnterTransitionFinish");
        }

        public virtual void OnExitTransitionStart()
        {
            //Console.WriteLine("OnExitTransitionStart");
        }
    }
}
