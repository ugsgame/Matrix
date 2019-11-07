
#ifndef __EMITTERCONFIG__
#define __EMITTERCONFIG__

#include "Common.h"

#include <string>
#include <vector>

class BulletDisplay;

enum PositionType
{ 
    PositionTypeFree,
    PositionTypeRelative,
    PositionTypeGrouped,
};

enum BlendValue
{
	MX_ZERO = 0,
	MX_ONE = 1,
	MX_SRC_COLOR = 0x0300,
	MX_ONE_MINUS_SRC_COLOR = 0x0301,
	MX_DST_COLOR = 0x0306,
	MX_ONE_MINUS_DST_COLOR = 0x0307,
	MX_SRC_ALPHA = 0x0302,
	MX_ONE_MINUS_SRC_ALPHA = 0x0303,
	MX_DST_ALPHA = 0x0304,
	MX_ONE_MINUS_DST_ALPHA = 0x0305,
	MX_SRC_ALPHA_SATURATE = 0x0308,
	MX_CONSTANT_COLOR = 0x8001,
	MX_ONE_MINUS_CONSTANT_COLOR = 0x8002,
	MX_CONSTANT_ALPHA = 0x8003,
	MX_ONE_MINUS_CONSTANT_ALPHA = 0x8004,
};

struct DisplayConfig
{
	std::string		displayFile;
	int				displayType;

	float			displayRoation;
	float			displayScale;

	float			displayAnimSpeed;

	BlendValue		displaySrcBlend;
	BlendValue		displayDstBlend;

	std::string		atlasName;
	std::string	    armatureName;
	std::string		animationName;

	std::vector<std::string> frames;
	DisplayConfig()
	{
		displayType = 0;
		displayRoation = 0;
		displayScale = 1;
		displayAnimSpeed = 10;

		displaySrcBlend = MX_SRC_ALPHA;
		displayDstBlend = MX_ONE_MINUS_SRC_ALPHA;
	}

	BulletDisplay* create();
};

typedef struct FRect
{
	float x;
	float y;
	float width;
	float height;

	FRect()
	{
		x = 0;
		y = 0;
		width = 0;
		height = 0;
	}
	FRect(float x,float y,float w,float h)
	{
		this->x = x;
		this->y = y;
		this->width = w;
		this->height = h;
	}
}fRect;

struct FrameData
{
	fRect rect;
	std::string name;

	FrameData()
	{
	}
};

class EmitterConfig
{
public:
	EmitterConfig();
	~EmitterConfig();
public:
	static EmitterConfig* shareEmitterConfig(); 

	bool serialization(const char* configFile);
	bool unserialization(const char* configFile);

	bool seriCurrent();
	bool unseriCurrent();

	int checkDisplayType(const char* fileName);
	std::string getAtlasImageFile(const char* fileName);
	std::vector<FrameData> getDisplayFrameDatas();
	std::vector<FrameData> getDisplayFrameDatas(const char* fileName);
	std::vector<std::string> getArmatureAnimations();

	bool isFileExist(const char* file);
	void resetDisplay();
public: 
	//Emitter
	MX_PROPERTY(int,m_eEmitterMode,EmitterMode);
	MX_PROPERTY(int,m_iInitCount,InitCount);
	MX_PROPERTY(float,m_fDuration,Duration);
	MX_PROPERTY(float,m_fDurationVar,DurationVar);
	MX_PROPERTY(float,m_fInterimTime,InterimTime);
	MX_PROPERTY(float,m_fInterimTimeVar,InterimTimeVar);
	MX_PROPERTY(PositionType,m_ePositionType,PositionType);
	MX_PROPERTY(bool,m_bIsPointToTarget,IsPointToTarget);
	MX_PROPERTY(float,m_fEmissionRate,EmissionRate);
	MX_PROPERTY(float,m_fEmissionRateVar,EmissionRateVar);
	MX_PROPERTY(float,m_fEmissionAngle,EmissionAngle);
	MX_PROPERTY(unsigned int,m_uBeamCount,BeamCount);
	MX_PROPERTY(unsigned int,m_uBeamCountVar,BeamCountVar);
	MX_PROPERTY(float,m_fFieldAngle,FieldAngle);
	MX_PROPERTY(float,m_fFieldAngleVar,FieldAngleVar);
	MX_PROPERTY(float,m_fSpinSpeed,SpinSpeed);
	//Bullet
	MX_PROPERTY(float,m_fSpeed,Speed);
	MX_PROPERTY(float,m_fSpeedVar,SpeedVar);
	MX_PROPERTY(float,m_fSpeedDecay,SpeedDecay);
	MX_PROPERTY(float,m_fSpeedLimit,SpeedLimit);
	MX_PROPERTY(float,m_fLifeTime,LifeTime);
	MX_PROPERTY(float,m_fLifeTimeVar,LifeTimeVar);
	MX_PROPERTY(float,m_fBulletSpinSpeed,BulletSpinSpeed)
	MX_PROPERTY(float,m_fBulletSpinSpeedVar,BulletSpinSpeedVar)
	MX_PROPERTY(float,m_fDamage,Damage);
	MX_PROPERTY(bool,m_bIsFollow,IsFollow);
	MX_PROPERTY(float,m_fCurvity,Curvity);
	MX_PROPERTY(float,m_fSinAmplitude,SinAmplitude);
	MX_PROPERTY(float,m_fSinRate,SinRate);
	//
	//Display
	MX_PROPERTY(DisplayConfig*,m_pDisplayConfig,DisplayConfig);
	MX_PROPERTY(BlendValue,m_eSrcBlend,SrcBlend);
	MX_PROPERTY(BlendValue,m_eDstBlend,DstBlend);
	MX_PROPERTY(float, m_fDisplayRotation,DisplayRotation);
	MX_PROPERTY(float, m_fDisplayScale,DisplayScale);
	MX_PROPERTY(float, m_fAnimateSpeed,AnimateSpeed);
	MX_PROPERTY(std::vector<std::string>,m_vAnimateFrames,AnimateFrames);
	//Event
	//
protected:
private:

	static EmitterConfig* sm_pSharedEmitterConfig;
};

#endif