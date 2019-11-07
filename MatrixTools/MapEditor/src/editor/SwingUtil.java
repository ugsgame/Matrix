package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

class ButtonText extends JPanel {

	private ActionListener actionListener = null;
	private Object value;
	private JButton button;
	private JTextField text;
	private boolean acting;

	public ButtonText(Object value) {
		super();

		text = new JTextField();
		button = new JButton("...");
		button.setPreferredSize(new Dimension(25, 25));
		button.setMinimumSize(new Dimension(25, 25));
		button.setMaximumSize(new Dimension(25, 25));
		button.setSize(new Dimension(25, 25));
		acting = false;

		this.value = value;
		if (value != null) {
			text.setText(value.toString());
			text.setCaretPosition(0);
		}
		text.setEditable(false);
		text.setBackground(Color.WHITE);
		text.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == XUtil.LEFT_BUTTON && e.getClickCount() == 2) {
					if (button != null) {
						button.doClick();
					}
				}
			}
		});

		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				buttonActionPerformed(e);
			}
		});

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		add(text, c);

		button.setPreferredSize(new Dimension(20, 28));
		c.weightx = 0;
		c.gridx = 1;
		add(button, c);

		FocusListener focusListener = new FocusListener() {

			public void focusGained(FocusEvent e) {
				textFocusGained();
			}

			public void focusLost(FocusEvent e) {
				textFocusLost();
			}
		};
		text.addFocusListener(focusListener);
		button.addFocusListener(focusListener);
	}

	private void buttonActionPerformed(ActionEvent e) {
		if (actionListener != null) {
			if (actionListener instanceof ButtonTextActionListener) {
				((ButtonTextActionListener) actionListener).setValue(value);
			}
			acting = true;
			actionListener.actionPerformed(e);
			acting = false;
			refresh();
		}
	}

	/**
	 * 获得该控件所显示的对象
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 刷新该控件所显示的对象
	 */
	public void refresh() {
		if (value != null) {
			text.setText(value.toString());
		}
		else {
			text.setText(null);
		}
		text.setCaretPosition(0);
	}

	/**
	 * 设置该特定的ActionListener，在双击JText或者点击JButton时触发
	 */
	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	/**
	 * 设置然后刷新该控件所显示的对象
	 */
	public void setValue(Object value) {
		this.value = value;
		refresh();
	}

	private void textFocusGained() {
		text.setBackground(text.getSelectionColor());
	}

	private void textFocusLost() {
		if (!acting) {
			text.setBackground(Color.WHITE);
		}
	}
}

abstract class ButtonTextActionListener implements ActionListener {

	protected Object value = null;

	public void setValue(Object value) {
		this.value = value;
	}
}

class ButtonTextTableCellEditor extends AbstractCellEditor implements TableCellEditor {

	private ButtonText bt;

	public ButtonTextTableCellEditor(ActionListener actionListener) {
		super();
		bt = new ButtonText("");
		bt.setActionListener(actionListener);
	}

	public Object getCellEditorValue() {
		return bt.getValue();
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
	        int row, int column) {
		bt.setValue(value);
		return bt;
	}

	public boolean stopCellEditing() {
		try {
			bt.refresh();
		}
		catch (Exception e) {
			// return super.cancelCellEditing();
		}
		return super.stopCellEditing();
	}
}

class ButtonTextTableCellRenderer implements TableCellRenderer {

	private ButtonText bt;

	public ButtonTextTableCellRenderer() {
		super();
		bt = new ButtonText("");
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	        boolean hasFocus, int row, int column) {

		bt.setValue(value);
		return bt;
	}
}

class ComboTableCellEditor extends AbstractCellEditor implements TableCellEditor {

	private ValueChooser chooser;

	public ComboTableCellEditor(int value, int[] values, String[] descriptions) {
		super();
		chooser = new ValueChooser(value, values, descriptions);
	}

	public ComboTableCellEditor(int value, int[] values, String[] descriptions, int type) {
		super();
		chooser = new ValueChooser(value, values, descriptions, type);
	}
	
	public void reset(int[] values, String[] descriptions) {
		chooser.resetDefValues(values, descriptions);
	}
	
	public void reset(int[] values, String[] descriptions, int type) {
		chooser.resetDefValues(values, descriptions, type);
	}

	public Object getCellEditorValue() {
		return new Integer(chooser.getValue());
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
	        int row, int column) {
		chooser.setValue(((Integer) value).intValue());
		return chooser;
	}
}

class ComboTableCellRenderer implements TableCellRenderer {

	private ValueChooser chooser;

	public ComboTableCellRenderer(int value, int[] values, String[] descriptions) {
		super();
		chooser = new ValueChooser(value, values, descriptions);
	}

	public ComboTableCellRenderer(int value, int[] values, String[] descriptions, int type) {
		super();
		chooser = new ValueChooser(value, values, descriptions, type);
	}
	
	public void reset(int[] values, String[] descriptions) {
		chooser.resetDefValues(values, descriptions);
	}
	
	public void reset(int[] values, String[] descriptions, int type) {
		chooser.resetDefValues(values, descriptions, type);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	        boolean hasFocus, int row, int column) {
		chooser.setValue(((Integer) value).intValue());
		return chooser;
	}
}

class ManagerList extends JPanel {

	protected DefaultListModel model;
	protected JList list;
	protected int selectedIndex;
	protected Object copiedItem;

	public ManagerList() {
		model = new DefaultListModel();
		list = new JList(model);
		selectedIndex = -1;
		copiedItem = null;

		SwingUtil.SetObjListRenderer(list);

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setDragEnabled(false);
		list.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				selectionChanged();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 2));
		JButton b;

		b = new JButton("添加");
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				add();
			}
		});
		buttonPanel.add(b);

		b = new JButton("删除");
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});
		buttonPanel.add(b);

		b = new JButton("复制");
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				copy();
			}
		});
		buttonPanel.add(b);

		b = new JButton("粘贴");
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				paste();
			}
		});
		buttonPanel.add(b);

		setLayout(new BorderLayout());
		add(new JScrollPane(list), BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	protected void add() {
		int index = getAddIndex();
		Object obj = createNewItem(index);
		if (obj != null) {
			list.clearSelection();
			model.add(index, obj);
			list.setSelectedIndex(index);
		}
	}

	protected void afterEnter(int index) {}

	protected void beforeLeave(int index) {}

	protected void copy() {
		beforeLeave(selectedIndex);
		if (selectedIndex >= 0 && selectedIndex < model.size()) {
			Object obj = model.get(selectedIndex);
			if (obj != null) {
				copiedItem = copyItem(selectedIndex, obj);
			}
		}
	}

	protected Object copyItem(int sourceIndex, Object sourceItem) {
		Object result = null;
		if (sourceItem instanceof Copyable) {
			result = ((Copyable) sourceItem).copy();
		}
		return result;
	}

	protected Object createNewItem(int index) {
		return null;
	}

	protected int getAddIndex() {
		return getTailIndex();
	}

	protected int getInsertAfterIndex(int aIndex) {
		int index = aIndex + 1;
		if (index > model.size()) index = model.size();
		if (index < 0) index = 0;
		return index;
	}

	protected int getInsertBeforeIndex(int aIndex) {
		int index = aIndex;
		if (index > model.size()) index = model.size();
		if (index < 0) index = 0;
		return index;
	}

	public JList getList() {
		return list;
	}

	public DefaultListModel getModel() {
		return model;
	}

	protected int getPasteIndex() {
		return getInsertBeforeIndex(selectedIndex);
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	protected int getTailIndex() {
		int index = model.size();
		if (index < 0) index = 0;
		return index;
	}
	
	protected void paste() {
		if (copiedItem == null) return;
		int index = getPasteIndex();
		list.clearSelection();
		Object result = null;
		if (copiedItem instanceof Copyable) {
			result = ((Copyable) copiedItem).copy();
		}
		else {
			result = copiedItem;
		}
		model.add(index, result);
		list.setSelectedIndex(index);
	}

	protected void remove() {
		int index = list.getSelectedIndex();
		if (index >= 0 && index < model.size()) {
			list.clearSelection();
			model.remove(index);
			if (index >= model.size()) {
				index = model.size() - 1;
			}
			list.setSelectedIndex(index);
		}
	}

	protected void selectionChanged() {
		int index = list.getSelectedIndex();
		if (selectedIndex == index) return;
		beforeLeave(selectedIndex);
		selectedIndex = index;
		afterEnter(selectedIndex);
	}
}

class NumberSpinner extends JSpinner {

	private SpinnerNumberModel model;

	public NumberSpinner() {
		super(new SpinnerNumberModel());
		model = (SpinnerNumberModel) (getModel());
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(this);
		editor.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 3));
		setEditor(editor);
		setBorder(BorderFactory.createEmptyBorder());
	}

	public int getIntValue() {
		return ((Integer) (this.getValue())).intValue();
	}

	public Object getValue() {
		try {
			commitEdit();
		}
		catch (Exception e) {}
		return super.getValue();
	}

	public void setIntValue(int value) {
		setValue(new Integer(value));
	}

	public void setMax(int max) {
		model.setMaximum(new Integer(max));
	}

	public void setMin(int min) {
		model.setMinimum(new Integer(min));
	}
}

