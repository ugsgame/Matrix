
#include "ScriptDebug.h"
#include "MonoObject.h"

#include "MonoDomain.h"
#include "MonoAssembly.h"
#include "MonoClass.h"

#include "MonoScriptSystem.h"


CScriptObject::CScriptObject(MonoObject *pObject, bool allowGC)
: m_pObject(NULL)
{
	SetManagedObject(pObject, allowGC);
}

CScriptObject::~CScriptObject()
{
	// Decrement ref counter in the class, released if no longer used.
	SAFE_RELEASE(m_pClass);

	FreeGCHandle();

	m_pObject = 0;
}

MonoClass *CScriptObject::GetMonoClass() 
{
	MonoClass *pClass = mono_object_get_class(m_pObject);
	mm_assert(pClass);

	return pClass;
}

IMonoClass *CScriptObject::GetClass()
{
	if(m_pClass == NULL)
	{
		if(CScriptDomain *pDomain = g_pScriptSystem->TryGetDomain(mono_object_get_domain(m_pObject)))
		{
			MonoClass *pMonoClass = GetMonoClass();

			if(CScriptAssembly *pAssembly = pDomain->TryGetAssembly(mono_class_get_image(pMonoClass)))
				m_pClass = pAssembly->TryGetClass(pMonoClass);
		}
	}

	mm_assert(m_pClass);
	return m_pClass;
}

EMonoAnyType CScriptObject::GetType()
{
	MonoClass *pMonoClass = GetMonoClass();

	if(pMonoClass == mono_get_byte_class())
		return eMonoAnyType_char;
	else if(pMonoClass==mono_get_boolean_class())
		return eMonoAnyType_Boolean;
	else if(pMonoClass==mono_get_int32_class())
		return eMonoAnyType_Integer;
	else if(pMonoClass==mono_get_uint32_class())
		return eMonoAnyType_UnsignedInteger;
	else if(pMonoClass==mono_get_int16_class())
		return eMonoAnyType_Short;
	else if(pMonoClass==mono_get_uint16_class())
		return eMonoAnyType_UnsignedShort;
	else if(pMonoClass==mono_get_single_class())
		return eMonoAnyType_Float;
	else if(pMonoClass==mono_get_string_class())
		return eMonoAnyType_String;
	else if(pMonoClass == mono_get_array_class())
		return eMonoAnyType_Array;
	else
	{
		const char *className = mono_class_get_name(pMonoClass);
		if(!strcmp(className, "EntityId"))
			return eMonoAnyType_EntityId;
		else if(!strcmp(className, "Vec3"))
			return eMonoAnyType_Vec3;
		else if(!strcmp(className, "Quat"))
			return eMonoAnyType_Quat;
		else
		{
			std::string strClassName = className;

			if(!strcmp(strClassName.erase(strClassName.length() - 2, 2).c_str(), "[]"))
				return eMonoAnyType_Array;
		}
	}

	return eMonoAnyType_Unknown;
}

MonoAnyValue CScriptObject::GetAnyValue()
{
	switch(GetType())
	{
	case eMonoAnyType_char:
		return Unbox<char>();
	case eMonoAnyType_Boolean:
		return Unbox<bool>();
	case eMonoAnyType_Integer:
		return Unbox<int>();
	case eMonoAnyType_UnsignedInteger:
		return Unbox<uint>();
// 	case eMonoAnyType_EntityId:
// 		{
// 			MonoAnyValue value = Unbox<EntityId>();
// 			value.type = eMonoAnyType_EntityId;
// 
// 			return value;
// 		}
	case eMonoAnyType_Short:
		return Unbox<short>();
	case eMonoAnyType_UnsignedShort:
		return Unbox<unsigned short>();
	case eMonoAnyType_Float:
		return Unbox<float>();
// 	case eMonoAnyType_Vec3:
// 		return Unbox<Vec3>();
// 	case eMonoAnyType_Quat:
// 		return Unbox<Quat>();
	case eMonoAnyType_String:
		return ToMatrixString((mono::string)GetManagedObject());
	case eMonoAnyType_Array:
		{
			MonoAnyValue any = MonoAnyValue((mono::object)m_pObject);
			any.type = eMonoAnyType_Array;

			return any;
		}
	case eMonoAnyType_Unknown:
		return MonoAnyValue((mono::object)m_pObject);
	}

	return MonoAnyValue();
}

std::string CScriptObject::ToString()
{
	MonoObject *pException = NULL;

	MonoMethod *method = mono_method_desc_search_in_class(mono_method_desc_new("::ToString()", false), GetMonoClass());
	MonoObject *pResult = mono_runtime_invoke(method, m_pObject, NULL, &pException);

	if(pException)
		HandleException(pException);
	else
		return ToMatrixString((mono::string)pResult);

	return NULL;
}

void CScriptObject::HandleException(MonoObject *pException)
{
	// Fatal errors override disabling the message box option
// TODO
/*
	bool isFatal = g_pMonoCVars->mono_exceptionsTriggerFatalErrors != 0;

	IMonoAssembly *pCryBraryAssembly = ShareMonoScriptSystem()->GetCryBraryAssembly();

	if((g_pMonoCVars->mono_exceptionsTriggerMessageBoxes || isFatal) && pCryBraryAssembly)
	{
		auto args = CreateMonoArray(2);
		args->InsertMonoObject((mono::object)pException);
		args->Insert(isFatal);

		IMonoClass *pDebugClass = pCryBraryAssembly->GetClass("Debug");
		pDebugClass->InvokeArray(NULL, "DisplayException", args);
		SAFE_RELEASE(args);
	}
	else
	{
		auto method = mono_method_desc_search_in_class(mono_method_desc_new("::ToString()", false), mono_get_exception_class());
		auto stacktrace = (MonoString*)mono_runtime_invoke(method, pException, nullptr, nullptr);
		n_warning(ToMatrixString((mono::string)stacktrace));
	}
*/
}

void CScriptObject::SetManagedObject(MonoObject *newObject, bool allowGC)
{
	mm_assert(newObject);

	m_pClass = NULL; // Class pointer is most definitely invalid now, force recollection on next GetClass call.
	m_pObject = newObject;

	// We need this to allow the GC to collect the class object later on.
	if(allowGC)
		m_objectHandle = mono_gchandle_new(m_pObject, allowGC);
	else
		m_objectHandle = -1;
}

void CScriptObject::FreeGCHandle()
{
	if(m_objectHandle != -1)
		mono_gchandle_free(m_objectHandle);
}