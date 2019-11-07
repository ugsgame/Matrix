
#include "cocos2d.h"
#include <renren-ext.h>
#include "ScriptBind_FontCatalog.h"

USING_NS_CC;
USING_NS_CC_EXT;

using namespace dfont;

ScriptBind_FontCatalog::ScriptBind_FontCatalog()
{
	REGISTER_METHOD(Flush);
	REGISTER_METHOD(Char_Width);
	REGISTER_METHOD(Char_Height);
	REGISTER_METHOD(Add_HackFont);
	REGISTER_METHOD(Add_HackFontWithFaceIndex);
	REGISTER_METHOD(Dump_Textures);
}

ScriptBind_FontCatalog::~ScriptBind_FontCatalog()
{

}

void ScriptBind_FontCatalog::Flush(FontCatalog* fontcatalog)
{
	fontcatalog->flush();
}

int ScriptBind_FontCatalog::Char_Width(FontCatalog* fontcatalog)
{
	return fontcatalog->char_width();
}

int ScriptBind_FontCatalog::Char_Height(FontCatalog* fontcatalog)
{
	return fontcatalog->char_height();
}

bool ScriptBind_FontCatalog::Add_HackFont(FontCatalog* fontcatalog,mono::string fontname,int shift_y)
{
	return fontcatalog->add_hackfont(ToMatrixString(fontname).c_str(),latin_charset(),shift_y);
}

bool ScriptBind_FontCatalog::Add_HackFontWithFaceIndex(FontCatalog* fontcatalog,mono::string fontname,long face_idx,int shift_y)
{
	return fontcatalog->add_hackfont(ToMatrixString(fontname).c_str(),face_idx,latin_charset(),shift_y);
}

void ScriptBind_FontCatalog::Dump_Textures(FontCatalog* fontcatalog,mono::string prefix)
{
	fontcatalog->dump_textures(ToMatrixString(prefix).c_str());
}
