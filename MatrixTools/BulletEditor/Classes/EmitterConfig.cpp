
#include "EmitterConfig.h"
#include "EditorConfig.h"
#include "MainScene.h"
#include "BulletSystem/BulletDisplay.h"
#include "BulletSystem/BulletEmitter.h"

#include <cocos-ext.h>
#include <direct.h> 

#include "CocoStudio/Json/rapidjson/stringbuffer.h"
#include "CocoStudio/Json/rapidjson/writer.h"

EmitterConfig*  EmitterConfig::sm_pSharedEmitterConfig = 0;

BulletDisplay* DisplayConfig::create()
{
	BulletDisplay* display = NULL;
	switch (displayType)
	{
	case dTypeSprite:
		display = SpriteDisplay::create(displayFile.c_str());
		break;
	case dTypeSpriteFrame:
		display = SpriteDisplay::create(displayFile.c_str(),atlasName.c_str());
		break;
	case dTypeParticle:
		display = ParticleDisplay::create(displayFile.c_str());
		break;
	case dTypeAnimation:
		display = AnimationDisplay::create(displayFile.c_str(),frames);
		break;
	case  dTypeArmature:
		display = ArmatureDisplay::create(displayFile.c_str(),armatureName.c_str(),animationName.c_str());
		break;
	default:
		break;
	}
	display->setBlendFuc(displaySrcBlend,displayDstBlend);
	display->setRotationOffset(displayRoation);
	display->setScale(displayScale);
	display->setAnimateSpeed(displayAnimSpeed);
	return display;
}

EmitterConfig::EmitterConfig()
{
	m_fDuration = -1.0;
	m_fDurationVar = 0;
	m_fInterimTime = 0;
	m_fInterimTimeVar = 0;
	m_fLifeTime = -1.0;
	m_fLifeTimeVar = 0;
	m_iInitCount = 50;
	m_fSpeed = 300.0f;
	m_fSpeedVar = 0;
	m_eEmitterMode = 0;
	m_ePositionType =  PositionTypeFree;
	m_fEmissionRate = 5.0;
	m_fEmissionRateVar = 0;
	m_fEmissionAngle = 0.0;
	m_fDamage = 0.0;
	m_uBeamCount = 1;
	m_uBeamCountVar = 0;
	m_fFieldAngle = 0.0;
	m_fFieldAngleVar = 0.0;
	m_fSpinSpeed = 0.0;
	m_bIsPointToTarget = false;
	m_bIsFollow = false;
	m_fCurvity = 0.1f;
	m_fSinAmplitude = 0;
	m_fSinRate = 0;

	m_fSpeedDecay = 0;
	m_fSpeedLimit = 0;
	m_fBulletSpinSpeed = 0;
	m_fBulletSpinSpeedVar = 0;

	m_fDisplayRotation = 0;
	m_fDisplayScale = 1;
	m_fAnimateSpeed = 10;

	m_eSrcBlend = MX_SRC_ALPHA;
	m_eDstBlend = MX_ONE_MINUS_SRC_COLOR;

	m_eEmitterMode = 0;

	m_pDisplayConfig = new DisplayConfig();

	m_pDisplayConfig->displayType = dTypeSprite;
	m_pDisplayConfig->displayFile = DEF_PNG_DEFAULT_BULLET;
}

EmitterConfig::~EmitterConfig()
{

}

EmitterConfig* EmitterConfig::shareEmitterConfig()
{
	if(!sm_pSharedEmitterConfig)
	{
		sm_pSharedEmitterConfig = new EmitterConfig();
	}

	return sm_pSharedEmitterConfig;
}

