
#ifndef __FLOWDATA__
#define __FLOWDATA__

#include "IFlowSystem.h"

#define FLOWGRAPH_BIT_VAR(type,name,bits) type name : bits

class CFlowData : public IFlowNodeData
{
public:
	static const int TYPE_BITS = 12;
	static const int TYPE_MAX_COUNT = 1 << TYPE_BITS;

	CFlowData( IFlowNodePtr pImpl, const string& name, TFlowNodeTypeId type );
	CFlowData();
	~CFlowData();
	CFlowData( const CFlowData& );
	void Swap( CFlowData& );
	CFlowData& operator=( const CFlowData& rhs );

	ILINE int GetNumOutputs() const { return m_nOutputs; }
	ILINE void SetOutputFirstEdge( int output, int edge ) 
	{ 
		if(m_pOutputFirstEdge && output < m_nOutputs)	
			m_pOutputFirstEdge[output] = edge; 
	}

	ILINE int GetOutputFirstEdge( int output ) const 
	{ 
		if(m_pOutputFirstEdge && output < m_nOutputs)
			return m_pOutputFirstEdge[output]; 

		return 0;
	}

	// activation of ports with data
	template <class T>
	ILINE bool ActivateInputPort( TFlowPortId port, const T& value )
	{
		TFlowInputData * pPort = m_pInputData + port;
		pPort->SetUserFlag( true );
		return pPort->SetValueWithConversion( value );
	}

	ILINE bool SetInputPort( TFlowPortId port, const TFlowInputData& value )
	{
		TFlowInputData * pPort = m_pInputData + port;
		return pPort->SetValueWithConversion( value );
	}
	TFlowInputData* GetInputPort( TFlowPortId port )
	{
		TFlowInputData * pPort = m_pInputData + port;
		return pPort;
	}

	void FlagInputPorts();

	// perform a single activation round
	void Activated( IFlowNode::SActivationInfo *, IFlowNode::EFlowEvent );
	// perform a single update round (called if we have called SetRegularlyUpdated(id,true);
	void Update( IFlowNode::SActivationInfo * );
	// sends Suspended/Resumed events
	void SetSuspended( IFlowNode::SActivationInfo *, bool suspended );
	// resolve a port identifier
	bool ResolvePort( const char * name, TFlowPortId& port, bool isOutput );
	//TODO:需要加入xml支持
	//bool SerializeXML( IFlowNode::SActivationInfo *, const XmlNodeRef& node, bool reading );
	bool SerializeXML( IFlowNode::SActivationInfo *, const XMLNode& node, bool reading );
	//void Serialize( IFlowNode::SActivationInfo *, TSerialize ser );
	void PostSerialize( IFlowNode::SActivationInfo * );

	void CloneImpl( IFlowNode::SActivationInfo * pActInfo )
	{
		m_pImpl = m_pImpl->Clone( pActInfo );
	}

	bool IsValid() const
	{
		return !!m_pImpl;
	}

	bool ValidatePort( TFlowPortId id, bool isOutput ) const
	{
		if (isOutput)
		{
			return id < m_nOutputs;
		}
		else
		{
			return id < m_nInputs;
		}
	}

	const IFlowNodePtr& GetImpl() const
	{
		return m_pImpl;
	}

	TFlowNodeTypeId GetTypeId() const { return m_typeId; }
	const string& GetName() const { return m_name; }
	void SetName( const string &name ) { m_name = name; }

	const char * GetPortName( TFlowPortId port, bool output ) const
	{
		SFlowNodeConfig config;
		DoGetConfiguration( config );
		return output? config.pOutputPorts[port].name : config.pInputPorts[port].name;
	}

	//////////////////////////////////////////////////////////////////////////
	// IFlowNodeData
	//////////////////////////////////////////////////////////////////////////
	VIRTUAL void GetConfiguration( SFlowNodeConfig& config )
	{
		DoGetConfiguration( config );
	}
	VIRTUAL TFlowNodeTypeId GetNodeTypeId() { return m_typeId; }
	VIRTUAL const char* GetNodeName() { return m_name.c_str(); }
	VIRTUAL IFlowNode * GetNode()
	{
		return &*GetImpl();
	}
	VIRTUAL int GetNumInputPorts() const { return m_nInputs; }
	VIRTUAL int GetNumOutputPorts() const { return m_nOutputs; }
	//////////////////////////////////////////////////////////////////////////
	// ~IFlowNodeData
	//////////////////////////////////////////////////////////////////////////

