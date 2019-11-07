using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine;
using MatrixEngine.Cocos2d;

namespace Game.Test
{
    class ScheduleNode : CCNode
    {
        public ScheduleNode()
        {

        }

        public void ScheduleAll()
        {
            this.Schedule("OnTick_1", 0.1f);
            this.Schedule("OnTick_2", 0.5f);
            this.Schedule("OnTick_3", 1.0f);
        }

        public void UnscheduleAll()
        {
            this.Unschedule("OnTick_1");
            this.Unschedule("OnTick_2");
            this.Unschedule("OnTick_3");
        }

        public void OnTick_1(float dt)
        {
            Console.WriteLine("OnTick_1");
        }

        public void OnTick_2(float dt)
        {
            Console.WriteLine("OnTick_2");
        }

        public void OnTick_3(float dt)
        {
            Console.WriteLine("OnTick_3");
        }
    }

    public class ScheduleTest : CCLayer
    {
        ScheduleNode scheduleNode;
        int count = 0;

        public ScheduleTest()
        {
            scheduleNode = new ScheduleNode();
            this.AddChild(scheduleNode);
        }

        public override void OnEnter()
        {
            base.OnEnter();

            scheduleNode.ScheduleAll();
        }

        public override void OnUpdate(float dTime)
        {
            base.OnUpdate(dTime);

            count++;

            if (count == 100)
            {
                Console.WriteLine("stop~~~~~~~");
                scheduleNode.UnscheduleAll();
            }
            if (count == 200)
            {
                Console.WriteLine("run~~~~~~~");
                scheduleNode.ScheduleAll();
            }
        }
    }
}
