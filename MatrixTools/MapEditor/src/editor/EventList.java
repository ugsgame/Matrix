package editor;

import java.util.ArrayList;

import javax.swing.JDialog;

public class EventList extends XList {

	public static ArrayList getEventListItems(Event[] events) {
		ArrayList result = new ArrayList();
		if (events != null) {
			for (int i = 0; i < events.length; ++i) {
				if (events[i] != null) {
					Event event = events[i].copyEvent();
					result.add(new XListItem(new Pair(event, event.getListItemDescription())));
				}
			}
		}
		return result;
	}

	private int eventType;

	public EventList(JDialog owner) {
		super(owner);
	}

	public EventList(JDialog owner, ArrayList eventsItems) {
		super(owner, eventsItems);
	}

	public EventList(JDialog owner, Event[] events) {
		super(owner, getEventListItems(events));
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	protected Event createDefaultEvent() {
		return Event.createInstance();
	}

	protected XListItem insert(XListItem item) {
		Event event = createDefaultEvent();
		if (event == null) { return item; }

		EventSetter setter = new EventSetter(dialogOwner, event);
		setter.show();
		if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			event = setter.getEvent();
			XListItem eventItem = new XListItem(new Pair(event, event.getListItemDescription()));
			insertItem(item, eventItem, null);
			return eventItem;
		}
		else {
			return item;
		}
	}

	protected XListItem modify(XListItem item) {
		Event event = Event.getEventViaListItem(item);
		if (event == null) { return item; }
		EventSetter setter = new EventSetter(dialogOwner, event);
		setter.show();
		if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			event = setter.getEvent();
			XListItem eventItem = new XListItem(new Pair(event, event.getListItemDescription()));
			replaceItem(item, eventItem, null);
			return eventItem;
		}
		else {
			return item;
		}
	}

	public Event[] getEvents() {
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
			Event event = Event.getEventViaListItem(listItem);
			if (event != null) {
				tmp.add(event);
			}
		}
		if (tmp.size() <= 0) { return null; }
		Event[] result = new Event[tmp.size()];
		for (int i = 0; i < tmp.size(); ++i) {
			result[i] = (Event) (tmp.get(i));
		}
		return result;
	}
}