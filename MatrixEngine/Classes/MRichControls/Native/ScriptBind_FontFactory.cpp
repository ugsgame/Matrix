

#include "cocos2d.h"
#include <renren-ext.h>
#include "ScriptBind_FontFactory.h"

USING_NS_CC;
USING_NS_CC_EXT;

using namespace dfont;


ScriptBind_FontFactory::ScriptBind_FontFactory()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(Find);
	REGISTER_METHOD(Another_Alias);
}

ScriptBind_FontFactory::~ScriptBind_FontFactory()
{

}

FontCatalog* ScriptBind_FontFactory::Create(mono::string alias, mono::string font_name, unsigned int color, int size_pt, EFontStyle style, float strength, unsigned int secondary_color, int faceidx, int ppi)
{
	return FontFactory::instance()->create_font(ToMatrixString(alias).c_str(),ToMatrixString(font_name).c_str(),color,size_pt,style,strength,secondary_color,faceidx,ppi);
}

FontCatalog* ScriptBind_FontFactory::Find(mono::string alias, bool no_fail)
{
	return FontFactory::instance()->find_font(ToMatrixString(alias).c_str(),no_fail);
}

FontCatalog* ScriptBind_FontFactory::Another_Alias(mono::string another_alias, mono::string origin_alias)
{
	return FontFactory::instance()->another_alias(ToMatrixString(another_alias).c_str(),ToMatrixString(origin_alias).c_str());
}
