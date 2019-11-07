

#ifndef __I_MONO_SCRIPTSYSTEM_H__
#define __I_MONO_SCRIPTSYSTEM_H__

#include "MonoTypes.h"

struct IMonoObject;
struct IMonoArray;
struct IMonoClass;
struct IMonoAssembly;
struct IMonoDomain;
struct IMonoScriptBind;

struct IMonoConverter;


struct IMonoScriptSystem
{
	/// <summary>
	/// Returns true when the root domain has been initialized.
	/// </summary>
	virtual bool IsInitialized() = 0;

	/// <summary>
	/// Used to start script recompilation / serialization.
	/// </summary>
	/// <returns>True if successful, False if aborted.</returns>
	virtual bool Reload() = 0;

	/// <summary>
	/// Deletes script system instance; cleans up mono objects etc.
	/// Called from the dll which implements CryMono on engine shutdown (CGameStartup destructor within the sample project)
	/// </summary>
	virtual void Release() = 0;

	///
	///
	///
	virtual void RegisterPriorityBindings(IMonoScriptBind* scriptBind) = 0;

	/// <summary>
	/// Registers a method binding, called from IMonoScriptBind.
	/// </summary>
	/// <param name="fullMethodName">i.e. "CryEngine.GameRulesSystem::GetPlayerId"</param>
	virtual void RegisterMethodBinding(const void *method, const char *fullMethodName) = 0;

	/// <summary>
	/// Gets the core assembly, containing the System namespace etc.
	/// </summary>
	virtual IMonoAssembly *GetCorlibAssembly() = 0;

	/// <summary>
	/// Gets the root domain created on script system initialization.
	/// </summary>
	virtual IMonoDomain *GetRootDomain() = 0;

	/// <summary>
	/// Creates a new app domain.
	/// </summary>
	virtual IMonoDomain *CreateDomain(const char *name, const char *configurationFile = NULL, bool setActive = false) = 0;

	/// <summary>
	/// Gets the currently active app domain.
	/// </summary>
	virtual IMonoDomain *GetActiveDomain() = 0;

	/// <summary>
	/// Gets the domain in which scripts are stored and executed.
	/// </summary>
	virtual IMonoDomain *GetScriptDomain() = 0;

	/// <summary>
	/// Retrieves an instance of the IMonoConverter; a class used to easily convert C# types to C++ and the other way around.
	/// </summary>
	virtual IMonoConverter *GetConverter() = 0;

	virtual void SetMonoEnvPath(const char* assemblyPath,const char* momoDllPath) = 0;

	virtual bool SetupScriptSystem(bool debug = false) = 0;

	virtual bool ShutdownScriptSystem() = 0;

	virtual bool AddPlugin() = 0;

	virtual IMonoAssembly* LoadAssembly(const char* assemblyFile, const char* domainName ,void* mdbData = NULL , int mdbSize = 0) = 0;

	virtual IMonoAssembly* LoadAssembly(void* assemblyData,int size, const char* domainName,void* mdbData = NULL , int mdbSize = 0) = 0;

	// internal storage to avoid having the extra overhead from having to call GetPluginByName all the time.
	static IMonoScriptSystem *g_pThis;
};

static void SetShareMonoScriptSystem(IMonoScriptSystem* sys)
{
	IMonoScriptSystem::g_pThis = sys;
}

static IMonoScriptSystem *ShareMonoScriptSystem()
{
	return IMonoScriptSystem::g_pThis;
}

#define RegisterBinding(T) ShareMonoScriptSystem()->RegisterPriorityBindings(new T());


#endif