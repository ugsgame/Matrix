
#include "JsonHelper.h"


static JsonHelper* sharedHelper = NULL;

JsonHelper::JsonHelper()
{
    
}

JsonHelper::~JsonHelper()
{
    
}

JsonHelper* JsonHelper::shareHelper()
{
    if (!sharedHelper) {
        sharedHelper = new JsonHelper();
    }
    return sharedHelper;
}

void JsonHelper::purgeJsonHelper()
{
	 CC_SAFE_DELETE(sharedHelper);
}

cocos2d::CCDictionary* JsonHelper::getSubDictionary(cocos2d::CCDictionary* root,const char* key)
{
    if (!root) {
        return NULL;
    }
    cocos2d::CCObject* obj = root->objectForKey(key);
    if (!obj) {
        return NULL;
    }
    return (cocos2d::CCDictionary*)(obj);
}

int JsonHelper::getIntValue(cocos2d::CCDictionary* root,const char* key)
{
    if (!root) {
        return 0;
    }
    cocos2d::CCObject* obj = root->objectForKey(key);
    if (!obj) {
        return 0;
    }
    
    cocos2d::CCString* cstr = (cocos2d::CCString*)(obj);
    return cstr->intValue();
}

float JsonHelper::getFloatValue(cocos2d::CCDictionary* root,const char* key)
{
    if (!root) {
        return 0.0;
    }
    cocos2d::CCObject* obj = root->objectForKey(key);
    if (!obj) {
        return 0.0f;
    }
    cocos2d::CCString* cstr = (cocos2d::CCString*)(obj);
    return cstr->floatValue();
}

const char* JsonHelper::getStringValue(cocos2d::CCDictionary* root,const char* key)
{
    if (!root) {
        return NULL;
    }
    cocos2d::CCObject* obj = root->objectForKey(key);
    if (!obj) {
        return NULL;
    }
    cocos2d::CCString* cstr = (cocos2d::CCString*)(obj);
    return cstr->m_sString.c_str();
}

bool JsonHelper::getBooleanValue(cocos2d::CCDictionary* root,const char* key)
{
    return 0 == this->getIntValue(root, key) ? false : true;
}

cocos2d::CCArray* JsonHelper::getArrayValue(cocos2d::CCDictionary *root, const char *key)
{
    if (!root) {
        return NULL;
    }
    cocos2d::CCObject* obj = root->objectForKey(key);
    if (!obj) {
        return NULL;
    }
    cocos2d::CCArray* array = (cocos2d::CCArray*)(obj);
    return array;
}

cocos2d::CCObject* JsonHelper::checkObjectExist(cocos2d::CCDictionary *root, const char *key)
{
    if (!root) {
        return NULL;
    }
    return root->objectForKey(key);
}

int JsonHelper::objectToIntValue(cocos2d::CCObject *obj)
{
    if (!obj)
    {
        return 0;
    }
    cocos2d::CCString* cstr = (cocos2d::CCString*)(obj);
    return cstr->intValue();
}

float JsonHelper::objectToFloatValue(cocos2d::CCObject *obj)
{
    if (!obj)
    {
        return 0.0f;
    }
    cocos2d::CCString* cstr = (cocos2d::CCString*)(obj);
    return cstr->floatValue();
}

const char* JsonHelper::objectToStringValue(cocos2d::CCObject *obj)
{
    if (!obj)
    {
        return NULL;
    }
    cocos2d::CCString* cstr = (cocos2d::CCString*)(obj);
    return cstr->m_sString.c_str();
}

bool JsonHelper::objectToBooleanValue(cocos2d::CCObject *obj)
{
    if (!obj)
    {
        return 0;
    }
    return this->objectToIntValue(obj) != 0? true:false;
}

cocos2d::CCArray* JsonHelper::objectToCCArray(cocos2d::CCObject *obj)
{
    if (!obj)
    {
        return NULL;
    }
    cocos2d::CCArray* array = (cocos2d::CCArray*)(obj);
    return array;
}


const rapidjson::Value& JsonHelper::getSubDictionary_json(const rapidjson::Value &root, const char* key)
{
	return root[key];
}

const rapidjson::Value& JsonHelper::getSubDictionary_json(const rapidjson::Value &root, const char* key, int idx)
{
    return root[key][idx];
}

const rapidjson::Value& JsonHelper::getSubDictionary_json(const rapidjson::Value &root, int idx)
{
    return root[idx];
}

