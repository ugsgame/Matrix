
#ifndef __IFLOWSYSTEM_H__
#define __IFLOWSYSTEM_H__

#include "Typelist.h"
#include "ConfigurableVariant.h"
#include "smartptr.h"

#include "tinyxml2.h"

#include <assert.h>
#include <stdio.h>  
#include <iostream>  
#include <stdarg.h>  

#include <string>


using namespace std;
using namespace tinyxml2;


/************************************************************************/
/* TODO:�Ƶ�����ͷ�ļ�   */                                                                  
typedef unsigned char	uint8 ;
typedef unsigned short	uint16 ;
typedef unsigned int	uint32 ;

#ifndef  ILINE
#define  ILINE inline
#endif // ! ILINE

#ifndef VIRTUAL
// VIRTUAL should be used for pure virtual methods with only a single
// implementation - it should be applied to both the method
// declaration/definition in the interface _and_ the implementation (as an
// alternative the 'virtual' keyword may be omitted from the
// implementation).  On platforms not using automatic de-virtualization
// VIRTUAL simply expands to 'virtual'.
#define VIRTUAL virtual
#endif

#if defined(_MSC_VER)
#include "MSVCspecific.h"
#define PREFAST_SUPPRESS_WARNING(W) __pragma(warning(suppress:W))
#else
#define PREFAST_SUPPRESS_WARNING(W) 
#endif

//////////////////////////////////////////////////////////////////////////
// Help message, all help text in code must be wrapped in this define.
// Only include these in non RELEASE builds
#if !defined(_RELEASE)
#define _HELP(x) x
#else
#define _HELP(x) ""
#endif

//TODO:��Ҫ�ض���
// Summary:
//		Script function reference.
struct SScriptFuncHandle {};
typedef SScriptFuncHandle* HSCRIPTFUNCTION;

/************************************************************************/

typedef uint8		TFlowPortId;
typedef uint16		TFlowNodeId;
typedef uint16		TFlowNodeTypeId;
typedef uint32		TFlowGraphId;

typedef uint32 EntityId;	

static const TFlowNodeId InvalidFlowNodeId = ~TFlowNodeId(0);
static const TFlowPortId InvalidFlowPortId = ~TFlowPortId(0);
static const TFlowNodeTypeId InvalidFlowNodeTypeId = 0; // must be 0! FlowSystem.cpp relies on it
static const TFlowGraphId InvalidFlowGraphId = ~TFlowGraphId(0);

// Notes:
//	 This is a special type which means "no input data"
struct SFlowSystemVoid 
{
	// 	void Serialize(TSerialize ser) {}
	// 	void GetMemoryUsage(ICrySizer *pSizer) const{}
};

inline bool operator==(const SFlowSystemVoid& a, const SFlowSystemVoid& b)
{
	return true;
}

ILINE void Format(string &str,const char *_format, ...)  
{  
	char szBuffer[1000];  
	memset(szBuffer, 0x00, sizeof(szBuffer));  
	va_list ap;  
	va_start(ap, _format);    
	try  
	{  
		vsnprintf(szBuffer, 1000, _format, ap);  
	} catch (...)  
	{  
		cout << "ERROR: format the string failed..." << endl;  
	}  

	va_end(ap);  
	str.append(szBuffer);   
}  


// Notes:
//	 By adding types to this list, we can extend the flow system to handle
//	 new data types.
// Remarks:
//	 OTHER THINGS THAT NEED UPDATING HOWEVER INCLUDE:
//   CFlowData::ConfigureInputPort
// See also:
//	 CFlowData::ConfigureInputPort
typedef NTypelist::CConstruct<
	SFlowSystemVoid,
	int,
	float,
	EntityId,
	//	Vec3,
	string,
	bool
>::TType TFlowSystemDataTypes;

// Notes:
//	 Default conversion uses C++ rules.
template <class From, class To>
struct SFlowSystemConversion
{
	static ILINE bool ConvertValue( const From& from, To& to )
	{
		to = (To)from;
		return true;
	}
};

#define FLOWSYSTEM_NO_CONVERSION(T) \
	template <> struct SFlowSystemConversion<T,T> { \
	static ILINE bool ConvertValue( const T& from, T& to ) { to = from; return true; } \
}
FLOWSYSTEM_NO_CONVERSION(SFlowSystemVoid);
FLOWSYSTEM_NO_CONVERSION(int);
FLOWSYSTEM_NO_CONVERSION(float);
FLOWSYSTEM_NO_CONVERSION(EntityId);
//FLOWSYSTEM_NO_CONVERSION(Vec3);
FLOWSYSTEM_NO_CONVERSION(string);
FLOWSYSTEM_NO_CONVERSION(bool);
#undef FLOWSYSTEM_NO_CONVERSION

