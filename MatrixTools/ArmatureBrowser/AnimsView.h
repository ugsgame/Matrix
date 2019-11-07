#ifndef __QC_ANIMSVIEW__
#define __QC_ANIMSVIEW__

#include <QWidget>
#include "ui_AnimsView.h"

QT_BEGIN_NAMESPACE
class QListWidget;
QT_END_NAMESPACE

class AnimsView:public QWidget
{
	Q_OBJECT

public:
	AnimsView(QWidget *parent = 0 );
	~AnimsView();

	void resetList(std::vector<std::string> movements);
protected:
	virtual void resizeEvent(QResizeEvent * event);

private  slots:
	void playAnimation(QListWidgetItem * item);
	void itemChanged();
 
private:

	Ui::AnimsViewClass ui;

	QListWidget *animList;
	std::string curAnim;
};

#endif