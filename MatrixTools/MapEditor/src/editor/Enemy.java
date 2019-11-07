package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Enemy extends SISprite {

	public final static Enemy[] copyArray(Enemy[] array) {
		Enemy[] result = null;
		if (array != null) {
			result = new Enemy[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyEnemy();
			}
		}
		return result;
	}

	public final static Enemy[] createArrayViaFile(DataInputStream in, SIManager siManager)
	        throws Exception {
		Enemy[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new Enemy[length];
			for (int i = 0; i < length; ++i) {
				result[i] = Enemy.createViaFile(in, siManager);
			}
		}
		return result;
	}

	public final static Enemy createViaFile(DataInputStream in, SIManager siManager)
	        throws Exception {
		Enemy result = new Enemy();
		result.load(in, siManager);
		return result;
	}
	
	public int compareTo(Object o) {
		if (o == null) {
			return 1;
		}
		else if (!(o instanceof BasicSprite)) { return this.hashCode() - o.hashCode(); }

		BasicSprite dest = (BasicSprite) o;

		if (this.getX() != dest.getX()) {
			return this.getX() - dest.getX();
		}
		else if (this.getY() != dest.getY()) {
			return this.getY() - dest.getY();
		}
		else {
			return this.getID() - dest.getID();
		}
	}

	public final static int TRIGGER_RIGHT_H = 1;
	public final static int TRIGGER_MIDDLE_H = 2;
	public final static int TRIGGER_LEFT_H = 3;
	public final static int TRIGGER_TOP_V = 4;
	public final static int TRIGGER_MIDDLE_V = 5;
	public final static int TRIGGER_BOTTOM_V = 6;
	
	public final static int[] TRIGGER_TYPES = {
		TRIGGER_RIGHT_H,
		TRIGGER_MIDDLE_H, 
		TRIGGER_LEFT_H,
		TRIGGER_TOP_V,
		TRIGGER_MIDDLE_V,
		TRIGGER_BOTTOM_V
	};
	
	public final static String[] TRIGGER_NAMES = {
		"", 
		"ÆÁÄ»ÓÒ±ß´¥·¢", 
		"ÆÁÄ»ÖÐ¼ä´¥·¢", 
		"ÆÁÄ»×ó±ß´¥·¢",
		"ÆÁÄ»ÉÏ±ß´¥·¢",
		"ÆÁÄ»ÖÐ¼ä´¥·¢",
		"ÆÁÄ»ÏÂ±ß´¥·¢",
	};
	
	public final static Color[] TRIGGER_LINE_COLORS = {
		null, 
		Color.RED,
		Color.GREEN, 
		Color.BLUE , 
		Color.RED,
		Color.GREEN, 
		Color.BLUE 
	}; 

	private String scriptName;
	private int triggerType;
	private int triggerY;
	private int triggerX;
	
	private String prop;

	private Enemy() {
		super();
		init();
	}

	public Enemy(SingleImage si, int id, int left, int top, boolean flip, int layer) {
		super(si, id, left, top, flip, si.getName() + "_" + id, layer);
		init();
	}
	

	// public Color getSelectedBorderColor() {
	// return new Color(0xFF00FF);
	// }

	// public Color getMovingBorderColor() {
	// return new Color(0xFF00FF);
	// }

	private void init() {
		this.prop = "";
		this.scriptName = "";
		this.triggerType = TRIGGER_RIGHT_H;
		this.triggerY = getY() + (si == null ? 0 : getHeight());
		this.triggerX = getX();
	}

	public Copyable copy() {
		return copyEnemy();
	}

	public void copyFrom(Enemy source) {
		super.copyFrom(source);
		this.prop = source.prop;
		this.scriptName = source.scriptName;
		this.triggerType = source.triggerType;
		this.triggerY = source.triggerY;
		this.triggerX = source.triggerX;
	}

	public final Enemy copyEnemy() {
		Enemy result = new Enemy();
		result.copyFrom(this);
		return result;
	}

	public int getLeft() {
		return getX();
	}

	public int getTop() {
		return getY();
	}

	public int getWidth() {
		return si.getWidth();
	}

	public int getHeight() {
		return si.getHeight();
	}

	public String getInfo() {
		String result = getName() + ", id = " + getID() + ", x = " + getX() + ", y = " + getY();
		return result;
	}

//	public void paintIdle(Graphics g, int x, int y ,double roate ,double scale) {
//		si.paintRoate(g, x + getLeft() - getX(), y + getTop() - getY(), isFlip() ,roate ,scale);
//		//paintIdle(g, x, y ,roate);
//	}

	protected void load(DataInputStream in, SIManager siManager) throws Exception {
		super.load(in);
		int siID = in.readInt();
		si = siManager.getSI(siID);
		layer = in.readInt();
		layer2 = in.readInt();
		flip = in.readBoolean();
		scriptName = SL.readString(in);
		triggerType = in.readInt();
		triggerX = in.readInt();
		triggerY = in.readInt();
		this.setRoate(in.readDouble());
		this.setScale(in.readDouble());
		prop = SL.readString(in);
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(getID());
		out.writeInt(getX());
		out.writeInt(getY());
		SL.writeString(getName(), out);
		
		out.writeInt(si.getID());
		out.writeInt(layer);
		out.writeInt(layer2);
		out.writeBoolean(flip);
		SL.writeString(scriptName, out);
		out.writeInt(triggerType);
		out.writeInt(triggerX);
		out.writeInt(triggerY);
		out.writeDouble(this.getRoate());
		out.writeDouble(this.getScale());
		SL.writeString(prop, out);
	}
	

	public void saveMobile(DataOutputStream out) throws Exception {
	// do nothing
	}

	public String getFullScriptName() {
		return "Script_" + scriptName;
	}
	
	public void saveJson(StringBuffer s, boolean wins) {
		MapInfo mi = MainFrame.self.getMapInfo();
		int x = getLeft() - mi.getRealLeft();
		int y = getTop() - mi.getRealTop();
		int w = getWidth();
		int h = getHeight();
		
		s.append("{\"id\":" + getID() + ",\"t\":" + si.getID() + ",\"bowenT\":{}");
		s.append(",\"x\":" + x + ",\"y\":" + y);
		s.append(",\"w\":" + w + ",\"h\":" + h);
		s.append(",\"r\":" + this.getRoate());
		s.append(",\"s\":" + this.getScale());
		if (triggerType <= 3)
		{
			s.append(",\"trig\":" + triggerType + ",\"trigX\":" + triggerX);
		}
		else
		{
			s.append(",\"trig\":" + triggerType + ",\"trigY\":" + triggerY);
		}
		if (flip) 
			s.append(",\"flip\":true");
		else
			s.append(",\"flip\":false");
		
		if (!scriptName.equals("")) {
			if(wins) s.append(",\"SN\":" + getScriptName() + "\"");
			s.append(",\"Script\":" + getFullScriptName());
		}
		if (!prop.equals("")) s.append("," + prop);
		s.append("}");		
	}

	public void saveLua(StringBuffer s, boolean wins) {
		MapInfo mi = MainFrame.self.getMapInfo();
		int x = getLeft() - mi.getRealLeft();
		int y = getTop() - mi.getRealTop();
		int w = getWidth();
		int h = getHeight();	
		s.append("{id=" + getID() + ",t=" + si.getID() + ",bowenT={}");
		s.append(",x=" + x + ",y=" + y);
		s.append(",w=" + w + ",h=" + h);
		s.append(",r=" + this.getRoate());
		s.append(",s=" + this.getScale());
		if (triggerType <= 3)
		{
			s.append(",trig=" + triggerType + ",trigX=" + triggerX);
		}
		else
		{
			s.append(",trig=" + triggerType + ",trigY=" + triggerY);
		}
		if (flip) s.append(",flip=1");
		if (!scriptName.equals("")) {
			if(wins) s.append(",SN=\"" + getScriptName() + "\"");
			s.append(",Script=" + getFullScriptName());
		}
		if (!prop.equals("")) s.append("," + prop);
		s.append("}");
	}

	public boolean setScriptName(EnemyManager em) {
		ScriptNameChooser c = new ScriptNameChooser(MainFrame.self, em, this);
		if (c.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			scriptName = c.getScriptName().trim();
			return !scriptName.equals("");
		}
		else {
			return false;
		}
	}
	
	public final static String getScriptPath() {
		File f = new File(XUtil.getDefPropStr("ScriptPath"));
		if(!f.exists()) {
			f = new File(".\\lua");
			if(!f.exists()) {
				try {
					f.createNewFile();
				}
				catch(IOException e) {
				}
			}
		}
		return f.getAbsolutePath();
	}
	
	public final static File getScriptFile(String name) {
		return new File(getScriptPath() + "\\" + name + ".lua");
	}

	public void editScript(EnemyManager em) throws Exception {
		if (scriptName.equals("")) {
			if (!setScriptName(em)) return;
		}
		File f = getScriptFile(scriptName);
		if (!f.exists()) {
			f.createNewFile();
			String init = "function " + getFullScriptName() + "(e) \r\n\tlocal b = e.bowenT;\r\n\t\r\nend";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
			out.write(init.getBytes());
			out.flush();
			out.close();
		}
		
		Runtime.getRuntime().exec(XUtil.getDefPropStr("EmEditor") + " " + f.getAbsolutePath());
	}
	
	public void editProp(EnemyManager em) {
		PropEditor editor = new PropEditor(MainFrame.self, em, this);
		if (editor.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			setProp(editor.getProp());
		}
	}

	public String getProp() {
		return this.prop;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}
	
	public String getScriptName() {
		return this.scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public int getTriggerType() {
		return this.triggerType;
	}

	public void setTriggerType(int triggerType) {
		this.triggerType = triggerType;
	}

	public int getTriggerY() {
		return this.triggerY;
	}
	
	public int getTriggerX()
	{
		return this.triggerX;
	}

	public void setTriggerY(int triggerY) {
		this.triggerY = triggerY;
	}
	
	public void setTriggerX(int triggerX) {
		this.triggerX = triggerX;
	}
}


class ScriptNameChooser extends OKCancelDialog {

	//private EnemyManager em;
	//private Enemy enemy;
	private JTextField nameText;
	private JButton fileBt;
	private JFileChooser fc;

	public ScriptNameChooser(JFrame owner, EnemyManager em, Enemy enemy) {
		super(owner);
		setTitle("ÉèÖÃ½Å±¾Ãû³Æ");
		
		fc = new JFileChooser(Enemy.getScriptPath()) {
			public boolean accept(File f) {
				return (f.isFile() && FileExtFilter.getExtension(f).equalsIgnoreCase("lua"));
			}
		};
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
		
		
		//this.em = em;
		//this.enemy = enemy;
		nameText = new JTextField(enemy.getScriptName());
		nameText.setText(enemy.getScriptName().trim().equals("") ? enemy.getName() : enemy.getScriptName());
		
		fileBt = new JButton("...");
		fileBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseFileName();
			}
		});
		
		Container cp = this.getContentPane(); 
		cp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 5, 3, 5);

		
		c.gridy = 0;
		c.weighty = 1;
		
		c.gridx = 0;
		c.weightx = 0;
		cp.add(new JLabel("½Å±¾Ãû³Æ£º"), c);
		
		c.gridx = 1;
		c.weightx = 1;
		cp.add(nameText, c);
		
		c.gridx = 2;
		c.weightx = 0;
		cp.add(fileBt, c);
		
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 3;
		c.weightx = 1;
		c.weighty = 0;
		cp.add(buttonPanel, c);
		
		show();
	}
	
	private void chooseFileName() {
		String name = nameText.getText();
		if(name != null) {
			if(!name.trim().equals("")) {
				File f = Enemy.getScriptFile(name);
				if(f.exists()) {
					fc.setSelectedFile(f);
				}
			}
		}
		int result = fc.showOpenDialog(this);
		
		if(result == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			if(f != null) {
				nameText.setText(FileExtFilter.getName(f));
			}
		}
	}

	protected void cancelPerformed() {
		hide();
	}

	protected void okPerformed() {
		if (nameText.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this, "½Å±¾Ãû³Æ²»ÄÜÎª¿Õ", "´íÎó", JOptionPane.ERROR_MESSAGE);
			return;
		}
		closeType = OKCancelDialog.OK_PERFORMED;
		hide();
	}
	
	public String getScriptName() {
		return nameText.getText();
	}

}


