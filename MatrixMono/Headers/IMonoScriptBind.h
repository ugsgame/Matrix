/////////////////////////////////////////////////////////////////////////*
//Ink Studios Source File.
//Copyright (C), Ink Studios, 2011.
//////////////////////////////////////////////////////////////////////////
// IMonoScriptBind interface for external projects, i.e. CryGame.
//////////////////////////////////////////////////////////////////////////
// 20/11/2011 : Created by Filip 'i59' Lundgren (Based on version by ins\)
////////////////////////////////////////////////////////////////////////*/
#ifndef __IMONOSCRIPTBIND_H__
#define __IMONOSCRIPTBIND_H__

#include <IMonoScriptSystem.h>
#include <IMonoAssembly.h>
#include <string>

/// <summary>
/// Simple pre-processor method used to quickly register methods within scriptbinds.
/// </summary>
#define REGISTER_METHOD(method) ShareMonoScriptSystem()->RegisterMethodBinding(method, (GetNamespace() + (std::string)".").append(GetClassName()).append("::").append(#method).c_str())

#define REGISTER_METHOD_NAME(method, name) ShareMonoScriptSystem()->RegisterMethodBinding(method, (GetNamespace() + (std::string)".").append(GetClassName()).append("::").append(name).c_str())

/// <summary>
/// </summary>
struct IMonoScriptBind
{
	virtual ~IMonoScriptBind() {}

	/// <summary>
	/// The namespace in which the Mono class this scriptbind is tied to resides in; returns "CryEngine.Native" by default if not overridden.
	/// </summary>
	virtual const char *GetNamespace() { return "MatrixEngine.Native"; }
	/// <summary>
	/// The Mono class which this scriptbind is tied to. Unlike GetNameSpace and GetNameSpaceExtension, this has no default value and MUST be set.
	/// </summary>
	virtual const char *GetClassName() = 0;
};

#endif //__IMONOSCRIPTBIND_H__