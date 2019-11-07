
#ifndef __FLOWGRAPPLUGIN__
#define __FLOWGRAPPLUGIN__

#include "PluginSystem/MatrixPlugin.h"

#define PLUGIN_NAME			"FlowGrapPlugin"
#define	PLUGIN_NAMESPACE	"Game.Plugins"


extern "C" EXPORT_DLL MatrixPlugin*  CreatePlugin();

class FlowGrapPlugin:public MatrixPlugin
{
public:
	FlowGrapPlugin();
	~FlowGrapPlugin();

public:
	static FlowGrapPlugin* create();

	virtual bool setupPlugin(bool debug);
	virtual void registerScript();

protected:
private:
};

#endif
