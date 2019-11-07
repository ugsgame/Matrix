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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

abstract public class RectShape implements Shape {

	public final static int CORNER_LEFT_TOP = 1; // 左上
	public final static int CORNER_LEFT_MIDDLE = 2; // 左中
	public final static int CORNER_LEFT_BOTTOM = 3; // 左下
	public final static int CORNER_CENTER_TOP = 4; // 中上
	public final static int CORNER_CENTER_MIDDLE = 5; // 正中
	public final static int CORNER_CENTER_BOTTOM = 6; // 中下
	public final static int CORNER_RIGHT_TOP = 7; // 右上
	public final static int CORNER_RIGHT_MIDDLE = 8; // 右中
	public final static int CORNER_RIGHT_BOTTOM = 9; // 右下

	public final static int CORNER_SIZE = 3;

	public final static int[] CORNERS = { CORNER_LEFT_TOP, CORNER_LEFT_MIDDLE, CORNER_LEFT_BOTTOM,
	        CORNER_CENTER_TOP, CORNER_CENTER_MIDDLE, CORNER_CENTER_BOTTOM, CORNER_RIGHT_TOP,
	        CORNER_RIGHT_MIDDLE, CORNER_RIGHT_BOTTOM };

	public final static double[][] CORNER_OFFSETS = { { 0, 0 }, { 0, 0 }, { 0, 0.5 }, { 0, 1 },
	        { 0.5, 0 }, { 0.5, 0.5 }, { 0.5, 1 }, { 1, 0 }, { 1, 0.5 }, { 1, 1 } };

	public final static int[] CORNER_CURSORS = { Cursor.DEFAULT_CURSOR, Cursor.NW_RESIZE_CURSOR,
	        Cursor.W_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR,
	        Cursor.MOVE_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
	        Cursor.E_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR };

	private final static int reverseX(int corner) {
		switch (corner) {
		case CORNER_LEFT_TOP:
			return CORNER_RIGHT_TOP;
		case CORNER_LEFT_MIDDLE:
			return CORNER_RIGHT_MIDDLE;
		case CORNER_LEFT_BOTTOM:
			return CORNER_RIGHT_BOTTOM;
		case CORNER_RIGHT_TOP:
			return CORNER_LEFT_TOP;
		case CORNER_RIGHT_MIDDLE:
			return CORNER_LEFT_MIDDLE;
		case CORNER_RIGHT_BOTTOM:
			return CORNER_LEFT_BOTTOM;
		default:
			return corner;
		}
	}

	private final static int reverseY(int corner) {
		switch (corner) {
		case CORNER_LEFT_TOP:
			return CORNER_LEFT_BOTTOM;
		case CORNER_LEFT_BOTTOM:
			return CORNER_LEFT_TOP;
		case CORNER_CENTER_TOP:
			return CORNER_CENTER_BOTTOM;
		case CORNER_CENTER_BOTTOM:
			return CORNER_CENTER_TOP;
		case CORNER_RIGHT_TOP:
			return CORNER_RIGHT_BOTTOM;
		case CORNER_RIGHT_BOTTOM:
			return CORNER_RIGHT_TOP;
		default:
			return corner;
		}
	}

	public int left, top, width, height;
	private double scaleX, scaleY ,roate;
	private boolean selected, scaled ,rpress;
	
	public void Rpressed(boolean t)
	{
		this.rpress = t;
	}
	
	public boolean getrpress()
	{
		return this.rpress;
	}
	
	public boolean getselect()
	{
		return this.selected;
	}
	
