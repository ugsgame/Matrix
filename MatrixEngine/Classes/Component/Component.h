
/************************************************************************/
/*组件基类                                                                      
/************************************************************************/

#ifndef __COMMPONENT_H__
#define __COMMPONENT_H__

//#include "core/refcounted.h"
#include "MatrixMono.h"

class GameActor;

class Component //:public Core::RefCounted
{
	//__DeclareClass(Component);
	__ScriptBind
public:
	Component();
	~Component();
public: 
	virtual void SetActor(GameActor* actor){ m_pActor = actor; }
	virtual GameActor* GetActor(){return m_pActor;}

	void SetName(const char* name){ mName = name;}
	const char* GetName(){ return mName;}
public:
	//GameActor回调的函数
	//TODO
	virtual void OnUpdate(float fDelta) = 0;
protected:
	const char* mName;
	//
	GameActor*  m_pActor;	
private:
};
#endif