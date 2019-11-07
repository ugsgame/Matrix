package editor;

//should modify
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Trigger implements Copyable, Saveable {

	public final static int SWITCH = 0;
	public final static int CAMERA_OVER_X = 1;
	public final static int ENEMY_DEAD = 2;
	public final static int CAMERA_FREE = 3;
	public final static int PLAYER_IN_AREA = 4;
	public final static int PLAYER_LB = 5;
	public final static int PLAYER_DC = 6;

	// 按照一定的顺序保存触发器，用以提高手机的执行速度
	public final static int[] ORDER = { SWITCH, CAMERA_FREE, PLAYER_LB, PLAYER_DC, CAMERA_OVER_X, ENEMY_DEAD, PLAYER_IN_AREA };

	public final static int[] TYPES = { SWITCH, ENEMY_DEAD, CAMERA_OVER_X, CAMERA_FREE, PLAYER_IN_AREA, PLAYER_LB, PLAYER_DC };

	public final static String[] TYPE_NAMES = { "某个开关量满足条件", "镜头越过了某个位置", "某个敌人死佐", "摄像机未被霸占",
	        "玩家在某个矩形范围内", "主角是小强", "主角是貂蝉" };

	public final static Trigger[] copyArray(Trigger[] array) {
		Trigger[] result = null;
		if (array != null) {
			result = new Trigger[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyTrigger();
			}
		}
		return result;
	}

	public final static Trigger[] createArrayViaFile(DataInputStream in) throws Exception {
		Trigger[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new Trigger[length];
			for (int i = 0; i < length; ++i) {
				result[i] = Trigger.createViaFile(in);
			}
		}
		return result;
	}

	public final static Trigger createViaFile(DataInputStream in) throws Exception {
		int id = in.readInt();
		int type = in.readInt();
		Trigger result = createInstance(id, type);
		result.load(in);
		return result;
	}

	private static int maxID = 0;

	private int id, type, targetID;
	private int[] data;

	public static Trigger getTriggerViaListItem(XListItem item) {
		if (item == null) { return null; }
		if (item.getValue() == null) { return null; }
		if (!(item.getValue() instanceof Pair)) { return null; }
		Object result = ((Pair) item.getValue()).first;
		if (!(result instanceof Trigger)) { return null; }
		return ((Trigger) result);
	}

	protected Trigger(int id, int type, int targetID, int[] data) {
		this.id = id;
		this.type = type;
		this.targetID = targetID;
		this.data = data;
	}

	public Copyable copy() {
		return copyTrigger();
	}

	public Trigger copyTrigger() {
		Trigger result = Trigger.createInstance(this.id, this.type);
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(Trigger source) {
		this.id = source.id;
		this.type = source.type;
		this.targetID = source.targetID;
		this.data = XUtil.copyArray(source.data);
	}

	public static Trigger createInstance(int type) {
		return createInstance(maxID++, type);
	}

	public static Trigger createInstance(int id, int type) {
		Trigger result = new Trigger(id, type, -1, null);
		if (result != null) {
			if (maxID <= id) {
				maxID = id + 1;
			}
		}
		return result;
	}

	public int getID() {
		return id;
	}

	public int getType() {
		return type;
	}

	public int getTargetID() {
		return targetID;
	}

	public int[] getData() {
		return data;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setTargetID(int targetID) {
		this.targetID = targetID;
	}

	public void setData(int[] data) {
		this.data = data;
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		// SL.writeInt(id, out);
		SL.writeInt(type, out);
		MapInfo info = MainFrame.self.getMapInfo();
		switch (type) {
		case SWITCH:
			SL.writeInt(targetID, out);
			SL.writeInt(data[0], out);
			break;
		case CAMERA_OVER_X:
			SL.writeInt(targetID + info.getRealLeft(), out);
			break;
		case ENEMY_DEAD:
			SL.writeInt(targetID, out);
			break;
		case CAMERA_FREE:// 这个触发条件没有后继数据
		case PLAYER_LB:
		case PLAYER_DC:
			break;
		case PLAYER_IN_AREA:
			SL.writeInt(data[0] + info.getRealLeft(), out);
			SL.writeInt(data[1] + info.getRealTop(), out);
			SL.writeInt(data[2], out);
			SL.writeInt(data[3], out);
			break;
		}
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(id);
		out.writeInt(type);
		out.writeInt(targetID);
		SL.writeArray(data, out);
	}

	protected void load(DataInputStream in) throws Exception {
		this.targetID = in.readInt();
		data = SL.readIntArray(in);
	}

	public String getListItemDescription() {
		switch (type) {
		case SWITCH:
			return getSwitchDescription();
		case CAMERA_OVER_X:
			return getCameraOverXDescription();
		case ENEMY_DEAD:
			return getEnemyDeadDescription();
		case CAMERA_FREE:
		case PLAYER_LB:
		case PLAYER_DC:
			return TYPE_NAMES[type];
		case PLAYER_IN_AREA:
			return getPlayerInAreaDescription();
		}
		return "";
	}

	private String getSwitchDescription() {
		String result = Event.getSwitchDescription(targetID) + "为";
		if (data == null) {
			result = result + "On";
		}
		else {
			if (data.length < 1) {
				result = result + "On";
			}
			else {
				if (data[0] == 0) {
					result = result + "Off";
				}
				else {
					result = result + "On";
				}
			}
		}
		return result;
	}

	private String getCameraOverXDescription() {
		return "镜头越过【" + targetID + "】";
	}

	private String getEnemyDeadDescription() {
		return MainFrame.self.getEnemyName(targetID) + "死佐";
	}
	
	private String getPlayerInAreaDescription() {
		return "玩家位于" + Event.getAreaDescription(data);
	}
}
