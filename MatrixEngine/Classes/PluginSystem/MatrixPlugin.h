
#ifndef __MATRIXPLUGIN__
#define __MATRIXPLUGIN__

#include <string>

#include "MatrixExport.h"

struct IMonoScriptSystem;

class  MatrixPlugin
{
public:
	MatrixPlugin();
	~MatrixPlugin();

public:

	virtual bool setupPlugin(bool debug);

	virtual void registerScript();

	virtual void setScriptSystem(IMonoScriptSystem* sys);

	virtual void setPluginName(std::string name)
	{
		pluginName = name;
	}

	virtual std::string getPluginName()
	{
		return pluginName;
	}

	virtual void setAssemblyName(std::string assembly)
	{
		pluginAssembly = assembly;
	}

	virtual std::string getAssemblyName()
	{
		return pluginAssembly;
	}

	virtual void setNamespace(std::string pluginName)
	{
		pluginNamespace = pluginName;
	}
	virtual std::string getNamespace()
	{
		return pluginNamespace;
	}
protected:

	std::string pluginName;
	std::string pluginAssembly;

	std::string pluginNamespace;
private:
};

#endif