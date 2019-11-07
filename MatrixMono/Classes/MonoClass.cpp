
//#include "stdneb.h"
#include "MonoClass.h"

#include "MonoScriptSystem.h"

#include "MonoDomain.h"
#include "MonoAssembly.h"
#include "MonoArray.h"
#include "MonoObject.h"

#include "MonoException.h"

CScriptClass::CScriptClass(MonoClass *pClass, CScriptAssembly *pDeclaringAssembly)
: m_pDeclaringAssembly(pDeclaringAssembly)
, m_refs(0)
{
	mm_assert(pClass);

	m_pObject = (MonoObject *)pClass; 
	m_objectHandle = -1;
	m_pClass = NULL;

	m_name = std::string(mono_class_get_name(pClass));
	m_namespace = std::string(mono_class_get_namespace(pClass));
}

CScriptClass::~CScriptClass()
{
	m_name.clear();
	m_namespace.clear();
}

void CScriptClass::Release(bool triggerGC) 
{
	if(0 >= --m_refs)
	{
		if(!triggerGC)
			m_objectHandle = -1;

		// Remove this class from the assembly's class registry, and decrement its release counter.
		m_pDeclaringAssembly->OnClassReleased(this);

		// delete class should only be directly done by this method and the CScriptAssembly dtor, otherwise Release.
		delete this;
	}
}

mono::object CScriptClass::CreateInstance(IMonoArray *pConstructorParams)
{
	CScriptDomain *pDomain = static_cast<CScriptDomain *>(GetAssembly()->GetDomain());

	MonoObject *pInstance = mono_object_new(pDomain->GetMonoDomain(), (MonoClass *)m_pObject);

	if(pConstructorParams)
		InvokeArray((mono::object)pInstance, ".ctor", pConstructorParams);
	else
		mono_runtime_object_init(m_pObject);

	return (mono::object)pInstance;
}

mono::object CScriptClass::InvokeArray(mono::object object, const char *methodName, IMonoArray *pParams, bool throwOnFail)
{
	MonoMethod *pMethod = GetMonoMethod(methodName, pParams);
	if(pMethod)
	{
		MonoObject *pException = NULL;
		MonoObject *pResult = mono_runtime_invoke_array(pMethod, object, pParams ? (MonoArray *)pParams->GetManagedObject() : NULL, &pException);

		if(pException)
			HandleException(pException);
		else if(pResult)
			return (mono::object)pResult;
	}
	else if(throwOnFail)
	{
		MM_LOG("System MissingMethodException: Failed to locate method %s in class %s",methodName);
		g_pScriptSystem->GetCorlibAssembly()->GetException("System", "MissingMethodException", "Failed to locate method %s in class %s", methodName, GetName())->Throw();
	}

	return NULL;
}

mono::object CScriptClass::Invoke(mono::object object, const char *methodName, void **pParams, int numParams, bool throwOnFail)
{
	MonoMethod *pMethod = GetMonoMethod(methodName, numParams);
	if(pMethod)
	{
		MonoObject *pException = NULL;
		MonoObject *pResult = mono_runtime_invoke(pMethod, object, pParams, &pException);

		if(pException)
			HandleException(pException);
		else if(pResult)
			return (mono::object)pResult;
	}
	else if(throwOnFail)
		g_pScriptSystem->GetCorlibAssembly()->GetException("System", "MissingMethodException", "Failed to locate method %s in class %s", methodName, GetName())->Throw();

	return NULL;
}

void *CScriptClass::GetMethodThunk(const char *methodName, int numParams)
{
	MonoMethod *pMethod = GetMonoMethod(methodName, numParams);
	if(pMethod)
		//return mono_method_get_unmanaged_thunk(pMethod);
		return NULL;
	else
		g_pScriptSystem->GetCorlibAssembly()->GetException("System", "MissingMethodException", "Failed to locate method %s in class %s", methodName, GetName())->Throw();

	return NULL;
}

