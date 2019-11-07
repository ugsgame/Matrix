
//#include "stdneb.h"
#include "cocos2d.h"
#include "cocos-ext.h"
#include "ExtensionMacros.h"

#include "MatrixMono.h"
#include "Native/ScriptBind_CCArmAnim.h"
#include "Native/ScriptBind_CCArmature.h"
#include "Native/ScriptBind_CCArmDataManager.h"
#include "Native/ScriptBind_CCBone.h"
#include "Native/ScriptBind_CCDisplay.h"

#include "Native/ScriptBind_GUIReader.h"
#include "Native/ScriptBind_UILayer.h"
#include "Native/ScriptBind_UIWidget.h"
#include "Native/ScriptBind_UIButton.h"
#include "Native/ScriptBind_UICheckBox.h"
#include "Native/ScriptBind_UIImageView.h"
#include "Native/ScriptBind_UILabel.h"
#include "Native/ScriptBind_UILabelAtlas.h"
#include "Native/ScriptBind_UILabelBMFont.h"
#include "Native/ScriptBind_UILayer.h"
#include "Native/ScriptBind_UILayout.h"
#include "Native/ScriptBind_UILayoutParameter.h"
#include "Native/ScriptBind_UILoadingBar.h"
#include "Native/ScriptBind_UISlider.h"
#include "Native/ScriptBind_UITextField.h"
#include "Native/ScriptBind_UIWidget.h"
#include "Native/ScriptBind_UIListView.h"
#include "Native/ScriptBind_UIPageView.h"
#include "Native/ScriptBind_UIScrollView.h"

#include "Native/ScriptBind_SceneReader.h"

#include "MatrixCocoStudio.h"

USING_NS_CC;
USING_NS_CC_EXT;

static MatrixCocoStudio *s_SharedMatrixCocoStudio = NULL;

MatrixCocoStudio* MatrixCocoStudio::ShareMatrixCocoStudio(void)
{
	if (!s_SharedMatrixCocoStudio)
	{
		s_SharedMatrixCocoStudio = new MatrixCocoStudio();
	}

	return s_SharedMatrixCocoStudio;
}


void MatrixCocoStudio::RegisterScript(void)
{
	RegisterBinding(ScriptBind_CCArmDataManager);
	RegisterBinding(ScriptBind_CCArmature);
	RegisterBinding(ScriptBind_CCArmAnim);
	RegisterBinding(ScriptBind_CCBone);

	RegisterBinding(ScriptBind_GUIReader);
	RegisterBinding(ScriptBind_UILayer);
	RegisterBinding(ScriptBind_UIWidget);
	RegisterBinding(ScriptBind_UIButton);
	RegisterBinding(ScriptBind_UICheckBox);
	RegisterBinding(ScriptBind_UIImageView);
	RegisterBinding(ScriptBind_UILabel);
	RegisterBinding(ScriptBind_UILabelAtlas);
	RegisterBinding(ScriptBind_UILabelBMFont);
	RegisterBinding(ScriptBind_UILayout);
	RegisterBinding(ScriptBind_UILayoutParameter);
	RegisterBinding(ScriptBind_UILoadingBar);
	RegisterBinding(ScriptBind_UISlider);
	RegisterBinding(ScriptBind_UITextField);
	RegisterBinding(ScriptBind_UIListView);
	RegisterBinding(ScriptBind_UIPageView);
	RegisterBinding(ScriptBind_UIScrollView);

	RegisterBinding(ScriptBind_SceneReader);
}