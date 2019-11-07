
#include "EditorConfig.h"

#include <cocos2d.h>
#include <cocos-ext.h>
#include "CocoStudio/Json/rapidjson/stringbuffer.h"
#include "CocoStudio/Json/rapidjson/writer.h"

USING_NS_CC;
USING_NS_CC_EXT;

EditorConfig* EditorConfig::sm_pSharedEditorConfig = NULL;

#define CONFIG_FILE "config.cf"

EditorConfig::EditorConfig()
{

}

EditorConfig::~EditorConfig()
{

}

EditorConfig* EditorConfig::shareEditorConfig()
{
	if(!sm_pSharedEditorConfig)
	{
		sm_pSharedEditorConfig = new EditorConfig();
		//sm_pSharedEditorConfig->unserialization();
	}
	return sm_pSharedEditorConfig;
}

void EditorConfig::setApplicationPath(std::string var)
{
	m_sApplicationPath = var;
}
std::string EditorConfig::getApplicationPath()
{
	return m_sApplicationPath;
}

void EditorConfig::setWorkPath(std::string var)
{
	m_sWorkPath = var;
}
std::string EditorConfig::getWorkPath()
{
	return m_sWorkPath;
}

void EditorConfig::setCurrentFile(std::string var)
{
	m_sCurrentFile = var;
}
std::string EditorConfig::getCurrentFile()
{
	return m_sCurrentFile;
}

std::string EditorConfig::getCurrentDir()
{
	return m_sCurrentFile.substr(0,m_sCurrentFile.find_last_of("/"));
}
bool EditorConfig::serialization()
{
	std::string configFile = this->m_sApplicationPath + "/" + CONFIG_FILE;

	rapidjson::Document document;
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	rapidjson::Value root(rapidjson::kObjectType);

	root.AddMember("workPath",m_sWorkPath.c_str(),allocator);
	root.AddMember("currentFile",m_sCurrentFile.c_str(),allocator);

	rapidjson::StringBuffer buffer;
	rapidjson::Writer<rapidjson::StringBuffer> write(buffer);
	root.Accept(write);

	FILE* fp = fopen(configFile.c_str(),"w");
	fwrite(buffer.GetString(),buffer.Size(),1,fp);
	fclose(fp);

	return true;
}

bool EditorConfig::unserialization()
{
	std::string configFile = this->m_sApplicationPath + "/" + CONFIG_FILE;
	CCFileUtils* fileUtils = CCFileUtils::sharedFileUtils();
	if(fileUtils->isFileExist(configFile.c_str()))
	{
		unsigned char *pBytes = NULL;
		rapidjson::Document jsonDict;
		unsigned long size = 0;
		pBytes = CCFileUtils::sharedFileUtils()->getFileData(configFile.c_str(),"r" , &size);

		std::string load_str = std::string((const char *)pBytes, size);
		jsonDict.Parse<0>(load_str.c_str());

		if (jsonDict.HasParseError())
		{
			CCLog("GetParseError %s\n",jsonDict.GetParseError());

			return false;
		}

		this->setWorkPath(DICTOOL->getStringValue_json(jsonDict,"workPath"));
		this->setCurrentFile(DICTOOL->getStringValue_json(jsonDict,"currentFile"));

		if(!CCFileUtils::sharedFileUtils()->isFileExist(this->m_sCurrentFile))
			m_sCurrentFile = "";


		CC_SAFE_DELETE_ARRAY(pBytes);

		return true;
	}

	return false;
}