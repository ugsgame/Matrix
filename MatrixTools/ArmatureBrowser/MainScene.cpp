#include "MainScene.h"
#include "IconvString.h"

#include <cocos-ext.h>
#include <direct.h> 
#include <support/tinyxml2/tinyxml2.h>

USING_NS_CC;
USING_NS_CC_EXT;


// shareMainScene pointer
MainScene * MainScene::sm_pSharedMainScene = 0;

//////////////////////////////////////////////////////////////////////////
//File path util
//将字符串str中的所有target字符串替换为replacement
bool StringReplace(std::string& src, std::string target, std::string replacement)
{ 
	std::string::size_type startpos = 0;
	while (startpos!= std::string::npos)
	{    
		startpos = src.find(target);   
		//找到'.'的位置
		if( startpos != std::string::npos ) 
		//std::string::npos表示没有找到该字符 
		{      
			src.replace(startpos,1,replacement); 
			//实施替换，注意后面一定要用""引起来，表示字符串
		} 
	}   
	return true;
}

//提取路径中的文件名字（带路径，不带扩展名）
//substr字符串中，第一个参数为截取的位置，第二个为截取的长度
std::string StringGetFullFileName(std::string path)
{
	return path.substr(0, path.rfind('.') == std::string::npos ? path.length() : path.rfind('.') );
}

//提取路径中的文件名字
std::string StringGetFileName(std::string path)
{ 
	StringReplace(path, "/", "\\"); 
	std::string::size_type startpos = path.rfind('\\') == std::string::npos ? path.length() : path.rfind('\\')+1;
	std::string::size_type endpos   = path.rfind('.') == std::string::npos ? path.length() : path.rfind('.');
	return path.substr(startpos, endpos-startpos);
}

//提取路径中文件名字（带扩展名）
std::string StringGetFileNameWithExt(std::string path)
{ 
	StringReplace(path, "/", "\\"); 
	std::string::size_type startpos = path.rfind('\\') == std::string::npos ? path.length() : path.rfind('\\')+1;
	return path.substr(startpos);
}
//////////////////////////////////////////////////////////////////////////


CCScene* MainScene::scene()
{
    // 'scene' is an autorelease object
    CCScene *scene = CCScene::create();
    
    // 'layer' is an autorelease object
    sm_pSharedMainScene = MainScene::create();

    // add layer as a child to scene
    scene->addChild(sm_pSharedMainScene);

    // return the scene
    return scene;
}

// on "init" you need to initialize your instance
bool MainScene::init()
{
    if ( !CCLayer::init() )
    {
        return false;
    }
    
//     CCSize visibleSize = CCDirector::sharedDirector()->getVisibleSize();
//     CCPoint origin = CCDirector::sharedDirector()->getVisibleOrigin();
// 
// 	//TEST:deleted it
//     CCSprite* pSprite = CCSprite::create("HelloWorld.png");
// 
//     // position the sprite on the center of the screen
//     pSprite->setPosition(ccp(visibleSize.width/2 + origin.x, visibleSize.height/2 + origin.y));
// 
//     // add the sprite as a child to this layer
//     this->addChild(pSprite, 0);

	//TODO:设置屏幕背景为灰白色

	this->scheduleUpdate();
    
    return true;
}

//////////////////////////////////////////////////////////////////////////
// static member function
//////////////////////////////////////////////////////////////////////////
MainScene* MainScene::sharedMainScene()
{
	CC_ASSERT(sm_pSharedMainScene);
	return sm_pSharedMainScene;
}

void MainScene::update(float delta)
{
}

void MainScene::draw()
{
	drawGrid();
	CCLayer::draw();
}

void MainScene::changeArmature(std::string armatureName)
{
	if(mArmatureName.compare(armatureName))
	{
		mArmatureName = armatureName;
		this->removeAllChildren();
		mArmature = NULL;
		mAnimation = NULL;

		mArmature = CCArmature::create(mArmatureName.c_str());
		mAnimation = mArmature->getAnimation();

		CCAnimationData* animData = mAnimation->getAnimationData();
		if(animData->getMovementCount()>0)
			mAnimation->playWithIndex(0,-1,-1,1);

		this->addChild(mArmature);
		//reset translation
		center();
	}
}