abstract class OKCancelDialog extends JDialog {

	public final static int CANCEL_PERFORMED = 0;
	public final static int OK_PERFORMED = 1;

	protected int closeType;
	protected JButton okButton;
	protected JButton cancelButton;
	protected JPanel buttonPanel;

	public OKCancelDialog(JDialog owner) {
		super(owner, true);
		init();
		setLocationRelativeTo(owner);
	}

	public OKCancelDialog(JFrame owner) {
		super(owner, true);
		init();
		setLocationRelativeTo(owner);
	}

	protected abstract void cancelPerformed();

	public int getCloseType() {
		return closeType;
	}

	private void init() {
		int w = XUtil.getDefPropInt(XUtil.getClassName(getClass()) + "Width");
		int h = XUtil.getDefPropInt(XUtil.getClassName(getClass()) + "Height");
		if(w > 0 && h >0)
			setSize(new Dimension(w, h));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		closeType = CANCEL_PERFORMED;

		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				okPerformed();
			}
		});
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cancelPerformed();
			}
		});

		buttonPanel = new JPanel();
		GridLayout buttonLayout = new GridLayout();
		buttonLayout.setHgap(10);
		buttonPanel.setLayout(buttonLayout);
		buttonPanel.setPreferredSize(new Dimension(100, XUtil.getDefPropInt("ButtonPanelHeight")));
	}

	protected abstract void okPerformed();
}

class ProgressDialog extends JDialog {

	private JLabel titleLabel;
	private JLabel infoLabel;
	private JProgressBar progressBar;

	public ProgressDialog() {
		super();
		init();
		setLocationRelativeTo(null);
	}

	public ProgressDialog(JFrame owner) {
		super(owner);
		init();
		setLocationRelativeTo(owner);
	}

	private void init() {
		setBounds(0, 0, 200, 60);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setUndecorated(true);
		Container cp = getContentPane();

		titleLabel = new JLabel("标题", SwingConstants.CENTER);
		cp.add(titleLabel, BorderLayout.NORTH);

		progressBar = new JProgressBar();
		cp.add(progressBar, BorderLayout.CENTER);

		infoLabel = new JLabel("信息", SwingConstants.CENTER);
		cp.add(infoLabel, BorderLayout.SOUTH);

		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	public void setInfo(final String s) {
		SwingUtilities.invokeLater(new Runnable() {
			 public void run() {
				 infoLabel.setText(s);
			 }
		});
	}

	public void setTitle(final String s) {
		super.setTitle(s);
		SwingUtilities.invokeLater(new Runnable() {
			 public void run() {
				titleLabel.setText(s);
			 }
		});
		
//		super.setTitle(s);
//		titleLabel.setText(s);
	}

	public void setValue(final int value) {
	//	progressBar.setValue(value);
		SwingUtilities.invokeLater(new Runnable() {
			 public void run() {
				 progressBar.setValue(value);
			 }
		});
	}
}

class RadioPanel extends JPanel {

	public final static int[] VALUES = { 1, 0 };
	public final static String[] DESCS_ON = { "On", "Off" };
	public final static String[] DESCS_YES = { "是", "否" };
	public final static String[] DESCS_CAN = { "能", "不能" };

	private int[] values;
	private JRadioButton[] radios;

	public RadioPanel() {
		init(VALUES, DESCS_ON);
	}

	public RadioPanel(int[] values) {
		init(values, DESCS_ON);
	}

	public RadioPanel(int[] values, String[] descs) {
		init(values, descs);
	}

	public RadioPanel(String[] descs) {
		init(VALUES, descs);
	}

	public boolean getBoolValue() {
		int value = getIntValue();
		boolean result = value == 0 ? false : true;
		return result;
	}

	public int getIntValue() {
		int result = 0;
		for (int i = 0; i < radios.length; ++i) {
			if (radios[i].isSelected()) {
				result = values[i];
				break;
			}
		}
		return result;
	}

	private void init(int[] values, String[] descs) {
		this.values = XUtil.copyArray(values);
		radios = new JRadioButton[values.length];
		this.setLayout(new GridLayout(1, radios.length));
		ButtonGroup radioGroup = new ButtonGroup();

		for (int i = 0; i < values.length; ++i) {
			String title = "未知";
			if (i < descs.length) {
				if (descs[i] != null) {
					title = descs[i];
				}
			}
			radios[i] = new JRadioButton(title);
			radioGroup.add(radios[i]);
			this.add(radios[i]);
		}
	}

	public void setValue(boolean on) {
		int value = on ? 1 : 0;
		setValue(value);
	}

	public void setValue(int value) {
		for (int i = 0; i < values.length; ++i) {
			if (value == values[i]) {
				radios[i].setSelected(true);
				break;
			}
		}
	}
}

class RangePanel extends JPanel {

	private NumberSpinner minSpinner;
	private NumberSpinner maxSpinner;

	public RangePanel(String name) {
		super();
		init(name, 0, 0);
	}

	public RangePanel(String name, int min, int max) {
		super();
		init(name, min, max);
	}

	public RangePanel(String name, IntPair p) {
		super();
		init(name, p.x, p.y);
	}

	private void init(String name, int min, int max) {
		minSpinner = new NumberSpinner();
		maxSpinner = new NumberSpinner();

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 5, 3, 5);

		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 0;
		c.weighty = 1;
		//c.gridwidth = 4;
		add(new JLabel(name), c);
		//c.gridwidth = 1;

		//c.gridy = 1;
		c.gridx = 2;
		c.weightx = 0;
		add(new JLabel("从"), c);
		c.gridx = 3;
		c.weightx = 1;
		add(minSpinner, c);