// Notes:
//	 Specialization for converting to bool to avoid compiler warnings.
template <class From>
struct SFlowSystemConversion<From, bool>
{
	static ILINE bool ConvertValue( const From& from, bool& to )
	{
		to = (from != 0);
		return true;
	}
};
// Notes:
//	 "Void" conversion needs some help
//	 converting from void to anything yields default value of type.
template <class To>
struct SFlowSystemConversion<SFlowSystemVoid, To>
{
	static ILINE bool ConvertValue( SFlowSystemVoid, To& to )
	{
		to = To();
		return true;
	}
};
template <>
struct SFlowSystemConversion<SFlowSystemVoid, bool>
{
	static ILINE bool ConvertValue( const SFlowSystemVoid& from, bool& to )
	{
		to = false;
		return true;
	}
};

// Notes:
//	 Converting to void is trivial - just lose the value info.
template <class From>
struct SFlowSystemConversion<From, SFlowSystemVoid>
{
	static ILINE bool ConvertValue( const From& from, SFlowSystemVoid& )
	{
		return true;
	}
};

#define FLOWSYSTEM_STRING_CONVERSION(type,fmt) \
	template <> \
struct SFlowSystemConversion<type, string> \
{ \
	static ILINE bool ConvertValue( const type& from, string& to ) \
{ \
	Format(to, fmt, from ); \
	return true; \
} \
}; \
	template <> \
struct SFlowSystemConversion<string, type> \
{ \
	static ILINE bool ConvertValue( const string& from, type& to ) \
{ \
	return 1 == sscanf( from.c_str(), fmt, &to ); \
} \
};

FLOWSYSTEM_STRING_CONVERSION(int, "%d");
FLOWSYSTEM_STRING_CONVERSION(float, "%f");
FLOWSYSTEM_STRING_CONVERSION(EntityId, "%u");
// FLOWSYSTEM_STRING_CONVERSION(bool, "%d");

#undef FLOWSYSTEM_STRING_CONVERSION

template <>
struct SFlowSystemConversion<bool, string>
{
	static ILINE bool ConvertValue( const bool& from, string& to )
	{
		//to.format( "%d", from );
		Format(to,"%d",from);
		return true;
	}	
};

template <>
struct SFlowSystemConversion<string, bool>
{
	// Description:
	//	 Leaves 'to' unchanged in case of error.
	static ILINE bool ConvertValue( const string& from, bool& to )
	{
		int to_i;
		if (1 == sscanf( from.c_str(), "%d", &to_i ))
		{
			to = !!to_i;
			return true;
		}
		if (0 == stricmp (from.c_str(), "true"))
		{
			to = true;
			return true;
		}
		if (0 == stricmp (from.c_str(), "false"))
		{
			to = false;
			return true;
		}
		return false;
	}	
};

enum EFlowDataTypes
{
	eFDT_Any = -1,
	eFDT_Void = NTypelist::IndexOf<SFlowSystemVoid, TFlowSystemDataTypes>::value,
	eFDT_Int = NTypelist::IndexOf<int, TFlowSystemDataTypes>::value,
	eFDT_Float = NTypelist::IndexOf<float, TFlowSystemDataTypes>::value,
	eFDT_EntityId = NTypelist::IndexOf<EntityId, TFlowSystemDataTypes>::value,
	//	eFDT_Vec3 = NTypelist::IndexOf<Vec3, TFlowSystemDataTypes>::value,
	eFDT_String = NTypelist::IndexOf<string, TFlowSystemDataTypes>::value,
	eFDT_Bool = NTypelist::IndexOf<bool, TFlowSystemDataTypes>::value,
};

typedef CConfigurableVariant<TFlowSystemDataTypes, sizeof(void*), SFlowSystemConversion> TFlowInputData;

struct SEqualFlowInputData
{
	class CEqualVisitor
	{
	public:
		ILINE CEqualVisitor( const TFlowInputData& b ) : m_b(b) {}
		template <class T> ILINE void Visit( const T& value )
		{
			const T * ptrB = 0;
			m_b.GetPtr(&ptrB);
			assert( ptrB );
			m_result = value == *ptrB;
		}
		ILINE void Visit( const SFlowSystemVoid& value )
		{
			m_result = false;
		}
		ILINE bool result() { return m_result; }

	private:
		bool m_result;
		const TFlowInputData& m_b;
	};

	bool operator()( const TFlowInputData& a, const TFlowInputData& b ) const
	{
		int aTag = a.GetTag();
		int bTag = b.GetTag();
		if (aTag == bTag)
		{
			CEqualVisitor visitor(b);
			a.Visit( visitor );
			return visitor.result();
		}
		else // aTag != bTag
			return false;
	}
};

// Summary:
//	 TFlowInputData compare function.
// See also:
//	 TFlowInputData
inline bool IsEqualFlowInputData( const TFlowInputData &v1,const TFlowInputData &v2 )
{
	SEqualFlowInputData pred;
	return pred(v1,v2);
}

struct SFlowAddress
{
	SFlowAddress( TFlowNodeId node, TFlowPortId port, bool isOutput )
	{
		this->node = node;
		this->port = port;
		this->isOutput = isOutput;
	}
	SFlowAddress()
	{
		node = InvalidFlowNodeId;
		port = InvalidFlowPortId;
		isOutput = true;
	}
	bool operator ==( const SFlowAddress &n ) const { return node == n.node && port == n.port && isOutput == n.isOutput; }
	bool operator !=( const SFlowAddress &n ) const { return !(*this == n); }

