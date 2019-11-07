#ifndef __MATRIXCONFIG__
#define __MATRIXCONFIG__

#include <string>
#include <vector>

struct sConfig
{
	virtual void ToMsg(){}
};
//针对桌面系统
struct AppConfig:public sConfig
{
	std::string appName;					//APP名字
	float screenWidth;						//窗口宽
	float screenHeight;						//窗口高
	bool  displayStats;
	float fps;

	virtual void  ToMsg();
};
//脚本相关
struct ScriptConfig:public sConfig
{
	std::string monoPath;					//mono dll目录
	std::string scriptPath;					//脚本dll目录
	std::string gameDll;					//脚本dll名字
	std::string mainClass;					//游戏入口类
	std::string mainFunction;				//入口函数
	bool  isDebug;							//是否需要调试

	virtual void  ToMsg();
};
//依赖库
struct LibrarysConfig:public sConfig
{
	std::string libraryPath;				//依赖库路径
	std::vector<std::string> librayies;		//依赖库名字

	virtual void  ToMsg();
};
//插件相关
struct PluginConfig:public sConfig
{
	std::string pluginPath;					//插件所在的目录
	std::vector<std::string> plugins;		//插件名字（不带后缀名）

	virtual void  ToMsg();
};

class MatrixConfig:public sConfig
{
public:
	MatrixConfig(const char* filePath);
	~MatrixConfig();

	static MatrixConfig* shareMatrixConfig(const char* filePath = NULL);

	bool openFile();
	bool saveFile();

	void setAppConfig(AppConfig config){ appConfig = config; }
	AppConfig getAppConfig(){ return appConfig; }

	void setScriptConfig(ScriptConfig config){ scriptConfig = config; }
	ScriptConfig getScriptConfig(){ return scriptConfig; }

	void setLibrarysConfig(LibrarysConfig config){ librarysConfig = config; }
	LibrarysConfig getLibrarysConfig(){ return librarysConfig; }

	void setPluginConfig(PluginConfig config){ pluginConfig = config; }
	PluginConfig getPluginConfig(){ return pluginConfig; }

	virtual void ToMsg();

protected:
private:

	AppConfig		appConfig;
	ScriptConfig	scriptConfig;
	LibrarysConfig	librarysConfig;
	PluginConfig	pluginConfig;

	std::string configFilePath;
	static MatrixConfig* _shareMatrixConfig;
};

#endif
