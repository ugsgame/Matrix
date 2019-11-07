package editor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * 管理Shape的选择、添加、移动和删除的管理器。
 */
abstract public class ShapeManager implements Manager {

	// 鼠标状态
	private final static int MOUSE_STATE_NORMAL = 0;
	private final static int MOUSE_STATE_DRAGGING = 1;
	private final static int MOUSE_STATE_RECTANGLING = 2; // 鼠标拉框
	private final static int MOUSE_STATE_CTRL_RECTING = 3; // 按住Ctrl键鼠标拉框
	private final static int MOUSE_STATE_SELECT_POINT_RIGHT_PRESSED = 4; // 在SelectPoint状态按下鼠标右键的标志

	// 选择状态
	public final static int NONE_SELECTION = 0;
	public final static int SINGLE_SELECTION = 1;
	public final static int MULTI_SELECTION = 2;

	public final static int[][] KEY_CODES = { { KeyEvent.VK_UP, 0x00000001, KeyEvent.VK_DOWN },
	        { KeyEvent.VK_DOWN, 0x00000002, KeyEvent.VK_UP },
	        { KeyEvent.VK_LEFT, 0x00000004, KeyEvent.VK_RIGHT },
	        { KeyEvent.VK_RIGHT, 0x00000008, KeyEvent.VK_LEFT } };

	protected ScrollablePanel scrollablePanel;
	protected MouseInfo mouseInfo;
	protected ArrayList shapes;
	protected static ArrayList copiedShapes;
	protected ShapeSelection selection;
	protected boolean moveable, deleteable, copyable, layerable;

	protected int maxID, mouseState, oldMouseX, oldMouseY, mouseX, mouseY, pressedKey,
	        keyMoveShapeX, keyMoveShapeY, selectionMode;

	private ArrayList listeners;