class PropEditor extends OKCancelDialog {

	//private EnemyManager em;
	//private Enemy enemy;
	private JTextArea propText;

	public PropEditor(JFrame owner, EnemyManager em, Enemy enemy) {
		super(owner);
		setTitle("±à¼­ÊôÐÔ");
				
		
		//this.em = em;
		//this.enemy = enemy;
		
		propText = new JTextArea(enemy.getProp());
		propText.setLineWrap(true);
		
		Container cp = this.getContentPane(); 
		cp.setLayout(new BorderLayout());
		
		cp.add(new JScrollPane(propText), BorderLayout.CENTER);
		
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
		
		show();
	}

	protected void cancelPerformed() {
		hide();
	}

	protected void okPerformed() {
		closeType = OKCancelDialog.OK_PERFORMED;
		hide();
	}
	
	public String getProp() {
		return propText.getText();
	}

}


class EnemyManager extends SpriteManager {

	public EnemyManager(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) {
		super(scrollablePanel, mouseInfo);
	}

	protected Sprite createSprite(int x, int y) {
		if (!(mouseInfo.getPainter() instanceof SIPainter)) { return null; }
		if(mouseInfo.getLayer() < 0) {return null;}
		if(!MainFrame.self.getLayerManager().getLayerIsShow(mouseInfo.getLayer())) {return null;}
		SIPainter painter = (SIPainter) mouseInfo.getPainter();
		return new Enemy(painter.getSI(), useMaxID(), painter.getSI().getLeft(x),
		        painter.getSI().getTop(y), mouseInfo.isFlip(), mouseInfo.getLayer());
	}

