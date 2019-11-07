
#include "BulletDisplay.h"
#include "EmitterConfig.h"

//////////////////////////////////////////////////////////////////////////
BulletDisplay::BulletDisplay()
{
	this->setTag(this->getID());

	m_bBatch = false;
	m_fRotationOffset = 0;
	m_fAnimateSpeed = 10;

	mSrc = GL_SRC_ALPHA;
	mDst = GL_ONE_MINUS_SRC_ALPHA;

	displayNode = NULL;
}
BulletDisplay::~BulletDisplay()
{

}

void BulletDisplay::setType(tDisplayType value)
{
	m_eType = value;
}
tDisplayType BulletDisplay::getType()
{
	return m_eType;
}

void  BulletDisplay::setUsingBatch(bool value)
{
	m_bBatch = value;
}
bool BulletDisplay::getUsingBatch()
{
	return m_bBatch;
}

void BulletDisplay::setRotationOffset(float var)
{
	m_fRotationOffset = var;
	displayNode->setRotation(m_fRotationOffset);
}
float BulletDisplay::getRotationOffset()
{
	return m_fRotationOffset;
}

void BulletDisplay::setAnimateSpeed(float var)
{
	m_fAnimateSpeed = var;
}
float BulletDisplay::getAnimateSpeed()
{
	return m_fAnimateSpeed;
}

CCSpriteBatchNode* BulletDisplay::createBatchNode()
{
	return NULL;
}

int BulletDisplay::getSrcBlend()
{
	return mSrc;
}
int BulletDisplay::getDstBlend()
{
	return mDst;
}

std::string BulletDisplay::plistTexturePath(const char* pszPlist)
{
	CCAssert(pszPlist, "plist filename should not be NULL");

	std::string fullPath = CCFileUtils::sharedFileUtils()->fullPathForFilename(pszPlist);
	CCDictionary *dict = CCDictionary::createWithContentsOfFileThreadSafe(fullPath.c_str());

	std::string texturePath;

	CCDictionary* metadataDict = (CCDictionary*)dict->objectForKey("metadata");
	if (metadataDict)
	{
		// try to read  texture file name from meta data
		texturePath = metadataDict->valueForKey("textureFileName")->getCString();
	}

	if (! texturePath.empty())
	{
		// build texture path relative to plist file
		texturePath = CCFileUtils::sharedFileUtils()->fullPathFromRelativeFile(texturePath.c_str(), pszPlist);
	}
	else
	{
		// build texture path by replacing file extension
		texturePath = pszPlist;

		// remove .xxx
		size_t startPos = texturePath.find_last_of("."); 
		texturePath = texturePath.erase(startPos);

		// append .png
		texturePath = texturePath.append(".png");

		CCLOG("cocos2d: CCSpriteFrameCache: Trying to use file %s as texture", texturePath.c_str());
	}
	//dict->release();
	return texturePath;

}

BulletDisplay* BulletDisplay::create(BulletDisplay* display)
{
	if (display&&display->init())
	{
		display->autorelease();
		return display;
	} 
	else
	{
		delete display; 
		display = NULL; 
		return NULL; 
	}
	return display;
}

//
void BulletDisplay::setScale(float scale)
{
	if(m_bBatch&&displayNode)
		((CCSprite*)displayNode)->setScale(scale);
	else
		CCNode::setScale(scale);
}
float BulletDisplay::getScale()
{
	if(m_bBatch&&displayNode)
		return ((CCSprite*)displayNode)->getScale(); 
	else
		return CCNode::getScale();
}

void BulletDisplay::setPosition(const CCPoint &position)
{
	if(m_bBatch&&displayNode)
		((CCSprite*)displayNode)->setPosition(position);
	else
		CCNode::setPosition(position);
}
const CCPoint& BulletDisplay::getPosition()
{
	if(m_bBatch&&displayNode)
		return ((CCSprite*)displayNode)->getPosition();
	else
		return CCNode::getPosition();

}

void BulletDisplay::setRotation(float fRotation)
{
	if(m_bBatch&&displayNode)
		((CCSprite*)displayNode)->setRotation(fRotation);
	else
		CCNode::setRotation(fRotation);
}
float BulletDisplay::getRotation()
{
	if(m_bBatch&&displayNode)
		return ((CCSprite*)displayNode)->getRotation();
	else
		return CCNode::getRotation();
}

void BulletDisplay::setVisible(bool visible)
{
	if(m_bBatch&&displayNode)
		((CCSprite*)displayNode)->setVisible(visible);
	else
		CCNode::setVisible(visible);
}
bool BulletDisplay::isVisible()
{
	if(m_bBatch&&displayNode)
		return ((CCSprite*)displayNode)->isVisible();
	else
		return CCNode::isVisible();
}

