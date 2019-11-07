#ifndef BULLETEDITOR_H
#define BULLETEDITOR_H

#include <QtWidgets/QMainWindow>
#include "ui_BulletEditor.h"

class EmitterSettingWidget;
class DisplaySettingWidget;
class ResourcesBrowserWidget;
class DecorateSettingWidget;
class ObjectsWidget;

class BulletEditor : public QMainWindow
{
	Q_OBJECT
public:
	static BulletEditor* Instanse;
public:
	BulletEditor(QWidget *parent = 0);
	~BulletEditor();

	void reset();

protected:
	void contectDockWindows();
protected:
	virtual void closeEvent(QCloseEvent * event);
private slots:
	void action_openFile();
	void action_saveFile();
	void action_saveFileAs();

private:
	Ui::BulletEditorClass ui;

	EmitterSettingWidget* emitterSettingWidget;
	DisplaySettingWidget* dispalySettingWidget;
	ResourcesBrowserWidget* resourcesBrowserWidget;
	DecorateSettingWidget* decorateSettingWidget;
	ObjectsWidget* objectsWidget;
};

#endif // BULLETEDITOR_H
