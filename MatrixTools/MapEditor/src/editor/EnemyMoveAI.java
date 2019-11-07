package editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

class EnemyMoveLogic implements Copyable, Saveable {

	public final static EnemyMoveLogic[] copyArray(EnemyMoveLogic[] array) {
		EnemyMoveLogic[] result = null;
		if (array != null) {
			result = new EnemyMoveLogic[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyEnemyMoveLogic();
			}
		}
		return result;
	}

	public final static EnemyMoveLogic[] createArrayViaFile(DataInputStream in) throws Exception {
		EnemyMoveLogic[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new EnemyMoveLogic[length];
			for (int i = 0; i < length; ++i) {
				result[i] = EnemyMoveLogic.createViaFile(in);
			}
		}
		return result;
	}

	public final static EnemyMoveLogic createViaFile(DataInputStream in) throws Exception {
		EnemyMoveLogic result = new EnemyMoveLogic();
		result.load(in);
		return result;
	}

	public final static int[] MOVE_DIRS = { 0, 1, 2, 3, 4, 5 };
	public final static String[] MOVE_DIR_NAMES = { "发呆", "靠近玩家", "远离玩家", "左", "右", "随机方向" };

	private int dir;
	private int tick;
	private int speed;
	private int prob;

	public EnemyMoveLogic() {
		dir = 0;
		tick = 0;
		speed = 0;
		prob = 0;
	}

	public Copyable copy() {
		return copyEnemyMoveLogic();
	}

