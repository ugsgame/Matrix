package editor;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AniEdit extends JPanel{
	private JScrollPane scrollPane;
	ShowJpanel showjpanel;
	ShowPanel showpanel;
	MouseInfo mouseinfo;
	int mouseX, mouseY;
	int pressX, pressY;
	int drag_l, drag_r, drag_t, drag_b;
	int drag_x, drag_y, drag_w, drag_h;
	boolean isDrag;
	boolean isEdit;
	OneFrameDraw framedrawPre;
	OneFrameDraw framedraw;//当前动作，桢下的绘画图片
	FrameRectArr framerect;
	FramePointArr framepoint;
//	LogicRect[] rects;
	double scaleNum =1.0;
	int offx,offy;
	public static int drawOffSetX;
	public static int drawOffSetY;
	private Timer timer;
	
	public AniEdit(MouseInfo aMouseInfo){
		this.mouseinfo =aMouseInfo;
		showjpanel =new ShowJpanel();
		showjpanel.setPreferredSize(new Dimension(FrameManager.BASIC_WIDTH, FrameManager.BASIC_HEIGHT));
		
		scrollPane = new JScrollPane(showjpanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//设置滚动信息
		SwingUtil.setDefScrollIncrement(scrollPane);
		scrollPane.setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new InputMap());
		
		showjpanel.setFocusable(true);
		
		showpanel =new ShowPanel();
		setLayout(new BorderLayout());
		add(showpanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
//		add(scrollPane);
	}
	
	public void setOffsetX(int ox){
		offx =ox;
	}
	public void setOffsetY(int oy){
		offy =oy;
	}
	
	public void setIsEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
	
	class ShowJpanel extends JPanel implements MouseMotionListener, MouseListener, KeyListener, ActionListener{
		boolean mousIsSelect;//是否是选择状态
		
		int mousState; //鼠标状态
		public final static int CORNER_DEFAULT =0;//默认状态
		public final static int CORNER_LEFT_TOP = 1; // 左上
		public final static int CORNER_LEFT_MIDDLE = 2; // 左中
		public final static int CORNER_LEFT_BOTTOM = 3; // 左下
		public final static int CORNER_CENTER_TOP = 4; // 中上
		public final static int CORNER_CENTER_MIDDLE = 5; // 正中
		public final static int CORNER_CENTER_BOTTOM = 6; // 中下
		public final static int CORNER_RIGHT_TOP = 7; // 右上
		public final static int CORNER_RIGHT_MIDDLE = 8; // 右中
		public final static int CORNER_RIGHT_BOTTOM = 9; // 右下
		JPopupMenu popMenu;
		JMenuItem delItem;
		JMenuItem moveUp;
		JMenuItem movedown;
		JMenuItem copy;
		JMenuItem paste;
		JMenuItem levelFilp;
		JMenuItem verticalFilp;
		JMenuItem alphaMenu;
		JButton bt;
		private boolean firstDrawRect;
		LogicRect lr;
		
		public ShowJpanel(){
			popMenu = new JPopupMenu();
			delItem = new JMenuItem("删除");
			moveUp = new JMenuItem("向上层移动");
			movedown = new JMenuItem("向下层移动");
	//		copy =new JMenuItem("复制");
	//		paste =new JMenuItem("粘贴");
			levelFilp =new JMenuItem("水平翻转");
			verticalFilp =new JMenuItem("垂直翻转");
			alphaMenu = new JMenuItem("属性");
			
			delItem.addActionListener(this);
			moveUp.addActionListener(this);
			movedown.addActionListener(this);
	//		copy.addActionListener(this);
	//		paste.addActionListener(this);
			levelFilp.addActionListener(this);
			verticalFilp.addActionListener(this);
			alphaMenu.addActionListener(this);

			popMenu.add(delItem);
			popMenu.add(moveUp);
			popMenu.add(movedown);
	//		popMenu.add(copy);
	//		popMenu.add(paste);
			popMenu.add(levelFilp);
			popMenu.add(verticalFilp);
			popMenu.add(alphaMenu);
			
			addKeyListener(this);
			addMouseMotionListener(this);
			addMouseListener(this);
			
			/*bt =new JButton();
			bt.setBounds(-1, -1, 1, 1);
			add(bt);*/
		}
		
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 =(Graphics2D)g;
			Composite oldComposite = g2.getComposite();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1));
			AffineTransform oldTransform = g2.getTransform();
			
			g2.scale(scaleNum, scaleNum);
			g2.setColor(new Color(0xFF888888));
			g2.fillRect(FrameManager.REAL_LEFT, FrameManager.REAL_TOP, FrameManager.REAL_WIDTH, FrameManager.REAL_HEIGHT);
			
			g2.translate(drawOffSetX+offx, drawOffSetY+offy);
			/*System.out.println("offx ="+offx+",offy ="+offy);*/
			if(framedraw !=null){
				if(framedrawPre != null && isEdit)
					framedrawPre.paint(g, 0.5f);
				framedraw.paint(g, 1);
			}
			if(MainFrame.isCollisionBox){
				if(framerect !=null){
					framerect.paint(g2);
				}
			}
			if(framepoint != null)
				framepoint.paint(g2);
			mouseinfo.paint(g, mouseX, mouseY);
			g2.translate(-(drawOffSetX+offx), -(drawOffSetY+offy));
			g2.setTransform(oldTransform);
			g2.setComposite(oldComposite);
			
			paintPoints(g);
			if(framedraw !=null){
				//同步更新下面显示框
				MainFrame.self.animPanel.updateFbPanel();
			}
			if(isDrag){
				g.setColor(new Color(0x000000));
				g.drawRect(drag_x, drag_y, drag_w, drag_h);
			}
		}
		
		public void paintPoints(Graphics g) {
			g.setColor(new Color(0xFF00FF));
			g.fillRect(0, (int) ((drawOffSetY -1)*scaleNum), FrameManager.BASIC_WIDTH, 1);
			g.fillRect((int) ((drawOffSetX -1)*scaleNum), 0, 1, FrameManager.BASIC_HEIGHT);
		}
		
		public void clearSelect(){
			mousIsSelect = false;
			if(framerect != null)
				framerect.setState(false);
			if(framedraw != null)
				framedraw.allSelectObj(false);
			if(framepoint != null)
				framepoint.setState(false);
			updateSelectInfo();
			repaint();
		}
		
		public void mouseDragged(MouseEvent e) {			
			int showX =(int)(e.getX()/scaleNum -drawOffSetX);
			int showY =(int)(e.getY()/scaleNum -drawOffSetY);
			mouseLabel.setText(" X:"+showX+", Y:"+showY+"   ");
			
			if(MainFrame.isCollisionBox){
				if(firstDrawRect){
					lr.left =Math.min(pressX, showX);
					lr.top =Math.min(pressY, showY);
					lr.width =Math.abs(pressX-showX);
					lr.height =Math.abs(pressY-showY);
					repaint();
					return;
				}else if(mousState !=CORNER_DEFAULT){
					framerect.changeRect(mousState, showX -pressX, showY -pressY);
					pressX =showX;
					pressY =showY;
					repaint();
					return;
				}
			}else{
				if(mousState !=CORNER_DEFAULT){
					if(mousState ==CORNER_CENTER_MIDDLE){
						setIsEdit(true);
						framepoint.movePoint(showX -pressX, showY-pressY);
						framedraw.moveClipPic(showX -pressX, showY-pressY);
						pressX =showX;
						pressY =showY;
					}
					else {
						if(framedraw.selectLenght() > 1)
							framedraw.returnSelect(pressX, pressY, false);
						if(mousState ==CORNER_LEFT_MIDDLE){
							//坐标不变，等比缩放图片
							framedraw.changeClipPic(pressX -showX, 0);
						}else if(mousState ==CORNER_CENTER_TOP){
							framedraw.changeClipPic(pressY -showY, 1);
						}else if(mousState ==CORNER_CENTER_BOTTOM){
							framedraw.changeClipPic(showY - pressY, 1);
						}else if(mousState ==CORNER_RIGHT_MIDDLE){
							framedraw.changeClipPic(showX -pressX, 0);
						}
						else { 
							if(e.isControlDown()) {
								framedraw.computeJiao(showX, showY, pressX, pressY);
								pressX =showX;
								pressY =showY;
							}
						}
					}
					updateSelectInfo();
					repaint();
					return;
				}
			}
			
			//drag select
			isDrag = true;
			mousIsSelect =true;
			drag_r = e.getX();
			drag_b = e.getY();

			if(drag_l > drag_r){
				drag_x = drag_r;
				drag_w = drag_l-drag_r;
			}else{
				drag_x = drag_l;
				drag_w = drag_r-drag_l;
			}
			if(drag_t > drag_b){
				drag_y = drag_b;
				drag_h = drag_t-drag_b;
			}else{
				drag_y = drag_t;
				drag_h = drag_b-drag_t;
			}
			
			int x = (int)(drag_x/scaleNum-drawOffSetX);
			int y = (int)(drag_y/scaleNum-drawOffSetY);
			int w = (int)(drag_w/scaleNum);
			int h = (int)(drag_h/scaleNum);
			if(MainFrame.isCollisionBox)
				framerect.inRect(x, y, w, h);
			else{
				framedraw.inRect(x, y, w, h);
				framepoint.inRect(x, y, w, h);
			}
			repaint();
		}
		
		public void mouseMoved(MouseEvent e) {
			mouseX =(int) (e.getX()/scaleNum -drawOffSetX);
			mouseY =(int) (e.getY()/scaleNum -drawOffSetY);
			
			mouseLabel.setText(" X:"+mouseX+", Y:"+mouseY+"   ");
			if(mouseinfo.getPainter() ==null){
				changeMouseState(mouseX, mouseY);
			}else{
				repaint();
			}
		}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		
		public void mousePressed(MouseEvent e) {
			//鼠标左键
//			if(!bt.isRequestFocusEnabled()){
				requestFocus();//获取焦点
//			}
			drag_l = e.getX();
			drag_t = e.getY();
			pressX =(int) (e.getX()/scaleNum -drawOffSetX);
			pressY =(int) (e.getY()/scaleNum -drawOffSetY);
			if(e.getButton() ==MouseEvent.BUTTON1){
				//左键上面有图片
				if(mouseinfo.getPainter() !=null){
					//只有为切块时才添加
					/*if(framedraw ==null){
						framedraw =MainFrame.self.animPanel.setFrameDraw();
					}*/
					if(mouseinfo.getPainter().getGroupID() <0){
						if(mouseinfo.getPainter().getGroupID()>-999){
							framedraw.addOneToFrame(mouseinfo.getPainter(), new int[]{pressX, pressY});
						}else if(mouseinfo.getPainter().getGroupID() ==-999){
							if(MainFrame.isCollisionBox){
								int rectId =framerect.getArr().size();
								framerect.setState(false);
								
								RectPainter rp =(RectPainter)mouseinfo.getPainter();
								lr =new LogicRect(rectId, rp.getType(), pressX, pressY, 1, 1);
								lr.setSelected(true);
								
								framerect.addOneToArr(lr);
								mouseinfo.resetAll();
								firstDrawRect =true;
							}
						}else if(mouseinfo.getPainter().getGroupID() ==-1000){
							int pId =framepoint.getArr().size();
							framepoint.setState(false);
							
							SIPainter rp =(SIPainter)mouseinfo.getPainter();
							int type = Math.abs(rp.getID())-1;
							int index = framepoint.GetIndex(type);
							if(index >= 0){
								LogicPoint lp=framepoint.GetPoint(index);
								lp.x = pressX;
								lp.y = pressY;
								lp.setSelected(true);
							}else{
								LogicPoint lp =new LogicPoint(pId, type, pressX, pressY);
								lp.setSelected(true);
								framepoint.addOneToArr(lp);
							}
							mousIsSelect = true;
							mouseinfo.resetAll();
						}
					}
				}else{
					if(MainFrame.isCollisionBox){
						framerect.chooseRects(pressX, pressY, e.isControlDown());
						changeMouseState(mouseX, mouseY);
					}else{
						if(mousState == CORNER_DEFAULT){//mousState !=CORNER_LEFT_TOP&&mousState !=CORNER_LEFT_BOTTOM&&mousState !=CORNER_RIGHT_TOP&&mousState !=CORNER_RIGHT_BOTTOM){
							if(mousState !=CORNER_CENTER_MIDDLE){
								mousIsSelect = false;
								if(framepoint.returnSelect(pressX, pressY, e.isControlDown(), true)){
									mousIsSelect =true;
								}
								
								if(!mousIsSelect && framedraw.returnSelect(pressX, pressY, e.isControlDown())){
									mousIsSelect =true;
								}
							}else{
								if(framepoint.returnSelect(pressX, pressY, e.isControlDown(), false)){
									mousIsSelect =true;
								}
							}
						}else{
							mousIsSelect = false;
							if(framepoint.returnSelect(pressX, pressY, e.isControlDown(), true)){
								mousIsSelect =true;
							}
							
							if(framedraw.selectLenght() > 0)
								mousIsSelect = true;
							
							if(!mousIsSelect && framedraw.returnSelect(pressX, pressY, false)){
								mousIsSelect =true;
							}
						}
					}
				}
				
				changeMouseState(mouseX, mouseY);
				updateSelectInfo();
				repaint();
			}else if(e.getButton() ==MouseEvent.BUTTON3){
				if(mousIsSelect){
					if(framedraw.isShowPP(pressX, pressY)){
						if(framedraw.selectLenght() > 1)
							framedraw.returnSelect(pressX, pressY, false);
						popMenu.show(this, e.getX(), e.getY());
					}else{
						framedraw.allSelectObj(false);
						mouseinfo.resetAll();
					}
				}else{
					mouseinfo.resetAll();
				}
				repaint();
			}
		}
		
		public void updateSelectInfo(){
			if(MainFrame.isCollisionBox){
				if(framerect != null) {
					String s = framerect.GetInfo();
					selectLabel.setText(s);
				}
			}else{
				String s = "";
				if(framepoint != null)
					s = s+framepoint.GetInfo();
				if(s.equals("") && framedraw != null){
					s = s+framedraw.GetInfo();
				}
				selectLabel.setText(s);
			}
		}
		
		public void mouseReleased(MouseEvent e) {
			isDrag = false;
			firstDrawRect =false;
			framedraw.moveClipPic(0, 0);
			framedraw.computeJiao(0, 0, 0, 0);
			if(mousState >=CORNER_LEFT_TOP){
				if(mousState ==CORNER_LEFT_MIDDLE||mousState ==CORNER_CENTER_TOP||mousState ==CORNER_CENTER_BOTTOM||mousState ==CORNER_RIGHT_MIDDLE){
					//坐标不变，等比缩放图片
					framedraw.changePainterImg();
				}
			}
			updateSelectInfo();
			repaint();
			
		}
		private LogicRect[] addOneRectToArr(LogicRect[] lrarr, LogicRect lr){
			if(lr ==null)return lrarr;
			if(lrarr ==null){
				lrarr =new LogicRect[1];
			}else {
				LogicRect[] tempArr =new LogicRect[lrarr.length +1];
				System.arraycopy(lrarr, 0, tempArr, 0, lrarr.length);
				lrarr =tempArr;
			}
			lrarr[lrarr.length -1] =lr;
			return lrarr;
		}

		private void moveRect(int offx, int offy){			
			framerect.moveRect(offx, offy);
			repaint();
		}
		public void keyPressed(KeyEvent e) {
			if(MainFrame.isCollisionBox){
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					moveRect(0, -1);
					break;
				case KeyEvent.VK_DOWN:
					moveRect(0, 1);
					break;
				case KeyEvent.VK_LEFT:
					moveRect(-1, 0);
					break;
				case KeyEvent.VK_RIGHT:
					moveRect(1, 0);
					break;
				case KeyEvent.VK_DELETE:
					todoDel();
					break;
				case KeyEvent.VK_A:
					if(e.isControlDown()){
						mousIsSelect =true;
						framerect.setState(true);
						repaint();
					}
					break;
				case KeyEvent.VK_C:
					if(e.isControlDown()){
						framerect.doCopy();
						MainFrame.self.getAnimPanel().setCopy();
					}
					break;
				case KeyEvent.VK_V:
					if(e.isControlDown()){
						Frame frame = MainFrame.self.getAnimPanel().getCopyFrame();
						if(frame != null){
							LogicRect lr = frame.getFrameRect().getCopyFirst();
							if(lr != null){
								int dx = mouseX-lr.left;
								int dy = mouseY-lr.top;
								framerect.doPaste(frame.getFrameRect(), dx, dy);
								updateSelectInfo();
								repaint();
							}
						}						
					}
					break;
				default:
					break;
				}
			}else{
				if(mousIsSelect){
					switch (e.getKeyCode()) {
						case KeyEvent.VK_UP:
							if(e.isControlDown()){
								framedraw.todoMoveUpOrDown(true);
								updateSelectInfo();
							}else{
								setIsEdit(true);
								framedraw.picMove(0);
								framepoint.movePoint(0, -1);
							}
							repaint();
							break;
						case KeyEvent.VK_DOWN:
							if(e.isControlDown()){
								framedraw.todoMoveUpOrDown(false);
								updateSelectInfo();
							}else{
								setIsEdit(true);
								framedraw.picMove(1);
								framepoint.movePoint(0, 1);
							}
							repaint();
							break;
						case KeyEvent.VK_LEFT:
							setIsEdit(true);
							framedraw.picMove(2);
							framepoint.movePoint(-1, 0);
							repaint();
							break;
						case KeyEvent.VK_RIGHT:
							setIsEdit(true);
							framedraw.picMove(3);
							framepoint.movePoint(1, 0);
							repaint();
							break;
						case KeyEvent.VK_DELETE:
							todoDel();
							break;
						case KeyEvent.VK_C:
							if(e.isControlDown()){
								framedraw.todoCopy();
								framepoint.todoCopy();
								MainFrame.self.getAnimPanel().setCopy();
							}
							break;
						default:
							break;
					}
				}
				
				switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					if(e.isControlDown()){
						mousIsSelect =true;
						framedraw.allSelectObj(true);
						repaint();
					}
					break;
				case KeyEvent.VK_V:
					if(e.isControlDown()){
						Frame frame = MainFrame.self.getAnimPanel().getCopyFrame();
						if(frame != null){
							ClipPic cp = frame.getFrameDraw().getCopyFirst();
							if(cp != null){
								int dx = mouseX-cp.posX;
								int dy = mouseY-cp.posY;
								framedraw.todoPaste(frame.getFrameDraw(), dx, dy);
								framepoint.todoPaste(frame.getFramePoint(), dx, dy);
								updateSelectInfo();
								repaint();
							}
						}
					}
					break;
				case KeyEvent.VK_Z:
					if(e.isControlDown()) {
						if(framedraw != null) {
							framedraw.undoManager.undo();
							updateSelectInfo();
							repaint();
						}
					}
					break;
				case KeyEvent.VK_Y:
					if(e.isControlDown()) {
						if(framedraw != null) {
							framedraw.undoManager.redo();
							updateSelectInfo();
							repaint();
						}
					}
					break;
				default:
					break;
				}
			}
		}

		public void keyReleased(KeyEvent e) {
			framedraw.picMove(4);
		}
		public void keyTyped(KeyEvent e) {}

		public void actionPerformed(ActionEvent e) {
			if(e.getSource() ==delItem){
				todoDel();
			}else if(e.getSource() ==moveUp){
				//切块在arr中的位置向后移一位
				framedraw.todoMoveUpOrDown(true);
				updateSelectInfo();
				repaint();
			}else if(e.getSource() ==movedown){
				framedraw.todoMoveUpOrDown(false);
				updateSelectInfo();
				repaint();
			}else if(e.getSource() ==copy){
		//		framedraw.todoCopy();
			}else if(e.getSource() ==paste){
		//		framedraw.todoPaste(mouseX, mouseY);
		//		repaint();
			}else if(e.getSource() ==levelFilp){
				framedraw.todoFlip(1);
				repaint();
			}else if(e.getSource() ==verticalFilp){
				framedraw.todoFlip(2);
				repaint();
			}else if(e.getSource() == alphaMenu) {
				if(framedraw.selectLenght() == 1) {
					int oldAlpha = framedraw.getAlpha();
					ClipPic cp = framedraw.getSelect();
					ClipPropsDialog sp = new ClipPropsDialog(MainFrame.self);
					sp.setValue((int)((float)oldAlpha/255*100));
					sp.setClipPic(cp);
					sp.getSliper().addChangeListener(new ChangeListener() {
						
						public void stateChanged(ChangeEvent e) {
							framedraw.todoAlpha(((JSlider)e.getSource()).getValue()*255/100);
							updateSelectInfo();
							repaint();
						}
					});
					sp.show();
					if (sp.getCloseType() == OKCancelDialog.OK_PERFORMED) {
						framedraw.undoManager.addUndoSpriteAlpha(new ClipPic[]{cp}, cp.alpha-oldAlpha);
					}
					else if(sp.getCloseType() == OKCancelDialog.CANCEL_PERFORMED) {
						framedraw.todoAlpha(oldAlpha);
					}
					updateSelectInfo();
					repaint();
				}
			}
		}
		
		public void todoDel(){
			int n = JOptionPane.showConfirmDialog(null,"是否删除选中的对象?","警告",JOptionPane.YES_NO_OPTION);
			if(n==0){
				//确认删除
				if(MainFrame.isCollisionBox){
					framerect.removeObj();
				}else{
					mousIsSelect =false;
					framedraw.removeObj();
					framepoint.removeObj();
				}
				repaint();
			}else if(n ==1){
				//取消
			}
		}
		
		private void changeMouseState(int x, int y){
			if(MainFrame.isCollisionBox){
				int setNum =framerect.mouseState(x, y);
				mousState =setNum+1;
				if(setNum >=0){
					setCursor(Cursor.getPredefinedCursor(RectShape.CORNER_CURSORS[setNum+1]));
				}else{
					setCursor(Cursor.getDefaultCursor());
				}
			}else{
				if(framepoint ==null)return;
				int tempNum =framepoint.mouseState(x, y);
				if(tempNum >=0){
					mousState = CORNER_CENTER_MIDDLE;
				//	setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					return;
				}
				
				if(framedraw ==null)return;
				tempNum =framedraw.mousChange(x, y);
				mousState =tempNum+1;
				if(tempNum >=0){
					setCursor(Cursor.getPredefinedCursor(RectShape.CORNER_CURSORS[tempNum+1]));
				}else{
					setCursor(Cursor.getDefaultCursor());
				}
			}
		}
	}
	JButton jbutton1, jbutton2;
	private JLabel mouseLabel, selectLabel;
	private JCheckBox jChkBox1;//选择检查框
	private JLabel jlabDel;
	class ShowPanel extends JPanel{
		public ShowPanel(){
			setBorder(BorderFactory.createEtchedBorder());
			setLayout(new BoxLayout(this, 2));
			jbutton1 =new JButton(new ImageIcon("icon/largen.png"));
			jbutton1.setEnabled(true);
			jbutton1.setBorderPainted(false);
			jbutton1.setMargin(new Insets(0, 0, 0, 0));
			jbutton1.setFocusPainted(false);
			jbutton1.setContentAreaFilled(true);
			jbutton1.setToolTipText("放大");
			jbutton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	scaleNum +=0.5;
                	jbutton2.setEnabled(true);
                	showjpanel.repaint();
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
                	if(scaleNum >0.5){
                		scaleNum -=0.5;
                		if(scaleNum <=1.0){
                			scaleNum =1.0;
                			jbutton2.setEnabled(false);
                		}
                		showjpanel.repaint();
                	}
                }
            });
			
