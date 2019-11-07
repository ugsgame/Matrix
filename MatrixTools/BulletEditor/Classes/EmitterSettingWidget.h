
#ifndef __EMITTERSETTINGWIDGHT_H__
#define __EMITTERSETTINGWIDGHT_H__

#include <QWidget>
#include "ui_EmitterSetting.h"

class EmitterSettingWidget:public QWidget
{
	Q_OBJECT
public:
	EmitterSettingWidget(QWidget *parent = 0 );
	~EmitterSettingWidget();

	void resetValue();
protected:
	//void resetValue();
private  slots:
	void spinBox_initCount();
	void horizontalSlider_initCount();

	void doubleSpinBox_emissionAngle();
	void horizontalSlider_emissionAngle();

	void horizontalSlider_beamCount();
	void spinBox_beamCountVar();
	void spinBox_beamCount();

	void horizontalSlider_fieldAngle();
	void doubleSpinBox_fieldAngleVar();
	void doubleSpinBox_fieldAngle();

	void horizontalSlider_spinSpeed();
	void doubleSpinBox_spinSpeed();

	void comboBox_positionType(QString combo);
	void checkBox_isPointToTarget(int stateChanged);

	void doubleSpinBox_duration();
	void doubleSpinBox_durationVar();
	void dial_duration();

	void doubleSpinBox_interimTime();
	void doubleSpinBox_interimTimeVar();
	void dial_interimTime();

	void doubleSpinBox_lifeTime();
	void doubleSpinBox_lifeTimeVar();
	void dial_lifeTime();

	void doubleSpinBox_speed();
	void doubleSpinBox_speedVar();
	void dial_speed();

	void doubleSpinBox_emissionRate();
	void doubleSpinBox_emissionRateVar();
	void dial_emissionRate();

	void doubleSpinBox_damage();
	void dial_damage();

	void doubleSpinBox_bulletSpin();
	void doubleSpinBox_bulletSpinVar();
	void horizontalSlider_bulletSpin();

	void doubleSpinBox_speedDecay();
	void doubleSpinBox_speedLimit();

	void horizontalSlider_sinAmplitude();
	void doubleSpinBox_sinAmplitude();
	void horizontalSlider_sinRate();
	void doubleSpinBox_sinRate();

	void checkBox_isFollow(int stateChanged);
	void horizontalSlider_curvity();
	void doubleSpinBox_curvity();

private:
	Ui::EitterSettingClass ui;
};

#endif