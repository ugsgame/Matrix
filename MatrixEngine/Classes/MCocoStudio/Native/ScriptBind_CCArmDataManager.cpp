
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"
#include "ExtensionMacros.h"

#include "ScriptBind_CCArmDataManager.h"

USING_NS_CC;
USING_NS_CC_EXT;

class ArmLoadEvent:public CCObject
{
public:
	ArmLoadEvent(){}
	~ArmLoadEvent(){};
	static ArmLoadEvent* Create(IMonoObject* obj)
	{
		ArmLoadEvent* event = new ArmLoadEvent();

		int objectCount = event->ObjectCount;
		event->setMonoObject(obj);
		if(event->ObjectCount>objectCount)event->ObjectCount--;

		return event;
	}
	
	void DataLoaded(float percent)
	{
		CCAssert(p_MonoObject,"");
		p_MonoObject->CallMethod("Loading",percent);		
	}	
};



ScriptBind_CCArmDataManager::ScriptBind_CCArmDataManager()
{
	REGISTER_METHOD(AddArmatureFileInfo);
	REGISTER_METHOD(AddArmatureFileInfoes);
	REGISTER_METHOD(AddArmatureFileInfoAsync);
	REGISTER_METHOD(AddArmatureFileInfoesAsync);

	REGISTER_METHOD(RemoveArmatureFileInfo);
}

ScriptBind_CCArmDataManager::~ScriptBind_CCArmDataManager()
{
	
}

void ScriptBind_CCArmDataManager::AddArmatureFileInfo(mono::string configFilePath )
{
	CCArmatureDataManager::sharedArmatureDataManager()->addArmatureFileInfo(ToMatrixString(configFilePath).c_str());
}

void ScriptBind_CCArmDataManager::AddArmatureFileInfoes(mono::string imagePath, mono::string plistPath, mono::string configFilePath)
{
	CCArmatureDataManager::sharedArmatureDataManager()->addArmatureFileInfo(ToMatrixString(imagePath).c_str(),ToMatrixString(plistPath).c_str(),ToMatrixString(configFilePath).c_str());
}

void ScriptBind_CCArmDataManager::AddArmatureFileInfoAsync(mono::string configFilePath,mono::object obj)
{
	CCAssert(obj,"");
	CCArmatureDataManager::sharedArmatureDataManager()->addArmatureFileInfoAsync(ToMatrixString(configFilePath).c_str(),ArmLoadEvent::Create(*obj),schedule_selector(ArmLoadEvent::DataLoaded));
}

void ScriptBind_CCArmDataManager::AddArmatureFileInfoesAsync(mono::string imagePath, mono::string plistPath, mono::string configFilePath,mono::object obj)
{
	CCAssert(obj,"");
	CCArmatureDataManager::sharedArmatureDataManager()->addArmatureFileInfoAsync(ToMatrixString(imagePath).c_str(),ToMatrixString(plistPath).c_str(),ToMatrixString(configFilePath).c_str(),ArmLoadEvent::Create(*obj),schedule_selector(ArmLoadEvent::DataLoaded));
}

void ScriptBind_CCArmDataManager::RemoveArmatureFileInfo(mono::string configFilePath)
{
	CCArmatureDataManager::sharedArmatureDataManager()->removeArmatureFileInfo(ToMatrixString(configFilePath).c_str());
}

