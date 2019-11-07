
#include "EmitterSettingWidget.h"
#include "EmitterConfig.h"

#include <qevent.h>
#include <QScrollArea>

EmitterSettingWidget::EmitterSettingWidget(QWidget *parent /* = 0 */ )
	:QWidget(parent)
{
	ui.setupUi(this);
	//////////////////////////////////////////////////////////////////////////
	connect(ui.doubleSpinBox_duration, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_duration()));
	connect(ui.doubleSpinBox_durationVar, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_durationVar()));
	connect(ui.dial_duration, SIGNAL(valueChanged(int)), this, SLOT(dial_duration())); 

	connect(ui.doubleSpinBox_interimTime, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_interimTime()));
	connect(ui.doubleSpinBox_interimTimeVar, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_interimTimeVar()));
	connect(ui.dial_interimTime, SIGNAL(valueChanged(int)), this, SLOT(dial_interimTime())); 

	connect(ui.spinBox_initCount, SIGNAL(valueChanged(int)), this, SLOT(spinBox_initCount())); 
	connect(ui.horizontalSlider_initCount, SIGNAL(valueChanged(int)), this, SLOT(horizontalSlider_initCount())); 

	connect(ui.doubleSpinBox_emissionRate, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_emissionRate()));
	connect(ui.doubleSpinBox_emissionRateVar, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_emissionRateVar()));
	connect(ui.dial_emissionRate, SIGNAL(valueChanged(int)), this, SLOT(dial_emissionRate())); 

	connect(ui.doubleSpinBox_emissionAngle, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_emissionAngle()));
	connect(ui.horizontalSlider_emissionAngle, SIGNAL(valueChanged(int)), this, SLOT(horizontalSlider_emissionAngle()));

	connect(ui.spinBox_beamCount, SIGNAL(valueChanged(int)), this, SLOT(spinBox_beamCount()));
	connect(ui.spinBox_beamCountVar, SIGNAL(valueChanged(int)), this, SLOT(spinBox_beamCountVar()));
	connect(ui.horizontalSlider_beamCount, SIGNAL(valueChanged(int)), this, SLOT(horizontalSlider_beamCount()));

	connect(ui.doubleSpinBox_fieldAngle, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_fieldAngle()));
	connect(ui.doubleSpinBox_fieldAngleVar, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_fieldAngleVar()));
	connect(ui.horizontalSlider_fieldAngle, SIGNAL(valueChanged(int)), this, SLOT(horizontalSlider_fieldAngle()));  

	connect(ui.doubleSpinBox_spinSpeed, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_spinSpeed()));
	connect(ui.horizontalSlider_spinSpeed, SIGNAL(valueChanged(int)), this, SLOT(horizontalSlider_spinSpeed()));

	connect(ui.comboBox_positionType,SIGNAL(activated(QString)),this, SLOT(comboBox_positionType(QString)));
	connect(ui.checkBox_isPointToTarget,SIGNAL(stateChanged(int)),this, SLOT(checkBox_isPointToTarget(int)));
	//////////////////////////////////////////////////////////////////////////

	connect(ui.doubleSpinBox_lifeTime, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_lifeTime()));
	connect(ui.doubleSpinBox_lifeTimeVar, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_lifeTimeVar()));
	connect(ui.dial_lifeTime, SIGNAL(valueChanged(int)), this, SLOT(dial_lifeTime())); 

	connect(ui.doubleSpinBox_speed, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_speed()));
	connect(ui.doubleSpinBox_speedVar, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_speedVar()));
	connect(ui.dial_speed, SIGNAL(valueChanged(int)), this, SLOT(dial_speed())); 

	connect(ui.doubleSpinBox_damage, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_damage()));
	connect(ui.dial_damage, SIGNAL(valueChanged(int)), this, SLOT(dial_damage())); 

	connect(ui.doubleSpinBox_bulletSpin, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_bulletSpin()));
	connect(ui.doubleSpinBox_bulletSpinVar, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_bulletSpinVar()));
	connect(ui.horizontalSlider_bulletSpin, SIGNAL(valueChanged(int)), this, SLOT(horizontalSlider_bulletSpin()));

	connect(ui.doubleSpinBox_speedDecay, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_speedDecay()));
	connect(ui.doubleSpinBox_speedLimit, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_speedLimit()));

	connect(ui.horizontalSlider_sinAmplitude, SIGNAL(valueChanged(int)), this, SLOT(horizontalSlider_sinAmplitude()));
	connect(ui.horizontalSlider_sinRate, SIGNAL(valueChanged(int)), this, SLOT(horizontalSlider_sinRate()));
	connect(ui.doubleSpinBox_sinAmplitude, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_sinAmplitude()));
	connect(ui.doubleSpinBox_sinRate, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_sinRate()));

	connect(ui.horizontalSlider_sinAmplitude, SIGNAL(valueChanged(int)), this, SLOT(horizontalSlider_sinAmplitude()));
	connect(ui.doubleSpinBox_sinAmplitude, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_sinAmplitude()));
	connect(ui.horizontalSlider_sinRate, SIGNAL(valueChanged(int)), this, SLOT(horizontalSlider_sinRate()));
	connect(ui.doubleSpinBox_sinRate, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_sinRate()));

	connect(ui.checkBox_isFollow,SIGNAL(stateChanged(int)),this, SLOT(checkBox_isFollow(int)));
	connect(ui.horizontalSlider_curvity, SIGNAL(valueChanged(int)), this, SLOT(horizontalSlider_curvity()));
	connect(ui.doubleSpinBox_curvity, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_curvity()));
	//////////////////////////////////////////////////////////////////////////
	ui.horizontalSlider_initCount->setRange(0,200);
	ui.spinBox_initCount->setRange(0,200);

	ui.horizontalSlider_emissionAngle->setRange(0,360);
	ui.doubleSpinBox_emissionAngle->setRange(0,360);

	ui.horizontalSlider_beamCount->setRange(1,30);
	ui.spinBox_beamCount->setRange(1,30);

	ui.horizontalSlider_fieldAngle->setRange(0,360);
	ui.doubleSpinBox_fieldAngle->setRange(0,360);

	ui.horizontalSlider_spinSpeed->setRange(-360,360);
	ui.doubleSpinBox_spinSpeed->setRange(-360,360);

	ui.doubleSpinBox_duration->setRange(-1,20);
	ui.dial_duration->setRange(-1,20);

	ui.doubleSpinBox_interimTime->setRange(-1,20);
	ui.dial_interimTime->setRange(-1,20);

	ui.doubleSpinBox_lifeTime->setRange(-1,30);
	ui.dial_lifeTime->setRange(-1,30);

	ui.doubleSpinBox_speed->setRange(0,2000);
	ui.dial_speed->setRange(0,2000);

	ui.doubleSpinBox_speedDecay->setRange(-100,100);
	ui.doubleSpinBox_speedLimit->setRange(-2000,2000);

	ui.doubleSpinBox_emissionRate->setRange(0,100);
	ui.dial_emissionRate->setRange(0,100);

	ui.doubleSpinBox_damage->setRange(0,2000);
	ui.dial_damage->setRange(0,2000);

	ui.horizontalSlider_bulletSpin->setRange(-720,720);
	ui.doubleSpinBox_bulletSpin->setRange(-720,720);

	ui.doubleSpinBox_fieldAngleVar->setRange(0,360);
	ui.doubleSpinBox_speedVar->setRange(0,2000);
	ui.doubleSpinBox_bulletSpinVar->setRange(0,720);

	ui.doubleSpinBox_sinAmplitude->setRange(0,1);
	ui.horizontalSlider_sinAmplitude->setRange(0,100);
	ui.doubleSpinBox_sinRate->setRange(0,1);
	ui.horizontalSlider_sinRate->setRange(0,100);
	ui.doubleSpinBox_curvity->setRange(0,1);
	ui.horizontalSlider_curvity->setRange(0,100);



	QStringList positionType;
	positionType.push_back("Free");
	positionType.push_back("Relative");
	positionType.push_back("Grouped");

	ui.comboBox_positionType->addItems(positionType);

	resetValue();
}

