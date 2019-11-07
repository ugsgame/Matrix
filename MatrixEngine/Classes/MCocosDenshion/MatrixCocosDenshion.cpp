
#include "MatrixCocosDenshion.h"

#include "Native/ScriptBind_AudioEngine.h"

static MatrixCocosDenshion *s_SharedMatrixCocosDenshion = NULL;

MatrixCocosDenshion* MatrixCocosDenshion::ShareMatrixCocosDenshion(void)
{
	if (!s_SharedMatrixCocosDenshion)
	{
		s_SharedMatrixCocosDenshion = new MatrixCocosDenshion();
	}

	return s_SharedMatrixCocosDenshion;
}

void MatrixCocosDenshion::RegisterScript(void)
{
	RegisterBinding(ScriptBind_AudioEngine);
}