bool MainScene::openArmature(std::string armatureName)
{

	CCArmatureDataManager::sharedArmatureDataManager()->addArmatureFileInfo(armatureName.c_str());

	//TODO:check it
	{
		this->removeAllChildren();
		mArmature = NULL;
		mAnimation = NULL;
		//get armature name
		std::string  fileName = StringGetFileName(armatureName).c_str();
		CCLog("Open Armature:%s",fileName.c_str());
		mArmature = CCArmature::create(fileName.c_str());
		if (mArmature)
		{
			//init a ArmatureContainer
			ArmatureContainer armContatin;
			armContatin.armatureName = fileName;
			armContatin.armatureFile = armatureName;
			mArmatureName = fileName;
			//add or replace armature
			int rel = true;
			for (int i = 0; i < armatureList.size(); i++)
			{
				if (!armatureList[i].compare(fileName))
				{
					armatureList[i] = fileName;
					rel = false;
					break;
				}
			}
			if (rel)armatureList.push_back(fileName);
			//

			//fine the animations
			mAnimation = mArmature->getAnimation();
			CCAnimationData* animData = mAnimation->getAnimationData();
			armContatin.movementList = animData->movementNames;

			CCLog("Movement Count:%d",animData->getMovementCount());
			if (animData->getMovementCount()>0)
			{
				for (int i=0;i<armContatin.movementList.size();i++)
				{
					CCLog("Movement id:%s",armContatin.movementList.at(i).c_str());
				}
				//
				mAnimation->playWithIndex(0,-1,-1,1);
			}
			//

			this->addChild(mArmature);
			armatureContainer[mArmatureName] = armContatin;
			//reset translation
			center();

		}
		else
		{
			CCLog("Can't create armature: %s", fileName);
			return false;
		}
		
	}

	return true;
}

bool MainScene::exportArmature(std::string filePath)
{
	std::string armature = getCurArmature();
	std::string curArmatureFile = getArmatureFile(armature);
	//std::string curArmaturePath = getArmaturePath(armature);

	CCLog("export Armature to xml.animation:%s",curArmatureFile.c_str());
	CCLog("path:%s",filePath.c_str());

	unsigned char *pBytes = NULL;
	rapidjson::Document jsonDict;
	unsigned long size = 0;
	pBytes = CCFileUtils::sharedFileUtils()->getFileData(curArmatureFile.c_str(),"r" , &size);

	std::string load_str = std::string((const char *)pBytes, size);
	jsonDict.Parse<0>(load_str.c_str());
	if (jsonDict.HasParseError())
	{
		CCLog("GetParseError %s\n",jsonDict.GetParseError());

		return false;
	}

	std::string exportPath = filePath+"/"+armature+"/";
	mkdir(exportPath.c_str());
	//export json
	std::string jsonExportPath = exportPath + "Json/";
	mkdir(jsonExportPath.c_str());

	FILE* fp = fopen((jsonExportPath+armature+".json").c_str(),"w");
	fwrite(pBytes,size,1,fp);
	fclose(fp);
	//export xml
	exportAnimXML(exportPath);


	//get *.plist file
	//config_file_path
	const rapidjson::Value&  config_file_path = DICTOOL->getSubDictionary_json(jsonDict,"config_file_path");
	if (DICTOOL->checkObjectExist_json(config_file_path))
	{
		for (int i = 0;i<DICTOOL->getArrayCount_json(jsonDict,"config_file_path");i++)
		{
			//export plist file
			std::string plistFile = DICTOOL->getStringValueFromArray_json(jsonDict,"config_file_path",i);
			exportPlist(getArmaturePath(getCurArmature())+plistFile,exportPath);
		}
		
	}
	else
		return false;

	return true;
}