EmitterSettingWidget::~EmitterSettingWidget()
{

}

void EmitterSettingWidget::spinBox_initCount()
{
	ui.horizontalSlider_initCount->setValue(ui.spinBox_initCount->value());
	EmitterConfig::shareEmitterConfig()->setInitCount(ui.horizontalSlider_initCount->value());
}
void EmitterSettingWidget::horizontalSlider_initCount()
{
	ui.spinBox_initCount->setValue(ui.horizontalSlider_initCount->value());
	EmitterConfig::shareEmitterConfig()->setInitCount(ui.horizontalSlider_initCount->value());
}

void EmitterSettingWidget::doubleSpinBox_emissionAngle()
{
	ui.horizontalSlider_emissionAngle->setValue(ui.doubleSpinBox_emissionAngle->value());
	EmitterConfig::shareEmitterConfig()->setEmissionAngle(ui.horizontalSlider_emissionAngle->value());
}
void EmitterSettingWidget::horizontalSlider_emissionAngle()
{
	EmitterConfig* config = EmitterConfig::shareEmitterConfig();
	float decimal = config->getEmissionAngle() - ((int)config->getEmissionAngle());

	ui.doubleSpinBox_emissionAngle->setValue(ui.horizontalSlider_emissionAngle->value() + decimal);
	config->setEmissionAngle(ui.horizontalSlider_emissionAngle->value()+decimal);
}