		c.gridx = 4;
		c.weightx = 0;
		add(new JLabel("到"), c);
		c.gridx = 5;
		c.weightx = 1;
		add(maxSpinner, c);

		setMin(min);
		setMax(max);
	}

	public int getMin() {
		return minSpinner.getIntValue();
	}

	public int getMax() {
		return maxSpinner.getIntValue();
	}

	public IntPair getRange() {
		return new IntPair(getMin(), getMax());
	}

	public void setMin(int min) {
		minSpinner.setIntValue(min);
	}

	public void setMax(int max) {
		maxSpinner.setIntValue(max);
	}

	public void setRange(IntPair range) {
		setMin(range.x);
		setMax(range.y);
	}
}

class SpinnerTableCellEditor extends AbstractCellEditor implements TableCellEditor {

	private NumberSpinner spinner;

	public SpinnerTableCellEditor() {
		super();
		spinner = new NumberSpinner();
	}

	public Object getCellEditorValue() {
		return (spinner.getValue());
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
	        int row, int column) {
		spinner.setValue(value);
		return spinner;
	}

	public boolean stopCellEditing() {
		try {
			spinner.commitEdit();
		}
		catch (Exception e) {
			// return super.cancelCellEditing();
		}
		return super.stopCellEditing();
	}
}

class SpinnerTableCellRenderer implements TableCellRenderer {

	private NumberSpinner spinner;

	public SpinnerTableCellRenderer() {
		super();
		spinner = new NumberSpinner();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	        boolean hasFocus, int row, int column) {
		spinner.setValue(value);
		return spinner;
	}
}

interface StateListListener {
	public void ValueChanged();
}

abstract class StateList extends JList {

	private class StateCheckBox extends JPanel implements ListCellRenderer {

		private class CheckRect extends JPanel {

			public CheckRect() {
				super();
				setBackground(Color.WHITE);
			}

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				drawState(checkState, g, getWidth(), getHeight());
			}
		}

		private int checkState;
		private CheckRect checkRect;
		private JLabel textLabel;
		private DefaultListCellRenderer defaultRenderer;

		public StateCheckBox() {
			init(stateValues[defStateIndex], "");
		}

		public int getCheckState() {
			return checkState;
		}

		public Component getListCellRendererComponent(JList list, Object value, int index,
		        boolean isSelected, boolean cellHasFocus) {
			Component co = defaultRenderer.getListCellRendererComponent(list, value, index,
			        isSelected, cellHasFocus);
			if (co == null || value == null) return co;
			if (!(value instanceof IntPair)) return co;
			IntPair pair = (IntPair) value;
			if (pair.x >= 0 && pair.x < itemNames.length) {
				setText(itemNames[pair.x]);
			}
			else {
				setText("无效名称索引：" + pair.x);
			}
			setCheckState(pair.y);
			setBackground(co.getBackground());
			setForeground(co.getForeground());
			return this;
		}

		public String getText() {
			return textLabel.getText();
		}

		private void init(int checkState, String text) {
			this.checkState = checkState;
			defaultRenderer = new DefaultListCellRenderer();
			checkRect = new CheckRect();
			checkRect.setPreferredSize(new Dimension(getDefCheckBoxWidth(), getDefCheckBoxHeight()));
			textLabel = new JLabel(text);
			setLayout(new BorderLayout());
			add(checkRect, BorderLayout.WEST);
			add(textLabel, BorderLayout.CENTER);
			setBackground(Color.WHITE);
		}

		public boolean isInCheckRect(int x, int y) {
			return checkRect.contains(x, y);
		}

		public void setBackground(Color color) {
			super.setBackground(color);
			if (textLabel != null) textLabel.setBackground(color);
		}

		public void setCheckState(int checkState) {
			this.checkState = checkState;
			checkRect.repaint();
		}

		public void setForeground(Color color) {
			super.setForeground(color);
			if (textLabel != null) textLabel.setForeground(color);
		}

		public void setText(String text) {
			textLabel.setText(text);
		}
	}

	private DefaultListModel model;
	private StateCheckBox renderer;
	protected int[] stateValues;
	protected String[] itemNames;
	private int defStateIndex;
	private StateListListener listener;

	public StateList(int[] stateValues, String[] itemNames) {
		super();
		init(stateValues, itemNames, 0);
	}

	public StateList(int[] stateValues, String[] itemNames, int defStateIndex) {
		super();
		init(stateValues, itemNames, defStateIndex);
	}

	protected abstract void drawState(int state, Graphics g, int width, int height);

	protected int getDefCheckBoxHeight() {
		return XUtil.getDefPropInt("StateCheckBoxHeight");
	}

	protected int getDefCheckBoxWidth() {
		return XUtil.getDefPropInt("StateCheckBoxWidth");
	}

	protected int getNextStateValue(int stateValue) {
		int index = defStateIndex;
		for (int i = 0; i < stateValues.length; ++i) {
			if (stateValue == stateValues[i]) {
				index = i + 1;
				if (index >= stateValues.length) index = 0;
				break;
			}
		}
		return stateValues[index];
	}

	public int[] getStates() {
		ArrayList tmp = new ArrayList();
		for (int i = 0; i < model.size(); ++i) {
			IntPair value = (IntPair) (model.get(i));
			tmp.add(new Integer(value.y));
		}

		int[] result = new int[tmp.size()];
		for (int i = 0; i < result.length; ++i) {
			result[i] = ((Integer) (tmp.get(i))).intValue();
		}
		return result;
	}

	private void init(int[] stateValues, String[] itemNames, int defStateIndex) {
		listener = null;
		if (stateValues == null || itemNames == null) return;
		this.stateValues = stateValues;
		this.itemNames = itemNames;
		this.defStateIndex = defStateIndex;
		model = new DefaultListModel();
		for (int i = 0; i < itemNames.length; ++i) {
			model.addElement(new IntPair(i, stateValues[defStateIndex]));
		}
		setModel(model);
		setDragEnabled(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		renderer = new StateCheckBox();
		setCellRenderer(renderer);

		addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				selfMouseReleased(e);
			}
		});
	}
	
	public void setListener(StateListListener listener) {
		this.listener = listener;
	}
	
	public void resetItems(String[] itemNames) {
		model.clear();
		this.itemNames = itemNames;
		for (int i = 0; i < itemNames.length; ++i) {
			model.addElement(new IntPair(i, stateValues[defStateIndex]));
		}
		if(listener != null) listener.ValueChanged();
	}

	private void selfMouseReleased(MouseEvent e) {
		int index = getSelectedIndex();
		Rectangle cellBounds = getCellBounds(index, index);
		if (cellBounds == null) return;
		int x = e.getX(), y = e.getY();
		if (!cellBounds.contains(x, y)) return;
		x -= cellBounds.x;
		y -= cellBounds.y;

		if (renderer.isInCheckRect(x, y)) {
			Object value = model.get(index);
			if (value == null) return;
			if (!(value instanceof IntPair)) return;
			IntPair pair = (IntPair) value;
			if (e.getButton() == XUtil.LEFT_BUTTON) {
				pair.y = getNextStateValue(pair.y);
			}
			else if (e.getButton() == XUtil.RIGHT_BUTTON) {
				pair.y = stateValues[defStateIndex];
			}
			repaint(cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height);
			if(listener != null) listener.ValueChanged();
		}
	}

	public void setStates(int[] states) {
		if (states == null) return;
		if (states.length != itemNames.length) return;
		for (int i = 0; i < itemNames.length; ++i) {
			int value = stateValues[defStateIndex];
			for (int j = 0; j < stateValues.length; ++j) {
				if (states[i] == stateValues[j]) {
					value = stateValues[j];
					break;
				}
			}
			model.set(i, new IntPair(i, value));
		}
		if(listener != null) listener.ValueChanged();
	}
}

