package editor;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import sun.swing.table.DefaultTableCellHeaderRenderer;

class Tile extends SISprite {

	TileData td;
	int siId;
	int gridId;
	ArrayList<String> props;

	public Tile(SingleImage si, int id, int left, int top, boolean flip) {
		super(si, id, left, top, flip, 0);		
		init();
	}

	public Tile(SingleImage si, int id, int left, int top, boolean flip, String name) {
		super(si, id, left, top, flip, name, 0);
		init();
	}
	
	public Tile(SingleImage si, int id, int left, int top, boolean flip, String name, TileData td) {
		super(si, id, left, top, flip, name, 0);
		this.props = new ArrayList<String>();
		this.td = td;
		this.siId = td.siId;
	}
	
	private void init() {
		props = new ArrayList<String>();
		
		SingleImage si = getSI();
		TileImage ti = MainFrame.self.getTIManager().getTI(si.getGroup().getID());
		this.td = ti.getTD(si.getID());
		this.siId = td.siId;
	//	System.out.println(getID()+", "+getX()+", "+getY()+", "+getWidth()+", "+getHeight());
	}
	
	public void setSI(SingleImage si) {
		setSI(si);		
		init();
	}
	
	public void setGridId(int gridId) {
		this.gridId = gridId;
	}
	
	public void setProps(ArrayList<String> ps) {
		props.clear();
		for(int i=0; i<ps.size(); ++i) {
			props.add(new String(ps.get(i)));
		}
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

	public int compareTo(Object o) {
		return super.compareTo(o);
	}

	public Copyable copy() {
		return copyTile();
	}

	public void copyFrom(Tile source) {
		super.copyFrom(source);
	}

	public final Tile copyTile() {
		Tile result = new Tile(this.getSI(), this.getID(), this.getX(), this.getY(),
		        this.isFlip(), this.getName(), this.td);
		result.copyFrom(this);
		return result;
	}

	public String getSelectMenuName() {
		SingleImage si = getSI();
		return si.getGroup().getName() + "\\" + si.getName();
	}

	public void load(DataInputStream in) throws Exception {
		
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(td.id);
		out.writeInt(td.siId);
		out.writeInt(getID());
		out.writeInt(getLeft());
		out.writeInt(getTop());
		
		out.writeInt(props.size());
		for(int i=0; i<props.size(); ++i) {
			SL.writeString(props.get(i), out);
		}
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		
	}
}

//每一块图元
class TileData {
	int id;		//在<code>TileImage</code>.tds中的位置索引
	int pId;	//父图片id, 在<code>TIManager</code>.tis中的位置索引
	int siId; // 父图片名字id
	int x, y, w, h;
	boolean isUse;
	BufferedImage image;
	BufferedImage srcImage;
	SingleImage si;
	SIPainter painter;
	ArrayList<String> props;
	
	public TileData() {
		props = new ArrayList<String>();
		isUse = false;
	}
	
	public TileData(SingleImage srcSI, int id, int pId, int x, int y, int w, int h) {
		this.id = id;
		this.pId = pId;
		this.siId = srcSI.getID();
		this.srcImage = srcSI.image;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.props = new ArrayList<String>();
		this.isUse = false;
		
		generateImage(x, y, w, h);
		
		SIGroup group = new SIGroup(srcSI.getID(), srcSI.getName(), srcSI.getDefLayer());
		this.si = new SingleImage(image, group, id, srcSI.getName()+"_"+id);
		this.painter = new SIPainter(si);
	}
	
	public void setProps(ArrayList<String> ps) {
		props.clear();
		for(int i=0; i<ps.size(); ++i) {
			props.add(new String(ps.get(i)));
		}
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
	
	public void setIsUse(boolean isUse) {
		this.isUse = isUse;
	}
	
	public void generateImage(int x, int y, int w, int h) {
		image = null;
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		g.drawImage(srcImage, 0, 0, w, h, x, y, x+w, y+h, null);
		image.flush();
	}
	
	public void paintSelected(Graphics g, int left, int top) {
		si.paintLeftTop(g, left, top, false);
		
		Graphics2D g2 = (Graphics2D) g;
		Composite oldComposite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
		g2.setColor(Color.BLUE);
		g2.fillRect(left, top, w, h);
		g2.setComposite(oldComposite);
	}
	
	public void paintLeftTop(Graphics g, int left, int top) {
		si.paintLeftTop(g, left, top, false);
	}
	
	public void save(DataOutputStream out) throws Exception {
		out.writeInt(props.size());
		for(int i=0; i<props.size(); ++i) {
			SL.writeString(props.get(i), out);
		}
	}

	public void load(DataInputStream in) throws Exception {
		int len = in.readInt();
		for(int i=0; i<len; ++i) {
			props.add(SL.readString(in));
		}
	}
}

class TileImage {
	static int Def_Offset = 2;
	
	int id;	//在TIManager中的索引位置
	int siId;	//si.id, 保存文件名id
	int tileWidth, tileHeight;
	int width, height;
	int row, col;
	int selectIndex;
	boolean isUse;
	boolean isSave;
	String name;
	ArrayList<TileData> tds;
	
	private int offset = Def_Offset;
	private int imageOffset;
	
	public TileImage() {
		tds = new ArrayList<TileData>();
		imageOffset = XUtil.getDefPropInt("TileDataOffset");
	}
	
	public TileImage(SingleImage si, int id, int tileWidth, int tileHeight, boolean isSave) {
		this.id = id;
		this.siId = si.getID();
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.width = si.getWidth();
		this.height = si.getHeight();
		this.tds = new ArrayList<TileData>();
		this.selectIndex = -1;
		this.isUse = false;
		this.isSave = isSave;
		
		row = height/tileHeight;
		col = width/tileWidth;
		imageOffset = XUtil.getDefPropInt("TileDataOffset");
		for(int i=0; i< row; ++i) {
			for(int j=0; j<col; ++j) {
				TileData td = new TileData(si, i*col + j, id, j*(tileWidth+imageOffset), i*(tileHeight+imageOffset), tileWidth, tileHeight);
		//		System.out.println((i*col + j)+", "+(j*tileWidth)+", "+(i*tileHeight));
				tds.add(td);
			}
		}
	}
	
	public TileImage(SingleImage si, int id, int tileWidth, int tileHeight, int row, int col, boolean isSave) {
		this.id = id;
		this.siId = si.getID();
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.width = si.getWidth();
		this.height = si.getHeight();
		this.tds = new ArrayList<TileData>();
		this.selectIndex = -1;
		this.row = row;
		this.col = col;
		this.isUse = false;
		this.isSave = isSave;
		this.imageOffset = XUtil.getDefPropInt("TileDataOffset");
		
		for(int i=0; i< row; ++i) {
			for(int j=0; j<col; ++j) {
				TileData td = new TileData(si, i*col + j, id, j*(tileWidth+imageOffset), i*(tileHeight+imageOffset), tileWidth, tileHeight);
				tds.add(td);
			}
		}
	}
	
	public boolean ChangeOffset() {
		if(offset == Def_Offset) {
			offset = 0;
			return false;
		}
		else {
			offset = Def_Offset;
			return true;
		}
	}
	
	public TileData getSelectedTD() {
		return tds.get(selectIndex);
	}
	
	public void paintLeftTop(Graphics g, int left, int top) {
		int w = tileWidth +offset;
		int h = tileHeight + offset;
		for(int i=0; i< row; ++i) {
			for(int j=0; j<col; ++j) {
				int index = i*col + j;
				TileData td = tds.get(index);
				if(index == selectIndex)
					td.paintSelected(g, left+w*j, top+h*i);
				else
					td.paintLeftTop(g, left+w*j, top+h*i);
			}
		}
	}
	
	public void mouseLeftPressed(int x, int y) {
		int w = tileWidth + offset;
		int h = tileHeight + offset;
		selectIndex = (int)(y/w)*col + (int)(x/h);
		
		if(MainFrame.self.tileEdit.isInitTileLayers() && selectIndex >=0 && selectIndex < tds.size()) {
			MouseInfo mouseInfo = MainFrame.self.getMouseInfo();
			mouseInfo.setInfo(MouseInfo.NEW_SPRITE);
			mouseInfo.setPainter(tds.get(selectIndex).painter);
		}
	}
	
	public int getRealWidth() {
		return col*(tileWidth + offset);
	}
	
	public int getRealHeight() {
		return row*(tileHeight + offset);
	}
	
	public TileData getTD(int index) {
		return tds.get(index);
	}
	
	public void setIsUse(boolean isUse) {
		this.isUse = isUse;
	}
	
	public void copyProps(TileImage ti) {
		for(int i=0; i<tds.size() && i<ti.tds.size(); ++i) {
			tds.get(i).setProps(ti.tds.get(i).props);
		}
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(siId);
		out.writeInt(row);
		out.writeInt(col);
		for(int i=0; i<tds.size(); ++i) {
			tds.get(i).save(out);
		}
	}
	public void load(DataInputStream in) throws Exception {
		siId = in.readInt();
		row = in.readInt();
		col = in.readInt();
		for(int i=0; i< row; ++i) {
			for(int j=0; j<col; ++j) {
				TileData td = new TileData();
				td.load(in);
				tds.add(td);
			}
		}
	}
	
	public void saveLuaAll(StringBuffer tilesLua, int tabNum) {
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("width=");
		tilesLua.append(col);
		tilesLua.append(",height=");
		tilesLua.append(row);
		tilesLua.append(",isHasPic=");
		tilesLua.append(isSave);
		tilesLua.append(",\r\n");
		
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("props={\r\n");
		for(int i=0; i<tds.size(); ++i) {
			TileData td = tds.get(i);
			if(td.props.size() > 0) {
				tilesLua.append(XUtil.getTabs(tabNum+1));
				tilesLua.append("["+i+"]={");
				tilesLua.append(td.getPropsLua());
				tilesLua.append("},\r\n");
			}
		}
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("},\r\n");
	}

	public void saveLua(StringBuffer tilesLua, int tabNum) {
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("width=");
		tilesLua.append(col);
		tilesLua.append(",height=");
		tilesLua.append(row);
		tilesLua.append(",isHasPic=");
		tilesLua.append(isSave);
		tilesLua.append(",\r\n");
		
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("props={\r\n");
		for(int i=0; i<tds.size(); ++i) {
			TileData td = tds.get(i);
			if(td.isUse) {
				td.setIsUse(false);
				if(td.props.size() > 0) {
				tilesLua.append(XUtil.getTabs(tabNum+1));
				tilesLua.append("["+i+"]={");
				tilesLua.append(td.getPropsLua());
				tilesLua.append("},\r\n");
				}
			}
		}
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("},\r\n");
	}
}

// 管理所有的TileImage
class TIManager extends SIManager {
	public static int DEF_WIDTH = 33;
	public static int DEF_HEIGHT = 33;
	public static String saveName = "tile.dat";
	
