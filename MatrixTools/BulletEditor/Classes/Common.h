#ifndef __COMMON_H__
#define __COMMON_H__

#define MX_PROPERTY(varType, varName, funName)\
protected: varType varName;\
public: virtual varType get##funName(void);\
public: virtual void set##funName(varType var);

#define MX_PROPERTY_PASS_BY_REF(varType, varName, funName)\
protected: varType varName;\
public: virtual const varType& get##funName(void);\
public: virtual void set##funName(const varType& var);

#define DEF_PNG_DEFAULT_BULLET		"Default/default_bullet.png"
#define DEF_PNG_DEFAULT_PARTICLE	"Default/default_particles.png"

#endif