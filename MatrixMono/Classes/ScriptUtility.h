
#ifndef __SCRIPT_UTILITY_H__
#define __SCRIPT_UTILITY_H__

//#include "util/string.h"
#include <string>
#include "MonoAPI.h"
#include "ScriptBind.h"

// - define some mono function by macro
#define mono_array_addr(array,type,index) ((type*)(void*) mono_array_addr_with_size (array, sizeof (type), index))
#define mono_array_get(array,type,index) ( *(type*)mono_array_addr ((array), type, (index)) ) 
#define mono_array_set(array,type,index,value)	\
	do {	\
	type *__p = (type *) mono_array_addr ((array), type, (index));	\
	*__p = (value);	\
	} while (false)

namespace mono
{
	// Mono Object has private data offset when get C# object. See Mono Source Code
	enum {
		MonoObjectMemoryOffset = 2 * sizeof(void*),
		MonoArrayMemoryOffset = 4 * sizeof(void*) 
	};

	/// structure in front of a C# object
	struct DataOnHead
	{
		void*       __CppObjPtr;
	};

	struct InternalCallDetail
	{
		char* __pScriptFunName;
		void* __pCppFunPtr;
	};
	/// extract a stucture from a MonoObject
	inline DataOnHead* GetDataOnHead(MonoObject* wrapper)
	{
		return reinterpret_cast<DataOnHead*> (((char*)wrapper) + MonoObjectMemoryOffset);
	}

	/// get cpp object pointer from a script object
	inline void* Utility_GetCppObjectPtr( MonoObject* wrapper )
	{
		return GetDataOnHead(wrapper)->__CppObjPtr;
	}

	/// bind a cpp object with a script object
	inline void Utility_SetCppObjectPtr(MonoObject* wrapper, void* pCppObj )
	{
		GetDataOnHead(wrapper)->__CppObjPtr = pCppObj;
	}

	//------------------------------------------------------------------------
	MonoString* Utility_NewMonoString( const std::string& string );

	//------------------------------------------------------------------------
	// 尽量改用 void Utility_MonoStringToCppString( MonoString* pMonoStr, Util::String& buffer)
	// 特别是字符对象很长的时候，以减少 Util::String 临时对象的拷贝复制。
	std::string Utility_MonoStringToCppString( MonoString* pMonoStr );

	/// create a child domain
	MonoDomain* Utility_CreateChildDomain();

	///  set it as current domain
	void Utility_SetChildDomain(MonoDomain* newDomain);

	/// unload a child domain ,never unload the root domain
	void Utility_UnloadChildDomain();

	bool FastTestAndConvertUtf16ToAscii( char* dest, gunichar2 const* str, int length );

	//------------------------------------------------------------------------
	template<typename StringType>
	void Utility_MonoStringToCppString( MonoString* pMonoStr, StringType& buffer)
	{
		char* pStr = mono_string_to_utf8( pMonoStr );
		buffer = pStr;
		//mono_free( pStr );
		free(pStr);
	}

	// -- template function define -------
	/// bind a mono object with a cpp object
	template<typename T>
	bool BindCppObjWithMonoObj( T* pCppObj, MonoObject* pMonoObj )
	{
		script_fatal_error( NULL!=pCppObj );
		script_fatal_error( NULL!=pMonoObj );

		// - this cpp obj must havn't bind before
		bool bBind = pCppObj->IsBindMonoObject();
		script_fatal_error( !bBind );

		// - bind this two object
		Utility_SetCppObjectPtr( pMonoObj, pCppObj );
		pCppObj->SetMonoObject( pMonoObj );

		// - add ref to this cpp object
		//pCppObj->AddRef();
		
		return true;
	}

	/*
	* wrapper a mono object and associate with its cpp object,act like a pointer 
	*/
	template<typename T>
	class ScriptObjWrapper
	{
	public:
		ScriptObjWrapper( MonoObject* pMonoObj )
			: m_pMonoObj( pMonoObj )
			, m_pCppObj( NULL )
		{
			void* pObj = Utility_GetCppObjectPtr( pMonoObj );
			m_pCppObj  = static_cast<T*>( pObj );
			mm_assert( NULL!=m_pCppObj );
		}

		T* GetCppObjPtr( void ) { return m_pCppObj; }
		T& operator * () const	{ return *m_pCppObj; }
		T* operator -> () const	{ return m_pCppObj;	}
		bool IsValid( void ) const	{ return m_pMonoObj!=NULL;}

		static T* Convert( MonoObject* pMonoObj )
		{
			if (NULL == pMonoObj)
			{
				return NULL;
			}
			return static_cast<T*>( Utility_GetCppObjectPtr( pMonoObj ) );
		}
	private:
		MonoObject* m_pMonoObj;
		T* m_pCppObj;
	};
}
#endif