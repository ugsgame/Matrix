using System;
using MatrixEngine.Cocos2d.Native;
using MatrixEngine.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCObject : Base,IDisposable
    {

        public static volatile int ObjectCount = 0;

        internal CCObject(IntPtr ptr)
            : base(ptr)
        {
            //ObjectCount++;

            //Console.WriteLine("Add ObjectCount:" + ObjectCount);
            //Console.WriteLine("Add NativeObjectCount:" + NativeDirector.GetObjectCount());
        }

        public CCObject()
            : base(IntPtr.Zero)
        {
            //ObjectCount++;

            //Console.WriteLine("Add ObjectCount:" + ObjectCount);
            //Console.WriteLine("Add NativeObjectCount:" + NativeDirector.GetObjectCount());
        }

        internal override IntPtr CppObjPtr
        {
            get { return base.CppObjPtr; }
            set
            {
                if (value != IntPtr.Zero)
                {
                    base.CppObjPtr = value;
                    Retain();
                    NativeObject.SetMonoObject(value, this);
                }
                else
                {
                }
            }
        }

        ~CCObject()
        {
            this.Dispose(false);
        }

        internal virtual void Release()
        {
            if (CppObjPtr != IntPtr.Zero)
            {
                NativeObject.Release(CppObjPtr);
                //this.ClearPtr();
            }
        }

        internal virtual void DelayRelease()
        {
            if (CppObjPtr != IntPtr.Zero)
            {
                NativeObject.DelayRelease(this.CppObjPtr);
                //this.ClearPtr();
            }
        }

        public void Dispose()
        {
            //调用带参数的Dispose方法，释放托管和非托管资源
            Dispose(true);
            //手动调用了Dispose释放资源，那么析构函数就是不必要的了，这里阻止GC调用析构函数
            System.GC.SuppressFinalize(this);

        }

        protected bool releaseFlag;
        protected virtual void Dispose(bool disposing)
        {
            //Console.WriteLine("Del NativeObjectCount:" + NativeDirector.GetObjectCount()+" "+disposing);
            if (!releaseFlag)
            {
                if (disposing)
                {
                    this.Release();
                }
                else
                {
                    lock (this)
                    {
                        this.DelayRelease();
                    }
                }
                lock (this)
                {
                    ObjectCount--;
                }
                releaseFlag = true;
            }
            else
            {
                Console.WriteLine("Error release:" + this.GetType().Name);
            }
            //Console.WriteLine("Del ObjectCount:" + ObjectCount);
        }

        internal virtual void Retain()
        {
            if (CppObjPtr != IntPtr.Zero)
            {
                NativeObject.Retain(CppObjPtr);
                ObjectCount++;
            }
        }

        internal virtual void UnBindMonoObject()
        {
            NativeObject.UnBindMonoObject(CppObjPtr);
        }

        public virtual int RetainCount()
        {
            if (CppObjPtr != IntPtr.Zero)
            {
                return NativeObject.RetainCount(CppObjPtr);
            }
            return 0;
        }

        public virtual CCObject Copy()
        {
            if (CppObjPtr == IntPtr.Zero)
            {
                return new CCObject();
            }
            return new CCObject(NativeObject.Copy(CppObjPtr));
        }
    }
}