
#ifndef __FLOWNODE__
#define __FLOWNODE__

#include "IFlowSystem.h"

//////////////////////////////////////////////////////////////////////////
// Enum used for templating node base class
//////////////////////////////////////////////////////////////////////////
enum ENodeCloneType
{
	eNCT_Singleton,		// node has only one instance; not cloned
	eNCT_Instanced,		// new instance of the node will be created
	eNCT_Cloned 		// node clone method will be called
};

//////////////////////////////////////////////////////////////////////////
// Auto registration for flow nodes. Handles both instanced and 
//	singleton nodes
//////////////////////////////////////////////////////////////////////////
class CAutoRegFlowNodeBase : public IFlowNodeFactory
{
public:
	CAutoRegFlowNodeBase( const char *sClassName )
	{
		m_sClassName = sClassName;
		m_pNext = 0;
		if (!m_pLast)
		{
			m_pFirst = this;
		}
		else
		{
			m_pLast->m_pNext = this;
		}

		m_pLast = this;
	}

	virtual ~CAutoRegFlowNodeBase()
	{
		CAutoRegFlowNodeBase* node = m_pFirst;
		CAutoRegFlowNodeBase* prev = NULL;
		while (node && node != this)
		{
			prev = node;
			node = node->m_pNext;
		}

		assert(node);
		if (node)
		{
			if (prev)
				prev->m_pNext = m_pNext;
			else
				m_pFirst = m_pNext;

			if (m_pLast == this)
				m_pLast = prev;
		}
	}

	void AddRef() {}
	void Release() {}

	void Reset() {}

// 	virtual void GetMemoryUsage(ICrySizer * s) const
// 	{ 
// 		SIZER_SUBCOMPONENT_NAME(s, "CAutoRegFlowNodeBase");
// 		//s->Add(*this); // static object
// 	}

	//////////////////////////////////////////////////////////////////////////
	const char *m_sClassName;
	CAutoRegFlowNodeBase* m_pNext;
	static CAutoRegFlowNodeBase *m_pFirst;
	static CAutoRegFlowNodeBase *m_pLast;
	//////////////////////////////////////////////////////////////////////////
};

//////////////////////////////////////////////////////////////////////////
template <class T>
class CAutoRegFlowNode : public CAutoRegFlowNodeBase
{
public:
	CAutoRegFlowNode( const char *sClassName ) 
		: CAutoRegFlowNodeBase( sClassName )
	{
	}

	IFlowNodePtr Create( IFlowNode::SActivationInfo * pActInfo ) 
	{ 
		PREFAST_SUPPRESS_WARNING(6326)
			if(T::myCloneType == eNCT_Singleton)
			{
				if(!m_pInstance.get())
					m_pInstance = new T(pActInfo);

				return m_pInstance;
			}
			else if(T::myCloneType == eNCT_Instanced)
			{
				return new T(pActInfo);
			}
			else if(T::myCloneType == eNCT_Cloned)
			{
				if(m_pInstance.get())
					return m_pInstance->Clone(pActInfo); 
				else
				{
					m_pInstance = new T(pActInfo);
					return m_pInstance;
				}
			}
			else
			{
				assert(false);
			}
	}

private:
	IFlowNodePtr m_pInstance;	// only applies for singleton nodes
};

