/************************************************************************/
/*×é¼þÈÝÆ÷
/************************************************************************/

#ifndef __COMPONENTCONTAINER_H__
#define __COMPONENTCONTAINER_H__

//#include "core/refcounted.h"
//#include "util/dictionary.h"
//#include "util/stl.h"

#include <map>

//using namespace Util;
using namespace std;

class Component;
class Entity;

class ComponentContainer //:public Core::RefCounted
{
//	__DeclareClass(ComponentContainer);
public:
	ComponentContainer();
	~ComponentContainer();

public:
	static ComponentContainer* Create(); 
	//CallBack
	//TODO
	virtual void OnUpdate(float fDelta);
public:
	virtual Component* Get(const char* pName);
	virtual bool Add(Component* pCom);
	virtual bool Remove(const char* pName);
	virtual void RemoveAll();
private:
	//Dictionary<const char* ,Component*> m_pComponets;
	map<const char* ,Component*> m_pComponets;
private:
};

#endif