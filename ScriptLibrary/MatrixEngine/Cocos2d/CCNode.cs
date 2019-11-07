using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.Util;
using MatrixEngine.Math;
using MatrixEngine.Cocos2d.Native;

namespace MatrixEngine.Cocos2d
{
    public class CCNode : CCObject
    {


        internal ScriptManager scriptManager = new ScriptManager();
        //schedule Cache
        Dictionary<string, IntPtr> scheduleCache = new Dictionary<string, IntPtr>(4);

        internal CCNode(IntPtr t)
            : base(t)
        {
        }

        public CCNode()
            : base(NativeNode.Create())
        {
            //this.CppObjPtr = NativeNode.Create();
            //对象类型缓存
            //ScriptManager.PutScriptObject(this);
        }
        ~CCNode()
        {
            //            StopAllAction();
        }
        public virtual int ZOrder
        {
            set
            {
                NativeNode.SetZOrder(this.CppObjPtr, value);
            }
            get { return NativeNode.GetZOrder(this.CppObjPtr); }
        }

        public virtual float VertexZ
        {
            set { NativeNode.SetVertexZ(this.CppObjPtr, value); }
            get { return NativeNode.GetVertexZ(this.CppObjPtr); }
        }

        public virtual float ScaleX
        {
            set { NativeNode.SetScaleX(this.CppObjPtr, value); }
            get { return NativeNode.GetScaleX(this.CppObjPtr); }
        }

        public virtual float ScaleY
        {
            set { NativeNode.SetScaleY(this.CppObjPtr, value); }
            get { return NativeNode.GetScaleY(this.CppObjPtr); }
        }

        private Vector2 scale;
        public virtual Vector2 Scale
        {
            //             set { NativeNode.SetScale(this.CppObjPtr, value); }
            //             get { return NativeNode.GetScale(this.CppObjPtr); }
            set
            {
                ScaleX = value.X;
                ScaleY = value.Y;
            }
            get
            {
                scale.X = ScaleX;
                scale.Y = ScaleY;
                return scale;
            }
        }

        public virtual void SetScale(float scale)
        {
            NativeNode.SetScale(this.CppObjPtr, scale);
        }

        public virtual float GetScale()
        {
            return NativeNode.GetScale(this.CppObjPtr);
        }

        private Vector2 postion = new Vector2();
        public virtual Vector2 Postion
        {
            set { NativeNode.SetPostion(this.CppObjPtr, value.X, value.Y); }
            get
            {
                postion.X = NativeNode.GetPostionX(this.CppObjPtr);
                postion.Y = NativeNode.GetPostionY(this.CppObjPtr);
                return postion;
            }

        }

        public virtual float PostionX
        {

            set { NativeNode.SetPostionX(this.CppObjPtr, value); }
            get { return NativeNode.GetPostionX(this.CppObjPtr); }
        }

        public virtual float PostionY
        {

            set { NativeNode.SetPostionY(this.CppObjPtr, value); }
            get { return NativeNode.GetPostionY(this.CppObjPtr); }
        }

        public virtual float SkewX
        {
            set { NativeNode.SetSkewX(this.CppObjPtr, value); }
            get { return NativeNode.GetSkewX(this.CppObjPtr); }
        }

        public virtual float SkewY
        {
            set { NativeNode.SetSkewY(this.CppObjPtr, value); }
            get { return NativeNode.GetSkewY(this.CppObjPtr); }
        }

        private Vector2 anchorPoint = new Vector2();
        public virtual Vector2 AnchorPoint
        {
            set { NativeNode.SetAnchorPoint(this.CppObjPtr, value.X, value.Y); }
            get
            {
                anchorPoint.X = NativeNode.GetAnchorPointX(this.CppObjPtr);
                anchorPoint.Y = NativeNode.GetAnchorPointY(this.CppObjPtr);
                return anchorPoint;
            }
        }

        private Size contextSize = new Size();
        public virtual Size ContextSize
        {
            set { NativeNode.SetContentSize(this.CppObjPtr, value.width, value.height); }
            get
            {
                contextSize.width = NativeNode.GetContentSizeW(this.CppObjPtr);
                contextSize.height = NativeNode.GetContentSizeH(this.CppObjPtr);
                return contextSize;
            }
        }

