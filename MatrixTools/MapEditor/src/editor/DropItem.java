package editor;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.table.DefaultTableModel;

class DropItem implements Copyable, Saveable {
	
	public final static String[] ITEM_NAMES = {"元宝", "蓝瓶", "小粒金", "包子", "烧鸡", "眼睛"};

	private int itemIndex;
	private int prob;

	public final static DropItem[] copyArray(DropItem[] array) {
		DropItem[] result = null;
		if (array != null) {
			result = new DropItem[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyDropItem();
			}
		}
		return result;
	}

	public final static DropItem[] createArrayViaFile(DataInputStream in) throws Exception {
		DropItem[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new DropItem[length];
			for (int i = 0; i < length; ++i) {
				result[i] = DropItem.createViaFile(in);
			}
		}
		return result;
	}

	public final static DropItem createViaFile(DataInputStream in) throws Exception {
		DropItem result = new DropItem();
		result.load(in);
		return result;
	}

	public DropItem() {
		itemIndex = 0;
		prob = 0;
	}

	public Copyable copy() {
		return copyDropItem();
	}

	public final DropItem copyDropItem() {
		DropItem result = new DropItem();
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(DropItem source) {
		this.itemIndex = source.itemIndex;
		this.prob = source.prob;
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(itemIndex);
		out.writeInt(prob);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		SL.writeInt(itemIndex, out);
		SL.writeInt(prob, out);
	}

	public void load(DataInputStream in) throws Exception {
		itemIndex = in.readInt();
		prob = in.readInt();
	}

	public int getItemIndex() {
		return this.itemIndex;
	}

	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}

	public int getProb() {
		return this.prob;
	}

	public void setProb(int prob) {
		this.prob = prob;
	}
}

class DropItemTable extends XTable {

	public DropItemTable() {
		super("设置掉落道具的数据", new String[] {"道具", "概率（％）"}, true, false);
		int[] ids = new int[DropItem.ITEM_NAMES.length];
		for(int i = 0; i < ids.length; ++i) {
			ids[i] = i;
		}
		this.setComboCol(0, ids, DropItem.ITEM_NAMES, ValueChooser.NATURE_INDEX);
		this.setNumberCol(1);
	}

	public void setDropItems(DropItem[] dropItems) {
		DefaultTableModel model = this.getModel();
		while(model.getRowCount() > 0) {
			model.removeRow(0);
		}
		if(dropItems != null) {
			for (int row = 0; row < dropItems.length; ++row) {
				DropItem dropItem = dropItems[row];
				Object[] data = new Object[2];
				data[0] = new Integer(dropItem.getItemIndex());
				data[1] = new Integer(dropItem.getProb());
				model.addRow(data);
			}
		}
	}

	public DropItem[] getDropItems() {
		DropItem[] result = null;
		this.stopEditing();
		DefaultTableModel model = this.getModel();
		if (model.getRowCount() > 0) {
			result = new DropItem[model.getRowCount()];
			for (int row = 0; row < model.getRowCount(); ++row) {
				DropItem dropItem = new DropItem();
				dropItem.setItemIndex(((Integer) (model.getValueAt(row, 0))).intValue());
				dropItem.setProb(((Integer) (model.getValueAt(row, 1))).intValue());
				result[row] = dropItem;
			}
		}
		return result;
	}
}