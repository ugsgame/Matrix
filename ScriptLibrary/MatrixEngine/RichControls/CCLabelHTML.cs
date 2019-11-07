using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;
using MatrixEngine.RichControls.Native;
using MatrixEngine.Math;

namespace MatrixEngine.RichControls
{

    public class CCLabelHTML : CCNode
    {
        public interface ITouhchListener
        {
            void OnClick(int id);
            void OnMove(int id, Vector2 location, Vector2 delta);
        }

        private ITouhchListener listener;

        internal CCLabelHTML(IntPtr t) : base(t) { }
        public CCLabelHTML() :
            base(NativeLabelHTML.Create())
        {

        }

        public CCLabelHTML(string str, Size preferred)
            : base(NativeLabelHTML.CreateWithString(str, ref preferred, "default"))
        {

        }

        public CCLabelHTML(string str, Size preferred, string font_alias)
            : base(NativeLabelHTML.CreateWithString(str, ref preferred, font_alias))
        {

        }

        public virtual string String
        {
            get { return NativeLabelHTML.GetString(this.CppObjPtr); }
            set { NativeLabelHTML.SetString(this.CppObjPtr, value); }
        }

        public virtual void AppendString(string str)
        {
            NativeLabelHTML.AppendString(this.CppObjPtr,str);
        }

        public void RegisterListener(ITouhchListener listener)
        {
            NativeLabelHTML.RegisterListener(this.CppObjPtr,this);
            this.listener = listener;
        }

        private void Native_OnClick(int id)
        {
            try
            {
                if (listener != null)
                {
                    listener.OnClick(id);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
                //throw;
            }
        }

        private void Native_OnMove(int id, float locationX, float locationY, float deltaX, float deltaY)
        {
            try
            {
                if (listener != null)
                {
                    listener.OnMove(id, new Vector2(locationX,locationY), new Vector2(deltaX,deltaY));
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
                //throw;
            }
        }
    }
}
