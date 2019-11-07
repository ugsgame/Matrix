
#ifndef __ACTION_CONNECTION__
#define __ACTION_CONNECTION__

#include <string>
#include <vector>

class ActionConnect
{
public:
	ActionConnect();
	~ActionConnect();

	static ActionConnect* shareActionConnect();

	//MainScene
	std::vector<std::string> getArmaturePaths();
	std::string getArmatureFile(std::string armatureName);
	std::string getArmaturePath(std::string armatureName);
	std::string getCurArmature();
	std::vector<std::string> getArmatureList();
	std::vector<std::string> getMovementList(std::string armature);

	void changeArmature(std::string armature); 
	bool openArmature(std::string armature);
	bool exportArmature(std::string filePath);
	void playAnimation(std::string anim,bool loop);
	void resize(float w,float h);
	void center();

	//
	//

protected:
private:

	static ActionConnect* _shareActionConnect;
};

#endif