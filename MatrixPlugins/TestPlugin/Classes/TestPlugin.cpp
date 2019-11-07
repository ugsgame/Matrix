
#include "TestPlugin.h"
#include "Native/ScriptBind_Test.h"

extern "C"
{
	MatrixPlugin* CreatePlugin()
	{
		return TestPlugin::create();
	}

}

TestPlugin::TestPlugin()
{
	this->setPluginName(TESTPLUGIN_NAME);
	this->setNamespace(TESTPLUGIN_NAMESPACE);
}

TestPlugin::~TestPlugin()
{

}


TestPlugin* TestPlugin::create()
{
	return new TestPlugin();
}

bool TestPlugin::setupPlugin(bool debug)
{
	this->registerScript();

	printf("TestPlugin setup: %s \n",this->getPluginName().c_str());

	return true;
}

void TestPlugin::registerScript()
{
	RegisterBinding(ScriptBind_Test);
}