	public ShapeManager(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) {
		try {
			init(scrollablePanel, mouseInfo);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) throws Exception {
		this.scrollablePanel = scrollablePanel;
		scrollablePanel.addListener(new ScrollablePanelAdapter() {

			public void scaleChanged() {
				selfScaleChanged();
			}
		});
		this.mouseInfo = mouseInfo;

		shapes = new ArrayList();
		copiedShapes = new ArrayList();
		listeners = new ArrayList();

		this.selection = new ShapeSelection(this);

		selectionMode = MULTI_SELECTION;
		moveable = true;
		deleteable = true;
		copyable = true;
		layerable = true;

		initReset();
		copiedShapes.clear();

		scrollablePanel.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				selfMouseClicked(e);
			}

			public void mousePressed(MouseEvent e) {
				selfMousePressed(e);
			}

			public void mouseReleased(MouseEvent e) {
				selfMouseReleased(e);
			}

			public void mouseEntered(MouseEvent e) {
				selfMouseEntered(e);
			}

			public void mouseExited(MouseEvent e) {
				selfMouseExited(e);
			}
		});

		scrollablePanel.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				selfMouseDragged(e);
			}

			public void mouseMoved(MouseEvent e) {
				selfMouseMoved(e);
			}
		});

		scrollablePanel.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				selfKeyTyped(e);
			}

			public void keyPressed(KeyEvent e) {
				selfKeyPressed(e);
			}

			public void keyReleased(KeyEvent e) {
				selfKeyReleased(e);
			}
		});
	}
	
	public ScrollablePanel getScrollablePanel() {
		return scrollablePanel;
	}

	private void initReset() {
		maxID = 0;
		mouseState = MOUSE_STATE_NORMAL;
		pressedKey = 0;
		keyMoveShapeX = 0;
		keyMoveShapeY = 0;

		shapes.clear();

		selection.clear();
	}

	public void reset() {
		initReset();
		if (scrollablePanel != null) scrollablePanel.repaint();
	}

	public void resetOrigin(int offsetX, int offsetY) {
		for (int i = 0; i < shapes.size(); ++i) {
			Shape shape = (Shape) (shapes.get(i));
			shape.move(offsetX, offsetY);
		}
		if (scrollablePanel != null) scrollablePanel.repaint();
	}

	public void addListener(ManagerListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public void removeListener(ManagerListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

	protected int useMaxID() {
		return maxID++;
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public Shape getShape(int shapeID) {
		Shape result = null;
		for (int i = 0; i < shapes.size(); ++i) {
			if (((Shape) (shapes.get(i))).getID() == shapeID) {
				result = (Shape) (shapes.get(i));
				break;
			}
		}
		return result;
	}

	public Shape[] getShapes() {
		Shape[] result = new Shape[shapes.size()];
		for (int i = 0; i < shapes.size(); ++i) {
			result[i] = (Shape) (shapes.get(i));
		}
		return result;
	}

	public ShapeSelection getSelection() {
		return selection;
	}

	public void setSelectionMode(int mode) {
		if (this.selectionMode == mode) { return; }
		selection.clear();
		selectionMode = mode;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public void setDeleteable(boolean deleteable) {
		this.deleteable = deleteable;
	}

	public void setCopyable(boolean copyable) {
		this.copyable = copyable;
		copiedShapes.clear();
	}

	public void setLayerable(boolean layerable) {
		this.layerable = layerable;
	}

	protected void setMouseState(int state) {
		switch (state) {
		case MOUSE_STATE_NORMAL: // 鼠标状态还原
			this.mouseState = state;
			break;
		case MOUSE_STATE_DRAGGING: // 拖动Shape
			if (moveable) {
				this.mouseState = state;
			}
			break;
		case MOUSE_STATE_RECTANGLING: // 框选Shape
			if (selectionMode == MULTI_SELECTION) {
				this.mouseState = state;
			}
			break;
		case MOUSE_STATE_CTRL_RECTING: // Ctrl框选Shape
			if (selectionMode == MULTI_SELECTION) {
				this.mouseState = state;
			}
			break;
		case MOUSE_STATE_SELECT_POINT_RIGHT_PRESSED: // 右键选择Shape
			if (selectionMode != NONE_SELECTION) {
				this.mouseState = state;
			}
			break;
		default:
			break;
		}
	}

	protected int getMouseX(MouseEvent e) {
		return scrollablePanel.getMouseX(e);
	}

	protected int getMouseY(MouseEvent e) {
		return scrollablePanel.getMouseY(e);
	}

	private void selfScaleChanged() {
		for (int i = 0; i < shapes.size(); ++i) {
			Shape shape = (Shape) (shapes.get(i));
			shape.setScale(scrollablePanel.getScale(), scrollablePanel.getScale());
		}
	}

	private void selfMouseClicked(MouseEvent e) {
		if (!scrollablePanel.isMouseMovingViewport()) {
			mouseClicked(e);
		}
		mouseChanged();
	}

	protected void mouseClicked(MouseEvent e) {
		if (e.getButton() == XUtil.LEFT_BUTTON && e.getClickCount() >= 2) {
			switch (mouseInfo.getInfo()) {
			case MouseInfo.SELECT_POINT:
				selectPointMouseDoubleClick(e);
				break;
			default:
				break;
			}
		}
	}

	private void selfMousePressed(MouseEvent e) {
		oldMouseX = mouseX = getMouseX(e);
		oldMouseY = mouseY = getMouseY(e);
		if (!scrollablePanel.isMouseMovingViewport()) {
			mousePressed(e);
		}
		mouseChanged();
	}

	protected void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case XUtil.LEFT_BUTTON:
			switch (mouseInfo.getInfo()) {
			case MouseInfo.SELECT_POINT:
				selectPointMouseLeftPressed(e);
				break;
			case MouseInfo.NEW_SPRITE:
				newShapeMouseLeftPressed(e);
				break;
			default:
				break;
			}
			break;
		case XUtil.RIGHT_BUTTON:
			switch (mouseInfo.getInfo()) {
			case MouseInfo.SELECT_POINT:
				selectPointMouseRightPressed(e);
				break;
			default:
				otherMouseRightPressed(e);
				break;
			}
		default:
			break;
		}
	}

	private void selfMouseReleased(MouseEvent e) {
		mouseX = getMouseX(e);
		mouseY = getMouseY(e);
		if (!scrollablePanel.isMouseMovingViewport()) {
			mouseReleased(e);
		}
		setMouseState(MOUSE_STATE_NORMAL);
		mouseChanged();
	}

	protected void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case XUtil.LEFT_BUTTON:
			switch (mouseInfo.getInfo()) {
			case MouseInfo.SELECT_POINT:
				selectPointMouseLeftReleased(e);
				break;
			case MouseInfo.NEW_SPRITE:
				// newShapeMouseLeftReleased(e);
				break;
			default:
				break;
			}
			break;
		case XUtil.RIGHT_BUTTON:
			switch (mouseInfo.getInfo()) {
			case MouseInfo.SELECT_POINT:
				selectPointMouseRightReleased(e);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	private void selfMouseDragged(MouseEvent e) {
		mouseX = getMouseX(e);
		mouseY = getMouseY(e);
		// checkMouseXY();
		if (!scrollablePanel.isMouseMovingViewport()) {
			mouseDragged(e);
		}
		mouseChanged();
	}

	protected void mouseDragged(MouseEvent e) {
		switch (mouseInfo.getInfo()) {
		case MouseInfo.SELECT_POINT:
			selectPointMouseDragged(e);
			break;
		default:
			break;
		}
	}

	private void selfMouseMoved(MouseEvent e) {
		mouseX = getMouseX(e);
		mouseY = getMouseY(e);
		if (!scrollablePanel.isMouseMovingViewport()) {
			mouseMoved(e);
		}
		mouseChanged();
	}

	protected void mouseMoved(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.NOBUTTON:
			switch (mouseInfo.getInfo()) {
			case MouseInfo.SELECT_POINT:
				selectPointMouseMoved(e);
				break;
			case MouseInfo.NEW_SPRITE:
				newShapeMouseMoved(e);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	private void selfMouseEntered(MouseEvent e) {
		selfMouseMoved(e);
		mouseEntered(e);
	}

	protected void mouseEntered(MouseEvent e) {}

	private void selfMouseExited(MouseEvent e) {
		selfMouseMoved(e);
		mouseExited(e);
	}

	protected void mouseExited(MouseEvent e) {
		scrollablePanel.setCursor(Cursor.getDefaultCursor());
	}

	private void selfKeyTyped(KeyEvent e) {}

	private void selfKeyPressed(KeyEvent e) {
		int tmp, keyID = getKeyID(e.getKeyCode());
		if (keyID >= 0) {
			pressedKey = pressedKey | KEY_CODES[keyID][1];
			for (int i = 2; i < KEY_CODES[keyID].length; ++i) {
				tmp = getKeyID(KEY_CODES[keyID][i]);
				if (tmp >= 0) {
					pressedKey = pressedKey & (~KEY_CODES[tmp][1]);
				}
			}
		}
		keyPressed(e);
		keyChanged();
	}

	protected void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			keyUpPressed(e);
			break;
		case KeyEvent.VK_DOWN:
			keyDownPressed(e);
			break;
		case KeyEvent.VK_LEFT:
			keyLeftPressed(e);
			break;
		case KeyEvent.VK_RIGHT:
			keyRightPressed(e);
			break;
		case KeyEvent.VK_C:
			if (copyable && e.isControlDown() && !selection.isEmpty()) {
				copyShapes(selection.getShapes());
			}
			break;
		case KeyEvent.VK_V:
			if (copyable && e.isControlDown()) {
				if (scrollablePanel.isMouseIn()) {
					pasteShapes(mouseX, mouseY);
				}
				else {
					pasteShapes(scrollablePanel.getBasicWidth() / 2,
					        scrollablePanel.getBasicHeight() / 2);
				}
			}
			break;
		default:
			break;
		}
	}

	private void selfKeyReleased(KeyEvent e) {
		int keyID = getKeyID(e.getKeyCode());
		if (keyID >= 0) {
			pressedKey = pressedKey & (~KEY_CODES[keyID][1]);
		}
		keyReleased(e);
		keyChanged();
	}

	protected void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DELETE:
			if (deleteable) {
				keyDeleteReleased();
			}
			break;
		case KeyEvent.VK_HOME:
			if (layerable && e.isControlDown() && !selection.isEmpty()) {
				setShapesToTop(selection.getShapes());
			}
			break;
		case KeyEvent.VK_END:
			if (layerable && e.isControlDown() && !selection.isEmpty()) {
				setShapesToBottom(selection.getShapes());
			}
			break;
		default:
			break;
		}
	}

	protected int getKeyID(int keyCode) {
		for (int i = 0; i < KEY_CODES.length; ++i) {
			if (keyCode == KEY_CODES[i][0]) { return i; }
		}
		return -1;
	}

	protected boolean isKeyPressed(int keyCode) {
		int keyID = getKeyID(keyCode);
		if (keyID >= 0) { return (pressedKey & KEY_CODES[keyID][1]) != 0; }
		return false;
	}

	protected int getShapeIndex(Shape shape) {
		if (shape != null) {
			Shape tmp;
			for (int i = 0; i < shapes.size(); ++i) {
				tmp = (Shape) (shapes.get(i));
				if (tmp.getID() == shape.getID()) { return i; }
			}
		}
		return -1;
	}

	protected Shape findShape(int id) {
		Shape tmp;
		for (int i = 0; i < shapes.size(); ++i) {
			tmp = (Shape) (shapes.get(i));
			if (tmp.getID() == id) { return tmp; }
		}
		return null;
	}

	protected Shape addNewShape(int x, int y) {
		Shape shape = createShape(x, y);
		if (shape == null) { return null; }
		if (addShape(shape)) { return shape; }
		return null;
	}

	protected Shape createShape(int x, int y) {
		return null;
	}

	protected boolean addShape(Shape shape) {
		if (getShapeIndex(shape) < 0) {
			if (shapes.add(shape)) {
				shape.setScale(scrollablePanel.getScale(), scrollablePanel.getScale());
				if (maxID <= shape.getID()) {
					maxID = shape.getID() + 1;
				}
				resortShapes();
				return true;
			}
		}
		return false;
	}

	protected boolean removeShape(Shape shape) {
		selection.removeShape(shape);
		int shapeIndex = getShapeIndex(shape);
		if (shapeIndex >= 0) {
			shapes.remove(shapeIndex);
			return true;
		}
		return false;
	}

	protected void resortShapes() {
		TreeSet tmp = new TreeSet();
		for (int i = 0; i < shapes.size(); ++i) {
			tmp.add(shapes.get(i));
		}
		shapes.clear();
		Iterator it = tmp.iterator();
		while (it.hasNext()) {
			shapes.add(it.next());
		}
	}

	protected Shape[] getAllShapesAtPoint(int x, int y) {
		Shape shape;
		ArrayList tmp = new ArrayList();

		for (int i = 0; i < shapes.size(); ++i) {
			shape = (Shape) (shapes.get(i));
			if (shape.containPoint(x, y)) {
				tmp.add(shape);
			}
		}

		Shape[] result = new Shape[tmp.size()];
		for (int i = 0; i < tmp.size(); ++i) {
			result[i] = (Shape) (tmp.get(i));
		}
		return result;
	}

	protected Shape getTopShapeAtPoint(int x, int y) {
		Shape shape;
		for (int i = shapes.size() - 1; i >= 0; --i) {
			shape = (Shape) (shapes.get(i));
			if (shape.containPoint(x, y)) { return shape; }
		}
		return null;
	}

	protected Shape[] getAllShapesInRect(int left, int top, int width, int height) {
		Shape shape;
		ArrayList tmp = new ArrayList();

		for (int i = 0; i < shapes.size(); ++i) {
			shape = (Shape) (shapes.get(i));
			if (shape.inRect(left, top, width, height)) {
				tmp.add(shape);
			}
		}

		Shape[] result = new Shape[tmp.size()];
		for (int i = 0; i < tmp.size(); ++i) {
			result[i] = (Shape) (tmp.get(i));
		}
		return result;
	}

	protected void selectPointMouseLeftPressed(MouseEvent e) {
		switch (selectionMode) {
		case NONE_SELECTION:
			break;
		case SINGLE_SELECTION:
			singleSelectionMouseLeftPressed(e);
			break;
		case MULTI_SELECTION:
			multiSelectionMouseLeftPressed(e);
			break;
		default:
			break;
		}
	}

	protected void singleSelectionMouseLeftPressed(MouseEvent e) {
		if (selection.pointInShapes(e.getX(), e.getY())) {// 按键点在已选择的物体内
			if (selection.readyToDrag(e.getX(), e.getY())) {// 可以拖动
				setMouseState(MOUSE_STATE_DRAGGING); // 开始准备拖动物体了
			}
		}
		else {
			Shape shape = getTopShapeAtPoint(e.getX(), e.getY());
			if (shape != null) {
				selection.selectShape(shape);
				// if(selection.readyToDrag(e.getX(), e.getY())) {
				// setMouseState(MOUSE_STATE_DRAGGING); //可以拖动物体了
				// }
			}
			else {
				selection.clear();
			}
		}
		scrollablePanel.repaint();
	}

	protected void multiSelectionMouseLeftPressed(MouseEvent e) {
		if (selection.pointInShapes(e.getX(), e.getY())) { // 按键点在已选择的物体内
			if (e.isControlDown()) { // 按了Ctrl键
				selection.click(e.getX(), e.getY(), KeyEvent.VK_CONTROL); // 取消该物体的选择
			}
			else { // 没有按Ctrl键
				if (selection.readyToDrag(e.getX(), e.getY())) {
					setMouseState(MOUSE_STATE_DRAGGING); // 可以拖动物体了
				}
			}
		}
		else { // 按键点不在已选的物体内
			Shape shape = getTopShapeAtPoint(e.getX(), e.getY()); // 获得最上方的物体
			if (shape != null) { // 选中物体
				if (e.isControlDown()) { // 按了Ctrl键
					selection.addShape(shape); // 将选中的物体加入到selection中
				}
				else {
					selection.selectShape(shape); // 取消之前选中的物体，然后将现在选中的物体加入到selection中
					// if(selection.readyToDrag(e.getX(), e.getY())) {
					// setMouseState(MOUSE_STATE_DRAGGING); //可以拖动物体了
					// }
				}
			}
			else { // 没有选中任何物体
				if (e.isControlDown()) {
					setMouseState(MOUSE_STATE_CTRL_RECTING); // Ctrl键拖框
				}
				else {
					selection.clear(); // 取消之前选中的物体
					setMouseState(MOUSE_STATE_RECTANGLING); // 普通拖框
				}
			}
		}
		scrollablePanel.repaint();
	}

	protected void selectPointMouseRightPressed(MouseEvent e) {
		switch (mouseState) {
		case MOUSE_STATE_RECTANGLING:
			setMouseState(MOUSE_STATE_NORMAL);
			scrollablePanel.repaint();
			break;
		case MOUSE_STATE_DRAGGING:
			setMouseState(MOUSE_STATE_NORMAL);
			if (!selection.isEmpty()) {
				// selection.clear();
				scrollablePanel.repaint();
			}
			break;
		case MOUSE_STATE_NORMAL:
			setMouseState(MOUSE_STATE_SELECT_POINT_RIGHT_PRESSED);
			Shape shape = selection.getTopShapeAtPoint(e.getX(), e.getY());
			if (shape == null) {
				selection.clear();
				scrollablePanel.repaint();
			}
			break;
		default:
			break;
		}
	}

	protected void otherMouseRightPressed(MouseEvent e) {
		boolean shapeChanged = false;
		if (!selection.isEmpty()) {
			selection.clear();
		}
		mouseInfo.resetAll();
		setMouseState(MOUSE_STATE_NORMAL);
		if (shapeChanged) {
			resortShapes();
		}
		scrollablePanel.repaint();
	}

	protected void selectPointMouseDragged(MouseEvent e) {
		switch (mouseState) {
		case MOUSE_STATE_DRAGGING:
			selection.dragTo(mouseX, mouseY);
			scrollablePanel.repaint();
			break;
		case MOUSE_STATE_RECTANGLING:
			scrollablePanel.repaint();
			break;
		default:
			break;
		}
	}

	protected void selectPointMouseLeftReleased(MouseEvent e) {
		switch (mouseState) {
		case MOUSE_STATE_DRAGGING:
			selection.confirmDrag();
			// resortShapes();
			scrollablePanel.repaint();
			break;
		case MOUSE_STATE_RECTANGLING:
			selection.selectShapes(getAllShapesInRect(Math.min(oldMouseX, mouseX), Math.min(
			        oldMouseY, mouseY), Math.abs(mouseX - oldMouseX), Math.abs(mouseY - oldMouseY)));
			scrollablePanel.repaint();
			break;
		default:
			break;
		}
		setMouseState(MOUSE_STATE_NORMAL);
	}

	protected void selectPointMouseRightReleased(MouseEvent e) {
		switch (mouseState) {
		case MOUSE_STATE_SELECT_POINT_RIGHT_PRESSED: // can select
			if (selection.isEmpty()) {
				Shape[] shapes = getAllShapesAtPoint(e.getX(), e.getY());
				if (shapes != null) {
					if (shapes.length == 1) {
						selection.selectShape(shapes[0]);
						scrollablePanel.repaint();
						showPopupMenu(shapes[0], e.getX(), e.getY());
					}
					else if (shapes.length > 1) {
						showPopupMenu(shapes, e.getX(), e.getY());
					}
				}
			}
			else {
				Shape shape = selection.getTopShapeAtPoint(e.getX(), e.getY());
				if (shape != null) {
					showPopupMenu(shape, e.getX(), e.getY());
				}
			}
			break;
		default:
			break;
		}
		setMouseState(MOUSE_STATE_NORMAL);
	}

	protected void selectPointMouseDoubleClick(MouseEvent e) {
		switch (mouseState) {
		case MOUSE_STATE_NORMAL:
			Shape shape = selection.getTopShapeAtPoint(e.getX(), e.getY());
			if (shape != null) {
				showProperties(shape);
			}
			break;
		default:
			break;
		}
	}

	protected void newShapeMouseLeftPressed(MouseEvent e) {
		Painter painter = mouseInfo.getPainter();
		if (painter != null) {
			Shape newShape = addNewShape(mouseX, mouseY);
			if (newShape != null) {
				selection.selectShape(newShape);
				selection.startCreatingDrag(mouseX, mouseY);
				setMouseState(MOUSE_STATE_DRAGGING);
				mouseInfo.resetAll();
				scrollablePanel.repaint();
			}
		}
	}

	protected void selectPointMouseMoved(MouseEvent e) {
		if (!selection.isEmpty()) {
			scrollablePanel.setCursor(selection.getCursor(e.getX(), e.getY()));
		}
	}

	protected void newShapeMouseMoved(MouseEvent e) {
		scrollablePanel.repaint();
	}

	protected void copyShapes(Shape[] shapes) {
		ArrayList tmp = new ArrayList();
		if (shapes != null) {
			for (int i = 0; i < shapes.length; ++i) {
				if (shapes[i] != null) {
					if (shapes[i] instanceof Copyable) {
						tmp.add(((Copyable) (shapes[i])).copy());
					}
				}
			}
		}
		if (tmp.size() > 0) {
			copiedShapes.clear();
			for (int i = 0; i < tmp.size(); ++i) {
				copiedShapes.add(tmp.get(i));
			}
		}
	}

	protected void pasteShapes(int x, int y) {
		if (copiedShapes.size() > 0) {
			ArrayList tmp = new ArrayList();
			int left = 0;
			int top = 0;
			int right = 0;
			int bottom = 0;

			boolean init = false;
			for (int i = 0; i < copiedShapes.size(); ++i) {
				Shape shape = (Shape) (copiedShapes.get(i));
				if (shape instanceof Copyable) {
					if (!init) {
						left = shape.getCenterX();
						top = shape.getCenterY();
						right = shape.getCenterX();
						bottom = shape.getCenterY();
						init = true;
					}
					else {
						if (left > shape.getCenterX()) {
							left = shape.getCenterX();
						}
						if (top > shape.getCenterY()) {
							top = shape.getCenterY();
						}
						if (right < shape.getCenterX()) {
							right = shape.getCenterX();
						}
						if (bottom < shape.getCenterY()) {
							bottom = shape.getCenterY();
						}
					}
					tmp.add(((Copyable) shape).copy());
				}
			}

			if (tmp.size() > 0) {
				Shape[] shapes = new Shape[tmp.size()];
				for (int i = 0; i < tmp.size(); ++i) {
					Shape shape = (Shape) (tmp.get(i));
					shape.setID(useMaxID());
					shape.move(mouseX - (left + right) / 2, mouseY - (top + bottom) / 2);
					addShape(shape);
					shapes[i] = shape;
				}
				selection.selectShapes(shapes);
				scrollablePanel.repaint();
			}
		}
	}

	protected void keyUpPressed(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {

			if (!selection.isEmpty() && moveable) {
				selection.move(0, -1); // move up
				scrollablePanel.repaint();
			}
		}
		else if (layerable && e.isControlDown() && !selection.isEmpty()) {
			increaseLayer(selection.getShapes(), 1);
		}
	}

	protected void keyDownPressed(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {

			if (!selection.isEmpty() && moveable) {
				selection.move(0, 1); // move down
				scrollablePanel.repaint();
			}
		}
		else if (layerable && e.isControlDown() && !selection.isEmpty()) {
			increaseLayer(selection.getShapes(), -1);
		}
	}

	protected void keyLeftPressed(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {

			if (!selection.isEmpty() && moveable) {
				selection.move(-1, 0); // move left
				scrollablePanel.repaint();
			}
		}
	}

	protected void keyRightPressed(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {

			if (!selection.isEmpty() && moveable) {
				selection.move(1, 0); // move right
				scrollablePanel.repaint();
			}
		}
	}

	protected void setShapesToTop(Shape[] shapes) {
		int minLayer = getMinLayer(shapes);
		int maxLayer = getAllShapesMaxLayer();

		increaseLayer(shapes, maxLayer - minLayer + 1);
	}

	protected void setShapesToBottom(Shape[] shapes) {
		int minLayer = getAllShapesMinLayer();
		int maxLayer = getMaxLayer(shapes);

		increaseLayer(shapes, minLayer - maxLayer - 1);
	}

	protected int getAllShapesMinLayer() {
		int minLayer = 0;
		boolean first = true;
		for (int i = 0; i < shapes.size(); ++i) {
			if (shapes.get(i) instanceof Layerable) {
				Layerable tmp = (Layerable) (shapes.get(i));
				if (first) {
					minLayer = tmp.getLayer();
					first = false;
				}
				else {
					if (minLayer > tmp.getLayer()) {
						minLayer = tmp.getLayer();
					}
				}
			}
		}
		return minLayer;
	}

	protected int getAllShapesMaxLayer() {
		int maxLayer = 0;
		boolean first = true;
		for (int i = 0; i < shapes.size(); ++i) {
			if (shapes.get(i) instanceof Layerable) {
				Layerable tmp = (Layerable) (shapes.get(i));
				if (first) {
					maxLayer = tmp.getLayer();
					first = false;
				}
				else {
					if (maxLayer < tmp.getLayer()) {
						maxLayer = tmp.getLayer();
					}
				}
			}
		}
		return maxLayer;
	}

	protected int getMinLayer(Shape[] shapes) {
		int minLayer = 0;
		boolean first = true;
		if (shapes != null) {
			for (int i = 0; i < shapes.length; ++i) {
				if (shapes[i] instanceof Layerable) {
					Layerable tmp = (Layerable) (shapes[i]);
					if (first) {
						minLayer = tmp.getLayer();
						first = false;
					}
					else {
						if (minLayer > tmp.getLayer()) {
							minLayer = tmp.getLayer();
						}
					}
				}
			}
		}
		return minLayer;
	}

	protected int getMaxLayer(Shape[] shapes) {
		int maxLayer = 0;
		boolean first = true;
		if (shapes != null) {
			for (int i = 0; i < shapes.length; ++i) {
				if (shapes[i] instanceof Layerable) {
					Layerable tmp = (Layerable) (shapes[i]);
					if (first) {
						maxLayer = tmp.getLayer();
						first = false;
					}
					else {
						if (maxLayer < tmp.getLayer()) {
							maxLayer = tmp.getLayer();
						}
					}
				}
			}
		}
		return maxLayer;
	}

	protected void increaseLayer(Shape[] shapes, int offsetLayer) {
		if (shapes == null || offsetLayer == 0) { return; }
		for (int i = 0; i < shapes.length; ++i) {
			if (shapes[i] instanceof Layerable) {
				Layerable tmp = (Layerable) (shapes[i]);
				tmp.setLayer(tmp.getLayer() + offsetLayer);
			}
		}
		resortShapes();
		scrollablePanel.repaint();
		selectionChanged();
	}

	protected void keyDeleteReleased() {
		if (!selection.isEmpty() && mouseInfo.getInfo() == MouseInfo.SELECT_POINT
		        && mouseState == MOUSE_STATE_NORMAL) {

			Shape[] shapes = selection.getShapes();
			for (int i = 0; i < shapes.length; ++i) {
				removeShape(shapes[i]);
			}
			selection.clear();
			scrollablePanel.repaint();
		}
	}

	protected void showPopupMenu(Shape[] shapes, int x, int y) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem menu;
		for (int i = shapes.length - 1; i >= 0; --i) {
			menu = new JMenuItem(shapes[i].getName());
			menu.addActionListener(new SelectShapeActionListener(shapes[i]));
			popup.add(menu);
		}
		popup.show(scrollablePanel, x, y);
	}

	private class SelectShapeActionListener implements ActionListener {

		Shape shape;

		public SelectShapeActionListener(Shape shape) {
			this.shape = shape;
		}

		public void actionPerformed(ActionEvent e) {
			selection.selectShape(shape);
			scrollablePanel.repaint();
		}
	}

	public void paintShape(Shape shape, Graphics g) {
		if (shape != null) {
			shape.paint(g);
		}
	}

	public void paintShapes(Graphics g) {
		paintShapes(shapes, g);
	}

	public void paintShapes(ArrayList shapes, Graphics g) {
		if (shapes == null) { return; }
		for (int i = 0; i < shapes.size(); ++i) {
			Shape shape = (Shape) (shapes.get(i));
			paintShape(shape, g);
		}
	}

	public void paintStatic(Graphics g) {
		paintStatic(shapes, g);
	}

	public void paintStatic(ArrayList shapes, Graphics g) {
		if (shapes == null) { return; }
		for (int i = 0; i < shapes.size(); ++i) {
			Shape shape = (Shape) (shapes.get(i));
			shape.paintIdle(g);
		}
	}

	public void paintOthers(Graphics g) {
		if (scrollablePanel.isMouseMovingViewport()) {
			paintMouseMovingViewport(g);
		}
		else {
			mouseInfo.paint(g, mouseX, mouseY);
			switch (mouseState) {
			case MOUSE_STATE_RECTANGLING:
				paintRectangling(g);
				break;
			default:
				break;
			}
		}
	}

	protected void paintMouseMovingViewport(Graphics g) { // do sth like
															// special mouse
															// icon
	}

	protected void paintRectangling(Graphics g) {
		Color oldColor = g.getColor();
		g.setColor(Color.WHITE);
		g.drawRect(Math.min(oldMouseX, mouseX), Math.min(oldMouseY, mouseY), Math.abs(mouseX
		        - oldMouseX), Math.abs(mouseY - oldMouseY));
		g.setColor(oldColor);
	}

	protected void showPopupMenu(Shape shape, int x, int y) {}

	protected void showProperties(Shape shape) {}

	public String getSelectionInfo() {
		String result = null;
		Shape[] shapes = selection.getShapes();
		if (shapes != null) {
			if (shapes.length == 1) {
				result = shapes[0].getInfo();
			}
			else if (shapes.length > 1) {
				result = "一共选取了" + shapes.length + "个物体";
			}
		}
		return result;
	}

	protected void selectionChanged() {
		for (int i = 0; i < listeners.size(); ++i) {
			((ManagerListener) (listeners.get(i))).selectionChanged();
		}
	}

	protected void mouseChanged() {
		for (int i = 0; i < listeners.size(); ++i) {
			((ManagerListener) (listeners.get(i))).mouseChanged();
		}
	}

	protected void keyChanged() {
		for (int i = 0; i < listeners.size(); ++i) {
			((ManagerListener) (listeners.get(i))).keyChanged();
		}
	}

	abstract public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception;

	abstract public void save(DataOutputStream out, Object[] resManagers) throws Exception;

	abstract public void load(DataInputStream in, Object[] resManagers) throws Exception;

}

