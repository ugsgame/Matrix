using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.CocosDenshion.Native;

namespace MatrixEngine.CocosDenshion
{
    public class AudioEngine
    {
        public static void PreloadBackgroundMusic(string pszFilePath)
        {
            NativeAudioEngine.PreloadBackgroundMusic(pszFilePath);
        }

        public static void PlayBackgroundMusic(string pszFilePath)
        {
            NativeAudioEngine.PlayBackgroundMusic(pszFilePath, true);
        }

        public static void PlayBackgroundMusic(string pszFilePath, bool bLoop)
        {
            NativeAudioEngine.PlayBackgroundMusic(pszFilePath, bLoop);
        }

        public static void StopBackgroundMusic()
        {
            NativeAudioEngine.StopBackgroundMusic(false);
        }
        public static void StopBackgroundMusic(bool bReleaseData)
        {
            NativeAudioEngine.StopBackgroundMusic(bReleaseData);
        }

        public static void PauseBackgroundMusic()
        {
            NativeAudioEngine.PauseBackgroundMusic();
        }

        public static void ResumeBackgroundMusic()
        {
            NativeAudioEngine.ResumeBackgroundMusic();
        }

        public static void RewindBackgroundMusic()
        {
            NativeAudioEngine.RewindBackgroundMusic();
        }

        public static bool WillPlayBackgroundMusic()
        {
            return NativeAudioEngine.WillPlayBackgroundMusic();
        }

        public static bool IsBackgroundMusicPlaying()
        {
            return NativeAudioEngine.IsBackgroundMusicPlaying();
        }
        /// <summary>
        /// 0.0f-1.0f
        /// </summary>
        public static float BackgroundMusicVolume
        {
            get { return NativeAudioEngine.GetBackgroundMusicVolume(); }
            set { NativeAudioEngine.SetBackgroundMusicVolume(value); }
        }
        /// <summary>
        /// 0.0f-1.0f
        /// </summary>
        public static float EffectsVolume
        {
            get { return NativeAudioEngine.GetEffectsVolume(); }
            set { NativeAudioEngine.SetEffectsVolume(value); }
        }

        public static int PlayEffect(string pszFilePath)
        {
            return PlayEffect(pszFilePath, false);
        }
        public static int PlayEffect(string pszFilePath, bool bLoop)
        {
            return NativeAudioEngine.PlayEffect(pszFilePath, bLoop);
        }

        public static void PauseEffect(int nSoundId)
        {
            NativeAudioEngine.PauseEffect(nSoundId);
        }

        public static void PauseAllEffects()
        {
            NativeAudioEngine.PauseAllEffects();
        }

        public static void ResumeEffect(int nSoundId)
        {
            NativeAudioEngine.ResumeEffect(nSoundId);
        }

        public static void ResumeAllEffects()
        {
            NativeAudioEngine.ResumeAllEffects();
        }

        public static void StopEffect(int nSoundId)
        {
            NativeAudioEngine.StopEffect(nSoundId);
        }

        public static void StopAllEffects()
        {
            NativeAudioEngine.StopAllEffects();
        }

        public static void PreloadEffect(string pszFilePath)
        {
            NativeAudioEngine.PreloadEffect(pszFilePath);
        }

        public static void UnloadEffect(string pszFilePath)
        {
            NativeAudioEngine.UnloadEffect(pszFilePath);
        }
    }
}
