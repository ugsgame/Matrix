/////////////////////////////////////////////////////////////////////////*
//Ink Studios Source File.
//Copyright (C), Ink Studios, 2011.


#ifndef __MONO_COMMON_H__
#define __MONO_COMMON_H__

#include <algorithm> 
#include <vector>
#include <map>
#include <string>
#include <assert.h>

#include "MonoTypes.h"
#include "MonoAPI.h"

#include <IMonoScriptSystem.h>
#include <IMonoConverter.h>

namespace mono 
{
	class _string; typedef _string* string; 
	class _object; typedef _object* object;
};

#ifndef SAFE_DELETE
#define SAFE_DELETE(p)			{ if(p) { delete (p);		(p)=NULL; } }
#endif

#ifndef SAFE_DELETE_ARRAY
#define SAFE_DELETE_ARRAY(p)	{ if(p) { delete [] (p);		(p)=NULL; } }
#endif

#ifndef SAFE_RELEASE
#define SAFE_RELEASE(p)			{ if(p) { (p)->Release();	(p)=NULL; } }
#endif

#define n_new(type) new type
#define n_new_array(type,size) new type[size]
#define n_delete(ptr) delete ptr
#define n_delete_array(ptr) delete[] ptr

// - define some mono function by macro
#define mono_array_addr(array,type,index) ((type*)(void*) mono_array_addr_with_size (array, sizeof (type), index))
#define mono_array_get(array,type,index) ( *(type*)mono_array_addr ((array), type, (index)) ) 
#define mono_array_set(array,type,index,value)	\
	do {	\
	type *__p = (type *) mono_array_addr ((array), type, (index));	\
	*__p = (value);	\
	} while (false)

#define mono_string_chars(s) ((gunichar2*)&((s)->firstCharacter))
#define mono_string_length(s) ((s)->length)

/// <summary>
/// Converts a C++ string to the C# equivalent.
/// </summary>
inline std::string ToMatrixString(mono::string monoString)
{
	if(!monoString)
		return "";

	return ShareMonoScriptSystem()->GetConverter()->ToString(monoString);
}

inline std::string ToStdString(mono::string monoString)
{
	char* pStr = mono_string_to_utf8((MonoString *)monoString);
	char* str = (char*)malloc(strlen(pStr));
	strcpy(str,pStr);
	std::string cppString(str);
	g_free(pStr);

	return cppString;
}

//////////////////////////////////////////////////////////////////////////
//! Find and erase element from container.
// @return true if item was find and erased, false if item not found.
//////////////////////////////////////////////////////////////////////////
template <class Container,class Value>
inline bool find_and_erase( Container& container,const Value &value )
{
	typename Container::iterator it = std::find( container.begin(),container.end(),value );
	if (it != container.end())
	{
		container.erase( it );
		return true;
	}
	return false;
}

/// <summary>
/// Wrapped 'helpers' used to easily convert native mono objects to IMonoArray's, strings etc.
/// </summary>
namespace mono
{
	/// <summary>
	/// Mono String; used in scriptbind parameters and when invoking Mono scripts.
	/// </summary>
	class _string
	{
	public:
		/// <summary>
		/// Allows direct casting from mono::string to const char *, no more manual ToMatrixString calls, woo!
		/// </summary>
		operator std::string () const
		{
			return ToMatrixString(const_cast<_string *>(this));
		}

		/// <summary>
		/// Allows direct casting from mono::string to CryStringT<char> (string).
		/// </summary>
// 		operator String<char>() const
// 		{
// 			return (String<char>)ToMatrixString(const_cast<_string *>(this));
// 		}
	};

	class _object
	{
	public:
		operator IMonoObject *() const
		{
			return ShareMonoScriptSystem()->GetConverter()->ToObject(const_cast<_object *>(this));
		}

		operator IMonoArray *() const
		{
			return ShareMonoScriptSystem()->GetConverter()->ToArray(const_cast<_object *>(this));
		}
	};

	typedef _string* string;
	typedef _object* object;
};

#include <IMonoDomain.h>
#include <IMonoObject.h>

#endif //__MONO_COMMON_H__