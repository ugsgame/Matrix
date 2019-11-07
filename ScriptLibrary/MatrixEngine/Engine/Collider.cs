using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MatrixEngine.Native;

namespace MatrixEngine
{
    /// <summary>
    /// 
    /// </summary>
    public class Collider:Component
    {
        protected Collider(ActorBehavior actor):base(actor)
        {

        }
    }

    public class BoxCollider : Collider
    {
        public BoxCollider(ActorBehavior actor):base(actor)
        {
        }
    }
}
