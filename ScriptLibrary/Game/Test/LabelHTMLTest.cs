using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine;
using MatrixEngine.Cocos2d;
using MatrixEngine.RichControls;
using MatrixEngine.Math;

namespace Game.Test
{
    class LabelHTMLTest:CCLayer,CCLabelHTML.ITouhchListener
    {
        CCLabelHTML s_htmlLabel;

        public override void OnEnter()
        {
            base.OnEnter();

           FontCatalog font_catalog = null;
	        font_catalog = FontFactory.CreateFont(
                "font1", "Fonts/fangzhengyihei.TTF", 0xffffffff, 36);
	        // font2
 	        font_catalog = FontFactory.CreateFont(
 		        "font2", "simkai.ttf", 0xffffffff, 24, FontFactory.EFontStyle.e_shadow, 1.0f,0xff000000, 0,96);
 	        font_catalog.Add_HackFont("htmltest/Marker Felt.ttf", -1);
// 	        // font3
            font_catalog = FontFactory.CreateFont(
		        "font3", "simli.ttf", 0xffffffff, 20, FontFactory.EFontStyle.e_border, 1.0f, 0xff000000, 0,96);
            font_catalog.Add_HackFont("simhei.ttf", 5);

            String doc = CCFileUtils.GetFileDataToString("htmltest/html.htm");
            s_htmlLabel = new CCLabelHTML(doc, new Size(TestScene.SCREEN_WIDTH * 0.8f, TestScene.SCREEN_HEIGHT));
            s_htmlLabel.AnchorPoint = new Vector2(0.5f, 0.5f);
            s_htmlLabel.Postion = TestScene.SCREEN_SIZE_MID;
            s_htmlLabel.RegisterListener(this);
            this.AddChild(s_htmlLabel);

            CCLabelTTF lableTTF = new CCLabelTTF("你好中国dfdd，字体！！", "fangzhengyihei.ttf", 36);
            lableTTF.String = "afdasf";
            string a = lableTTF.String;
            lableTTF.Color = 0xffffffff;
            lableTTF.Postion = new Vector2(300, 200);
            lableTTF.Scale = 1.0f;
            this.AddChild(lableTTF);

            CCLabelFT labelFT = new CCLabelFT("你好中国，字体！！", "font1");
            labelFT.Color = 0xffffff00;
            labelFT.Postion = new Vector2(300, 100);
            labelFT.AnchorPoint = lableTTF.AnchorPoint;
            labelFT.Scale = 1.0f;
            this.AddChild(labelFT);
        }

        public void OnClick(int id)
        {
            Console.WriteLine("click:" + id);
        }

        public void OnMove(int id, Vector2 location, Vector2 delta)
        {
            if (id == 1001)
            {
                s_htmlLabel.Postion += delta;
            }
        }
    }
}
