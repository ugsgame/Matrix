#ifndef __MATRIXCONFIG__
#define __MATRIXCONFIG__

#include <string>
#include <vector>

struct sConfig
{
	virtual void ToMsg(){}
};
//�������ϵͳ
struct AppConfig:public sConfig
{
	std::string appName;					//APP����
	float screenWidth;						//���ڿ�
	float screenHeight;						//���ڸ�
	bool  displayStats;
	float fps;

	virtual void  ToMsg();
};
//�ű����
struct ScriptConfig:public sConfig
{
	std::string monoPath;					//mono dllĿ¼
	std::string scriptPath;					//�ű�dllĿ¼
	std::string gameDll;					//�ű�dll����
	std::string mainClass;					//��Ϸ�����
	std::string mainFunction;				//��ں���
	bool  isDebug;							//�Ƿ���Ҫ����

	virtual void  ToMsg();
};
//������
struct LibrarysConfig:public sConfig
{
	std::string libraryPath;				//������·��
	std::vector<std::string> librayies;		//����������

	virtual void  ToMsg();
};
//������
struct PluginConfig:public sConfig
{
	std::string pluginPath;					//������ڵ�Ŀ¼
	std::vector<std::string> plugins;		//������֣�������׺����

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
