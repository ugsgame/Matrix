package editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

class MoveCameraSetter extends OperationSetter {

	private MoveCamera mc;
	private CameraPosPanel posPanel;
	private NumberSpinner speedSpinner;

	public MoveCameraSetter(JDialog owner, MoveCamera mc) {
		super(owner);
		setTitle("设置摇镜头的数据");
		
		this.mc = mc;
		posPanel = new CameraPosPanel(this, "镜头终点的X坐标：", null);
		posPanel.setPos(mc.getX(), 0);
		
		speedSpinner = new NumberSpinner();
		speedSpinner.setIntValue(mc.getSpeed());
		speedSpinner.setPreferredSize(new Dimension(100, 10));
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(new JLabel("速度："), BorderLayout.WEST);
		p.add(speedSpinner, BorderLayout.CENTER);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(p, BorderLayout.WEST);
		cp.add(posPanel, BorderLayout.CENTER);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public Operation getOperation() {
		return mc;
	}

	public void okPerformed() {
		mc.setX(posPanel.getPosX());
		mc.setSpeed(speedSpinner.getIntValue());
		this.closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}
}

class MoveCamera extends Operation {

	private int x;
	private int speed;
	
	public MoveCamera(int id) {
		super(id, MOVE_CAMERA);
		init();
	}

	protected MoveCamera(int id, int type) {
		super(id, type);
		init();
	}

	private void init() {
		x = 0;
		speed = 10;
	}

	public Operation copyOperation() {
		return copyMoveCamera();
	}

	public final MoveCamera copyMoveCamera() {
		MoveCamera result = new MoveCamera(this.id);
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(MoveCamera source) {
		this.id = source.id;
		this.x = source.x;
		this.speed = source.speed;
	}

	public String getListItemDescription() {
		return "以速度" + speed + "摇镜头到" + x;
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		super.saveMobile(out);
		MapInfo info = MainFrame.self.getMapInfo();
		SL.writeInt(x + info.getRealLeft(), out);
		SL.writeInt(speed, out);
	}

	public void save(DataOutputStream out) throws Exception {
		super.save(out);
		out.writeInt(x);
		out.writeInt(speed);
	}

	protected void load(DataInputStream in) throws Exception {
		super.load(in);
		x = in.readInt();
		speed = in.readInt();
	}
	
    public int getX() {
    	return this.x;
    }

	
    public void setX(int x) {
    	this.x = x;
    }

	
    public int getSpeed() {
    	return this.speed;
    }

	
    public void setSpeed(int speed) {
    	this.speed = speed;
    }

}