public class SwingUtil {

	public final static void MakeListDeleteable(final JList list, final SwingUtilListener listener) {
		if (list == null) { return; }
		list.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DELETE:
					if (list.getModel() == null) { return; }
					if (!(list.getModel() instanceof DefaultListModel)) { return; }
					DefaultListModel model = (DefaultListModel) (list.getModel());
					int index = list.getSelectedIndex();
					if (index < 0 || index >= model.size()) { return; }
					int oldIndex = index;
					model.remove(index);
					if (index >= model.size()) {
						index = model.size() - 1;
					}
					if (index < 0) {
						index = -1;
					}
					list.setSelectedIndex(index);
					int newIndex = index;
					if (listener != null) {
						listener.listItemDeleted(oldIndex, newIndex);
					}
					break;
				default:
					break;
				}
			}
		});
	}

	public final static void setDefScrollIncrement(JScrollPane scrollPane) {
		if (scrollPane == null) { return; }
		scrollPane.getHorizontalScrollBar().setUnitIncrement(
		        XUtil.getDefPropInt("ScrollUnitIncrement"));
		scrollPane.getVerticalScrollBar().setUnitIncrement(
		        XUtil.getDefPropInt("ScrollUnitIncrement"));
		scrollPane.getHorizontalScrollBar().setBlockIncrement(
		        XUtil.getDefPropInt("ScrollBlockIncrement"));
		scrollPane.getVerticalScrollBar().setBlockIncrement(
		        XUtil.getDefPropInt("ScrollBlockIncrement"));
	}

	public final static void SetObjListRenderer(final JList list) {
		if (list == null) { return; }

		list.setCellRenderer(new DefaultListCellRenderer() {

			public Component getListCellRendererComponent(JList list, Object value, int index,
			        boolean isSelected, boolean cellHasFocus) {
				String strValue = "";
				if (value != null) {
					if (value.toString() != null) {
						strValue = value.toString();
					}
				}
				if (strValue.equals("")) {
					strValue = " ";
				}
				return super.getListCellRendererComponent(list, strValue, index, isSelected,
				        cellHasFocus);
			}
		});

	}
}

class SwingUtilAdapter implements SwingUtilListener {

	public void listItemDeleted(int oldIndex, int newIndex) {}
}

interface SwingUtilListener {

	public void listItemDeleted(int oldIndex, int newIndex);
}

class ValueChooseDialog extends OKCancelDialog {

	private class ValueComboItem {

		public int value;
		public String description;

		public ValueComboItem(int value, String description) {
			this.value = value;
			this.description = description;
		}

		public String toString() {
			return description;
		}
	}

	public final static int VALUE_INDEX = 0;// 第i个元素的值为value[i]、描述为desc[value[i]]
	public final static int NATURE_INDEX = 1;// 第i个元素的值为value[i]、描述为desc[i]

	private DefaultListModel model;
	private JList list;

	public ValueChooseDialog(JDialog owner, int value, int[] defValues, String[] defDescriptions) {
		super(owner);
		init(value, defValues, defDescriptions, VALUE_INDEX);
	}

	public ValueChooseDialog(JDialog owner, int value, int[] defValues, String[] defDescriptions,
	        int type) {
		super(owner);
		init(value, defValues, defDescriptions, type);
	}

	public ValueChooseDialog(JFrame owner, int value, int[] defValues, String[] defDescriptions) {
		super(owner);
		init(value, defValues, defDescriptions, VALUE_INDEX);
	}

	public ValueChooseDialog(JFrame owner, int value, int[] defValues, String[] defDescriptions,
	        int type) {
		super(owner);
		init(value, defValues, defDescriptions, type);
	}

	public void cancelPerformed() {
		dispose();
	}

	public int getValue() {
		Object item = list.getSelectedValue();
		if (item != null) {
			if (item instanceof ValueComboItem) { return ((ValueComboItem) item).value; }
		}
		return -1;
	}

	private void init(int value, int[] defValues, String[] defDescriptions, int type) {
		model = new DefaultListModel();
		list = new JList(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setDragEnabled(false);
		list.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				listMouseClicked(e);
			}
		});
		resetDefValues(defValues, defDescriptions, type);
		setValue(value);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(new JScrollPane(list), BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void listMouseClicked(MouseEvent e) {
		if (e.getButton() == XUtil.LEFT_BUTTON && e.getClickCount() == 2) {
			okPerformed();
		}
	}

	public void okPerformed() {
		closeType = OK_PERFORMED;
		dispose();
	}

	public void resetDefValues(int[] defValues, String[] defDescriptions) {
		resetDefValues(defValues, defDescriptions, VALUE_INDEX);
	}

	public void resetDefValues(int[] defValues, String[] defDescriptions, int type) {
		model.clear();
		if (defValues != null) {
			int unknownCount = 0;
			for (int i = 0; i < defValues.length; ++i) {
				int value = defValues[i];
				int index = (type == VALUE_INDEX ? value : i);
				String description = "";
				boolean hasDescription = false;
				if (defDescriptions != null) {
					if (index >= 0 && index < defDescriptions.length) {
						hasDescription = true;
						description = defDescriptions[index];
						if (description == null) {
							description = "";
						}
					}
				}
				if (!hasDescription) {
					description = "未知" + (++unknownCount);
				}
				model.addElement(new ValueComboItem(value, description));
			}
		}
	}

	public void setValue(int value) {
		list.clearSelection();
		for (int i = 0; i < model.size(); ++i) {
			Object item = model.get(i);
			if (item == null) {
				continue;
			}
			if (item instanceof ValueComboItem) {
				ValueComboItem valueItem = (ValueComboItem) item;
				if (valueItem.value == value) {
					list.setSelectedIndex(i);
					break;
				}
			}
		}
	}
}

class ValueChooser extends JComboBox {

	private class ValueComboItem {

		public int value;
		public String description;

		public ValueComboItem(int value, String description) {
			this.value = value;
			this.description = description;
		}

		public String toString() {
			return description;
		}
	}

	public final static int VALUE_INDEX = 0;

	public final static int NATURE_INDEX = 1;

	public ValueChooser(int value, int[] defValues, String[] defDescriptions) {
		super();
		init(value, defValues, defDescriptions, VALUE_INDEX);
	}

	public ValueChooser(int value, int[] defValues, String[] defDescriptions, int type) {
		super();
		init(value, defValues, defDescriptions, type);
	}

	public int getValue() {
		Object item = getSelectedItem();
		if (item != null) {
			if (item instanceof ValueComboItem) { return ((ValueComboItem) item).value; }
		}
		return -1;
	}

	private void init(int value, int[] defValues, String[] defDescriptions, int type) {
		resetDefValues(defValues, defDescriptions, type);
		setValue(value);
	}

	public void resetDefValues(int[] defValues, String[] defDescriptions) {
		resetDefValues(defValues, defDescriptions, VALUE_INDEX);
	}

