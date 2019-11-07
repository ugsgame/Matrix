package editor;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Clip extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JScrollPane jscp;
	ClipJPanel cpPanel;
//	Image testImg =XImage.readPNG(new File(".\\shuig.png"), null);
	public Painter painter;
	double scaleNum =0.0;
	clipInfoSave clipinfo;
	Image drawImg;
	BufferedImage originalBufImage; // 原始缓冲区图像
	public final static double[][] COR_OFFSETS = { { 0, 0.5 }, { 0.5, 0 }, { 0.5, 1 }, { 1, 0.5 } };
	public final static int[] CORNER_CURSORS ={Cursor.W_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.E_RESIZE_CURSOR};
	
	public Clip(){
		iconPa =new iconPanel();
		
		cpPanel =new ClipJPanel();
//		cpPanel.setBackground(Color.white); //取用默认
//		cpPanel.setPreferredSize(new Dimension(600,600));
		resizeCpPanel();
		
		jscp = new JScrollPane(cpPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscp.setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new InputMap());
//		jscp.setPreferredSize(new Dimension(200,200));
//		jscp.setViewportBorder(BorderFactory.createLineBorder(Color.black)); //加黑边框
		
		setLayout(new BorderLayout());
		add(iconPa, BorderLayout.NORTH);
		add(jscp, BorderLayout.CENTER);
	}
	public void recoverALlThing(){
		if(clipinfo != null)
			clipinfo.chooseArr = -1;
		cpPanel.clear();
		drawImg =null;
		originalBufImage =null;
		scaleNum =0.0;
		clipinfo =null;
		painter =null;
		cpPanel.repaint();
	}
	public void updatePainter(Painter pat){
		if(pat==null)return;
//		System.out.println("父节点 ="+pat.getGroupID()+"子节点 ="+pat.getID());
		if(this.painter !=null){
			cpPanel.clear();
			if(this.painter.getGroupID() !=pat.getGroupID()||this.painter.getID() !=pat.getID()){
				this.painter =pat;
				scaleNum =0.0;
				jbutton2.setEnabled(false);
				creatNewClipInfo(painter);
			}
		}else{
			this.painter =pat;
			scaleNum =0.0;
			jbutton2.setEnabled(false);
			creatNewClipInfo(painter);
		}
		resizeCpPanel();
	}
	private void creatNewClipInfo(Painter pat){
		if(pat instanceof SIPainter){
			originalBufImage =((SIPainter)pat).getSI().image;
		}else if(pat instanceof ClipPicPanel){
			originalBufImage =((ClipPicPanel)pat).clip.image;
		}
		drawImg =(Image)originalBufImage;
		/*AffineTransform at = new AffineTransform(-1, 0, 0, 1, originalBufImage.getWidth(), 0);
		AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		drawImg = ato.filter(originalBufImage, null);*/
		
		if(drawImg !=null){
			jbutton1.setEnabled(true);
		}else {
			return;
		}
		int clipinfoIndex =0;//切块信息类
		if(MainFrame.clipArr.isEmpty()){
			MainFrame.clipArr.add(new clipInfoSave(pat.getGroupID(), pat.getID()));
			clipinfoIndex =0;
		}else{
			boolean canCreat =true;
			clipInfoSave c;
			for(int i=0;i<MainFrame.clipArr.size();i++){
				c =(clipInfoSave)(MainFrame.clipArr.get(i));
				if(c.groupType ==pat.getGroupID() &&c.sonType ==pat.getID()){
					canCreat =false;
					clipinfoIndex =i;
					break;
				}
			}
			if(canCreat){
				clipinfoIndex =MainFrame.clipArr.size();
				MainFrame.clipArr.add(new clipInfoSave(pat.getGroupID(), pat.getID()));
			}
		}
		clipinfo =(clipInfoSave)(MainFrame.clipArr.get(clipinfoIndex));
		clipinfo.chooseArr = -1;
	}
	
	private class ClipJPanel extends JPanel implements MouseMotionListener, MouseListener, KeyListener, ActionListener{		
		int[] chooseArr;
		int[] writeArr;//记忆选中切块的 xywh
		int mouseX, mouseY;
		int moveX, moveY;
		int differX, differY;
		boolean isPressed;//是否按下，减少repaint次数
		boolean isEdit; //是否调整切块位置
		int[] typeSort;
		
		JPopupMenu popMenu;
		JMenu[] typeSortMenu;
		JMenuItem[][] typeSortOptions;
		
		public ClipJPanel(){
			super();
			//pop menu
			typeSort = new int[GroupImage.TypeCount];
			popMenu = new JPopupMenu();
			typeSortMenu = new JMenu[GroupImage.TypeCount];
			typeSortOptions = new JMenuItem[GroupImage.TypeCount][];
			for(int i=0; i<GroupImage.TypeCount; ++i) {
				typeSortMenu[i] = new JMenu(GroupImage.menuList[i]);
				typeSortOptions[i] = new JMenuItem[GroupImage.labelList[i].length-1];
				for(int j=0; j<GroupImage.labelList[i].length-1; ++j) {
					typeSortOptions[i][j] = new JMenuItem(GroupImage.labelList[i][j]);
					typeSortOptions[i][j].addActionListener(this);
					typeSortMenu[i].add(typeSortOptions[i][j]);
				}
				popMenu.add(typeSortMenu[i]);
			}
			
			//满足条件才能开始切图
			addKeyListener(this);
			addMouseListener(this);
			//添加指定的鼠标移动侦听器
			addMouseMotionListener(this);
		}
		
		public void clear() {
			if(isEdit) {
				isEdit = false;
				updatePic();
				jbuttonSave.setEnabled(false);
			}
		}
		
		//更新调整的切块图片位置
		private void updatePic() {
			MainFrame.self.getSIManager().reGenerate(painter.getGroupID());
			originalBufImage =((SIPainter)painter).getSI().image;
			drawImg =(Image)originalBufImage;
			cpPanel.repaint();
		}
		
		private void refreshPic() {
			if(painter != null && painter.getGroupID()>=0) {
				MainFrame.self.getSIManager().refresh(painter.getGroupID());
				recoverALlThing();
			}
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.gray);
			g.fillRect(0, 0, (int)(this.getWidth()*(1.0+scaleNum)), (int)(this.getHeight()*(1.0+scaleNum)));
			if(isEdit) {
				MainFrame.self.getSIManager().getGI(painter.getGroupID()).paintLeftTop(g, 0, 0);
		//		System.out.println("is edit");
			}
			else if(drawImg !=null){
				g.drawImage(drawImg, 0, 0, null);
			}
			/*if(originalBufImage !=null){
				Graphics2D g2d = (Graphics2D) g;
	            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                RenderingHints.VALUE_INTERPOLATION_BILINEAR); // 这里使用双线性过滤插件算法 VALUE_INTERPOLATION_BICUBIC   VALUE_INTERPOLATION_BILINEAR

//	            g2d.translate(0, 60);
	            g2d.rotate(Math.toRadians(180),originalBufImage.getWidth()/2, originalBufImage.getHeight()/2);
	            g2d.drawImage(originalBufImage, 0, 0, null);
			}*/
			
			
			drawClipInfo(g);
		}
		private void drawClipInfo(Graphics g){
			Graphics2D g2 = (Graphics2D) g;
			
			Composite oldComposite = g2.getComposite();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			AffineTransform oldTransform = g2.getTransform();
			
			/*int compositeRule = AlphaComposite.SRC_OVER; //源排斥目标法合成规则  
			AlphaComposite alphaComposite=AlphaComposite.getInstance(compositeRule,0.4f); //创建AlphaComposite对象 半透率
			g2.setComposite(alphaComposite);*/
			int[] tempInt=null;
			chooseArr =null;
			if(clipinfo !=null){
				g2.setColor(Color.BLACK);
			//	g2.drawRect(0, 0, drawImg.getWidth(null), drawImg.getHeight(null));
				
				for(int i=0;clipinfo.clipArea !=null&&i<clipinfo.clipArea.length;i++){
					tempInt =new int[4];
					for(int j=0;j<tempInt.length;j++){
						tempInt[j] =(int) (clipinfo.clipArea[i][j]*(1.0+scaleNum));
					}
					if(clipinfo.chooseArr==i){
						g2.setColor(Color.BLUE);
						g2.fillRect(tempInt[0], tempInt[1], tempInt[2], tempInt[3]);
						chooseArr =new int[4];
						chooseArr[0] =tempInt[0];
			            chooseArr[1] =tempInt[1];
						chooseArr[2] =tempInt[2];
						chooseArr[3] =tempInt[3];
						/*g2.setColor(Color.black);
						g2.drawRect(tempInt[0], tempInt[1], tempInt[2], tempInt[3]);*/
					}else{
						if(clipinfo.clipArea[i][6] == 1) {
							g2.setColor(Color.RED);
							g2.fillRect(tempInt[0], tempInt[1], tempInt[2], tempInt[3]);
						}
						else {
							g2.setColor(Color.BLUE);
							g2.drawRect(tempInt[0], tempInt[1], tempInt[2], tempInt[3]);
						}
					}
				}
			}
			if(painter !=null &&painter.getGroupID()>=0&&isPressed){
				if(clipinfo !=null&&clipinfo.chooseArr <0){
					g2.setColor(Color.black);
					g2.fillRect(Math.min(moveX, mouseX), Math.min(moveY, mouseY), Math.abs(mouseX - moveX), Math.abs(mouseY - moveY));
				}
			}
			g2.setTransform(oldTransform);
			g2.setComposite(oldComposite);
			if(chooseArr !=null){
				g2.setColor(Color.black);
				for (int j = 0; j < COR_OFFSETS.length; j++) {
					int cornerX = (int) ((chooseArr[0] + COR_OFFSETS[j][0] * chooseArr[2]))- (5 / 2);
					int cornerY = (int) ((chooseArr[1] + COR_OFFSETS[j][1] * chooseArr[3])) - (5 / 2);
					g2.fillRect(cornerX, cornerY, 5 , 5 );
				}
			}
		}
		
		//生成节点
		public void addOneToTree(int[] inArr){
			BufferedImage tempImg = new BufferedImage(inArr[2], inArr[3], BufferedImage.TYPE_INT_ARGB);
			Graphics g = tempImg.createGraphics();
			g.drawImage(((SIPainter)painter).getSI().image, 0, 0, inArr[2], inArr[3], inArr[0], inArr[1], inArr[0]+inArr[2], inArr[1]+inArr[3], null);
			tempImg.flush();
			
			SIGroup sicc =new SIGroup(-painter.getID(), painter.getName());
			int nodeMax =clipinfo.getMaxId();	//MainFrame.self.trees[MainFrame.LAYER_CLIP].getNodeLenght(-painter.getID());
			MainFrame.self.trees[MainFrame.LAYER_CLIP].addNewNodeToTree(new SIPainterGroup(sicc), new ClipPicPanel(tempImg, nodeMax, -painter.getID(), inArr[0], inArr[1], inArr[2], inArr[3]));
			MainFrame.self.trees[MainFrame.LAYER_CLIP].expandRow(0);
			
			clipinfo.addOneCut(inArr[0], inArr[1], inArr[2], inArr[3], -painter.getID(), nodeMax, 1);
			clipinfo.chooseArr =clipinfo.clipArea.length -1;
		}
		//更新节点
		private void updateOneNode(int[] inArr){
			//更新选择区域
			BufferedImage tempImg = new BufferedImage(inArr[2], inArr[3], BufferedImage.TYPE_INT_ARGB);
			Graphics g = tempImg.createGraphics();
			g.drawImage(((SIPainter)painter).getSI().image, 0, 0, inArr[2], inArr[3], inArr[0], inArr[1], inArr[0]+inArr[2], inArr[1]+inArr[3], null);
			tempImg.flush();
			MainFrame.self.trees[MainFrame.LAYER_CLIP].updateOneChild(tempImg, inArr[4], inArr[5]);
			MainFrame.self.trees[MainFrame.LAYER_CLIP].getScrollPane().repaint();
			MainFrame.self.trees[MainFrame.LAYER_CLIP].expandRow(0);
			
			//更新编辑区域
			MainFrame.self.animPanel.updateClipEdit(tempImg, inArr);
			//更新其他act的切片信息
			MainFrame.self.animList.updateClipInfo(tempImg, inArr);
			MainFrame.self.animPanel.aniedit.showjpanel.repaint();
		}
		
		// 设置输出类型
		private void setTypeSort(int[] type) {
			int groupId = painter.getGroupID();
			MainFrame.self.getSIManager().setGISortType(groupId, type);
			for(int i=0; i<typeSort.length; ++i)
				typeSort[i] = type[i];
			
			for(int i=0; i<typeSortMenu.length; ++i) {
				for(int j=0; j<typeSortOptions[i].length; ++j) {
					if(j != type[i])
						 typeSortOptions[i][j].setLabel("  " + GroupImage.labelList[i][j]);
					else
						typeSortOptions[i][j].setLabel(GroupImage.labelList[i][typeSortOptions[i].length] + " " + GroupImage.labelList[i][j]);
				}
			}
			creatNewClipInfo(painter);
			cpPanel.repaint();
			resizeCpPanel();
		}
		
		//mouse press
		public void mousePressed(MouseEvent e) {
			requestFocus();
			
			//修正图片放大后的鼠标点击值
			int bf =(int)(1.0+scaleNum);
			int ccx =e.getX()/bf;
			int ccy =e.getY()/bf;
			mouseX =ccx*bf;
			mouseY =ccy*bf;
			
			moveX =mouseX;
			moveY =mouseY;
			
			writeArr =null;
			if(painter ==null ||painter.getGroupID()<0){
				return;
			}
			if(clipinfo !=null){
				if(e.getButton() == 3) {
					int groupId = painter.getGroupID();
					GroupImage gi = MainFrame.self.getSIManager().getGI(groupId);
					setTypeSort(gi.getTypeSort());
					popMenu.show(this, e.getX(), e.getY());
				}
				else 	if(mouseX <=drawImg.getWidth(null) && mouseY<=drawImg.getHeight(null)){
					Cursor cc =getCursor();
					int dd =cc.getType();
					if(dd ==Cursor.DEFAULT_CURSOR){
						if(clipinfo.checkIsChoose(mouseX/bf, mouseY/bf, 0, 0)){
							differX =mouseX/bf -clipinfo.clipArea[clipinfo.chooseArr][0];
							differY =mouseY/bf -clipinfo.clipArea[clipinfo.chooseArr][1];
						}
					}else{
						differX =mouseX;
						differY =mouseY;
					}
					if(clipinfo.chooseArr >=0){
						writeArr =new int[4];
						writeArr[0] =clipinfo.clipArea[clipinfo.chooseArr][0];
						writeArr[1] =clipinfo.clipArea[clipinfo.chooseArr][1];
						writeArr[2] =clipinfo.clipArea[clipinfo.chooseArr][2];
						writeArr[3] =clipinfo.clipArea[clipinfo.chooseArr][3];
						if(clipinfo.clipArea[clipinfo.chooseArr][6] == 1)
							clear();
					}
					else
						clear();
					
					isPressed =true;
				}else{
					clipinfo.chooseArr =-1;
				}
				cpPanel.repaint();
			}
		}

		public void mouseReleased(MouseEvent e) {
			if(clipinfo !=null){
				if(isPressed){
					int bf =(int)(1.0+scaleNum);
					//生成新的
					if(clipinfo.chooseArr <0){
						if(mouseX !=moveX ||mouseY !=moveY){
							int sureX =Math.min(moveX, mouseX)/bf;
							int sureY =Math.min(moveY, mouseY)/bf;
							int sureW =Math.abs(mouseX - moveX)/bf;
							int sureH =Math.abs(mouseY - moveY)/bf;
							if (sureW <= 5 && sureH <= 5) {
								JOptionPane.showMessageDialog(null, "切块过小", "警告", JOptionPane.WARNING_MESSAGE);
							}
							else {
								int n = JOptionPane.showConfirmDialog(null,"确定生成新的切块?","确定",JOptionPane.YES_NO_OPTION);
								if(n ==0){
									/*clipinfo.clipArea =Tools.addToIntArr2(clipinfo.clipArea, new int[]{sureX, sureY, sureW, sureH});
									clipinfo.chooseArr =clipinfo.clipArea.length -1;*/
									addOneToTree(new int[]{sureX, sureY, sureW, sureH});
								}else{
									//取消
								}
							}
							cpPanel.repaint();
						}
					}else{
						if(writeArr !=null){
							if(clipinfo.clipArea[clipinfo.chooseArr][6] == 1) {
								if(writeArr[0] !=clipinfo.clipArea[clipinfo.chooseArr][0] ||writeArr[1] !=clipinfo.clipArea[clipinfo.chooseArr][1]||writeArr[2] !=clipinfo.clipArea[clipinfo.chooseArr][2]||writeArr[3] !=clipinfo.clipArea[clipinfo.chooseArr][3]){
									//选择做移动 更新
									int n = JOptionPane.showConfirmDialog(null,"编辑区域的切块也将被更新,确定更新切块?","提示",JOptionPane.YES_NO_OPTION);
									if(n==0){
										updateOneNode(clipinfo.clipArea[clipinfo.chooseArr]);
									}else{
										clipinfo.clipArea[clipinfo.chooseArr][0] =writeArr[0];
										clipinfo.clipArea[clipinfo.chooseArr][1] =writeArr[1];
										clipinfo.clipArea[clipinfo.chooseArr][2] =writeArr[2];
										clipinfo.clipArea[clipinfo.chooseArr][3] =writeArr[3];
										repaint();
									}
								}
							}
						}
					}
				}
				
			}
			isPressed =false;
		}

		//按下时
		public void mouseDragged(MouseEvent e) {
//			moveX =(int)(100*e.getX()/(100*(1.0+scaleNum)));
//			moveY =(int)(100*e.getY()/(100*(1.0+scaleNum)));
			//修正
			int bf=(int)(1.0+scaleNum);
			int ccx =e.getX()/bf;
			int ccy =e.getY()/bf;
			mouseLabel.setText("   X: "+ccx+"  Y: "+ccy);
			mouseLabel.setToolTipText("   X: "+ccx+"  Y: "+ccy);
			
			if(e.getX()%bf >0){
				ccx +=1;
			}
			if(e.getY()%bf >0){
				ccy +=1;
			}
			
			moveX =ccx*bf;
			moveY =ccy*bf;
			if(isPressed){
				//不能超出图片范围
				if(moveX >drawImg.getWidth(null))moveX =drawImg.getWidth(null);
				if(moveY >drawImg.getHeight(null))moveY =drawImg.getHeight(null);
				if(moveX <0)moveX =0;
				if(moveY <0)moveY =0;
				
				if(clipinfo !=null&&clipinfo.chooseArr >=0) {
					if(clipinfo.clipArea[clipinfo.chooseArr][6] == 1) {
						Cursor cc =getCursor();
						int dd =cc.getType();
						if(dd ==Cursor.DEFAULT_CURSOR){
							//只是坐标的变化
							clipinfo.clipArea[clipinfo.chooseArr][0] =moveX/bf -differX;
							clipinfo.clipArea[clipinfo.chooseArr][1] =moveY/bf -differY;
							if(clipinfo.clipArea[clipinfo.chooseArr][0]<0){
								clipinfo.clipArea[clipinfo.chooseArr][0] =0;
							}
							if(clipinfo.clipArea[clipinfo.chooseArr][1]<0){
								clipinfo.clipArea[clipinfo.chooseArr][1] =0;
							}
							//有问题，再处理
							if(clipinfo.clipArea[clipinfo.chooseArr][0] +clipinfo.clipArea[clipinfo.chooseArr][2]>originalBufImage.getWidth(null)){
								clipinfo.clipArea[clipinfo.chooseArr][0] =originalBufImage.getWidth(null)-clipinfo.clipArea[clipinfo.chooseArr][2];
							}
							if(clipinfo.clipArea[clipinfo.chooseArr][1] +clipinfo.clipArea[clipinfo.chooseArr][3]>originalBufImage.getHeight(null)){
								clipinfo.clipArea[clipinfo.chooseArr][1] =originalBufImage.getHeight(null) -clipinfo.clipArea[clipinfo.chooseArr][3];
							}
						}else{
							//大小改变
							switch (dd) {
							case Cursor.W_RESIZE_CURSOR:
								if(moveX !=differX){
									int addC =differX/bf -moveX/bf;
									if(moveX ==0){
										addC =clipinfo.clipArea[clipinfo.chooseArr][0];
									}
									
									clipinfo.clipArea[clipinfo.chooseArr][0] -=addC;
									clipinfo.clipArea[clipinfo.chooseArr][2] +=addC;
									if(clipinfo.clipArea[clipinfo.chooseArr][2] <0){
										clipinfo.clipArea[clipinfo.chooseArr][2] =Math.abs(clipinfo.clipArea[clipinfo.chooseArr][2]);
										clipinfo.clipArea[clipinfo.chooseArr][0] -=clipinfo.clipArea[clipinfo.chooseArr][2];
										setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
									}
									differX =moveX;
								}
								break;
							case Cursor.N_RESIZE_CURSOR:
								if(moveY !=differY){
									int addC =differY/bf -moveY/bf;
									if(moveY ==0){
										addC =clipinfo.clipArea[clipinfo.chooseArr][1];
									}
									clipinfo.clipArea[clipinfo.chooseArr][1] -=addC;
									clipinfo.clipArea[clipinfo.chooseArr][3] +=addC;
									if(clipinfo.clipArea[clipinfo.chooseArr][3] <0){
										clipinfo.clipArea[clipinfo.chooseArr][3] =Math.abs(clipinfo.clipArea[clipinfo.chooseArr][3]);
										clipinfo.clipArea[clipinfo.chooseArr][1] -=clipinfo.clipArea[clipinfo.chooseArr][3];
										setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
									}
									differY =moveY;
								}
								break;
							case Cursor.S_RESIZE_CURSOR:
								if(moveY !=differY){
									int addC = moveY/bf -differY/bf;
									if(moveY ==drawImg.getHeight(null)){
										addC =drawImg.getHeight(null)-clipinfo.clipArea[clipinfo.chooseArr][1]-clipinfo.clipArea[clipinfo.chooseArr][3];
									}
									clipinfo.clipArea[clipinfo.chooseArr][3] +=addC;
									if(clipinfo.clipArea[clipinfo.chooseArr][3] <0){
										clipinfo.clipArea[clipinfo.chooseArr][3] =Math.abs(clipinfo.clipArea[clipinfo.chooseArr][3]);
										clipinfo.clipArea[clipinfo.chooseArr][1] -=clipinfo.clipArea[clipinfo.chooseArr][3];
										setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
									}
									differY =moveY;
								}
								break;
							case Cursor.E_RESIZE_CURSOR:
								if(moveX !=differX){
									int addC = moveX/bf -differX/bf;
									if(moveX ==drawImg.getWidth(null)){
										addC =drawImg.getWidth(null)-clipinfo.clipArea[clipinfo.chooseArr][0]-clipinfo.clipArea[clipinfo.chooseArr][2];
									}
									clipinfo.clipArea[clipinfo.chooseArr][2] +=addC;
									if(clipinfo.clipArea[clipinfo.chooseArr][2] <0){
										clipinfo.clipArea[clipinfo.chooseArr][2] =Math.abs(clipinfo.clipArea[clipinfo.chooseArr][2]);
										clipinfo.clipArea[clipinfo.chooseArr][0] -=clipinfo.clipArea[clipinfo.chooseArr][2];
										setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
									}
									differX =moveX;
								}
								break;
	
							default:
								break;
							}
						}
					}
					else { // 调整切块位置
						if(!isEdit) {
							isEdit = true;
							jbuttonSave.setEnabled(true);
						}
						Cursor cc =getCursor();
						int dd =cc.getType();
						if(dd ==Cursor.DEFAULT_CURSOR){
							//只是坐标的变化
							clipinfo.clipArea[clipinfo.chooseArr][0] =moveX/bf -differX;
							clipinfo.clipArea[clipinfo.chooseArr][1] =moveY/bf -differY;
							if(clipinfo.clipArea[clipinfo.chooseArr][0]<0){
								clipinfo.clipArea[clipinfo.chooseArr][0] =0;
							}
							if(clipinfo.clipArea[clipinfo.chooseArr][1]<0){
								clipinfo.clipArea[clipinfo.chooseArr][1] =0;
							}
							//有问题，再处理
							if(clipinfo.clipArea[clipinfo.chooseArr][0] +clipinfo.clipArea[clipinfo.chooseArr][2]>originalBufImage.getWidth(null)){
								clipinfo.clipArea[clipinfo.chooseArr][0] =originalBufImage.getWidth(null)-clipinfo.clipArea[clipinfo.chooseArr][2];
							}
							if(clipinfo.clipArea[clipinfo.chooseArr][1] +clipinfo.clipArea[clipinfo.chooseArr][3]>originalBufImage.getHeight(null)){
								clipinfo.clipArea[clipinfo.chooseArr][1] =originalBufImage.getHeight(null) -clipinfo.clipArea[clipinfo.chooseArr][3];
							}
						}
					}
				}
			}
			cpPanel.repaint();
		}
		//没按下
		public void mouseMoved(MouseEvent e) {
			int bf =(int)(1.0+scaleNum);
			int ccx =e.getX()/bf;
			int ccy =e.getY()/bf;
			mouseLabel.setText("   X: "+ccx+"  Y: "+ccy);
			mouseLabel.setToolTipText("   X: "+ccx+"  Y: "+ccy);
			setCursor(Cursor.getDefaultCursor());
			if(chooseArr !=null){
				for (int j = 0; j < COR_OFFSETS.length; j++) {
					int cornerX = (int) ((chooseArr[0]/bf + COR_OFFSETS[j][0] * (chooseArr[2]/bf)))- (5 / 2);
					int cornerY = (int) ((chooseArr[1]/bf + COR_OFFSETS[j][1] * (chooseArr[3]/bf))) - (5 / 2);
					if(Tools.checkBoxInter(cornerX, cornerY, 5 , 5, ccx, ccy, 0, 0)){
						setCursor(Cursor.getPredefinedCursor(CORNER_CURSORS[j]));
					}
				}
			}
		}
		
	//	@Override
		public void keyPressed(KeyEvent e) {
			requestFocus();
			
			int x = 0;
			int y = 0;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_DELETE:
				if(clipinfo.chooseArr >=0){
					if(clipinfo.clipArea[clipinfo.chooseArr][6] == 1) {
						int n = JOptionPane.showConfirmDialog(null,"将同时删除编辑区所对应的切块,确定删除?","警告",JOptionPane.YES_NO_OPTION);
						if(n==0){
							clipinfo.delOneArea(clipinfo.chooseArr);
							repaint();
						}
					}
				}
				break;
			case KeyEvent.VK_UP:
				y = -1;
				break;
			case KeyEvent.VK_DOWN:
				y = 1;
				break;
			case KeyEvent.VK_LEFT:
				x = -1;
				break;
			case KeyEvent.VK_RIGHT:
				x = 1;
				break;
			default:
				break;
			}
			
			if(clipinfo.chooseArr >=0 && (x != 0 || y != 0)) {
				//只是坐标的变化
				int bf=(int)(1.0+scaleNum);
				int clipx =clipinfo.clipArea[clipinfo.chooseArr][0] + x*bf;
				int clipy =clipinfo.clipArea[clipinfo.chooseArr][1] + y*bf;
				if(!clipinfo.checkIsPile(clipx, clipy, clipinfo.clipArea[clipinfo.chooseArr][2], clipinfo.clipArea[clipinfo.chooseArr][3], clipinfo.chooseArr)) {
					clipinfo.clipArea[clipinfo.chooseArr][0] = clipx;
					clipinfo.clipArea[clipinfo.chooseArr][1] = clipy;
					
					if(clipinfo.clipArea[clipinfo.chooseArr][0]<0){
						clipinfo.clipArea[clipinfo.chooseArr][0] =0;
					}
					if(clipinfo.clipArea[clipinfo.chooseArr][1]<0){
						clipinfo.clipArea[clipinfo.chooseArr][1] =0;
					}
					//有问题，再处理
					if(clipinfo.clipArea[clipinfo.chooseArr][0] +clipinfo.clipArea[clipinfo.chooseArr][2]>originalBufImage.getWidth(null)){
						clipinfo.clipArea[clipinfo.chooseArr][0] =originalBufImage.getWidth(null)-clipinfo.clipArea[clipinfo.chooseArr][2];
					}
					if(clipinfo.clipArea[clipinfo.chooseArr][1] +clipinfo.clipArea[clipinfo.chooseArr][3]>originalBufImage.getHeight(null)){
						clipinfo.clipArea[clipinfo.chooseArr][1] =originalBufImage.getHeight(null) -clipinfo.clipArea[clipinfo.chooseArr][3];
					}
					
					if(!isEdit) {
						isEdit = true;
						jbuttonSave.setEnabled(true);
					}
					
					repaint();
				}
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
		
		public void actionPerformed(ActionEvent e) {
			for(int i=0; i<typeSortMenu.length; ++i) {
				for(int j=0; j<typeSortOptions[i].length; ++j) {
					if(e.getSource() == typeSortOptions[i][j]) {
						typeSort[i] = j;
						setTypeSort(typeSort);
						break;
					}
				}
			}
		}
		public void mouseClicked(MouseEvent e) {
			
		}
		public void mouseEntered(MouseEvent e) {
			
		}
		public void mouseExited(MouseEvent e) {
			
		}
	}
	
	JPanel iconPa;
	JButton jbutton1, jbutton2;
	JButton jbuttonRefresh;
	JButton jbuttonSave;
	private JLabel mouseLabel;
	class iconPanel extends JPanel{
		public iconPanel(){
			setBorder(BorderFactory.createEtchedBorder());
			setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			jbutton1 =new JButton(new ImageIcon("icon/largen.png"));
			jbutton1.setEnabled(false);
			jbutton1.setBorderPainted(false);
			jbutton1.setMargin(new Insets(0, 0, 0, 0));
			jbutton1.setFocusPainted(false);
			jbutton1.setContentAreaFilled(true);
			jbutton1.setToolTipText("放大");
			jbutton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	scaleNum +=1.0;
                	todoPic(1.0+scaleNum);
                	jbutton2.setEnabled(true);
                	cpPanel.repaint();
                }
            });
			
			jbutton2 =new JButton(new ImageIcon("icon/lessen.png"));
			jbutton2.setEnabled(false);
			jbutton2.setBorderPainted(false);
			jbutton2.setMargin(new Insets(0, 0, 0, 0));
			jbutton2.setFocusPainted(false);
			jbutton2.setContentAreaFilled(true);
			jbutton2.setToolTipText("缩小");
			jbutton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	if(scaleNum >0.0){
                		scaleNum -=1.0;
                		todoPic(1.0+scaleNum);
                		if(scaleNum <=0.0){
                			scaleNum =0.0;
                			jbutton2.setEnabled(false);
                		}
                		cpPanel.repaint();
                	}
                }
            });
			
			jbuttonSave =new JButton(new ImageIcon("icon/save.png"));
			jbuttonSave.setEnabled(false);
			jbuttonSave.setBorderPainted(false);
			jbuttonSave.setMargin(new Insets(0, 0, 0, 0));
			jbuttonSave.setFocusPainted(false);
			jbuttonSave.setContentAreaFilled(true);
			jbuttonSave.setToolTipText("保存");
			jbuttonSave.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	jbuttonSave.setEnabled(false);
                	cpPanel.updatePic();
                }
            });
			
			jbuttonRefresh =new JButton(new ImageIcon("icon/refresh.png"));
			jbuttonRefresh.setBorderPainted(false);
			jbuttonRefresh.setMargin(new Insets(0, 0, 0, 0));
			jbuttonRefresh.setFocusPainted(false);
			jbuttonRefresh.setContentAreaFilled(true);
			jbuttonRefresh.setToolTipText("刷新");
			jbuttonRefresh.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	cpPanel.refreshPic();
                }
            });
			
			add(jbutton1);
			add(jbutton2);
			add(jbuttonRefresh);
			add(jbuttonSave);
			
			mouseLabel = new JLabel();
			mouseLabel.setHorizontalAlignment(SwingConstants.CENTER);
			mouseLabel.setText("   X: 0  Y: 0");
			mouseLabel.setToolTipText("   X: 0  Y: 0");
			
			add(mouseLabel);
