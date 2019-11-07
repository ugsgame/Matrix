/****************************************************************************
Copyright (c) 2010-2013 cocos2d-x.org
Copyright (c) Microsoft Open Technologies, Inc.

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/

#include "cocoa/CCDictionary.h"
#include "AppDelegate.h"
#include "AppMacros.h"
//MatrixMono H files
#include "MatrixMono.h"
//MatrixEngine H files
#include "MatrixScript.h"
#include "Config/MatrixConfig.h"
#include "PluginSystem/PluginSystem.h"
#include "Platform/SystemFeature.h"
#include "Platform/NativeBridge.h"

USING_NS_CC;

using namespace std;

int	   cmd_Argc	 = 0;
char** cmd_Argv  = NULL ;
IMonoClass *pMainClass = NULL;

#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32) 
#include <iconv.h>
// 编译链接的时候指定静态库  
// #pragma comment(lib,"libiconv.lib") 
// #endif  

int code_convert(char *from_charset,char *to_charset,char *inbuf,int inlen,char *outbuf,int outlen) 
{ 
	iconv_t cd; 
	char **pin = (char**)&inbuf; 
	char **pout = &outbuf; 

	cd = iconv_open(to_charset,from_charset);
	if (cd==0) return -1; 
	memset(outbuf,0,outlen);
#if(CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
	if (iconv(cd,pin,(size_t *)&inlen,pout,(size_t *)&outlen)==-1) return -1;
#else
	if (iconv(cd,(const char **)pin,(size_t *)&inlen,pout,(size_t *)&outlen)==-1) return -1;
#endif
	iconv_close(cd);
	return 0; 
} 
#endif

AppDelegate::AppDelegate() {

}

AppDelegate::~AppDelegate() 
{
}

bool AppDelegate::applicationDidFinishLaunching() {


	//load config file
	std::string configPath = std::string(CONFIG_PATH)+CONFIG_FILE;
	MatrixConfig::shareMatrixConfig(configPath.c_str());
	MatrixConfig::shareMatrixConfig()->ToMsg();
	//

	// initialize director
	CCDirector* pDirector = CCDirector::sharedDirector();
	CCEGLView* pEGLView = CCEGLView::sharedOpenGLView();

	AppConfig appConfig = MatrixConfig::shareMatrixConfig()->getAppConfig();

#if(CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
	size_t inLen = appConfig.appName.length();
	size_t outLen = inLen * 2;
	char* utfImgPath = (char  *)malloc(outLen);
	code_convert("gb2312","utf-8",(char*)appConfig.appName.c_str(), inLen,utfImgPath,outLen);

	pEGLView->setViewName(utfImgPath);
	pEGLView->setFrameSize(appConfig.screenWidth,appConfig.screenHeight);
#endif

	pDirector->setOpenGLView(pEGLView);

	// turn on display FPS
	pDirector->setDisplayStats(appConfig.displayStats);

	// set FPS. the default value is 1.0/60 if you don't call this
	pDirector->setAnimationInterval(1.0 / appConfig.fps);

	// 	FILE* fp = fopen("TempSave.dat","w");
	// 	fclose(fp
	//setup scriptSystem
	this->setupScript();

	return true;
}

// This function will be called when the app is inactive. When comes a phone call,it's be invoked too
void AppDelegate::applicationDidEnterBackground() {
	CCDirector::sharedDirector()->stopAnimation();

	// if you use SimpleAudioEngine, it must be pause
	//SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();

	if (pMainClass)
	{
		*pMainClass->CallMethod("OnEnterBackground");
		NativeBridge::ShareNativeBridge()->OnPause();
	}
	CCLog("OnEnterBackground");
}

// this function will be called when the app is active again
void AppDelegate::applicationWillEnterForeground() {
	CCDirector::sharedDirector()->startAnimation();

	// if you use SimpleAudioEngine, it must resume here
	//SimpleAudioEngine::sharedEngine()->resumeBackgroundMusic();
	if (pMainClass)
	{
		*pMainClass->CallMethod("OnEnterForeground");
		NativeBridge::ShareNativeBridge()->OnResume();
	}
	CCLog("OnEnterForeground");
}

// 加载依赖库
bool AppDelegate::setupDependLibs()
{
	IMonoScriptSystem* pScriptSystem = ShareMonoScriptSystem(); 
	CCFileUtils* fileUtils = CCFileUtils::sharedFileUtils();

	LibrarysConfig libraryConfig = MatrixConfig::shareMatrixConfig()->getLibrarysConfig();

	std::string libPath = fileUtils->fullPathForFilename(libraryConfig.libraryPath.c_str());
	std::vector<std::string> dependLibs = libraryConfig.librayies;

	if (dependLibs.size() > 0)
	{
		for (int i= 0;i< dependLibs.size();i++)
		{
			std::string  lib = dependLibs[i];
			std::string  libFullPath = libPath +lib;
			std::string  libMdbPath = libFullPath+".mdb";

			ulong MdbSize;
			unsigned char* MdbData = fileUtils->getFileData(libMdbPath.c_str(),"rb",&MdbSize);
			std::string libName = lib.erase(lib.length()-4,lib.length());
#if (ASSEMBLY_IMG)
			ulong AssSize;
			void* dllData = fileUtils->getFileData(libFullPath.c_str(),"rb",&AssSize);
			IMonoAssembly* eAssembly = pScriptSystem->LoadAssembly(dllData,AssSize,libName.c_str(),MdbData,MdbSize);
#else
			IMonoAssembly* eAssembly = pScriptSystem->LoadAssembly(libFullPath.c_str(),libName.c_str(),MdbData,MdbSize);	
#endif
		}
		return true;
	}
	else
	{
		return false;
	}
}

void AppDelegate::setupScript()
{
	InitMatrixMono();

	IMonoScriptSystem* pScriptSystem = ShareMonoScriptSystem(); 
	CCFileUtils* fileUtils = CCFileUtils::sharedFileUtils();

	ScriptConfig scriptConfig = MatrixConfig::shareMatrixConfig()->getScriptConfig();

	bool bNeedDebug = scriptConfig.isDebug;

	std::string monoPath		= fileUtils->fullPathForFilename(scriptConfig.monoPath.c_str());
	std::string scriptPath		= fileUtils->fullPathForFilename(scriptConfig.scriptPath.c_str());
	std::string monoDllPath		= monoPath+"mono-2.0.dll";

	std::string engineDll	= "MatrixEngine.dll";
	std::string gameDll		= fileUtils->fullPathForFilename(scriptConfig.gameDll.c_str());
	std::string mainClass	= fileUtils->fullPathForFilename(scriptConfig.mainClass.c_str());
	std::string mainFuction = fileUtils->fullPathForFilename(scriptConfig.mainFunction.c_str());

#if(CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
	std::string packageName = SystemFeature::shareSystemFeature()->getPackageName();
	std::string dataPath = "/data/data/";
	monoPath		= dataPath +packageName +"/";
	monoDllPath		= monoPath +"lib/libmono.so";
	scriptPath      = monoPath;

	cmd_Argc  = 1;
	cmd_Argv  = new char*(cmd_Argc);
	cmd_Argv[0] = "";
#endif
	std::string engineAssPath	= scriptPath+engineDll;
	std::string gameAssPath		= scriptPath+gameDll;

	std::string engineMdbPath   = engineAssPath+".mdb";
	std::string gameMdbPath		= gameAssPath+".mdb";
	//SetupScriptSystem
	pScriptSystem->SetMonoEnvPath(monoPath.c_str(),monoDllPath.c_str());
	pScriptSystem->SetupScriptSystem(bNeedDebug);
	MatrixScript::ShareMatrixScript()->SetupMatrixScript();
	//SetupPluginSystem
	PluginSystem::sharePluginSystem()->setupPluginSystem(bNeedDebug);


	std::string engineDomain = engineDll.erase(engineDll.length()-4,engineDll.length());
	std::string gameDomain = gameDll.erase(gameDll.length()-4,gameDll.length());
	//Load mdb files
	ulong eMdbSize,gMdbSize;
	unsigned char* eMdbData = fileUtils->getFileData(engineMdbPath.c_str(),"rb",&eMdbSize);
	unsigned char* gMdbData = fileUtils->getFileData(gameMdbPath.c_str(),"rb",&gMdbSize);
	//Load dll files
	//TODO:从image data加载dll,运行目录不正确，还要修复
#if(ASSEMBLY_IMG)
	ulong eAssSize,gAssSize;
	void* eDllData = fileUtils->getFileData(engineAssPath.c_str(),"rb",&eAssSize);
	void* gDllData = fileUtils->getFileData(gameAssPath.c_str(),"rb",&gAssSize);
	IMonoAssembly* eAssembly = pScriptSystem->LoadAssembly(eDllData,eAssSize,engineDomain.c_str(),eMdbData,eMdbSize);	
	this->setupDependLibs();
	IMonoAssembly* gAssembly = pScriptSystem->LoadAssembly(gDllData,gAssSize,"Game",gameDomain.c_str(),gMdbSize);
#else
	//加载引擎库
	IMonoAssembly* eAssembly = pScriptSystem->LoadAssembly(engineAssPath.c_str(),engineDomain.c_str(),eMdbData,eMdbSize);	
	//装载游戏依赖库
	this->setupDependLibs();
	//加载游戏逻辑库
	IMonoAssembly* gAssembly = pScriptSystem->LoadAssembly(gameAssPath.c_str(),gameDomain.c_str(),gMdbData,gMdbSize);
#endif

	MatrixScript::ShareMatrixScript()->SetEngineAssembly(eAssembly);
	MatrixScript::ShareMatrixScript()->SetGameAssembly(gAssembly);

	NativeBridge::ShareNativeBridge();
	//调主函数
	pMainClass = gAssembly->GetClass(mainClass.c_str(),gameDomain.c_str());

	//传入配置
	//平台
	//*pMainClass->CallMethod("SetTargetPlatform",CC_TARGET_PLATFORM);
	//

	IMonoArray *pArgs = CreateMonoArray(cmd_Argc);
	for(int i = 0;i < cmd_Argc;i++)
	{
		pArgs->InsertMonoString(ToMonoString(cmd_Argv[i]));
	}

	IMonoArray *pame = CreateMonoArray(1);
	pame->InsertMonoObject(pArgs->GetManagedObject());
	IMonoObject* mInt = *pMainClass->InvokeArray(NULL,mainFuction.c_str(),pame,NULL);

	//TODO:返回结果处理
	int rel = mInt->GetAnyValue().i;
	CCLog("%s:%d",mainFuction.c_str(),rel);

	switch(rel)
	{
	case 0:
		CCLog("rel: %d",rel);
		break;
	case 1:
		CCLog("rel: %d",rel);
		break;
	default:
		CCLog("rel: %d",rel);
		break;
	}
}