	public void resetDefValues(int[] defValues, String[] defDescriptions, int type) {
		removeAllItems();
		if (defValues != null) {
			int unknownCount = 0;
			for (int i = 0; i < defValues.length; ++i) {
				int value = defValues[i];
				int index = (type == VALUE_INDEX ? value : i);
				String description = "";
				boolean hasDescription = false;
				if (defDescriptions != null) {
					if (index >= 0 && index < defDescriptions.length) {
						hasDescription = true;
						description = defDescriptions[index];
						if (description == null) {
							description = "";
						}
					}
				}
				if (!hasDescription) {
					description = "未知" + (++unknownCount);
				}
				addItem(new ValueComboItem(value, description));
			}
		}
	}

	public void setValue(int value) {
		setSelectedIndex(-1);
		for (int i = 0; i < getItemCount(); ++i) {
			Object item = getItemAt(i);
			if (item == null) {
				continue;
			}
			if (item instanceof ValueComboItem) {
				ValueComboItem valueItem = (ValueComboItem) item;
				if (valueItem.value == value) {
					setSelectedIndex(i);
					break;
				}
			}
		}
	}
}

class ValueEditor extends JPanel {

	private class ValueComboItem {

		public int value;
		public String description;

		public ValueComboItem(int value, String description) {
			this.value = value;
			this.description = description;
		}

		public String toString() {
			return description;
		}
	}

	private JComboBox valueComboBox;
	private NumberSpinner valueSpinner;
	private boolean hasSpinner;

	private ActionListener comboListener;

	public ValueEditor(int value, int[] defValues, String[] defDescriptions) {
		super();
		init(value, defValues, defDescriptions, true);
	}

	public ValueEditor(int value, int[] defValues, String[] defDescriptions, boolean hasSpinner) {
		super();
		init(value, defValues, defDescriptions, hasSpinner);
	}

	public JComboBox getComboBox() {
		return valueComboBox;
	}

	public JSpinner getSpinner() {
		return valueSpinner;
	}

	public int getValue() {
		return valueSpinner.getIntValue();
	}

	private void init(int value, int[] defValues, String[] defDescriptions, boolean hasSpinner) {
		this.hasSpinner = hasSpinner;
		valueSpinner = new NumberSpinner();
		valueComboBox = new JComboBox();
		resetDefValues(defValues, defDescriptions);
		valueComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				valueComboBoxActionPerformed(e);
			}
		});
		setValue(value);

		if (hasSpinner) {
			setLayout(new GridLayout(1, 2, 10, 0));
			add(valueSpinner);
			add(valueComboBox);
		}
		else {
			setLayout(new BorderLayout());
			add(valueComboBox, BorderLayout.CENTER);
		}
	}

	public boolean isHasSpinner() {
		return this.hasSpinner;
	}

	public void resetDefValues(int[] defValues, String[] defDescriptions) {
		valueComboBox.removeAllItems();
		if (defValues != null) {
			int unknownCount = 0;
			for (int i = 0; i < defValues.length; ++i) {
				String description = "";
				boolean hasDescription = false;
				if (defDescriptions != null) {
					if (i >= 0 && i < defDescriptions.length) {
						hasDescription = true;
						description = defDescriptions[i];
						if (description == null) {
							description = "";
						}
					}
				}
				if (!hasDescription) {
					description = "未知" + (++unknownCount);
				}
				valueComboBox.addItem(new ValueComboItem(defValues[i], description));
			}
		}
	}

	public void setComboBoxListener(ActionListener comboListener) {
		this.comboListener = comboListener;
	}

	public void setValue(int value) {
		valueSpinner.setIntValue(value);
		valueComboBox.setSelectedIndex(-1);
		for (int i = 0; i < valueComboBox.getItemCount(); ++i) {
			Object item = valueComboBox.getItemAt(i);
			if (item == null) {
				continue;
			}
			if (item instanceof ValueComboItem) {
				ValueComboItem valueItem = (ValueComboItem) item;
				if (valueItem.value == value) {
					valueComboBox.setSelectedIndex(i);
					break;
				}
			}
		}
	}

	private void valueComboBoxActionPerformed(ActionEvent e) {
		Object item = valueComboBox.getSelectedItem();
		if (item == null) { return; }
		if (item instanceof ValueComboItem) {
			valueSpinner.setIntValue(((ValueComboItem) item).value);
		}
		if (comboListener != null) {
			comboListener.actionPerformed(e);
		}
	}
}

class ValueTable extends XTable {

	public ValueTable(String name, String[] colNames, String[] itemNames) {
		super(name, colNames, false, false);
		this.setNumberCol(1);
		DefaultTableModel model = getModel();
		for (int i = 0; i < itemNames.length; ++i) {
			model.addRow(new Object[] { itemNames[i], new Integer(getDefValue()) });
		}
	}

	protected boolean canPass(int value) {
		return value == getDefValue();
	}

	protected int getDefValue() {
		return 0;
	}

	protected int getItemValue(int row) {
		return row;
	}

	public IntPair[] getValues() {
		stopEditing();
		DefaultTableModel model = getModel();
		ArrayList tmp = new ArrayList();

		for (int row = 0; row < model.getRowCount(); ++row) {
			int value = ((Integer) (model.getValueAt(row, 1))).intValue();
			if (!canPass(value)) {
				tmp.add(new IntPair(getItemValue(row), value));
			}
		}

		IntPair[] result = null;
		if (tmp.size() > 0) {
			result = new IntPair[tmp.size()];
			for (int i = 0; i < tmp.size(); ++i) {
				result[i] = (IntPair) (tmp.get(i));
			}
		}
		return result;
	}

	public void setValues(IntPair[] values) {
		stopEditing();
		DefaultTableModel model = getModel();
		for (int row = 0; row < model.getRowCount(); ++row) {
			model.setValueAt(new Integer(getDefValue()), row, 1);
			if (values != null) {
				int itemValue = getItemValue(row);
				for (int i = 0; i < values.length; ++i) {
					if (values[i].x == itemValue) {
						model.setValueAt(new Integer(values[i].y), row, 1);
						break;
					}
				}
			}
		}
	}
}

class XList extends JList {

