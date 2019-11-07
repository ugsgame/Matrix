#include "ScriptDebug.h"
#include "MonoConverter.h"

//#include "MonoCVars.h"

#include "MonoScriptSystem.h"
#include "MonoArray.h"
#include "DynMonoArray.h"
#include "MonoObject.h"

#include <IMonoAssembly.h>

IMonoArray *CConverter::ToArray(mono::object arr)
{
	mm_assert(arr);

	return new CScriptArray(arr);
}

IMonoObject *CConverter::ToObject(mono::object obj, bool allowGC)
{
	mm_assert(obj);

	return new CScriptObject((MonoObject *)obj, allowGC);
}