using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d.Native;
using MatrixEngine.Math;

namespace MatrixEngine.Cocos2d
{
    public enum ResolutionPolicy
    {
        // The entire application is visible in the specified area without trying to preserve the original aspect ratio.
        // Distortion can occur, and the application may appear stretched or compressed.
        kResolutionExactFit,
        // The entire application fills the specified area, without distortion but possibly with some cropping,
        // while maintaining the original aspect ratio of the application.
        kResolutionNoBorder,
        // The entire application is visible in the specified area without distortion while maintaining the original
        // aspect ratio of the application. Borders can appear on two sides of the application.
        kResolutionShowAll,
        // The application takes the height of the design resolution size and modifies the width of the internal
        // canvas so that it fits the aspect ratio of the device
        // no distortion will occur however you must make sure your application works on different
        // aspect ratios
        kResolutionFixedHeight,
        // The application takes the width of the design resolution size and modifies the height of the internal
        // canvas so that it fits the aspect ratio of the device
        // no distortion will occur however you must make sure your application works on different
        // aspect ratios
        kResolutionFixedWidth,

        kResolutionUnKnown,
    };

    public enum Transition
    {
        CCTransitionJumpZoom,

        CCTransitionProgressRadialCCW,
        CCTransitionProgressRadialCW,
        CCTransitionProgressHorizontal,
        CCTransitionProgressVertical,
        CCTransitionProgressInOut,
        CCTransitionProgressOutIn,

        CCTransitionCrossFade,
        TransitionPageForward,
        TransitionPageBackward,
        CCTransitionFadeTR,
        CCTransitionFadeBL,
        CCTransitionFadeUp,
        CCTransitionFadeDown,
        CCTransitionTurnOffTiles,
        CCTransitionSplitRows,
        CCTransitionSplitCols,

        CCTransitionFade,
        FadeWhiteTransition,

        FlipXLeftOver,
        FlipXRightOver,
        FlipYUpOver,
        FlipYDownOver,
        FlipAngularLeftOver,
        FlipAngularRightOver,

        ZoomFlipXLeftOver,
        ZoomFlipXRightOver,
        ZoomFlipYUpOver,
        ZoomFlipYDownOver,
        ZoomFlipAngularLeftOver,
        ZoomFlipAngularRightOver,

        CCTransitionShrinkGrow,
        CCTransitionRotoZoom,

        CCTransitionMoveInL,
        CCTransitionMoveInR,
        CCTransitionMoveInT,
        CCTransitionMoveInB,
        CCTransitionSlideInL,
        CCTransitionSlideInR,
        CCTransitionSlideInT,
        CCTransitionSlideInB,
    };

    public class CCDirector
    {
        private static ScriptManager ScriptManager = new ScriptManager();

        private static CCScene currentScene;

        public static void SetNotificationLayer(CCLayer layer)
        {
            NativeDirector.SetNotificationNode(layer.CppObjPtr);

            ScriptManager.PutScriptObject(layer);

            NativeNode.OnEnter(layer.CppObjPtr);
        }

        public static CCLayer GetNotificationLayer()
        {
            //查找子的指针
            IntPtr scriptObject = NativeDirector.GetNotificationNode();
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                ///查找相关的对象缓存是否存在
                CCLayer putNode = ScriptManager.GetScriptObject(scriptObject) as CCLayer;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new CCLayer(scriptObject);
                    ScriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            else
                return null;
        }

        public static int TargetPlatform
        {
            get { return NativeDirector.GetTargetPlatform(); }
        }

        public static void SetDisplayFPS(bool display)
        {
            NativeDirector.SetDisplayFPS(display);
        }

        public static void SetFPS(float fps)
        {
            NativeDirector.SetFPS(fps);
        }

        public static float GetFPS()
        {
            return NativeDirector.GetFPS();
        }

        public static float FPS
        {
            set 
            {
                NativeDirector.SetFPS(value);
            }
            get
            {
                return NativeDirector.GetFPS();
            }
        }

        public static void SetFrameSize(Size size)
        {
            NativeDirector.SetFrameSize(size.width, size.height);
        }

