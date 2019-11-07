package editor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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

	public final static int CORNER_SIZE = 4;

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
	private double scaleX, scaleY;
	private boolean selected;

	public RectShape(int left, int top, int width, int height) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.scaleX = 1;
		this.scaleY = 1;
		this.selected = false;
	}

	public void copyFrom(RectShape source) {
		this.left = source.left;
		this.top = source.top;
		this.width = source.width;
		this.height = source.height;
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
		int cornerX = (int) ((this.left + CORNER_OFFSETS[corner][0] * this.width) * scaleX)
		        + (int)((CORNER_OFFSETS[corner][0]-1) * cornerSize);
		int cornerY = (int) ((this.top + CORNER_OFFSETS[corner][1] * this.height) * scaleY)
				+ (int)((CORNER_OFFSETS[corner][1]-1) * cornerSize);
		return new IntPair(cornerX, cornerY);
	}

	public int getCreatingDragCorner() {
		return CORNER_RIGHT_BOTTOM;
	}

	public int getCornerCorner(int x, int y, int lenght){
		if(lenght ==1){
			for (int i = 0; i < CORNERS.length; ++i) {
				IntPair cornerXY = getCornerXY(CORNERS[i], CORNER_SIZE);
				int cornerX = cornerXY.x;
				int cornerY = cornerXY.y;
				if(Tools.checkBoxInter(cornerX, cornerY, CORNER_SIZE, CORNER_SIZE, x, y, 0, 0)){
					return i;
				}
			}
		}else{
			if(Tools.checkBoxInter(left, top, width, height, x, y, 0, 0)){
				return 4;
			}
		}
		
		if(Tools.checkBoxInter(left, top, width, height, x, y, 0, 0)){
			return RectShape.CORNER_CENTER_MIDDLE-1;
		}
		return -1;
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
		String result = "Left：" + (this.left - FrameManager.ORIGIN_X) + "  Top："
		        + (this.top - FrameManager.ORIGIN_Y) + "  Width：" + this.width + "  Height："
		        + this.height;
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


class RectPainter extends PainterPanel {

	public final static int W = 50;
	public final static int H = 50;

	public final static RectPainter[] getPainters() {
		RectPainter[] result = new RectPainter[Frame.RECT_LUA_NAMES.length];
		for (int i = 0; i < result.length; ++i) {
			result[i] = new RectPainter(i);
			result[i].computeSize();
		}
		return result;
	}

	public static void paint(Graphics g, int type, int left, int top, int width, int height) {
		Graphics2D g2 = (Graphics2D) g;
		Composite oldComposite = g2.getComposite();
		g2.setComposite(ScrollablePanel.ALPHA_COMPOSITE);
		Color oldColor = g.getColor();
		Color color = Frame.RECT_COLORS[type];
		g.setColor(color);
		g.fillRect(left, top, width, height);
		g.setColor(oldColor);
		g2.setComposite(oldComposite);
	}

	int type;

	public RectPainter(int type) {
		this.type = type;
	}

	public int getGroupID() {
		return -999;
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
		return Frame.RECT_NAMES[type];
	}

	public void paintLeftTop(Graphics g, int left, int top) {
		paint(g, type, left, top, W, H);
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

	private int id;
	private int type;
	private int logicID;

	public LogicRect(int id, int type, int left, int top, int width, int height) {
		super(left, top, width, height);
		this.id = id;
		this.type = type;
		this.logicID = 0;
	}

	public Copyable copy() {
		return copyLogicRect();
	}

	public final LogicRect copyLogicRect() {
		LogicRect result = new LogicRect(id, type, left, top, width, height);
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(LogicRect source) {
		super.copyFrom(source);
		this.id = source.id;
		this.type = source.type;
		this.logicID = source.logicID;
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
		return Frame.RECT_NAMES[type];
	}

	public int getLogicID() {
		return logicID;
	}

	public void setLogicID(int logicID) {
		this.logicID = logicID;
	}

	public String getInfo() {
		String result = Frame.RECT_NAMES[type] + "  " + super.getInfo();
		return result;
	}

	public void paintIdle(Graphics g) {
		RectPainter.paint(g, type, left, top, width, height);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		// SL.writeInt(type, out);
		// SL.writeInt(subType, out);
		SL.writeInt(left - FrameManager.ORIGIN_X, out);
		SL.writeInt(top - FrameManager.ORIGIN_Y, out);
		SL.writeInt(width, out);
		SL.writeInt(height, out);
	}

	public void saveLua(StringBuffer s) {
		s.append("{l=");
		s.append(left - FrameManager.ORIGIN_X);
		s.append(",t=");
		s.append(top - FrameManager.ORIGIN_Y);
		s.append(",w=");
		s.append(width);
		s.append(",h=");
		s.append(height);
		s.append("}");
	}
	
	public void saveLuaX(StringBuffer s, int index) {
		int l = left - FrameManager.ORIGIN_X;
		int t = top  - FrameManager.ORIGIN_Y;
		int r = l + width;
		int b = t + height;
		s.append("["+index+"]=");
		s.append("{l=");
		s.append(l);
		s.append(",t=");
		s.append(t);
		s.append(",r=");
		s.append(r);
		s.append(",b=");
		s.append(b);
		s.append("}");
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(id);
		out.writeInt(type);
		out.writeInt(left - FrameManager.ORIGIN_X);
		out.writeInt(top - FrameManager.ORIGIN_Y);
		out.writeInt(width);
		out.writeInt(height);
		out.writeInt(logicID);
	}

	public final static LogicRect createViaFile(DataInputStream in) throws Exception {
		LogicRect result = new LogicRect(0, 0, 0, 0, 0, 0);
		result.load(in);
		return result;
	}

	private void load(DataInputStream in) throws Exception {
		id = in.readInt();
		type = in.readInt();
		left = in.readInt() + FrameManager.ORIGIN_X;
		top = in.readInt() + FrameManager.ORIGIN_Y;
		width = in.readInt();
		height = in.readInt();
		logicID = in.readInt();
	}
}

class RectManager extends ShapeManager {

	public RectManager(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) {
		super(scrollablePanel, mouseInfo);
	}

	protected Shape createShape(int x, int y) {
		if (!(mouseInfo.getPainter() instanceof RectPainter)) { return null; }
		RectPainter painter = (RectPainter) mouseInfo.getPainter();
		return new LogicRect(useMaxID(), painter.getType(), x, y, 0, 0);
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

	protected void showProperties(Shape shape) {}

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