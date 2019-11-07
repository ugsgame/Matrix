#ifndef IMAGEPACK_H
#define IMAGEPACK_H

#include <QtWidgets/QMainWindow>
#include "ui_imagepack.h"

class ImagePack : public QMainWindow
{
	Q_OBJECT

public:
	ImagePack(QWidget *parent = 0);
	~ImagePack();

private slots:
	void start();

private:
	Ui::ImagePackClass ui;
};

#endif // IMAGEPACK_H