	public class MenuActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (!(e.getSource() instanceof XMenuItem)) { return; }
			XListItem item = ((XMenuItem) (e.getSource())).getItem();
			String menuName = (e.getActionCommand()).toUpperCase();
			if (menuName.equals("INSERT")) {
				startInsert(item);
			}
			else if (menuName.equals("MODIFY")) {
				startModify(item);
			}
			else if (menuName.equals("DELETE")) {
				startDelete(item);
			}
		}
	}

	private class XMenuItem extends JMenuItem {

		private XListItem item;

		public XMenuItem(XListItem item) {
			super();
			this.item = item;
			addActionListener(new MenuActionListener());
		}

		public XListItem getItem() {
			return item;
		}

		public void setItem(XListItem item) {
			this.item = item;
		}
	}

	private JPopupMenu popup;
	private XMenuItem menuInsert;
	private XMenuItem menuModify;
	private XMenuItem menuDelete;

	protected JDialog dialogOwner;
	protected JFrame frameOwner;
	protected DefaultListModel model;
	protected XListItem selectedItem;

	protected boolean popupping;

	protected XListItem lastItem;

	public XList(JDialog owner) {
		init(null, owner, null);
	}

	public XList(JFrame owner) {
		init(owner, null, null);
	}

	public XList(JDialog owner, XListItem[] items) {
		ArrayList tmp = new ArrayList();
		if (items != null) {
			for (int i = 0; i < items.length; ++i) {
				tmp.add(items[i]);
			}
		}
		init(null, owner, tmp);
	}

	public XList(JDialog owner, ArrayList items) {
		init(null, owner, items);
	}

	public XList(JFrame owner, ArrayList items) {
		init(owner, null, items);
	}

	public XList(JFrame owner, XListItem[] items) {
		ArrayList tmp = new ArrayList();
		if (items != null) {
			for (int i = 0; i < items.length; ++i) {
				tmp.add(items[i]);
			}
		}
		init(owner, null, tmp);
	}

	private void dbClick(XListItem item) {
		if (!startModify(item)) { // 如果能修改则修改
			startInsert(item); // 不能修改则插入
		}
	}

	protected void delete(XListItem item) {
		deleteItem(item);
	}

	public void deleteItem(XListItem item) {
		if (item == null) { return; }
		XListItem[] children = getChildren(item);
		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				if (children[i] != null) {
					model.removeElement(children[i]);
				}
			}
		}
		model.removeElement(item);
	}

	public XListItem[] getChildren(XListItem item) {
		if (item == null) { return null; }
		// 获得全部的项目
		ArrayList tmp = new ArrayList();
		for (int i = 0; i < model.size(); ++i) {
			Object o = model.get(i);
			if (o == null) {
				continue;
			}
			if (!(o instanceof XListItem)) {
				continue;
			}
			tmp.add(o);
		}
		if (tmp.isEmpty()) { return null; }

		// 获得参数的子项目
		XListItem[] allItems = new XListItem[tmp.size()];
		for (int i = 0; i < tmp.size(); ++i) {
			allItems[i] = (XListItem) (tmp.get(i));
		}
		return XListItem.getChildren(allItems, item);
	}

	private void init(JFrame frameOwner, JDialog dialogOwner, ArrayList items) {
		this.frameOwner = frameOwner;
		this.dialogOwner = dialogOwner;
		model = new DefaultListModel();
		super.setModel(model);
		super.setCellRenderer(new XListCellRenderer());
		super.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		lastItem = new XListItem(null, XListItem.CAN_INSERT); // 必然存在的一行

		popup = new JPopupMenu();
		// menuInsert
		menuInsert = new XMenuItem(null);
		menuInsert.setActionCommand("Insert");
		menuInsert.setText("插入");
		menuInsert.setMnemonic('I');
		popup.add(menuInsert);
		// menuModify
		menuModify = new XMenuItem(null);
		menuModify.setActionCommand("Modify");
		menuModify.setText("修改");
		menuModify.setMnemonic('M');
		popup.add(menuModify);
		// menuDelete
		menuDelete = new XMenuItem(null);
		menuDelete.setActionCommand("Delete");
		menuDelete.setText("删除");
		menuDelete.setMnemonic('D');
		popup.add(menuDelete);

		setItems(items);

		addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				selfSelectionChanged();
			}
		});
		addFocusListener(new FocusAdapter() {

			public void focusLost(FocusEvent e) {
				if (!popup.isVisible() && !popupping) {
					clearSelection(); // 在非弹出菜单或者修改属性的时候，清空选项
				}
			}
		});
		addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == XUtil.LEFT_BUTTON && e.getClickCount() == 2) {
					dbClick(selectedItem);
				}
			}

			public void mousePressed(MouseEvent e) {
				if (e.getButton() == XUtil.RIGHT_BUTTON) {
					setSelectedIndex(locationToIndex(new Point(e.getX(), e.getY())));
					requestFocus();
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == XUtil.RIGHT_BUTTON) {
					popupMenu(selectedItem, e);
				}
			}
		});

		addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				if (selectedItem == null) { return; }
				switch (e.getKeyCode()) {
				case KeyEvent.VK_SPACE:
					startModify(selectedItem);
					break;
				case KeyEvent.VK_ENTER:
					startInsert(selectedItem);
					break;
				case KeyEvent.VK_DELETE:
					startDelete(selectedItem);
					break;
				default:
					break;
				}
			}
		});
	}

	protected XListItem insert(XListItem item) {
		return item;
	}

	public void insertItem(XListItem itemToInsertBefore, XListItem item, XListItem[] children) {
		if (item == null) { return; }
		if (itemToInsertBefore == null) { return; }
		int index = model.indexOf(itemToInsertBefore);
		if (index < 0) { return; }

		if (children != null) {
			for (int i = children.length - 1; i >= 0; --i) {
				if (children[i] != null) {
					model.add(index, children[i]);
				}
			}
		}

		item.setParent(itemToInsertBefore.getParent());
		model.add(index, item);
	}

	protected XListItem modify(XListItem item) {
		return item;
	}

	private void popupMenu(XListItem item, MouseEvent e) {
		if (item == null) { return; }
		menuInsert.setItem(item);
		menuInsert.setEnabled((item.getChangeType() & XListItem.CAN_INSERT) != 0);

		menuModify.setItem(item);
		menuModify.setEnabled((item.getChangeType() & XListItem.CAN_MODIFY) != 0);

		menuDelete.setItem(item);
		menuDelete.setEnabled((item.getChangeType() & XListItem.CAN_DELETE) != 0);

		popup.show(this, e.getX(), e.getY());
	}

	public void replaceItem(XListItem itemToBeReplaced, XListItem item, XListItem[] children) {
		insertItem(itemToBeReplaced, item, children);
		deleteItem(itemToBeReplaced);
	}

	private void selfSelectionChanged() {
		selectedItem = null;
		unselectAllItems(); // 清除所有项的选择状态
		Object tmp = getSelectedValue();
		if (tmp != null) {
			if (tmp instanceof XListItem) {
				selectedItem = (XListItem) tmp;
				selectedItem.setSelected(true); // 设置自己的选择状态
				XListItem[] children = getChildren(selectedItem);
				if (children != null) {
					for (int i = 0; i < children.length; ++i) {
						children[i].setSelected(true); // 设置子项的选择状态
					}
				}
			}
		}
		repaint();
	}

	protected final void setItems(ArrayList items) {
		model.clear();
		if (items != null) {
			for (int i = 0; i < items.size(); ++i) {
				model.addElement(items.get(i));
			}
		}

		model.addElement(lastItem);
		selectedItem = null;
		popupping = false;
	}

	private boolean startDelete(XListItem item) {
		if (item == null) { return false; }
		if ((item.getChangeType() & XListItem.CAN_DELETE) != 0) {
			popupping = true;
			delete(item);
			popupping = false;
			return true;
		}
		else {
			return false;
		}
	}

	private boolean startInsert(XListItem item) {
		if (item == null) { return false; }
		if ((item.getChangeType() & XListItem.CAN_INSERT) != 0) {
			popupping = true;
			XListItem newItem = insert(item);
			popupping = false;
			if (newItem != null) {
				setSelectedIndex(model.indexOf(newItem));
			}
			return true;
		}
		else {
			return false;
		}
	}

	private boolean startModify(XListItem item) {
		if (item == null) { return false; }
		if ((item.getChangeType() & XListItem.CAN_MODIFY) != 0) {
			popupping = true;
			XListItem newItem = modify(item);
			popupping = false;
			if (newItem != null) {
				setSelectedIndex(model.indexOf(newItem));
			}
			return true;
		}
		else {
			return false;
		}
	}

	public void unselectAllItems() {
		Object tmp;
		for (int i = 0; i < model.size(); ++i) {
			tmp = model.get(i);
			if (tmp != null) {
				if (tmp instanceof XListItem) {
					((XListItem) tmp).setSelected(false);
				}
			}
		}
	}

}

class XListCellRenderer extends DefaultListCellRenderer {

