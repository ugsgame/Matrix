using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCFileUtils
    {

        public static void AddSearchPath(string path)
        {
            NativeFileUtils.AddSearchPath(path);
        }

        public static void RemoveSearchPath(string path)
        {
            NativeFileUtils.RemoveSearchPath(path);
        }

        public static string FullPathForFilename(string filename)
        {
            return NativeFileUtils.FullPathForFilename(filename);
        }

        public static string GetSDPath()
        {
            return NativeFileUtils.GetSDPath();
        }

        public static bool IsSDExist()
        {
            return NativeFileUtils.IsSDExist();
        }

        public static bool IsFileExist(string filePath)
        {
            return NativeFileUtils.IsFileExist(filePath);
        }
        /// <summary>
        /// 删除指定文件（只能操作可读写的路径）
        /// </summary>
        /// <returns></returns>
        public static bool DeleteFile(string filePath)
        {
            return NativeFileUtils.DeleteFile(filePath);
        }

        public static string GetWritablePath()
        {
            return NativeFileUtils.GetWritablePath();
        }

        public static string GetFileDataToString(string pszFileName)
        {
            int size;
            return NativeFileUtils.GetFileDataToString(pszFileName, "rb", out size);
        }

        public static string GetFileDataToString(string pszFileName, string pszMode)
        {
            int size;
            return NativeFileUtils.GetFileDataToString(pszFileName, pszMode, out size);
        }

        public static string GetFileDataToString(string pszFileName, string pszMode, int pSize)
        {
            //int size;
            return NativeFileUtils.GetFileDataToString(pszFileName, pszMode, out pSize);
        }

        //Low IO
        //TODO:测试没问题把它封装起来
        /// <summary>
        /// 打开一个文件流
        /// </summary>
        /// <param name="filePath"></param>
        /// <param name="pszMode"></param>
        /// "r" 以只读方式打开文件 
        /// "w" 以只写方式打开文件 
        /// "a" 以追加方式打开文件 
        /// "r+" 以读/写方式打开文件，如无文件出错 
        /// "w+" 以读/写方式打开文件，如无文件生成新文件
        /// <returns>c/c++文件错针</returns>
        public static IntPtr FileOpen(string filePath, string pszMode)
        {
            return NativeFileUtils.FileOpen(filePath, pszMode);
        }
        /// <summary>
        /// 关闭文件流
        /// </summary>
        /// <param name="fp"></param>
        /// <returns></returns>
        public static bool FileClose(IntPtr fp)
        {
            return NativeFileUtils.FileClose(fp);
        }
        /// <summary>
        /// 创建buffer
        /// </summary>
        /// <param name="mSize">指定buffer大小</param>
        /// <returns></returns>
        public static IntPtr CreateBuffer(int mSize)
        {
            return NativeFileUtils.CreateBuffer(mSize);
        }
        public static void DeleteBuffer(IntPtr buffer)
        {
            NativeFileUtils.DeleteBuffer(buffer);
            buffer = IntPtr.Zero;
        }
        /// <summary>
        /// 向文件流写入buffer
        /// </summary>
        /// <param name="file">文件指针</param>
        /// <param name="size"></param>
        /// <param name="buffer"></param>
        public static void WriteBuffer(IntPtr file, int size, IntPtr buffer)
        {
            NativeFileUtils.WriteBuffer(file, size, buffer);
        }
        /// <summary>
        /// 从文件流读取buffer
        /// </summary>
        /// <param name="file">文件指针</param>
        /// <param name="pSize">返回buffer大小</param>
        /// <returns></returns>
        public static IntPtr ReadBuffer(IntPtr file, out int pSize)
        {
            return NativeFileUtils.ReadBuffer(file, out pSize);
        }

        /// <summary>
        /// 向buffer写入byte
        /// </summary>
        /// <param name="buffer">buffer指针</param>
        /// <param name="size">buffer大小</param>
        /// <param name="seek">buffer内存游标</param>
        /// <param name="_byte"></param>
        public static void WriteBufferByte(IntPtr buffer, int size, int seek, byte _byte)
        {
            NativeFileUtils.WriteBufferByte(buffer, size, seek, _byte);
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="buffer"></param>
        /// <param name="size"></param>
        /// <param name="seek"></param>
        /// <returns></returns>
        public static byte ReadBufferByte(IntPtr buffer, int size, int seek)
        {
            return NativeFileUtils.ReadBufferByte(buffer, size, seek);
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="buffer"></param>
        /// <param name="size"></param>
        /// <param name="seek"></param>
        /// <param name="_short"></param>
        public static void WriteBufferShort(IntPtr buffer, int size, int seek, short _short)
        {
            NativeFileUtils.WriteBufferShort(buffer, size, seek, _short);
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="buffer"></param>
        /// <param name="size"></param>
        /// <param name="seek"></param>
        /// <returns></returns>
        public static short ReadBufferShort(IntPtr buffer, int size, int seek)
        {
            return NativeFileUtils.ReadBufferShort(buffer, size, seek);
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="buffer"></param>
        /// <param name="size"></param>
        /// <param name="seek"></param>
        /// <param name="_int"></param>
        public static void WriteBufferInt(IntPtr buffer, int size, int seek, int _int)
        {
            NativeFileUtils.WriteBufferInt(buffer, size, seek, _int);
        }
        public static int ReadBufferInt(IntPtr buffer, int size, int seek)
        {
            return NativeFileUtils.ReadBufferInt(buffer, size, seek);
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="buffer"></param>
        /// <param name="size"></param>
        /// <param name="seek"></param>
        /// <param name="_float"></param>
        public static void WriteBufferFloat(IntPtr buffer, int size, int seek, float _float)
        {
            NativeFileUtils.WriteBufferFloat(buffer, size, seek, _float);
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="buffer"></param>
        /// <param name="size"></param>
        /// <param name="seek"></param>
        /// <returns></returns>
        public static float ReadBufferFloat(IntPtr buffer, int size, int seek)
        {
            return NativeFileUtils.ReadBufferFloat(buffer, size, seek);
        }
        //
    }
}
