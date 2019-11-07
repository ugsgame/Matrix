package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

public class LayerManager {
	
	int maxId;
	int selectIndex;
	LayerPanel panel;
	MouseInfo mouseInfo;
	public ArrayList<Layer> layers;
	
	public LayerManager(MouseInfo mouseInfo){
		panel = new LayerPanel();
		layers = new ArrayList<Layer>();
		this.mouseInfo = mouseInfo;
		this.selectIndex = -1;
		this.maxId = 0;
		init();
	}
	
	private void init() {
		setLayerManager(this);
		// test
//		if(layers.size() == 0) {
//			Layer layer = addLayer("0");
//			
//			panel.iconsPanel.setEnabledAll(true);
//		}
	}
	
	private void delFile(int index) {
		String name = MainFrame.self.getMapInfo().getName();
		File fm = new File(XUtil.getDefPropStr("SaveMobilePath"));
		if(!fm.exists()) fm = new File(".\\savem");
		fm = new File(fm.getAbsoluteFile() + "\\" + name + "_map_" + index + ".dat");
		if(fm.exists()) {
			fm.delete();
		}
	}
	
	public void reset() {
		layers.clear();
		setLayerManager(this);
	}
	
	public String getLayerName(int layer) {
		for(int i=0; i<layers.size(); ++i) {
			if(layers.get(i).id == layer)
				return layers.get(i).name;
		}
		
		return "";
	}
	
	public boolean getLayerIsShow(int layer) {
		int index = -1;
		for(int i=0; i<layers.size(); ++i) {
			if(layers.get(i).id == layer) {
				index = i;
				break;
			}
		}
		if(index >= 0)
			return panel.lmt.getLayerIsShow(index);
		else
			return false;
	}
	
	public Layer getLayer(int index) {
		return layers.get(index);
	}
	
	public Layer getLayerOfLayer(int layer) {
		for(int i=0; i<layers.size(); ++i) {
			if(layers.get(i).id == layer) {
				return layers.get(i);
			}
		}
		
		return null;
	}
	
	public void setLayerManager(LayerManager lm) {
		panel.lmt.setLayerManager(lm);
		mouseInfo.setLayer(-1);
	}
	
	public Layer addLayer(String name, int left, int top, int w, int h) {
		int len = layers.size();
		Layer layer = new Layer(maxId++, 0, name);
		layer.setLeft(left);
		layer.setTop(top);
		layer.setWidth(w);
		layer.setHeight(h);
		
//		for(int i=0; i<len; ++i) {
//			Layer _layer = layers.get(i);
//			_layer.setLayer(_layer.layer+1);
//		}
		layers.add(0, layer);
		setSelectLayer(0);
		
		for(int i=0; i<layers.size(); ++i) {
			Layer _layer = layers.get(i);
			_layer.setLayer(i);
		}		
		
		return layer;
	}
	
	public void delLayer(int index) {
		int id = layers.get(index).id;
		ManagerPanel[] panels = MainFrame.self.getPanels();
		for(int i=0; i<2; ++i) {
			ArrayList sprites = ((SpriteManager)panels[i].getManager()).getRealSprites();
			if(sprites != null) {
				for(int j=sprites.size()-1; j>=0; --j) {
					if(id == ((SISprite)sprites.get(j)).getLayer2())
						sprites.remove(j);
				}
			}
			((SpriteManagerPanel)panels[i]).repaint();
		}
		
		for(int i=2; i<4; ++i) {
			ArrayList sprites = ((ShapeManager)panels[i].getManager()).getRealShapes();
			if(sprites != null) {
				for(int j=sprites.size()-1; j>=0; --j) {
					if(id == ((Shape)sprites.get(j)).getLayer())
						sprites.remove(j);
				}
			}
			((ShapeManagerPanel)panels[i]).repaint();
		}
		
		delFile(layers.size()-1);
		layers.remove(index);
		if(selectIndex >= layers.size())
			selectIndex = layers.size()-1;
		
		for(int i=0; i<layers.size(); ++i) {
			Layer _layer = layers.get(i);
			_layer.setLayer(i);
		}			
		
	}
	
