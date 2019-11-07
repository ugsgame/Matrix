
#ifndef __SCRIPTBIND_MOOGER_H__
#define __SCRIPTBIND_MOOGER_H__

#include "MatrixMono.h"

class MoogerMap;

class ScriptBind_Mooger:public IMonoScriptBind
{
public:
	ScriptBind_Mooger();
	~ScriptBind_Mooger();

	virtual const char* GetNamespace(){ return "MatrixEngine.Mooger.Native";}
	virtual const char* GetClassName(){ return "NativeMoogerMap";}

	static MoogerMap* CreateMoogrMap();
	static bool LoadMapWithFile(MoogerMap* moogerMap,mono::string fileName,mono::string resPath);
protected:
private:
};

#endif