abstract class ShapeManagerPanel extends ScrollablePanel implements ManagerPanel {

	protected JDialog dlgOwner;
	protected JFrame frmOwner;
	protected ShapeManager manager;
	protected MouseInfo mouseInfo;
	protected ArrayList backManagers;
	protected ArrayList foreManagers;

	public ShapeManagerPanel(JFrame owner, MouseInfo mouseInfo) {
		super(XUtil.getDefPropInt("DefMapWidth"), XUtil.getDefPropInt("DefMapHeight"));
		init(owner, null, mouseInfo);
	}

	public ShapeManagerPanel(JDialog owner, MouseInfo mouseInfo) {
		super(XUtil.getDefPropInt("DefMapWidth"), XUtil.getDefPropInt("DefMapHeight"));
		init(null, owner, mouseInfo);
	}

	private void init(JFrame frmOwner, JDialog dlgOwner, MouseInfo mouseInfo) {
		this.frmOwner = frmOwner;
		this.dlgOwner = dlgOwner;
		this.mouseInfo = mouseInfo;
		this.manager = createManager();
		backManagers = new ArrayList();
		foreManagers = new ArrayList();
	}

	abstract protected ShapeManager createManager();
	
	public JFrame getFrameOwner() {
		return frmOwner;
	}
	
