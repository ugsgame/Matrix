#ifndef __MAIN_SCENE_H__
#define __MAIN_SCENE_H__

#include "cocos2d.h"
#include "cocos-ext.h"
#include "ExtensionMacros.h"

struct ArmatureContainer
{
	//
	std::string armatureFile;
	//armature名字
	std::string armatureName;
	//armature包含的movenent名字表
	std::vector<std::string> movementList;
};

class MainScene : public cocos2d::CCLayer
{
public:
    // Here's a difference. Method 'init' in cocos2d-x returns bool, instead of returning 'id' in cocos2d-iphone
    virtual bool init();  

	virtual void update(float delta);
	virtual void draw();

    // there's no 'id' in cpp, so we recommend returning the class instance pointer
    static cocos2d::CCScene* scene();

	// 
	static MainScene* sharedMainScene();

	std::map<std::string,ArmatureContainer> getArmatureContainer()
	{
		return armatureContainer;
	}

	std::string getArmatureFile(std::string armatureName)
	{
		return armatureContainer[armatureName].armatureFile;
	}

	std::string getArmaturePath(std::string armatureName)
	{
		std::string armatureFile = armatureContainer[armatureName].armatureFile;
		return armatureFile.erase(armatureFile.length()-armatureName.length()-11,armatureFile.length());
	}

	std::string getCurArmature()
	{
		return mArmatureName;
	}

	std::vector<std::string> getArmatureList()
	{
		return armatureList;
	}
	//
	std::vector<std::string> getMovementList(std::string armatureName)
	{
		return armatureContainer[armatureName].movementList;
	}
    
    // implement the "static node()" method manually
    CREATE_FUNC(MainScene);

public:
	//Actions
	void changeArmature(std::string armatureName);
	bool openArmature(std::string armatureName);
	bool exportArmature(std::string filePath);
	void playAnimation(std::string animName,bool loop);
	void resize(float w,float h);
	void center();
	void save(const unsigned char* buffer);
	void load(const unsigned char* buffer);

private:
	//TODO:画背景网格
	void drawGrid();
	void exportPlist(std::string plistPath,std::string exportPath);
	void exportAnimXML(std::string exportPath);
private:
	//curent armature
	cocos2d::extension::CCArmature* mArmature;
	cocos2d::extension::CCArmatureAnimation* mAnimation; 
	std::string mArmatureName;

	//ArmatureContainer
	std::map<std::string,ArmatureContainer> armatureContainer;
	std::vector<std::string> armatureList;

	static MainScene * sm_pSharedMainScene;
};

#endif // __HELLOWORLD_SCENE_H__
