
#include "BulletEmitter.h"

int BulletEmitter::AllBulletCount = 0;
CCNode* BulletEmitter::worldNode = new CCNode();

BulletEmitter::BulletEmitter()
{

	m_uTotalBullets = 0;
	m_fDuration = -1;
	m_fDurationVar = 0;
	m_fInterimTime = 0;
	m_fInterimTimeVar = 0;
	m_ePositionType= kCCPositionTypeFree;
	m_fSpeed = 0;
	m_fSpeedVar = 0;
	m_fLifeTime = -1;
	m_fLifeTimeVar = 0;
	m_fEmissionAngle = 0;
	m_fEmissionAngleCount = 0;
	m_fEmissionRate = 0;
	m_fEmissionRateVar = 0;
	m_nEmitterMode = kBulletModeNormal;
	m_uBeamCount = 1;
	m_uBeamCountVar = 0;
	m_fFieldAngle = 0;
	m_fFieldAngleVar = 0;
	m_fDamage = 0;
	m_fSpinSpeed = 0;
	m_fSinAmplitude = 0;
	m_fSinRate = 0;

	m_uBulletIdx = 0;
	m_uBulletCount = 0;
	m_uTotalBullets = 0;
	m_fEmitCounter = 0;

	m_bIsActive = true;
	m_fElapsed = 0;
	m_fInterim = 0;

	modeA.speed = 0;
	modeA.speedVar = 0;
	modeA.speedDecay = 0;
	modeA.speedLimit = 0;
	modeA.lifeTime = -1;
	modeA.lifeTimeVar = 0;
	modeA.spinSpeed = 0;
	modeA.spinSpeedVar = 0;

	m_pBatchNode = NULL;
	m_pDisplay = NULL;

	v_fDuration = m_fDuration;
	v_fInterimTime = m_fInterimTime;
	v_fEmissionRate = m_fEmissionRate;
}

BulletEmitter::~BulletEmitter()
{

}

bool BulletEmitter::init()
{ 
	scheduleUpdate();
	return true;
}
//////////////////////////////////////////////////////////////////////////
void BulletEmitter::setEmitterMode(BulletMode var)
{
	m_nEmitterMode = var;
	if (m_nEmitterMode == kBulletModeLaser)
	{
		this->m_ePositionType = kCCPositionTypeRelative;
	}
}
BulletMode BulletEmitter::getEmitterMode()
{
	return m_nEmitterMode;
}

void BulletEmitter::setTotalBullets(unsigned int var)
{
	m_uTotalBullets = var;
}
unsigned int BulletEmitter::getTotalBullets()
{
	return m_uTotalBullets;
}

void BulletEmitter::setDuration(float var)
{
	m_fDuration = var;
	v_fDuration = var;
}
float BulletEmitter::getDuration()
{
	return m_fDuration;
}

void BulletEmitter::setDurationVar(float var)
{
	m_fDurationVar = var;
}
float BulletEmitter::getDurationVar()
{
	return m_fDurationVar;
}

void BulletEmitter::setInterimTime(float var)
{
	m_fInterimTime = var;
	v_fInterimTime = var;
}
float BulletEmitter::getInterimTime()
{
	return m_fInterimTime;
}

void BulletEmitter::setInterimTimeVar(float var)
{
	m_fInterimTimeVar = var;
}
float BulletEmitter::getInterimTimeVar()
{
	return m_fInterimTimeVar;
}

void BulletEmitter::setEmissionAngle(float var)
{
	m_fEmissionAngle = var;
	m_fEmissionAngleCount = m_fEmissionAngle;
}
float BulletEmitter::getEmissionAngle()
{
	return m_fEmissionAngle;
}

void BulletEmitter::setEmissionRate(float var)
{
	m_fEmissionRate = var;
	v_fEmissionRate = var;
}
float BulletEmitter::getEmissionRate()
{
	return m_fEmissionRate;
}

void BulletEmitter::setEmissionRateVar(float var)
{
	m_fEmissionRateVar = var;
}
float BulletEmitter::getEmissionRateVar()
{
	return m_fEmissionRateVar;
}

