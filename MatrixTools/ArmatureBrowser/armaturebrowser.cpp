#include "armaturebrowser.h"
#include "ArmatureView.h"
#include "AnimsView.h"

#include "qtimer.h"

//#include "cocos2d.h"
//#include "QcApplication.h"
//#include "AppDelegate.h"
//#include "MainScene.h"
#include "ActionConnect.h"
#include "Archive.h"

#include "CCRenderWidget.h"

#include <QtGlobal>
#include <QtWidgets>
#include <QApplication>
#include <QMenuBar>
#include <QGroupBox>
#include <QGridLayout>
#include <QSlider>
#include <QLabel>
#include <QTimer>


//USING_NS_CC;

ArmatureBrowser::ArmatureBrowser(QWidget *parent)
	: QMainWindow(parent)
{
 	ui.setupUi(this); 

	archive = Archive::shareArchive();
	archive->load();

	QTextCodec::setCodecForLocale(QTextCodec::codecForName("UTF-8"));

	CCRenderWidget *glwidget = new CCRenderWidget();
	QLabel *label = new QLabel(this);
	QTimer *timer = new QTimer(this);
	QSlider *slider = new QSlider(this);
	slider->setOrientation(Qt::Horizontal);

	slider->setRange(0, 100);
	slider->setSliderPosition(50);
	timer->setInterval(10);
	label->setText("Animation Controller");
	label->setAlignment(Qt::AlignHCenter);

	QGroupBox * groupBox = new QGroupBox(this);
	setCentralWidget(groupBox);
	groupBox->setTitle("Animation View");

	QGridLayout *layout = new QGridLayout(groupBox);

	layout->addWidget(glwidget,1,0,8,1);
	layout->addWidget(label,9,0,1,1);
	layout->addWidget(slider, 11,0,1,1);

	groupBox->setLayout(layout);

	createDockWindows();

	//connect actions
	connect(ui.actionOpen,SIGNAL(triggered()), this, SLOT(open()));
	connect(ui.actionExport,SIGNAL(triggered()), this,SLOT(exportArmature()));

	this->resize(archive->getMainSizeW(),archive->getMainSizeH());

	timer->start();
	//TODO:放到进度条加载
	ActionConnect* actionConnect = ActionConnect::shareActionConnect();
	std::vector<std::string> armaturePaths = archive->getArmaturePaths();
	for (int i = 0; i < armaturePaths.size(); i++)
	{
		if(actionConnect->openArmature(armaturePaths[i]))
		{
			//reset armatureList
			std::string armature = actionConnect->getCurArmature();
			armatureView->resetList(actionConnect->getArmatureList());
			animsView->resetList(actionConnect->getMovementList(armature));
		}

	}
}

void ArmatureBrowser::resizeEvent(QResizeEvent * event)
{
	QSize size = event->size();
	Archive::shareArchive()->setMainSize(size.width(),size.height());
}

void ArmatureBrowser::closeEvent(QCloseEvent * event)
{
	//
	archive->save();
}

void ArmatureBrowser::open()
{
	//Test:修改默认打开路经
	QString fileName =QFileDialog::getOpenFileName(this,"open file",archive->getFileOpenPath().c_str(),"armature file(*.ExportJson)");

 	if (!fileName.isEmpty())
	{
		archive->setFileOpenPath(fileName.toStdString());
 		qDebug()<<"Open file:"<<fileName;
		ActionConnect* actionConnect = ActionConnect::shareActionConnect();
		if(actionConnect->openArmature(fileName.toStdString()))
		{
			//reset armatureList
			std::string armature = actionConnect->getCurArmature();
			armatureView->resetList(actionConnect->getArmatureList());
			animsView->resetList(actionConnect->getMovementList(armature));
			//
			archive->setArmaturePaths(actionConnect->getArmaturePaths());
		}
	}
}

void ArmatureBrowser::exportArmature()
{
	ActionConnect* actionConnect = ActionConnect::shareActionConnect();

	//TODO:添加导出文件路径，判断是否存在，并提示是否替换
	QString fileName =QFileDialog::getExistingDirectory (this,"export",actionConnect->getArmaturePath(actionConnect->getCurArmature()).c_str());
	qDebug()<<"Open file:"<<fileName;
	if (fileName!="")
	{
		actionConnect->exportArmature(fileName.toStdString());
	}
}

void ArmatureBrowser::createDockWindows()
{
	//
	QDockWidget* dock = new QDockWidget(tr("Armatures"), this);
	dock->setAllowedAreas(Qt::LeftDockWidgetArea | Qt::RightDockWidgetArea);
	dock->setMinimumSize(150,200);
	armatureView = new ArmatureView(dock);
	dock->setWidget(armatureView);
	addDockWidget(Qt::RightDockWidgetArea, dock);

	dock = new QDockWidget(tr("Movements"), this);
	animsView = new AnimsView(dock);
	dock->setWidget(animsView);
	dock->setMinimumSize(150,200);
	addDockWidget(Qt::RightDockWidgetArea, dock);
}

ArmatureBrowser::~ArmatureBrowser()
{

}

