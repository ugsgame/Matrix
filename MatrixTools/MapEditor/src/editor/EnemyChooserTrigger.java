package editor;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JDialog;

class EnemyChooserTrigger extends TriggerSetter {

	private EnemyChoosePanel panel;

	public EnemyChooserTrigger(JDialog owner, Trigger trigger) {
		super(owner, trigger);
		setTitle("选择触发条件里面的敌人");

		panel = new EnemyChoosePanel(owner);
		panel.setSelectedEnemyID(trigger.getTargetID());

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(panel, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void okPerformed() {
		trigger.setTargetID(panel.getSelectedEnemyID());
		closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}

}