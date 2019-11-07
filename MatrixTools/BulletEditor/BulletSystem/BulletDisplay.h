
#ifndef __BULLETDISPLAY_H__
#define __BULLETDISPLAY_H__

#include "cocos2d.h"
#include "cocos-ext.h"

USING_NS_CC;
USING_NS_CC_EXT;


typedef enum DisplayType
{
	/**普通精灵*/
	dTypeSprite = 0,
	/**精灵帧*/
	dTypeSpriteFrame,
	/**粒子*/
	dTypeParticle,
	/**cocos动画*/
	dTypeAnimation,
	/**cocostudio动画*/
	dTypeArmature
}tDisplayType;


class BulletDisplay:public CCNode
{

public:
	BulletDisplay();
	~BulletDisplay();

public:
	virtual CCNode* getDisplayNode()
	{
		return displayNode;
	}

	CC_PROPERTY(tDisplayType,m_eType,Type);
	CC_PROPERTY(bool,m_bBatch,UsingBatch);
	CC_PROPERTY(float,m_fRotationOffset,RotationOffset);
	CC_PROPERTY(float,m_fAnimateSpeed,AnimateSpeed);

	virtual CCSpriteBatchNode* createBatchNode();

	virtual void play()
	{
		this->play(true);
	}

	virtual void setBlendFuc(int src,int dst) = 0;
	virtual int  getSrcBlend();
	virtual int  getDstBlend();

	virtual void play(bool loop) = 0;
	virtual BulletDisplay* clone() = 0;
	
	virtual void setScale(float scale);
	virtual float getScale();

	virtual void setPosition(const CCPoint &position);
	virtual const CCPoint& getPosition();

	virtual void setRotation(float fRotation);
	virtual float getRotation();

	virtual void setVisible(bool visible);
	virtual bool isVisible();

	virtual void setZOrder(int zOrder);
	virtual int getZOrder();
	
	//
protected:
	virtual std::string plistTexturePath(const char* plisFileName);
	static BulletDisplay* create(BulletDisplay* display);

protected:
	CCNode* displayNode;
	tDisplayType displayType;

	int mSrc;
	int mDst;
private:
};

class SpriteDisplay:public BulletDisplay
{

public:
	SpriteDisplay(const char* spriteFileName);
	SpriteDisplay(const char* plisFileName, const char* spriteFileName);
	~SpriteDisplay();


	static SpriteDisplay* create(const char* spriteFileName);
	static SpriteDisplay* create(const char* plisFileName, const char* spriteFileName);

	virtual CCSpriteBatchNode* createBatchNode();
	virtual void setBlendFuc(int src,int dst);
	virtual void play(bool loop){};
	virtual BulletDisplay* clone();
protected:

private:
	const char* spritePath;
	const char* plistFile;
};

class ParticleDisplay:public BulletDisplay
{
public:
	ParticleDisplay(const char* particlePath);
	~ParticleDisplay();

	static ParticleDisplay* create(const char* particlePath);

	virtual void setRotation(float fRotation);
	virtual void setRotationOffset(float var);

	virtual void setBlendFuc(int src,int dst);
	virtual void play(bool loop);
	virtual BulletDisplay* clone();
protected:
private:

	const char* particleFile;
	CCParticleSystemQuad* particle;
};

class AnimationDisplay :public BulletDisplay
{
public:
	AnimationDisplay(const char* plistFileName,std::vector<std::string> frames);
	~AnimationDisplay();

	static AnimationDisplay* create(const char* plistFileName,std::vector<std::string> frames);

	virtual void setAnimateSpeed(float var);

	virtual void setBlendFuc(int src,int dst);
	virtual void play(bool loop);
	virtual BulletDisplay* clone();
protected:
private:

	const char* plistFile;
	std::vector<std::string> frameNames;

	CCSprite* spriteNode;
	CCAnimation* animation;
};

class ArmatureDisplay:public BulletDisplay
{
public:
	ArmatureDisplay(const char* armaName, const char* animName);
	ArmatureDisplay(const char* armaFile, const char* armaName, const char* animName);
	~ArmatureDisplay();

	static ArmatureDisplay* create(const char* armaName, const char* animName);
	static ArmatureDisplay* create(const char* armaFile, const char* armaName, const char* animName);

	virtual void setBlendFuc(int src,int dst);
	virtual void play(bool loop);
	virtual BulletDisplay* clone();
protected:

private:
	const char* armatureFile;

	const char*  armatureName;
	const char*  animationName;

	CCArmature* armature;
	CCArmatureAnimation* animtion;
};

#endif