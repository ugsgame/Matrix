#ifndef __MONO_EXCEPTION_H__
#define __MONO_EXCEPTION_H__

#include "ScriptDebug.h"
#include <IMonoException.h>

class CScriptException : public IMonoException
{
public:
	CScriptException(MonoException *pException) : m_pException(pException) { mm_assert(pException); }
	~CScriptException() { m_pException = NULL; }

	// IMonoException
	virtual void Throw() { mono_raise_exception(m_pException); }
	// ~IMonoException

	MonoException *m_pException;
};

#endif // __MONO_EXCEPTION_H__