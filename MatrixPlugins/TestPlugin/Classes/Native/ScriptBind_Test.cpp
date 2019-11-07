
#include "ScriptBind_Test.h"


ScriptBind_Test::ScriptBind_Test()
{
	REGISTER_METHOD(Test);
}

ScriptBind_Test::~ScriptBind_Test()
{
	
}

void ScriptBind_Test::Test()
{
	printf("C++ HellowPlugin \n");
}