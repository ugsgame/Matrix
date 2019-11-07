
#include "DisplaySettingWidget.h"
#include "EmitterConfig.h"
#include "AtlasListWidget.h"
#include "FramesListWidget.h"
#include "Common.h"

#include <QtGui>
#include <qfiledialog.h>

#if defined(Q_OS_SYMBIAN) || defined(Q_WS_SIMULATOR)
static const QSize resultSize(50, 50);
#else
static const QSize resultSize(120,120);
#endif

BlendValue toGLBlend(int blend)
{
	int b = blend;
	BlendValue glBlend;
	switch (b)
	{
	case 0:
		glBlend = MX_ZERO;
		break;
	case 1:
		glBlend = MX_ONE;
		break;
	case 2:
		glBlend = MX_SRC_COLOR;
		break;
	case 3:
		glBlend = MX_ONE_MINUS_SRC_COLOR;
		break;
	case 4:
		glBlend = MX_DST_COLOR;
		break;
	case 5:
		glBlend = MX_ONE_MINUS_DST_COLOR;
		break;
	case 6:
		glBlend = MX_SRC_ALPHA;
		break;
	case 7:
		glBlend = MX_ONE_MINUS_SRC_ALPHA;
		break;
	case 8:
		glBlend = MX_DST_ALPHA;
		break;
	case 9:
		glBlend = MX_ONE_MINUS_DST_ALPHA;
		break;
	case 10:
		glBlend = MX_SRC_ALPHA_SATURATE;
		break;
	case 11:
		glBlend = MX_CONSTANT_COLOR;
		break;
	case 12:
		glBlend = MX_ONE_MINUS_CONSTANT_COLOR;
		break;
	case 13:
		glBlend = MX_CONSTANT_ALPHA;
		break;
	case 14:
		glBlend = MX_ONE_MINUS_CONSTANT_ALPHA;
		break;
	default:
		glBlend = MX_ZERO;
		break;
	}
	return glBlend;
}
int toBelndIndex(BlendValue blend)
{
	BlendValue b = blend;
	int glBlend;
	switch (b)
	{
	case MX_ZERO:
		glBlend = 0;
		break;
	case MX_ONE:
		glBlend = 1;
		break;
	case MX_SRC_COLOR:
		glBlend = 2;
		break;
	case MX_ONE_MINUS_SRC_COLOR:
		glBlend = 3;
		break;
	case MX_DST_COLOR:
		glBlend = 4;
		break;
	case MX_ONE_MINUS_DST_COLOR:
		glBlend = 5;
		break;
	case MX_SRC_ALPHA:
		glBlend = 6;
		break;
	case MX_ONE_MINUS_SRC_ALPHA:
		glBlend = 7;
		break;
	case MX_DST_ALPHA:
		glBlend = 8;
		break;
	case MX_ONE_MINUS_DST_ALPHA:
		glBlend = 9;
		break;
	case MX_SRC_ALPHA_SATURATE:
		glBlend = 10;
		break;
	case MX_CONSTANT_COLOR:
		glBlend = 1;
		break;
	case MX_ONE_MINUS_CONSTANT_COLOR:
		glBlend = 12;
		break;
	case MX_CONSTANT_ALPHA:
		glBlend = 13;
		break;
	case MX_ONE_MINUS_CONSTANT_ALPHA:
		glBlend = 14;
		break;
	default:
		glBlend = 0;
		break;
	}
	return glBlend;
}

