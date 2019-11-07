
package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalLookAndFeel;

import test.check.SampleMenuFactory;

import com.sun.istack.internal.Nullable;

public class MainFrame extends JFrame{
	

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
				animList.setAnims(anims);
				setMapName(mapName);
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(progress, "加载动画组  " + mapName + "  出错" + "\n" + e,
				        "加载出错", JOptionPane.ERROR_MESSAGE);
			}

			progress.setInfo("加载成功");
			progress.setValue(100);
			dealAfterShowProgress();
		}
	}
	
	private void openDir(String dirName) {
		if(dirName != "") {
			try {
				if(MainFrame.self.getSIManager().add(dirName))
					MainFrame.self.UpdatePic();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class MenuActionListener implements ActionListener {
		DirChooser dirChooser = new DirChooser();

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
			else if(menuName.equals("OPENPIC")) {
				
			}
			else if(menuName.equals("OPENMENU")) {
				dirChooser.chooseDir();
				String dirName = dirChooser.getDir();
				openDir(dirName);
			}
			else if (menuName.equals("FLIP")) {
				flip();
			}
		}
	}

	private class SaveMapThread extends Thread {

		String mapName;

		public SaveMapThread(String mapName) {
			this.mapName = mapName;
		}

		public void run() {
			dealBeforeShowProgress();
			progress.setTitle("保存");
			progress.setValue(0);

			try {
				saveMap(mapName);
				setMapName(mapName);
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(progress, "保存当前动画组出错" + "\n" + e, "保存出错",
				        JOptionPane.ERROR_MESSAGE);
			}

			progress.setInfo("保存成功");
			progress.setValue(100);
			dealAfterShowProgress();
		}
	}

	public static Font DEF_FONT = new Font("Dialog", Font.PLAIN, 12);

	public static MainFrame self;

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
//		w.setVisible(true);
		
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
	
	public final static int version = 101;

	public final static int LAYER_COUNT = 2;
	public final static int LAYER_IMAGE = 0;
	public final static int LAYER_RECT = 1;
	public final static int LAYER_CLIP = 2;
	public final static String[] LAYER_NAMES = { "图片", "矩形", "切块" };
	
	public int verLoad;
	private ProgressDialog progress;
	private SIManager siManager;
	private MouseInfo mouseInfo;
	// main component
	public PainterTree[] trees;
	public JTabbedPane treeTab;
	private int tabIndex;

	public AnimPanel animPanel;
	public AnimList animList;
	private InfoPanel infoPanel;
	// menus
	private JMenuBar menuBar;
	// menu file
	private JMenu menuFile;
	private JMenuItem menuFileNew;
	private JMenuItem menuFileLoad;
	private JMenuItem menuFileSave;
	// menu pic
	private JMenu menuPic;
	private JMenuItem menuPicOpenFile;
	private JMenuItem menuPicOpenMenu;
	// menu edit
	private JMenu menuEdit;

	private JMenuItem menuEditFlip;
	private JRadioButtonMenuItem jMenuShow;
	private JRadioButtonMenuItem jMenuShow1;
	private ButtonGroup buttonGroup;

	// toolbar
	private JToolBar toolBar;

	private JButton toolFlip;

	private MenuActionListener menuActionListener;

	private String name;
	private Animation[] anims;
	
	private SoundManager soundManager;
	private static String substance = "org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel";
	public static ArrayList clipArr;//切块数组集
	public static boolean isCollisionBox;
	public static int editType;//编辑类型 0:塞班|Kjava模式   1:android|iphone模式
	int[] picNameList; //图片ID LIST

	public MainFrame() {
		self = this;
		verLoad = version;
	//	setLAF(substance);
		try {
			setIconImage(XImage.readPNG(new File(".\\icon.png"), null));
		}
		catch (Exception e) {}
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(new Dimension(1280, 720));
		setLocationRelativeTo(null);
		
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
			//	System.out.println("窗口获得了焦点！");
				try {
					MainFrame.self.getSIManager().getFocus();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			public void windowLostFocus(WindowEvent e) {
			//	System.out.println("窗口失去了焦点！");
			}
		});

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
		animPanel.repaint();
		SwingUtilities.invokeLater(new Runnable() {
			 public void run() {
				 animList.updateUI();
				 validate();
			 }
		});
		setEnabled(true);
		this.requestFocus();
	}

	private void dealBeforeShowProgress() {
		// setEnabled(false);
		progress.setTitle("标题");
		progress.setInfo("信息");
		progress.setValue(0);
		progress.show();
	}

	private void flip() {
		mouseInfo.setFlip(!mouseInfo.isFlip());
		FrameManager manager = animPanel.getFrameManager();
		manager.flip();
	}

	public AnimList getAnimList() {
		return animList;
	}

	public AnimPanel getAnimPanel() {
		return animPanel;
	}

	private String getDefName() {
		return XUtil.getDefPropStr("AnimName");
	}

	public SIManager getSIManager() {
		return siManager;
	}
	
	public SoundManager getSoundManager() {
		return soundManager;
	}

	Container cp;
	private void init() throws Exception {		
		setMapName(getDefName());

		// 初始化进度条
		progress = new ProgressDialog(this);
		progress.setTitle("正在启动");
		progress.setInfo("Loading");
		progress.setValue(0);
		progress.show();

		// 加载资源
		initResManager();
		mouseInfo = new MouseInfo();
		menuActionListener = new MenuActionListener();

		// 加载各个控件
		progress.setInfo("初始化控件");
		initTree();
		initPanel();
		initList();
//		initInfo();
		initMenu();
		initToolBar();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, testJp, animPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(200);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane, animList);
		splitPane.setResizeWeight(1);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(1100);

		this.setJMenuBar(menuBar);
		cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(toolBar, BorderLayout.NORTH);
		cp.add(splitPane, BorderLayout.CENTER);
//		cp.add(infoPanel, BorderLayout.SOUTH);

		tabIndex = 0;
		treeTab.setSelectedIndex(tabIndex);
		tabChanged();
		
		MainFrame.clipArr =new ArrayList();
		FrameManager.REAL_WIDTH =XUtil.getDefPropInt("editWidth");
		FrameManager.REAL_HEIGHT =XUtil.getDefPropInt("editHeight");
		FrameManager.BASIC_WIDTH =FrameManager.REAL_WIDTH + FrameManager.REAL_LEFT * 2 + 500;
		FrameManager.BASIC_HEIGHT =FrameManager.REAL_HEIGHT + FrameManager.REAL_TOP * 2 + 200;
		
		FrameButtonPanel.SCALEX =(FrameButtonPanel.BUTTON_WIDTH * 1.0) / FrameManager.REAL_WIDTH;
		FrameButtonPanel.SCALEY =(FrameButtonPanel.BUTTON_HEIGHT * 1.0) / FrameManager.REAL_HEIGHT;
		AniEdit.drawOffSetX =FrameManager.REAL_LEFT +FrameManager.REAL_WIDTH/2;
		AniEdit.drawOffSetY =FrameManager.REAL_TOP +FrameManager.REAL_HEIGHT/2;
		//System.out.println("EEEE ="+AniEdit.drawOffSetX);

		LoadMapThread t = new LoadMapThread(getDefName());
		t.run();
		
		progress.setValue(100);
		progress.hide();
		show();
	}
	
	private void initInfo() {
		infoPanel = new InfoPanel();
		infoPanel.setScrollablePanel(animPanel.getFramePanel());
		infoPanel.setManager(animPanel.getFrameManager());
	}

	private void initList() {
		animList = new AnimList(animPanel);
	}

	private void initMenu() {
		// menuBar
		menuBar = new JMenuBar();
		// menuFile
		menuFile = new JMenu("文件");
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menuFile);
		// menuFileNew
		menuFileNew = new JMenuItem("新建", KeyEvent.VK_N);
		menuFileNew.setActionCommand("NEWMAP");
		menuFileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		menuFileNew.addActionListener(menuActionListener);
		menuFile.add(menuFileNew);
		// menuFileLoad
		menuFileLoad = new JMenuItem("加载", KeyEvent.VK_L);
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
		
		// menuPic
		menuPic = new JMenu("图片");
		menuPic.setMnemonic(KeyEvent.VK_P);
		menuBar.add(menuPic);
		// menuPicOpenFile
		menuPicOpenFile = new JMenuItem("打开图片", KeyEvent.VK_P);
		menuPicOpenFile.setActionCommand("OPENPIC");
		menuPicOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		menuPicOpenFile.addActionListener(menuActionListener);
	//	menuPic.add(menuPicOpenFile);
		// menuPicOpenFile
		menuPicOpenMenu = new JMenuItem("打开目录", KeyEvent.VK_M);
		menuPicOpenMenu.setActionCommand("OPENMENU");
		menuPicOpenMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
		menuPicOpenMenu.addActionListener(menuActionListener);
		menuPic.add(menuPicOpenMenu);

		// menuEdit
		menuEdit = new JMenu("编辑");
		menuEdit.setMnemonic(KeyEvent.VK_E);
		menuBar.add(menuEdit);
		
		buttonGroup = new ButtonGroup();
		editType =1;
		jMenuShow =new JRadioButtonMenuItem("Symbian|Kjava模式");
		jMenuShow.setSelected(false);
		jMenuShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(jMenuShow.isSelected()){
                	editType =0;
                }
                animPanel.aniedit.showjpanel.repaint();
            }
        });
		buttonGroup.add(jMenuShow);
		menuEdit.add(jMenuShow);
		
		jMenuShow1 =new JRadioButtonMenuItem("Android|Iphone模式");
		jMenuShow1.setSelected(true);
		jMenuShow1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(jMenuShow1.isSelected()){
                	editType =1;
                }
                animPanel.aniedit.showjpanel.repaint();
            }
        });
		buttonGroup.add(jMenuShow1);
		menuEdit.add(jMenuShow1);
		
		JMenu menuSkins = SampleMenuFactory.getSkinMenu();
		menuSkins.setText("皮肤");
		menuBar.add(menuSkins);
		
		// menuEditFlip
		/*menuEditFlip = new JMenuItem("水平翻转", KeyEvent.VK_F);
		menuEditFlip.setActionCommand("FLIP");
		menuEditFlip.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		menuEditFlip.addActionListener(menuActionListener);
		menuEdit.add(menuEditFlip);*/
	}

	private void initPanel() {
		animPanel = new AnimPanel(this, mouseInfo);
	}

	private void initResManager() throws Exception {

		progress.setInfo("读取图片");
		siManager = new SIManager();
		progress.setValue(50);
	//	progress.setInfo("加载声音");
		soundManager = new SoundManager();
		progress.setValue(70);
	}

	private void initToolBar() {
		// toolbar
		toolBar = new JToolBar();
		toolBar.setPreferredSize(new Dimension(100, XUtil.getDefPropInt("ToolBarHeight")));
		toolBar.setFloatable(false);
		// toolFlipHorizontal
		toolFlip = new JButton("F");
		toolFlip.setActionCommand("FLIP");
		toolFlip.addActionListener(menuActionListener);
		toolBar.add(toolFlip);
	}

	JPanel testJp;
	Clip showJp;
	
	private void initTree() {
		SingleImage[] testsi =siManager.getSIs();
		if(testsi !=null){
			picNameList =new int[testsi.length];
			for(int i=0;i<testsi.length;i++){
				picNameList[i] =testsi[i].getID();
			}
		}
		
		trees = new PainterTree[3];
		trees[0] = new PainterTree(LAYER_NAMES[0], SIPainterGroup.getGroups(siManager.getGroups()), SIPainter.getPainters(siManager.getSIs()), mouseInfo);
		trees[1] = new PainterTree(LAYER_NAMES[1], null, RectPainter.getPainters(), mouseInfo);
		
		SIGroup sicc =new SIGroup(PointSI.GROUP_ID, "逻辑点");
		SingleImage[] sis =new SingleImage[Frame.POINT_COLORS.length];
		for (int i = 0; i < Frame.POINT_COLORS.length; ++i) {
			sis[i] = new PointSI(i, Frame.POINT_NAMES[i], Frame.POINT_COLORS[i]);
		}
		trees[LAYER_CLIP] = new PainterTree(LAYER_NAMES[LAYER_CLIP], SIPainterGroup.getGroups(new SIGroup[]{sicc}), SIPainter.getPainters(sis), mouseInfo);
//		trees[MainFrame.LAYER_CLIP] = new PainterTree(LAYER_NAMES[LAYER_CLIP], null, null, mouseInfo);

		treeTab = new JTabbedPane();
		treeTab.setInputMap(JScrollPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new InputMap());
		for (int i = 0; i < trees.length; ++i) {
			treeTab.addTab(LAYER_NAMES[i], trees[i].getScrollPane());
		}
		treeTab.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
//				treeTab.clearSelection();
				for(int i=0;i<trees.length;i++){
					trees[i].clearSelection();
				}
				treeTabChanged();
			}
		});
