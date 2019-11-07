using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCShaderNode : CCNode
    {
        internal CCShaderNode(IntPtr t) : base(t) { }

        /// <summary>
        /// 如果设为true,会实时resetShader()，不用手动调ResetShader（）
        /// 但会每画一次就会遍历一次所有node，会耗一点效率
        /// </summary>
        /// <param name="realTime"></param>
        public void SetRealTimeReset(bool realTime)
        {
            NativeEffectNode.SetRealTimeReset(this.CppObjPtr, realTime);
        }
        /// <summary>
        /// 添加完节点后要调用ResetShader()才会有效果
        /// </summary>
        public void ResetShader()
        {
            NativeEffectNode.ResetShader(this.CppObjPtr);
        }

        public bool Enable
        {
            get { return NativeEffectNode.IsEnale(this.CppObjPtr); }
            set { NativeEffectNode.SetIsEnable(this.CppObjPtr,value); }
        }

    }
    
    /// <summary>
    /// 灰度效果
    /// </summary>
    public class CCGrayNode : CCShaderNode
    {
        internal CCGrayNode(IntPtr t) : base(t) { }
        public CCGrayNode()
            : base(NativeEffectNode.GrayNodeCreate())
        {

        }
    }
}
