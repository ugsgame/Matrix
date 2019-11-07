
#include <QtGui>
#include <qmessagebox.h>
#include "FramesListWidget.h"

FramesListWidget::FramesListWidget(int pieceSize, QWidget *parent)
	: QListWidget(parent), m_PieceSize(pieceSize)
{
	//setDragEnabled(true);
	setViewMode(QListView::ListMode);
	setIconSize(QSize(m_PieceSize, m_PieceSize));
	setSpacing(10);
	setAcceptDrops(true);
	setDropIndicatorShown(true);
}

void FramesListWidget::dragEnterEvent(QDragEnterEvent *event)
{
	if (event->mimeData()->hasFormat("image/x-puzzle-piece"))
		event->accept();
	else
		event->ignore();
}

void FramesListWidget::dragMoveEvent(QDragMoveEvent *event)
{
	if (event->mimeData()->hasFormat("image/x-puzzle-piece")) {
		event->setDropAction(Qt::MoveAction);
		event->accept();
	} else
		event->ignore();
}

void FramesListWidget::dropEvent(QDropEvent *event)
{
	if (event->mimeData()->hasFormat("image/x-puzzle-piece")) {
		QByteArray pieceData = event->mimeData()->data("image/x-puzzle-piece");
		QDataStream dataStream(&pieceData, QIODevice::ReadOnly);
		QPixmap pixmap;
		QPoint location;
		QString name;
		dataStream >> pixmap >> location >> name;

		addPiece(pixmap, location, name);

		event->setDropAction(Qt::MoveAction);
		event->accept();
	} else
		event->ignore();
}

void FramesListWidget::addPiece(QPixmap pixmap, QPoint location,QString name)
{
	QListWidgetItem *pieceItem = new QListWidgetItem(this);
	pieceItem->setText(name);
	pieceItem->setIcon(QIcon(pixmap));
	pieceItem->setData(Qt::UserRole, QVariant(pixmap));
	pieceItem->setData(Qt::UserRole+1, location);
	pieceItem->setFlags(Qt::ItemIsEnabled | Qt::ItemIsSelectable
		| Qt::ItemIsDragEnabled);
}

int FramesListWidget::pieceSize()
{
	return m_PieceSize;
}

void FramesListWidget::deleteSelectedPiece()
{
	QListWidgetItem *item = currentItem();
	if(item)
	{
		QMessageBox::StandardButton reply;
		reply = QMessageBox::question(this, tr("Delete frames"),QString("Are you sure to delete:") + item->text(),QMessageBox::Yes | QMessageBox::No);
		if (reply == QMessageBox::Yes)
		{
			takeItem(row(item));
		}
	}
}

void FramesListWidget::moveUpPiece()
{
	if(this->count()>1)
	{
		int row = this->currentRow();
		if(row > 0)
		{
			QListWidgetItem* item = this->takeItem(row);
			this->insertItem(row - 1,item);
			this->setCurrentRow(row - 1);
		}
	}
}

void FramesListWidget::moveDownPiece()
{
	if(this->count()>1)
	{
		int row = this->currentRow();
		if(row < this->count()-1)
		{
			QListWidgetItem* item = this->takeItem(row);
			this->insertItem(row + 1,item);
			this->setCurrentRow(row + 1);
		}
	}
}

std::vector<std::string> FramesListWidget::getFrameNames()
{
	std::vector<std::string> frameNames;
	qDebug()<<this->count();
	for (int i=0;i<this->count(); i++)
	{
		QListWidgetItem* item = this->item(i);
		frameNames.push_back(item->text().toStdString());
	}
	return frameNames;
}

void FramesListWidget::startDrag(Qt::DropActions /*supportedActions*/)
{
	QListWidgetItem *item = currentItem();

	QByteArray itemData;
	QDataStream dataStream(&itemData, QIODevice::WriteOnly);
	QPixmap pixmap = qvariant_cast<QPixmap>(item->data(Qt::UserRole));
	QPoint location = item->data(Qt::UserRole+1).toPoint();

	dataStream << pixmap << location << item->text();

	QMimeData *mimeData = new QMimeData;
	mimeData->setData("image/x-puzzle-piece", itemData);

	QDrag *drag = new QDrag(this);
	drag->setMimeData(mimeData);
	drag->setHotSpot(QPoint(pixmap.width()/2, pixmap.height()/2));
	drag->setPixmap(pixmap);
	drag->setObjectName(item->text());

	if (drag->exec(Qt::MoveAction) == Qt::MoveAction)
		delete takeItem(row(item));
}
