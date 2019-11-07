
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;
using MatrixEngine.Cocos2d.Native;
using MatrixEngine.CocoStudio.GUI;

namespace MatrixEngine.Engine
{
    //扩展Action MCAction

    public class MCAction : CCActionInterval
    {
        
        
        public MCAction(float fDuration)
            : base( NativeAction.MCAction(fDuration))
        {
        }

        ~MCAction()
        {
            UnBindMonoObject();
        }

        private void native_StartWithTarget(CCNode pNode)
        {
            try
            {
                Node = pNode;
                this.StartWithTarget(Node);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        private void native_OnStop()
        {
//            Console.WriteLine("===========native_OnStop=============");
        }

        private int native_Reverse()
        {
            //返回c++指针
            try
            {
                return Reverse().CppObjPtr.ToInt32();
            }
            catch (Exception e)
            {  
                Console.WriteLine(e);
                return 0;
            }
        }

        private void native_OnUpdate(float percent)
        {
            //
//            Console.WriteLine("MCAction native_OnUpdate:" + percent);
            try
            {
                this.OnUpdate(percent);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        protected virtual void StartWithTarget(CCNode node)
        {
            //
        }

        public new virtual CCAction Reverse()
        {
            throw new NotImplementedException();
           // return null;
        }

        protected virtual void OnUpdate(float percent)
        {
            //
        }
    }

    public class MCActionInterval : CCActionInterval
    {


        public MCActionInterval(float fDuration)
            : base(NativeAction.MCActionInterval(fDuration))
        {
        }

        ~MCActionInterval()
        {
            UnBindMonoObject();
        }

        private void native_StartWithTarget(CCNode pNode)
        {
            try
            {
                Node = pNode;
                this.StartWithTarget(Node);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        private void native_OnStop()
        {
            //            Console.WriteLine("===========native_OnStop=============");
            try
            {
                this.OnStop();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        private int native_Reverse()
        {
            //返回c++指针
            try
            {
                return Reverse().CppObjPtr.ToInt32();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return 0;
            }
        }

        private void native_OnUpdate(float percent)
        {
            //
            //            Console.WriteLine("MCAction native_OnUpdate:" + percent);
            try
            {
                this.OnUpdate(percent);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        protected virtual void StartWithTarget(CCNode node)
        {
            //
        }

        protected virtual void OnStop()
        {

        }

        public new virtual CCAction Reverse()
        {
            throw new NotImplementedException();
            // return null;
        }

        protected virtual void OnUpdate(float percent)
        {
            //
        }
    }

    public class MCActionInstant : CCAtionInstant
    {


        public MCActionInstant()
            : base(NativeAction.MCActionInstant())
        {
        }

        ~MCActionInstant()
        {
            UnBindMonoObject();
        }

        private int native_Reverse()
        {
            //返回c++指针
            try
            {
                return Reverse().CppObjPtr.ToInt32();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return 0;
            }
        }

        private void native_OnUpdate(CCNode pNode)
        {
            try
            {
                this.OnUpdate(pNode);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        public new virtual CCAction Reverse()
        {
            throw new NotImplementedException();
            // return null;
        }

        protected virtual void OnUpdate(CCNode pNode)
        {
            //
        }
    }

    //在下面扩展自己的Action
    //

    public class MCActionFadeIn : MCActionInterval
    {
        public MCActionFadeIn(float time)
            : base(time)
        {

        }

        public override CCAction Reverse()
        {
            //return base.Reverse();
            CCAction action = new MCActionFadeOut(GetTime());
            return action;
        }

        protected override void OnUpdate(float percent)
        {
            //base.OnUpdate(time);
            if (Node is UIWidget)
            {
                UIWidget widget = (UIWidget)Node;
                widget.Alpha = (int)(255 * percent);
            }
            else if (Node is CCNodeRGBA)
            {
                CCNodeRGBA nodeR = (CCNodeRGBA)Node;
                nodeR.Alpha = (int)(255 * percent);
            }
        }
    }
    //
    public class MCActionFadeOut : MCActionInterval
    {
        public MCActionFadeOut(float time)
            : base(time)
        {

        }

        public override CCAction Reverse()
        {
            //return base.Reverse();
            CCAction action = new MCActionFadeIn(GetTime());
            return action;
        }

        protected override void OnUpdate(float percent)
        {
            //base.OnUpdate(time);
            if (Node is UIWidget)
            {
                UIWidget widget = (UIWidget)Node;
                widget.Alpha = (int)(255* (1-percent));
            }
            else if (Node is CCNodeRGBA)
            {
                CCNodeRGBA nodeR = (CCNodeRGBA)Node;
                nodeR.Alpha = (int)(255 * (1-percent));
            }
        }
    }
    //
    public class MCActionFadeTo : MCActionInterval
    {
        private int m_fromOpacity;
        private int m_toOpacity;

        public MCActionFadeTo(float time, int opacity)
            : base(time)
        {
            m_toOpacity = opacity;
        }

        protected override void StartWithTarget(CCNode node)
        {
            //base.StartWithTarget(node);
            if (Node is UIWidget)
            {
                UIWidget widget = (UIWidget)Node;
                m_fromOpacity = widget.Alpha;
            }
            else if (Node is CCNodeRGBA)
            {
                CCNodeRGBA nodeR = (CCNodeRGBA)Node;
                m_fromOpacity = nodeR.Alpha;
            }
        }

        protected override void OnUpdate(float percent)
        {
            //base.OnUpdate(time);  
            if (Node is UIWidget)
            {
                UIWidget widget = (UIWidget)Node;
                widget.Alpha = m_fromOpacity + (int)((m_toOpacity - m_fromOpacity) * percent);
            }
            else if (Node is CCNodeRGBA)
            {
                CCNodeRGBA nodeR = (CCNodeRGBA)Node;
                nodeR.Alpha = m_fromOpacity + (int)((m_toOpacity - m_fromOpacity) * percent);
            }
        }
    }
}