	TFlowNodeId node;
	TFlowPortId port;
	bool isOutput;

};

// Summary:
//	 Flags used by the flow system.
enum EFlowNodeFlags
{
	EFLN_TARGET_ENTITY        = 0x0001, // CORE FLAG: This node targets an entity, entity id must be provided.
	EFLN_HIDE_UI              = 0x0002, // CORE FLAG: This node cannot be selected by user for placement in flow graph UI.
	EFLN_DYNAMIC_OUTPUT				= 0x0004, // CORE FLAG: This node is setup for dynamic output port growth in runtime.
	EFLN_UNREMOVEABLE					= 0x0008,	// CORE FLAG: This node cannot be deleted by the user
	EFLN_CORE_MASK            = 0x000F, // CORE_MASK

	EFLN_APPROVED							= 0x0010, // CATEGORY:  This node is approved for designers.
	EFLN_ADVANCED             = 0x0020, // CATEGORY:  This node is slightly advanced and approved.
	EFLN_DEBUG                = 0x0040, // CATEGORY:  This node is for debug purpose only.
	EFLN_OBSOLETE             = 0x0200, // CATEGORY:  This node is obsolete and is not available in the editor.
	EFLN_CATEGORY_MASK        = 0x0FF0, // CATEGORY_MASK.

	EFLN_USAGE_MASK           = 0xF000, // USAGE_MASK.
};

//////////////////////////////////////////////////////////////////////////
enum EFlowSpecialEntityId
{
	EFLOWNODE_ENTITY_ID_GRAPH1 = -1,
	EFLOWNODE_ENTITY_ID_GRAPH2 = -2,
};

struct SInputPortConfig
{
	SInputPortConfig(const char* _name, const char* _humanName, const char* _description, const char* _sUIConfig, TFlowInputData _defaultData)
		: name(_name),
		humanName(_humanName),
		description(_description),
		sUIConfig(_sUIConfig),
		defaultData(_defaultData)
	{
		if(_name != NULL)
		{
			//CRY_ASSERT_MESSAGE(strstr(_name, " ") == NULL, "Whitespaces are not allowed in the input port name");
			//TODO:CCAssert
			if(strstr(_name, " ") == NULL)
			{
				printf("Whitespaces are not allowed in the input port name /n");
				assert(1);
			}
		}	
	}

	SInputPortConfig()
		: name(NULL),
		humanName(NULL),
		description(NULL),
		sUIConfig(NULL),
		defaultData(TFlowInputData(0, false))
	{

	}

	// Summary:
	//	 Name of this port.
	const char * name;
	// Summary:
	//	 Human readable name of this port (default: same as name).
	const char * humanName;
	// Summary:
	//	 Human readable description of this port (help).
	const char * description;
	// Summary:
	//	 UIConfig: enums for the variable
	// Example:
	//	 "enum_string:a,b,c"
	//	 "enum_string:something=a,somethingelse=b,whatever=c"
	//	 "enum_int:something=0,somethingelse=10,whatever=20"
	//	 "enum_float:something=1.0,somethingelse=2.0"
	//	 "enum_global:GlobalEnumName"
	const char * sUIConfig;
	// Summary:
	//	 Default data.
	TFlowInputData defaultData;

	//void GetMemoryUsage(ICrySizer *pSizer ) const{}
};

struct SOutputPortConfig
{
	SOutputPortConfig(const char* _name, const char* _humanName, const char* _description, int _type)
		: name(_name),
		humanName(_humanName),
		description(_description),
		type(_type)
	{
		if(_name != NULL)
		{
			//CRY_ASSERT_MESSAGE(strstr(_name, " ") == NULL, "Whitespaces are not allowed in the output port name");
			if(strstr(_name, " ") == NULL)
			{
				printf("Whitespaces are not allowed in the input port name /n");
				assert(1);
			}
		}	
	}

	SOutputPortConfig()
		: name(NULL),
		humanName(NULL),
		description(NULL),
		type(0)
	{

	}

	// Summary:
	//	 Name of this port.
	const char * name;
	// Summary:
	//	 Human readable name of this port (default: same as name).
	const char * humanName;
	// Summary:
	//	 Human readable description of this port (help).
	const char * description;
	// Summary:
	//	 Type of our output (or -1 for "dynamic").
	int type;

	//void GetMemoryUsage(ICrySizer *pSizer ) const{}
};

struct SFlowNodeConfig
{
	SFlowNodeConfig() : pInputPorts(0), pOutputPorts(0), nFlags(EFLN_DEBUG), sDescription(0), sUIClassName(0) {}

	const SInputPortConfig * pInputPorts;
	const SOutputPortConfig * pOutputPorts;
	// Summary:
	//	 Node configuration flags 
	// See also:
	//	 EFlowNodeFlags
	uint32 nFlags;
	const char *sDescription;
	const char *sUIClassName;

	ILINE void SetCategory(uint32 flags)
	{
		nFlags = (nFlags & ~EFLN_CATEGORY_MASK) | flags;
	}

	ILINE uint32 GetCategory() const
	{
		return /* static_cast<EFlowNodeFlags> */ (nFlags & EFLN_CATEGORY_MASK);
	}

