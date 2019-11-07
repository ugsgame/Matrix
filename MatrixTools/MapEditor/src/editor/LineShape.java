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
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

abstract public class LineShape implements Shape {

	public final static int CORNER_LEFT_TOP = 1; // 左上
	public final static int CORNER_RIGHT_BOTTOM = 2; // 右下
	public final static int CORNER_MIDDLE = 3;// 中间

	private final static int CORNER_SIZE = 5;

	private final static int CONTAIN_OFFSET = 5;

	private final static int[] CORNERS = { CORNER_LEFT_TOP, CORNER_RIGHT_BOTTOM, CORNER_MIDDLE };

	private final static int[] CORNER_CURSORS = { Cursor.DEFAULT_CURSOR, Cursor.MOVE_CURSOR,
	        Cursor.MOVE_CURSOR, Cursor.MOVE_CURSOR };

	public int x1, y1, x2, y2;
	private double scaleX, scaleY;
	private boolean selected;

	LineShape(int x1, int y1, int x2, int y2) {

		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.scaleX = 1;
		this.scaleY = 1;
		this.selected = false;
	}

	public void copyFrom(LineShape source) {
		this.x1 = source.x1;
		this.y1 = source.y1;
		this.x2 = source.x2;
		this.y2 = source.y2;
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
		double x1 = this.x1 * scaleX;
		double y1 = this.y1 * scaleY;
		double x2 = this.x2 * scaleX;
		double y2 = this.y2 * scaleY;
		double offset = CONTAIN_OFFSET;

		if (x >= Math.min(x1, x2) - offset && x <= Math.max(x1, x2) + offset
		        && y >= Math.min(y1, y2) - offset && y <= Math.max(y1, y2) + offset) {

			if (x1 == x2 || y1 == y2) {// 垂线或者水平线
				inLineRange = true;
			}
			else {
				double k = (1.0 * (y2 - y1)) / (x2 - x1);
				double b = y1 - k * x1;
				double k1 = 1 / k;
				double b1 = y - k1 * x;
				double cx = (b1 - b) / (k - k1);
				double cy = k * cx + b;
				double dist = Math.pow((cx - x) * (cx - x) + (cy - y) * (cy - y), 0.5);
				if (dist <= offset) {
					inLineRange = true;
				}
				else {
					inLineRange = false;
				}
			}
		}
		else {
			inLineRange = false;
		}
		return (inLineRange || (selected && getCornerAtPoint(x, y) != CORNER_NONE));
	}

	public boolean inRect(int left, int top, int width, int height) {
		return (Math.min(x1, x2) >= left && Math.max(x1, x2) <= left + width
		        && Math.min(y1, y2) >= top && Math.max(y1, y2) <= top + height);
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
		double x = x1;
		double y = y1;
		switch (corner) {
		case CORNER_LEFT_TOP:
			x = x1;
			y = y1;
			break;
		case CORNER_RIGHT_BOTTOM:
			x = x2;
			y = y2;
			break;
		case CORNER_MIDDLE:
			x = (x1 + x2) / 2;
			y = (y1 + y2) / 2;
			break;
		}
		int cornerX = (int) (x * scaleX - cornerSize / 2);
		int cornerY = (int) (y * scaleY - cornerSize / 2);
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

		int nx1 = this.x1;
		int ny1 = this.y1;
		int nx2 = this.x2;
		int ny2 = this.y2;

		switch (corner) {
		case CORNER_LEFT_TOP:
			nx1 += offsetX;
			ny1 += offsetY;
			break;
		case CORNER_RIGHT_BOTTOM:
			nx2 += offsetX;
			ny2 += offsetY;
			break;
		case CORNER_MIDDLE:
			nx1 += offsetX;
			ny1 += offsetY;
			nx2 += offsetX;
			ny2 += offsetY;
			break;
		}

		int result = corner;
		boolean reverse = false;
		if (nx1 > nx2) {
			reverse = true;
		}
		else if (nx1 == nx2 && ny1 > ny2) {
			reverse = true;
		}

		if (reverse) {
			x1 = nx2;
			y1 = ny2;
			x2 = nx1;
			y2 = ny1;
			if (corner == CORNER_LEFT_TOP) {
				result = CORNER_RIGHT_BOTTOM;
			}
			else if (corner == CORNER_RIGHT_BOTTOM) {
				result = CORNER_LEFT_TOP;
			}
		}
		else {
			x1 = nx1;
			y1 = ny1;
			x2 = nx2;
			y2 = ny2;
		}
		return result;
	}

