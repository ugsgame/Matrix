#include "BulletEditor.h"

#include "CCRenderWidget.h"
#include "EmitterSettingWidget.h"
#include "DisplaySettingWidget.h"
#include "ResourcesBrowserWidget.h"
#include "DecorateSettingWidget.h"
#include "ObjectsWidget.h"

#include "EditorConfig.h"
#include "EmitterConfig.h"

#include <QtGlobal>
#include <QtWidgets>
#include <QApplication>
#include <QMenuBar>
#include <QGroupBox>
#include <QGridLayout>
#include <QSlider>
#include <QLabel>
#include <QTimer>

BulletEditor* BulletEditor::Instanse = NULL;

BulletEditor::BulletEditor(QWidget *parent)
	: QMainWindow(parent)
{
	ui.setupUi(this);

	QTextCodec::setCodecForLocale(QTextCodec::codecForName("UTF-8"));

	CCRenderWidget *glwidget = new CCRenderWidget();
	glwidget->setMinimumWidth(200);
	//glwidget->setMaximumWidth(480);

	glwidget->setMinimumHeight(200);
	//glwidget->setMaximumHeight(480);

	QTimer *timer = new QTimer(this);

	timer->setInterval(10);

	QGroupBox * groupBox = new QGroupBox(this);
	setCentralWidget(groupBox);

	QGridLayout *layout = new QGridLayout(groupBox);
	//
	layout->addWidget(glwidget,1,0,8,1);
	//
	groupBox->setTitle("BulletView");
	groupBox->setLayout(layout);

	//
	EditorConfig::shareEditorConfig()->setApplicationPath(QApplication::applicationDirPath().toStdString());	
	EditorConfig::shareEditorConfig()->setWorkPath(QDir::currentPath().toStdString());
	EditorConfig::shareEditorConfig()->unserialization();
	//
	contectDockWindows();
	//connet actions
	connect(ui.action_openFile,SIGNAL(triggered()),this,SLOT(action_openFile()));
	connect(ui.action_saveFile,SIGNAL(triggered()),this,SLOT(action_saveFile()));
	connect(ui.action_saveFileAs,SIGNAL(triggered()),this,SLOT(action_saveFileAs()));

	timer->start();

	EmitterConfig::shareEmitterConfig()->unseriCurrent();
	this->reset();

	Instanse = this;
}

BulletEditor::~BulletEditor()
{

}

void BulletEditor::reset()
{

	this->setWindowTitle(QString("BulletEditor   -") + EditorConfig::shareEditorConfig()->getCurrentFile().c_str());

	emitterSettingWidget->resetValue();
	resourcesBrowserWidget->resetValue();
	dispalySettingWidget->setupDisplay(EmitterConfig::shareEmitterConfig()->getDisplayConfig());
}

void BulletEditor::contectDockWindows()
{

	QScrollArea* scrollArea = new QScrollArea(this);  
	//scrollArea->setGeometry(20,100,460,180); 
	emitterSettingWidget = new EmitterSettingWidget();
	scrollArea->setWidget(emitterSettingWidget);
	ui.dockWidget_Editing->setFeatures(QDockWidget::DockWidgetFloatable|QDockWidget::DockWidgetMovable);
	ui.dockWidget_Editing->setWidget(scrollArea);

	resourcesBrowserWidget = new ResourcesBrowserWidget();
	ui.dockWidget_Browser->setFeatures(QDockWidget::DockWidgetFloatable|QDockWidget::DockWidgetMovable);
	ui.dockWidget_Browser->setWidget(resourcesBrowserWidget);

	scrollArea = new QScrollArea(this);  
	dispalySettingWidget = new DisplaySettingWidget();
	scrollArea->setWidget(dispalySettingWidget);
	ui.dockWidget_Display->setFeatures(QDockWidget::DockWidgetFloatable|QDockWidget::DockWidgetMovable);
	ui.dockWidget_Display->setWidget(scrollArea);

	scrollArea = new QScrollArea(this);
	decorateSettingWidget = new DecorateSettingWidget();
	scrollArea->setWidget(decorateSettingWidget);
	ui.dockWidget_Decorate->setFeatures(QDockWidget::DockWidgetFloatable|QDockWidget::DockWidgetMovable);
	ui.dockWidget_Decorate->setWidget(scrollArea);

	scrollArea = new QScrollArea(this);
	objectsWidget = new ObjectsWidget();
	scrollArea->setWidget(objectsWidget);
	ui.dockWidget_Objects->setFeatures(QDockWidget::DockWidgetFloatable|QDockWidget::DockWidgetMovable);
	ui.dockWidget_Objects->setWidget(scrollArea);
}

void BulletEditor::action_openFile()
{
	QString fileName =QFileDialog::getOpenFileName(this,"open file",EditorConfig::shareEditorConfig()->getWorkPath().c_str(),"bullet file(*.bt)");
	qDebug()<<fileName;
	if (!fileName.isEmpty())
	{
		//EditorConfig::shareEditorConfig()->setCurrentFile(fileName.toStdString());
		if(EmitterConfig::shareEmitterConfig()->unserialization(fileName.toStdString().c_str()))
		{
			reset();
		}
	}
}

void BulletEditor::action_saveFile()
{
	if(EditorConfig::shareEditorConfig()->getCurrentFile()=="")
	{
		action_saveFileAs();
	}
	else
	{
		EmitterConfig::shareEmitterConfig()->seriCurrent();
	}
}

void BulletEditor::action_saveFileAs()
{
	QString path = QDir::currentPath() + "/untitled.bt";
	QString fileName = QFileDialog::getSaveFileName(this, tr("Save Bullet"),path);
	EditorConfig::shareEditorConfig()->setCurrentFile(fileName.toStdString());

	if (EmitterConfig::shareEmitterConfig()->seriCurrent())
	{
		this->reset();
	}
}

void BulletEditor::closeEvent(QCloseEvent * event)
{
	EditorConfig::shareEditorConfig()->serialization();
	EmitterConfig::shareEmitterConfig()->seriCurrent();
}