//	        add(Box.createRigidArea(new Dimension(4, 4)));
		}
		
	//	BufferedImage filteredBufImage;
		private void todoPic(double bf){
			if (drawImg == null)return; // 如果bufImage为空则直接返回
			
			//方法消费内存过多，不可用
			/*filteredBufImage = new BufferedImage((int) (drawImg.getWidth() * bf),(int) (drawImg.getHeight() * bf),BufferedImage.TYPE_INT_ARGB); // 过滤后的图像
			AffineTransform transform = new AffineTransform(); // 仿射变换对象
			transform.setToScale(bf, bf); // 设置仿射变换的比例因子
			AffineTransformOp imageOp = new AffineTransformOp(transform, null);// 创建仿射变换操作对象
			imageOp.filter(originalBufImage, filteredBufImage);// 过滤图像，目标图像在filteredBufImage
			drawImg = filteredBufImage; // 让用于显示的缓冲区图像指向过滤后的图像*/	
			drawImg =((Image)originalBufImage).getScaledInstance((int)(originalBufImage.getWidth(this)* bf), (int)(originalBufImage.getHeight(this)*bf), Image.SCALE_DEFAULT);
			resizeCpPanel();
		}
	}
	
	private void resizeCpPanel() {
		int w =1;
		int h =1;
		if(drawImg !=null){
			w =drawImg.getWidth(this);
			h =drawImg.getHeight(this);
		}
		cpPanel.setPreferredSize(new Dimension(w, h));
		cpPanel.repaint();
		cpPanel.revalidate();
	}
}

