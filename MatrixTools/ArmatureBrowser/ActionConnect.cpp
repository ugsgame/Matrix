
#include "ActionConnect.h"
#include "MainScene.h"

ActionConnect * ActionConnect::_shareActionConnect = 0;

ActionConnect::ActionConnect()
{

}

ActionConnect::~ActionConnect()
{

}

ActionConnect* ActionConnect::shareActionConnect()
{
	if(!_shareActionConnect)
	{
		_shareActionConnect = new ActionConnect();
	}
	return _shareActionConnect;
}

std::vector<std::string> ActionConnect::getArmaturePaths()
{
	std::vector<std::string> paths;
	std::vector<std::string> armature = getArmatureList();
	std::map<std::string,ArmatureContainer> container = MainScene::sharedMainScene()->getArmatureContainer();

	for (int i = 0; i < armature.size(); i++)
	{
		paths.push_back(container[armature[i]].armatureFile);
	}

	return paths;
}

std::string ActionConnect::getArmatureFile(std::string armatureName)
{
	MainScene* scene = MainScene::sharedMainScene();
	return scene->getArmatureFile(armatureName);
}

std::string ActionConnect::getArmaturePath(std::string armatureName)
{
	MainScene* scene = MainScene::sharedMainScene();
	return scene->getArmaturePath(armatureName);
}

std::string ActionConnect::getCurArmature()
{
	MainScene* scene = MainScene::sharedMainScene();
	return scene->getCurArmature();
}

std::vector<std::string> ActionConnect::getArmatureList()
{
	MainScene* scene = MainScene::sharedMainScene();
	return scene->getArmatureList();
}

std::vector<std::string> ActionConnect::getMovementList(std::string armature)
{
	MainScene* scene = MainScene::sharedMainScene();
	return scene->getMovementList(armature);
}

void ActionConnect::changeArmature(std::string armature)
{
	MainScene::sharedMainScene()->changeArmature(armature);
}

bool ActionConnect::openArmature(std::string armature)
{
	MainScene* scene = MainScene::sharedMainScene();
	{
		if (scene)
		{
			return scene->openArmature(armature);
		}
	}
	return false;
}

bool ActionConnect::exportArmature(std::string filePath)
{
	MainScene* scene = MainScene::sharedMainScene();
	{
		if (scene)
		{
			return scene->exportArmature(filePath);
		}
	}
	return false;
}

void ActionConnect::playAnimation(std::string anim, bool loop)
{
	MainScene* scene = MainScene::sharedMainScene();
	{
		if (scene)
		{
			scene->playAnimation(anim,loop);
		}
	}
}

void ActionConnect::resize(float w,float h)
{
	MainScene* scene = MainScene::sharedMainScene();
	{
		if (scene)
		{
			scene->resize(w,h);
		}
	}
}

void ActionConnect::center()
{
	MainScene* scene = MainScene::sharedMainScene();
	{
		if (scene)
		{
			scene->center();
		}
	}
}