	ILINE uint32 GetCoreFlags() const
	{
		return nFlags & EFLN_CORE_MASK;
	}

	ILINE uint32 GetUsageFlags() const
	{
		return nFlags & EFLN_USAGE_MASK;
	}

};

template <class T>
ILINE SOutputPortConfig OutputPortConfig( const char * name,const char *description=NULL, const char *humanName=NULL )
{
	//ScopedSwitchToGlobalHeap useGlobalHeap;
	SOutputPortConfig result(name, humanName, description, NTypelist::IndexOf<T, TFlowSystemDataTypes>::value);
	return result;
}

ILINE SOutputPortConfig OutputPortConfig_AnyType( const char * name,const char *description=NULL, const char *humanName=NULL )
{
	//ScopedSwitchToGlobalHeap useGlobalHeap;
	SOutputPortConfig result(name, humanName, description, eFDT_Any);
	return result;
}

ILINE SOutputPortConfig OutputPortConfig_Void( const char * name,const char *description=NULL, const char *humanName=NULL )
{
	//ScopedSwitchToGlobalHeap useGlobalHeap;
	SOutputPortConfig result(name, humanName, description, eFDT_Void);
	return result;
}

ILINE SOutputPortConfig OutputPortConfig_Null()
{
	//ScopedSwitchToGlobalHeap useGlobalHeap;
	SOutputPortConfig result;
	return result;
}

template <class T>
ILINE SInputPortConfig InputPortConfig( const char * name,const char *description=NULL, const char *humanName=NULL, const char *sUIConfig=NULL )
{
	//ScopedSwitchToGlobalHeap useGlobalHeap;
	SInputPortConfig result(name, humanName, description, sUIConfig, TFlowInputData(TFlowInputData::ConstructType<T>::Helper::ConstructedValue(), true));
	return result;
}





template <class T, class ValueT>
ILINE SInputPortConfig InputPortConfig( const char * name, const ValueT& value, const char *description=NULL, const char *humanName=NULL, const char *sUIConfig=NULL )
{
	//ScopedSwitchToGlobalHeap useGlobalHeap;
	//SInputPortConfig result = {name, humanName, description, sUIConfig, TFlowInputData(T(value), true)};
	SInputPortConfig result(name, humanName, description, sUIConfig, TFlowInputData(T(value), true));
	return result;
}


ILINE SInputPortConfig InputPortConfig_AnyType( const char * name, const char *description=NULL, const char *humanName=NULL, const char *sUIConfig=NULL  )
{
	//ScopedSwitchToGlobalHeap useGlobalHeap;
	//SInputPortConfig result = {name, humanName, description, sUIConfig, TFlowInputData(0, false)};
	SInputPortConfig result(name, humanName, description, sUIConfig, TFlowInputData(0, false));
	return result;
}

ILINE SInputPortConfig InputPortConfig_Void( const char * name, const char *description=NULL, const char *humanName=NULL, const char *sUIConfig=NULL )
{
	//ScopedSwitchToGlobalHeap useGlobalHeap;
	SInputPortConfig result(name, humanName, description, sUIConfig, TFlowInputData(SFlowSystemVoid(), false));
	return result;
}

ILINE SInputPortConfig InputPortConfig_Null()
{
	//ScopedSwitchToGlobalHeap useGlobalHeap;
	SInputPortConfig result;
	return result;
}

namespace NFlowSystemUtils
{

	// Description:
	//	 This class helps define IFlowGraph by declaring typed virtual
	//	 functions corresponding to TFlowSystemDataTypes.
	// See also:
	//	 IFlowGraph, TFlowSystemDataTypes
	template <class TL>
	struct IFlowSystemTyped;

	template <class H>
	struct IFlowSystemTypedHelper
	{
		virtual ~IFlowSystemTypedHelper(){}
		virtual void DoActivatePort( const SFlowAddress, const H& value ) = 0;
	};

	template <class H, class T>
	struct IFlowSystemTyped< NTypelist::CNode<H,T> > : public IFlowSystemTyped<T>, public IFlowSystemTypedHelper<H>
	{
	};

	template <>
	struct IFlowSystemTyped< NTypelist::CEnd >
	{
	};

}

struct IFlowGraph;

struct IFlowNode;
TYPEDEF_AUTOPTR(IFlowNode);
typedef IFlowNode_AutoPtr IFlowNodePtr;

class Entity;

struct IFlowNode
{
	virtual ~IFlowNode(){}
	struct SActivationInfo
	{
		SActivationInfo( IFlowGraph	* pGraph = 0, TFlowNodeId myID = 0, void * pUserData = 0, TFlowInputData * pInputPorts = 0 )
		{
			this->pGraph = pGraph;
			this->myID = myID;
			this->m_pUserData = pUserData;
			this->pInputPorts = pInputPorts;
			this->pEntity = 0;
			this->connectPort = InvalidFlowPortId;
		}
		IFlowGraph * pGraph;
		TFlowNodeId myID;
		Entity* pEntity;
		TFlowPortId connectPort;
		TFlowInputData * pInputPorts;
		void *m_pUserData;
	};

