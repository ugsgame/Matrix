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

class PointPainter extends PainterPanel {

	public final static int W = 4;
	public final static int H = 4;
	public final static int GROUPSNUM = LogicPoint.GROUPNAMES.length;

	//public final static int[] TYPES = { LogicPoint.LINE_U, LogicPoint.LINE_X, LogicPoint.LINE_L, LogicPoint.LINE_R, LogicPoint.LINE_Y };

	public final static PointPainter[] getPainters() {
		int length = LogicPoint.NAMES.length;
		PointPainter[] result = new PointPainter[GROUPSNUM*length];
		for (int i = 0; i < GROUPSNUM; ++i) {
			for(int j = 0; j < length; ++j){
				result[i*length + j] = new PointPainter(i, j);
				result[i*length + j].computeSize();
			}
		}
		return result;
	}
	
	public final static SIGroup[] getGroups() {
		SIGroup[] result = new SIGroup[GROUPSNUM];
		for (int i = 0; i < GROUPSNUM; ++i) {
			result[i] = new SIGroup(i, "路径"+Integer.toString(i+1), 0);
		}
		return result;
	}

	public static void paint(Graphics g, int type, int x, int y) {
		Color oldColor = g.getColor();	
		
		Color color = LogicPoint.POINT_COLORS[type];
		g.setColor(color);
		g.fillRect(x-W/2, y-H/2, W, H);
		g.setColor(oldColor);		
	}

	int type;
	int group;

	public PointPainter(int group, int type) {
		this.type = type;
		this.group = group;
	}

	public int getGroupID() {
		return this.group;
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
		return LogicPoint.NAMES[type];
	}

	public void paintLeftTop(Graphics g, int left, int top) {
		int x = left+W/2;
		int y = top+H/2;
		paint(g, type, x, y);
	}

	public void paintOrigin(Graphics g, int originX, int originY) {
	// paintLeftTop(g, originX - W / 2, originY - H / 2);
	}
}

class LogicPoint extends PointShape implements Copyable, Saveable {

	public final static LogicPoint[] copyArray(LogicPoint[] array) {
		LogicPoint[] result = null;
//		if (array != null) {
//			result = new LogicPoint[array.length];
//			for (int i = 0; i < array.length; ++i) {
//				result[i] = array[i].copyLogicLine();
//			}
//		}
		return result;
	}

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

	public final static int DATA_LENGTH = 2;

	private final static double LINE_X_K = 0.5;
	
	public final static Color[] POINT_COLORS = { 
			Color.RED, //Color.RED,
			new Color(0xFF880000), //new Color(0xFF880000),
			Color.BLUE, //Color.BLUE,
			new Color(0xFF000088),// new Color(0xFF000088),  
			Color.GREEN, //Color.GREEN,
			new Color(0xFF008800),// new Color(0xFF008800),
			new Color(0xFFFFFF00), //new Color(0xFFFFFF00), 
			new Color(0xFF00FFFF), //new Color(0xFF00FFFF), 
			new Color(0xFFFF00FF),
	};

	public final static String[] NAMES = { 
		"分支1" , "分支2", "分支3", "分支4",
		"分支5" , "分支6", "分支7", "分支8",
		"分支9" , 
	//	"分支10", "分支11", "分支12",
	//	"分支13" , "分支14", "分支15", "分支16",
	};
	public final static String[] GROUPNAMES = { 
		"路径1" , "路径2", "路径3", "路径4", "路径5", 
		"路径6" , "路径7", "路径8", "路径9", "路径10", 
	};

	//public final static int[][] SUBTYPES = { { 0 }, { 0 }, { 0 }, { 0 }, {0} };
	public final static int[][] SUBTYPES = { {0}, { 0 }, { 0 } };

	//public final static String[][] SUBTYPE_NAMES = { { "普通地板" }, { "普通斜坡" }, { "普通垂线" }, { "普通垂线" }, {""}};
	public final static String[][] SUBTYPE_NAMES = { { "分支1" }, { "分支2" }, { "分支3" }, { "分支4" } };

	private int id;
	private int type;
	private int group;
	private int subType;
	private int logicID;
	private int[] data = new int[DATA_LENGTH];

	public LogicPoint(int id, int group, int type, int x, int y) {
		super(x, y);
		this.id = id;
		this.type = type;
		this.group = group;
		this.logicID = id;
		this.subType = 0;
		for (int i = 0; i < DATA_LENGTH; ++i) {
			if(i == 1)
				data[i] = 0;
			else
				data[i] = 1;
		}
	}

