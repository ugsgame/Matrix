
#include "ScriptDebug.h"

#include "MonoAssembly.h"

#include "MonoScriptSystem.h"
#include "MonoDomain.h"
#include "MonoException.h"

#include <MonoClass.h>

CScriptAssembly::CScriptAssembly(CScriptDomain *pDomain, MonoImage *pImage, const char *path, bool nativeAssembly)
: m_pDomain(pDomain)
, m_bNative(nativeAssembly) // true if this assembly was loaded via C++.
, m_bDestroying(false)
, m_pClass(NULL)
{
	mm_assert(pImage);
	m_pImage = pImage;
	m_pClass = NULL;

	m_path = std::string(path);
}

CScriptAssembly::~CScriptAssembly()
{
	m_bDestroying = true;

	std::vector<CScriptClass *>::iterator it;  
	for(it = m_classes.begin(); it != m_classes.end(); ++it)
		delete *it;

	m_classes.clear();

	m_pImage = NULL;
}

void CScriptAssembly::Release(bool triggerGC)
{
	if(m_classes.empty())
	{
		m_pDomain->OnAssemblyReleased(this);
		// delete assembly should only be directly done by this method and the CScriptDomain dtor, otherwise Release.
		delete this;
	}
}

void CScriptAssembly::OnClassReleased(CScriptClass *pClass)
{
	if(!m_bDestroying)
		find_and_erase(m_classes, pClass);
}

IMonoClass *CScriptAssembly::GetClass()
{
	if(m_pClass == NULL)
	{
		if(CScriptDomain *pDomain = g_pScriptSystem->TryGetDomain(mono_object_get_domain((MonoObject *)m_pImage)))
		{
			MonoClass *pMonoClass = mono_object_get_class((MonoObject *)m_pImage);

			if(CScriptAssembly *pAssembly = pDomain->TryGetAssembly(mono_class_get_image(pMonoClass)))
				m_pClass = pAssembly->TryGetClass(pMonoClass);
		}
	}

	mm_assert(m_pClass);
	return m_pClass;
}

IMonoClass *CScriptAssembly::GetClass(const char *className, const char *nameSpace)
{ 
	if(MonoClass *monoClass = mono_class_from_name(m_pImage, nameSpace, className))
		return TryGetClass(monoClass);

	mm_warning("Failed to get class %s.%s", nameSpace, className);
	return NULL;
}

CScriptClass *CScriptAssembly::TryGetClass(MonoClass *pMonoClass)
{
	mm_assert(pMonoClass);

	//for each(CScriptClass* pClass in m_classes)
	std::vector<CScriptClass *>::iterator it;  
	for( it = m_classes.begin(); it != m_classes.end(); ++it)
	{
		CScriptClass* pClass = *it;
		if((MonoClass *)pClass->GetManagedObject() == pMonoClass)
		{
			pClass->AddRef();
			return pClass;
		}
	}

	CScriptClass *pScriptClass = new CScriptClass(pMonoClass, this);
	m_classes.push_back(pScriptClass);
	pScriptClass->AddRef();

	return pScriptClass;
}

IMonoException *CScriptAssembly::_GetException(const char *nameSpace, const char *exceptionClass, const char *message)
{
	MonoException *pException;
	if(message != NULL)
		pException = mono_exception_from_name_msg(m_pImage, nameSpace, exceptionClass, message);
	else
		pException = mono_exception_from_name(m_pImage, nameSpace, exceptionClass);

	return new CScriptException(pException);
}

mono::object CScriptAssembly::GetManagedObject() 
{
	CScriptDomain *pDomain = static_cast<CScriptDomain *>(GetDomain());

	return (mono::object)mono_assembly_get_object(pDomain->GetMonoDomain(), mono_image_get_assembly(m_pImage)); 
}