	public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception {
		SL.writeInt(sprites.size(), out);
		for (int i = 0; i < sprites.size(); ++i) {
			Enemy enemy = (Enemy) (sprites.get(i));
			enemy.saveMobile(out);
		}
	}
	
	@Override
	public void saveMobile(DataOutputStream out, int layer) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void save(DataOutputStream out, Object[] resManagers) throws Exception {
		out.writeInt(sprites.size());
		for (int i = 0; i < sprites.size(); ++i) {
			Enemy enemy = (Enemy) (sprites.get(i));
			enemy.save(out);
		}
	}

	public void load(DataInputStream in, Object[] resManagers) throws Exception {
		reset();
		SIManager siManager = (SIManager) (resManagers[MainFrame.RES_ENEMY]);
		int length = in.readInt();
		for (int i = 0; i < length; ++i) {
			Enemy enemy = Enemy.createViaFile(in, siManager);
			addSprite(enemy);
		}
	}
	
	public void saveLuaScript(StringBuffer buffer) throws Exception {
		StringBuffer scriptLua = new StringBuffer();
		StringBuffer cleanFunc = new StringBuffer();
		cleanFunc.append("function CleanEnemyScript()\r\n");
		
		ArrayList scriptFiles = new ArrayList();
		for (int i = 0; i < sprites.size(); ++i) {
			Enemy enemy = (Enemy) (sprites.get(i));
			String scriptName = enemy.getScriptName();
			File f = Enemy.getScriptFile(scriptName);
			if(f.exists()) {
				boolean hasScript = false;
				for(int j = 0; j < scriptFiles.size(); ++j) {
					File _f = (File)(scriptFiles.get(j));
					if(f.equals(_f)) {
						hasScript = true;
						break;
					}
				}
				if(!hasScript) {
					scriptFiles.add(f);
					scriptLua.append(XUtil.readFile(f));
					scriptLua.append("\r\n");
					cleanFunc.append("\t" + enemy.getFullScriptName() + "=nil\r\n");
				}
			}
		}
		cleanFunc.append("end");
		
		buffer.append(scriptLua);
		buffer.append("--Clean Function\r\n");
		buffer.append(cleanFunc);
		buffer.append("\r\n\r\n");
	}
	
