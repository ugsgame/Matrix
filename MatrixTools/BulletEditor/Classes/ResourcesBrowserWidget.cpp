#include "ResourcesBrowserWidget.h"
#include "BulletEditor.h"
#include "EditorConfig.h"
#include "EmitterConfig.h"

#include <qevent.h>
#include <qmessagebox.h>
#include <qinputdialog.h>

bool checkFlag = false;

ResourcesBrowserWidget::ResourcesBrowserWidget(QWidget *parent /* = 0 */)
	:QWidget(parent)
{
	ui.setupUi(this);

	connect(ui.treeView,SIGNAL(clicked(const QModelIndex)),this,SLOT(treeView_clicked(const QModelIndex)));
	connect(ui.treeView,SIGNAL(currentChanged(const QModelIndex&,const QModelIndex&)),this,SLOT(treeView_currentChanged(const QModelIndex&,const QModelIndex&)));

	connect(ui.pushButton_add,SIGNAL(clicked()),this,SLOT(pushButton_add()));
	connect(ui.pushButton_remove,SIGNAL(clicked()),this,SLOT(pushButton_remove()));
	//resetValue();
}

ResourcesBrowserWidget::~ResourcesBrowserWidget()
{

}

void ResourcesBrowserWidget::resetValue()
{
	if(checkFlag)
	{
		checkFlag = false;
		return;
	}

	std::string workPath = EditorConfig::shareEditorConfig()->getCurrentDir();

	model = new QFileSystemModel();
	//model->setReadOnly(false); 
	model->setRootPath(workPath.c_str());

	// 设置过滤器  
	QStringList filter;  
	//filter << "*.png" << "*.jpg" << "*.plist"<<"*.bt";  
	filter<<"*.bt";
	model->setNameFilters(filter);  
	model->setNameFilterDisables(false);  

	ui.treeView->setModel(model);

	QModelIndex index = model->index(workPath.c_str());
	ui.treeView->setRootIndex(index);
	ui.treeView->expand(index);      //当前项展开
	ui.treeView->scrollTo(index);    //定位到当前项

	ui.treeView->setAnimated(false);
	ui.treeView->setIndentation(20);
	ui.treeView->setSortingEnabled(true);
}

void ResourcesBrowserWidget::resizeEvent(QResizeEvent * event)
{
	QSize size = event->size();
	ui.verticalLayoutWidget->resize(size);
}

void ResourcesBrowserWidget::treeView_clicked(const QModelIndex modelIndex)
{
	EmitterConfig::shareEmitterConfig()->seriCurrent();
	QModelIndex index = modelIndex;  
	if ( !index.isValid() ) 
	{  
		return;  
	}  

	QFileInfo info = model->fileInfo(index); 
	std::string path = info.absoluteFilePath().toStdString();
	if(EditorConfig::shareEditorConfig()->getCurrentFile() != path)
	{
		if(EmitterConfig::shareEmitterConfig()->unserialization(path.c_str()))
		{
			checkFlag = true;
			if(BulletEditor::Instanse)BulletEditor::Instanse->reset();
		}
	}  

}

void ResourcesBrowserWidget::treeView_currentChanged(const QModelIndex  &current, const QModelIndex  &previous)
{
	QModelIndex index = current;  
}

void ResourcesBrowserWidget::pushButton_add()
{
	// 	QModelIndex index = ui.treeView->currentIndex();  
	// 	if ( !index.isValid() ) {  
	// 		return;  
	// 	}  
	std::string curDir = EditorConfig::shareEditorConfig()->getCurrentDir();
	QString name = QInputDialog::getText(this, tr("Create bullet"), tr("Bullet name"));  
	if ( !name.isEmpty() ) {  
		if(curDir == "")
		{
			curDir = QDir::currentPath().toStdString() + "/Data/Bullets";  
		}
		curDir += "/";
		curDir += name.toStdString()+".bt";

		if(EmitterConfig::shareEmitterConfig()->isFileExist(curDir.c_str()))
		{
			QMessageBox::StandardButton reply;
			reply = QMessageBox::question(this, tr("Replace file"),QString("File:")+ name + tr(" is exist!Do you want to replace it?"),QMessageBox::Yes | QMessageBox::No);
			if (reply == QMessageBox::Yes)
			{
				EditorConfig::shareEditorConfig()->setCurrentFile(curDir);
				EmitterConfig::shareEmitterConfig()->seriCurrent();
				checkFlag = true;
				BulletEditor::Instanse->reset();
			}
		}
		else
		{
			EditorConfig::shareEditorConfig()->setCurrentFile(curDir);
			EmitterConfig::shareEmitterConfig()->seriCurrent();
			checkFlag = true;
			BulletEditor::Instanse->reset();
		}

		//
	}  

}
void ResourcesBrowserWidget::pushButton_remove()
{
	EmitterConfig::shareEmitterConfig()->seriCurrent();
	QModelIndex index = ui.treeView->currentIndex();  
	if ( !index.isValid() ) {  
		return;  
	}  

	QFileInfo info = model->fileInfo(index);  
	QString path = info.absoluteFilePath();

	QMessageBox::StandardButton reply;
	reply = QMessageBox::question(this, tr("Delete bullet file"),QString("Do you want to delete:")+ info.baseName(),QMessageBox::Yes | QMessageBox::No);
	if (reply == QMessageBox::Yes)
	{
		model->remove(index); 
	}

// 	if(ui.treeView->children().length()>0)
// 	{		
// 		QModelIndex _index = ui.treeView->indexAt(QPoint(0,0));
// 		QFileInfo _info = model->fileInfo(_index);  
// 		QString _path = _info.absoluteFilePath();
// 		EditorConfig::shareEditorConfig()->setCurrentFile(_path.toStdString());
// 
// 		ui.treeView->setCurrentIndex(_index);
// 	}
// 	else
	{
		EditorConfig::shareEditorConfig()->setCurrentFile("");
	}
	checkFlag = true;
	BulletEditor::Instanse->reset();
}