void BulletDisplay::setZOrder(int zOrder)
{
	if(m_bBatch&&displayNode)
		((CCSprite*)displayNode)->setZOrder(zOrder);
	else
		CCNode::setZOrder(zOrder);
}
int BulletDisplay::getZOrder()
{
	if(m_bBatch&&displayNode)
		return ((CCSprite*)displayNode)->getZOrder();
	else
		return CCNode::getZOrder();
}
//
//////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////
SpriteDisplay::SpriteDisplay(const char* spriteFileName)
{
	spritePath = spriteFileName;
	this->setType(dTypeSprite);
	this->displayNode = CCSprite::create(spriteFileName);
	if(this->displayNode)this->addChild(this->displayNode);
}

SpriteDisplay::SpriteDisplay(const char* plisFileName, const char* spriteFileName)
{
	plistFile = plisFileName;
	spritePath = spriteFileName;
	this->setType(dTypeSpriteFrame);
	CCSpriteFrameCache::sharedSpriteFrameCache()->addSpriteFramesWithFile(plisFileName);
	this->displayNode = CCSprite::createWithSpriteFrameName(spriteFileName);

	//m_bBatch = true;

	if(this->displayNode)this->addChild(this->displayNode);
}

SpriteDisplay::~SpriteDisplay()
{

}

SpriteDisplay* SpriteDisplay::create(const char* spriteFileName)
{
	SpriteDisplay* pRet = new SpriteDisplay(spriteFileName);
	return (SpriteDisplay*)BulletDisplay::create(pRet);
}
SpriteDisplay* SpriteDisplay::create(const char* plisFileName, const char* spriteFileName)
{
	SpriteDisplay* pRet = new SpriteDisplay(plisFileName,spriteFileName);
	return (SpriteDisplay*)BulletDisplay::create(pRet);
}

CCSpriteBatchNode* SpriteDisplay::createBatchNode()
{
	std::string textPath = plistTexturePath(plistFile);
	if(CCFileUtils::sharedFileUtils()->isFileExist(textPath.c_str()));
	return CCSpriteBatchNode::create(textPath.c_str());
}

void SpriteDisplay::setBlendFuc(int src,int dst)
{
	ccBlendFunc blend;
	mSrc = src;
	mDst = dst;
	blend.src = src;
	blend.dst = dst;

	((CCSprite*)displayNode)->setBlendFunc(blend);
}

BulletDisplay* SpriteDisplay::clone()
{
	BulletDisplay* display = 0;
	if (m_eType == dTypeSprite)
	{
		display = new SpriteDisplay(this->spritePath);
	}
	else if (m_eType == dTypeSpriteFrame)
	{
		display = new SpriteDisplay(this->plistFile, this->spritePath);
	}

	display->setBlendFuc(this->mSrc,this->mDst);
	display->setRotation(this->getRotation());
	display->setRotationOffset(this->getRotationOffset());
	display->setScale(this->getScale());
	display->setTag(this->getTag());
	return display;
}

//////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////
ParticleDisplay::ParticleDisplay(const char* particlePath)
{
	this->particleFile = particlePath;
	this->particle = CCParticleSystemQuad::create(particleFile);
	this->displayNode = particle;
	particle->setPositionType(kCCPositionTypeFree);
	particle->setPosition(0,0);
	this->setType(dTypeParticle);
	this->addChild(particle);
}

ParticleDisplay::~ParticleDisplay()
{
	this->removeChild(particle);

	//this->particle->release();
}

ParticleDisplay* ParticleDisplay::create(const char* particlePath)
{
	ParticleDisplay* display = new ParticleDisplay(particlePath);
	return (ParticleDisplay*)BulletDisplay::create(display);
}

void ParticleDisplay::setRotation(float fRotation)
{

}
void ParticleDisplay::setRotationOffset(float fRotation)
{

}

void ParticleDisplay::setBlendFuc(int src,int dst)
{
	ccBlendFunc blend;
	mSrc = src;
	mDst = dst;
	blend.src = src;
	blend.dst = dst;

	((CCParticleSystemQuad*)displayNode)->setBlendFunc(blend);
}

void ParticleDisplay::play(bool loop)
{
	if (!particle->isActive())
	{
		particle->resetSystem();
	}
}

