#include "imagepack.h"

#include <QtGlobal>
#include <QtDebug>
#include <QApplication>

ImagePack::ImagePack(QWidget *parent)
	: QMainWindow(parent)
{
	ui.setupUi(this);
	
	connect(ui.pushButton_start,SIGNAL(clicked()),this, SLOT(start()));

}

ImagePack::~ImagePack()
{

}

void ImagePack::start()
{
	qDebug()<<"start!!";


}