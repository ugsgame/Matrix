
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

using MatrixEngine;
using MatrixEngine.Cocos2d;
using MatrixEngine.CocoStudio;
using MatrixEngine.CocoStudio.Armature;


namespace Game.Test
{
    public class ThreadTest:CCLayer,ILoading
    {
        CCSprite sprite;

        public ThreadTest()
        {

        }

        public override void OnEnter()
        {
            base.OnEnter();

            Thread loadThread = new Thread(this.Run);
            loadThread.Start();

        }

        public void Run()
        {
            //while (true)
	        {

                CCArmDataManager.AddArmatureFile("Anim/Character/nanzhanshi/nanzhanshi.ExportJson",this);
                CCArmDataManager.AddArmatureFile("armature/DemoPlayer/DemoPlayer.ExportJson",this);

                Console.WriteLine("hiihiii");

                this.Schedule("CallOnce", 3.0f);
	        }
        }

        public override void OnUpdate(float dTime)
        {
            base.OnUpdate(dTime);
        }

        public void CallOnce(float t)
        {
            CCArmature arm = new CCArmature("nanzhanshi");
            this.AddChild(arm);
            this.Postion = TestScene.SCREEN_SIZE_MID;
            MatrixEngine.CocoStudio.Armature.CCAnimation anim = arm.GetAnimation();
            anim.Play("nanzhanshi_walk", true);

            this.Unschedule("CallOnce");
        }

        public void Loading(float percent)
        {
            Console.WriteLine("percent:" + percent);
        }
    }
}