	ArrayList<TileImage> tis;
	TileEdit tileEdit;
	int tileW, tileH;
	int nowIndex;
	
	public TIManager(String name) throws Exception {
		super(name);
		tileW = XUtil.getDefPropInt("TileDataWidth");//DEF_WIDTH;
		tileH = XUtil.getDefPropInt("TileDataHeight");//DEF_HEIGHT;
		nowIndex = -1;
		
		tis = new ArrayList<TileImage>();
		File f = new File(XUtil.getDefPropStr("SavePath") + "\\" + saveName);
		SingleImage[] sis = getSIs();
		for(int i=0; i<sis.length; ++i) {
			TileImage ti = new TileImage(sis[i], i, tileW, tileH, sis[i].isSave);
			tis.add(ti);
		}
	}
	
	public void setIndex(int index) {
		if(index < 0 || index > tis.size())
			nowIndex = -1;
		else
			nowIndex = index;
	}
	
	public TileImage getTI(int siId) {
		TileImage ti = null;
		for(int i=0; i<tis.size(); ++i) {
			SingleImage nowsi = getSI(tis.get(i).siId);
			if(nowsi.getID() == siId) {
				ti = tis.get(i);
				break;
			}
		}
		
		return ti;
	}
	
	public void save() throws Exception {
		File f = new File(XUtil.getDefPropStr("SavePath") + "\\" + saveName);
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
		        new FileOutputStream(f)));
		
		out.writeInt(tis.size());
		for(int i=0; i<tis.size(); ++i) {
			tis.get(i).save(out);
		}
		out.flush();
		out.close();
	}

	public void load() throws Exception {
		File f = new File(XUtil.getDefPropStr("SavePath") + "\\" + saveName);
		if (f.exists()) {
			DataInputStream in = new DataInputStream(
			        new BufferedInputStream(new FileInputStream(f)));
			
			int len = in.readInt();
			for(int i=0; i<len; ++i) {
				TileImage loadTI = new TileImage();
				loadTI.load(in);
				TileImage ti = getTI(loadTI.siId);
				if(ti != null) {
					ti.copyProps(loadTI);
				}
			}
			in.close();
		}
	}
	
	public void saveLuaAll() throws Exception {
		StringBuffer tilesLua = new StringBuffer();
		tilesLua.append("-------------图块属性------------\r\n");
		tilesLua.append("tileImages={\r\n");
		for(int i=0; i<tis.size(); ++i) {
			TileImage ti = tis.get(i);
			tilesLua.append(XUtil.getTabs(1));
			tilesLua.append("["+ti.siId + "]={\r\n");
			ti.saveLuaAll(tilesLua, 2);
			tilesLua.append(XUtil.getTabs(1));
			tilesLua.append("},\r\n");
		}
		tilesLua.append("}\r\n");
		
		String s = tilesLua.toString();
		String scriptPath = XUtil.getDefPropStr("ScriptPath");
		FileWriter writer = new FileWriter(scriptPath + "\\tile.lua");
		writer.write(s);
		writer.flush();
		writer.close();
		
		File lvFile = new File(scriptPath + "\\tile.lua");		
		String releasePath = XUtil.getDefPropStr("LuaReleasePath");
		File releaseF = new File(releasePath + "\\tile.lua");
		if(new File(releasePath).exists())
			XUtil.copyFile(lvFile.getAbsolutePath(), releaseF.getAbsolutePath());
	}
	
	public void saveLua(StringBuffer tilesLua, int tabNum) {
		for(int i=0; i<tis.size(); ++i) {
			TileImage ti = tis.get(i);
			if(ti.isUse) {
				ti.setIsUse(false);
				tilesLua.append(XUtil.getTabs(tabNum));
				tilesLua.append("["+ti.siId + "]={\r\n");
				ti.saveLua(tilesLua, tabNum+1);
				tilesLua.append(XUtil.getTabs(tabNum));
				tilesLua.append("},\r\n");
			}
		}
	}
}

class TileManager extends SpriteManager {
	
	TileLayerManager tlm;
	ArrayList<Tile> tileSelected;
	ArrayList<Tile> tileBrush;
	int tileSelectedX, tileSelectedY;
	int dragButton;

	public TileManager(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) {
		super(scrollablePanel, mouseInfo);
		tileSelected = new ArrayList<Tile>();
		dragButton = -1;
	}
	
	public void setTileLayerManager(TileLayerManager tlm) {
		this.tlm = tlm;
	}
	
	public void setIsSelectedAll(boolean selected) {
		for(int i=0; i<tileSelected.size(); ++i) {
			tileSelected.get(i).setSelected(selected);
		}
	}
	
	public void moveAll(int offsetx, int offsety) {
		for(int i=0; i<tileSelected.size(); ++i) {
			tileSelected.get(i).move(offsetx, offsety);
		}
	}
	
	public void delSelectedObjs() {
	//	System.out.println("tileSelected: "+tileSelected.size());
		tlm.delSelectedObjs();
		tileSelected.clear();
	}

	protected Sprite createSprite(int x, int y) {
		if (!(mouseInfo.getPainter() instanceof SIPainter)) { return null; }
		if(tlm == null) { return null; }
		
		SIPainter painter = (SIPainter) mouseInfo.getPainter();		
		TileLayer tl = tlm.getSelectLayer();
		if(tl != null) {
			if(tl.type == TileLayer.Type_Tile) {
				MapInfo mapInfo = MainFrame.self.getMapInfo();
				int l = y*tlm.w+mapInfo.getRealLeft();
				int t = x*tlm.h+mapInfo.getRealTop();
				return new Tile(painter.getSI(), x*tlm.col + y, l, t, false);
			}
			else if(tl.type == TileLayer.Type_Obj) {
				return new Tile(painter.getSI(), tl.getMaxId(), x, y, false);
			}
		}
		
		return null;
	}
	
	public void delSprite(int x, int y) {
		tlm.delTile(x, y);
	}
	
	// add new tile, call in mouse pressed
	protected boolean addNewSprite(int x, int y) {
		if(tlm == null) { return false; }
	//	return super.addNewSprite(x, y);
		TileLayer tl = tlm.getSelectLayer();
		if(tl != null) {
			if(tl.type == TileLayer.Type_Tile) {
				int r = tlm.nowr;
				int c = tlm.nowc;
				if(r != -1 && c != -1){
					tl.add((Tile) createSprite(r, c), r, c);
					scrollablePanel.repaint();
				}
			}
			else if(tl.type == TileLayer.Type_Obj) {
				tl.add((Tile) createSprite(x-tlm.w/2, y-tlm.h/2));
				scrollablePanel.repaint();
			}
		}
		return true;
	}
	
	// call in load
	protected boolean addSprite(Sprite sprite) {
		if(tlm == null) { return false; }
		
		return super.addSprite(sprite);
	}
	
	public void addSelected(Tile t) {
		if(t != null && !t.isSelected()) {
			t.setSelected(true);
			tileSelected.add(t);
			MainFrame.self.repaintPanel();
		}
	}
	
	@Override
	protected void mousePressed(MouseEvent e) {
		if(tlm == null) { 
			super.mousePressed(e);
			return;
		}
		
		int x = (int) (e.getX()/scrollablePanel.scale);
		int y = (int) (e.getY()/scrollablePanel.scale);
		int type = tlm.getSelectLayerType();
		dragButton = e.getButton();
		
		if(type == TileLayer.Type_Tile) {
			if(e.getButton() == XUtil.LEFT_BUTTON) {
				if(mouseInfo.getInfo() == MouseInfo.NEW_SPRITE) {
					addNewSprite(x, y);
				}
				else {
					MapInfo mapInfo = MainFrame.self.getMapInfo();
					delSprite(x-mapInfo.getRealLeft(), y-mapInfo.getRealTop());
				}
			}
			else if(e.getButton() == XUtil.RIGHT_BUTTON){
				if(mouseInfo.getInfo() == MouseInfo.NEW_SPRITE) {
					MainFrame.self.tileEdit.setTISelect(-1);
					super.mousePressed(e);
				}
				else {
					
				}
			}
		}
		else if(type == TileLayer.Type_Obj) {
			if(e.getButton() == XUtil.LEFT_BUTTON) {
				if(mouseInfo.getInfo() == MouseInfo.NEW_SPRITE) {
					addNewSprite(x, y);
				}
				else {
					tileSelectedX = x;
					tileSelectedY = y;
					
					Tile t = tlm.getTileSelected(x, y);
					if(t != null) {
						if(!t.isSelected()) {
							setIsSelectedAll(false);
							tileSelected.clear();
							t.setSelected(true);
							tileSelected.add(t);
						}
						mouseInfo.setInfo(MouseInfo.MOVE_TILE);
						MainFrame.self.repaintPanel();
					}
					else {
						setIsSelectedAll(false);
						tileSelected.clear();
						setMouseState(MOUSE_STATE_RECTANGLING);
					}
				}
			}
			else if(e.getButton() == XUtil.RIGHT_BUTTON) {
				if(mouseInfo.getInfo() == MouseInfo.NEW_SPRITE) {
					MainFrame.self.tileEdit.setTISelect(-1);
					super.mousePressed(e);
				}
			}
		}
	}
	
	@Override
	protected void mouseReleased(MouseEvent e) {
		dragButton = -1;
		if(mouseInfo.getInfo() == MouseInfo.MOVE_TILE)
			mouseInfo.resetSelf();
		super.mouseReleased(e);
	}
	
