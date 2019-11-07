
#ifndef __SCRIPTBIND_FONTFACTORY__
#define __SCRIPTBIND_FONTFACTORY__

#include "ScriptBind_RichControls.h"

class dfont::FontFactory;
class dfont::FontCatalog;

class ScriptBind_FontFactory:public ScriptBind_RichControls
{
public:
	ScriptBind_FontFactory();
	~ScriptBind_FontFactory();
protected:
	virtual const char* GetClassName(){ return "NativeFontFactory"; };

	static dfont::FontCatalog* Create(mono::string alias, mono::string font_name,unsigned int color, int size_pt,
		dfont::EFontStyle style, float strength,unsigned int secondary_color, 
		int faceidx, int ppi);

	 static dfont::FontCatalog* Find(mono::string alias, bool no_fail);

	 static dfont::FontCatalog* Another_Alias(mono::string another_alias, mono::string origin_alias);
protected:
private:
};

#endif