void EmitterSettingWidget::horizontalSlider_beamCount()
{
	ui.spinBox_beamCount->setValue(ui.horizontalSlider_beamCount->value());
	EmitterConfig::shareEmitterConfig()->setBeamCount(ui.horizontalSlider_beamCount->value());
}
void EmitterSettingWidget::spinBox_beamCountVar()
{
	EmitterConfig::shareEmitterConfig()->setBeamCountVar(ui.spinBox_beamCountVar->value());
}
void EmitterSettingWidget::spinBox_beamCount()
{
	ui.horizontalSlider_beamCount->setValue(ui.spinBox_beamCount->value());
	EmitterConfig::shareEmitterConfig()->setBeamCount(ui.spinBox_beamCount->value());
}

void EmitterSettingWidget::horizontalSlider_fieldAngle()
{
	EmitterConfig* config = EmitterConfig::shareEmitterConfig();
	float decimal = config->getFieldAngle() - ((int)config->getFieldAngle());

	ui.doubleSpinBox_fieldAngle->setValue(ui.horizontalSlider_fieldAngle->value()+decimal);
	config->setFieldAngle(ui.horizontalSlider_fieldAngle->value()+decimal);
}
void EmitterSettingWidget::doubleSpinBox_fieldAngleVar()
{
	EmitterConfig::shareEmitterConfig()->setFieldAngleVar(ui.doubleSpinBox_fieldAngleVar->value());
}
void EmitterSettingWidget::doubleSpinBox_fieldAngle()
{
	ui.horizontalSlider_fieldAngle->setValue(ui.doubleSpinBox_fieldAngle->value());
	EmitterConfig::shareEmitterConfig()->setFieldAngle(ui.doubleSpinBox_fieldAngle->value());
}

void EmitterSettingWidget::horizontalSlider_spinSpeed()
{
	EmitterConfig* config = EmitterConfig::shareEmitterConfig();
	float decimal = config->getSpinSpeed() - ((int)config->getSpinSpeed());

	ui.doubleSpinBox_spinSpeed->setValue(ui.horizontalSlider_spinSpeed->value() + decimal);
	config->setSpinSpeed(ui.horizontalSlider_spinSpeed->value() + decimal);
}
void EmitterSettingWidget::doubleSpinBox_spinSpeed()
{
	ui.horizontalSlider_spinSpeed->setValue(ui.doubleSpinBox_spinSpeed->value());
	EmitterConfig::shareEmitterConfig()->setSpinSpeed(ui.doubleSpinBox_spinSpeed->value());
}

void EmitterSettingWidget::comboBox_positionType(QString combo)
{
	EmitterConfig::shareEmitterConfig()->setPositionType((PositionType)ui.comboBox_positionType->currentIndex());
}

