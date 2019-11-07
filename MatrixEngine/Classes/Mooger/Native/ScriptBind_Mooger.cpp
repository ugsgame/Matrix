
//#include "stdneb.h"
#include "Mooger/MoogerMap.h"

#include "ScriptBind_Mooger.h"

ScriptBind_Mooger::ScriptBind_Mooger()
{

	REGISTER_METHOD(CreateMoogrMap);
	REGISTER_METHOD(LoadMapWithFile);
}

ScriptBind_Mooger::~ScriptBind_Mooger()
{

}

MoogerMap* ScriptBind_Mooger::CreateMoogrMap()
{
	return MoogerMap::Create();
}

bool ScriptBind_Mooger::LoadMapWithFile(MoogerMap* moogerMap,mono::string fileName,mono::string resPath)
{
	 return moogerMap->LoadWithFile(ToMatrixString(fileName).c_str(),ToMatrixString(resPath).c_str());
}