	enum EFlowEvent
	{
		eFE_Update,					
		eFE_Activate,				// Sent if one or more input ports have been activated.
		eFE_FinalActivate,			// Must be eFE_Activate+1 (same as activate, but at the end of FlowSystem:Update)
		eFE_PrecacheResources,	// Called after flow graph loading to precache resources inside flow nodes.
		eFE_Initialize,				// Sent once after level has been loaded. it is NOT called on Serialization!
		eFE_FinalInitialize,		// Must be eFE_Initialize+1
		eFE_SetEntityId,			// This event is send to set the entity of the FlowNode. Might also be sent in conjunction (pre) other events (like eFE_Initialize).
		eFE_Suspend,				
		eFE_Resume,					
		eFE_ConnectInputPort,
		eFE_DisconnectInputPort,
		eFE_ConnectOutputPort,
		eFE_DisconnectOutputPort,
		eFE_DontDoAnythingWithThisPlease
	};

	virtual void AddRef() = 0;
	virtual void Release() = 0;
	virtual IFlowNodePtr Clone( SActivationInfo * ) = 0;

	virtual void GetConfiguration( SFlowNodeConfig& ) = 0;
	//TODO:Ҫ֧��xml
	//virtual bool SerializeXML( SActivationInfo *, const XmlNodeRef& root, bool reading ) = 0;
	virtual bool SerializeXML( SActivationInfo *, const XMLNode& root, bool reading ) = 0;
	//virtual void Serialize( SActivationInfo *, TSerialize ser ) = 0;
	virtual void PostSerialize( SActivationInfo * ) = 0;
	virtual void ProcessEvent( EFlowEvent event, SActivationInfo * ) = 0;

	//virtual void GetMemoryUsage(ICrySizer * s) const = 0;

	// Summary:
	//	 Used to return the global UI enum name to use for the given input port, if the port defines a UI enum of type 'enum_global_def'
	//	 Returns TRUE if a global enum name was determined and should be used. Otherwise the common name is used.
	// In:
	//	 portId - The input port Id for which to determine the global enum name
	//	 pNodeEntity - Current entity attached to the node
	//	 szName - The common name defined with the port (enum_global_def:commonName)
	// Out:
	//	 outGlobalEnum - The global enum name to use for this port
	virtual bool GetPortGlobalEnum(uint32 portId, Entity *pNodeEntity, const char* szName, string& outGlobalEnum) const { return false; }
};

// Summary:
//	 Wraps IFlowNode for specific data
struct IFlowNodeData
{
	virtual ~IFlowNodeData(){}
	// Summary:
	//	 Gets a pointer to the node.
	virtual IFlowNode * GetNode() = 0;
	// Summary:
	//	 Gets the ID of the node type.
	virtual TFlowNodeTypeId GetNodeTypeId() = 0;
	// Summary:
	//	 Gets the node's name.
	virtual const char* GetNodeName() = 0;
	virtual void GetConfiguration( SFlowNodeConfig& ) = 0;

	// Summary:
	//	 Gets the amount of ports in the flownode
	virtual int GetNumInputPorts() const = 0;
	virtual int GetNumOutputPorts() const = 0;
};

struct IFlowGraph;
TYPEDEF_AUTOPTR(IFlowGraph);
typedef IFlowGraph_AutoPtr IFlowGraphPtr;

struct IFlowGraphHook;
TYPEDEF_AUTOPTR(IFlowGraphHook);
typedef IFlowGraphHook_AutoPtr IFlowGraphHookPtr;

struct IFlowGraphHook
{
	enum EActivation
	{
		eFGH_Stop, 
		eFGH_Pass,
		eFGH_Debugger_Input,
		eFGH_Debugger_Output,
	};

	virtual ~IFlowGraphHook(){}
	virtual void AddRef() = 0;
	virtual void Release() = 0;
	virtual IFlowNodePtr CreateNode( IFlowNode::SActivationInfo*, TFlowNodeTypeId typeId ) = 0;
	// Description:
	//	 Returns false to disallow this creation!!
	virtual bool CreatedNode( TFlowNodeId id, const char * name, TFlowNodeTypeId typeId, IFlowNodePtr pNode ) = 0;
	// Description:
	//	 This gets called if CreatedNode was called, and then cancelled later
	virtual void CancelCreatedNode( TFlowNodeId id, const char * name, TFlowNodeTypeId typeId, IFlowNodePtr pNode ) = 0;

	virtual IFlowGraphHook::EActivation PerformActivation( IFlowGraphPtr pFlowgraph, TFlowNodeId srcNode, TFlowPortId srcPort, TFlowNodeId toNode, TFlowPortId toPort, const TFlowInputData* value) = 0;

	//void GetMemoryUsage(ICrySizer *pSizer ) const{}
};

// Description:
//	 Structure that permits to iterate through the node of the flowsystem.
struct IFlowNodeIterator
{
	virtual ~IFlowNodeIterator(){}
	virtual void AddRef() = 0;
	virtual void Release() = 0;
	virtual IFlowNodeData* Next( TFlowNodeId &id ) = 0;
};

