
#ifndef __FLOWGRAP__
#define __FLOWGRAP__

#include "FlowSystem.h"
#include "FlowData.h"

// class CFlowSystem;

//#undef  FLOW_DEBUG_PENDING_UPDATES

#define MAX_GRAPH_ENTITIES 2

//////////////////////////////////////////////////////////////////////////
class CFlowGraphBase : public IFlowGraph
{
public:
	CFlowGraphBase( CFlowSystem* pSys );
	virtual ~CFlowGraphBase();

	// IFlowGraph
	virtual void AddRef();
	virtual void Release();
	VIRTUAL IFlowGraphPtr Clone();
	VIRTUAL void Clear();
	VIRTUAL void RegisterHook( IFlowGraphHookPtr );
	VIRTUAL void UnregisterHook( IFlowGraphHookPtr );
	VIRTUAL IFlowNodeIteratorPtr CreateNodeIterator();
	VIRTUAL IFlowEdgeIteratorPtr CreateEdgeIterator();
	VIRTUAL void SetUserData(TFlowNodeId id, const XMLNode& data );
	VIRTUAL XMLNode GetUserData(TFlowNodeId id );
	VIRTUAL void SetGraphEntity( EntityId id,int nIndex=0 );
	VIRTUAL EntityId GetGraphEntity( int nIndex ) const;
	VIRTUAL SFlowAddress ResolveAddress( const char * addr, bool isOutput );
	VIRTUAL TFlowNodeId ResolveNode( const char * name );
	VIRTUAL void GetNodeConfiguration( TFlowNodeId id, SFlowNodeConfig& );
	VIRTUAL bool LinkNodes( SFlowAddress from, SFlowAddress to );
	VIRTUAL void UnlinkNodes( SFlowAddress from, SFlowAddress to );
	VIRTUAL TFlowNodeId CreateNode( TFlowNodeTypeId typeId, const char *name, void *pUserData=0 );
	VIRTUAL TFlowNodeId CreateNode( const char* typeName, const char *name, void *pUserData=0 );
	VIRTUAL bool SetNodeName( TFlowNodeId id,const char *sName );
	VIRTUAL IFlowNodeData* GetNodeData( TFlowNodeId id );
	VIRTUAL const char* GetNodeName( TFlowNodeId id );
	VIRTUAL TFlowNodeTypeId GetNodeTypeId( TFlowNodeId id );
	VIRTUAL const char* GetNodeTypeName( TFlowNodeId id );
	VIRTUAL void RemoveNode( const char * name );
	VIRTUAL void RemoveNode( TFlowNodeId id );
	VIRTUAL void SetEnabled(bool bEnabled);
	VIRTUAL bool IsEnabled() const { return m_bEnabled; }
	VIRTUAL void SetActive(bool bActive); 
	VIRTUAL bool IsActive() const { return m_bActive; }

	VIRTUAL void SetType(IFlowGraph::EFlowGraphType type){m_Type = type;}
	VIRTUAL IFlowGraph::EFlowGraphType GetType() const {return m_Type;}

	VIRTUAL void RegisterFlowNodeActivationListener(SFlowNodeActivationListener *listener);
	VIRTUAL void RemoveFlowNodeActivationListener(SFlowNodeActivationListener *listener);

	VIRTUAL void Update();
	VIRTUAL void InitializeValues();
	VIRTUAL bool SerializeXML( const XMLNode& root, bool reading );
	//VIRTUAL void Serialize( TSerialize ser );
	VIRTUAL void PostSerialize();
	VIRTUAL void SetRegularlyUpdated( TFlowNodeId id, bool regularly );
	VIRTUAL void RequestFinalActivation( TFlowNodeId );
	VIRTUAL void ActivateNode( TFlowNodeId id ) { ActivateNodeInt(id); }
	VIRTUAL void ActivatePortAny( SFlowAddress output, const TFlowInputData& );
	VIRTUAL void ActivatePortCString( SFlowAddress output, const char *cstr );
	VIRTUAL bool SetInputValue( TFlowNodeId node, TFlowPortId port, const TFlowInputData& );
	VIRTUAL bool IsOutputConnected( SFlowAddress output );
	VIRTUAL const TFlowInputData* GetInputValue( TFlowNodeId node, TFlowPortId port );
	VIRTUAL void SetEntityId(TFlowNodeId, EntityId);
	VIRTUAL EntityId GetEntityId(TFlowNodeId);

	VIRTUAL IFlowGraphPtr GetClonedFlowGraph() const { return m_pClonedFlowGraph; }

	VIRTUAL void PrecacheResources();