DisplaySettingWidget::DisplaySettingWidget(QWidget *parent /* = 0 */)
	:QWidget(parent)
{
	ui.setupUi(this);

	ui.toolButton_source->setIconSize(resultSize);

	//
	ui.listWidget_atlas->setVisible(false);
	listWidget_atlas = new AtlasListWidget(50);
	listWidget_atlas->setGeometry(ui.listWidget_atlas->geometry());
	listWidget_atlas->setParent(ui.groupBox_atlas);
	//
	listWidget_frames = new FramesListWidget(50);
	listWidget_frames->setGeometry(ui.listWidget_frames->geometry());
	listWidget_frames->setParent(ui.groupBox_frame);
	//
	connect(ui.toolButton_source, SIGNAL(clicked()), this, SLOT(chooseSource()));
	connect(listWidget_atlas,SIGNAL(itemClicked ( QListWidgetItem * )),this,SLOT( changeFrame(QListWidgetItem *) ));
	connect(ui.listWidget_armature,SIGNAL(itemClicked ( QListWidgetItem * )),this,SLOT( changeAnimition(QListWidgetItem *) ));

	connect(ui.doubleSpinBox_Rotation, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_Rotation()));
	connect(ui.doubleSpinBox_Scale, SIGNAL(valueChanged(double)), this, SLOT(doubleSpinBox_Scale()));

	connect(ui.comboBox_src,SIGNAL(activated(QString)),this, SLOT(comboBox_src(QString))); 
	connect(ui.comboBox_dst,SIGNAL(activated(QString)),this, SLOT(comboBox_dst(QString))); 

	connect(ui.pushButton_blendNormal,SIGNAL(clicked()),this,SLOT(pushButton_blendNormal()));
	connect(ui.pushButton_blendAdditive,SIGNAL(clicked()),this,SLOT(pushButton_blendAdditive()));

	connect(ui.pushButton_flashFrame,SIGNAL(clicked()),this,SLOT(pushButton_flashFrame()));
	connect(ui.pushButton_deleteFrame,SIGNAL(clicked()),this,SLOT(pushButton_deleteFrame()));
	connect(ui.pushButton_moveUp,SIGNAL(clicked()),this,SLOT(pushButton_moveUp()));
	connect(ui.pushButton_moveDown,SIGNAL(clicked()),this,SLOT(pushButton_moveDown()));

	connect(ui.horizontalSlider_animateSpeed, SIGNAL(valueChanged(int)), this, SLOT(horizontalSlider_animateSpeed()));
	connect(ui.spinBox_animateSpeed, SIGNAL(valueChanged(double)), this, SLOT(spinBox_animateSpeed()));

	connect(ui.checkBox_setFrameAnim,SIGNAL(stateChanged(int)),this, SLOT(checkBox_setFrameAnim(int)));

	//
	emitterConfig = EmitterConfig::shareEmitterConfig();
	DisplayConfig* displayConfig = emitterConfig->getDisplayConfig();
	//
	//QTextCodec::setCodecForLocale(QTextCodec::codecForName("GBK2314"));
	//
	QStringList blendValue;
	blendValue.push_back("GL_ZERO");
	blendValue.push_back("GL_ONE");
	blendValue.push_back("GL_SRC_COLOR");
	blendValue.push_back("GL_ONE_MINUS_SRC_COLOR");
	blendValue.push_back("GL_DST_COLOR");
	blendValue.push_back("GL_ONE_MINUS_DST_COLOR");
	blendValue.push_back("GL_SRC_ALPHA");
	blendValue.push_back("GL_ONE_MINUS_SRC_ALPHA");
	blendValue.push_back("GL_DST_ALPHA");
	blendValue.push_back("GL_ONE_MINUS_DST_ALPHA");
	blendValue.push_back("GL_SRC_ALPHA_SATURATE");
	blendValue.push_back("GL_CONSTANT_COLOR");
	blendValue.push_back("GL_ONE_MINUS_CONSTANT_COLOR");
	blendValue.push_back("GL_CONSTANT_ALPHA");
	blendValue.push_back("GL_ONE_MINUS_CONSTANT_ALPHA");

	ui.comboBox_src->addItems(blendValue);
	ui.comboBox_dst->addItems(blendValue);

	ui.comboBox_src->setCurrentIndex(toBelndIndex(MX_SRC_ALPHA));
	ui.comboBox_dst->setCurrentIndex(toBelndIndex(MX_ONE_MINUS_SRC_ALPHA));

	ui.doubleSpinBox_Rotation->setRange(0,360);
	ui.doubleSpinBox_Rotation->setValue(emitterConfig->getDisplayRotation());

	ui.doubleSpinBox_Scale->setRange(-10,10);
	ui.doubleSpinBox_Scale->setValue(emitterConfig->getDisplayScale());

	ui.horizontalSlider_animateSpeed->setRange(0,50);
	ui.spinBox_animateSpeed->setRange(0,50);
	//
	originSize = QSize(this->width(),ui.groupBox_display->height() + 10);
	this->restore();
	//
	setupDisplay(displayConfig);
}

DisplaySettingWidget::~DisplaySettingWidget()
{

}

