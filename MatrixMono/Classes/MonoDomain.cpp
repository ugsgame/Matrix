
#include "ScriptDebug.h"

#include "MonoDomain.h"

#include "MonoScriptSystem.h"

#include "MonoAssembly.h"
#include "DynMonoArray.h"

#include <MonoCommon.h>

CScriptDomain::CScriptDomain(ERuntimeVersion runtimeVersion)
: m_bRootDomain(true)
, m_bDestroying(false)
, m_name("root")
{
	const char *version = "v2.0.50727";
	switch(runtimeVersion)
	{
	case eRV_2_50215:
		version = "v2.0.50215";
		break;
	case eRV_2_50727:
		break;

	case eRV_4_20506:
		version = "v4.0.20506";
		break;
	case eRV_4_30128:
		version = "v4.0.30128";
		break;
	case eRV_4_30319:
		version = "v4.0.30319";
		break;
	}

	// Crashing on this line is an indicator of mono being incorrectly configured, Make sure Bin(32/64)/mono.exe, Bin(32/64)/mono-2.0.dll & Engine/Mono are up-to-date.
	m_pDomain = mono_jit_init_version("MatrixMono", version);
	if(!m_pDomain)
		mm_error("Failed to initialize root domain with runtime version %s!", version);
}

CScriptDomain::CScriptDomain(const char *name, const char *configurationFile, bool setActive)
: m_bRootDomain(false)
, m_bDestroying(false)
, m_name(name)
{
	m_pDomain = mono_domain_create_appdomain(const_cast<char *>(name), const_cast<char *>(configurationFile));
	mm_assert(m_pDomain);

	if(setActive)
		SetActive();
}

CScriptDomain::~CScriptDomain()
{
	m_bDestroying = true;

	std::vector<CScriptAssembly *>::iterator it; 
	for( it = m_assemblies.begin(); it != m_assemblies.end(); ++it)
		delete *it;
	//for each(CScriptAssembly * assembly in m_assemblies)
	//	delete assembly;

	m_assemblies.clear();

	if(m_bRootDomain)
		mono_jit_cleanup(m_pDomain);
	else
	{
		if(IsActive())
			mono_domain_set(mono_get_root_domain(), false);

		mono_domain_finalize(m_pDomain, 2000);

		MonoObject *pException = NULL;
		try
		{
			//mono_domain_try_unload(m_pDomain, &pException);
			mono_domain_unload(m_pDomain);
		}
		catch(char *ex)
		{
			mm_warning("An exception was raised during ScriptDomain unload: %s", ex);
		}

		if(pException)	
		{	
			mm_warning("An exception was raised during ScriptDomain unload:");
			MonoMethod *pExceptionMethod = mono_method_desc_search_in_class(mono_method_desc_new("::ToString()", false),mono_get_exception_class());		
			MonoString *exceptionString = (MonoString *)mono_runtime_invoke(pExceptionMethod, pException, NULL, NULL);		
			mm_printf(ToMatrixString((mono::string)exceptionString).c_str());
		}
	}

	g_pScriptSystem->OnDomainReleased(this);
}

bool CScriptDomain::SetActive(bool force)
{
	return mono_domain_set(m_pDomain, force) == 1;
}

IMonoAssembly *CScriptDomain::LoadAssembly(void* data , int size, const char* path,void* mdbData,int mdbSize)
{
	int	  status = 0;

	//for each(CScriptAssembly * assembly in m_assemblies)
	std::vector<CScriptAssembly *>::iterator it; 
	for( it = m_assemblies.begin(); it != m_assemblies.end(); ++it)
	{
		CScriptAssembly* assembly = *it;
		if(!strcmp(path, assembly->GetPath()))
			return assembly;
	}

	mm_assert(data != NULL);

	MonoImage*  pImage = mono_image_open_from_data_full ( data, size, /*Copy data*/true, &status, false /* ref only*/ );
	mm_assert( pImage != NULL );

	if(mdbData)mono_debug_open_image_from_memory(pImage,(const char*)mdbData,mdbSize);

	MonoAssembly* pMonoAssembly = mono_assembly_load_from_full ( pImage, path, &status, false/* ref only*/ );
	mm_assert( pMonoAssembly != NULL );

	CScriptAssembly *pAssembly = new CScriptAssembly(this, mono_assembly_get_image(pMonoAssembly), path);
	m_assemblies.push_back(pAssembly);
	
	return pAssembly;
}

