
#ifndef __MATRIX_SCRIPT__
#define __MATRIX_SCRIPT__

struct IMonoAssembly;

//TODO

class MatrixScript
{
public:
	MatrixScript(){}
	~MatrixScript(){}

	static MatrixScript* ShareMatrixScript();

	bool SetupMatrixScript(void);
	
	void SetEngineAssembly(IMonoAssembly* assembly){ pEngineAssembly =assembly; }
	void SetGameAssembly(IMonoAssembly* assembly){ pGameAssembly = assembly; }

	IMonoAssembly* GetEngineAssembly(){ return pEngineAssembly;}
	IMonoAssembly* GetGameAssembly(){ return pGameAssembly; }

protected:

	void RegisterScript(void){};

private:

	IMonoAssembly* pEngineAssembly;
	IMonoAssembly* pGameAssembly;
};


#endif