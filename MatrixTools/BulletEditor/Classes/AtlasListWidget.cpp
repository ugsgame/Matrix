
#include <QtGui>

#include "AtlasListWidget.h"

AtlasListWidget::AtlasListWidget(int pieceSize, QWidget *parent)
	: QListWidget(parent), m_PieceSize(pieceSize)
{
	setDragEnabled(true);
	setViewMode(QListView::IconMode);
	setIconSize(QSize(m_PieceSize, m_PieceSize));
	setSpacing(10);
	setAcceptDrops(true);
	setDropIndicatorShown(true);
}

void AtlasListWidget::dragEnterEvent(QDragEnterEvent *event)
{
// 	if (event->mimeData()->hasFormat("image/x-puzzle-piece"))
// 		event->accept();
// 	else
		event->ignore();
}

void AtlasListWidget::dragMoveEvent(QDragMoveEvent *event)
{
	if (event->mimeData()->hasFormat("image/x-puzzle-piece")) {
		event->setDropAction(Qt::MoveAction);
		event->accept();
	} else
		event->ignore();
}

void AtlasListWidget::dropEvent(QDropEvent *event)
{
	if (event->mimeData()->hasFormat("image/x-puzzle-piece")) {
		QByteArray pieceData = event->mimeData()->data("image/x-puzzle-piece");
		QDataStream dataStream(&pieceData, QIODevice::ReadOnly);
		QPixmap pixmap;
		QPoint location;
		dataStream >> pixmap >> location;

		addPiece(pixmap, location);

		event->setDropAction(Qt::MoveAction);
		event->accept();
	} else
		event->ignore();
}

void AtlasListWidget::addPiece(QPixmap pixmap, QPoint location,QString name)
{
	QListWidgetItem *pieceItem = new QListWidgetItem(this);
	pieceItem->setText(name);
	pieceItem->setIcon(QIcon(pixmap));
	pieceItem->setData(Qt::UserRole, QVariant(pixmap));
	pieceItem->setData(Qt::UserRole+1, location);
	pieceItem->setFlags(Qt::ItemIsEnabled | Qt::ItemIsSelectable
		| Qt::ItemIsDragEnabled);
}

int AtlasListWidget::pieceSize()
{
	return m_PieceSize;
}

void AtlasListWidget::startDrag(Qt::DropActions /*supportedActions*/)
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

	drag->exec(Qt::MoveAction);
// 	if (drag->exec(Qt::MoveAction) == Qt::MoveAction)
// 		delete takeItem(row(item));
}
