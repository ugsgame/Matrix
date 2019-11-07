package editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

class Head {

	private int id;
	private String name;

	public Head(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}
}

class HeadResManager {

	private File iniFile;
	private Head[] heads;

	public HeadResManager() {
		iniFile = new File(XUtil.getDefPropStr("HeadIniFile"));
		try {
			readIniFile();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Head[] getHeads() {
		return heads;
	}

	public Head getHead(int headID) {
		for (int i = 0; i < heads.length; ++i) {
			if (heads[i].getID() == headID) { return heads[i]; }
		}
		return null;
	}

	private void readIniFile() throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(iniFile)));
		ArrayList data = new ArrayList();
		String sLine;

		sLine = in.readLine();
		while (sLine != null) {
			data.add(sLine);
			sLine = in.readLine();
		}
		in.close();

		// get max group id
		ArrayList tmp = new ArrayList();
		for (int i = 0; i < data.size(); ++i) {
			sLine = ((String) (data.get(i))).trim();
			if (sLine == null) {
				continue;
			}
			if (sLine.length() < 2) {
				continue;
			}
			if (sLine.startsWith("@") && sLine.endsWith(";") && sLine.length() > 2) { // headline
				String infos[] = sLine.substring(1, sLine.length() - 1).split(",", 0);
				if (infos != null) {
					if (infos.length >= 2) {
						int headID = Integer.parseInt(infos[0]);
						String headName = infos[1];
						tmp.add(new Head(headID, headName));
					}
				}
			}
		}

		heads = new Head[tmp.size()];
		for (int i = 0; i < tmp.size(); ++i) {
			heads[i] = (Head) (tmp.get(i));
		}
	}
	
	public void saveMobile() throws Exception {
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
		        XUtil.getDefPropStr("SaveMobilePath") + "\\headname.dat")));
		if(heads == null) {
			SL.writeInt(0, out);
		}
		else {
			SL.writeInt(heads.length, out);
			for(int i = 0; i < heads.length; ++i) {
				SL.writeStringUnic(heads[i].getName(), out);
			}
		}
		out.flush();
		out.close();
	}
	
}

class HeadSelecter extends OKCancelDialog {

	private DefaultListModel headModel;
	private JList headList;

	public HeadSelecter(JDialog owner, int headID) {
		super(owner);
		setTitle("选择头像");
		Container cp = this.getContentPane();

		headModel = new DefaultListModel();
		headModel.addElement("无");
		Head[] heads = MainFrame.self.getHeadResManager().getHeads();
		for (int i = 0; i < heads.length; ++i) {
			headModel.addElement(heads[i]);
		}
		headList = new JList(headModel);
		headList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		headList.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				headListMouseClicked(e);
			}
		});
		JScrollPane headScroll = new JScrollPane(headList);

		setSelectedHead(headID);
		cp.add(headScroll, BorderLayout.CENTER);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(owner);
	}

	public int getHeadID() {
		Object obj = headList.getSelectedValue();
		if (obj != null) {
			if (obj instanceof Head) { return ((Head) obj).getID(); }
		}
		return -1;
	}

	private void setSelectedHead(int headID) {
		headList.clearSelection();
		for (int i = 0; i < headModel.getSize(); ++i) {
			Object obj = headModel.get(i);
			if (obj != null) {
				if (obj instanceof Head) {
					Head head = (Head) obj;
					if (head.getID() == headID) {
						headList.setSelectedValue(obj, true);
						break;
					}
				}
			}
		}
	}

	private void headListMouseClicked(MouseEvent e) {
		if (e.getButton() == XUtil.LEFT_BUTTON && e.getClickCount() == 2) {
			okPerformed();
		}
	}

	public void okPerformed() {
		closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}
}

public class Dialog implements Copyable, Saveable {

	public final static int HEAD_LEFT = 0;
	public final static int HEAD_RIGHT = 1;

	public final static Dialog[] copyArray(Dialog[] array) {
		Dialog[] result = null;
		if (array != null) {
			result = new Dialog[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyDialog();
			}
		}
		return result;
	}

	public final static Dialog[] createArrayViaFile(DataInputStream in) throws Exception {
		Dialog[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new Dialog[length];
			for (int i = 0; i < length; ++i) {
				result[i] = Dialog.createViaFile(in);
			}
		}
		return result;
	}

	public final static Dialog createViaFile(DataInputStream in) throws Exception {
		Dialog result = new Dialog();
		result.load(in);
		return result;
	}

	public static Dialog getDialogViaListItem(XListItem item) {
		if (item == null) { return null; }
		if (item.getValue() == null) { return null; }
		if (!(item.getValue() instanceof Pair)) { return null; }
		Object result = ((Pair) item.getValue()).first;
		if (!(result instanceof Dialog)) { return null; }
		return ((Dialog) result);
	}