        public virtual float Rotation
        {
            set { NativeNode.SetRotation(this.CppObjPtr, value); }
            get { return NativeNode.GetRotation(this.CppObjPtr); }
        }

        public virtual int OrderOfArrival
        {
            set { NativeNode.SetOrderOfArrival(this.CppObjPtr, value); }
            get { return NativeNode.GetOrderOfArrival(this.CppObjPtr); }
        }

        public virtual int Tag
        {
            set { NativeNode.SetTag(this.CppObjPtr, value); }
            get { return NativeNode.GetTag(this.CppObjPtr); }
        }

        public virtual CCScheduler Scheduler
        {
            set
            {
                NativeNode.SetScheduler(this.CppObjPtr, value.CppObjPtr);
            }
            get
            {
                return new CCScheduler(NativeNode.GetScheduler(this.CppObjPtr));
            }
        }

        private Rect boundingBox;
        public virtual Rect BoundingBox
        {
            get
            {
                NativeNode.BoundingBox(this.CppObjPtr, out boundingBox);
                return boundingBox;
            }
        }

        internal CCNode _parent;
        public virtual CCNode Parent
        {
            set
            {
                if (_parent != null)
                {
                    _parent.scriptManager.RemoveScriptObject(this);
                }
                _parent = value;
                if (_parent != null)
                {
                    _parent.scriptManager.PutScriptObject(this);
                }
                NativeNode.SetParent(this.CppObjPtr, value.CppObjPtr);
            }
            get
            {
                if (_parent != null)
                {
                    return _parent;
                }
                //查找子的指针
                IntPtr scriptObject = NativeNode.GetParent(this.CppObjPtr);
                //如果不是空指针表示存在子
                if (scriptObject != IntPtr.Zero)
                {
                    var putNode = new CCNode(scriptObject);
                    putNode.scriptManager.PutScriptObject(this);
                    _parent = putNode;
                    return putNode;
                }
                return null;
            }
        }

        public CCNode this[int index]
        {
            get
            {
                int count = GetChildrenCount();
                if (index < 0 || index >= count)
                {
                    throw new IndexOutOfRangeException("" + count);
                }


                //查找子的指针
                IntPtr scriptObject = NativeNode.GetChildByIndex(this.CppObjPtr, index);
                //如果不是空指针表示存在子
                if (scriptObject != IntPtr.Zero)
                {
                    ///查找相关的对象缓存是否存在
                    CCNode putNode = scriptManager.GetScriptObject(scriptObject) as CCNode;
                    if (putNode == null)
                    {
                        //对象类型缓存
                        putNode = new CCNode(scriptObject);
                        putNode._parent = this;
                        scriptManager.PutScriptObject(putNode);
                    }
                    return putNode;
                }
                return null;
            }
        }

        public virtual bool IsVisible
        {
            get { return GetVisible(); }
            set { SetVisible(value); }
        }

        public virtual void SetVisible(bool visible)
        {
            NativeNode.SetVisible(this.CppObjPtr, visible);
        }

        public virtual bool GetVisible()
        {
            return NativeNode.IsVisible(this.CppObjPtr);
        }

        public virtual bool IsIgnoreAnchorPointForPosition
        {
            set
            {
                NativeNode.SetIgnoreAnchorPointForPosition(this.CppObjPtr, value);
            }
            get
            {
                return NativeNode.IsIgnoreAnchorPointForPosition(this.CppObjPtr);
            }
        }

        public virtual void AddChild(CCNode node)
        {
            if (node.Parent != null)
            {
                node.RemoveFromParent(false);
            }

            node._parent = this;
            scriptManager.PutScriptObject(node);
            NativeNode.AddChild(this.CppObjPtr, node.CppObjPtr);
        }

        public virtual void AddChild(CCNode node, int zOrder)
        {
            if (node.Parent != null)
            {
                node.RemoveFromParent(false);
            }

            node._parent = this;
            scriptManager.PutScriptObject(node);
            NativeNode.AddChildByOrder(this.CppObjPtr, node.CppObjPtr, zOrder);
        }

