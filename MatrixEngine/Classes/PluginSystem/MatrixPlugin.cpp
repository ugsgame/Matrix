
#include <IMonoScriptbind.h>
#include "MatrixPlugin.h"

MatrixPlugin::MatrixPlugin()
{

}

MatrixPlugin::~MatrixPlugin()
{

}

void MatrixPlugin::setScriptSystem(IMonoScriptSystem* sys)
{
	SetShareMonoScriptSystem(sys);
}

bool MatrixPlugin::setupPlugin(bool debug)
{
	return true;
}

void MatrixPlugin::registerScript()
{

}