	private final static String NORMAL_HEAD = "◆";
	private final static String SPECIAL_HEAD = "◇";
	private final static String LEVEL_SPACE = "      ";

	public Component getListCellRendererComponent(JList list, Object value, int index,
	        boolean isSelected, boolean cellHasFocus) {
		if (value != null) {
			if (value instanceof XListItem) {
				XListItem tmp = (XListItem) value;
				String newValue = "";
				XListItem parent = tmp.getParent();
				while (parent != null) {
					newValue += LEVEL_SPACE;
					parent = parent.getParent();
				}
				if (tmp.getChangeType() != XListItem.CANNT_CHANGE) {
					newValue += NORMAL_HEAD;
				}
				else {
					newValue += SPECIAL_HEAD;
				}
				newValue += tmp.toString();
				return super.getListCellRendererComponent(list, newValue, index, tmp.isSelected(),
				        cellHasFocus);
			}
		}
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}
}

class XListItem {

	public final static int CANNT_CHANGE = 0; // 不能改变
	public final static int CAN_DELETE = 0x01; // 删除
	public final static int CAN_MODIFY = 0x02; // 修改
	public final static int CAN_INSERT = 0x04; // 在前面插入
	public final static int CAN_CHANGE_ALL = 0xFF; // 全部功能
	public final static int DEFAULT_CHANGE_TYPE = CAN_CHANGE_ALL; // 默认为全部功能
	public final static String DEFAULT_STRING = "";

	public static XListItem[] getChildren(XListItem[] allItems, XListItem parent) {
		if (allItems == null || parent == null) { return null; }
		ArrayList tmp = new ArrayList();
		for (int i = 0; i < allItems.length; ++i) {
			XListItem item = allItems[i];
			if (item == null) {
				continue;
			}
			if (item.getParent() == parent) {
				tmp.add(item);
				XListItem[] children = XListItem.getChildren(allItems, item);
				if (children != null) {
					for (int j = 0; j < children.length; ++j) {
						tmp.add(children[j]);
					}
				}
			}
		}
		if (tmp.isEmpty()) { return null; }
		XListItem[] result = new XListItem[tmp.size()];
		for (int i = 0; i < tmp.size(); ++i) {
			result[i] = (XListItem) (tmp.get(i));
		}
		return result;
	}

	private XListItem parent;
	private Object value;
	private int changeType;
	private boolean selected;

	public XListItem(Object value) {
		init(null, value, DEFAULT_CHANGE_TYPE);
	}

	public XListItem(Object value, int changeType) {
		init(null, value, changeType);
	}

	public XListItem(XListItem parent, Object value) {
		init(parent, value, DEFAULT_CHANGE_TYPE);
	}

	public XListItem(XListItem parent, Object value, int changeType) {
		init(parent, value, changeType);
	}

	public int getChangeType() {
		return changeType;
	}

	public XListItem getParent() {
		return parent;
	}

	public Object getValue() {
		return value;
	}

	private void init(XListItem parent, Object value, int changeType) {
		this.parent = parent;
		this.value = value;
		this.changeType = changeType;
		this.selected = false;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setParent(XListItem parent) {
		this.parent = parent;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String toString() {
		if (value != null) {
			if (value instanceof Pair) { // 意味着 pair (realObject,description)
				return ((Pair) value).second.toString();
			}
			else {
				return value.toString();
			}
		}
		else {
			return DEFAULT_STRING;
		}
	}
}

class XTable extends JPanel {

	public final static int NUMBER = 0;
	public final static int COMBO = 1;
	public final static int STRING = 2;

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		f.setSize(new Dimension(800, 600));
		f.setLocationRelativeTo(null);

		f.addWindowListener(new WindowAdapter() {

			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});

		XTable t = new XTable("测试", new String[] { "C1", "C2", "C3", "C4", "C5", "C6" });
		t.setNumberCol(0);
		t.setComboCol(1, new int[] { 0, 1, 2, 3, 4 }, new String[] { "A0", "A1", "A2", "A3", "A4" });
		t.setNumberCol(2);
		t.setButtonTextCol(3, new ButtonTextActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println(value);
			}
		});
		t.setComboCol(4, new int[] { 2, 3, 4 }, new String[] { "B0", "B1", "B2", "B3" });
		t.setComboCol(5, new int[] { 2, 3, 4, 5, 6 }, new String[] { "C1", "C2", "C3", "C4" });

		t.getModel().addRow(
		        new Object[] { new Integer(0), new Integer(0), new Integer(0),
		                "斯多夫巨卡随机掉连发搜到；附件；阿三定界符； 阿三定界符看啦洒扫发", new Integer(0), new Integer(0) });