// Description:
//	 Structure that permits to iterate through the edge of the flowsystem.
struct IFlowEdgeIterator
{
	virtual ~IFlowEdgeIterator(){}
	// Summary:
	//	 Structure describing flowsystem edges.
	struct Edge
	{
		TFlowNodeId fromNodeId;		// ID of the starting node.
		TFlowPortId fromPortId;		// ID of the starting port.
		TFlowNodeId toNodeId;		// ID of the arrival node.
		TFlowPortId toPortId;		// ID of the arrival port.
	};

	virtual void AddRef() = 0;
	virtual void Release() = 0;
	virtual bool Next( Edge& edge ) = 0;
};

TYPEDEF_AUTOPTR(IFlowNodeIterator);
typedef IFlowNodeIterator_AutoPtr IFlowNodeIteratorPtr;
TYPEDEF_AUTOPTR(IFlowEdgeIterator);
typedef IFlowEdgeIterator_AutoPtr IFlowEdgeIteratorPtr;

struct SFlowNodeActivationListener
{
	virtual ~SFlowNodeActivationListener(){}
	virtual bool OnFlowNodeActivation(IFlowGraphPtr pFlowgraph, TFlowNodeId srcNode, TFlowPortId srcPort, TFlowNodeId toNode, TFlowPortId toPort, const char* value) = 0;
};

// Description:
//	 Refers to NFlowSystemUtils::IFlowSystemTyped to see extra per-flow system type
//	 functions that are added to this interface.
struct IFlowGraph : public NFlowSystemUtils::IFlowSystemTyped<TFlowSystemDataTypes>
{
	enum EFlowGraphType
	{
		eFGT_Default = 0,
		eFGT_AIAction,
		eFGT_UIAction,
		eFGT_Module,
		eFGT_MaterialFx,
	};

	virtual void AddRef() = 0;
	virtual void Release() = 0;
	virtual IFlowGraphPtr Clone() = 0;

	// Summary:
	//	 Clears flow graph, deletes all nodes and edges.
	virtual void Clear() = 0;

	virtual void RegisterHook( IFlowGraphHookPtr ) = 0;
	virtual void UnregisterHook( IFlowGraphHookPtr ) = 0;

	virtual IFlowNodeIteratorPtr CreateNodeIterator() = 0;
	virtual IFlowEdgeIteratorPtr CreateEdgeIterator() = 0;

	// Summary:
	//	 Assigns id of the default entity associated with this flow graph.
	virtual void SetGraphEntity( EntityId id,int nIndex=0 ) = 0;
	// Summary:
	//	 Retrieves id of the default entity associated with this flow graph.
	virtual EntityId GetGraphEntity( int nIndex ) const = 0;

	// Summary:
	//	 Enables/disables the flowgraph - used from editor.
	virtual void SetEnabled (bool bEnable) = 0;
	// Summary:
	//	 Checks if flowgraph is currently enabled.
	virtual bool IsEnabled() const = 0;

	// Summary:
	//	 Activates/deactivates the flowgraph - used from FlowGraphProxy during Runtime.
	// See also:
	//	 FlowGraphProxy
	virtual void SetActive (bool bActive) = 0;
	// Summary:
	//	 Checks if flowgraph is currently active.
	virtual bool IsActive() const = 0;

	virtual void SetType (IFlowGraph::EFlowGraphType type) = 0;
	virtual IFlowGraph::EFlowGraphType GetType() const = 0;

	// Primary game interface.

	//
	virtual void Update() = 0;
	//TODO:����xml֧��
	//virtual bool SerializeXML( const XmlNodeRef& root, bool reading ) = 0;
	virtual bool SerializeXML( const XMLNode& root, bool reading ) = 0;
	//virtual void Serialize( TSerialize ser ) = 0;
	virtual void PostSerialize() = 0;
	// Description:
	//	 Similar to Update, but sends an eFE_Initialize instead of an eFE_Update
	virtual void InitializeValues() = 0;

	// Description:
	//	 Send eFE_PrecacheResources event to all flow graph nodes, so they can cache thierinternally used assets.
	virtual void PrecacheResources() = 0;

	virtual void EnsureSortedEdges() = 0;

	// Node manipulation.
	//##@{
	virtual SFlowAddress ResolveAddress( const char * addr, bool isOutput ) = 0;
	virtual TFlowNodeId ResolveNode( const char * name ) = 0;
	virtual TFlowNodeId CreateNode( TFlowNodeTypeId typeId, const char *name, void *pUserData=0 ) = 0;
	virtual TFlowNodeId CreateNode( const char* typeName, const char *name, void *pUserData=0 ) = 0; // deprecated
	virtual IFlowNodeData* GetNodeData( TFlowNodeId id ) = 0;
	virtual bool SetNodeName( TFlowNodeId id,const char *sName ) = 0;
	virtual const char* GetNodeName( TFlowNodeId id ) = 0;
	virtual TFlowNodeTypeId GetNodeTypeId( TFlowNodeId id ) = 0;
	virtual const char* GetNodeTypeName( TFlowNodeId id ) = 0;
	virtual void RemoveNode( const char * name ) = 0;
	virtual void RemoveNode( TFlowNodeId id ) = 0;
	//virtual void SetUserData( TFlowNodeId id, const XmlNodeRef& data ) = 0;
	//virtual XmlNodeRef GetUserData( TFlowNodeId id ) = 0;
	virtual bool LinkNodes( SFlowAddress from, SFlowAddress to ) = 0;
	virtual void UnlinkNodes( SFlowAddress from, SFlowAddress to ) = 0;
	//##@}

