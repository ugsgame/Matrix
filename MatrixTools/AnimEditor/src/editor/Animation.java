package editor;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class Animation implements Copyable, Saveable {

	public final static Animation[] copyArray(Animation[] array) {
		Animation[] result = null;
		if (array != null) {
			result = new Animation[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyAnim();
			}
		}
		return result;
	}

	public final static Animation[] createArrayViaFile(DataInputStream in, SIManager siManager)
	        throws Exception {
		Animation[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new Animation[length];
			for (int i = 0; i < length; ++i) {
				result[i] = Animation.createViaFile(in, siManager);
			}
		}
		return result;
	}

	public final static Animation createViaFile(DataInputStream in, SIManager siManager) throws Exception {
		Animation result = new Animation();
		result.load(in, siManager);
		return result;
	}

	private int id;
	private String name;
	private Frame[] frames;
	private boolean isFrame;
	private int maxCutId;

	public Animation() {
		id = 0;
		name = "新建动画";
		frames = null;
		isFrame = true;
		maxCutId = 0;
	}

	public Copyable copy() {
		return copyAnim();
	}

	public final Animation copyAnim() {
		Animation result = new Animation();

		result.copyFrom(this);

		return result;
	}

	public void copyFrom(Animation source) {
		this.id = source.id;
		this.name = source.name;
	//	this.setFrames(source.frames);
		this.setFrames(Frame.copyArray(source.frames));
		this.isFrame = source.isFrame;
		this.maxCutId = source.maxCutId;
	}

	public Frame[] getFrames() {
		return frames;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public boolean isFrame() {
		return isFrame;
	}
	
	public int getCutId() {
		return maxCutId++;
	}

	public void load(DataInputStream in, SIManager siManager) throws Exception {
		// start v100, 2013.7.27 0:帧动画; 1:关键帧动画
		int v = in.readInt();
		isFrame = v == 0 ? true : false;
		maxCutId = in.readInt();
		// end
		id = in.readInt();
		name = SL.readString(in);
		frames = Frame.createArrayViaFile(in, siManager);
	}
	public void exportData(DataOutputStream out) throws Exception {
		// start v100, 2013.7.27 0:帧动画; 1:关键帧动画
		int v = isFrame ? 0 : 1;
		out.writeInt(v);
		// end
		out.writeInt(id);
		if(frames ==null){
			System.out.println("桢为空？？？？？？？？");
			out.writeInt(0);
		}else{
			out.writeInt(frames.length);
			for(int i=0;i<frames.length;i++){
				frames[i].export(out);
			}
		}
	}

	public void save(DataOutputStream out) throws Exception {
		// start v100, 2013.7.27 0:帧动画; 1:关键帧动画
		int v = isFrame ? 0 : 1;
		out.writeInt(v);
		out.writeInt(maxCutId);
		// end
		out.writeInt(id);
		SL.writeString(name, out);
		SL.saveArray(frames, out);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		SL.writeShort(id, out);
		
		if(frames == null) {
			SL.writeShort(0, out);
		}
		else {
			SL.writeShort(frames.length, out);
			for(int i = 0 ;i < frames.length; ++i) {
				frames[i].saveMobile(out);
			}
		}
	}
	
	public boolean isSaveLua() {
		boolean isSave = true;
		if(frames == null) {
			isSave = false;
		}
		else {
			isSave = false;
			for(int i = 0 ;i < frames.length; ++i) {
				if(frames[i].isSaveLua())
					isSave = true;
			}
		}
		
		return isSave;
	}
	
	public void saveLua(StringBuffer s) {
		// 如果anim的id大于等于30000说明是武器动画
		if (30000 <= id)
		{
			s.append("{");
			if(frames == null) {
				s.append("c=0");
			}
			else {
				s.append("c=" + frames.length);
				for(int i = 0 ;i < frames.length; ++i) {
					s.append(",");
					frames[i].saveWeaponLua(s);
				}
			}
			s.append("}");
		}
		else
		{
			s.append("{");
			if(frames == null) {
		//		s.append("c=0");
			}
			else {
		//		s.append("c=" + frames.length);
				boolean f = true;
				for(int i = 0 ;i < frames.length; ++i) {
					if(frames[i].isSaveLua()){
						if(f)
							f = false;
						else
							s.append(",");;
						s.append("[" + (i+1) + "]={");
						frames[i].saveLua(s);
						s.append("}");
					}
				}
			}
			s.append("}");
		}
	}

	public void setFrames(Frame[] aFrames) {
		this.frames = aFrames;//Frame.copyArray(aFrames);
	}

	public void setID(int aID) {
		this.id = aID;
	}

	public void setName(String aName) {
		String s = aName;
		if (s == null) {
			s = "【未命名】";
		}
		else if (s.trim().equals("")) {
			s = "【未命名】";
		}
		this.name = s;
	}
	
	public void setIsFrame(boolean isFrame) {
		this.isFrame = isFrame;
	}

	public String toString() {
		return name.trim();
	}
}

class AnimList extends ManagerList {

	private AnimPanel animPanel;

	public AnimList(AnimPanel aAnimPanel) {
		super();
		this.animPanel = aAnimPanel;
		add();
	}

	//得到新的
	protected void afterEnter(int index) {
		Animation anim = (index >= 0 && index < model.size()) ? (Animation) (model.get(selectedIndex)) : null;
		animPanel.setAnim(anim);
		animPanel.setAnimIndex(index);
	}
	public void delClipInfo(int groupId, int sonId){
		if(!model.isEmpty()){
			for(int i=0;i<model.size();i++){
				Animation anim =(Animation)(model.get(i));
				Frame[] t_frame =anim.getFrames();
				if(t_frame !=null){
					for(int j=0;j<t_frame.length;j++){
						OneFrameDraw ofd =(OneFrameDraw)(t_frame[j].getFrameDraw());
						ofd.delOneTypeClip(groupId, sonId);
					}
				}
			}
		}
	}
	public void updateClipInfo(BufferedImage img, int[] clipArr){
		if(!model.isEmpty()){
			for(int i=0;i<model.size();i++){
				Animation anim =(Animation)(model.get(i));
				Frame[] t_frame =anim.getFrames();
				if(t_frame !=null){
					for(int j=0;j<t_frame.length;j++){
						OneFrameDraw ofd =(OneFrameDraw)(t_frame[j].getFrameDraw());
						ofd.updateClipPic(img, clipArr);
					}
				}
			}
		}
	}

	//先更新
	protected void beforeLeave(int index) {
		if (index >= 0 && index < model.size()) {
			Object obj = model.get(selectedIndex);
			if (obj != null) {
				Animation anim = (Animation) obj;
				animPanel.updateAnim(anim);
			}
		}
	}

	protected Object createNewItem(int index) {
		Animation anim = new Animation();
		Frame frame = new Frame();
		anim.setFrames(new Frame[] { frame });
		anim.setID(index);
		return anim;
	}

	public Animation[] getAnims() {
		beforeLeave(selectedIndex);

		//按id排序
		ArrayList tmp = new ArrayList();
		for (int i = 0; i < model.size(); ++i) {
			Animation manim = (Animation) (model.get(i));
			boolean added = false;
			for (int j = 0; j < tmp.size(); ++j) {
				Animation ranim = (Animation) (tmp.get(j));
				if (manim.getID() < ranim.getID()) {
					tmp.add(j, manim);
					added = true;
					break;
				}
			}
			if (!added) {
				tmp.add(manim);
			}
		}

		Animation[] result = null;
		if (tmp.size() > 0) {
			result = new Animation[tmp.size()];
			for (int i = 0; i < tmp.size(); ++i) {
				result[i] = (Animation) (tmp.get(i));
			}
		}
		return result;
	}

	public void setAnims(Animation[] aAnims) {
		list.clearSelection();
		model.clear();
		if (aAnims != null) {
			for (int i = 0; i < aAnims.length; ++i) {
				model.addElement(aAnims[i]);
			}
			list.setSelectedIndex(0);
		}
	}
}

class AnimPanel extends JPanel {

	AniEdit aniedit;
	public int animIndex, copyAnimIndex;
	public int frameIndex, copyFrameIndex;
	
	private FramePanel framePanel;
	private FrameManager frameManager;
	private FrameRectPanel frameRectPanel;
	private FrameRectManager frameRectManager;
	public FrameButtonPanel fbPanel;
	private ScrollablePanel[] panels;
	private JTabbedPane panelTab;

	private JTextField nameText;
	private JCheckBox isFrameAnim;
	private JCheckBox specialFrameCheck;
	private ValueEditor intervalEditor;
	private NumberSpinner idSpinner;
	private NumberSpinner offsetXSpinner;
	private NumberSpinner offsetYSpinner;
	private ValueChooser moveTypeChooser;

	private ValueChooser damageTypeChooser;
	private NumberSpinner hitDistSpinner;
	private NumberSpinner damageSpinner;

	private JCheckBox shadowFrameCheck;
	private ValueChooser soundChooser;

	public AnimPanel(JDialog owner, MouseInfo aMouseInfo) {
		super();
		framePanel = new FramePanel(owner, aMouseInfo);
		frameRectPanel = new FrameRectPanel(owner, aMouseInfo);
		init(aMouseInfo);
	}

	public AnimPanel(JFrame owner, MouseInfo aMouseInfo) {
		super();
		framePanel = new FramePanel(owner, aMouseInfo);
		frameRectPanel = new FrameRectPanel(owner, aMouseInfo);
		init(aMouseInfo);
	}

	private void applyInterval() {
		fbPanel.applyInterval(intervalEditor.getValue());
	}

	public FrameManager getFrameManager() {
		return frameManager;
	}

	public FramePanel getFramePanel() {
		return framePanel;
	}

	public FrameRectManager getFrameRectManager() {
		return frameRectManager;
	}

	public FrameRectPanel getFrameRectPanel() {
		return frameRectPanel;
	}

	public JTabbedPane getPanelTab() {
		return panelTab;
	}

	public ScrollablePanel[] getPanels() {
		return panels;
	}

	private void init(MouseInfo aMouseInfo) {
		this.frameManager = (FrameManager) (framePanel.getManager());
		framePanel.reset(FrameManager.BASIC_WIDTH, FrameManager.BASIC_HEIGHT);
		this.frameRectManager = (FrameRectManager) (frameRectPanel.getManager());
		frameRectPanel.reset(FrameManager.BASIC_WIDTH, FrameManager.BASIC_HEIGHT);
		frameRectPanel.addBackManager(frameManager);
		fbPanel = new FrameButtonPanel(this);
		
		aniedit =new AniEdit(aMouseInfo);

		this.panels = new ScrollablePanel[2];
		panels[0] = framePanel;
		panels[1] = frameRectPanel;
		panelTab = new JTabbedPane();
		panelTab.setInputMap(JScrollPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new InputMap());
		for (int i = 0; i < MainFrame.LAYER_COUNT; ++i) {
			panelTab.addTab(MainFrame.LAYER_NAMES[i], panels[i].getBackPanel());
		}
		panelTab.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				MainFrame.self.panelTabChanged();
			}
		});

		nameText = new JTextField();
		specialFrameCheck = new JCheckBox("该帧为特殊帧");
		isFrameAnim = new JCheckBox("帧动画");
		intervalEditor = new ValueEditor(100, new int[] { 50, 100, 150, 200, 250, 300, 400 }, new String[] {
		        "50", "100", "150", "200", "250", "300", "400" });
		idSpinner = new NumberSpinner();
		JButton pointButton = new JButton("设置特殊点");
		pointButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				frameManager.setSelfPoint(true);
			}
		});

		JButton intervalButton = new JButton("设置时间间隔");
		intervalButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				applyInterval();
			}
		});

		offsetXSpinner = new NumberSpinner();
		offsetYSpinner = new NumberSpinner();
		moveTypeChooser = new ValueChooser(0, Frame.MOVE_TYPES, Frame.MOVE_TYPE_NAMES);

		damageTypeChooser = new ValueChooser(0, Frame.DAMAGE_TYPES, Frame.DAMAGE_TYPE_NAMES);
		hitDistSpinner = new NumberSpinner();
		damageSpinner = new NumberSpinner();
		shadowFrameCheck = new JCheckBox("拖影");
		Pair soundsPair = Sound.getSoundsPair(MainFrame.self.getSoundManager().getSounds());
		soundChooser = new ValueChooser(0, (int[]) (soundsPair.first), (String[]) (soundsPair.second),
		        ValueChooser.NATURE_INDEX);

		JPanel np = new JPanel();
		np.setLayout(new GridLayout(4, 1));

		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 2, 3, 3);
		c.weighty = 1;

		c.gridx = 0;
		c.weightx = 0;
		p.add(new JLabel("动画的ID："), c);
		c.gridx = 1;
		c.weightx = 1;
		// idSpinner.setPreferredSize(new Dimension(100, 0));
		p.add(idSpinner, c);

		c.gridx = 2;
		c.weightx = 0;
		p.add(new JLabel("动画的名称："), c);
		c.gridx = 3;
		c.weightx = 1;
		p.add(nameText, c);

		c.gridx = 4;
		c.weightx = 0;
	//	p.add(specialFrameCheck, c);
		p.add(isFrameAnim, c);

		c.gridx = 5;
		c.weightx = 1;
		p.add(soundChooser, c);
		np.add(p);

		p = new JPanel();
		p.setLayout(new GridBagLayout());
		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 0;
		p.add(new JLabel("该帧延迟（毫秒）："), c);
		c.gridx = 1;
		c.weightx = 1;
		p.add(intervalEditor, c);

		c.gridx = 2;
		c.weightx = 0;
		c.gridwidth = 1;
		p.add(intervalButton, c);

		c.gridx = 3;
		c.weightx = 0;
		p.add(pointButton, c);
		np.add(p);

		p = new JPanel();
		p.setLayout(new GridBagLayout());
		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 0;
		p.add(new JLabel("向右移动（向左为负）："), c);
		c.gridx = 1;
		c.weightx = 1;
		p.add(offsetXSpinner, c);

		c.gridx = 2;
		c.weightx = 0;
		p.add(new JLabel("向上移动（向下为负）："), c);
		c.gridx = 3;
		c.weightx = 1;
		p.add(offsetYSpinner, c);

		c.gridx = 4;
		// c.weightx = 0;
		// p.add(new JLabel("移动的类型："), c);
		// c.gridx = 5;
		c.weightx = 1;
		p.add(moveTypeChooser, c);
		np.add(p);

		p = new JPanel();
		p.setLayout(new GridBagLayout());
		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 0;
		p.add(new JLabel("伤害类型："), c);
		c.gridx = 1;
		c.weightx = 1;
		p.add(damageTypeChooser, c);

		c.gridx = 2;
		c.weightx = 0;
		p.add(new JLabel("击退距离（向左为负）："), c);
		c.gridx = 3;
		c.weightx = 1;
		p.add(hitDistSpinner, c);

		c.gridx = 4;
		c.weightx = 0;
		p.add(new JLabel("伤害量："), c);
		c.gridx = 5;
		c.weightx = 1;
		p.add(damageSpinner, c);
		c.gridx = 6;
		c.weightx = 0;
		p.add(shadowFrameCheck, c);
		np.add(p);
		
		this.setLayout(new BorderLayout());
		this.add(np, BorderLayout.NORTH);
		this.add(aniedit, BorderLayout.CENTER);
		this.add(fbPanel, BorderLayout.SOUTH);
	}

	public void updateFbPanel(){
		fbPanel.updatefbPanel();
	}
	//
	public void delClipEdit(int groupId, int sonId){
		fbPanel.delEditClipInfo(groupId, sonId);
	}
	public void updateClipEdit(BufferedImage img, int[] clipArr){
		fbPanel.updateClipInfo(img, clipArr);
	}
	
	public void setAnimIndex(int index){
		this.animIndex = index;
	}
	
	public void setFrameIndex(int index){
		this.frameIndex = index;
	}
	
	public void setCopy(){
		copyAnimIndex = animIndex;
		copyFrameIndex = frameIndex;
	//	System.out.println("setCopy: "+copyAnimIndex+", "+copyFrameIndex);
	}
	
	public Frame getCopyFrame(){
		Frame frame=null;
		Animation anim = MainFrame.self.getAnimList().getAnims()[copyAnimIndex];
		frame = anim.getFrames()[copyFrameIndex];
		return frame;
	}
	
	public Frame getFrame() {
		Animation anim = MainFrame.self.getAnimList().getAnims()[animIndex];
		return anim.getFrames()[frameIndex];
	}
	
	public Frame getPreFrame() {
		Frame frame=null;
		Animation anim = MainFrame.self.getAnimList().getAnims()[animIndex];
		if(frameIndex > 0)
			frame = anim.getFrames()[frameIndex-1];
		return frame;
	}
	
	public Frame getNextFrame() {
		Frame frame=null;
		Animation anim = MainFrame.self.getAnimList().getAnims()[animIndex];
		if(frameIndex < anim.getFrames().length-1)
			frame = anim.getFrames()[frameIndex+1];
		return frame;
	}
	
	public Animation getAnim() {
		return MainFrame.self.getAnimList().getAnims()[animIndex];
	}
	
	public boolean getIsFrameAnim() {
		return isFrameAnim.isSelected();
	}
	
	public void setFrame(Frame frame, Frame framePre) {
		aniedit.showjpanel.clearSelect();
		if (frame == null) {
			intervalEditor.setValue(100);
			specialFrameCheck.setSelected(false);
			offsetXSpinner.setIntValue(0);
			offsetYSpinner.setIntValue(0);
			moveTypeChooser.setValue(0);
			damageTypeChooser.setValue(0);
			hitDistSpinner.setIntValue(0);
			damageSpinner.setIntValue(0);
			shadowFrameCheck.setSelected(false);
			soundChooser.setValue(0);
			aniedit.framedraw =null;
			aniedit.framerect =null;
			aniedit.framepoint = null;
			aniedit.framedrawPre = null;
			aniedit.setIsEdit(false);
			aniedit.showjpanel.repaint();
		}
		else {			
			intervalEditor.setValue(frame.getInterval());
			specialFrameCheck.setSelected(frame.isSpecial());
			offsetXSpinner.setIntValue(frame.getOffsetX());
			offsetYSpinner.setIntValue(frame.getOffsetY());
			moveTypeChooser.setValue(frame.getMoveType());
			damageTypeChooser.setValue(frame.getDamageType());
			hitDistSpinner.setIntValue(frame.getHitDist());
			damageSpinner.setIntValue(frame.getDamage());
			shadowFrameCheck.setSelected(frame.isShadow());
			soundChooser.setValue(frame.getSoundID());
			aniedit.framedraw =frame.getFrameDraw();
			aniedit.framerect =frame.getFrameRect();
			aniedit.framepoint =frame.getFramePoint();
			aniedit.setIsEdit(false);
			if(framePre != null)
				aniedit.framedrawPre = framePre.getFrameDraw();
			aniedit.showjpanel.repaint();
		}
	}

	public void updateFrame(Frame frame) {
		if (frame == null) return;
		frame.setInterval(intervalEditor.getValue());
		frame.setSpecial(specialFrameCheck.isSelected());
		frame.setOffsetX(offsetXSpinner.getIntValue());
		frame.setOffsetY(offsetYSpinner.getIntValue());
		frame.setMoveType(moveTypeChooser.getValue());
		frame.setDamageType(damageTypeChooser.getValue());
		frame.setHitDist(hitDistSpinner.getIntValue());
		frame.setDamage(damageSpinner.getIntValue());
		frame.setShadow(shadowFrameCheck.isSelected());
		frame.setSoundID(soundChooser.getValue());
	}

	public void setAnim(Animation anim) {
		if (anim == null) {
			idSpinner.setIntValue(0);
			nameText.setText("");
			isFrameAnim.setSelected(true);
			fbPanel.setFrames(null);
		}
		else {
			idSpinner.setIntValue(anim.getID());
			nameText.setText(anim.getName());
			isFrameAnim.setSelected(anim.isFrame());
			fbPanel.setFrames(anim.getFrames());
		}
	}

	public void updateAnim(Animation anim) {
		if (anim == null) return;
		anim.setID(idSpinner.getIntValue());
		anim.setName(nameText.getText());
		anim.setFrames(fbPanel.getFrames());
		anim.setIsFrame(isFrameAnim.isSelected());
	}
	
}