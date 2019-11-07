
#ifndef __MCSCRIPT_HELPER__
#define __MCSCRIPT_HELPER__

#include "MatrixMono.h"

#define CC_NAMESPACE "MatrixEngine.Cocos2d"

class MCScriptHelper
{
protected:
	MCScriptHelper();
public:
	static MCScriptHelper* Share();

	mono::object New_CCTouch(int id, float x,float y);
	mono::object New_CCSet();
private:

	IMonoClass* class_CCTouch;
	IMonoClass* class_CCSet;

	IMonoAssembly* pEngineAssembly;

};

#endif