	private int headID;
	private int headPos;
	private String text;

	public Dialog() {
		init();
	}

	private void init() {
		headID = -1;
		headPos = HEAD_LEFT;
		text = "";
	}

	public int getHeadID() {
		return headID;
	}

	public void setHeadID(int headID) {
		this.headID = headID;
	}

	public int getHeadPos() {
		return headPos;
	}

	public void setHeadPos(int headPos) {
		this.headPos = headPos;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text == null) text = "";
		this.text = text;
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		SL.writeInt(headID, out);
		SL.writeInt(headPos, out);
		SL.writeStringUnic(getText(), out);
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(headID);
		out.writeInt(headPos);
		SL.writeString(getText(), out);
	}

	protected void load(DataInputStream in) throws Exception {
		headID = in.readInt();
		headPos = in.readInt();
		setText(SL.readString(in));
	}

	public String toString() {
		Head head = MainFrame.self.getHeadResManager().getHead(headID);
		String result = head == null ? "无" : head.getName();
		return result + "：" + text;
	}

	public Copyable copy() {
		return copyDialog();
	}

	public final Dialog copyDialog() {
		Dialog result = new Dialog();
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(Dialog source) {
		this.headID = source.headID;
		this.headPos = source.headPos;
		this.text = source.text;
	}
}

class DialogPanel extends JPanel {

//	private JDialog owner;
	private int headID;
	private ValueChooser headChooser;
	private RadioPanel headPosRadio;
	private JTextArea dialogText;

	public DialogPanel(JDialog owner, Dialog dialog) {
		super();
//		this.owner = owner;

		headID = dialog.getHeadID();
		int[] ids;
		String[] names;
		Head[] heads = MainFrame.self.getHeadResManager().getHeads();
		if(heads == null) {
			ids = new int[] {-1};
			names = new String[] {"无"};
		}
		else {
			ids = new int[heads.length + 1];
			names = new String[heads.length + 1];
			ids[0] = -1;
			names[0] = "无";
			for(int i = 0; i < heads.length; ++i) {
				ids[i + 1] = heads[i].getID();
				names[i + 1] = heads[i].getName();
			}
		}
		headChooser = new ValueChooser(headID,ids, names, ValueChooser.NATURE_INDEX);

		headPosRadio = new RadioPanel(new int[] { Dialog.HEAD_LEFT, Dialog.HEAD_RIGHT },
		        new String[] { "左", "右" });
		setHeadPos(dialog.getHeadPos());

		dialogText = new JTextArea(dialog.getText());
		dialogText.setLineWrap(true);

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0;
		c.insets = new Insets(2, 2, 3, 3);

		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 0;
		this.add(new JLabel("头像："), c);
		c.gridx = 1;
		c.weightx = 1;
		this.add(headChooser, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		this.add(new JLabel("头像位置："), c);
		c.gridx = 1;
		c.weightx = 1;
		this.add(headPosRadio, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.weighty = 1;
		this.add(new JScrollPane(dialogText), c);
	}

	public int getHeadID() {
		return headChooser.getValue();
	}

//	private void setHeadID(int headID) {
//		this.headID = headID;
//		headChooser.setValue(headID);
//	}

	private void setHeadPos(int headPos) {
		headPosRadio.setValue(headPos);
	}

	public int getHeadPos() {
		return headPosRadio.getIntValue();
	}

	public void setText(String text) {
		dialogText.setText(text);
	}

	public String getText() {
		String result = dialogText.getText();
		if (result == null) {
			result = "";
		}
		return result;
	}

//	private void selectHead() {
//		HeadSelecter headSelecter = new HeadSelecter(owner, headID);
//		headSelecter.show();
//		if (headSelecter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
//			setHeadID(headSelecter.getHeadID());
//		}
//	}

	public void updateDialog(Dialog dialog) {
		if(dialog == null) return;
		dialog.setHeadID(getHeadID());
		dialog.setHeadPos(getHeadPos());
		dialog.setText(getText());
	}
}

class DialogList extends XList {

	public static ArrayList getDialogListItems(Dialog[] dialogs) {
		ArrayList result = new ArrayList();
		if (dialogs != null) {
			for (int i = 0; i < dialogs.length; ++i) {
				if (dialogs[i] != null) {
					Dialog dialog = dialogs[i].copyDialog();
					result.add(new XListItem(new Pair(dialog, dialog.toString())));
				}
			}
		}
		return result;
	}

	public DialogList(JDialog owner) {
		super(owner);
	}

	public DialogList(JDialog owner, ArrayList dialogsItems) {
		super(owner, dialogsItems);
	}

	public DialogList(JDialog owner, Dialog[] dialogs) {
		super(owner, getDialogListItems(dialogs));
	}

	protected Dialog createDefaultDialog() {
		return new Dialog();
	}

	protected XListItem insert(XListItem item) {
		Dialog dialog = createDefaultDialog();
		if (dialog == null) { return item; }

		DialogSetter setter = new DialogSetter(dialogOwner, dialog);
		setter.show();
		if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			dialog = setter.getDialog();
			XListItem dialogItem = new XListItem(new Pair(dialog, dialog.toString()));
			insertItem(item, dialogItem, null);
			return dialogItem;
		}
		else {
			return item;
		}
	}

	protected XListItem modify(XListItem item) {
		Dialog dialog = Dialog.getDialogViaListItem(item);
		if (dialog == null) { return item; }
		DialogSetter setter = new DialogSetter(dialogOwner, dialog);
		setter.show();
		if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			dialog = setter.getDialog();
			XListItem dialogItem = new XListItem(new Pair(dialog, dialog.toString()));
			replaceItem(item, dialogItem, null);
			return dialogItem;
		}
		else {
			return item;
		}
	}