	// Description:
	//	 Registers hypergraph/editor listeners for visual flowgraph debugging.
	virtual void RegisterFlowNodeActivationListener(SFlowNodeActivationListener *listener) = 0;
	// Description:
	//	 Removes hypergraph/editor listeners for visual flowgraph debugging.
	virtual void RemoveFlowNodeActivationListener(SFlowNodeActivationListener *listener) = 0;

	virtual bool NotifyFlowNodeActivationListeners(TFlowNodeId srcNode, TFlowPortId srcPort, TFlowNodeId toNode, TFlowPortId toPort, const char *value) = 0;

	virtual void SetEntityId( TFlowNodeId, EntityId ) = 0;
	virtual EntityId GetEntityId( TFlowNodeId ) = 0;

	virtual IFlowGraphPtr GetClonedFlowGraph() const = 0;

	// Summary:
	//	 Gets node configuration.
	// Notes:
	//	 Low level functions.
	virtual void GetNodeConfiguration( TFlowNodeId id, SFlowNodeConfig& ) = 0;

	// node-graph interface
	virtual void SetRegularlyUpdated( TFlowNodeId, bool ) = 0;
	virtual void RequestFinalActivation( TFlowNodeId ) = 0;
	virtual void ActivateNode( TFlowNodeId ) = 0;
	virtual void ActivatePortAny( SFlowAddress output, const TFlowInputData& ) = 0;
	// Description:
	//	 Special function to activate port from other dll module and pass 
	//	 const char* string to avoid passing string class across dll boundaries
	virtual void ActivatePortCString( SFlowAddress output, const char *cstr ) = 0;

	virtual bool SetInputValue( TFlowNodeId node, TFlowPortId port, const TFlowInputData& ) = 0;
	virtual bool IsOutputConnected( SFlowAddress output ) = 0;
	virtual const TFlowInputData* GetInputValue( TFlowNodeId node, TFlowPortId port ) = 0;

	template <class T>
	ILINE void ActivatePort( const SFlowAddress output, const T& value )
	{
		static_cast<NFlowSystemUtils::IFlowSystemTypedHelper<T>*>(this)->DoActivatePort( output, value );
	}
	ILINE void ActivatePort( SFlowAddress output, const TFlowInputData& value )
	{
		ActivatePortAny( output, value );
	}

	// temporary solutions [ ask Dejan ]

	// Description:
	//	 Suspended flow graphs were needed for AI Action flow graphs.
	//	 Suspended flow graphs shouldn't be updated!
	//	 Nodes in suspended flow graphs should ignore OnEntityEvent notifications!
	virtual void SetSuspended( bool suspend = true ) = 0;
	//	Description:
	//	 Checks if the flow graph is suspended.
	virtual bool IsSuspended() const = 0;

	// AI action related

	// Summary:
	//	 Sets an AI Action
	//virtual void SetAIAction( IAIAction* pAIAction ) = 0;
	// Summary:
	//	 Gets an AI Action
	//virtual IAIAction* GetAIAction() const = 0;

	virtual void UnregisterFromFlowSystem() = 0;

	//virtual void GetMemoryUsage(ICrySizer * s) const = 0;

	// Graph tokens are gametokens which are unique to a particular flow graph
	struct SGraphToken
	{
		SGraphToken() : type(eFDT_Void) {}

		string name;
		EFlowDataTypes type;
	};
	virtual void RemoveGraphTokens() = 0;
	virtual bool AddGraphToken(const SGraphToken& token) = 0;
	virtual size_t GetGraphTokenCount() const = 0;
	virtual const IFlowGraph::SGraphToken* GetGraphToken(size_t index) const = 0;
	virtual string GetGlobalNameForGraphToken(const string& tokenName) const = 0;

	virtual TFlowGraphId GetGraphId() const = 0;
};

struct IFlowNodeFactory
{
	virtual ~IFlowNodeFactory(){}
	virtual void AddRef() = 0;
	virtual void Release() = 0;
	virtual IFlowNodePtr Create( IFlowNode::SActivationInfo* ) = 0;
	//virtual void GetMemoryUsage(ICrySizer * s) const = 0;
	virtual void Reset() = 0;
};

TYPEDEF_AUTOPTR(IFlowNodeFactory);
typedef IFlowNodeFactory_AutoPtr IFlowNodeFactoryPtr;

// Description:
//	 Structure that permits to iterate through the node type.
//	 For each node type the ID and the name are stored.
struct IFlowNodeTypeIterator
{
	virtual ~IFlowNodeTypeIterator(){}
	struct SNodeType
	{
		TFlowNodeTypeId		typeId;
		const char*			typeName;
	};
	virtual void AddRef() = 0;
	virtual void Release() = 0;
	virtual bool Next(SNodeType& nodeType) = 0;
};
TYPEDEF_AUTOPTR(IFlowNodeTypeIterator);
typedef IFlowNodeTypeIterator_AutoPtr IFlowNodeTypeIteratorPtr;

