
#include "Archive.h"

#include <stdio.h>

#include "MainScene.h"


Archive* Archive::_shareArchive = 0;

Archive::Archive()
	:width(800),
	height(600),
	openFileDir("/")
{
	
}

Archive::~Archive()
{

}

Archive* Archive::shareArchive()
{
	if (!_shareArchive)
	{
		_shareArchive = new Archive();
	}
	return _shareArchive;
}
//.save.dat
bool Archive::load()
{
//	FILE* fp = fopen("save.dat","r");
	//CCAssert(fp,"can't open save.dat to read!!!");
	if(cocos2d::CCFileUtils::sharedFileUtils()->isFileExist("save.dat"))
	{
		int size;
		unsigned long _size;
		int seek = 0;
		//const unsigned char* buf = readBuffer(fp,size);
		const unsigned char* buf = cocos2d::CCFileUtils::sharedFileUtils()->getFileData("save.dat","r",&_size);
		size = _size;

		if(buf && size > 0)
		{
			//main size
			this->width = readBufferInt(buf,size,seek);
			this->height = readBufferInt(buf,size,seek);
			//file dir
			openFileDir = readBufferString(buf,size,seek);
			////armtureFile
			int armtureCount = readBufferInt(buf,size,seek);
			for (int i = 0; i < armtureCount; i++)
			{
				armatureContainer.push_back(readBufferString(buf,size,seek));
				cocos2d::CCLog("armatur: %s",armatureContainer[i].c_str());
			}
		}

		//fclose(fp);
	}
	else
	{
		cocos2d::CCLog("can't open save.dat to read!!!");
		return false;
	}
	return true;
}

bool Archive::save()
{
	int size = 0;	//1 m
	int seek = 0;

	size = 12;
	size += strlen(this->openFileDir.c_str());
	size += 4;
	for (int i = 0; i < armatureContainer.size(); i++)
	{
		size += 4;
		size += armatureContainer[i].length();
	}
	//
	const unsigned char* buf = createBuffer(size);

	//main size
	writeBufferInt(buf,size,seek,this->width);
	writeBufferInt(buf,size,seek,this->height);
	//openFileDir
	writeBufferString(buf,size,seek,this->openFileDir);
	//armtureFile
	// size
	writeBufferInt(buf,size,seek,armatureContainer.size());
	for (int i = 0; i < armatureContainer.size(); i++)
	{
		writeBufferString(buf,size,seek,armatureContainer[i]);
	}

	//EOF
	//writeBufferByte(buf,size,seek,0);
	FILE* fp = fopen("save.dat","w");
	writeBuffer(fp,size,buf);
	fclose(fp);

	return true;
}

void Archive::setMainSize(int w,int h)
{
	width = w;
	height = h;
}

int Archive::getMainSizeW()
{
	return width;
}

int Archive::getMainSizeH()
{
	return height;
}

std::string Archive::getFileOpenPath()
{
	return openFileDir;
}

void Archive::setFileOpenPath(std::string filePath)
{
	openFileDir = filePath;
}

std::vector<std::string> Archive::getArmaturePaths()
{
	return armatureContainer;
}

void Archive::setArmaturePaths(std::vector<std::string> container)
{
	armatureContainer = container;
}




const unsigned char* Archive::createBuffer(int pSize)
{
	const unsigned char* buffer = NULL;
	buffer = (const unsigned char*)malloc(pSize);
	memset((void*)buffer,0,pSize);

	return buffer;
}

void Archive::deleteBuffer(const unsigned char* buffer)
{
	if(buffer)
	{
		delete buffer;
		buffer = NULL;
	}
}

void Archive::writeBuffer(FILE* fp,int size, const unsigned char* buffer)
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
		//抛出异常
	}
}
const unsigned char* Archive::readBuffer(FILE* fp,int &pSize)
{
	unsigned char * pBuffer = NULL;
	pSize = 0;
	do
	{
		// read the file from hardware
		if(!fp)return 0;

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
		printf("%s \n", msg.c_str());
	}
	return pBuffer;
}
//Buffer

void  Archive::writeBufferByte(const unsigned char* buffer,int size,int& seek,unsigned char _byte)
{
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+1)<=size),"Seek Out Of buffer Exception!!");

	memcpy((void*)(buffer+seek),&_byte,1);
	seek += 1;
}
unsigned char  Archive::readBufferByte(const unsigned char* buffer,int size,int& seek)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+1)<=size),"Seek Out Of buffer Exception!!");

	unsigned char value;
	memcpy(&value,(void*)(buffer+seek),1);
	seek += 1;
	return value;
}

void  Archive::writeBufferShort(const unsigned char* buffer,int size,int& seek,short _short)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+2)<=size),"Seek Out Of buffer Exception!!");

	memcpy((void*)(buffer+seek),&_short,2);
	seek += 2;
}
short Archive::readBufferShort(const unsigned char* buffer,int size,int& seek)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+2)<=size),"Seek Out Of buffer Exception!!");

	short value;
	memcpy(&value,(void*)(buffer+seek),2);
	seek += 2;
	return value;
}

void Archive::writeBufferInt(const unsigned char* buffer,int size,int& seek,int _Int)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+4)<=size),"Seek Out Of buffer Exception!!");

	memcpy((void*)(buffer+seek),&_Int,4);
	seek += 4;
}
int  Archive::readBufferInt(const unsigned char* buffer,int size,int& seek)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+4)<=size),"Seek Out Of buffer Exception!!");

	int value;
	memcpy(&value,(void*)(buffer+seek),4);
	seek += 4;
	return value;
}

void  Archive::writeBufferFloat(const unsigned char* buffer,int size,int& seek,float _float)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+4)<=size),"Seek Out Of buffer Exception!!");

	memcpy((void*)(buffer+seek),&_float,4);
	seek += 4;
}
float  Archive::readBufferFloat(const unsigned char* buffer,int size,int& seek)
{
	//int size = strlen((const char*)buffer);
	CCAssert(buffer,"Buffer is NULL!!");
	CCAssert(((seek+4)<=size),"Seek Out Of buffer Exception!!");

	float value;
	memcpy(&value,(void*)(buffer+seek),4);
	seek += 4;
	return value;
}

void Archive::writeBufferString(const unsigned char* buffer,int size,int& seek,std::string str )
{
	CCAssert(buffer,"Buffer is NULL!!");

	CCAssert(((seek+4)<=size),"Seek Out Of buffer Exception!!");
	writeBufferInt(buffer,size,seek,str.length());

	CCAssert(((seek+str.length())<=size),"Seek Out Of buffer Exception!!");
	memcpy((void*)(buffer+seek),str.c_str(),str.length());
	seek += str.length();
}

std::string Archive::readBufferString(const unsigned char* buffer,int size,int& seek)
{
	CCAssert(buffer,"Buffer is NULL!!");

	CCAssert(((seek+4)<=size),"Seek Out Of buffer Exception!!");
	int len = readBufferInt(buffer,size,seek);

	CCAssert(((seek+len)<=size),"Seek Out Of buffer Exception!!");
	const char* str = new char[len+1];	//添加结束符
	memset((void*)str,0,len+1);

	memcpy((void*)str,(void*)(buffer+seek),len);
	seek += len;

	return std::string(str);
}