void MainScene::playAnimation(std::string animName,bool loop)
{
	if (mAnimation)
	{
		loop?mAnimation->play(animName.c_str(),-1,-1,1):mAnimation->play(animName.c_str(),-1,-1,-1);
	}
	
}

void MainScene::resize(float w,float h)
{
	//TODO:不一定要居中
	center();
}

void MainScene::center()
{
	CCSize visibleSize = CCDirector::sharedDirector()->getOpenGLView()->getFrameSize();
	visibleSize = visibleSize/2.0f;
	visibleSize.height = visibleSize.height*0.7f;
	this->setPosition(visibleSize);
}

void MainScene::save(const unsigned char* buffer)
{

}

void MainScene::load(const unsigned char* buffer)
{

}

void MainScene::drawGrid()
{

}

void MainScene::exportPlist(std::string plistPath,std::string exportPath)
{
	CCLog("%s",plistPath.c_str());

	exportPath += "Resources/";
	mkdir(exportPath.c_str());

	CCDictionary* plistDic = CCDictionary::createWithContentsOfFile(plistPath.c_str());


	CCDictionary *metadataDict = (CCDictionary*)plistDic->objectForKey("metadata");
	CCDictionary *framesDict = (CCDictionary*)plistDic->objectForKey("frames");
	int format = 0;
	CCSprite* img = NULL; 

	// get the format
	if(metadataDict != NULL) 
	{
		format = metadataDict->valueForKey("format")->intValue();
		CCString* imgFile = dynamic_cast<CCString*>(metadataDict->objectForKey("realTextureFileName"));
		std::string imgPath = getArmaturePath(getCurArmature());
		//get the texture
		img = CCSprite::create((imgPath+imgFile->getCString()).c_str()); 
		//test
		//this->addChild(img);
	}

	// check the format
	CCAssert(format >=0 && format <= 3, "format is not supported for CCSpriteFrameCache addSpriteFramesWithDictionary:textureFilename:");

	CCDictElement* pElement = NULL;
	CCDICT_FOREACH(framesDict, pElement)
	{
		CCDictionary* frameDict = (CCDictionary*)pElement->getObject();
		std::string spriteFrameName = pElement->getStrKey();
		CCLog("key:%s",spriteFrameName.c_str());
		if(format == 0) 
		{
			float x = frameDict->valueForKey("x")->floatValue();
			float y = frameDict->valueForKey("y")->floatValue();
			float w = frameDict->valueForKey("width")->floatValue();
			float h = frameDict->valueForKey("height")->floatValue();
			float ox = frameDict->valueForKey("offsetX")->floatValue();
			float oy = frameDict->valueForKey("offsetY")->floatValue();
			int ow = frameDict->valueForKey("originalWidth")->intValue();
			int oh = frameDict->valueForKey("originalHeight")->intValue();
			// check ow/oh
			if(!ow || !oh)
			{
				CCLOGWARN("cocos2d: WARNING: originalWidth/Height not found on the CCSpriteFrame. AnchorPoint won't work as expected. Regenrate the .plist");
			}
			// abs ow/oh
			ow = abs(ow);
			oh = abs(oh);

			if(img)
			{
				img->setTextureRect(CCRectMake(x,y,w,h));
				CCRenderTexture* pRT = CCRenderTexture::create(ow, oh, kCCTexture2DPixelFormat_RGBA8888);
				pRT->clear(0,0,0,0);
				pRT->begin();
				//img->setAnchorPoint(ccp(0,0));
				img->setPosition(ccp(ow/2+ox,oh/2+oy));
				img->visit();
				pRT->end();
				//std::string exportPath = getArmaturePath(getCurArmature())+"plistRes/";

				std::string imgFile = exportPath + spriteFrameName;
				std::string _imgFile = imgFile;
				std::string imaName = StringGetFileNameWithExt(_imgFile);
				std::string imgPath = _imgFile.erase(_imgFile.length()-imaName.length()-1,_imgFile.length());
				CCLog("export image:%s",imgFile.c_str());


				size_t inLen = imgPath.length();
				size_t outLen = inLen * 2;

				char* gbkImgPath = (char  *)malloc(outLen);
				code_convert("utf-8","gb2312",(char*)imgPath.c_str(), inLen,gbkImgPath,outLen);
				mkdir(gbkImgPath);


				inLen = imgFile.length();
				outLen = inLen * 2;
				char* gbkImgFile = (char  *)malloc(outLen);

				//gbk2utf8((char*)imgFile.c_str(), inLen, utf8ImgFile, outLen); 
				//CCLog("utf8ImgFile: %s",utf8ImgFile);

				code_convert("utf-8","gb2312",(char*)imgFile.c_str(), inLen,gbkImgFile,outLen);

				CCImage *pImage = pRT->newCCImage(true);
				pImage->saveToFile(gbkImgFile, false);
			}

		} 
		//TODO
		else if(format == 1 || format == 2) 
		{
			CCRect frame = CCRectFromString(frameDict->valueForKey("frame")->getCString());
			bool rotated = false;

			// rotation
			if (format == 2)
			{
				rotated = frameDict->valueForKey("rotated")->boolValue();
			}

			CCPoint offset = CCPointFromString(frameDict->valueForKey("offset")->getCString());
			CCSize sourceSize = CCSizeFromString(frameDict->valueForKey("sourceSize")->getCString());

		} 
		//TODO:
		else if (format == 3)
		{
			// get values
			CCSize spriteSize = CCSizeFromString(frameDict->valueForKey("spriteSize")->getCString());
			CCPoint spriteOffset = CCPointFromString(frameDict->valueForKey("spriteOffset")->getCString());
			CCSize spriteSourceSize = CCSizeFromString(frameDict->valueForKey("spriteSourceSize")->getCString());
			CCRect textureRect = CCRectFromString(frameDict->valueForKey("textureRect")->getCString());
			bool textureRotated = frameDict->valueForKey("textureRotated")->boolValue();

			// get aliases
			CCArray* aliases = (CCArray*) (frameDict->objectForKey("aliases"));
			CCString * frameKey = new CCString(spriteFrameName);

			CCObject* pObj = NULL;
			CCARRAY_FOREACH(aliases, pObj)
			{
				std::string oneAlias = ((CCString*)pObj)->getCString();
			}

		}
	}	
}