void DisplaySettingWidget::setupDisplay(DisplayConfig* displayConfig)
{
	int dType = displayConfig->displayType;
	std::string fileName =  displayConfig->displayFile;
	int slash = fileName.find_last_of("/")+1;
	std::string filePath = fileName.substr(0,slash);
	std::string fileWithSuf = fileName.substr(slash,fileName.length()-1);
	std::string fileWithoutSuf = fileWithSuf.substr(0,fileWithSuf.find_last_of("."));

	ui.label_displayInfo->setText(QString(fileWithSuf.c_str()));

	this->restore();
	switch (dType)
	{
	case 0:
		{
			loadImage(QString(fileName.c_str()),&sourceImage,ui.toolButton_source);
		}
		break;
	case 1:
		{
			QPixmap newImage;
			frameDatas = EmitterConfig::shareEmitterConfig()->getDisplayFrameDatas();	
			std::string imgFile = EmitterConfig::shareEmitterConfig()->getAtlasImageFile(displayConfig->displayFile.c_str());
			std::string imgPath = filePath + imgFile;

			//
			loadImage(QString(imgPath.c_str()),&sourceImage,ui.toolButton_source);

			newImage.load(QString(imgPath.c_str()));
			//Add atlasItem
			bool rel = false;
			listWidget_atlas->clear();
			for (int i=0;i<frameDatas.size();i++)
			{
				if(displayConfig->atlasName == frameDatas[i].name)
					rel = true;
				fRect rect = frameDatas[i].rect;
				QPixmap pieceImage = newImage.copy(rect.x, rect.y, rect.width, rect.height);
				listWidget_atlas->addPiece(pieceImage, QPoint(listWidget_atlas->pieceSize(),listWidget_atlas->pieceSize()),QString(frameDatas[i].name.c_str()));
			}
			if(frameDatas.size()>0 && !rel)
			{
				displayConfig->atlasName = frameDatas[1].name;
			}
			//
			ui.groupBox_atlas->setVisible(true);
			ui.groupBox_atlas->setGeometry(QRect( QPoint(5,originSize.height() + 10) ,ui.groupBox_atlas->rect().size()));

			ui.groupBox_frame->setVisible(false);
			//ui.groupBox_frame->setGeometry(QRect( QPoint(5,originSize.height() + ui.groupBox_atlas->rect().height() + 20) ,ui.groupBox_frame->rect().size()));

			this->resize(QSize(originSize.width(),originSize.height() + ui.groupBox_atlas->rect().height() + 20 ));
			ui.checkBox_setFrameAnim->setCheckState(Qt::Unchecked);
		}
		break;
	case 2:
		{
			loadImage(DEF_PNG_DEFAULT_PARTICLE,&sourceImage,ui.toolButton_source);
		}
		break;
	case 3:
		{	
			QPixmap newImage;
			frameDatas = EmitterConfig::shareEmitterConfig()->getDisplayFrameDatas();	
			std::string imgFile = EmitterConfig::shareEmitterConfig()->getAtlasImageFile(displayConfig->displayFile.c_str());
			std::string imgPath = filePath + imgFile;
			loadImage(QString(imgPath.c_str()),&sourceImage,ui.toolButton_source);

			//
			loadImage(QString(imgPath.c_str()),&sourceImage,ui.toolButton_source);

			newImage.load(QString(imgPath.c_str()));
			//Add atlasItem
			bool rel = false;
			listWidget_atlas->clear();
			for (int i=0;i<frameDatas.size();i++)
			{
				if(displayConfig->atlasName == frameDatas[i].name)
					rel = true;
				fRect rect = frameDatas[i].rect;
				QPixmap pieceImage = newImage.copy(rect.x, rect.y, rect.width, rect.height);
				listWidget_atlas->addPiece(pieceImage, QPoint(listWidget_atlas->pieceSize(),listWidget_atlas->pieceSize()),QString(frameDatas[i].name.c_str()));
			}
			if(frameDatas.size()>0 && !rel)
			{
				displayConfig->atlasName = frameDatas[1].name;
			}
			//
			listWidget_frames->clear();
			//
			for (int i=0;i<displayConfig->frames.size();i++)
			{
				for (int j= 0; j < frameDatas.size(); j++)
				{
					if(displayConfig->frames[i] == frameDatas[j].name)
					{
						fRect rect = frameDatas[j].rect;
						QPixmap pieceImage = newImage.copy(rect.x, rect.y, rect.width, rect.height);

						listWidget_frames->addPiece(pieceImage,QPoint(listWidget_atlas->pieceSize(),listWidget_atlas->pieceSize()),QString(frameDatas[j].name.c_str()));
					}
				}
			}
			//
			ui.horizontalSlider_animateSpeed->setValue(displayConfig->displayAnimSpeed);
			//
			ui.groupBox_atlas->setVisible(true);
			ui.groupBox_atlas->setGeometry(QRect( QPoint(5,originSize.height() + 10) ,ui.groupBox_atlas->rect().size()));

			ui.groupBox_frame->setVisible(true);
			ui.groupBox_frame->setGeometry(QRect( QPoint(5,originSize.height() + ui.groupBox_atlas->rect().height() + 20) ,ui.groupBox_frame->rect().size()));

			this->resize(QSize(originSize.width(),originSize.height() + ui.groupBox_frame->rect().height() + ui.groupBox_atlas->rect().height() + 40 ));

			ui.checkBox_setFrameAnim->setCheckState(Qt::Checked);
		}
		break;
	case 4:
		{
			displayConfig->armatureName = fileWithoutSuf;
			std::vector<std::string> animations = EmitterConfig::shareEmitterConfig()->getArmatureAnimations();
			//Add atlasItem
			ui.listWidget_armature->clear();
			bool rel = false;
			for (int i=0;i<animations.size();i++)
			{
				if(displayConfig->animationName == animations[i])
					rel = true;
				ui.listWidget_armature->addItem(QString(animations[i].c_str()));
			}
			if(animations.size()>0&&!rel)
			{
				displayConfig->animationName = animations[1];
			}

			loadImage(DEF_PNG_DEFAULT_PARTICLE,&sourceImage,ui.toolButton_source);

			ui.groupBox_armature->setVisible(true);
			ui.groupBox_armature->setGeometry(QRect( QPoint(5,originSize.height() + 10) ,ui.groupBox_armature->rect().size()));
			this->resize(QSize(originSize.width(),originSize.height() + ui.groupBox_armature->height() + 20 ));
		}
		break;
	default:
		break;
	}

	EmitterConfig::shareEmitterConfig()->resetDisplay();
	ui.comboBox_src->setCurrentIndex(toBelndIndex(emitterConfig->getSrcBlend()));
	ui.comboBox_dst->setCurrentIndex(toBelndIndex(emitterConfig->getDstBlend()));
	ui.doubleSpinBox_Rotation->setValue(emitterConfig->getDisplayRotation());
	ui.doubleSpinBox_Scale->setValue(emitterConfig->getDisplayScale());
}