bool EmitterConfig::serialization(const char* configFile)
{
	rapidjson::Document document;
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	rapidjson::Value root(rapidjson::kObjectType);

	root.AddMember("duration",this->getDuration(),allocator);
	root.AddMember("durationVar",this->getDurationVar(),allocator);
	root.AddMember("interimTime",this->getInterimTime(),allocator);
	root.AddMember("interimTimeVar",this->getInterimTimeVar(),allocator);
	root.AddMember("lifeTime",this->getLifeTime(),allocator);
	root.AddMember("lifeTimeVar",this->getLifeTimeVar(),allocator);
	root.AddMember("initCount",this->getInitCount(),allocator);
	root.AddMember("speed",this->getSpeed(),allocator);
	root.AddMember("speedVar",this->getSpeedVar(),allocator);
	root.AddMember("speedDecay",this->getSpeedDecay(),allocator);
	root.AddMember("speedLimit",this->getSpeedLimit(),allocator);
	root.AddMember("emitterMode",this->getEmitterMode(),allocator);
	root.AddMember("positionType",this->getPositionType(),allocator);
	root.AddMember("isPointToTarget",this->getIsPointToTarget(),allocator);
	root.AddMember("emissionRate",this->getEmissionRate(),allocator);
	root.AddMember("emissionRateVar",this->getEmissionRateVar(),allocator);
	root.AddMember("emissionAngle",this->getEmissionAngle(),allocator);
	root.AddMember("beamCount",this->getBeamCount(),allocator);
	root.AddMember("beamCountVar",this->getBeamCountVar(),allocator);
	root.AddMember("fieldAngle",this->getFieldAngle(),allocator);
	root.AddMember("fieldAngleVar",this->getFieldAngleVar(),allocator);
	root.AddMember("spinSpeed",this->getSpinSpeed(),allocator);
	root.AddMember("bulletSpinSpeed",this->getBulletSpinSpeed(),allocator);
	root.AddMember("bulletSpinSpeedVar",this->getBulletSpinSpeedVar(),allocator);
	root.AddMember("damage",this->getDamage(),allocator);
	root.AddMember("isFollow",this->getIsFollow(),allocator);
	root.AddMember("curvity",this->getCurvity(),allocator);
	root.AddMember("sinAmplitude",this->getSinAmplitude(),allocator);
	root.AddMember("sinRate",this->getSinRate(),allocator);

	std::string workPath = EditorConfig::shareEditorConfig()->getWorkPath();
	std::string file = this->m_pDisplayConfig->displayFile;

	if(file.find(workPath) != std::string::npos)
	{
		file = file.substr(workPath.length()+1,file.length()-1);
	}

	rapidjson::Value display; 
	display.SetObject(); 
	{
		display.AddMember("file",file.c_str(),allocator);
		display.AddMember("type",this->m_pDisplayConfig->displayType,allocator);
		display.AddMember("rotation",this->m_pDisplayConfig->displayRoation,allocator);
		display.AddMember("scale",this->m_pDisplayConfig->displayScale,allocator);
		display.AddMember("blend_src",this->m_pDisplayConfig->displaySrcBlend,allocator);
		display.AddMember("blend_dst",this->m_pDisplayConfig->displayDstBlend,allocator);
		display.AddMember("atlas",this->m_pDisplayConfig->atlasName.c_str(),allocator);
		display.AddMember("armature",this->m_pDisplayConfig->armatureName.c_str(),allocator);
		display.AddMember("animation",this->m_pDisplayConfig->animationName.c_str(),allocator);
		display.AddMember("animateSpeed",this->m_pDisplayConfig->displayAnimSpeed,allocator);
		rapidjson::Value frames(rapidjson::kArrayType);
		//frames.SetArray();
		{
			//std::vector<std::string> frameList = this->m_pDisplayConfig->frames;
			for (int i = 0;i<this->m_pDisplayConfig->frames.size();i++)
			{
				frames.PushBack(this->m_pDisplayConfig->frames[i].c_str(),allocator);
			}			
		}
		display.AddMember("frames",frames,allocator);
	}
	root.AddMember("display",display,allocator);

	rapidjson::StringBuffer buffer;
	rapidjson::Writer<rapidjson::StringBuffer> write(buffer);
	root.Accept(write);

	//CCLog("Json:%s",buffer.GetString());

	FILE* fp = fopen(configFile,"w");
	fwrite(buffer.GetString(),buffer.Size(),1,fp);
	fclose(fp);

	return true;
}

