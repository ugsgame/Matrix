
#ifndef __RESOURCESBROWSERWIDGET_H_
#define __RESOURCESBROWSERWIDGET_H_

#include <QWidget>
#include <QtGui>
#include <qfilesystemmodel.h>
#include "ui_ResourcesBrowser.h"

QT_BEGIN_NAMESPACE
class QListWidget;
QT_END_NAMESPACE

class ResourcesBrowserWidget:public QWidget
{
	Q_OBJECT
public:
	ResourcesBrowserWidget(QWidget *parent = 0);
	~ResourcesBrowserWidget();

	void resetValue();
protected:
	virtual void resizeEvent(QResizeEvent * event);
private slots:
	void treeView_clicked(const QModelIndex modelIndex);
	void treeView_currentChanged( const QModelIndex&  current, const QModelIndex&  previous);

	void pushButton_add();
	void pushButton_remove();
private:
	Ui::ResourcesBrowserClass ui;
	QFileSystemModel* model;
};

#endif