void DisplaySettingWidget::chooseFile(const QString &title, QImage *image, QToolButton *button)
{
	QString fileName = QFileDialog::getOpenFileName(this, title,"","display file(*.png;;*.jpg;;*.plist;;*.ExportJson)");
	QString oldFile = emitterConfig->getDisplayConfig()->displayFile.c_str();
	if (!fileName.isEmpty() && oldFile != fileName)
	{
		std::string fn = fileName.toStdString();
		emitterConfig->getDisplayConfig()->displayType = emitterConfig->checkDisplayType(fn.c_str());
		emitterConfig->getDisplayConfig()->displayFile = fn;

		setupDisplay(emitterConfig->getDisplayConfig());
	}
}

void DisplaySettingWidget::loadImage(const QString &fileName, QImage *image, QToolButton *button)
{
	image->load(fileName);

	// Scale the image to given size
	*image = image->scaled(resultSize, Qt::KeepAspectRatio);

	QImage fixedImage(resultSize, QImage::Format_ARGB32_Premultiplied);
	QPainter painter(&fixedImage);
	painter.setCompositionMode(QPainter::CompositionMode_Source);
	painter.fillRect(fixedImage.rect(), Qt::transparent);
	painter.setCompositionMode(QPainter::CompositionMode_SourceOver);
	painter.drawImage(imagePos(*image), *image);
	painter.end();
	button->setIcon(QPixmap::fromImage(fixedImage));
}

QPoint DisplaySettingWidget::imagePos(const QImage &image)const
{
	return QPoint((resultSize.width() - image.width()) / 2,
		(resultSize.height() - image.height()) / 2);
}

void DisplaySettingWidget::restore()
{
	ui.groupBox_atlas->setVisible(false);
	ui.groupBox_frame->setVisible(false);
	ui.groupBox_armature->setVisible(false);

	listWidget_atlas->setVisible(true);
	
	this->resize(originSize);
}

