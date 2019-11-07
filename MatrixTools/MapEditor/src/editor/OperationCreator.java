package editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class OperationCreator extends OKCancelDialog {

	private Operation operation;
	private int[][] types;

	public OperationCreator(JDialog owner) {
		super(owner);
		init();
	}

	private void init() {
		operation = null;
		setTitle("��������");

		types = Operation.TYPES;

		ActionListener createListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				createOperation(e);
			}
		};

		int gridRows = 1, gridCols = 1, maxOperationCount = 1;
		for (int i = 0; i < types.length; ++i) {
			if (maxOperationCount < types[i].length) {
				maxOperationCount = types[i].length;
			}
		}

		if (maxOperationCount > XUtil.getDefPropInt("operationCreatorSingleRowMaxCount")) {
			gridCols = 2;
			gridRows = (maxOperationCount + 1) / 2;
		}
		else {
			gridCols = 1;
			gridRows = maxOperationCount;
		}

		JTabbedPane operationTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		for (int i = 0; i < types.length; ++i) {
			JPanel operationPanel = new JPanel();
			operationPanel.setLayout(new GridLayout(gridRows, gridCols));
			operationPanel.setName(Operation.KIND_NAMES[i]);
			for (int j = 0; j < types[i].length; ++j) {
				int type = types[i][j];
				JButton bt = new JButton(Operation.TYPE_NAMES[type]);
				bt.setActionCommand(type + "");
				bt.addActionListener(createListener);
				operationPanel.add(bt);
			}
			operationTabbedPane.add(operationPanel);
		}

		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		buttonPanel.add(new JLabel(), c);
		c.gridx = 1;
		c.weightx = 0;
		buttonPanel.add(cancelButton, c);

		Container cp = this.getContentPane();
		cp.add(operationTabbedPane, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public Operation getOperation() {
		return operation;
	}

	private void createOperation(ActionEvent e) {
		try {
			int type = Integer.parseInt(e.getActionCommand());
			boolean hasType = false;
			for (int i = 0; i < types.length; ++i) {
				for (int j = 0; j < types[i].length; ++j) {
					if (type == types[i][j]) {
						hasType = true;
						break;
					}
				}
			}
			if (!hasType) { throw new Exception("�޷��ҵ���Ӧ�� " + type + " �Ĳ������͡�"); }

			operation = Operation.createInstance(type);
			if (operation instanceof SimpleOperation) {
				okPerformed();
			}
			else {
				OperationSetter setter = OperationSetter.createSetter(this, operation);
				if (setter == null) { throw new Exception("�޷������������ô��ڡ�"); }
				setter.show();
				if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
					operation = setter.getOperation();
					okPerformed();
				}
			}
		}
		catch (Exception err) {
			JOptionPane.showMessageDialog(this, err, "�߼�����", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void okPerformed() {
		try {
			if (operation == null) { throw new Exception("�ղ�����"); }
			closeType = OK_PERFORMED;
			dispose();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, e, "�߼�����", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void cancelPerformed() {
		dispose();
	}
}