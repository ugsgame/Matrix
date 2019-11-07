
#ifndef __QC_APPLICATION__
#define __QC_APPLICATION__

#include "cocos2d.h"

USING_NS_CC;

class QcApplication:public CCApplication
{
public:
	QcApplication();
	~QcApplication();

	virtual int run(HWND hWnd, LPCTSTR szTitle, UINT width, UINT height);
	virtual bool initInstance(HWND hWnd, LPCTSTR szTitle, UINT width, UINT height);

	virtual void renderWorld();

protected:

private:
	
	LARGE_INTEGER       m_nLast;
};

#endif __QC_APPLICATION__