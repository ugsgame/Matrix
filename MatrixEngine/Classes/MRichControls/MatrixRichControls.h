
#ifndef  __MRICHCONTROLS_SCRIPT__
#define  __MRICHCONTROLS_SCRIPT__

class MatrixRichControls
{
public:
	MatrixRichControls(){};
	~MatrixRichControls(){};

	static MatrixRichControls* ShareMatrixRichControls(void);

	void RegisterScript(void);
protected:
private:
};

#endif