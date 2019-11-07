
#include "ComponentContainer.h"
#include "Component.h"
#include "Entity/Entity.h"

//__ImplementClass(ComponentContainer,'COMC',Core::RefCounted);

ComponentContainer::ComponentContainer()
{

}

ComponentContainer::~ComponentContainer()
{

}

ComponentContainer* ComponentContainer::Create()
{
	return new ComponentContainer();
}

void ComponentContainer::OnUpdate(float fDelta)
{	
// 	for (Util::Dictionary<const char* ,Component*>::Iterator iter = m_pComponets.Begin();iter != m_pComponets.End();iter++)
// 	{
// 		Component* component = (Component*)iter;
// 		component->OnUpdate(fDelta);
// 	}
}

Component* ComponentContainer::Get(const char* pName)
{
	return m_pComponets[pName];
}

bool ComponentContainer::Add(Component* pCom)
{
// 	if(m_pComponets.Contains(pCom->GetName()))return false;
// 	m_pComponets.Add(pCom->GetName(),pCom);
	m_pComponets.insert(map<const char* ,Component*>::value_type(pCom->GetName(),pCom));
	return true;
}

bool ComponentContainer::Remove(const char* pName)
{
// 	if(!m_pComponets.Contains(pName))
// 	{
// 		m_pComponets.Erase(pName);
// 		return true;
// 	}
// 	return false;
	m_pComponets.erase(pName);
	return true;
}

void ComponentContainer::RemoveAll()
{
//	m_pComponets.Clear();
	m_pComponets.clear();
}