		Container cp = f.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(t, BorderLayout.CENTER);
		f.show();
	}

	private DefaultTableModel model;
	private JTable table;
	private TableColumnModel columnModel;
	protected Object[] copiedObjs;
	protected boolean copied;

	public XTable(String name, String[] colNames) {
		super();
		init(name, colNames, true, true);
	}

	public XTable(String name, String[] colNames, boolean hasAddButton, boolean hasCopyButton) {
		super();
		init(name, colNames, hasAddButton, hasCopyButton);
	}

	public void addRow() {
		int colCount = model.getColumnCount();
		int row = table.getSelectedRow();
		Object[] data = new Object[colCount];
		for (int col = 0; col < colCount; ++col) {
			data[col] = getDefaultColValue(col);
		}
		if (row >= 0 && row < model.getRowCount() - 1) {
			model.insertRow(row + 1, data);
		}
		else {
			model.addRow(data);
		}
	}

	public void copyRow() {}

	public TableColumnModel getColumnModel() {
		return columnModel;
	}

	protected Object getDefaultColValue(int col) {
		return new Integer(0);
	}

	public int getDefaultRowHeight() {
		int result = XUtil.getDefPropInt("DefaultTableRowHeight");
		if (result <= 0) result = 25;
		return result;
	}

	public DefaultTableModel getModel() {
		return model;
	}

	public JTable getTable() {
		return table;
	}

	private void init(String name, String[] colNames, boolean hasAddButton, boolean hasCopyButton) {
		model = new DefaultTableModel();
		for (int i = 0; i < colNames.length; ++i) {
			model.addColumn(colNames[i]);
		}
		table = new JTable(model);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setRowHeight(getDefaultRowHeight());
		columnModel = table.getColumnModel();

		copiedObjs = new Object[colNames.length];
		copied = false;

		// scrollPane
		JScrollPane scrollPane = new JScrollPane(table);
		SwingUtil.setDefScrollIncrement(scrollPane);

		// buttons
		JButton addButton = new JButton("添加");
		addButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				addRow();
			}
		});

		JButton removeButton = new JButton("删除");
		removeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				removeRow();
			}
		});

		JButton copyButton = new JButton("复制");
		copyButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				copyRow();
			}
		});

		JButton pasteButton = new JButton("粘贴");
		pasteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				pasteRow();
			}
		});

		this.setLayout(new BorderLayout());
		this.add(new JLabel(name), BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);

		// buttonPanel
		if (hasAddButton || hasCopyButton) {
			JPanel buttonPanel = new JPanel();
			int col = 2;
			if (hasAddButton && hasCopyButton) {
				col = 4;
			}
			buttonPanel.setLayout(new GridLayout(1, col, 5, 5));
			if (hasAddButton) {
				buttonPanel.add(addButton);
				buttonPanel.add(removeButton);
			}
			if (hasCopyButton) {
				buttonPanel.add(copyButton);
				buttonPanel.add(pasteButton);
			}
			this.add(buttonPanel, BorderLayout.SOUTH);
		}
	}

	public void pasteRow() {
		if (!copied) return;
		int row = table.getSelectedRow();
		if (row >= 0 && row < model.getRowCount() - 1) {
			model.insertRow(row + 1, copiedObjs);
		}
		else {
			model.addRow(copiedObjs);
		}
		copied = false;

	}

	public void removeRow() {
		stopEditing();
		int row = table.getSelectedRow();
		if (row >= 0 && row < model.getRowCount()) {
			model.removeRow(row);
		}
		if (row > model.getRowCount() - 1) {
			row = model.getRowCount() - 1;
		}
		if (row >= 0 && row < model.getRowCount()) {
			table.setRowSelectionInterval(row, row);
			table.requestFocus();
		}
	}

	public void setButtonTextCol(int colIndex, ActionListener actionListener) {
		TableColumn column = columnModel.getColumn(colIndex);
		column.setCellRenderer(new ButtonTextTableCellRenderer());
		column.setCellEditor(new ButtonTextTableCellEditor(actionListener));
	}

	public void setComboCol(int colIndex, int[] defValues, String[] defDescs) {
		TableColumn column = columnModel.getColumn(colIndex);
		column.setCellRenderer(new ComboTableCellRenderer(0, defValues, defDescs));
		column.setCellEditor(new ComboTableCellEditor(0, defValues, defDescs));
	}

	public void setComboCol(int colIndex, int[] defValues, String[] defDescs, int type) {
		TableColumn column = columnModel.getColumn(colIndex);
		column.setCellRenderer(new ComboTableCellRenderer(0, defValues, defDescs, type));
		column.setCellEditor(new ComboTableCellEditor(0, defValues, defDescs, type));
	}
	
	public void resetComboCol(int colIndex, int[] defValues, String[] defDescs) {
		TableColumn column = columnModel.getColumn(colIndex);
		ComboTableCellRenderer renderer = (ComboTableCellRenderer)column.getCellRenderer();
		ComboTableCellEditor editor = (ComboTableCellEditor)column.getCellEditor();
		renderer.reset(defValues, defDescs);
		editor.reset(defValues, defDescs);
	}
	
	public void resetComboCol(int colIndex, int[] defValues, String[] defDescs, int type) {
		TableColumn column = columnModel.getColumn(colIndex);
		ComboTableCellRenderer renderer = (ComboTableCellRenderer)column.getCellRenderer();
		ComboTableCellEditor editor = (ComboTableCellEditor)column.getCellEditor();
		renderer.reset(defValues, defDescs, type);
		editor.reset(defValues, defDescs, type);
	}

	public void setNumberCol(int colIndex) {
		TableColumn column = columnModel.getColumn(colIndex);
		column.setCellRenderer(new SpinnerTableCellRenderer());
		column.setCellEditor(new SpinnerTableCellEditor());
	}

	public void stopEditing() {
		TableCellEditor editor = table.getCellEditor();
		if (editor != null) {
			editor.stopCellEditing();
		}
	}
}

class PMStateList extends StateList {

	public final static int STATE_NONE = 0;
	public final static int STATE_PLUS = 1;
	public final static int STATE_MINUS = 2;
	public final static int[] STATE_VALUES = { STATE_NONE, STATE_PLUS, STATE_MINUS };

	public PMStateList(String[] itemNames) {
		super(STATE_VALUES, itemNames, 0);
	}

	protected void drawState(int state, Graphics g, int width, int height) {
		int l = 1;
		int t = 1;
		int w = width - 3;
		int h = height - 3;
		g.setColor(new Color(28, 81, 128));
		g.drawRect(l, t, w, h);
		
		switch (state) {
		case STATE_PLUS:
			g.setColor(new Color(167, 33, 33));
			g.fillRect(l + 2, t + h / 2, w - 3, 2);
			g.fillRect(l + w / 2, t + 2, 2, h - 3);
			break;
		case STATE_MINUS:
			g.setColor(new Color(33, 65, 167));
			g.fillRect(l + 2, t + h / 2, w - 3, 2);
			break;
		}
	}

	protected int getItemValue(int index) {
		int result = index + 1;
		return result;
	}

	public int[] getStates() {
		int[] states = super.getStates();
		ArrayList tmp = new ArrayList();
		for (int i = 0; i < states.length; ++i) {
			int itemValue = getItemValue(i);
			switch (states[i]) {
			case STATE_PLUS:
				tmp.add(new Integer(itemValue));
				break;
			case STATE_MINUS:
				tmp.add(new Integer(-itemValue));
				break;
			}
		}
		int[] result = null;
		if (tmp.size() > 0) {
			result = new int[tmp.size()];
			for (int i = 0; i < tmp.size(); ++i) {
				result[i] = ((Integer) (tmp.get(i))).intValue();
			}
		}
		return result;
	}

	public void setStates(int[] states) {
		int[] sts = new int[itemNames.length];
		for (int i = 0; i < itemNames.length; ++i) {
			sts[i] = STATE_NONE;
			if(states != null) {
				int itemValue = getItemValue(i);
				for (int j = 0; j < states.length; ++j) {
					if (states[j] == itemValue) {
						sts[i] = STATE_PLUS;
						break;
					}
					else if (states[j] == -itemValue) {
						sts[i] = STATE_MINUS;
						break;
					}
				}
			}
		}
		super.setStates(sts);
	}
}


class CheckList extends StateList {

	public final static int[] STATE_VALUES = { 0, 1 };
	
	public CheckList(String[] itemNames) {
		super(STATE_VALUES, itemNames, 0);
	}

	protected void drawState(int state, Graphics g, int width, int height) {	
		int l = 1;
		int t = 1;
		int w = width - 3;
		int h = height - 3;
		g.setColor(new Color(28, 81, 128));
		g.drawRect(l, t, w, h);
				
		if(state != 0) {
			l += 2;
			t += 2;
			w -= 4;
			h -= 4;
			g.setColor(new Color(65, 167, 33));
			g.drawLine(l, t + (int)(h  * 0.77), l + (int)(h * 0.1), t + (int)(h * 0.67));
			g.drawLine(l + (int)(h * 0.1), t + (int)(h * 0.67), l + (int)(h * 0.33), t + h);
			g.drawLine(l + (int)(h * 0.33), t + h, l + w, t + (int)(h * 0.15));
		}
	}

	protected int getItemValue(int index) {
		return index;
	}

	public int[] getStates() {
		int[] states = super.getStates();
		ArrayList tmp = new ArrayList();
		for (int i = 0; i < states.length; ++i) {
			int itemValue = getItemValue(i);
			if (states[i] != 0) {
				tmp.add(new Integer(itemValue));
			}
		}
		int[] result = null;
		if (tmp.size() > 0) {
			result = new int[tmp.size()];
			for (int i = 0; i < tmp.size(); ++i) {
				result[i] = ((Integer) (tmp.get(i))).intValue();
			}
		}
		return result;
	}

	public void setStates(int[] states) {
		int[] sts = new int[itemNames.length];
		for (int i = 0; i < itemNames.length; ++i) {
			sts[i] = 0;
			if(states != null) {
				int itemValue = getItemValue(i);
				for (int j = 0; j < states.length; ++j) {
					if (states[j] == itemValue) {
						sts[i] = 1;
						break;
					}
				}
			}
		}
		super.setStates(sts);
	}
}