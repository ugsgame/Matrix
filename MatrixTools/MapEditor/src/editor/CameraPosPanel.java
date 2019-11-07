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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class CameraPosPanel extends JPanel {

	private JDialog owner;
	private IntPair pos;
	private NumberSpinner xSpinner;
	private NumberSpinner ySpinner;

	public CameraPosPanel(JDialog owner, String xName, String yName) {
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

		c.gridx = 0;
		add(new JLabel(xName), c);
		c.gridx++;
		add(xSpinner, c);
		if (yName != null) {
			c.gridx++;
			add(new JLabel(yName), c);
			c.gridx++;
			add(ySpinner, c);
		}
		c.gridx++;
		add(setBT, c);
		c.gridx++;
		c.weightx = 1;
		add(new JPanel(), c);
	}

	private void setPos() {
		pos.x = xSpinner.getIntValue();
		pos.y = ySpinner.getIntValue();
		CameraPosSetter setter = new CameraPosSetter(owner);
		setter.setPos(pos);
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
}

class CameraPosMapPanel extends MapPanel {

	private MapPanel.ManagerPanel panel;
	private MapPanel.Manager manager;
	private IntPair cameraLT;
	private boolean canDrag;
	private int mouseX;
	private int mouseY;
	private int oldMouseX;
	private int oldMouseY;

	public CameraPosMapPanel(JDialog owner) {
		super(owner);
		manager = getManager();
		cameraLT = new IntPair();

		panel = getPanel();

		canDrag = false;

		panel.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				if (panel.isMouseMovingViewport()) return;
				selfMousePressed(e);
			}

			public void mouseReleased(MouseEvent e) {
				canDrag = false;
			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseMoved(MouseEvent e) {
				if (panel.isMouseMovingViewport() || canDrag) return;
				selfMouseMoved(e);
			}

			public void mouseDragged(MouseEvent e) {
				if (panel.isMouseMovingViewport() || !canDrag) return;
				selfMouseDragged(e);
			}
		});

		panel.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (panel.isMouseMovingViewport()) return;
				selfKeyPressed(e);
			}
		});
	}

	private void getMousePos(MouseEvent e) {
		MapInfo info = MainFrame.self.getMapInfo();
		mouseX = manager.getMouseX() - info.getRealLeft();
		mouseY = manager.getMouseY() - info.getRealTop();
	}

	private boolean canDragAtPoint(int x, int y) {
		MapInfo info = MainFrame.self.getMapInfo();
		return (x >= cameraLT.x && x <= cameraLT.x + MainFrame.SCR_W && y >= 0 && y <= info.getRealHeight());
	}

	private void selfMousePressed(MouseEvent e) {
		getMousePos(e);
		canDrag = canDragAtPoint(mouseX, mouseY);
		if (canDrag) {
			oldMouseX = mouseX;
			oldMouseY = mouseY;
		}
	}

	private void selfMouseMoved(MouseEvent e) {
		getMousePos(e);
		panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		if (canDragAtPoint(mouseX, mouseY)) {
			panel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}
	}

	private void selfMouseDragged(MouseEvent e) {
		getMousePos(e);
		cameraLT.x += (mouseX - oldMouseX);
		oldMouseX = mouseX;
		cameraLT.y += (mouseY - oldMouseY);
		oldMouseY = mouseY;
		panel.repaint();
	}

	private void selfKeyPressed(KeyEvent e) {
		int offsetX = 0;
		int offsetY = 0;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			offsetX = -1;
			break;
		case KeyEvent.VK_RIGHT:
			offsetX = 1;
			break;
		case KeyEvent.VK_UP:
			offsetY = -1;
			break;
		case KeyEvent.VK_DOWN:
			offsetY = 1;
			break;
		}
		if (offsetX != 0 || offsetY != 0) {
			cameraLT.x += offsetX;
			cameraLT.y += offsetY;
			panel.repaint();
		}
	}

	public void scrollToCamera() {
		MapInfo info = MainFrame.self.getMapInfo();
		panel.scrollRectToVisible(new Rectangle(cameraLT.x + info.getRealLeft() - 25, info.getRealTop(),
		        MainFrame.SCR_W + 100, info.getRealHeight() + 50));
	}

	public IntPair getPos() {
		return cameraLT;
	}

	public void setPosX(int left) {
		cameraLT.x = left;
		panel.repaint();
	}

	public void setPosY(int top) {
		cameraLT.y = top;
		panel.repaint();
	}

	public void setPos(IntPair cameraLT) {
		if (cameraLT == null) {
			cameraLT = new IntPair();
		}
		this.cameraLT.copyFrom(cameraLT);
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
		g.fillRect(cameraLT.x + info.getRealLeft(), info.getRealTop(), MainFrame.SCR_W, info.getRealHeight());
		g.setColor(oldColor);
		g2.setComposite(oldComposite);
	}
};

class CameraPosSetter extends OKCancelDialog {

	private CameraPosMapPanel panel;

	public CameraPosSetter(JDialog owner) {
		super(owner);
		init();
	}

	public CameraPosSetter(JFrame owner) {
		super(owner);
		init();
	}

	private void init() {
		setTitle("设置摄像机位置");
		panel = new CameraPosMapPanel(this);

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

	public IntPair getPos() {
		return panel.getPos();
	}

	public void setPos(IntPair cameraLT) {
		panel.setPos(cameraLT);
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