void EmitterSettingWidget::checkBox_isPointToTarget(int clicked)
{
	EmitterConfig::shareEmitterConfig()->setIsPointToTarget(clicked);
}

void EmitterSettingWidget::doubleSpinBox_duration()
{
	ui.dial_duration->setValue(ui.doubleSpinBox_duration->value());
	EmitterConfig::shareEmitterConfig()->setDuration(ui.doubleSpinBox_duration->value());
}
void EmitterSettingWidget::doubleSpinBox_durationVar()
{
	EmitterConfig::shareEmitterConfig()->setDurationVar(ui.doubleSpinBox_durationVar->value());
}
void EmitterSettingWidget::dial_duration()
{
	EmitterConfig* config = EmitterConfig::shareEmitterConfig();
	float decimal = config->getDuration() - ((int)config->getDuration());

	ui.doubleSpinBox_duration->setValue(ui.dial_duration->value() + decimal);
	config->setDuration(ui.dial_duration->value() + decimal);
}

void EmitterSettingWidget::doubleSpinBox_interimTime()
{
	ui.dial_interimTime->setValue(ui.doubleSpinBox_interimTime->value());
	EmitterConfig::shareEmitterConfig()->setInterimTime(ui.doubleSpinBox_interimTime->value());
}
void EmitterSettingWidget::doubleSpinBox_interimTimeVar()
{
	EmitterConfig::shareEmitterConfig()->setInterimTimeVar(ui.doubleSpinBox_interimTimeVar->value());
}
void EmitterSettingWidget::dial_interimTime()
{
	EmitterConfig* config = EmitterConfig::shareEmitterConfig();
	float decimal = config->getInterimTime() - ((int)config->getInterimTime());

	ui.doubleSpinBox_interimTime->setValue(ui.dial_interimTime->value() + decimal);
	config->setInterimTime(ui.dial_interimTime->value() + decimal);
}

void EmitterSettingWidget::doubleSpinBox_lifeTime()
{
	ui.dial_lifeTime->setValue(ui.doubleSpinBox_lifeTime->value());
	EmitterConfig::shareEmitterConfig()->setLifeTime(ui.doubleSpinBox_lifeTime->value());
}
void EmitterSettingWidget::doubleSpinBox_lifeTimeVar()
{
	EmitterConfig::shareEmitterConfig()->setLifeTimeVar(ui.doubleSpinBox_lifeTimeVar->value());
}
void EmitterSettingWidget::dial_lifeTime()
{
	EmitterConfig* config = EmitterConfig::shareEmitterConfig();
	float decimal = config->getLifeTime() - ((int)config->getLifeTime());

	ui.doubleSpinBox_lifeTime->setValue(ui.dial_lifeTime->value()+decimal);
	config->setLifeTime(ui.dial_lifeTime->value()+decimal);
}

void EmitterSettingWidget::doubleSpinBox_speed()
{
	ui.dial_speed->setValue(ui.doubleSpinBox_speed->value());
	EmitterConfig::shareEmitterConfig()->setSpeed(ui.doubleSpinBox_speed->value());
}
void EmitterSettingWidget::doubleSpinBox_speedVar()
{
	EmitterConfig::shareEmitterConfig()->setSpeedVar(ui.doubleSpinBox_speedVar->value());
}
void EmitterSettingWidget::dial_speed()
{
	EmitterConfig* config = EmitterConfig::shareEmitterConfig();
	float decimal = config->getSpeed() - ((int)config->getSpeed());

	ui.doubleSpinBox_speed->setValue(ui.dial_speed->value() + decimal);
	config->setSpeed(ui.dial_speed->value() + decimal);
}

void EmitterSettingWidget::doubleSpinBox_emissionRate()
{
	ui.dial_emissionRate->setValue(ui.doubleSpinBox_emissionRate->value());
	EmitterConfig::shareEmitterConfig()->setEmissionRate(ui.doubleSpinBox_emissionRate->value());
}
void EmitterSettingWidget::doubleSpinBox_emissionRateVar()
{
	EmitterConfig::shareEmitterConfig()->setEmissionRateVar(ui.doubleSpinBox_emissionRateVar->value());
}
void EmitterSettingWidget::dial_emissionRate()
{
	EmitterConfig* config = EmitterConfig::shareEmitterConfig();
	float decimal = config->getEmissionRate() - ((int)config->getEmissionRate());

	ui.doubleSpinBox_emissionRate->setValue(ui.dial_emissionRate->value() + decimal);
	config->setEmissionRate(ui.dial_emissionRate->value() + decimal);
}