bool EmitterConfig::unserialization(const char* configFile)
{
	CCFileUtils* fileUtils = CCFileUtils::sharedFileUtils();
	if(fileUtils->isFileExist(configFile))
	{
		unsigned char *pBytes = NULL;
		rapidjson::Document jsonDict;
		unsigned long size = 0;
		pBytes = CCFileUtils::sharedFileUtils()->getFileData(configFile,"r" , &size);

		std::string load_str = std::string((const char *)pBytes, size);
		jsonDict.Parse<0>(load_str.c_str());

		if (jsonDict.HasParseError())
		{
			CCLog("GetParseError %s\n",jsonDict.GetParseError());

			return false;
		}

		float duration =  DICTOOL->getFloatValue_json(jsonDict,"duration",-1);
		float durationVar =  DICTOOL->getFloatValue_json(jsonDict,"durationVar",0);
		float interimTime = DICTOOL->getFloatValue_json(jsonDict,"interimTime",0);
		float interimTimeVar = DICTOOL->getFloatValue_json(jsonDict,"interimTimeVar",0);
		float lifeTime =  DICTOOL->getFloatValue_json(jsonDict,"lifeTime",-1);
		float lifeTimeVar =  DICTOOL->getFloatValue_json(jsonDict,"lifeTimeVar",0);
		int   initCount =  DICTOOL->getIntValue_json(jsonDict,"initCount",50);
		float speed = DICTOOL->getFloatValue_json(jsonDict,"speed",500);
		float speedVar = DICTOOL->getFloatValue_json(jsonDict,"speedVar",0);
		float speedDecay = DICTOOL->getFloatValue_json(jsonDict,"speedDecay",0);
		float speedLimit = DICTOOL->getFloatValue_json(jsonDict,"speedLimit",0);
		int emitterMode = DICTOOL->getIntValue_json(jsonDict,"emitterMode",0);
		int positionType = DICTOOL->getIntValue_json(jsonDict,"positionType",0);
		bool isPointToTarget = DICTOOL->getBooleanValue_json(jsonDict,"isPointToTarget",false);
		float emissionRate = DICTOOL->getFloatValue_json(jsonDict,"emissionRate",5);
		float emissionAngle = DICTOOL->getFloatValue_json(jsonDict,"emissionAngle",0);
		int beamCount = DICTOOL->getIntValue_json(jsonDict,"beamCount",1);
		int beamCountVar = DICTOOL->getIntValue_json(jsonDict,"beamCountVar",0);
		float fieldAngle = DICTOOL->getFloatValue_json(jsonDict,"fieldAngle",0);
		float fieldAngleVar = DICTOOL->getFloatValue_json(jsonDict,"fieldAngleVar",0);
		float spinSpeed = DICTOOL->getFloatValue_json(jsonDict,"spinSpeed",0);
		float bulletSpinSpeed = DICTOOL->getFloatValue_json(jsonDict,"bulletSpinSpeed",0);
		float bulletSpinSpeedVar = DICTOOL->getFloatValue_json(jsonDict,"bulletSpinSpeedVar",0);
		float damage = DICTOOL->getFloatValue_json(jsonDict,"damage",0);
		bool  isFollow = DICTOOL->getBooleanValue_json(jsonDict,"isFollow",false);
		float curvity  = DICTOOL->getFloatValue_json(jsonDict,"curvity",0.1);
		float sinAmplitude = DICTOOL->getFloatValue_json(jsonDict,"sinAmplitude",0);
		float sinRate = DICTOOL->getFloatValue_json(jsonDict,"sinRate",0);

		this->setDuration(duration);
		this->setDurationVar(durationVar);
		this->setInterimTime(interimTime);
		this->setInterimTimeVar(interimTimeVar);
		this->setLifeTime(lifeTime);
		this->setLifeTimeVar(lifeTimeVar);
		this->setInitCount(initCount);
		this->setSpeed(speed);
		this->setSpeedVar(speedVar);
		this->setSpeedDecay(speedDecay);
		this->setSpeedLimit(speedLimit);
		this->setPositionType((PositionType)positionType);
		this->setIsPointToTarget(isPointToTarget);
		this->setEmitterMode(emitterMode);
		this->setEmissionRate(emissionRate);
		this->setEmissionAngle(emissionAngle);
		this->setBeamCount(beamCount);
		this->setBeamCountVar(beamCountVar);
		this->setFieldAngle(fieldAngle);
		this->setFieldAngleVar(fieldAngleVar);
		this->setSpinSpeed(spinSpeed);
		this->setBulletSpinSpeed(bulletSpinSpeed);
		this->setBulletSpinSpeedVar(bulletSpinSpeedVar);
		this->setDamage(damage);
		this->setIsFollow(isFollow);
		this->setCurvity(curvity);
		this->setSinAmplitude(sinAmplitude);
		this->setSinRate(sinRate);
		//display config
		const rapidjson::Value&  display = DICTOOL->getSubDictionary_json(jsonDict,"display");
		{
			DisplayConfig* displayConfig = new DisplayConfig();
			const char* file = DICTOOL->getStringValue_json(display,"file");
			int _type = DICTOOL->getIntValue_json(display,"type");
			float rotation = DICTOOL->getFloatValue_json(display,"rotation");
			float scale = DICTOOL->getFloatValue_json(display,"scale");
			int blend_src = DICTOOL->getIntValue_json(display,"blend_src");
			int blend_dst = DICTOOL->getIntValue_json(display,"blend_dst");

			displayConfig->displayFile = file;
			displayConfig->displayType = _type;
			displayConfig->displayRoation = rotation;
			displayConfig->displayScale = scale;
			displayConfig->displaySrcBlend = (BlendValue)blend_src;
			displayConfig->displayDstBlend = (BlendValue)blend_dst;
			displayConfig->atlasName = DICTOOL->getStringValue_json(display,"atlas");
			displayConfig->armatureName = DICTOOL->getStringValue_json(display,"armature");
			displayConfig->animationName = DICTOOL->getStringValue_json(display,"animation");
			displayConfig->displayAnimSpeed = DICTOOL->getFloatValue_json(display,"animateSpeed",10);

			for (int i=0;i<DICTOOL->getArrayCount_json(display,"frames");i++)
			{
				displayConfig->frames.push_back(DICTOOL->getStringValueFromArray_json(display,"frames",i));
			}


			this->setDisplayConfig(displayConfig);

		}
		EditorConfig::shareEditorConfig()->setCurrentFile(configFile);
		CC_SAFE_DELETE_ARRAY(pBytes);

		return true;
	}
	return false;
}