	@Override
	protected void mouseDragged(MouseEvent e) {
		if(tlm == null) { 
			super.mouseDragged(e);
			return;
		}
		
		int x = (int) (e.getX()/scrollablePanel.scale);
		int y = (int) (e.getY()/scrollablePanel.scale);		
		int type = tlm.getSelectLayerType();
		if(type == TileLayer.Type_Tile) {
			if(mouseInfo.getInfo() == MouseInfo.NEW_SPRITE) {
				UpdateMouseInfo(e);
				addNewSprite(x, y);
			}
			else if(dragButton == XUtil.LEFT_BUTTON) {
				MapInfo mapInfo = MainFrame.self.getMapInfo();
				delSprite(x-mapInfo.getRealLeft(), y-mapInfo.getRealTop());
			}
			else {
				super.mouseDragged(e);
			}
		}
		else if(type == TileLayer.Type_Obj) {
			if(mouseInfo.getInfo() == MouseInfo.NEW_SPRITE) {
				mouseX = x;
				mouseY = y;
				MainFrame.self.repaintPanel();
			}
			else if(mouseInfo.getInfo() == MouseInfo.MOVE_TILE) {
				if(tileSelected.size() > 0) {
					int offsetx = x-tileSelectedX;
					int offsety = y-tileSelectedY;
					moveAll(offsetx, offsety);
					tileSelectedX = x;
					tileSelectedY = y;
					MainFrame.self.repaintPanel();
				}
			}
			else {
				super.mouseDragged(e);
				TileLayer tl = tlm.getSelectLayer();
				if(tl != null) {
					int l, r, t, b;
					if(tileSelectedX < x) {
						l = tileSelectedX;
						r = x;
					}
					else {
						l = x;
						r = tileSelectedX;
					}
					
					if(tileSelectedY < y) {
						t = tileSelectedY;
						b = y;
					}
					else {
						t = y;
						b = tileSelectedY;
					}			
					
					if(tl.type == TileLayer.Type_Obj) {
						for(int i=0; i<tl.objs.size(); ++i) {
							Tile tile = tl.objs.get(i);
							if(!tile.isSelected() && tile.inRect(l, t, r-l, b-t)) {
								tile.setSelected(true);
								tileSelected.add(tile);
							}
						}
						
						int nowi = 0;
						for(int i=0; i<tileSelected.size(); ++i) {
							Tile tile = tileSelected.get(nowi);
							if(!tile.inRect(l, t, r-l, b-t)) {
								tile.setSelected(false);
								tileSelected.remove(nowi);
							}
							else
								nowi += 1;
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		
		if(tlm == null) { return; }
		int type = tlm.getSelectLayerType();
		if(type == TileLayer.Type_Tile && mouseInfo.getInfo() == MouseInfo.NEW_SPRITE) {
			UpdateMouseInfo(e);
		}
	}
	
	@Override
	protected void keyReleased(KeyEvent e) {
		if(tlm == null) { return; }
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DELETE:
			if(tileSelected.size() > 0) {
				delSelectedObjs();
				MainFrame.self.repaintPanel();
			}
		}
	}
	
	private void UpdateMouseInfo(MouseEvent e) {
		MapInfo mapInfo = MainFrame.self.getMapInfo();
		int x = (int) (e.getX()/scrollablePanel.scale);
		int y = (int) (e.getY()/scrollablePanel.scale);
		mouseX = tlm.getMoveX(x-mapInfo.getRealLeft()) + mapInfo.getRealLeft();
		mouseY = tlm.getMoveY(y-mapInfo.getRealTop()) + mapInfo.getRealTop();
	}

	public void paintStatic(Graphics g) {
		if(tlm == null) { return; }
		super.paintStatic(g);
		
		MapInfo mapInfo = MainFrame.self.getMapInfo();
		tlm.paintTileLayers(g);	
		if(MainFrame.self.getTabIndex() == MainFrame.LAYER_TILE) {
			tlm.paintBackGround(g, mapInfo.getRealLeft(), mapInfo.getRealTop());
		}
	}

	public void paintSprites(Graphics g) {
		if(tlm == null) { return; }
		
		MapInfo mapInfo = MainFrame.self.getMapInfo();
		tlm.paintBackGround(g, mapInfo.getRealLeft(), mapInfo.getRealTop());
		tlm.paintTileLayers(g);	
	}

	public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception {
		
	}

	public void save(DataOutputStream out, Object[] resManagers) throws Exception {
		
	}

	public void load(DataInputStream in, Object[] resManagers) throws Exception {
		
	}

	@Override
	public void saveMobile(DataOutputStream out, int layer) throws Exception {
		// TODO Auto-generated method stub
		
	}
}

class TilePanel extends SpriteManagerPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPopupMenu popMenu;
	private AbstractButton tilePropMenu;

	public TilePanel(JFrame owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
		init();
	}

	public TilePanel(JDialog owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
		init();
	}
	
	public void init() {
		popMenu = new JPopupMenu();
		
		tilePropMenu = new JMenuItem("图块属性");
		tilePropMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource() == tilePropMenu) {
					TileManager tm = (TileManager)(getManager());
					if(tm.tileSelected.size() == 1) {
						Tile t = tm.tileSelected.get(0);
						TilePropDialog setter;
						setter = new TilePropDialog(MainFrame.self, t.props);
						setter.setTitle("设置图块属性");
						setter.show();
						if(setter.getCloseType() != -1) {
							tm.setIsSelectedAll(false);
							tm.tileSelected.clear();
							repaint();
						}
					}
				}
			}
		});
		
		popMenu.add(tilePropMenu);
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getButton() == XUtil.RIGHT_BUTTON) {
					TileManager tm = (TileManager)(getManager());
					if(tm.tlm == null)
						return;
					
					int x = (int) (e.getX()/scale);
					int y = (int) (e.getY()/scale);
					tm.setIsSelectedAll(false);
					tm.tileSelected.clear();
					if(tm.tlm.getSelectLayerType() == TileLayer.Type_Tile) {
						MapInfo mapInfo = MainFrame.self.getMapInfo();
						tm.addSelected(tm.tlm.getTileSelected(x-mapInfo.getRealLeft(), y-mapInfo.getRealTop()));
					}
					else if(tm.tlm.getSelectLayerType() == TileLayer.Type_Obj) {
						tm.addSelected(tm.tlm.getTileSelected(x, y));
					}
					if(tm.tileSelected.size() == 1) {
						showProps(e.getX(), e.getY());
					}
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void showProps(int x, int y) {
		popMenu.show(this, x, y);
	}

	protected SpriteManager createManager() {
		return new TileManager(this, mouseInfo);
	}
}

class TileLayer {
	
	public static int Type_Obj = 0;
	public static int Type_Tile = 1;
	
	int type;
	int row, col;
	int maxId;
	int count;
	boolean isShow;
	String name;
	String iconName;
	Tile[][] tiles;
	ArrayList<Tile> objs;
	
	public TileLayer(String name, int row, int col) {
		this.type = Type_Tile;
		this.row = row;
		this.col = col;
		this.isShow = true;
		this.name = name;
		this.iconName = "7.png";
		this.count = 0;
		
		tiles = new Tile[row][];
		for(int i=0; i<row; ++i){
			tiles[i] = new Tile[col];
		}
	}

	public TileLayer(String name) {
		this.type = Type_Obj;
		this.maxId = 0;
		this.isShow = true;
		this.name = name;
		this.iconName = "6.png";
		
		objs = new ArrayList<Tile>();
	}
	
	public TileLayer copySelf() {
		TileLayer tl = null;
		if(type == Type_Obj) {
			tl = new TileLayer(name+"_副本");
			tl.maxId = maxId;
			
			for(int i=0; i<objs.size(); ++i) {
				Tile t = objs.get(i).copyTile();
				tl.objs.add(t);
			}
		}
		else if(type == Type_Tile) {
			tl = new TileLayer(name+"_副本", row, col);
			tl.count = count;
			
			for(int i=0; i<row; ++i){
				for(int j=0; j<col; ++j){
					if(tiles[i][j] != null) {
						Tile t = tiles[i][j].copyTile();
						tl.tiles[i][j] = t;
					}
				}
			}
		}
		
		return tl;
	}
	
	public void delSelf() {
		if(type == Type_Obj) {
			objs.clear();
		}
		else if(type == Type_Tile) {
			tiles = null;
		}
	}
	
	public void setIsShow(boolean isShow) {
		this.isShow = isShow;
	}
	
	public Tile getTile(int r, int c) {
		Tile t = null;
		if(r>=0 && r<row && c>=0 && c<col)
			t = tiles[r][c];
		
		return t;
	}
	
	public void add(Tile t, int r, int c) {
	//	if(r>=0 && r<row && c>=0 && c<col)
		if(isShow) {
			if(tiles[r][c] == null)
				count  += 1;
			tiles[r][c] =t ;
		}
	}
	
	public boolean del(int r, int c) {
		if(isShow && r>=0 && r<row && c>=0 && c<col && tiles[r][c] != null) {
			count -= 1;
			tiles[r][c] = null;
			return true;
		}
		
		return false;
	}
	
	public void add(Tile t) {
		if(isShow)
			objs.add(t);
	}
	
	public boolean del(int index) {
		if(isShow) {
			objs.remove(index);
			return true;
		}
		
		return false;
	}
	