	VIRTUAL void RemoveGraphTokens();
	VIRTUAL bool AddGraphToken(const IFlowGraph::SGraphToken& token);
	VIRTUAL size_t GetGraphTokenCount() const;
	VIRTUAL const IFlowGraph::SGraphToken* GetGraphToken(size_t index) const;
	VIRTUAL string GetGlobalNameForGraphToken(const string& tokenName) const;

	VIRTUAL TFlowGraphId GetGraphId() const { return m_graphId; }

	VIRTUAL void EnsureSortedEdges()
	{
		if (!m_bEdgesSorted)
			SortEdges();
	}
	// ~IFlowGraph

	//VIRTUAL void GetMemoryUsage(ICrySizer * s) const;

	// temporary solutions [ ask Dejan ]

	// Suspended flow graphs were needed for AI Action flow graphs.
	// Suspended flow graphs aren't updated... 
	// Nodes in suspended flow graphs should ignore OnEntityEvent notifications!
	VIRTUAL void SetSuspended( bool suspend = true );
	VIRTUAL bool IsSuspended() const;

	// AI action related
	//VIRTUAL void SetAIAction( IAIAction* pAIAction );
	//VIRTUAL IAIAction* GetAIAction() const;

	VIRTUAL void UnregisterFromFlowSystem();

	//////////////////////////////////////////////////////////////////////////
	IEntity* GetIEntityForNode( TFlowNodeId id );

	// Called only from CFlowSystem::~CFlowSystem()
	void NotifyFlowSystemDestroyed();


	void RegisterInspector(IFlowGraphInspectorPtr pInspector);
	void UnregisterInspector(IFlowGraphInspectorPtr pInspector);
	const std::vector<IFlowGraphInspectorPtr>& GetInspectors() const { return m_inspectors; }

	CFlowSystem* GetSys() const { return m_pSys; }

	// get some more stats
	void GetGraphStats(int& nodeCount, int& edgeCount);

	void OnEntityReused( IEntity *pEntity, SEntitySpawnParams &params );
	void UpdateForwardings();

	bool NotifyFlowNodeActivationListeners(TFlowNodeId srcNode, TFlowPortId srcPort, TFlowNodeId toNode, TFlowPortId toPort, const char *value);


protected:

	// helper to broadcast an activation
	template <class T> 
	void PerformActivation( const SFlowAddress, const T& value );

	void CloneInner( CFlowGraphBase * pClone );

private:
	void ResetGraphToken(const IFlowGraph::SGraphToken& token);

	class CNodeIterator;
	class CEdgeIterator;

	void FlowLoadError( const char *format,... ) PRINTF_PARAMS(2, 3);

	ILINE void NeedUpdate()
	{
		m_bNeedsUpdating = true;
	}
	ILINE void ActivateNodeInt( TFlowNodeId id )
	{
		if (m_modifiedNodes[id] == NOT_MODIFIED)
		{
			m_modifiedNodes[id] = m_firstModifiedNode;
			m_firstModifiedNode = id;
		}
		if (!m_bInUpdate)
			NeedUpdate();
	}
	IFlowNodePtr CreateNodeOfType( IFlowNode::SActivationInfo* pInfo, TFlowNodeTypeId typeId );
	void SortEdges();
	TFlowNodeId AllocateId();
	void DeallocateId( TFlowNodeId );
	bool ValidateAddress( SFlowAddress );
	bool ValidateNode( TFlowNodeId );
	bool ValidateLink( SFlowAddress& from, SFlowAddress& to );
	static void RemoveNodeFromActivationArray( TFlowNodeId id, TFlowNodeId& front, std::vector<TFlowNodeId>& array );
	void Cleanup();
	bool ReadXML( const XmlNodeRef& root );
	bool WriteXML( const XmlNodeRef& root );
	std::pair<CFlowData *, TFlowNodeId> CreateNodeInt( TFlowNodeTypeId typeId, const char * name,void *pUserData=0 );
	string PrettyAddress( SFlowAddress addr );
	SFlowAddress ResolveAddress( const string &node, const string &port, bool isOutput )
	{
		return ResolveAddress(node.c_str(), port.c_str(), isOutput);
	}
	SFlowAddress ResolveAddress( const char *node, const char *port, bool isOutput );
	void DoUpdate( IFlowNode::EFlowEvent event );
	void NeedInitialize()
	{
		m_bNeedsInitialize = true;
		NeedUpdate();
	}

	template <class T>
	bool NotifyFlowNodeActivationListeners(TFlowNodeId srcNode, TFlowPortId srcPort, TFlowNodeId toNode, TFlowPortId toPort, const T &value);

	const char* InternalGetDebugName();

#if defined (FLOW_DEBUG_PENDING_UPDATES)
	void DebugPendingActivations();
	void CreateDebugName();
	// a more or less descriptive name
	string m_debugName;
#endif