bool EmitterConfig::seriCurrent()
{
	std::string curfile =  EditorConfig::shareEditorConfig()->getCurrentFile();
	if(curfile!="")
	{
		return serialization(curfile.c_str());
	}
	return false;
}
bool EmitterConfig::unseriCurrent()
{
	std::string curfile =  EditorConfig::shareEditorConfig()->getCurrentFile();
	if(CCFileUtils::sharedFileUtils()->isFileExist(curfile.c_str()))
	{
		return unserialization(curfile.c_str());
	}
	return false;
}

int EmitterConfig::checkDisplayType(const char* fileName)
{
	std::string fn = fileName;
	if(fn.find(".png")!=std::string::npos)
	{
		return dTypeSprite;
	}
	else if(fn.find(".plist") != std::string::npos)
	{
		CCDictionary* plistDic = CCDictionary::createWithContentsOfFile(fileName);

		if(plistDic->objectForKey("frames"))
		{
			return dTypeSpriteFrame;
		}
		else
		{
			return dTypeParticle;
		}
	}
	else if(fn.find(".ExportJson")!= std::string::npos)
	{
		return dTypeArmature;
	}
	else
	{
		return dTypeSprite;
	}
}

std::string EmitterConfig::getAtlasImageFile(const char* fileName)
{
	std::string img;
	CCDictionary* plistDic = CCDictionary::createWithContentsOfFile(fileName);
	CCDictionary *metadataDict = (CCDictionary*)plistDic->objectForKey("metadata");

	if(metadataDict)
	{
		CCString* imgFile = dynamic_cast<CCString*>(metadataDict->objectForKey("realTextureFileName"));
		img = imgFile->getCString();
	}
	return img;
}

std::vector<FrameData> EmitterConfig::getDisplayFrameDatas()
{
	return getDisplayFrameDatas(this->m_pDisplayConfig->displayFile.c_str());
}