	public int getMaxId() {
		return maxId++;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isHasTile() {
		if(count > 0 || (objs != null && objs.size() > 0))
			return true;
		return false;
	}
	
	public boolean isHasProps() {
		if(type == Type_Obj) {
			for(int i=0; i<objs.size(); ++i) {
				if(objs.get(i).props.size() > 0)
					return true;
			}
		}
		else if(type == Type_Tile) {
			for(int i=0; i<row; ++i)
				for(int j=0; j<col; ++j) {
					if(tiles[i][j] != null) {
						if(tiles[i][j].props.size() > 0)
							return true;
					}
				}
		}
		
		return false;
	}
	
	public void panit(Graphics g) {
		if(!isShow) return;
		
		if(type == Type_Obj) {
			for(int i=0; i<objs.size(); ++i) {
				if(objs.get(i).isSelected())
					objs.get(i).paintSelected(g);
				else
					objs.get(i).paintIdle(g);
			}
		}
		else if(type == Type_Tile) {
			for(int i=0; i<row; ++i)
				for(int j=0; j<col; ++j) {
					if(tiles[i][j] != null) {
						if(tiles[i][j].isSelected()) {
							tiles[i][j].paintSelected(g);
						}
						else {
							tiles[i][j].paintIdle(g);
						}
					}
				}
		}
	}
	
	public void load(DataInputStream in) throws Exception {
	//	type = in.readInt();	已加载
	//	name = SL.readString(in);	已加载
		
		TIManager tim = MainFrame.self.getTIManager();
		maxId = in.readInt();
		if(type == Type_Obj) {
			int len = in.readInt();
			for(int i=0; i<len; ++i) {
				int td_id = in.readInt();
				int td_pId = in.readInt();
				int id = in.readInt();
				int left = in.readInt();
				int top = in.readInt();
				TileImage ti = tim.getTI(td_pId);
				
				ArrayList<String> props = new ArrayList<String>();
				int plen = in.readInt();
				for(int j=0; j<plen; ++j) {
					props.add(SL.readString(in));
				}
				
				if(ti != null) {
					TileData td = ti.getTD(td_id);
					Tile t = new Tile(td.si, id, left, top, false, "", td);
					t.setProps(props);
					objs.add(t);
				}
			}
		//	System.out.println("objs: "+objs.size());
		}
		else if(type == Type_Tile) {
			count = in.readInt();
			for(int i=0; i<count; ++i) {
				int r = in.readInt();
				int c = in.readInt();
				int td_id = in.readInt();
				int td_pId = in.readInt();
				int id = in.readInt();
				int left = in.readInt();
				int top = in.readInt();
				TileImage ti = tim.getTI(td_pId);
				
				ArrayList<String> props = new ArrayList<String>();
				int plen = in.readInt();
				for(int j=0; j<plen; ++j) {
					props.add(SL.readString(in));
				}
				
				if(ti != null) {
					TileData td = ti.getTD(td_id);
					tiles[r][c] = new Tile(td.si, id, left, top, false, "", td);
					tiles[r][c].setProps(props);
				}
				else {
					count -= 1;
				}
			}
		//	System.out.println("tiles: "+count);
		}
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(type);
		SL.writeString(name, out);
		out.writeInt(maxId);
		if(type == Type_Obj) {
			out.writeInt(objs.size());
			for(int i=0; i<objs.size(); ++i) {
				objs.get(i).save(out);
			}
		}
		else if(type == Type_Tile) {
			out.writeInt(count);
			for(int i=0; i<row; ++i) {
				for(int j=0; j<col; ++j) {
					if(tiles[i][j] != null) {
						out.writeInt(i);
						out.writeInt(j);
						tiles[i][j].save(out);
					}
				}
			}
		}
	}
	
	public void saveDataLua(StringBuffer tilesLua, int tabNum) {
		tilesLua.append("{\r\n");
		if(type == TileLayer.Type_Obj) {
			for(int i=0; i<objs.size(); ++i) {
				Tile t = objs.get(i);
				if(t.props.size() > 0) {
					tilesLua.append(XUtil.getTabs(tabNum+1));
					tilesLua.append("["+i+"]={");
					tilesLua.append(t.getPropsLua());
					tilesLua.append("},\r\n");
				}
			}
		}
		else if(type == TileLayer.Type_Tile) {
			ArrayList<Tile> tmp = new ArrayList<Tile>();
			for(int i=0; i<row; ++i) {
				for(int j=0; j<col; ++j) {
					Tile t = tiles[i][j];
					if(t != null && t.props.size() > 0) {
						tilesLua.append(XUtil.getTabs(tabNum+1));
						tilesLua.append("["+(i*col+j)+"]={");
						tilesLua.append(t.getPropsLua());
						tilesLua.append("},\r\n");
					}
				}
			}
		}
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("},\r\n");
	}

	public void saveLua(StringBuffer tilesLua, int tabNum) {
		tilesLua.append("{\r\n");
		tilesLua.append(XUtil.getTabs(tabNum+1));
		tilesLua.append("layerType=");
		if(type == TileLayer.Type_Obj) {
			tilesLua.append("\"objs\",\r\n");
			tilesLua.append(XUtil.getTabs(tabNum+1));
			tilesLua.append("datas={\r\n");
			
			ArrayList<Tile> tmp = new ArrayList<Tile>();
			for(int i=0; i<objs.size(); ++i) {
				Tile t = objs.get(i);
				t.setGridId(i);
				int pos = tmp.size();
				for(int j=0; j<tmp.size(); ++j) {
					if(t.siId <= tmp.get(j).siId) {
						pos = j;
						break;
					}
				}
				tmp.add(pos, t);
			}
			Tile t = tmp.get(0);
			int left = MainFrame.self.getMapInfo().getRealLeft();
			int top = MainFrame.self.getMapInfo().getRealTop();
			int siId =t.siId;
			tilesLua.append(XUtil.getTabs(tabNum+2));
			tilesLua.append("["+siId+"]={"+t.td.id+","+t.gridId+","+(t.getLeft()-left)+","+(t.getTop()-top));
			for(int i=1; i<tmp.size(); ++i) {
				t = tmp.get(i);
				if(siId == t.siId) {
					tilesLua.append(","+t.td.id+","+t.gridId+","+(t.getLeft()-left)+","+(t.getTop()-top));
				}
				else {
					siId = t.siId;
					tilesLua.append("},\r\n");
					tilesLua.append(XUtil.getTabs(tabNum+2));
					tilesLua.append("["+siId+"]={"+t.td.id+","+t.gridId+","+(t.getLeft()-left)+","+(t.getTop()-top));
				}
			}
			
			tilesLua.append("},\r\n");
			tilesLua.append(XUtil.getTabs(tabNum+1));
			tilesLua.append("},\r\n");
		}
		else if(type == TileLayer.Type_Tile) {
			tilesLua.append("\"tiles\",\r\n");
			tilesLua.append(XUtil.getTabs(tabNum+1));
			tilesLua.append("datas={\r\n");
			
			ArrayList<Tile> tmp = new ArrayList<Tile>();
			for(int i=0; i<row; ++i) {
				for(int j=0; j<col; ++j) {
					Tile t = tiles[i][j];
					if(t != null) {
						int pos = tmp.size();
						for(int k=0; k<tmp.size(); ++k) {
							if(t.siId <= tmp.get(k).siId) {
								pos =k;
								break;
							}
						}
						t.setGridId(i*col + j);
						tmp.add(pos, t);
					}
				}
			}
			Tile t = tmp.get(0);
			int siId =t.siId;
			tilesLua.append(XUtil.getTabs(tabNum+2));
			tilesLua.append("["+siId+"]={"+t.td.id+","+t.gridId);
			for(int i=1; i<tmp.size(); ++i) {
				t = tmp.get(i);
				if(siId == t.siId) {
					tilesLua.append(","+t.td.id+","+t.gridId);
				}
				else {
					siId = t.siId;
					tilesLua.append("},\r\n");
					tilesLua.append(XUtil.getTabs(tabNum+2));
					tilesLua.append("["+siId+"]={"+t.td.id+","+t.gridId);
				}
			}
			
			tilesLua.append("},\r\n");
			tilesLua.append(XUtil.getTabs(tabNum+1));
			tilesLua.append("},\r\n");
		}
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("},\r\n");
	}
}

class TileLayerManager {
	
	ArrayList<TileLayer> tls;
	int row, col;	//图层宽高块数
	int w, h;
	int nowLeft, nowTop, nowr, nowc;
	int selectIndex;
	
	public TileLayerManager(int row, int col) {
		this.tls = new ArrayList<TileLayer>();
		this.row = row;
		this.col = col;
		this.w = XUtil.getDefPropInt("TileDataWidth");
		this.h = XUtil.getDefPropInt("TileDataHeight");
		this.nowr = -1;
		this.nowc = -1;
		this.selectIndex = -1;
		
		//test
//		addTile("tile1");
//		this.selectIndex = 0;
	}
	
	public void clear() {
		tls.clear();
	}

	//tls层次按索引顺序从上到下排序
	public TileLayer CreateLayer(String name, int type) {
		if(type == TileLayer.Type_Obj)
			addObj(name);
		else if(type == TileLayer.Type_Tile)
			addTile(name);
		
		return tls.get(0);
	}
	
	// 添加图块层
	private void addTile(String name) {
		tls.add(0, new TileLayer(name, row, col));
	}
	
	// 添加对象层
	private void addObj(String name) {
		tls.add(0, new TileLayer(name));
	}
	
	public void addLayer(TileLayer tl, int index) {
		tls.add(index, tl);
	}
	
	public void delLayer(int index) {
		tls.remove(index);
		if(selectIndex >= tls.size())
			selectIndex = tls.size()-1;
	}
	
	public void delTile(int x, int y) {
		TileLayer tl = getSelectLayer();
		if(tl != null && tl.type == TileLayer.Type_Tile) {
			int r = y/w;
			int c = x/h;
			if(tl.del(r, c))
				MainFrame.self.repaintPanel();
		}
	}
	
	public void delSelectedObjs() {
		TileLayer tl = getSelectLayer();
		if(tl != null && tl.type == TileLayer.Type_Obj) {
			int nowi = 0;
			int len = tl.objs.size();
			for(int i=0; i<len; ++i) {
				if(tl.objs.get(nowi).isSelected()) {
					if(!tl.del(nowi)) {
						tl.objs.get(nowi).setSelected(false);
						nowi += 1;
					}
				}
				else  {
					nowi += 1;
				}
			}
	//		System.out.println("del: "+len+" - "+tl.objs.size()+ " = "+(len-tl.objs.size()));
		}
	}
	
	public TileLayer copyLayer(int index) {
		TileLayer tl = getLayer(index);
		addLayer(tl.copySelf(), index);
		
		return tls.get(index);
	}
	
	public void setRC(int r, int c) {
		int minRow = r > row ? row : r;
		int minCol = c > col ? col : c;
		
		for(int l=0; l<tls.size(); ++l) {
			TileLayer tl = tls.get(l);
			tl.row =r;
			tl.col =c;
			tl.count = 0;
			if(tl.type == TileLayer.Type_Tile) {
				Tile[][] oldts = tl.tiles;
				Tile[][] newts = new  Tile[r][];
				for(int i=0; i<minRow; ++i) {
					newts[i] = new Tile[c];
					for(int j=0; j<minCol; ++j) {
						newts[i][j] = oldts[i][j];
						if(newts[i][j] != null)
							tl.count += 1;
					}
				}
				if(minRow < r) {
					for(int i=minRow; i<r; ++i) {
						newts[i] = new Tile[c];
					}
				}
				tl.tiles = newts;
			}
		}
		row = r;
		col = c;		
	}
	
	public TileLayer getSelectLayer() {
		return getLayer(selectIndex);
	}
	
	public TileLayer getLayer(int index) {
		TileLayer tl = null;
		if(index >=0 && index < tls.size()) {
			tl = tls.get(index);
		}
		
		return tl;
	}
	
	public int getSelectLayerType() {
		TileLayer tl = getLayer(selectIndex);
		int type = -1;
		if(tl != null)
			type = tl.type;
		return type;
	}
	
	public Tile getTileSelected(int x, int y) {
		Tile tileSelected = null;
		TileLayer tl = getSelectLayer();
		if(tl != null) {
			if(tl.type == TileLayer.Type_Obj) {
				for(int i=0; i<tl.objs.size(); ++i){
					Tile t = tl.objs.get(i);
					if(t.containPoint(x, y)){
						tileSelected = t;
						break;
					}					
				}
			}
			else if(tl.type == TileLayer.Type_Tile){
				int r = (int)(y/h);
				int c = (int)(x/w);
				tileSelected = tl.getTile(r, c);
			}
		}
		return tileSelected;
	}
	
	public int getMoveX(int x) {
		int c = x/w;
		nowLeft = c*w;
		nowc = c<0 || c>=col ? -1 : c;
		return nowLeft+w/2;
	}
	
	public int getMoveY(int y) {
		int r =y/h;
		nowTop = r*h;
		nowr = r<0 || r>=row ? -1 : r;
		return nowTop+h/2;
	}
	
	public void setLayerName(int index, String name) {
		tls.get(index).setName(name);
	}
	
	public void setLayerIsShow(int index, boolean isShow) {
		tls.get(index).setIsShow(isShow);
	}