void BulletEmitter::setBeamCount(unsigned int value)
{
	m_uBeamCount = value;
}
unsigned int BulletEmitter::getBeamCount()
{
	return m_uBeamCount;
}

void BulletEmitter::setBeamCountVar(unsigned int value)
{
	m_uBeamCountVar = value;
}
unsigned int BulletEmitter::getBeamCountVar()
{
	return m_uBeamCountVar;
}

void BulletEmitter::setFieldAngle(float value)
{
	m_fFieldAngle = value;
}
float BulletEmitter::getFieldAngle()
{
	return m_fFieldAngle;
}

void BulletEmitter::setFieldAngleVar(float value)
{
	m_fFieldAngleVar = value;
}
float BulletEmitter::getFieldAngleVar()
{
	return m_fFieldAngleVar;
}

void BulletEmitter::setSpinSpeed(float var)
{
	m_fSpinSpeed = var;
}
float BulletEmitter::getSpinSpeed()
{
	return m_fSpinSpeed;
}
void BulletEmitter::setPositionType(tCCPositionType var)
{
	m_ePositionType = var;
}

tCCPositionType BulletEmitter::getPositionType()
{
	return m_ePositionType;
}
//////////////////////////////////////////////////////////////////////////
void BulletEmitter::setSpeed(float var)
{
	modeA.speed = var;
}
float BulletEmitter::getSpeed()
{
	return modeA.speed;
}

void BulletEmitter::setSpeedVar(float var)
{
	modeA.speedVar = var;
}
float BulletEmitter::getSpeedVar()
{
	return modeA.speedVar;
}

void BulletEmitter::setSpeedDecay(float var)
{
	modeA.speedDecay = var;
}
float BulletEmitter::getSpeedDecay()
{
	return modeA.speedDecay;
}

void BulletEmitter::setSpeedLimit(float var)
{
	modeA.speedLimit = var;
}
float BulletEmitter::getSpeedLimit()
{
	return modeA.speedLimit;
}

void BulletEmitter::setLifeTime(float var)
{
	modeA.lifeTime = var;
}
float BulletEmitter::getLifeTime()
{
	return modeA.lifeTime;
}

void BulletEmitter::setLifeTimeVar(float var)
{
	modeA.lifeTimeVar = var;
}
float BulletEmitter::getLifeTimeVar()
{
	return modeA.lifeTimeVar;
}

void BulletEmitter::setBulletSpinSpeed(float var)
{
	modeA.spinSpeed = var;
}
float BulletEmitter::getBulletSpinSpeed()
{
	return modeA.spinSpeed;
}

void BulletEmitter::setBulletSpinSpeedVar(float var)
{
	modeA.spinSpeedVar = var;
}
float BulletEmitter::getBulletSpinSpeedVar()
{
	return modeA.spinSpeedVar;
}

void BulletEmitter::setDamage(float var)
{
	m_fDamage = var;
}
float BulletEmitter::getDamage()
{
	return m_fDamage;
}

void BulletEmitter::setIsTrack(bool var)
{
	m_bIsTrack = var;
}
bool BulletEmitter::getIsTrack()
{
	return m_bIsTrack;
}

void BulletEmitter::setSinAmplitude(float var)
{
	m_fSinAmplitude = var;
}
float BulletEmitter::getSinAmplitude()
{
	return m_fSinAmplitude;
}
void BulletEmitter::setSinRate(float var)
{
	m_fSinRate = var;
}
float BulletEmitter::getSinRate()
{
	return m_fSinRate;
}

