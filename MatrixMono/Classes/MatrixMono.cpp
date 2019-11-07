
//#include "stdneb.h"

#include "MatrixMono.h"
#include "MonoScriptSystem.h"

bool InitMatrixMono()
{
	CScriptSystem* pScriptSystem = new CScriptSystem();
	return true;
}


void* MonoGetField(IMonoObject* obj,const char* value)
{
	IMonoObject* val = *(obj->GetFieldValue(value));
	return val->GetAnyValue().GetValue();
}

void* MonoGetProperty(IMonoObject* obj, const char* value)
{
	IMonoObject* val = *(obj->GetPropertyValue(value));
	return val->GetAnyValue().GetValue();
}
