
#include "MainScene.h"
#include "EmitterConfig.h"
#include "BulletSystem/BulletEmitter.h"
#include "BulletSystem/BulletDisplay.h"

USING_NS_CC;

// shareMainScene pointer
MainScene * MainScene::sm_pSharedMainScene = 0;

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
	this->setTouchEnabled(true);
	this->setTouchMode(kCCTouchesOneByOne);
	this->setPosition(0,0);

	this->m_User = new CCNode();

	this->m_pBulletEmitter = BulletEmitter::create();
	BulletEmitter::SetWorldNode(this);
	//this->m_pBulletEmitter->setPosition(ccp(0,0));
	this->m_User->setPosition(this->getContentSize().width/2,this->getContentSize().height/2);
	this->m_User->addChild(this->m_pBulletEmitter);
	this->addChild(this->m_User);

	//
	//SpriteDisplay* display = SpriteDisplay::create(DEF_PNG_DEFAULT_BULLET);
	//this->m_pBulletEmitter->setDisplay(display->clone());
	//
	this->m_pBulletEmitter->setSpeed(1000);
	this->m_pBulletEmitter->setDuration(-1);
	this->m_pBulletEmitter->setEmitterMode(kBulletModeNormal);
	this->m_pBulletEmitter->setEmissionRate(10);
	this->m_pBulletEmitter->setEmissionAngle(0);
	//

	this->m_pBulletEmitter->startSystem();

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

BulletEmitter* MainScene::getBulletEmitter()
{
	return m_pBulletEmitter;
}

bool MainScene::ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent)
{
	m_User->setPosition(pTouch->getLocation());
	return true;
}

void MainScene::ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent)
{
	m_User->setPosition(pTouch->getLocation());
}

void MainScene::ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent)
{

}

void MainScene::ccTouchCancelled(CCTouch *pTouch, CCEvent *pEvent)
{

}

void MainScene::ccTouchesBegan(CCSet *pTouches, CCEvent *pEvent)
{

}
void MainScene::ccTouchesMoved(CCSet *pTouches, CCEvent *pEvent)
{

}
void MainScene::ccTouchesEnded(CCSet *pTouches, CCEvent *pEvent)
{

}
void MainScene::ccTouchesCancelled(CCSet *pTouches, CCEvent *pEvent)
{

}

void MainScene::resize(float w,float h)
{
	CCSize oldSize = this->getContentSize();
	CCPoint oldPos = m_User->getPosition();
	float ratioX = oldPos.x/oldSize.width;
	float ratioY = oldPos.y/oldSize.height;
 
 	this->setContentSize(CCSize(w,h));
 	m_User->setPosition(ccp(w*ratioX,h*ratioY));
}

void MainScene::update(float delta)
{
}

void MainScene::draw()
{
	CCLayer::draw();
}