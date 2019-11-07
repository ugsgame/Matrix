package editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

interface Painter {

	public int getGroupID();//对应于父节点的ID

	public int getID();//自身ID

	public int getImageHeight();

	public int getImageWidth();

	public String getName();

	public void paintLeftTop(Graphics g, int left, int top);

	public void paintOrigin(Graphics g, int originX, int originY);

	public void setSelected(boolean selected);
}

interface PainterGroup {

	public int getID();

	public String getName();

	public void setSelected(boolean selected);
}

class PainterGroupTreeNode extends DefaultMutableTreeNode {

	private PainterGroup painterGroup;

	public PainterGroupTreeNode(PainterGroup painterGroup) {
		this.painterGroup = painterGroup;
	}

	public PainterGroup getPainterGroup() {
		return painterGroup;
	}

	public void setSelected(boolean selected) {
		painterGroup.setSelected(selected);
	}

	public String toString() {
		return painterGroup.getName();
	}
}

abstract class PainterPanel extends JPanel implements Painter {

	private final static int LEFT_GAP = 0;
	private final static int RIGHT_GAP = 0;
	private final static int TOP_GAP = 0;
	private final static int BOTTOM_GAP = 5;

	private final static int IMAGE_BORDER_LEFT = 2;
	private final static int IMAGE_BORDER_RIGHT = 2;
	private final static int IMAGE_BORDER_TOP = 2;
	private final static int IMAGE_BORDER_BOTTOM = 2;

	private final static int NAME_BORDER_LEFT = 2;
	private final static int NAME_BORDER_RIGHT = 2;
	private final static int NAME_BORDER_TOP = 2;
	private final static int NAME_BORDER_BOTTOM = 2;

	protected boolean selected;

	private int imageLeft;
	private int imageTop;
	private int nameLeft;
	private int nameBottom;

	public PainterPanel() {
		selected = false;
	}

	protected void computeSize() {
		BufferedImage tmpImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = tmpImage.createGraphics();

		Rectangle2D rect = MainFrame.DEF_FONT.getStringBounds(getName(), g2.getFontRenderContext());

		imageLeft = nameLeft = LEFT_GAP;
		imageTop = TOP_GAP;

		int nameWidth = (int) (rect.getWidth() + NAME_BORDER_LEFT + NAME_BORDER_RIGHT);
		// int nameHeight = (int)(rect.getHeight() + NAME_BORDER_TOP +
		// NAME_BORDER_BOTTOM);
		int nameHeight = (MainFrame.DEF_FONT.getSize() + NAME_BORDER_TOP + NAME_BORDER_BOTTOM);
		int imageWidth = (getImageWidth() + IMAGE_BORDER_LEFT + IMAGE_BORDER_RIGHT);
		int imageHeight = (getImageHeight() + IMAGE_BORDER_TOP + IMAGE_BORDER_BOTTOM);
		int width = Math.max(imageWidth, nameWidth);
		int height = imageHeight + nameHeight;

		nameBottom = TOP_GAP + height;

		this.setPreferredSize(new Dimension(width + LEFT_GAP + RIGHT_GAP, height + TOP_GAP
		        + BOTTOM_GAP));
		this.setSize(getPreferredSize());
	}

	protected Color getBorderColor() {
		return Color.BLUE;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color oldColor = g.getColor();

		Color bg0 = new Color(0xaaaaaa);
		g.setColor(bg0);
		g.fillRect(0, 0, getWidth(), getHeight());

		paintLeftTop(g, imageLeft + IMAGE_BORDER_LEFT, imageTop + IMAGE_BORDER_TOP);
		if (selected) {
			paintImageBorder(g, getBorderColor());
		}

		g.setColor(Color.BLACK);
		g.drawString(getName(), nameLeft + NAME_BORDER_LEFT, nameBottom - NAME_BORDER_BOTTOM);

		g.setColor(oldColor);
	}

	protected void paintImageBorder(Graphics g, Color c) {
		Color oldColor = g.getColor();
		g.setColor(c);

		g.fillRect(imageLeft, imageTop, getImageWidth() + IMAGE_BORDER_LEFT + IMAGE_BORDER_RIGHT,
		        IMAGE_BORDER_TOP);
		g.fillRect(imageLeft, imageTop, IMAGE_BORDER_LEFT, getImageHeight() + IMAGE_BORDER_TOP
		        + IMAGE_BORDER_BOTTOM);
		g.fillRect(imageLeft + getImageWidth() + IMAGE_BORDER_LEFT, imageTop, IMAGE_BORDER_RIGHT,
		        getImageHeight() + IMAGE_BORDER_TOP + IMAGE_BORDER_BOTTOM);
		g.fillRect(imageLeft, imageTop + getImageHeight() + IMAGE_BORDER_TOP, getImageWidth()
		        + IMAGE_BORDER_LEFT + IMAGE_BORDER_RIGHT, IMAGE_BORDER_BOTTOM);

		g.setColor(oldColor);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}

