/////////////////////////////////////////////////////////////////////////*
//Ink Studios Source File.
//Copyright (C), Ink Studios, 2012.
//////////////////////////////////////////////////////////////////////////
// IMonoDomain interface to handle script / app domains.
//////////////////////////////////////////////////////////////////////////
// 03/05/2012 : Created by Filip 'i59' Lundgren
////////////////////////////////////////////////////////////////////////*/
#ifndef __SCRIPT_DOMAIN_H_
#define __SCRIPT_DOMAIN_H_

#include "MonoCommon.h"
#include "IMonoDomain.h"

class CScriptAssembly;

class CScriptDomain : public IMonoDomain
{
public:
	// Create root domain
	CScriptDomain(ERuntimeVersion runtimeVersion = eRV_2_50727);
	CScriptDomain(const char *name, const char *configurationFile, bool setActive = false);
	CScriptDomain(MonoDomain *pMonoDomain) : m_pDomain(pMonoDomain), m_bRootDomain(false), m_name("<unknown>") {}
	~CScriptDomain();

	// IMonoDomain
	virtual void Release()  { delete this; }

	virtual bool SetActive(bool force = false)  ;
	virtual bool IsActive()  { return m_pDomain == mono_domain_get(); }

	virtual bool IsRoot()  { return m_bRootDomain; }

	virtual IMonoAssembly *LoadAssembly(void* data, int size, const char* path,void* mdbData = NULL,int mdbSize = 0) ;
	virtual IMonoAssembly *LoadAssembly(const char *file,void* mdbData = NULL, int mdbSize = 0,bool shadowCopy = false, bool convertPdbToMdb = true) ;

	virtual const char *GetName()  { return m_name; }

	virtual IMonoArray *CreateArray(int size, IMonoClass *pElementClass = NULL, bool allowGC = true) ;
	virtual IMonoArray *CreateDynamicArray(IMonoClass *pElementClass = NULL, int size = 0, bool allowGC = true) ;
	
	virtual mono::object BoxAnyValue(MonoAnyValue &any) ;

	virtual mono::string CreateMonoString(const char *cStr)  { return (mono::string)mono_string_new(m_pDomain, cStr); }
	// ~IMonoDomain

	MonoDomain *GetMonoDomain() { return m_pDomain; }

	CScriptAssembly *TryGetAssembly(MonoImage *pImage);
	void OnAssemblyReleased(CScriptAssembly *pAssembly);

private:
	MonoDomain *m_pDomain;

	std::vector<CScriptAssembly *> m_assemblies;

	bool m_bRootDomain;
	bool m_bDestroying;

	const char *m_name;
};

#endif //__SCRIPT_DOMAIN_H_