#if defined(WIN32) && defined(_LIB)
#define CRY_EXPORT_STATIC_LINK_VARIABLE( Var ) \
	extern "C" { int lib_func_##Var() { return (int)&Var; } } \
	__pragma( message("#pragma comment(linker,\"/include:_lib_func_"#Var"\")") )
#else
#define CRY_EXPORT_STATIC_LINK_VARIABLE( Var )
#endif

//////////////////////////////////////////////////////////////////////////
// Use this define to register a new flow node class. Handles 
//	both instanced and singleton nodes
// Ex. REGISTER_FLOW_NODE( "Delay",CFlowDelayNode )
//////////////////////////////////////////////////////////////////////////
#define REGISTER_FLOW_NODE( FlowNodeClassName,FlowNodeClass ) \
	CAutoRegFlowNode<FlowNodeClass> g_AutoReg##FlowNodeClass ( FlowNodeClassName ); \
	CRY_EXPORT_STATIC_LINK_VARIABLE( g_AutoReg##FlowNodeClass );

// macro similar to REGISTER_FLOW_NODE, but allows a different name for registration
#define REGISTER_FLOW_NODE_EX( FlowNodeClassName,FlowNodeClass,RegName ) \
	CAutoRegFlowNode<FlowNodeClass> g_AutoReg##RegName ( FlowNodeClassName ); \
	CRY_EXPORT_STATIC_LINK_VARIABLE( g_AutoReg##RegName );

//////////////////////////////////////////////////////////////////////////
// Internal base class for flow nodes. Node classes shouldn't be
//	derived directly from this, but should derive from CFlowBaseNode<>
//////////////////////////////////////////////////////////////////////////
class CFlowBaseNodeInternal : public IFlowNode
{
	template <ENodeCloneType CLONE_TYPE> friend class CFlowBaseNode;
public:
	// private ctor/dtor to prevent classes directly derived from this;
	//	the exception is CFlowBaseNode (friended above)
	CFlowBaseNodeInternal() { m_refs = 0; };
	virtual ~CFlowBaseNodeInternal() {}

	//////////////////////////////////////////////////////////////////////////
	// IFlowNode
	virtual void AddRef() { ++m_refs; };
	virtual void Release() { if (0 >= --m_refs)	delete this; };

	virtual IFlowNodePtr Clone( SActivationInfo *pActInfo ) { assert(false); return this; }
	//TODO:搞好再支持，同时加入json支持
	//virtual bool SerializeXML( SActivationInfo *, const XmlNodeRef&, bool ) { return true; }
	//virtual void Serialize(SActivationInfo *, TSerialize ser) {}
	virtual void PostSerialize(SActivationInfo *) {}
	virtual void ProcessEvent( EFlowEvent event, SActivationInfo *pActInfo ) {};
	//////////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////////
	// Common functions to use in derived classes.
	//////////////////////////////////////////////////////////////////////////
	bool IsPortActive( SActivationInfo *pActInfo,int nPort ) const
	{
		return pActInfo->pInputPorts[nPort].IsUserFlagSet();
	}
	bool IsBoolPortActive( SActivationInfo *pActInfo,int nPort ) const
	{
		if (IsPortActive(pActInfo,nPort) && GetPortBool(pActInfo,nPort))
			return true;
		else
			return false;
	}
	EFlowDataTypes GetPortType( SActivationInfo *pActInfo,int nPort ) const
	{
		return (EFlowDataTypes)pActInfo->pInputPorts[nPort].GetType();
	}

	const TFlowInputData& GetPortAny( SActivationInfo *pActInfo,int nPort ) const
	{
		return pActInfo->pInputPorts[nPort];
	}

	bool GetPortBool( SActivationInfo *pActInfo,int nPort ) const
	{
		bool* p_x = (pActInfo->pInputPorts[nPort].GetPtr<bool>());
		if (p_x != 0) return *p_x;
		SFlowNodeConfig config;
		const_cast<CFlowBaseNodeInternal*> (this)->GetConfiguration(config);
		printf("CFlowBaseNodeInternal::GetPortBool: Node=%p Port=%d '%s' Tag=%d -> Not a bool tag!", this, nPort,
			config.pInputPorts[nPort].name,
			pActInfo->pInputPorts[nPort].GetTag());
		return false;
	}
	int GetPortInt( SActivationInfo *pActInfo,int nPort ) const
	{
		int x = *(pActInfo->pInputPorts[nPort].GetPtr<int>());
		return x;
	}
	EntityId GetPortEntityId( SActivationInfo *pActInfo,int nPort )
	{
		EntityId x = *(pActInfo->pInputPorts[nPort].GetPtr<EntityId>());
		return x;
	}
	float GetPortFloat( SActivationInfo *pActInfo,int nPort ) const
	{
		float x = *(pActInfo->pInputPorts[nPort].GetPtr<float>());
		return x;
	}
	//TODO:搞好再支持
// 	Vec3 GetPortVec3( SActivationInfo *pActInfo,int nPort ) const
// 	{
// 		Vec3 x = *(pActInfo->pInputPorts[nPort].GetPtr<Vec3>());
// 		return x;
// 	}
	EntityId GetPortEntityId( SActivationInfo *pActInfo,int nPort ) const
	{
		EntityId x = *(pActInfo->pInputPorts[nPort].GetPtr<EntityId>());
		return x;
	}
	const string& GetPortString( SActivationInfo *pActInfo,int nPort ) const
	{
		const string* p_x = (pActInfo->pInputPorts[nPort].GetPtr<string>());
		if (p_x != 0) return *p_x;
		const static string empty ("");
		SFlowNodeConfig config;
		const_cast<CFlowBaseNodeInternal*> (this)->GetConfiguration(config);
		printf("CFlowBaseNodeInternal::GetPortString: Node=%p Port=%d '%s' Tag=%d -> Not a string tag! \n", this, nPort,
			config.pInputPorts[nPort].name,
			pActInfo->pInputPorts[nPort].GetTag());
		return empty;
	}

	//////////////////////////////////////////////////////////////////////////
	EFlowDataTypes GetPortType( SActivationInfo *pActInfo,int nPort )
	{
		return (EFlowDataTypes)pActInfo->pInputPorts[nPort].GetType();
	}

	//////////////////////////////////////////////////////////////////////////
	// Sends data to output port.
	//////////////////////////////////////////////////////////////////////////
	template <class T>
	void ActivateOutput( SActivationInfo *pActInfo,int nPort, const T &value )
	{
		SFlowAddress addr( pActInfo->myID, nPort, true );
		pActInfo->pGraph->ActivatePort( addr, value );
	}
	//////////////////////////////////////////////////////////////////////////
	bool IsOutputConnected( SActivationInfo *pActInfo,int nPort ) const
	{
		SFlowAddress addr( pActInfo->myID, nPort, true );
		return pActInfo->pGraph->IsOutputConnected( addr );
	}
	//////////////////////////////////////////////////////////////////////////


	//--------------------------------------------------------------
	// returns true if the input entity is the local player. In single player, when the input entity is NULL, it returns true,  for backward compatibility
// 	bool InputEntityIsLocalPlayer( const SActivationInfo* const pActInfo ) const
// 	{
// 		bool bRet = true;
// 
// 		if (pActInfo->pEntity) 
// 		{
// 			IActor* pActor = gEnv->pGame->GetIGameFramework()->GetIActorSystem()->GetActor( pActInfo->pEntity->GetId() );
// 			if (pActor!=gEnv->pGame->GetIGameFramework()->GetClientActor())
// 				bRet = false;
// 		}
// 		else
// 		{
// 			if (gEnv->bMultiplayer)
// 				bRet = false;
// 		}
// 
// 		return bRet;
// 	}


	//-------------------------------------------------------------
	// returns the actor associated with the input entity. In single player, it returns the local player if that actor does not exists.
// 	IActor* GetInputActor( const SActivationInfo* const pActInfo ) const
// 	{
// 		IActor* pActor = pActInfo->pEntity ? gEnv->pGame->GetIGameFramework()->GetIActorSystem()->GetActor( pActInfo->pEntity->GetId() ) : NULL;
// 		if (!pActor && !gEnv->bMultiplayer)
// 			pActor = gEnv->pGame->GetIGameFramework()->GetClientActor();
// 
// 		return pActor;
// 	}

private:
	int m_refs;
};

//////////////////////////////////////////////////////////////////////////
// Base class that nodes can derive from. 
//	eg1: Singleton node:
//	class CSingletonNode : public CFlowBaseNode<eNCT_Singleton>
//
//	eg2: Instanced node:
//	class CInstancedNode : public CFlowBaseNode<eNCT_Instanced>
//
//	Instanced nodes must override Clone (pure virtual in base), while
//	Singleton nodes cannot - will result in a compile error.
//////////////////////////////////////////////////////////////////////////

template <ENodeCloneType CLONE_TYPE>
class CFlowBaseNode : public CFlowBaseNodeInternal
{ 
public:
	CFlowBaseNode() : CFlowBaseNodeInternal() {}

	static const int myCloneType = CLONE_TYPE;
};

#if defined(WIN32)
// prevent derived classes overriding Clone().	
// NB VC10 uses 'sealed'; VC11 may use 'final'.
#define FINAL sealed
#else
// other platforms can ignore
#define FINAL 
#endif

// specialization for singleton nodes: Clone() is
//	marked so derived classes cannot override it.
template <>
class CFlowBaseNode<eNCT_Singleton> : public CFlowBaseNodeInternal
{
public:
	CFlowBaseNode<eNCT_Singleton>() : CFlowBaseNodeInternal()	{}

	virtual IFlowNodePtr Clone( SActivationInfo *pActInfo ) FINAL { return this; }

	static const int myCloneType = eNCT_Singleton;
};

#undef FINAL

#endif