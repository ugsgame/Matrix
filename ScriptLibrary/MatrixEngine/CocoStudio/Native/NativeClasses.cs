using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.CompilerServices;

using MatrixEngine.CocoStudio.Armature;
using MatrixEngine.CocoStudio.GUI;
using MatrixEngine.Math;
using MatrixEngine.Cocos2d;

namespace MatrixEngine.CocoStudio.Native
{
    internal class NativeArmDataManager
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddArmatureFileInfo(string configFilePath);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddArmatureFileInfoes(string imagePath, string plistPath, string configFilePath);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddArmatureFileInfoAsync(string configFilePath,ILoading loading);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddArmatureFileInfoesAsync(string imagePath, string plistPath, string configFilePath,ILoading loading);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveArmatureFileInfo(string configFilePath);
    }

    internal class NativeArmature
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RegisterContactListener();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void BindGameActor(IntPtr pArm, IntPtr pActor);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void UnBindGameActor(IntPtr pArm);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateColliderFilter(ushort categoryBits /* = 0x0001 */, ushort maskBits /* = 0xFFFF */,short groupIndex );

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateWithName(string name);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateWithNameAndBone(string name, IntPtr bone);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static  void GetBoneRect(IntPtr pArm,string boneName,ref Rect rect);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetBonePosition(IntPtr pArm, string boneName, ref Vector2 pos);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool BoneIsDisplay(IntPtr pArm,string boneName);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddBone(IntPtr pArm,IntPtr pBone);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetBone(IntPtr pArm,string name);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveBone(IntPtr pArm,IntPtr pBone, bool recursion);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ChangeBoneParent(IntPtr pArm, IntPtr pBone, string parentName);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void  SetBlendFunc(IntPtr pArm,ref BlendFunc blendfuc);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetBlendFunc(IntPtr pArm, out BlendFunc blendfuc);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void  SetParticlePositionType(IntPtr pArm,tCCPositionType posType);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static tCCPositionType GetParticlePositionType(IntPtr pArm);
         
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetAnimation(IntPtr pArm,IntPtr pAnim);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetAnimation(IntPtr pArm);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetColliderFilter(IntPtr pArm, IntPtr pColliderFilter);
    }

    internal class NativeAnimation
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create(IntPtr pArmature);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetAnimScale(IntPtr pArmature, float scale);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetAnimScale(IntPtr pArmature);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetSpeedScale(IntPtr pArmature,float scale);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetSpeedScale(IntPtr pArmature);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PlayWithName(IntPtr pArmature,string name,int durationTo,int durationTween,int loop);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PlayWithIndex(IntPtr pArmature, int animationIndex, int durationTo, int durationTween, int loop);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PlayByIndex(IntPtr pArmature, int animationIndex,  int durationTo, int durationTween, int loop);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GotoAndPlay(IntPtr pArmature, int frameIndex);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GotoAndPause(IntPtr pArmature, int frameIndex);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Pause(IntPtr pArmature);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Resume(IntPtr pArmature);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Stop(IntPtr pArmature);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetMovementCount(IntPtr pArmature);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetCureentMovementID(IntPtr pArmature);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetMovementEvent(IntPtr pArmature,IAnimationEvent _event);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetFrameEvent(IntPtr pArmature, IAnimationEvent _event);
    }

    internal class NativeBone
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateWithName(string name);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddDisplay(IntPtr pBone,IntPtr pDisplayData,int index);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddNodeDisplay(IntPtr pBone, IntPtr pNode, int index);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveDisplay(IntPtr pBone, int index);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ChangeDisplayWithIndex(IntPtr pBone,int index,bool force);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ChangeDisplayWithName(IntPtr pBone,string name,bool force);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddChildBone(IntPtr pBone,IntPtr childBone);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetParentBone(IntPtr pBone,IntPtr pParent);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetParentBone(IntPtr pBone);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveFromParent(IntPtr pBone,bool recursion);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveChildBone(IntPtr pBone,IntPtr pChildBone,bool recursion);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetDisplayRenderNode(IntPtr pBone);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static DisplayType GetDisplayRenderType(IntPtr pBone);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetName(IntPtr pBone);
    }

    internal class NativeGUIReader
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr WidgetFromJsonFile(string fileName);
    }

    internal class NativeUILayer
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void AddWidget(IntPtr layer, IntPtr widget);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RemoveWidget(IntPtr layer, IntPtr widget);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetWidgetByTag(IntPtr layer, int tag);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetWidgetByName(IntPtr layer, string name);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetRootWidget(IntPtr layer);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float TickTime(IntPtr pLayer);
    }

    internal class NativeUIWidget
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetEnabled(IntPtr widget ,bool enabled);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static bool IsEnabled(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetBright(IntPtr widget ,bool bright);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static bool IsBright(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetTouchEnabled(IntPtr widget,bool enabled);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsTouchEnabled(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetBrightStyle(IntPtr widget ,int style);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetFocused(IntPtr widget ,bool fucosed);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static bool IsFocused(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static float GetLeftInParent(IntPtr widget);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static float GetBottomInParent(IntPtr widget);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static float GetRightInParent(IntPtr widget);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static float GetTopInParent(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr GetChildByName(IntPtr widget ,string name);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddTouchEventListener(IntPtr widget, object touchEvent);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetPositionType(IntPtr widget ,int type);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static int  GetPositionType(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void SetFlipX(IntPtr widget ,bool flipX);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  bool IsFlipX(IntPtr widget);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void SetFlipY(IntPtr widget ,bool flipY);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  bool IsFlipY(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetColor(IntPtr widget, int r, int g, int b);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetColor(IntPtr widget, out Color32 color);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetOpacity(IntPtr widget, int Opacity);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static  int GetOpacity(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void GetTouchStartPos(IntPtr widget,out Vector2 pos);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetTouchMovePos(IntPtr widget, out Vector2 pos);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void GetTouchEndPos(IntPtr widget, out Vector2 pos);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetName(IntPtr widget ,string name);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static string GetName(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static int GetWidgetType(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetSize(IntPtr widget ,float w, float h);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void GetSize(IntPtr widget,out Size size);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetSizeType(IntPtr widget ,int type);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static int  GetSizeType(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetLayoutParameter(IntPtr widget ,IntPtr parameter);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr GetLayoutParameter(IntPtr widget ,int type);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void IgnoreContentAdaptWithSize(IntPtr widget ,bool ignore);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static bool IsIgnoreContentAdaptWithSize(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetDescription(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void AddNode(IntPtr widget,IntPtr node);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void AddNodeWithzOrder(IntPtr widget,IntPtr node, int zOrder);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void AddNodeWithzOrderAndTag(IntPtr widget,IntPtr node, int zOrder, int tag);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr GetNodeByTag(IntPtr widget,int tag);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr GetNodeByIndex(IntPtr widget,int index);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void RemoveNode(IntPtr widget,IntPtr node);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void RemoveNodeByTag(IntPtr widget,int tag);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void RemoveAllNodes(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Clone(IntPtr widget);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetEffectNode(IntPtr widget,IntPtr node);
    }
    internal class NativeUIButton
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadTextures(IntPtr checkbox,string normal,string selected,string disabled,TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadTextureNormal(IntPtr checkbox, string normal, TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadTexturePressed(IntPtr checkbox, string selected, TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadTextureDisabled(IntPtr checkbox, string disabled, TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetCapInsets(IntPtr button,float x,float y,float w,float h);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetCapInsetsNormalRenderer(IntPtr button,float x,float y,float w,float h);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetCapInsetsPressedRenderer(IntPtr button,float x,float y,float w,float h);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetCapInsetsDisabledRenderer(IntPtr button,float x,float y,float w,float h);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetPressedActionEnabled(IntPtr button,bool enabled);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetTitleText(IntPtr button,string text);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static string GetTitleText(IntPtr button) ;

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetTitleColor(IntPtr button,int r,int g,int b);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void GetTitleColor(IntPtr button,out Color32 color) ;

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetTitleFontSize(IntPtr button,float size);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static float GetTitleFontSize(IntPtr button) ;

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetTitleFontName(IntPtr button, string fontName);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static string GetTitleFontName(IntPtr button) ;

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetScale9Enabled(IntPtr button, bool able);
    }

    internal class NativeUICheckBox
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadTextures(IntPtr checkbox, string backGround, string backGroundSelected, string cross, string backGroundDisabled, string frontCrossDisabled, TextureResType textType);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadTextureBackGround(IntPtr checkbox, string backGround, TextureResType textType);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadTextureBackGroundSelected(IntPtr checkbox, string backGroundSelected, TextureResType textType);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadTextureFrontCross(IntPtr checkbox, string cross, TextureResType textType);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadTextureBackGroundDisabled(IntPtr checkbox, string backGroundDisabled, TextureResType textType);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadTextureFrontCrossDisabled(IntPtr checkbox, string frontCrossDisabled, TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetSelectedState(IntPtr checkbox,bool state);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool GetSelectedState(IntPtr checkbox);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddEventListenerCheckBox(IntPtr checkbox,ICheckBoxEventListener _evet);
    }

    internal class NativeUIImageView
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadTexture(IntPtr imageview,string fileName,TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetTextureRect(IntPtr imageview, float x, float y, float w, float h);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetScale9Enabled(IntPtr imageview, bool able);
    }

    internal class NativeUILabel
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr Create();
    	
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetText(IntPtr label,string text);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static string GetText(IntPtr label);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static int GetStringLength(IntPtr label);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetFontSize(IntPtr label,int size);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetFontSize(IntPtr label);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetFontName(IntPtr label,string name);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetFontName(IntPtr label);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetTouchScaleChangeEnabled(IntPtr label,bool enabled);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static bool IsTouchScaleChangeEnabled(IntPtr label);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetTextAreaSize(IntPtr labell,float w,float h);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetTextHorizontalAlignment(IntPtr label,CCTextAlignment alignment);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetTextVerticalAlignment(IntPtr label,CCVerticalTextAlignment alignment);
    }

    internal class NativeUILabelAtlas
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetProperty(IntPtr label, string stringValue,string charMapFile, int itemWidth, int itemHeight, string  startCharMap); 

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetText(IntPtr label,string stringValue);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static string GetText(IntPtr label);
    }

    internal class NativeUILabelBMFont
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetFontFile(IntPtr label, string fileName);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetText(IntPtr label, string text);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string GetText(IntPtr label);
    }

    internal class NativeUILayout
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetBackGroundImage(IntPtr layout, string fileName, TextureResType textType);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void SetBackGroundImageCapInsets(IntPtr layout,float x,float y ,float w,float h);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void SetBackGroundColorType(IntPtr layout,int type);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void SetBackGroundColorS(IntPtr layout,ref Color32 color);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void SetBackGroundColorSE(IntPtr layout,ref Color32 startColor,ref Color32 endColor);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void SetBackGroundColorOpacity(IntPtr layout,int opacity);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static  void SetBackGroundColorVector(IntPtr layout, Vector2 vector);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void RemoveBackGroundImage(IntPtr layout);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void GetBackGroundImageTextureSize(IntPtr layout,out Size size);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void SetClippingEnabled(IntPtr layout,bool enabled);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  bool IsClippingEnabled(IntPtr layout);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void SetClippingType(IntPtr layout,int type);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  void SetLayoutType(IntPtr layout,int type);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static  int GetLayoutType(IntPtr layout);


    }

    internal class NativeUILayoutParameter
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetMargin(IntPtr LayoutParameter);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int GetLayoutType(IntPtr LayoutParameter);
    }

    internal class NativeUILoadingBar
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetDirection(IntPtr bar, int dir);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static int GetDirection(IntPtr bar);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadTexture(IntPtr bar, string texture, TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetPercent(IntPtr bar,int percent);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static int GetPercent(IntPtr bar);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetCapInsets(IntPtr bar,float x, float y, float width, float height);
    }

    internal class NativeUISlider
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadBarTexture(IntPtr slider, string fileName, TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetCapInsets(IntPtr slider,float x,float y ,float w,float h);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetCapInsetsBarRenderer(IntPtr slider,float x,float y ,float w,float h);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetCapInsetProgressBarRebderer(IntPtr slider,float x,float y ,float w,float h);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadSlidBallTextures(IntPtr slider, string normal, string pressed, string disabled, TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadSlidBallTextureNormal(IntPtr slider, string normal, TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadSlidBallTexturePressed(IntPtr slider, string pressed, TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadSlidBallTextureDisabled(IntPtr slider, string disabled, TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LoadProgressBarTexture(IntPtr slider, string fileName, TextureResType textType);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetPercent(IntPtr slider,int percent);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static int GetPercent(IntPtr slider);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddEventListenerSlider(IntPtr slider, ISliderEventListener target);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetScale9Enabled(IntPtr slider, bool able);
    }

    internal class NativeUITextField
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetTouchSize(IntPtr textfile, float w,float h);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetText(IntPtr textfile,string text);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetPlaceHolder(IntPtr textfile,string value);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetFontSize(IntPtr textfile,int size);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetFontName(IntPtr textfile,string name);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void DidNotSelectSelf(IntPtr textfile);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static string GetStringValue(IntPtr textfile);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]

	    extern internal static void SetMaxLengthEnabled(IntPtr textfile,bool enable);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static bool IsMaxLengthEnabled(IntPtr textfile);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetMaxLength(IntPtr textfile,int length);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static int  GetMaxLength(IntPtr textfile);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetPasswordEnabled(IntPtr textfile,bool enable);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static bool IsPasswordEnabled(IntPtr textfile);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetPasswordStyleText(IntPtr textfile,string styleText);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]

	    extern internal static bool GetAttachWithIME(IntPtr textfile);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetAttachWithIME(IntPtr textfile,bool attach);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static bool GetDetachWithIME(IntPtr textfile);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetDetachWithIME(IntPtr textfile,bool detach);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static bool GetInsertText(IntPtr textfile);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetInsertText(IntPtr textfile,bool insertText);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static bool GetDeleteBackward(IntPtr textfile);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetDeleteBackward(IntPtr textfile,bool deleteBackward);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddEventListenerTextField(IntPtr textfile, ITextFieldEventListener target);
    }

    internal class NativeUIListView
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr Create();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetItemModel(IntPtr listView,IntPtr model);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void PushBackDefaultItem(IntPtr listView);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void InsertDefaultItem(IntPtr listView,int index);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void PushBackCustomItem(IntPtr listView,IntPtr item);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void InsertCustomItem(IntPtr listView,IntPtr item, int index);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void RemoveItem(IntPtr listView,int index);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void RemoveAllItems(IntPtr listView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr GetItem(IntPtr listView,int index);

	    //CCArray* GetItems();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static int GetIndex(IntPtr listView,IntPtr item);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetGravity(IntPtr listView,int gravity);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetItemsMargin(IntPtr listView,float margin);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static int GetCurSelectedIndex(IntPtr listView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void AddEventListenerListView(IntPtr listView,IListEventListener target);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void RequestRefreshView(IntPtr listView);
    }

    internal class NativeUIPageView
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr Create();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void AddWidgetToPage(IntPtr pageView,IntPtr widget, int pageIdx, bool forceCreate);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void AddPage(IntPtr pageView,IntPtr page);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void InsertPage(IntPtr pageView,IntPtr page, int idx);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void RemovePage(IntPtr pageView,IntPtr page);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void RemovePageAtIndex(IntPtr pageView,int index);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void RemoveAllPages(IntPtr pageView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void ScrollToPage(IntPtr pageView,int idx);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static int GetCurPageIndex(IntPtr pageView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr GetPage(IntPtr pageView,int index);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddEventListenerPageView(IntPtr pageView, IPageEventListener target);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetNeedOptimization(IntPtr pageView, bool optimization);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsNeedOptimization(IntPtr pageView);
    }

    internal class NativeUIScrollView
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr Create();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetDirection(IntPtr srollView,int dir);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static int GetDirection(IntPtr srollView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static IntPtr GetInnerContainer(IntPtr srollView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void ScrollToBottom(IntPtr srollView,float time, bool attenuated);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void ScrollToTop(IntPtr srollView,float time, bool attenuated);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void ScrollToLeft(IntPtr srollView,float time, bool attenuated);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void ScrollToRight(IntPtr srollView,float time, bool attenuated);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void ScrollToTopLeft(IntPtr srollView,float time, bool attenuated);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void ScrollToTopRight(IntPtr srollView,float time, bool attenuated);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void ScrollToBottomLeft(IntPtr srollView,float time, bool attenuated);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void ScrollToBottomRight(IntPtr srollView,float time, bool attenuated);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void ScrollToPercentVertical(IntPtr srollView,float percent, float time, bool attenuated);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void ScrollToPercentHorizontal(IntPtr srollView,float percent, float time, bool attenuated);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void ScrollToPercentBothDirection(IntPtr srollView,float percentX,float percentY, float time, bool attenuated);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void JumpToBottom(IntPtr srollView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void JumpToTop(IntPtr srollView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void JumpToLeft(IntPtr srollView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void JumpToRight(IntPtr srollView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void JumpToTopLeft(IntPtr srollView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void JumpToTopRight(IntPtr srollView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void JumpToBottomLeft(IntPtr srollView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void JumpToBottomRight(IntPtr srollView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void JumpToPercentVertical(IntPtr srollView,float percent);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void JumpToPercentHorizontal(IntPtr srollView,float percent);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void JumpToPercentBothDirection(IntPtr srollView,float percentX,float percentY);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetInnerContainerSize(IntPtr srollView, float w,float h);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void GetInnerContainerSize(IntPtr srollView,out Size size);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void AddEventListenerScrollView(IntPtr srollView, IScrollEventListener target);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetBounceEnabled(IntPtr srollView,bool enabled);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static bool IsBounceEnabled(IntPtr srollView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static void SetInertiaScrollEnabled(IntPtr srollView,bool enabled);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
	    extern internal static bool IsInertiaScrollEnabled(IntPtr srollView);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetNeedOptimization(IntPtr srollView, bool optimization);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsNeedOptimization(IntPtr srollView);
    }

    internal class NativeSceneReader
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Purge();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static string SceneReaderVersion();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateNodeWithSceneFile(string pszFileName);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetNodeByTag(int nTag); 

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void DoOptimization(); 
    }
}
