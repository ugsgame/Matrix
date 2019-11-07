using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.CompilerServices;
using MatrixEngine.CocoStudio.GUI;
using MatrixEngine.Math;
using MatrixEngine.Cocos2d;
using MatrixEngine.Engine;

namespace MatrixEngine.Cocos2d.Native
{
    /// <summary>
    /// 
    /// </summary>
    internal static class NativeFileUtils
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddSearchPath(string filePath);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveSearchPath(string filePath);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string FullPathForFilename(string filename);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsFileExist(string filePath);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool DeleteFile(string filePath);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetWritablePath();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetSDPath();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsSDExist();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetFileDataToString(string pszFileName, string pszMode, out int pSize);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetFileDataToIntptr(string pszFileName, string pszMode, out int pSize);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr FileOpen(string filePath, string pszMode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool FileClose(IntPtr file);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateBuffer(int mSize);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void DeleteBuffer(IntPtr buffer);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void WriteBuffer(IntPtr file, int size, IntPtr buffer);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ReadBuffer(IntPtr file, out int pSize);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void WriteBufferByte(IntPtr buffer, int size, int seek, byte _byte);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static byte ReadBufferByte(IntPtr buffer, int size, int seek);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void WriteBufferShort(IntPtr buffer, int size, int seek, short _short);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static short ReadBufferShort(IntPtr buffer, int size, int seek);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void WriteBufferInt(IntPtr buffer, int size, int seek, int _int);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int ReadBufferInt(IntPtr buffer, int size, int seek);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void WriteBufferFloat(IntPtr buffer, int size, int seek, float _float);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float ReadBufferFloat(IntPtr buffer, int size, int seek);
    }

    /// <summary>
    /// CCDirector接口
    /// </summary>
    internal static class NativeDirector
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetNotificationNode(IntPtr pNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetNotificationNode();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetTargetPlatform();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetDisplayFPS(bool display);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetFPS(float FPS);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetFPS();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetFrameSize(float width, float height);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetResolutionSize(float width, float height, ResolutionPolicy resolution);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetResolutionSize(ref Size size);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetFrameSize(ref Size size);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetSecondsPerFrame();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetScheduler();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RunWithScene(IntPtr pScene);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ReplaceScene(IntPtr pScene);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ReplaceSceneWithTransitions(IntPtr pScene, Transition tran, float dTime);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PushScene(IntPtr pScene);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PushSceneWithTransitions(IntPtr pScene, Transition tran, float dTime);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PopScene();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PopToRootScene();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PopToSceneStackLevel(int level);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetRuningScene();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetObjectCount();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetReleasePoolSize();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void End();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Pause();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Resume();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsPaused();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ConverToUI(out Vector2 pos, ref Vector2 uiPos);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ConverToGL(out Vector2 pos, ref Vector2 glPos);
    }

