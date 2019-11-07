
#ifndef __SCRIPTBIND_CCPARTICLE__
#define __SCRIPTBIND_CCPARTICLE__

#include "ScriptBind_Cocos2d.h"

class cocos2d::CCParticleSystemQuad;
class cocos2d::CCParticleBatchNode;
class cocos2d::CCTexture2D;
class cocos2d::CCRect;

class ScriptBind_CCParticleSystem: public ScriptBind_Cocos2d
{
public:
	ScriptBind_CCParticleSystem();
	~ScriptBind_CCParticleSystem();

	virtual const char* GetClassName(){ return "NativeParticleSystem"; }

	static cocos2d::CCParticleSystemQuad* Create(mono::string plistFile);

	//particle pro
	//TODO:add more
	static bool IsActive(cocos2d::CCParticleSystemQuad* particle);

	static bool IsBlendAdditive(cocos2d::CCParticleSystemQuad* particle);
	static void SetBlendAdditive(cocos2d::CCParticleSystemQuad* particle,bool value);
	//
	static void InitIndices(cocos2d::CCParticleSystemQuad* particle);
	static void InitTexCoordsWithRect(cocos2d::CCParticleSystemQuad* particle,float rx,float fy,float rw,float rh);

	static void SetDisplayFrame(cocos2d::CCParticleSystemQuad* particle,cocos2d::CCSpriteFrame *spriteFrame);

	static void SetTextureWithRect(cocos2d::CCParticleSystemQuad* particle,cocos2d::CCTexture2D *texture, float rx, float ry,float rw,float rh);

	static void SetPositionType(cocos2d::CCParticleSystemQuad* particle,int type);
	static int GetPositionType(cocos2d::CCParticleSystemQuad* particle);

	static void SetEmitterMode(cocos2d::CCParticleSystemQuad* particle,int mode);
	static int GetEmitterMode(cocos2d::CCParticleSystemQuad* particle);

	static void SetRotationIsDir(cocos2d::CCParticleSystemQuad* particle,bool var);
	static bool GetRotationIsDir(cocos2d::CCParticleSystemQuad* particle);

	static bool IsAutoRemoveOnFinish(cocos2d::CCParticleSystemQuad* particle);
	static void SetAutoRemoveOnFinish(cocos2d::CCParticleSystemQuad* particle,bool var);

	static void SetTexture(cocos2d::CCParticleSystemQuad* particle,cocos2d::CCTexture2D* texture);

	//static void UpdateQuadWithParticle(tCCParticle* particle, const CCPoint& newPosition);

	static void PostStep(cocos2d::CCParticleSystemQuad* particle);
	static void SetBatchNode(cocos2d::CCParticleSystemQuad* particle,cocos2d::CCParticleBatchNode* batchNode);

	static void Start(cocos2d::CCParticleSystemQuad* particle);
	static void Play(cocos2d::CCParticleSystemQuad* particle);
	static void Stop(cocos2d::CCParticleSystemQuad* particle);
protected:
private:
};

#endif