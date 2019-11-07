
#include "FlowGrapPlugin.h"

extern "C"
{
	MatrixPlugin* CreatePlugin()
	{
		return FlowGrapPlugin::create();
	}

}

FlowGrapPlugin::FlowGrapPlugin()
{
	this->setPluginName(PLUGIN_NAME);
	this->setNamespace(PLUGIN_NAMESPACE);
}

FlowGrapPlugin::~FlowGrapPlugin()
{

}


FlowGrapPlugin* FlowGrapPlugin::create()
{
	return new FlowGrapPlugin();
}

bool FlowGrapPlugin::setupPlugin(bool debug)
{
	this->registerScript();

	return true;
}

void FlowGrapPlugin::registerScript()
{
	//RegisterBinding();
}