    internal static class NativeTextureCache
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PurgeSharedTextureCache();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr AddImage(string fileimage);
        //TODO:加入c#的回调
        //[MethodImplAttribute(MethodImplOptions.InternalCall)]
        //extern internal static void AddImageAsync(const char *path, CCObject *target, SEL_CallFuncO selector);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr AddUIImageWithKey(IntPtr image, string key);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr TextureForKey(string key);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool ReloadTexture(string fileName);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveAllTextures();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveUnusedTextures();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveTexture(IntPtr texture);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveTextureForKey(string textureKeyName);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void DumpCachedTextureInfo();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr AddPVRImage(string filename);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr AddETCImage(string filename);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ReloadAllTextures();
    }


    /// <summary>
    /// 
    /// </summary>
    internal static class NativeScene
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetScene(IntPtr pCCScene);

    }
    /// <summary>
    /// 
    /// </summary>
    internal static class NativeLayer
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetState(IntPtr pLayer, int state, bool enable);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ScheduleStep(IntPtr pLayer, float dt);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void UnscheduleStep(IntPtr pLayer);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTouchMode(IntPtr pLayer, int mode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static TouchMode GetTouchMode(IntPtr pLayer); 

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTouchEnabeld(IntPtr pLayer, bool enable);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsTouchEnabeld(IntPtr pLayer);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetAccelerometerEnabled(IntPtr pLayer, bool enable);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsAccelerometerEnabled(IntPtr pLayer);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetKeypadEnabled(IntPtr pLayer, bool enable);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsKeypadEnabled(IntPtr pLayer);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTouchPriority(IntPtr pLayer, int priority);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetTouchPriority(IntPtr pLayer);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ScheduleUpdate(IntPtr pLayer);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float TickTime(IntPtr pLayer);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetSwallowsTouches(IntPtr pLayer, bool bSwallowsTouches);
    }
    /// <summary>
    /// Cocos2d CCObject基本接口函数
    /// </summary>
    internal static class NativeObject
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetMonoObject(IntPtr obj, CCObject _object);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static CCObject GetMonoObject(IntPtr obj);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsBindMonoObject(IntPtr obj);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void UnBindMonoObject(IntPtr obj);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Release(IntPtr obj);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void DelayRelease(IntPtr obj);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Retain(IntPtr obj);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int RetainCount(IntPtr obj);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsEqual(IntPtr obj, IntPtr _obj);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Copy(IntPtr obj);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetID(IntPtr obj);
    }
    /// <summary>
    /// Cocos2d CCAction基本接口函数
    /// </summary>
    internal static class NativeAction
    {
        //Action
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsDone(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetTag(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTag(IntPtr action, int tag);

        //FiniteTimeAction
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetDuration(IntPtr action, float time);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetDuration(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Reverse(IntPtr action);
        //InstantActions
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionShow();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionHide();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionToggleVisibility();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionRemoveSelf(bool isNeedCleanUp);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionFlipX(bool x);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionFlipY(bool y);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionPlace(float x, float y);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionCallFunc(object action);
        //IntervalActions
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionSequence(CCAction[] mArray);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionSpawn(CCAction[] mArray);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionRepeat(IntPtr action, int times);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionRepeatForever(IntPtr action);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionRotateTo1(float fDuration, float fDeltaAngle);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionRotateTo2(float fDuration, float fDeltaAngleX, float fDeltaAngleY);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionRotateBy1(float fDuration, float fDeltaAngle);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionRotateBy2(float fDuration, float fDeltaAngleX, float fDeltaAngleY);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionMoveBy(float duration, float posX, float posY);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionMoveTo(float duration, float posX, float posY);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionSkewTo(float t, float sx, float sy);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionSkewBy(float t, float sx, float sy);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionJumpBy(float duration, float posX, float posY, float height, int jumps);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionJumpTo(float duration, float posX, float posY, float height, int jumps);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionBezierBy(float duration, float pos1X, float pos1Y, float pos2X, float pos2Y, float pos3X, float pos3Y);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionBezierTo(float duration, float pos1X, float pos1Y, float pos2X, float pos2Y, float pos3X, float pos3Y);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionScaleTo1(float duration, float s);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionScaleTo2(float duration, float sx, float sy);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionScaleBy1(float duration, float s);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionScaleBy2(float duration, float sx, float sy);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionBlink(float duration, int uBlinks);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionFadeIn(float time);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionFadeOut(float time);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionFadeTo(float time, int opacity);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionTintTo(float duration, int red, int green, int blue);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionTintBy(float duration, int red, int green, int blue);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionDelayTime(float duration);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionReverseTime(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionProgressTo(float duration, float fPercent);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionProgressFromTo(float duration, float fFromPercentage, float fToPercentage);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionAnimate(IntPtr pAnimation);
        //EaseActions
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseIn(IntPtr action, float fRate);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseOut(IntPtr action, float fRate);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseInOut(IntPtr action, float fRate);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseExponentialIn(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseExponentialOut(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseExponentialInOut(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseSineIn(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseSineOut(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseSineInOut(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseElastic1(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseElastic2(IntPtr action, float fPeriod);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseElasticIn1(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseElasticIn2(IntPtr action, float fPeriod);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseElasticOut1(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseElasticOut2(IntPtr action, float fPeriod);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseElasticInOut1(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseElasticInOut2(IntPtr action, float fPeriod);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseBounce(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseBounceIn(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseBounceOut(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseBounceInOut(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseBackIn(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseBackOut(IntPtr action);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActionEaseBackInOut(IntPtr action);

        //扩展action
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr MCAction(float fDuration);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr MCActionInterval(float fDuration);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr MCActionInstant();
    }
    /// <summary>
    /// CCAnimation
    /// </summary>
    internal static class NativeAnimation
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateWithSpriteFrames(CCSpriteFrame[] spriteFrames);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateWithAnimationFrames(CCAnimationFrame[] animationFrameNames);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddSpriteFrame(IntPtr pAnimation, IntPtr pFrame);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddSpriteFrameWithFileName(IntPtr pAnimation, string pzFileName);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddSpriteFrameWithTexture(IntPtr pAnimation, IntPtr pTexture, ref Rect rect);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetTotalDelayUnits(IntPtr pAnimation);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetDelayPerUnit(IntPtr pAnimation,float var);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetDelayPerUnit(IntPtr pAnimation);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetDuration(IntPtr pAnimation);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetFrames(IntPtr pAnimation, CCSpriteFrame[] frames);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetRestoreOriginalFrame(IntPtr pAnimation, bool var);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool GetRestoreOriginalFrame(IntPtr pAnimation);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetLoops(IntPtr pAnimation, int var);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetLoops(IntPtr pAnimation);
    }

    internal static class NativeAnimationFrame
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetSpriteFrame(IntPtr pAnimationFrame, IntPtr pSpriteFrame);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetDelayUnits(IntPtr pAnimationFrame, float var);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetDelayUnits(IntPtr pAnimationFrame);
    }
    /// <summary>
    /// CCSpriteFrame
    /// </summary>
    internal static class NativeSpriteFrame
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateWithFile(string fileName, ref Rect rect);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateWithTexture(IntPtr pTexture, ref Rect rect);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetRectInPixels(IntPtr frame, out Rect rect);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetRectInPixels(IntPtr frame, ref Rect rect);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsRotated(IntPtr frame);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetRotated(IntPtr frame, bool bRotated);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetRect(IntPtr frame, out Rect rect);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetRect(IntPtr frame, ref Rect rect);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetOffsetInPixels(IntPtr frame, out Vector2 offsetInPixels);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetOffsetInPixels(IntPtr frame, ref Vector2 offsetInPixels);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetOriginalSizeInPixels(IntPtr frame, out Size sizeInPixels);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetOriginalSizeInPixels(IntPtr frame, ref Size sizeInPixels);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetOriginalSize(IntPtr frame, out Size sizeInPixels);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetOriginalSize(IntPtr frame, ref Size sizeInPixels);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetTexture(IntPtr frame);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTexture(IntPtr frame, IntPtr pTexture);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetOffset(IntPtr frame, out Vector2 offsets);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetOffset(IntPtr frame, ref Vector2 offsets);
    }
    /// <summary>
    /// CCSpriteBatchNode
    /// </summary>
    internal static class NativeSpriteBatchNode
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateWithFile(string fileName);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateWithTexture(IntPtr texture);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveChildAtIndex(IntPtr pSpriteBatchNode, int index, bool doCleanup);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void InsertChild(IntPtr pSpriteBatchNode, IntPtr child, int index);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AppendChild(IntPtr pSpriteBatchNode, IntPtr sprite);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveSpriteFromAtlas(IntPtr pSpriteBatchNode, IntPtr sprite);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int RebuildIndexInOrder(IntPtr pSpriteBatchNode, IntPtr parent, int index);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int HighestAtlasIndexInChild(IntPtr pSpriteBatchNode, IntPtr sprite);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int LowestAtlasIndexInChild(IntPtr pSpriteBatchNode, IntPtr sprite);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int AtlasIndexForChild(IntPtr pSpriteBatchNode, IntPtr sprite, int z);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ReorderBatch(IntPtr pSpriteBatchNode, bool reorder);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetTextureAtlas(IntPtr pSpriteBatchNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTextureAtlas(IntPtr pSpriteBatchNode, IntPtr textureAtlas);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetTexture(IntPtr pSpriteBatchNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTexture(IntPtr pSpriteBatchNode, IntPtr texture);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetBlendFunc(IntPtr pSpriteBatchNode, ref BlendFunc blendFunc);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetBlendFunc(IntPtr pSpriteBatchNode, out BlendFunc blendFunc);
    }
    /// <summary>
    /// Cocos2d CCNode基本接口函数
    /// </summary>
    internal static class NativeNode
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetUserData(IntPtr pNode, IntPtr pData);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetUserData(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetZOrder(IntPtr pNode, int zOrder);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetZOrder(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetVertexZ(IntPtr pNode, float vertexZ);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetVertexZ(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetScaleX(IntPtr pNode, float fScaleX);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetScaleX(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetScaleY(IntPtr pNode, float fScaleY);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetScaleY(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetScale(IntPtr pNode, float fScale);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetScale(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetPostion(IntPtr pNode, float posX, float posY);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetPostionX(IntPtr pNode, float posX);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetPostionY(IntPtr pNode, float posY);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetPostionX(IntPtr pNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetPostionY(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetSkewX(IntPtr pNode, float fSkewX);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetSkewX(IntPtr pNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetSkewY(IntPtr pNode, float fSkewY);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetSkewY(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetAnchorPoint(IntPtr pNode, float x, float y);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetAnchorPointX(IntPtr pNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetAnchorPointY(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetContentSize(IntPtr pNode, float wide, float height);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetContentSizeW(IntPtr pNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetContentSizeH(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetVisible(IntPtr pNode, bool visible);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsVisible(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetRotation(IntPtr pNode, float fRotation);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetRotation(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetOrderOfArrival(IntPtr pNode, int order);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetOrderOfArrival(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTag(IntPtr pNode, int tag);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetTag(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetScheduler(IntPtr pNode, IntPtr pScheduler);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetScheduler(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void BoundingBox(IntPtr pNode, out Rect box);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetParent(IntPtr pNode, IntPtr pParent);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetParent(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetIgnoreAnchorPointForPosition(IntPtr pNode, bool ignore);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsIgnoreAnchorPointForPosition(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddChild(IntPtr pParent, IntPtr pNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddChildByOrder(IntPtr pParent, IntPtr pNode, int zOrder);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddChildByOrderTag(IntPtr pParent, IntPtr pNode, int zOrder, int tag);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetChildByTag(IntPtr pParent, int tag);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetChildByIndex(IntPtr pParent, int index);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetChildrenCount(IntPtr pParent);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveFromParent(IntPtr pParent);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveFromParentAndCleanup(IntPtr pParent, bool cleanup);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveChild(IntPtr pNode, IntPtr pChild);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveChildAndCleanup(IntPtr pNode, IntPtr pChild, bool cleanup);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveChildByTag(IntPtr pNode, int tag);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveChildByTagAndCleanup(IntPtr pNode, int tag, bool cleanup);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveAllChildren(IntPtr pNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveAllChildrenAndCleanup(IntPtr pNode, bool cleanup);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr RunAction(IntPtr pNode, IntPtr pAction);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void StopAction(IntPtr pNode, IntPtr pAction);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void StopAllAction(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void CoverToNodeSpace(IntPtr pNode, ref Vector2 intPoint, out Vector2 outPoint);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ConvertToWorldSpace(IntPtr pNode, ref Vector2 intPoint, out Vector2 outPoint);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ConvertToNodeSpaceAR(IntPtr pNode, ref Vector2 intPoint, out Vector2 outPoint);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ConvertToWorldSpaceAR(IntPtr pNode, ref Vector2 intPoint, out Vector2 outPoint);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void OnEnter(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ScheduleUpdate(IntPtr pNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void UnscheduleUpdate(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ResumeSchedulerAndActions(IntPtr pNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PauseSchedulerAndActions(IntPtr pNode);
    }
    /// <summary>
    /// Cocos2d 的CCNodeRGBA基本接口函数
    /// </summary>
    internal static class NativeNodeRGBA
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetAlpha(IntPtr pNode, int alpha);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetAlpha(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetColor(IntPtr pNode, int r, int g, int b);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetColor(IntPtr pNode, out Color32 color);
    }

    internal static class NativeTexture2D
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool InitWithImage(IntPtr uiImage);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool InitWithString(string text, string fontName, float fontSize, ref Size dimensions, CCTextAlignment hAlignment, CCVerticalTextAlignment vAlignment);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool InitWithPVRFile(string file);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool InitWithETCFile(string file);
    }

    /// <summary>
    /// CCSprite基本接口
    /// </summary>
    internal static class NativeSprite
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateByFile(string fileName);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateWithSpriteFrameName(string pszSpriteFrameName);
        //[MethodImplAttribute(MethodImplOptions.InternalCall)]
        //extern internal static IntPtr CreateByPak(CCSprite sprite, IntPtr pak, string fileName);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetBlendFunc(IntPtr sprite, ref BlendFunc blendfunc);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetBlendFunc(IntPtr sprite, out BlendFunc blendfunc);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetBatchNode(IntPtr sprite, IntPtr batchNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetBatchNode(IntPtr sprite);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTextureRect1(IntPtr sprite, float x, float y, float w, float h);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTextureRect2(IntPtr sprite, float x, float y, float w, float h, bool rotated, float sw, float wh);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetTextureRect(IntPtr sprite, Rect rect);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsDirty(IntPtr sprite);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetDirty(IntPtr sprite, bool bDirty);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetAtlasIndex(IntPtr sprite);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetAtlasIndex(IntPtr sprite, int uAtlasIndex);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetOffsetPosition(IntPtr sprite, Vector2 point);
    }

    internal static class NativeSpriteFrameCache
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddSpriteFramesWithFile_0(string pszPlist);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddSpriteFramesWithFile_1(string plist, string textureFileName);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddSpriteFramesWithFile_2(string pszPlist, IntPtr pobTexture);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddSpriteFrame(IntPtr pobFrame, string pszFrameName);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveSpriteFrames();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveUnusedSpriteFrames();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveSpriteFrameByName(string pszName);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveSpriteFramesFromFile(string plist);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveSpriteFramesFromTexture(IntPtr texture);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr SpriteFrameByName(string pszName);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PurgeSharedSpriteFrameCache();
    }

    internal static class NativeLabel
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateLabelAtlasWithFont(string text, string fntFile);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateLabelAtlasWithMap(string text, string charMapFile, int itemWidth, int itemHeight, int starCharMap);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateLabelBMFont(string text, string fintFile, float width, CCTextAlignment alignment, ref Vector2 pos);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateLabelTTF(string text, string fintFile, float fontSize, ref  Size dimensions, CCTextAlignment hAlignment, CCVerticalTextAlignment vAlignment);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetString_Atlas(IntPtr pLabel, string text);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetString_BMFont(IntPtr pLabel, string text);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetString_TTF(IntPtr pLabel, string text);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetString_Atlas(IntPtr pLabel);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetString_BMFont(IntPtr pLabel);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetString_TTF(IntPtr pLabel);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTextDefinition_TTF(IntPtr pLabel, IntPtr ccFontDefinition);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetTextDefinition_TTF(IntPtr pLabel);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void EnableShadow_TTF(IntPtr pLabel, ref Size shadowOffset, float shadowOpacity, float shadowBlur, bool mustUpdateTexture);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void DisableShadow_TTF(IntPtr pLabel, bool mustUpdateTexture);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void EnableStroke_TTF(IntPtr pLabel, ref Color32 strokeColor, float strokeSize, bool mustUpdateTexture);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void DisableStroke_TTF(IntPtr pLabel, bool mustUpdateTexture);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetFontFillColor_TTF(IntPtr pLabel, ref Color32 tintColor, bool mustUpdateTexture);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static CCTextAlignment GetHorizontalAlignment_TTF(IntPtr pLabel);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetHorizontalAlignment_TTF(IntPtr pLabel, CCTextAlignment alignment);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static CCVerticalTextAlignment GetVerticalAlignment_TTF(IntPtr pLabel);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetVerticalAlignment_TTF(IntPtr pLabel, CCVerticalTextAlignment verticalAlignment);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetDimensions_TTF(IntPtr pLabel, ref Size dim);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetDimensions_TTF(IntPtr pLabel, ref Size dim);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetFontSize_TTF(IntPtr pLabel);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetFontSize_TTF(IntPtr pLabel, float fontSize);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetFontName_TTF(IntPtr pLabel);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetFontName_TTF(IntPtr pLabel, string fontName);
    }

    internal static class NativeEffectNode
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetRealTimeReset(IntPtr pNode, bool realTime);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ResetShader(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetIsEnable(IntPtr pNode, bool enable);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsEnale(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GrayNodeCreate();
    }

    internal static class NativeParticleSystem
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create(string plistFile);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsActive(IntPtr particle);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsBlendAdditive(IntPtr particle);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetBlendAdditive(IntPtr particle, bool value);
        //
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void InitIndices(IntPtr particle);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void InitTexCoordsWithRect(IntPtr particle, float rx, float fy, float rw, float rh);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetDisplayFrame(IntPtr particle, IntPtr spriteFrame);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTextureWithRect(IntPtr particle, IntPtr texture, float rx, float ry, float rw, float rh);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]

        extern internal static void SetPositionType(IntPtr particle, tCCPositionType type);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static tCCPositionType GetPositionType(IntPtr particle);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetEmitterMode(IntPtr particle, kCCParticleMode mode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static kCCParticleMode GetEmitterMode(IntPtr particle);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetRotationIsDir(IntPtr particle, bool var);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool GetRotationIsDir(IntPtr particle);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsAutoRemoveOnFinish(IntPtr particle);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetAutoRemoveOnFinish(IntPtr particle, bool var);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTexture(IntPtr particle, IntPtr texture);


        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PostStep(IntPtr particle);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetBatchNode(IntPtr particle, IntPtr batchNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Start(IntPtr particle);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Play(IntPtr particle);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Stop(IntPtr particle);
    }

    internal static class NativeScheduler
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTimeScale(IntPtr pScheduler, float timeScale);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetTimeScale(IntPtr pScheduler);
    }

    internal static class NativeScheduleNode
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create(CCNode node, string method, float dt);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Schedule(IntPtr pScheduleNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Unschedule(IntPtr pScheduleNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsScheduling(IntPtr pScheduleNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetMethodName(IntPtr pScheduleNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetMethodName(IntPtr pScheduleNode, string method);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetDelayTime(IntPtr pScheduleNode, float dt);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetDelayTime(IntPtr pScheduleNode);
    }

    internal static class NativeProgressTimer
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create(IntPtr sprite);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static CCProgressTimerType GetType(IntPtr progeressTimer);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetType(IntPtr progeressTimer, CCProgressTimerType _type);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetPercentage(IntPtr progeressTimer);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetPercentage(IntPtr progeressTimer, float fPercentage);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetSprite(IntPtr progeressTimer, IntPtr sprite);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetSprite(IntPtr progeressTimer);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetReverseProgress(IntPtr progeressTimer, bool reverse);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsReverseDirection(IntPtr progeressTimer);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetReverseDirection(IntPtr progeressTimer, bool value);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetMidPoint(IntPtr progeressTimer, ref Vector2 pos);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetMidPoint(IntPtr progeressTimer, out Vector2 pos);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetBarChangeRate(IntPtr progeressTimer, ref Vector2 pos);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetBarChangeRate(IntPtr progeressTimer, out Vector2 pos);
    }

    internal static class NativeClippingNode
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateWithStencil(IntPtr pStencil);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetStencil(IntPtr pNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetStencil(IntPtr pNode, IntPtr pStencil);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]

        extern internal static IntPtr AddDefaultStendcil(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetAlphaThreshold(IntPtr pNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetAlphaThreshold(IntPtr pNode, float fAlphaThreshold);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsInverted(IntPtr pNode);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetInverted(IntPtr pNode, bool bInverted);
    }
}
