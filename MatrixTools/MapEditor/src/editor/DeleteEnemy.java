package editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JDialog;
import javax.swing.JLabel;

class DeleteEnemySetter extends OperationSetter {

	private DeleteEnemy deleteEnemy;
	private EnemyChoosePanel panel;

	public DeleteEnemySetter(JDialog owner, DeleteEnemy deleteEnemy) {
		super(owner);
		setTitle("设置延迟的数据");
		
		this.deleteEnemy = deleteEnemy;
		
		panel = new EnemyChoosePanel(owner);
		panel.setSelectedEnemyID(deleteEnemy.getEnemyID());

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(new JLabel("选择要删掉的敌人："), BorderLayout.WEST);
		cp.add(panel, BorderLayout.CENTER);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public Operation getOperation() {
		return deleteEnemy;
	}

	public void okPerformed() {
		deleteEnemy.setEnemyID(panel.getSelectedEnemyID());
		this.closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}
}

class DeleteEnemy extends Operation {

	private int enemyID;
	
	public DeleteEnemy(int id) {
		super(id, DELETE_ENEMY);
		init();
	}

	protected DeleteEnemy(int id, int type) {
		super(id, type);
		init();
	}

	private void init() {
		enemyID = 10;
	}

	public Operation copyOperation() {
		return copyDeleteEnemy();
	}

	public final DeleteEnemy copyDeleteEnemy() {
		DeleteEnemy result = new DeleteEnemy(this.id);
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(DeleteEnemy source) {
		this.id = source.id;
		this.enemyID = source.enemyID;
	}

	public String getListItemDescription() {
		return "删除" + MainFrame.self.getEnemyName(enemyID);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		super.saveMobile(out);
		SL.writeInt(enemyID, out);
	}

	public void save(DataOutputStream out) throws Exception {
		super.save(out);
		out.writeInt(enemyID);
	}

	protected void load(DataInputStream in) throws Exception {
		super.load(in);
		enemyID = in.readInt();
	}
	
    public int getEnemyID() {
    	return this.enemyID;
    }

	
    public void setEnemyID(int enemyID) {
    	this.enemyID = enemyID;
    }

}