	public RectShape(int left, int top, int width, int height) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.scaleX = 1;
		this.scaleY = 1;
		this.selected = false;
		this.scaled = true;
	}

	public void copyFrom(RectShape source) {
		this.left = source.left;
		this.top = source.top;
		this.width = source.width;
		this.height = source.height;
		this.scaleX = source.scaleX;
		this.scaleY = source.scaleY;
	}
	
	public void setIsScaled(boolean scaled) {
		this.scaled = scaled;
	}

	public void setScale(double scaleX, double scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		//System.out.println("selertt");
		this.selected = selected;
	}

	public boolean containPoint(int x, int y) {// 此处的XY是放大之后的XY{
		double left = this.left * scaleX;
		double top = this.top * scaleY;
		double width = this.width * scaleX;
		double height = this.height * scaleY;

		boolean inRect = (x >= left && x <= left + width && y >= top && y <= top + height);
		return (inRect || (selected && getCornerAtPoint(x, y) != CORNER_NONE));
	}

	public boolean inRect(int left, int top, int width, int height) {
		boolean result = (left <= this.left && (left + width) >= (this.left + this.width)
		        && top <= this.top && (top + height) >= (this.top + this.height));
		return result;
	}

	public int getCornerAtPoint(int x, int y) {// 此处的XY是放大之后的XY{
		return getCornerAtPoint(x, y, CORNER_SIZE);
	}

	private int getCornerAtPoint(int x, int y, int cornerSize) {// 此处的XY是放大之后的XY{
		int result = CORNER_NONE;

		for (int i = 0; i < CORNERS.length; ++i) {
			IntPair cornerXY = getCornerXY(CORNERS[i], cornerSize);
			int cornerX = cornerXY.x;
			int cornerY = cornerXY.y;
			if (x >= cornerX && x <= cornerX + cornerSize && y >= cornerY
			        && y <= cornerY + cornerSize) {
				result = CORNERS[i];
				break;
			}

		}
		return result;
	}

	private IntPair getCornerXY(int corner, int cornerSize) {// 此处的XY是放大之后的XY{
		int x = this.left;
		int y = this.top;
//		if(this.roate != 0)
//		{
//			int cx = this.left + this.width/2;
//			int cy = this.top + this.height/2;
//			if(this.roate>0)
//			{
//				x = (int) ((x-cx)*Math.cos(this.roate)+(y-cy)*Math.sin(this.roate)+cx+0.5);
//				y = (int) ((y-cy)*Math.cos(this.roate)-(x-cx)*Math.sin(this.roate)+cy+0.5);
//			}
//			else
//			{
//				x = (int) ((x-cx)*Math.cos(this.roate)-(y-cy)*Math.sin(this.roate)+cx+0.5);
//				y = (int) ((y-cy)*Math.cos(this.roate)+(x-cx)*Math.sin(this.roate)+cy+0.5);				
//			}
//		}
		int cornerX = (int) ((x + CORNER_OFFSETS[corner][0] * this.width) * scaleX)
		        - (cornerSize / 2);
		int cornerY = (int) ((y + CORNER_OFFSETS[corner][1] * this.height) * scaleY)
		        - (cornerSize / 2);
		return new IntPair(cornerX, cornerY);
	}

	public int getCreatingDragCorner() {
		return CORNER_RIGHT_BOTTOM;
	}

	public Cursor getCornerCursor(int corner) {
		if (corner >= 0 && corner < CORNER_CURSORS.length) {
			return Cursor.getPredefinedCursor(CORNER_CURSORS[corner]);
		}
		else {
			return Cursor.getDefaultCursor();
		}
	}

	public int drag(int corner, int offsetX, int offsetY) {
		if (corner == CORNER_NONE) { return corner; }

		int left = this.left;
		int top = this.top;
		int right = this.left + this.width;
		int bottom = this.top + this.height;

		switch (corner) {
		case CORNER_LEFT_TOP:
			left += offsetX;
			top += offsetY;
			break;
		case CORNER_LEFT_MIDDLE:
			left += offsetX;
			break;
		case CORNER_LEFT_BOTTOM:
			left += offsetX;
			bottom += offsetY;
			break;
		case CORNER_CENTER_TOP:
			top += offsetY;
			break;
		case CORNER_CENTER_MIDDLE:
			left += offsetX;
			right += offsetX;
			top += offsetY;
			bottom += offsetY;
			break;
		case CORNER_CENTER_BOTTOM:
			bottom += offsetY;
			break;
		case CORNER_RIGHT_TOP:
			right += offsetX;
			top += offsetY;
			break;
		case CORNER_RIGHT_MIDDLE:
			right += offsetX;
			break;
		case CORNER_RIGHT_BOTTOM:
			right += offsetX;
			bottom += offsetY;
			break;
		}

		this.left = Math.min(left, right);
		this.top = Math.min(top, bottom);
		this.width = Math.abs(right - left);
		this.height = Math.abs(bottom - top);

		int newCorner = corner;
		if (left >= right) {
			newCorner = reverseX(newCorner);
		}
		if (top >= bottom) {
			newCorner = reverseY(newCorner);
		}
		return newCorner;
	}

	public void confirmDrag() {
		if (width <= 5 && height <= 5) {
			JOptionPane.showMessageDialog(null, "矩形过小", "警告", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void move(int offsetX, int offsetY) {
		this.left += offsetX;
		this.top += offsetY;
	}

	public int getCenterX() {
		return this.left + this.width / 2;
	}

	public int getCenterY() {
		return this.top + this.height / 2;
	}

	public String getInfo() {
		String result = "Left：" + this.left + "  Top：" + this.top + "  Width：" + this.width
		        + "  Height：" + this.height;
		return result;
	}

	public void paintCorner(Graphics g) {
		paintCorner(g, CORNER_SIZE);
	}

	private void paintCorner(Graphics g, int cornerSize) {
		//System.out.println("paint corner");
		if(scaled){
			Graphics2D g2 = (Graphics2D) g;
			Composite oldComposite = g2.getComposite();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1));
			Color oldColor = g2.getColor();
			g2.setColor(getCornerColor());

			AffineTransform oldTransform = g2.getTransform();
			g2.scale(1.0 / scaleX, 1.0 / scaleY);
			for (int i = 0; i < CORNERS.length; ++i) {
				IntPair cornerXY = getCornerXY(CORNERS[i], cornerSize);
				int cornerX = cornerXY.x;
				int cornerY = cornerXY.y;
				g2.fillRect(cornerX, cornerY, cornerSize, cornerSize);
			}
			g2.setTransform(oldTransform);
			g2.setColor(oldColor);
			g2.setComposite(oldComposite);
		}else{
			Color oldColor = g.getColor();
			g.setColor(Color.BLACK);
			g.drawRect(left, top, width, height);
			g.setColor(oldColor);
		}
	}

	public Color getCornerColor() {
		return Color.BLACK;
	}

	public void paintSelected(Graphics g) {
		//System.out.println("00000000000000000");
		paintIdle(g);
		paintCorner(g);
	}
	
	public void setRoate(double roate)
	{
		this.roate = roate;
	}
	
	public double getRoate()
	{
		return this.roate;
	}
	
	public void paint(Graphics g) {
		if (!selected) {
			paintIdle(g);
		}
		else {
			
			paintSelected(g);
		}
	}
}

