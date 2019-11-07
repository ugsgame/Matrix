package editor;

import java.awt.Color;
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
 * 管理Sprite的选择、添加、移动和删除的管理器。
 */
abstract public class SpriteManager implements Manager {

	private class SelectSpriteActionListener implements ActionListener {

		Sprite sprite;

		public SelectSpriteActionListener(Sprite sprite) {
			this.sprite = sprite;
		}

		public void actionPerformed(ActionEvent e) {
			selection.selectSprite(sprite);
			scrollablePanel.repaint();
		}
	}

	// 鼠标状态
	protected final static int MOUSE_STATE_NORMAL = 0;
	protected final static int MOUSE_STATE_DRAGGING_SPRITE = 1;
	protected final static int MOUSE_STATE_RECTANGLING = 2; // 鼠标拉框
	protected final static int MOUSE_STATE_CTRL_RECTING = 3; // 按住Ctrl键鼠标拉框
	protected final static int MOUSE_STATE_SELECT_POINT_RIGHT_PRESSED = 4; // 在SelectPoint状态按下鼠标右键的标志
	protected final static int MOUSE_STATE_NEW_SPRITE_LEFT_PRESSED = 5; // 在NewSprite状态按下鼠标左键
	// 选择状态
	public final static int NONE_SELECTION = 0;
	public final static int SINGLE_SELECTION = 1;
	public final static int MULTI_SELECTION = 2;
	// 新加节点状态
	public final static int NONE_NEWSPRITE = 0;
	public final static int SINGLE_NEWSPRITE = 1;
	public final static int MULTI_NEWSPRITE = 2;

	public final static int[][] KEY_CODES = { { KeyEvent.VK_UP, 0x00000001, KeyEvent.VK_DOWN },
	        { KeyEvent.VK_DOWN, 0x00000002, KeyEvent.VK_UP },
	        { KeyEvent.VK_LEFT, 0x00000004, KeyEvent.VK_RIGHT },
	        { KeyEvent.VK_RIGHT, 0x00000008, KeyEvent.VK_LEFT } };
	protected static ArrayList newspritePoints;
	protected static ArrayList copiedSprites;
	protected ScrollablePanel scrollablePanel;
	protected MouseInfo mouseInfo;
	protected ArrayList sprites;
	protected SpriteSelection selection;
	protected SpriteUndoManager undoManager;
	protected boolean undoable;
	protected boolean moveable;
	protected boolean deleteable;
	protected boolean copyable;
	protected boolean layerable;
	protected boolean tooltipable;
	
	protected boolean rdown;
	protected boolean sdown;

	protected int maxID;
	protected int mouseState;
	protected int oldMouseX;
	protected int oldMouseY;
	protected int mouseX;
	protected int mouseY;
	protected int pressedKey;
	protected int keyMoveSpriteX;
	protected int keyMoveSpriteY;
	protected int newSpriteX;
	protected int newSpriteY;
	protected int selectionMode;
	protected int newspriteMode;
	
	private ArrayList listeners;

