
#ifndef __MATRIX_MONO_H__
#define __MATRIX_MONO_H__
//TODO

#include "IMonoObject.h"
#include "IMonoClass.h"
#include "IMonoDomain.h"
#include "IMonoArray.h"
#include "IMonoAssembly.h"
#include "IMonoConverter.h"
#include "IMonoException.h"
#include "IMonoScriptBind.h"
#include "IMonoScriptSystem.h"

bool InitMatrixMono();
//获得对象的值
void* MonoGetField(IMonoObject* obj,const char* value);
//获得对象的属性
void* MonoGetProperty(IMonoObject* obj, const char* value);


// - bind
#define __ScriptBind \
public:\
	void SetMonoObject( IMonoObject* pObj )  { this->m_ScriptDetail.SetMonoObject( pObj );  }\
	IMonoObject* GetMonoObject( void )       { return this->m_ScriptDetail.GetMonoObject(); }\
	bool IsBindMonoObject( void )			{ return this->m_ScriptDetail.IsBindMonoObject(); }\
private:\
	ScriptDetail m_ScriptDetail;\

/*
* just make a smart pointer of script object
*/
class ScriptDetail
{
public:
	/// constructor
	ScriptDetail()
		: m_MonoObject( NULL )
		//, m_pScriptObj( NULL )
	{}
	/// destructor
	~ScriptDetail()
	{}
	/// set m_pScriptObj
	void SetMonoObject( IMonoObject* pObj );
	/// Get m_pScriptObj
	IMonoObject* GetMonoObject( void );
	/// to see if this cpp object is binding a mono object
	bool IsBindMonoObject( void );
private:
	// - do not allow copy
	ScriptDetail( const ScriptDetail& );
	ScriptDetail& operator=( const ScriptDetail& );
	// - private data 
	IMonoObject* m_MonoObject;
};

// - inline function implement ---
//------------------------------------------------------------------------

inline void ScriptDetail::SetMonoObject( IMonoObject* pObj )
{
	m_MonoObject = pObj;
}

inline IMonoObject* ScriptDetail::GetMonoObject( void ) 
{
	return m_MonoObject;
}
inline bool ScriptDetail::IsBindMonoObject( void ) 
{
	return (NULL!=m_MonoObject); 
}

#endif