	public void setLayerIsShow(int index, boolean isShow) {
		int id = layers.get(index).id;
		ManagerPanel[] panels = MainFrame.self.getPanels();
		for(int i=0; i<2; ++i) {
			Sprite[] sprites = ((SpriteManager)panels[i].getManager()).getSprites();
			if(sprites != null) {
				for(int j=0; j<sprites.length; ++j) {
			//		System.out.println(index+", "+((SISprite)sprites[j]).getLayer2());
					if(id == ((SISprite)sprites[j]).getLayer2())
						((SISprite)sprites[j]).setVisible(isShow);
				}
			}
			((SpriteManagerPanel)panels[i]).repaint();
		}
		
		for(int i=2; i<4; ++i) {
			Shape[] sprites = ((ShapeManager)panels[i].getManager()).getShapes();
			if(sprites != null) {
				for(int j=0; j<sprites.length; ++j) {
			//		System.out.println(j+", "+((Shape)sprites[j]).getLayer());
					if(id == ((Shape)sprites[j]).getLayer())
						((Shape)sprites[j]).setVisible(isShow);
				}
			}
			((ShapeManagerPanel)panels[i]).repaint();
		}
	}
	
	public void moveDown(int index) {
		if(index < layers.size()-1) {
			Layer layer = layers.remove(index);
			//layer.setLayer(index+1);
			layers.add(index+1, layer);
			//
			System.out.println("change layer");
			for(int i=0;i< layers.size();i++)
			{
				Layer slayer = layers.get(i);
				slayer.setLayer(i);
				System.out.println("name:"+slayer.name);
				System.out.println("index:"+slayer.layer);
			}
		}
	}

	public void moveUp(int index) {
		if(index > 0) {
			Layer layer = layers.remove(index);
			//layer.setLayer(index-1);
			layers.add(index-1, layer);
			
			System.out.println("change layer");
			for(int i=0;i< layers.size();i++)
			{
				Layer slayer = layers.get(i);
				slayer.setLayer(i);
				System.out.println("name:"+slayer.name);
				System.out.println("index:"+slayer.layer);
			}			
			
		}
	}

	public void setSelectLayer(int index) {
		if(index >= layers.size() || index < 0) {
			return;
		}
		
		Layer layer = layers.get(index);
		this.selectIndex = index;
		mouseInfo.setLayer(layer.id);
		
		if(MainFrame.self.getTabIndex() < MainFrame.LAYER_MAX_LAYER) {
			MainFrame.self.getMapInfo().setDrawInfo(layer.left, layer.top, layer.w, layer.h);
			MainFrame.self.updatePanelSize();
		}
		
		ManagerPanel[] panels = MainFrame.self.getPanels();
		for(int i=0; i<2; ++i) {
			((SpriteManager)panels[i].getManager()).clearSelection();
			((SpriteManager)panels[i].getManager()).setSelectionLayer(layer.id);
			((SpriteManagerPanel)panels[i]).repaint();
		}
		
		for(int i=2; i<4; ++i) {
			((ShapeManager)panels[i].getManager()).clearSelection();
			((ShapeManager)panels[i].getManager()).setSelectionLayer(layer.id);
			((ShapeManagerPanel)panels[i]).repaint();
		}
	}

	public void setLayerName(int index, String name) {
		layers.get(index).setName(name);
	}
	
	public void saveMobile(DataOutputStream out, int index) throws Exception {
		Layer layer = layers.get(index);
		SL.writeInt(layer.left, out);
		SL.writeInt(layer.w, out);
		SL.writeInt(layer.top, out);
		SL.writeInt(layer.h, out);
		out.writeByte(0);
		out.writeByte(0);
		out.writeByte(0);
	}

