/////////////////////////////////////////////////////////////////////////*
//Ink Studios Source File.
//Copyright (C), Ink Studios, 2011.
//////////////////////////////////////////////////////////////////////////
// Wrapper for the MonoClass for less intensively ugly code and
// better workflow.
//////////////////////////////////////////////////////////////////////////
// 17/12/2011 : Created by Filip 'i59' Lundgren
////////////////////////////////////////////////////////////////////////*/
#ifndef __MONO_CLASS_H__
#define __MONO_CLASS_H__

#include "MonoObject.h"
#include "MonoAssembly.h"

#include <MonoCommon.h>
#include <IMonoClass.h>

struct IMonoArray;


class CScriptClass 
	: public CScriptObject
	, public IMonoClass
{
public:
	CScriptClass(MonoClass *pClass, CScriptAssembly *pDeclaringAssembly);
	virtual ~CScriptClass();

	// IMonoClass
	virtual const char *GetName()  { return m_name.c_str(); }
	virtual const char *GetNamespace()  { return m_namespace.c_str(); }

	virtual IMonoAssembly *GetAssembly() { return m_pDeclaringAssembly; }

	virtual mono::object CreateInstance(IMonoArray *pConstructorParams = NULL) ;

	mono::object BoxObject(void *object, IMonoDomain *pDomain = NULL) ;

	virtual void AddRef()  { ++m_refs; }

	virtual mono::object InvokeArray(mono::object pObject, const char *methodName, IMonoArray *params = NULL, bool throwOnFail = true) ;
	virtual mono::object Invoke(mono::object pObject, const char *methodName, void **params = NULL, int numParams = 0, bool throwOnFail = true) ;

	virtual void *GetMethodThunk(const char *methodName, int numParams) ;

	virtual mono::object GetPropertyValue(mono::object pObject, const char *propertyName, bool throwOnFail = true) ;
	virtual void SetPropertyValue(mono::object pObject, const char *propertyName, mono::object newValue, bool throwOnFail = true) ;
	virtual mono::object GetFieldValue(mono::object pObject, const char *fieldName, bool throwOnFail = true) ;
	virtual void SetFieldValue(mono::object pObject, const char *fieldName, mono::object newValue, bool throwOnFail = true) ;
	
	virtual bool ImplementsClass(const char *className, const char *nameSpace = NULL) ;
	virtual bool ImplementsInterface(const char *interfaceName, const char *nameSpace = NULL, bool bSearchDerivedClasses = true) ;
	// ~IMonoClass

	// IMonoObject
	virtual void Release(bool triggerGC = true) ;

	virtual EMonoAnyType GetType()  { return eMonoAnyType_Unknown; }
	virtual MonoAnyValue GetAnyValue()  { return MonoAnyValue(); }

	virtual mono::object GetManagedObject()  { return CScriptObject::GetManagedObject(); }

	virtual IMonoClass *GetClass()  { return this; }

	virtual void *UnboxObject()  { return CScriptObject::UnboxObject(); }

	virtual std::string ToString()  { return CScriptObject::ToString(); }
	// ~IMonoObject

private:
	MonoMethod *GetMonoMethod(const char *name, IMonoArray *pArgs);
	MonoMethod *GetMonoMethod(const char *name, int numParams);

	MonoProperty *GetMonoProperty(const char *name, bool requireSetter = false, bool requireGetter = false);
	MonoClassField *GetMonoField(const char *name);

	std::string m_name;
	std::string m_namespace;

	CScriptAssembly *m_pDeclaringAssembly;

	int m_refs;
};

#endif //__MONO_CLASS_H__