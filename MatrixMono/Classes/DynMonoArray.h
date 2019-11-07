#ifndef __DYN_MONO_ARRAY_H__
#define __DYN_MONO_ARRAY_H__

#include "MonoArray.h"

class CDynScriptArray
	: public CScriptArray
{
public:
	CDynScriptArray(MonoDomain *pDomain, IMonoClass *pContainingType = NULL, int size = 0, bool allowGC = true);
	CDynScriptArray(mono::object monoArray) : CScriptArray(monoArray) {}

	// CScriptArray
	virtual void Clear() ;

	virtual void Remove(int index) ;

	virtual IMonoArray *Clone()  { return new CDynScriptArray((mono::object)mono_array_clone((MonoArray *)m_pObject)); }

	virtual void InsertMonoObject(mono::object object, int index = -1) ;

	virtual void InsertNativePointer(void *ptr, int index = -1) ;
	virtual void InsertAny(MonoAnyValue value, int index = -1) ;
	virtual void InsertMonoString(mono::string string, int index = -1)  { InsertMonoObject((mono::object)string, index); }
	// ~CScriptArray
};

#endif // __DYN_MONO_ARRAY_H__