	public void saveJson(StringBuffer buffer, int layer, int tabNum) throws Exception {
		StringBuffer winsLua = new StringBuffer();
		StringBuffer enemyLua = new StringBuffer();
		enemyLua.append(XUtil.getTabs(tabNum));
		winsLua.append(XUtil.getTabs(tabNum));
		enemyLua.append("\"enemies\":[\r\n");
		winsLua.append("\"enemies\":[\r\n");
		
		if(sprites.size() <= 0) {
		//	enemyLua.append("c=0");
		//	winsLua.append("c=0");
		}
		else {
		//	enemyLua.append("\r\n\tc=" + sprites.size() + ",\r\n");
		//	winsLua.append("\r\n\tc=" + sprites.size() + ",\r\n");
			int count = 0,count_ = 0;
			for (int i = 0; i < sprites.size(); ++i)
			{
				Enemy enemy = (Enemy) (sprites.get(i));
				if(enemy.getLayer2() == layer)count++;
			}
			for (int i = 0; i < sprites.size(); ++i) {
				Enemy enemy = (Enemy) (sprites.get(i));
				if(enemy.getLayer2() == layer) {
					count_++;
					enemyLua.append(XUtil.getTabs(tabNum+1));
					winsLua.append(XUtil.getTabs(tabNum+1));
					enemy.saveJson(enemyLua, false);
					enemy.saveJson(winsLua, true);
					if(count_ < count)
					{
						enemyLua.append(",");
						winsLua.append(",");					
					}
//					if(i < sprites.size() - 1) {
//						enemyLua.append(",");
//						winsLua.append(",");
//						
//					}
					enemyLua.append("\r\n");
					winsLua.append("\r\n");
				}
			}
		}
		enemyLua.append(XUtil.getTabs(tabNum));
		winsLua.append(XUtil.getTabs(tabNum));
		enemyLua.append("]");
		winsLua.append("]");
		
		buffer.append(winsLua);
		buffer.append("\r\n");	
	}
	