class RectPainter extends PainterPanel {
	public final static Color[] COLORS = { 
		Color.BLUE,
		Color.RED,
		Color.GREEN,
		new Color(0xFF880000),
		new Color(0xFF000088),
		new Color(0xFF008800),
		new Color(0xFFFFFF00),
		new Color(0xFF00FFFF),
		new Color(0xFFFF00FF),
	};
	
	public final static Color getColor(int index) {
		if(index < COLORS.length)
			return COLORS[index];
		else
			return new Color((int)(Math.random()*0x00FFFFFF));
	}

	public final static int W = 50;
	public final static int H = 50;

	public final static int[] TYPES = { LogicRect.RECT_BASE };

	public final static RectPainter[] getPainters() {
		RectPainter[] result = new RectPainter[TYPES.length];
		for (int i = 0; i < TYPES.length; ++i) {
			result[i] = new RectPainter(TYPES[i]);
			result[i].computeSize();
		}
		return result;
	}

	public static void paint(Graphics g, Color color, int left, int top, int width, int height ,double roate) {
		Graphics2D g2 = (Graphics2D) g;
		Composite oldComposite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5));
		AffineTransform oldTransform = g2.getTransform();
		
		if(roate != 0)
			g2.rotate(roate, left+width/2, top+height/2);
		//g2.setComposite(ScrollablePanel.ALPHA_COMPOSITE);
		
		Color oldColor = g.getColor();
		g.setColor(color);
		g.fillRect(left, top, width, height);
		g.setColor(oldColor);
		g2.setTransform(oldTransform);
		g2.setComposite(oldComposite);
	}

	int type;
	String name;
	Color color;
	
	public RectPainter(int type) {
		this.type = type;
		this.name = "";
		this.color = Color.BLUE;
	}

	public RectPainter(int type, String name, Color color) {
		this.type = type;
		this.name = name;
		this.color = color;
	}

	public int getGroupID() {
		return -1;
	}

	public int getID() {
		return type;
	}

	public int getType() {
		return type;
	}

	public int getImageWidth() {
		return W;
	}

	public int getImageHeight() {
		return H;
	}

	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}

	public void paintLeftTop(Graphics g, int left, int top) {
		//System.out.println("画矩形？");
		paint(g, color, left, top, W, H ,0);
	}

	public void paintOrigin(Graphics g, int originX, int originY) {
	// paintLeftTop(g, originX - W / 2, originY - H / 2);
	}
}

