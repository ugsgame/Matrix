
#ifndef __SCRIPTBIND_AUDIOENGINE__
#define __SCRIPTBIND_AUDIOENGINE__

#include "ScriptBind_CocosDenshion.h"

class ScriptBind_AudioEngine:public ScriptBind_CocosDenshion
{
public:
	ScriptBind_AudioEngine();
	~ScriptBind_AudioEngine();

	virtual const char* GetClassName(){ return "NativeAudioEngine"; };

	static void PreloadBackgroundMusic(mono::string pszFilePath);
	static void PlayBackgroundMusic(mono::string pszFilePath, bool bLoop);
	//static void PlayBackgroundMusic(const char* pszFilePath);

	static void StopBackgroundMusic(bool bReleaseData);
	static void PauseBackgroundMusic();
	static void ResumeBackgroundMusic();
	static void RewindBackgroundMusic();

	static bool WillPlayBackgroundMusic();
	static bool IsBackgroundMusicPlaying();

	static float GetBackgroundMusicVolume();
	static void  SetBackgroundMusicVolume(float volume);

	static float GetEffectsVolume();
	static void SetEffectsVolume(float volume);

	static int PlayEffect(mono::string pszFilePath, bool bLoop);

	static void PauseEffect(unsigned int nSoundId);
	static void PauseAllEffects();

	static void ResumeEffect(unsigned int nSoundId);
	static void ResumeAllEffects();

	static void StopEffect(unsigned int nSoundId);
	static void StopAllEffects();

	static void PreloadEffect(mono::string  pszFilePath);
	static void UnloadEffect(mono::string  pszFilePath);
protected:
private:
};

#endif