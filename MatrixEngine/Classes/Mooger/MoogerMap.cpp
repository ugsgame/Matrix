
//#include "stdneb.h"
#include "MoogerMap.h"


MoogerMap::MoogerMap()
{

}

MoogerMap::~MoogerMap()
{
	//delete imgNames;
	CCTextureCache::sharedTextureCache()->removeUnusedTextures();
	if(iSprites)
	{
		for (int i=0;i<spriteCount;i++)
		{
			if(iSprites[i])
			{
				if(iSprites[i]->getTexture())
					CCTextureCache::sharedTextureCache()->removeTexture(iSprites[i]->getTexture());
			}

		}
	}
	this->removeAllChildrenWithCleanup(true);
}

bool MoogerMap::LoadWithFile(const char* mapName, const char* resFilePath)
{
	CCFileUtils* fileUtils = CCFileUtils::sharedFileUtils();

	std::string mapPath = fileUtils->fullPathForFilename(mapName);
	std::string resPath = resFilePath;

	TFileAble dat(mapPath.c_str());

	//读取地图实际范围
	dat.Read(&iLeft, 4);
	dat.Read(&iWidth, 4);
	dat.Read(&iTop, 4);
	dat.Read(&iHeight, 4);

	//背景色
	unsigned char  r,g,b;
	dat.Read(&r, 1);
	dat.Read(&g, 1);
	dat.Read(&b, 1);
	int color = ((r & 0xF0) << 4) | (g & 0xF0) | ((b & 0xF0) >> 4);

	//初始化地图
	this->setContentSize(CCSizeMake((float)iWidth,(float)iHeight));

	//读取总共图片的数量和最大图片ID
	unsigned short imageCount;
	dat.Read(&imageCount, 2);
	unsigned short maxId;
	dat.Read(&maxId, 2);

	char** fileName = new char*[maxId + 1];

	//读取图片ID
	unsigned short* ids = new unsigned short[imageCount];
	dat.Read(ids, imageCount * 2);

	//读取图片
	unsigned short i;
	unsigned short id; 
	for(i = 0; i < imageCount; ++i) {
		id = ids[i];
		fileName[id] = new char[512];
		sprintf(fileName[id], "%s/%d.png",resPath.c_str(), id);
	}

	//读取地图Sprite
	dat.Read(&spriteCount, 2);;
	TSprite* sprites = new TSprite[spriteCount];
//	TSprite* sprites = (TSprite*)malloc(spriteCount * sizeof(TSprite));
	iSprites = new CCSprite* [spriteCount];
	dat.Read(sprites, spriteCount * sizeof(TSprite));
	for(i = 0; i < spriteCount; ++i) {
		TSprite& sprite = sprites[i];
		CCSprite* csprite = CCSprite::create(fileName[sprite.iImageId]);

		float x = sprite.iX - iLeft+csprite->getContentSize().width/2;
		float y = sprite.iY - iTop+csprite->getContentSize().height/2;
		ccColor4B color;
		color.r = 10;
		color.g = 0;
		color.b = 0;
		color.a = 0;
		//这样做是为了避免x方向出现裂缝
		CCPoint uiPos= CCDirector::sharedDirector()->convertToUI(ccp(x,y));
		csprite->setPosition(ccp(x,uiPos.y));	
		//csprite->setAABB(CCRectMake(x , y ,csprite->getContentSize().width,csprite->getContentSize().height));
		csprite->setFlipX((bool)sprite.iFlip);
		csprite->setRotation(sprite.iR/100.0f);
		csprite->setScale(sprite.iS/100.0f);
		iSprites[i] = csprite;
		this->addChild(csprite);
	}

	//清理地图Sprite、图片数组和图片ID
	delete sprites;
	delete fileName;

	imgNames = ids;
	imgLen = imageCount;

	return true;
}

void MoogerMap::Clean()
{

}

CCSize MoogerMap::GetMapSize()
{
	return CCSize((float)iWidth,(float)iHeight);
}

MoogerMap* MoogerMap::Create()
{
	MoogerMap *pSprite = new MoogerMap();
	if (pSprite && pSprite->init())
	{
		pSprite->autorelease();
		return pSprite;
	}
	CC_SAFE_DELETE(pSprite);
	return NULL;
}