class LogicRect extends RectShape implements Copyable, Saveable {

	public final static LogicRect[] copyArray(LogicRect[] array) {
		LogicRect[] result = null;
		if (array != null) {
			result = new LogicRect[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyLogicRect();
			}
		}
		return result;
	}

	public final static LogicRect[] createArrayViaFile(DataInputStream in) throws Exception {
		LogicRect[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new LogicRect[length];
			for (int i = 0; i < length; ++i) {
				result[i] = createViaFile(in);
			}
		}
		return result;
	}

	public final static int DATA_LENGTH = 1;

	public final static int RECT_BASE = 0;

	public final static String[] NAMES = { "基本矩形" };

	public final static int[][] SUBTYPES = { { 0 } };

	public final static String[][] SUBTYPE_NAMES = { { "基本矩形" } };

	private int layer;
	private int id;
	private int type;
	private String name;
	private Color color;
	private int subType;
	private int logicID;
	private boolean isVisible;
	private int[] data = new int[DATA_LENGTH];
	private ArrayList<String> props;

	public LogicRect(int layer, int id, int type, int left, int top, int width, int height) {
		super(left, top, width, height);
		this.layer = layer;
		this.id = id;
		this.type = type;
		this.setName("");
		this.color = Color.BLUE;
		this.logicID = 0;
		this.subType = 0;
		this.isVisible = true;
		this.props = new ArrayList<String>();
		for (int i = 0; i < DATA_LENGTH; ++i) {
			data[i] = 0;
		}
	}
	
	public LogicRect(int layer, int id, int type, String name,
			Color color, int left, int top, int width, int height) {
		super(left, top, width, height);
		this.layer = layer;
		this.id = id;
		this.type = type;
		this.setName(name);
		this.color = color;
		this.logicID = 0;
		this.subType = 0;
		this.isVisible = true;
		this.props = new ArrayList<String>();
		for (int i = 0; i < DATA_LENGTH; ++i) {
			data[i] = 0;
		}
	}

	public void setProps(ArrayList<String> ps) {
		props.clear();
		for(int i=0; i<ps.size(); ++i) {
			props.add(new String(ps.get(i)));
		}
	}
	
	public ArrayList<String> getProps() {
		return props;
	}
	
	public StringBuffer getPropsLua() {
		StringBuffer s = new StringBuffer("");
		if(props.size() > 0) {
			for(int i=0; i<props.size()-1; ++i) {
				s.append(props.get(i));
				s.append(",");
			}
			s.append(props.get(props.size()-1));
		}
		
		return s;
	}

	public Copyable copy() {
		return copyLogicRect();
	}

