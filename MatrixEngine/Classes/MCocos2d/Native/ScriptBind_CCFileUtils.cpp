
//#include "stdneb.h"
#include "cocos2d.h"
#include "ScriptBind_CCFileUtils.h"
#include "Platform/SystemFeature.h"

#include <stdio.h>

USING_NS_CC;

ScriptBind_CCFileUtils::ScriptBind_CCFileUtils()
{
	REGISTER_METHOD(AddSearchPath);
	REGISTER_METHOD(RemoveSearchPath);
	REGISTER_METHOD(FullPathForFilename);

	REGISTER_METHOD(IsFileExist);
	REGISTER_METHOD(DeleteFile);
	REGISTER_METHOD(GetWritablePath);

	REGISTER_METHOD(GetSDPath);
	REGISTER_METHOD(IsSDExist);

	REGISTER_METHOD(GetFileDataToString);
	REGISTER_METHOD(GetFileDataToIntptr);

	REGISTER_METHOD(FileOpen);
	REGISTER_METHOD(FileClose);

	REGISTER_METHOD(CreateBuffer);
	REGISTER_METHOD(DeleteBuffer);
	REGISTER_METHOD(WriteBuffer);
	REGISTER_METHOD(ReadBuffer);

	REGISTER_METHOD(WriteBufferByte);
	REGISTER_METHOD(ReadBufferByte);
	REGISTER_METHOD(WriteBufferShort);
	REGISTER_METHOD(ReadBufferShort);
	REGISTER_METHOD(WriteBufferInt);
	REGISTER_METHOD(ReadBufferInt);
	REGISTER_METHOD(WriteBufferFloat);
	REGISTER_METHOD(ReadBufferFloat);
};

ScriptBind_CCFileUtils::~ScriptBind_CCFileUtils()
{
	
}


void ScriptBind_CCFileUtils::AddSearchPath(mono::string filePath)
{
	CCFileUtils* fileUtils = CCFileUtils::sharedFileUtils();

	//std::vector<std::string> paths;
	//paths.push_back(std::string(*filePath));
	//fileUtils->setSearchPaths(paths);

	fileUtils->addSearchPath(ToMatrixString(filePath).c_str());
}

void ScriptBind_CCFileUtils::RemoveSearchPath(mono::string filePath)
{
	CCFileUtils* fileUtils = CCFileUtils::sharedFileUtils();
	fileUtils->removeSearchPath(ToMatrixString(filePath).c_str());
}

mono::string ScriptBind_CCFileUtils::FullPathForFilename(mono::string filename)
{
	CCFileUtils* fileUtils = CCFileUtils::sharedFileUtils();
	
	std::string fullPath = fileUtils->fullPathForFilename(ToMatrixString(filename).c_str());
	return ToMonoString(fullPath.c_str());
}

mono::string ScriptBind_CCFileUtils::GetSDPath()
{
	mono::string SDPath = GetWritablePath();
#if(CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
	SDPath = ToMonoString(SystemFeature::shareSystemFeature()->getSDPath());
#endif
	return SDPath;
}

bool ScriptBind_CCFileUtils::IsSDExist()
{
	return SystemFeature::shareSystemFeature()->isSDExist();
}

bool ScriptBind_CCFileUtils::IsFileExist(mono::string filePath)
{
	return CCFileUtils::sharedFileUtils()->isFileExist(*filePath);
}

bool ScriptBind_CCFileUtils::DeleteFile(mono::string filePath)
{
	if(!remove(ToMatrixString(filePath).c_str()))
		return true;
	else
		return false;
}

static inline std::string convertPathFormatToUnixStyle(const std::string& path)  
{   
    std::string ret = path; int len = ret.length();  
    for (int i = 0; i < len; ++i)   
    {   
         if (ret[i] == '\\')   
         {   
              ret[i] = '/';   
         }   
     }   
    return ret;  
} 


mono::string ScriptBind_CCFileUtils::GetWritablePath()
{
#if (CC_TARGET_PLATFORM != CC_PLATFORM_WIN32)  
	     return ToMonoString(CCFileUtils::sharedFileUtils()->getWritablePath().c_str());   
#else  
	     char full_path[MAX_PATH + 1];  
	     ::GetModuleFileNameA(NULL, full_path,MAX_PATH + 1);  
         std::string ret((char*)full_path);  
	     // remove xxx.exe  
	     ret =  ret.substr(0, ret.rfind("\\") + 1);  
	     ret = convertPathFormatToUnixStyle(ret);  
	     return ToMonoString(ret.c_str());  
#endif 
//	 return ToMonoString(CCFileUtils::sharedFileUtils()->getWritablePath().c_str());  
}