std::vector<FrameData> EmitterConfig::getDisplayFrameDatas(const char* fileName)
{
	std::vector<FrameData> frameDatas;
	CCDictionary* plistDic = CCDictionary::createWithContentsOfFile(fileName);
	if(plistDic)
	{
		CCDictionary *metadataDict = (CCDictionary*)plistDic->objectForKey("metadata");
		CCDictionary *framesDict = (CCDictionary*)plistDic->objectForKey("frames");

		int format = metadataDict->valueForKey("format")->intValue();

		// check the format
		CCAssert(format >=0 && format <= 3, "format is not supported for CCSpriteFrameCache addSpriteFramesWithDictionary:textureFilename:");

		CCDictElement* pElement = NULL;
		CCDICT_FOREACH(framesDict, pElement)
		{
			FrameData frameData;
			CCDictionary* frameDict = (CCDictionary*)pElement->getObject();
			std::string spriteFrameName = pElement->getStrKey();
			//CCLog("key:%s",spriteFrameName.c_str());
			frameData.name = spriteFrameName;

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

				frameData.rect.x = ow;
				frameData.rect.y = oh;
				frameData.rect.width = w;
				frameData.rect.height = h;

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

				frameData.rect.x = frame.origin.x + offset.x;
				frameData.rect.y = frame.origin.y + offset.y;

				if(rotated)
				{
					frameData.rect.width = frame.size.height;
					frameData.rect.height = frame.size.width;
				}
				else
				{
					frameData.rect.width = frame.size.width;
					frameData.rect.height = frame.size.height;
				}

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

				frameData.rect.x = spriteOffset.x;
				frameData.rect.y =spriteOffset.y;
				frameData.rect.width = spriteSourceSize.width;
				frameData.rect.height = spriteSourceSize.height;
			}

			frameDatas.push_back(frameData);
		}	
	}

	return frameDatas;
}

std::vector<std::string> EmitterConfig::getArmatureAnimations()
{
	CCArmatureDataManager::sharedArmatureDataManager()->addArmatureFileInfo(this->m_pDisplayConfig->displayFile.c_str());
	CCArmature* armature = CCArmature::create(this->m_pDisplayConfig->armatureName.c_str());
	CCArmatureAnimation* animition =  armature->getAnimation();
	CCAnimationData* animData = animition->getAnimationData();

	return animData->movementNames;
}

bool EmitterConfig::isFileExist(const char* file)
{
	return CCFileUtils::sharedFileUtils()->isFileExist(file);
}

void EmitterConfig::resetDisplay()
{
	BulletDisplay* display = this->m_pDisplayConfig->create();
	BulletDisplay* oldDisplay = MainScene::sharedMainScene()->getBulletEmitter()->getDisplay();

	this->setSrcBlend((BlendValue)display->getSrcBlend());
	this->setDstBlend((BlendValue)display->getDstBlend());

	this->setDisplayRotation(display->getRotationOffset());
	this->setDisplayScale(display->getScale());
	this->setAnimateSpeed(display->getAnimateSpeed());

	if(oldDisplay)
	{
		oldDisplay->release();
	}
	MainScene::sharedMainScene()->getBulletEmitter()->setDisplay(display->clone());

	display->release();
}

//////////////////////////////////////////////////////////////////////////
void EmitterConfig::setDuration(float value)
{
	m_fDuration = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setDuration(value);
	MainScene::sharedMainScene()->getBulletEmitter()->resetSystem();
}
float EmitterConfig::getDuration()
{
	return m_fDuration;
}
void EmitterConfig::setDurationVar(float value)
{
	m_fDurationVar = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setDurationVar(value);
	MainScene::sharedMainScene()->getBulletEmitter()->resetSystem();
}
float EmitterConfig::getDurationVar()
{
	return m_fDurationVar;
}

void EmitterConfig::setInterimTime(float var)
{
	m_fInterimTime = var;
	MainScene::sharedMainScene()->getBulletEmitter()->setInterimTime(var);
	//MainScene::sharedMainScene()->getBulletEmitter()->resetSystem();
}
float EmitterConfig::getInterimTime()
{
	return m_fInterimTime;
}

void EmitterConfig::setInterimTimeVar(float var)
{
	m_fInterimTimeVar = var;
	MainScene::sharedMainScene()->getBulletEmitter()->setInterimTimeVar(var);
	//MainScene::sharedMainScene()->getBulletEmitter()->resetSystem();
}
float EmitterConfig::getInterimTimeVar()
{
	return m_fInterimTimeVar;
}

void EmitterConfig::setInitCount(int value)
{
	m_iInitCount = value;
}
int EmitterConfig::getInitCount()
{
	return m_iInitCount;
}

