
#include "cocos2d.h"
#include "ScriptBind_CCParticleSystem.h"

USING_NS_CC;

ScriptBind_CCParticleSystem::ScriptBind_CCParticleSystem()
{
	REGISTER_METHOD(Create);
	REGISTER_METHOD(IsActive);
	REGISTER_METHOD(IsBlendAdditive);
	REGISTER_METHOD(SetBlendAdditive);
	REGISTER_METHOD(InitIndices);
	REGISTER_METHOD(InitTexCoordsWithRect);
	REGISTER_METHOD(SetDisplayFrame);
	REGISTER_METHOD(SetTextureWithRect);
	REGISTER_METHOD(SetPositionType);
	REGISTER_METHOD(GetPositionType);
	REGISTER_METHOD(SetEmitterMode);
	REGISTER_METHOD(GetEmitterMode);
	REGISTER_METHOD(SetRotationIsDir);
	REGISTER_METHOD(GetRotationIsDir);
	REGISTER_METHOD(IsAutoRemoveOnFinish);
	REGISTER_METHOD(SetAutoRemoveOnFinish);
	REGISTER_METHOD(SetTexture);
	REGISTER_METHOD(PostStep);
	REGISTER_METHOD(SetBatchNode);
	REGISTER_METHOD(Start);
	REGISTER_METHOD(Play);
	REGISTER_METHOD(Stop);
}

ScriptBind_CCParticleSystem::~ScriptBind_CCParticleSystem()
{

}

CCParticleSystemQuad* ScriptBind_CCParticleSystem::Create(mono::string plistFile)
{
	return CCParticleSystemQuad::create(ToMatrixString(plistFile).c_str());
}

//particle pro
bool ScriptBind_CCParticleSystem::IsActive(CCParticleSystemQuad* particle)
{
	CCAssert(particle,"");
	return particle->isActive();
}

bool ScriptBind_CCParticleSystem::IsBlendAdditive(CCParticleSystemQuad* particle)
{
	CCAssert(particle,"");
	return particle->isBlendAdditive();
}
void ScriptBind_CCParticleSystem::SetBlendAdditive(CCParticleSystemQuad* particle,bool value)
{
	CCAssert(particle,"");
	particle->setBlendAdditive(value);
}
//

void ScriptBind_CCParticleSystem::InitIndices(CCParticleSystemQuad* particle)
{
	CCAssert(particle,"");
	particle->initIndices();
}
void ScriptBind_CCParticleSystem::InitTexCoordsWithRect(CCParticleSystemQuad* particle,float rx,float ry,float rw,float rh)
{
	CCAssert(particle,"");
	particle->initTexCoordsWithRect(CCRect(rx,ry,rw,rh));
}

void ScriptBind_CCParticleSystem::SetDisplayFrame(CCParticleSystemQuad* particle,CCSpriteFrame *spriteFrame)
{
	CCAssert(particle,"");
	particle->setDisplayFrame(spriteFrame);
}

void ScriptBind_CCParticleSystem::SetTextureWithRect(CCParticleSystemQuad* particle,CCTexture2D *texture, float rx, float ry,float rw,float rh)
{
	CCAssert(particle,"");
	particle->setTextureWithRect(texture,CCRect(rx,ry,rw,rh));
}

void ScriptBind_CCParticleSystem::SetPositionType(CCParticleSystemQuad* particle,int type)
{
	CCAssert(particle,"");
	particle->setPositionType((tCCPositionType)type);
}
int  ScriptBind_CCParticleSystem::GetPositionType(CCParticleSystemQuad* particle)
{
	CCAssert(particle,"");
	return (int)particle->getPositionType();
}

void ScriptBind_CCParticleSystem::SetEmitterMode(CCParticleSystemQuad* particle,int mode)
{
	CCAssert(particle,"");
	particle->setEmitterMode(mode);
}
int ScriptBind_CCParticleSystem::GetEmitterMode(CCParticleSystemQuad* particle)
{
	CCAssert(particle,"");
	return particle->getEmitterMode();
}

void ScriptBind_CCParticleSystem::SetRotationIsDir(CCParticleSystemQuad* particle,bool var)
{
	CCAssert(particle,"");
	particle->setRotationIsDir(var);
}

bool ScriptBind_CCParticleSystem::GetRotationIsDir(CCParticleSystemQuad* particle)
{
	CCAssert(particle,"");
	return particle->getRotationIsDir();
}

bool ScriptBind_CCParticleSystem::IsAutoRemoveOnFinish(CCParticleSystemQuad* particle)
{
	CCAssert(particle,"");
	return particle->isAutoRemoveOnFinish();
}
void ScriptBind_CCParticleSystem::SetAutoRemoveOnFinish(CCParticleSystemQuad* particle,bool var)
{
	CCAssert(particle,"");
	particle->setAutoRemoveOnFinish(var);
}

void ScriptBind_CCParticleSystem::SetTexture(CCParticleSystemQuad* particle,CCTexture2D* texture)
{
	CCAssert(particle,"");
	particle->setTexture(texture);
}

void ScriptBind_CCParticleSystem::PostStep(CCParticleSystemQuad* particle)
{
	CCAssert(particle,"");
	particle->postStep();
}
void ScriptBind_CCParticleSystem::SetBatchNode(CCParticleSystemQuad* particle,CCParticleBatchNode* batchNode)
{
	CCAssert(particle,"");
	particle->setBatchNode(batchNode);
}	

void ScriptBind_CCParticleSystem::Start(CCParticleSystemQuad* particle)
{
	CCAssert(particle,"");
	particle->startSystem();
}

void ScriptBind_CCParticleSystem::Play(CCParticleSystemQuad* particle)
{
	CCAssert(particle,"");
	particle->resetSystem();
}
void ScriptBind_CCParticleSystem::Stop(CCParticleSystemQuad* particle)
{
	CCAssert(particle,"");
	particle->stopSystem();
}