	public void confirmDrag() {
		if (Math.abs(x1 - x2) <= 5 && Math.abs(y1 - y2) <= 5) {
			JOptionPane.showMessageDialog(null, "线段长度过短", "警告", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void move(int offsetX, int offsetY) {
		x1 += offsetX;
		y1 += offsetY;
		x2 += offsetX;
		y2 += offsetY;
	}

	public int getCenterX() {
		return (x1 + x2) / 2;
	}

	public int getCenterY() {
		return (y1 + y2) / 2;
	}

	public String getInfo() {
		String result = "X1：" + x1 + "  Y1：" + y1 + "  X2：" + x2 + "  Y2：" + y2;
		return result;
	}

	public void paintCorner(Graphics g) {
		//System.out.println("draw line shape");
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
		for (int i = 0; i < CORNERS.length; ++i) {
			IntPair cornerXY = getCornerXY(CORNERS[i], cornerSize);
			int cornerX = cornerXY.x;
			int cornerY = cornerXY.y;
			g2.fillRect(cornerX, cornerY, cornerSize, cornerSize);
		}
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
}

class LinePainter extends PainterPanel {

	public final static int W = 50;
	public final static int H = 50;

	//public final static int[] TYPES = { LogicLine.LINE_U, LogicLine.LINE_X, LogicLine.LINE_L, LogicLine.LINE_R, LogicLine.LINE_Y };
	public final static int[] TYPES = { LogicLine.LINE_P, LogicLine.LINE_H};

	public final static LinePainter[] getPainters() {
		LinePainter[] result = new LinePainter[TYPES.length];
		for (int i = 0; i < TYPES.length; ++i) {
			result[i] = new LinePainter(TYPES[i]);
			result[i].computeSize();
		}
		return result;
	}

	public static void paint(Graphics g, int type, int x1, int y1, int x2, int y2) {
		Color oldColor = g.getColor();		

		g.setColor(Color.BLACK);
		if(type == LogicLine.LINE_P) {
		}
		
		Color color = Color.BLACK;
		switch (type) {
		case LogicLine.LINE_P:
		case LogicLine.LINE_H:
		//case LogicLine.LINE_X:
		//case LogicLine.LINE_L:
		//case LogicLine.LINE_R:
			color = Color.BLUE;
			break;
		default:
			color = Color.RED;
		}
		g.setColor(color);
		g.drawLine(x1, y1, x2, y2);
		
		g.setColor(oldColor);		
	}

	int type;

	public LinePainter(int type) {
		this.type = type;
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
		return LogicLine.NAMES[type];
	}

	public void paintLeftTop(Graphics g, int left, int top) {
		int x1 = left;
		int y1 = top;
		int x2 = left + W;
		int y2 = top + H;

		switch (type) {
		case LogicLine.LINE_P:
			/*
			x1 = left;
			y1 = top + H / 2;
			x2 = left + W;
			y2 = top + H / 2;
			*/
			x1 = left + W / 2;
			y1 = top;
			x2 = left + W / 2;
			y2 = top + H;
			break;
		case LogicLine.LINE_H:
			x1 = left;
			y1 = top + H / 2;
			x2 = left + W;
			y2 = top + H / 2;
		break;
			/*
		case LogicLine.LINE_X:
			x1 = left;
			y1 = top + H;
			x2 = left + W;
			y2 = top;
			break;
		case LogicLine.LINE_L:
		case LogicLine.LINE_R:
			x1 = left + W / 2;
			y1 = top;
			x2 = left + W / 2;
			y2 = top + H;
			break;
			*/
		}
		paint(g, type, x1, y1, x2, y2);
	}

	public void paintOrigin(Graphics g, int originX, int originY) {
	// paintLeftTop(g, originX - W / 2, originY - H / 2);
	}
}

class LogicLine extends LineShape implements Copyable, Saveable {

	public final static LogicLine[] copyArray(LogicLine[] array) {
		LogicLine[] result = null;
		if (array != null) {
			result = new LogicLine[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyLogicLine();
			}
		}
		return result;
	}

	public final static LogicLine[] createArrayViaFile(DataInputStream in) throws Exception {
		LogicLine[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new LogicLine[length];
			for (int i = 0; i < length; ++i) {
				result[i] = createViaFile(in);
			}
		}
		return result;
	}

	public final static int DATA_LENGTH = 2;

	public final static int LINE_P = 0;
	public final static int LINE_H = 1;
	//public final static int LINE_X = 1;
	//public final static int LINE_L = 2;
	//public final static int LINE_R = 3;
	//public final static int LINE_Y = 4;

	private final static double LINE_X_K = 0.5;

	//public final static String[] NAMES = { "地板线", "斜坡", "左垂线", "右垂线", "测试" };
	public final static String[] NAMES = { "分割线" , "水平分割线"};

	//public final static int[][] SUBTYPES = { { 0 }, { 0 }, { 0 }, { 0 }, {0} };
	public final static int[][] SUBTYPES = { {0}, { 0 } };

	//public final static String[][] SUBTYPE_NAMES = { { "普通地板" }, { "普通斜坡" }, { "普通垂线" }, { "普通垂线" }, {""}};
	public final static String[][] SUBTYPE_NAMES = { { "普通分割线" }, { "水平分割线" }};

	private int id;
	private int layer;
	private int type;
	private int subType;
	private int logicID;
	private boolean isVisible;
	private int[] data = new int[DATA_LENGTH];

	public LogicLine(int layer, int id, int type, int x1, int y1, int x2, int y2) {
		super(x1, y1, x2, y2);
		this.layer = layer;
		this.id = id;
		this.type = type;
		this.logicID = 0;
		this.subType = 0;
		this.isVisible = true;
		for (int i = 0; i < DATA_LENGTH; ++i) {
			data[i] = 0;
		}
	}

	public Copyable copy() {
		return copyLogicLine();
	}

	public final LogicLine copyLogicLine() {
		LogicLine result = new LogicLine(layer, id, type, x1, y1, x2, y2);
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(LogicLine source) {
		super.copyFrom(source);
		this.layer = source.layer;
		this.id = source.id;
		this.type = source.type;
		this.logicID = source.logicID;
		this.subType = source.subType;
		this.isVisible = source.isVisible;
		for (int i = 0; i < DATA_LENGTH; ++i) {
			this.data[i] = source.data[i];
		}
	}

	public int compareTo(Object o) {
		if (o instanceof LogicLine) {
			LogicLine line = (LogicLine) o;
			if (this.logicID != line.logicID) {
				return this.logicID - line.logicID;
			}
			else {
				return this.id - line.id;
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
		return NAMES[type];
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

	public int drag(int corner, int offsetX, int offsetY) {
		switch (type) {
		case LINE_P:
			if (corner != CORNER_MIDDLE) {
				offsetX = 0;
			}
			break;
		case LINE_H:
			if (corner != CORNER_MIDDLE )
			{
				offsetY = 0;
			}
		break;
			/*
		case LINE_X:
			if (corner != CORNER_MIDDLE) {
				if (Math.abs(y1 - y2) > 5) {// 小线段随便移动
					double k = LINE_X_K;
					if (((x1 - x2) * 1.0) / (y1 - y2) < 0) k = -k;
					int sx = x1;
					int sy = y1;
					int dx = x2;
					int dy = y2;
					if (corner == CORNER_LEFT_TOP) {
						sx = x2;
						sy = y2;
						dx = x1;
						dy = y1;
					}
					offsetY = (int) (((dx + offsetX - sx) * k) - (dy - sy));
				}
			}
			break;
		case LINE_L:
		case LINE_R:
			if (corner != CORNER_MIDDLE) {
				offsetX = 0;
			}
			break;
			*/
		}
		return super.drag(corner, offsetX, offsetY);
	}

	public String getInfo() {
		String result = NAMES[type] + "  LogicID：" + logicID + "  " + super.getInfo();
		switch (type) {
		/*
		case LINE_X:
			if (x1 == x2) {
				result = result + "  垂线";
			}
			else {
				double k = Math.abs(((y1 - y2) * 1.0) / (x1 - x2));
				String ks = "";
				if (k > 10000) {
					ks = ((int) k) + "";
				}
				else {
					ks = k + "";
					if (ks.length() > 6) {
						ks = ks.substring(0, 6);
					}
				}
				result = result + "  斜率：" + ks;
			}
			break;
			*/
		default:
			break;
		}
		result = result + "  " + SUBTYPE_NAMES[type][subType];
		return result;
	}

	public void paintIdle(Graphics g) {
		if(isVisible)
			LinePainter.paint(g, type, x1, y1, x2, y2);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		//调整顺序
		int x1 = this.x1;
		int y1 = this.y1;
		int x2 = this.x2;
		int y2 = this.y2;
		boolean reverse = false;
		if (this.x1 > this.x2) {
			reverse = true;
		}
		else if (this.x1 == this.x2 && this.y1 > this.y2) {
			reverse = true;
		}
		if (reverse) {
			x1 = this.x2;
			y1 = this.y2;
			x2 = this.x1;
			y2 = this.y1;
		}

		//保存数据
//		SL.writeInt(type, out);
//		SL.writeInt(subType, out);

		switch (type) {
		case LINE_P:
		case LINE_H:
		//case LINE_X:
		//case LINE_L:
		//case LINE_R:
			SL.writeInt(x1, out);
			SL.writeInt(y1, out);
			SL.writeInt(x2, out);
			SL.writeInt(y2, out);
			break;
		}
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(layer);
		out.writeInt(id);
		out.writeInt(type);
		out.writeInt(x1);
		out.writeInt(y1);
		out.writeInt(x2);
		out.writeInt(y2);
		out.writeInt(logicID);
		out.writeInt(subType);
		for (int i = 0; i < DATA_LENGTH; ++i) {
			out.writeInt(data[i]);
		}
	}

	public final static LogicLine createViaFile(DataInputStream in) throws Exception {
		LogicLine result = new LogicLine(0, 0, 0, 0, 0, 0, 0);
		result.load(in);
		return result;
	}

	private void load(DataInputStream in) throws Exception {
		layer = in.readInt();
		id = in.readInt();
		type = in.readInt();
		x1 = in.readInt();
		y1 = in.readInt();
		x2 = in.readInt();
		y2 = in.readInt();
		logicID = in.readInt();
		subType = in.readInt();
		for (int i = 0; i < DATA_LENGTH; ++i) {
			data[i] = in.readInt();
		}
	}

	@Override
	public void setRoate(double roate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getRoate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void Rpressed(boolean t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roate(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	public int getLayer2() {
		return layer;
	}
	
	public int getLayer() {
		return layer;
	}

	@Override
	public void setLayer(int layer) {
		// TODO Auto-generated method stub
		this.layer = layer;
	}
}

class LineManager extends ShapeManager {
	void childMousePressed(MouseEvent e){};
	
	public LineManager(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) {
		super(scrollablePanel, mouseInfo);
	}

	protected Shape createShape(int x, int y) {
		if (!(mouseInfo.getPainter() instanceof LinePainter)) { return null; }
		if(mouseInfo.getLayer() < 0) {return null;}
		if(!MainFrame.self.getLayerManager().getLayerIsShow(mouseInfo.getLayer())) {return null;}
		
		LinePainter painter = (LinePainter) mouseInfo.getPainter();
		return new LogicLine(mouseInfo.getLayer(), useMaxID(), painter.getType(), x, y, x, y);
	}
	
	public void paintShapesOther(Graphics g){
		
	}

	protected void showProperties(Shape shape) {
		if (shape != null) {
			if (shape instanceof LogicLine) {
				LogicLine line = (LogicLine) shape;
				LinePropSetter setter;
				if (scrollablePanel instanceof ShapeManagerPanel) {
					ShapeManagerPanel panel = (ShapeManagerPanel) scrollablePanel;
					if (panel.getFrameOwner() != null) {
						setter = new LinePropSetter(panel.getFrameOwner(), line);
					}
					else {
						setter = new LinePropSetter(panel.getDialogOwner(), line);
					}
				}
				else {
					setter = new LinePropSetter(MainFrame.self, line);
				}
				setter.show();
				scrollablePanel.repaint();
				selectionChanged();
			}
		}
	}

	public void sortMobileLine(ArrayList lines, int type) {
		if (lines == null) return;
		if (lines.size() <= 0) return;

		LogicLine[] tmp = new LogicLine[lines.size()];
		for (int i = 0; i < lines.size(); ++i) {
			tmp[i] = (LogicLine) (lines.get(i));
		}

		for (int i = 0; i < tmp.length - 1; ++i) {
			for (int j = i + 1; j < tmp.length; ++j) {
				boolean exchange = false;
				switch(type) {
				case LogicLine.LINE_P:
					//exchange = tmp[i].y1 > tmp[j].y1;
					exchange = tmp[i].x1 > tmp[j].x1;
					break;
				case LogicLine.LINE_H:
					exchange = tmp[i].y1 > tmp[j].y1;
				break;
					/*
				case LogicLine.LINE_L:
					exchange = tmp[i].x1 > tmp[j].x1;
					break;
				case LogicLine.LINE_R:
					exchange = tmp[i].x1 < tmp[j].x1;
					break;
					*/
				}
				if (exchange) {
					LogicLine l = tmp[j];
					tmp[j] = tmp[i];
					tmp[i] = l;
				}
			}
		}
		lines.clear();
		for (int i = 0; i < tmp.length; ++i) {
			lines.add(tmp[i]);
		}
	}

	public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception {
		ArrayList[] linesArray = new ArrayList[2];
		for(int i = 0; i < linesArray.length; ++i) {
			linesArray[i] = new ArrayList();
		}
		int[] types = new int[linesArray.length];
		types[0] = LogicLine.LINE_P;
		types[1] = LogicLine.LINE_H;
		//types[1] = LogicLine.LINE_L;
		//types[2] = LogicLine.LINE_R;
		for(int typeIndex = 0; typeIndex < linesArray.length; ++typeIndex) {
			ArrayList lines = linesArray[typeIndex];			
			for (int lineIndex = 0; lineIndex < shapes.size(); ++lineIndex) {
				LogicLine line = (LogicLine) (shapes.get(lineIndex));
				if(line.getType() == types[typeIndex]) {
					lines.add(line);
				}
			}
			sortMobileLine(lines, types[typeIndex]);
			SL.saveArrayMobile(lines, out);
		}
	}
	
	public void saveLua(StringBuffer buffer, int layer, int tabNum)
	{
		int realx = MainFrame.self.getMapInfo().getRealLeft();
		int realy = MainFrame.self.getMapInfo().getRealTop();
		StringBuffer maprectLua = new StringBuffer();
	//	maprectLua.append("c="+shapes.size()+",\r\n");
		maprectLua.append("\r\n");
		for (int i = 0; i < shapes.size(); ++i) {
			LogicLine line = (LogicLine) (shapes.get(i));
			if(line.getLayer() == layer) {
				maprectLua.append(XUtil.getTabs(tabNum+1));
				maprectLua.append("{");
				maprectLua.append("id="+line.getID());
				maprectLua.append(",x="+String.valueOf(line.x1-realx));
				maprectLua.append(",y="+String.valueOf(line.y1-realy));
				maprectLua.append(",x2="+String.valueOf(line.x2-realx));			
				maprectLua.append(",y2="+String.valueOf(line.y2-realy));
				maprectLua.append(",r="+String.valueOf((float)(line.getRoate()*50)));
				maprectLua.append(",logId="+String.valueOf(line.getLogicID()));
				maprectLua.append(",data={c="+LogicLine.DATA_LENGTH);
				for(int j=0 ;j<LogicLine.DATA_LENGTH ;j++)
				{
					maprectLua.append(",["+String.valueOf(j+1)+"]="+line.getData(j));
				}
				maprectLua.append("}},\r\n");
			}
		}
		
		buffer.append(XUtil.getTabs(tabNum));
		buffer.append("lines={");
		buffer.append(maprectLua);
		buffer.append(XUtil.getTabs(tabNum));
		buffer.append("},\r\n");
	}

	public void save(DataOutputStream out, Object[] resManagers) throws Exception {
		resortShapes();

		out.writeInt(shapes.size());
		for (int i = 0; i < shapes.size(); ++i) {
			LogicLine line = (LogicLine) (shapes.get(i));
			line.save(out);
		}
	}

	public void load(DataInputStream in, Object[] resManagers) throws Exception {
		reset();
		int length = in.readInt();
		for (int i = 0; i < length; ++i) {
			LogicLine line = LogicLine.createViaFile(in);
			addShape(line);
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

class LinePanel extends ShapeManagerPanel {

	public LinePanel(JFrame owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	public LinePanel(JDialog owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	protected ShapeManager createManager() {
		return new LineManager(this, mouseInfo);
	}
}

class LinePropSetter extends OKCancelDialog {

	private LogicLine line;
	private NumberSpinner logicIDSpinner;
	private ValueChooser subTypeChooser;
	private NumberSpinner[] dataSpinners = new NumberSpinner[LogicLine.DATA_LENGTH];

	public LinePropSetter(JFrame owner, LogicLine line) {
		super(owner);
		init(line);
	}

	public LinePropSetter(JDialog owner, LogicLine line) {
		super(owner);
		init(line);
	}

	private void init(LogicLine line) {
		this.setTitle("设置逻辑线的属性");
		this.line = line;
		logicIDSpinner = new NumberSpinner();
		logicIDSpinner.setIntValue(line.getLogicID());
		subTypeChooser = new ValueChooser(line.getSubType(), LogicLine.SUBTYPES[line.getType()],
		        LogicLine.SUBTYPE_NAMES[line.getType()]);
		for (int i = 0; i < LogicLine.DATA_LENGTH; ++i) {
			dataSpinners[i] = new NumberSpinner();
			dataSpinners[i].setIntValue(line.getData(i));
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
			int[] data = new int[LogicLine.DATA_LENGTH];
			for (int i = 0; i < LogicLine.DATA_LENGTH; ++i) {
				data[i] = dataSpinners[i].getIntValue();
			}
			line.setLogicID(logicID);
			line.setSubType(subType);
			for (int i = 0; i < LogicLine.DATA_LENGTH; ++i) {
				line.setData(i, data[i]);
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