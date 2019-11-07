
#include "MatrixConfig.h"

#include "FileService/JsonHelper.h"
#include "FileService/Json/document.h"

#include <cocos2d.h>
USING_NS_CC;

#define  JSON_ISNULL(value) (!strcmp(value,"null"))

MatrixConfig* MatrixConfig::_shareMatrixConfig = NULL;

void AppConfig::ToMsg()
{
	CCLog("AppName: %s",appName.c_str());
	CCLog("ScreenWidth: %f", screenWidth);
	CCLog("ScreenHeight: %f", screenHeight);
	CCLog("displayStats: %d", displayStats);
	CCLog("FPS: %f",fps);
}

void ScriptConfig::ToMsg()
{
	CCLog("MonoPath: %s",monoPath.c_str());
	CCLog("ScriptPath: %s",scriptPath.c_str());
	CCLog("GameDLL: %s",gameDll.c_str());
	CCLog("MainClass: %s",mainClass.c_str());
	CCLog("MainFunction: %s",mainFunction.c_str());
	CCLog("IsDebug: %d",isDebug);
}

void LibrarysConfig::ToMsg()
{
	CCLog("LibraryPath: %s",libraryPath.c_str());
	CCLog("Libraries:");
	for (int i = 0; i < librayies.size(); i++)
	{
		CCLog("     %s",librayies[i].c_str());
	}
}

void PluginConfig::ToMsg()
{
	CCLog("PluginPath: %s",pluginPath.c_str());
	CCLog("Plugin:");
	for (int i = 0; i < plugins.size(); i++)
	{
		CCLog("     %s",plugins[i].c_str());
	}
}

MatrixConfig::MatrixConfig(const char* filePath)
{
	configFilePath = CCFileUtils::sharedFileUtils()->fullPathForFilename(filePath);
	//默认配置
	appConfig.appName = "Matrix";
	appConfig.screenWidth  = 960;
	appConfig.screenHeight = 540;
	appConfig.displayStats = true;
	appConfig.fps = 60.0f;

	scriptConfig.monoPath = "Scripts/";
	scriptConfig.scriptPath = "Scripts/";
	scriptConfig.gameDll    = "Game.dll";
	scriptConfig.mainClass  = "MatrixApp";
	scriptConfig.mainFunction = "StartApp";
	scriptConfig.isDebug = true;

	librarysConfig.libraryPath = "Plugins/";

	pluginConfig.pluginPath = "Plugins/";
}

MatrixConfig::~MatrixConfig()
{

}

MatrixConfig* MatrixConfig::shareMatrixConfig(const char* filePath)
{
	if (!_shareMatrixConfig)
	{
		_shareMatrixConfig = new MatrixConfig(filePath);
		_shareMatrixConfig->openFile();
	}
	return _shareMatrixConfig;
}

