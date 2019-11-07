
#ifndef __SCRIPTBIND_COLLISTION__
#define __SCRIPTBIND_COLLISTION__

#include "ScriptBind_Matrix.h"

class GameActor;
class Collider;
class CollistionSystem;

class cocos2d::CCNode;

class ScriptBind_CollistionSystem:public ScriptBind_Matrix
{
public:
	ScriptBind_CollistionSystem();
	~ScriptBind_CollistionSystem();

	virtual const char* GetClassName(){ return "NativeCollistionSystem"; }

	//TODO
	//以后collider是不会放给脚本创建的
	static Collider* CreateCollider(mono::object collider,GameActor* actor);
	static mono::object ColliderGetActor(Collider* collider);

	static void Update(float dTime);

	static CollistionSystem* GetCollistionSystem();
	static void CollistionSystemSetDrawLayer(cocos2d::CCNode* pNode);
	static void CollistionSystemSetDebug(bool debug);
};

#endif