        public virtual void AddChild(CCNode node, int zOrder, int tag)
        {
            if (node.Parent != null)
            {
                node.RemoveFromParent(false);
            }

            node._parent = this;
            scriptManager.PutScriptObject(node);
            NativeNode.AddChildByOrderTag(this.CppObjPtr, node.CppObjPtr, zOrder, tag);
        }

        //TODO
        public virtual CCNode GetChild(int tag)
        {
            ///return new CCNode(NativeNode.GetChildByTag(this.CppObjPtr, tag));
            //查找子的指针
            IntPtr scriptObject = NativeNode.GetChildByTag(this.CppObjPtr, tag);
            //如果不是空指针表示存在子
            if (scriptObject != IntPtr.Zero)
            {
                ///查找相关的对象缓存是否存在
                CCNode putNode = scriptManager.GetScriptObject(scriptObject) as CCNode;
                if (putNode == null)
                {
                    //对象类型缓存
                    putNode = new CCNode(scriptObject);
                    putNode._parent = this;
                    scriptManager.PutScriptObject(putNode);
                }
                return putNode;
            }
            return null;
        }

        public virtual int GetChildrenCount()
        {
            return NativeNode.GetChildrenCount(this.CppObjPtr);
        }

        public virtual void RemoveFromParent()
        {
            if (_parent != null)
            {
                _parent.scriptManager.RemoveScriptObject(this);
                _parent = null;
            }
            NativeNode.RemoveFromParent(this.CppObjPtr);
        }

        public virtual void RemoveFromParent(bool cleanup)
        {
            if (_parent != null)
            {
                _parent.scriptManager.RemoveScriptObject(this);
                _parent = null;
            }
            NativeNode.RemoveFromParentAndCleanup(this.CppObjPtr, cleanup);
        }

        public virtual void RemoveChild(CCNode node)
        {
            node._parent = null;
            scriptManager.RemoveScriptObject(node);

            NativeNode.RemoveChild(this.CppObjPtr, node.CppObjPtr);
        }

        public virtual void RemoveChild(CCNode node, bool cleanup)
        {
            node._parent = null;
            scriptManager.RemoveScriptObject(node);

            NativeNode.RemoveChildAndCleanup(this.CppObjPtr, node.CppObjPtr, cleanup);
        }

        public virtual void RemoveChild(int tag)
        {
            var node = GetChild(tag);
            node._parent = null;
            scriptManager.RemoveScriptObject(node);

            NativeNode.RemoveChildByTag(this.CppObjPtr, tag);
        }

        public virtual void RemoveChild(int tag, bool cleanup)
        {
            var node = GetChild(tag);
            node._parent = null;
            scriptManager.RemoveScriptObject(node);

            NativeNode.RemoveChildByTagAndCleanup(this.CppObjPtr, tag, cleanup);
        }

        public virtual void RemoveAllChildren()
        {
            //NativeNode.RemoveAllChildren(this.CppObjPtr);
            RemoveAllChildren(false);
        }

        public virtual void RemoveAllChildren(bool cleanup)
        {
            var count = GetChildrenCount();
            for (int i = 0; i < count; i++)
            {
                var node = this[i];
                node._parent = null;
                scriptManager.RemoveScriptObject(node);
            }
            NativeNode.RemoveAllChildrenAndCleanup(this.CppObjPtr, cleanup);
        }
        //这里导致没释放
        internal object runningAction;
        public virtual CCAction RunAction(CCAction action)
        {
            runningAction = action;
            NativeNode.RunAction(this.CppObjPtr, action.CppObjPtr);
            return action;
        }
        //
        internal CCAction runningSequenceAction;
        public virtual CCAction RunSequenceActions(params CCAction[] actions)
        {
            runningSequenceAction = new CCActionSequence(actions);
            NativeNode.RunAction(this.CppObjPtr, runningSequenceAction.CppObjPtr);
            return runningSequenceAction;
        }
        internal CCAction runningSpawnAction;
        public virtual CCAction RunSpawnActions(params CCAction[] actions)
        {
            runningSpawnAction = new CCActionSpawn(actions);
            NativeNode.RunAction(this.CppObjPtr, runningSpawnAction.CppObjPtr);
            return runningSpawnAction;
        }