IMonoAssembly *CScriptDomain::LoadAssembly(const char *file, void* mdbData ,int mdbSize, bool shadowCopy, bool convertPdbToMdb)
{
	const char *path;

	path = file;
	
	//for each(CScriptAssembly * assembly in m_assemblies)
	std::vector<CScriptAssembly *>::iterator it; 
	for( it = m_assemblies.begin(); it != m_assemblies.end(); ++it)
	{
		CScriptAssembly* assembly = *it;
		if(!strcmp(path, assembly->GetPath()))
			return assembly;
	}

	MonoAssembly *pMonoAssembly = mono_domain_assembly_open(m_pDomain, path);
	mm_assert(pMonoAssembly);

	CScriptAssembly *pAssembly = new CScriptAssembly(this, mono_assembly_get_image(pMonoAssembly), path);
	m_assemblies.push_back(pAssembly);

	if(mdbData)mono_debug_open_image_from_memory(mono_assembly_get_image(pMonoAssembly),(const char*)mdbData,mdbSize);

	return pAssembly;
}

void CScriptDomain::OnAssemblyReleased(CScriptAssembly *pAssembly)
{
	if(!m_bDestroying)
		find_and_erase(m_assemblies, pAssembly);
}


CScriptAssembly *CScriptDomain::TryGetAssembly(MonoImage *pImage)
{
	mm_assert(pImage);

	//for each(CScriptAssembly * assembly in m_assemblies)
	std::vector<CScriptAssembly *>::iterator it; 
	for( it = m_assemblies.begin(); it != m_assemblies.end(); ++it)
	{
		CScriptAssembly* assembly = *it;
		if(assembly->GetImage() == pImage)
			return assembly;
	}

	// This assembly was loaded from managed code.
	CScriptAssembly *pAssembly = new CScriptAssembly(this, pImage, mono_image_get_filename(pImage), false);
	m_assemblies.push_back(pAssembly);

	return pAssembly;
}

IMonoArray *CScriptDomain::CreateArray(int numArgs, IMonoClass *pElementClass, bool allowGC)
{
	return new CScriptArray(m_pDomain, numArgs, pElementClass, allowGC); 
}

IMonoArray *CScriptDomain::CreateDynamicArray(IMonoClass *pElementClass, int size, bool allowGC)
{
	return new CDynScriptArray(m_pDomain, pElementClass, size, allowGC);
}

mono::object CScriptDomain::BoxAnyValue(MonoAnyValue &any)
{
	switch(any.type)
	{
	case eMonoAnyType_Boolean:
		return (mono::object)mono_value_box(m_pDomain, mono_get_boolean_class(), &any.b);
	case eMonoAnyType_Integer:
		return (mono::object)mono_value_box(m_pDomain, mono_get_int32_class(), &any.i);
	case eMonoAnyType_UnsignedInteger:
		return (mono::object)mono_value_box(m_pDomain, mono_get_uint32_class(), &any.u);
	case eMonoAnyType_Short:
		return (mono::object)mono_value_box(m_pDomain, mono_get_int16_class(), &any.i);
	case eMonoAnyType_UnsignedShort:
		return (mono::object)mono_value_box(m_pDomain, mono_get_uint16_class(), &any.u);
	case eMonoAnyType_Float:
		return (mono::object)mono_value_box(m_pDomain, mono_get_single_class(), &any.f);
	case eMonoAnyType_String:
		return (mono::object)CreateMonoString(any.str);
// 	case eMonoAnyType_EntityId:
// 		{
// 			IMonoClass *pEntityIdClass = g_pScriptSystem->GetCryBraryAssembly()->GetClass("EntityId");
// 			return pEntityIdClass->BoxObject(&mono::entityId(any.u), this);
// 		}
// 	case eMonoAnyType_Vec3:
// 		{
// 			IMonoClass *pVec3Class = g_pScriptSystem->GetCryBraryAssembly()->GetClass("Vec3");
// 
// 			Vec3 vec3(any.vec4.x, any.vec4.y, any.vec4.z);
// 			return pVec3Class->BoxObject(&vec3, this);
// 		}
// 		break;
// 	case eMonoAnyType_Quat:
// 		{
// 			IMonoClass *pQuatClass = g_pScriptSystem->GetCryBraryAssembly()->GetClass("Quat");
// 
// 			return pQuatClass->BoxObject(&any.vec4, this);
// 		}
// 		break;
	case eMonoAnyType_Array:
	case eMonoAnyType_Unknown:
		return any.monoObject;
	}

	return NULL;
}