
#ifndef __ACTORMANAGER_H__
#define __ACTORMANAGER_H__

#include "cocos2d.h"

class GameActor;

USING_NS_CC;

class ActorManager:public CCObject
{
public:
	ActorManager();
	~ActorManager();

public: 

	bool init();

	void AddActor(GameActor* actor);
	void RemoveActor(GameActor* actor);

	static ActorManager* Create(IMonoObject* pObj);
private:

	CCArray* ActorArray;
};
#endif