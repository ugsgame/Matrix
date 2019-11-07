package editor;

import java.awt.Cursor;
import java.awt.Graphics;

public interface Shape extends Comparable {

	public final static int CORNER_NONE = 0;

	public void setScale(double scaleX, double scaleY);

	public int getID();

	public void setID(int id);

	public boolean isSelected();

	public void setSelected(boolean selected);

	public boolean containPoint(int x, int y);// 此处的XY是放大之后的XY

	public boolean inRect(int left, int top, int width, int height);

	public int getCenterX();

	public int getCenterY();

	public int getCornerAtPoint(int x, int y);// 此处的XY是放大之后的XY

	public int getCreatingDragCorner();

	public Cursor getCornerCursor(int corner);

	public int drag(int corner, int offsetX, int offsetY);

	public void confirmDrag();

	public void move(int offsetX, int offsetY);

	public String getName();

	public String getInfo();

	public void paintIdle(Graphics g);

	public void paintCorner(Graphics g);

	public void paintSelected(Graphics g);

	public void paint(Graphics g);
}