	public JDialog getDialogOwner() {
		return dlgOwner;
	}

	public void setMapSize(int basicWidth, int basicHeight) {
		super.reset(basicWidth, basicHeight);
	}

	public void reset(int basicWidth, int basicHeight) {
		super.reset(basicWidth, basicHeight);
		manager.reset();
	}

	public Manager getManager() {
		return manager;
	}

	public void addBackManager(Manager manager) {
		if (manager == null) { return; }
		for (int i = 0; i < backManagers.size(); ++i) {
			if (backManagers.get(i).equals(manager)) { return; }
		}
		backManagers.add(manager);
	}

	public void removeBackManager(Manager manager) {
		if (manager == null) { return; }
		backManagers.remove(manager);
	}

	public void addForeManager(Manager manager) {
		if (manager == null) { return; }
		for (int i = 0; i < foreManagers.size(); ++i) {
			if (foreManagers.get(i).equals(manager)) { return; }
		}
		foreManagers.add(manager);
	}

	public void removeForeManager(Manager manager) {
		if (manager == null) { return; }
		foreManagers.remove(manager);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!isVisible()) { return; }

		paintBack(g);
		paintSelf(g);
		paintFore(g);
	}

	protected void paintBack(Graphics g) {
		paintBackManagers(g);
	}

	protected void paintBackManagers(Graphics g) {
		for (int i = 0; i < backManagers.size(); ++i) {
			((Manager) (backManagers.get(i))).paintStatic(g);
		}
	}

	protected void paintSelf(Graphics g) {
		manager.paintShapes(g);
		manager.paintOthers(g);
	}

	protected void paintFore(Graphics g) {
		paintForeManagers(g);
	}

	protected void paintForeManagers(Graphics g) {
		for (int i = 0; i < foreManagers.size(); ++i) {
			((Manager) (foreManagers.get(i))).paintStatic(g);
		}
	}

	public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception {
		manager.saveMobile(out, resManagers);
	}

	public void save(DataOutputStream out, Object[] resManagers) throws Exception {
		manager.save(out, resManagers);
	}

	public void load(DataInputStream in, Object[] resManagers) throws Exception {
		manager.load(in, resManagers);
		repaint();
	}
}