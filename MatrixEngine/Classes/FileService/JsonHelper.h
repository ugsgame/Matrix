

#ifndef __JSON_HELPER_H__
#define __JSON_HELPER_H__

#include "cocos2d.h"
#include "Json//document.h"

#define JSONTOOL JsonHelper::shareHelper()


class JsonHelper
{
public:
    JsonHelper();
    ~JsonHelper();
    static JsonHelper* shareHelper();
	static void purgeJsonHelper();
    cocos2d::CCDictionary* getSubDictionary(cocos2d::CCDictionary* root,const char* key);
    int   getIntValue(cocos2d::CCDictionary* root,const char* key);
    float getFloatValue(cocos2d::CCDictionary* root,const char* key);
    const char* getStringValue(cocos2d::CCDictionary* root,const char* key);
    bool  getBooleanValue(cocos2d::CCDictionary* root,const char* key);
    cocos2d::CCArray* getArrayValue(cocos2d::CCDictionary* root,const char* key);
    cocos2d::CCObject* checkObjectExist(cocos2d::CCDictionary* root,const char* key);
    int   objectToIntValue(cocos2d::CCObject* obj);
    float objectToFloatValue(cocos2d::CCObject* obj);
    const char* objectToStringValue(cocos2d::CCObject* obj);
    bool  objectToBooleanValue(cocos2d::CCObject* obj);
    cocos2d::CCArray* objectToCCArray(cocos2d::CCObject* obj);
    
	const rapidjson::Value& getSubDictionary_json(const rapidjson::Value &root, const char* key);
    const rapidjson::Value& getSubDictionary_json(const rapidjson::Value &root, const char* key, int idx);
    const rapidjson::Value& getSubDictionary_json(const rapidjson::Value &root, int idx);
    
	int   getIntValue_json(const rapidjson::Value& root, const char* key, int def = 0);
	float getFloatValue_json(const rapidjson::Value& root,const char* key, float def = 0.0f);
    bool  getBooleanValue_json(const rapidjson::Value& root,const char* key, bool def = false);
    const char* getStringValue_json(const rapidjson::Value& root,const char* key, const char *def = NULL);
    int   getArrayCount_json(const rapidjson::Value& root,const char* key, int def = 0);
	
    int   getIntValueFromArray_json(const rapidjson::Value& root,const char* arrayKey,int idx, int def = 0);
	float getFloatValueFromArray_json(const rapidjson::Value& root,const char* arrayKey,int idx, float def = 0.0f);
	bool  getBoolValueFromArray_json(const rapidjson::Value& root,const char* arrayKey,int idx, bool def = false);
	const char* getStringValueFromArray_json(const rapidjson::Value& root,const char* arrayKey,int idx, const char *def = NULL);
	const rapidjson::Value &getDictionaryFromArray_json(const rapidjson::Value &root, const char* key,int idx);
	bool checkObjectExist_json(const rapidjson::Value &root);
    bool checkObjectExist_json(const rapidjson::Value &root, const char* key);
    bool checkObjectExist_json(const rapidjson::Value &root, int index);
};

#endif /* defined(__JSON_HELPER_H__) */