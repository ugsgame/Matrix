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
using MatrixEngine.Engine;

namespace Game.Test
{
    public class A
    {
        public A()
        {

            Console.WriteLine("A---------------------");
        }
        ~A()
        {
            Console.WriteLine("~A---------------------");
        }
    }

    public class GCTest : CCLayer
    {
        bool isCopy;
        UIWidget widget;
        ActorBehavior actor;

        UIWidget[] widgetClones = new UIWidget[50];
        ActorBehavior[] actors = new ActorBehavior[100];

        UILayout uiLayout = new UILayout();

        public GCTest()
        {
            isCopy = true;

            widget = UIReader.GetWidget("PanelDisplay/Store.ExportJson");

            //this.AddChild(uiLayout);

            this.SetTouchMode(TouchMode.Single);

        }

        public override void OnEnter()
        {
            base.OnEnter();
        }

        public override void OnExit()
        {
            base.OnExit();
        }

        public override bool OnTouchBegan(float x, float y)
        {
            Console.WriteLine("isCopy:" + isCopy);
            if (isCopy)
            {
                
                for (int i = 0; i < widgetClones.Length; i++)
                {
                    widgetClones[i] = (UIWidget)widget.Copy();
                    //uiLayout.AddChild(widgetClones[i]);
                }
                
//                 for (int i = 0; i < actors.Length; i++)
//                 {
//                     actors[i] = new ActorBehavior();
//                     //this.AddChild(actors[i]);
//                 }
            }
            else
            {
                
                for (int i = 0; i < widgetClones.Length; i++)
                {
                    //uiLayout.RemoveChild(widgetClones[i]);
                    widgetClones[i].Dispose();
                    //widgetClones[i] = null;
                }
                
//                 for (int i = 0; i < actors.Length; i++)
//                 {
//                     //this.RemoveChild(actors[i]);
//                     actors[i].Dispose();
//                     //actors[i] = null;
//                 }

                //uiLayout.RemoveAllChildren(true);
            }
            System.GC.Collect();
            isCopy = !isCopy;

            //CCAction action = new CCActionSequence(new CCActionScaleTo(1.0f, 0.5f), new CCActionMoveBy(1.0f, new Vector2(0, 0)),new MCActionFadeIn(1.0f));
            //uiLayout.RunAction(action);

            return true;
        }
    }
}