bool MatrixConfig::openFile()
{
	if(CCFileUtils::sharedFileUtils()->isFileExist(configFilePath))
	{
		unsigned char *pBytes = NULL;
		rapidjson::Document jsonDict;
		unsigned long size = 0;
		pBytes = CCFileUtils::sharedFileUtils()->getFileData(configFilePath.c_str(),"r" , &size);

		std::string load_str = std::string((const char *)pBytes, size);
		jsonDict.Parse<0>(load_str.c_str());
		if (jsonDict.HasParseError())
		{
			CCLog("GetParseError %s\n",jsonDict.GetParseError());

			return false;
		}

		const char* version = JSONTOOL->getStringValue_json(jsonDict, "version");

		CCLog("Version:%s",version);
		//appConfig
		const rapidjson::Value&  vAppConfig = JSONTOOL->getSubDictionary_json(jsonDict,"AppConfig");
		if (JSONTOOL->checkObjectExist_json(vAppConfig))
		{
			appConfig.appName = JSONTOOL->getStringValue_json(vAppConfig,"appName",appConfig.appName.c_str());

			appConfig.screenWidth = JSONTOOL->getFloatValue_json(vAppConfig,"screenWidth",appConfig.screenWidth);
			appConfig.screenHeight = JSONTOOL->getFloatValue_json(vAppConfig,"screenHeight",appConfig.screenHeight);
			appConfig.displayStats = JSONTOOL->getBooleanValue_json(vAppConfig,"displayStats",appConfig.displayStats);
			appConfig.fps = JSONTOOL->getFloatValue_json(vAppConfig,"fps",appConfig.fps);
		}
		//scriptConfig
		const rapidjson::Value&  vScriptConfig = JSONTOOL->getSubDictionary_json(jsonDict,"ScriptConfig");
		if (JSONTOOL->checkObjectExist_json(vScriptConfig))
		{
			scriptConfig.monoPath = JSONTOOL->getStringValue_json(vScriptConfig,"monoPath",scriptConfig.monoPath.c_str());
			scriptConfig.scriptPath = JSONTOOL->getStringValue_json(vScriptConfig,"scriptPath",scriptConfig.scriptPath.c_str());
			scriptConfig.gameDll = JSONTOOL->getStringValue_json(vScriptConfig,"gameDll",scriptConfig.gameDll.c_str());
			scriptConfig.mainClass = JSONTOOL->getStringValue_json(vScriptConfig,"mainClass",scriptConfig.mainClass.c_str());
			scriptConfig.mainFunction = JSONTOOL->getStringValue_json(vScriptConfig,"mainFunction",scriptConfig.mainFunction.c_str());

			scriptConfig.isDebug = JSONTOOL->getBooleanValue_json(vScriptConfig,"isDebug",scriptConfig.isDebug);
		}
		//librarysConfig
		const rapidjson::Value&  vLibrarysConfig = JSONTOOL->getSubDictionary_json(jsonDict,"LibrarysConfig");
		if (JSONTOOL->checkObjectExist_json(vLibrarysConfig))
		{
			librarysConfig.libraryPath = JSONTOOL->getStringValue_json(vLibrarysConfig,"libraryPath",librarysConfig.libraryPath.c_str());
			
			for (int i = 0; i < JSONTOOL->getArrayCount_json(vLibrarysConfig,"librayies"); i++)
			{
				librarysConfig.librayies.push_back(JSONTOOL->getStringValueFromArray_json(vLibrarysConfig,"librayies",i));
			}
		}
		//pluginConfig
		const rapidjson::Value&  vPluginConfig = JSONTOOL->getSubDictionary_json(jsonDict,"PluginConfig");
		if (JSONTOOL->checkObjectExist_json(vPluginConfig))
		{
			pluginConfig.pluginPath = JSONTOOL->getStringValue_json(vPluginConfig,"pluginPath",pluginConfig.pluginPath.c_str());

			for (int i = 0; i < JSONTOOL->getArrayCount_json(vPluginConfig,"plugins"); i++)
			{
				pluginConfig.plugins.push_back(JSONTOOL->getStringValueFromArray_json(vPluginConfig,"plugins",i));
			}
		}

		CC_SAFE_DELETE_ARRAY(pBytes);
		return true;
	}
	else
	{
		CCLog("No config file found!! using the default setting.");
		return false;
	}
}

//TODO:以当前的配置保存到配置文件
bool MatrixConfig::saveFile()
{
	return true;
}

void MatrixConfig::ToMsg()
{
	CCLog("Matrix Config:");
	CCLog("");

	CCLog("AppConfig");
	appConfig.ToMsg();
	CCLog("");

	CCLog("ScriptConfig");
	scriptConfig.ToMsg();
	CCLog("");

	CCLog("LibrarysConfig");
	librarysConfig.ToMsg();
	CCLog("");

	CCLog("PluginConfig");
	pluginConfig.ToMsg();
	CCLog("");
}
#undef JSON_ISNULL