using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine;
using MatrixEngine.Math;
using MatrixEngine.Cocos2d;

namespace Game.Test
{
    public class ParticleSystemTest : CCLayer
    {
        CCParticleSystem particle;

        bool enable = true;
        CCGrayNode grayNode;

        public ParticleSystemTest()
        {
            this.SetTouchMode(TouchMode.Single);
        }

        public override void OnEnter()
        {
            grayNode = new CCGrayNode();
            grayNode.ResetShader();
            this.AddChild(grayNode);

            CCSprite backgroup = new CCSprite("test.png");
            backgroup.Postion = TestScene.SCREEN_SIZE_MID;
            grayNode.AddChild(backgroup);
            grayNode.ResetShader();

            CCSprite sss = new CCSprite("nanzhanshi8.png");
            sss.Postion = TestScene.SCREEN_SIZE_MID;
            this.AddChild(sss);

            CCProgressTimer progressTimer = new CCProgressTimer(backgroup);
            progressTimer.Type = CCProgressTimerType.kCCProgressTimerTypeRadial;
            //this.AddChild(progressTimer);
            progressTimer.Postion = TestScene.SCREEN_SIZE_MID;

            CCAction progressTo = new CCActionProgressTo(2, 100);
            progressTimer.RunAction(progressTo);

             particle = new CCParticleSystem("effect/lines.plist");
            particle.Postion = TestScene.SCREEN_SIZE_MID;
            particle.IsAutoRemoveOnFinish = true;
            particle.Stop();
            particle.Play();

            CCAction moveRight = new CCActionMoveTo(2.0f,new Size(TestScene.SCREEN_WIDTH,TestScene.SCREEN_HEIGHT_MID));
            CCAction moveLeft = new CCActionMoveTo(2.0f,new Size(0,TestScene.SCREEN_HEIGHT_MID));
            CCAction moveLoop = new CCActionRepeatForever(new CCActionSequence(moveRight, moveLeft));
            particle.RunAction(moveLoop);

            particle.PositionType = tCCPositionType.kCCPositionTypeFree;

            try
            {
                CCLabelTTF label = new CCLabelTTF("字体~~的劳动法sdfs k在时", "Simsun", 32);
                 label.Postion = TestScene.SCREEN_SIZE_MID;
                 label.EnableShadow(new Size(100, 100), 100f, 0.1f);
                 label.EnableStroke(Color32.Green, 10);
                 this.AddChild(label);

                 CCLabelTTF fontName = new CCLabelTTF(label.FontName, 30);
                 fontName.Postion = TestScene.SCREEN_SIZE_MID + 100;
                 this.AddChild(fontName);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
                //throw;
            }
            this.AddChild(particle);
        }

        public override void OnUpdate(float dTime)
        {

        }

        public override bool OnTouchBegan(float x, float y)
        {
            enable = !enable;
            grayNode.Enable = enable;
            return false;
        }

        public override void OnTouchMoved(float x, float y)
        {
            particle.Postion = new Size(x, y);
        }

        public override void OnTouchCancelled(float x, float y)
        {
            particle.Postion = new Size(x, y);

        }

        public override void OnTouchesBegan(CCSet touches)
        {
            enable = !enable;
            grayNode.Enable = enable;

            base.OnTouchesBegan(touches);
        }

    }
}
