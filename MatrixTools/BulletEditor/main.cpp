#include "BulletEditor.h"
#include <QtWidgets/QApplication>

int main(int argc, char *argv[])
{
	QApplication a(argc, argv);
	BulletEditor w;
	w.show();
	return a.exec();
}
