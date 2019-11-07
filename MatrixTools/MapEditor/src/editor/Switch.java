package editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//public class Switch {
//    public Switch() {
//    }
//}

class SwitchManager {

	public final static int DEF_SWITCH_LENGTH = XUtil.getDefPropInt("SwitchsDefLength");

	private String[] switchs;

	public SwitchManager() {
		switchs = createEmptySwitchs(DEF_SWITCH_LENGTH);
	}

	public void reset() {
		switchs = createEmptySwitchs(DEF_SWITCH_LENGTH);
	}

	public static String[] createEmptySwitchs(int length) {
		String[] result = new String[length];
		for (int i = 0; i < length; ++i) {
			result[i] = "";
		}
		return result;
	}

	public String[] getSwitchs() {
		return switchs;
	}

	public void setSwitchs(String[] switchs) {
		this.switchs = switchs;
	}

	public String getSwitch(int switchID) {
		if (switchID < 0 || switchID >= switchs.length) {
			return null;
		}
		else {
			return switchs[switchID];
		}
	}

	public void save(String name) throws Exception {
		File f = new File(XUtil.getDefPropStr("SavePath") + "\\" + name + "_switch.dat");
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
		out.writeInt(switchs.length);
		for (int i = 0; i < switchs.length; ++i) {
			SL.writeString(switchs[i], out);
		}
		out.flush();
		out.close();
	}

	public void load(String name) throws Exception {
		File f = new File(XUtil.getDefPropStr("SavePath") + "\\" + name + "_switch.dat");
		if (!f.exists()) { return; }
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
		int length = in.readInt();
		switchs = createEmptySwitchs(length);
		for (int i = 0; i < length; ++i) {
			switchs[i] = SL.readString(in);
		}
		in.close();
	}
}

class SwitchSelecter extends OKCancelDialog {

	private final static int GROUP_COUNT = XUtil.getDefPropInt("SwitchGroupLength");

	private String[] switchs;
	private JList groupList;
	private DefaultListModel groupModel;
	private JList switchList;
	private DefaultListModel switchModel;
	private JTextField switchText;
	private int selectedSwitch;