/**
 * 
 * @author nihao
 *每一个切块图片对应一个信息保存对象
 */
class clipInfoSave{
	int groupType;//父类型(对应切图对象groupid)
	int sonType;//子类型
	int[][] clipArea; //切块范围  还要有每一个切块在树中的 groupId和id(用于做删除操作), isSelf: 为1表示手动切块, 为0表示自动生成的切块
	int chooseArr;//被选中的那些切块
	int countSelf;
	int maxId;
	public clipInfoSave(int group, int son){
		maxId = 0;
		countSelf = 0;
		chooseArr = -1;
		groupType =group;
		sonType =son;
	}
	
	public void addOneCut(int x, int y, int w, int h, int groupid, int sonid, int isSelf){
		clipArea =Tools.addToIntArr2(clipArea, new int[]{x, y, w, h, groupid, sonid, isSelf});
		if(isSelf == 1)
			countSelf += 1;
	}
	
	public int getMaxId() {
		return maxId ++;
	}
	
	public int getLength() {
		return clipArea ==null ? 0 : clipArea.length;
	}
	
	public int getCutIndex(int sonid) {
		int index = -1;
		for(int i=0;clipArea !=null&&i<clipArea.length;i++){
			if(clipArea[i][5] == sonid){
				index = i;
				break;
			}
		}
		
		return index;
	}
	
