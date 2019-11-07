using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d;
using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Engine
{
    /// <summary>
    /// Archive文件现在只能保存到根目录
    /// </summary>
    public class Archive
    {
        /// <summary>
        ///  
        /// </summary>
        private List<byte> buffer = new List<byte>(1024);

        /// <summary>
        /// 文件指针游标
        /// </summary>
        private int _seek;

        /// <summary>
        /// 文件名
        /// </summary>
        private string fullFilePath;

        public Archive(string filePath)
            : this(filePath, false)
        {
        }

        /// <summary>
        /// 打开或创建一个存档
        /// </summary>
        /// <param name="filePath">文件全路径名</param>
        /// 
        public Archive(string filePath, bool writablePath)
        {
            if (writablePath)
                fullFilePath = CCFileUtils.GetWritablePath() + filePath;
            else
                fullFilePath = filePath;

            if (CCFileUtils.IsFileExist(fullFilePath))
            {
                int fileSize = 0;
                //获取buffer
                IntPtr cbuffer = NativeFileUtils.GetFileDataToIntptr(fullFilePath, "r", out fileSize);
                Console.WriteLine("fileSize:" + fileSize);
                if (cbuffer != IntPtr.Zero)
                {
                    int cSeek = 0;
                    for (int i = 0; i < fileSize; i++)
                    {
                        buffer.Add(NativeFileUtils.ReadBufferByte(cbuffer, fileSize, cSeek));
                        cSeek += 1;
                    }
                    NativeFileUtils.DeleteBuffer(cbuffer);
                }
            }
            _seek = 0;
        }

        public virtual void Clean()
        {
            buffer.Clear();
            _seek = 0;
        }

        /// <summary>
        /// 保存
        /// </summary>
        /// <returns></returns>
        public virtual bool Save()
        {
            IntPtr fp = CCFileUtils.FileOpen(fullFilePath, "w+");

            IntPtr cb = CCFileUtils.CreateBuffer(buffer.Count);
            for (int i = 0; i < buffer.Count; i++)
            {
                CCFileUtils.WriteBufferByte(cb, buffer.Count, i, buffer[i]);
            }
            CCFileUtils.WriteBuffer(fp, buffer.Count, cb);
            CCFileUtils.FileClose(fp);

            CCFileUtils.DeleteBuffer(cb);
            return true;
        }
        /// <summary>
        /// 保存自定义buffer
        /// </summary>
        /// <param name="_buffer"></param>
        /// <returns></returns>
        public virtual bool Save(byte[] _buffer)
        {
            IntPtr fp = CCFileUtils.FileOpen(fullFilePath, "w+");

            IntPtr cb = CCFileUtils.CreateBuffer(_buffer.Length);
            for (int i = 0; i < _buffer.Length; i++)
            {
                CCFileUtils.WriteBufferByte(cb, _buffer.Length, i, _buffer[i]);
            }
            CCFileUtils.WriteBuffer(fp, _buffer.Length, cb);
            CCFileUtils.FileClose(fp);

            CCFileUtils.DeleteBuffer(cb);
            return true;
        }

        public byte[] Buffer
        {
            get { return buffer.ToArray(); }
        }

        public int Size
        {
            get { return buffer.Count; }
        }

        public int Seek
        {
            set
            {
                _seek = value;
            }

            get { return _seek; }
        }

        //readable
        public virtual bool Read(out byte _byte)
        {
            _byte = 0;
            if (_seek + 1 <= buffer.Count)
            {
                _byte = buffer[_seek];
                _seek += 1;
                return true;
            }
            return false;
        }

        public virtual bool Read(out short _short)
        {
            _short = 0;
            if (_seek + 2 <= buffer.Count)
            {
                byte[] temp = { 0, 0 };
                temp[0] = buffer[_seek];
                temp[1] = buffer[_seek + 1];
                _short = BitConverter.ToInt16(temp, 0);
                _seek += 2;
                return true;
            }
            return false;
        }

        public virtual bool Read(out int _int)
        {
            _int = 0;
            if (_seek + 4 <= buffer.Count)
            {
                byte[] temp = { 0, 0, 0, 0 };
                temp[0] = buffer[_seek];
                temp[1] = buffer[_seek + 1];
                temp[2] = buffer[_seek + 2];
                temp[3] = buffer[_seek + 3];
                _int = BitConverter.ToInt32(temp, 0);
                _seek += 4;
                return true;
            }
            return false;
        }

        public virtual bool Read(out float _float)
        {
            _float = 0;
            if (_seek + 4 <= buffer.Count)
            {
                byte[] temp = { 0, 0, 0, 0 };
                temp[0] = buffer[_seek];
                temp[1] = buffer[_seek + 1];
                temp[2] = buffer[_seek + 2];
                temp[3] = buffer[_seek + 3];
                _float = BitConverter.ToSingle(temp, 0);
                _seek += 4;
                return true;
            }
            return false;
        }

        //writeable
        public virtual bool Write(byte _byte)
        {
            if (_seek + 1 <= buffer.Count)
            {
                buffer[_seek] = _byte;
            }
            else
            {
                buffer.Add(_byte);
            }
            _seek += 1;
            return true;
        }
        public virtual bool Write(short _short)
        {
            byte[] bytes = BitConverter.GetBytes(_short);
            WriteBytes(bytes);
            return true;
        }
        public virtual bool Write(int _int)
        {
            byte[] bytes = BitConverter.GetBytes(_int);
            WriteBytes(bytes);
            return true;
        }

        public virtual bool Write(float _float)
        {
            byte[] bytes = BitConverter.GetBytes(_float);
            WriteBytes(bytes);
            return true;
        }

        private void WriteBytes(byte[] bytes)
        {
            for (int i = 0; i < bytes.Length; i++)
            {
                if (_seek + 1 <= buffer.Count)
                {
                    buffer[_seek] = bytes[i];
                }
                else
                {
                    buffer.Add(bytes[i]);
                }
                _seek += 1;
            }
        }

    }
}
