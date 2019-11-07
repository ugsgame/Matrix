using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MatrixEngine.Util
{
    public class FilePathUtil
    {
        string filePath;

        char[] delimiterChars = { '/', '.' };
        string[] words = null;

        public FilePathUtil(string path)
        {
            this.SetPath(path);
        }

        public void SetPath(string path)
        {
            words = null;
            filePath = path;
            words = filePath.Split(delimiterChars);
        }

        public string GetFileName()
        {
            return words[words.Length - 2];
        }

        public string GetSuffix()
        {
            return words[words.Length - 1];
        }

        public string GetPath()
        {
            return filePath.Substring(0, filePath.LastIndexOf('/'));
        }
    }
}
