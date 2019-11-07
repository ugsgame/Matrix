
#include "MatrixPlugin.h"
#include "PluginManager.h"
#include "PluginSystem.h"
#include "Config/MatrixConfig.h"

#include <IMonoScriptbind.h>

#include <cocos2d.h>
USING_NS_CC;


#ifdef  WIN32
//#include "WinBase.h" // - for LoadLibrary and GetProcAddress
#include<windows.h> 
static HMODULE s_pluginDllHandle = NULL;
#elif defined(ANDROID)
#include "dlfcn.h" // - for dlopen and dlsym
static void* s_pluginDllHandle;
#endif

#ifdef WIN32
int _UTF8ToWide(const char* src, unsigned short * dst, int dstMaxBytes)
{
	CCAssert((0 != src) && (0 != dst) && (dstMaxBytes > 2) && ((dstMaxBytes & 1) == 0),"");
	int numConv = MultiByteToWideChar(CP_UTF8, 0, src, -1, (LPWSTR) dst, (dstMaxBytes / 2) - 1);
	if (numConv > 0)
	{
		dst[numConv] = 0;
		return numConv;
	}
	else
	{
		DWORD errCode = GetLastError();
		CCAssert("UTF8ToWide() failed to convert string '%s' to wide char! ErrorCode %d", src, errCode);
		return 0;
	}
}

#endif

PluginSystem * PluginSystem::_sharePluginSystem = 0;

PluginSystem::PluginSystem()
{
	//
}

PluginSystem::~PluginSystem()
{

}

void PluginSystem::setupPluginSystem(bool debug)
{
	PluginConfig pluginConfig = MatrixConfig::shareMatrixConfig()->getPluginConfig();

	const char* pluginPath = pluginConfig.pluginPath.c_str();
	for (int i = 0; i < pluginConfig.plugins.size(); i++)
	{
		std::string  libPath = CCFileUtils::sharedFileUtils()->fullPathForFilename(pluginPath);
		std::string  lib = pluginConfig.plugins[i];
		libPath += lib;

		loadPluginLibrary(libPath.c_str());
	}
	//setup plugins
	PluginManager::sharePluginManager()->setupPlugins(debug);
}

PluginSystem* PluginSystem::sharePluginSystem()
{
	if(!_sharePluginSystem)
	{
		_sharePluginSystem = new PluginSystem();
	}
	return _sharePluginSystem;
}

const char* PluginSystem::loadPluginLibrary(std::string libPath)
{
	//TODO:
	typedef MatrixPlugin* (*CreatePlugin)();		//定义函数指针类型
	MatrixPlugin* plugin = NULL;

#if(CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
	libPath += ".dll";
	char16_t path[512] = {0};
	_UTF8ToWide(libPath.c_str(), path, 512);
	HMODULE hMonoModule = LoadLibraryW( (wchar_t*)path );

	if (!hMonoModule)
	{
		CCLog((libPath + " not found!").c_str());
		return 0;
	}
	else
	{
		CCLog((libPath+" load successful").c_str());
	}
	// - save this handle
	s_pluginDllHandle = hMonoModule;

	CreatePlugin createPlugin=(CreatePlugin)GetProcAddress(hMonoModule,PLUGIN_CREATE); //
#elif(CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
	libPath += ".so";
	// - dynamic load so
	const char* loadInfo = NULL;
	void* hMonoModule = dlopen( (char*)libPath.c_str() ,RTLD_LAZY);
	loadInfo = dlerror();

	if(NULL != loadInfo)
	{
		CCLog((libPath + " not found!").c_str());
		return 0;
	}
	else
	{
		CCLog((libPath+" load successful").c_str());
	}
	// - save this handle
	s_pluginDllHandle = hMonoModule;

	CreatePlugin createPlugin=(CreatePlugin)dlsym(hMonoModule,PLUGIN_CREATE); //
#elif(CC_TARGET_PLATFORM == CC_PLATFORM_IOS)

#endif
	CCAssert(createPlugin,"Can't find CreatePlugin function!!");
	//FreeLibrary(hMonoModule);

	plugin = createPlugin();
	if(!plugin)
	{
		CCLog("Can't create plugin from %s",libPath.c_str());
		return 0;
	}

	plugin->setScriptSystem(ShareMonoScriptSystem());
	PluginManager::sharePluginManager()->addPlugin(plugin);


	CCLog("Load plugin: %s",plugin->getPluginName().c_str());
	//
	return plugin->getPluginName().c_str();
}



MatrixPlugin* PluginSystem::getPlugin(const char* pluginName)
{
	return PluginManager::sharePluginManager()->getPlugin(pluginName);
}