//			jChkBox1 =new JCheckBox();
//	        jChkBox1.setSelected(false);
//	        jChkBox1.setBounds(15, 70, 20, 20);
//	        jChkBox1.addActionListener(new ActionListener() {
				
	//			@Override
//				public void actionPerformed(ActionEvent e) {
//					// TODO Auto-generated method stub
//					if(jChkBox1.isSelected()){
//						jChkBox1.setSelected(true);
//						MainFrame.isCollisionBox =true;
//					}else{
//						jChkBox1.setSelected(false);
//						MainFrame.isCollisionBox =false;
//					}
//					showjpanel.repaint();
//				}
//			});
	        
//	        jlabDel =new JLabel("碰撞框模式");
//	        jlabDel.setBounds(40, 70, 60, 20);
			
			mouseLabel = new JLabel();
			mouseLabel.setHorizontalAlignment(SwingConstants.CENTER);
			mouseLabel.setText(" X:0, Y:0   ");
			
			selectLabel = new JLabel();
			selectLabel.setHorizontalAlignment(SwingConstants.CENTER);
			selectLabel.setText("");
			
//			this.setLayout(new BorderLayout(1,0));
			this.add(jbutton1);
			this.add(jbutton2);
	//		this.add(jChkBox1);
	//		this.add(jlabDel);
			this.add(mouseLabel);
			this.add(selectLabel);
			