	public SwitchSelecter(JDialog owner, int switchID) {
		super(owner);
		setTitle("选择开关量");

		iniSwitchs();

		groupModel = new DefaultListModel();
		groupList = new JList(groupModel);
		groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groupList.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				groupSelectionChanged();
			}
		});
		JScrollPane groupScroll = new JScrollPane(groupList);

		JButton buttonAddGroup = new JButton("添加一组");
		buttonAddGroup.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				addAGroup();
			}
		});

		JPanel groupPanel = new JPanel();
		groupPanel.setLayout(new BorderLayout());
		groupPanel.add(groupScroll, BorderLayout.CENTER);
		groupPanel.add(buttonAddGroup, BorderLayout.SOUTH);

		switchModel = new DefaultListModel();
		switchList = new JList(switchModel);
		switchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		switchList.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				switchSelectionChanged();
			}
		});
		switchList.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				switchListMouseClicked(e);
			}
		});
		JScrollPane switchScroll = new JScrollPane(switchList);

		switchText = new JTextField();
		switchText.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent e) {
				switchTextChanged();
			}

			public void insertUpdate(DocumentEvent e) {
				switchTextChanged();
			}

			public void removeUpdate(DocumentEvent e) {
				switchTextChanged();
			}
		});
		switchText.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				okPerformed();
			}
		});

		JPanel switchPanel = new JPanel();
		switchPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.weightx = 1;
		c.weighty = 1;
		c.gridy = 0;
		switchPanel.add(switchScroll, c);

		switchText.setPreferredSize(new Dimension(25, 25));
		c.weighty = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 2, 0, 0);
		switchPanel.add(switchText, c);

		iniGroupList();
		setSelectedSwitch(switchID);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, groupPanel, switchPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		Container cp = this.getContentPane();
		cp.add(splitPane, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void iniSwitchs() {
		String[] tmp = MainFrame.self.getSwitchManager().getSwitchs();
		int length;
		if (tmp == null) {
			length = GROUP_COUNT;
		}
		else {
			if (tmp.length % GROUP_COUNT != 0) {
				length = ((int) (tmp.length / GROUP_COUNT) + 1) * GROUP_COUNT;
			}
			else {
				length = tmp.length;
			}
		}
		int oldLength;
		if (tmp == null) {
			oldLength = 0;
		}
		else {
			oldLength = tmp.length;
		}
		switchs = new String[length];
		for (int i = 0; i < oldLength; ++i) {
			switchs[i] = tmp[i];
		}
		for (int i = oldLength; i < length; ++i) {
			switchs[i] = "";
		}
	}

	private void iniGroupList() {
		int length = (int) (switchs.length / GROUP_COUNT);
		for (int i = 0; i < length; ++i) {
			groupModel.addElement(getGroupListItem(i * GROUP_COUNT, (i + 1) * GROUP_COUNT - 1));
		}
	}

	private String getGroupListItem(int start, int end) {
		int switchIDStringLength = XUtil.getDefPropInt("SwitchIDStringLength");
		return "[  " + XUtil.getNumberString(start, switchIDStringLength) + "  －  "
		        + XUtil.getNumberString(end, switchIDStringLength) + "  ]";
	}

	private void addAGroup() {
		String[] tmp = switchs;
		switchs = new String[tmp.length + GROUP_COUNT];
		for (int i = 0; i < tmp.length; ++i) {
			switchs[i] = tmp[i];
		}
		for (int i = tmp.length; i < switchs.length; ++i) {
			switchs[i] = "";
		}
		groupModel.addElement(getGroupListItem(tmp.length, switchs.length - 1));
	}

	private void setSelectedSwitch(int switchID) {
		if (switchID < 0 || switchID >= switchs.length) {
			switchList.clearSelection();
			groupList.clearSelection();
		}
		else {
			groupList.setSelectedValue(groupModel.get((int) (switchID / GROUP_COUNT)), true);
			switchList.setSelectedValue(switchModel.get((int) (switchID % GROUP_COUNT)), true);
		}
	}

	private void groupSelectionChanged() {
		switchList.clearSelection();
		switchModel.clear();
		int groupID = groupList.getSelectedIndex();
		if (groupID >= 0 && groupID < (int) (switchs.length / GROUP_COUNT)) {
			for (int i = groupID * GROUP_COUNT; i < (groupID + 1) * GROUP_COUNT; ++i) {
				switchModel.addElement(getSwitchListItem(i));
			}
		}
	}

	private String getSwitchListItem(int switchIndex) {
		return Event.getSwitchDescription(switchIndex, switchs);
	}

	private void switchSelectionChanged() {
		selectedSwitch = -1;
		switchText.setText(null);
		int groupID = groupList.getSelectedIndex();
		int switchID = switchList.getSelectedIndex();
		if (groupID >= 0 && groupID < (int) (switchs.length / GROUP_COUNT)) {
			if (switchID >= 0 && switchID < GROUP_COUNT) {
				selectedSwitch = groupID * GROUP_COUNT + switchID;
				switchText.setText(switchs[selectedSwitch]);
			}
		}
	}

	private void switchTextChanged() {
		if (selectedSwitch >= 0 && selectedSwitch < switchs.length) {
			switchs[selectedSwitch] = switchText.getText();
			switchModel.set((int) (selectedSwitch % GROUP_COUNT), getSwitchListItem(selectedSwitch));
		}
	}

	public int getSelectedSwitchID() {
		return selectedSwitch;
	}

	public void switchListMouseClicked(MouseEvent e) {
		if (e.getButton() == XUtil.LEFT_BUTTON && e.getClickCount() == 2) {
			okPerformed();
		}
	}

	public void okPerformed() {
		if (selectedSwitch >= 0 && selectedSwitch < switchs.length) {
			closeType = OK_PERFORMED;
			MainFrame.self.getSwitchManager().setSwitchs(switchs);
			dispose();
		}
		else {
			JOptionPane.showMessageDialog(this, "必须选择一个开关量", "错误", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void cancelPerformed() {
		dispose();
	}
}

class SwitchSetPanel extends JPanel {

	private ButtonText switchText;
	private JRadioButton radioOn;
	private JRadioButton radioOff;
	private int switchID;
	private JDialog owner;

	public SwitchSetPanel(JDialog owner, int switchID, int switchState) {
		super();
		this.owner = owner;
		this.switchID = switchID;		

		switchText = new ButtonText(Event.getSwitchDescription(switchID));
		switchText.setActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				selectSwitch();
			}
		});

		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new GridLayout(1, 2));
		ButtonGroup radioGroup = new ButtonGroup();
		radioOn = new JRadioButton("On");
		radioOff = new JRadioButton("Off");
		radioGroup.add(radioOn);
		radioGroup.add(radioOff);
		radioPanel.add(radioOn);
		radioPanel.add(radioOff);
		if (switchState == 0) {
			radioOff.setSelected(true);
		}
		else {
			radioOn.setSelected(true);
		}

		this.setLayout(new GridLayout(2, 1));
		this.add(switchText);
		this.add(radioPanel);
	}

	public int getSwitchID() {
		return switchID;
	}

	public void setSwitchID(int switchID) {
		this.switchID = switchID;
		switchText.setValue(Event.getSwitchDescription(switchID));
	}

	public int getSwitchState() {
		return (radioOff.isSelected() ? 0 : 1);
	}

	private void selectSwitch() {
		SwitchSelecter selecter = new SwitchSelecter(owner, switchID);
		selecter.show();
		if (selecter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			setSwitchID(selecter.getSelectedSwitchID());
		}
	}
}

