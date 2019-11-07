package editor;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * 事件。 id： 在某个地图内事件之间唯一标志性的ID。不同地图之间可能有相同ID的事件。 ownerType：
 * 所有者类型。包括“无所有者”、“Unit所有”、“事件物体所有” ownerID： 所有者的ID。所有者类型为“无所有者”的时候没有意义。 name：
 * 名称 triggers： 触发器集合。按照Trigger.TYPE_ORDER的顺序来保存之。 operations： 操作集合。手机顺序执行各个操作。
 */

public class Event implements Copyable, Saveable {

	public final static int OWNER_NONE = 0;
	public final static int OWNER_Unit = 1;
	public final static int OWNER_EVENTOBJECT = 2;

	public final static Event[] copyArray(Event[] array) {
		Event[] result = null;
		if (array != null) {
			result = new Event[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyEvent();
			}
		}
		return result;
	}

	public final static Event[] createArrayViaFile(DataInputStream in) throws Exception {
		Event[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new Event[length];
			for (int i = 0; i < length; ++i) {
				result[i] = Event.createViaFile(in);
			}
		}
		return result;
	}

	public final static Event createViaFile(DataInputStream in) throws Exception {
		Event result = new Event();
		if (result != null) {
			result.load(in);
		}
		return result;
	}

	public static Event getEventViaListItem(XListItem item) {
		if (item == null) { return null; }
		if (item.getValue() == null) { return null; }
		if (!(item.getValue() instanceof Pair)) { return null; }
		Object result = ((Pair) item.getValue()).first;
		if (!(result instanceof Event)) { return null; }
		return ((Event) result);
	}

	public static String getAreaDescription(int left, int top, int width, int height) {
		return getAreaDescription(new int[] { left, top, width, height });
	}

	public static String getAreaDescription(int[] data) {
		String result = "区域【";
		if (data != null) {
			if (data.length >= 1) {
				result = result + data[0];
			}
			if (data.length >= 2) {
				result = result + "," + data[1];
			}
			if (data.length >= 3) {
				result = result + "," + data[2];
			}
			if (data.length >= 4) {
				result = result + "," + data[3];
			}
		}
		result = result + "】";
		return result;
	}

	public static String getPointDescription(IntPair p) {
		int[] data = null;
		if (p != null) {
			data = new int[] { p.x, p.y };
		}
		return getPointDescription(data);
	}

	public static String getPointDescription(int[] data) {
		String result = "地点【";
		if (data != null) {
			if (data.length >= 1) {
				result = result + data[0];
			}
			if (data.length >= 2) {
				result = result + "," + data[1];
			}
		}
		result = result + "】";
		return result;
	}

	public static String getRangeDescription(IntPair r) {
		int[] data = null;
		if (r != null) {
			data = new int[] { r.x, r.y };
		}
		return getRangeDescription(data);
	}

	public static String getRangeDescription(int[] data) {
		String result = "范围【";
		if (data != null) {
			if (data.length >= 1) {
				result = result + data[0];
			}
			if (data.length >= 2) {
				result = result + "," + data[1];
			}
		}
		result = result + "】";
		return result;
	}

	public static String getSwitchDescription(int id) {
		String[] switchs = MainFrame.self.getSwitchManager().getSwitchs();
		return "开关量【" + getSwitchDescription(id, switchs) + "】";
	}

	public static String getSwitchDescription(int id, String[] switchs) {
		String result = "无";
		if (switchs != null) {
			if (id >= 0 && id < switchs.length) {
				result = XUtil.getNumberString(id, XUtil.getDefPropInt("SwitchIDStringLength")) + "："
				        + switchs[id];
			}
		}
		return result;
	}

	public static int getColorValue(Color color) {
		return (int) (((color.getRed() & 0xFF) << 16) | ((color.getGreen() & 0xFF) << 8) | (color.getBlue() & 0xFF));
	}

	public static String getColorDescription(Color color) {
		String result = "颜色[0x" + Integer.toHexString(Event.getColorValue(color)).toUpperCase() + "]";
		return result;
	}