//	        add(Box.createRigidArea(new Dimension(4, 4)));
		}
	}
}

class ClipPropsDialog extends OKCancelDialog  {
	
	private JSlider slider;
	private NumberSpinner idSpinner;
	private ClipPic cp;
	
	public ClipPropsDialog(JFrame owner) {
		super(owner);
		init();
	}

	private void init() {
		setTitle("属性");
		setSize(new Dimension(300, 130));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		idSpinner = new NumberSpinner();
		slider = new JSlider();
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0;
		c.insets = new Insets(5, 5, 5, 5);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		centerPanel.add(new JLabel("id："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(idSpinner, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		centerPanel.add(new JLabel("透明度："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(slider, c);
		
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(centerPanel, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void setOrientation(int orientation) {
		slider.setOrientation(orientation);
	}
	
	public void setRange(int min, int max) {
		slider.setMinimum(min);
		slider.setMaximum(max);
	}
	
	public void setValue(int v) {
		slider.setValue(v);
	}
	
	public JSlider getSliper() {
		return slider;
	}
	
	public int getValue() {
		return slider.getValue();
	}
	
	public void setClipPic(ClipPic cp) {
		this.cp = cp;
		
		if(cp != null) {
			idSpinner.setIntValue(cp.getLogicId());
		}
	}

	@Override
	protected void cancelPerformed() {
		dispose();
	}

	@Override
	protected void okPerformed() {
		if(cp != null) {
			cp.setLogicId(idSpinner.getIntValue());
		}
		
		closeType = OK_PERFORMED;
		dispose();
	}
	
}


