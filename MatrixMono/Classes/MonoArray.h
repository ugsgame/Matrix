/////////////////////////////////////////////////////////////////////////*
//Ink Studios Source File.
//Copyright (C), Ink Studios, 2011.
//////////////////////////////////////////////////////////////////////////
// Wrapper for the MonoArray for less intensively ugly code and
// better workflow.
//////////////////////////////////////////////////////////////////////////
// 17/12/2011 : Created by Filip 'i59' Lundgren
////////////////////////////////////////////////////////////////////////*/
#ifndef __MONO_ARRAY_H__
#define __MONO_ARRAY_H__

#include "MonoObject.h"
#include "MonoClass.h"
#include "MonoAssembly.h"

#include <IMonoArray.h>

//////////////////////////////////////////////////////////////////////////
// 映射出MonoArray结构，以便调用max_length
typedef struct {
	guint32 length;
	guint32 lower_bound;
} MonoArrayBounds;

typedef struct {
	int obj1;
	int obj2;
	/* bounds is NULL for szarrays */
	MonoArrayBounds *bounds;
	/* total number of elements of the array */
	guint32 max_length; 
	/* we use double to ensure proper alignment on platforms that need it */
	double vector [1];
} MMArray;
//////////////////////////////////////////////////////////////////////////

class CScriptArray 
	: public CScriptObject
	, public IMonoArray
{
public:
	// Used on MonoArray's returned from C#.
	CScriptArray(mono::object monoArray, bool allowGC = true);
	// Used to send arrays to C#.
	CScriptArray(MonoDomain *pDomain, int size, IMonoClass *pContainingType = NULL, bool allowGC = true);

	CScriptArray() {}

	virtual ~CScriptArray();

	// IMonoArray
	virtual void Clear() ;

	virtual void Remove(int index) ;

	virtual void Resize(int size) ;
	virtual int GetSize() const  { return (int)((MMArray *)m_pObject)->max_length; }

	virtual IMonoArray *Clone()  { return new CScriptArray((mono::object)mono_array_clone((MonoArray *)m_pObject)); }

	virtual IMonoClass *GetElementClass()  { return GetClass(m_pElementClass); }
	virtual IMonoClass *GetDefaultElementClass() { return GetClass(m_pDefaultElementClass); }

	virtual mono::object GetItem(int index) ;

	virtual void InsertNativePointer(void *ptr, int index = -1) ;
	virtual void InsertAny(MonoAnyValue value, int index = -1) ;
	virtual void InsertMonoString(mono::string string, int index = -1)  { InsertMonoObject((mono::object)string, index); }
	virtual void InsertMonoObject(mono::object object, int index = -1) ;
	// ~IMonoArray

	// IMonoObject
	virtual void Release(bool triggerGC = true)  
	{
		if(!triggerGC)
			m_objectHandle = -1;

		delete this;
	}
	
	virtual EMonoAnyType GetType()  { return eMonoAnyType_Array; }
	virtual MonoAnyValue GetAnyValue()  { return MonoAnyValue(); }

	virtual mono::object GetManagedObject()  { return CScriptObject::GetManagedObject(); }

	virtual IMonoClass *GetClass()  { return CScriptObject::GetClass(); }

	virtual void *UnboxObject()  { return CScriptObject::UnboxObject(); }

	virtual std::string ToString()  { return CScriptObject::ToString(); }
	// ~IMonoObject

	IMonoClass *GetClass(MonoClass *pClass);

	static MonoClass *m_pDefaultElementClass;

protected:
	// index of the last object in the array
	int m_lastIndex;
	// Size of each element in the array
	int m_elementSize;

	MonoClass *m_pElementClass;
};

#endif //__MONO_ARRAY_H__