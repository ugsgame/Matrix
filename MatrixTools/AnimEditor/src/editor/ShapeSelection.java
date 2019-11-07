package editor;

import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * 一个Shape的集合，用来包含一个或多个被选中的Shape。
 * 
 * @see ShapeManager
 */
public class ShapeSelection {

	private ShapeManager shapeManager;
	private ArrayList shapes;
	private int dragCorner, dragStartX, dragStartY;

	public ShapeSelection(ShapeManager shapeManager) {
		this.shapeManager = shapeManager;
		shapes = new ArrayList();
		dragStartX = 0;
		dragStartY = 0;
	}

	public Shape[] getShapes() {
		TreeSet treeSet = new TreeSet();
		Shape[] result = new Shape[shapes.size()];
		for (int i = 0; i < shapes.size(); ++i) {
			treeSet.add(shapes.get(i));
		}
		Iterator it = treeSet.iterator();
		int i = 0;
		while (it.hasNext()) {
			result[i++] = (Shape) (it.next());
		}
		return result;
	}

	public boolean isEmpty() {
		return shapes.size() <= 0;
	}

	public void clear() {
		Shape tmp;
		for (int i = 0; i < shapes.size(); ++i) {
			tmp = (Shape) (shapes.get(i));
			tmp.setSelected(false);
		}
		shapes.clear();
		shapeManager.selectionChanged();
	}

	public void selectShape(Shape shape) {
		Shape[] tmp = new Shape[1];
		tmp[0] = shape;
		selectShapes(tmp);
	}

	public void selectShapes(Shape[] shapes) {
		clear();
		for (int i = 0; i < shapes.length; ++i) {
			if (this.shapes.add(shapes[i])) {
				shapes[i].setSelected(true);
			}
		}
		shapeManager.selectionChanged();
	}

	public boolean hasShape(Shape shape) {
		for (int i = 0; i < shapes.size(); ++i) {
			if (((Shape) (shapes.get(i))).getID() == shape.getID()) { return true; }
		}
		return false;
	}

	public boolean addShape(Shape shape) {
		if (!hasShape(shape)) {
			if (shapes.add(shape)) {
				shape.setSelected(true);
				shapeManager.selectionChanged();
				return true;
			}
		}
		return false;
	}

	public Shape getTopShapeAtPoint(int x, int y) {// 此处的XY是放大之后的XY{
		Shape shape = null, result = null;
		for (int i = 0; i < shapes.size(); ++i) {
			shape = (Shape) (shapes.get(i));
			if (shape.containPoint(x, y)) {
				if (result != null) {
					if (shape.compareTo(result) > 0) {
						result = shape;
					}
				}
				else {
					result = shape;
				}
			}
		}
		return result;
	}

	public boolean removeShape(Shape shape) {
		for (int i = 0; i < shapes.size(); ++i) {
			if (((Shape) (shapes.get(i))).getID() == shape.getID()) {
				removeShape(i);
				shapeManager.selectionChanged();
				return true;
			}
		}
		return false;
	}

	private void removeShape(int index) {
		if (index < 0 || index >= shapes.size()) { return; }
		((Shape) (shapes.get(index))).setSelected(false);
		shapes.remove(index);
	}

	public boolean pointInShapes(int x, int y) {// 此处的XY是放大之后的XY{
		for (int i = 0; i < shapes.size(); ++i) {
			if (((Shape) (shapes.get(i))).containPoint(x, y)) { return true; }
		}
		return false;
	}

	public Cursor getCursor(int x, int y) {// 此处的XY是放大之后的XY{
		Shape shape = null;
		if (shapes.size() == 1) {
			shape = (Shape) (shapes.get(0));
			return shape.getCornerCursor(shape.getCornerAtPoint(x, y));
		}
		else if (shapes.size() > 1) {
			for (int i = 0; i < shapes.size(); ++i) {
				shape = (Shape) (shapes.get(i));
				if (shape.containPoint(x, y)) { return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR); }
			}
		}

		return Cursor.getDefaultCursor();
	}

	public boolean readyToDrag(int x, int y) {// 此处的XY是放大之后的XY{
		Shape shape = null;
		if (shapes.size() == 1) {
			shape = (Shape) (shapes.get(0));
			dragCorner = shape.getCornerAtPoint(x, y);
			if (dragCorner != Shape.CORNER_NONE) {
				dragStartX = shapeManager.getMouseX();
				dragStartY = shapeManager.getMouseY();
				return true;
			}
		}
		else if (shapes.size() > 1) {
			for (int i = 0; i < shapes.size(); ++i) {
				shape = (Shape) (shapes.get(i));
				if (shape.containPoint(x, y)) {
					dragStartX = shapeManager.getMouseX();
					dragStartY = shapeManager.getMouseY();
					return true;
				}
			}
		}

		return false;
	}

	public void startCreatingDrag(int x, int y) {
		if (shapes.size() <= 0) { return; }
		Shape shape = (Shape) (shapes.get(0));
		dragCorner = shape.getCreatingDragCorner();
		dragStartX = x; // shapeManager.getMouseX();
		dragStartY = y; // shapeManager.getMouseY();
	}

	public void dragTo(int x, int y) {
		int offsetX = x - dragStartX;
		int offsetY = y - dragStartY;
		Shape shape;

		if (shapes.size() == 1) {
			shape = (Shape) (shapes.get(0));
			dragCorner = shape.drag(dragCorner, offsetX, offsetY);
		}
		else if (shapes.size() > 1) {
			for (int i = 0; i < shapes.size(); ++i) {
				shape = (Shape) (shapes.get(i));
				shape.move(offsetX, offsetY);
			}
		}
		dragStartX = x;
		dragStartY = y;

		shapeManager.selectionChanged();
	}

	public void click(int x, int y, int keyCode) {// 此处的XY是放大之后的XY{
		Shape shape;
		switch (keyCode) {
		case KeyEvent.VK_CONTROL:
			int itemIndex = -1;
			for (int i = 0; i < shapes.size(); ++i) {
				shape = (Shape) (shapes.get(i));
				if (shape.containPoint(x, y)) {
					if (itemIndex >= 0) {
						if (shape.compareTo(shapes.get(itemIndex)) > 0) {
							itemIndex = i;
						}
					}
					else {
						itemIndex = i;
					}
				}
			}
			if (itemIndex >= 0) {
				removeShape(itemIndex);
			}
			shapeManager.selectionChanged();
			break;
		default:
			break;
		}
	}

	public void move(int offsetX, int offsetY) {
		for (int i = 0; i < shapes.size(); ++i) {
			((Shape) (shapes.get(i))).move(offsetX, offsetY);
		}
		shapeManager.selectionChanged();
	}

	public void confirmDrag() {
		if (shapes.size() == 1) {
			Shape shape = (Shape) (shapes.get(0));
			shape.confirmDrag();
		}
	}
}