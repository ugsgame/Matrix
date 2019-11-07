using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Native;

namespace MatrixEngine
{
    public class Component : Base
    {
        protected ActorBehavior mActor = null;

        public Component(ActorBehavior actor):base(IntPtr.Zero)
        {
            mActor = actor;
            this.CppObjPtr = NativeActorSystem.ActorAddComponent(actor.CppObjPtr, ((object)this).GetType().Name, this);
        }

        public ActorBehavior GetActor()
        {
            return mActor;
        }
    }
}
