using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;
using MatrixEngine.Platform;
using MatrixEngine.Engine;

namespace Game.Test
{
    class FileUtilsTest : CCLayer
    {
        public FileUtilsTest()
        {

        }

        public override void OnEnter()
        {


            Random rand = new Random();

            Archive saveData = new Archive("ArchiveTest.data");
            for (int i = 0; i < 1000; i++)
            {
                int val = rand.Next(1,100);
                saveData.Write(val);
                Console.WriteLine("write val:" + val);
            }
            saveData.Save();


            Console.WriteLine("");
            Console.WriteLine("read data");
            Archive readData = new Archive("ArchiveTest.data");
            Console.WriteLine("data size:" + readData.Size);
            for (int i = 0; i < 1000; i++)
            {
                int val;
                if(readData.Read(out val))
                {
                    Console.WriteLine("read val:" + val);
                }
                else
                {
                    Console.WriteLine("read error:" + readData.Seek);
                }
            }


        }


    }
}