	public void setSelectLayer(int index) {
		TileManager tm = (TileManager)(MainFrame.self.getPanels()[MainFrame.LAYER_TILE].getManager());
		tm.setIsSelectedAll(false);
		tm.tileSelected.clear();
		MainFrame.self.repaintPanel();
		
		selectIndex = index;
	}

	public void moveUp(int index) {
		if(index > 0) {
			TileLayer tl = tls.get(index-1);
			tls.set(index-1, tls.get(index));
			tls.set(index, tl);
		}
	}

	public void moveDown(int index) {
		if(index < tls.size()-1) {
			TileLayer tl = tls.get(index+1);
			tls.set(index+1, tls.get(index));
			tls.set(index, tl);
		}
	}
	
	public void paintTileLayers(Graphics g) {
		for(int i=tls.size()-1; i>=0; --i){
			tls.get(i).panit(g);
		}
	}
	
	public void paintBackGround(Graphics g, int left, int top) {
        Graphics2D g2d = (Graphics2D)g;
		Color oldColor = g2d.getColor();	
		g2d.setColor(Color.BLACK);
		int w1 = col*w;
		int h1 = row*h;
	//	g2d.drawRect(left, top, w1, h1);
		
		Stroke st = g2d.getStroke();   
        //LINE_TYPE_DASHED   
        Stroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2,   4}, 0);
        g2d.setStroke(bs);		
		for(int i=1; i<row+1; ++i) {
			int x1 = left;
			int y1 = top+h*i;
			g2d.drawLine(x1, y1, x1+w1, y1);
		}
		
		for(int i=1; i<col+1; ++i) {
			int x1 = left+i*w;
			int y1 = top;
			g2d.drawLine(x1, y1, x1, y1+h1);
		}
		g2d.setColor(oldColor);
		g2d.setStroke(st);
	}
	
	public void load(DataInputStream in) throws Exception {
		int len = in.readInt();
		TileLayer tl = null;
		for(int i=0; i<len; ++i) {
			int type = in.readInt();
			String name = SL.readString(in);
			if(type == TileLayer.Type_Obj)
				tl = new TileLayer(name);
			else if(type == TileLayer.Type_Tile)
				tl = new TileLayer(name, row, col);
			tl.load(in);
			tls.add(tl);
		}
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(tls.size());
		for(int i=0; i<tls.size(); ++i) {
			tls.get(i).save(out);
		}
	}

	public void saveLua(StringBuffer tilesLua, int tabNum) {
		// 保存全局数据
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("width=");
		tilesLua.append(col);
		tilesLua.append(",height=");
		tilesLua.append(row);
		tilesLua.append(",tileWidth=");
		tilesLua.append(w);
		tilesLua.append(",tileHeight=");
		tilesLua.append(h);
		tilesLua.append(",\r\n");
		
		// 保存图块数据
//		tilesLua.append(XUtil.getTabs(tabNum));
//		tilesLua.append("tileImages={\r\n");
//		getTIIsUse();
//		TIManager tim = MainFrame.self.getTIManager();
//		tim.saveLua(tilesLua, tabNum+1);
//		tilesLua.append(XUtil.getTabs(tabNum));
//		tilesLua.append("},\r\n");
		
		// 保存对象数据
		int index = 0;
		for(int i=tls.size()-1; i>=0; --i) {
			if(tls.get(i).isHasTile()) {
				index++;
			}
		}
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("tileDatas={\r\n");
		for(int i=tls.size()-1; i>=0; --i) {
			if(tls.get(i).isHasProps()) {
				tilesLua.append(XUtil.getTabs(tabNum+1));
				tilesLua.append("["+index+"]=");
				tls.get(i).saveDataLua(tilesLua, tabNum+1);
				index--;
			}
		}
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("},\r\n");
		
		// 保存图层数据
		index = 1;
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("tileLayers={\r\n");
		for(int i=tls.size()-1; i>=0; --i) {
			if(tls.get(i).isHasTile()) {
				tilesLua.append(XUtil.getTabs(tabNum+1));
				tilesLua.append("["+index+"]=");
				tls.get(i).saveLua(tilesLua, tabNum+1);
				index++;
			}
		}
		tilesLua.append(XUtil.getTabs(tabNum));
		tilesLua.append("},\r\n");
	}
	
	private void getTIIsUse() {
		TIManager tim = MainFrame.self.getTIManager();
		for(int i=0; i<tls.size(); ++i) {
			if(tls.get(i).type == TileLayer.Type_Obj) {
				ArrayList<Tile> objs = tls.get(i).objs;
				for(int j=0; j<objs.size(); ++j) {
					TileData td = objs.get(j).td;
					if(!td.isUse)
						td.setIsUse(true);
					
					TileImage ti = tim.getTI(td.siId);
					if(!ti.isUse) {
						ti.setIsUse(true);
					}
				}
			}
			else {	// Type_Tile
				Tile[][] tiles = tls.get(i).tiles;
				for(int r=0; r<row; ++r) {
					for(int c=0; c<col; ++c) {
						if(tiles[r][c] != null) {
							TileData td = tiles[r][c].td;
							if(!td.isUse)
								td.setIsUse(true);
							
							TileImage ti = tim.getTI(td.siId);
							if(!ti.isUse) {
								ti.setIsUse(true);
							}
						}
					}
				}
			}
		}
	}
}

class TileLayerManagerTable extends JTable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3688690547521036000L;
	
	TileLayerManager tlm;
	MyTableModel myDataModel;
	boolean isInit;
	boolean isShowOthers;
	
	public TileLayerManagerTable() {
		myDataModel = new MyTableModel();
		setModel(myDataModel);
		setRowHeight(25);
		putClientProperty("terminateEditOnFocusLost", true);
		
		for(int i=0; i<2; ++i) {
			int w = 25+i*6;
			TableColumn firsetColumn = getColumnModel().getColumn(i);
			firsetColumn.setPreferredWidth(w);
			firsetColumn.setMaxWidth(w);
			firsetColumn.setMinWidth(w);
		}
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel rowSM = getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) return;

                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {
      //          	System.out.println("No rows are selected.");
                } else {
                	int selectedRow = lsm.getMinSelectionIndex();
                	setSelectLayer(selectedRow);
     //           	System.out.println("Row " + selectedRow + " is now selected.");
                }
            }
        });
        
        myDataModel.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if(isInit) return;
				
				int row = e.getFirstRow();
				int col = e.getColumn();
				Object data = getValueAt(row, col);
				if(col == 0) {
					setLayerIsShow(row, new Boolean(data.toString()).booleanValue());
				}
				else if(col == 2) {
						setLayerName(row, data.toString());
				}
		//		System.out.println("change("+row+", "+col+"): "+data.toString());
			}
		});
	}
	
	public void setTileLayerManager(TileLayerManager tlm) {
		if(this.tlm != null) {
			clear();
			this.tlm = null;
		}
		
		this.tlm = tlm;
		this.isShowOthers = false;
		init();
	}
	
	private void init() {
		if(tlm == null)
			return;
		
		isInit = true;
		ArrayList<TileLayer> tls = tlm.tls;
		for(int i=tls.size()-1; i>=0; --i) {
			myDataModel.addOneRow(tls.get(i).iconName, tls.get(i).name);
		}
		UpdateUI();
		isInit = false;
	}
	
	public void clear() {
		tlm.clear();
		myDataModel.delAllRow();
		UpdateUI();
	}
	
	public void UpdateUI() {
		SwingUtilities.invokeLater(new Runnable() {
			 public void run() {
				 updateUI();
			 }
		});
	}
	
	public boolean isInit() {
		return tlm != null;
	}
	
	public void setRC(int r, int c) {
		if(tlm == null) return;
		tlm.setRC(r, c);
	}
	
	public void insertLayer(String name, int type, int pos) {
		if(tlm == null) return;
		if(isEditing())
			removeEditor();
		TileLayer tl = tlm.CreateLayer(name, type);
		myDataModel.addOneRow(tl.iconName, tl.name);
		addRowSelectionInterval(pos, pos);
		editCellAt(pos, 2);
		JTextField tf = (JTextField)getCellEditor().getTableCellEditorComponent(this, tl.name, true, pos, 2);
		tf.requestFocus();
		tf.selectAll();
		tf.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
		//		editingStopped(null);
		//		removeEditor();
			}
			public void focusGained(FocusEvent e) {
			}
		});
	}
	
	public void removeLayer(int index) {
		if(tlm == null) return;
		tlm.delLayer(index);
		myDataModel.delOneRow(index);
	}

	public void setLayerIsShow(int index, boolean isShow) {
		if(tlm == null) return;
		tlm.setLayerIsShow(index, isShow);
		MainFrame.self.repaintPanel();
	}
	
	public void setLayerName(int index, String name) {
		if(tlm == null) return;
		tlm.setLayerName(index, name);
	}
	
	public void setSelectLayer(int index) {
		if(tlm == null) return;
		tlm.setSelectLayer(index);
		isShowOthers = false;
	}
	
	public void moveUp() {
		if(tlm == null) return;
		int index = getSelectedRow();
		if(index > 0) {
			tlm.moveUp(index);
			myDataModel.moveUp(index);
			addRowSelectionInterval(index-1, index-1);
			MainFrame.self.repaintPanel();
		}
	}
	
	public void moveDown() {
		if(tlm == null) return;
		int index = getSelectedRow();
		if(index < getRowCount()-1) {
			tlm.moveDown(index);
			myDataModel.moveDown(index);
			addRowSelectionInterval(index+1, index+1);
			MainFrame.self.repaintPanel();
		}
	}
	
	public void copyLayer() {
		if(tlm == null) return;
		int index = getSelectedRow();
		TileLayer tl = tlm.copyLayer(index);
		myDataModel.addOneRow(tl.iconName, tl.name);
		addRowSelectionInterval(0, 0);
	}
	
	public void delLayer() {
		if(tlm == null) return;
		int index = getSelectedRow();
		if(index >= 0) {
			tlm.delLayer(index);
			myDataModel.delOneRow(index);
			if(tlm.selectIndex >= 0)
				addRowSelectionInterval(tlm.selectIndex, tlm.selectIndex);
			else
				getSelectionModel().clearSelection();
			MainFrame.self.repaintPanel();
		}
	}
	
	public void setIsShowOtherLayer() {
		if(tlm == null) return;
		int index = getSelectedRow();
		if(index >= 0) {
			boolean isShow = new Boolean(getValueAt(index, 0).toString()).booleanValue();
			isShow = !isShow;
			for(int i=0; i<getRowCount(); ++i) {
				if(i != index)
					setValueAt(new Boolean(isShowOthers), i, 0);
				else
					setValueAt(new Boolean(true), i, 0);;
			}
			isShowOthers = !isShowOthers;
		}
	}
	
	public int getLayerNum() {
		return getRowCount();
	}
	
	public void load(DataInputStream in) throws Exception {
		if(tlm == null) return;
		tlm.load(in);
		init();
	}

	public void save(DataOutputStream out) throws Exception {
		if(tlm == null) return;
		tlm.save(out);
	}
	
	public void saveLua(String name) throws Exception{
		if(tlm == null) return;
		if(tlm.row == 0 || tlm.col == 0)
			return;
		
		StringBuffer tilesLua = new StringBuffer();
		tilesLua.append("\r\n\r\n--Tiles, 对象层4个数据一组, 为id, index, x, y; 图层数据2个一组, 为id, gridId\r\ntiles={\r\n");
		tlm.saveLua(tilesLua, 1);		
		tilesLua.append("}");
		
		String s = tilesLua.toString();
		String scriptPath = XUtil.getDefPropStr("ScriptPath");
		FileWriter writer = new FileWriter(scriptPath + "\\" + name + ".lua", true);
		writer.write(s);
		writer.flush();
		writer.close();
	}
	
	class MyTableModel extends AbstractTableModel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String[] columnNames = {"", "", ""};
        private Object[][] data = {
//        		{new Boolean(true), new ImageIcon("icon/save.png"), "Kathy"},
//        		{new Boolean(true), new ImageIcon("icon/save.png"), "Kathy"},
//        		{new Boolean(true), new ImageIcon("icon/save.png"), "Kathy"},
//        		{new Boolean(true), new ImageIcon("icon/save.png"), "Kathy"},
        };

        public int getColumnCount() {
            return columnNames.length;
        }

		public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
        	if(row < data.length)
        		return data[row][col];
        	return null;
        }
        
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
        	Object obj = getValueAt(0, c);
        	if(obj != null)
        		return obj.getClass();
        	return null;
        }
        
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col == 1) {
                return false;
            } else {
                return true;
            }
        }
        
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
        
        private void setCapacity() {
        	Object[][] tmp = new Object[data.length+1][];
        	for(int i=0; i<data.length; ++i) {
        		tmp[i+1] = new Object[data[i].length];
        		for(int j=0; j<data[i].length; ++j) {
        			tmp[i+1][j] = data[i][j];
        		}
        	}
        	tmp[0] = new Object[columnNames.length];
        	data = null;
        	data = tmp;
        }
        
        public void moveDown(int index) {
			if(index < data.length-1) {
				Object[] tmp = new Object[columnNames.length];
				for(int i=0; i<columnNames.length; ++i)
					tmp[i] = data[index+1][i];
				for(int i=0; i<columnNames.length; ++i)
					data[index+1][i] = data[index][i];
				for(int i=0; i<columnNames.length; ++i)
					data[index][i] = tmp[i];
				UpdateUI();
			}
		}

		public void moveUp(int index) {
			if(index > 0) {
				Object[] tmp = new Object[columnNames.length];
				for(int i=0; i<columnNames.length; ++i)
					tmp[i] = data[index-1][i];
				for(int i=0; i<columnNames.length; ++i)
					data[index-1][i] = data[index][i];
				for(int i=0; i<columnNames.length; ++i)
					data[index][i] = tmp[i];
				UpdateUI();
			}
		}
        
        public void addOneRow(String iconName, String layerName) {
        	setCapacity();
    		setValueAt(new Boolean(true), 0, 0);
    		setValueAt(new ImageIcon("icon/"+iconName), 0, 1);
    		setValueAt(layerName, 0, 2);
    		if(!isInit)
    			UpdateUI();
    	}
        
        public void delOneRow(int index) {
        	if(data.length > 0) {
        		Object[][] tmp = new Object[data.length-1][];
        		for(int i=0; i<index; ++i) {
            		tmp[i] = new Object[data[i].length];
            		for(int j=0; j<data[i].length; ++j) {
            			tmp[i][j] = data[i][j];
            		}
            	}
        		for(int i=index+1; i<data.length; ++i) {
            		tmp[i-1] = new Object[data[i].length];
            		for(int j=0; j<data[i].length; ++j) {
            			tmp[i-1][j] = data[i][j];
            		}
            	}
        		data = null;
            	data = tmp;
            	UpdateUI();
        	}
        }
        
        public void delAllRow() {
        	data = null;
        	data = new Object[][]{};
        }
    }
}

class IconButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7709836579260632893L;

	public IconButton(String iconPath, String tip) {
		super();
		setIcon(new ImageIcon(iconPath));
		setBorderPainted(false);
		setMargin(new Insets(0, 0, 0, 0));
		setFocusPainted(false);
		setContentAreaFilled(true);
		setToolTipText(tip);
	}
}

class IconsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<IconButton> buttons;
	
	public IconsPanel() {
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		buttons = new ArrayList<IconButton>();
	}
	
	public ArrayList<IconButton> getButtons() {
		return buttons;
	}
	
	public void addButton(IconButton b) {
		buttons.add(b);
		add(b);
	}
	
	public void setEnabledAll(boolean is) {
		for(int i=0; i<buttons.size(); ++i) {
			buttons.get(i).setEnabled(is);
		}
	}
	
	public void setEnabled(int index, boolean is) {
		buttons.get(index).setEnabled(is);
	}
	
	public int getPressedIndex(ActionEvent e) {
		for(int i=0; i<buttons.size(); ++i) {
			if(buttons.get(i) == e.getSource()){
		//		System.out.println("button "+(i+1));
				return i;
			}
		}
		return -1;
	}
}

class TileEdit {
	public static int TI_OffsetX = 0;
	public static int TI_OffsetY = 20;
	
	public static String[][] PathList_Pic = {
		{ "icon/save.png", "打开图片" },
	};
	
	public static String[][] PathList_Layer = {
		{ "icon/1.png", "新建图层" },
		{ "icon/1.png", "新建对象层" },
		{ "icon/2.png", "向上移动" },
		{ "icon/8.png", "向下移动" },
		{ "icon/3.png", "复制图层" },
		{ "icon/4.png", "删除图层" },
		{ "icon/5.png", "显示/隐藏其他图层" },
	};
	
	LayerPanel layerPanel;
	PicPanel picPanel;
	JSplitPane splitPane;
	TileImage ti;
	int pixW;
	int pixH;
	
	public TileEdit() {
		layerPanel = new LayerPanel();
		picPanel = new PicPanel();
		JScrollPane scrollPane = new JScrollPane(picPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.addMouseListener(picPanel);
		scrollPane.setPreferredSize(new Dimension(100, 500));
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, layerPanel, scrollPane);
	}
	
	public void setTileLayerManager(TileLayerManager tlm) {
		layerPanel.tlmTable.setTileLayerManager(tlm);
	//	layerPanel.iconsPanel.setEnabledAll(true);
		setTISelect(-1);
	}
	
	public void clear() {
		setPixWH(0, 0);
		setTileLayerManager(null);
	}
	
	public boolean isInitTileLayers() {
		return layerPanel.tlmTable.isInit();
	}
	
	public void setPixWH(int w, int h) {
		pixW = h*XUtil.getDefPropInt("TileDataWidth");
		pixH = w*XUtil.getDefPropInt("TileDataHeight");
	}
	
	public void setTileWH(int w, int h) {
		setPixWH(w, h);
		layerPanel.tlmTable.setRC(w, h);
	}

	public void setTISelect(int i) {
		if(ti != null) {
			if(ti.selectIndex != i) {
				ti.selectIndex = i;
				picPanel.repaint();
			}
		}
	}
	
	public void setIconsEnable(boolean enable) {
		if(isInitTileLayers()) {
			layerPanel.iconsPanel.setEnabledAll(enable);
		}
	}

	public void setTI(TileImage ti) {
		setTISelect(-1);
		
		this.ti = ti;
		picPanel.setTI(ti);
	}
	
	public void load(DataInputStream in) throws Exception {
		layerPanel.tlmTable.load(in);
	}

	public void save(DataOutputStream out) throws Exception {
		layerPanel.tlmTable.save(out);
	}
	
	public void saveLua(String name) throws Exception {
		layerPanel.tlmTable.saveLua(name);
	}
	
	private class LayerPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8086692990326985403L;
		private IconsPanel iconsPanel;
		private IconsActionListener iconsActionListener;
		
		private TileLayerManagerTable tlmTable;
		private JScrollPane showScroll;
		
		public LayerPanel() {
			setLayout(new BorderLayout());
			JLabel label = new JLabel("Tile图层");
			add(label, BorderLayout.NORTH);
			
			iconsPanel = new IconsPanel();
			iconsActionListener = new IconsActionListener();
			for(int i=0; i<PathList_Layer.length; ++i) {
				IconButton b = new IconButton(PathList_Layer[i][0], PathList_Layer[i][1]);
				b.addActionListener(iconsActionListener);
				iconsPanel.addButton(b);
			}
			iconsPanel.setEnabledAll(false);
			
			tlmTable = new TileLayerManagerTable();
			showScroll = new JScrollPane(tlmTable);//, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			showScroll.setMinimumSize(new Dimension(100, 200));
			add(showScroll, BorderLayout.CENTER);
			add(iconsPanel, BorderLayout.SOUTH);
		}
		
		public void clear() {
			tlmTable.clear();
		}
		
		public void addLayerTile() {
			tlmTable.insertLayer("图层"+tlmTable.getLayerNum(), TileLayer.Type_Tile, 0);
		}
		
		public void addLayerObj() {
			tlmTable.insertLayer("对象层"+tlmTable.getLayerNum(), TileLayer.Type_Obj, 0);
		}
		
		public void moveUp() {
			tlmTable.moveUp();
		}
		
		public void moveDown() {
			tlmTable.moveDown();
		}
		
		public void copyLayer() {
			tlmTable.copyLayer();
		}
		
		public void delLayer() {
			tlmTable.delLayer();
		}
		
		public void setIsShowOtherLayer() {
			tlmTable.setIsShowOtherLayer();
		}
		
