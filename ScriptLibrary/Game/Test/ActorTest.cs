using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine;
using MatrixEngine.Cocos2d;
using MatrixEngine.Math;
using MatrixEngine.CocoStudio.Armature;
using MatrixEngine.CocoStudio.GUI;
using LitJson;
using MatrixEngine.Platform;

namespace Game.Test
{
    class MyActor : ActorBehavior
    {
        protected struct ArmCollider
        {
            ActorBehavior Owner;   //
            string BoneName;                //当前骨骼名字
            private string AnimName;        //当前动画名字
        }

        protected ArmCollider MyArmCollider;
        protected ArmCollider OtherArmCollider;

        bool filpX;
        public CCArmature arm;
        MatrixEngine.CocoStudio.Armature.CCAnimation anim;

        public MyActor()
        {
            //BoxCollider collider = this.BoxCollider;
            arm = new CCArmature("luzhishen");
            this.AddChild(arm);
            //arm.Alpha = 100;
            anim = arm.GetAnimation();
            //anim.Play("luzhishen_loading", true);

            //arm.RunAction(new CCActionFadeOut(3f));
        }

        protected override void OnEnable()
        {

            arm.BindGameActor(this);
            anim.Play("luzhishen_loading", true);
        }

        public void UnBind()
        {
            //this.RemoveRigibody();
            //arm.UnBindGameActor();
            this.RemoveChild(arm);
        }

        public bool FilpX
        {
            set 
            {
                filpX = value;
                if(filpX)
                    arm.ScaleX = -1;
                else
                    arm.ScaleX = 1;
            }
            get
            {
                return filpX;
            }
        }


        private void native_OnArmatureEnter(ActorBehavior ActorA, ActorBehavior ActorB, string BoneA, string BoneB)
        {
           //base.OnTriggerEnter(ColliderA, ColliderB);
           // Debug.Log("BoneA:" + BoneA);
           // Debug.Log("BoneB:" + BoneB);
            //anim.Play("zhujiang_attack", true);
        }

        private void native_OnArmatureExit(ActorBehavior ActorA, ActorBehavior ActorB, string BoneA, string BoneB)
        {

        }

        private void native_OnArmatureStay(ActorBehavior ActorA, ActorBehavior ActorB, string BoneA, string BoneB)
        {

        }

        public override void OnUpdate(float dTime)
        {
//            base.OnUpdate(dTime);
            //Debug.Log("x y" + this.PostionX + "  " + this.PostionY);
        }

    }

    public class ActorTest : CCLayer
    {
        MyActor aActor;
        MyActor bActor;

        CCNode CollistionNode;

        public ActorTest()
        {
            this.SetTouchMode(TouchMode.Single);
            //this.SetSwallowsTouches(false);
        }
        public override void OnEnter()
        {

            CCArmDataManager.AddArmatureFile("armature/luzhishen/luzhishen.ExportJson");

            aActor = new MyActor();
            bActor = new MyActor();

            UILabelBMFont label = new UILabelBMFont();
            label.SetFontFile("fonts/bitmapFontTest2.fnt");
            label.SetText("1256");
            label.Postion = new Vector2(100, 100);

            aActor.IsActive = true;
            bActor.IsActive = true;
            aActor.FilpX =  false;

            this.AddChild(aActor);
            this.AddChild(bActor);

            aActor.Postion = new Vector2(0, 400);
            bActor.Postion = new Vector2(900, 400);

            Debug.CollistionDisplay(false);
    
             CollistionSystem.SetDrawLayer(this);
             CollistionNode = CollistionSystem.GetSystemNode();
 //            this.AddChild(CollistionNode);
             CCArmature.RegisterContactListener();
// 
             this.AddChild(label);
// 
             this.SetSwallowsTouches(false);
  
        }
        public override void OnUpdate(float dTime)
        {
           aActor.PostionX++;
           bActor.PostionX--;

           CollistionSystem.Update(dTime);
        }

        public override bool OnTouchBegan(float x, float y)
        {
            //this.RemoveChild(aActor);
            //aActor = null;

            return true;
        }
    }
}
