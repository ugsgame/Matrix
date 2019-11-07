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

abstract public class GridShape implements Shape {
	private final static int[] CORNER_CURSORS = { Cursor.DEFAULT_CURSOR, Cursor.MOVE_CURSOR };

	public int x, y;
	protected double scaleX;

	protected double scaleY;
	private boolean selected;

	GridShape(int x, int y) {
		this.x = x;
		this.y = y;
		this.scaleX = 1;
		this.scaleY = 1;
		this.selected = false;
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
		double x2 = x1 + GridManager.w*scaleX;
		double y2 = y1 + GridManager.h*scaleY;

		if (x >= x1 && x <= x2
		        && y >= y1 && y <= y2)
				inLineRange = true;
		return (inLineRange || (selected && getCornerAtPoint(x, y) != CORNER_NONE));
	}
	
	public int getCornerAtPoint(int x, int y){
		return getCornerAtPoint(x, y, 4);
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
		return 0;
	}

	public void move(int offsetX, int offsetY) {
	}

	public int getCenterX() {
		return x+(int)(GridManager.w*0.5);
	}

	public int getCenterY() {
		return y+(int)(GridManager.h*0.5);
	}

	public String getInfo() {
		String result = "";
		return result;
	}

	public void paintCorner(Graphics g) {
		paintCorner(g, 4);
	}

	private void paintCorner(Graphics g, int cornerSize) {
		Graphics2D g2 = (Graphics2D) g;
		Composite oldComposite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1));
		Color oldColor = g2.getColor();
		g2.setColor(getCornerColor());
		AffineTransform oldTransform = g2.getTransform();
		g2.scale(1.0 / scaleX, 1.0 / scaleY);
		
		int cornerX = (int)(x*scaleX);
		int cornerY = (int)(y*scaleY);
		int w = (int)(GridManager.w*scaleX);
		int h = (int)(GridManager.h*scaleY);
		g2.fillRect(cornerX, cornerY, w, h);
			
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

class GridPainter extends PainterPanel {
	public final static int W = 50;
	public final static int H = 50;
	public final static int[] TYPES = { 0, 1, 2, 3, 4, 5 };
	public final static Color[] COLORS = { 
		Color.WHITE,
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
		"0", "1" , "2", "3", "4", "5",
	};

	public final static GridPainter[] getPainters(){
		GridPainter[] result = new GridPainter[TYPES.length];
		for (int i = 0; i < TYPES.length; ++i) {
			result[i] = new GridPainter(TYPES[i]);
			result[i].computeSize();
		}
		return result;
	}
	
	public final static SIGroup[] getGroups() {
		return null;
	}

	public static void paint(Graphics g, int type, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		Composite oldComposite = g2.getComposite();
		g2.setComposite(ScrollablePanel.ALPHA_COMPOSITE);
		Color oldColor = g.getColor();
		Color color = COLORS[type];
		g.setColor(color);
		g.fillRect(x, y, W, H);
		g.setColor(oldColor);
		g2.setComposite(oldComposite);
	}

	private static void swtich(int type2) {
		
	}

	int type;
	int group;

	public GridPainter(int type) {
		this.type = type;
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
		return NAMES[type];
	}

	public void paintLeftTop(Graphics g, int left, int top) {
		paint(g, type, left, top);
	}

	public void paintOrigin(Graphics g, int originX, int originY) {
	// paintLeftTop(g, originX - W / 2, originY - H / 2);
	}
}

class LogicGrid extends GridShape implements Saveable {

	public final static LogicGrid[] copyArray(LogicGrid[] array) {
		LogicGrid[] result = null;
//		if (array != null) {
//			result = new LogicGrid[array.length];
//			for (int i = 0; i < array.length; ++i) {
//				result[i] = array[i].copyLogicLine();
//			}
//		}
		return result;
	}

	public final static LogicGrid[] createArrayViaFile(DataInputStream in) throws Exception {
		LogicGrid[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new LogicGrid[length];
			for (int i = 0; i < length; ++i) {
				result[i] = createViaFile(in);
			}
		}
		return result;
	}

	private int type;

	public LogicGrid(int type, int x, int y) {
		super(x, y);
		this.type = type;
	}


