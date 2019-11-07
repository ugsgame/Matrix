#ifndef __EDITORCONFIG_H__
#define __EDITORCONFIG_H__

#include "Common.h"
#include <string>

class EditorConfig
{

public:
	EditorConfig();
	~EditorConfig();

	static EditorConfig* shareEditorConfig();
public:
	MX_PROPERTY(std::string,m_sApplicationPath,ApplicationPath);
	MX_PROPERTY(std::string,m_sWorkPath,WorkPath);
	MX_PROPERTY(std::string,m_sCurrentFile,CurrentFile);

	std::string getCurrentDir();

	bool serialization();
	bool unserialization();

protected:
private:

	static EditorConfig* sm_pSharedEditorConfig;
};

#endif