	virtual bool SetEntityId(EntityId id);

	EntityId GetEntityId() 
	{
		EntityId id = 0;
		if (m_hasEntity)
		{
			EntityId * pTemp = 0;
			m_pInputData[0].GetPtr(&pTemp);
			if (pTemp)
				id = *pTemp;
		}
		return id;
	}

	void CompleteActivationInfo( IFlowNode::SActivationInfo* );

	bool DoForwardingIfNeed( IFlowNode::SActivationInfo * );
	EntityId GetCurrentForwardingEntity() const { return m_forwardingEntityID; }


private:	         
	// REMEMBER: When adding members update CFlowData::Swap(CFlowData&)
	// 32 Bit        64 Bit
	TFlowInputData * m_pInputData;																					// ptr	4	8	
	int * m_pOutputFirstEdge;																						// ptr	4	8
	IFlowNodePtr m_pImpl;																							// int+ptr	8 (4+4)	12 (4+8)
	string m_name;																									// 4	8
	//TODO:
	HSCRIPTFUNCTION m_getFlowgraphForwardingEntity;																	// ptr	4	8
	uint8  m_nInputs; 																								// uint8	1	1
	uint8  m_nOutputs;																								// uint8	1	1
	EntityId m_forwardingEntityID;																					// uint32		4
	FLOWGRAPH_BIT_VAR(uint16, m_typeId, TYPE_BITS);																	// 12 Bits	// uint16	2 2
	FLOWGRAPH_BIT_VAR(uint16, m_hasEntity, 1);																		// ..
	FLOWGRAPH_BIT_VAR(uint16, m_failedGettingFlowgraphForwardingEntity, 1);											// ..
	FLOWGRAPH_BIT_VAR(uint16, m_reserved, 1);																
	//------------------------------
	//32 Bytes    48 Bytes

	void DoGetConfiguration( SFlowNodeConfig& config ) const;
	bool ForwardingActivated( IFlowNode::SActivationInfo *, IFlowNode::EFlowEvent );
	bool NoForwarding( IFlowNode::SActivationInfo * );
	static ILINE bool HasForwarding( IFlowNode::SActivationInfo * ) { return true; }
	void ClearInputActivations();
};


ILINE void CFlowData::ClearInputActivations()
{
	for (int i=0; i<m_nInputs; i++)
		m_pInputData[i].ClearUserFlag();
}

ILINE void CFlowData::CompleteActivationInfo( IFlowNode::SActivationInfo * pActInfo )
{
	pActInfo->pInputPorts = m_pInputData + m_hasEntity;
}

ILINE void CFlowData::Activated( IFlowNode::SActivationInfo * pActInfo, IFlowNode::EFlowEvent event )
{
	//	FRAME_PROFILER( m_type.c_str(), GetISystem(), PROFILE_ACTION );
	if (m_hasEntity && (m_getFlowgraphForwardingEntity || m_failedGettingFlowgraphForwardingEntity))
	{
		if (ForwardingActivated( pActInfo, event ))
		{
			ClearInputActivations();
			return;
		}
	}

	CompleteActivationInfo( pActInfo );
	if (m_hasEntity && m_pInputData[0].IsUserFlagSet())
		m_pImpl->ProcessEvent( IFlowNode::eFE_SetEntityId, pActInfo );

	m_pImpl->ProcessEvent( event, pActInfo );

	// now clear any ports
	ClearInputActivations();
}

ILINE void CFlowData::Update( IFlowNode::SActivationInfo * pActInfo )
{
	//	FRAME_PROFILER( m_type.c_str(), GetISystem(), PROFILE_ACTION );
	CompleteActivationInfo( pActInfo );
	if (m_hasEntity && (m_getFlowgraphForwardingEntity || m_failedGettingFlowgraphForwardingEntity))
		if (ForwardingActivated( pActInfo, IFlowNode::eFE_Update ))
			return;
	m_pImpl->ProcessEvent( IFlowNode::eFE_Update, pActInfo );
}

ILINE void CFlowData::SetSuspended( IFlowNode::SActivationInfo* pActInfo, bool suspended )
{
	CompleteActivationInfo( pActInfo );
	m_pImpl->ProcessEvent( suspended ? IFlowNode::eFE_Suspend : IFlowNode::eFE_Resume, pActInfo );
}


#endif