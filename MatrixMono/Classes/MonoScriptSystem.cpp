
//#include "stdneb.h"
#include <stdlib.h>
#include <stdio.h>

//#include "util/stringconvert.h"
//#include "io/stream.h"
//#include "io/ioserver.h"
//#include "io/memorystream.h"
//#include "io/iointerface.h"
//#include "io/iointerfaceprotocol.h"

#include "MonoScriptSystem.h"
#include "MonoArray.h"
#include "MonoAssembly.h"
#include "MonoDomain.h"
#include "MonoConverter.h"

#include "ScriptBind.h"
#include "ScriptUtility.h"
#include "ScriptDebug.h"

// #include "ScriptBind_Actor.h"
// #include "ScriptBind_TNode.h"

using namespace mono;

#ifdef  WIN32
//#include "WinBase.h" // - for LoadLibrary and GetProcAddress
#include<windows.h> 
static HMODULE s_monoDllHandle = NULL;
#elif defined(ANDROID)
#include "dlfcn.h" // - for dlopen and dlsym
static void* s_monoSOHandle;
#endif

#define MONO_API_TYPE(ret, fun, params)	typedef ret (*_fp_mono_##fun) params;
#define MONO_API_DECL(ret,fun, params)	_fp_mono_##fun fun = NULL;
#define GET_MONO_API(ret, fun, params)	fun = (_fp_mono_##fun) GetProcAddress(hMonoModule, #fun); if( NULL==fun ) { mm_assert(false && "can't find " #fun " in mono dll"); }
#define GET_MONO_API_ANDROID(ret, fun, params)	fun = (_fp_mono_##fun) dlsym(hMonoModule, #fun); if( NULL==fun ) { mm_assert(false && "can't find " #fun " in mono so"); }
#define RESET_MONO_API(ret,fun,params)	fun = NULL;

// - make a typedef for each function pointer
#ifndef __OSX__
ALL_MONO_API(MONO_API_TYPE)
ALL_MONO_API(MONO_API_DECL)
#endif

CScriptSystem *g_pScriptSystem = 0;
IMonoScriptSystem *IMonoScriptSystem::g_pThis = 0;


static std::string g_LastError = "";

char* OpenFile(const char* fn,int& size)
{
	FILE* fp = fopen(fn, "rb");

	if (!fp)
	{
		mm_printf("can't open file:%s",fn);
		return NULL;
	}
	fseek(fp, 0, SEEK_END);
	size = ftell(fp);
	fseek(fp, 0, SEEK_SET);
	char* data = (char*)malloc(size);
	fread(data, 1, size, fp);
	fclose(fp);
	return data;
}

#ifdef WIN32
SizeT UTF8ToWide(const char* src, ushort* dst, SizeT dstMaxBytes)
{
	mm_assert((0 != src) && (0 != dst) && (dstMaxBytes > 2) && ((dstMaxBytes & 1) == 0));
	SizeT numConv = MultiByteToWideChar(CP_UTF8, 0, src, -1, (LPWSTR) dst, (dstMaxBytes / 2) - 1);
	if (numConv > 0)
	{
		dst[numConv] = 0;
		return numConv;
	}
	else
	{
		DWORD errCode = GetLastError();
		mm_error("UTF8ToWide() failed to convert string '%s' to wide char! ErrorCode %d", src, errCode);
		return 0;
	}
}

#endif

CScriptSystem::CScriptSystem()
	 : sMonoPath(NULL)
	 , sAssemblyPath(NULL)
	 , m_pRootDomain(NULL)
	 , m_pPdb2MdbAssembly(NULL)
	 , m_pScriptManager(NULL)
	 , m_pScriptDomain(NULL)
	 , m_bQuitting(false)
	 , m_pConverter(NULL)
{
	g_pThis = this;
	g_pScriptSystem = this;

	m_pConverter = new CConverter();

}

CScriptSystem::~CScriptSystem()
{
	g_pThis = NULL;
	g_pScriptSystem = NULL;
}

void CScriptSystem::RegisterPriorityBindings(IMonoScriptBind* scriptBind)
{
	mm_assert(scriptBind != NULL);
	m_localScriptBinds.push_back(scriptBind);
}

void CScriptSystem::RegisterMethodBinding(const void *method, const char *fullMethodName)
{
	if(!IsInitialized())
		m_methodBindings.insert(TMethodBindings::value_type(method, fullMethodName));
	else
		mono_add_internal_call(fullMethodName, method);
}