void BulletEmitter::setDisplay(BulletDisplay* var)
{
	this->m_pDisplay = var;

	if(this->m_pDisplay->getUsingBatch())
	{
		if(m_pBatchNode)
		{
			m_pBatchNode->removeAllChildren();
			m_pBatchNode->removeFromParent();
			m_pBatchNode->release();
		}
		m_pBatchNode = var->createBatchNode();
		m_pBatchNode->setPosition(ccp(0,0));
		worldNode->addChild(m_pBatchNode);
	}
}
BulletDisplay* BulletEmitter::getDisplay()
{
	return this->m_pDisplay;
}
//////////////////////////////////////////////////////////////////////////
void BulletEmitter::startSystem()
{
	m_bIsActive = true;
	m_fElapsed = 0;
	m_fInterim = 0;
	m_fEmissionAngleCount = m_fEmissionAngle;

	v_fDuration = m_fDuration - m_fDurationVar * CCRANDOM_0_1();
	v_fInterimTime = m_fInterimTime - m_fInterimTimeVar*CCRANDOM_0_1();

	m_bIsPause = false;
	m_bIsStop = true;
}

void BulletEmitter::pauseSystem()
{
	m_bIsActive = false;
	m_fElapsed = m_fDuration;
	m_fEmitCounter = 0;

	v_fDuration = m_fDuration - m_fDurationVar * CCRANDOM_0_1();
	v_fInterimTime = m_fInterimTime - m_fInterimTimeVar*CCRANDOM_0_1();

	m_bIsPause = true;
}

void BulletEmitter::resumeSystem()
{
	m_bIsActive = true;
	m_fElapsed = 0;
	m_fInterim = 0;

	v_fDuration = m_fDuration - m_fDurationVar * CCRANDOM_0_1();
	v_fInterimTime = m_fInterimTime - m_fInterimTimeVar*CCRANDOM_0_1();

	m_bIsPause = false;
}

void BulletEmitter::stopSystem()
{
	m_bIsActive = false;
	m_fElapsed = m_fDuration;
	m_fEmitCounter = 0;

	v_fDuration = m_fDuration - m_fDurationVar * CCRANDOM_0_1();
	v_fInterimTime = m_fInterimTime - m_fInterimTimeVar*CCRANDOM_0_1();

	m_bIsPause = false;
	m_bIsStop = true;
}

void BulletEmitter::resetSystem()
{
	m_bIsActive = true;
	m_fElapsed = 0;
	m_fInterim = 0;
	for (m_uBulletIdx = 0; m_uBulletIdx < m_uBulletCount; ++m_uBulletIdx)
	{
		Bullet* p = m_pBullets[m_uBulletIdx];
		p->live = true;
	}
	m_bIsPause = false;
}

bool BulletEmitter::isFull()
{
	return (m_uBulletCount == m_uTotalBullets);
}

tBullet* BulletEmitter::addBullet()
{
	Bullet* bullet;
	if(this->isFull())
	{
		bullet = new Bullet();
		// 		bullet->display = NULL;
		// 		bullet->hitEffect = NULL;
		// 		bullet->fireEffect = NULL;

		m_pBullets.push_back(bullet);
		m_uTotalBullets = m_pBullets.size();
	}
	else
	{
		bullet = m_pBullets[m_uBulletCount];
	}
	this->initBullet(bullet);

	++m_uBulletCount;
	++AllBulletCount;

	return bullet;
}

bool BulletEmitter::initWithTotalBullets(unsigned int numberOfBullet)
{
	m_uTotalBullets = numberOfBullet;

	//Release old bullets
	for(int i=0;i<m_pBullets.size();i++)
	{
		worldNode->removeChild(m_pBullets[i]->display);
		m_pBullets[i]->display->release();
	}
	m_pBullets.clear();
	//
	for (int i=0;i<numberOfBullet;i++)
	{
		m_pBullets.push_back(new Bullet());
	}

	m_uAllocatedBullets = numberOfBullet;

	m_bIsActive = true;

	return true;
}