		private class IconsActionListener implements ActionListener {			
			public void actionPerformed(ActionEvent e) {
				int index = iconsPanel.getPressedIndex(e);
				tlmTable.removeEditor();
				tlmTable.requestFocus();
				switch(index) {
					case 0:
						addLayerTile();
						break;
					case 1:
						addLayerObj();
						break;
					case 2:
						moveUp();
						break;
					case 3:
						moveDown();
						break;
					case 4:
						copyLayer();
						break;
					case 5:
						delLayer();
						break;
					case 6:
						setIsShowOtherLayer();
						break;
				}
			}
		}
	}
		
	private class PicPanel extends JPanel implements MouseListener, ActionListener {		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private IconsPanel iconsPanel;
//		private IconsActionListener iconsActionListener;
//		private PicTab picTab; 
		
		JPopupMenu popMenu;
		JMenuItem tilePropMenu;
		JMenuItem showGridMenu;
		
		private TileImage ti;
		
		public PicPanel() {
			setLayout(new BorderLayout());
			JLabel label = new JLabel("Tile图块");
			add(label, BorderLayout.NORTH);
			
			popMenu = new JPopupMenu();
			
			tilePropMenu = new JMenuItem("  图块属性");
			tilePropMenu.addActionListener(this);
			showGridMenu = new JMenuItem("√显示网格");
			showGridMenu.addActionListener(this);
			
			popMenu.add(tilePropMenu);
			popMenu.add(showGridMenu);
			
			addMouseListener(this);
			
//			iconsPanel = new IconsPanel();
//			iconsActionListener = new IconsActionListener();
//			for(int i=0; i<PathList_Pic.length; ++i) {
//				IconButton b = new IconButton(PathList_Pic[i][0], PathList_Pic[i][1]);
//				b.addActionListener(iconsActionListener);
//				iconsPanel.addButton(b);
//			}
//			
//			picTab = new PicTab();
//			for(int i=0; i<5; ++i) {
//				picTab.addPic("pic "+(i+1));
//			}
			
//			add(picTab, BorderLayout.CENTER);
//			add(iconsPanel, BorderLayout.SOUTH);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(ti != null) {
				ti.paintLeftTop(g, TI_OffsetX, TI_OffsetY);
			}
		}
		
		public void setTI(TileImage ti) {
			this.ti = ti;
			resizePanel();
			repaint();
		}
		
		private void resizePanel() {
			if(ti !=null){
				int w =ti.getRealWidth()+TI_OffsetX;
				int h =ti.getRealHeight()+TI_OffsetY;
				setPreferredSize(new Dimension(w, h));
				getParent().setPreferredSize(new Dimension(w, h));
				revalidate();
			}
		}
		
		private class IconsActionListener implements ActionListener {	
			public void actionPerformed(ActionEvent e) {
				int index = iconsPanel.getPressedIndex(e);
				if(index == 0){ // 打开图片
					try {
						new OpenPic(MainFrame.self);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		public void mouseClicked(MouseEvent e) {}
		
		public void mousePressed(MouseEvent e) {
			if(ti != null) {
				ti.mouseLeftPressed(e.getX()-TI_OffsetX, e.getY()-TI_OffsetY);
				if(e.getButton() == 3) {
					popMenu.show(this, e.getX(), e.getY());
				}
				else {
					
				}
				repaint();
			}
		}

		public void mouseReleased(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(ti !=null){
				if(e.getSource() == tilePropMenu) {
					if(ti.selectIndex >= 0) {
						TileData td = ti.getSelectedTD();
						TilePropDialog setter;
						setter = new TilePropDialog(MainFrame.self, td.props);
						setter.setTitle("设置图块属性");
						setter.show();
					}
				}
				else if(e.getSource() == showGridMenu) {
					if(ti != null) {
						if(ti.ChangeOffset())
							showGridMenu.setText("√显示网格");
						else
							showGridMenu.setText("  显示网格");
						repaint();
					}
				}
			}
		}
		
		private class OpenPic extends OKCancelDialog {
			private JFileChooser fc;
			private NumberSpinner widthSpinner;
			private NumberSpinner heightSpinner;
			private JTextField fileText;
			
			public OpenPic(JFrame owner) throws Exception {
				super(owner);
				init();
			}

			public OpenPic(JDialog owner) throws Exception {
				super(owner);
				init();
			}
			
			private void init() throws Exception  {
				setTitle("打开图片");
				setSize(new Dimension(300, 150));
				fc = new JFileChooser(".\\res");
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        
				widthSpinner = new NumberSpinner();
				widthSpinner.setIntValue(10);
				
				heightSpinner = new NumberSpinner();
				heightSpinner.setIntValue(10);
		        
		        fileText = new JTextField();
		        JButton fileButton = new JButton("...");
		        fileButton.addActionListener(new ActionListener() {
		        	public void actionPerformed(ActionEvent e) {
		        		chooseFiles();
		        	}
		        });
		        
		        JPanel p = new JPanel();
				p.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.BOTH;
				c.insets = new Insets(2, 2, 3, 3);
				
				c.gridy = 0;
				c.gridx = 0;
				c.weighty = 0;
				c.weightx = 0;
				p.add(new JLabel("文件名："), c);
				c.gridx = 1;
				c.weightx = 1;
				p.add(fileText, c);
				c.gridx = 2;
				c.weightx = 0;
				p.add(fileButton, c);
				
				c.gridy = 1;
				c.gridx = 0;
				c.weighty = 0;
				c.weightx = 0;
				p.add(new JLabel("块宽："), c);
				c.gridx = 1;
				c.weightx = 1;
				c.gridwidth = 2;
				p.add(widthSpinner, c);
				
				c.gridy = 2;
				c.gridx = 0;
				c.weighty = 0;
				c.weightx = 0;
				p.add(new JLabel("块高："), c);
				c.gridx = 1;
				c.weightx = 1;
				c.gridwidth = 2;
				p.add(heightSpinner, c);
				
				buttonPanel.add(okButton);
				buttonPanel.add(cancelButton);

				Container cp = this.getContentPane();
				cp.setLayout(new BorderLayout());
				cp.add(p, BorderLayout.CENTER);
				cp.add(buttonPanel, BorderLayout.SOUTH);
				
				show();
			}
			
			private void chooseFiles() {
				int ct = fc.showOpenDialog(this);
				if(ct == JFileChooser.APPROVE_OPTION ) {
					String name = fc.getSelectedFile().getAbsolutePath();
					fileText.setText(name);
				}
				
			}

			@Override
			protected void cancelPerformed() {
				dispose();
			}

			@Override
			protected void okPerformed() {
				String name = fileText.getText();
				
				
				System.out.println(name);
				dispose();
			}
		}
		
		private class PicTab extends JTabbedPane {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			Image drawImg;
			BufferedImage originalBufImage; // 原始缓冲区图像
			
			public PicTab(){
				setInputMap(JScrollPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new InputMap());
			}
			
			public void addPic(String text) {
				JScrollPane sp = new JScrollPane();
				add(text, sp);
			}
		}
	}

}

class TileProps {
	
	ArrayList<String> props;
	String name;
	
	public TileProps() {
		props = new ArrayList<String>();
	}
	
	public void print() {
		System.out.println(name+":");
		for(int i=0; i<props.size(); ++i)
			System.out.println(i+": "+props.get(i));
	}
	
	public TileProps copySelf() {
		TileProps tp = new TileProps();
		tp.setName(name);
		tp.setProps(props);
		return tp;
	}
	
	public void setProps(ArrayList<String> ps) {
		props.clear();
		for(int i=0; i<ps.size(); ++i)
			props.add(new String(ps.get(i)));
	}
	
	public void setName(String name) {
		this.name = name;
	}
}

class TileInfo {
	
	ArrayList<TileProps> tps;
	
	public TileInfo() {
		tps = new ArrayList<TileProps>();
	}
	
	public TileProps getProps(int index) {
		return tps.get(index);
	}
	
	public ArrayList<TileProps> getCopyTps() {
		ArrayList<TileProps> tpsCopy = new ArrayList<TileProps>();
		for(int i=0; i<tps.size(); ++i) {
			tpsCopy.add(tps.get(i).copySelf());
		}
		return tpsCopy;
	}
	
	public void setProps(ArrayList<TileProps> tps) {
		this.tps.clear();
		for(int i=0; i<tps.size(); ++i) {
			this.tps.add(tps.get(i).copySelf());
		}
	}
	
	public void readIniFile() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(
		        "DefaultTileProp.ini")));
		String sLine;
		sLine = in.readLine();
		TileProps tp = null;
		while (sLine != null) {
			if(!sLine.equals("")) {
				if(sLine.charAt(0) == '$') {
					tp = null;
					tp = new TileProps();
					tp.setName(sLine.substring(1));
					tps.add(tp);
				}
				else
					tp.props.add(sLine);
			}
			sLine = in.readLine();
		}
		in.close();
	}
	
	public void writeIniFile() throws IOException {
		DataOutputStream tout;
		String s = "";
		for(int i=0; i<tps.size(); ++i) {
			TileProps tp = tps.get(i);
			s += "$" + tp.name + "\r\n";
			for(int j=0; j<tp.props.size(); ++j) {
				s +=tp.props.get(j) + "\r\n";
			}
			s += "\r\n";
		}

		tout = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
				"DefaultTileProp.ini")));
		byte[] bytes = s.getBytes();
		tout.write(bytes);
		tout.flush();
		tout.close();
	}
}

class TilePropDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Object[]  newIdentifiers = {
		"属性名称", "默认值"
	};
	
	private static Object[]  newIdentifiers2 = {
		"属性名称", "值"
	};
	
	private static Object[]  newIdentifiers3 = {
		"属性组列表"
	};
	
	public final static int CANCEL_PERFORMED = 0;
	public final static int OK_PERFORMED = 1;

	private int closeType;
	private int selectIndex;
	
	private JButton okButton;
	private JButton cancelButton;
	private JButton newButton;
	private JPanel buttonPanel;
	
	private String def_name = "新的属性名称";
	private String def_value = "0";
	
	private JTable table, table2;
	private DefaultTableModel dtm, dtm2;
	private ArrayList<TileProps> tps;
	private ArrayList<String> props;
	private ArrayList<String> props2;
	private boolean isInit;
	private boolean isBase;
	
	public TilePropDialog(JFrame owner) {
		super(owner, true);
		
		isBase = true;
		initData(null);
		initUI();
		setLocationRelativeTo(owner);
	}
	
	public ArrayList<String> getProps() {
		return props;
	}

	public TilePropDialog(JFrame owner, ArrayList<String> props) {
		super(owner, true);
		
		isBase = false;
		initData(props);
		initUI();
		setLocationRelativeTo(owner);
	}
	
	public TilePropDialog(JDialog dialogOwner, ArrayList<String> props) {
		super(dialogOwner, true);
		
		isBase = false;
		initData(props);
		initUI();
		setLocationRelativeTo(dialogOwner);
	}

	private void initData(ArrayList<String> props) {
		props2 = props;
		selectIndex = -1;
		tps = MainFrame.self.getBaseTileInfo().getCopyTps();
		if(props != null) {
			this.props = new ArrayList<String>();
			for(int i=0; i<props.size(); ++i)
				this.props.add(new String(props.get(i)));
		}
	}
	
	private void initUI() {		
		int w = XUtil.getDefPropInt(XUtil.getClassName(getClass()) + "Width");
		int h = XUtil.getDefPropInt(XUtil.getClassName(getClass()) + "Height");
		if(w > 0 && h >0)
			setSize(new Dimension(w, h));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		closeType = -1;
		
		newButton = new JButton("新建");
		newButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				newPerformed();
			}
		});
		okButton = new JButton("保存");
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				okPerformed();
			}
		});
		cancelButton = new JButton("取消");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cancelPerformed();
			}
		});

		buttonPanel = new JPanel();
		GridLayout buttonLayout = new GridLayout();
		buttonLayout.setHgap(10);
		buttonPanel.setLayout(buttonLayout);
		buttonPanel.setPreferredSize(new Dimension(100, XUtil.getDefPropInt("ButtonPanelHeight")));
		
		dtm = new DefaultTableModel(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 8778753162645941855L;

			public boolean isCellEditable(int row, int column) {
				if(isBase)
					return true;
				else
					return false;
			  }
		};
		dtm.setColumnIdentifiers(newIdentifiers3);
		table = new JTable(dtm);
		table.setRowHeight(20);
		table.putClientProperty("terminateEditOnFocusLost", true);
	    table.getTableHeader().setReorderingAllowed(false);
		
		DefaultTableCellHeaderRenderer thr = new DefaultTableCellHeaderRenderer();
	    thr.setHorizontalAlignment(JLabel.CENTER);
	    thr.setToolTipText("属性名不能包含中文，只能以字母跟下划线开头。");
	    
		dtm2 = new DefaultTableModel();
		table2 = new JTable(dtm2);
		table2.setRowHeight(20);
		table2.putClientProperty("terminateEditOnFocusLost", true);
		table2.setCellSelectionEnabled(true);
	    table2.getTableHeader().setDefaultRenderer(thr);
	    table2.getTableHeader().setReorderingAllowed(false);
	    
	    JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());
		JScrollPane scrollPane = new JScrollPane(table);
		JScrollPane scrollPane2 = new JScrollPane(table2);
		panel.add(scrollPane);
		panel.add(scrollPane2);
		
	    if(isBase) {
	    	buttonPanel.add(newButton);
	    	dtm2.setColumnIdentifiers(newIdentifiers);
	    }
	    else {
	    	dtm2.setColumnIdentifiers(newIdentifiers2);
	    }
	    buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
	    
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(panel, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel rowSM = table.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) return;

                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {
                	
                } else {
                	int selectedRow = lsm.getMinSelectionIndex();
                	setPropsGroup(selectedRow);
         //       	System.out.println("Row " + selectedRow + " is now selected.");
                }
            }
        });
		
		dtm.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if(isBase) {
					int row = e.getFirstRow();
				//	int col = e.getColumn();
					String name = dtm.getValueAt(row, 0).toString();
					tps.get(row).setName(name);
				}
			}
		});
		
		dtm2.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if(isInit) return;
				
				int row = e.getFirstRow();
				int col = e.getColumn();
				int rowCount = dtm2.getRowCount();
				String v = dtm2.getValueAt(row, 0).toString();
				String v2 = dtm2.getValueAt(row, 1).toString();
				if(row < rowCount-1 && v.equals("")) {
					delOneRow(row);
				}
				else {
					boolean isChangeV2 = false;
					boolean isChangeV = false;
					if(col == 0) {
						if(!v.equals(def_name)) {
							if(!checkNameIsCorrect(v))
								dtm2.setValueAt(def_name, row, 0);
							else  if(row == rowCount-1)
								isChangeV = true;
						}
					}
					else {
						String s = getCorrectValue(v2);
						if(!s.equals(v2))
							dtm2.setValueAt(s, row, 1);
						else if(row == rowCount-1)
							isChangeV2 = true;
					}
					if((isChangeV && !v2.equals("")) || (isChangeV2 && !v.equals(def_name)))
						addOneRow(def_name, def_value);
				}
			}
		});
		
		// 更新table
		for(int i=0; i<tps.size(); ++i) {
			dtm.addRow(new Object[]{tps.get(i).name});
		}
		
		if(!isBase) {
			isInit = true;
			updateTable2(props);
			isInit = false;
		}
	}
	
	private void updateTable2(ArrayList<String> ps) {
		for(int i=dtm2.getRowCount()-1; i>=0; --i) {
			dtm2.removeRow(i);
		}
		
		if(ps != null) {
			for(int i=0; i<ps.size(); ++i) {
				String p = ps.get(i);
				int index = p.indexOf('=');
				addOneRow(p.substring(0, index), p.substring(index+1));
			}
			addOneRow(def_name, def_value);
		}
	}
	
	private void setPropsGroup(int selectedRow) {
		if(selectedRow < 0) return;
		
		isInit = true;
		if(isBase) {
			if(selectIndex >= 0) {
				TileProps tp = tps.get(selectIndex);
				tp.props.clear();
				for(int i=dtm2.getRowCount()-2; i>=0; --i) {
					String name = dtm2.getValueAt(i, 0).toString();
					String value = dtm2.getValueAt(i, 1).toString();
					tp.props.add(0, name+"="+value);
				}
			}
			
			updateTable2(tps.get(selectedRow).props);
		}
		else {	// 设置图块属性
			int n = JOptionPane.showConfirmDialog(null,"确定重新加载属性?","确定",JOptionPane.YES_NO_OPTION);
			if(n ==0){
				ArrayList<String> newProps = tps.get(selectedRow).props;
				props.clear();
				for(int i=0; i<newProps.size(); ++i)
					props.add(new String(newProps.get(i)));
				updateTable2(props);
			}
			else {
				SwingUtilities.invokeLater(new Runnable() {
					 public void run() {
						 table.removeRowSelectionInterval(0, dtm.getRowCount()-1);
					 }
				});
			}
		}
		isInit = false;
		
		selectIndex = selectedRow;
	}
	
	private void addOneRow(String name, String value) {
		Object[] rowData = {name, value};
		dtm2.addRow(rowData);
	}
	
	private void delOneRow(int index) {
		dtm2.removeRow(index);
	}
	
	private boolean checkNameIsCorrect(String s) {
		boolean is = true;
		if(s.equals("")) {
			is = false;
		}
		else {
			char c = s.charAt(0);
			if(!(c == '_' || c>='a'&&c<='z' || c>='A'&&c<='Z')) {
				is = false;
			}
			else {
				for(int i=1; i<s.length(); ++i) {
					c = s.charAt(i);
					if(!(c == '_' || c>='a'&&c<='z' || c>='A'&&c<='Z' || c>='0' && c<='9')) {
						is = false;
						break;
					}
				}
			}
		}
		
		return is;
	}
	
	private int checkNameIsSameBase() {
		for(int i=0; i<tps.size(); ++i) {
			ArrayList<String> props = tps.get(i).props;
			for(int j=0; j<props.size(); ++j) {
				String name = props.get(j);
				name = name.substring(0, name.indexOf('='));
				for(int k=j+1; k<props.size(); ++k) {
					String name2 = props.get(k);
					name2 = name2.substring(0, name2.indexOf('='));
					if(name.equals(name2))
						return i;
				}
			}
		}
		
		return -1;
	}
	
	private boolean checkNameIsSame() {
		int len = dtm2.getRowCount()-1;
		for(int i=0; i<len; ++i) {
			String name = dtm2.getValueAt(i, 0).toString();
			for(int j=i+1; j<len; ++j) {
				String name2 = dtm2.getValueAt(j, 0).toString();
				if(name.equals(name2)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private String getCorrectValue(String s) {		
		String sc = null;
		boolean isAllNum = true;
		if(s.length() == 0)
			return "0";
		
		for(int i=0; i<s.length(); ++i) {
			char c = s.charAt(i);
			if(!(c =='.' || c == '-' || (c>='0' && c<='9'))) {
				isAllNum = false;
				break;
			}
		}
		if(isAllNum) {
			int beginIndex = 0;
			int endIndex = s.length();
			int dotIndex = s.indexOf('.');
			if(dotIndex > 0) {
				if(dotIndex > 1) {
					beginIndex = dotIndex-1;
					for(int i=0; i<beginIndex; ++i) {
						if(s.charAt(i) != '0') {
							beginIndex = i;
							break;
						}
					}
				}
				
				String s2 = s.substring(dotIndex+1);
				int dotIndex2 = s2.indexOf('.');
				if(dotIndex2 > 0)
					endIndex = dotIndex2+dotIndex+1;
			}
			else {
				beginIndex = endIndex-1;
				for(int i=0; i<beginIndex; ++i) {
					if(s.charAt(i) != '0') {
						beginIndex = i;
						break;
					}
				}
			}
			sc = s.substring(beginIndex, endIndex);
		}
		else {	// 非数字
			char[] cs = new char[s.length()+2];
			cs[0] = '\"';
			int nowi = 1;
			for(int i=0; i<s.length(); ++i) {
				char c = s.charAt(i);
				if(c == '_' || c>='a'&&c<='z' || c>='A'&&c<='Z' || c>='0' && c<='9') {
					cs[nowi++] = c;
				}
			}
			cs[nowi++] = '\"';
			sc = new String(cs);
			sc = sc.trim();
		}
		
		return sc;
	}
	
	public ArrayList<TileProps> getTps() {
		return tps;
	}
	
	public int getCloseType() {
		return closeType;
	}

	private void cancelPerformed() {
		MainFrame.self.getMouseInfo().resetSelf();
		closeType = CANCEL_PERFORMED;
		dispose();
	}
	
	private void newPerformed() {
		TileProps tp = new TileProps();
		tps.add(tp);
		
		int row = dtm.getRowCount();
		dtm.addRow(new Object[]{""});
		table.addRowSelectionInterval(row, row);
		table.editCellAt(row, 0);
		JTextField tf = (JTextField)table.getCellEditor().getTableCellEditorComponent(table, "", true, row, 0);
		tf.requestFocus();
		tf.selectAll();
	}

	private void okPerformed() {
		if(isBase) {
			int row = table.getSelectedRow();
			setPropsGroup(row);
			
			int index = checkNameIsSameBase();
			if(index >= 0) {
				JOptionPane.showMessageDialog(null, "属性组 " + tps.get(index).name +" 有属性名称重复！", "警告", JOptionPane.WARNING_MESSAGE);
			}
			else {			
				closeType = OK_PERFORMED;
				dispose();
			}
		}
		else {
			if(checkNameIsSame()) {
				JOptionPane.showMessageDialog(null, "属性名称重复！", "警告", JOptionPane.WARNING_MESSAGE);
			}
			else {
				props.clear();
				for(int i=0; i<dtm2.getRowCount()-1; ++i) {
					String name = dtm2.getValueAt(i, 0).toString();
					String value = dtm2.getValueAt(i, 1).toString();
					props.add(name+"="+value);
			//		System.out.println(props.get(props.size()-1));
				}
				
				props2.clear();
				for(int i=0; i<props.size(); ++i) {
					props2.add(props.get(i));
				}
				
				MainFrame.self.getMouseInfo().resetSelf();
				closeType = OK_PERFORMED;
				dispose();
			}
		}
	}
	
}