mono::string ScriptBind_CCFileUtils::GetFileDataToString(mono::string pszFileName, mono::string pszMode, unsigned long &pSize)
{
	CCFileUtils* fileUtils = CCFileUtils::sharedFileUtils();
	unsigned char* str = fileUtils->getFileData(ToMatrixString(pszFileName).c_str(),ToMatrixString(pszMode).c_str(),&pSize);
	char* _str = new char[pSize+1];
	memset(_str,'\0',pSize+1);
	memcpy(_str,str,pSize);
	return ToMonoString(_str);
}
void* ScriptBind_CCFileUtils::GetFileDataToIntptr(mono::string pszFileName, mono::string pszMode, unsigned long &pSize)
{
	CCFileUtils* fileUtils = CCFileUtils::sharedFileUtils();
	return fileUtils->getFileData(ToMatrixString(pszFileName).c_str(),ToMatrixString(pszMode).c_str(),&pSize);
}

//IO
FILE* ScriptBind_CCFileUtils::FileOpen(mono::string filePath, mono::string pszMode)
{
	const char* fn = ToMatrixString(filePath).c_str();
	const char* md = ToMatrixString(pszMode).c_str();
	FILE* fp = fopen(fn,md);
	CCAssert(fp,"Can't open or create fp!!!");
	return fp;
}
bool  ScriptBind_CCFileUtils::FileClose(FILE* file)
{
	if(!fclose(file))
		return true;
	else
		return false;
}

const unsigned char* ScriptBind_CCFileUtils::CreateBuffer(unsigned long pSize)
{
	const unsigned char* buffer = NULL;
	buffer = (const unsigned char*)malloc(pSize);
	memset((void*)buffer,'\0',pSize);

	return buffer;
}

void ScriptBind_CCFileUtils::DeleteBuffer(const unsigned char* buffer)
{
	if(buffer)
	{
		delete buffer;
		buffer = NULL;
	}
}

void ScriptBind_CCFileUtils::WriteBuffer(FILE* fp,int size, const unsigned char* buffer)
{
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(fp,"file is NULL!!");

	if(buffer&&fp)
	{
		//int size = strlen((const char*)buffer);
		fwrite(buffer,size,1,fp);
	}
	else
	{
		//Å×³öÒì³£
	}
}
const unsigned char* ScriptBind_CCFileUtils::ReadBuffer(FILE* fp,unsigned long &pSize)
{
	unsigned char * pBuffer = NULL;
	pSize = 0;
	do
	{
		// read the file from hardware
		CC_BREAK_IF(!fp);

		fseek(fp,0,SEEK_END);
		pSize = ftell(fp);
		fseek(fp,0,SEEK_SET);
		pBuffer = new unsigned char[pSize];
		pSize = fread(pBuffer,sizeof(unsigned char), pSize,fp);
		fclose(fp);
	} while (0);

	if (! pBuffer)
	{
		std::string msg = "Get buffer from file failed!";
		CCLOG("%s", msg.c_str());
	}
	return pBuffer;
}
//Buffer

void  ScriptBind_CCFileUtils::WriteBufferByte(const unsigned char* buffer,int size,int seek,unsigned char _byte)
{
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+1)<=size),"Seek Out Of buffer Exception!!");

	memcpy((void*)(buffer+seek),&_byte,1);
}
unsigned char  ScriptBind_CCFileUtils::ReadBufferByte(const unsigned char* buffer,int size,int seek)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+1)<=size),"Seek Out Of buffer Exception!!");

	unsigned char value;
	memcpy(&value,(void*)(buffer+seek),1);
	return value;
}

void  ScriptBind_CCFileUtils::WriteBufferShort(const unsigned char* buffer,int size,int seek,short _short)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+2)<=size),"Seek Out Of buffer Exception!!");

	memcpy((void*)(buffer+seek),&_short,2);
}
short ScriptBind_CCFileUtils::ReadBufferShort(const unsigned char* buffer,int size,int seek)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+2)<=size),"Seek Out Of buffer Exception!!");

	short value;
	memcpy(&value,(void*)(buffer+seek),2);
	return value;
}

void ScriptBind_CCFileUtils::WriteBufferInt(const unsigned char* buffer,int size,int seek,int _Int)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+4)<=size),"Seek Out Of buffer Exception!!");

	memcpy((void*)(buffer+seek),&_Int,4);
}
int  ScriptBind_CCFileUtils::ReadBufferInt(const unsigned char* buffer,int size,int seek)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+4)<=size),"Seek Out Of buffer Exception!!");

	int value;
	memcpy(&value,(void*)(buffer+seek),4);
	return value;
}

void  ScriptBind_CCFileUtils::WriteBufferFloat(const unsigned char* buffer,int size,int seek,float _float)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+4)<=size),"Seek Out Of buffer Exception!!");

	memcpy((void*)(buffer+seek),&_float,4);
}
float  ScriptBind_CCFileUtils::ReadBufferFloat(const unsigned char* buffer,int size,int seek)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+4)<=size),"Seek Out Of buffer Exception!!");

	float value;
	memcpy(&value,(void*)(buffer+seek),4);
	return value;
}