package editor;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JDialog;

class CameraOverXSetter extends TriggerSetter {

	private CameraPosPanel posPanel;

	public CameraOverXSetter(JDialog owner, Trigger trigger) {
		super(owner, trigger);
		setTitle("设置镜头越过某个位置触发条件");

		posPanel = new CameraPosPanel(owner, "镜头X坐标：", null);
		posPanel.setPos(trigger.getTargetID(), 0);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(posPanel, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void okPerformed() {
		trigger.setTargetID(posPanel.getPosX());
		closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}

}