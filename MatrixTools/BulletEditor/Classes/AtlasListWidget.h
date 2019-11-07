
#ifndef ATLASSLIST_H
#define ATLASSLIST_H

#include <QListWidget>

class AtlasListWidget : public QListWidget
{
	Q_OBJECT

public:
	AtlasListWidget(int pieceSize, QWidget *parent = 0);
	void addPiece(QPixmap pixmap, QPoint location, QString name = "");
	int pieceSize();
protected:
	void dragEnterEvent(QDragEnterEvent *event);
	void dragMoveEvent(QDragMoveEvent *event);
	void dropEvent(QDropEvent *event);
	void startDrag(Qt::DropActions supportedActions);

	int m_PieceSize;
};

#endif