	public Dialog[] getDialogs() {
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
			Dialog dialog = Dialog.getDialogViaListItem(listItem);
			if (dialog != null) {
				tmp.add(dialog);
			}
		}
		if (tmp.size() <= 0) { return null; }
		Dialog[] result = new Dialog[tmp.size()];
		for (int i = 0; i < tmp.size(); ++i) {
			result[i] = (Dialog) (tmp.get(i));
		}
		return result;
	}
}

class DialogSetter extends OKCancelDialog {

	private Dialog dialog;
	private DialogPanel dialogPanel;

	public DialogSetter(JDialog owner, Dialog dialog) {
		super(owner);
		this.dialog = dialog;
		dialogPanel = new DialogPanel(owner, dialog);

		setTitle("设置对话框");

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(dialogPanel, BorderLayout.CENTER);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public Dialog getDialog() {
		return dialog;
	}

	public void okPerformed() {
		dialogPanel.updateDialog(dialog);
		this.closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}
}

class EventDialogSetter extends OperationSetter {

	private EventDialog dialog;
	private DialogPanel lbDialogPanel;
	private DialogPanel dcDialogPanel;

	public EventDialogSetter(JDialog owner, EventDialog dialog) {
		super(owner);
		this.dialog = dialog;
		lbDialogPanel = new DialogPanel(this, dialog.getLBDialog());
		dcDialogPanel = new DialogPanel(this, dialog.getDCDialog());
		
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 2, 3, 3);
		c.weightx = 1;
		c.gridy = 0;
		c.gridx = 0;
		c.weighty = 0;
		p.add(new JLabel("主角为小强时的话："), c);
		c.gridx = 1;
		p.add(new JLabel("主角为貂蝉时的话："), c);
		c.gridy = 1;
		c.gridx = 0;
		c.weighty = 1;
		p.add(lbDialogPanel, c);
		c.gridx = 1;
		p.add(dcDialogPanel, c);
		

		setTitle("设置事件对话");

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(p, BorderLayout.CENTER);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	public Operation getOperation() {
		return dialog;
	}

	public void okPerformed() {
		lbDialogPanel.updateDialog(dialog.getLBDialog());
		dcDialogPanel.updateDialog(dialog.getDCDialog());
		this.closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}
}

class EventDialog extends Operation {

	private Dialog lvDialog;
	private Dialog dcDialog;

	public EventDialog(int id) {
		super(id, DIALOG);
		init();
	}

	protected EventDialog(int id, int type) {
		super(id, type);
		init();
	}

	private void init() {
		lvDialog = new Dialog();
		dcDialog = new Dialog();
	}

	public Operation copyOperation() {
		return copyEventDialog();
	}

	public final EventDialog copyEventDialog() {
		EventDialog result = new EventDialog(this.id);
		result.copyFrom(this);
		return result;
	}

	public void copyFrom(EventDialog source) {
		this.id = source.id;
		this.lvDialog.copyFrom(source.lvDialog);
		this.dcDialog.copyFrom(source.dcDialog);
	}

	public Dialog getLBDialog() {
		return lvDialog;
	}
	
	public Dialog getDCDialog() {
		return dcDialog;
	}

	public String getListItemDescription() {
		return lvDialog.toString() + "        " + dcDialog.toString();
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		super.saveMobile(out);
		lvDialog.saveMobile(out);
		dcDialog.saveMobile(out);
	}

	public void save(DataOutputStream out) throws Exception {
		super.save(out);
		lvDialog.save(out);
		dcDialog.save(out);
	}

	protected void load(DataInputStream in) throws Exception {
		super.load(in);
		lvDialog.load(in);
		dcDialog.load(in);
	}
}
