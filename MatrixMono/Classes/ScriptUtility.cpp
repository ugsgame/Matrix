
//#include "stdneb.h"
#include "MonoCommon.h"
#include "ScriptUtility.h"
#include "ScriptDebug.h"

namespace mono
{

	MonoString* Utility_NewMonoString( const std::string& string )
	{
		MonoString* monoStr = mono_string_new_wrapper( string.c_str() );
		mm_assert( NULL!=monoStr );
        
		return monoStr;
	}

	std::string Utility_MonoStringToCppString( MonoString* pMonoStr )
	{
		char* pStr = mono_string_to_utf8( pMonoStr );
		char* str = (char*)malloc(strlen(pStr));
		strcpy(str,pStr);
		std::string cppString(str);
		g_free(pStr);

		return cppString;
	}

	//------------------------------------------------------------------------
	MonoDomain* Utility_CreateChildDomain()
	{
		MonoDomain* newDomain = mono_domain_create_appdomain("Genesis Child Domain", NULL); 
		mm_assert( newDomain!=NULL );
		MonoDomain* old_domain = mono_domain_get(); 
		mm_assert( old_domain!=NULL );
		MonoDomain* rootDomaain = mono_get_root_domain(); 
		mm_assert( rootDomaain!=NULL );
		if(rootDomaain != old_domain)
		{
			mm_error("already have a child domain\n");
		}
		return newDomain;
		
	}
	void Utility_SetChildDomain(MonoDomain* newDomain)
	{
		if (newDomain)
		{
			if (!mono_domain_set (newDomain, false))
			{
				mm_error("Utility_SetChildDomain: mono_domain_set failed\n");
				return;
			}

			if(mono_domain_get() == mono_get_root_domain())
			{
				mm_error("Utility_SetChildDomain: root domain\n");
			}

			return;
		}

		mm_error("Utility_SetChildDomain: newDomain is null\n");
	}

	void Utility_UnloadChildDomain()
	{
		MonoDomain* domain = mono_domain_get();
		if (domain)
		{
			MonoDomain* rootDomain = mono_get_root_domain();
			if (domain != rootDomain)
			{
				//-we can't unload the domain that is running now,so we set the root domain as active domain
				mono_domain_set(rootDomain, false);
				mono_domain_unload(domain);
			}
		}
		mono_gc_collect(mono_gc_max_generation());
	}

	bool FastTestAndConvertUtf16ToAscii( char* destination, gunichar2 const* str, int length )
	{
		gunichar2 const* strEnd = str + length;
		while( str != strEnd ) { //length-- ) {
			if( (*str & ~((gunichar2)0x7f)) != 0 )
				return false;
			*destination = (char)*str;
			++destination;
			++str;
		}
		return true;
	}
    
}