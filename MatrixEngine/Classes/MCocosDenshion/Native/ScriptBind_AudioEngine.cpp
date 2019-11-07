
#include "SimpleAudioEngine.h"

#include "ScriptBind_AudioEngine.h"

using namespace CocosDenshion;

ScriptBind_AudioEngine::ScriptBind_AudioEngine()
{
	REGISTER_METHOD(PreloadBackgroundMusic);
	REGISTER_METHOD(PlayBackgroundMusic);
	REGISTER_METHOD(StopBackgroundMusic);
	REGISTER_METHOD(PauseBackgroundMusic);
	REGISTER_METHOD(ResumeBackgroundMusic);
	REGISTER_METHOD(RewindBackgroundMusic);
	REGISTER_METHOD(WillPlayBackgroundMusic);
	REGISTER_METHOD(IsBackgroundMusicPlaying);
	REGISTER_METHOD(GetBackgroundMusicVolume);
	REGISTER_METHOD(SetBackgroundMusicVolume);
	REGISTER_METHOD(GetEffectsVolume);
	REGISTER_METHOD(SetEffectsVolume);
	REGISTER_METHOD(PlayEffect);
	REGISTER_METHOD(PauseEffect);
	REGISTER_METHOD(PauseAllEffects);
	REGISTER_METHOD(ResumeEffect);
	REGISTER_METHOD(ResumeAllEffects);
	REGISTER_METHOD(StopEffect);
	REGISTER_METHOD(StopAllEffects);
	REGISTER_METHOD(PreloadEffect);
	REGISTER_METHOD(UnloadEffect);
}

ScriptBind_AudioEngine::~ScriptBind_AudioEngine()
{

}

void ScriptBind_AudioEngine::PreloadBackgroundMusic(mono::string pszFilePath)
{
	SimpleAudioEngine::sharedEngine()->preloadBackgroundMusic(ToMatrixString(pszFilePath).c_str());
}

void ScriptBind_AudioEngine::PlayBackgroundMusic(mono::string pszFilePath, bool bLoop)
{
	SimpleAudioEngine::sharedEngine()->playBackgroundMusic(ToMatrixString(pszFilePath).c_str(),bLoop);
}
//static void PlayBackgroundMusic(const char* pszFilePath);

void ScriptBind_AudioEngine::StopBackgroundMusic(bool bReleaseData)
{
	SimpleAudioEngine::sharedEngine()->stopBackgroundMusic(bReleaseData);
}
void ScriptBind_AudioEngine::PauseBackgroundMusic()
{
	SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();
}
void ScriptBind_AudioEngine::ResumeBackgroundMusic()
{
	SimpleAudioEngine::sharedEngine()->resumeBackgroundMusic();
}
void ScriptBind_AudioEngine::RewindBackgroundMusic()
{
	SimpleAudioEngine::sharedEngine()->rewindBackgroundMusic();
}

bool ScriptBind_AudioEngine::WillPlayBackgroundMusic()
{
	return SimpleAudioEngine::sharedEngine()->willPlayBackgroundMusic();
}
bool ScriptBind_AudioEngine::IsBackgroundMusicPlaying()
{
	return SimpleAudioEngine::sharedEngine()->isBackgroundMusicPlaying();
}

float ScriptBind_AudioEngine::GetBackgroundMusicVolume()
{
	return SimpleAudioEngine::sharedEngine()->getBackgroundMusicVolume();
}
void  ScriptBind_AudioEngine::SetBackgroundMusicVolume(float volume)
{
	SimpleAudioEngine::sharedEngine()->setBackgroundMusicVolume(volume);
}

float ScriptBind_AudioEngine::GetEffectsVolume()
{
	return SimpleAudioEngine::sharedEngine()->getEffectsVolume();
}
void ScriptBind_AudioEngine::SetEffectsVolume(float volume)
{
	SimpleAudioEngine::sharedEngine()->setEffectsVolume(volume);
}
int ScriptBind_AudioEngine::PlayEffect(mono::string pszFilePath, bool bLoop)
{
	return SimpleAudioEngine::sharedEngine()->playEffect(ToMatrixString(pszFilePath).c_str(),bLoop);
}

void ScriptBind_AudioEngine::PauseEffect(unsigned int nSoundId)
{
	SimpleAudioEngine::sharedEngine()->pauseEffect(nSoundId);
}
void ScriptBind_AudioEngine::PauseAllEffects()
{
	SimpleAudioEngine::sharedEngine()->pauseAllEffects();
}

void ScriptBind_AudioEngine::ResumeEffect(unsigned int nSoundId)
{
	SimpleAudioEngine::sharedEngine()->resumeEffect(nSoundId);
}
void ScriptBind_AudioEngine::ResumeAllEffects()
{
	SimpleAudioEngine::sharedEngine()->resumeAllEffects();
}

void ScriptBind_AudioEngine::StopEffect(unsigned int nSoundId)
{
	SimpleAudioEngine::sharedEngine()->stopEffect(nSoundId);
}
void ScriptBind_AudioEngine::StopAllEffects()
{
	SimpleAudioEngine::sharedEngine()->stopAllEffects();
}

void ScriptBind_AudioEngine::PreloadEffect(mono::string  pszFilePath)
{
	SimpleAudioEngine::sharedEngine()->preloadEffect(ToMatrixString(pszFilePath).c_str());
}
void ScriptBind_AudioEngine::UnloadEffect(mono::string  pszFilePath)
{
	SimpleAudioEngine::sharedEngine()->unloadEffect(ToMatrixString(pszFilePath).c_str());
}