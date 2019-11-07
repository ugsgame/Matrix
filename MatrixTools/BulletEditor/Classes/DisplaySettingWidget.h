
#ifndef __DISPLAYSETTINGWIDGET_H__
#define __DISPLAYSETTINGWIDGET_H__

#include <QPainter>
#include <QWidget>
#include <QListWidget>
#include "ui_DisplaySetting.h"

struct DisplayConfig;
struct FrameData;

class AtlasListWidget;
class FramesListWidget;
class EmitterConfig;

class DisplaySettingWidget:public QWidget
{
	Q_OBJECT
public:
	DisplaySettingWidget(QWidget *parent = 0);
	~DisplaySettingWidget();
public:
	void setupDisplay(DisplayConfig* displayConfig);
protected:
	void chooseFile(const QString &title, QImage *image, QToolButton *button);
	void loadImage(const QString &fileName, QImage *image, QToolButton *button);
	QPoint imagePos(const QImage &image) const;

	void restore();
private slots:
	void chooseSource();
	void changeFrame(QListWidgetItem * item);
	void changeAnimition(QListWidgetItem * item);

	void comboBox_src(QString combo);
	void comboBox_dst(QString combo);
	void doubleSpinBox_Rotation();
	void doubleSpinBox_Scale();
	void pushButton_blendNormal();
	void pushButton_blendAdditive();

	void pushButton_flashFrame();
	void pushButton_deleteFrame();
	void pushButton_moveUp();
	void pushButton_moveDown();

	void horizontalSlider_animateSpeed();
	void spinBox_animateSpeed();

	void checkBox_setFrameAnim(int clicked);
private:
	QImage sourceImage;

	Ui::DisplaySettingClass ui;
	AtlasListWidget *listWidget_atlas;
	FramesListWidget* listWidget_frames;

//	DisplayConfig* displayConfig;
	EmitterConfig* emitterConfig;

	std::vector<FrameData> frameDatas;

	QSize originSize;
};

#endif