	public final EnemyMoveLogic copyEnemyMoveLogic() {
		EnemyMoveLogic result = new EnemyMoveLogic();
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(EnemyMoveLogic source) {
		this.dir = source.dir;
		this.tick = source.tick;
		this.speed = source.speed;
		this.prob = source.prob;
	}

	public void load(DataInputStream in) throws Exception {
		dir = in.readInt();
		tick = in.readInt();
		speed = in.readInt();
		prob = in.readInt();
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(dir);
		out.writeInt(tick);
		out.writeInt(speed);
		out.writeInt(prob);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		SL.writeInt(dir, out);
		SL.writeInt(tick, out);
		SL.writeInt(speed, out);
		SL.writeInt(prob, out);
	}

	public int getDir() {
		return this.dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public int getTick() {
		return this.tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public int getSpeed() {
		return this.speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getProb() {
		return this.prob;
	}

	public void setProb(int prob) {
		this.prob = prob;
	}
}

class EnemyMoveLogicTable extends XTable {

	public EnemyMoveLogicTable() {
		super("设置移动逻辑", new String[] { "方向", "Tick", "速度", "概率（％）" }, true, false);
		this.setComboCol(0, EnemyMoveLogic.MOVE_DIRS, EnemyMoveLogic.MOVE_DIR_NAMES, 
		        ValueChooser.NATURE_INDEX);
		this.setNumberCol(1);
		this.setNumberCol(2);
		this.setNumberCol(3);
	}

	public void setEMLs(EnemyMoveLogic[] emls) {
		DefaultTableModel model = this.getModel();
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}
		if (emls != null) {
			for (int row = 0; row < emls.length; ++row) {
				EnemyMoveLogic eml = emls[row];
				Object[] data = new Object[4];
				data[0] = new Integer(eml.getDir());
				data[1] = new Integer(eml.getTick());
				data[2] = new Integer(eml.getSpeed());
				data[3] = new Integer(eml.getProb());
				model.addRow(data);
			}
		}
	}

	public EnemyMoveLogic[] getEMLs() {
		EnemyMoveLogic[] result = null;
		this.stopEditing();
		DefaultTableModel model = this.getModel();
		int len = model.getRowCount();
		if (len > 0) {
			result = new EnemyMoveLogic[len];
			for (int row = 0; row < len; ++row) {
				EnemyMoveLogic eml = new EnemyMoveLogic();
				eml.setDir(((Integer) (model.getValueAt(row, 0))).intValue());
				eml.setTick(((Integer) (model.getValueAt(row, 1))).intValue());
				eml.setSpeed(((Integer) (model.getValueAt(row, 2))).intValue());
				eml.setProb(((Integer) (model.getValueAt(row, 3))).intValue());
				result[row] = eml;
			}
		}
		return result;
	}
}

class EnemyMoveAI implements Saveable, Copyable {

	public final static EnemyMoveAI[] copyArray(EnemyMoveAI[] array) {
		EnemyMoveAI[] result = null;
		if (array != null) {
			result = new EnemyMoveAI[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyEnemyMoveAI();
			}
		}
		return result;
	}

	public final static EnemyMoveAI[] createArrayViaFile(DataInputStream in) throws Exception {
		EnemyMoveAI[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new EnemyMoveAI[length];
			for (int i = 0; i < length; ++i) {
				result[i] = EnemyMoveAI.createViaFile(in);
			}
		}
		return result;
	}

	public final static EnemyMoveAI createViaFile(DataInputStream in) throws Exception {
		EnemyMoveAI result = new EnemyMoveAI();
		result.load(in);
		return result;
	}

	private boolean mustFacePlayer;
	private boolean canDrop;
	private IntPair trigRange;
	private IntPair endRange;
	private EnemyMoveLogic[] emls;

	public EnemyMoveAI() {
		mustFacePlayer = false;
		canDrop = false;
		trigRange = new IntPair();
		endRange = new IntPair();
		emls = null;
	}

	public Copyable copy() {
		return copyEnemyMoveAI();
	}

	public final EnemyMoveAI copyEnemyMoveAI() {
		EnemyMoveAI result = new EnemyMoveAI();
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(EnemyMoveAI source) {
		this.mustFacePlayer = source.mustFacePlayer;
		this.canDrop = source.canDrop;
		this.trigRange.copyFrom(source.trigRange);
		this.endRange.copyFrom(source.endRange);
		setEMLs(source.emls);
	}

	public void load(DataInputStream in) throws Exception {
		mustFacePlayer = in.readBoolean();
		canDrop = in.readBoolean();
		trigRange = IntPair.createViaFile(in);
		endRange = IntPair.createViaFile(in);
		emls = EnemyMoveLogic.createArrayViaFile(in);
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeBoolean(mustFacePlayer);
		out.writeBoolean(canDrop);
		trigRange.save(out);
		endRange.save(out);
		SL.saveArray(emls, out);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		SL.writeBoolean(mustFacePlayer, out);
		SL.writeBoolean(canDrop, out);
		trigRange.saveMobile(out);
		endRange.saveMobile(out);
		SL.saveArrayMobile(emls, out);
	}

	public boolean isMustFacePlayer() {
		return this.mustFacePlayer;
	}

	public void setMustFacePlayer(boolean mustFacePlayer) {
		this.mustFacePlayer = mustFacePlayer;
	}

	public boolean isCanDrop() {
		return this.canDrop;
	}

	public void setCanDrop(boolean canDrop) {
		this.canDrop = canDrop;
	}

	public EnemyMoveLogic[] getEMLs() {
		return this.emls;
	}

	public void setEMLs(EnemyMoveLogic[] emls) {
		this.emls = EnemyMoveLogic.copyArray(emls);
	}

	
    public IntPair getTrigRange() {
    	return this.trigRange;
    }

	
    public void setTrigRange(IntPair trigRange) {
    	if(trigRange == null) {
    		trigRange = new IntPair();
    	}
    	this.trigRange.copyFrom(trigRange);
    }

	
    public IntPair getEndRange() {
    	return this.endRange;
    }

	
    public void setEndRange(IntPair endRange) {
    	if(endRange == null) {
    		endRange = new IntPair();
    	}
    	this.endRange.copyFrom(endRange);
    }
}

class EnemyMoveAIPanel extends JPanel {

	private JCheckBox faceCheck;
	private JCheckBox dropCheck;
	private RangePanel trigRangePanel;
	private RangePanel endRangePanel;
	private EnemyMoveLogicTable moveTable;

	public EnemyMoveAIPanel() {
		faceCheck = new JCheckBox("始终面向玩家");
		dropCheck = new JCheckBox("允许从高台跳落");
		trigRangePanel = new RangePanel("启动距离：");
		endRangePanel = new RangePanel("终止距离：");
		moveTable = new EnemyMoveLogicTable();
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 5, 3, 5);
		c.weighty = 0;
		c.weightx = 0;
		
		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 1;
		add(faceCheck, c);
		c.gridx = 1;
		add(dropCheck, c);
		
		c.gridy++;
		c.gridx = 0;
		c.weightx = 1;
		c.gridwidth = 2;
		add(trigRangePanel, c);
		c.gridwidth = 1;

		c.gridy++;
		c.gridx = 0;
		c.weightx = 1;
		c.gridwidth = 2;
		add(endRangePanel, c);
		c.gridwidth = 1;
		
		
		c.gridy++;
		c.gridx = 0;
		c.weighty = 1;
		c.gridwidth = 2;
		add(moveTable, c);
		c.gridwidth = 1;
	}
	
	public void setEMAI(EnemyMoveAI emai) {
		if(emai == null) {
			faceCheck.setSelected(false);
			dropCheck.setSelected(false);
			trigRangePanel.setRange(new IntPair(0, 0));
			endRangePanel.setRange(new IntPair(0, 0));
			moveTable.setEMLs(null);
		}
		else {
			faceCheck.setSelected(emai.isMustFacePlayer());
			dropCheck.setSelected(emai.isCanDrop());
			trigRangePanel.setRange(emai.getTrigRange());
			endRangePanel.setRange(emai.getEndRange());
			moveTable.setEMLs(emai.getEMLs());
		}
	}
	
	public void updataEMAI(EnemyMoveAI emai) {
		if(emai == null) return;
		emai.setMustFacePlayer(faceCheck.isSelected());
		emai.setCanDrop(dropCheck.isSelected());
		emai.setTrigRange(trigRangePanel.getRange());
		emai.setEndRange(endRangePanel.getRange());
		emai.setEMLs(moveTable.getEMLs());
	}
}