        public static void SetResolutionSize(Size size, ResolutionPolicy resolution)
        {
            NativeDirector.SetResolutionSize(size.width, size.height, resolution);
        }

        public static Size GetResolutionSize()
        {
            Size size = new Size();
            NativeDirector.GetResolutionSize(ref size);
            return size;
        }

        public static Size GetFrameSize()
        {
            Size size = new Size();
            NativeDirector.GetFrameSize(ref size);
            return size;
        }

        public static float GetSecondsPerFrame()
        {
            return NativeDirector.GetSecondsPerFrame();
        }

        private static CCScheduler scheduler;

        public static CCScheduler GetScheduler()
        {
            IntPtr ptr = NativeDirector.GetScheduler();
            if (ptr != IntPtr.Zero)
            {
                if (scheduler != null && scheduler.CppObjPtr == ptr)
                {
                    return scheduler;
                }
                return scheduler = new CCScheduler(ptr);
            }
            return scheduler = null;
        }

        public static void RunWithScene(CCScene scene)
        {
            currentScene = scene;
            NativeDirector.RunWithScene(scene.GetScene());
            IntPtr layer = NativeDirector.GetNotificationNode();

            if (layer != IntPtr.Zero)
                NativeNode.OnEnter(layer);
        }

        public static void ReplaceScene(CCScene scene)
        {
            currentScene = scene;
            NativeDirector.ReplaceScene(scene.GetScene());
            IntPtr layer = NativeDirector.GetNotificationNode();
            if (layer != IntPtr.Zero)
                NativeNode.OnEnter(layer);
        }

        public static void ReplaceScene(CCScene scene, Transition tran, float dTime)
        {
            currentScene = scene;
            NativeDirector.ReplaceSceneWithTransitions(scene.GetScene(), tran, dTime);
            IntPtr layer = NativeDirector.GetNotificationNode();
            if (layer != IntPtr.Zero)
                NativeNode.OnEnter(layer);
        }

        public static void PushScene(CCScene scene)
        {
            currentScene = scene;
            NativeDirector.PushScene(scene.GetScene());
            IntPtr layer = NativeDirector.GetNotificationNode();
            if (layer != IntPtr.Zero)
                NativeNode.OnEnter(layer);
        }

        public static void PushScene(CCScene scene, Transition tran, float dTime)
        {
            currentScene = scene;
            NativeDirector.PushSceneWithTransitions(scene.GetScene(), tran, dTime);
            IntPtr layer = NativeDirector.GetNotificationNode();
            if (layer != IntPtr.Zero)
                NativeNode.OnEnter(layer);
        }

        public static void PopScene()
        {
            NativeDirector.PopScene();
        }

        public static void PopToRootScene()
        {
            NativeDirector.PopToRootScene();
        }

        public static void PopToSceneStackLevel(int level)
        {
            NativeDirector.PopToSceneStackLevel(level);
        }

        public static CCScene GetRuningScene()
        {
            IntPtr runScene = NativeDirector.GetRuningScene();
            if (runScene != IntPtr.Zero)
            {

                if (currentScene == null)
                {
                    IntPtr scriptObject = NativeNode.GetUserData(runScene);
                    //如果不是空指针表示存在子
                    if (scriptObject != IntPtr.Zero)
                    {
                        currentScene = new CCScene(scriptObject);
                    }
                }
                return currentScene;
            }
            return null;
        }

        public static void End()
        {
            NativeDirector.End();
        }

        public static void Pause()
        {
            NativeDirector.Pause();
        }

        public static void Resume()
        {
            NativeDirector.Resume();
        }

        public static bool IsPaused()
        {
            return NativeDirector.IsPaused();
        }

        public static Vector2 ConverToUI(Vector2 pos)
        {
            Vector2 uiPos = new Vector2();
            NativeDirector.ConverToUI(out pos, ref uiPos);
            return uiPos;
        }

        public static Vector2 ConverToGL(Vector2 pos)
        {
            Vector2 glPos = new Vector2();
            NativeDirector.ConverToGL(out pos, ref glPos);
            return glPos;
        }
    }
}
