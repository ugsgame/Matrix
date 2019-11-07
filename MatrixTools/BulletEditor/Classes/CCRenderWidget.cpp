/****************************************************************************
**
** Copyright (C) 2013 Digia Plc and/or its subsidiary(-ies).
** Contact: http://www.qt-project.org/legal
**
** This file is part of the examples of the Qt Toolkit.
**
** $QT_BEGIN_LICENSE:BSD$
** You may use this file under the terms of the BSD license as follows:
**
** "Redistribution and use in source and binary forms, with or without
** modification, are permitted provided that the following conditions are
** met:
**   * Redistributions of source code must retain the above copyright
**     notice, this list of conditions and the following disclaimer.
**   * Redistributions in binary form must reproduce the above copyright
**     notice, this list of conditions and the following disclaimer in
**     the documentation and/or other materials provided with the
**     distribution.
**   * Neither the name of Digia Plc and its Subsidiary(-ies) nor the names
**     of its contributors may be used to endorse or promote products derived
**     from this software without specific prior written permission.
**
**
** THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
** "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
** LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
** A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
** OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
** SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
** LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
** DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
** THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
** (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
** OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE."
**
** $QT_END_LICENSE$
**
****************************************************************************/
#include<winsock2.h>
//#include "cocos2d.h"
#include "CCRenderWidget.h"


#include "QcApplication.h"
#include "AppDelegate.h"

#include <QTimer>
#include <qevent.h>

CCRenderWidget::CCRenderWidget(QWidget *parent)
    : QGLWidget(QGLFormat(QGL::SampleBuffers), parent),
	appDelegate(NULL)
{

	initializeCocos2dx();
	 
	QTimer *timer = new QTimer(this);
	connect(timer, SIGNAL(timeout()), this, SLOT(paintGL(void)));
	timer->start(1);

	this->setUpdatesEnabled(true);
}


CCRenderWidget::~CCRenderWidget()
{

}

void CCRenderWidget::initializeCocos2dx()
{
	appDelegate =  new AppDelegate();

	HWND hWnd = (HWND)this->winId();
	QSize frameSize = this->frameSize();
	oldSize = frameSize;

	appDelegate->run(hWnd,(LPCTSTR)"Test",frameSize.width(),frameSize.height());
}

void CCRenderWidget::closeEvent(QCloseEvent *event)
{
	appDelegate->end();
 	QWidget::closeEvent(event);
}

// 设置setFrameSize会回调窗口大小改小事件，形成死循环
void CCRenderWidget::resizeEvent(QResizeEvent *event)
{
 	QSize size = event->size();
	appDelegate->resize(size.width(),size.height());
}

void CCRenderWidget::mousePressEvent(QMouseEvent * event)
{
	QPoint pos = event->pos();
	int id = 0;
	float x = (float)pos.x();
	float y = (float)pos.y();

	appDelegate->toucheBegin(x,y);
}

void CCRenderWidget::mouseReleaseEvent(QMouseEvent * event)
{
	QPoint pos = event->pos();
	int id = 0;
	float x = (float)pos.x();
	float y = (float)pos.y();

	appDelegate->toucheEnd(x,y);
}

void CCRenderWidget::mouseDoubleClickEvent(QMouseEvent * event)
{
	QPoint pos = event->pos();
	//TODO:
}
void CCRenderWidget::mouseMoveEvent(QMouseEvent * event)
{
	QPoint pos = event->pos();
	int id = 0;
	float x = (float)pos.x();
	float y = (float)pos.y();
	
	appDelegate->toucheMove(x,y);
}

void CCRenderWidget::paintEvent(QPaintEvent * event)
{
	appDelegate->renderWorld();
}

void CCRenderWidget::paintGL()
{
	appDelegate->renderWorld();
}

void CCRenderWidget::resizeGL(int width, int height)
{
	appDelegate->resize(width,width);
}
