
#ifndef __SCRIPTBIND_FONTCATALOG__
#define __SCRIPTBIND_FONTCATALOG__

#include "ScriptBind_RichControls.h"

class dfont::FontCatalog;

class ScriptBind_FontCatalog:public ScriptBind_RichControls
{
public:
	ScriptBind_FontCatalog();
	~ScriptBind_FontCatalog();

protected:
	virtual const char* GetClassName(){ return "NativeFontCatalog"; };
	//TODO:create

	static void Flush(dfont::FontCatalog* fontcatalog);
	static int Char_Width(dfont::FontCatalog* fontcatalog);
	static int Char_Height(dfont::FontCatalog* fontcatalog);

	static bool Add_HackFont(dfont::FontCatalog* fontcatalog,mono::string fontname,int shift_y);
	static bool Add_HackFontWithFaceIndex(dfont::FontCatalog* fontcatalog,mono::string fontname,long face_idx,int shift_y);

	static void Dump_Textures(dfont::FontCatalog* fontcatalog,mono::string prefix);
protected:
private:
};

#endif