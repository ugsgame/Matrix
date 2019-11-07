
#ifndef __C_MONO_SCRIPTSYSTEM_H__
#define __C_MONO_SCRIPTSYSTEM_H__

#include <IMonoScriptSystem.h>
#include <IMonoDomain.h>
#include <IMonoScriptbind.h>

#include <MonoCommon.h>

class CScriptDomain;
class CScriptObject;


class CScriptSystem:public IMonoScriptSystem
{
	typedef std::map<const void *, const char *> TMethodBindings;

public:
	CScriptSystem();

	~CScriptSystem();

	virtual bool IsInitialized(){  return m_pRootDomain != NULL;  }
	//
	virtual bool Reload(){ return false; };

	virtual void Release(){};

	virtual void RegisterPriorityBindings(IMonoScriptBind* scriptBind) ;

	virtual void RegisterMethodBinding(const void *method, const char *fullMethodName) ;

	virtual IMonoAssembly *GetCorlibAssembly();

	virtual IMonoDomain *GetRootDomain()  { return (IMonoDomain *)m_pRootDomain; }
	virtual IMonoDomain *CreateDomain(const char *name, const char *configurationFile = NULL, bool setActive = false);
	virtual IMonoDomain *GetActiveDomain() ;
	virtual IMonoDomain *GetScriptDomain() { return m_pScriptDomain; }

	virtual IMonoConverter *GetConverter()  { return m_pConverter; }

	CScriptDomain *TryGetDomain(MonoDomain *pDomain);
	void OnDomainReleased(CScriptDomain *pDomain);

	IMonoAssembly *GetDebugDatabaseCreator() { return m_pPdb2MdbAssembly; }

	virtual void SetMonoEnvPath(const char* assemblyPath,const char* momoDllPath)
	{
		sMonoPath = momoDllPath;
		sAssemblyPath = assemblyPath;
	}
	//TODO
	virtual bool AddPlugin(){ return true; };

	virtual IMonoAssembly* LoadAssembly(const char* assemblyFile, const char* domainName,void* mdbData = NULL , int mdbSize = 0);

	virtual IMonoAssembly* LoadAssembly(void* assemblyData,int size, const char* domainName,void* mdbData = NULL , int mdbSize = 0);

	virtual bool SetupScriptSystem(bool debug = false);

	virtual bool ShutdownScriptSystem();

protected:

	bool SetupScriptSystem(  std::string& monoDirPaths,
			 std::string& monoConfigsPath,
			 std::string& monoDllPath,
			bool bEnableMonoDebugger);


	bool LoadMonoDll( const std::string& monoDllPath );

	bool UnLoadMonoDll();

	//bool LoadAssemblies( bool needDebug = false);
	//TODO
	bool SetupPlugins(){};
	protected:
		
	const char* sMonoPath;
	const char* sAssemblyPath;
	const char* sConfigsPath;

	bool m_bQuitting;
	bool m_debug;

	// The primary app domain, not really used for anything besides holding the script domain. Do *not* unload this at runtime, we cannot execute another root domain again without restarting.
	CScriptDomain *m_pRootDomain;
	std::vector<CScriptDomain *> m_domains;

	IMonoDomain *m_pScriptDomain;
	IMonoObject *m_pScriptManager;

	IMonoConverter *m_pConverter;

	IMonoAssembly *m_pPdb2MdbAssembly;

	// We temporarily store scriptbind methods here if developers attempt to register them prior to the script system has been initialized properly.
	TMethodBindings m_methodBindings;

	std::vector<IMonoScriptBind *> m_localScriptBinds;
};

extern CScriptSystem *g_pScriptSystem;

#endif