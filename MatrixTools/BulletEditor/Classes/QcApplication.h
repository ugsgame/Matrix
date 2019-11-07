
#ifndef __QC_APPLICATION__
#define __QC_APPLICATION__

#include "platform/win32/CCApplication.h"

class QcApplication:public cocos2d::CCApplication
{
public:
	QcApplication();
	~QcApplication();
public:
	virtual int run(HWND hWnd, LPCTSTR szTitle, UINT width, UINT height);
	virtual bool initInstance(HWND hWnd, LPCTSTR szTitle, UINT width, UINT height);

	virtual void renderWorld();
protected:

private:
	
	LARGE_INTEGER       m_nLast;
};

#endif __QC_APPLICATION__