//		showJp =new JPanel();
		//添加编辑区
		showJp =new Clip();
		showJp.setFocusable(true);
		
//		showJp.setName("编辑区");
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeTab);
        splitPane.setBottomComponent(showJp);
		
		testJp =new JPanel();
		testJp.setLayout(new BorderLayout());
		testJp.add(splitPane);
	}
	
	public void UpdatePic() {
		SingleImage[] testsi =siManager.getSIs();
		if(testsi !=null){
			picNameList =new int[testsi.length];
			for(int i=0;i<testsi.length;i++){
				picNameList[i] =testsi[i].getID();
			}
		}
		
		trees[0].refresh(SIPainterGroup.getGroups(siManager.getGroups()), SIPainter.getPainters(siManager.getSIs()));
	}

	private void treeTabChanged() {
		int index = treeTab.getSelectedIndex();
		if(index == 1)
			isCollisionBox = true;
		else
			isCollisionBox = false;
		animPanel.aniedit.showjpanel.repaint();
//		if (animPanel.getPanelTab().getSelectedIndex() != index) {
//			animPanel.getPanelTab().setSelectedIndex(index);
//		}
//		tabChanged();
	}

	public void panelTabChanged() {
		int index = animPanel.getPanelTab().getSelectedIndex();
		if (treeTab.getSelectedIndex() != index) {
			treeTab.setSelectedIndex(index);
		}
		tabChanged();
	}

	private void tabChanged() {
		mouseInfo.resetAll();
		int index = animPanel.getPanelTab().getSelectedIndex();

		ScrollablePanel pn = animPanel.getPanels()[index];
		if (tabIndex != index) {
			ScrollablePanel po = animPanel.getPanels()[tabIndex];
			pn.setScale(po.getScale());
			pn.scrollRectToVisible(po.getScrollPane().getViewport().getViewRect());
			tabIndex = index;
		}
		/*infoPanel.setScrollablePanel(pn);
		infoPanel.setManager(((ManagerPanel)(animPanel.getPanels()[index])).getManager());*/
	}

	private void loadMap() {
		Object tmp = JOptionPane.showInputDialog(this, "输入加载名称：", "加载",
		        JOptionPane.INFORMATION_MESSAGE, new ImageIcon(), null, getDefName());
		if (tmp == null) { return; }
		String mapName = tmp.toString();
		if (mapName == null) { return; }
		mapName = mapName.trim();
		if (mapName.equals("")) { return; }

		LoadMapThread t = new LoadMapThread(mapName);
		t.run();
	}

	private void creaNewNode(ArrayList al){
//		trees[MainFrame.LAYER_CLIP].
		trees[MainFrame.LAYER_CLIP].clearClipData();
		if(!al.isEmpty()){
			//把预切块加入tree中
			for(int i=0;i<al.size();i++){
				clipInfoSave cifs =(clipInfoSave)(al.get(i));
//				System.out.println("######"+cifs.groupType+","+cifs.sonType);
				if(cifs.clipArea !=null){
					Painter pt= trees[0].getSIPainterPainterById(cifs.groupType, cifs.sonType);
					for(int j=0;j<cifs.clipArea.length;j++){
						BufferedImage tempImg = new BufferedImage(cifs.clipArea[j][2], cifs.clipArea[j][3], BufferedImage.TYPE_INT_ARGB);
						Graphics g = tempImg.createGraphics();
						g.drawImage(((SIPainter)pt).getSI().image, 0, 0, cifs.clipArea[j][2], cifs.clipArea[j][3], cifs.clipArea[j][0], cifs.clipArea[j][1], cifs.clipArea[j][0]+cifs.clipArea[j][2], cifs.clipArea[j][1]+cifs.clipArea[j][3], null);
						tempImg.flush();
						
						SIGroup sicc =new SIGroup(-pt.getID(), pt.getName());
						trees[MainFrame.LAYER_CLIP].addNewNodeToTree(new SIPainterGroup(sicc), new ClipPicPanel(tempImg, cifs.clipArea[j][5], -pt.getID(), cifs.clipArea[j][0], cifs.clipArea[j][1],cifs.clipArea[j][2],cifs.clipArea[j][3]));
						trees[MainFrame.LAYER_CLIP].expandRow(0);
					}
				}
			}
		}
	}
	
	public clipInfoSave getClipArr(int groupId, int id) {
		for(int i=0; i<MainFrame.clipArr.size(); ++i) {
			clipInfoSave cifs = (clipInfoSave) MainFrame.clipArr.get(i);
			if(cifs.groupType == groupId && cifs.sonType == id){
				return cifs;
			}
		}
		return null;
	}
	
	public void delClipArr(int groupId, int id) {
		for(int i=0; i<MainFrame.clipArr.size(); ++i) {
			clipInfoSave cifs = (clipInfoSave) MainFrame.clipArr.get(i);
			if(cifs.groupType == groupId && cifs.sonType == id){
				cifs.clear();
				MainFrame.clipArr.remove(i);
				break;
			}
		}
	}
	
	private void loadClipArr(DataInputStream in)throws Exception{
		int arrl =in.readInt();
		//MainFrame.clipArr.clear();
	//	System.out.println("main load: "+arrl);
		if(arrl>0){
			for(int i=0;i<arrl;i++){
				clipInfoSave cifs =new clipInfoSave(-1, -1);
				cifs.load(in);
				//Print.printArray(cifs.clipArea, "切块信息");
		//		clipArr.add(cifs);
			}
		}
	//	System.out.println(arrl+", load切块信息长度 ="+clipArr.size());
	//	creaNewNode(clipArr);
		//清除切块区域
	//	showJp.recoverALlThing();
		mouseInfo.resetAll();
	}
	private void saveClipInfo(DataOutputStream out) throws Exception {
//		System.out.println("save切块信息长度 ="+clipArr.size());
		//先进行筛选，不存在切块信息的不保存
		for(int i=0;!clipArr.isEmpty()&&i<clipArr.size();i++){
			clipInfoSave cis =(clipInfoSave)clipArr.get(i);
			if(cis.clipArea ==null){
				clipArr.remove(i);
				i =-1;
			}
		}
		if(!clipArr.isEmpty()){
	//		System.out.println("main save: "+clipArr.size());
			out.writeInt(clipArr.size());
			for(int i=0;i<clipArr.size();i++){
				clipInfoSave cifs =(clipInfoSave)clipArr.get(i);
				cifs.save(out);
			}
		}else{
			out.writeInt(0);
		}
	}
	
	private void loadMap(String name) throws Exception {
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(
		        new File(XUtil.getDefPropStr("SavePath") + "\\" + name + ".dat"))));
		
		// start v100, 2013.7.27
		verLoad = in.readInt();
		// end
		siManager.load(in);
		loadClipArr(in);
		anims = Animation.createArrayViaFile(in, siManager);
		siManager.delLossPic();
		in.close();
	}

	private void newMap() {
		Object tmp = JOptionPane.showInputDialog(this, "输入新组的名称：", "新建",
		        JOptionPane.INFORMATION_MESSAGE, new ImageIcon(), null, getDefName());
		if (tmp == null) { return; }
		String mapName = tmp.toString();
		if (mapName == null) { return; }
		mapName = mapName.trim();
		if (mapName.equals("")) { return; }

		setMapName(mapName);
		animList.setAnims(null);
		animList.add();
	}
	
	private void saveMap() {
		Object tmp = JOptionPane.showInputDialog(this, "输入保存名称：", "保存",
		        JOptionPane.INFORMATION_MESSAGE, new ImageIcon(), null, this.name);
		if (tmp == null) { return; }
		String mapName = tmp.toString();
		if (mapName == null) { return; }
		mapName = mapName.trim();
		if (mapName.equals("")) { return; }

		SaveMapThread t = new SaveMapThread(mapName);
		t.start();
	}

	public final static int MAX_ANIM_ID = 9999;
	public final static int MAX_FRAME_COUNT = 30000;
	public final static int MAX_FRAME_INTERVAL = 30000;
	public final static int MAX_SPRITE_COUNT = 0xFF;
	
	private boolean checkAnims() {
		int maxId = 0;
		for(int i = 1; i < anims.length; ++i) {
			if(anims[i].getID() > maxId) maxId = anims[i].getID();
			for(int j = 0; j < i; ++j) {
				if(anims[i].getID() == anims[j].getID()) {
					JOptionPane.showMessageDialog(progress, "【" + anims[i].getName() + "】和【" + anims[j].getName() + "】ID相同。", 
					        "相同ID，保存不成功", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
			
//			if(anims[i].getID() > MAX_ANIM_ID) {
//				JOptionPane.showMessageDialog(progress, "【" + anims[i].getName() + "】的ID大于最大值" + MAX_ANIM_ID + "。",  
//				        "ID过大", JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
			Frame[] frames = anims[i].getFrames();
//			if(frames.length > MAX_FRAME_COUNT) {
//				JOptionPane.showMessageDialog(progress, "【" + anims[i].getName() + "】的帧数大于最大值" + MAX_FRAME_COUNT + "。",  
//				        "帧数过多", JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
			for(int k = 0; k < frames.length; ++k) {
//				if(frames[k].getInterval() > MAX_FRAME_INTERVAL) {
//					JOptionPane.showMessageDialog(progress, "【" + anims[i].getName() + "】的第" + (k + 1) + "帧的时间延迟大于最大值" + MAX_FRAME_INTERVAL + "。",  
//					        "时间延迟过长", JOptionPane.ERROR_MESSAGE);
//					return false;
//				}
//				SISprite[] sprites = frames[k].getSprites();
//				if(sprites.length > MAX_SPRITE_COUNT) {
//					JOptionPane.showMessageDialog(progress, "【" + anims[i].getName() + "】的第" + (k + 1) + "帧包含的图片数量大于最大值" + MAX_SPRITE_COUNT + "。",  
//					        "单帧图片过多", JOptionPane.ERROR_MESSAGE);
//					return false;
//				}
				int t = frames[k].hasMultiRectsWithSameType();
				if(t >= 0) {
					//JOptionPane.showMessageDialog(progress, "【" + anims[i].getName() + "】的第" + (k + 1) + "帧包含的" + Frame.RECT_NAMES[t] + "矩形数量超过1个",  
					 ///       "单帧同类型矩形过多", JOptionPane.ERROR_MESSAGE);
					///return false;
				}
			}
		}
		
		return true;
	}
	
	private void saveMap(String name) throws Exception {
		anims = animList.getAnims();
		
		if(!checkAnims()) return;
		
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
		        new File(XUtil.getDefPropStr("SavePath") + "\\" + name + "_tmp.dat"))));
		
		// start v100, 2013.7.27
		out.writeInt(version);
		// end
		siManager.save(out);
		saveClipInfo(out);
		SL.saveArray(anims, out);
		out.flush();
		out.close();
		XUtil.copyFile(XUtil.getDefPropStr("SavePath") + "\\" + name + "_tmp.dat",
		        XUtil.getDefPropStr("SavePath") + "\\" + name + ".dat");
		
		new File(XUtil.getDefPropStr("SavePath") + "\\" + name + "_tmp.dat").delete();

//		DataOutputStream outm = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
//		        new File(XUtil.getDefPropStr("SaveMobilePath") + "\\" + name + ".dat"))));
//
//		
//		if(anims == null) {
//			SL.writeShort(0, outm);
//		}
//		else {
//			SL.writeShort(anims.length, outm);
//			
//			for(int i = 0; i < anims.length; ++i) {
//				anims[i].saveMobile(outm);
//			}
//		}
//		
//		outm.flush();
//		outm.close();
		//saveAnimName();
		
		saveLua(name);
		exportData(name);
	}
	private void exportData(String name) throws Exception{
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File("export\\" + name + "_tmp.aed"))));
		
		out.writeInt(version);
		if(picNameList !=null){
			out.writeInt(picNameList.length);
			for(int i=0;i<picNameList.length;i++){
				out.writeInt(picNameList[i]);
			}
		}else{
			out.writeInt(0);
		}
		if(anims !=null){
			out.writeInt(anims.length);
			for(int i=0;i<anims.length;i++){
				anims[i].exportData(out);
			}
		}else{
			out.writeInt(0);
		}
		CreateXML.writeDocument(anims, "export\\" + name + ".xml");
		
		out.flush();
		out.close();
		
		XUtil.copyFile("export\\" + name + "_tmp.aed", "export\\" + name + ".aed");
		File file = new File("..\\..\\data");
		if(file.exists()){
			XUtil.copyFile("export\\" + name + "_tmp.aed", "..\\..\\data\\" + name + ".aed");
		}
		
		new File("export\\" + name + "_tmp.aed").delete();
	}
	
	private void saveLua(String name) throws Exception {
		StringBuffer s = new StringBuffer();
	//	s.append(getDefName());
		s.append(name+"Arm={");
		if(anims == null) {
		//	s.append("c=0");
		}
		else {
		//	s.append("c=" + anims.length);
			boolean f = true;
			for(int i = 0; i < anims.length; ++i) {
				if(anims[i].isSaveLua()){
					if(f)
						f=false;
					else
						s.append(",");
					s.append("[" + anims[i].getID() + "]=");
					anims[i].saveLua(s);
				}
			}
		}
		s.append("}");
		File f = new File(XUtil.getDefPropStr("SaveLuaPath"));
		if(!f.exists()) f = new File(".");
		f = new File(f.getAbsolutePath() + "\\" + name + "Arm.lua");
		DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
		out.write(s.toString().getBytes());
		out.flush();
		out.close();
	}
	
//	private void saveAnimName() throws Exception {
//		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
//		        new File(XUtil.getDefPropStr("SaveNamePath")))));
//		if(anims == null) {
//			out.writeInt(0);
//			return;
//		}
//		out.writeInt(anims.length);
//		for(int i = 0; i < anims.length; ++i) {
//			Animation anim = anims[i];
//			out.writeInt(anim.getID());
//			SL.writeString(anim.getName(), out);
//		}
//		out.flush();
//		out.close();
//	}

	public void setMapName(String aName) {
		this.name = aName;
		this.setTitle("动画编辑器v"+(float)version/100+"：" + name);
	}
	
	public String getMapName() {
		return name;
	}
}