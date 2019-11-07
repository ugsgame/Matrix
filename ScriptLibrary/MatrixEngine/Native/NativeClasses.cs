using System;
using System.Runtime.CompilerServices;
using MatrixEngine;
using MatrixEngine.Platform.Android;

namespace MatrixEngine.Native
{
    internal class NativeSystem
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Log(string log, bool line);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LogWaring(string log);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LogError(string log);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void LogFile(string log);
    }

    internal class NativeActorSystem
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr CreateActor();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ActorSetActive(IntPtr actor, bool active);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool ActorIsActive(IntPtr actor);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr ActorAddComponent(IntPtr actor,string component,Component com);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ActorRemoveComponent(IntPtr actor, string component);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float TickTime(IntPtr actor);

    }

    internal class NativeCollistionSystem
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static ActorBehavior ColliderGetActor(IntPtr pCollider);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void Update(float dTime);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static IntPtr GetCollistionSystem();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void CollistionSystemSetDrawLayer(IntPtr pNode);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void CollistionSystemSetDebug(bool debug);
    }

    internal class NativeNetHelper
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void OpenUrl(string url);
    }
}
