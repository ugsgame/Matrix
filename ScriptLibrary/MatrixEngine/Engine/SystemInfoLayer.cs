
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;
using MatrixEngine.Math;
using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Engine
{
    public class SystemInfoLayer : CCLayer
    {
        public static readonly SystemInfoLayer Instance = new SystemInfoLayer();

        private CCLabelTTF NativeObjectCount;
        private CCLabelTTF ObjectCount;
        private CCLabelTTF ObjectVerify;
        private CCLabelTTF ReleasePool;

        private CCLabelAtlas numNativeObjectCount;
        private CCLabelAtlas numObjectCount;
        private CCLabelAtlas numObjectVerify;
        private CCLabelAtlas numReleasePool;

        public SystemInfoLayer()
        {
            NativeObjectCount = new CCLabelTTF("NativeObjectCount:", 20);
            ObjectCount = new CCLabelTTF("ObjectCount:", 20);
            ObjectVerify = new CCLabelTTF("ObjectVerify:", 20);
            ReleasePool = new CCLabelTTF("ReleasePool:", 20);

//             numNativeObjectCount = new CCLabelAtlas("0", "Default/system_labelatlasimg.png",24,32,'0');
//             numObjectCount = new CCLabelAtlas("0", "Default/system_labelatlasimg.png", 24, 32, '0');
//             numObjectVerify = new CCLabelAtlas("0", "Default/system_labelatlasimg.png", 24, 32, '0');
//             numReleasePool = new CCLabelAtlas("0", "Default/system_labelatlasimg.png", 24, 32, '0');

            this.AddChild(NativeObjectCount);
            this.AddChild(ObjectCount);
            this.AddChild(ObjectVerify);
            this.AddChild(ReleasePool);

//             this.AddChild(numNativeObjectCount);
//             this.AddChild(numObjectCount);
//             this.AddChild(numObjectVerify);
//             this.AddChild(numReleasePool);

            NativeObjectCount.AnchorPoint = new Vector2(0, 0.5f);
            ObjectCount.AnchorPoint = new Vector2(0, 0.5f);
            ObjectVerify.AnchorPoint = new Vector2(0, 0.5f);
            ReleasePool.AnchorPoint = new Vector2(0, 0.5f);

            ObjectVerify.Postion = new Vector2(10, 120);
            NativeObjectCount.Postion = new Vector2(10, 140);
            ObjectCount.Postion = new Vector2(10, 160);
            ReleasePool.Postion = new Vector2(10, 180);

//             numNativeObjectCount.AnchorPoint = new Vector2(0, 0.5f);
//             numObjectCount.AnchorPoint = new Vector2(0, 0.5f);
//             numObjectVerify.AnchorPoint = new Vector2(0, 0.5f);
//             numReleasePool.AnchorPoint = new Vector2(0, 0.5f);
// 
//             numNativeObjectCount.Postion = new Vector2(120, 120);
//             numObjectCount.Postion = new Vector2(120, 140);
//             numObjectVerify.Postion = new Vector2(120, 160);
//             numReleasePool.Postion = new Vector2(120, 180);

        }

        public override void OnEnter()
        {
            
            base.OnEnter();
        }

        public override void OnUpdate(float dTime)
        {
            NativeObjectCount.String = "NativeObjectCount:" + Convert.ToString(NativeDirector.GetObjectCount());
            ObjectCount.String = "ObjectCount:" + Convert.ToString(CCObject.ObjectCount);
            ObjectVerify.String = "ObjectVerify:" + Convert.ToString(NativeDirector.GetObjectCount() - CCObject.ObjectCount);
            ReleasePool.String = "ReleasePool:" + Convert.ToString(NativeDirector.GetReleasePoolSize());

//             numObjectCount.String = NativeDirector.GetObjectCount().ToString();
//             numObjectCount.String = CCObject.ObjectCount.ToString();
//             numObjectVerify.String = (NativeDirector.GetObjectCount() - CCObject.ObjectCount).ToString();
//             numReleasePool.String = NativeDirector.GetReleasePoolSize().ToString();
        }
    }
}