void EmitterSettingWidget::doubleSpinBox_damage()
{
	ui.dial_damage->setValue(ui.doubleSpinBox_damage->value());
	EmitterConfig::shareEmitterConfig()->setDamage(ui.doubleSpinBox_damage->value());
}
void EmitterSettingWidget::dial_damage()
{
	EmitterConfig* config = EmitterConfig::shareEmitterConfig();
	float decimal = config->getDamage() - (int)config->getDamage();

	ui.doubleSpinBox_damage->setValue(ui.dial_damage->value() + decimal);
	config->setDamage(ui.dial_damage->value() + decimal);
}

void EmitterSettingWidget::doubleSpinBox_bulletSpin()
{
	ui.horizontalSlider_bulletSpin->setValue(ui.doubleSpinBox_bulletSpin->value());
	EmitterConfig::shareEmitterConfig()->setBulletSpinSpeed(ui.doubleSpinBox_bulletSpin->value());
}
void EmitterSettingWidget::doubleSpinBox_bulletSpinVar()
{
	EmitterConfig::shareEmitterConfig()->setBulletSpinSpeedVar(ui.doubleSpinBox_bulletSpinVar->value());
}
void EmitterSettingWidget::horizontalSlider_bulletSpin()
{
	EmitterConfig* config = EmitterConfig::shareEmitterConfig();
	float decimal = config->getBulletSpinSpeed() - (int)config->getBulletSpinSpeed();

	ui.doubleSpinBox_bulletSpin->setValue(ui.horizontalSlider_bulletSpin->value() + decimal);
	config->setBulletSpinSpeed(ui.horizontalSlider_bulletSpin->value() + decimal);
}

void EmitterSettingWidget::doubleSpinBox_speedDecay()
{
	EmitterConfig::shareEmitterConfig()->setSpeedDecay(ui.doubleSpinBox_speedDecay->value());
}
void EmitterSettingWidget::doubleSpinBox_speedLimit()
{
	EmitterConfig::shareEmitterConfig()->setSpeedLimit(ui.doubleSpinBox_speedLimit->value());
}

void EmitterSettingWidget::horizontalSlider_sinAmplitude()
{
	ui.doubleSpinBox_sinAmplitude->setValue(ui.horizontalSlider_sinAmplitude->value()*0.01f);
	EmitterConfig::shareEmitterConfig()->setSinAmplitude(ui.horizontalSlider_sinAmplitude->value()*0.01f);
}
void EmitterSettingWidget::doubleSpinBox_sinAmplitude()
{
	ui.horizontalSlider_sinAmplitude->setValue(ui.doubleSpinBox_sinAmplitude->value()*100);
	EmitterConfig::shareEmitterConfig()->setSinAmplitude(ui.doubleSpinBox_sinAmplitude->value());
}
void EmitterSettingWidget::horizontalSlider_sinRate()
{
	ui.doubleSpinBox_sinRate->setValue(ui.horizontalSlider_sinRate->value()*0.01f);
	EmitterConfig::shareEmitterConfig()->setSinRate(ui.horizontalSlider_sinRate->value()*0.01f);
}
void EmitterSettingWidget::doubleSpinBox_sinRate()
{
	ui.horizontalSlider_sinRate->setValue(ui.doubleSpinBox_sinRate->value()*100);
	EmitterConfig::shareEmitterConfig()->setSinRate(ui.doubleSpinBox_sinRate->value());
}