	public void saveLua(StringBuffer buffer, int layer, int tabNum) throws Exception {
		StringBuffer winsLua = new StringBuffer();
		StringBuffer enemyLua = new StringBuffer();
		enemyLua.append(XUtil.getTabs(tabNum));
		winsLua.append(XUtil.getTabs(tabNum));
		enemyLua.append("enemies={\r\n");
		winsLua.append("enemies={\r\n");
		
		if(sprites.size() <= 0) {
		//	enemyLua.append("c=0");
		//	winsLua.append("c=0");
		}
		else {
		//	enemyLua.append("\r\n\tc=" + sprites.size() + ",\r\n");
		//	winsLua.append("\r\n\tc=" + sprites.size() + ",\r\n");
			for (int i = 0; i < sprites.size(); ++i) {
				Enemy enemy = (Enemy) (sprites.get(i));
				if(enemy.getLayer2() == layer) {
					enemyLua.append(XUtil.getTabs(tabNum+1));
					winsLua.append(XUtil.getTabs(tabNum+1));
					enemy.saveLua(enemyLua, false);
					enemy.saveLua(winsLua, true);
					if(i < sprites.size() - 1) {
						enemyLua.append(",");
						winsLua.append(",");
					}
					enemyLua.append("\r\n");
					winsLua.append("\r\n");
				}
			}
		}
		enemyLua.append(XUtil.getTabs(tabNum));
		winsLua.append(XUtil.getTabs(tabNum));
		enemyLua.append("},");
		winsLua.append("},");
		
		buffer.append(winsLua);
		buffer.append("\r\n");
		
//		s = "--Script\r\n" + scriptLua + "\r\n\r\n--Clean Function\r\n" + cleanFunc + "\r\n\r\n--Data\r\n" + enemyLua.toString();
//		
//		String debugPath = XUtil.getDefPropStr("LuaDebugPath");
//		if(!(new File(debugPath).exists())) {
//			debugPath = ".";
//		}
//		String releasePath = XUtil.getDefPropStr("LuaReleasePath");
//		if(!(new File(releasePath).exists())) {
//			releasePath = ".";
//		}
//	//	File debugF = new File(debugPath + "\\" + name + ".lua");		
//		File releaseF = new File(releasePath + "\\" + name + ".lua");
//		
//		out = new DataOutputStream(new FileOutputStream(releaseF));
//		out.write(s.getBytes());
//	//	out.write(s.getBytes(), out.size(), s.length());
//		out.flush();
//		out.close();
		
	//	XUtil.copyFile(debugF.getAbsolutePath(), releaseF.getAbsolutePath());
	}
	
	private class SetTriggerActionListener implements ActionListener {
		private Enemy[] enemies;
		private int type;
		
		public SetTriggerActionListener(Enemy[] enemies, int type) {
			this.enemies = enemies;
			this.type = type;
		}
		
