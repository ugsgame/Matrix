
#include "cocos2d.h"

USING_NS_CC;

class SystemFeature
{
public:
	SystemFeature();
	~SystemFeature();

public: 
	const char* getSDPath();
	bool isSDExist();
	const char* getPackageName();
public:
	static SystemFeature* shareSystemFeature();
protected:
private:
};