	public String toString() {
		return getName();
	}
}

class PainterTree extends JTree {

	private String name;
	protected DefaultTreeModel model;
	protected DefaultMutableTreeNode root;//根节点 
	private JScrollPane scrollPane;
	private MouseInfo mouseInfo;
	
	Color bg0 = new Color(0xaaaaaa);
	public PainterTree(String name, PainterGroup[] groups, Painter[] painters) {
		super(new DefaultTreeModel(new DefaultMutableTreeNode(name)));
		this.setBackground(bg0);
		init(name, groups, painters, null);
	}

	public PainterTree(String name, PainterGroup[] groups, Painter[] painters, MouseInfo mouseInfo) {
		super(new DefaultTreeModel(new DefaultMutableTreeNode(name)));
		init(name, groups, painters, mouseInfo);
	}

	public String getName() {
		return name;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	private void init(String name, PainterGroup[] groups, Painter[] painters, MouseInfo mouseInfo) {
		this.mouseInfo = mouseInfo;
		initProp(name);
		initNodes(groups, painters);
		expandRow(0);
	}
	
	public void clearClipData(){
		if(root !=null&&root.getChildCount() >1){
			for(int i=1;root !=null &&i<root.getChildCount();i++){
				model.removeNodeFromParent((MutableTreeNode) root.getChildAt(i));
				i=0;
			}
		}
	}
	
	public Painter getClipPainterById(int groupId, int sonId){
		if(!root.isLeaf() &&root.getChildCount() >1){
			for(int i=0;i<root.getChildCount();i++){
				if(root.getChildAt(i) instanceof PainterGroupTreeNode){
					PainterGroupTreeNode ccNode =(PainterGroupTreeNode) root.getChildAt(i);
					if(ccNode.getPainterGroup().getID() ==groupId){
						for(int j=0;j<ccNode.getChildCount();j++){
							PainterTreeNode painterNode =(PainterTreeNode)ccNode.getChildAt(j);
							if(painterNode.getPainter().getID() ==sonId){
								return painterNode.getPainter();
							}
						}
					}
				}
			}
		}
		return null;
	}
	public Painter getSIPainterPainterById(int groupId, int sonId){
		if(root.getChildCount()>groupId){
			if(root.getChildAt(groupId) instanceof PainterGroupTreeNode){
				PainterGroupTreeNode ccNode =(PainterGroupTreeNode) root.getChildAt(groupId);
				for(int j=0;j<ccNode.getChildCount();j++){
					PainterTreeNode painterNode =(PainterTreeNode)ccNode.getChildAt(j);
					if(painterNode.getPainter().getID() ==sonId){
						return painterNode.getPainter();
					}
				}
			}
		}
		return null;
	}
	public void updateOneChild(BufferedImage changeImg, int groupId, int sonId){
		for(int i=0;root !=null &&i<root.getChildCount();i++){
			if(root.getChildAt(i) instanceof PainterGroupTreeNode){
				PainterGroupTreeNode ccNode =(PainterGroupTreeNode) root.getChildAt(i);
				if(ccNode.getPainterGroup().getID() ==groupId){
					for(int j=0;j<ccNode.getChildCount();j++){
						PainterTreeNode painterNode =(PainterTreeNode)ccNode.getChildAt(j);
						if(painterNode.getPainter().getID() ==sonId){
							if(painterNode.getPainter() instanceof ClipPic){
								((ClipPic)painterNode.getPainter()).image =changeImg;
								break;
							}else if(painterNode.getPainter() instanceof SIPainter) {
								((SIPainter)painterNode.getPainter()).si.image =changeImg;
								break;
							}
						}
					}
					break;
				}
			}
		}
	}
	
	public void delAllChild(int groupId, boolean isDelSelf) {
		for(int i=0;root !=null &&i<root.getChildCount();i++){
			if(root.getChildAt(i) instanceof PainterGroupTreeNode){
				PainterGroupTreeNode ccNode =(PainterGroupTreeNode) root.getChildAt(i);
				if(ccNode.getPainterGroup().getID() ==groupId){
					while(ccNode.getChildCount() > 0) {
						model.removeNodeFromParent((MutableTreeNode) ccNode.getChildAt(0));
					}
					if(isDelSelf)
						model.removeNodeFromParent((MutableTreeNode) root.getChildAt(i));
					System.out.println("delAllChild: "+groupId);
				}
			}
		}
	}
	
	public void delOneChild(int groupId, int sonId){
		int delIndex =-1;
		for(int i=0;root !=null &&i<root.getChildCount();i++){
			if(root.getChildAt(i) instanceof PainterGroupTreeNode){
				PainterGroupTreeNode ccNode =(PainterGroupTreeNode) root.getChildAt(i);
				if(ccNode.getPainterGroup().getID() ==groupId){
					int sonIndex =-1;
					for(int j=0;j<ccNode.getChildCount();j++){
						PainterTreeNode painterNode =(PainterTreeNode)ccNode.getChildAt(j);
						if(painterNode.getPainter().getID() ==sonId){
							sonIndex =j;
							break;
						}
					}
					if(sonIndex>=0){
						model.removeNodeFromParent((MutableTreeNode) ccNode.getChildAt(sonIndex));
//						ccNode.remove(sonIndex);
						if(ccNode.getChildCount() <=0){
							delIndex =i;
						}
						break;
					}
				}
			}
		}
		if(delIndex >=0){
			model.removeNodeFromParent((MutableTreeNode) root.getChildAt(delIndex));
		}
	}
	public int getNodeLenght(int groupId){
		int tempNodeLeng =1;
		if(root !=null && !root.isLeaf()){
			for(int i=0;i<root.getChildCount();i++){
				if(root.getChildAt(i) instanceof PainterGroupTreeNode){
					PainterGroupTreeNode ccNode =(PainterGroupTreeNode) root.getChildAt(i);
					if(ccNode.getPainterGroup().getID() ==groupId){
						if(ccNode.getLastChild() instanceof PainterTreeNode){
							tempNodeLeng =((PainterTreeNode) ccNode.getLastChild()).getPainter().getID() +1;
						}
					}
				}
			}
		}
		return tempNodeLeng;
	}
	public void addNewNodeToTree(PainterGroup group, Painter painter){
		//添加父节点
		if(painter ==null){
			return;
		}
		if(group !=null){
			if(judgeCanNewAdd(group)){
				model.insertNodeInto(new PainterGroupTreeNode(group), root, root.getChildCount());
			}
		}
		boolean added = false;
		PainterTreeNode painterNode = new PainterTreeNode(painter);
		for(int i=0;root!=null&&i<root.getChildCount();i++){
			if(root.getChildAt(i) instanceof PainterGroupTreeNode){
				PainterGroupTreeNode ccNode =(PainterGroupTreeNode) root.getChildAt(i);
				if(ccNode.getPainterGroup().getID() ==painter.getGroupID()){
					model.insertNodeInto(painterNode, ccNode, ccNode.getChildCount());
					added = true;
					break;
				}
			}
		}
	}

	public void initNodes(PainterGroup[] groups, Painter[] painters) {
		PainterGroupTreeNode[] groupNodes = null;
		//设置父节点
		if (groups != null) {
			groupNodes = new PainterGroupTreeNode[groups.length];
			for (int i = 0; i < groups.length; ++i) {
				groupNodes[i] = new PainterGroupTreeNode(groups[i]);
				model.insertNodeInto(groupNodes[i], root, i);
			}
		}
		if (painters != null) {
			for (int i = 0; i < painters.length; ++i) {
				PainterTreeNode painterNode = new PainterTreeNode(painters[i]);
				boolean added = false;
				if (groupNodes != null) {
					for (int j = 0; j < groupNodes.length; ++j) {
						if (groupNodes[j].getPainterGroup().getID() == painters[i].getGroupID()) {
							model.insertNodeInto(painterNode, groupNodes[j], groupNodes[j].getChildCount());
							added = true;
							break;
						}
					}
				}
				if (!added) {
					model.insertNodeInto(painterNode, root, root.getChildCount());
				}
			}
		}
	}
	
	private boolean judgeCanNewAdd(PainterGroup painterGroup){
		for(int i=0;root!=null&&i<root.getChildCount();i++){
			if(root.getChildAt(i) instanceof PainterGroupTreeNode){
				PainterGroupTreeNode ccNode =(PainterGroupTreeNode) root.getChildAt(i);
				if(painterGroup.getName().equals(ccNode.getPainterGroup().getName()) &&painterGroup.getID()==ccNode.getPainterGroup().getID()){
					return false;
				}
			}
		}
		return true;
	}

	private void initProp(String name) {
		this.name = name;
		model = (DefaultTreeModel) getModel();//返回正在提供数据的 TreeModel
		root = (DefaultMutableTreeNode) (model.getRoot());
		scrollPane = new JScrollPane(this);
		SwingUtil.setDefScrollIncrement(scrollPane);
		scrollPane.setName(name);
		setCellRenderer(new PainterTreeRenderer());//设置将用于绘制每个单元格的 TreeCellRenderer
		setEditable(false);//确定树是否可编辑
		setDragEnabled(false);//打开或关闭自动拖动处理
		
		DefaultTreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
		selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); //允许一条路径
		setSelectionModel(selectionModel);//选择模式
		//监听
		addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				selectionChanged(e);
			}
		});
		if (mouseInfo != null) {
			mouseInfo.addListener(new MouseInfoAdapter() {

				public void resetAll() {
					clearSelection();
				}
			});
		}
	}

	public void refresh(PainterGroup[] groups, Painter[] painters) {
		mouseInfo.resetAll();
		while (root.getChildCount() > 0) {
			TreeNode node = root.getChildAt(0);
			if (node instanceof MutableTreeNode) {
				model.removeNodeFromParent((MutableTreeNode) node);
			}
		}
		initNodes(groups, painters);
		expandRow(0);
	}

	private void selectionChanged(TreeSelectionEvent e) {
		TreePath oldPath = e.getOldLeadSelectionPath();//已经选择的旧路径
		TreePath newPath = e.getNewLeadSelectionPath();//新路径
		Object treeNode;

		if (oldPath != null) {
			unselectOldPath(oldPath);
			treeNode = oldPath.getLastPathComponent();
			if (treeNode instanceof PainterGroupTreeNode) {
				((PainterGroupTreeNode) treeNode).setSelected(false);
				unselectOldGroup((PainterGroupTreeNode) treeNode);
			}
			else if (treeNode instanceof PainterTreeNode) {
				((PainterTreeNode) treeNode).setSelected(false);
				unselectOldPainter((PainterTreeNode) treeNode);
			}
		}

		if (newPath != null) {
			selectNewPath(newPath);
			treeNode = newPath.getLastPathComponent();
			if (treeNode instanceof PainterGroupTreeNode) {
				((PainterGroupTreeNode) treeNode).setSelected(true);
				selectNewGroup((PainterGroupTreeNode) treeNode);
			}
			else if (treeNode instanceof PainterTreeNode) {
				((PainterTreeNode) treeNode).setSelected(true);
		//		System.out.println("父节点 ="+((PainterTreeNode) treeNode).getPainter().getGroupID()+"子节点 ="+((PainterTreeNode) treeNode).getPainter().getID());
//				if(MainFrame.self.treeTab.getSelectedIndex() !=1){
					MainFrame.self.showJp.updatePainter(((PainterTreeNode) treeNode).getPainter());
//					if(MainFrame.self.treeTab.getSelectedIndex() ==2){
						selectNewPainter((PainterTreeNode) treeNode);
//					}
//				}
			}
		}
	}

	protected void selectNewGroup(PainterGroupTreeNode newGroup) {}

	protected void selectNewPainter(PainterTreeNode newPainter) {
		if (mouseInfo != null) {
			mouseInfo.setInfo(MouseInfo.NEW_SPRITE);
			mouseInfo.setPainter(newPainter.getPainter());
		}
	}

	protected void selectNewPath(TreePath newPath) {
		if (mouseInfo != null) {
			mouseInfo.resetSelf();
		}
	}

	protected void unselectOldGroup(PainterGroupTreeNode node) {}

	protected void unselectOldPainter(PainterTreeNode node) {}

	protected void unselectOldPath(TreePath oldPath) {}
}

class PainterTreeNode extends DefaultMutableTreeNode {

	private Painter painter;

	public PainterTreeNode(Painter painter) {
		super(painter);
		this.painter = painter;
	}

	public Painter getPainter() {
		return painter;
	}

	public void setSelected(boolean selected) {
		painter.setSelected(selected);
	}
}

class PainterTreeRenderer implements TreeCellRenderer {

	private DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
	        boolean expanded, boolean leaf, int row, boolean hasFocus) {

		if (value != null) {
			if (value instanceof PainterTreeNode) {
				Painter painter = ((PainterTreeNode) value).getPainter();
				if (painter != null) {
					if (painter instanceof PainterPanel) { return (PainterPanel) painter; }
				}
			}
		}

		return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf,
		        row, hasFocus);
	}
}
