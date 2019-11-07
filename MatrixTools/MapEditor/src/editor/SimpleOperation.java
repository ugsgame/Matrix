package editor;

//should modify
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SimpleOperation extends Operation {

	public SimpleOperation(int id, int type) {
		super(id, type);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		super.saveMobile(out);
	}

	public void save(DataOutputStream out) throws Exception {
		super.save(out);
	}

	protected void load(DataInputStream in) throws Exception {
		super.load(in);
	}

	public String getListItemDescription() {
		return Operation.TYPE_NAMES[getType()];
	}

	public Operation copyOperation() {
		return copySimpleOperation();
	}
	
	public final SimpleOperation copySimpleOperation() {
		SimpleOperation result = new SimpleOperation(this.id, this.type);
		return result;
	}
}