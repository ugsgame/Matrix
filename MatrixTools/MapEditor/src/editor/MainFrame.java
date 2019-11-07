package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalLookAndFeel;

import test.check.SampleMenuFactory;

public class MainFrame extends JFrame {

	public static Font DEF_FONT = new Font("Dialog", Font.PLAIN, 12);
	static String substance = "org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel";

	public static void main(String[] args) {
//		try {
//			DefaultTheme theme = new DefaultTheme();
//			MetalLookAndFeel.setCurrentTheme(theme);
//			DEF_FONT = theme.getDefaultFont();
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			JOptionPane.showMessageDialog(null, e, "初始化错误", JOptionPane.ERROR_MESSAGE);
//			System.exit(0);
//		}
//		
//		MainFrame w = new MainFrame();
//	    w.setVisible(true);
		
		JFrame.setDefaultLookAndFeelDecorated(true);
	    SwingUtilities.invokeLater(new Runnable() {
	      public void run() {
	        try {
	          UIManager.setLookAndFeel(substance);
	        } catch (Exception e) {
	          System.out.println("Substance Graphite failed to initialize");
	        }
	        MainFrame w = new MainFrame();
	        w.setVisible(true);
		}
		});
	}

	public final static int SCR_W = 204;
	public final static int SCR_H = 320;
	public final static int MAP_BLOCK_WIDTH = SCR_W;

	public final static int RES_COUNT = 3;
	public final static int RES_TILE = 0;
	public final static int RES_FLOOR = 1;
	public final static int RES_ENEMY = 2;
	
	public final static int LAYER_MAX_LAYER = 4;
	public final static int LAYER_COUNT = 7;
	public final static int LAYER_FLOOR = 0;
	public final static int LAYER_ENEMY = 1;
	public final static int LAYER_LINE = 2;
	public final static int LAYER_RECT = 3;
	public final static int LAYER_POINT = 4;
	public final static int LAYER_GRID = 5;
	public final static int LAYER_TILE = 6;
	public final static String[] LAYER_NAMES = { "地图", "敌人", "逻辑线", "逻辑矩形", "逻辑点", "逻辑网格", "图层" };
	public final static String[] LAYER_SAVE_NAMES = { "map", "enemy", "line", "rect", "point", "grid", "tile" };
	public final static String[] TREE_NAMES = LAYER_NAMES;

	public static MainFrame self;

	private ProgressDialog progress;

	private SIManager[] resManagers;

	private MouseInfo mouseInfo;
	private MapInfo mapInfo;
	private TileInfo tileInfo;

	// main component
	private PainterTree[] trees;
	private ManagerPanel[] panels;
	private JTabbedPane treeTab;
	private JTabbedPane panelTab;
	private InfoPanel infoPanel;

	// menus
	private JMenuBar menuBar;
	// menu file
	private JMenu menuFile;
	private JMenuItem menuFileNew;
	private JMenuItem menuFileLoad;
	private JMenuItem menuFileSave;
	private JMenuItem menuTileSave;
	private JMenuItem menuFileSavePNG;
	// menu edit
	private JMenu menuEdit;
	private JMenuItem menuEditResetOrigin;
	private JMenuItem menuEditMapProp;
	private JMenuItem menuEditTileProp;
	private JMenuItem menuEditFlip;
	// menu manage
	private JMenu menuManage;
	private JMenuItem menuManageEvents;
	private JMenuItem menuManageEARs;
	private JMenuItem menuManageTest;
	
	//
	LayerManager layerManager;
	
	//tile
	TileEdit tileEdit;

	// toolbar
	private JToolBar toolBar;
	private JButton toolFlip;

	private MenuActionListener menuActionListener;

	private int tabIndex;

	private HeadResManager headResManager;

	private Event[] events = null;
	private SwitchManager switchManager = new SwitchManager();

