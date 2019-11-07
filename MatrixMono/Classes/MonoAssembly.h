#ifndef __MONO_ASSEMBLY_H__
#define __MONO_ASSEMBLY_H__

#include "MonoObject.h"

#include <IMonoAssembly.h>

struct IMonoScript;
struct IMonoArray;

class CScriptClass;
class CScriptDomain;

class CScriptAssembly 
	: public IMonoAssembly
{
public:
	CScriptAssembly(CScriptDomain *pDomain, MonoImage *pImage, const char *path, bool nativeAssembly = true);
	virtual ~CScriptAssembly();

	CScriptClass *TryGetClass(MonoClass *pClass);

	// IMonoAssembly
	virtual IMonoClass *GetClass(const char *className, const char *nameSpace = "Matrix") ;

	virtual const char *GetName()  { return mono_image_get_name((MonoImage *)m_pImage); }
	virtual const char *GetPath()  { return m_path.c_str(); }

	virtual bool IsNative()  { return m_bNative; }

	virtual IMonoDomain *GetDomain()  { return (IMonoDomain *)m_pDomain; }

	virtual IMonoException *_GetException(const char *nameSpace, const char *exceptionClass, const char *message = NULL);
	// ~IMonoAssembly

	// IMonoObject
	virtual void Release(bool triggerGC = true) ;

	virtual EMonoAnyType GetType()  { return eMonoAnyType_Unknown; }
	virtual MonoAnyValue GetAnyValue()  { return MonoAnyValue(); }

	virtual mono::object GetManagedObject() ;

	virtual IMonoClass *GetClass() ;

	virtual void *UnboxObject()  { return NULL; }

	virtual std::string ToString()  { return GetName(); }
	// ~IMonoObject

	/// <summary>
	/// Called when a IMonoClass created from this assembly is released.
	/// </summary>
	void OnClassReleased(CScriptClass *pClass);

	void SetImage(MonoImage *pImage) { m_pImage = pImage; }
	MonoImage *GetImage() const { return m_pImage; }

	void SetPath(const char *path) { m_path = std::string(path); }

private:
	std::string m_path;
	bool m_bNative;

	bool m_bDestroying;

	std::vector<CScriptClass *> m_classes;
	CScriptDomain *m_pDomain;

	IMonoClass *m_pClass;
	MonoImage *m_pImage;
};

#endif //__MONO_ASSEMBLY_H__