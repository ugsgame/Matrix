
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;

using MatrixEngine.Platform.Android;
using MatrixEngine.Native;

namespace Game.Test
{
    public class AndroidJNITest : CCLayer
    {
        public override void OnEnter()
        {
            base.OnEnter();

            this.SetTouchMode(TouchMode.Single);
        }

        public override bool OnTouchBegan(float x, float y)
        {
            //return base.OnTouchBegan(x, y);
            this.OpenUrl("http://www.baidu.com");

            return false;
        }

        public void OpenUrl(string url)
        {
            if (MatrixEngine.Engine.System.TARGET_PLATFORM == MatrixEngine.Engine.System.PLATFORM.ANDROID)
            {

               AndroidJavaObject netHelper = new AndroidJavaObject("org/fonle/matrix/jni/NetHelper");
               netHelper.CallStatic("OpenUrl", "http://www.baidu.com");
               netHelper.CallStatic("JniTest", 50, 300, 100, false, 0.5f);
            }
            else
            {
                global::System.Diagnostics.Process.Start(url);
            }
        }
    }
}
