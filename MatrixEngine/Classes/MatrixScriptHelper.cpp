//#include "stdneb.h"

#include "cocos2d.h"
#include "MatrixScript.h"
#include "MatrixScriptHelper.h"

USING_NS_CC;

static MatrixScriptHelper *s_SharedMatrixScriptHelper = NULL;

MatrixScriptHelper::MatrixScriptHelper()
{
	pEngineAssembly = MatrixScript::ShareMatrixScript()->GetEngineAssembly();
	CCAssert(pEngineAssembly,"");

	class_Vector2 = pEngineAssembly->GetClass("Vector2",CS_MATRIX_MATH_NAMESPACE);
	class_Size = pEngineAssembly->GetClass("Size",CS_MATRIX_MATH_NAMESPACE);
	class_Rect = pEngineAssembly->GetClass("Rect",CS_MATRIX_MATH_NAMESPACE);
	class_Color32 = pEngineAssembly->GetClass("Color32",CS_MATRIX_MATH_NAMESPACE);
}

MatrixScriptHelper* MatrixScriptHelper::Share(void)
{
	if (!s_SharedMatrixScriptHelper)
	{
		s_SharedMatrixScriptHelper = new MatrixScriptHelper();
	}

	return s_SharedMatrixScriptHelper;
}
/*
mono::object MatrixScriptHelper::New_Vector2(float x,float y)
{
	CCAssert(class_Vector2,"");

	IMonoArray *pArgs = CreateMonoArray(2);
	pArgs->Insert(x);
	pArgs->Insert(y);
	mono::object obj = class_Vector2->CreateInstance(pArgs);

	//CCAssert(obj,"");
	return obj;
}

mono::object MatrixScriptHelper::New_Size(float width, float height)
{
	CCAssert(class_Size,"");

	IMonoArray *pArgs = CreateMonoArray(2);
	pArgs->Insert(width);
	pArgs->Insert(height);
	mono::object obj = class_Size->CreateInstance(pArgs);

	//CCAssert(obj,"");
	return obj;
}

mono::object MatrixScriptHelper::New_Rect(float x, float y,float width, float height)
{
	CCAssert(class_Rect,"");

	IMonoArray *pArgs = CreateMonoArray(4);
	pArgs->Insert(x);
	pArgs->Insert(y);
	pArgs->Insert(width);
	pArgs->Insert(height);
	mono::object obj = class_Size->CreateInstance(pArgs);

	//CCAssert(obj,"");
	return obj;
}

mono::object MatrixScriptHelper::New_Color32(int r,int g,int b,int a)
{
	CCAssert(class_Color32,"");

	IMonoArray *pArgs = CreateMonoArray(4);
	pArgs->Insert(r);
	pArgs->Insert(g);
	pArgs->Insert(b);
	pArgs->Insert(a);
	mono::object obj = class_Size->CreateInstance(pArgs);

	return obj;
}
*/