	private static int maxID = 0;

	private int id, ownerType, ownerID;
	private String name;
	private String comment;
	private Trigger[] triggers;
	private Operation[] operations;

	public static Event createInstance() {
		return createInstance(maxID++);
	}

	public static Event createInstance(int id) {
		Event result = new Event(id);
		if (result != null) {
			if (maxID <= id) {
				maxID = id + 1;
			}
		}
		return result;
	}

	protected Event() {
		init(-1, null, null);
	}

	protected Event(int id) {
		init(id, null, null);
	}

	protected Event(int id, Trigger[] triggers, Operation[] operations) {
		init(id, triggers, operations);
	}

	private void init(int id, Trigger[] triggers, Operation[] operations) {
		this.id = id;
		this.name = "事件" + id;
		this.comment = "";
		this.triggers = triggers;
		this.operations = operations;
		this.ownerType = OWNER_NONE;
		this.ownerID = -1;
	}

	public int getID() {
		return id;
	}

	public int getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(int ownerType) {
		this.ownerType = ownerType;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public void setName(String name) {
		if (name == null) name = "";
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setComment(String comment) {
		if (comment == null) comment = "";
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setTriggers(Trigger[] triggers) {
		this.triggers = Trigger.copyArray(triggers);
	}

	public Trigger[] getTriggers() {
		return triggers;
	}

	public void setOperations(Operation[] operations) {
		this.operations = Operation.copyArray(operations);
	}

	public Operation[] getOperations() {
		return operations;
	}

	public String getListItemDescription() {
		return name;
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		try {

			// 保存触发器
			if (triggers == null) {
				SL.writeInt(0, out);
			}
			else {
				SL.writeInt(triggers.length, out);
				// 按照Trigger.TYPE_ORDER顺序保存触发器。
				for (int orderIndex = 0; orderIndex < Trigger.ORDER.length; ++orderIndex) {
					for (int triggerIndex = 0; triggerIndex < triggers.length; ++triggerIndex) {
						if (triggers[triggerIndex].getType() == Trigger.ORDER[orderIndex]) {
							triggers[triggerIndex].saveMobile(out);
						}
					}
				}
				// 按照加入时的顺序保存其类型不在Trigger.TYPE_ORDER里面的触发器。
				for (int triggerIndex = 0; triggerIndex < triggers.length; ++triggerIndex) {
					boolean hasSaved = false;
					for (int orderIndex = 0; orderIndex < Trigger.ORDER.length; ++orderIndex) {
						if (triggers[triggerIndex].getType() == Trigger.ORDER[orderIndex]) {
							hasSaved = true;
							break;
						}
					}
					if (!hasSaved) {
						triggers[triggerIndex].saveMobile(out);
					}
				}
			}

			// 保存操作
			SL.saveArrayMobile(operations, out);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("保存事件失败");
		}
	}

	public void save(DataOutputStream out) throws Exception {
		try {
			out.writeInt(id);
			out.writeInt(ownerType);
			out.writeInt(ownerID);
			SL.writeString(name, out);
			SL.writeString(comment, out);

			SL.saveArray(triggers, out);
			SL.saveArray(operations, out);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("保存事件失败");
		}
	}

	protected void load(DataInputStream in) throws Exception {
		init(-1, null, null);
		try {
			id = in.readInt();
			if (maxID <= id) {
				maxID = id + 1;
			}
			ownerType = in.readInt();
			ownerID = in.readInt();
			name = SL.readString(in);
			comment = SL.readString(in);

			triggers = Trigger.createArrayViaFile(in);
			operations = Operation.createArrayViaFile(in);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("读取事件失败");
		}
	}

	public Copyable copy() {
		return copyEvent();
	}

	public final Event copyEvent() {
		Event result = new Event(this.id);
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(Event source) {
		this.id = source.id;
		this.ownerType = source.ownerType;
		this.ownerID = source.ownerID;
		this.name = source.name;
		this.comment = source.comment;
		this.setTriggers(source.triggers);
		this.setOperations(source.operations);
	}
}