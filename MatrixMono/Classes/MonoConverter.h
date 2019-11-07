/////////////////////////////////////////////////////////////////////////*
//Ink Studios Source File.
//Copyright (C), Ink Studios, 2011.
//////////////////////////////////////////////////////////////////////////
// Handles converting between C# and C++ types and objects.
//////////////////////////////////////////////////////////////////////////
// 11/01/2012 : Created by Filip 'i59' Lundgren
////////////////////////////////////////////////////////////////////////*/
#ifndef __MONO_CONVERTER_H__
#define __MONO_CONVERTER_H__

#include <stdlib.h>
#include <IMonoConverter.h>
#include "MonoCommon.h"
#include "ScriptUtility.h"

using namespace mono;

struct IMonoClass;

class CConverter 
	: public IMonoConverter
{
public:
	CConverter() {}
	~CConverter() {}

	// IMonoConverter
	virtual std::string ToString(mono::string _monoString)  
	{ 
		MonoString* monoString = (MonoString *)_monoString;
		if (!monoString)
			return std::string ();

// 		char buff[256];
// 		if(monoString->length <= 256 && FastTestAndConvertUtf16ToAscii (buff,mono_string_chars(monoString), mono_string_length(monoString)) )
// 			return std::string((char const*)buff,mono_string_length (monoString));

		char* buf = mono_string_to_utf8 (monoString);
		std::string temp(buf);
		g_free (buf);
		return temp;
	}
	
	virtual IMonoArray *ToArray(mono::object arr) ;

	virtual IMonoObject *ToObject(mono::object obj, bool allowGC = true) ;
	// ~IMonoConverter
};

#endif //__MONO_CONVERTER_H__