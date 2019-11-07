using System;
using System.Collections.Generic;

using MatrixEngine;
using MatrixEngine.Native;
using MatrixEngine.Cocos2d;

namespace MatrixEngine
{
    /// <summary>
    /// 暂时先和cocos2dx的结构一起邦定
    /// </summary>
    public class ActorBehavior : CCNodeRGBA
    {
        private Rigidbody _Rigidboy = null;
        private BoxCollider _BoxCollider = null;

        internal ActorBehavior(IntPtr t) { }
        public ActorBehavior()
            : base(NativeActorSystem.CreateActor())
        {

            this.TagID = ((object)this).GetType().Name;
           
            //需要才添加
            //TODO:释放不切底
            //_Rigidboy = new Rigidbody(this);

            native_OnStart();
        }

        public void RemoveComponent(string component)
        {
            NativeActorSystem.ActorRemoveComponent(this.CppObjPtr, component);
        }

        public void RemoveRigibody()
        {
            this.RemoveComponent("Rigidbody");
        }

        //TODO
        public Rigidbody Rigibody
        {
            get
            {
                return _Rigidboy;
            }
        }

        public BoxCollider BoxCollider
        {
            get
            {
                if (_BoxCollider == null) _BoxCollider = new BoxCollider(this);
                return _BoxCollider;
            }
        }
        //native 回调
        private void native_OnEnter()
        {
            try
            {
                OnEnter();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnExit()
        {
            try
            {
                OnExit();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnDestroy()
        {
            try
            {
                OnDestroy();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnEnable()
        {
            try
            {
                OnEnable();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnDisable()
        {
            try
            {
                OnDisable();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnStart()
        {
            try
            {
                OnStart();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnTriggerEnter(Collider ColliderA, Collider ColliderB)
        {
            try
            {
                OnTriggerEnter(ColliderA, ColliderB);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnTriggerExit(Collider ColliderA, Collider ColliderB)
        {
            try
            {
                OnTriggerExit(ColliderA, ColliderB);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnTriggerStay(Collider ColliderA, Collider ColliderB)
        {
            try
            {
                OnTriggerStay(ColliderA, ColliderB);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnCollisionEnter()
        {
            try
            {
                OnCollisionEnter();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnCollisionExit()
        {
            try
            {
                OnCollisionExit();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnCollisionStay()
        {
            try
            {
                OnCollisionStay();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnMouseEnter()
        {
            try
            {
                OnMouseEnter();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnMouseOver()
        {
            try
            {
                OnMouseOver();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnMouseExit()
        {
            try
            {
                OnMouseExit();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnMouseDown()
        {
            try
            {
                OnMouseDown();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnMouseDrag()
        {
            try
            {
                OnMouseDrag();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnMouseUp()
        {
            try
            {
                OnMouseUp();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnApplicationPause()
        {
            try
            {
                OnApplicationPause();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnApplicationFocus()
        {
            try
            {
                OnApplicationFocus();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnApplicationQuit()
        {
            try
            {
                OnApplicationQuit();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnPlayerConnected()
        {
            try
            {
                OnPlayerConnected();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnPlayerDisconnected()
        {
            try
            {
                OnPlayerDisconnected();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnServerInitialized()
        {
            try
            {
                OnServerInitialized();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnDisconnectedFromServer()
        {
            try
            {
                OnDisconnectedFromServer();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnFailedToConnect()
        {
            try
            {
                OnFailedToConnect();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnFailedToConnectToMasterServer()
        {
            try
            {
                OnFailedToConnect();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnUpdate()
        {
            try
            {
                OnUpdate(NativeActorSystem.TickTime(this.CppObjPtr));
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnUpdate(float dTime)
        {
            try
            {
                OnUpdate(dTime);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnLateUpdate(float dTime)
        {
            try
            {
                OnLateUpdate(dTime);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
        private void native_OnVisit()
        {
            try
            {
                OnVisit();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }



        //进入场景时调用
        protected virtual void OnEnter()
        {
            //Debug.Log("ActorBehavior:OnEnter");
        }
        //退出场景时调用
        protected virtual void OnExit()
        {

        }
        //ActorBehavior 将被销毁时调用
        protected virtual void OnDestroy()
        {
            //Debug.Log("ActorBehavior:OnDestroy");
        }
        //当对象变为可用或激活状态时此函数被调用
        protected virtual void OnEnable()
        {
            //Debug.Log("ActorBehavior:OnEnable");
        }
        //当对象变为不可用或非激活状态时此函数被调用
        protected virtual void OnDisable()
        {

        }
        //Start仅在Update函数第一次被调用前调用
        protected virtual void OnStart()
        {
            //Debug.Log("ActorBehavior:OnStart");
        }
        //重置为默认值(编辑器调用)
        protected virtual void Reset()
        {

        }

        //当Collider(碰撞体)进入trigger(触发器)时调用
        protected virtual void OnTriggerEnter(Collider ColliderA, Collider ColliderB)
        {

        }
        //当Collider(碰撞体)停止触发trigger(触发器)时调用
        protected virtual void OnTriggerExit(Collider ColliderA, Collider ColliderB)
        {

        }
        //当碰撞体接触触发器时，OnTriggerStay将在每一帧被调用
        protected virtual void OnTriggerStay(Collider ColliderA, Collider ColliderB)
        {

        }
        //当Collider(碰撞体)进入trigger(触发器)时调用
        protected virtual void OnCollisionEnter()
        {

        }
        //当此collider/rigidbody停止触发另一个rigidbody/collider时，OnCollisionExit将被调用
        protected virtual void OnCollisionExit()
        {

        }
        //当此collider/rigidbody触发另一个rigidbody/collider时，OnCollisionStay将会在每一帧被调用
        protected virtual void OnCollisionStay()
        {

        }
        //鼠标相关
        //当鼠标进入到GUIElement(GUI元素)或Collider(碰撞体)中时调用(针对有鼠标的设备)
        protected virtual void OnMouseEnter()
        {

        }
        //当鼠标悬浮在GUIElement(GUI元素)或Collider(碰撞体)上时调用(针对有鼠标的设备)
        protected virtual void OnMouseOver()
        {

        }
        //当鼠标移出GUIElement(GUI元素)或Collider(碰撞体)上时调用(针对有鼠标的设备)
        protected virtual void OnMouseExit()
        {

        }
        //当鼠标在GUIElement(GUI元素)或Collider(碰撞体)上点击时调用(只有单点)
        protected virtual void OnMouseDown()
        {

        }
        //当用户鼠标拖拽GUIElement(GUI元素)或Collider(碰撞体)时调用(只有单点)
        protected virtual void OnMouseDrag()
        {

        }
        //当用户释放鼠标按钮时调用(只有单点)
        protected virtual void OnMouseUp()
        {

        }
        //当玩家暂停时发送到所有的游戏物体
        protected virtual void OnApplicationPause()
        {

        }
        //当玩家获得或失去焦点时发送给所有游戏物体
        protected virtual void OnApplicationFocus()
        {

        }
        //在应用退出之前发送给所有的游戏物体
        protected virtual void OnApplicationQuit()
        {

        }

        //网络相关
        protected virtual void OnPlayerConnected()
        {

        }
        protected virtual void OnPlayerDisconnected()
        {

        }
        protected virtual void OnServerInitialized()
        {

        }

        protected virtual void OnDisconnectedFromServer()
        {

        }
        protected virtual void OnFailedToConnect()
        {

        }
        protected virtual void OnFailedToConnectToMasterServer()
        {

        }
        //更新
        public virtual void OnUpdate(float dTime)
        {
            //Debug.Log("ActorBehavior:OnUpdate");
        }
        //OnUpdate后调用
        public virtual void OnLateUpdate(float dTime)
        {

        }
        //重绘
        public virtual void OnVisit()
        {

        }

        //用户调用函数
        //
        public bool IsActive
        {
            get { return NativeActorSystem.ActorIsActive(this.CppObjPtr); }
            set { NativeActorSystem.ActorSetActive(this.CppObjPtr, value); }
        }

        //事件相关(针对可视化编程设计)
        //TODO
        //Even

        public String TagID;
    }
}
