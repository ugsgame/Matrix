package editor;

//should modify
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * 操作的设置窗口。
 */
public abstract class OperationSetter extends OKCancelDialog {

	public OperationSetter(JDialog owner) {
		super(owner);
		init();
	}

	public OperationSetter(JFrame owner) {
		super(owner);
		init();
	}

	private void init() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void show() {
		if (getOperation() == null) { return; }
		super.show();
	}

	public abstract Operation getOperation();

	public static OperationSetter createSetter(JDialog owner, Operation operation) {
		if (operation == null) { return null; }
		OperationSetter result = null;
		switch (operation.getType()) {
		case Operation.SWITCH_CHANGE:
			result = new SwitchChangeSetter(owner, (SwitchChange) operation);
			break;
		case Operation.DIALOG:
			result = new EventDialogSetter(owner, (EventDialog) operation);
			break;
		case Operation.LOCK_CAMERA:
			result = new LockCameraSetter(owner, (LockCamera) operation);
			break;
		case Operation.DELAY:
			result = new DelaySetter(owner, (Delay) operation);
			break;
		case Operation.MOVE_CAMERA:
			result = new MoveCameraSetter(owner, (MoveCamera) operation);
			break;
		case Operation.DELETE_ENEMY:
			result = new DeleteEnemySetter(owner, (DeleteEnemy) operation);
			break;
		default:
			result = null;
			break;
		}
		return result;
	}
}