	public void save(String fileName) throws Exception {
		File f = new File(XUtil.getDefPropStr("SavePath") + "\\" + fileName + "_layer.dat");
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
		        new FileOutputStream(f)));
		
		out.writeInt(maxId);
		out.writeInt(layers.size());
		for(int i=0; i<layers.size(); ++i) {
			Layer layer = layers.get(i);
			out.writeInt(layer.id);
			out.writeInt(layer.layer);
			SL.writeString(layer.name, out);
			out.writeInt(layer.left);
			out.writeInt(layer.top);
			out.writeInt(layer.w);
			out.writeInt(layer.h);
		}
		
		out.flush();
		out.close();
	}

	public void load(String fileName) throws Exception {
		File f = new File(XUtil.getDefPropStr("SavePath") + "\\" + fileName + "_layer.dat");
		if (!f.exists()) return;
		DataInputStream in = new DataInputStream(
		        new BufferedInputStream(new FileInputStream(f)));
		
		layers.clear();
		maxId = in.readInt();
		int len = in.readInt();
		for(int i=0; i<len; ++i) {
			int id = in.readInt();
			int l = in.readInt();
			String name = SL.readString(in);
			Layer layer = new Layer(id, l, name);
			int left = in.readInt();
			int top = in.readInt();
			int w = in.readInt();
			int h = in.readInt();
			layer.setLeft(left);
			layer.setTop(top);
			layer.setWidth(w);
			layer.setHeight(h);
			
			layers.add(i, layer);
		}
		setLayerManager(this);
		in.close();
	}

	public class Layer {
		
		int id;
		int layer;
		int w;
		int h;
		int left;
		int top;
		String name;
		public Layer(int id, int layer, String name) {
			this.id = id;
			this.layer = layer;
			this.name = name;
			this.left = 0;
			this.top = 0;
			this.w = 0;
			this.h = 0;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public void setLayer(int layer) {
			this.layer = layer;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public void setLeft(int left) {
			this.left = left;
		}
		
		public void setTop(int top) {
			this.top = top;
		}
		
		public void setWidth(int w) {
			this.w = w;
		}
		
		public void setHeight(int h) {
			this.h = h;
		}
	}
	
	private class LayerPropDialog extends OKCancelDialog {

		private JTextField nameText;
		private NumberSpinner realLeftSpinner;
		private NumberSpinner realWidthSpinner;
		private NumberSpinner realTopSpinner;
		private NumberSpinner realHeightSpinner;
		
		Layer layerInfo;

		public LayerPropDialog(JFrame owner) {
			super(owner);
			init();
		}

		private void init() {
			layerInfo = new Layer(0, 0, "");
			
			MapInfo mapInfo = MainFrame.self.getMapInfo();
			nameText = new JTextField("");
			realLeftSpinner = new NumberSpinner();
			realLeftSpinner.setIntValue(mapInfo.getRealLeft());
			realWidthSpinner = new NumberSpinner();
			realWidthSpinner.setIntValue(mapInfo.getRealWidth());
			realTopSpinner = new NumberSpinner();
			realTopSpinner.setIntValue(mapInfo.getRealTop());
			realHeightSpinner = new NumberSpinner();
			realHeightSpinner.setIntValue(mapInfo.getRealHeight());

			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weighty = 0;
			c.insets = new Insets(5, 5, 5, 5);

			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0;
			centerPanel.add(new JLabel("名称："), c);

			c.gridx = 1;
			c.weightx = 1;
			centerPanel.add(nameText, c);

			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 0;
			centerPanel.add(new JLabel("左边空隙："), c);

			c.gridx = 1;
			c.weightx = 1;
			centerPanel.add(realLeftSpinner, c);

			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 0;
			centerPanel.add(new JLabel("实际宽度："), c);

			c.gridx = 1;
			c.weightx = 1;
			centerPanel.add(realWidthSpinner, c);

			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 0;
			centerPanel.add(new JLabel("上边空隙："), c);

			c.gridx = 1;
			c.weightx = 1;
			centerPanel.add(realTopSpinner, c);

			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 0;
			centerPanel.add(new JLabel("实际高度："), c);

			c.gridx = 1;
			c.weightx = 1;
			centerPanel.add(realHeightSpinner, c);
			
			buttonPanel.add(okButton);
			buttonPanel.add(cancelButton);

			Container cp = this.getContentPane();
			cp.setLayout(new BorderLayout());
			cp.add(centerPanel, BorderLayout.CENTER);
			cp.add(buttonPanel, BorderLayout.SOUTH);
		}
		
		public void setLayerInfo(Layer layerInfo) {
			nameText.setText(layerInfo.name);
			realLeftSpinner.setIntValue(layerInfo.left);
			realWidthSpinner.setIntValue(layerInfo.w);
			realTopSpinner.setIntValue(layerInfo.top);
			realHeightSpinner.setIntValue(layerInfo.h);
		}

		public void updateLayerInfo() {
			layerInfo.setName(nameText.getText());
			layerInfo.setLeft(realLeftSpinner.getIntValue());
			layerInfo.setWidth(realWidthSpinner.getIntValue());
			layerInfo.setTop(realTopSpinner.getIntValue());
			layerInfo.setHeight(realHeightSpinner.getIntValue());
		}

		public void okPerformed() {
			updateLayerInfo();
			closeType = OK_PERFORMED;
			dispose();
		}

		public void cancelPerformed() {
			dispose();
		}
	}
	
	private class LayerPanel extends JPanel implements ActionListener {
		
		public final String[][] PathList_Layer = {
			{ "icon/1.png", "新建图层" },
			{ "icon/2.png", "向上移动" },
			{ "icon/8.png", "向下移动" },
			{ "icon/3.png", "复制图层" },
			{ "icon/4.png", "删除图层" },
			{ "icon/5.png", "显示/隐藏其他图层" },
		};
		
		private IconsPanel iconsPanel;
		private IconsActionListener iconsActionListener;
		
		private LayerManagerTable lmt;
		private JScrollPane showScroll;
		
		private JPopupMenu popMenu;
		private JMenuItem changeItem;
		private JMenuItem savePNG;
		
		public LayerPanel() {
			setLayout(new BorderLayout());
			JLabel label = new JLabel("图层");
			add(label, BorderLayout.NORTH);
			
			iconsPanel = new IconsPanel();
			iconsActionListener = new IconsActionListener();
			for(int i=0; i<PathList_Layer.length; ++i) {
				IconButton b = new IconButton(PathList_Layer[i][0], PathList_Layer[i][1]);
				b.addActionListener(iconsActionListener);
				iconsPanel.addButton(b);
			}
	//		iconsPanel.setEnabledAll(false);
			
			lmt = new LayerManagerTable();
			showScroll = new JScrollPane(lmt);//, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			showScroll.setMinimumSize(new Dimension(100, 200));
			add(showScroll, BorderLayout.CENTER);
			add(iconsPanel, BorderLayout.SOUTH);
			
			popMenu = new JPopupMenu();
			changeItem = new JMenuItem("属性");
			changeItem.addActionListener(this);
			savePNG = new JMenuItem("保存PNG");
			savePNG.addActionListener(this);
			popMenu.add(changeItem);
			popMenu.add(savePNG);
			
			lmt.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON3){
						showPopMenu(e.getX(), e.getY());
					}
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					
				}
			});
		}
		
		public void showPopMenu(int x, int y) {
			if(selectIndex >= 0) {
				popMenu.show(this, x, y);
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(selectIndex >= 0) {
				if(e.getSource() == changeItem) {
					Layer layer = layers.get(selectIndex);
					LayerPropDialog setter = new LayerPropDialog(MainFrame.self);
					setter.setTitle("设置图层属性");
					setter.setLayerInfo(layer);
					setter.show();
					if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
						Layer layer2 = setter.layerInfo;
						layer.setName(layer2.name);
						layer.setLeft(layer2.left);
						layer.setTop(layer2.top);
						layer.setWidth(layer2.w);
						layer.setHeight(layer2.h);
						
						lmt.setValueAt(layer2.name, selectIndex, 1);
						MainFrame.self.getMapInfo().setDrawInfo(layer.left, layer.top, layer.w, layer.h);
						MainFrame.self.updatePanelSize();
						MainFrame.self.repaintPanel();
					}
				}
				else if(e.getSource() == savePNG) {
					Layer layer = layers.get(selectIndex);
					MainFrame.self.savePNG(layer.id);
				}
			}
		}
		
		public void addLayer() {
			LayerPropDialog setter = new LayerPropDialog(MainFrame.self);
			setter.setTitle("设置图层属性");
			setter.show();
			if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
				Layer layer = setter.layerInfo;
				lmt.insertLayer(layer.name, layer.left, layer.top, layer.w, layer.h);
			}
		}
		
		public void moveUp() {
			lmt.moveUp();
		}
		
		public void moveDown() {
			lmt.moveDown();
		}
		
		public void copyLayer() {
			
		}
		
		public void delLayer() {
			lmt.delLayer();
		}
		
		public void setIsShowOtherLayer() {
			lmt.setIsShowOtherLayer();
		}
		
		private class IconsActionListener implements ActionListener {			
			public void actionPerformed(ActionEvent e) {
				int index = iconsPanel.getPressedIndex(e);
				lmt.removeEditor();
				lmt.requestFocus();
				switch(index) {
					case 0:
						addLayer();
						break;
					case 1:
						//TODO:移位后地图导出顺不正确，先禁止掉
						//moveUp();				
						break;
					case 2:
						//TODO:移位后地图导出顺不正确，先禁止掉
						//moveDown();
						break;
					case 3:
						copyLayer();
						break;
					case 4:
						delLayer();
						break;
					case 5:
						setIsShowOtherLayer();
						break;
				}
			}
		}
		
		private class LayerManagerTable extends JTable {
			
			LayerManager lm;
			MyTableModel myDataModel;
			boolean isInit;
			boolean isShowOthers;
			
			public LayerManagerTable() {
				myDataModel = new MyTableModel();
				setModel(myDataModel);
				setRowHeight(25);
				putClientProperty("terminateEditOnFocusLost", true);
				
				for(int i=0; i<1; ++i) {
					int w = 25+i*6;
					TableColumn firsetColumn = getColumnModel().getColumn(i);
					firsetColumn.setPreferredWidth(w);
					firsetColumn.setMaxWidth(w);
					firsetColumn.setMinWidth(w);
				}
				
				setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				ListSelectionModel rowSM = getSelectionModel();
		        rowSM.addListSelectionListener(new ListSelectionListener() {
		            public void valueChanged(ListSelectionEvent e) {
		                //Ignore extra messages.
		                if (e.getValueIsAdjusting()) return;

		                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		                if (lsm.isSelectionEmpty()) {
		      //          	System.out.println("No rows are selected.");
		                } else {
		                	int selectedRow = lsm.getMinSelectionIndex();
		                	setSelectLayer(selectedRow);
		     //           	System.out.println("Row " + selectedRow + " is now selected.");
		                }
		            }
		        });
		        
		        myDataModel.addTableModelListener(new TableModelListener() {
					
					@Override
					public void tableChanged(TableModelEvent e) {
						if(isInit) return;
						
						int row = e.getFirstRow();
						int col = e.getColumn();
						Object data = getValueAt(row, col);
						if(col == 0) {
							setLayerIsShow(row, new Boolean(data.toString()).booleanValue());
						}
						else if(col == 1) {
							setLayerName(row, data.toString());
						}
				//		System.out.println("change("+row+", "+col+"): "+data.toString());
					}
				});
			}
			
			public void setLayerManager(LayerManager lm) {
				isInit = true;
				clear();
				isInit = false;
				
				this.lm = lm;
				this.isShowOthers = false;
				init();
			}
			
			private void init() {
				isInit = true;
				ArrayList<Layer> layers = lm.layers;
				for(int i=layers.size()-1; i>=0; --i) {
					myDataModel.addOneRow(layers.get(i).name);
				}
				UpdateUI();
				isInit = false;
			}
			
			private void clear() {
				 myDataModel.delAllRow();
				 UpdateUI();
			}
			
			public void UpdateUI() {
				SwingUtilities.invokeLater(new Runnable() {
					 public void run() {
						 updateUI();
					 }
				});
			}
			
			public boolean isInit() {
				return lm != null;
			}
			
			public boolean getLayerIsShow(int index) {
				if(lm == null) return false;
				return new Boolean(myDataModel.getValueAt(index, 0).toString()).booleanValue();
			}
			
			public void insertLayer(String name, int left, int top, int w, int h) {
				if(lm == null) return;
				if(isEditing())
					removeEditor();
				if(name.equals(""))
					name = "layer"+maxId;
				Layer tl = lm.addLayer(name, left, top, w, h);
				myDataModel.addOneRow(tl.name);
				addRowSelectionInterval(tl.layer, tl.layer);
//				editCellAt(tl.layer, 1);
//				JTextField tf = (JTextField)getCellEditor().getTableCellEditorComponent(this, tl.name, true, tl.layer, 1);
//				tf.requestFocus();
//				tf.selectAll();
//				tf.addFocusListener(new FocusListener() {
//					public void focusLost(FocusEvent e) {
//				//		editingStopped(null);
//				//		removeEditor();
//					}
//					public void focusGained(FocusEvent e) {
//					}
//				});
			}
			
			public void removeLayer(int index) {
				if(lm == null) return;
				lm.delLayer(index);
				myDataModel.delOneRow(index);
			}

			public void setLayerIsShow(int index, boolean isShow) {
				if(lm == null) return;
				lm.setLayerIsShow(index, isShow);
				MainFrame.self.repaintPanel();
			}
			
			public void setLayerName(int index, String name) {
				if(lm == null) return;
				lm.setLayerName(index, name);
			}
			
			public void setSelectLayer(int index) {
				if(lm == null) return;
				lm.setSelectLayer(index);
				isShowOthers = false;
			}
			
			public void moveUp() {
				if(lm == null) return;
				int index = getSelectedRow();
				if(index > 0) {
					lm.moveUp(index);
					myDataModel.moveUp(index);
					addRowSelectionInterval(index-1, index-1);
					MainFrame.self.repaintPanel();
				}
			}
			
			public void moveDown() {
				if(lm == null) return;
				int index = getSelectedRow();
				if(index < getRowCount()-1) {
					lm.moveDown(index);
					myDataModel.moveDown(index);
					addRowSelectionInterval(index+1, index+1);
					MainFrame.self.repaintPanel();
				}
			}
			
			public void copyLayer() {
				
			}
			
			public void delLayer() {
				if(lm == null) return;
				int index = getSelectedRow();
				if(index >= 0) {
					lm.delLayer(index);
					myDataModel.delOneRow(index);
					if(lm.selectIndex >= 0)
						addRowSelectionInterval(lm.selectIndex, lm.selectIndex);
					else
						getSelectionModel().clearSelection();
					MainFrame.self.repaintPanel();
				}
			}
			
			public void setIsShowOtherLayer() {
				if(lm == null) return;
				int index = getSelectedRow();
				if(index >= 0) {
					boolean isShow = new Boolean(getValueAt(index, 0).toString()).booleanValue();
					isShow = !isShow;
					for(int i=0; i<getRowCount(); ++i) {
						if(i != index)
							setValueAt(new Boolean(isShowOthers), i, 0);
						else
							setValueAt(new Boolean(true), i, 0);
					}
					isShowOthers = !isShowOthers;
				}
			}
			
			public int getLayerNum() {
				return getRowCount();
			}
			
			public void saveLua(String name) throws Exception{
				if(lm == null) return;
				
				StringBuffer tilesLua = new StringBuffer();
				tilesLua.append("\r\n\r\n--Tiles, 对象层3个数据一组, 为id, x, y; 图层数据2个一组, 为id, gridId\r\ntiles={\r\n");
						
				tilesLua.append("}");
				
				String s = tilesLua.toString();
				String scriptPath = XUtil.getDefPropStr("ScriptPath");
				FileWriter writer = new FileWriter(scriptPath + "\\" + name + ".lua", true);
				writer.write(s);
				writer.flush();
				writer.close();
			}
			
			class MyTableModel extends AbstractTableModel {
				
				private String[] columnNames = {"", ""};
		        private Object[][] data = {
//		        		{new Boolean(true), new ImageIcon("icon/save.png"), "Kathy"},
//		        		{new Boolean(true), new ImageIcon("icon/save.png"), "Kathy"},
//		        		{new Boolean(true), new ImageIcon("icon/save.png"), "Kathy"},
//		        		{new Boolean(true), new ImageIcon("icon/save.png"), "Kathy"},
		        };

		        public int getColumnCount() {
		            return columnNames.length;
		        }

				public int getRowCount() {
		            return data.length;
		        }

		        public String getColumnName(int col) {
		            return columnNames[col];
		        }

		        public Object getValueAt(int row, int col) {
		        	if(row < data.length)
		        		return data[row][col];
		        	return null;
		        }
		        
		        /*
		         * JTable uses this method to determine the default renderer/
		         * editor for each cell.  If we didn't implement this method,
		         * then the last column would contain text ("true"/"false"),
		         * rather than a check box.
		         */
		        public Class getColumnClass(int c) {
		        	Object obj = getValueAt(0, c);
		        	if(obj != null)
		        		return obj.getClass();
		        	return null;
		        }
		        
		        public boolean isCellEditable(int row, int col) {
		            //Note that the data/cell address is constant,
		            //no matter where the cell appears onscreen.
		            return true;
		        }
		        
		        public void setValueAt(Object value, int row, int col) {
		            data[row][col] = value;
		            fireTableCellUpdated(row, col);
		        }
		        
		        private void setCapacity() {
		        	Object[][] tmp = new Object[data.length+1][];
		        	for(int i=0; i<data.length; ++i) {
		        		tmp[i+1] = new Object[data[i].length];
		        		for(int j=0; j<data[i].length; ++j) {
		        			tmp[i+1][j] = data[i][j];
		        		}
		        	}
		        	tmp[0] = new Object[columnNames.length];
		        	data = null;
		        	data = tmp;
		        }
		        
		        public void moveDown(int index) {
					if(index < data.length-1) {
						Object[] tmp = new Object[columnNames.length];
						for(int i=0; i<columnNames.length; ++i)
							tmp[i] = data[index+1][i];
						for(int i=0; i<columnNames.length; ++i)
							data[index+1][i] = data[index][i];
						for(int i=0; i<columnNames.length; ++i)
							data[index][i] = tmp[i];
						UpdateUI();
					}
				}

				public void moveUp(int index) {
					if(index > 0) {
						Object[] tmp = new Object[columnNames.length];
						for(int i=0; i<columnNames.length; ++i)
							tmp[i] = data[index-1][i];
						for(int i=0; i<columnNames.length; ++i)
							data[index-1][i] = data[index][i];
						for(int i=0; i<columnNames.length; ++i)
							data[index][i] = tmp[i];
						UpdateUI();
					}
				}
		        
		        public void addOneRow(String layerName) {
		        	setCapacity();
		    		setValueAt(new Boolean(true), 0, 0);
		    		setValueAt(layerName, 0, 1);
		    		if(!isInit)
		    			UpdateUI();
		    	}
		        
		        public void delOneRow(int index) {
		        	if(data.length > 0) {
		        		Object[][] tmp = new Object[data.length-1][];
		        		for(int i=0; i<index; ++i) {
		            		tmp[i] = new Object[data[i].length];
		            		for(int j=0; j<data[i].length; ++j) {
		            			tmp[i][j] = data[i][j];
		            		}
		            	}
		        		for(int i=index+1; i<data.length; ++i) {
		            		tmp[i-1] = new Object[data[i].length];
		            		for(int j=0; j<data[i].length; ++j) {
		            			tmp[i-1][j] = data[i][j];
		            		}
		            	}
		        		data = null;
		            	data = tmp;
		            	UpdateUI();
		        	}
		        }
		        
		        public void delAllRow() {
		        	data = null;
		        	data = new Object[][]{};
		        }
		    }
		}
	}
}