	public SpriteManager(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) {
		try {
			init(scrollablePanel, mouseInfo);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addListener(ManagerListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	protected boolean addNewSprite(int x, int y) {
		Sprite sprite = createSprite(x, y);
		if (sprite == null) { return false; }
		return addUndoableSprite(sprite);
	}

	protected boolean addSprite(Sprite sprite) {
		if (getSpriteIndex(sprite) < 0) {
			if (sprites.add(sprite)) {
				if (maxID <= sprite.getID()) {
					maxID = sprite.getID() + 1;
				}
				return true;
			}
		}
		return false;
	}

	protected boolean addUndoableSprite(Sprite sprite) {
		Sprite[] tmp = new Sprite[1];
		tmp[0] = sprite;
		return addUndoableSprites(tmp);
	}

	protected boolean addUndoableSprites(Sprite[] sprites) {
		ArrayList tmp = new ArrayList();
		for (int i = 0; i < sprites.length; ++i) {
			if (addSprite(sprites[i])) {
				tmp.add(sprites[i]);
			}
		}
		if (tmp.size() > 0) {
			Sprite[] undoArray = new Sprite[tmp.size()];
			for (int i = 0; i < tmp.size(); ++i) {
				undoArray[i] = (Sprite) (tmp.get(i));
			}
			undoManager.addUndoSpriteAdd(undoArray);
			resortSprites();
			return true;
		}
		else {
			return false;
		}
	}

	protected void copySprites(Sprite[] sprites) {
		ArrayList tmp = new ArrayList();
		if (sprites != null) {
			for (int i = 0; i < sprites.length; ++i) {
				if (sprites[i] != null) {
					if (sprites[i] instanceof Copyable) {
						tmp.add(((Copyable) (sprites[i])).copy());
					}
				}
			}
		}
		if (tmp.size() > 0) {
			copiedSprites.clear();
			for (int i = 0; i < tmp.size(); ++i) {
				copiedSprites.add(tmp.get(i));
			}
		}
	}

	protected Sprite createSprite(int x, int y) {
		return null;
	}

	protected Sprite findSprite(int id) {
		Sprite tmp;
		for (int i = 0; i < sprites.size(); ++i) {
			tmp = (Sprite) (sprites.get(i));
			if (tmp.getID() == id) { return tmp; }
		}
		return null;
	}

	public void flip() {
		if (!selection.isEmpty()) {
			flipUndoableSprites(selection.getSprites());
		}
	}

	public void flipSprites(Sprite[] sprites) {
		if (sprites != null) {
			for (int i = 0; i < sprites.length; ++i) {
				if (sprites[i] instanceof Flipable) {
					((Flipable) sprites[i]).flip();
				}
			}
		}
		spriteChanged();
		scrollablePanel.repaint();
	}

	protected void flipUndoableSprites(Sprite[] sprites) {
		flipSprites(sprites);
		undoManager.addUndoSpriteFlip(sprites);
	}

	protected Sprite[] getAllSpritesAtPoint(int x, int y) {
		Sprite sprite;
		ArrayList tmp = new ArrayList();

		for (int i = 0; i < sprites.size(); ++i) {
			sprite = (Sprite) (sprites.get(i));
			if (sprite.containPoint(x, y)) {
				tmp.add(sprite);
			}
		}

		Sprite[] result = new Sprite[tmp.size()];
		for (int i = 0; i < tmp.size(); ++i) {
			result[i] = (Sprite) (tmp.get(i));
		}
		return result;
	}

	protected Sprite[] getAllSpritesInRect(int x, int y, int w, int h) {
		Sprite sprite;
		ArrayList tmp = new ArrayList();

		for (int i = 0; i < sprites.size(); ++i) {
			sprite = (Sprite) (sprites.get(i));
			if (sprite.inRect(x, y, w, h)) {
				tmp.add(sprite);
			}
		}

		Sprite[] result = new Sprite[tmp.size()];
		for (int i = 0; i < tmp.size(); ++i) {
			result[i] = (Sprite) (tmp.get(i));
		}
		return result;
	}

	protected int getAllSpritesMaxLayer() {
		int maxLayer = 0;
		boolean first = true;
		for (int i = 0; i < sprites.size(); ++i) {
			if (sprites.get(i) instanceof Layerable) {
				Layerable tmp = (Layerable) (sprites.get(i));
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

	protected int getAllSpritesMinLayer() {
		int minLayer = 0;
		boolean first = true;
		for (int i = 0; i < sprites.size(); ++i) {
			if (sprites.get(i) instanceof Layerable) {
				Layerable tmp = (Layerable) (sprites.get(i));
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

	protected int getKeyID(int keyCode) {
		for (int i = 0; i < KEY_CODES.length; ++i) {
			if (keyCode == KEY_CODES[i][0]) { return i; }
		}
		return -1;
	}

	protected int getMaxLayer(Sprite[] sprites) {
		int maxLayer = 0;
		boolean first = true;
		if (sprites != null) {
			for (int i = 0; i < sprites.length; ++i) {
				if (sprites[i] instanceof Layerable) {
					Layerable tmp = (Layerable) (sprites[i]);
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

	protected int getMinLayer(Sprite[] sprites) {
		int minLayer = 0;
		boolean first = true;
		if (sprites != null) {
			for (int i = 0; i < sprites.length; ++i) {
				if (sprites[i] instanceof Layerable) {
					Layerable tmp = (Layerable) (sprites[i]);
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

	public int getMouseX() {
		return mouseX;
	}

	protected int getMouseX(MouseEvent e) {
		return scrollablePanel.getMouseX(e);
	}

	public int getMouseY() {
		return mouseY;
	}

	protected int getMouseY(MouseEvent e) {
		return scrollablePanel.getMouseY(e);
	}

	public ScrollablePanel getScrollablePanel() {
		return scrollablePanel;
	}

	public SpriteSelection getSelection() {
		return selection;
	}

	public String getSelectionInfo() {
		String result = null;
		Sprite[] sprites = selection.getSprites();
		if (sprites != null) {
			if (sprites.length == 1) {
				result = sprites[0].getInfo();
			}
			else if (sprites.length > 1) {
				result = "一共选取了" + sprites.length + "个物体";
			}
		}
		return result;
	}

	public Sprite getSprite(int spriteID) {
		Sprite result = null;
		for (int i = 0; i < sprites.size(); ++i) {
			if (((Sprite) (sprites.get(i))).getID() == spriteID) {
				result = (Sprite) (sprites.get(i));
				break;
			}
		}
		return result;
	}

	protected int getSpriteIndex(Sprite sprite) {
		if (sprite != null) {
			Sprite tmp;
			for (int i = 0; i < sprites.size(); ++i) {
				tmp = (Sprite) (sprites.get(i));
				if (tmp.getID() == sprite.getID()) { return i; }
			}
		}
		return -1;
	}

	public Sprite[] getSprites() {
		Sprite[] result = new Sprite[sprites.size()];
		for (int i = 0; i < sprites.size(); ++i) {
			result[i] = (Sprite) (sprites.get(i));
		}
		return result;
	}
	
	public ArrayList getRealSprites() {
		return sprites;
	}

	protected String getToolTipText(Sprite sprite) {
		if (sprite == null) {
			return null;
		}
		else {
			return sprite.getName();
		}
	}

	protected Sprite getTopSpriteAtPoint(int x, int y) {
		SISprite sprite;
		int layer = MainFrame.self.getMouseInfo().getLayer();
		for (int i = sprites.size() - 1; i >= 0; --i) {
			sprite = (SISprite) (sprites.get(i));
			if (sprite.containPoint(x, y) && sprite.getLayer2() == layer) { return sprite; }
		}
		return null;
	}

	public SpriteUndoManager getUndoManager() {
		return undoManager;
	}

	protected void increaseLayer(Sprite[] sprites, int offsetLayer) {
		if (sprites == null || offsetLayer == 0) { return; }
		for (int i = 0; i < sprites.length; ++i) {
			if (sprites[i] instanceof Layerable) {
				Layerable tmp = (Layerable) (sprites[i]);
				tmp.setLayer(tmp.getLayer() + offsetLayer);
			}
		}
		resortSprites();
		scrollablePanel.repaint();
		selectionChanged();
	}

	private void init(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) throws Exception {
		this.scrollablePanel = scrollablePanel;
		this.mouseInfo = mouseInfo;

		sprites = new ArrayList();
		newspritePoints = new ArrayList();
		copiedSprites = new ArrayList();
		listeners = new ArrayList();

		this.selection = new SpriteSelection(this);
		this.undoManager = new SpriteUndoManager(this);

		selectionMode = MULTI_SELECTION;
		newspriteMode = MULTI_NEWSPRITE;
		undoable = true;
		moveable = true;
		deleteable = true;
		copyable = true;
		layerable = true;
		tooltipable = false;

		initReset();
		newspritePoints.clear();
		copiedSprites.clear();

		scrollablePanel.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				selfMouseClicked(e);
			}

			public void mouseEntered(MouseEvent e) {
				selfMouseEntered(e);
			}

			public void mouseExited(MouseEvent e) {
				selfMouseExited(e);
			}

			public void mousePressed(MouseEvent e) {
				selfMousePressed(e);
			}

			public void mouseReleased(MouseEvent e) {
				selfMouseReleased(e);
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

			public void keyPressed(KeyEvent e) {
				selfKeyPressed(e);
			}

			public void keyReleased(KeyEvent e) {
				selfKeyReleased(e);
			}

			public void keyTyped(KeyEvent e) {
				selfKeyTyped(e);
			}
		});
	}

	private void initReset() {
		maxID = 0;
		mouseState = MOUSE_STATE_NORMAL;
		pressedKey = 0;
		keyMoveSpriteX = 0;
		keyMoveSpriteY = 0;
		
		sprites.clear();
		//newspritePoints.clear();
		//copiedSprites.clear();

		if (selection != null) {
			selection.clear();
		}
		if (undoManager != null) {
			undoManager.discardAllEdits();
		}
	}
	
	public void clearSelection() {
		if (selection != null) {
			selection.clear();
		}
	}
	
	public void setSelectionLayer(int layer) {
		if (selection != null) {
			selection.setNowLayer(layer);
		}
	}

	protected boolean isKeyPressed(int keyCode) {
		int keyID = getKeyID(keyCode);
		if (keyID >= 0) { return (pressedKey & KEY_CODES[keyID][1]) != 0; }
		return false;
	}

	protected void keyChanged() {
		for (int i = 0; i < listeners.size(); ++i) {
			((ManagerListener) (listeners.get(i))).keyChanged();
		}
	}

	protected void keyDeleteReleased() {
		if (!selection.isEmpty() && mouseInfo.getInfo() == MouseInfo.SELECT_POINT
		        && mouseState == MOUSE_STATE_NORMAL) {

			selection.cancelMoving();
			removeUndoableSprites(selection.getSprites());
			selection.clear();
			scrollablePanel.repaint();
		}
	}

	protected void keyDownPressed(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {

			if (!selection.isEmpty() && moveable) {
				System.out.println("keyDownPressed moving");
				selection.moving(keyMoveSpriteX, ++keyMoveSpriteY); // move down
				scrollablePanel.repaint();
			}
		}
		else if (layerable && e.isControlDown() && !selection.isEmpty()) {
			increaseLayer(selection.getSprites(), -1);
		}
	}

	protected void keyDownReleased(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {

			if (!selection.isEmpty() && moveable) {
				keyMoveSpriteConfirm();
			}
		}
	}

	protected void keyLeftPressed(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {

			if (!selection.isEmpty() && moveable) {
				
				selection.moving(--keyMoveSpriteX, keyMoveSpriteY); // move left
				scrollablePanel.repaint();
			}
		}
	}

	protected void keyLeftReleased(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {

			if (!selection.isEmpty() && moveable) {
				keyMoveSpriteConfirm();
			}
		}
	}

	protected void keyMoveSpriteConfirm() {
		if (!isKeyPressed(KeyEvent.VK_UP) && !isKeyPressed(KeyEvent.VK_DOWN)
		        && !isKeyPressed(KeyEvent.VK_LEFT) && !isKeyPressed(KeyEvent.VK_RIGHT)) {

			keyMoveSpriteX = keyMoveSpriteY = 0;
			if (!selection.isEmpty()) {
				selection.confirmMoving();
				resortSprites();
				scrollablePanel.repaint();
			}
		}
	}
	
	public void keyShiftPressed(KeyEvent e)
	{
		//System.out.println("keyShiftPressed()");
		rdown = true;
		if(e.isShiftDown())
			selection.resetRange();
		else
			this.selection.shiftPressed();
		scrollablePanel.repaint();
	}
	
	public void keySPressed(KeyEvent e)
	{
		//System.out.println("keyShiftPressed()");
		if(!e.isControlDown())
		{
			sdown = true;
			if(e.isShiftDown())
				selection.resetScale();
			else
				this.selection.Spressed();
			scrollablePanel.repaint();
		}
	}
	
	protected void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_R:
			keyShiftPressed(e);
			//System.out.println("ctrl down");
			break;
		case KeyEvent.VK_S:
			keySPressed(e);
			break;
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
		case KeyEvent.VK_Z:
			if (undoable && e.isControlDown()) {
				undoManager.undo();
			}
			break;
		case KeyEvent.VK_Y:
			if (undoable && e.isControlDown()) {
				undoManager.redo();
			}
			break;
		case KeyEvent.VK_A:
			if (e.isControlDown()) {
				selection.selectSprites(getSprites());
				scrollablePanel.repaint();
			}
			break;
		case KeyEvent.VK_C:
			if (copyable && e.isControlDown() && !selection.isEmpty()) {
				copySprites(selection.getSprites());
			}
			break;
		case KeyEvent.VK_V:
			if (copyable && e.isControlDown()) {
				if (scrollablePanel.isMouseIn()) {
					pasteSprites(mouseX, mouseY);
				}
				else {
					pasteSprites(scrollablePanel.getBasicWidth() / 2,
					        scrollablePanel.getBasicHeight() / 2);
				}
			}
			break;
		case KeyEvent.VK_ESCAPE:
			selection.cancelMoving();
			selection.clear();
			mouseInfo.resetAll();
			scrollablePanel.repaint();
			break;
		default:
			break;
		}
	}
	
	public void keyShiftReleased()
	{
		//System.out.println("shiftrelease");
		rdown = false;
		this.selection.shiftrelease();
		scrollablePanel.repaint();
	}

	public void keySReleased()
	{
		//System.out.println("S release");
		sdown = false;
		this.selection.Srelease();
		scrollablePanel.repaint();
	}
	
	protected void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_R:
			keyShiftReleased();
			break;
		case KeyEvent.VK_S:
			keySReleased();
			break;
		case KeyEvent.VK_UP:
			keyUpReleased(e);
			break;
		case KeyEvent.VK_DOWN:
			keyDownReleased(e);
			break;
		case KeyEvent.VK_LEFT:
			keyLeftReleased(e);
			break;
		case KeyEvent.VK_RIGHT:
			keyRightReleased(e);
			break;
		case KeyEvent.VK_DELETE:
			if (deleteable) {
				keyDeleteReleased();
			}
			break;
		case KeyEvent.VK_HOME:
			if (layerable && e.isControlDown() && !selection.isEmpty()) {
				setSpritesToTop(selection.getSprites());
			}
			break;
		case KeyEvent.VK_END:
			if (layerable && e.isControlDown() && !selection.isEmpty()) {
				setSpritesToBottom(selection.getSprites());
			}
			break;
		default:
			break;
		}
	}

	protected void keyRightPressed(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {

			if (!selection.isEmpty() && moveable) {
				selection.moving(++keyMoveSpriteX, keyMoveSpriteY); // move
				// right
				scrollablePanel.repaint();
			}
		}
	}

	protected void keyRightReleased(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {

			if (!selection.isEmpty() && moveable) {
				keyMoveSpriteConfirm();
			}
		}
	}

	protected void keyUpPressed(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {

			if (!selection.isEmpty() && moveable) {
				selection.moving(keyMoveSpriteX, --keyMoveSpriteY); // move up
				scrollablePanel.repaint();
			}
		}
		else if (layerable && e.isControlDown() && !selection.isEmpty()) {
			increaseLayer(selection.getSprites(), 1);
		}
	}

	protected void keyUpReleased(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {

			if (!selection.isEmpty() && moveable) {
				keyMoveSpriteConfirm();
			}
		}
	}

	abstract public void load(DataInputStream in, Object[] resManagers) throws Exception;

	protected void mouseChanged(MouseEvent e) {
		for (int i = 0; i < listeners.size(); ++i) {
			((ManagerListener) (listeners.get(i))).mouseChanged(e);
		}
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

	protected void mouseDragged(MouseEvent e) {
		switch (mouseInfo.getInfo()) {
		case MouseInfo.SELECT_POINT:
			selectPointMouseDragged(e);
			break;
		case MouseInfo.NEW_SPRITE:
			newSpriteMouseDragged(e);
			break;
		default:
			break;
		}
	}

	protected void mouseEntered(MouseEvent e) {}

	protected void mouseExited(MouseEvent e) {}

	protected void mouseMoved(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.NOBUTTON:
			switch (mouseInfo.getInfo()) {
			case MouseInfo.SELECT_POINT:
				selectPointMouseMoved(e);
				break;
			case MouseInfo.NEW_SPRITE:
				newSpriteMouseMoved(e);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	protected void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case XUtil.LEFT_BUTTON:
			switch (mouseInfo.getInfo()) {
			case MouseInfo.SELECT_POINT:
				//System.out.println("MouseInfo.SELECT_POINT");
				selectPointMouseLeftPressed(e);
				break;
			case MouseInfo.NEW_SPRITE:
				newSpriteMouseLeftPressed(e);
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

	protected void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case XUtil.LEFT_BUTTON:
			switch (mouseInfo.getInfo()) {
			case MouseInfo.SELECT_POINT:
				selectPointMouseLeftReleased(e);
				break;
			case MouseInfo.NEW_SPRITE:
				newSpriteMouseLeftReleased(e);
				break;
			default:
				break;
			}
			break;
		case XUtil.RIGHT_BUTTON:
			switch (mouseInfo.getInfo()) {
			case MouseInfo.SELECT_POINT:
				selectPointMouseRightReleased(e.getX(), e.getY());
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	protected void multiSelectionMouseLeftPressed(MouseEvent e) {
		if (selection.pointInSprites(mouseX, mouseY)) { // 按键点在已选择的物体内
			if (e.isControlDown()) { // 按了Ctrl键
				selection.click(mouseX, mouseY, KeyEvent.VK_CONTROL); // 取消该物体的选择
			}
			else if(rdown)
			{   
				//System.out.println("按下shift");
				selection.click(mouseX, mouseY, KeyEvent.VK_R);
			}
			else if(sdown)
			{
				selection.click(mouseX, mouseY, KeyEvent.VK_S);
			}
			else { // 没有按Ctrl键
				selection.click(mouseX, mouseY); // 无反应
				setMouseState(MOUSE_STATE_DRAGGING_SPRITE); // 可以拖动物体了
			}
		}
		else { // 按键点不在已选的物体内
			Sprite sprite = getTopSpriteAtPoint(mouseX, mouseY); // 获得最上方的物体
			if (sprite != null) { // 选中物体
				if (e.isControlDown()) { // 按了Ctrl键
					selection.addSprite(sprite); // 将选中的物体加入到selection中
					selection.click(mouseX, mouseY); // 无反应
				}
				else if(rdown)
				{
					//System.out.println("按下shift");
					selection.click(mouseX, mouseY, KeyEvent.VK_R);
				}
				else if(sdown)
				{
					selection.click(mouseX, mouseY, KeyEvent.VK_S);
				}
				else {
					selection.selectSprite(sprite); // 取消之前选中的物体，然后将现在选中的物体加入到selection中
					selection.click(mouseX, mouseY); // 无反应
					setMouseState(MOUSE_STATE_DRAGGING_SPRITE); // 可以拖动物体了
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

	protected void newSpriteMouseDragged(MouseEvent e) {
		if (newspriteMode != MULTI_NEWSPRITE) { return; }
		Painter painter = mouseInfo.getPainter();
		if (painter != null && mouseState == MOUSE_STATE_NEW_SPRITE_LEFT_PRESSED) {

			int width = painter.getImageWidth();
			int height = painter.getImageHeight();
			int x = newSpriteX;
			int y = newSpriteY;
			int offsetX = 0; // 允许的偏差
			int offsetY = 0;

			if (mouseX <= newSpriteX - width + offsetX || mouseX >= newSpriteX + width - offsetX) {
				x = newSpriteX + (mouseX > newSpriteX ? width : -width);
				if (mouseY <= newSpriteY - height / 2 || mouseY >= newSpriteY + height / 2) {
					y = newSpriteY + (mouseY > newSpriteY ? height : -height);
				}
			}
			if (mouseY <= newSpriteY - height + offsetY || mouseY >= newSpriteY + height - offsetY) {
				y = newSpriteY + (mouseY > newSpriteY ? height : -height);
				if (mouseX <= newSpriteX - width / 2 || mouseX >= newSpriteX + width / 2) {
					x = newSpriteX + (mouseX > newSpriteX ? width : -width);
				}
			}

			if (x != newSpriteX || y != newSpriteY) {
				newSpriteX = x;
				newSpriteY = y;
				IntPair tmp;
				boolean hasSamePos = false;
				for (int i = 0; i < newspritePoints.size(); ++i) {
					tmp = (IntPair) (newspritePoints.get(i));
					if (tmp.x == newSpriteX && tmp.y == newSpriteY) {
						hasSamePos = true;
						break;
					}
				}
				if (!hasSamePos) {
					if (addNewSprite(newSpriteX, newSpriteY)) {
						newspritePoints.add(new IntPair(newSpriteX, newSpriteY));
					}
				}
			}
			scrollablePanel.repaint();
		}
	}

	protected void newSpriteMouseLeftPressed(MouseEvent e) {
		if (newspriteMode == NONE_NEWSPRITE) { return; }
		Painter painter = mouseInfo.getPainter();
		if (painter != null) {
			setMouseState(MOUSE_STATE_NEW_SPRITE_LEFT_PRESSED);
			newSpriteX = mouseX;
			newSpriteY = mouseY;
			if (addNewSprite(newSpriteX, newSpriteY)) {
				newspritePoints.clear();
				newspritePoints.add(new IntPair(newSpriteX, newSpriteY));
				scrollablePanel.repaint();
			}
		}
	}

	protected void newSpriteMouseLeftReleased(MouseEvent e) {
		newspritePoints.clear();
		setMouseState(MOUSE_STATE_NORMAL);
	}

	protected void newSpriteMouseMoved(MouseEvent e) {
		scrollablePanel.repaint();
	}

	protected void otherMouseRightPressed(MouseEvent e) {
		boolean changed = false;
		if (!selection.isEmpty()) {
			selection.cancelMoving();
			selection.clear();
		}
		newspritePoints.clear();
		mouseInfo.resetAll();
		setMouseState(MOUSE_STATE_NORMAL);
		if (changed) {
			resortSprites();
		}
		scrollablePanel.repaint();
	}

	protected void paintMouseMovingViewport(Graphics g) { // do sth like
	// special mouse
	// icon
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

	protected void paintRectangling(Graphics g) {
		//System.out.println("paintRectangling");
		Color oldColor = g.getColor();
		g.setColor(Color.WHITE);
		g.drawRect(Math.min(oldMouseX, mouseX), Math.min(oldMouseY, mouseY), Math.abs(mouseX
		        - oldMouseX), Math.abs(mouseY - oldMouseY));
		g.setColor(oldColor);
	}

	public void paintSprite(Sprite sprite, Graphics g) {
		if (sprite != null) {
			sprite.paint(g);
		}
	}
	
	public void paintSprites(ArrayList sprites, Graphics g) {
		if (sprites == null) { return; }
		// 先画静止的
		for (int i = 0; i < sprites.size(); ++i) {
			SISprite sprite = (SISprite) (sprites.get(i));
			if (!sprite.isMoving()) {
				paintSprite(sprite, g);
			}
		}
		// 再画运动的
		for (int i = 0; i < sprites.size(); ++i) {
			SISprite sprite = (SISprite) (sprites.get(i));
			if (sprite.isMoving()) {
				paintSprite(sprite, g);
			}
		}
	}

	public void paintSprites(ArrayList sprites, Graphics g, int layer) {
		if (sprites == null) { return; }
		
		Sprite[] ss = getSprites(layer);
		int min = getMinLayer(ss);
		int max = getMaxLayer(ss)+1;
		for(int l=min; l<max; ++l) {
			for (int i = 0; i < ss.length; ++i) {
				SISprite sprite = (SISprite) (ss[i]);
				if (l == sprite.getLayer()) {
					paintSprite(sprite, g);
				}
			}
		}
	}
	
	public void paintSprites(Graphics g, int layer) {
		// TODO Auto-generated method stub
		paintSprites(sprites, g, layer);
	}

	public void paintSprites(Graphics g) {
		paintSprites(sprites, g);
	}

	public void paintStatic(ArrayList sprites, Graphics g) {
		if (sprites == null) { return; }
		for (int i = 0; i < sprites.size(); ++i) {
			SISprite sprite = (SISprite) (sprites.get(i));
			sprite.paintIdle(g);
		}
	}

	public void paintStatic(Graphics g) {
		paintStatic(sprites, g);
	}
	
	public void paintStatic(Graphics g, int layer) {
		if (sprites == null) { return; }
		
		Sprite[] ss = getSprites(layer);
		int min = getMinLayer(ss);
		int max = getMaxLayer(ss)+1;
		for(int l=min; l<max; ++l) {
			for (int i = 0; i < ss.length; ++i) {
				SISprite sprite = (SISprite) (ss[i]);
				if(l == sprite.getLayer())
					sprite.paintIdle(g);
			}
		}
	}

	public Sprite[] getSprites(int layer) {
		// TODO Auto-generated method stub
		ArrayList list = new ArrayList();
		for (int i = 0; i < sprites.size(); ++i) {
			SISprite s = (SISprite) (sprites.get(i));
			if(s.getLayer2() == layer)
				list.add(sprites.get(i));
		}
		
		Sprite[] result = new Sprite[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			result[i] = (Sprite) (list.get(i));
		}
		return result;
	}

	protected void pasteSprites(int x, int y) {
		if (copiedSprites.size() > 0) {
			ArrayList tmp = new ArrayList();
			int left = 0;
			int top = 0;
			int right = 0;
			int bottom = 0;

			boolean init = false;
			for (int i = 0; i < copiedSprites.size(); ++i) {
				Sprite sprite = (Sprite) (copiedSprites.get(i));
				if (sprite instanceof Copyable) {
					if (!init) {
						left = sprite.getX();
						top = sprite.getY();
						right = sprite.getX();
						bottom = sprite.getY();
						init = true;
					}
					else {
						if (left > sprite.getX()) {
							left = sprite.getX();
						}
						if (top > sprite.getY()) {
							top = sprite.getY();
						}
						if (right < sprite.getX()) {
							right = sprite.getX();
						}
						if (bottom < sprite.getY()) {
							bottom = sprite.getY();
						}
					}
					tmp.add(((Copyable) sprite).copy());
				}
			}

			if (tmp.size() > 0) {
				Sprite[] sprites = new Sprite[tmp.size()];
				for (int i = 0; i < tmp.size(); ++i) {
					Sprite sprite = (Sprite) (tmp.get(i));
					sprite.setID(useMaxID());
					sprite.setPosition(sprite.getX() + mouseX - (left + right) / 2, sprite.getY()
					        + mouseY - (top + bottom) / 2);
					sprites[i] = sprite;
				}
				addUndoableSprites(sprites);
				selection.selectSprites(sprites);
				scrollablePanel.repaint();
			}
		}
	}

	public void redoAdd(Sprite[] sprites) {
		for (int i = 0; i < sprites.length; ++i) {
			addSprite(sprites[i]);
		}
		resortSprites();
		scrollablePanel.repaint();
	}

	public void redoMove(Sprite[] sprites, int offsetX, int offsetY) {
		for (int i = 0; i < sprites.length; ++i) {
			sprites[i].setPosition(sprites[i].getX() + offsetX, sprites[i].getY() + offsetY);
		}
		resortSprites();
		scrollablePanel.repaint();
	}

	public void redoRemove(Sprite[] sprites) {
		for (int i = 0; i < sprites.length; ++i) {
			removeSprite(sprites[i]);
		}
		selection.cancelMoving();
		selection.clear();
		spriteChanged();
		scrollablePanel.repaint();
	}

	public void removeListener(ManagerListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

	protected boolean removeSprite(Sprite sprite) {
		selection.removeSprite(sprite);
		int spriteIndex = getSpriteIndex(sprite);
		if (spriteIndex >= 0) {
			sprites.remove(spriteIndex);
			return true;
		}
		return false;
	}

	protected boolean removeUndoableSprite(Sprite sprite) {
		Sprite[] tmp = new Sprite[1];
		tmp[0] = sprite;
		return removeUndoableSprites(tmp);
	}

	protected boolean removeUndoableSprites(Sprite[] sprites) {
		ArrayList tmp = new ArrayList();
		for (int i = 0; i < sprites.length; ++i) {
			if (removeSprite(sprites[i])) {
				tmp.add(sprites[i]);
			}
		}
		if (tmp.size() > 0) {
			Sprite[] undoArray = new Sprite[tmp.size()];
			for (int i = 0; i < tmp.size(); ++i) {
				undoArray[i] = (Sprite) (tmp.get(i));
			}
			undoManager.addUndoSpriteRemove(undoArray);
			spriteChanged();
			return true;
		}
		else {
			return false;
		}
	}

	public void reset() {
		initReset();
		if (scrollablePanel != null) scrollablePanel.repaint();
	}

	public void resetOrigin(int offsetX, int offsetY) {
		for (int i = 0; i < sprites.size(); ++i) {
			Sprite sprite = (Sprite) (sprites.get(i));
			sprite.confirmMoving();
			sprite.setPosition(sprite.getX() + offsetX, sprite.getY() + offsetY);
		}
		if (scrollablePanel != null) scrollablePanel.repaint();
	}

	protected void resortSprites() {
		TreeSet tmp = new TreeSet();
		for (int i = 0; i < sprites.size(); ++i) {
			tmp.add(sprites.get(i));
		}
		sprites.clear();
		Iterator it = tmp.iterator();
		while (it.hasNext()) {
			sprites.add(it.next());
		}
		spriteChanged();
	}

	abstract public void save(DataOutputStream out, Object[] resManagers) throws Exception;

	abstract public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception;
	
	abstract public void saveMobile(DataOutputStream out, int layer) throws Exception;

	protected void selectionChanged() {
		for (int i = 0; i < listeners.size(); ++i) {
			((ManagerListener) (listeners.get(i))).selectionChanged();
		}
	}

	protected void selectPointMouseDoubleClick(MouseEvent e) {
		switch (mouseState) {
		case MOUSE_STATE_NORMAL:
			Sprite sprite = selection.getTopSpriteAtPoint(mouseX, mouseY);
			if (sprite != null) {
				showProperties(sprite);
			}
			break;
		default:
			break;
		}
	}

	protected void selectPointMouseDragged(MouseEvent e) {
		//System.out.println("selectPointMouseDragged   "+);
		switch (mouseState) {
		case MOUSE_STATE_NORMAL:
			if(rdown)//按住r旋转
			{
				//System.out.println("ShiftDown()");
				selection.moveoffsety(mouseY - oldMouseY ,oldMouseX);
				oldMouseY = mouseY;
			}
			else if(sdown)
			{
				selection.scaleoffsety(mouseY - oldMouseY ,oldMouseX);
				oldMouseY = mouseY;
			}
			scrollablePanel.repaint();
			break;
		case MOUSE_STATE_DRAGGING_SPRITE:
			if (e.isAltDown()) { // 按住alt键，水平或者垂直方向移动
				if (Math.abs(mouseX - oldMouseX) >= Math.abs(mouseY - oldMouseY)) { // X方向偏移量较大
					System.out.println("moving   x");
					selection.moving(mouseX - oldMouseX, 0);
				}
				else { // Y方向偏移量较大
					selection.moving(0, mouseY - oldMouseY);
					System.out.println("moving   y");
				}
			}
			else { // 普通移动
				selection.moving(mouseX - oldMouseX, mouseY - oldMouseY);
				//System.out.println("moving   normal");
			}
			scrollablePanel.repaint();
			break;
		case MOUSE_STATE_RECTANGLING:
			scrollablePanel.repaint();
			break;
		default:
			break;
		}
	}

	protected void selectPointMouseLeftPressed(MouseEvent e) {
		switch (selectionMode) {
		case NONE_SELECTION:
			break;
		case SINGLE_SELECTION:
			//System.out.println("SINGLE_SELECTION");
			singleSelectionMouseLeftPressed(e);
			break;
		case MULTI_SELECTION:
			//System.out.println("MULTI_SELECTION");
			multiSelectionMouseLeftPressed(e);
			break;
		default:
			break;
		}
	}

	protected void selectPointMouseLeftReleased(MouseEvent e) {
		switch (mouseState) {
		case MOUSE_STATE_DRAGGING_SPRITE:
			if (mouseX == oldMouseX && mouseY == oldMouseY) {
				selection.cancelMoving();
			}
			else {
				selection.confirmMoving();
				resortSprites();
				scrollablePanel.repaint();
			}
			//System.out.println("MOUSE_STATE_DRAGGING_SPRITE");
			break;
		case MOUSE_STATE_RECTANGLING:
			selection.selectSprites(getAllSpritesInRect(Math.min(oldMouseX, mouseX), Math.min(
			        oldMouseY, mouseY), Math.abs(mouseX - oldMouseX), Math.abs(mouseY - oldMouseY)));
			scrollablePanel.repaint();
			//System.out.println("MOUSE_STATE_RECTANGLING");
			break;
		default:
			break;
		}
		//System.out.println("MOUSE_STATE_NORMAL");
		setMouseState(MOUSE_STATE_NORMAL);
	}

	protected void selectPointMouseMoved(MouseEvent e) {
		//System.out.println("selectPointMouseMoved");
		if (tooltipable) {
			scrollablePanel.setToolTipText(getToolTipText(getTopSpriteAtPoint(mouseX, mouseY)));
		}
	}

	protected void selectPointMouseRightPressed(MouseEvent e) {
		switch (mouseState) {
		case MOUSE_STATE_RECTANGLING:
			setMouseState(MOUSE_STATE_NORMAL);
			scrollablePanel.repaint();
			break;
		case MOUSE_STATE_DRAGGING_SPRITE:
			setMouseState(MOUSE_STATE_NORMAL);
			if (!selection.isEmpty()) {
				selection.cancelMoving();
				// selection.clear();
				scrollablePanel.repaint();
			}
			break;
		case MOUSE_STATE_NORMAL:
			setMouseState(MOUSE_STATE_SELECT_POINT_RIGHT_PRESSED);
			Sprite sprite = selection.getTopSpriteAtPoint(mouseX, mouseY);
			if (sprite == null) {
				selection.clear();
				scrollablePanel.repaint();
			}
			break;
		default:
			break;
		}
	}

	protected void selectPointMouseRightReleased(int x, int y) {
		switch (mouseState) {
		case MOUSE_STATE_SELECT_POINT_RIGHT_PRESSED: // can select
			if (selection.isEmpty()) {
				Sprite[] sprites = getAllSpritesAtPoint(mouseX, mouseY);
				if (sprites != null) {
					if (sprites.length == 1) {
						selection.selectSprite(sprites[0]);
						scrollablePanel.repaint();
						showPopupMenu(sprites, x, y);
					}
					else if (sprites.length > 1) {
						showSelectSpriteMenu(sprites, x, y);
					}
				}
			}
			else {
				Sprite[] sprite = selection.getSprites();
				if (sprite != null) {
					showPopupMenu(sprite, x, y);
				}
			}
			break;
		default:
			break;
		}
		setMouseState(MOUSE_STATE_NORMAL);
	}

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

	private void selfKeyReleased(KeyEvent e) {
		int keyID = getKeyID(e.getKeyCode());
		if (keyID >= 0) {
			pressedKey = pressedKey & (~KEY_CODES[keyID][1]);
		}
		keyReleased(e);
		keyChanged();
	}

	private void selfKeyTyped(KeyEvent e) {}

	private void selfMouseClicked(MouseEvent e) {
		if (!scrollablePanel.isMouseMovingViewport()) {
			mouseClicked(e);
		}
		mouseChanged(e);
	}

	private void selfMouseDragged(MouseEvent e) {
		mouseX = getMouseX(e);
		mouseY = getMouseY(e);
		// checkMouseXY();
		if (!scrollablePanel.isMouseMovingViewport()) {
			mouseDragged(e);
		}
		mouseChanged(e);
	}

	private void selfMouseEntered(MouseEvent e) {
		selfMouseMoved(e);
		mouseEntered(e);
	}

	private void selfMouseExited(MouseEvent e) {
		selfMouseMoved(e);
		mouseExited(e);
	}

	private void selfMouseMoved(MouseEvent e) {
		mouseX = getMouseX(e);
		mouseY = getMouseY(e);
		if (!scrollablePanel.isMouseMovingViewport()) {
			mouseMoved(e);
		}
		mouseChanged(e);
	}

	private void selfMousePressed(MouseEvent e) {
		oldMouseX = mouseX = getMouseX(e);
		oldMouseY = mouseY = getMouseY(e);
		if (!scrollablePanel.isMouseMovingViewport()) {
			mousePressed(e);
		}
		mouseChanged(e);
	}

	private void selfMouseReleased(MouseEvent e) {
		mouseX = getMouseX(e);
		mouseY = getMouseY(e);
		if (!scrollablePanel.isMouseMovingViewport()) {
			mouseReleased(e);
		}
		setMouseState(MOUSE_STATE_NORMAL);
		mouseChanged(e);
	}

	public void setCopyable(boolean copyable) {
		this.copyable = copyable;
		copiedSprites.clear();
	}

	public void setDeleteable(boolean deleteable) {
		this.deleteable = deleteable;
	}

	public void setLayerable(boolean layerable) {
		this.layerable = layerable;
	}

	protected void setMouseState(int state) {
		switch (state) {
		case MOUSE_STATE_NORMAL: // 鼠标状态还原
			this.mouseState = state;
			break;
		case MOUSE_STATE_DRAGGING_SPRITE: // 拖动Sprite
			if (moveable) {
				this.mouseState = state;
			}
			break;
		case MOUSE_STATE_RECTANGLING: // 框选Sprite
			if (selectionMode == MULTI_SELECTION) {
				this.mouseState = state;
			}
			break;
		case MOUSE_STATE_CTRL_RECTING: // Ctrl框选Sprite
			if (selectionMode == MULTI_SELECTION) {
				this.mouseState = state;
			}
			break;
		case MOUSE_STATE_SELECT_POINT_RIGHT_PRESSED: // 右键选择Sprite
			if (selectionMode != NONE_SELECTION) {
				this.mouseState = state;
			}
			break;
		case MOUSE_STATE_NEW_SPRITE_LEFT_PRESSED: // 拖动新增Sprite
			if (newspriteMode == MULTI_NEWSPRITE) {
				this.mouseState = state;
			}
			break;
		default:
			break;
		}
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public void setNewspriteMode(int newspriteMode) {
		if (this.newspriteMode == newspriteMode) { return; }
		newspritePoints.clear();
		this.newspriteMode = newspriteMode;
	}

	public void setSelectionMode(int mode) {
		if (this.selectionMode == mode) { return; }
		selection.clear();
		selectionMode = mode;
	}

	protected void setSpritesToBottom(Sprite[] sprites) {
		int minLayer = getAllSpritesMinLayer();
		int maxLayer = getMaxLayer(sprites);

		increaseLayer(sprites, minLayer - maxLayer - 1);
	}

	protected void setSpritesToTop(Sprite[] sprites) {
		int minLayer = getMinLayer(sprites);
		int maxLayer = getAllSpritesMaxLayer();

		increaseLayer(sprites, maxLayer - minLayer + 1);
	}

	public void setToolTipable(boolean tooltipable) {
		this.tooltipable = tooltipable;
	}

	public void setUndoable(boolean undoable) {
		if (this.undoable == undoable) { return; }
		undoManager.discardAllEdits();
		this.undoable = undoable;
	}

	protected void showPopupMenu(Sprite[] sprites, int x, int y) {
		if(sprites != null) {
			if(sprites.length == 1) {
				showSinglePopupMenu(sprites[0], x, y);
			}
			else {
				showMultiPopupMenu(sprites, x, y);
			}
		}
	}
	
	protected void showSinglePopupMenu(Sprite sprite, int x, int y) {}
	protected void showMultiPopupMenu(Sprite[] sprites, int x, int y) {}

	protected void showSelectSpriteMenu(Sprite[] sprites, int x, int y) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem menu;
		for (int i = sprites.length - 1; i >= 0; --i) {
			menu = new JMenuItem(sprites[i].getSelectMenuName());
			menu.addActionListener(new SelectSpriteActionListener(sprites[i]));
			popup.add(menu);
		}
		popup.show(scrollablePanel, x, y);
	}

	protected void showProperties(Sprite sprite) {}

	protected void singleSelectionMouseLeftPressed(MouseEvent e) {
		if (selection.pointInSprites(mouseX, mouseY)) { // 按键点在已选择的物体内
			//System.out.println("按键点在已选择的物体内");
			selection.click(mouseX, mouseY);
			setMouseState(MOUSE_STATE_DRAGGING_SPRITE); // 可以拖动物体了
		}
		else {
			Sprite sprite = getTopSpriteAtPoint(mouseX, mouseY);
			if (sprite != null) {
				//System.out.println("按键点在已选择的物体wai");
				selection.selectSprite(sprite);
				selection.click(mouseX, mouseY);
				setMouseState(MOUSE_STATE_DRAGGING_SPRITE); // 可以拖动物体了
			}
			else {
				selection.clear();
			}
		}
		scrollablePanel.repaint();
	}

	protected void spriteChanged() {
		for (int i = 0; i < listeners.size(); ++i) {
			((ManagerListener) (listeners.get(i))).spriteChanged();
		}
	}

	public void undoAdd(Sprite[] sprites) {
		for (int i = 0; i < sprites.length; ++i) {
			removeSprite(sprites[i]);
		}
		selection.cancelMoving();
		selection.clear();
		spriteChanged();
		scrollablePanel.repaint();
	}

	public void undoMove(Sprite[] sprites, int offsetX, int offsetY) {
		for (int i = 0; i < sprites.length; ++i) {
			sprites[i].setPosition(sprites[i].getX() - offsetX, sprites[i].getY() - offsetY);
		}
		resortSprites();
		scrollablePanel.repaint();
	}

	public void undoRemove(Sprite[] sprites) {
		for (int i = 0; i < sprites.length; ++i) {
			addSprite(sprites[i]);
		}
		resortSprites();
		scrollablePanel.repaint();
	}

	protected int useMaxID() {
		return maxID++;
	}
}

abstract class SpriteManagerPanel extends ScrollablePanel implements ManagerPanel {

	protected JDialog dlgOwner;
	protected JFrame frmOwner;
	protected SpriteManager manager;
	protected MouseInfo mouseInfo;
	protected ArrayList backManagers;
	protected ArrayList foreManagers;

	public SpriteManagerPanel(JDialog owner, MouseInfo mouseInfo) {
		super(XUtil.getDefPropInt("DefMapWidth"), XUtil.getDefPropInt("DefMapHeight"));
		init(null, owner, mouseInfo);
	}

	public SpriteManagerPanel(JFrame owner, MouseInfo mouseInfo) {
		super(XUtil.getDefPropInt("DefMapWidth"), XUtil.getDefPropInt("DefMapHeight"));
		init(owner, null, mouseInfo);
	}

	public void addBackManager(Manager manager) {
		if (manager == null) { return; }
		for (int i = 0; i < backManagers.size(); ++i) {
			if (backManagers.get(i).equals(manager)) { return; }
		}
		backManagers.add(manager);
	}

	public void addForeManager(Manager manager) {
		if (manager == null) { return; }
		for (int i = 0; i < foreManagers.size(); ++i) {
			if (foreManagers.get(i).equals(manager)) { return; }
		}
		foreManagers.add(manager);
	}

	abstract protected SpriteManager createManager();
	
	public JFrame getFrameOwner() {
		return frmOwner;
	}
	
	public JDialog getDlgOwner() {
		return dlgOwner;
	}
	
	public Manager getManager() {
		return manager;
	}

	private void init(JFrame frmOwner, JDialog dlgOwner, MouseInfo mouseInfo) {
		this.frmOwner = frmOwner;
		this.dlgOwner = dlgOwner;
		this.mouseInfo = mouseInfo;
		this.manager = createManager();
		backManagers = new ArrayList();
		foreManagers = new ArrayList();
	}

	public void load(DataInputStream in, Object[] resManagers) throws Exception {
		manager.load(in, resManagers);
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!isVisible()) { return; }
		
		MainFrame.self.getMapInfo().paintBackground(g);
		MainFrame.self.getTilePanel().getManager().paintStatic(g);
		
		LayerManager lm = MainFrame.self.getLayerManager();
		for(int i=lm.layers.size()-1; i>=0; --i) {
			int layer = lm.getLayer(i).id;
			paintBack(g, layer);
			manager.paintSprites(g, layer);
			paintFore(g, layer);
		}
		paintSelf(g);

		MapInfo mapInfo = MainFrame.self.getMapInfo();
		mapInfo.paintBorder(g);
	}

	private void paintFore(Graphics g, int id) {
		// TODO Auto-generated method stub
		for (int i = 0; i < foreManagers.size(); ++i) {
			((Manager) (foreManagers.get(i))).paintStatic(g, id);
		}
	}

	private void paintBack(Graphics g, int id) {
		// TODO Auto-generated method stub
		for (int i = 0; i < backManagers.size(); ++i) {
			((Manager) (backManagers.get(i))).paintStatic(g, id);
		}
	}
	
	protected void paintBack(Graphics g) {
		paintBackManagers(g);
	}

	protected void paintBackManagers(Graphics g) {
		for (int i = 0; i < backManagers.size(); ++i) {
			((Manager) (backManagers.get(i))).paintStatic(g);
		}
	}

	protected void paintFore(Graphics g) {
		paintForeManagers(g);
	}

	protected void paintForeManagers(Graphics g) {
		for (int i = 0; i < foreManagers.size(); ++i) {
			((Manager) (foreManagers.get(i))).paintStatic(g);
		}
	}

	protected void paintSelf(Graphics g) {
	//	manager.paintSprites(g);
		manager.paintOthers(g);
	}

	public void removeBackManager(Manager manager) {
		if (manager == null) { return; }
		backManagers.remove(manager);
	}

	public void removeForeManager(Manager manager) {
		if (manager == null) { return; }
		foreManagers.remove(manager);
	}

	public void reset(int basicWidth, int basicHeight) {
		super.reset(basicWidth, basicHeight);
		manager.reset();
	}

	public void save(DataOutputStream out, Object[] resManagers) throws Exception {
		manager.save(out, resManagers);
	}

	public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception {
		manager.saveMobile(out, resManagers);
	}
	
	public void saveMobile(DataOutputStream out, int layer) throws Exception {
		manager.saveMobile(out, layer);
	}

	public void setMapSize(int basicWidth, int basicHeight) {
		super.reset(basicWidth, basicHeight);
	}
}