	public void setMaxId(int maxId) {
		if(this.maxId < maxId)
			this.maxId = maxId;
	}
	
	//保存切块信息
	public void save(DataOutputStream out) throws Exception {
		out.writeInt(groupType);
		out.writeInt(sonType);
	//	out.writeInt(maxId);
		int lenght =clipArea ==null ? 0 : clipArea.length;
	//	out.writeInt(lenght);
	//	if(lenght>0){
		out.writeInt(countSelf);
		if(countSelf>0){
			for(int i=0;i<lenght;i++){
				if(clipArea[i][6] == 1) {
					out.writeInt(clipArea[i].length);
					for(int j=0;j<clipArea[i].length;j++){
						out.writeInt(clipArea[i][j]);
					}
				}
			}
		}
	}
	public void load(DataInputStream in) throws Exception {
		groupType =in.readInt();
		sonType =in.readInt();
	//	maxId = in.readInt();
		clipInfoSave cifs = MainFrame.self.getClipArr(groupType, sonType);
		GroupImage gi = MainFrame.self.getSIManager().getGI(groupType);
		SIGroup sicc =new SIGroup(-gi.getId(), gi.getName());
		
		int lenght =in.readInt();
		if(lenght>0){
			for(int i=0;i<lenght;i++){
				int ccl =in.readInt();
				int[] clipInfo=new int[ccl];
				for(int j=0;j<ccl;j++){
					clipInfo[j] =in.readInt();
				}
		//		Print.printArray(clipInfo, "切块信息");
				BufferedImage tempImg = new BufferedImage(clipInfo[2], clipInfo[3], BufferedImage.TYPE_INT_ARGB);
				Graphics g = tempImg.createGraphics();
				g.drawImage(gi.getSI().image, 0, 0, clipInfo[2], clipInfo[3], clipInfo[0], clipInfo[1], clipInfo[0]+clipInfo[2], clipInfo[1]+clipInfo[3], null);
				tempImg.flush();
				cifs.addOneCut(clipInfo[0], clipInfo[1], clipInfo[2], clipInfo[3], clipInfo[4], clipInfo[5], clipInfo[6]);
				MainFrame.self.trees[MainFrame.LAYER_CLIP].addNewNodeToTree(new SIPainterGroup(sicc), new ClipPicPanel(tempImg, clipInfo[5], clipInfo[4], clipInfo[0], clipInfo[1], clipInfo[2], clipInfo[3]));
			}
		}
	}
	