void EmitterConfig::setEmitterMode(int value)
{
	m_eEmitterMode = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setEmitterMode((BulletMode)value);
}
int EmitterConfig::getEmitterMode()
{
	return m_eEmitterMode;
}

void EmitterConfig::setPositionType(PositionType value)
{
	m_ePositionType = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setPositionType((tCCPositionType)value);
}
PositionType EmitterConfig::getPositionType()
{
	return m_ePositionType;
}

void EmitterConfig::setIsPointToTarget(bool var)
{
	m_bIsPointToTarget = var;
}
bool EmitterConfig::getIsPointToTarget()
{
	return m_bIsPointToTarget;
}

void EmitterConfig::setEmissionRate(float value)
{
	m_fEmissionRate = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setEmissionRate(value);
}
float EmitterConfig::getEmissionRate()
{
	return m_fEmissionRate;
}

void EmitterConfig::setEmissionRateVar(float value)
{
	m_fEmissionRateVar = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setEmissionRateVar(value);
}
float EmitterConfig::getEmissionRateVar()
{
	return m_fEmissionRateVar;
}

void EmitterConfig::setEmissionAngle(float value)
{
	m_fEmissionAngle = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setEmissionAngle(value);
}
float EmitterConfig::getEmissionAngle()
{
	return m_fEmissionAngle;
}

void EmitterConfig::setBeamCount(unsigned int value)
{
	m_uBeamCount = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setBeamCount(value);
}
unsigned int EmitterConfig::getBeamCount()
{
	return m_uBeamCount;
}

void EmitterConfig::setBeamCountVar(unsigned int value)
{
	m_uBeamCountVar = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setBeamCountVar(value);
}
unsigned int EmitterConfig::getBeamCountVar()
{
	return m_uBeamCountVar;
}

void EmitterConfig::setFieldAngle(float value)
{
	m_fFieldAngle = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setFieldAngle(value);
}
float EmitterConfig::getFieldAngle()
{
	return m_fFieldAngle;
}

void EmitterConfig::setFieldAngleVar(float value)
{
	m_fFieldAngleVar = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setFieldAngleVar(value);
}
float EmitterConfig::getFieldAngleVar()
{
	return m_fFieldAngleVar;
}

void EmitterConfig::setSpinSpeed(float value)
{
	m_fSpinSpeed = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setSpinSpeed(value);
}
float EmitterConfig::getSpinSpeed()
{
	return m_fSpinSpeed;
}
//////////////////////////////////////////////////////////////////////////
void EmitterConfig::setSpeed(float value)
{
	m_fSpeed = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setSpeed(value);
}
float EmitterConfig::getSpeed()
{
	return m_fSpeed;
}

void EmitterConfig::setSpeedVar(float value)
{
	m_fSpeedVar = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setSpeedVar(value);
}
float EmitterConfig::getSpeedVar()
{
	return m_fSpeedVar;
}

void EmitterConfig::setSpeedDecay(float var)
{
	m_fSpeedDecay = var;
	MainScene::sharedMainScene()->getBulletEmitter()->setSpeedDecay(var);
}
float EmitterConfig::getSpeedDecay()
{
	return m_fSpeedDecay;
}

void EmitterConfig::setSpeedLimit(float var)
{
	m_fSpeedLimit = var;
	MainScene::sharedMainScene()->getBulletEmitter()->setSpeedLimit(var);
}
float EmitterConfig::getSpeedLimit()
{
	return m_fSpeedLimit;
}

void EmitterConfig::setBulletSpinSpeed(float var)
{
	m_fBulletSpinSpeed = var;
	MainScene::sharedMainScene()->getBulletEmitter()->setBulletSpinSpeed(var);
}
float EmitterConfig::getBulletSpinSpeed()
{
	return m_fBulletSpinSpeed;
}

void EmitterConfig::setBulletSpinSpeedVar(float var)
{
	m_fBulletSpinSpeedVar = var;
	MainScene::sharedMainScene()->getBulletEmitter()->setBulletSpinSpeedVar(var);
}
float EmitterConfig::getBulletSpinSpeedVar()
{
	return m_fBulletSpinSpeedVar;
}

