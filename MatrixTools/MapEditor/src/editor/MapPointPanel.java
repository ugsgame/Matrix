package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
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
import javax.swing.JLabel;
import javax.swing.JPanel;

class MapPointPanel extends JPanel {

	private JDialog owner;
	private IntPair pos;
	private NumberSpinner xSpinner;
	private NumberSpinner ySpinner;
	private boolean xLocked;
	private boolean yLocked;
	private int lockedX;
	private int lockedY;

	public MapPointPanel(JDialog owner, String xName, String yName) {
		this.owner = owner;
		pos = new IntPair();
		xSpinner = new NumberSpinner();
		xSpinner.setPreferredSize(new Dimension(100, 10));
		ySpinner = new NumberSpinner();
		ySpinner.setPreferredSize(new Dimension(100, 10));
		JButton setBT = new JButton("设置");
		setBT.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setPos();
			}
		});

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 5, 3, 5);
		c.weighty = 1;
		c.weightx = 0;

		c.gridx = -1;
		if(xName != null) {
			c.gridx++;
			add(new JLabel(xName), c);
			c.gridx++;
			c.weightx = 1;
			add(xSpinner, c);
			c.weightx = 0;
		}
		if (yName != null) {
			c.gridx++;
			add(new JLabel(yName), c);			
			c.gridx++;
			c.weightx = 1;
			add(ySpinner, c);
			c.weightx = 0;
		}
		c.gridx++;
		add(setBT, c);
	}

	private void setPos() {
		pos.x = xSpinner.getIntValue();
		pos.y = ySpinner.getIntValue();
		MapPointSetter setter = new MapPointSetter(owner);
		setter.setPos(pos);
		if(xLocked) setter.lockX(lockedX);
		if(yLocked) setter.lockY(lockedY);
		setter.show();
		if (setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			pos.copyFrom(setter.getPos());
			xSpinner.setIntValue(pos.x);
			ySpinner.setIntValue(pos.y);
		}
	}

	public void setPos(int x, int y) {
		xSpinner.setIntValue(x);
		ySpinner.setIntValue(y);
	}

	public int getPosX() {
		return xSpinner.getIntValue();
	}

	public int getPosY() {
		return ySpinner.getIntValue();
	}
	
	public void lockX(int lockedX) {
		this.xLocked = true;
		this.lockedX = lockedX;
	}
	
	public void lockY(int lockedY) {
		this.yLocked = true;
		this.lockedY = lockedY;
	}
}

class MapPointSetter extends OKCancelDialog {

	private MapPanel.ManagerPanel panel;
	private MapPanel.Manager manager;
	private IntPair pos;
	private boolean xLocked;
	private boolean yLocked;
	private int lockedX;
	private int lockedY;

	public MapPointSetter(JDialog owner) {
		super(owner);
		setTitle("设置摄像机位置");
		
		MapPanel mapPanel = new MapPanel(this) {
			public void paintManager(Graphics g) {
				selfPaintManager(g);
			}
		};
		pos = new IntPair();
		xLocked = false;
		yLocked = false;
		manager = mapPanel.getManager();

		panel = mapPanel.getPanel();

		panel.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				if (panel.isMouseMovingViewport()) return;
				selfMousePressed(e);
			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (panel.isMouseMovingViewport()) return;
				selfMouseDragged(e);
			}
		});

		addComponentListener(new ComponentAdapter() {

			public void componentShown(ComponentEvent e) {
				scrollToPoint();
			}
		});

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(mapPanel, BorderLayout.CENTER);
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void changePos(MouseEvent e) {
		MapInfo info = MainFrame.self.getMapInfo();
		pos.x = manager.getMouseX() - info.getRealLeft();
		pos.y = manager.getMouseY() - info.getRealTop();
	}

	private void selfMousePressed(MouseEvent e) {
		changePos(e);
		panel.repaint();
	}

	private void selfMouseDragged(MouseEvent e) {
		changePos(e);
		panel.repaint();
	}

	public void scrollToPoint() {
		MapInfo info = MainFrame.self.getMapInfo();
		panel.scrollRectToVisible(new Rectangle(pos.x + info.getRealLeft() - 100, pos.y + info.getRealTop() - 100, 200, 200));
	}

	public IntPair getPos() {
		return pos;
	}

	public void setPosX(int left) {
		pos.x = left;
		panel.repaint();
	}

	public void setPosY(int top) {
		pos.y = top;
		panel.repaint();
	}

	public void setPos(IntPair pos) {
		if (pos == null) {
			pos = new IntPair();
		}
		this.pos.copyFrom(pos);
		panel.repaint();
	}
	
	public void lockX(int lockedX) {
		this.xLocked = true;
		this.lockedX = lockedX;
	}
	
	public void lockY(int lockedY) {
		this.yLocked = true;
		this.lockedY = lockedY;
	}

	private void selfPaintManager(Graphics g) {
		manager.paintFloors(g);
		manager.paintEnemys(g);
		manager.paintLines(g);
		Graphics2D g2 = (Graphics2D) g;
		Composite oldComposite = g2.getComposite();
		g2.setComposite(ScrollablePanel.ALPHA_COMPOSITE);
		Color oldColor = g.getColor();
		g.setColor(Color.BLUE);
		MapInfo info = MainFrame.self.getMapInfo();
		int x = (xLocked ? lockedX : (pos.x + info.getRealLeft()));
		int y = (yLocked ? lockedY : (pos.y + info.getRealTop()));
		g.fillOval(x - 5, y - 5, 11, 11);
		g.setColor(Color.RED);
		g.fillOval(x - 1, y - 1, 3, 3);
		g.setColor(oldColor);
		g2.setComposite(oldComposite);
	}

	protected void cancelPerformed() {
		closeType = CANCEL_PERFORMED;
		dispose();
	}

	protected void okPerformed() {
		closeType = OK_PERFORMED;
		dispose();
	}
};