	public void clear() {
		int len = clipArea.length;
		for(int i=len-1;clipArea !=null&&i>=0;i--){
			if(clipArea[i][6] == 1)
				delOneArea(i);
			else
				clipArea =Tools.removeOneFromIntArr(clipArea, i);
		}
	}
	
	public boolean checkIsPile(int x, int y, int w, int h, int index) {
		boolean isPile = false;
		for(int i=0; clipArea !=null&&i<clipArea.length; i++){
			if(i != index) {
				if(Tools.checkBoxInter(new int[]{clipArea[i][0],clipArea[i][1],clipArea[i][2],clipArea[i][3]}, new int[]{x,y,w,h})){
					if(clipArea[i][6] == 0) { 
						isPile =true;
						break;
					}
				}
			}
		}
		
		return isPile;
	}
	
	//检查切块是否被选中
	public boolean checkIsChoose(int x, int y, int w, int h){
		chooseArr =-1;
		for(int i=0; clipArea !=null&&i<clipArea.length; i++){
			if(Tools.checkBoxInter(new int[]{clipArea[i][0],clipArea[i][1],clipArea[i][2],clipArea[i][3]}, new int[]{x,y,w,h})){
			//	if(clipArea[i][6] == 1) { // 屏蔽调整图片位置
					chooseArr =i;
					break;
			//	}
			}
		}
		if(chooseArr >=0){
			return true;
		}
		return false;
	}
	//删除一个切块
	public void delOneArea(int index){
		//做树子节点删除
		MainFrame.self.trees[MainFrame.LAYER_CLIP].delOneChild(clipArea[index][4], clipArea[index][5]);
//		MainFrame.self.trees[MainFrame.LAYER_CLIP].getScrollPane().repaint();
		MainFrame.self.trees[MainFrame.LAYER_CLIP].expandRow(0);
		
		//更新编辑区
		MainFrame.self.animPanel.delClipEdit(clipArea[index][4], clipArea[index][5]);
		//更新其他act的切片信息
		MainFrame.self.animList.delClipInfo(clipArea[index][4], clipArea[index][5]);
		
		if(clipArea[index][6] == 1)
			countSelf -= 1;
		
		MainFrame.self.animPanel.aniedit.showjpanel.repaint();
		clipArea =Tools.removeOneFromIntArr(clipArea, index);
		chooseArr =-1;
	}
}