	public final LogicRect copyLogicRect() {
		LogicRect result = new LogicRect(layer, id, type, left, top, width, height);
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(LogicRect source) {
		super.copyFrom(source);
		this.layer= source.layer;
		this.id = source.id;
		this.type = source.type;
		this.name = source.name;
		this.color = source.color;
		this.logicID = source.logicID;
		this.subType = source.subType;
		this.isVisible = source.isVisible;
		for (int i = 0; i < DATA_LENGTH; ++i) {
			this.data[i] = source.data[i];
		}
		for (int i = 0; i < props.size(); ++i) {
			this.props.add(source.props.get(i));
		}
	}

	public int compareTo(Object o) {
		if (o instanceof LogicRect) {
			LogicRect rect = (LogicRect) o;
			if (this.type != rect.type) {
				return this.type - rect.type;
			}
			else if (this.logicID != rect.logicID) {
				return this.logicID - rect.logicID;
			}
			else {
				return this.id - rect.id;
			}
		}
		return this.hashCode() - o.hashCode();
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
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLogicID() {
		return logicID;
	}

	public void setLogicID(int logicID) {
		this.logicID = logicID;
	}

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	public int getData(int index) {
		return data[index];
	}
	
	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		if(!isVisible)
			setSelected(false);
	}

	public void setData(int index, int value) {
		data[index] = value;
	}

	public String getInfo() {
		String result = name+"_"+id+"  type："+type+"  "+super.getInfo();
		return result;
	}

	public void paintIdle(Graphics g) {
		if(!isVisible)
			return;
		
		//System.out.println("draw rect");
		//RectPainter.paint(g, type, left, top, width, height ,this.getRoate());
		Graphics2D g2 = (Graphics2D) g;
		Composite oldComposite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5));
		AffineTransform oldTransform = g2.getTransform();
		
		if(this.getRoate() != 0)
			g2.rotate(this.getRoate(), left+width/2, top+height/2);
		//System.out.println(this.getRoate());
		//g2.setComposite(ScrollablePanel.ALPHA_COMPOSITE);
		
		Color oldColor = g.getColor();
		g.setColor(color);
		g.fillRect(left, top, width, height);
		g.setColor(oldColor);
		g2.setTransform(oldTransform);
		g2.setComposite(oldComposite);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		SL.writeInt(type, out);
		SL.writeInt(subType, out);
		SL.writeInt(left, out);
		SL.writeInt(top, out);
		SL.writeInt(width, out);
		SL.writeInt(height, out);
		SL.writeDouble(this.getRoate()*50, out);
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(layer);
		out.writeInt(id);
		out.writeInt(type);
		SL.writeString(name, out);
		out.writeInt(color.getRGB());
		out.writeInt(left);
		out.writeInt(top);
		out.writeInt(width);
		out.writeInt(height);
		out.writeInt(logicID);
		out.writeInt(subType);
		out.writeDouble(this.getRoate()*50);
		for (int i = 0; i < DATA_LENGTH; ++i) {
			out.writeInt(data[i]);
		}
		
		out.writeInt(props.size());
		for(int i=0; i<props.size(); ++i) {
			SL.writeString(props.get(i), out);
		}
	}

	public final static LogicRect createViaFile(DataInputStream in) throws Exception {
		LogicRect result = new LogicRect(0, 0, 0, 0, 0, 0, 0);
		result.load(in);
		return result;
	}

	private void load(DataInputStream in) throws Exception {
		layer = in.readInt();
		id = in.readInt();
		type = in.readInt();
		name = SL.readString(in);
		color = new Color(in.readInt());
		left = in.readInt();
		top = in.readInt();
		width = in.readInt();
		height = in.readInt();
		logicID = in.readInt();
		subType = in.readInt();
		this.setRoate(in.readDouble()/50);
		for (int i = 0; i < DATA_LENGTH; ++i) {
			data[i] = in.readInt();
		}
		
		ArrayList<String> _props = new ArrayList<String>();
		int plen = in.readInt();
		for(int j=0; j<plen; ++j) {
			_props.add(SL.readString(in));
		}
		setProps(_props);
	}

	@Override
	public void roate(int y, int x) {
		// TODO Auto-generated method stub
		
		if(this.getselect() && this.getrpress())
		{
			//System.out.println("旋转角度改变");
			double roate = this.getRoate();
			if(x< (left+width/2))
			{
				//System.out.println(x);
				roate = roate - 0.01*y;
			}
			else
				roate = roate + 0.01*y;
			
			this.setRoate(roate);
		//	System.out.println(roate);
		}
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return layer;
	}

	@Override
	public void setLayer(int layer) {
		// TODO Auto-generated method stub
		this.layer = layer;
	}

	public int getLayer2() {
		// TODO Auto-generated method stub
		return layer;
	}
}

class RectManager extends ShapeManager {
	void childMousePressed(MouseEvent e){};
	
	public RectManager(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) {
		super(scrollablePanel, mouseInfo);
	}

