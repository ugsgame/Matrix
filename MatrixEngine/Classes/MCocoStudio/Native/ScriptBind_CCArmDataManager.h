
#ifndef __SCRIPTBIND_CCARMDTAMANAGER__
#define __SCRIPTBIND_CCARMDTAMANAGER__

#include "ScriptBind_CocoStudio.h"

class ScriptBind_CCArmDataManager:public ScriptBind_CocoStudio
{
public:
	ScriptBind_CCArmDataManager();
	~ScriptBind_CCArmDataManager();

	virtual const char* GetClassName(){ return "NativeArmDataManager";}

	static void AddArmatureFileInfo(mono::string configFilePath );
	static void AddArmatureFileInfoes(mono::string imagePath, mono::string plistPath, mono::string configFilePath);

	static void AddArmatureFileInfoAsync(mono::string configFilePath,mono::object obj);
	static void AddArmatureFileInfoesAsync(mono::string imagePath, mono::string plistPath, mono::string configFilePath,mono::object obj);

	static void RemoveArmatureFileInfo(mono::string configFilePath);

protected:
private:
};


#endif