MonoMethod *CScriptClass::GetMonoMethod(const char *methodName, IMonoArray *pArgs)
{
	MonoMethodSignature *pSignature = NULL;

	void *pIterator = 0;

	MonoClass *pClass = (MonoClass *)m_pObject;
	MonoType *pClassType = mono_class_get_type(pClass);
	MonoMethod *pCurMethod = NULL;

	int suppliedArgsCount = pArgs ? pArgs->GetSize() : 0;

	while (pClass != NULL)
	{
		pCurMethod = mono_class_get_methods(pClass, &pIterator);
		if(pCurMethod == NULL)
		{
			pClass = mono_class_get_parent(pClass);
			if(pClass == mono_get_object_class())
				break;

			pIterator = 0;
			continue;
		}

		pSignature = mono_method_signature(pCurMethod);
		int signatureParamCount = mono_signature_get_param_count(pSignature);

		bool bCorrectName = !strcmp(mono_method_get_name(pCurMethod), methodName);
		if(bCorrectName && signatureParamCount == 0 && suppliedArgsCount == 0)
			return pCurMethod;
		else if(bCorrectName && signatureParamCount >= suppliedArgsCount)
		{
			//if(bStatic != (mono_method_get_flags(pCurMethod, nullptr) & METHOD_ATTRIBUTE_STATIC) > 0)
			//continue;

			void *pIter = NULL;

			MonoType *pType = NULL;
			for(int i = 0; i < signatureParamCount; i++)
			{
				pType = mono_signature_get_params(pSignature, &pIter);

				if(mono::object item = pArgs->GetItem(i))
				{
					MonoClass *pItemClass = mono_object_get_class((MonoObject *)item);
					MonoType *pItemType = mono_class_get_type(pItemClass);

					MonoTypeEnum itemMonoType = (MonoTypeEnum)mono_type_get_type(pItemType);

					MonoTypeEnum monoType = (MonoTypeEnum)mono_type_get_type(pType);

					if(itemMonoType != monoType)
					{
						// exceptions:
						// Anything can be treated as object.
						if(monoType == MONO_TYPE_OBJECT) {}
						// The runtime confuses things with value types a lot, so ignore parameters that appear with that type.
						else if(itemMonoType == MONO_TYPE_VALUETYPE || monoType == MONO_TYPE_VALUETYPE) {}
						else
						{
							if(MonoClass *pMethodParameterClass = mono_type_get_class(pType))
							{
								mm_warning("Parameter mismatch when searching for method %s in class %s.%s, on parameter %i: Provided type %s.%s does not match expected parameter type %s.%s.", 
									methodName, mono_class_get_namespace(pClass), mono_class_get_name(pClass), i + 1, mono_class_get_namespace(pItemClass), mono_class_get_name(pItemClass), mono_class_get_namespace(pMethodParameterClass), mono_class_get_name(pMethodParameterClass));
							}
							else
							{
								mm_warning("Parameter mismatch when searching for method %s in class %s.%s, on parameter %i: Provided type %s.%s does not match parameter type.", 
									methodName, mono_class_get_namespace(pClass), mono_class_get_name(pClass), i + 1, mono_class_get_namespace(pItemClass), mono_class_get_name(pItemClass));
							}
							break;
						}
					}
				}

				if(i + 1 == suppliedArgsCount)
					return pCurMethod;
			}
		}
	}

	return NULL;
}

MonoMethod *CScriptClass::GetMonoMethod(const char *methodName, int numParams)
{
	MonoMethodSignature *pSignature = NULL;

	void *pIterator = 0;

	MonoClass *pClass = (MonoClass *)m_pObject;
	MonoType *pClassType = mono_class_get_type(pClass);
	MonoMethod *pCurMethod = NULL;

	while (pClass != NULL)
	{
		pCurMethod = mono_class_get_methods(pClass, &pIterator);
		if(pCurMethod == NULL)
		{
			pClass = mono_class_get_parent(pClass);
			if(pClass == mono_get_object_class())
				break;

			pIterator = 0;
			continue;
		}

		pSignature = mono_method_signature(pCurMethod);
		int signatureParamCount = mono_signature_get_param_count(pSignature);

		bool bCorrectName = !strcmp(mono_method_get_name(pCurMethod), methodName);
		if(bCorrectName && signatureParamCount == numParams)
			return pCurMethod;
	}

	return NULL;
}

mono::object CScriptClass::GetPropertyValue(mono::object object, const char *propertyName, bool throwOnFail)
{
	MonoProperty *pProperty = GetMonoProperty(propertyName, false, true);
	if(pProperty)
	{
		MonoObject *pException = NULL;

		MonoObject *propertyValue = mono_property_get_value(pProperty, object, NULL, &pException);

		if(pException)
			HandleException(pException);
		else if(propertyValue)
			return (mono::object)propertyValue;
	}
	else if(throwOnFail)
		g_pScriptSystem->GetCorlibAssembly()->GetException("System", "MissingMemberException", "Failed to locate property %s in class %s", propertyName, GetName())->Throw();

	return NULL;
}

void CScriptClass::SetPropertyValue(mono::object object, const char *propertyName, mono::object newValue, bool throwOnFail)
{
	MonoProperty *pProperty = GetMonoProperty(propertyName, true);
	if(pProperty)
	{
		void *args[1];
		args[0] = newValue;

		mono_property_set_value(pProperty, object, args, NULL);
	}
	else if(throwOnFail)
		g_pScriptSystem->GetCorlibAssembly()->GetException("System", "MissingMemberException", "Failed to locate property %s in class %s", propertyName, GetName())->Throw();
}

