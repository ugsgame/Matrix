package editor;

import java.util.ArrayList;

import javax.swing.JDialog;

public class TriggerList extends XList {

	public static ArrayList getTriggerListItems(Trigger[] triggers) {
		ArrayList result = new ArrayList();
		if (triggers != null) {
			for (int i = 0; i < triggers.length; ++i) {
				if (triggers[i] != null) {
					Trigger trigger = triggers[i].copyTrigger();
					result.add(new XListItem(new Pair(trigger, trigger.getListItemDescription())));
				}
			}
		}
		return result;
	}

	public TriggerList(JDialog owner) {
		super(owner);
	}

	public TriggerList(JDialog owner, ArrayList triggerItems) {
		super(owner, triggerItems);
	}

	public TriggerList(JDialog owner, Trigger[] triggers) {
		super(owner, getTriggerListItems(triggers));
	}

	protected XListItem insert(XListItem item) {
		TriggerCreator creator = new TriggerCreator(dialogOwner);
		creator.show();
		if (creator.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			Trigger trigger = creator.getTrigger();
			if (trigger == null) { return item; }
			XListItem triggerItem = new XListItem(new Pair(trigger,
			        trigger.getListItemDescription()));
			insertItem(item, triggerItem, null);
			return triggerItem;
		}
		else {
			return item;
		}
	}

	protected XListItem modify(XListItem item) {
		Trigger trigger = Trigger.getTriggerViaListItem(item);
		if (trigger == null) return item;
		TriggerSetter setter = TriggerSetter.createSetter(dialogOwner, trigger);
		if (setter == null) { return item; }
		setter.show();
		if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			trigger = setter.getTrigger();
			XListItem triggerItem = new XListItem(new Pair(trigger,
			        trigger.getListItemDescription()));
			replaceItem(item, triggerItem, null);
			return triggerItem;
		}
		else {
			return item;
		}
	}

	public Trigger[] getTriggers() {
		ArrayList tmp = new ArrayList();
		Object[] items = model.toArray();
		if (items == null) { return null; }
		for (int i = 0; i < items.length; ++i) {
			Object item = items[i];
			if (item == null) {
				continue;
			}
			if (!(item instanceof XListItem)) {
				continue;
			}
			XListItem listItem = (XListItem) item;
			if (listItem.getParent() != null) {
				continue;
			}
			Trigger trigger = Trigger.getTriggerViaListItem(listItem);
			if (trigger != null) {
				tmp.add(trigger);
			}
		}
		if (tmp.size() <= 0) { return null; }
		Trigger[] result = new Trigger[tmp.size()];
		for (int i = 0; i < tmp.size(); ++i) {
			result[i] = (Trigger) (tmp.get(i));
		}
		return result;
	}
}