        public virtual void StopAction(CCAction action)
        {
            NativeNode.StopAction(this.CppObjPtr, action.CppObjPtr);
        }

        public virtual void StopAllAction()
        {
            NativeNode.StopAllAction(this.CppObjPtr);
        }

        private Vector2 CoverToNodeSpace_outPoint;
        public virtual Vector2 CoverToNodeSpace(Vector2 worldPoint)
        {
            //Vector2 outPoint = new Vector2();
            NativeNode.CoverToNodeSpace(this.CppObjPtr, ref worldPoint, out CoverToNodeSpace_outPoint);
            return CoverToNodeSpace_outPoint;
        }

        private Vector2 ConvertToWorldSpace_outPoint;
        public virtual Vector2 ConvertToWorldSpace(Vector2 nodePoint)
        {
            //Vector2 outPoint = new Vector2();
            NativeNode.ConvertToWorldSpace(this.CppObjPtr, ref nodePoint, out ConvertToWorldSpace_outPoint);
            return ConvertToWorldSpace_outPoint;
        }

        private Vector2 ConvertToNodeSpaceAR_outPoint;
        public virtual Vector2 ConvertToNodeSpaceAR(Vector2 worldPoint)
        {
            //Vector2 outPoint = new Vector2();
            NativeNode.ConvertToNodeSpaceAR(this.CppObjPtr, ref worldPoint, out ConvertToNodeSpaceAR_outPoint);
            return ConvertToNodeSpaceAR_outPoint;
        }

        private Vector2 ConvertToWorldSpaceAR_outPoint;
        public virtual Vector2 ConvertToWorldSpaceAR(Vector2 nodePoint)
        {
            //Vector2 outPoint = new Vector2();
            NativeNode.ConvertToWorldSpaceAR(this.CppObjPtr, ref nodePoint, out ConvertToWorldSpaceAR_outPoint);
            return ConvertToWorldSpaceAR_outPoint;
        }

        private bool CheckMethod(string method)
        {
            try
            {
                this.GetType().GetMethod(method);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
                return false;
                throw;
            }
            return true;
        }

        //TODO:改成托管
        public virtual void Schedule(string method, float dt)
        {
            if (!CheckMethod(method)) return;

            IntPtr pScheduleNode;
            if (scheduleCache.TryGetValue(method, out pScheduleNode))
            {
                //if (!NativeScheduleNode.IsScheduling(pScheduleNode))
                {
                    NativeScheduleNode.SetDelayTime(pScheduleNode, dt);
                    NativeScheduleNode.Schedule(pScheduleNode);
                }
            }
            else
            {
                pScheduleNode = NativeScheduleNode.Create(this, method, dt);
                scheduleCache[method] = pScheduleNode;
                NativeNode.AddChild(this.CppObjPtr, pScheduleNode);
                NativeScheduleNode.Schedule(pScheduleNode);
            }
        }
        //TODO:改成托管
        public virtual void Unschedule(string method)
        {
            if (!CheckMethod(method)) return;
            IntPtr pScheduleNode;
            if (scheduleCache.TryGetValue(method, out pScheduleNode))
            {
                //if (NativeScheduleNode.IsScheduling(pScheduleNode))
                {
                    NativeScheduleNode.Unschedule(pScheduleNode);
                }
            }
        }
        //TODO:改成托管
        public virtual bool IsScheduled(string method)
        {
            if (!CheckMethod(method)) return false;

            IntPtr pScheduleNode;
            if (scheduleCache.TryGetValue(method, out pScheduleNode))
            {
                return NativeScheduleNode.IsScheduling(pScheduleNode);
            }
            return false;
        }

        public virtual void ScheduleUpdate()
        {
            NativeNode.ScheduleUpdate(this.CppObjPtr);
        }

        public virtual void UnscheduleUpdate()
        {
            NativeNode.UnscheduleUpdate(this.CppObjPtr);
        }

        public virtual void ResumeSchedulerAndActions()
        {
            NativeNode.ResumeSchedulerAndActions(this.CppObjPtr);
        }
        public virtual void PauseSchedulerAndActions()
        {
            NativeNode.PauseSchedulerAndActions(this.CppObjPtr);
        }
    }
}
