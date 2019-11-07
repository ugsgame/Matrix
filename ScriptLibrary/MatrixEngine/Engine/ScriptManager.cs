using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MatrixEngine.Native;

namespace MatrixEngine
{
    public class ScriptManager
    {
        internal Dictionary<IntPtr, Base> sa = new Dictionary<IntPtr, Base>(32);

        public void PutScriptObject(Base obj)
        {
            if (obj != null)
            {
                sa[obj.CppObjPtr] = obj;
            }
        }

        public void clear()
        {
            sa.Clear();
        }

        public Base GetScriptObject(Base key)
        {
            Base obj = null;
            try
            {
                obj = sa[key.CppObjPtr];
            }
            catch (KeyNotFoundException)
            {
            }
            return obj;
        }

        public Base GetScriptObject(IntPtr key)
        {
            Base obj = null;
            try
            {
                obj = sa[key];
            }
            catch (KeyNotFoundException)
            {
            }
            return obj;
        }


        public void RemoveScriptObject(Base obj)
        {
            if (obj != null)
            {
                sa.Remove(obj.CppObjPtr);
            }
        }

        public void RemoveScriptObject(IntPtr key)
        {
            sa.Remove(key);
        }


//        public void GC()
//        {
//            List<IntPtr> keys = new List<IntPtr>(64);
//            foreach (KeyValuePair<IntPtr, Base> val in sa)
//            {
//                Base b = val.Value;
//                if (b == null)
//                {
//                    keys.Add(val.Key);
//                }
//                else
//                {
//                    //Console.WriteLine("b.IsCanRelease()=" + b.IsCanRelease() + " b="+b);
//                    if (b.IsCanRelease())
//                    {
//                        //b.Release();
//                        keys.Add(val.Key);
//                    }
//                }
//            }
//            foreach (IntPtr key in keys)
//            {
//                //Console.WriteLine("key = " + key);
//                sa.Remove(key);
//            }
//        }
    }
}
