
//#include "stdneb.h"
#include "ScriptDebug.h"

#include "MonoArray.h"

#include "MonoDomain.h"
#include "MonoObject.h"

#include "MonoScriptSystem.h"

#include "MonoHelpers.h"

#include <IMonoClass.h>

MonoClass *CScriptArray::m_pDefaultElementClass = NULL;

CScriptArray::CScriptArray(mono::object managedArray, bool allowGC)
{
	mm_assert(managedArray);

	SetManagedObject((MonoObject *)managedArray, allowGC);
	m_pElementClass = mono_class_get_element_class(GetMonoClass());

	m_elementSize = mono_array_element_size(mono_array_class_get(m_pElementClass, 1));

	m_lastIndex = GetSize() - 1;
}

CScriptArray::CScriptArray(MonoDomain *pDomain, int size, IMonoClass *pContainingType, bool allowGC)
: m_lastIndex(-1)
{
	mm_assert(size >= 0);
	mm_assert(pDomain);

	m_pElementClass = (pContainingType ? (MonoClass *)(pContainingType->GetManagedObject()) : m_pDefaultElementClass);
	mm_assert(m_pElementClass);

	SetManagedObject((MonoObject *)mono_array_new(pDomain, m_pElementClass, size), allowGC);

	m_elementSize = mono_array_element_size(mono_array_class_get(m_pElementClass, 1));
}

CScriptArray::~CScriptArray()
{
	m_lastIndex = 0;
}

void CScriptArray::Resize(int size)
{
	int oldArraySize = GetSize();
	if(oldArraySize == size)
		return;

	MonoArray *pOldArray = (MonoArray *)m_pObject;

	CScriptDomain *pDomain = static_cast<CScriptDomain *>(GetClass()->GetAssembly()->GetDomain());

	m_pObject = (MonoObject *)mono_array_new(pDomain->GetMonoDomain(), m_pElementClass, size);

	for(int i = 0; i < oldArraySize; i++)
	{
		if(i < size)
		{
			mono_array_set_addr_with_size((MonoArray *)m_pObject, i, m_elementSize, *(void **)mono_array_addr_with_size(pOldArray, m_elementSize, i));
			m_lastIndex = i;
		}
	}
}

void CScriptArray::Clear()
{
	int size = GetSize();
	for(int i = 0; i < size; i++)
		mono_array_set_addr_with_size((MonoArray *)m_pObject, i, m_elementSize, NULL);

	m_lastIndex = -1;
}

void CScriptArray::Remove(int index)
{
	int size = GetSize();

	mm_assert(index < size);

	mono_array_set_addr_with_size((MonoArray *)m_pObject, index, m_elementSize, NULL);

	if(index == size - 1)
		m_lastIndex--;
}

mono::object CScriptArray::GetItem(int index)
{ 
	mm_assert(index < GetSize());

	return *(mono::object *)mono_array_addr_with_size((MonoArray *)m_pObject, m_elementSize, index);
}

void CScriptArray::InsertMonoObject(mono::object object, int index)
{
	if(index == -1)
	{
		m_lastIndex++;
		index = m_lastIndex;
	}

	mm_assert(index < GetSize());

	mono_array_set_addr_with_size((MonoArray *)m_pObject, index, m_elementSize, object);
}

void CScriptArray::InsertNativePointer(void *ptr, int index)
{
	CScriptDomain *pDomain = static_cast<CScriptDomain *>(GetClass()->GetAssembly()->GetDomain());

	InsertMonoObject((mono::object)mono_value_box(pDomain->GetMonoDomain(), mono_get_intptr_class(), (long *)&ptr), index);
}

void CScriptArray::InsertAny(MonoAnyValue value, int index)
{ 
	IMonoDomain *pDomain = GetClass()->GetAssembly()->GetDomain();

	InsertMonoObject(pDomain->BoxAnyValue(value), index);
}

IMonoClass *CScriptArray::GetClass(MonoClass *pClass)
{
	if(CScriptDomain *pDomain = g_pScriptSystem->TryGetDomain(mono_object_get_domain(m_pObject)))
	{
		MonoClass *pMonoClass = GetMonoClass();

		if(CScriptAssembly *pAssembly = pDomain->TryGetAssembly(mono_class_get_image(pMonoClass)))
			return pAssembly->TryGetClass(pMonoClass);
	}

	return NULL;
}