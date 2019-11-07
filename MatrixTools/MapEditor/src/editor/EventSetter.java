package editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * 设置Event的属性。
 * 
 * @see Event
 */
public class EventSetter extends OKCancelDialog {

	private Event event;
	private TriggerList triggerList;
	private OperationList operationList;
	private JTextField nameText;
	private JTextArea commentText;
	private JButton unitOwnerButton, eventobjectOwnerButton, clearOwnerButton;
	private int ownerType, ownerID;
	private JLabel ownerInfoLabel;
	private int eventType;

	public EventSetter(JDialog owner, Event event) {
		super(owner);
		init(event);
	}

	public EventSetter(JFrame owner, Event event) {
		super(owner);
		init(event);
	}

	private void init(Event event) {
		this.event = event;
		ownerType = event.getOwnerType();
		ownerID = event.getOwnerID();
		setTitle("设置事件");

		nameText = new JTextField(event.getName());
		commentText = new JTextArea(event.getComment());
		commentText.setLineWrap(true);
		ownerInfoLabel = new JLabel("", SwingConstants.CENTER);
		refreshOwnerInfo();
		unitOwnerButton = new JButton("属于Unit");
		unitOwnerButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setUnitOwner();
			};
		});
		eventobjectOwnerButton = new JButton("属于事件物体");
		eventobjectOwnerButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setEventobjectOwner();
			};
		});
		clearOwnerButton = new JButton("不属于任何事物");
		clearOwnerButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				clearOwner();
			};
		});
		
		JPanel headPanel = new JPanel();
		headPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 5, 3, 5);
		c.weighty = 0;
		c.weightx = 0;
		
		c.gridy = 0;
		c.gridx = 0;
		headPanel.add(new JLabel("名称："), c);
		c.gridx = 1;
		c.weightx = 1;
		headPanel.add(nameText, c);
		
		c.weighty = 1;
		c.gridy = 1;
		c.gridx = 0;
		c.weightx = 0;
		headPanel.add(new JLabel("注释："), c);
		c.gridx = 1;
		c.weightx = 1;
		JScrollPane scrollPane = new JScrollPane(commentText);
		scrollPane.setPreferredSize(new Dimension(100, 50));
		headPanel.add(scrollPane, c);

		triggerList = new TriggerList(this, event.getTriggers());
		operationList = new OperationList(this, event.getOperations());

		JPanel triggerPanel = new JPanel();
		triggerPanel.setLayout(new BorderLayout());
		triggerPanel.add(new JLabel("触发条件", JLabel.CENTER), BorderLayout.NORTH);
		triggerPanel.add(new JScrollPane(triggerList));

		JPanel operationPanel = new JPanel();
		operationPanel.setLayout(new BorderLayout());
		operationPanel.add(new JLabel("执行的操作", JLabel.CENTER), BorderLayout.NORTH);
		operationPanel.add(new JScrollPane(operationList));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, triggerPanel,
		        operationPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(300);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		Container cp = this.getContentPane();
		cp.add(headPanel, BorderLayout.NORTH);
		cp.add(splitPane, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public Event getEvent() {
		return event;
	}

	public int getEventType() {
		return eventType;
	}

	private void refreshOwnerInfo() {
		String info = "";
	// switch (ownerType) {
	// case Event.OWNER_Unit:
	// info = "属于" + Event.getUnitDescription(ownerID);
	// break;
	// case Event.OWNER_EVENTOBJECT:
	// info = "属于" + XUtil.getEventobjectDescription(ownerID);
	// break;
	// case Event.OWNER_NONE:
	// info = "不属于任何事物";
	// break;
	// }
		ownerInfoLabel.setText(info);
	}

	private void setUnitOwner() {
	// int unitID = (ownerType == Event.OWNER_Unit) ? ownerID : -1;
	// UnitChooser unitChooser = new UnitChooser(this, mainFrame, unitID, -1);
	// unitChooser.show();
	// if (unitChooser.getCloseType() == OKCancelDialog.OK_PERFORMED) {
	// ownerType = Event.OWNER_Unit;
	// ownerID = unitChooser.getUnitID();
	// refreshOwnerInfo();
	// }
	}

	private void setEventobjectOwner() {
	// int eventobjectID = (ownerType == Event.OWNER_EVENTOBJECT) ? ownerID :
	// -1;
	// EventobjectChooser eventobjectChooser = new EventobjectChooser(this,
	// mainFrame, eventobjectID, -1);
	// eventobjectChooser.show();
	// if (eventobjectChooser.getCloseType() == OKCancelDialog.OK_PERFORMED) {
	// ownerType = Event.OWNER_EVENTOBJECT;
	// ownerID = eventobjectChooser.getEventobjectID();
	// refreshOwnerInfo();
	// }
	}

	private void clearOwner() {
		ownerType = Event.OWNER_NONE;
		ownerID = -1;
		refreshOwnerInfo();
	}

	protected void okPerformed() {
		String name = nameText.getText().trim();
		if (name.equals("")) {
			JOptionPane.showMessageDialog(this, "名称不允许为空", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		event.setName(name);
		event.setComment(commentText.getText());
		event.setOwnerType(ownerType);
		event.setOwnerID(ownerID);
		event.setTriggers(triggerList.getTriggers());
		event.setOperations(operationList.getOperations());
		closeType = OK_PERFORMED;
		dispose();
	}

	protected void cancelPerformed() {
		dispose();
	}
}