using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.CompilerServices;

namespace MatrixEngine.CocosDenshion.Native
{
    internal static class NativeAudioEngine
    {
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PreloadBackgroundMusic(string pszFilePath);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PlayBackgroundMusic(string pszFilePath, bool bLoop);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void StopBackgroundMusic(bool bReleaseData);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PauseBackgroundMusic();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ResumeBackgroundMusic();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void RewindBackgroundMusic();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool WillPlayBackgroundMusic();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static bool IsBackgroundMusicPlaying();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetBackgroundMusicVolume();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void  SetBackgroundMusicVolume(float volume);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static float GetEffectsVolume();
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void SetEffectsVolume(float volume);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static int PlayEffect(string pszFilePath, bool bLoop);

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PauseEffect( int nSoundId);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PauseAllEffects();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ResumeEffect( int nSoundId);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void ResumeAllEffects();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void StopEffect( int nSoundId);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void StopAllEffects();

        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void PreloadEffect(string  pszFilePath);
        [MethodImplAttribute(MethodImplOptions.InternalCall)]
        extern internal static void UnloadEffect(string  pszFilePath);
    }
}
