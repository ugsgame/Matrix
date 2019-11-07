
#ifndef __NET_HELPER__
#define __NET_HELPER__

#include "cocos2d.h"
//TODO:ÓÐ´ýÍêÉÆ

USING_NS_CC;

class NetHelper
{
public:
	NetHelper();
	~NetHelper();

public: 
	virtual void openUrl(const char* url);
public:
	static NetHelper* shareNetHelper();
protected:
private:
};

#endif