		public void actionPerformed(ActionEvent ae) {
			Enemy enemy = null;
			
			for(int i = 0; i < enemies.length; ++i) {
				Enemy e = enemies[i];
				if(i == 0) {
					enemy = e;
				}
				else {
					if(e.getTriggerX() < enemy.getTriggerX()) {
						enemy = e;
					}
				}
			}
			
			for(int i = 0; i < enemies.length; ++i) {
				Enemy e = enemies[i];
				e.setTriggerX(enemy.getTriggerX());
				e.setTriggerY(enemy.getTriggerY());
				e.setTriggerType(type);
			}
			
			startSetTrigger(enemies, type);
		}
	}
	
	private class SetScriptNameActionListener implements ActionListener {
		private EnemyManager em;
		private Enemy enemy;
		
		public SetScriptNameActionListener(EnemyManager em, Enemy enemy) {
			this.em = em;
			this.enemy = enemy;
		}
		
		public void actionPerformed(ActionEvent e) {
			enemy.setScriptName(em);
		}
	}
	
	private class EditScriptActionListener implements ActionListener {
		private EnemyManager em;
		private Enemy enemy;
		
		public EditScriptActionListener(EnemyManager em, Enemy enemy) {
			this.em = em;
			this.enemy = enemy;
		}
		
		public void actionPerformed(ActionEvent e) {
			em.showProperties(enemy);
		}
	}
	
	private class EditPropActionListener implements ActionListener {
		private EnemyManager em;
		private Enemy enemy;
		
		public EditPropActionListener(EnemyManager em, Enemy enemy) {
			this.em = em;
			this.enemy = enemy;
		}
		
		public void actionPerformed(ActionEvent e) {
			em.editProp(enemy);
		}
	}
	
	private Enemy[] triggerEnemies;
	private int triggerType;
	private int triggerY;
	private int triggerX;
	private boolean triggerSetting = false;
	private boolean triggerDragging = false;
	private int triggerDragY;
	private int triggerDragX;
	
	private void startSetTrigger(Enemy[] enemies, int type) {
		triggerEnemies = enemies;
		triggerType = type;
		triggerY = enemies[0].getTriggerY();
		triggerX = enemies[0].getTriggerX();
		triggerSetting = true;
		triggerDragging = false;
		scrollablePanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		scrollablePanel.repaint();
	}
	
	private boolean canStartDragTrigger(MouseEvent e) {
		if (triggerType > 3) 
		{
			return Math.abs(mouseY - triggerY) <= 5;
		}
		else
		{
			return Math.abs(mouseX - triggerX) <= 5;
		}
	}

