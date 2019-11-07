
#ifndef __BULLETEMITTER_H__
#define __BULLETEMITTER_H__

#include "cocos2d.h"
#include "BulletDisplay.h"
USING_NS_CC;

enum BulletMode
{
	/**常规子弹 （A mode）*/
	kBulletModeNormal = 0,
	/**跟踪弹 （B mode）*/
	kBulletModeFallow,
	/**激光 （C mode）*/
	kBulletModeLaser,
	/**其它子弹 (D mode)*/
	kBulletModeOther,
};

typedef struct Bullet
{
	 CCPoint pos;
	 CCPoint startPos;

	 float size;
	 float deltaSize;

	 float rotation;
	 float deltaRotation;

	 bool live;

	 float liveToTime;
	 float lifeTime;

	 unsigned int  atlasIndex;

	 float sinRotation;

	struct 
	{
		CCPoint dir;
		CCPoint targetPos;
		float speed;
		float spinSpeed;
	}modeA;

	//显示形态相关:
	/////////////////////////////
	BulletDisplay* display;

	Bullet()
	{
		size = 1;
		deltaSize = 0;

		rotation = 0;
		deltaRotation = 0;

		liveToTime = 0;
		lifeTime = -1;

		live = true;

		display = NULL;
	}
}tBullet;


class BulletEmitter:public CCNode
{
public:
	static int AllBulletCount;
public:
	BulletEmitter();
	~BulletEmitter();

	virtual bool init();
public:
	virtual bool IsActive()
	{
		return m_bIsActive;
	}
	virtual unsigned int BulletCount()
	{
		return this->m_uBulletCount;
	}

	static void SetWorldNode(CCNode* node)
	{
		worldNode = node;
	}
	static CCNode* GetWorldNode()
	{
		return worldNode;
	}

	//Emitter
	CC_PROPERTY(BulletMode,m_nEmitterMode,EmitterMode);
	CC_PROPERTY(unsigned int,m_uTotalBullets,TotalBullets);
	CC_PROPERTY(float,m_fDuration,Duration);
	CC_PROPERTY(float,m_fDurationVar,DurationVar);
	CC_PROPERTY(float,m_fInterimTime,InterimTime);
	CC_PROPERTY(float,m_fInterimTimeVar,InterimTimeVar);
	CC_PROPERTY(float,m_fEmissionRate,EmissionRate);
	CC_PROPERTY(float,m_fEmissionRateVar,EmissionRateVar);
	CC_PROPERTY(float,m_fEmissionAngle,EmissionAngle);
	CC_PROPERTY(unsigned int,m_uBeamCount,BeamCount);
	CC_PROPERTY(unsigned int,m_uBeamCountVar,BeamCountVar);
	CC_PROPERTY(float,m_fFieldAngle,FieldAngle);
	CC_PROPERTY(float,m_fFieldAngleVar,FieldAngleVar);
	CC_PROPERTY(float,m_fSpinSpeed,SpinSpeed);
	CC_PROPERTY(tCCPositionType, m_ePositionType, PositionType);
	//Bullet
	CC_PROPERTY(float,m_fSpeed,Speed);
	CC_PROPERTY(float,m_fSpeedVar,SpeedVar);
	CC_PROPERTY(float,m_fSpeedDecay,SpeedDecay);
	CC_PROPERTY(float,m_fSpeedLimit,SpeedLimit);
	CC_PROPERTY(float,m_fLifeTime,LifeTime);
	CC_PROPERTY(float,m_fLifeTimeVar,LifeTimeVar);
	CC_PROPERTY(float,m_fBulletSpinSpeed,BulletSpinSpeed)
	CC_PROPERTY(float,m_fBulletSpinSpeedVar,BulletSpinSpeedVar)
	CC_PROPERTY(float,m_fDamage,Damage);
	CC_PROPERTY(bool,m_bIsTrack,IsTrack);
	CC_PROPERTY(float,m_fSinAmplitude,SinAmplitude);
	CC_PROPERTY(float,m_fSinRate,SinRate);
	CC_PROPERTY(BulletDisplay*,m_pDisplay,Display);
	//
	CREATE_FUNC(BulletEmitter);
public:
	virtual void startSystem();
	virtual void pauseSystem();
	virtual void resumeSystem();
	virtual void stopSystem();
	virtual void resetSystem();

	inline bool isFull();
protected:

	virtual tBullet* addBullet();
	virtual bool initWithTotalBullets(unsigned int numberOfBullet);
	virtual void initBullet(tBullet* bullet);

	virtual bool worldCollistion(tBullet* bullet, CCPoint pos);

	virtual void makeBeam();

	virtual void updateQuadWithBullet(tBullet* bullet, CCPoint newPostion);
	virtual void update(float dt);
private:
	//! time elapsed since the start of the system (in seconds)
	float m_fElapsed;
	float m_fInterim;
	/// <summary>
	/// modeA的共用参数
	/// </summary>
	struct 
	{
		/**初速度*/
		float speed;
		float speedVar;
		/**速度衰变值 正加，负减*/
		float speedDecay;
		/**速度限值 衰变值正为大，负为小*/
		float speedLimit;

		/**生命时间(-1无限)*/
		float lifeTime;
		float lifeTimeVar;
		/**自旋速度*/
		float spinSpeed;
		float spinSpeedVar;
	}modeA;

	//子弹
	std::vector<tBullet*> m_pBullets;
	//! How many bullets can be emitted per second
	float m_fEmitCounter;
	//!  bullet idx
	unsigned int m_uBulletIdx;
	//
	CCPoint m_pEmissionDir;
	//
	float m_fEmissionAngleCount;
	//
	bool m_bIsStop;
	//
	bool m_bIsPause;
	//
	bool m_bIsActive;
	//
	unsigned int m_uAllocatedBullets;
	//
	unsigned int m_uBulletCount;
	//
	float v_fDuration;
	float v_fInterimTime;
	float v_fEmissionRate;
	//
	CCNode* m_pBatchNode;
	static CCNode* worldNode;
};

#endif