package editor;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

abstract public class PointShape implements Shape {

	private final static int CORNER_SIZE = 4;

	private final static int CONTAIN_OFFSET = 4;

	private final static int[] CORNER_CURSORS = { Cursor.DEFAULT_CURSOR, Cursor.MOVE_CURSOR };

	public int x, y;
	private double scaleX, scaleY;
	private boolean selected;

	PointShape(int x, int y) {
		this.x = x;
		this.y = y;
		this.scaleX = 1;
		this.scaleY = 1;
		this.selected = false;
	}

	public void copyFrom(PointShape source) {
		this.x = source.x;
		this.y = source.y;
		this.scaleX = source.scaleX;
		this.scaleY = source.scaleY;
	}

	public void setScale(double scaleX, double scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean containPoint(int x, int y) {// 此处的XY是放大之后的XY{
		boolean inLineRange = false;
		double x1 = this.x * scaleX;
		double y1 = this.y * scaleY;
		double offset = CONTAIN_OFFSET/2;

		if (x >= x1 - offset && x <= x1 + offset
		        && y >= y1 - offset && y <= y1 + offset)
				inLineRange = true;
		return (inLineRange || (selected && getCornerAtPoint(x, y) != CORNER_NONE));
	}
	
	public int getCornerAtPoint(int x, int y){
		return getCornerAtPoint(x, y, CORNER_SIZE);
	}

	private int getCornerAtPoint(int x, int y, int cornerSize) {// 此处的XY是放大之后的XY{
		int result = CORNER_NONE;
		IntPair cornerXY = getCornerXY(cornerSize);
		int cornerX = cornerXY.x;
		int cornerY = cornerXY.y;
		if (x >= cornerX && x <= cornerX + cornerSize && y >= cornerY
		        && y <= cornerY + cornerSize)
			result = 1;
		
		return result;
	}

	public boolean inRect(int left, int top, int width, int height) {
		return (x >= left && x <= left + width
		        && y >= top && y <= top + height);
	}

	private IntPair getCornerXY(int cornerSize) {// 此处的XY是放大之后的XY{
		int cornerX = (int) (x * scaleX - cornerSize / 2);
		int cornerY = (int) (y * scaleY - cornerSize / 2);
		return new IntPair(cornerX, cornerY);
	}

	public Cursor getCornerCursor(int corner) {
		if (corner >= 0 && corner < CORNER_CURSORS.length) {
			return Cursor.getPredefinedCursor(CORNER_CURSORS[corner]);
		}
		else {
			return Cursor.getDefaultCursor();
		}
	}
	
	public int drag(int offsetX, int offsetY) {
		x += offsetX;
		y += offsetY;
		return 0;
	}

	public void move(int offsetX, int offsetY) {
		x += offsetX;
		y += offsetY;
	}

	public int getCenterX() {
		return x;
	}

	public int getCenterY() {
		return y;
	}

	public String getInfo() {
		String result = "X：" + x + "  Y：" + y;
		return result;
	}

	public void paintCorner(Graphics g) {
		paintCorner(g, CORNER_SIZE);
	}

	private void paintCorner(Graphics g, int cornerSize) {
		Graphics2D g2 = (Graphics2D) g;
		Composite oldComposite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1));
		Color oldColor = g2.getColor();
		g2.setColor(getCornerColor());
		AffineTransform oldTransform = g2.getTransform();
		g2.scale(1.0 / scaleX, 1.0 / scaleY);
		
		int cornerX = (int)(x*scaleX-cornerSize/2);
		int cornerY = (int)(y*scaleY-cornerSize/2);
		g2.fillRect(cornerX, cornerY, cornerSize, cornerSize);
			
		g2.setTransform(oldTransform);
		g2.setColor(oldColor);
		g2.setComposite(oldComposite);
	}

	public Color getCornerColor() {
		return Color.BLACK;
	}

	public void paintSelected(Graphics g) {
		paintIdle(g);
		paintCorner(g);
	}

	public void paint(Graphics g) {
		if (!selected) {
			paintIdle(g);
		}
		else {
			paintSelected(g);
		}
	}
	
	public void confirmDrag(){
		
	}
	
	public int getCreatingDragCorner() {
		return 0;
	}
}

class LogicPoint extends PointShape implements Copyable, Saveable {
	public final static LogicPoint[] createArrayViaFile(DataInputStream in) throws Exception {
		LogicPoint[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new LogicPoint[length];
			for (int i = 0; i < length; ++i) {
				result[i] = createViaFile(in);
			}
		}
		return result;
	}

	public final static int W = 4;
	public final static int H = 4;

	private int id;
	private int type;

	public LogicPoint(int id, int type, int x, int y) {
		super(x, y);
		this.id = id;
		this.type = type;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return Frame.POINT_NAMES[type];
	}

	public int drag(int corner, int offsetX, int offsetY) {
		return super.drag(offsetX, offsetY);
	}

	public String getInfo() {
		String result = getName() + ",  " + super.getInfo();
		return result;
	}

	public void paintIdle(Graphics g) {
		Color oldColor = g.getColor();	
		
		Color color = Frame.POINT_COLORS[type];
		g.setColor(color);
		g.fillRect(x-W/2, y-H/2, W, H);
		g.setColor(oldColor);		
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		//调整顺序
		int x = this.x;
		int y = this.y;
	
		//保存数据
//		SL.writeInt(type, out);
//		SL.writeInt(subType, out);
		
		SL.writeInt(x, out);
		SL.writeInt(y, out);
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(id);
		out.writeInt(type);
		out.writeInt(x);
		out.writeInt(y);
	}

	public final static LogicPoint createViaFile(DataInputStream in) throws Exception {
		LogicPoint result = new LogicPoint(0, 0, 0, 0);
		result.load(in);
		return result;
	}

	private void load(DataInputStream in) throws Exception {
		id = in.readInt();
		type = in.readInt();
		x = in.readInt();
		y = in.readInt();
	}

	public int compareTo(Object arg0) {
		return 0;
	}

	public Copyable copy() {
		return null;
	}
	
	public final LogicPoint copyLogicPoint() {
		LogicPoint result = new LogicPoint(id, type, x, y);
		result.copyFrom(this);
		return result;
	}
}