	public Copyable copy() {
		return copyLogicLine();
	//	return null;
	}

	public final LogicPoint copyLogicLine() {
		LogicPoint result = new LogicPoint(id, group, type, x, y);
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(LogicPoint source) {
		super.copyFrom(source);
		this.id = source.id;
		this.type = source.type;
		this.group = source.group;
		this.logicID = source.logicID;
		this.subType = source.subType;
		for (int i = 0; i < DATA_LENGTH; ++i) {
			this.data[i] = source.data[i];
		}
	}

	public int compareTo(Object o) {
		if (o instanceof LogicPoint) {
			LogicPoint line = (LogicPoint) o;
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
	
	public int getGroupID() {
		return group;
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

	public void setData(int index, int value) {
		data[index] = value;
	}

	public int drag(int corner, int offsetX, int offsetY) {
		return super.drag(offsetX, offsetY);
	}

	public String getInfo() {
		String result = GROUPNAMES[group] + "   " + NAMES[type] + "   逻辑ID：" + logicID + "   " + super.getInfo();
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
	//	result = result + "  " + SUBTYPE_NAMES[type][subType];
		return result;
	}

	public void paintIdle(Graphics g) {
		PointPainter.paint(g, type, x, y);
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
		out.writeInt(group);
		out.writeInt(type);
		out.writeInt(x);
		out.writeInt(y);
		out.writeInt(logicID);
		out.writeInt(subType);
		for (int i = 0; i < DATA_LENGTH; ++i) {
			out.writeInt(data[i]);
		}
	}

	public final static LogicPoint createViaFile(DataInputStream in) throws Exception {
		LogicPoint result = new LogicPoint(0, 0, 0, 0, 0);
		result.load(in);
		return result;
	}

	private void load(DataInputStream in) throws Exception {
		id = in.readInt();
		group = in.readInt();
		type = in.readInt();
		x = in.readInt();
		y = in.readInt();
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

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLayer(int layer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setVisible(boolean isVisible) {
		// TODO Auto-generated method stub
		
	}
}

class PointRoad{
	private int id;
	private int index;
	private int logicId;

	public PointRoad(int id, int logicId) {
		this.id = id;
		this.logicId = logicId;
		this.index = -1;
	}

	public int getID() {
		return id;
	}

	public int getLogicID() {
		return logicId;
	}

	public void setLogicID(int logicId) {
		this.logicId = logicId;
	}
}

class PointManager extends ShapeManager {
	private ArrayList[][] roadData = new ArrayList[LogicPoint.GROUPNAMES.length][LogicPoint.NAMES.length];
	
	void childMousePressed(MouseEvent e){};
	
	public PointManager(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) {
		super(scrollablePanel, mouseInfo);
		
		for(int i = 0; i < LogicPoint.GROUPNAMES.length; ++i){
			for(int j = 0; j < LogicPoint.NAMES.length; ++j){
				roadData[i][j] = new ArrayList();
			}
		}
	}
	
	protected void InsertRoadPoint(LogicPoint point, int group, int type){
		ArrayList list = roadData[group][type];
		
	//	PointRoad point = new PointRoad(id, logicId);
		int length = list.size();
		if(length > 0){
			for(int i=0; i<length; ++i){
				LogicPoint tmp = (LogicPoint)list.get(i);
				if( tmp.getLogicID() > point.getLogicID() ){
					list.add(i, point);
					break;
				}
				if(i == length-1){
					list.add(point);
					break;
				}
			}
		}
		else{
			list.add(point);
		}
	}
	
	protected void RemoveRoadPoint(int id){
		
	}
	
	protected void SetRoadPoint(int LogicId){
		
	}
	
	protected void DrawRoadPoint(Graphics g){
//		for(int i = 0; i < LogicPoint.GROUPNAMES.length; ++i){
//			for(int j = 0; j < LogicPoint.NAMES.length; ++j){
//				ArrayList tmp = roadData[i][j];				
//				for(int n=0; n < tmp.size(); ++n){
//					Shape shape = (Shape)(tmp.get(n));
//					if(getShapePos(shape) < 0){
//						tmp.remove(n);
//				//		System.out.println("n: "+Integer.toString(n));
//					}
//				}
//				
//				for(int n=0; n < tmp.size()-1; ++n){
//				//	PointRoad point = (PointRoad)(tmp.get(n));
//					Shape shape = (Shape)(tmp.get(n));//(Shape) (shapes.get(point.getID()));
//				//	point = (PointRoad)(tmp.get(n+1));
//					Shape shape2 = (Shape)(tmp.get(n+1));//(Shape) (shapes.get(point.getID()));
//					g.drawLine(shape.getCenterX(), shape.getCenterY(), shape2.getCenterX(), shape2.getCenterY());
//				}
//			}
//		}
		
		int num = 0;
		ArrayList[] tmp = new ArrayList[LogicPoint.NAMES.length];
		for(int i = 0; i < LogicPoint.NAMES.length; ++i){
			tmp[i] = new ArrayList();
		}
		
		for(int i = 0; i < PointPainter.GROUPSNUM; ++i){
			if(num <= shapes.size()){
				for(int j = 0; j < shapes.size(); ++j) {
					LogicPoint point = (LogicPoint) (shapes.get(j));
					if(point.getGroupID() == i){
						int type = point.getType();
						int logicID = point.getLogicID();
						int pos = -1;
						ArrayList list = (ArrayList) tmp[type];
						if(list.size() > 0){
							if(logicID < ((LogicPoint)list.get(0)).getLogicID())
								pos = 0;
							else
								for(int m = 1; m < list.size(); ++m){
									if(logicID > ((LogicPoint)list.get(m-1)).getLogicID() 
										&& logicID < ((LogicPoint)list.get(m)).getLogicID()){
										pos = m;
									}
								}
						}
						
						if(pos == -1)
							pos = list.size();
						list.add(pos, point);
						num += 1;
					}
				}
				
				ArrayList tmp2 = new ArrayList();
				for(int n = 0; n < LogicPoint.NAMES.length; ++n){
					ArrayList list = (ArrayList) tmp[n];
					if(list.size() > 0)
						tmp2.add(list.get(0));
					for(int m=0; m < list.size()-1; ++m){
						Shape shape = (Shape)(list.get(m));//(Shape) (shapes.get(point.getID()));
						Shape shape2 = (Shape)(list.get(m+1));//(Shape) (shapes.get(point.getID()));
						g.drawLine(shape.getCenterX(), shape.getCenterY(), shape2.getCenterX(), shape2.getCenterY());
					}
					list.clear();
				}
				
				for(int n=0; n<tmp2.size(); ++n){
					LogicPoint point = (LogicPoint)(tmp2.get(n));
					int v = point.getData(0);
					v = v%10;
					tmp[v].add(point);
				}
				
				for(int n = 0; n < LogicPoint.NAMES.length; ++n){
					ArrayList list = (ArrayList) tmp[n];
					Color oldColor = g.getColor();
					Color color = LogicPoint.POINT_COLORS[n];
					g.setColor(color);	
					
					for(int m=0; m < list.size()-1; ++m){
						Shape shape = (Shape)(list.get(m));//(Shape) (shapes.get(point.getID()));
						Shape shape2 = (Shape)(list.get(m+1));//(Shape) (shapes.get(point.getID()));
						g.drawLine(shape.getCenterX(), shape.getCenterY(), shape2.getCenterX(), shape2.getCenterY());
					}
					g.setColor(oldColor);
					list.clear();
				}
			}
		}
	}

	protected Shape createShape(int x, int y) {
		if (!(mouseInfo.getPainter() instanceof PointPainter)) { return null; }
		PointPainter painter = (PointPainter) mouseInfo.getPainter();
		
		int id = useMaxID();
		LogicPoint point = new LogicPoint(id, painter.getGroupID(), painter.getType(), x, y);
		return point;
	}
	
	public void paintShapesOther(Graphics g){
		DrawRoadPoint(g);
		
		// test
//		for (int i = 0; i < shapes.size()-1; ++i) {
//			Shape shape = (Shape) (shapes.get(i));
//			Shape shape2 = (Shape) (shapes.get(i+1));
//			g.drawLine(shape.getCenterX(), shape.getCenterY(), shape2.getCenterX(), shape2.getCenterY());
//		}
	}

	protected void showProperties(Shape shape) {
		if (shape != null) {
			if (shape instanceof LogicPoint) {
				LogicPoint line = (LogicPoint) shape;
				PointPropSetter setter;
				if (scrollablePanel instanceof ShapeManagerPanel) {
					ShapeManagerPanel panel = (ShapeManagerPanel) scrollablePanel;
					if (panel.getFrameOwner() != null) {
						setter = new PointPropSetter(panel.getFrameOwner(), line);
					}
					else {
						setter = new PointPropSetter(panel.getDialogOwner(), line);
					}
				}
				else {
					setter = new PointPropSetter(MainFrame.self, line);
				}
				setter.show();
				scrollablePanel.repaint();
				selectionChanged();
			}
		}
	}

	public void sortMobileLine(ArrayList points, int type) {
		if (points == null) return;
		if (points.size() <= 0) return;

		LogicPoint[] tmp = new LogicPoint[points.size()];
		for (int i = 0; i < points.size(); ++i) {
			tmp[i] = (LogicPoint) (points.get(i));
		}

//		for (int i = 0; i < tmp.length - 1; ++i) {
//			for (int j = i + 1; j < tmp.length; ++j) {
//				boolean exchange = false;
//				switch(type) {
//				case LogicPoint.LINE_P:
//					//exchange = tmp[i].y1 > tmp[j].y1;
//					exchange = tmp[i].x1 > tmp[j].x1;
//					break;
//				case LogicPoint.LINE_H:
//					exchange = tmp[i].y1 > tmp[j].y1;
//				break;
//					/*
//				case LogicPoint.LINE_L:
//					exchange = tmp[i].x1 > tmp[j].x1;
//					break;
//				case LogicPoint.LINE_R:
//					exchange = tmp[i].x1 < tmp[j].x1;
//					break;
//					*/
//				}
//				if (exchange) {
//					LogicPoint l = tmp[j];
//					tmp[j] = tmp[i];
//					tmp[i] = l;
//				}
//			}
//		}
//		points.clear();
//		for (int i = 0; i < tmp.length; ++i) {
//			points.add(tmp[i]);
//		}
	}

	public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception {
//		ArrayList[] linesArray = new ArrayList[2];
//		for(int i = 0; i < linesArray.length; ++i) {
//			linesArray[i] = new ArrayList();
//		}
//		int[] types = new int[linesArray.length];
//		types[0] = LogicPoint.LINE_P;
//		types[1] = LogicPoint.LINE_H;
//		//types[1] = LogicPoint.LINE_L;
//		//types[2] = LogicPoint.LINE_R;
//		for(int typeIndex = 0; typeIndex < linesArray.length; ++typeIndex) {
//			ArrayList points = linesArray[typeIndex];			
//			for (int lineIndex = 0; lineIndex < shapes.size(); ++lineIndex) {
//				LogicPoint line = (LogicPoint) (shapes.get(lineIndex));
//				if(line.getType() == types[typeIndex]) {
//					points.add(line);
//				}
//			}
//			sortMobileLine(points, types[typeIndex]);
//			SL.saveArrayMobile(points, out);
//		}
	}
	
	public void saveLua(String name) throws Exception{
		if(shapes.size() <= 0)
			return;
		
		MapInfo mapinfo = MainFrame.self.getMapInfo();
		int left = mapinfo.getRealLeft();
		int top = mapinfo.getRealTop();
		int num = 0;
		int num2 = 0;
		
		ArrayList[] tmp = new ArrayList[LogicPoint.NAMES.length];
		for(int i = 0; i < LogicPoint.NAMES.length; ++i){
			tmp[i] = new ArrayList();
		}
		
		StringBuffer mapRoadLua = new StringBuffer();
		mapRoadLua.append("\r\n\r\n--Roads\r\nroads={\r\n");
		
		for(int i = 0; i < PointPainter.GROUPSNUM; ++i){
			if(num <= shapes.size()){
				num2 = 0;
				for(int j = 0; j < shapes.size(); ++j) {
					LogicPoint point = (LogicPoint) (shapes.get(j));
					if(point.getGroupID() == i){
						int type = point.getType();
						int logicID = point.getLogicID();
						int pos = -1;
						ArrayList list = (ArrayList) tmp[type];
						if(list.size() > 0){
							if(logicID < ((LogicPoint)list.get(0)).getLogicID())
								pos = 0;
							else
								for(int m = 1; m < list.size(); ++m){
									if(logicID > ((LogicPoint)list.get(m-1)).getLogicID() 
										&& logicID < ((LogicPoint)list.get(m)).getLogicID()){
										pos = m;
									}
								}
						}
						
						if(pos == -1)
							pos = list.size();
						list.add(pos, point);
						num += 1;
						num2 += 1;
					}
				}
				
				if(num2 > 0){
					mapRoadLua.append("\t["+(i+1)+"]={");
					int num3 = 0;
					for(int j=0; j<LogicPoint.NAMES.length; ++j){
						ArrayList list = (ArrayList) tmp[j];
							
						if(list.size() > 0){
							if(num3 > 0)
								mapRoadLua.append(",");
							
							num3 += 1;
							mapRoadLua.append(" ["+num3+"]="+"{");
							int main = ((LogicPoint)list.get(0)).getData(0);
							if(main > 10){
								mapRoadLua.append("m="+(main%10)+",");
								mapRoadLua.append("t="+((main-main%10)/10)+",");
							}
							else
								mapRoadLua.append("m="+main+",");
							for(int m = 0; m < list.size(); ++m){
								mapRoadLua.append("{");
								mapRoadLua.append("x="+Integer.toString(((LogicPoint)list.get(m)).getCenterX()-left)+",");
								mapRoadLua.append("y="+Integer.toString(((LogicPoint)list.get(m)).getCenterY()-top));
								mapRoadLua.append("}");
								if(m<list.size()-1)
									mapRoadLua.append(",");
							}
							mapRoadLua.append("}");
							list.clear();
						}
					}
					if(num == shapes.size())
						mapRoadLua.append(" }\r\n");
					else
						mapRoadLua.append(" },\r\n");;
				}
			}
		}
		mapRoadLua.append("}");
		
		String s = mapRoadLua.toString();
		String scriptPath = XUtil.getDefPropStr("ScriptPath");
		
		FileWriter writer = new FileWriter(scriptPath + "\\" + name + ".lua", true);
		writer.write(s);
		writer.flush();
		writer.close();
	}

	public void save(DataOutputStream out, Object[] resManagers) throws Exception {
		resortShapes();

		out.writeInt(shapes.size());
		for (int i = 0; i < shapes.size(); ++i) {
			LogicPoint line = (LogicPoint) (shapes.get(i));
			line.save(out);
		}
	}

	public void load(DataInputStream in, Object[] resManagers) throws Exception {
		reset();
		int length = in.readInt();
		for (int i = 0; i < length; ++i) {
			LogicPoint line = LogicPoint.createViaFile(in);
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

class PointPanel extends ShapeManagerPanel {

	public PointPanel(JFrame owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	public PointPanel(JDialog owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	protected ShapeManager createManager() {
		return new PointManager(this, mouseInfo);
	}
}

class PointPropSetter extends OKCancelDialog {

	private LogicPoint line;
	private NumberSpinner logicIDSpinner;
	private ValueChooser subTypeChooser;
	private NumberSpinner[] dataSpinners = new NumberSpinner[LogicPoint.DATA_LENGTH];

	public PointPropSetter(JFrame owner, LogicPoint line) {
		super(owner);
		init(line);
	}

	public PointPropSetter(JDialog owner, LogicPoint line) {
		super(owner);
		init(line);
	}

	private void init(LogicPoint line) {
		this.setTitle("设置逻辑点的属性");
		this.line = line;
		logicIDSpinner = new NumberSpinner();
		logicIDSpinner.setIntValue(line.getLogicID());
	//	subTypeChooser = new ValueChooser(line.getSubType(), LogicPoint.SUBTYPES[line.getType()],
	//	        LogicPoint.SUBTYPE_NAMES[line.getType()]);
		for (int i = 0; i < LogicPoint.DATA_LENGTH; ++i) {
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
	//	p.add(new JLabel("具体类型："), c);

		c.weightx = 1;
		c.gridx = 1;
	//	p.add(subTypeChooser, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		String title = "主分支：";
		p.add(new JLabel(title), c);

		c.gridx = 1;
		c.weightx = 1;
		p.add(dataSpinners[0], c);
		
//		c.gridx = 0;
//		c.gridy = 3;
//		c.weightx = 0;
//		title = "传送时间：";
//		p.add(new JLabel(title), c);
//
//		c.gridx = 1;
//		c.weightx = 1;
//		p.add(dataSpinners[1], c);

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
			int subType = 0;//subTypeChooser.getValue();
			int[] data = new int[LogicPoint.DATA_LENGTH];
			for (int i = 0; i < LogicPoint.DATA_LENGTH; ++i) {
				data[i] = dataSpinners[i].getIntValue();
			}
			line.setLogicID(logicID);
			line.setSubType(subType);
			for (int i = 0; i < LogicPoint.DATA_LENGTH; ++i) {
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