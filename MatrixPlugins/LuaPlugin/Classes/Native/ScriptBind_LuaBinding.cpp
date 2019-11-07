
#include "ScriptBind_LuaBinding.h"


ScriptBind_LuaBinding::ScriptBind_LuaBinding()
{
	REGISTER_METHOD(Test);
}

ScriptBind_LuaBinding::~ScriptBind_LuaBinding()
{
	
}

void ScriptBind_LuaBinding::Test()
{
	printf("C++ HellowPlugin \n");
}