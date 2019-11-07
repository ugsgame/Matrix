using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;
using MatrixEngine.Mooger;
using MatrixEngine;
using LitJson;
using MatrixEngine.Math;

namespace Game.Test
{
    class MoogerTest:CCLayer
    {
        MoogerMap map;
        public MoogerTest()
        {
            this.SetTouchMode(TouchMode.Single);
        }

        public override void OnEnter()
        {
            try
            {
                map = new MoogerMap();
                map.LoadWithFile("mooger/map/TestMap_map_1.dat", "mooger/map/map");
                this.AddChild(map);
                Console.WriteLine("map retain:" + map.RetainCount());

                string str = CCFileUtils.GetFileDataToString("E:/Dev/ShuiHuHero/dev/Tech/Client/Resources/Data/Map/TestMap.json");
                JsonData json = JsonMapper.ToObject(str);
                JsonData mapdata = (JsonData)json["map_data"];
                JsonData map_1 = (JsonData)mapdata[0];
                Console.WriteLine("Name:" + map_1["mapName"]);
                Console.WriteLine("count:" + mapdata.Count);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                Console.WriteLine(e.StackTrace);
            }

        }

        public override void OnUpdate(float dTime)
        {

        }

        public override bool OnTouchBegan(float x, float y)
        {
            //return base.OnTouchBegan(x, y);

            return true;
        }
    }
}
