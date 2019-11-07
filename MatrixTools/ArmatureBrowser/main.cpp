#include "armaturebrowser.h"
#include <QDesktopWidget>
#include <QtWidgets/QApplication>

int main(int argc, char *argv[])
{
	QApplication a(argc, argv);
	ArmatureBrowser w;
	w.show();
	w.move ((QApplication::desktop()->width() - w.width())/2,(QApplication::desktop()->height() - w.height())/2);
	return a.exec();
}
