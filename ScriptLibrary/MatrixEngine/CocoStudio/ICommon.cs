using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MatrixEngine.CocoStudio.GUI;
using MatrixEngine.CocoStudio.Armature;

namespace MatrixEngine.CocoStudio
{

    /// 接口类

    ///动数据加载接口
    public interface ILoading
    {
        /// <summary>
        /// 加载中
        /// </summary>
        void Loading(float percent);
    }

    public interface IAnimationEvent
    {
        //TODO animation 要改为Animation对像而不是指针
        //  void AnimationEvent(IntPtr pAnimation, int movementType, string movementID);
        void AnimationEvent(MovementEventType movementType, string movementID);

        //  void FrameEvent(IntPtr pBone, string evt, int originFrameIndex, int currentFrameIndex);
        void FrameEvent(string evt, int originFrameIndex, int currentFrameIndex);
    }

    public interface ITouchEventListener
    {
        //TODO
        void TouchListener(UIWidget widget, TouchEventType eventType);
    }


    public interface ICheckBoxEventListener
    {
        //TODO
        void SelectedStateEvent(CheckBoxEventType eventType);
    }

    public interface ISliderEventListener
    {
        //
        void SliderListener(int eventType);
    }

    public interface ITextFieldEventListener
    {
        //
        void TextFieldListener(TextFiledEventType eventType);
    }

    public interface IListEventListener
    {
        void ListListener(int eventType);
    }

    public interface IPageEventListener
    {
        void PageListener(int eventType);
    }

    public interface IScrollEventListener
    {
        void ScrollListener(SCROLLVIEW_DIR eventType);
    }

    public interface IAddChildEventListener
    {
        void AddChildListener(UIWidget node);
    }
}
