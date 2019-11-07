#ifndef ARMATUREBROWSER_H
#define ARMATUREBROWSER_H

#include <QtWidgets/QMainWindow>
#include "ui_armaturebrowser.h"

QT_BEGIN_NAMESPACE
class QAction;
class QListWidget;
class QMenu;
class QTextEdit;
QT_END_NAMESPACE

class ArmatureView;
class AnimsView;
class Archive;

class ArmatureBrowser : public QMainWindow
{
	Q_OBJECT

public:
	ArmatureBrowser(QWidget *parent = 0);
	~ArmatureBrowser();

protected:
	virtual void resizeEvent(QResizeEvent * event);
	virtual void closeEvent(QCloseEvent * event);
private slots:
	void open();
	void exportArmature();		
// 	bool save();
// 	bool saveAs();
// 	void about();

private:
	void createDockWindows();

	Ui::ArmatureBrowserClass ui;

	ArmatureView *armatureView;
	AnimsView *animsView;
	Archive *archive;
};

#endif // ARMATUREBROWSER_H
