using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine;
using MatrixEngine.Cocos2d;
using MatrixEngine.CocoStudio.GUI;
using MatrixEngine.Platform;
using MatrixEngine.Math;

namespace Game.Test
{
    public class UIWidgetTest : UILayer
    {
        CCSprite label;
        private UIWidget t;
        public UIWidgetTest()
        {
            this.ContextSize = new Size(900, 900);
            this.SetTouchMode(TouchMode.Single);
        }

        public override void OnEnter()
        {
            UIWidget widget = UIReader.GetWidget("cocosgui/DemoShop/DemoShop.json");
            //widget.Postion = TestScene.SCREEN_SIZE_MID;
            UIWidget button_1 = widget.GetWidget("back_Button");
            //button.Alpha = 50;
            t = button_1.Copy();
            t.TouchEnabled = true;
            UIWidget button_3 = t.Copy();
            this.AddChild(button_3);

            //CCSpriteFrameCache.AddSpriteFramesWithFile("ttt.plist");

            UIReader.GetWidget("Icon/Icon.ExportJson");

            UIImageView imageView = new UIImageView();
            imageView.LoadTexture("Data/UI/Icon/HeadIcon/likui.png", TextureResType.UI_TEX_TYPE_PLIST);
            imageView.Postion = new Vector2(200, 200);

            this.AddChild(imageView);

            CCSprite sprite = new CCSprite("Data/UI/Icon/HeadIcon/likui.png", true);
            sprite.Postion = new Vector2(400, 400);
            this.AddChild(sprite);

            t.Postion = new Vector2(400, 300);
        }

        public override bool OnTouchBegan(float x, float y)
        {
            return true;
        }

        public override void OnTouchEnded(float x, float y)
        {
            base.OnTouchEnded(x, y);
        }        public void CallFuc()
        {
            Console.WriteLine("CallFuc");
        }
    }
}