	// the set of modified nodes
	// not modified marker
	static const TFlowNodeId NOT_MODIFIED
#if !defined(__GNUC__)
		= ~TFlowNodeId(0)
#endif
		;
	// end of modified list marker
	static const TFlowNodeId END_OF_MODIFIED_LIST
#if !defined(__GNUC__)
		= NOT_MODIFIED-1
#endif
		;
	// PerformActivation works with this
	std::vector<TFlowNodeId> m_modifiedNodes;
	// This list is used for flowgraph debugging
	std::vector<SFlowNodeActivationListener*> m_flowNodeActivationListeners;
	// and Update swaps modifiedNodes and this so that we don't get messed
	// up during the activation sweep
	std::vector<TFlowNodeId> m_activatingNodes;
	// and this is the head of m_modifiedNodes
	TFlowNodeId m_firstModifiedNode;
	// and this is the head of m_activatingNodes
	TFlowNodeId m_firstActivatingNode;
	// are we in an update loop?
	bool m_bInUpdate;
	// Activate may request a final activation; these get inserted here, and we
	// sweep through it at the end of the update process
	std::vector<TFlowNodeId> m_finalActivatingNodes;
	TFlowNodeId m_firstFinalActivatingNode;

	// all of the node data
	std::vector<CFlowData> m_flowData;
	// deallocated id's waiting to be reused
	std::vector<TFlowNodeId> m_deallocatedIds;	

	// a link between nodes
	struct SEdge
	{
		ILINE SEdge() : fromNode(InvalidFlowNodeId), toNode(InvalidFlowNodeId), fromPort(InvalidFlowPortId), toPort(InvalidFlowPortId) {}
		ILINE SEdge( SFlowAddress from, SFlowAddress to ) : fromNode(from.node), toNode(to.node), fromPort(from.port), toPort(to.port) 
		{
			CRY_ASSERT( from.isOutput );
			CRY_ASSERT( !to.isOutput );
		}
		ILINE SEdge( TFlowNodeId fromNode_, TFlowPortId fromPort_, TFlowNodeId toNode_, TFlowPortId toPort_ ) : fromNode(fromNode_), toNode(toNode_), fromPort(fromPort_), toPort(toPort_) {}

		TFlowNodeId fromNode;
		TFlowNodeId toNode;
		TFlowPortId fromPort;
		TFlowPortId toPort;

		ILINE bool operator<(const SEdge& rhs) const
		{
			if (fromNode < rhs.fromNode)
				return true;
			else if (fromNode > rhs.fromNode)
				return false;
			else if (fromPort < rhs.fromPort)
				return true;
			else if (fromPort > rhs.fromPort)
				return false;
			else if (toNode < rhs.toNode)
				return true;
			else if (toNode > rhs.toNode)
				return false;
			else if (toPort < rhs.toPort)
				return true;
			else
				return false;
		}
		ILINE bool operator==(const SEdge& rhs) const
		{
			return fromNode == rhs.fromNode && fromPort == rhs.fromPort &&
				toNode == rhs.toNode && toPort == rhs.toPort;
		}

		void GetMemoryUsage(ICrySizer *pSizer ) const{}
	};
	class SEdgeHasNode;
	std::vector<SEdge> m_edges;
	bool m_bEnabled; 
	bool m_bActive;   
	bool m_bEdgesSorted;
	bool m_bNeedsInitialize;
	bool m_bNeedsUpdating;
	CFlowSystem* m_pSys;

	// all of the regularly updated nodes (there aught not be too many)
	typedef std::vector<TFlowNodeId> RegularUpdates;
	RegularUpdates m_regularUpdates;
	RegularUpdates m_activatingUpdates;
	EntityId m_graphEntityId[MAX_GRAPH_ENTITIES];

	// reference count
	int m_nRefs;

	// nodes -> id resolution
	std::map<string, TFlowNodeId> m_nodeNameToId;
	// hooks
	std::vector<IFlowGraphHookPtr> m_hooks;
	// user data for editor
	std::map<TFlowNodeId, XmlNodeRef> m_userData;
	// inspectors
	std::vector<IFlowGraphInspectorPtr> m_inspectors;

	IEntitySystem *m_pEntitySystem;

	// temporary solutions [ ask Dejan ]
	bool m_bSuspended;
	bool m_bIsAIAction; // flag that this FlowGraph is an AIAction
	//                     first and only time set in SetAIAction call with an action != 0
	//                     it is never reset. needed when activations are pending which is o.k. for Actiongraphs
	IAIAction* m_pAIAction;

	IFlowGraphPtr m_pClonedFlowGraph;

	TFlowGraphId m_graphId;
	typedef std::vector<IFlowGraph::SGraphToken> TGraphTokens;
	TGraphTokens m_graphTokens;

