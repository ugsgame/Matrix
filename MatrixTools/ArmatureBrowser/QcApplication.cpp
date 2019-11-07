
#include "QcApplication.h"

//////////////////////////////////////////////////////////////////////////
// Local function
//////////////////////////////////////////////////////////////////////////
static void PVRFrameEnableControlWindow(bool bEnable)
{
	HKEY hKey = 0;

	// Open PVRFrame control key, if not exist create it.
	if(ERROR_SUCCESS != RegCreateKeyExW(HKEY_CURRENT_USER,
		L"Software\\Imagination Technologies\\PVRVFRame\\STARTUP\\",
		0,
		0,
		REG_OPTION_NON_VOLATILE,
		KEY_ALL_ACCESS,
		0,
		&hKey,
		NULL))
	{
		return;
	}

	const WCHAR* wszValue = L"hide_gui";
	const WCHAR* wszNewData = (bEnable) ? L"NO" : L"YES";
	WCHAR wszOldData[256] = {0};
	DWORD   dwSize = sizeof(wszOldData);
	LSTATUS status = RegQueryValueExW(hKey, wszValue, 0, NULL, (LPBYTE)wszOldData, &dwSize);
	if (ERROR_FILE_NOT_FOUND == status              // the key not exist
		|| (ERROR_SUCCESS == status                 // or the hide_gui value is exist
		&& 0 != wcscmp(wszNewData, wszOldData)))    // but new data and old data not equal
	{
		dwSize = sizeof(WCHAR) * (wcslen(wszNewData) + 1);
		RegSetValueEx(hKey, wszValue, 0, REG_SZ, (const BYTE *)wszNewData, dwSize);
	}

	RegCloseKey(hKey);
}

QcApplication::QcApplication()
{

}

QcApplication::~QcApplication()
{

}

int QcApplication::run(HWND hWnd, LPCTSTR szTitle, UINT width, UINT height)
{
	PVRFrameEnableControlWindow(false);

	//
	LARGE_INTEGER nFreq;

	QueryPerformanceFrequency(&nFreq);
	QueryPerformanceCounter(&m_nLast);

	if (!initInstance(hWnd,szTitle,width,height)|| !applicationDidFinishLaunching())
		return 0;

	return 1;
}

bool QcApplication::initInstance(HWND hWnd, LPCTSTR szTitle, UINT width, UINT height)
{
	CCEGLView* pMainWnd = CCEGLView::sharedOpenGLView();

	if (pMainWnd)
	{
//  		pMainWnd->setHWnd(hWnd);
//  		pMainWnd->resize(width,height);
		pMainWnd->setEditorFrameSize(width,height,hWnd);
		if(pMainWnd->initGL())
		{
			pMainWnd->setFrameSize(width,height);
			pMainWnd->setViewName((const char*)szTitle);
			return true;
		}
	}

	return false;
}

void QcApplication::renderWorld()
{
	LARGE_INTEGER nNow;

	QueryPerformanceCounter(&nNow);
	if(nNow.QuadPart - m_nLast.QuadPart > m_nAnimationInterval.QuadPart)
	{
		m_nLast.QuadPart = nNow.QuadPart;
		CCDirector::sharedDirector()->mainLoop();
	}
}