package editor;

import java.awt.Color;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

abstract class BasicSprite implements Sprite, Saveable {

	private int id;
	private int x;
	private int y;
	private int movingX;
	private int movingY;
	private boolean selected;
	private boolean moving;
	private String name;

	public BasicSprite(int id, int x, int y, String name) {
		init(id, x, y, name);
	}

	public void cancelMoving() {
		if (moving) {
			moving = false;
			movingX = x;
			movingY = y;
		}
	}

	public int compareTo(Object o) {
		if (o == null) {
			return 1;
		}
		else if (!(o instanceof BasicSprite)) { return this.hashCode() - o.hashCode(); }

		BasicSprite dest = (BasicSprite) o;

		if (this.getY() != dest.getY()) {
			return this.getY() - dest.getY();
		}
		else if (this.getX() != dest.getX()) {
			return this.getX() - dest.getX();
		}
		else {
			return this.getID() - dest.getID();
		}
	}

	public void confirmMoving() {
		if (moving) {
			moving = false;
			moveTo(movingX, movingY);
		}
	}

	public boolean containPoint(int x, int y) {
		return (x >= getLeft() && x <= getLeft() + getWidth() && y >= getTop() && y <= getTop()
		        + getHeight());
	}

	public void copyFrom(BasicSprite source) {
		this.id = source.id;
		this.x = this.movingX = source.x;
		this.y = this.movingY = source.y;
		this.selected = false;
		this.moving = false;
		this.name = source.name;
	}

	public int getID() {
		return id;
	}

	public String getInfo() {
		String result = getName() + "  ID:" + getID();
		return result;
	}

	abstract public int getLeft();

	public Color getMovingBorderColor() {
		return Color.BLUE;
	}

	public int getMovingX() {
		return movingX;
	}

	public int getMovingY() {
		return movingY;
	}

	public String getName() {
		return name;
	}

	public String getSelectMenuName() {
		return name;
	}

	public Color getSelectedBorderColor() {
		return new Color(0xFF00FF);
	}

	abstract public int getTop();

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	private void init(int id, int x, int y, String name) {
		this.id = id;
		this.x = this.movingX = x;
		this.y = this.movingY = y;
		this.selected = false;
		this.moving = false;
		this.name = name;
	}

	public boolean inRect(int x, int y, int w, int h) {
		return (getLeft() >= x && getLeft() + getWidth() <= x + w && getTop() > y && getTop()
		        + getHeight() <= y + h);
	}

	public boolean isMoving() {
		return moving;
	}

	public boolean isSelected() {
		return selected;
	}

	public void load(DataInputStream in) throws Exception {
		id = in.readInt();
		int ax = in.readInt() + FrameManager.ORIGIN_X;
		int ay = in.readInt() + FrameManager.ORIGIN_Y;
		setPosition(ax, ay);
		name = SL.readString(in);
	}

	protected void moveTo(int x, int y) {
		if (this.x == x && this.y == y) { return; }
		setPosition(x, y);
	}

	public void moving(int offsetX, int offsetY) {
		moving = true;
		movingX = x + offsetX;
		movingY = y + offsetY;
	}

	public void paint(Graphics g) {
		if (selected) {
			if (!moving) {
				paintSelected(g);
			}
			else {
				paintMoving(g);
			}
		}
		else {
			paintIdle(g);
		}
	}

	public void paintBorder(Graphics g) {
		paintBorder(g, isMoving() ? getMovingBorderColor() : getSelectedBorderColor());
	}

	public void paintBorder(Graphics g, Color color) {
		Color oldColor = g.getColor();
		g.setColor(color);
		int left = getLeft();
		int top = getTop();
		if (moving) {
			left += movingX - x;
			top += movingY - y;
		}
		g.fillRect(left, top, getWidth(), 1);
		g.fillRect(left, top, 1, getHeight());
		g.fillRect(left, top + getHeight() - 1, getWidth(), 1);
		g.fillRect(left + getWidth() - 1, top, 1, getHeight());
		g.setColor(oldColor);
	}

	public void paintIdle(Graphics g) {
		paintIdle(g, x, y);
	}

	abstract public void paintIdle(Graphics g, int x, int y);

	public void paintMoving(Graphics g) {
		paintIdle(g, movingX, movingY);
		paintBorder(g, getMovingBorderColor());
	}

	public void paintSelected(Graphics g) {
		paintIdle(g);
		paintBorder(g, getSelectedBorderColor());
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(id);
		out.writeInt(x - FrameManager.ORIGIN_X);
		out.writeInt(y - FrameManager.ORIGIN_Y);
		SL.writeString(name, out);
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPosition(int x, int y) {
		this.x = this.movingX = x;
		this.y = this.movingY = y;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}

interface Flipable {

	public void flip();
}

interface Layerable {

	public int getLayer();

	public void setLayer(int layer);
}

interface Sprite extends Comparable {

	public void cancelMoving();

	public void confirmMoving();

	public boolean containPoint(int x, int y);

	public int getHeight();

	public int getID();

	public String getInfo();
	
	public String getName();
	
	public String getSelectMenuName();

	public int getWidth();

	public int getX();

	public int getY();

	public boolean inRect(int x, int y, int w, int h);

	public boolean isMoving();

	public boolean isSelected();

	public void moving(int offsetX, int offsetY);

	public void paint(Graphics g);

	public void paintIdle(Graphics g);

	public void paintMoving(Graphics g);

	public void paintSelected(Graphics g);

	public void setID(int id);

	public void setName(String name);

	public void setPosition(int x, int y);

	public void setSelected(boolean selected);
}