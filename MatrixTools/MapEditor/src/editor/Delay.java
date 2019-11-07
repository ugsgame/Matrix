package editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JDialog;
import javax.swing.JLabel;

class DelaySetter extends OperationSetter {

	private Delay delay;
	private NumberSpinner intervalSpinner;

	public DelaySetter(JDialog owner, Delay delay) {
		super(owner);
		setTitle("设置延迟的数据");
		
		this.delay = delay;
		
		intervalSpinner = new NumberSpinner();
		intervalSpinner.setIntValue(delay.getInterval());

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(new JLabel("时长（毫秒）："), BorderLayout.WEST);
		cp.add(intervalSpinner, BorderLayout.CENTER);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public Operation getOperation() {
		return delay;
	}

	public void okPerformed() {
		delay.setInterval(intervalSpinner.getIntValue());
		this.closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}
}

class Delay extends Operation {

	private int interval;
	
	public Delay(int id) {
		super(id, DELAY);
		init();
	}

	protected Delay(int id, int type) {
		super(id, type);
		init();
	}

	private void init() {
		interval = 10;
	}

	public Operation copyOperation() {
		return copyDelay();
	}

	public final Delay copyDelay() {
		Delay result = new Delay(this.id);
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(Delay source) {
		this.id = source.id;
		this.interval = source.interval;
	}

	public String getListItemDescription() {
		return "等待" + interval + "毫秒";
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		super.saveMobile(out);
		SL.writeInt(interval, out);
	}

	public void save(DataOutputStream out) throws Exception {
		super.save(out);
		out.writeInt(interval);
	}

	protected void load(DataInputStream in) throws Exception {
		super.load(in);
		interval = in.readInt();
	}
	
    public int getInterval() {
    	return this.interval;
    }

	
    public void setInterval(int interval) {
    	this.interval = interval;
    }

}