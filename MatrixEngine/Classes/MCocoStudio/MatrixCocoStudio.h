
#ifndef __MCOCOSTUDIO_SCRIPT__
#define __MCOCOSTUDIO_SCRIPT__

//TODO
class  MatrixCocoStudio
{
public:
	MatrixCocoStudio(){};
	~MatrixCocoStudio(){};

	static MatrixCocoStudio* ShareMatrixCocoStudio(void);

	void RegisterScript(void);

protected:
private:
};

#endif