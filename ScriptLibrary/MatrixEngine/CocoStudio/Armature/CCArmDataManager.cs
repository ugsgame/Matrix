using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.CocoStudio.Native;

namespace MatrixEngine.CocoStudio.Armature
{
    public class CCArmDataManager
    {
        public static void AddArmatureFile(string configFilePath)
        {
            NativeArmDataManager.AddArmatureFileInfo(configFilePath);
        }
        public static void AddArmatureFile(string imagePath, string plistPath, string configFilePath)
        {
            NativeArmDataManager.AddArmatureFileInfoes(imagePath, plistPath, configFilePath);
        }

        public static void AddArmatureFile(string configFilePath, ILoading loading)
        {
            NativeArmDataManager.AddArmatureFileInfoAsync(configFilePath, loading);
        }

        public static void AddArmatureFile(string imagePath, string plistPath, string configFilePath, ILoading loading)
        {
            NativeArmDataManager.AddArmatureFileInfoesAsync(imagePath,plistPath,configFilePath,loading);
        }

        public static void RemoveArmatureFile(string configFilePath)
        {
            NativeArmDataManager.RemoveArmatureFileInfo(configFilePath);
        }
    }
}