IMonoAssembly *CScriptSystem::GetCorlibAssembly()
{
	return m_pRootDomain->TryGetAssembly(mono_get_corlib());
}

IMonoDomain *CScriptSystem::CreateDomain(const char *name, const char *configurationFile, bool setActive)
{
	CScriptDomain *pDomain = new CScriptDomain(name, configurationFile, setActive);
	m_domains.push_back(pDomain);

	return pDomain;
}

IMonoDomain *CScriptSystem::GetActiveDomain()
{
	MonoDomain *pActiveDomain = mono_domain_get();
	if(pActiveDomain == NULL)
	{
		MM_LOG("Waring:No Active Domain");
		return NULL;
	}

	return TryGetDomain(pActiveDomain);
}

CScriptDomain *CScriptSystem::TryGetDomain(MonoDomain *pMonoDomain)
{
	mm_assert(pMonoDomain);

	std::vector<CScriptDomain *>::iterator it; 
	for( it = m_domains.begin(); it != m_domains.end(); ++it)
	//for each(CScriptDomain* domain in m_domains)
	{
		CScriptDomain* domain = *it;
		if(domain->GetMonoDomain() == pMonoDomain)
			return domain;
	}

	CScriptDomain *pDomain = new CScriptDomain(pMonoDomain);
	m_domains.push_back(pDomain);

	return pDomain;
}

void CScriptSystem::OnDomainReleased(CScriptDomain *pDomain)
{
	if(!m_bQuitting)
		find_and_erase(m_domains, pDomain);
}

IMonoAssembly* CScriptSystem::LoadAssembly(const char* assemblyFile, const char* domainName,void* mdbData , int mdbSize)
{

	IMonoDomain *pScriptDomain = CreateDomain(domainName, NULL, true);
	IMonoAssembly *pMatrixAssembly = NULL;

	if(this->m_debug)
	 {
		if(!mdbData)mm_printf("No mdb file found! \n");
		pMatrixAssembly = pScriptDomain->LoadAssembly(assemblyFile,mdbData,mdbSize);
	 }
	else
		pMatrixAssembly = pScriptDomain->LoadAssembly(assemblyFile);

	if(!pMatrixAssembly)mm_printf("LoadAssembly failed %s \n",assemblyFile);
	mm_assert(pMatrixAssembly);

	return pMatrixAssembly;
}

IMonoAssembly* CScriptSystem::LoadAssembly(void* assemblyData,int size, const char* domainName,void* mdbData, int mdbSize)
{
	mm_assert(assemblyData);

	IMonoDomain *pScriptDomain = CreateDomain(domainName, NULL, true);
	IMonoAssembly *pMatrixAssembly = NULL;

	if(this->m_debug)
	 {
		if(!mdbData)mm_printf("No mdb file found! \n");
		pMatrixAssembly = pScriptDomain->LoadAssembly( assemblyData, size, domainName,mdbData,mdbSize);
	 }
	else
		pMatrixAssembly = pScriptDomain->LoadAssembly( assemblyData, size, domainName);

	mm_assert(pMatrixAssembly);
	return pMatrixAssembly;
}

bool CScriptSystem::SetupScriptSystem(bool debug)
{
	this->m_debug = debug;
	std::string str1 = sAssemblyPath;
	std::string str2 = "";
	std::string str3 = sMonoPath;
	return this->SetupScriptSystem(str1,str2,str3,debug);
}

bool CScriptSystem::SetupScriptSystem(  std::string& monoDirPaths,
										 std::string& monoConfigsPath,
										 std::string& monoDllPath,
										bool bEnableMonoDebugger)
{
        //setenv(<#const char *#>, <#const char *#>, <#int#>)
#ifdef __OSX__
        RegisterMonoModules();
#endif        
		bool bLoadDll = LoadMonoDll( monoDllPath );
		mm_assert( bLoadDll );
		// - set mono dll search path
		mono_set_assemblies_path( monoDirPaths.c_str());

		if ( bEnableMonoDebugger )
		{
			//mono_set_signal_chaining(true);
			int port;
#ifdef WIN32
			port = port = (int)(56000 + (GetCurrentProcessId() % 1000)); 
#elif defined(ANDROID)
			port = port = (int)(56000 + (getpid() % 1000)); 
#endif
			char options[255];  
			sprintf(options,"--debugger-agent=transport=dt_socket,address=127.0.0.1:%d,server=y", port);  
			char* args = options;  
			//TODO:现在只能依赖Unity的调试器才能断点单步调试c#
#ifdef DEBUG_UNITY
			mm_printf( "Script system is in debug state!" );
			mono_jit_parse_options (1, &args);  
#endif // DEBUG
			mono_debug_init (1);  


		    //mm_printf( "Script system is in debug state!" );
		}
		// - init mono
		m_pRootDomain = new CScriptDomain(eRV_2_50727);
		m_domains.push_back(m_pRootDomain);

		CScriptArray::m_pDefaultElementClass = mono_get_object_class();

		if ( bEnableMonoDebugger )
		{
			// - The mono soft debugger needs this
			mono_thread_set_main( mono_thread_current() );
		}

		// - create a child domain,and run in this child domain
#ifndef __OSX__
        MonoDomain* domain = Utility_CreateChildDomain();
		script_fatal_error( NULL!=domain );
		Utility_SetChildDomain( domain );
#endif
		return true;
}

