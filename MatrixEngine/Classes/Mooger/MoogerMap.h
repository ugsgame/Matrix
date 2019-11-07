
#ifndef __MOOGERMAP_H__
#define __MOOGERMAP_H__

//#include "core/types.h"
#include "cocos2d.h"

USING_NS_CC;

/************************************************************************/
/* TODO
/* 以后增加对pak文件的支持
/************************************************************************/

class MoogerMap:public CCSprite
{
private:
	struct TSprite  
	{
		unsigned short	iImageId;
		short	iX;
		short   iY;
		unsigned short  iFlip;
		int	iR;
		int   iS;
	};

	struct TFileAble
	{
		unsigned char* oData; 
		unsigned long  oSize;
		int i;

		TFileAble(const char* file)
		{
			CCFileUtils* fileUtils = CCFileUtils::sharedFileUtils();
			oData = fileUtils->getFileData(file,"rb",&oSize);
			i = 0;
		}
		~TFileAble()
		{
			delete oData;
			oData = NULL;
			i = 0;
		}
		void Read(void* buf,int len)
		{
			if( i+len > (int)oSize)
			{
				len = oSize - i;
			};
			if(len > 0)
			{
				memcpy(buf,oData+i,len);
				i += len;
			}
		}
	};

public:
	MoogerMap();
	~MoogerMap();

	bool LoadWithFile(const char* mapName,const char* resPath);
	//bool Load( const char* mapName,CPakRead* aPak,);
	void Clean();

	CCSize GetMapSize();

	static MoogerMap* Create();
protected:
	int	  iLeft;
	int   iTop;
	int	  iWidth;
	int   iHeight;
private:
	CCSprite**	iSprites;		//地图精灵图片
	unsigned short*		imgNames;

	unsigned short		imgLen;
	unsigned short		spriteCount;
};

#endif