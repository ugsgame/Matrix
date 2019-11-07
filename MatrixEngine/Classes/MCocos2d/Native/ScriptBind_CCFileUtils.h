
#ifndef __SCRIPTBIND_CCFILEUTILS__
#define __SCRIPTBIND_CCFILEUTILS__

#include "ScriptBind_Cocos2d.h"

//class cocos2d::CCFileUtils;

class ScriptBind_CCFileUtils:public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCFileUtils();
	~ScriptBind_CCFileUtils();

	virtual const char* GetClassName(){ return "NativeFileUtils";}

	static void AddSearchPath(mono::string filePath);
	static void RemoveSearchPath(mono::string filePath);
	static mono::string FullPathForFilename(mono::string filename);

	static mono::string GetSDPath();
	static bool IsSDExist();

	static bool IsFileExist(mono::string filePath);
	static bool DeleteFile(mono::string filePath);
	static  mono::string GetWritablePath();

	static mono::string GetFileDataToString(mono::string pszFileName, mono::string pszMode, unsigned long &pSize);
	static void* GetFileDataToIntptr(mono::string pszFileName, mono::string pszMode, unsigned long &pSize);

	//IO
	static FILE* FileOpen(mono::string filePath, mono::string pszMode);
	static bool  FileClose(FILE* file);

	static const unsigned char* CreateBuffer(unsigned long pSize);
	static void DeleteBuffer(const unsigned char* buffer);
	static void WriteBuffer(FILE* file,int size,const unsigned char* buffer);
	static const unsigned char* ReadBuffer(FILE* file,unsigned long &pSize); 
	//Buffer
	static void  WriteBufferByte(const unsigned char* buffer,int size,int seek,unsigned char _byte);
	static unsigned char  ReadBufferByte(const unsigned char* buffer,int size,int seek);

	static void  WriteBufferShort(const unsigned char* buffer,int size,int seek,short _short);
	static short  ReadBufferShort(const unsigned char* buffer,int size,int seek);

	static void  WriteBufferInt(const unsigned char* buffer,int size,int seek,int _Int);
	static int  ReadBufferInt(const unsigned char* buffer,int size,int seek);

	static void  WriteBufferFloat(const unsigned char* buffer,int size,int seek,float _float);
	static float  ReadBufferFloat(const unsigned char* buffer,int size,int seek);
	//
protected:
private:
};

#endif