void EmitterConfig::setLifeTime(float var)
{
	m_fLifeTime = var;
	MainScene::sharedMainScene()->getBulletEmitter()->setLifeTime(var);
}
float EmitterConfig::getLifeTime()
{
	return m_fLifeTime;
}
void EmitterConfig::setLifeTimeVar(float var)
{
	m_fLifeTimeVar = var;
	MainScene::sharedMainScene()->getBulletEmitter()->setLifeTimeVar(var);
}
float EmitterConfig::getLifeTimeVar()
{
	return m_fLifeTimeVar;
}

void EmitterConfig::setDamage(float value)
{
	m_fDamage = value;
	MainScene::sharedMainScene()->getBulletEmitter()->setDamage(value);
}
float EmitterConfig::getDamage()
{
	return m_fDamage;
}

void EmitterConfig::setIsFollow(bool var)
{
	m_bIsFollow = var;
}
bool EmitterConfig::getIsFollow()
{
	return m_bIsFollow;
}

void EmitterConfig::setCurvity(float var)
{
	m_fCurvity = var;
}
float EmitterConfig::getCurvity()
{
	return m_fCurvity;
}

void EmitterConfig::setSinAmplitude(float var)
{
	m_fSinAmplitude = var;
	MainScene::sharedMainScene()->getBulletEmitter()->setSinAmplitude(var);
}
float EmitterConfig::getSinAmplitude()
{
	return m_fSinAmplitude;
}
void EmitterConfig::setSinRate(float var)
{
	m_fSinRate = var;
	MainScene::sharedMainScene()->getBulletEmitter()->setSinRate(var);
}
float EmitterConfig::getSinRate()
{
	return m_fSinRate;
}
//////////////////////////////////////////////////////////////////////////
void EmitterConfig::setDisplayConfig(DisplayConfig* var)
{
	m_pDisplayConfig = var;
}
DisplayConfig* EmitterConfig::getDisplayConfig()
{
	return m_pDisplayConfig;
}

void EmitterConfig::setSrcBlend(BlendValue var)
{
	m_eSrcBlend = var;
	m_pDisplayConfig->displaySrcBlend = var;
	BulletDisplay* display = MainScene::sharedMainScene()->getBulletEmitter()->getDisplay();
	if(display)
	{
		display->setBlendFuc(var,m_eDstBlend);
	}
}
BlendValue EmitterConfig::getSrcBlend()
{
	return m_eSrcBlend;
}

void EmitterConfig::setDstBlend(BlendValue var)
{
	m_eDstBlend = var;
	m_pDisplayConfig->displayDstBlend = var;
	BulletDisplay* display = MainScene::sharedMainScene()->getBulletEmitter()->getDisplay();
	if(display)
	{
		display->setBlendFuc(m_eSrcBlend,var);
	}
}
BlendValue EmitterConfig::getDstBlend()
{
	return m_eDstBlend;
}

void EmitterConfig::setDisplayRotation(float var)
{
	m_fDisplayRotation = var;
	m_pDisplayConfig->displayRoation = var;
	BulletDisplay* display = MainScene::sharedMainScene()->getBulletEmitter()->getDisplay();
	if(display)
	{
		display->setRotationOffset(var);
	}
}
float EmitterConfig::getDisplayRotation()
{
	return m_fDisplayRotation;
}

void EmitterConfig::setDisplayScale(float var)
{
	m_fDisplayScale = var;
	m_pDisplayConfig->displayScale = var;
	BulletDisplay* display = MainScene::sharedMainScene()->getBulletEmitter()->getDisplay();
	if(display)
	{
		display->setScale(var);
	}

}
float EmitterConfig::getDisplayScale()
{
	return m_fDisplayScale;
}

void EmitterConfig::setAnimateSpeed(float var)
{
	m_fAnimateSpeed = var;
	m_pDisplayConfig->displayAnimSpeed = var;
// 	BulletDisplay* display = MainScene::sharedMainScene()->getBulletEmitter()->getDisplay();
// 	if(display)
// 	{
// 		display->setAnimateSpeed(var);
// 	}
}
float EmitterConfig::getAnimateSpeed()
{
	return m_fAnimateSpeed;
}

void EmitterConfig::setAnimateFrames(std::vector<std::string> var)
{
	m_vAnimateFrames = var;
	m_pDisplayConfig->frames = var;
}

std::vector<std::string> EmitterConfig::getAnimateFrames()
{
	return m_vAnimateFrames;
}
//////////////////////////////////////////////////////////////////////////