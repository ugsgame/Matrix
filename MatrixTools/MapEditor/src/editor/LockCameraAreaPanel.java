package editor;

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
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class LockCameraAreaPanel extends JPanel {
	private JDialog owner;
	private NumberSpinner leftSpinner;
	private NumberSpinner rightSpinner;
	
	public LockCameraAreaPanel(JDialog owner, String leftName, String rightName) {
		this.owner = owner;
		leftSpinner = new NumberSpinner();
		leftSpinner.setPreferredSize(new Dimension(100, 10));
		rightSpinner = new NumberSpinner();
		rightSpinner.setPreferredSize(new Dimension(100, 10));
		JButton setBT = new JButton("设置");
		setBT.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setArea();
			}
		});
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 5, 3, 5);
		c.weighty = 1;
		c.weightx = 0;
		
		c.gridx = 0;
		add(new JLabel(leftName), c);
		c.gridx++;
		add(leftSpinner, c);
		c.gridx++;
		add(new JLabel(rightName), c);
		c.gridx++;
		add(rightSpinner, c);
		c.gridx++;
		add(setBT, c);
		c.gridx++;
		c.weightx = 1;
		add(new JPanel(), c);
	}

	private void setArea() {
		LockCameraAreaSetter setter = new LockCameraAreaSetter(owner);
		setter.setAreaLeft(leftSpinner.getIntValue());
		setter.setAreaRight(rightSpinner.getIntValue());
		setter.show();
		if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			leftSpinner.setIntValue(setter.getAreaLeft());
			rightSpinner.setIntValue(setter.getAreaRight());
		}
	}
	
	public void setArea(int left, int right) {
		leftSpinner.setIntValue(left);
		rightSpinner.setIntValue(right);
	}
	
	public int getAreaLeft() {
		return leftSpinner.getIntValue();
	}
	
	public int getAreaRight() {
		return rightSpinner.getIntValue();
	}
}

class LockCameraAreaMapPanel extends MapPanel {
	public final static int DRAG_NONE = 0;
	public final static int DRAG_LEFT = 1;
	public final static int DRAG_CENTER = 2;
	public final static int DRAG_RIGHT = 3;
	
	private final static int DRAG_SPACE = 5;
	
	private MapPanel.ManagerPanel panel;
	private MapPanel.Manager manager;
	private int left;
	private int right;
	private int drag;
	private int mouseX;
	private int mouseY;
	private int oldMouseX;
	private int oldMouseY;
	
	public LockCameraAreaMapPanel(JDialog owner) {
		super(owner);
		manager = getManager();
		MapInfo info = MainFrame.self.getMapInfo();
		left = info.getRealLeft();
		right = left + MainFrame.SCR_W;
		drag = DRAG_NONE;
		
		panel = getPanel();
		
		panel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(panel.isMouseMovingViewport()) return;
				selfMousePressed(e);
			}
			