struct IFlowGraphInspector
{
	virtual ~IFlowGraphInspector(){}
	struct IFilter;
	TYPEDEF_AUTOPTR(IFilter);
	typedef IFilter_AutoPtr IFilterPtr;

	struct IFilter
	{
		virtual ~IFilter(){}
		enum EFilterResult
		{
			eFR_DontCare, // Lets others decide. [not used currently]
			eFR_Block,    // Blocks flow record.
			eFR_Pass      // Lets flow record pass.
		};
		virtual void AddRef() = 0;
		virtual void Release() = 0;
		// Summary:
		//	 Adds a new filter
		// Notes:
		//	 Filter can have subfilters. (these are generally ANDed)
		virtual void AddFilter(IFilterPtr pFilter) = 0;
		// Summary:
		//	 Removes the specified filter.
		virtual void RemoveFilter(IFilterPtr pFilter) = 0;
		// Summary:
		//	 Applies filter(s).
		virtual IFlowGraphInspector::IFilter::EFilterResult Apply(IFlowGraph * pGraph, const SFlowAddress from, const SFlowAddress to) = 0;		
	};


	virtual void AddRef() = 0;
	virtual void Release() = 0;
	// Description: 
	//	 Called once per frame before IFlowSystem::Update
	// See also:
	//	 IFlowSystem::Update
	virtual void PreUpdate(IFlowGraph * pGraph) = 0;   
	// Description:
	//	 Called once per frame after  IFlowSystem::Update
	// See also:
	//	 IFlowSystem::Update
	virtual void PostUpdate(IFlowGraph * pGraph) = 0;  
	virtual void NotifyFlow(IFlowGraph * pGraph, const SFlowAddress from, const SFlowAddress to) = 0;
	virtual void NotifyProcessEvent(IFlowNode::EFlowEvent event, IFlowNode::SActivationInfo * pActInfo, IFlowNode * pImpl) = 0;
	virtual void AddFilter(IFlowGraphInspector::IFilterPtr pFilter) = 0;
	virtual void RemoveFilter(IFlowGraphInspector::IFilterPtr pFilter) = 0;

	//virtual void GetMemoryUsage( ICrySizer *pSizer ) const=0;
};

TYPEDEF_AUTOPTR(IFlowGraphInspector);
typedef IFlowGraphInspector_AutoPtr IFlowGraphInspectorPtr;

struct IFlowSystem
{
	virtual ~IFlowSystem(){}

	virtual void Release() = 0;

	// Summary:
	//	 Updates the flow system.
	// Notes:
	//	 IGameFramework calls this every frame.
	virtual void Update() = 0;

	// Summary:
	//	 Resets the flow system.
	virtual void Reset(bool unload) = 0;

	virtual void Enable(bool enable) = 0;
	// Summary:
	//	 Reload all FlowNode templates; only in Sandbox!
	virtual void ReloadAllNodeTypes() = 0;

	// Summary:
	//	 Creates a flowgraph.
	virtual IFlowGraphPtr CreateFlowGraph() = 0;

	// Summary:
	//	 Type registration and resolving.
	virtual TFlowNodeTypeId RegisterType( const char *typeName, IFlowNodeFactoryPtr factory ) = 0;

	virtual bool UnregisterType(const char *type) = 0;
	// Summary:
	//	 Gets a type name from a flow node type ID.
	virtual const char* GetTypeName( TFlowNodeTypeId typeId ) = 0;
	// Summary:
	//	 Gets a flow node type ID from a type name.
	virtual TFlowNodeTypeId GetTypeId( const char* typeName ) = 0;
	// Summary:
	//	 Creates an iterator to visit the node type.
	virtual IFlowNodeTypeIteratorPtr CreateNodeTypeIterator() = 0;

	// Inspecting flowsystem.

	// Summary:
	//	 Registers an inspector for the flowsystem.
	virtual void RegisterInspector( IFlowGraphInspectorPtr pInspector, IFlowGraphPtr pGraph = 0 ) = 0;
	// Summary:
	//	 Unregisters an inspector for the flowsystem.
	virtual void UnregisterInspector( IFlowGraphInspectorPtr pInspector, IFlowGraphPtr pGraph = 0 ) = 0;
	// Summary:
	//	 Gets the default inspector for the flowsystem.
	virtual IFlowGraphInspectorPtr GetDefaultInspector() const = 0;
	// Summary:
	//	 Enables/disables inspectors for the flowsystem.
	virtual void EnableInspecting( bool bEnable ) = 0;
	// Summary:
	//	 Returns true if inspectoring is enable, false otherwise.
	virtual bool IsInspectingEnabled() const = 0;

	// Summary:
	//	Returns the graph with the specified ID (NULL if not found)
	virtual IFlowGraph* GetGraphById(TFlowGraphId graphId) = 0;

	// Gets memory statistics.
	//virtual void GetMemoryUsage(ICrySizer * s) const = 0;

	// Gets the module manager
	//virtual IFlowGraphModuleManager* GetIModuleManager() = 0;
};


#endif