//TODO
bool CScriptSystem::ShutdownScriptSystem()
{
	return true;
}

bool CScriptSystem::LoadMonoDll( const std::string& monoDllPath )
{

#ifdef WIN32
		// - dynamic load mono dll
		char16_t path[512] = {0};
		UTF8ToWide(monoDllPath.c_str(), path, 512);
		HMODULE hMonoModule = LoadLibraryW( (wchar_t*)path );
		if (!hMonoModule)
		{
			g_LastError = monoDllPath + " not found!";
			throw(0);
		}

		mm_printf("mono dll path: %s\n",monoDllPath.c_str());
		mm_assert( NULL!=hMonoModule );
		
		// - save this handle
		s_monoDllHandle = hMonoModule;
        
		// - assign value to the function pointer which we define before

        ALL_MONO_API(GET_MONO_API)
        
#elif defined(ANDROID)
        // - dynamic load mono dll
        const char* loadInfo = NULL;
       
        void* hMonoModule = dlopen( (char*)monoDllPath.c_str() ,RTLD_LAZY);
        mm_printf("\n monodllpath: %s\n",monoDllPath.c_str());
        loadInfo = dlerror();

        if(NULL != loadInfo)
		{
			mm_printf("\n load_error: %s\n",loadInfo);
		}
        else
		{
			mm_printf("\n load_successful\n");
		}
        
        mm_printf("\n monodllpath: %s\n",monoDllPath.c_str());
        mm_assert( NULL!=hMonoModule );
        
        // - save this handle
        s_monoSOHandle = hMonoModule;
        
        // - assign value to the function pointer which we define before
        ALL_MONO_API(GET_MONO_API_ANDROID)
#endif
		return true;
}	

bool CScriptSystem::UnLoadMonoDll()
{
	return true;
}
/*
bool CScriptSystem::LoadAssemblies(bool needDebug)
{
	//GPtr<IO::Stream> srcStream = IO::IoServer::Instance()->ReadFile( "D:/Dev/Matrix_v0.0.2/Matrix/Resources/Script" );

#ifndef __OSX__
		MonoDomain* pDomain = mono_domain_get();
		script_fatal_error( NULL != pDomain );
		MonoDomain* pRootDomain = mono_get_root_domain();
		script_fatal_error( NULL != pRootDomain );

		//-a child domain must hane been ceated before loading the assemblies;
		script_fatal_error( pDomain!=pRootDomain );
#endif

	void* dllData = NULL;
	int	  dataSize = 0;
 	MonoImage* pImage = NULL;
	int status = 0;

	const char* path = "D:/Dev/Matrix_v0.0.2/Matrix/Resources/Script/TestLibrary.dll";

	dllData = OpenFile(path,dataSize);

	if(dllData)
	{
		pImage = mono_image_open_from_data_full ( dllData, dataSize, true, &status, false );
        script_fatal_error( pImage != NULL );

		MonoAssembly* pAssembly = mono_assembly_load_from_full ( pImage, path, &status, false );
		script_fatal_error( pAssembly != NULL );

		if ( mono_assembly_get_image( pAssembly )!=pImage )
		{
			n_error( "this assemble has already been loaded" );
            //n_printf( "this assemble has already been loaded" );
		}
		
		MonoClass* klass;
		//MonoObject* obj;
		MonoMethod* start = NULL,*m = NULL;

		klass = mono_class_from_name(pImage,"ScriptTest","MainClass");

		start = mono_class_get_method_from_name(klass,"Start",0);
		
		mono_runtime_invoke (start, NULL, NULL, NULL);
	}

	n_printf("load! \n");
	return true;
}
*/