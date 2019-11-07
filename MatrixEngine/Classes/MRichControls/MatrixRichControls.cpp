
#include "cocos2d.h"
#include <cocos-ext.h>
#include <renren-ext.h>
#include "ExtensionMacros.h"

#include "MatrixRichControls.h"
#include "Native/ScriptBind_CCLabelHTML.h"
#include "Native/ScriptBind_FontCatalog.h"
#include "Native/ScriptBind_FontFactory.h"


static MatrixRichControls *s_SharedMatrixRichControls= NULL;

MatrixRichControls* MatrixRichControls::ShareMatrixRichControls(void)
{
	if (!s_SharedMatrixRichControls)
	{
		s_SharedMatrixRichControls = new MatrixRichControls();
	}

	return s_SharedMatrixRichControls;
}

void MatrixRichControls::RegisterScript(void)
{
	RegisterBinding(ScriptBind_CCLabelHTML);
	RegisterBinding(ScriptBind_FontCatalog);
	RegisterBinding(ScriptBind_FontFactory);
}