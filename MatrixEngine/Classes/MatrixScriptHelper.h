
#ifndef __MCSCRIPT_HELPER__
#define __MCSCRIPT_HELPER__

#include "MatrixMono.h"

#define CS_MATRIX_MATH_NAMESPACE "MatrixEngine.Math"

//ֻ�������࣬�������ڽṹ��

class MatrixScriptHelper
{
protected:
	MatrixScriptHelper();
public:
	static MatrixScriptHelper* Share();

//	mono::object New_Vector2(float x, float y);
//	mono::object New_Size(float width, float height);
//	mono::object New_Rect(float x, float y,float width, float height);
//	mono::object New_Color32(int r,int g,int b,int a);
private:

	IMonoClass* class_Vector2;
	IMonoClass* class_Size;
	IMonoClass* class_Rect;
	IMonoClass* class_Color32;

	IMonoAssembly* pEngineAssembly;

};

#endif