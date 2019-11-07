#ifndef  _APP_DELEGATE_H_
#define  _APP_DELEGATE_H_


#include "QcApplication.h"

/**
@brief    The cocos2d Application.

The reason for implement as private inheritance is to hide some interface call by CCDirector.
*/
class  AppDelegate : public QcApplication
{
public:
    AppDelegate();
    virtual ~AppDelegate();

    /**
    @brief    Implement CCDirector and CCScene init code here.
    @return true    Initialize success, app continue.
    @return false   Initialize failed, app terminate.
    */
    virtual bool applicationDidFinishLaunching();

    /**
    @brief  The function be called when the application enter background
    @param  the pointer of the application
    */
    virtual void applicationDidEnterBackground();

    /**
    @brief  The function be called when the application enter foreground
    @param  the pointer of the application
    */
    virtual void applicationWillEnterForeground();

	virtual void resize(float width,float height);

	virtual void toucheBegin(float x,float y);
	virtual void toucheEnd(float x,float y);
	virtual void toucheMove(float x,float y);
	/**
	*/
	virtual void end();
};

#endif // _APP_DELEGATE_H_