class Tools{
	public static int[] addToIntArr(int[] intArr, int addInt) {
        if (intArr == null) {
            intArr = new int[1];
            intArr[0] = addInt;
            return intArr;
        } else {
            int[] tmp = new int[intArr.length + 1];
            System.arraycopy(intArr, 0, tmp, 0, intArr.length);
            tmp[tmp.length - 1] = addInt;
            return tmp;
        }
    }
	 public static int[][] addToIntArr2(int[][] intArr, int[] addInt) {
        if (intArr == null) {
            intArr = new int[1][];
            intArr[0] = addInt;
            return intArr;
        } else {
        	int[][] tmp = new int[intArr.length + 1][];
            System.arraycopy(intArr, 0, tmp, 0, intArr.length);
            tmp[tmp.length - 1] = addInt;
            return tmp;
        }
    }
	 public static int[][] removeOneFromIntArr(int[][] intArr, int index) {
        if (intArr == null || intArr.length == 1) {
            return null;
        }
        for (int i = index; i < intArr.length - 1; i++) {
            intArr[i] = intArr[i + 1];
        }
        int[][] tmp = new int[intArr.length - 1][];
        System.arraycopy(intArr, 0, tmp, 0, intArr.length - 1);
        intArr = tmp;
        return intArr;
    }
	 public static final boolean checkBoxInter(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2)
    {
        return x2 < x1 + w1 && y2 < y1 + h1 && x2 + w2 > x1 && y2 + h2 > y1;
    }

    public static final boolean checkBoxInter(int box1[], int box2[])
    {
        return box2[0] < box1[0] + box1[2] && box2[1] < box1[1] + box1[3] && box2[0] + box2[2] > box1[0] && box2[1] + box2[3] > box1[1];
    }
    public static boolean intArrContain(int[] sourceArr, int findInt) {
        for (int i = 0; sourceArr != null && i < sourceArr.length; i++) {
            if (sourceArr[i] == findInt) {
                return true;
            }
        }
        return false;
    }
}
