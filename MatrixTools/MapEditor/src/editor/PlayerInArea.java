package editor;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JDialog;

class PlayerInAreaSetter extends TriggerSetter {

	private Trigger trigger;
	private MapAreaPanel panel;

	public PlayerInAreaSetter(JDialog owner, Trigger trigger) {
		super(owner, trigger);
		setTitle("设置玩家原点所在矩形的数据");
		
		this.trigger = trigger;
		panel = new MapAreaPanel(this, new IntPair(5, 5));
		int[] data = trigger.getData();
		int left = 0;
		int top = 0;
		int width = 20;
		int height = 10;
		if(data != null) {
			if(data.length >= 1) left = data[0];
			if(data.length >= 2) top = data[1];
			if(data.length >= 3) width = data[2];
			if(data.length >= 4) height = data[3];
		}
		panel.setArea(left, top, width, height);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(panel, BorderLayout.CENTER);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void okPerformed() {
		int[] data = new int[4];
		data[0] = panel.getAreaLeft();
		data[1] = panel.getAreaTop();
		data[2] = panel.getAreaWidth();
		data[3] = panel.getAreaHeight();
		trigger.setData(data);
		this.closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}
}