	public MainFrame() {
		// while(true) {
		// try {
		// synchronized(this) {
		// wait(5000);
		// }
		// }
		// catch(Exception e) {
		// e.printStackTrace();
		// }
		// System.out.println("A");
		// break;
		// }
		// int xx = 0;
		// if(++xx == 1) return;
		self = this;
	//	setLAF(substance);
		setMapName("无");
		try {
			setIconImage(XImage.readPNG(new File(".\\icon.png"), null));
		}
		catch (Exception e) {}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(XUtil.getDefPropInt("MainFrameWidth"),
		        XUtil.getDefPropInt("MainFrameHeight")));
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {

			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		try {
			init();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setLAF(String s) {
        try {
            UIManager.setLookAndFeel(s);
            SwingUtilities.updateComponentTreeUI(this);//更新控件的外观
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private void init() throws Exception {
		// 初始化进度条
		progress = new ProgressDialog(this);
		progress.setTitle("正在启动");
		progress.setInfo("Loading");
		progress.setValue(0);
		progress.show();

		// 加载资源
		initResManagers();
		mouseInfo = new MouseInfo();
		mapInfo = new MapInfo(XUtil.getDefPropInt("DefMapWidth"),
		        XUtil.getDefPropInt("DefMapHeight"), XUtil.getDefPropStr("DefMapName"));
		tileInfo = new TileInfo();
		tileInfo.readIniFile();
		menuActionListener = new MenuActionListener();

		// 加载其它资源
		progress.setInfo("加载头像");
		headResManager = new HeadResManager();
		progress.setValue(70);
		
		// 加载各个控件
		progress.setInfo("初始化控件");
		initTrees();
		initPanels();
		initTab();
		initInfo();
		initMenu();
		initToolBar();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeTab, panelTab);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(200);
		
		tileEdit = new TileEdit();
		layerManager = new LayerManager(mouseInfo);
		JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, layerManager.panel, tileEdit.splitPane);
		splitPane2.setOneTouchExpandable(true);
		splitPane2.setDividerLocation(200);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane, splitPane2);
		splitPane.setResizeWeight(1);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(800);

		tabIndex = XUtil.getDefPropInt("DefLayer");
		treeTab.setSelectedIndex(tabIndex);
		tabChanged();

		this.setJMenuBar(menuBar);
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(toolBar, BorderLayout.NORTH);
		cp.add(splitPane, BorderLayout.CENTER);
		cp.add(infoPanel, BorderLayout.SOUTH);

		progress.setValue(100);
		progress.hide();
		show();
	}

	private void initResManagers() throws Exception {
		resManagers = new SIManager[RES_COUNT];
		
		progress.setInfo("读取图层图片");
		TIManager tim = new TIManager("Tile");
		tim.load();
		resManagers[RES_TILE] = tim;
		progress.setValue(15);

		progress.setInfo("读取地图图片");
		resManagers[RES_FLOOR] = new SIManager("Map");
		progress.setValue(30);

		progress.setInfo("读取敌人图片");
		resManagers[RES_ENEMY] = new SIManager("Enemy");
		progress.setValue(35);

		progress.setValue(50);
	}

	private void initTrees() {
		trees = new PainterTree[LAYER_COUNT];
		
		trees[LAYER_TILE] = new PainterTree(TREE_NAMES[LAYER_TILE],
		        SIPainterGroup.getGroups(((SIManager) (resManagers[RES_TILE])).getGroups()),
		        SIPainter.getPainters(((SIManager) (resManagers[RES_TILE])).getSIs()), mouseInfo);
		trees[LAYER_TILE].setSelect(MouseInfo.SELECT_TILE);

		trees[LAYER_FLOOR] = new PainterTree(TREE_NAMES[LAYER_FLOOR],
		        SIPainterGroup.getGroups(((SIManager) (resManagers[RES_FLOOR])).getGroups()),
		        SIPainter.getPainters(((SIManager) (resManagers[RES_FLOOR])).getSIs()), mouseInfo);

		trees[LAYER_ENEMY] = new PainterTree(TREE_NAMES[LAYER_ENEMY], null,
		        SIPainter.getPainters(((SIManager) (resManagers[RES_ENEMY])).getSIs()), mouseInfo);

		trees[LAYER_LINE] = new PainterTree(TREE_NAMES[LAYER_LINE], null,
		        LinePainter.getPainters(), mouseInfo);

		trees[LAYER_RECT] = new RectPainterTree(TREE_NAMES[LAYER_RECT], mouseInfo);
		try {
			loadRects();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		trees[LAYER_POINT] = new PainterTree(TREE_NAMES[LAYER_POINT], SIPainterGroup.getGroups(PointPainter.getGroups()),
				PointPainter.getPainters(), mouseInfo);
		
		trees[LAYER_GRID] = new PainterTree(TREE_NAMES[LAYER_GRID], null,
				GridPainter.getPainters(), mouseInfo);

		//											
		// trees[LAYER_Enemy].addMouseListener(new MouseAdapter() {
		// public void mouseClicked(MouseEvent e) {
		// unitTreeMouseClicked(e);
		// }
		// });
	}

	private void initPanels() {
		panels = new ManagerPanel[LAYER_COUNT];
		panels[LAYER_FLOOR] = new FloorPanel(this, mouseInfo);
		panels[LAYER_ENEMY] = new EnemyPanel(this, mouseInfo);
		panels[LAYER_LINE] = new LinePanel(this, mouseInfo);
		panels[LAYER_RECT] = new RectPanel(this, mouseInfo);
		panels[LAYER_POINT] = new PointPanel(this, mouseInfo);
		panels[LAYER_GRID] = new GridPanel(this, mouseInfo);
		panels[LAYER_TILE] = new TilePanel(this, mouseInfo);
		
		panels[LAYER_FLOOR].addBackManager(panels[LAYER_TILE].getManager());
		panels[LAYER_ENEMY].addBackManager(panels[LAYER_TILE].getManager());
		panels[LAYER_ENEMY].addBackManager(panels[LAYER_FLOOR].getManager());
		
		for(int i=LAYER_LINE; i<=LAYER_GRID; ++i) {
			panels[i].addBackManager(panels[LAYER_TILE].getManager());
			panels[i].addBackManager(panels[LAYER_FLOOR].getManager());
			panels[i].addBackManager(panels[LAYER_ENEMY].getManager());
		}
	}

	private void initTab() {
		treeTab = new JTabbedPane();
		treeTab.setInputMap(JScrollPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new InputMap());
		for (int i = 0; i < trees.length; ++i) {
			treeTab.addTab(LAYER_NAMES[i], trees[i].getScrollPane());
		}
		treeTab.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				treeTabChanged();
			}
		});
		
