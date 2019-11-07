
#ifndef FRAMESLIST_H
#define FRAMESLIST_H

#include <QListWidget>
#include <vector>
#include <string>

class FramesListWidget : public QListWidget
{
	Q_OBJECT

public:
	FramesListWidget(int pieceSize, QWidget *parent = 0);
	void addPiece(QPixmap pixmap, QPoint location, QString name = "");
	int pieceSize();

	void deleteSelectedPiece();
	void moveUpPiece();
	void moveDownPiece();

	std::vector<std::string> getFrameNames();
protected:
	void dragEnterEvent(QDragEnterEvent *event);
	void dragMoveEvent(QDragMoveEvent *event);
	void dropEvent(QDropEvent *event);
	void startDrag(Qt::DropActions supportedActions);

	int m_PieceSize;

private: 

};

#endif