	public int compareTo(Object o) {
		return this.hashCode() - o.hashCode();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int drag(int corner, int offsetX, int offsetY) {
		return super.drag(offsetX, offsetY);
	}

	public String getInfo() {
		return "";
	}

	public void paintIdle(Graphics g) {
		Color oldColor = g.getColor();	
		g.setColor(Color.BLACK);
		
		int x1 = (int)(x * scaleX);
		int y1 = (int)(y * scaleY);
		int w = (int)(GridManager.w*scaleX);
		int h = (int)(GridManager.h*scaleY);
		int dx = (int)(1/scaleX);
		int dy = (int)(1/scaleY);
		
		g.drawRect(x1, y1, w, h);
		if(type > 0){
			Graphics2D g2 = (Graphics2D) g;
			Composite oldComposite = g2.getComposite();
			g2.setComposite(ScrollablePanel.ALPHA_COMPOSITE);
			g2.setColor(GridPainter.COLORS[type]);
			AffineTransform oldTransform = g2.getTransform();
			g2.scale(1.0 / scaleX, 1.0 / scaleY);

			g2.fillRect(x1+dx, y1+dy, w-dx, h-dy);
				
			g2.setTransform(oldTransform);
			g2.setColor(oldColor);
			g2.setComposite(oldComposite);
		}
		
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
		out.writeInt(type);
		out.writeInt(x);
		out.writeInt(y);
	}

	public final static LogicGrid createViaFile(DataInputStream in) throws Exception {
		LogicGrid result = new LogicGrid(0, 0, 0);
		result.load(in);
		return result;
	}

	public void load(DataInputStream in) throws Exception {
		type = in.readInt();
		x = in.readInt();
		y = in.readInt();
	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public void setID(int id) {
		
	}

	@Override
	public String getName() {
		return null;
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

class GridBrush extends RectShape {
	private int id = 0;
	private int type = 0;

	public GridBrush(int id, int type, int left, int top, int width, int height) {
		super(left, top, width, height);
		
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

	public void setType(int type) {
		this.type = type;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setXY(int x, int y){
		this.left = x;
		this.top = y;
	}

	public String getName() {
		return null;
	}

	public void paintIdle(Graphics g) {
		GridPainter.paint(g, type, left, top);
	}

	public int compareTo(Object o) {
		return 0;
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

class GridManager extends ShapeManager {
	public static int MS_NORMAL = 0;
	public static int MS_NEW = 1;
	public static int MS_SELECTED = 2;
	
	private LogicGrid[][] gridData;
	private int rowNum = 0;
	private int colNum = 0;
	
	private ArrayList<GridBrush> brushs;
	private int bID = 0;
	private int bSelectIndex = 0;
	private int cornerDrag = 0;
	
	private int mouseX;
	private int mouseY;
	private int oldMouseX;
	private int oldMouseY;
	private int mouseState = 0;
	
	private ManagerAdapter managerListener;
	
	public static int w = 0;
	public static int h = 0;
	
	public GridManager(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) {
		super(scrollablePanel, mouseInfo);
		
		setSelectionMode(ShapeManager.NONE_SELECTION);
		
		brushs = new ArrayList<GridBrush>();
		managerListener = new ManagerAdapter() {
			public void keyChanged() {
		//		System.out.println("keyChanged");
			}

			public void mouseChanged(MouseEvent e) {
				selfMouseChanged(e);
			}

			public void selectionChanged() {
		//		System.out.println("selectionChanged");
			}
			
			public void spriteChanged() {
		//		System.out.println("spriteChanged");
			}
		};
		
		this.addListener(managerListener);
	}
	
	public void InitGrid(MapInfo mapInfo){
		rowNum = mapInfo.getGridWidth();
		colNum = mapInfo.getGridHeight();
		
		int mapw = mapInfo.getRealWidth();
		int maph = mapInfo.getRealHeight();
		int sx = mapInfo.getRealLeft();
		int sy = mapInfo.getRealTop();
		
		if(rowNum > 0 && colNum > 0) {
			h = (int)((maph-1)/rowNum)+1;
			w = (int)((mapw-1)/colNum)+1;
		}
		else {
			h = 0;
			w = 0;
		}
		
		gridData = new LogicGrid[rowNum][colNum];
		for(int i = 0; i < rowNum; ++i){
			gridData[i] = new LogicGrid[colNum];
			for(int j = 0; j < colNum; ++j){
				gridData[i][j] = new LogicGrid(0, sx+j*w, sy+i*h);
			}
		}
		
//		w = mapInfo.getGridWidth();
//		h = mapInfo.getGridHeight();
//		
//		int mapw = mapInfo.getRealWidth();
//		int maph = mapInfo.getRealHeight();
//		int sx = mapInfo.getRealLeft();
//		int sy = mapInfo.getRealTop();
//		
//		rowNum = (int)((maph-1)/h)+1;
//		colNum = (int)((mapw-1)/w)+1;
//		
//		gridData = new LogicGrid[rowNum][colNum];
//		for(int i = 0; i < rowNum; ++i){
//			gridData[i] = new LogicGrid[colNum];
//			for(int j = 0; j < colNum; ++j){
//				gridData[i][j] = new LogicGrid(0, sx+j*w, sy+i*h);
//			}
//		}
	}
	
	int getBrushMaxID(){
		bID += 1;
		return bID;
	}
	
	int getBrushID(){
		return bID;
	}
	
	void childMousePressed(MouseEvent e){
		
	}
	
	void selfMouseChanged(MouseEvent e){		
		int eID = e.getID();
		int buttonID = e.getButton();
		this.oldMouseX = this.mouseX;
		this.oldMouseY = this.mouseY;
		this.mouseX = this.scrollablePanel.getMouseX(e);
		this.mouseY = this.scrollablePanel.getMouseY(e);
		
		reSetMouseState();
		if(buttonID == 1 && e.getClickCount() >= 2)
			selfMouseDoubleClicked(e);
		else if(eID == MouseEvent.MOUSE_PRESSED){
			selfMousePressed(e);
		}else if(eID == MouseEvent.MOUSE_RELEASED){
			selfMouseReleased(e);
		}else if(eID == MouseEvent.MOUSE_DRAGGED){
			selfMouseDragged(e);
		}else if(eID == MouseEvent.MOUSE_MOVED){
			selfMouseMoved(e);
		}
	}
	
	private void reSetMouseState(){
		if(mouseInfo.getInfo() == MouseInfo.NEW_SPRITE){
			if (!(mouseInfo.getPainter() instanceof GridPainter)) { return; }
			
			if(mouseState == MS_SELECTED){
				GridBrush _brush = (GridBrush)brushs.get(bSelectIndex);
				_brush.setSelected(false);
				bSelectIndex = 0;
				mouseState = MS_NORMAL;
			}
		}
	}
	
	private void selfMousePressed(MouseEvent e){
		int buttonID = e.getButton();
		if(buttonID == 1){
			if(mouseInfo.getInfo() == MouseInfo.NEW_SPRITE){
				if (!(mouseInfo.getPainter() instanceof GridPainter)) { return; }
				
				mouseState = MS_NEW;
				
				GridPainter painter = (GridPainter) mouseInfo.getPainter();
				GridBrush brush = new GridBrush(getBrushMaxID(), painter.getType(), mouseX, mouseY, 0, 0);
				brush.setIsScaled(false);
				brushs.add(brush);
				mouseInfo.resetAll();
				scrollablePanel.repaint();
		//		System.out.println(brushs.size());
			}
			
			if(mouseState == MS_NORMAL){
				for(int i=0; i<brushs.size(); ++i){
					GridBrush brush = (GridBrush)brushs.get(i);
					if(brush.containPoint(mouseX, mouseY)){
						brush.setSelected(true);
						bSelectIndex = i;
						mouseState = MS_SELECTED;
						
						int x = mouseX-brush.getWidth()/2;
						int y = mouseY-brush.getHeight()/2;
						brush.setXY(x, y);
						
						mouseInfo.resetAll();
						scrollablePanel.repaint();
						return;
					}
				}
			}
		}
		else if(buttonID == 3){
			if(mouseState == MS_SELECTED){
				GridBrush brush = (GridBrush)brushs.get(bSelectIndex);
			//	if(!brush.containPoint(mouseX, mouseY)){
					brush.setSelected(false);
					bSelectIndex = 0;
					mouseState = MS_NORMAL;
					mouseInfo.resetAll();
					scrollablePanel.repaint();
			//	}
			}
		}		
	}
	
	private void selfMouseReleased(MouseEvent e){
		if(mouseState == MS_NEW){				
			int bID = getBrushID();
			for(int i=0; i<brushs.size(); ++i){
				GridBrush brush = (GridBrush)brushs.get(i);
				if(brush.getID() == bID){
					if(brush.width < 10 && brush.height < 10){
						brush.width = 10;
						brush.height = 10;
						mouseInfo.resetAll();
						scrollablePanel.repaint();
					}
					break;
				}
			}
			mouseState = MS_NORMAL;
		}else if(mouseState == MS_SELECTED){
			
		}
	}

	private void selfMouseDragged(MouseEvent e){
		if(mouseState == MS_NEW){
			int bID = getBrushID();
			for(int i=0; i<brushs.size(); ++i){
				GridBrush brush = (GridBrush)brushs.get(i);
				if(brush.getID() == bID){
					int offsetX = mouseX - oldMouseX;
					int offsetY = mouseY - oldMouseY;
					brush.drag(RectShape.CORNER_RIGHT_BOTTOM, offsetX, offsetY);
					mouseInfo.resetAll();
					scrollablePanel.repaint();
			//		System.out.println("MS_NEW drag");
					break;
				}
			}
		}else{
			int offsetX = mouseX - oldMouseX;
			int offsetY = mouseY - oldMouseY;
			if(mouseState == MS_SELECTED){
				GridBrush brush = (GridBrush)brushs.get(bSelectIndex);
				brush.move(offsetX, offsetY);
				brushGrid();
				
//				if(cornerDrag == RectShape.CORNER_CENTER_MIDDLE){
//					brush.move(offsetX, offsetY);					
//					if(e.isControlDown()){
//			//			System.out.println("ctrl drag");
//						brushGrid();
//					}
//				}else{
//					brush.drag(cornerDrag, offsetX, offsetY);
//				}
				
				mouseInfo.resetAll();
				scrollablePanel.repaint();
			}
		}			
	}
	
	private void selfMouseMoved(MouseEvent e){
		if(mouseState == MS_SELECTED){
			int offsetX = mouseX - oldMouseX;
			int offsetY = mouseY - oldMouseY;
			GridBrush brush = (GridBrush)brushs.get(bSelectIndex);
			brush.move(offsetX, offsetY);
			
//			GridBrush brush = (GridBrush)brushs.get(bSelectIndex);
//			cornerDrag = brush.getCornerAtPoint(mouseX, mouseY);
//			scrollablePanel.setCursor(brush.getCornerCursor(cornerDrag));
			
			mouseInfo.resetAll();
			scrollablePanel.repaint();
		} 
	}
	
	private void selfMouseDoubleClicked(MouseEvent e){
		if(mouseState == MS_SELECTED){
			mouseState = MS_NORMAL;
			GridBrush brush = (GridBrush)brushs.get(bSelectIndex);
			brush.setSelected(false);
		}
		
		int index = getMouseSelected();
		if(index >=0 ){
			GridBrush brush = (GridBrush)brushs.get(index);
			showProperties(brush);
		}
	}
	
	private int getMouseSelected(){
		int selected = -1;
		for(int i=0; i<brushs.size(); ++i){
			GridBrush brush = (GridBrush)brushs.get(i);
			if(brush.containPoint(mouseX, mouseY)){
				selected = i;
				break;
			}
		}
		return selected;
	}
	
	protected void showProperties(GridBrush brush) {
		if (brush != null) {
			BrushPropSetter setter;
			if(scrollablePanel instanceof ShapeManagerPanel) {
				ShapeManagerPanel panel = (ShapeManagerPanel)scrollablePanel;
				if(panel.getFrameOwner() != null) {
					setter = new BrushPropSetter(panel.getFrameOwner(), brush);
				}
				else {
					setter = new BrushPropSetter(panel.getDialogOwner(), brush);
				}
			}
			else {
				setter = new BrushPropSetter(MainFrame.self, brush);
			}
			setter.show();
			scrollablePanel.repaint();
			selectionChanged();
		}
	}
	
	private void brushGrid(){
		GridBrush brush = (GridBrush)brushs.get(bSelectIndex);
		for(int i = 0; i < rowNum; ++i){
			for(int j = 0; j < colNum; ++j){
				LogicGrid grid = gridData[i][j];
				int gx = grid.getCenterX();
				int gy = grid.getCenterY();
				if(brush.containPoint(gx, gy)){
					grid.setType(brush.getType());
				}
			}
		}
	}
	
	protected void DrawRoadPoint(Graphics g){
		for(int i = 0; i < rowNum; ++i){
			for(int j = 0; j < colNum; ++j){
				LogicGrid grid = gridData[i][j];
				grid.paint(g);
			}
		}
	}
	
	void DrawBrushs(Graphics g){
		for(int i=0; i<brushs.size(); ++i){
			GridBrush brush = (GridBrush)brushs.get(i);
			brush.paint(g);
		}
	}
	
	public void paintShapesOther(Graphics g){
		DrawRoadPoint(g);
		DrawBrushs(g);
	}

	public void sortMobileLine(ArrayList points, int type) {
		
	}

	public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception {
		
	}
	
	public void saveLua(String name) throws Exception{
		if(rowNum == 0 || colNum == 0)
			return;
		
		StringBuffer mapGridLua = new StringBuffer();
		mapGridLua.append("\r\n\r\n--Grids\r\ngrids={\r\n");
		mapGridLua.append("\tr=");
		mapGridLua.append(rowNum);
		mapGridLua.append(",c=");
		mapGridLua.append(colNum);
		mapGridLua.append(",w=");
		mapGridLua.append(w);
		mapGridLua.append(",h=");
		mapGridLua.append(h);
		mapGridLua.append(",\r\n");
		
		//一维
//		for(int i = 0; i < rowNum; ++i){
//			mapGridLua.append("\t");
//			for(int j = 0; j < colNum; ++j){
//				LogicGrid grid = gridData[i][j];
//				mapGridLua.append(grid.getType());
//				mapGridLua.append(",");
//			}
//			mapGridLua.append("\r\n");
//		}
//		mapGridLua.append("}");
		
		//二维
		for(int i = -1; i < rowNum+1; ++i){
			if(i == -1){
				mapGridLua.append("\t[0]={[0]=0,");
				for(int j = 0; j < colNum+1; ++j){
					mapGridLua.append(0);
					if(j<colNum)
						mapGridLua.append(",");
				}
				mapGridLua.append("},\r\n");
			}else if(i == rowNum){
				mapGridLua.append("\t{[0]=0,");
				for(int j = 0; j < colNum+1; ++j){
					mapGridLua.append(0);
					if(j<colNum)
						mapGridLua.append(",");						
				}
				mapGridLua.append("},\r\n");
			}else{			
				mapGridLua.append("\t{[0]=0,");
				for(int j = 0; j < colNum+1; ++j){
					if(j<colNum){
						LogicGrid grid = gridData[i][j];
						mapGridLua.append(grid.getType());
						mapGridLua.append(",");
					}else if(j == colNum)
						mapGridLua.append(0);
				}
				mapGridLua.append("},\r\n");
			}
		}
		mapGridLua.append("}");
		//end
		
		String s = mapGridLua.toString();
		String scriptPath = XUtil.getDefPropStr("ScriptPath");
		
		FileWriter writer = new FileWriter(scriptPath + "\\" + name + ".lua", true);
		writer.write(s);
		writer.flush();
		writer.close();
	}

	public void save(DataOutputStream out, Object[] resManagers) throws Exception {
		for(int i = 0; i < rowNum; ++i){
			for(int j = 0; j < colNum; ++j){
				LogicGrid grid = gridData[i][j];
				grid.save(out);
			}
		}
	}

	public void load(DataInputStream in, Object[] resManagers) throws Exception {
		reset();
		for(int i = 0; i < rowNum; ++i){
			for(int j = 0; j < colNum; ++j){
				LogicGrid grid = gridData[i][j];
				grid.load(in);
			}
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

class GridPanel extends ShapeManagerPanel {

	public GridPanel(JFrame owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	public GridPanel(JDialog owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	protected ShapeManager createManager() {
		return new GridManager(this, mouseInfo);
	}
}

class BrushPropSetter extends OKCancelDialog {

	private GridBrush brush;
	private NumberSpinner wSpinner, hSpinner;

	public BrushPropSetter(JFrame owner, GridBrush brush) {
		super(owner);
		init(brush);
	}

	public BrushPropSetter(JDialog owner, GridBrush brush) {
		super(owner);
		init(brush);
	}

	private void init(GridBrush brush) {
		this.setTitle("设置刷子属性");
		this.brush = brush;
		wSpinner = new NumberSpinner();
		wSpinner.setIntValue(brush.getWidth());
		hSpinner = new NumberSpinner();
		hSpinner.setIntValue(brush.getHeight());

		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(2, 2, 3, 3);
		p.add(new JLabel("宽："), c);

		c.weightx = 1;
		c.gridx = 1;
		p.add(wSpinner, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		String title = "高：";
		p.add(new JLabel(title), c);

		c.gridx = 1;
		c.weightx = 1;
		p.add(hSpinner, c);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(p, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void okPerformed() {
		try {
			int w = wSpinner.getIntValue();
			int h = hSpinner.getIntValue();
			if(w<3 || h<3)
				JOptionPane.showMessageDialog(null, "矩形过小", "警告", JOptionPane.WARNING_MESSAGE);
			else{
				brush.setWidth(w);
				brush.setHeight(h);
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