	protected Shape createShape(int x, int y) {
	//	System.out.println("create shape");
		if (!(mouseInfo.getPainter() instanceof RectPainter)) { return null; }
		if(mouseInfo.getLayer() < 0) {return null;}
		if(!MainFrame.self.getLayerManager().getLayerIsShow(mouseInfo.getLayer())) {return null;}
		
		RectPainter painter = (RectPainter) mouseInfo.getPainter();
		return new LogicRect(mouseInfo.getLayer(), useMaxID(), painter.getType(), 
				painter.getName(), painter.getColor(), x, y, 0, 0);
	}
	
	public void paintShapesOther(Graphics g){
		
	}
	
	public void saveLua(StringBuffer buffer, int layer, int tabNum)
	{
		int realx = MainFrame.self.getMapInfo().getRealLeft();
		int realy = MainFrame.self.getMapInfo().getRealTop();
		StringBuffer maprectLua = new StringBuffer();
	//	maprectLua.append("c="+shapes.size()+",\r\n");
		maprectLua.append("\r\n");
		for (int i = 0; i < shapes.size(); ++i) {
			LogicRect rect = (LogicRect) (shapes.get(i));
			if(rect.getLayer2() == layer) {
				maprectLua.append(XUtil.getTabs(tabNum+1));
				maprectLua.append("{");
				maprectLua.append("id="+rect.getID());
				maprectLua.append(",type="+String.valueOf(rect.getType()));
				maprectLua.append(",name=\""+rect.getName()+"\"");
				maprectLua.append(",x="+String.valueOf(rect.left - realx));
				maprectLua.append(",y="+String.valueOf(rect.top - realy));
				maprectLua.append(",w="+String.valueOf(rect.width));			
				maprectLua.append(",h="+String.valueOf(rect.height));
				maprectLua.append(",r="+String.valueOf((float)(rect.getRoate()*50)));
				maprectLua.append(",props={"+rect.getPropsLua());
//				maprectLua.append(",data={c="+LogicRect.DATA_LENGTH);
//				for(int j=0 ;j<LogicRect.DATA_LENGTH ;j++)
//				{
//					maprectLua.append(",["+String.valueOf(j+1)+"]="+rect.getData(j));
//				}
				maprectLua.append("}},\r\n");
			}
		}
		
		buffer.append(XUtil.getTabs(tabNum));
		buffer.append("rects={");
		buffer.append(maprectLua);
		buffer.append(XUtil.getTabs(tabNum));
		buffer.append("},\r\n");
	}
	
	public void sortMobileRect(ArrayList rects) {
		if (rects == null) return;
		if (rects.size() <= 0) return;

		LogicRect[] tmp = new LogicRect[rects.size()];
		for (int i = 0; i < rects.size(); ++i) {
			tmp[i] = (LogicRect) (rects.get(i));
		}

		for (int i = 0; i < tmp.length - 1; ++i) {
			for (int j = i + 1; j < tmp.length; ++j) {
				if (tmp[i].getLogicID() > tmp[j].getLogicID()) {
					LogicRect l = tmp[j];
					tmp[j] = tmp[i];
					tmp[i] = l;
				}
			}
		}
		rects.clear();
		for (int i = 0; i < tmp.length; ++i) {
			rects.add(tmp[i]);
		}
	}

	protected void showProperties(Shape shape) {
		if (shape != null) {
			if (shape instanceof LogicRect) {
				LogicRect rect = (LogicRect) shape;
				TilePropDialog setter;
				if(scrollablePanel instanceof ShapeManagerPanel) {
					ShapeManagerPanel panel = (ShapeManagerPanel)scrollablePanel;
					if(panel.getFrameOwner() != null) {
						setter = new TilePropDialog(panel.getFrameOwner(), rect.getProps());
					}
					else {
						setter = new TilePropDialog(panel.getDialogOwner(), rect.getProps());
					}
				}
				else {
					setter = new TilePropDialog(MainFrame.self, rect.getProps());
				}
				
				setter.setTitle("设置矩形属性");
				setter.show();
				scrollablePanel.repaint();
				selectionChanged();
			}
		}
	}