	IFlowGraph::EFlowGraphType m_Type;
	IFlowGraphDebuggerPtr m_pFlowGraphDebugger;
};

template<class T>
bool CFlowGraphBase::NotifyFlowNodeActivationListeners(TFlowNodeId srcNode, TFlowPortId srcPort, TFlowNodeId toNode, TFlowPortId toPort, const T &value)
{
	const string val = CryStringUtils::toString(value);
	return NotifyFlowNodeActivationListeners(srcNode, srcPort, toNode, toPort, val.c_str());
}

// this function is only provided to assist implementation of ActivatePort()
// force it inline for code size
template <class T>
ILINE void CFlowGraphBase::PerformActivation( const SFlowAddress addr, const T& value )
{
	FUNCTION_PROFILER(gEnv->pSystem, PROFILE_ACTION);

	CRY_ASSERT( ValidateAddress( addr ) );

	if (m_bActive == false || m_bEnabled==false)
		return;

	static ICVar *pToggleDebugger = NULL;

	if(!pToggleDebugger)
		pToggleDebugger = gEnv->pConsole->GetCVar("fg_iEnableFlowgraphNodeDebugging");

	const bool bFlowGraphDebuggerEnabled = (pToggleDebugger && pToggleDebugger->GetIVal() > 0);

	if (addr.isOutput)
	{
		EnsureSortedEdges();

		TFlowInputData valueData;
		valueData.SetUserFlag( true );
		valueData.SetValueWithConversion( value );

		int edgeIndex = 0;
		bool notify = gEnv->IsEditor() && bFlowGraphDebuggerEnabled && CCryAction::GetCryAction()->IsGameStarted() && !m_bNeedsInitialize;
		const int firstEdgeIndex = m_flowData[addr.node].GetOutputFirstEdge(addr.port);

		if(m_pFlowGraphDebugger && notify && !IsOutputConnected(addr))
		{
			bool bPerform = m_pFlowGraphDebugger->PerformActivation(this, addr, valueData);

			if(false == bPerform)
				return;
		}

		std::vector<SEdge>::const_iterator iter = m_edges.begin() + firstEdgeIndex;
		std::vector<SEdge>::const_iterator iterEnd = m_edges.end();

		while (iter != iterEnd && iter->fromNode == addr.node && iter->fromPort == addr.port)
		{
			if(notify)
			{
				SFlowAddress fromAddr;
				fromAddr.node = iter->fromNode;
				fromAddr.port = iter->fromPort;
				fromAddr.isOutput = true;

				SFlowAddress toAddr;
				toAddr.node = iter->toNode;
				toAddr.port = iter->toPort;
				toAddr.isOutput = false;

				bool bPerform = m_pFlowGraphDebugger ? m_pFlowGraphDebugger->PerformActivation(this, edgeIndex, fromAddr, toAddr, valueData) : true;

				if(false == bPerform)
					return;
			}

			m_flowData[iter->toNode].ActivateInputPort( iter->toPort, value );
			// see if we need to insert this node into the modified list
			ActivateNodeInt( iter->toNode );

			if(notify)
				NotifyFlowNodeActivationListeners(iter->fromNode, iter->fromPort, iter->toNode, iter->toPort, value);

			if (m_pSys != 0 && m_pSys->IsInspectingEnabled()) {
				m_pSys->NotifyFlow(this, addr, SFlowAddress(iter->toNode, iter->toPort, false));
			}

			// move next
			++iter;
			++edgeIndex;
		}
	}
	else
	{
		if(m_flowData[addr.node].GetImpl())
		{
			m_flowData[addr.node].ActivateInputPort( addr.port, value );
			ActivateNodeInt( addr.node );
		}
	}
}

// this class extends CFlowGraph with type dependent operations
template <class TL>
class CFlowGraphStub;

template <class H, class T>
class CFlowGraphStub< NTypelist::CNode<H,T> > : public CFlowGraphStub<T>
{
public:
	CFlowGraphStub( CFlowSystem* pSys ) : CFlowGraphStub<T>(pSys) {}
	virtual void DoActivatePort( const SFlowAddress addr, const H& value )
	{
		PerformActivation( addr, value );
	}
};

template <>
class CFlowGraphStub< NTypelist::CEnd > : public CFlowGraphBase
{
public:
	CFlowGraphStub( CFlowSystem* pSys ) : CFlowGraphBase(pSys) {}
};

class CFlowGraph : public CFlowGraphStub<TFlowSystemDataTypes>
{
public:
	CFlowGraph( CFlowSystem* pSys ) : CFlowGraphStub<TFlowSystemDataTypes>(pSys) {}

	virtual void GetMemoryUsage(ICrySizer * s) const;
};


#endif