class SwitchTriggerSetter extends TriggerSetter {

	private SwitchSetPanel switchSetPanel;

	public SwitchTriggerSetter(JDialog owner, Trigger trigger) {
		super(owner, trigger);
		setTitle("设置开关量");

		int switchState = 1;
		int[] data = trigger.getData();
		if (data != null) {
			if (data.length > 0) {
				switchState = data[0];
			}
		}
		switchSetPanel = new SwitchSetPanel(this, trigger.getTargetID(), switchState);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(switchSetPanel, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void okPerformed() {
		try {
			int switchID = switchSetPanel.getSwitchID();
			String[] switchs = MainFrame.self.getSwitchManager().getSwitchs();
			if (switchID < 0 || switchID >= switchs.length) { throw new Exception("必须选择一个正确的开关量！"); }
			trigger.setTargetID(switchID);
			trigger.setData(new int[] { switchSetPanel.getSwitchState() });
			closeType = OK_PERFORMED;
			dispose();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, e, "逻辑错误", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void cancelPerformed() {
		dispose();
	}

}

class SwitchChange extends Operation {

	private int switchID;
	private int switchState;

	protected SwitchChange(int id) {
		super(id, SWITCH_CHANGE);
		init();
	}

	protected SwitchChange(int id, int type) {
		super(id, type);
		init();
	}

	private void init() {
		switchID = -1;
		switchState = 1;
	}

	public static SwitchChange getInstance(int id) {
		return new SwitchChange(id);
	}

	public int getSwitchID() {
		return switchID;
	}

	public void setSwitchID(int switchID) {
		this.switchID = switchID;
	}

	public int getSwitchState() {
		return switchState;
	}

	public void setSwitchState(int switchState) {
		this.switchState = switchState;
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		super.saveMobile(out);
		SL.writeInt(switchID, out);
		SL.writeInt(switchState, out);
	}

	public void save(DataOutputStream out) throws Exception {
		super.save(out);
		out.writeInt(switchID);
		out.writeInt(switchState);
	}

	protected void load(DataInputStream in) throws Exception {
		super.load(in);
		init();

		switchID = in.readInt();
		switchState = in.readInt();
	}

	public String getListItemDescription() {
		String result = "设置" + Event.getSwitchDescription(switchID) + "为" + (switchState == 0 ? "Off" : "On");
		return result;
	}

	public Operation copyOperation() {
		return copySwitchChange();
	}
	
	public final SwitchChange copySwitchChange() {
		SwitchChange result = new SwitchChange(this.id, this.type);
		result.switchID = this.switchID;
		result.switchState = this.switchState;
		return result;
	}
}

class SwitchChangeSetter extends OperationSetter {

	private SwitchChange switchChange;
	private SwitchSetPanel switchSetPanel;

	public SwitchChangeSetter(JDialog owner, SwitchChange switchChange) {
		super(owner);
		init(switchChange);
	}

	private void init(SwitchChange switchChange) {
		this.switchChange = switchChange;
		this.setTitle("设置开关量");

		switchSetPanel = new SwitchSetPanel(this, switchChange.getSwitchID(),
		        switchChange.getSwitchState());

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(switchSetPanel, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public Operation getOperation() {
		return switchChange;
	}

	protected void okPerformed() {
		try {
			int switchID = switchSetPanel.getSwitchID();
			String[] switchs = MainFrame.self.getSwitchManager().getSwitchs();
			if (switchID < 0 || switchID >= switchs.length) { throw new Exception("必须选择一个正确的开关量！"); }
			switchChange.setSwitchID(switchID);
			switchChange.setSwitchState(switchSetPanel.getSwitchState());
			closeType = OK_PERFORMED;
			dispose();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, e, "逻辑错误", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void cancelPerformed() {
		dispose();
	}
}