void BulletEmitter::initBullet(tBullet* bullet)
{
	bullet->live = true;
	bullet->pos = this->getPosition();

	if(bullet->display == NULL && this->m_pDisplay!=NULL)
	{
		bullet->display = this->m_pDisplay->clone();
		bullet->display->play();

		if(this->m_pDisplay->getUsingBatch())		
			m_pBatchNode->addChild(bullet->display->getDisplayNode());
		else
			worldNode->addChild(bullet->display);
	}
	else if(bullet->display->getTag() != this->m_pDisplay->getTag())
	{
		bullet->display->removeFromParent();
		bullet->display->release();

		bullet->display = this->m_pDisplay->clone();
		bullet->display->play();

		if(this->m_pDisplay->getUsingBatch())		
			m_pBatchNode->addChild(bullet->display->getDisplayNode());
		else
			worldNode->addChild(bullet->display);
	}

	if (bullet->display&&this->m_pDisplay)
	{
		bullet->display->setBlendFuc(this->m_pDisplay->getSrcBlend(),this->m_pDisplay->getDstBlend());
		bullet->display->setRotationOffset(this->m_pDisplay->getRotationOffset());
		bullet->display->setScale(this->m_pDisplay->getScale());		
	}

	CCPoint wPos = -worldNode->getPosition();
	if (m_ePositionType == kCCPositionTypeFree)
	{
		bullet->startPos = this->convertToWorldSpace(wPos);
	}
	else if (m_ePositionType == kCCPositionTypeRelative)
	{
		bullet->startPos = this->getPosition();
	}
	bullet->deltaRotation = 0;
	bullet->liveToTime = 0;
	bullet->sinRotation = 0;

	bullet->modeA.speed = modeA.speed - modeA.speedVar*CCRANDOM_0_1();
	bullet->modeA.spinSpeed = modeA.spinSpeed - modeA.spinSpeedVar*CCRANDOM_0_1();
	bullet->modeA.dir = ccp(0,0);

	bullet->lifeTime = modeA.lifeTime - modeA.lifeTimeVar*CCRANDOM_0_1();
}

bool BulletEmitter::worldCollistion(tBullet* bullet, CCPoint pos)
{
	CCRect worldRect = worldNode->boundingBox();
	bool rel = worldRect.containsPoint(this->convertToWorldSpace(pos));
	if(bullet->live)bullet->live = rel;
	return rel;
}

void BulletEmitter::makeBeam()
{
	CCPoint newDir;
	if(m_fFieldAngleVar>m_fFieldAngle)m_fFieldAngleVar = m_fFieldAngle;

	int v_uBeamCount = m_uBeamCount - m_uBeamCountVar*CCRANDOM_0_1();
	float v_fFieldAngle = m_fFieldAngle - m_fFieldAngleVar*CCRANDOM_0_1();
	float perAngle =v_fFieldAngle;

	if(v_uBeamCount>1)
	{
		perAngle = v_fFieldAngle/(v_uBeamCount-1);
		float beginAngle = this->m_fEmissionAngleCount - v_fFieldAngle/2;
		for (int i=0;i<v_uBeamCount;i++)
		{	 		
			Bullet* b = this->addBullet();
			float angle;
			if(m_fFieldAngle==360)
			{
				angle = i * (m_fFieldAngle/v_uBeamCount) + this->m_fEmissionAngleCount;
			}
			else
			{
				angle = beginAngle + i*perAngle;
			}
			newDir.x = sinf(CC_DEGREES_TO_RADIANS(angle));
			newDir.y = cosf(CC_DEGREES_TO_RADIANS(angle));
			b->modeA.dir = newDir;
		}
	}
	else
	{
		Bullet* b = this->addBullet();

		newDir.x = sinf(CC_DEGREES_TO_RADIANS(m_fEmissionAngleCount));
		newDir.y = cosf(CC_DEGREES_TO_RADIANS(m_fEmissionAngleCount));
		b->modeA.dir = newDir;
	}
}

void BulletEmitter::updateQuadWithBullet(tBullet* bullet, CCPoint newPostion)
{
	if (bullet->display != NULL)
	{
		bullet->display->setVisible(bullet->live);
		bullet->display->setPosition(ccpSub(this->convertToWorldSpace(newPostion) , worldNode->getPosition()));
		bullet->display->setRotation(bullet->rotation);
		bullet->display->setZOrder(-newPostion.y);
	}
}