void DisplaySettingWidget::chooseSource()
{
	chooseFile(tr("Choose Source Image"), &sourceImage, ui.toolButton_source);
}

void DisplaySettingWidget::changeFrame(QListWidgetItem * item)
{
	QString name = item->text();
	DisplayConfig* dConfig = EmitterConfig::shareEmitterConfig()->getDisplayConfig();
	if(dConfig->displayType == 1)
	{
		dConfig->atlasName = name.toStdString();
		EmitterConfig::shareEmitterConfig()->resetDisplay();
	}
}

void DisplaySettingWidget::changeAnimition(QListWidgetItem * item)
{
	QString name = item->text();
	DisplayConfig* dConfig = EmitterConfig::shareEmitterConfig()->getDisplayConfig();
	if(dConfig->displayType == 4)
	{
		dConfig->animationName = name.toStdString();
		EmitterConfig::shareEmitterConfig()->resetDisplay();
	}
}

void DisplaySettingWidget::comboBox_src(QString combo)
{
	emitterConfig->getDisplayConfig()->displaySrcBlend = toGLBlend(ui.comboBox_src->currentIndex());
	emitterConfig->resetDisplay();
}

void DisplaySettingWidget::comboBox_dst(QString combo)
{
    emitterConfig->getDisplayConfig()->displayDstBlend = toGLBlend(ui.comboBox_dst->currentIndex());
	emitterConfig->resetDisplay();
}

void DisplaySettingWidget::doubleSpinBox_Rotation()
{
	emitterConfig->setDisplayRotation(ui.doubleSpinBox_Rotation->value());
}

void DisplaySettingWidget::doubleSpinBox_Scale()
{
	emitterConfig->setDisplayScale(ui.doubleSpinBox_Scale->value());
}

void DisplaySettingWidget::pushButton_blendNormal()
{
	ui.comboBox_src->setCurrentIndex(toBelndIndex(MX_SRC_ALPHA));
	ui.comboBox_dst->setCurrentIndex(toBelndIndex(MX_ONE_MINUS_SRC_ALPHA));

	emitterConfig->setSrcBlend(MX_SRC_ALPHA);
	emitterConfig->setDstBlend(MX_ONE_MINUS_SRC_ALPHA);

	emitterConfig->resetDisplay();
}

void DisplaySettingWidget::pushButton_blendAdditive()
{
	ui.comboBox_src->setCurrentIndex(toBelndIndex(MX_SRC_ALPHA));
	ui.comboBox_dst->setCurrentIndex(toBelndIndex(MX_ONE));

	emitterConfig->setSrcBlend(MX_SRC_ALPHA);
	emitterConfig->setDstBlend(MX_ONE);

	emitterConfig->resetDisplay();
}

void DisplaySettingWidget::pushButton_flashFrame()
{
	emitterConfig->setAnimateFrames(listWidget_frames->getFrameNames());
	emitterConfig->resetDisplay();
}

void DisplaySettingWidget::pushButton_deleteFrame()
{
	listWidget_frames->deleteSelectedPiece();
}

void DisplaySettingWidget::pushButton_moveUp()
{
	listWidget_frames->moveUpPiece();
}
void DisplaySettingWidget::pushButton_moveDown()
{
	listWidget_frames->moveDownPiece();
}

void DisplaySettingWidget::horizontalSlider_animateSpeed()
{
	ui.spinBox_animateSpeed->setValue(ui.horizontalSlider_animateSpeed->value());
	emitterConfig->setAnimateSpeed(ui.horizontalSlider_animateSpeed->value());
}
void DisplaySettingWidget::spinBox_animateSpeed()
{
	ui.horizontalSlider_animateSpeed->setValue(ui.spinBox_animateSpeed->value());
	emitterConfig->setAnimateSpeed(ui.spinBox_animateSpeed->value());
}

void DisplaySettingWidget::checkBox_setFrameAnim(int clicked)
{
	if(clicked)
	{
		ui.groupBox_frame->setVisible(true);
		emitterConfig->getDisplayConfig()->displayType = 3;
		setupDisplay(emitterConfig->getDisplayConfig());
	}
	else
	{
		ui.groupBox_frame->setVisible(false);
		emitterConfig->getDisplayConfig()->displayType = 1;
		setupDisplay(emitterConfig->getDisplayConfig());
	}
}