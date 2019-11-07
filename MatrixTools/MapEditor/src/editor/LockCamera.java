package editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JDialog;

class LockCameraSetter extends OperationSetter {

	private LockCamera lc;
	private LockCameraAreaPanel panel;

	public LockCameraSetter(JDialog owner, LockCamera lc) {
		super(owner);
		this.lc = lc;
		panel = new LockCameraAreaPanel(this, "×ó±ßµÄX×ø±ê£º", "ÓÒ±ßµÄX×ø±ê£º");
		panel.setArea(lc.getLeft(), lc.getRight());

		setTitle("ÉèÖÃÉãÏñ»úËø¶¨·¶Î§");

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(panel, BorderLayout.CENTER);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public Operation getOperation() {
		return lc;
	}

	public void okPerformed() {
		lc.setLeft(panel.getAreaLeft());
		lc.setRight(panel.getAreaRight());
		this.closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}
}

class LockCamera extends Operation {

	private int left;
	private int right;

	public LockCamera(int id) {
		super(id, LOCK_CAMERA);
		init();
	}

	protected LockCamera(int id, int type) {
		super(id, type);
		init();
	}

	private void init() {
		left = 0;
		right = MainFrame.SCR_W;
	}

	public Operation copyOperation() {
		return copyLockCamera();
	}

	public final LockCamera copyLockCamera() {
		LockCamera result = new LockCamera(this.id);
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(LockCamera source) {
		this.id = source.id;
		this.left = source.left;
		this.right = source.right;
	}

	public String getListItemDescription() {
		return "Ëø×¡ÉãÏñ»ú¡¾·¶Î§£º" + left + "µ½" + right + "¡¿";
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		super.saveMobile(out);
		MapInfo info = MainFrame.self.getMapInfo();
		SL.writeInt(left + info.getRealLeft(), out);
		SL.writeInt(right + info.getRealLeft(), out);
	}

	public void save(DataOutputStream out) throws Exception {
		super.save(out);
		out.writeInt(left);
		out.writeInt(right);
	}

	protected void load(DataInputStream in) throws Exception {
		super.load(in);
		left = in.readInt();
		right = in.readInt();
	}

	public int getLeft() {
		return this.left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return this.right;
	}

	public void setRight(int right) {
		this.right = right;
	}
}