mono::object CScriptClass::GetFieldValue(mono::object object, const char *fieldName, bool throwOnFail)
{
	MonoClassField *pField = GetMonoField(fieldName);
	if(pField)
	{
		CScriptDomain *pDomain = static_cast<CScriptDomain *>(GetAssembly()->GetDomain());

		MonoObject *fieldValue = mono_field_get_value_object(pDomain->GetMonoDomain(), pField, (MonoObject *)object);

		if(fieldValue)
			return (mono::object)fieldValue;
	}
	else if(throwOnFail)
		g_pScriptSystem->GetCorlibAssembly()->GetException("System", "MissingFieldException", "Failed to locate field %s in class %s", fieldName, GetName())->Throw();

	return NULL;
}

void CScriptClass::SetFieldValue(mono::object object, const char *fieldName, mono::object newValue, bool throwOnFail)
{
	MonoClassField *pField = GetMonoField(fieldName);
	if(pField)
		mono_field_set_value((MonoObject *)(object), pField, newValue);
	else if(throwOnFail)
		g_pScriptSystem->GetCorlibAssembly()->GetException("System", "MissingFieldException", "Failed to locate field %s in class %s", fieldName, GetName())->Throw();
}

MonoProperty *CScriptClass::GetMonoProperty(const char *name, bool requireSetter, bool requireGetter)
{
	MonoClass *pClass = (MonoClass *)m_pObject;
	MonoProperty *pCurProperty = NULL;

	void *pIterator = 0;

	while (pClass != NULL)
	{
		pCurProperty = mono_class_get_properties(pClass, &pIterator);
		if(pCurProperty == NULL)
		{
			pClass = mono_class_get_parent(pClass);
			if(pClass == mono_get_object_class())
				break;
			pIterator = 0;
			continue;
		}

		if(!strcmp(mono_property_get_name(pCurProperty), name))
		{
			if(requireSetter && mono_property_get_set_method(pCurProperty) == NULL)
				continue;
			if(requireGetter && mono_property_get_get_method(pCurProperty) == NULL)
				continue;

			return pCurProperty;
		}
	}

	return NULL;
}

MonoClassField *CScriptClass::GetMonoField(const char *name)
{
	MonoClass *pClass = (MonoClass *)m_pObject;
	MonoClassField *pCurField = NULL;

	void *pIterator = 0;

	while (pClass != NULL)
	{
		pCurField = mono_class_get_fields(pClass, &pIterator);
		if(pCurField == NULL)
		{
			pClass = mono_class_get_parent(pClass);
			if(pClass == mono_get_object_class())
				break;

			pIterator = 0;
			continue;
		}

		if(!strcmp(mono_field_get_name(pCurField), name))
			return pCurField;
	}

	return NULL;
}

mono::object CScriptClass::BoxObject(void *object, IMonoDomain *pDomain)
{
	if(pDomain == NULL)
		pDomain = g_pScriptSystem->GetActiveDomain();

	return (mono::object)mono_value_box(static_cast<CScriptDomain *>(pDomain)->GetMonoDomain(), (MonoClass *)m_pObject, object);
}

bool CScriptClass::ImplementsClass(const char *className, const char *nameSpace)
{
	MonoClass *pClass = (MonoClass *)m_pObject;

	while (pClass != NULL)
	{
		if(!strcmp(mono_class_get_name(pClass), className) 
			&& (nameSpace == NULL || !strcmp(mono_class_get_namespace(pClass), nameSpace)))
			return true;
		else
			mm_printf("%s did not match pattern %s", mono_class_get_name(pClass), className);

		pClass = mono_class_get_parent(pClass);
		if(pClass == mono_get_object_class())
			break;
	}

	return false;
}

bool CScriptClass::ImplementsInterface(const char *interfaceName, const char *nameSpace, bool bSearchDerivedClasses)
{
	void *pIterator = 0;

	MonoClass *pClass = (MonoClass *)m_pObject;

	while (pClass != NULL)
	{
		MonoClass *pCurInterface = mono_class_get_interfaces(pClass, &pIterator);
		if(pCurInterface == NULL)
		{
			if(!bSearchDerivedClasses)
				return false;

			pClass = mono_class_get_parent(pClass);
			if(pClass == mono_get_object_class())
				break;

			pIterator = 0;
			continue;
		}

		if(!strcmp(mono_class_get_name(pCurInterface), interfaceName) 
			&& (nameSpace == NULL || !strcmp(mono_class_get_namespace(pCurInterface), nameSpace)))
			return true;
	}

	return false;
}