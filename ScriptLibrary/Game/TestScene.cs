using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Math;
using MatrixEngine.Cocos2d;
using MatrixEngine;

using Game.Test;
using Game.Plugins;

namespace Game
{
    public class TestScene:CCScene
    {
        public static float SCREEN_WIDTH;
        public static float SCREEN_HEIGHT;
        public static float SCREEN_WIDTH_MID;
        public static float SCREEN_HEIGHT_MID;

        public static Size SCREEN_SIZE;
        public static Size SCREEN_SIZE_MID;

        public TestScene()
        {
            this.SetTouchMode(TouchMode.Single);
        }

        public override void OnEnter()
        {
            SCREEN_SIZE = CCDirector.GetFrameSize();
            SCREEN_WIDTH = SCREEN_SIZE.width;
            SCREEN_HEIGHT = SCREEN_SIZE.height;

            SCREEN_WIDTH_MID = SCREEN_WIDTH / 2;
            SCREEN_HEIGHT_MID = SCREEN_HEIGHT / 2;
            SCREEN_SIZE_MID.width = SCREEN_WIDTH_MID;
            SCREEN_SIZE_MID.height = SCREEN_HEIGHT_MID;

//              ActorTest actorTest = new ActorTest();
//              MoogerTest moogerTest = new MoogerTest();
//              FileUtilsTest fileUtilTest = new FileUtilsTest();
//              ParticleSystemTest particleSysteTest = new ParticleSystemTest();
//              UIWidgetTest widgetTest = new UIWidgetTest();
//             ScheduleTest scheduleTest = new ScheduleTest();
//             ThreadTest threadTest = new ThreadTest();
//             LuaTest luaTest = new LuaTest();
//            LabelHTMLTest labelHTMLTest = new LabelHTMLTest();
//             AndroidJNITest androidJNITest = new AndroidJNITest();
            GCTest gcTest = new GCTest();

            // this.AddChild(moogerTest);
            //this.AddChild(actorTest);
            //this.AddChild(fileUtilTest);
            //this.AddChild(particleSysteTest);
            // this.AddChild(widgetTest);
            //this.AddChild(threadTest);
            //this.AddChild(luaTest);
            //this.AddChild(labelHTMLTest);
            //this.AddChild(androidJNITest);
            this.AddChild(gcTest);

        }

        public override void OnEnterTransitionFinish()
        {

        }

        public override void OnExitTransitionStart()
        {

        }

        public override bool OnTouchBegan(float x, float y)
        {
            return true;
        }

        public override void OnUpdate(float dTime)
        {
            base.OnUpdate(dTime);
        }

        public override void OnTouchesBegan(CCSet touches)
        {
            base.OnTouchesBegan(touches);
        }

    }
}