		panelTab = new JTabbedPane();
		panelTab.setInputMap(JScrollPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new InputMap());
		for (int i = 0; i < panels.length; ++i) {
			panelTab.addTab(LAYER_NAMES[i], panels[i].getBackPanel());
		}
		panelTab.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				panelTabChanged();
			}
		});
	}

	private void initInfo() {
		infoPanel = new InfoPanel();
	}

	private void initMenu() {
		// menuBar
		menuBar = new JMenuBar();
		// menuFile
		menuFile = new JMenu("文件");
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menuFile);
		// menuFileNew
		menuFileNew = new JMenuItem("新建地图", KeyEvent.VK_N);
		menuFileNew.setActionCommand("NEWMAP");
		menuFileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		menuFileNew.addActionListener(menuActionListener);
		menuFile.add(menuFileNew);
		// menuFileLoad
		menuFileLoad = new JMenuItem("加载地图", KeyEvent.VK_L);
		menuFileLoad.setActionCommand("LOADMAP");
		menuFileLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		menuFileLoad.addActionListener(menuActionListener);
		menuFile.add(menuFileLoad);
		// menuFileSave
		menuFileSave = new JMenuItem("保存", KeyEvent.VK_S);
		menuFileSave.setActionCommand("SAVEMAP");
		menuFileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menuFileSave.addActionListener(menuActionListener);
		menuFile.add(menuFileSave);
		// menuTileSave
		menuTileSave = new JMenuItem("保存图块", KeyEvent.VK_T);
		menuTileSave.setActionCommand("SAVETILE");
		menuTileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
		menuTileSave.addActionListener(menuActionListener);
	//	menuFile.add(menuTileSave);
		// menuFileSavePNG
		menuFileSavePNG = new JMenuItem("保存为PNG", KeyEvent.VK_P);
		menuFileSavePNG.setActionCommand("SAVEPNG");
		// menuFileSavePNG.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
		// InputEvent.CTRL_MASK));
		menuFileSavePNG.addActionListener(menuActionListener);
		menuFile.add(menuFileSavePNG);

		// menuEdit
		menuEdit = new JMenu("编辑");
		menuEdit.setMnemonic(KeyEvent.VK_E);
		menuBar.add(menuEdit);
		// menuEditResetOrigin
		menuEditResetOrigin = new JMenuItem("移动所有物体", KeyEvent.VK_M);
		menuEditResetOrigin.setActionCommand("RESETORIGIN");
		// menuEditResetOrigin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
		// InputEvent.CTRL_MASK));
		menuEditResetOrigin.addActionListener(menuActionListener);
		menuEdit.add(menuEditResetOrigin);
		// menuEditMapProp
		menuEditMapProp = new JMenuItem("设置地图属性", KeyEvent.VK_P);
		menuEditMapProp.setActionCommand("MAPPROP");
		// menuEditMapProp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
		// InputEvent.CTRL_MASK));
		menuEditMapProp.addActionListener(menuActionListener);
		menuEdit.add(menuEditMapProp);
		// menuEditTileProp
		menuEditTileProp = new JMenuItem("设置基本属性", KeyEvent.VK_T);
		menuEditTileProp.setActionCommand("TILEPROP");
		menuEditTileProp.addActionListener(menuActionListener);
		menuEdit.add(menuEditTileProp);
		// menuEditFlip
		menuEditFlip = new JMenuItem("水平翻转", KeyEvent.VK_F);
		menuEditFlip.setActionCommand("FLIP");
		menuEditFlip.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		menuEditFlip.addActionListener(menuActionListener);
		menuEdit.add(menuEditFlip);

		// menuManage
		menuManage = new JMenu("管理");
		menuManage.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menuManage);
		// menuManageEvents
		menuManageEvents = new JMenuItem("事件管理", KeyEvent.VK_E);
		menuManageEvents.setActionCommand("MANAGEEVENTS");
		menuManageEvents.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		menuManageEvents.addActionListener(menuActionListener);
		menuManage.add(menuManageEvents);
		// menuManageEARs
		menuManageEARs = new JMenuItem("敌人动画管理", KeyEvent.VK_R);
		menuManageEARs.setActionCommand("MANAGEEARS");
		menuManageEARs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		menuManageEARs.addActionListener(menuActionListener);
		menuManage.add(menuManageEARs);
		// menuManageTest
		menuManageTest = new JMenuItem("测试", KeyEvent.VK_T);
		menuManageTest.setActionCommand("MANAGETEST");
		menuManageTest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
		menuManageTest.addActionListener(menuActionListener);
		menuManage.add(menuManageTest);
		
		JMenu menuSkins = SampleMenuFactory.getSkinMenu();
		menuSkins.setText("皮肤");
		menuBar.add(menuSkins);
	}

	private void initToolBar() {
		// toolbar
		toolBar = new JToolBar();
		toolBar.setPreferredSize(new Dimension(100, XUtil.getDefPropInt("ToolBarHeight")));
		toolBar.setFloatable(false);
		// toolFlipHorizontal
		toolFlip = new JButton("水平翻转");
		toolFlip.setActionCommand("FLIP");
		toolFlip.addActionListener(menuActionListener);
		toolBar.add(toolFlip);
	}

	public HeadResManager getHeadResManager() {
		return headResManager;
	}
	
	public TIManager getTIManager() {
		return (TIManager) resManagers[RES_TILE];
	}
	
	public TilePanel getTilePanel() {
		return (TilePanel)panels[LAYER_TILE];
	}
	
	public LayerManager getLayerManager() {
		return layerManager;
	}

	public MapInfo getMapInfo() {
		return mapInfo;
	}

	public MouseInfo getMouseInfo() {
		return mouseInfo;
	}
	
	public TileInfo getBaseTileInfo() {
		return tileInfo;
	}

	public Object[] getResManagers() {
		return resManagers;
	}

	public ManagerPanel[] getPanels() {
		return panels;
	}
	
	public int getTabIndex() {
		return tabIndex;
	}

	public String getEnemyName(int aID) {
		String name = "无";
		Sprite[] sprites = ((EnemyManager) (panels[LAYER_ENEMY].getManager())).getSprites();
		for (int i = 0; i < sprites.length; ++i) {
			if (sprites[i].getID() == aID) {
				name = sprites[i].getName();
			}
		}
		return "敌人【" + XUtil.getNumberString(aID, 3) + "：" + name + "】";

	}

	// private void unitTreeMouseClicked(MouseEvent e) {
	// if (e.getButton() == XUtil.LEFT_BUTTON && e.getClickCount() >= 2) {
	// unitTreeDBClicked();
	// }
	// }

	// private void unitTreeDBClicked() {
	// PainterTree unitTree = trees[LAYER_Enemy];
	// if (unitTree.getModel().getChildCount(unitTree.getModel().getRoot()) <=
	// 0) {
	// // setURDB(null);
	// }
	// if (mouseInfo.getPainter() != null) {
	// if (mouseInfo.getPainter()instanceof SIPainter) {
	// // setURDB( ( (SIPainter) (mouseInfo.getPainter())).getSI());
	// }
	// }
	// }

	// private void setURDB(SingleImage si) {
	// URManager urManager = (URManager) (resManagers[RES_UR]);
	// SIManager siManager = (SIManager) (resManagers[RES_Enemy]);
	//		
	//		
	// URDBSetter setter = new URDBSetter(this, urManager, siManager.getSIs());
	// if(si != null) {
	// setter.setSelectedUR(si.getID());
	// }
	// else {
	// setter.setSelectedUR(null);
	// }
	// setter.show();
	// if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
	// refreshUR();
	// }
	// }

	// private void refreshUR() {
	// URManager urManager = (URManager) (resManagers[RES_UR]);
	// PainterTree unitTree = trees[LAYER_UNIT];
	// UnitPanel unitPanel = (UnitPanel) (panels[LAYER_UNIT]);
	//
	// mouseInfo.resetAll();
	// unitTree.refresh(null, URPainter.getPainters(urManager.getURs()));
	// ( (UnitManager) (unitPanel.getManager())).refresh(urManager);
	// }

	private class MenuActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String menuName = (e.getActionCommand()).trim().toUpperCase();
			if (menuName.equals("NEWMAP")) {
				newMap();
			}
			else if (menuName.equals("LOADMAP")) {
				loadMap();
			}
			else if (menuName.equals("SAVEMAP")) {
				saveMap();
			}
			else if(menuName.equals("SAVETILE")) {
				saveTile();
			}
			else if (menuName.equals("SAVEPNG")) {
				savePNG();
			}
			else if (menuName.equals("RESETORIGIN")) {
				resetOrigin();
			}
			else if (menuName.equals("MAPPROP")) {
				setMapProp();
			}
			else if(menuName.equals("TILEPROP")) {
				setTileBaseProp();
			}
			else if (menuName.equals("FLIP")) {
				flip();
			}
			else if (menuName.equals("MANAGEEVENTS")) {
				manageEvents();
			}
			else if (menuName.equals("MANAGEEARS")) {
				// manageEARs();
			}
			else if (menuName.equals("MANAGETEST")) {
				test();
			}
		}
	}

	private void treeTabChanged() {
		int index = treeTab.getSelectedIndex();
		if (panelTab.getSelectedIndex() != index) {
			panelTab.setSelectedIndex(index);
		}
		tabChanged();
	}

	private void panelTabChanged() {
		int index = panelTab.getSelectedIndex();
		if (treeTab.getSelectedIndex() != index) {
			treeTab.setSelectedIndex(index);
		}
		tabChanged();
	}

	private void tabChanged() {
		mouseInfo.resetAll();
		int index = panelTab.getSelectedIndex();
		ScrollablePanel pn = (ScrollablePanel) (panels[index]);
		if (tabIndex != index) {
			ScrollablePanel po = (ScrollablePanel) (panels[tabIndex]);
			pn.setScale(po.getScale());
			pn.resetSize(mapInfo.getWidth(), mapInfo.getHeight());
			pn.scrollRectToVisible(po.getScrollPane().getViewport().getViewRect());
			tabIndex = index;
			
			tileEdit.setTI(null);
			tileEdit.setIconsEnable(index == LAYER_TILE);
		}
		if(index < LAYER_MAX_LAYER)
			layerManager.setSelectLayer(layerManager.selectIndex);
		else if(index == LAYER_TILE)
			mapInfo.setDrawInfo(mapInfo.getRealLeft(), mapInfo.getRealTop(), tileEdit.pixW, tileEdit.pixH);
		else
			mapInfo.setDrawInfo(mapInfo.getRealLeft(), mapInfo.getRealTop(), mapInfo.getRealWidth(), mapInfo.getRealHeight());
		infoPanel.setScrollablePanel(pn);
		infoPanel.setManager(panels[index].getManager());
	}

	private void resetOrigin() {
		ResetOriginDialog n = new ResetOriginDialog(this);
		n.show();
		if (n.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			for (int i = 0; i < panels.length; ++i) {
				panels[i].getManager().resetOrigin(n.getOffsetX(), n.getOffsetY());
				panels[i].getBackPanel().repaint();
			}
		}
	}

	private void flip() {
		mouseInfo.setFlip(!mouseInfo.isFlip());
		Manager manager = panels[panelTab.getSelectedIndex()].getManager();
		if (manager instanceof SpriteManager) {
			((SpriteManager) manager).flip();
		}
	}

	private void manageEvents() {
		EventDBSetter setter = new EventDBSetter();
		setter.show();
	}
	
	private void changeGrid(){
		GridManager manager = (GridManager)(panels[LAYER_GRID].getManager());
		manager.InitGrid(mapInfo);
	}
	
	private void changeTile() {
		TileManager manager = (TileManager)(panels[LAYER_TILE].getManager());
		TileLayerManager tlm = new TileLayerManager(mapInfo.getTileHeight(), mapInfo.getTileWidth());
		manager.setTileLayerManager(tlm);
		tileEdit.setTileLayerManager(tlm);
		tileEdit.setPixWH(mapInfo.getTileHeight(), mapInfo.getTileWidth());
		if(tabIndex == LAYER_TILE) {
			tileEdit.setIconsEnable(true);
		}
	}
	
	public void changePainter() {
		SIPainter painter = (SIPainter) mouseInfo.getPainter();
		int info = mouseInfo.getInfo();
		if(info == MouseInfo.SELECT_TILE) {
			tileEdit.setTI(getTIManager().getTI(painter.getSI().getID()));
		}
	}
	
	public void repaintPanel() {
		int index = panelTab.getSelectedIndex();
		ScrollablePanel pn = (ScrollablePanel) (panels[index]);
		pn.repaint();
	}
	
	public void updatePanelSize() {
		int index = panelTab.getSelectedIndex();
		((ScrollablePanel)(panels[index])).resetSize(mapInfo.getWidth(), mapInfo.getHeight());
	}
	
	public void updatePanelDrawSize(int l, int t, int w, int h) {
		mapInfo.setDrawInfo(l, t, w, h);
	}

	private void newMap() {
		MapPropDialog setter = new MapPropDialog(this);
		setter.setTitle("新建地图");
		setter.show();
		if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			setter.updateMapInfo(mapInfo);
			updatePanelSize();
			events = null;
			switchManager.reset();
			// refreshBR();
			// refreshUR();
			setMapName(mapInfo.getName());
			updatePanelDrawSize(mapInfo.getRealLeft(), mapInfo.getRealTop(), mapInfo.getRealWidth(), mapInfo.getRealHeight());
			
			changeGrid();
			changeTile();
			layerManager.reset();
		}
	}
	
	private void setTileBaseProp() {
		TilePropDialog setter = new TilePropDialog(this);
		setter.setTitle("设置图块基本属性");
		setter.show();
		if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			ArrayList<TileProps> props = setter.getTps();
			tileInfo.setProps(props);
			try {
				tileInfo.writeIniFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setMapName(String name) {
		setTitle("地图编辑器    -    " + name);
	}

	private void setMapProp() {
		MapPropDialog setter = new MapPropDialog(this);
		setter.setTitle("设置地图属性");
		setter.setMapInfo(mapInfo);
		setter.show();
		if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			setter.updateMapInfo(mapInfo);
			updatePanelSize();
			// refreshBR();
			// refreshUR();
			setMapName(mapInfo.getName());
			changeGrid();
			
			if(mapInfo.getTileHeight()>0 && mapInfo.getTileWidth()>0) {
				if(!tileEdit.isInitTileLayers())
					changeTile();
				tileEdit.setTileWH(mapInfo.getTileHeight(), mapInfo.getTileWidth());
				mapInfo.setDrawInfo(mapInfo.getRealLeft(), mapInfo.getRealTop(), tileEdit.pixW,  tileEdit.pixH);
			}
			else
				mapInfo.setDrawInfo(mapInfo.getRealLeft(), mapInfo.getRealTop(), mapInfo.getRealWidth(), mapInfo.getRealHeight());
		}
	}

	private void dealBeforeShowProgress() {
		// setEnabled(false);
		progress.setTitle("标题");
		progress.setInfo("信息");
		progress.setValue(0);
		progress.show();
	}

	private void dealAfterShowProgress() {
		try {
			synchronized (this) {
				this.wait(XUtil.getDefPropInt("progressWaitTime"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		progress.hide();
		panelTab.repaint();
		SwingUtilities.invokeLater(new Runnable() {
			 public void run() {
				 validate();
			 }
		});
		setEnabled(true);
		this.requestFocus();
	}

	private void saveMap() {
		String mapName = "";
		if(mapInfo.isInit) {
			Object tmp = JOptionPane.showInputDialog(this, "输入地图名称：", "保存",
			        JOptionPane.INFORMATION_MESSAGE, new ImageIcon(), null, mapInfo.getName());
			// String mapName = mapInfo.getName();
			if (tmp == null) { return; }
			mapName = tmp.toString();
			if (mapName == null) { return; }
			mapName = mapName.trim();
			if (mapName.equals("")) { return; }
		}

		SaveMapThread t = new SaveMapThread(mapName);
		t.start();
	}
	
	private void saveTile() {
		SaveTileThread t = new SaveTileThread();
		t.start();
	}

	private void savePNG() {
		String mapName = mapInfo.getName();
		if (mapName == null) { return; }
		mapName = mapName.trim();
		if (mapName.equals("")) { return; }

		SavePNGThread t = new SavePNGThread(mapName);
		t.start();
	}
	
	public void savePNG(int layer) {
		String mapName = mapInfo.getName();
		if (mapName == null) { return; }
		mapName = mapName.trim();
		if (mapName.equals("")) { return; }

		SavePNGThread t = new SavePNGThread(mapName, layer);
		t.start();
	}

	private void loadMap() {
		String mapName = JOptionPane.showInputDialog(this, "输入地图名称：", "加载",
		        JOptionPane.INFORMATION_MESSAGE);
		if (mapName == null) { return; }
		mapName = mapName.trim();
		if (mapName.equals("")) { return; }

		LoadMapThread t = new LoadMapThread(mapName);
		t.start();
	}

	private class SaveMapThread extends Thread {

		String mapName;

		public SaveMapThread(String mapName) {
			this.mapName = mapName;
		}

		public void run() {
			dealBeforeShowProgress();
			progress.setTitle("保存(鼠标别动)");
			progress.setValue(0);

			try {
				saveMap(mapName);
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(progress, "保存当前地图出错" + "\n" + e, "保存出错",
				        JOptionPane.ERROR_MESSAGE);
			}

			progress.setInfo("保存成功");
			progress.setValue(100);
			dealAfterShowProgress();
		}
	}
	
	private class SaveTileThread extends Thread {
		public void run() {
			dealBeforeShowProgress();
			progress.setTitle("保存(鼠标别动)");
			progress.setValue(0);

			try {
				saveTiles();
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(progress, "保存Tile出错" + "\n" + e, "保存出错",
				        JOptionPane.ERROR_MESSAGE);
			}

			progress.setInfo("保存成功");
			progress.setValue(100);
			dealAfterShowProgress();
		}
	}

	private class LoadMapThread extends Thread {

		String mapName;

		public LoadMapThread(String mapName) {
			this.mapName = mapName;
		}

		public void run() {
			dealBeforeShowProgress();
			progress.setTitle("加载");
			progress.setValue(0);

			try {
				loadMap(mapName);
			}
			catch (Exception e) {
				System.out.println( "加载地图  " + mapName + "  出错" + "\n" + e);
//				JOptionPane.showMessageDialog(progress, "加载地图  " + mapName + "  出错" + "\n" + e,
//				        "加载出错", JOptionPane.ERROR_MESSAGE);
			}

			progress.setInfo("加载成功");
			progress.setValue(100);
			dealAfterShowProgress();
		}
	}
	
	private void saveTiles() throws Exception {
		TIManager tim = (TIManager)resManagers[RES_TILE];
		tim.save();
		tim.saveLuaAll();
	}

	private void saveMap(String name) throws Exception {
		progress.setInfo("保存图块信息");
		saveTiles();
		progress.setInfo("保存矩形信息");
		saveRects();
		progress.setValue(10);
		
		if(mapInfo.isInit) {
			// 保存全局数据库
			progress.setInfo("保存敌人资源");
			//headResManager.saveMobile();
			layerManager.save(name);
			progress.setValue(20);
	
			for (int i = 0; i < panels.length; ++i) {
				if (i < LAYER_NAMES.length) {
					progress.setInfo("保存" + LAYER_NAMES[i]);
				}
				else {
					progress.setInfo("保存未知" + i);
				}
				String fileName = name;
				if (i < LAYER_SAVE_NAMES.length) {
					fileName += "_" + LAYER_SAVE_NAMES[i];
				}
				else {
					fileName += "_" + "unknown" + i;
				}
		//		File ft = new File(XUtil.getDefPropStr("SavePath") + "\\" + fileName + "_tmp.dat");
				File f = new File(XUtil.getDefPropStr("SavePath") + "\\" + fileName + ".dat");
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				        new FileOutputStream(f)));
	
				if(i == LAYER_TILE) {
					tileEdit.save(out);
				}
				else {
					if (i == LAYER_FLOOR) {
						mapInfo.save(out);
						
						System.out.println("save data");
						for(int j=0; j<layerManager.layers.size(); ++j) {
							File fm = new File(XUtil.getDefPropStr("SaveMobilePath"));
							if(!fm.exists()) fm = new File(".\\savem");
							fm = new File(fm.getAbsoluteFile() + "\\" + fileName +"_"+layerManager.layers.get(j).layer + ".dat");
							DataOutputStream outm = new DataOutputStream(new BufferedOutputStream(
							        new FileOutputStream(fm)));
							
							System.out.println("name:"+layerManager.layers.get(j).name);
							System.out.println("layer:"+layerManager.layers.get(j).layer);
							
							layerManager.saveMobile(outm, layerManager.layers.get(j).layer);						
							panels[i].saveMobile(outm,layerManager.layers.size() - layerManager.layers.get(j).layer - 1);
							
							outm.flush();
							outm.close();
						}
					}
					panels[i].save(out, resManagers);
				}
				
				out.flush();
				out.close();
		//		XUtil.copyFile(ft.getAbsolutePath(), f.getAbsolutePath());
				progress.setValue((int) (20 + 1.0 * (50 - 20) * (i + 1) / panels.length));
			}
			
			progress.setValue(60);
			progress.setInfo("保存Lua");
			saveLua(name);
			saveJson(name);
			
			progress.setValue(80);
			progress.setInfo("保存事件");
			//saveEvents(name);
	
			// progress.setValue(90);
			// progress.setInfo("保存作战单位资源");
			// URManager urManager = (URManager) (resManagers[RES_UR]);
			// urManager.save(name);
			// urManager.saveMobile(name);
		}

		progress.setValue(100);
		setMapName(mapInfo.getName());
	}
	
	private void saveRects() throws Exception {
		((RectPainterTree)(trees[LAYER_RECT])).save();
	}
	
	private void loadRects() throws Exception {
		((RectPainterTree)(trees[LAYER_RECT])).load();
	}
	private void saveJson(String name)throws Exception
	{
		String releasePath = XUtil.getDefPropStr("JsonReleasePath");
		saveJson(name,releasePath);
	}
	private void saveJson(String name,String savePath)throws Exception 
	{
		String scriptPath = XUtil.getDefPropStr("ScriptPath");
		if(!(new File(scriptPath).exists())) {
			scriptPath = ".";
		}
		File lvFile = new File(scriptPath + "\\" + name + ".json");
		DataOutputStream out = new DataOutputStream(new FileOutputStream(lvFile));
		
		StringBuffer buffer = new StringBuffer();	
		buffer.append("{\r\n");
		buffer.append("\t"+"\"map_data\":[\r\n");
		int num = 1;
		for(int i=layerManager.layers.size()-1 ; i >= 0; --i) {
			int layer = layerManager.layers.get(i).id;
		//	System.out.println("save lua, layer: "+layer);
			buffer.append(XUtil.getTabs(1));
			buffer.append("\40{\r\n");
			
			//buffer.append(XUtil.getTabs(2));
			//buffer.append("\"mapName\":"+"\""+name+"_map_"+i+".dat"+"\",\r\n");
			buffer.append(XUtil.getTabs(2));
			buffer.append("\"MapName\":"+"\""+name+"_map_"+layerManager.layers.get(i).layer+".dat"+"\",\r\n");
			buffer.append(XUtil.getTabs(2));
			buffer.append("\"LayerName\":"+"\""+layerManager.layers.get(i).name+"\",\r\n");
			
			EnemyManager em = (EnemyManager)(panels[LAYER_ENEMY].getManager());
			em.saveJson(buffer, layer, 2);
//			需要时再加			
//			LineManager lm = (LineManager)(panels[LAYER_LINE].getManager());
//			lm.saveLua(buffer, layer, 2);
//			
//			RectManager sm = (RectManager)panels[LAYER_RECT].getManager();
//			sm.saveLua(buffer, layer, 2);
			
			buffer.append(XUtil.getTabs(1));
			if(i>0)
				buffer.append("\40},\r\n");
			else
				buffer.append("\40}\r\n");
		}		
		buffer.append("\t]\r\n");
		buffer.append("}\r\n");
		
		byte[] bufferBytes = buffer.toString().getBytes("UTF-8");		
		out.write(bufferBytes);
		//buffer.delete(0, buffer.length());	
		buffer.setLength(0);
		
		out.flush();
		out.close();	
		
		//String releasePath = XUtil.getDefPropStr("JsonReleasePath");
		File releaseF = new File(savePath + "\\" + name + ".json");
		if(new File(savePath).exists())
			XUtil.copyFile(lvFile.getAbsolutePath(), releaseF.getAbsolutePath());		
	
	}
	private void saveLua(String name) throws Exception {
		String scriptPath = XUtil.getDefPropStr("ScriptPath");
		if(!(new File(scriptPath).exists())) {
			scriptPath = ".";
		}
		File lvFile = new File(scriptPath + "\\" + name + ".lua");
		DataOutputStream out = new DataOutputStream(new FileOutputStream(lvFile));
		
		StringBuffer buffer = new StringBuffer();
		EnemyManager emer = (EnemyManager)(panels[LAYER_ENEMY].getManager());
		emer.saveLuaScript(buffer);
		out.writeBytes(buffer.toString());
		buffer.delete(0, buffer.length());
		
		buffer.append("--Datas\r\n");
		buffer.append("datas={\r\n");
		int num = 1;
		for(int i=layerManager.layers.size()-1; i>=0; --i) {
			int layer = layerManager.layers.get(i).id;
		//	System.out.println("save lua, layer: "+layer);
			buffer.append(XUtil.getTabs(1));
			buffer.append("["+(num++)+"]={\r\n");
			
			buffer.append(XUtil.getTabs(2));
			buffer.append("mapName="+"\""+name+"_map_"+i+"\",\r\n");
			
			EnemyManager em = (EnemyManager)(panels[LAYER_ENEMY].getManager());
			em.saveLua(buffer, layer, 2);
			
			LineManager lm = (LineManager)(panels[LAYER_LINE].getManager());
			lm.saveLua(buffer, layer, 2);
			
			RectManager sm = (RectManager)panels[LAYER_RECT].getManager();
			sm.saveLua(buffer, layer, 2);
			
			buffer.append(XUtil.getTabs(1));
			buffer.append("},\r\n");
		}
		buffer.append("}\r\n");
		out.writeBytes(buffer.toString());
		buffer.delete(0, buffer.length());
		
		out.flush();
		out.close();
		
		//save road
		PointManager pm = (PointManager)panels[LAYER_POINT].getManager();
		pm.saveLua(name);
		
		//sava grid
		GridManager gm = (GridManager)panels[LAYER_GRID].getManager();
		gm.saveLua(name);
		
		//save tiles
		tileEdit.saveLua(name);
			
		String releasePath = XUtil.getDefPropStr("LuaReleasePath");
		File releaseF = new File(releasePath + "\\" + name + ".lua");
		if(new File(releasePath).exists())
			XUtil.copyFile(lvFile.getAbsolutePath(), releaseF.getAbsolutePath());
	}
	
	private void loadMap(String name) throws Exception {
		layerManager.load(name);
		
		for (int i = 0; i < panels.length; ++i) {
			if (i < LAYER_NAMES.length) {
				progress.setInfo("加载" + LAYER_NAMES[i]);
			}
			else {
				progress.setInfo("加载未知" + i);
			}
			String fileName = name;
			if (i < LAYER_SAVE_NAMES.length) {
				fileName += "_" + LAYER_SAVE_NAMES[i];
			}
			else {
				fileName += "_" + "unknown" + i;
			}
			File f = new File(XUtil.getDefPropStr("SavePath") + "\\" + fileName + ".dat");
			if (!f.exists()) continue;
			DataInputStream in = new DataInputStream(
			        new BufferedInputStream(new FileInputStream(f)));
			
			if(i == LAYER_TILE) {
				if(mapInfo.getTileHeight()>0 && mapInfo.getTileWidth()>0) {
					updatePanelSize();
					changeTile();
					tileEdit.load(in);
				}
				else {
					((TileManager)(panels[LAYER_TILE].getManager())).setTileLayerManager(null);
					tileEdit.clear();
				}
			}
			else {
				if (i == LAYER_FLOOR) {
					mapInfo.load(in);
					mapInfo.setName(name);
					changeGrid();
				}
				updatePanelSize();
				panels[i].load(in, resManagers);
			}
			
			progress.setValue((int) (80.0 * (i + 1) / panels.length));
			in.close();
		}

		progress.setValue(80);
		progress.setInfo("加载事件");
		loadEvents(name);
		progress.setValue(100);
		setMapName(mapInfo.getName());
	}

	public Event[] getEvents() {
		return events;
	}

	public SwitchManager getSwitchManager() {
		return switchManager;
	}

	public void setEvents(Event[] events) {
		this.events = Event.copyArray(events);
	}

	private void loadEvents(String name) throws Exception {
		switchManager.load(name);

		File f = new File(XUtil.getDefPropStr("SavePath") + "\\" + name + "_event.dat");
		if (!f.exists()) return;

		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));

		events = Event.createArrayViaFile(in);
		in.close();
	}

	private void saveEvents(String name) throws Exception {
		switchManager.save(name);

		XUtil.copyFile(XUtil.getDefPropStr("SavePath") + "\\" + name + "_event.dat",
		        XUtil.getDefPropStr("SavePath") + "\\" + name + "_event_bak.dat");
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
		        new File(XUtil.getDefPropStr("SavePath") + "\\" + name + "_event.dat"))));

		DataOutputStream outm = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
		        new File(XUtil.getDefPropStr("SaveMobilePath") + "\\" + name + "_event.dat"))));

		SL.saveArray(events, out);
		SL.saveArrayMobile(events, outm);
		out.flush();
		outm.flush();
		out.close();
		outm.close();
	}

	private class SavePNGThread extends Thread {

		String mapName;
		int layer;

		public SavePNGThread(String mapName) {
			this.mapName = mapName;
			this.layer = -1;
		}

		public SavePNGThread(String mapName, int layer) {
			this.mapName = mapName;
			this.layer = layer;
		}

		public void run() {
			dealBeforeShowProgress();
			progress.setTitle("保存为PNG");

			try {
				savePNG(mapName, layer);
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(progress, "保存PNG出错" + "\n" + e, "保存出错",
				        JOptionPane.ERROR_MESSAGE);
			}

			progress.setInfo("保存成功");
			progress.setValue(100);
			dealAfterShowProgress();
		}
	}

	private void savePNG(String name, int layer) throws Exception {
		progress.setInfo("创建空图");
		int l = mapInfo.getRealLeft();
		int t = mapInfo.getRealTop();
		int w = mapInfo.getRealWidth();
		int h = mapInfo.getRealHeight();
		if(layer >= 0) {
			l = layerManager.getLayerOfLayer(layer).left;
			t = layerManager.getLayerOfLayer(layer).top;
			w = layerManager.getLayerOfLayer(layer).w;
			h = layerManager.getLayerOfLayer(layer).h;
		}
			
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.translate(-l, -t);
		progress.setValue(10);

		for (int i = 0; i < LAYER_ENEMY; ++i) {
			if (i < LAYER_NAMES.length) {
				progress.setInfo("绘制" + LAYER_NAMES[i]);
			}
			if (i < panels.length) {
				if(layer >= 0)
					panels[i].getManager().paintStatic(g, layer);
				else
					panels[i].getManager().paintStatic(g);
				progress.setValue((int) (10 + 1.0 * (50 - 10) * (i + 1) / (LAYER_ENEMY + 1)));
			}
		}

		progress.setInfo("生成PNG文件");
		img.flush();
		File f = new File(".\\save\\" + name + "_" + layerManager.getLayerName(layer) + ".png");
		ImageIO.write(img, "png", f);
		progress.setValue(100);
	}

	private void test() {
	// EnemyChooser chooser = new EnemyChooser(this);
	// chooser.show();
	// if(chooser.getCloseType() == OKCancelDialog.OK_PERFORMED) {
	// System.out.println(chooser.getSelectedEnemyID());
	// }
	// CameraPosSetter setter = new CameraPosSetter(this);
	// setter.setPos(cameraPos);
	// setter.show();
	// if(setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
	// cameraPos = setter.getPos().copyIntPair();
	// }
		
		Runtime runt=Runtime.getRuntime();
		Process p=null;
		try 
		  {
			String MapViewerPath = XUtil.getDefPropStr("MapExportPath");
			String MatrixHome = XUtil.getDefPropStr("MatrixHome");
			String MatrixRuntime = XUtil.getDefPropStr("MatrixRuntime"); 
			
			p=runt.exec("cmd.exe /k start " +MatrixRuntime +" "+mapInfo.getName()+" "+MapViewerPath,null,new File(MatrixHome));
		  }   
		catch (Exception ex) 
		{
		  ex.printStackTrace();
		 }
	}
}

