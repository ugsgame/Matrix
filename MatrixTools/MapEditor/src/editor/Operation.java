package editor;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class Operation implements Copyable, Saveable {

	public final static int SWITCH_CHANGE = 0;
	public final static int DIALOG = 1;
	public final static int USE_CAMERA = 2;
	public final static int FREE_CAMERA = 3;
	public final static int LOCK_CAMERA = 4;
	public final static int UNLOCK_CAMERA = 5;
	public final static int LOCK_PLAYER_KEY = 6;
	public final static int UNLOCK_PLAYER_KEY = 7;
	public final static int DELAY = 8;
	public final static int MOVE_CAMERA = 9;
	public final static int CAMERA_RETURN_PLAYER = 10;
	public final static int DONGZHUO_FRENZY = 11;
	public final static int CAOCAO_FRENZY = 12;
	public final static int PASS_STAGE = 13;
	public final static int DELETE_ENEMY = 14;

	public final static int[][] TYPES = {
	        { SWITCH_CHANGE, DIALOG, LOCK_PLAYER_KEY, UNLOCK_PLAYER_KEY, DELAY, DONGZHUO_FRENZY,
	                CAOCAO_FRENZY, PASS_STAGE, DELETE_ENEMY },
	        { LOCK_CAMERA, UNLOCK_CAMERA, USE_CAMERA, FREE_CAMERA, MOVE_CAMERA,
	                CAMERA_RETURN_PLAYER } };

	public final static String[] KIND_NAMES = { "基本", "镜头" };

	public final static String[] TYPE_NAMES = { "设置开关量", "普通对话框", "霸占摄像机", "释放摄像机", "锁定摄像机",
	        "摄像机解锁", "剥夺玩家操作权", "归还玩家操作权", "延迟", "摇镜头", "镜头回到玩家", "董卓狂暴", "曹操狂暴", "过关", "删除敌人" };

	public final static Operation[] copyArray(Operation[] array) {
		Operation[] result = null;
		if (array != null) {
			result = new Operation[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyOperation();
			}
		}
		return result;
	}

	public final static Operation[] createArrayViaFile(DataInputStream in) throws Exception {
		Operation[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new Operation[length];
			for (int i = 0; i < length; ++i) {
				result[i] = Operation.createViaFile(in);
			}
		}
		return result;
	}

	public final static Operation createViaFile(DataInputStream in) throws Exception {
		int id = in.readInt();
		int type = in.readInt();
		Operation result = createInstance(id, type);
		result.load(in);
		return result;
	}

	private static int maxID = 0;

	protected int type, id;

	protected Operation(int id, int type) {
		this.type = type;
		this.id = id;
	}

	public static Operation createInstance(int type) {
		return createInstance(maxID++, type);
	}

	public static Operation createInstance(int id, int type) {
		Operation result = null;
		switch (type) {
		case SWITCH_CHANGE:
			result = new SwitchChange(id);
			break;
		case DIALOG:
			result = new EventDialog(id);
			break;
		case LOCK_CAMERA:
			result = new LockCamera(id);
			break;
		case DELAY:
			result = new Delay(id);
			break;
		case MOVE_CAMERA:
			result = new MoveCamera(id);
			break;
		case DELETE_ENEMY:
			result = new DeleteEnemy(id);
			break;
		case UNLOCK_CAMERA:
		case USE_CAMERA:
		case FREE_CAMERA:
		case LOCK_PLAYER_KEY:
		case UNLOCK_PLAYER_KEY:
		case CAMERA_RETURN_PLAYER:
		case DONGZHUO_FRENZY:
		case CAOCAO_FRENZY:
		case PASS_STAGE:
			result = new SimpleOperation(id, type);
			break;
		}
		if (result != null) {
			if (maxID <= id) {
				maxID = id + 1;
			}
		}
		return result;
	}

	public static Operation getOperationViaListItem(XListItem item) {
		if (item == null) { return null; }
		if (item.getValue() == null) { return null; }
		if (!(item.getValue() instanceof Pair)) { return null; }
		Object result = ((Pair) item.getValue()).first;
		if (!(result instanceof Operation)) { return null; }
		return ((Operation) result);
	}

	public int getType() {
		return type;
	}

	public int getID() {
		return id;
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(id);
		out.writeInt(type);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		// SL.writeInt(id, out);
		SL.writeInt(type, out);
	}

	protected void load(DataInputStream in) throws Exception {}

	public abstract String getListItemDescription();

	public Copyable copy() {
		return copyOperation();
	}

	public abstract Operation copyOperation();
}