void MainScene::exportAnimXML(std::string exportPath)
{
	std::string armature = getCurArmature();

	StringReplace(exportPath,"/","\\");

	std::string modle1 =  "<?xml version=\"1.0\"?>\n\
						  <AnimationProject xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n \
						  <Version>1.4.0.1</Version>\n";

	std::string modle2 =  std::string("<Resources>")+(exportPath+"\\Resources")+"</Resources>\n";
	std::string modle3 =  std::string("<Name>")+armature+"</Name>\n";
	std::string modle4 =  std::string("<JsonFileName>")+(armature+".json")+"</JsonFileName>\n";
	std::string modle5 =  std::string("<JsonFolder>")+(exportPath+"\\Json")+"</JsonFolder>\n";

	std::string modle6 =  "<JsonList />\n \
						   <CanvasSize>\n \
						   <Width>0</Width>\n \
						   <Height>0</Height>\n \
						   </CanvasSize>\n";

	std::string modle7 =  std::string("<filePath>")+(exportPath+armature+".xml.animation")+"</filePath>\n";

	std::string modle8 =  	"<ProjectType>AnimationProject</ProjectType>\n \
							<ResRelativePath />\n  \
							<ResourceFileList />\n \
							<bBatchImage>false</bBatchImage>\n \
							</AnimationProject>\n ";

	std::string modle = modle1 + modle2 + modle3 + modle4 + modle5 + modle6 + modle7 + modle8;
	
	CCLog("export xml:%s",modle.c_str());

	FILE* fp = fopen((exportPath+getCurArmature()+".xml.animation").c_str(),"w");
	fwrite(modle.c_str(),modle.length(),1,fp);
	fclose(fp);
}