void EmitterSettingWidget::checkBox_isFollow(int clicked)
{
	EmitterConfig::shareEmitterConfig()->setIsFollow(clicked);
}
void EmitterSettingWidget::horizontalSlider_curvity()
{
	ui.doubleSpinBox_curvity->setValue(ui.horizontalSlider_curvity->value()*0.01f);
	EmitterConfig::shareEmitterConfig()->setCurvity(ui.horizontalSlider_curvity->value()*0.01f);
}
void EmitterSettingWidget::doubleSpinBox_curvity()
{
	ui.horizontalSlider_curvity->setValue(ui.doubleSpinBox_curvity->value()*100);
	EmitterConfig::shareEmitterConfig()->setCurvity(ui.doubleSpinBox_curvity->value());
}

void EmitterSettingWidget::resetValue()
{
	ui.doubleSpinBox_emissionAngle->setValue(EmitterConfig::shareEmitterConfig()->getEmissionAngle());
	ui.horizontalSlider_initCount->setValue(EmitterConfig::shareEmitterConfig()->getInitCount());
	ui.spinBox_beamCount->setValue(EmitterConfig::shareEmitterConfig()->getBeamCount());
	ui.spinBox_beamCountVar->setValue(EmitterConfig::shareEmitterConfig()->getBeamCountVar());
	ui.doubleSpinBox_fieldAngle->setValue(EmitterConfig::shareEmitterConfig()->getFieldAngle());
	ui.doubleSpinBox_fieldAngleVar->setValue(EmitterConfig::shareEmitterConfig()->getFieldAngleVar());
	ui.doubleSpinBox_spinSpeed->setValue(EmitterConfig::shareEmitterConfig()->getSpinSpeed());

	ui.comboBox_positionType->setCurrentIndex(EmitterConfig::shareEmitterConfig()->getPositionType());

	if(EmitterConfig::shareEmitterConfig()->getIsPointToTarget())
		ui.checkBox_isPointToTarget->setCheckState(Qt::Checked);
	else
		ui.checkBox_isPointToTarget->setCheckState(Qt::Unchecked);

	ui.doubleSpinBox_duration->setValue(EmitterConfig::shareEmitterConfig()->getDuration());
	ui.doubleSpinBox_durationVar->setValue(EmitterConfig::shareEmitterConfig()->getDurationVar());
	ui.doubleSpinBox_interimTime->setValue(EmitterConfig::shareEmitterConfig()->getInterimTime());
	ui.doubleSpinBox_interimTimeVar->setValue(EmitterConfig::shareEmitterConfig()->getInterimTimeVar());
	ui.doubleSpinBox_lifeTime->setValue(EmitterConfig::shareEmitterConfig()->getLifeTime());
	ui.doubleSpinBox_lifeTimeVar->setValue(EmitterConfig::shareEmitterConfig()->getLifeTimeVar());
	ui.doubleSpinBox_speed->setValue(EmitterConfig::shareEmitterConfig()->getSpeed());
	ui.doubleSpinBox_speedVar->setValue(EmitterConfig::shareEmitterConfig()->getSpeedVar());
	ui.doubleSpinBox_emissionRate->setValue(EmitterConfig::shareEmitterConfig()->getEmissionRate());
	ui.doubleSpinBox_damage->setValue(EmitterConfig::shareEmitterConfig()->getDamage());
	ui.doubleSpinBox_bulletSpin->setValue(EmitterConfig::shareEmitterConfig()->getBulletSpinSpeed());
	ui.doubleSpinBox_bulletSpinVar->setValue(EmitterConfig::shareEmitterConfig()->getBulletSpinSpeedVar());
	ui.doubleSpinBox_speedDecay->setValue(EmitterConfig::shareEmitterConfig()->getSpeedDecay());
	ui.doubleSpinBox_speedLimit->setValue(EmitterConfig::shareEmitterConfig()->getSpeedLimit());

	if(EmitterConfig::shareEmitterConfig()->getIsFollow())
		ui.checkBox_isFollow->setCheckState(Qt::Checked);
	else
		ui.checkBox_isFollow->setCheckState(Qt::Unchecked);

	ui.doubleSpinBox_curvity->setValue(EmitterConfig::shareEmitterConfig()->getCurvity());
	ui.doubleSpinBox_sinAmplitude->setValue(EmitterConfig::shareEmitterConfig()->getSinAmplitude());
	ui.doubleSpinBox_sinRate->setValue(EmitterConfig::shareEmitterConfig()->getSinRate());
}