using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MatrixEngine.CocoStudio.Native;
using MatrixEngine.Native;

namespace MatrixEngine.CocoStudio.Armature
{
    public class CCColliderFilter : Base
    {

        ushort m_CategoryBits;
        public ushort CategoryBits
        {
            set{ m_CategoryBits = value; }
            get{ return m_CategoryBits; }
        }
        ushort m_MaskBits; 
        public ushort MaskBits
        {
            set{ m_MaskBits = value; }
            get{ return m_MaskBits; }
        }
        short m_GroupIndex;
        public short GroupIndex
        {
            set { m_GroupIndex = value; }
            get { return m_GroupIndex; }
        }

        public CCColliderFilter(ushort categoryBits /* = 0x0001 */, ushort maskBits /* = 0xFFFF */, short groupIndex)
            : base(NativeArmature.CreateColliderFilter(categoryBits, maskBits, groupIndex))
        {
            m_CategoryBits = categoryBits;
            m_MaskBits = maskBits;
            m_GroupIndex = groupIndex;
        }

        public bool ShouldCollide(CCColliderFilter filter)
        {
            if (filter.GroupIndex == this.GroupIndex && filter.GroupIndex != 0)
            {
                return filter.GroupIndex > 0;
            }

            return (filter.MaskBits & this.CategoryBits) != 0 && (filter.CategoryBits & this.MaskBits) != 0;
        }
    }
}
