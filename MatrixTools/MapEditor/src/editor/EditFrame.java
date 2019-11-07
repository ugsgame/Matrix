package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;

public class EditFrame {
	
	public static int TI_OffsetX = 0;
	public static int TI_OffsetY = 20;
	
	public static String[][] PathList_Pic = {
		{ "icon/save.png", "��ͼƬ" },
	};
	
	public static String[][] PathList_Layer = {
		{ "icon/1.png", "�½�ͼ��" },
		{ "icon/2.png", "�����ƶ�" },
		{ "icon/8.png", "�����ƶ�" },
		{ "icon/3.png", "����ͼ��" },
		{ "icon/4.png", "ɾ��ͼ��" },
		{ "icon/5.png", "��ʾ/��������ͼ��" },
	};	
	
	LayerPanel layerPanel;
	ProPanel   proPanel;
	JSplitPane splitPane;
	
	public EditFrame() {
		layerPanel = new LayerPanel();
		proPanel   = new ProPanel();
		JScrollPane scrollPane = new JScrollPane(proPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//scrollPane.addMouseListener(proPanel);
		scrollPane.setPreferredSize(new Dimension(100, 500));
		
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, layerPanel, scrollPane);	
	}	
	
   private class ProPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	   public  ProPanel()
	   {
		   
	   }
   }

	private class LayerPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8086692990326985403L;
		private IconsPanel iconsPanel;
		private IconsActionListener iconsActionListener;
		
		private TileLayerManagerTable tlmTable;
		private JScrollPane showScroll;
		
		public LayerPanel() {
			setLayout(new BorderLayout());
			JLabel label = new JLabel("ͼ��");
			add(label, BorderLayout.NORTH);
			
			iconsPanel = new IconsPanel();
			iconsActionListener = new IconsActionListener();
			for(int i=0; i<PathList_Layer.length; ++i) {
				IconButton b = new IconButton(PathList_Layer[i][0], PathList_Layer[i][1]);
				b.addActionListener(iconsActionListener);
				iconsPanel.addButton(b);
			}
			iconsPanel.setEnabledAll(true);
			
			tlmTable = new TileLayerManagerTable();
			showScroll = new JScrollPane(tlmTable);//, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			showScroll.setMinimumSize(new Dimension(100, 200));
			add(showScroll, BorderLayout.CENTER);
			add(iconsPanel, BorderLayout.SOUTH);
		}
		
		public void addLayer() {
			tlmTable.insertLayer("ͼ��"+tlmTable.getLayerNum(), TileLayer.Type_Tile, 0);
		}
			
		public void moveUp() {
			tlmTable.moveUp();
		}
		
		public void moveDown() {
			tlmTable.moveDown();
		}
		
		public void copyLayer() {
			tlmTable.copyLayer();
		}
		
		public void delLayer() {
			tlmTable.delLayer();
		}
		
		public void setIsShowOtherLayer() {
			tlmTable.setIsShowOtherLayer();
		}
		
		private class IconsActionListener implements ActionListener {			
			public void actionPerformed(ActionEvent e) {
				int index = iconsPanel.getPressedIndex(e);
				tlmTable.removeEditor();
				tlmTable.requestFocus();
				switch(index) {
					case 0:
						addLayer();
						break;
					case 1:
						moveUp();
						break;
					case 2:
						moveDown();
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
	}

}

