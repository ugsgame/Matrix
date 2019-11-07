//#include "stdneb.h"
#include "cocos2d.h"
#include "MatrixScript.h"
#include "MCScriptHelper.h"

USING_NS_CC;

static MCScriptHelper *s_SharedMCScriptHelper = NULL;

MCScriptHelper::MCScriptHelper()
{
	pEngineAssembly = MatrixScript::ShareMatrixScript()->GetEngineAssembly();
	CCAssert(pEngineAssembly,"");

	class_CCTouch = pEngineAssembly->GetClass("CCTouch",CC_NAMESPACE);
	class_CCSet   = pEngineAssembly->GetClass("CCSet",CC_NAMESPACE);
}

MCScriptHelper* MCScriptHelper::Share(void)
{
	if (!s_SharedMCScriptHelper)
	{
		s_SharedMCScriptHelper = new MCScriptHelper();
	}

	return s_SharedMCScriptHelper;
}

mono::object MCScriptHelper::New_CCTouch(int id, float x,float y)
{
	CCAssert(class_CCTouch,"");

	IMonoArray *pArgs = CreateMonoArray(3);
	pArgs->Insert(id);
	pArgs->Insert(x);
	pArgs->Insert(y);
	mono::object obj = class_CCTouch->CreateInstance(pArgs);

	//CCAssert(obj);
	return obj;
}
mono::object MCScriptHelper::New_CCSet()
{
	CCAssert(class_CCSet,"");

	mono::object obj = class_CCSet->CreateInstance(CreateMonoArray(0));

	//CCAssert(obj);
	return obj;
}