	public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception {
		ArrayList ra = new ArrayList();
		for (int i = 0; i < shapes.size(); ++i) {
			LogicRect rect = (LogicRect) (shapes.get(i));
			ra.add(rect);
		}

		sortMobileRect(ra);
		SL.saveArrayMobile(ra, out);
	}

	public void save(DataOutputStream out, Object[] resManagers) throws Exception {
		resortShapes();

		out.writeInt(shapes.size());
		for (int i = 0; i < shapes.size(); ++i) {
			LogicRect rect = (LogicRect) (shapes.get(i));
			rect.save(out);
		}
	}

	public void load(DataInputStream in, Object[] resManagers) throws Exception {
		reset();
		int length = in.readInt();
		for (int i = 0; i < length; ++i) {
			LogicRect rect = LogicRect.createViaFile(in);
			addShape(rect);
		}
	}

	@Override
	public void paintStatic(Graphics g, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveMobile(DataOutputStream out, int layer) throws Exception {
		// TODO Auto-generated method stub
		
	}
}

class RectPanel extends ShapeManagerPanel {

	public RectPanel(JFrame owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	public RectPanel(JDialog owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	protected ShapeManager createManager() {
		return new RectManager(this, mouseInfo);
	}
}

class RectPainterData {
	
	int type;
	String name;
	Color color;
	
	public RectPainterData(int type, String name, Color color) {
		this.type = type;
		this.name = name;
		this.color = color;
	}
	
	public RectPainterData() {
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}

class RectPainterTree extends PainterTree implements MouseListener, ActionListener {

	ArrayList<RectPainterData> datas;
	int maxId;
	
	JPopupMenu popMenu;
	JMenuItem addMenu;
	JMenuItem removeMenu;
	
	public RectPainterTree(String name, MouseInfo mouseInfo) {
		super(name, null, null, mouseInfo);
		
		datas = new ArrayList<RectPainterData>();
		maxId = 0;
		
		popMenu = new JPopupMenu();
		
		addMenu = new JMenuItem("添加");
		addMenu.addActionListener(this);
		removeMenu = new JMenuItem("删除");
		removeMenu.addActionListener(this);
		
		popMenu.add(addMenu);
		popMenu.add(removeMenu);
		
		addMouseListener(this);
		
		// test
//		addPainter(getMaxId(), "矩形", Color.RED);
	}
	
	public Painter[] getPainters() {
		RectPainter[] result = new RectPainter[datas.size()];
		for (int i = 0; i < datas.size(); ++i) {
			RectPainterData data = datas.get(i);
			result[i] = new RectPainter(data.type, data.name, data.color);
			result[i].computeSize();
		}
		return result;
	}
	
	public int getMaxId() {
		return maxId++;
	}
	
	public void addPainter(int type, String name, Color color) {
		datas.add(new RectPainterData(type, name, color));
		refresh(null, getPainters());
	}
	
	public void removePainter(int type) {
		for(int i=0; i<datas.size(); ++i) {
			if(datas.get(i).type == type) {
				datas.remove(i);
				refresh(null, getPainters());
				break;
			}
		}
	}
	
	public void save() throws Exception {
		File f = new File(XUtil.getDefPropStr("SavePath") + "\\rect.dat");
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
		        new FileOutputStream(f)));
		
		out.writeInt(maxId);
		out.writeInt(datas.size());
		for(int i=0; i<datas.size(); ++i) {
			RectPainterData data = datas.get(i);
			out.writeInt(data.type);
			SL.writeString(data.name, out);
			out.writeInt(data.color.getRGB());
		}
		out.flush();
		out.close();
	}

	public void load() throws Exception {
		File f = new File(XUtil.getDefPropStr("SavePath") + "\\rect.dat");
		if (f.exists()) {
			DataInputStream in = new DataInputStream(
			        new BufferedInputStream(new FileInputStream(f)));
			
			maxId = in.readInt();
			int len = in.readInt();
			for(int i=0; i<len; ++i) {
				int type = in.readInt();
				String name = SL.readString(in);
				Color color = new Color(in.readInt());
				addPainter(type, name, color);
			}
			in.close();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == XUtil.RIGHT_BUTTON) {
			popMenu.show(this, e.getX(), e.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == addMenu) {
			RectPropDialog setter = new RectPropDialog(MainFrame.self);
			setter.setTitle("新建逻辑矩形");
			setter.show();
			if(setter.closeType == RectPropDialog.OK_PERFORMED) {
				RectPainterData data = new RectPainterData();
				setter.updateRectInfo(data);
				
				addPainter(getMaxId(), data.name, data.color);
			}
		}
		else if(e.getSource() == removeMenu) {
			MouseInfo mouseInfo = MainFrame.self.getMouseInfo();
			if (!(mouseInfo.getPainter() instanceof RectPainter)) { return; }
			
			RectPainter painter = (RectPainter) mouseInfo.getPainter();
			if(painter.getType() >= 0) {
				removePainter(painter.getType());
			}
		}
	}
	
}

class RectPropDialog extends OKCancelDialog {

	private JTextField nameText;
	private JButton colorButton;

	public RectPropDialog(JFrame owner) {
		super(owner);
		init();
	}

	private void init() {
		nameText = new JTextField("");
		colorButton = new JButton("设置颜色");
		colorButton.setBackground(Color.BLUE);
		colorButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				chooseColor();
			}
		});

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0;
		c.insets = new Insets(5, 5, 5, 5);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		centerPanel.add(new JLabel("名称："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(nameText, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		centerPanel.add(new JLabel("背景色："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(colorButton, c);
		
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(centerPanel, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void chooseColor() {
		Color color = JColorChooser.showDialog(this, "选择颜色", colorButton.getBackground());
		if (color != null) {
			colorButton.setBackground(color);
		}
	}

	public void setRectInfo(RectPainterData data) {
		colorButton.setBackground(data.color);
		nameText.setText(data.name);
	}

	public void updateRectInfo(RectPainterData data) {
		data.setName(nameText.getText());
		data.setColor(colorButton.getBackground());
	}

	public void okPerformed() {
		closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}
}

class RectPropSetter extends OKCancelDialog {

	private LogicRect rect;
	private NumberSpinner logicIDSpinner;
	private ValueChooser subTypeChooser;
	private NumberSpinner[] dataSpinners = new NumberSpinner[LogicRect.DATA_LENGTH];

	public RectPropSetter(JFrame owner, LogicRect rect) {
		super(owner);
		init(rect);
	}

	public RectPropSetter(JDialog owner, LogicRect rect) {
		super(owner);
		init(rect);
	}

	private void init(LogicRect rect) {
		this.setTitle("设置逻辑矩形的属性");
		this.rect = rect;
		logicIDSpinner = new NumberSpinner();
		logicIDSpinner.setIntValue(rect.getLogicID());
		subTypeChooser = new ValueChooser(rect.getSubType(), LogicRect.SUBTYPES[rect.getType()],
		        LogicRect.SUBTYPE_NAMES[rect.getType()]);
		for (int i = 0; i < LogicRect.DATA_LENGTH; ++i) {
			dataSpinners[i] = new NumberSpinner();
			dataSpinners[i].setIntValue(rect.getData(i));
		}

		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(2, 2, 3, 3);
		p.add(new JLabel("逻辑ID："), c);

		c.weightx = 1;
		c.gridx = 1;
		p.add(logicIDSpinner, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(2, 2, 3, 3);
		p.add(new JLabel("具体类型："), c);

		c.weightx = 1;
		c.gridx = 1;
		p.add(subTypeChooser, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		String title = "无意义数据：";
		p.add(new JLabel(title), c);

		c.gridx = 1;
		c.weightx = 1;
		p.add(dataSpinners[0], c);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(p, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void okPerformed() {
		try {
			int logicID = logicIDSpinner.getIntValue();
			int subType = subTypeChooser.getValue();
			int[] data = new int[LogicRect.DATA_LENGTH];
			for (int i = 0; i < LogicRect.DATA_LENGTH; ++i) {
				data[i] = dataSpinners[i].getIntValue();
			}
			rect.setLogicID(logicID);
			rect.setSubType(subType);
			for (int i = 0; i < LogicRect.DATA_LENGTH; ++i) {
				rect.setData(i, data[i]);
			}
			this.closeType = OK_PERFORMED;
			dispose();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, "未知错误\n" + e, "保存出错", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void cancelPerformed() {
		dispose();
	}
}