BulletDisplay* ParticleDisplay::clone()
{
	BulletDisplay* display = new ParticleDisplay(this->particleFile);
	display->setBlendFuc(this->mSrc,this->mDst);
	display->setRotation(this->getRotation());
	display->setRotationOffset(this->getRotationOffset());
	display->setScale(this->getScale());
	display->setTag(this->getTag());
	return display;
}
//////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////
AnimationDisplay::AnimationDisplay(const char* plistFileName,std::vector<std::string> frames)
{
	plistFile = plistFileName;
	frameNames = frames;
	this->setType(dTypeAnimation);
	CCSpriteFrameCache::sharedSpriteFrameCache()->addSpriteFramesWithFile(plistFileName);
	spriteNode = CCSprite::create();
	animation = CCAnimation::create();
	displayNode = spriteNode;
	for (int i=0;i<frames.size();i++)
	{
		CCSpriteFrame* frame = CCSpriteFrameCache::sharedSpriteFrameCache()->spriteFrameByName(frames[i].c_str());
		animation->addSpriteFrame(frame);
	}

	animation->setDelayPerUnit(1.0f / m_fAnimateSpeed);
	animation->setRestoreOriginalFrame(true);

	this->addChild(spriteNode);
}
AnimationDisplay::~AnimationDisplay()
{
	this->removeChild(spriteNode);

	//animation->release();
	//spriteNode->release();
}

AnimationDisplay* AnimationDisplay::create(const char* plistFileName,std::vector<std::string> frames)
{
	AnimationDisplay* display = new AnimationDisplay(plistFileName,frames);
	return (AnimationDisplay*)BulletDisplay::create(display);
}

void AnimationDisplay::setAnimateSpeed(float var)
{
	BulletDisplay::setAnimateSpeed(var);
	animation->setDelayPerUnit(1.0f / m_fAnimateSpeed);
}

void AnimationDisplay::setBlendFuc(int src,int dst)
{
	ccBlendFunc blend;
	mSrc = src;
	mDst = dst;
	blend.src = src;
	blend.dst = dst;

	spriteNode->setBlendFunc(blend);
}
void AnimationDisplay::play(bool loop)
{	
	/*animation->setRestoreOriginalFrame(true);*/
	CCAnimate* action = CCAnimate::create(animation);
	spriteNode->stopAllActions();
	if(loop)
		spriteNode->runAction(CCRepeatForever::create(action));
	else
		spriteNode->runAction(action);	
}
BulletDisplay* AnimationDisplay::clone()
{
	AnimationDisplay* display = new AnimationDisplay(this->plistFile,frameNames);
	display->setBlendFuc(this->mSrc,this->mDst);
	display->setRotation(this->getRotation());
	display->setRotationOffset(this->getRotationOffset());
	display->setScale(this->getScale());
	display->setTag(this->getTag());
	display->setAnimateSpeed(this->getAnimateSpeed());
	return display;
}

//////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////
ArmatureDisplay::ArmatureDisplay(const char* armaName, const char* animName)
{
	armatureName = armaName;
	animationName = animName;

	armature = CCArmature::create(armatureName);
	animtion = armature->getAnimation();
	displayNode = armature;
	this->addChild(armature);
}

ArmatureDisplay::ArmatureDisplay(const char* armaFile, const char* armaName, const char* animName)
{
	armatureFile = armaFile;
	armatureName = armaName;
	animationName = animName;

	CCArmatureDataManager::sharedArmatureDataManager()->addArmatureFileInfo(armatureFile);
	armature = CCArmature::create(armatureName);
	animtion = armature->getAnimation();
	displayNode = armature;
	this->addChild(armature);
}

ArmatureDisplay::~ArmatureDisplay()
{

}

ArmatureDisplay* ArmatureDisplay::create(const char* armaName, const char* animName)
{
	ArmatureDisplay* display = new ArmatureDisplay(armaName,animName);
	return (ArmatureDisplay*)BulletDisplay::create(display);
}

ArmatureDisplay* ArmatureDisplay::create(const char* armaFile,const char* armaName, const char* animName)
{
	ArmatureDisplay* display = new ArmatureDisplay(armaFile,armaName,animName);
	return (ArmatureDisplay*)BulletDisplay::create(display);
}

void ArmatureDisplay::setBlendFuc(int src,int dst)
{
	ccBlendFunc blend;
	mSrc = src;
	mDst = dst;
	blend.src = src;
	blend.dst = dst;

	((CCArmature*)displayNode)->setBlendFunc(blend);
}

void ArmatureDisplay::play(bool loop)
{
	animtion->play(animationName, loop);
}
BulletDisplay* ArmatureDisplay::clone()
{
	BulletDisplay* display = 0;
	if (!armatureFile || armatureFile == "")
	{
		display = new ArmatureDisplay(armatureName, animationName);
	}
	else
	{
		display = new ArmatureDisplay(armatureFile, armatureName, animationName);
	}
	display->setBlendFuc(this->mSrc,this->mDst);
	display->setRotation(this->getRotation());
	display->setRotationOffset(this->getRotationOffset());
	display->setScale(this->getScale());
	display->setTag(this->getTag());
	return display;
}
//////////////////////////////////////////////////////////////////////////