int JsonHelper::getIntValue_json(const rapidjson::Value& root, const char* key, int def)
{
    int nRet = def;
    do {
        CC_BREAK_IF(root.IsNull());
        CC_BREAK_IF(root[key].IsNull());
        nRet = root[key].GetInt();
    } while (0);
    
    return nRet;
}


float JsonHelper::getFloatValue_json(const rapidjson::Value& root,const char* key, float def)
{
	float fRet = def;
    do {
        CC_BREAK_IF(root.IsNull());
        CC_BREAK_IF(root[key].IsNull());
        fRet = (float)root[key].GetDouble();
    } while (0);
    
    return fRet;
}

bool JsonHelper::getBooleanValue_json(const rapidjson::Value& root,const char* key, bool def)
{
    bool bRet = def;
    do {
        CC_BREAK_IF(root.IsNull());
        CC_BREAK_IF(root[key].IsNull());
        bRet = root[key].GetBool();
    } while (0);
    
    return bRet;
}

const char* JsonHelper::getStringValue_json(const rapidjson::Value& root,const char* key, const char *def)
{
    const char* sRet = def;
    do {
        CC_BREAK_IF(root.IsNull());
        CC_BREAK_IF(root[key].IsNull());
        sRet = root[key].GetString();
    } while (0);
    
    return sRet;
}



int JsonHelper::getArrayCount_json(const rapidjson::Value& root, const char* key, int def)
{
    int nRet = def;
    do {
        CC_BREAK_IF(root.IsNull());
        CC_BREAK_IF(root[key].IsNull());
        nRet = (int)(root[key].Size());
    } while (0);
    
    return nRet;
}


int JsonHelper::getIntValueFromArray_json(const rapidjson::Value& root,const char* arrayKey,int idx, int def)
{
    int nRet = def;
    do {
        CC_BREAK_IF(root.IsNull());
        CC_BREAK_IF(root[arrayKey].IsNull());
        CC_BREAK_IF(root[arrayKey][idx].IsNull());
        nRet = root[arrayKey][idx].GetInt();
    } while (0);
    
    return nRet;
}


float JsonHelper::getFloatValueFromArray_json(const rapidjson::Value& root,const char* arrayKey,int idx, float def)
{
    float fRet = def;
    do {
        CC_BREAK_IF(root.IsNull());
        CC_BREAK_IF(root[arrayKey].IsNull());
        CC_BREAK_IF(root[arrayKey][idx].IsNull());
        fRet = (float)root[arrayKey][idx].GetDouble();
    } while (0);
    
    return fRet;
}

bool JsonHelper::getBoolValueFromArray_json(const rapidjson::Value& root,const char* arrayKey,int idx, bool def)
{
	bool bRet = def;
    do {
        CC_BREAK_IF(root.IsNull());
        CC_BREAK_IF(root[arrayKey].IsNull());
        CC_BREAK_IF(root[arrayKey][idx].IsNull());
        bRet = root[arrayKey][idx].GetBool();
    } while (0);
    
    return bRet;
}

const char* JsonHelper::getStringValueFromArray_json(const rapidjson::Value& root,const char* arrayKey,int idx, const char *def)
{
    const char *sRet = def;
    do {
        CC_BREAK_IF(root.IsNull());
        CC_BREAK_IF(root[arrayKey].IsNull());
        CC_BREAK_IF(root[arrayKey][idx].IsNull());
        sRet = root[arrayKey][idx].GetString();
    } while (0);
    
    return sRet;
}

const rapidjson::Value &JsonHelper::getDictionaryFromArray_json(const rapidjson::Value &root, const char* key,int idx)
{
	return root[key][idx];
}

bool JsonHelper::checkObjectExist_json(const rapidjson::Value &root)
{
    bool bRet = false;
    do {
        CC_BREAK_IF(root.IsNull());
        bRet = true;
    } while (0);
    
    return bRet;
}

bool JsonHelper::checkObjectExist_json(const rapidjson::Value &root,const char* key)
{
    bool bRet = false;
    do {
        CC_BREAK_IF(root.IsNull());
        bRet = root.HasMember(key);
    } while (0);
    
    return bRet;
}

bool JsonHelper::checkObjectExist_json(const rapidjson::Value &root, int index)
{
    bool bRet = false;
    do {   
        CC_BREAK_IF(root.IsNull());
        CC_BREAK_IF(!root.IsArray());
        CC_BREAK_IF(index < 0 || root.Size() <= (unsigned int )index);
        bRet = true;
    } while (0);

    return bRet;
}