			public void mouseReleased(MouseEvent e) {
				drag = DRAG_NONE;
			}
		});
		
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				if(panel.isMouseMovingViewport() || drag != DRAG_NONE) return;
				selfMouseMoved(e);
			}
			
			public void mouseDragged(MouseEvent e) {
				if(panel.isMouseMovingViewport() || drag == DRAG_NONE) return;
				selfMouseDragged(e);
			}
		});
	}
	
	private void getMousePos(MouseEvent e) {
		MapInfo info = MainFrame.self.getMapInfo();
		mouseX = manager.getMouseX() - info.getRealLeft();
		mouseY = manager.getMouseY() - info.getRealTop();
	}
	
	private int getDragAtPoint(int x, int y) {
		int result = DRAG_NONE;
		MapInfo info = MainFrame.self.getMapInfo();
		drag = DRAG_NONE;
		if(y >= 0 && y <= info.getRealHeight()) {
			if(x >= left - DRAG_SPACE && x <= left + DRAG_SPACE) {
				result = DRAG_LEFT;
			}
			else if(x >= right - DRAG_SPACE && x <= right + DRAG_SPACE) {
				result = DRAG_RIGHT;
			}
			else {
				int centerX = (left + right) / 2;
				int centerY = (info.getRealHeight()) / 2;
				if(x >= centerX - 10 && x <= centerX + 10 && y >= centerY - 10 && y <= centerY + 10) {
					result = DRAG_CENTER;
				}
			}
		}
		return result;
	}
	
	private void selfMousePressed(MouseEvent e) {
		getMousePos(e);
		drag = getDragAtPoint(mouseX, mouseY);
		if(drag != DRAG_NONE) {
			oldMouseX = mouseX;
			oldMouseY = mouseY;
		}
	}
	
	private void selfMouseMoved(MouseEvent e) {
		getMousePos(e);
		panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		switch(getDragAtPoint(mouseX, mouseY)) {
		case DRAG_LEFT:
			panel.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
			break;
		case DRAG_RIGHT:
			panel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			break;
		case DRAG_CENTER:
			panel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));			
			break;
		}
	}
	
	private void selfMouseDragged(MouseEvent e) {
		getMousePos(e);
		int offsetX = (mouseX - oldMouseX);	oldMouseX = mouseX;
		int offsetY = (mouseY - oldMouseY);	oldMouseY = mouseY;
		if(offsetY > 100000) return;
		switch(drag) {
		case DRAG_LEFT:
			left += offsetX;
			if(right - left < MainFrame.SCR_W) right = left + MainFrame.SCR_W;
			break;
		case DRAG_RIGHT:
			right += offsetX;
			if(right - left < MainFrame.SCR_W) left = right - MainFrame.SCR_W;
			break;
		case DRAG_CENTER:
			left += offsetX;
			right += offsetX;
			break;
		}
		panel.repaint();
	}
	
	public void scrollToCamera() {
		MapInfo info = MainFrame.self.getMapInfo();
		panel.scrollRectToVisible(new Rectangle(left + info.getRealLeft() - 25, info.getRealTop(),
		        right - left + 100, info.getRealHeight() + 50));
	}
	
	public int getAreaLeft() {
		return left;
	}
	
	public int getAreaRight() {
		return right;
	}
	
	public void setAreaLeft(int left) {
		this.left = left;
		panel.repaint();
	}
	
	public void setAreaRight(int right) {
		this.right = right;
		panel.repaint();
	}

	public void paintManager(Graphics g) {
		manager.paintFloors(g);
		manager.paintEnemys(g);
		manager.paintLines(g);
		Graphics2D g2 = (Graphics2D) g;
		Composite oldComposite = g2.getComposite();
		g2.setComposite(ScrollablePanel.ALPHA_COMPOSITE);
		Color oldColor = g.getColor();
		g.setColor(Color.BLUE);
		MapInfo info = MainFrame.self.getMapInfo();
		g.fillRect(left + info.getRealLeft(), info.getRealTop(), right - left, info.getRealHeight());
		//g.setColor(Color.RED);
		//g.fillRect(left + info.getRealLeft() - DRAG_SPACE, info.getRealTop(), DRAG_SPACE * 2, info.getRealHeight());
		//g.fillRect(right + info.getRealLeft() - DRAG_SPACE, info.getRealTop(), DRAG_SPACE * 2, info.getRealHeight());
		int centerX = info.getRealLeft() + (left + right) / 2;
		int centerY = info.getRealTop() + info.getRealHeight() / 2;
		g.setColor(Color.RED);
		g.fillOval(centerX - 10, centerY - 10, 20, 20);
		g.setColor(oldColor);
		g2.setComposite(oldComposite);
    }
};

class LockCameraAreaSetter extends OKCancelDialog {
	private LockCameraAreaMapPanel panel;
	
	public LockCameraAreaSetter(JDialog owner) {
		super(owner);
		init();
	}
	
	public LockCameraAreaSetter(JFrame owner) {
		super(owner);
		init();
	}
	
	private void init() {
		setTitle("设置地图区域");
		panel = new LockCameraAreaMapPanel(this);
		
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				panel.scrollToCamera();
			}
		});
		
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(panel, BorderLayout.CENTER);
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public int getAreaLeft() {
		return panel.getAreaLeft();
	}
	
	public void setAreaLeft(int left) {
		panel.setAreaLeft(left);
	}
	
	public int getAreaRight() {
		return panel.getAreaRight();
	}
	
	public void setAreaRight(int right) {
		panel.setAreaRight(right);
	}
	
	protected void cancelPerformed() {
		closeType = CANCEL_PERFORMED;
		dispose();
	}
	
	protected void okPerformed() {
		closeType = OK_PERFORMED;
		dispose();
	}
}














