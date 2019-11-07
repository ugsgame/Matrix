#include "main.h"
#include "../Classes/AppMacros.h"
#include "../Classes/AppDelegate.h"
#include "CCEGLView.h"

USING_NS_CC;

//TODO
// int APIENTRY _tWinMain(HINSTANCE hInstance,
// 					   HINSTANCE hPrevInstance,
// 					   LPTSTR    lpCmdLine,
// 					   int       nCmdShow)
// {
// 	UNREFERENCED_PARAMETER(hPrevInstance);
// 	UNREFERENCED_PARAMETER(lpCmdLine);
int  main(int argc,char* argv[])
{

#ifdef USE_WIN32_CONSOLE
	AllocConsole();
	freopen("CONIN$", "r", stdin);
	freopen("CONOUT$", "w", stdout);
	freopen("CONOUT$", "w", stderr);
#endif
	//
	cmd_Argc = argc;
	cmd_Argv = argv;
	// create the application instance
	AppDelegate app;
	CCEGLView* eglView = CCEGLView::sharedOpenGLView();
	eglView->setViewName("MatrixGame");
	eglView->setFrameSize(960, 640);
	// The resolution of ipad3 is very large. In general, PC's resolution is smaller than it.
	// So we need to invoke 'setFrameZoomFactor'(only valid on desktop(win32, mac, linux)) to make the window smaller.
	//eglView->setFrameZoomFactor(1.0f);
	return CCApplication::sharedApplication()->run();

#ifdef USE_WIN32_CONSOLE
	FreeConsole();
#endif
}