void BulletEmitter::update(float dt)
{

	CCPoint wPos = - worldNode->getPosition();

	if(m_bIsActive && m_fEmissionRate !=0)
	{
		//this->m_fEmissionAngle += this->m_fSpinSpeed*dt;
		this->m_fEmissionAngleCount += this->m_fSpinSpeed*dt;
		float rate = 1.0f/m_fEmissionRate;

		// 		CCLog("m_uBulletCount:%d",m_uBulletCount);
		// 		CCLog("m_uTotalBullets:%d",m_uTotalBullets);

		m_fEmitCounter += dt;

		while (m_uBulletCount <= m_uTotalBullets && m_fEmitCounter > rate)
		{
			makeBeam();
			m_fEmitCounter -= rate;
		}

		m_fElapsed += dt;
		if (m_fDuration != -1 && v_fDuration < m_fElapsed)
		{
			this->pauseSystem();
		}
	}
	else if(m_bIsPause)		//只有暂停系统才会重启
	{
		m_fInterim+=dt;
		if(m_fInterimTime!=-1 && m_fInterim>=v_fInterimTime)
		{
			this->resumeSystem();
		}
	}
	m_uBulletIdx = 0;

	CCPoint currentPosition = wPos;
	if(m_ePositionType == kCCPositionTypeFree)
	{
		currentPosition = this->convertToWorldSpace(currentPosition);
	}
	else if(m_ePositionType == kCCPositionTypeRelative)
	{
		currentPosition = this->getPosition();
	}

	if(this->isVisible())
	{
		while (m_uBulletIdx < m_uBulletCount)
		{
			Bullet* p = m_pBullets[m_uBulletIdx];

			if(p->live)
			{
				if (m_nEmitterMode == kBulletModeNormal || m_nEmitterMode == kBulletModeFallow)
				{
					CCPoint tmp,sin,dir;
					//
					if(modeA.speedDecay>0)
					{
						if(p->modeA.speed < modeA.speedLimit)
							p->modeA.speed += modeA.speedDecay;
						else
							p->modeA.speed  = modeA.speedLimit;
					}
					else if(modeA.speedDecay<0)
					{
						if(p->modeA.speed > modeA.speedLimit)
							p->modeA.speed += modeA.speedDecay;
						else
							p->modeA.speed  = modeA.speedLimit;
					}
					//
					float t = 1.5707963 - p->modeA.dir.getAngle() + (sinf(p->sinRotation) + cosf(p->sinRotation))*m_fSinAmplitude;
					dir.x = sinf(t);
					dir.y = cosf(t);
					//
					tmp = ccpMult(dir,p->modeA.speed * dt);
					p->pos = ccpAdd(p->pos , tmp);
					// angle
					p->deltaRotation += p->modeA.spinSpeed * dt;
					p->rotation = 90 - CC_RADIANS_TO_DEGREES(dir.getAngle()) + p->deltaRotation;
					//
					p->sinRotation += m_fSinRate;

					p->liveToTime += dt;
					if(modeA.lifeTime!=-1&& p->liveToTime>=p->lifeTime)
					{
						p->live = false;
					}
				}
				else
				{

				}

				// size
				p->size += (p->deltaSize * dt);
				p->size = MAX( 0, p->size );

				CCPoint newPos;
				if (m_ePositionType == kCCPositionTypeFree || m_ePositionType == kCCPositionTypeRelative)
				{
					CCPoint diff = ccpSub(currentPosition , p->startPos);
					newPos =ccpSub(p->pos , diff);
				}
				else
				{
					newPos = p->pos;
				}

				worldCollistion(p, newPos);

				updateQuadWithBullet(p, newPos);

				// update bullet counter
				++m_uBulletIdx;
			}
			else
			{
				if (m_uBulletIdx != m_uBulletCount - 1)
				{
					tBullet* temp = m_pBullets[(int)m_uBulletIdx];
					m_pBullets[(int)m_uBulletIdx] = m_pBullets[(int)m_uBulletCount - 1];
					m_pBullets[(int)m_uBulletCount - 1] = temp;
				}

				--m_uBulletCount;
				--AllBulletCount;
			}
		}
	}
}