class EventDBSetter extends OKCancelDialog {

	private EventList eventList;

	public EventDBSetter() {
		super(MainFrame.self);
		setTitle("事件管理");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Container cp = this.getContentPane();
		eventList = new EventList(this, MainFrame.self.getEvents());
		cp.add(new JScrollPane(eventList), BorderLayout.CENTER);
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(MainFrame.self);
	}

	protected void okPerformed() {
		MainFrame.self.setEvents(eventList.getEvents());
		closeType = OK_PERFORMED;
		dispose();
	}

	protected void cancelPerformed() {
		dispose();
	}
}

class ResetOriginDialog extends OKCancelDialog {

	private NumberSpinner xSpinner;
	private NumberSpinner ySpinner;

	public ResetOriginDialog(JFrame owner) {
		super(owner);
		init();
	}

	private void init() {
		setTitle("设置移动所有物体的偏移量");
		xSpinner = new NumberSpinner();
		xSpinner.setIntValue(0);
		ySpinner = new NumberSpinner();
		ySpinner.setIntValue(0);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0;
		c.insets = new Insets(5, 5, 5, 5);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		centerPanel.add(new JLabel("水平向右移动的像素（向左为负数）："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(xSpinner, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		centerPanel.add(new JLabel("垂直向下移动的像素（向上为负数）："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(ySpinner, c);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(centerPanel, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public int getOffsetX() {
		return xSpinner.getIntValue();
	}

	public int getOffsetY() {
		return ySpinner.getIntValue();
	}

	public void okPerformed() {
		closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}
}


class CreateShadowData {
	public static void main(String[] args) {
		try {
			DefaultTheme theme = new DefaultTheme();
			MetalLookAndFeel.setCurrentTheme(theme);
//			DEF_FONT = theme.getDefaultFont();
		}
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e, "初始化错误", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		try {
			new CreateShadowData();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public CreateShadowData() throws Exception {
		JFileChooser fc = new JFileChooser(new File(XUtil.getDefPropStr("SavePath"))) {
			public boolean accept(File f) {
				return f.isFile() && FileExtFilter.getExtension(f).equalsIgnoreCase("png");
			}
		};		

		fc.setMultiSelectionEnabled(true);
		int r = fc.showOpenDialog(null);
		if(r != JFileChooser.APPROVE_OPTION) return;
		
		File[] files = fc.getSelectedFiles();
		if(files == null) return;
		
		for (int i = 0; i < files.length; ++i) {
			File f = files[i];
			if(f == null) continue;
			
			BufferedImage img = ImageIO.read(f);

			Raster raster = img.getData();
			ColorModel model = img.getColorModel();			

			
			int cellW = 4;
			int cellH = 4;
			
			int[] data = new int[((img.getWidth() + (cellW - 1)) / cellW) * ((img.getHeight() + (cellH - 1)) / cellH) / 32];
			int dataIdx1 = 0;
			int dataIdx2 = 0;
			int v = 0;
			int imgW = img.getWidth();
			int imgH = img.getHeight();
			
			for (int iy = 0; iy < imgH; iy = iy + cellH) {
				for (int ix = 0; ix < imgW; ix = ix + cellW) {
					boolean black = false;
					for(int y = iy; y < Math.min(imgH, iy + cellH); ++y) {
						for(int x = ix; x < Math.min(imgW, ix + cellW); ++x) {
							Object colorData = raster.getDataElements(x, y, null);
							Color color = new Color(model.getRGB(colorData), true);
							if((color.getRGB() & 0x00FFFFFF) == 0) {
								black = true;
								break;
							}
						}
						if(black) break;
					}
					if(black) {
						v |= (1 << dataIdx2);
					}
					++dataIdx2;
					if(dataIdx2 >= 32) {
						dataIdx2 = 0;
						data[dataIdx1++] = v;
						v = 0;
					}
				}
			}
			
			File fm = new File(XUtil.getDefPropStr("SaveMobilePath"));
			if(!fm.exists()) fm = new File(".\\savem");
			fm = new File(fm.getAbsoluteFile() + "\\" + FileExtFilter.getName(f) + "_shadow.dat");
			DataOutputStream outm = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fm)));
			SL.writeArrayMobile(data, outm);
			outm.flush();
			outm.close();
			
//			int xx = 8;
//			int yy = 2409;
//			int vv = (yy / cellH) * ((img.getWidth() + (cellW - 1)) / cellW) + (xx / cellW);
//			System.out.println(data[vv / 32] & (1 << (vv % 32)));
			
			BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(),BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = img2.createGraphics();
			g2.setColor(Color.white);
			g2.fillRect(0, 0, img2.getWidth(), img2.getHeight());
			g2.setColor(Color.black);
			int x2 = 0;
			int y2 = 0;
			for(int i2 = 0; i2 < data.length; ++i2) {
				for(int j2 = 0; j2 < 32; ++j2) {					
					if((data[i2] & (1 << j2)) != 0) {
						g2.fillRect(x2, y2, cellW, cellH);
					}
					x2 = x2 + cellW;
					if(x2 >= img.getWidth()) {
						x2 = 0;
						y2 = y2 + cellH;
					}
				}
			}
			
			File f2 = new File(fm.getParent() + "\\" + FileExtFilter.getName(f) + "_real.png");
			ImageIO.write(img2, "png", f2);
			
		}
		
		JOptionPane.showMessageDialog(null, "搞定");
		
	}
}









