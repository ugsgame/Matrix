/****************************************************************************
Copyright (c) 2010-2013 cocos2d-x.org
Copyright (c) Microsoft Open Technologies, Inc.

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
#include "cocos2d.h"
#include "cocoa/CCDictionary.h"
#include "AppDelegate.h"
#include "MainScene.h"


USING_NS_CC;

using namespace std;

AppDelegate::AppDelegate() {

}

AppDelegate::~AppDelegate() 
{
}

bool AppDelegate::applicationDidFinishLaunching() {

	// initialize director
	CCDirector* pDirector = CCDirector::sharedDirector();
	CCEGLView* pEGLView = CCEGLView::sharedOpenGLView();


	pDirector->setOpenGLView(pEGLView);
	//CCSize frameSize = pEGLView->getFrameSize();

	// set searching path
	//CCFileUtils::sharedFileUtils()->setSearchPaths(searchPath);

	// turn on display FPS
	pDirector->setDisplayStats(true);

	// set FPS. the default value is 1.0/60 if you don't call this
	pDirector->setAnimationInterval(1.0 / 60);

	//setup scene
	pDirector->runWithScene(MainScene::scene());
	return true;
}

// This function will be called when the app is inactive. When comes a phone call,it's be invoked too
void AppDelegate::applicationDidEnterBackground() {
	CCDirector::sharedDirector()->stopAnimation();

	// if you use SimpleAudioEngine, it must be pause
	// SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();
}

// this function will be called when the app is active again
void AppDelegate::applicationWillEnterForeground() {
	CCDirector::sharedDirector()->startAnimation();

	// if you use SimpleAudioEngine, it must resume here
	// SimpleAudioEngine::sharedEngine()->resumeBackgroundMusic();
}

void AppDelegate::resize(float width,float height)
{
	CCEGLView* eglView = CCEGLView::sharedOpenGLView();
 	eglView->resizeFrame(width,height);
	eglView->setDesignResolutionSize(width,height,kResolutionShowAll);

	MainScene::sharedMainScene()->resize(width,height);
}

void AppDelegate::toucheBegin(float x,float y)
{
	int id = 0;
	CCEGLView* eglView = CCEGLView::sharedOpenGLView();
	eglView->handleTouchesBegin(1, &id, &x, &y);
}

void AppDelegate::toucheEnd(float x,float y)
{
	int id = 0;
	CCEGLView* eglView = CCEGLView::sharedOpenGLView();
	eglView->handleTouchesEnd(1, &id, &x, &y);
}

void AppDelegate::toucheMove(float x,float y)
{
	int id = 0;
	CCEGLView* eglView = CCEGLView::sharedOpenGLView();
	eglView->handleTouchesMove(1, &id, &x, &y);
}

void AppDelegate::end()
{
	CCDirector::sharedDirector()->end();
	CCDirector::sharedDirector()->mainLoop(); 

}