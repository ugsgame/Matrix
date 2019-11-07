/////////////////////////////////////////////////////////////////////////*
//Ink Studios Source File.
//Copyright (C), Ink Studios, 2011.
//////////////////////////////////////////////////////////////////////////
// IMonoClass interface for external projects, i.e. CryGame.
// Represents a managed type.
//////////////////////////////////////////////////////////////////////////
// 18/12/2011 : Created by Filip 'i59' Lundgren
////////////////////////////////////////////////////////////////////////*/
#ifndef __I_MONO_CLASS_H__
#define __I_MONO_CLASS_H__

#include <IMonoObject.h>

struct IMonoAssembly;
struct IMonoArray;

struct IMonoMethod;

/// <summary>
/// Reference to a Mono class, used to call static methods and etc.
/// </summary>
/// <example>
/// IMonoScript *pCryNetwork = GetMonoScriptSystem()->GetCustomClass("CryNetwork", "CryEngine");
/// </example>
struct IMonoClass
	: public IMonoObject
{
public:
	virtual void AddRef() = 0;

	/// <summary>
	/// Gets the class name.
	/// </summary>
	virtual const char *GetName() = 0;

	/// <summary>
	/// Gets the namespace the class resides in.
	/// </summary>
	virtual const char *GetNamespace() = 0;

	/// <summary>
	/// Gets the assembly that declares this type.
	/// </summary>
	virtual IMonoAssembly *GetAssembly() = 0;

	/// <summary>
	/// Instantiates the class, if not already instantiated.
	/// </summary>
	virtual mono::object CreateInstance(IMonoArray *pConstructorParams = NULL) = 0;

	/// <summary>
	/// Boxes a type to be received as a managed type.
	/// </summary>
	virtual mono::object BoxObject(void *object, IMonoDomain *pDomain = NULL) = 0;

	/// <summary>
	/// TODO
	/// </summary>
	virtual mono::object InvokeArray(mono::object object, const char *methodName, IMonoArray *params = NULL, bool throwOnFail = true) = 0;
	/// <summary>
	/// TODO
	/// </summary>
	virtual mono::object Invoke(mono::object object, const char *methodName, void **params = NULL, int numParams = 0, bool throwOnFail = true) = 0;

	/// <summary>
	/// Gets a managed function as a native function pointer. See example for use case.
	/// Much faster than standard invoke!
	/// </summary>
	/// <example>
	/// typedef int (*GetHashCode) (mono::object obj);
	///
	/// GetHashCode func = pObjectClass->GetMethodThunk("GetHashCode", 0);
	/// int hashCode = func(myObject);
	/// </example>
	virtual void *GetMethodThunk(const char *methodName, int numParams) = 0;
	
	/// <summary>
	/// Gets the value of a property in the specified instance.
	/// </summary>
	virtual mono::object GetPropertyValue(mono::object object, const char *propertyName, bool throwOnFail = true) = 0;
	/// <summary>
	/// Sets the value of a property in the specified instance.
	/// </summary>
	virtual void SetPropertyValue(mono::object object, const char *propertyName, mono::object newValue, bool throwOnFail = true) = 0;
	/// <summary>
	/// Gets the value of a field in the specified instance.
	/// </summary>
	virtual mono::object GetFieldValue(mono::object object, const char *fieldName, bool throwOnFail = true) = 0;
	/// <summary>
	/// Sets the value of a field in the specified instance.
	/// </summary>
	virtual void SetFieldValue(mono::object object, const char *fieldName, mono::object newValue, bool throwOnFail = true) = 0;

	/// <summary>
	/// Checks whether this class implements another class by name.
	/// </summary>
	virtual bool ImplementsClass(const char *className, const char *nameSpace = NULL) = 0;
	/// <summary>
	/// Checks whether this class implements an interface by name.
	/// </summary>
	virtual bool ImplementsInterface(const char *interfaceName, const char *nameSpace = NULL, bool bSearchDerivedClasses = true) = 0;
};

#endif //__I_MONO_CLASS_H__