	protected void selectPointMouseMoved(MouseEvent e) {
		if(!triggerSetting) {
			super.selectPointMouseMoved(e);
			return;
		}
		if(canStartDragTrigger(e)) {
			scrollablePanel.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
		}
		else {
			scrollablePanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
	}
	
	protected void selectPointMouseLeftPressed(MouseEvent e) {
		if(!triggerSetting) {
			super.selectPointMouseLeftPressed(e);
			return;
		}
		if(canStartDragTrigger(e)) {
			triggerDragging = true;
			triggerDragY = mouseY;
			triggerDragX = mouseX;
		}
	}
	
	protected void selectPointMouseDragged(MouseEvent e) {
		if(!triggerSetting) {
			super.selectPointMouseDragged(e);
			return;
		}
		if(!triggerDragging) return;
		triggerY += mouseY - triggerDragY;
		triggerX += mouseX - triggerDragX;
		triggerDragX = mouseX;
		triggerDragY = mouseY;
		scrollablePanel.repaint();
	}
	
	protected void selectPointMouseLeftReleased(MouseEvent e) {
		if(!triggerSetting) {
			super.selectPointMouseLeftReleased(e);
			return;
		}
		if(!triggerDragging) return;
		for(int i = 0; i < triggerEnemies.length; ++i) {
			Enemy en = triggerEnemies[i];
			en.setTriggerY(triggerY);
			en.setTriggerX(triggerX);
		}
		triggerDragging = false;
	}
	
	protected void selectPointMouseRightPressed(MouseEvent e) {
		if(!triggerSetting) {
			super.selectPointMouseRightPressed(e);
			return;
		}
		triggerSetting = false;
		scrollablePanel.setCursor(Cursor.getDefaultCursor());
	}
	
	protected void showSinglePopupMenu(Sprite sprite, int x, int y) {
		Enemy[] enemies = new Enemy[1];
		enemies[0] = (Enemy)sprite;
		
		JPopupMenu popup = new JPopupMenu();
		JMenuItem menu;
		for(int i = 0; i < Enemy.TRIGGER_TYPES.length; ++i) {
			int type = Enemy.TRIGGER_TYPES[i];
			menu = new JMenuItem(Enemy.TRIGGER_NAMES[type]);
			menu.addActionListener(new SetTriggerActionListener(enemies, type));
			popup.add(menu);
		}
		
		menu = new JMenuItem("ÉèÖÃ½Å±¾Ãû");
		menu.addActionListener(new SetScriptNameActionListener(this, enemies[0]));
		popup.add(menu);
		
		menu = new JMenuItem("±à¼­½Å±¾");
		menu.addActionListener(new EditScriptActionListener(this, enemies[0]));
		popup.add(menu);
		
		menu = new JMenuItem("±à¼­ÊôÐÔ");
		menu.addActionListener(new EditPropActionListener(this, enemies[0]));
		popup.add(menu);
		
		popup.show(scrollablePanel, x, y);
	}
	
	protected void editProp(Enemy enemy) {
		if(enemy == null) return;
		enemy.editProp(this);
	}

	protected void showProperties(Sprite sprite) {
		if (sprite == null) return;
		if (!(sprite instanceof Enemy)) return;
		Enemy enemy = (Enemy) sprite;
		try {
			enemy.editScript(this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void showMultiPopupMenu(Sprite[] sprites, int x, int y) {
		Enemy[] enemies = new Enemy[sprites.length];
		for(int i = 0; i < sprites.length; ++i) {
			enemies[i] = (Enemy)sprites[i];
		}
		
		JPopupMenu popup = new JPopupMenu();
		JMenuItem menu;
		menu = new JMenuItem("ÆÁÄ»×ó±ß´¥·¢");
		menu.addActionListener(new SetTriggerActionListener(enemies, Enemy.TRIGGER_RIGHT_H));
		popup.add(menu);
		
		popup.show(scrollablePanel, x, y);
	}
	
	private void paintTriggerLines(Graphics g, Enemy[] enemies) {
		for(int i = 0; i < enemies.length; ++i) {
			Enemy e = enemies[i];
			int y = e.getTriggerY();
			int x = e.getTriggerX();
			int type = e.getTriggerType();
			paintTriggerLine(g, x, y, type);
		}
	}
	
	private void paintTriggerLine(Graphics g, int x, int y, int type) {
		MapInfo mapInfo = MainFrame.self.getMapInfo();
		g.setColor(Enemy.TRIGGER_LINE_COLORS[type]);
		if (type <= 3) 
		{
			g.drawLine(x, 0, x, mapInfo.getHeight());
			g.setColor(Color.BLACK);
			drawStringV(g, Enemy.TRIGGER_NAMES[type], x, 10);
		}
		else
		{
			g.drawLine(0, y, mapInfo.getWidth(), y);
			g.setColor(Color.BLACK);
			g.drawString(Enemy.TRIGGER_NAMES[type], 10, y - 5);
		}
		
	}
	
	private void drawStringV(Graphics g, String str, int x, int y)
	{
		int strLen = str.length();
		for (int i = 0; i < strLen; i++)
		{
			String s = str.substring(i, i+1);
			g.drawString(s, x, y + (i * g.getFontMetrics().getHeight()));
		}
	}
	
	public void paintSprites(Graphics g) {
		super.paintSprites(g);
		if(triggerSetting) {
			paintTriggerLine(g, triggerX, triggerY, triggerType);
		}
		else {
			if(!getSelection().isEmpty()) {
				Sprite[] sprites = getSelection().getSprites();
				Enemy[] enemies = new Enemy[sprites.length];
				for(int i = 0; i < enemies.length; ++i) {
					enemies[i] = (Enemy)sprites[i];
				}
				paintTriggerLines(g, enemies);
			}
		}
	}
}

class EnemyPanel extends SpriteManagerPanel {

	public EnemyPanel(JFrame owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	public EnemyPanel(JDialog owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	protected SpriteManager createManager() {
		return new EnemyManager(this, mouseInfo);
	}
}