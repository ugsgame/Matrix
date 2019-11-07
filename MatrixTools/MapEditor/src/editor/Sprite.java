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
	private boolean shiftdown;
	private boolean sdown;
	private String name;
	private double roate = 0;
	private double scale = 1;

	public BasicSprite(int id, int x, int y, String name) {
		init(id, x, y, name);
	}

	@Override
	public void setShiftDownTrue(boolean shiftdown) {
		// TODO Auto-generated method stub
		if(selected)
		{
			//if(shiftdown)
				//System.out.println("我被按了shift键了");
			this.shiftdown = shiftdown;
		}
	}
	
	public void setSdownTrue(boolean s)
	{
		//System.out.print
		if(selected)
		{
			//System.out.println("sdown false");
			this.sdown = s;
		}
	}
	
	public boolean isShiftdown()
	{
		return this.shiftdown;
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
		int w2 = (int) (getWidth()*scale+0.5);
		int h2 = (int)(getHeight()*scale+0.5);
		int w3 = (getWidth()-w2)/2;
		int h3 = (getHeight()-h2)/2;
		return (x >= getLeft()+w3 && x <= getLeft()+w3 + getWidth()*scale && y >= getTop()+h3 && y <= getTop()+h3
		        + getHeight()*scale);
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
		int w2 = (int) (getWidth()*scale+0.5);
		int h2 = (int)(getHeight()*scale+0.5);
		int w3 = (getWidth()-w2)/2;
		int h3 = (getHeight()-h2)/2;
		return (getLeft()+w3 >= x && getLeft()+w3 + getWidth()*scale <= x + w && getTop()+h3 > y && getTop()+h3
		        + getHeight()*scale <= y + h);
	}

	public boolean isMoving() {
		return moving;
	}

	public boolean isSelected() {
		return selected;
	}

	protected void moveTo(int x, int y) {
		if (this.x == x && this.y == y) { return; }
		setPosition(x, y);
	}

	public void moving(int offsetX, int offsetY) {
		moving = true;
		movingX = x + offsetX;
		movingY = y + offsetY;
		//System.out.println("moving");
	}
	
	public void move(int offsetX, int offsetY) {
		x = x + offsetX;
		y = y + offsetY;
	}

	public void paint(Graphics g) {
		if(this.selected && this.shiftdown)
		{
			//System.out.println("可以绘制旋转了");
			paintcircle(g);
		}
		else if(this.selected && this.sdown)
		{
			paintscale(g);
		}
		else if (selected) {
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
	
	public void moveoffsety(int offset ,int x)
	{
		if(selected)
		{
			//System.out.println("旋转角度改变");
			if(x< (this.getLeft()+this.getWidth()/2))
			{
				//System.out.println(x);
				roate = roate - 0.5*offset;
			}
			else
				roate = roate + 0.5*offset;
			
			//System.out.println(roate);
		}
	}
	
	public void scaleoffsety(int offset ,int x)
	{
		if(selected)
		{
			scale = scale + 0.005*offset;
		}
	}
	
	public void resetRange()
	{
		roate = 0;
	}
	
	public void resetScale()
	{
		scale = 1;
	}
	
	public double getRoate()
	{
		return this.roate;
	}
	
	public double getScale()
	{
		return this.scale;
	}
	
	public void setRoate(double a)
	{
		this.roate = a;
	}
	
	public void setScale(double a)
	{
		this.scale = a;
	}
	
	public void paintscale(Graphics g)
	{
		paintIdle(g);
		g.setColor(Color.pink);
		int left = getLeft();
		int top = getTop();
		int sw = (int) (getWidth()*scale+0.5);
		int sh = (int) (getHeight()*scale+0.5);
		int w = sw/2-sw/8;
		int h = sh/2-sw/8;
		int w1 = sw/2-sw/10;
		int h1 = sh/2-sw/10;
		
		int w2 = (int) (getWidth()*scale+0.5);
		int h2 = (int)(getHeight()*scale+0.5);
		int w3 = (getWidth()-w2)/2;
		int h3 = (getHeight()-h2)/2;
		
		g.drawRect(left+w+w3,top+h+h3,sw/4,sw/4);
		g.drawRect(left+w1+w3,top+h1+h3,sw/5,sw/5);
	}
	
	public void paintcircle(Graphics g)
	{
		paintIdle(g);
		int left = getLeft();
		int top = getTop();
		int sw = (int) (getWidth()*scale+0.5);
		int sh = (int) (getHeight()*scale+0.5);
		int w = sw/2-sw/8;
		int h = sh/2-sw/8;
		int w1 = sw/2-sw/10;
		int h1 = sh/2-sw/10;
		g.setColor(Color.MAGENTA);
		
		int w2 = (int) (getWidth()*scale+0.5);
		int h2 = (int)(getHeight()*scale+0.5);
		int w3 = (getWidth()-w2)/2;
		int h3 = (getHeight()-h2)/2;
		
		g.drawArc(left+w+w3,top+h+h3,sw/4,sw/4,0,360); //画圆
		g.drawArc(left+w1+w3,top+h1+h3,sw/5,sw/5,0,360); //画圆
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
		int w = (int) (getWidth()*scale+0.5);
		int h = (int)(getHeight()*scale+0.5);
		int w1 = (getWidth()-w)/2;
		int h1 = (getHeight()-h)/2;
		
	//	g.fillRect(left, top, w, h);
		g.fillRect(left+w1, top+h1, (int) (getWidth()*scale+0.5), 1);
		g.fillRect(left+w1, top+h1, 1, (int) (getHeight()*scale+0.5));
		g.fillRect(left+w1, top+h1 + (int)(getHeight()*scale+0.5) - 1, (int) (getWidth()*scale+0.5), 1);
		g.fillRect(left+w1 + (int)(getWidth()*scale+0.5) - 1, top+h1, 1, (int) (getHeight()*scale+0.5));
		g.setColor(oldColor);
	}
	
	//画地图
	public void paintIdle(Graphics g) {
		paintIdle(g, x, y ,roate ,scale);
	}

	abstract public void paintIdle(Graphics g, int x, int y ,double roate ,double scale);

	public void paintMoving(Graphics g) {
		//System.out.println("paint moving");
		paintIdle(g, movingX, movingY ,roate ,scale);
		paintBorder(g, getMovingBorderColor());
	}

	public void paintSelected(Graphics g) {
		//System.out.println("paint Selected");
		paintIdle(g);
		paintBorder(g, getSelectedBorderColor());
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(id);
		out.writeInt(x);
		out.writeInt(y);
		SL.writeString(name, out);
	}
	
	public void load(DataInputStream in) throws Exception {
		id = in.readInt();
		int ax = in.readInt();
		int ay = in.readInt();
		setPosition(ax, ay);
		name = SL.readString(in);
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
//		if(selected)
//			System.out.println("设置选中");
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
	
	public void setShiftDownTrue(boolean ctrldown);
	
	public void setSdownTrue(boolean s);
	
	public void moveoffsety(int offset ,int x);
	
	public void scaleoffsety(int offset ,int x);
	
	public void resetRange();
	
	public void resetScale();
}